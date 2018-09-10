package collection.Queue;

import org.junit.Test;

import java.util.Iterator;
import java.util.PriorityQueue;

public class PriorityQueueTest {
    @Test
    public void iteratorTest(){
        PriorityQueue priorityQueue = new PriorityQueue();
        priorityQueue.offer(1);
        priorityQueue.offer(2);
        priorityQueue.offer(3);
        priorityQueue.offer(4);
        priorityQueue.offer(5);
        priorityQueue.offer(6);

        Iterator it = priorityQueue.iterator();
        while (it.hasNext()){
            it.next();
            it.remove();
        }
    }
}
