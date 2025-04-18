package com.jostinhv.jakarta.domain.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataTransferObject {
    DtoType type() default DtoType.REQUEST;

    String value() default "";

    enum DtoType {
        REQUEST,
        RESPONSE,
        INTERNAL
    }
}