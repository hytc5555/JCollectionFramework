package Annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RepeatablesAnnotation.class)
public @interface RepeatableAnnotation {
    String value();
}

