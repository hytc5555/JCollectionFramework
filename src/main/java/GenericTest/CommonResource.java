package GenericTest;

import Annotation.MyAnnotationDemo;
import Annotation.SuperAnnotation;
import Annotation.SuperAnnotatonDemo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommonResource<E> {

    private E e;

    public CommonResource() {
    }

    void test(List<? super E> list){
    }

    void test1(List<? super Thread> list){
    }


    public <T> void testSub(E e){

    }

    <T extends Object> T test2(T t){
        return t;
    }


}
