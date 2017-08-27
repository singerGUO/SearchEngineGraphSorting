package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import datastructures.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

public class TestArrayHeap extends BaseTest {
    /**
     * You MUST always call this method to create new ArrayHeaps:
     * never directly use your ArrayHeap constructor from the
     * rest of your code.
     */
    public <T extends Comparable<T>> IPriorityQueue<T> makeQueue() {
        return new ArrayHeap<>();
    }

    // TODO: Add your tests here

    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IPriorityQueue<Integer> queue = this.makeQueue();
        queue.insert(3);
        queue.insert(2);
        queue.insert(1);

        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());

        assertEquals(1, queue.removeMin());
        assertEquals(2, queue.removeMin());
        assertEquals(3, queue.removeMin());

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }
}
