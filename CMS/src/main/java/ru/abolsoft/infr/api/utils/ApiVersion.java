package ru.abolsoft.infr.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@ApiVersions() todo: Multiple annotations of same type...
public @interface ApiVersion {
    static String defaultApiVersion = "1";

    String  value() default defaultApiVersion;
}



