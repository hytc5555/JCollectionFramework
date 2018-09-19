package Reflect;


import Annotation.MethodAnnotation;
import Annotation.MyAnnotationDemo;
import Annotation.SuperAnnotation;
import GenericTest.CommonResource;

import java.lang.reflect.*;
import java.util.Map;

public class TestType<K extends Thread> {
    Map<String, String> map;


    @SuperAnnotation
    K k;

    String test;

    K[] ks;

    <T extends Thread> @SuperAnnotation T  getK(){
        return (T)new Object();
    }

    CommonResource<String> commonResource;

    public static void main(String[] args) throws Exception {
        Field f = TestType.class.getDeclaredField("k");
        System.out.println(f.getGenericType());
        System.out.println(f.getGenericType() instanceof TypeVariable);
        System.out.println(f.getGenericType() instanceof ParameterizedType);

        // 获取字段的类型
        Field fk = TestType.class.getDeclaredField("k");

        TypeVariable keyType = (TypeVariable)fk.getGenericType();

        keyType.getAnnotations();

        // getName 方法
        System.out.println(keyType.getName());                 // K

        // getGenericDeclaration 方法
        System.out.println(keyType.getGenericDeclaration());   // class com.test.TestType

        // getBounds 方法
        System.out.println("K 的上界:");                        // 有两个
        for (Type type : keyType.getBounds()) {                // interface java.lang.Comparable
            System.out.println(type);                          // interface java.io.Serializable
        }

        Method method = TestType.class.getDeclaredMethod("getK");
        //System.out.println(method.getGenericReturnType() instanceof TypeVariable);
        //System.out.println(method.getGenericReturnType() instanceof ParameterizedType);
        TypeVariable typeVariable = (TypeVariable) method.getGenericReturnType();
        typeVariable.getAnnotations();


        Field f1 = TestType.class.getDeclaredField("ks");
        System.out.println(f1.getGenericType());
        System.out.println(f1.getGenericType() instanceof TypeVariable);
        System.out.println(f1.getGenericType() instanceof GenericArrayType);
        GenericArrayType genericArrayType = (GenericArrayType)f1.getGenericType();
        System.out.println(genericArrayType.getGenericComponentType() );
        System.out.println(genericArrayType.getGenericComponentType() instanceof  TypeVariable);
    }
}

