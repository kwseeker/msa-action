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

### micrometer-registry-prometheus

 Micrometer 实现的 Prometheus 数据采集器，引入下面两个依赖后，可以看到 `/actuator`下会多出一个 `/actuator/prometheus`。

Prometheus 数据中心借助  simpleclient  通过这个接口拉取监控数据。

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

采集的数据包括应用基本信息、应用中线程池信息、HTTP Server 请求统计信息、JVM信息、系统信息等。

