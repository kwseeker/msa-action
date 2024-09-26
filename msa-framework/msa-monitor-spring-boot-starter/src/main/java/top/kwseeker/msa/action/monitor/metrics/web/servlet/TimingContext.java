package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import java.util.concurrent.TimeUnit;

public class TimingContext {

    private long startTime;
    private TimeUnit timeUnit;

    public TimingContext() {}

    public TimingContext(long startTime, TimeUnit timeUnit) {
        this.startTime = startTime;
        this.timeUnit = timeUnit;
    }

    public static TimingContext start() {
        TimingContext timingContext = new TimingContext();
        timingContext.startTime = System.currentTimeMillis();
        timingContext.timeUnit = TimeUnit.MILLISECONDS;
        return timingContext;
    }

    public long getStartTimeInMillis() {
        return timeUnit.toMillis(startTime);
    }

    long getStartTime() {
        return startTime;
    }

    TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
