package com.jostinhv.jakarta.domain.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Adapter {
    AdapterType type() default AdapterType.REST;
    String value() default "";

    enum AdapterType {
        REST,
        JPA,
        SOAP,
        GRAPHQL
    }
}