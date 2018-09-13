package Reflect;

import Annotation.FieldAnnotation;
import Annotation.MyAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectTest {

    public static void main(String[] args) {
        try {

            MyAnnotation myAnnotation = new MyAnnotation();
            myAnnotation.setValue("1234");

            Class<MyAnnotation> cl = MyAnnotation.class;
            Class<? extends MyAnnotation> c = new MyAnnotation().getClass();
            Class<?> clazz =  Class.forName("collection.List.ArrayListTest");

            Annotation[] classAnnotations = cl.getAnnotations();

            //获取类名
            System.out.println(cl.getName());
            System.out.println(cl.getSimpleName());
            System.out.println(cl.getCanonicalName());
            System.out.println(cl.getTypeName());


            //获取类修饰符
            int modi = cl.getModifiers();
            System.out.println(Modifier.isPublic(modi));
            System.out.println(Modifier.toString(modi));

            //获取成员变量
            Field[] fields = cl.getFields();
            Field field1 = cl.getField("value");
            Field field2 = cl.getDeclaredField("value");
            Field[] fields2 = cl.getDeclaredFields();
            for(Field field : fields2){
                field.setAccessible(true);
                //成员变量修饰符
                int fieldModi = field.getModifiers();
                System.out.println(Modifier.isPublic(fieldModi));
                //成员变量类型
                System.out.println(field.getType().getSimpleName());
                //成员变量名称
                System.out.println(field.getName());
                //获取一个对象中改属性的值
                System.out.println(field.get(myAnnotation));
                //往对象的属性中设置值
                field.set(myAnnotation,"23");
                System.out.println(myAnnotation.getValue());
                //获取成员变量上的注解
                FieldAnnotation f1 = field.getAnnotation(FieldAnnotation.class);
                System.out.println(f1.value());
                AnnotatedType annotatedType = field.getAnnotatedType();
                FieldAnnotation[] myAnnotations = field.getAnnotationsByType(FieldAnnotation.class);
                field.getAnnotations();
                //忽略继承的注解
                field.getDeclaredAnnotations();
                field.getDeclaredAnnotation(FieldAnnotation.class);
                field.getDeclaredAnnotationsByType(FieldAnnotation.class);
                field.isAnnotationPresent(FieldAnnotation.class);
                field.get(new MyAnnotation());
            }

            //获取 Constructor
            Constructor<?>[] constructors = cl.getConstructors();
            for(Constructor constructor : constructors){
                
            }



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
