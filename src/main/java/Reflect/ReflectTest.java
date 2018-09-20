package Reflect;

import Annotation.FieldAnnotation;
import Annotation.MyAnnotationDemo;
import Annotation.ClassAnnotation;
import Annotation.RepeatableAnnotation;
import Annotation.SuperAnnotation;
import Annotation.MethodAnnotation;
import GenericTest.CommonResource;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class ReflectTest {


    @Test
    public void classTest() throws ClassNotFoundException {

        //获取Class对象
        Class<MyAnnotationDemo> demoClass = MyAnnotationDemo.class;

        Class<MyAnnotationDemo> demoClass1 = (Class<MyAnnotationDemo>) new MyAnnotationDemo().getClass();

        Class<MyAnnotationDemo> demoClass2 =(Class<MyAnnotationDemo>) Class.forName("collection.List.ArrayListTest");


        //获取类修饰符
        int modi = demoClass.getModifiers();
        System.out.println(Modifier.isPublic(modi));

        //获取类名
        System.out.println(demoClass.getName());
        System.out.println(demoClass.getSimpleName());
        System.out.println(demoClass.getCanonicalName());

        //获取父类类型
        demoClass.getSuperclass();//获取父类的原始类型
        Type type = demoClass.getGenericSuperclass();//获取父类的泛型类型
        AnnotatedType annotatedType = demoClass.getAnnotatedSuperclass();//获取父类的注解类型


        //获取接口类型
        demoClass.getInterfaces();//获取接口的原始类型
        Type[] types = demoClass.getGenericInterfaces();//获取接口的泛型类型
        AnnotatedType[] annotatedTypes = demoClass.getAnnotatedInterfaces();//获取接口的注解类型

        //获取类上的注解
        demoClass.isAnnotation();//是否有注解
        demoClass.isAnnotationPresent(SuperAnnotation.class);//是否包含某种类型的注解
        Annotation[] annotations1 = demoClass.getAnnotations();//获取类的注解，包括父类
        Annotation[] annotations2 = demoClass.getDeclaredAnnotations();//只获取本类的注解
        ClassAnnotation classAnnotation = demoClass.getAnnotation(ClassAnnotation.class);//获取指定类型的注解
        RepeatableAnnotation[] repeatablesAnnotation = demoClass.getAnnotationsByType(RepeatableAnnotation.class);//获取指定类型的注解，用于可重复的注解


        demoClass.isArray();
        demoClass.isEnum();
        demoClass.getEnumConstants();
    }

    @Test
    public void methodTest() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        Class<CommonResource> demoClass = CommonResource.class;


        //获取方法
        demoClass.getMethods();//获取public方法，包含父类
        demoClass.getDeclaredMethods();//获取定义的方法
        Method method = demoClass.getMethod("testSub",Object.class);
        demoClass.getDeclaredMethod("testSub",Object.class);


        //设置方法的可访问标志
        method.setAccessible(true);


        //成员所属类的类型
        method.getDeclaringClass();


        //获取方法注解
        method.isAnnotationPresent(MethodAnnotation.class);
        method.getAnnotations();
        method.getDeclaredAnnotations();
        method.getAnnotation(MethodAnnotation.class);
        method.getDeclaredAnnotation(MethodAnnotation.class);
        method.getAnnotationsByType(MethodAnnotation.class);
        method.getDeclaredAnnotationsByType(MethodAnnotation.class);


        //获取方法修饰符
        method.getModifiers();


        //获取方法的类型变量列表
        TypeVariable<Method>[] typeVariables = method.getTypeParameters();


        //返回值相关
        method.getReturnType();
        method.getGenericReturnType();
        method.getAnnotatedReturnType();


        //获取方法名称
        method.getName();


        //形参相关
        Parameter[] parameters = method.getParameters();//获取参数列表
        method.getParameterCount();//获取参数个数

        Annotation[][] annotations = method.getParameterAnnotations();//获取参数的注解列表

        Class<?>[] classes = method.getParameterTypes();//获取参数的原始类型列表
        Type[] types = method.getGenericParameterTypes();//获取参数的泛型类型列表
        AnnotatedType[] annotatedTypes = method.getAnnotatedParameterTypes();//获取参数的注解类型列表

        Type type = types[0];
        System.out.println(type instanceof Class);
        System.out.println(type instanceof ParameterizedType);//泛型类型是否是参数类型
        System.out.println(type instanceof TypeVariable);//泛型类型是否是类型变量
        System.out.println(type instanceof GenericArrayType);//泛型类型是否是泛型数组
        System.out.println(type instanceof WildcardType);//泛型类型是否是通配符方式
        AnnotatedType annotatedType = annotatedTypes[0];
        System.out.println(annotatedType instanceof AnnotatedParameterizedType);
        System.out.println(annotatedType instanceof AnnotatedTypeVariable);
        System.out.println(annotatedType instanceof AnnotatedArrayType);
        System.out.println(annotatedType instanceof AnnotatedWildcardType);


        //异常相关
        method.getExceptionTypes();
        method.getGenericExceptionTypes();
        method.getAnnotatedExceptionTypes();


        //方法执行
        method.invoke(demoClass.newInstance(),new Object());
    }

    @Test
    public void testField() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<CommonResource> demoClass = CommonResource.class;

        //获取属性
        demoClass.getFields();
        demoClass.getDeclaredFields();
        //demoClass.getField("");
        Field field = demoClass.getDeclaredField("e");

        //设置属性的可访问标志
        field.setAccessible(true);


        //获取成员所属的类的类型
        field.getDeclaringClass();


        //获取注解信息
        field.isAnnotationPresent(FieldAnnotation.class);
        field.getAnnotations();
        field.getDeclaredAnnotations();
        field.getAnnotation(FieldAnnotation.class);
        field.getDeclaredAnnotation(FieldAnnotation.class);
        field.getAnnotationsByType(FieldAnnotation.class);
        field.getDeclaredAnnotationsByType(FieldAnnotation.class);


        //获取属性修饰符
        field.getModifiers();


        //获取属性类型
        Class<?> classes = field.getType();//获取属性原始类型
        Type type = field.getGenericType();//获取属性泛型类型
        AnnotatedType annotatedType = field.getAnnotatedType();//获取属性注解类型


        //获取属性名称
        field.getName();


        //获取一个对象中改属性的值
        field.get(new CommonResource());
        //往对象的属性中设置值
        field.set(new CommonResource(),new Object());

    }


    @Test
    public void TestConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<CommonResource> demoClass = CommonResource.class;

        demoClass.getConstructors();
        demoClass.getDeclaredConstructors();
        Constructor<CommonResource> constructor = demoClass.getConstructor();
        demoClass.getDeclaredConstructor();

        constructor.setAccessible(true);

        //同Method

        //创建对象
        CommonResource commonResource = constructor.newInstance();
    }

}
