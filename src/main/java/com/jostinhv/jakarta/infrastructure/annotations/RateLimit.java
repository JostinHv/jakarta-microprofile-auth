package com.jostinhv.jakarta.infrastructure.annotations;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RateLimit {
    @Nonbinding int limit() default 10;
    @Nonbinding int minutes() default 1;
}