package Annotation;

@ClassAnnotation(values="myclass",name="myclass")
@RepeatableAnnotation("123")
@RepeatableAnnotation("234")
public class MyAnnotationDemo extends @SuperAnnotation SuperAnnotatonDemo {


    @FieldAnnotation("name")
    private String name;

    @FieldAnnotation("value")
    public String value;

    @ConstractorAnnotation("ConstractorAnnotation")
    public MyAnnotationDemo(){
    }


    @ConstractorAnnotation("ConstractorAnnotation")
    private MyAnnotationDemo(String name){

    }


    @ConstractorAnnotation("ConstractorAnnotation")
    public MyAnnotationDemo(String name, String value){

    }

    @MethodAnnotation("method")
    public @SuperAnnotation String annoTest(String name){
        return "";
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
