# 开发过程中踩坑和填坑之旅

+ **msa-monitor-spring-boot-starter 中 WebApiMetricsFilter 获取请求对应的 HandlerMethod 失败问题**

  由于是借助 `request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE)` 查找 Handler Method 需要确保在此属性设置之后再查询，解决办法两种：

  1. 看下 Spring MVC 哪里设置的HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE属性，将 WebApiMetricsFilter 注册到之后；
  2. 在接口返回时统计，这样总是可以拿到这个属性值，无所谓 WebApiMetricsFilter 放到过滤器链的哪个位置。

+ **Prometheus 定时拉取自定义端点（“/actuator/msa-monitor”）的监控数据，但是在 Prometheus 中却搜不到**

  忘记了根据输出格式写返回，浏览器直接请求端点，要求的输出格式是 TextOutputFormat.CONTENT_TYPE_004，但是 Prometheus 定时请求要求的输出格式是 TextOutputFormat.CONTENT_TYPE_OPENMETRICS_100，需要调整代码根据要求格式选择不同的 ExpositionFormatWriter 写返回值。

  TextOutputFormat.CONTENT_TYPE_OPENMETRICS_100 相对于 TextOutputFormat.CONTENT_TYPE_004 主要是最后多了一个 `# EOF`结束标识。

