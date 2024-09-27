package top.kwseeker.msa.action.monitor.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class MergedAnnotationsTest {

    @Test
    void getAllAnnotations() throws NoSuchMethodException {
        Class<ExampleClass> clazz = ExampleClass.class;
        // 获取 当前类 + 父类 + 接口 + 外部类 ( 当前类是内部类时才有外部类 ) 所有的注解
        MergedAnnotations annotations = MergedAnnotations.from(clazz, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES);
        // RetentionPolicy.CLASS 到运行时已经被删除了
        Assertions.assertFalse(annotations.isPresent(MyClassAnnotation.class));
        Assertions.assertTrue(annotations.isPresent(MyRuntimeAnnotation.class));
        // MetricsWebApi 在类的方法上，MergedAnnotations 不会查方法上的注解
        Assertions.assertFalse(annotations.isPresent(MetricsWebApi.class));

        // 获取方法上的注解
        Method method = ExampleClass.class.getMethod("anotherMethod");
        MergedAnnotations methodAnnotations = MergedAnnotations.from(method);   // 默认 SearchStrategy.DIRECT，仅查当前类
        Assertions.assertTrue(methodAnnotations.isPresent(MetricsWebApi.class));
    }

    @Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.CLASS)
    @Inherited
    @interface MyClassAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface MyRuntimeAnnotation {}

    static class BaseClass {
        @MyClassAnnotation
        public void method() {}
    }

    @MyRuntimeAnnotation
    static class ExampleClass extends BaseClass {
        @MetricsWebApi
        public void anotherMethod() {}
    }
}
