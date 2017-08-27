package datastructures.dictionaries;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;

public class TestArrayDictionary extends TestDictionary {
    protected <K, V> IDictionary<K, V> newDictionary() {
        return new ArrayDictionary<>();
    }
}
