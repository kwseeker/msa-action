# 链路追踪

 常见方案有两种

+ Sleuth + Zipkin
+ SkyWalking

网上对这两种方案的对比有很多，SkyWalking 更受欢迎，这里仅展示集成 SkyWalking。

> 从图表展示、埋点是否有侵入、支持的功能是否丰富、是否支持报警、支持的存储机制、高可用等方面进行对比。

## SkyWalking

和前面说的监控系统架构类似（链路追踪其实也属于监控系统），也包括数据采集、数据整合、数据存储、数据展示。

工作原理这里不再赘述，参考 framework-src-guide。

项目中使用 SkyWalking 程序员需要做的最多是做些配置，Skywalking 本身已经提供了很丰富的功能，实际业务中可能并不需要自己实现采集器。不过这里为了回顾之前看的 Skywalking Java Agent 源码，自己编码实操下自行封装一个采集器。

步骤：

  1. 使用 Docker 启动一个 skywalking-oap-server 单节点、一个 skywalking-ui 节点、一个 ES 节点；
     
     oap-server 配置：主要是配置 ES 数据库。

  2. 提供一组服务，服务间通过 HTTP Feign RPC 三种方式调用。



