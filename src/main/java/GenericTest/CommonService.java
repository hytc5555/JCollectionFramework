package GenericTest;

import java.util.Collection;

public interface CommonService<E> {

    int add(E e);

    boolean addAll(Collection<E> c,E... ts);

    E get(E e);


    int update(E e);

    E remove(E e);

    <T> T[] toArray();

}
