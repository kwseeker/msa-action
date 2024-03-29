/**
 * 将接口（HTTP、RPC）、MQ、定时任务放在这一层，因为这么做程序的逻辑入口比较清晰，首次接收项目梳理业务逻辑或者后面回顾业务逻辑时只需要去这一层寻找逻辑入口即可。
 */
package top.kwseeker.msa.mall.trigger;