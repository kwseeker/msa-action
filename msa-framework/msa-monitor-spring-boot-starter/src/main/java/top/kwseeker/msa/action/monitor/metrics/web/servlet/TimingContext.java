package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import java.util.concurrent.TimeUnit;

public class TimingContext {

    private final long startTime;
    private final TimeUnit timeUnit;

    public TimingContext(long startTime, TimeUnit timeUnit) {
        this.startTime = startTime;
        this.timeUnit = timeUnit;
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
