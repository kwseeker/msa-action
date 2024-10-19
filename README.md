# msa-action

微服务项目架构。弱化业务，专注于框架本身，相当于一个裸脚手架。

后面也会在这里模拟实现一些常用业务方案。

开发过程中的踩坑与填坑之旅：[questions_and_answers.md](questions_and_answers.md)

## 计划整合

+ 基础框架 

  + [x] Spring Boot 

  + [x] Spring Cloud 
+ 认证与授权

  + [x] Spring Security 
+ [ ] Shiro
  + [x] JCasbin
+ [ ] 安全框架平替
+ 数据相关

  + [ ] Mybatis

  + [x] Mybatis-Plus

  + [x] MySQL

  + [ ] PostgreSQL

  + [ ] TiDB

  + [ ] MongoDB
+ 分布式技术

  + 注册中心与配置中心

    + [x] Nacos
  + [ ] Etcd
    + [ ] 框架平替
+ 缓存
    + [ ] Redis
    + [ ] Ehcache
+ 消息队列
  
  + [ ] RocketMQ
  
  + [ ] Kafka


+ 计划任务
    + [ ] XXL-JOB
+ 分库分表
    + [ ] Sharding-JDBC
+ 网关
    + [ ] Nginx
    + [x] Spring Cloud Gateway
+ 通信

  + [ ] Thrift
  + [x] GRPC
  + [x] Feign
  + [ ] Dubbo

+ 分布式协调

   + [x] Zookeeper 


+ 日志、监控、告警

  + [ ] ELK
  + [x] SkyWalking
  + [x] Prometheus

+ 工作流
  + [ ] Activiti


+ 测试
  + [ ] Mock
    + [ ] 第三方接口模拟
+ 架构模式
  + [ ] DDD
+ 运维

  + [x] Docker-Compose
  + [ ] K8S
+ 三高保证

  + 高并发

  + 高性能

  + 高可用
    + [x] Sentinel
    + [ ] Hystrix
    + [ ] Jd-Hotkey
+ 文档工具
  + [ ] SpringFox Swagger2


+ 效率工具

  + [ ] 代码生成器

  + [ ] 项目模板
+ 特殊需求

  + [ ] 支付

  + [ ] 大模型

  + [ ] 三方登录
+ JDK

  + [ ] JDK8
  + [ ] JDK17
  + [ ] JDK21



## 经验整合

> 一些实用的功能：

+ [ ] [动态配置](docs/business-solution/dynamic-configuration-support.md)

  案例：

  + 业务动态配置，比如线上配置活动开始、结束时间等等
  + 业务接口降级开关
  + 线上动态修改日志级别

+ [ ] [权限管理设计](docs/business-solution/permission-management-rbac-and-abac.md)

  案例：

  + 运营管理后台以及面向B端的产品基本都需要权限管理

  + [ ] SpringSecurity + JCasbin 实现灵活的 RBAC + ABAC 权限控制

+ [ ] 高可用
  + [ ] 接口降级开关
  + [ ] Sentinel 自定义用户白名单限流规则
  + [ ] Jd-Hotkey 接口防刷

+ [ ] 网关
  + [ ] 游戏网关与负载均衡

+ [ ] 任务编排
+ [ ] 接口灰度发布
  + [ ] 自定义网关灰度负载均衡方案
  + [ ] 自定义灰度服务
+ [ ] 接口性能
  + [ ] 接口聚合
+ [ ] ...



## 中间件拓展

> 中间件的提供的功能无法满足业务需求时的拓展。

+ Sentinel
  + 接口定制化降级实现
  + 用户黑名单限流




## 模块说明

+ **msa-dependencies**

+ msa-example

+ **msa-framework**

  + **msa-common**

  + **msa-monitor-reporter**

    监控系统的数据收集器，主要包含两部分：数据采集、数据上报。

    数据采集：从监控指标类型看，数据收集器可能需要依赖日志 Appender（收集业务日志、性能指标）、Metric（收集机器系统资源占用）、JMX（收集 JVM 信息）实现。

    数据上报：可以借助消息队列、Redis发布订阅、Guava消息总线、RPC框架、甚至Netty实现。

  + **msa-mybatis-spring-boot-starter**

  + **msa-security-spring-boot-starter**

  + **msa-web-spring-boot-starter**

+ **msa-gateway**

+ **msa-service-mall**

+ **msa-service-user**

+ **msa-service-web-one**