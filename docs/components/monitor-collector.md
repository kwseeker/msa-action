# 监控系统-数据采集器

## 数据采集器的主要功能

+ **数据采集**

  常见的监控指标包括：

  + 系统资源占用

    + CPU
    + 内存
    + 磁盘
    + 带宽

  + 业务日志

    + 业务埋点

      常用于业务数据统计，用来研究用户习惯、查看营销收入等等。

    + 异常调用链路

  + 性能指标

    + TPS
    + QPS
    + 接口响应时间

  + JVM监控

    + Java 进程粒度的系统资源占用
    + GC 垃圾回收时间
    + JVM 内线程状态
    + ...

  分析上面各种指标：

  **系统资源占用**可以借助Java API `OperatingSystemMXBean` 或一些第三方的库进行收集；

  **业务日志**需要借助自定义日志 Appender 进行收集；

  **JVM监控**可以借助 Java API ManagementFactory  进行收集。

+ **数据上报**

  数据采集器一般是集成到业务服务中，一般需要通过某种通信方式将收集的数据传输到数据中心处理。

  数据传输的方式可以选择：

  + 消息队列
  + Redis 发布与订阅
  + Guava 消息总线
  + RPC
  + Netty

数据采集器并不一定需要自己从零开始设计，可以参考现有的监控系统的数据采集器实现，生产环境还是直接用成熟的技术方案。

## 数据采集器实现

### Prometheus micrometer-registry-prometheus

 Micrometer 实现的 Prometheus 数据采集器，引入下面两个依赖后，可以看到 `/actuator`下会多出一个 `/actuator/prometheus`。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Prometheus 数据中心配置采集任务借助  simpleclient  通过这个接口主动拉取监控数据。

另外 Prometheus 也支持数据采集器通过 [PushGateway](https://prometheus.io/docs/practices/pushing/) 主动推送监控数据到数据中心。

```yaml
scrape_configs:
  # 配置一个采集任务，综合下面的配置信息意思是抓取 msa-service-user:28100/user/actuator/prometheus 接口信息
  - job_name: 'msa-service-user'
    # 监控的目标请求路径，默认是 '/metrics', scheme 默认是 'http'
    metrics_path: '/user/actuator/prometheus'
    static_configs:
      - targets:  # 抓取目标
          - '172.24.0.1:28100'
        labels:
          app: 'msa-service-user'
```

采集的数据包括应用基本信息、应用中线程池信息、HTTP Server 请求统计信息、JVM信息、系统信息等（根据应用启动的组件会调整监控指标）。

Micrometer 内置支持对很多框架的监控，参考 [Reference Instrumentations](https://docs.micrometer.io/micrometer/reference/reference.html)。

### 自定义Prometheus数据采集器

搜索资料有两种方式：

+ **定制`micrometer-registry-prometheus` 的 `MeterRegistry`**

  **简单粗糙的做法：**

  由源码看已经注册了 **PrometheusMeterRegistry** Bean，可以写个配置类获取此Bean，创建自定义的监控指标类（可以实现MetricsBinder注册，也可以直接实现指标类型，比如 Counter、Gauge 等），然后直接注册到这个Bean。

  **优雅的做法：**

  仿造 spring-boot-actuator-autoconfigurer 的写法：写自动配置类，使用条件注解根据业务条件选择加载 MetricsBinder Bean 并注册。

+ **使用Prometheus Client Library 实现， 比如 client_java**

  需要在业务服务代码中通过 Prometheus [客户端库](https://prometheus.io/docs/instrumenting/clientlibs/)添加 instrumentation。

  > 这里的 instrumentation 和 Java 的 Instrumentation 接口无关，看源码并没有使用字节码增强。

  官方的部分客户端：

  - [Go](https://github.com/prometheus/client_golang)

  - [Java or Scala](https://github.com/prometheus/client_java)

    [Docs](https://prometheus.github.io/client_java)

  - [Rust](https://github.com/prometheus/client_rust)


分析上面两种方式工作原理：

#### [micrometer-registry-prometheus](https://docs.micrometer.io/micrometer/reference/implementations/prometheus.html)

[Github Project](https://github.com/micrometer-metrics/micrometer/tree/main/implementations/micrometer-registry-prometheus)

SpringBoot 项目中只需要引入 `spring-boot-starter-actuator` 和 `micrometer-registry-prometheus` 就可以完成添加 `/actuator/prometheus` 端点，请求这个端点时就可以根据服务配置的组件抓取对应组件监控指标的数据。。

这里看看它内部是怎么做到的？

+ `/actuator/prometheus` 端点怎么注册的？

  是在 spring-boot-actuator-autoconfigurer PrometheusScrapeEndpoint 中通过 @WebEndpoint 注册的，注册了一个 GET 请求接口。

+ Micrometer 怎么判断服务中有配置哪些组件以及怎么注册对应的数据采集器(MicrometerCollector)的？

  不会主动判断有配置哪些组件，而是由spring-boot-actuator-autoconfigure 配置上所有组件的监控指标类，**通过自动配置类的条件注解判断组件是否存在以及是否加载这些组件的监控指标(采集器)Bean**，然后通过 Spring ObjectProvider 获取这些监控指标类 Bean 实例，然后遍历依次注册到 Micrometer 监控指标注册表（CollectorRegistry）中。

+ 监控数据抓取流程？

  参考 micrometer-registry-prometheus.drawio。

  简述：micrometer-registry-prometheus 是 micrometer 为了适配 prometheus 的上报接口提供的库，实现通过 Micrometer 的内核抓取 Prometheus 格式的指标数据，`/actuator/prometheus` 端点是 spring-boot-starter-actuator 内部依赖项  spring-boot-starter-actuator-autoconfigure `PrometheusMetricsExportAutoConfiguration`引入的。

##### Micrometer 基础

想弄明白 micrometer-registry-prometheus 工作原理还是需要先梳理清 Micrometer 工作原理，发现无法跳过，不然不明不白的。

> Micrometer 意为千分尺。

**Micrometer 的基本概念**：

Micrometer 本身是一个监控指标数据采集器。详细使用方法参考官方源码中的单元测试。

支持多种监控指标类型，参考后文。

其工作原理其实和 Prometheus client_java 类似，也包括下面步骤：

1. 向指标注册表 （MeterRegistry）注册监控指标，每个指标都是一个采集器；

   MeterRegistry是抽象类，是注册表的核心实现，Micrometer 核心中提供了3种注册表实现类：

   + SimpleMeterRegistry

   + CompositeMeterRegistry

     是 MeterRegistry 的集合，同时还继承 MeterRegistry。

   + LoggingMeterRegistry

2. 业务服务通过监控指标记录数据；

   

3. 从监控指标提取统计数据上报。

**Micrometer 监控指标类型**：

+ Counters
  + Counter
  + FunctionCounter
+ Gauges
  + Gauge
  + TimeGauge
  + MultiGauge
+ Timers
  + Timer
  + FunctionTimer
+ DistributionSummary
+ LongTaskTimer
+ Histograms and Percentiles

Spring Boot Metrics 已经支持的监控指标，参考：[Supported Metrics and Meters](https://docs.spring.io/spring-boot/reference/actuator/metrics.html#actuator.metrics.supported)

#### [client_java](https://prometheus.github.io/client_java/)

官方文档中没有解释的一些重要概念：

+ **Exporter**

  即输出者，更准确地说是被封装的通信组件，负责**数据上报**，业务服务收集监控数据需要通过某种通信方式传给数据中心，client_java 中现在支持 **HttpServer**、**Servlet**、**Pushgateway**、**Opentelemetry** 四种方式。

  > **Pushgateway**:  用于临时任务和批处理任务向Prometheus上报它们的指标监控数据，由于这类工作可能存在的时间不够长，无法被抓取，他们需要自行推送指标监控数据到推送网关。。
  >
  > 像 SpringBoot 项目直接使用 Servlet Exporter 上报数据即可，不需要再引入额外的通信组件。
  >
  > **Opentelemetry**： client_java 允许用户使用**OTLP协议**将指标推送到**OpenTelemetry端点**，再传输给 Prometheus 数据中心。

+ **Instrumentation**

  封装了监控指标的组件，负责**数据采集**。

+ **Metric**

  监控指标，还是数据采集器（内部实现了 Collector 接口） ，定义指标类型、指标数据结构、以及采集方法。

  监控指标类型：

  + [Counter](https://prometheus.github.io/client_java/getting-started/metric-types/#counter)

    Counter 是计数器类型，适合单调递增的场景，比如请求的总数、完成的任务总数、出现的错误总数等。它拥有很好的不相关性，不会因为重启而重置为 0。

  + [Gauge](https://prometheus.github.io/client_java/getting-started/metric-types/#gauge)

    Gauge 用来表示可增可减的值（gauge: 计量器），比如 CPU 和内存的使用量、IO 大小等。

  + [Histogram](https://prometheus.github.io/client_java/getting-started/metric-types/#histogram)

    Histogram 是一种**累积直方图**，它通常用来描述监控项的长尾效应。

    比如使用 Hitogram 来分析 API 调用的响应时间，使用数组 [30ms, 100ms, 300ms, 1s, 3s, 5s, 10s] 将响应时间分为 8 个区间。那么每次采集到响应时间，比如 200ms，那么对应的区间 (0, 30ms], (30ms, 100ms], (100ms, 300ms] 的计数都会加 1。最终以响应时间为横坐标，每个区间的计数值为纵坐标，就能得到 API 调用响应时间的累积直方图。

  + [Summary](https://prometheus.github.io/client_java/getting-started/metric-types/#summary)

    Summary 和 Histogram 类似，它记录的是监控项的**分位数**。比如一个 http 请求调用了 100 次，得到 100 个响应时间值。将这 100 个时间响应值按照从小到大的顺序排列，那么 0.9 分位数（90% 位置）就代表着第 90 个数。

  + [Info](https://prometheus.github.io/client_java/getting-started/metric-types/#info)

  + [StateSet](https://prometheus.github.io/client_java/getting-started/metric-types/#stateset)

  + [GaugeHistogram and Unknown](https://prometheus.github.io/client_java/getting-started/metric-types/#gaugehistogram-and-unknown)

  关于指标类型测试参考 `SpringBoot-Labs/prometheus/prometheus-client-01`。

  client_java 按  [OpenMetrics](https://openmetrics.io/) 标准支持其所有指标类型，像 Prometheus 主要支持前面4种。

+ **PrometheusRegistry**

  一系列监控指标的注册表，Instruemntation 组件中的监控指标或自定义的监控指标都要注册到这个单例对象，在数据采集时遍历所有监控指标（也是数据采集器）进行数据采集。

+ **数据格式**
  - OpenMetrics text format
  - Prometheus text format
  - Prometheus protobuf format

和 Skywalking Agent 库感觉有点像，也有一堆根据服务实例中各种组件定制的**数据采集组件**，可以按需引入，不过看上去没有  Skywalking Agent 支持的组件全面（Agent 支持的组件非常多）。

```shell
├── prometheus-metrics-bom
├── prometheus-metrics-config
├── prometheus-metrics-core
├── prometheus-metrics-exporter-common
├── prometheus-metrics-exporter-httpserver		# 为了支持Prometheus数据中心拉取
├── prometheus-metrics-exporter-opentelemetry	
├── prometheus-metrics-exporter-pushgateway		# 为了支持业务服务主动推送监控数据
├── prometheus-metrics-exporter-servlet-jakarta	# Servlet标准的Web服务器监控组件
├── prometheus-metrics-exporter-servlet-javax
├── prometheus-metrics-exposition-formats
├── prometheus-metrics-instrumentation-caffeine
├── prometheus-metrics-instrumentation-dropwizard5
├── prometheus-metrics-instrumentation-guava
├── prometheus-metrics-instrumentation-jvm		# JVM 信息监控组件
├── prometheus-metrics-model
├── prometheus-metrics-shaded-dependencies
├── prometheus-metrics-simpleclient-bridge		# 为了兼容 simpleclient
├── prometheus-metrics-tracer					# 或许还支持链路追踪
```

这里分析 [Quickstart](https://prometheus.github.io/client_java/getting-started/quickstart/) 中的案例的工作原理。

这个案例中引入了 `prometheus-metrics-core` 和两个小组件`prometheus-metrics-instrumentation-jvm` `prometheus-metrics-exporter-httpserver`。第一个小组件用于采集 JVM 信息，第二个小组件用于创建一个 HTTP 服务器为了提供给 Prometheus 调用的接口。

```java
// 配置了两组监控，启动HTTPServer提供了一个监控接口
public static void main(String[] args) throws InterruptedException, IOException {
    // 这个是 prometheus-metrics-instrumentation-jvm 中的类
    JvmMetrics.builder().register();
    
	// 还定义了 Counter 类型的监控指标
    Counter counter = Counter.builder()
        .name("my_count_total")
        .help("example counter")
        .labelNames("status")
        .register();
	// ok标签值设置为2
    counter.labelValues("ok").inc();
    counter.labelValues("ok").inc();
    // error标签值设置为1
    counter.labelValues("error").inc();
    // 通过上面配置写死的监控数据如下
    // # HELP my_count_total example counter
	// # TYPE my_count_total counter
	// my_count_total{status="error"} 1.0
	// my_count_total{status="ok"} 2.0
    
    // 启动 HTTPServe, 监听端口 9400, 默认的监控接口是 /metrics
    HTTPServer server = HTTPServer.builder()
        .port(9400)
        .buildAndStart();
    
    // Prometheus 监控任务配置
    // scrape_configs:
    //  - job_name: "java-example"
    //    static_configs:
    //      - targets: ["localhost:9400"]
    System.out.println("HTTPServer listening on port http://localhost:" + server.getPort() + "/metrics");
    Thread.currentThread().join(); // sleep forever
    
    // 请求 http://localhost:9400/metrics 可以看到 jvm 以及 my_count_total 的统计数据
}
```

## 基于 spring-boot-actuator、 micrometer-registry-prometheus 自定义Http请求状态监控并上报 Prometheus

**目标：**

+ 统计接口访问次数

  spring boot actuator 中已经包含了此统计项，但是仅仅是记录从节点启动到当前时间段内的访问次数，这里我们统计从接口上线到当前时间段所有节点的所有访问次数。

+ 统计接口QPS

  这里统计一段时间内（比如1小时，做成可配置的）接口访问的QPS。  

+ 统计接口一段时间内响应时间的百分位直方图

  比如统计1小时内接口响应时间的 0.5 0.9 0.99 百分位直方图。    

+ 统计接口响应超过设定时间（比如300ms、500ms、1000ms）的次数

**针对上面目标监控指标 Spring Boot Actuator 已经做到了什么地步？**

相关官方文档：

+ [Supported Metrics and Meters](https://docs.spring.io/spring-boot/reference/actuator/metrics.html#actuator.metrics.supported)

  + [Registering Custom Metrics](https://docs.spring.io/spring-boot/reference/actuator/metrics.html#actuator.metrics.registering-custom) 注册自定义监控指标

  + [Customizing Individual Metrics](https://docs.spring.io/spring-boot/reference/actuator/metrics.html#actuator.metrics.customizing) 指对已存在的指控指标进行定制 

    比如重命名 tag 、设置公共 tag、开关对某个指标的监控、拓展监控指标类型等。

从下面可以看到，已经包含的监控指标：接口访问次数、接口所有访问总耗时、接口访问最大耗时。

Http请求相关的默认监控指标（WebMvcMetricsAutoConfiguration 中配置）：

```shell
# HELP http_server_requests_seconds Duration of HTTP server request handling
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/hello",} 7.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/hello",} 0.045323521
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 4.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 0.067335264
http_server_requests_seconds_count{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 2.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 0.010434425
# HELP http_server_requests_seconds_max Duration of HTTP server request handling
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/hello",} 0.003209155
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 0.016351575
http_server_requests_seconds_max{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 0.0
```



## 参考资料

+ [详解 Prometheus](https://blog.csdn.net/actiontech/category_9950968.html)
