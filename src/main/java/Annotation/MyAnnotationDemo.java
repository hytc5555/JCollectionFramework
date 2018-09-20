package Annotation;

import GenericTest.CommonResource;

@ClassAnnotation(values="myclass",name="myclass")
@RepeatableAnnotation("123")
@RepeatableAnnotation("234")
public class MyAnnotationDemo<T> extends @SuperAnnotation SuperAnnotatonDemo<T> {


    private T t;
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
    public @SuperAnnotation T annoTest(String name){
        System.out.println("annoTest:"+name);
        return t;
    }

    public static String staticTest(){
        return "234";
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
