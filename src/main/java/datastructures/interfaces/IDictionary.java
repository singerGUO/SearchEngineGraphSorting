package datastructures.interfaces;

import datastructures.concrete.KVPair;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;

/**
 * Represents a data structure that contains a bunch of key-value mappings. Each key must be unique.
 */
public interface IDictionary<K, V> extends Iterable<KVPair<K, V>> {
    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    V get(K key);

    /**
     * Returns the value corresponding to the given key, if the key exists
     * in the map.
     *
     * If the map does *not* contain the given key, returns the given value.
     *
     * Note: this method does not modify the map in any way.
     */
    V getOrDefault(K key, V defaultValue);

    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replace its value with the given one.
     */
    void put(K key, V value);

    /**
     * Remove the key-value pair corresponding to the given key from the dictionary.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    V remove(K key);

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    boolean containsKey(K key);

    /**
     * Returns the number of key-value pairs stored in this dictionary.
     */
    int size();

    /**
     * Returns 'true' if this dictionary is empty and 'false' otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns a iterator of all key-value pairs contained within this dict.
     *
     * The iterator is not required to yield the key-value pairs in
     * any particular order.
     *
     * Implementation note: you may assume the client will NOT modify a
     * dictionary while simultaneously iterating over it.
     */
    Iterator<KVPair<K, V>> iterator();
}
