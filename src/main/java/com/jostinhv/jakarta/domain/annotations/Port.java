package com.jostinhv.jakarta.domain.annotations;



import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Port {
    enum Type {
        INPUT,
        OUTPUT
    }

    Type type() default Type.INPUT;
    String value() default "";
}