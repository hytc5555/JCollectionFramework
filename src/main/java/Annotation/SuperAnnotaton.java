package Annotation;

import org.springframework.stereotype.Controller;

@ClassAnnotation(values = "124")
@Controller
public class SuperAnnotaton {

    public String name1;

    private String name2;

    @FieldAnnotation("value1")
    public String value;

}
