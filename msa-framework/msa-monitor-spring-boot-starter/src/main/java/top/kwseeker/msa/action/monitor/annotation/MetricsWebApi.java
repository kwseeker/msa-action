package top.kwseeker.msa.action.monitor.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // 保留到运行时，否则注解拿不到
@Inherited
public @interface MetricsWebApi {
}
