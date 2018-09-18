package Reflect;

import Annotation.FieldAnnotation;
import Annotation.MyAnnotationDemo;
import Annotation.ClassAnnotation;
import Annotation.RepeatablesAnnotation;
import Annotation.RepeatableAnnotation;
import Annotation.SuperAnnotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class ReflectTest {

    public static void main(String[] args) {
        try {
            MyAnnotationDemo myAnnotation = new MyAnnotationDemo();
            myAnnotation.setValue("1234");

            //获取Class对象
            Class<MyAnnotationDemo> cl = MyAnnotationDemo.class;
            Class<? extends MyAnnotationDemo> c = new MyAnnotationDemo().getClass();
            Class<?> clazz =  Class.forName("collection.List.ArrayListTest");


            //获取类名
            System.out.println(cl.getName());
            System.out.println(cl.getSimpleName());
            System.out.println(cl.getCanonicalName());
            System.out.println(cl.getTypeName());
            //获取类修饰符
            int modi = cl.getModifiers();
            System.out.println(Modifier.isPublic(modi));
            System.out.println(Modifier.toString(modi));
            //获取父类
            cl.getSuperclass();
            //获取接口
            cl.getInterfaces();
            //获取类上的注解
            boolean hasAnnotation = cl.isAnnotation();//是否是注解
            boolean hasAnno = cl.isAnnotationPresent(SuperAnnotation.class);//是否包含某种类型的注解
            Annotation[] classAnnotations = cl.getAnnotations();//获取类的注解，包括父类
            Annotation[] cDeclaredAnnotations = cl.getDeclaredAnnotations();//只获取本类的注解
            ClassAnnotation classAnnotation = cl.getAnnotation(ClassAnnotation.class);//获取指定类型的注解
            RepeatableAnnotation[] repeatablesAnnotation = cl.getAnnotationsByType(RepeatableAnnotation.class);//获取指定类型的注解，用于可重复的注解
            AnnotatedType annotatedType1 = cl.getAnnotatedSuperclass();//获取父类注解
            AnnotatedType[] annotatedType2 = cl.getAnnotatedInterfaces();//获取接口注解


            //获取成员变量
            //获取自身的所有的 public 属性，包括从父类继承下来的。
            Field[] fields = cl.getFields();
            Field field1 = cl.getField("value");
            Field field2 = cl.getDeclaredField("value");
            //获取所有的属性，但不包括从父类继承下来的属性
            Field[] fields2 = cl.getDeclaredFields();
            for(Field field : fields2){
                field.setAccessible(true);
                //成员变量修饰符
                int fieldModi = field.getModifiers();
                System.out.println(Modifier.isPublic(fieldModi));
                //成员变量类型
                field.getGenericType();//可以获取到泛型
                field.getType().getSimpleName();
                //成员变量名称
                System.out.println(field.getName());
                //获取一个对象中改属性的值
                System.out.println(field.get(myAnnotation));
                //往对象的属性中设置值
                field.set(myAnnotation,field.getType().getConstructor().newInstance());

                //获取属性上的注解
                field.isAnnotationPresent(FieldAnnotation.class);
                field.isAccessible();
                Annotation[] annotations = field.getAnnotations();
                FieldAnnotation f1 = field.getAnnotation(FieldAnnotation.class);//获取成员变量上的注解
                FieldAnnotation[] myAnnotations = field.getAnnotationsByType(FieldAnnotation.class);
                field.getDeclaredAnnotations();//忽略继承的注解
                field.getDeclaredAnnotation(FieldAnnotation.class);
                field.getDeclaredAnnotationsByType(FieldAnnotation.class);
                field.isAnnotationPresent(FieldAnnotation.class);
            }

            //获取方法
            Method[] methods  = cl.getMethods();
            Method[] methods1 = cl.getDeclaredMethods();
            Method method  = cl.getEnclosingMethod();
            Method method1 = cl.getMethod("annoTest",String.class);
            Method method2 = cl.getDeclaredMethod("annoTest",String.class);
            int i = method1.getModifiers();//获取修饰符
            String name = method1.getName();//获取方法名称
            Class class1 = method1.getReturnType();//获取方法返回值类型
            Type type = method1.getGenericReturnType();//获取方法返回值类型，包括泛型
            AnnotatedType annotatedType = method1.getAnnotatedReturnType();//获取方法返回类型注解
            Parameter[] parameters = method1.getParameters();
            for (Parameter parameter : parameters){
                parameter.getName();
            }
            method1.getParameterCount();
           String result = (String)method1.invoke(cl.newInstance(),"12");
            Method method3 = cl.getMethod("staticTest");
            String result2 =(String) method3.invoke(null);


            //获取 Constructor
            Constructor<?>[] constructors = cl.getConstructors();
            Constructor<?>[] constructors2 = cl.getDeclaredConstructors();
            Constructor<MyAnnotationDemo> constructor1 = cl.getConstructor();
            constructor1.newInstance();

            cl.isArray();

            cl.isEnum();
            cl.getEnumConstants();
            field1.isEnumConstant();

            cl.getDeclaredClasses();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
