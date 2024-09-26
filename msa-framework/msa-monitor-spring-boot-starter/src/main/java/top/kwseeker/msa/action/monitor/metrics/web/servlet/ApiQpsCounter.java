package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import io.prometheus.metrics.core.datapoints.CounterDataPoint;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.model.snapshots.CounterSnapshot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 重新封装一个Counter用于统计接口QPS
 * 只保留各接口最近10分钟的数据
 */
public class ApiQpsCounter {

    private static final int TTL = 10 * 60;  //10 minutes
    private static final int CLEAR_INTERVAL = 10;  //10s
    private final Counter counter;
    // 需要记录一下各接口的labels(uri、method)，用于清除过期的数据, 保持和 client_java 的 key 一致
    // 时间（s）-> 各接口此时间记录的labels
    private final Map<Long, Set<List<String>>> timeToLabels;

    public ApiQpsCounter(Counter counter, ScheduledExecutorService clearTimeoutRecordData) {
        this.counter = counter;
        this.timeToLabels = new ConcurrentHashMap<>();
        // 每10秒清除一次超过10分钟的数据
        clearTimeoutRecordData.scheduleAtFixedRate(this::clearTask,
                TTL - CLEAR_INTERVAL, CLEAR_INTERVAL, TimeUnit.SECONDS);
    }

    public CounterDataPoint labelValues(String uri, String method, Long timeSeconds) {
        //记录labels
        Set<List<String>> labelsSet = timeToLabels.computeIfAbsent(timeSeconds, time -> new HashSet<>());
        List<String> newLabelsValues = new ArrayList<>(3);
        newLabelsValues.add(uri);
        newLabelsValues.add(method);
        newLabelsValues.add(Long.toString(timeSeconds));
        labelsSet.add(newLabelsValues);
        //获取标签的DataPoint(内部是不存在则创建)
        return counter.labelValues(newLabelsValues.toArray(new String[3]));
    }

    public CounterSnapshot collect() {
        // 采集前先执行一下清理过期数据
        clearTask();
        return counter.collect();
    }

    private void clearTask() {
        long currentTimeSecond = System.currentTimeMillis() / 1000;
        for (Map.Entry<Long, Set<List<String>>> entry : timeToLabels.entrySet()) {
            long recordTime = entry.getKey();
            if (currentTimeSecond - recordTime > TTL) {
                timeToLabels.remove(recordTime);
                for (List<String> labelValues : entry.getValue()) {
                    String[] array = labelValues.toArray(new String[0]);
                    counter.remove(array);
                }
            }
        }
    }

    public Counter getCounter() {
        return counter;
    }

    public Map<Long, Set<List<String>>> getTimeToLabels() {
        return timeToLabels;
    }
}
