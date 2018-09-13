package Annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        MyAnnotation myAnnotation = new MyAnnotation();

        Class<MyAnnotation> cl = MyAnnotation.class;

        if(cl.isAnnotationPresent(ClassAnnotation.class)){
            ClassAnnotation classAnnotation = cl.getAnnotation(ClassAnnotation.class);
            System.out.println(classAnnotation.name());
            System.out.println(classAnnotation.values());
        }

        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
            System.out.println(fieldAnnotation.value());
        }

        try {
            Constructor<MyAnnotation> constructors = cl.getConstructor();
            ConstractorAnnotation constractorAnnotation = constructors.getAnnotation(ConstractorAnnotation.class);
            System.out.println(constractorAnnotation.value());

            Method testMethod =MyAnnotation.class.getDeclaredMethod("annoTest");
            if(testMethod != null){
                System.out.println(testMethod.getAnnotation(MethodAnnotation.class).value());
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
