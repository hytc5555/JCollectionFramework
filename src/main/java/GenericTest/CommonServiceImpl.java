package GenericTest;

import java.util.Collection;

public class CommonServiceImpl<E> implements CommonService<E>{

    public static <T> boolean equals1(T e){
        return  false;
    }

    @Override
    public int add(E e) {
        return 0;
    }

    @Override
    public boolean addAll(Collection<?> c, E... ts) {
        return false;
    }

    @Override
    public E get(E e) {
        return null;
    }

    @Override
    public int update(E e) {
        return 0;
    }

    @Override
    public E remove(E e) {
        return null;
    }

    @Override
    public <T> T[] toArray() {
        return null;
    }
}
