package com.jostinhv.jakarta.domain.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseCase {
    String value() default "";
}
