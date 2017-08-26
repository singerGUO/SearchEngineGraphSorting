package datastructures.interfaces;

import java.util.Iterator;

import misc.exceptions.NoSuchItemException;

/**
 * Represents a collection of non-duplicate items.
 */
public interface ISet<T> extends Iterable<T> {
    /**
     * Adds the given item to the set, if not already present.
     */
    void add(T item);

    /**
     * Removes the given item from the set, if possible.
     *
     * @throws NoSuchItemException  if this set does not contain the given item
     */
    void remove(T item);

    /**
     * Returns 'true' if this set contains the given element and false otherwise.
     */
    boolean contains(T item);

    /**
     * Returns the number of elements contained within this set.
     */
    int size();

    /**
     * Returns 'true' if this set is empty and 'false' otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns a iterator of all items contained within this set.
     *
     * The iterator is not required to yield the items in any particular order.
     *
     * Implementation note: you may assume the client will NOT modify a
     * set while simultaneously iterating over it.
     */
    Iterator<T> iterator();
}
