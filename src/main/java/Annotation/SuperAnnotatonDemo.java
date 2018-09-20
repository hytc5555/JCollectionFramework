package Annotation;

import org.springframework.stereotype.Controller;

@SuperAnnotation
@Controller
public class SuperAnnotatonDemo<T> {

    public String name1;

    private String name2;

    @FieldAnnotation("value1")
    public String value;

}
