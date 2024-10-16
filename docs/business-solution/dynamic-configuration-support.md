# 动态业务配置支持

之前碰到一个业务的动态配置实现有两种方式，但是感觉都有瑕疵：

+ 本地缓存 + MySQL

  实现方式：服务实例中设置定时任务每隔几分钟从MySQL同步一下，同步到本地缓存。

  存在问题：

  + 本地配置无法实时更新
  + 配置其实修改频率很低，每隔几分钟同步一次，浪费系统资源。

+ Redis

  实现方式：直接将配置存在Redis。

  存在问题：

  + 配置其实修改频率很低，每次都通过请求Redis服务器拉取配置，浪费系统资源。

其实可以通过 Zookeeper、Nacos、Apollo、Disconf 这些成熟的中间件作为业务配置中心。
针对配置修改频率低的特点，使用配置中心**主动推送**的模式明显更合理。

这里以活动为例展示分别使用 Zookeeper、Nacos 实现业务配置中心。

为简单起见配置数据仅包含活动开始和结束时间。



## Zookeeper 作为业务配置中心

核心原理就是使用持久化节点保存配置数据，ZK客户端（业务服务）注册监控（Watcher）监听节点数据变化，一旦有数据变更则更新本地数据。

> 注意数据是 ZK Server 主动推过来的。



## Nacos 作为业务配置中心

本质和上面的原理类似，依赖 Nacos 注册与发现，Nacos 配置变更后可以通过监听客户端列表以主动推的方式发给各个服务实例。



## 动态业务配置使用场景

+ 业务动态配置，比如线上配置活动开始、结束时间等等

  参考 msa-service-web-bdcc 。

+ 业务接口降级开关

  + Zookeeper 实现方案

    业务服务本地维持一个 Manager 类用于监听 Zookeeper 中降级节点并缓存。将路由每一层拆分为一个ZNode, 每一层都可以设置是否降级。比如路由 /a/b/c , ZNode层级为：

    ````
    /a：true / false
    	/b: true / false
    		/c: true / false
    ````

    节点数据结构：

    ```java
    static class Node {
        // 路由节点路径
        private String path;
        // 是否降级
        private boolean degrade;
        private boolean isLeaf;
        // 子路由节点
        private Map<String, Node> children;
    }
    ```

    注册Filter、Inteceptor、AOP在请求被处理前判断从根路由开始依次判断是否被降级，直到遇到被降级直接调用对应的降级方法并返回。

  + Nacos 实现方案

    和 ZK 方案类似。

+ 线上动态修改日志级别
