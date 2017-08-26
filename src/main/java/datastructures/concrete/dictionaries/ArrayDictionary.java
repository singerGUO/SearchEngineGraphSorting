package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;

/**
 * Replace this class with your ArrayDictionary implementation from project 1.
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    public ArrayDictionary() {
        throw new NotYetImplementedException();
    }

    @Override
    public V get(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        throw new NotYetImplementedException();
    }

    @Override
    public void put(K key, V value) {
        throw new NotYetImplementedException();
    }

    @Override
    public V remove(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public boolean containsKey(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        throw new NotYetImplementedException();
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        throw new NotYetImplementedException();
    }
}
