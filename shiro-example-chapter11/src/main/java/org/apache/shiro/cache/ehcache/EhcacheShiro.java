package org.apache.shiro.cache.ehcache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

public class EhcacheShiro<K, V> implements Cache<K, V> {

    private final org.ehcache.Cache<K, V> cache;

    public EhcacheShiro(org.ehcache.Cache<K, V> cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }

        this.cache = cache;
    }

    @Override
    public V get(K key) throws CacheException {
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V previousValue = null;

        while (true) {
            previousValue = cache.get(key);
            if (previousValue == null) {
                if (cache.putIfAbsent(key, value) == null) {
                    break;
                }
            } else {
                if (cache.replace(key, value) != null) {
                    break;
                }
            }
        }

        return previousValue;
    }

    @Override
    public V remove(K key) throws CacheException {
        V previousValue = null;

        while (true) {
            previousValue = cache.get(key);
            if (previousValue == null) {
                break;
            } else {
                if (cache.remove(key, previousValue)) {
                    break;
                }
            }
        }

        return previousValue;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        Iterator<org.ehcache.Cache.Entry<K, V>> iterator = cache.iterator();
        int size = 0;
        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }
        return size;
    }

    @Override
    public Set<K> keys() {
        Set<K> sets = new HashSet<>();
        cache.iterator().forEachRemaining(entry -> {
            sets.add(entry.getKey());
        });
        return sets;
    }

    @Override
    public Collection<V> values() {
        Set<V> sets = new HashSet<>();
        cache.iterator().forEachRemaining(entry -> {
            sets.add(entry.getValue());
        });
        return sets;
    }

}
