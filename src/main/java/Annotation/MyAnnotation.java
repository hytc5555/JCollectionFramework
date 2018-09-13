package Annotation;

import org.springframework.stereotype.Controller;

@ClassAnnotation(values="myclass",name="myclass")
@Controller
public class MyAnnotation extends SuperAnnotaton{


    @FieldAnnotation("name")
    private String name;

    @FieldAnnotation("value")
    public String value;

    @ConstractorAnnotation("ConstractorAnnotation")
    public MyAnnotation(){
    }


    @ConstractorAnnotation("ConstractorAnnotation")
    public MyAnnotation(String name){

    }


    @ConstractorAnnotation("ConstractorAnnotation")
    public MyAnnotation(String name,String value){

    }

    @MethodAnnotation("method")
    public void annoTest(){

    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }
}
