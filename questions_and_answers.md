# 开发过程中踩坑和填坑之旅

+ **msa-monitor-spring-boot-starter 中 WebApiMetricsFilter 获取请求对应的 HandlerMethod 失败问题**

  由于是借助 `request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE)` 查找 Handler Method 需要确保在此属性设置之后再查询，解决办法两种：

  1. 看下 Spring MVC 哪里设置的HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE属性，将 WebApiMetricsFilter 注册到之后；
  2. 在接口返回时统计，这样总是可以拿到这个属性值，无所谓 WebApiMetricsFilter 放到过滤器链的哪个位置。



