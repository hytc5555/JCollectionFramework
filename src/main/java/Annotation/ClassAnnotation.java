package Annotation;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface ClassAnnotation {
   String values() default "default";
   String name() default "class";
}
