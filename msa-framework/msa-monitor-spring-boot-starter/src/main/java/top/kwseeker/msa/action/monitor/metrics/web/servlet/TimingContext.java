package top.kwseeker.msa.action.monitor.metrics.web.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimingContext {

    private long startTime;
    private TimeUnit timeUnit;

    public static TimingContext start() {
        TimingContext timingContext = new TimingContext();
        timingContext.startTime = System.currentTimeMillis();
        timingContext.timeUnit = TimeUnit.MILLISECONDS;
        return timingContext;
    }

    public long getStartTimeInMillis() {
        return timeUnit.toMillis(startTime);
    }
}
