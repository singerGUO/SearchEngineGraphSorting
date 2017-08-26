package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;

/**
 * See IDictionary for details on what each method must do.
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You MUST use this field to store the contents of your dictionary.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    //
    // Implementation note: This array should ultimately contain
    // ArrayDictionary instances: each ArrayDictionary should be a
    // "chain" or "bucket" in your hash map.
    private IDictionary<K, V>[] chains;

    // Feel free to add more fields and constants.

    public ChainedHashDictionary() {
        throw new NotYetImplementedException();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type IDictionary<K, V>.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (IDictionary<K, V>[]) new IDictionary[size];
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
        return new ChainedIterator<>(this.chains);
    }

    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        // Note: you are strongly encouraged to add more fields to this inner class.
        // Hint: our reference solution uses three fields, including the one we've already
        // given you.
        private IDictionary<K, V>[] chains;

        private ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            throw new NotYetImplementedException();
        }

        @Override
        public boolean hasNext() {
            throw new NotYetImplementedException();
        }

        @Override
        public KVPair<K, V> next() {
            throw new NotYetImplementedException();
        }
    }
}
