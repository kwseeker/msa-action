package top.kwseeker.msa.action.webha.trigger.http;

/*
以 Nacos 作为 Sentinel 配置中心：
规则配置：
msa-service-web-ha-sentinel-flow-rules:
[
    {
        "resource": "/flow",
        "limitApp": "default",
        "grade": 1,
        "count": 5,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "/flow1",
        "limitApp": "default",
        "grade": 1,
        "count": 5,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    },
    {
        "resource": "HelloService::testFlow",
        "limitApp": "default",
        "grade": 1,
        "count": 3,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]

msa-service-web-ha-sentinel-degrade-rules:
[
    {
        "resource": "/fuse",
        "grade": 0,
        "count": 1000,
        "timeWindow": 10,
        "minRequestAmount": 5,
        "statIntervalMs": 1000,
        "slowRatioThreshold": 0.1
    }
]


msa-service-web-ha-sentinel-system-rules:
[
    {
        "highestSystemLoad": 0.8,
        "highestCpuUsage": 0.8,
        "qps": 10,
        "avgRt": 1000,
        "maxThread": 200
    }
]

msa-service-web-ha-sentinel-param-flow-rules:
[
    {
        "resource": "/hot_param",
        "count": 5,
        "grade": 1,
        "durationInSec": 1,
        "controlBehavior": 0,
        "maxQueueingTimeMs": 0,
        "paramIdx": 1,
        "clusterMode": false
    }
]

msa-service-web-ha-sentinel-user-authority-rules:
[
    {
        "resource": "*",
        "limitUsers": "10001,10002",
        "strategy": 1
    }
]
*/