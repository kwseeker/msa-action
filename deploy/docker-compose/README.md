# 本地开发环境部署

注意项目模块在 IDEA 中本地执行，中间件是在 Docker 中执行（通过 docker-compose 启动）。

## 项目启动

先启动依赖的中间件：

```shell
cd deploy/docker-compose/local-dev
dc up
# 或者根据需要启动中间件容器
dc up <container_name>
```

> 本地开发环境 docker 容器全部关闭自动重启。



