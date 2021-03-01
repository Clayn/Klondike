package de.clayntech.klondike.sdk.param;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterDefinition {

    Class<?> type();

    String value() default "";

    boolean optional() default true;
}
