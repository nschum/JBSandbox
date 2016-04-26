package de.nschum.jbsandbox.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A map that can contain only up to two entries but is very lightweight and fast.
 * <p/>
 * These are much faster to create than HashMaps and help optimize the common case of very few variables (map/reduce).
 */
public class TinyMap<K, V> extends AbstractMap<K, V> {
    private K key1;
    private V value1;
    private K key2;
    private V value2;

    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {

                    private K next = key1;

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public Entry<K, V> next() {
                        if (next == key1) {
                            next = key2;
                            return new SimpleEntry<K, V>(key1, value1);
                        } else if (next == key2) {
                            next = null;
                            return new SimpleEntry<K, V>(key2, value2);
                        } else {
                            return null;
                        }
                    }
                };
            }

            @Override
            public int size() {
                return (TinyMap.this.size());
            }

        };
    }

    @Override
    public V get(Object key) {
        if (key.equals(key1)) {
            return value1;
        } else if (key.equals(key2)) {
            return value2;
        } else {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        if (key.equals(key1)) {
            value1 = value;
        } else if (key.equals(key2)) {
            value2 = value;
        } else if (key1 == null) {
            key1 = key;
            value1 = value;
        } else if (key2 == null) {
            key2 = key;
            value2 = value;
        } else {
            throw new TinyMapLimitExceededException();
        }
        return value;
    }

    @Override
    public int size() {
        return (key1 != null ? 1 : 0) + (key2 != null ? 1 : 0);
    }

    public static class TinyMapLimitExceededException extends RuntimeException {
    }
}
