package Annotation;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.PARAMETER})
@Inherited
public @interface MethodAnnotation {
    String value();
}
