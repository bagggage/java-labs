package com.example.lab.cache;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Cache<K, V> {
    public interface ValueAccessor<T> {
        T access();
    }

    public class CacheEntry {
        private K key;
        
        public CacheEntry(K key) {
            this.key = key;
        }

        public V orDefault(V defaultValue) {
            return content.getOrDefault(key, defaultValue);
        }

        public V orPut(ValueAccessor<V> valueAccessor) {
            if (isCached(key)) {
                return content.get(key);
            }

            V value = valueAccessor.access();

            if (content.size() >= capacity) {
                content.remove(content.keySet().iterator().next());
            }

            content.put(key, value);

            return value;
        }
    }

    @Getter
    private Map<K, V> content;

    @Getter
    private int capacity;

    public Cache(int maxCacheSize) {
        content = HashMap.newHashMap(maxCacheSize);
        capacity = maxCacheSize;
    }

    public int size() {
        return content.size();
    }

    public void clear() {
        content.clear();
    }

    public boolean put(K key, V value) {
        if (!content.containsKey(key)) {
            return false;
        }

        content.put(key, value);

        return true;
    }

    public boolean uncache(K key) {
        if (!content.containsKey(key)) {
            return false;
        }

        content.remove(key);

        return true;
    }

    public boolean isCached(K key) {
        return content.containsKey(key);
    }

    public CacheEntry tryGet(K key) {
        return new CacheEntry(key);
    }
}
