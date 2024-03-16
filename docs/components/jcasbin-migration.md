# JCasbin 迁移

指从其他安全框架迁移到 JCasbin。

JCasbin 的权限配置更灵活、使用更简单，
但是原本使用 Spring Security 的工程迁移到 JCasbin 并不是很方便。 

迁移的主要难点其实不在于框架，Spring Security 的源码对业务代码的侵入性已经很低了，几乎只有一些注解的侵入;
不过将Spring Security的依赖全部去掉，这些注解也不会对代码运行产生什么影响；

迁移的主要难点在于使用 Spring Security 实现权限校验的时候对权限数据结构的设计可能是随意发挥的，
这些数据结构可能无法用于JCasbin；除非集成Spring Security时就是参考的JCasbin的权限数据模型设计的权限数据结构。

> 其实可以考虑在 Spring Security 中使用 Casbin 的权限管理模型，相比自己设计 Casbin 的模型更灵活。

像 casbin 官方提供的 casbin-spring-boot-example 虽然看上去是将 JCasbin 集成到了 Spring Security 但是
完全是错误的使用方式，里面出现了重复的权限校验（Casbin的Enforcer和Spring Security的FilterSecurityInterceptor各执行了一次权限校验）
这样的话其实相当于是将Casbin当作了一个权限数据源。
