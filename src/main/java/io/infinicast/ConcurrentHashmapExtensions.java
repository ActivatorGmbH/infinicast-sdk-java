package io.infinicast;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashmapExtensions {
    public static <K, V> V getOrAdd(ConcurrentHashMap<K, V> map,K key, V value) {
        V result = map.putIfAbsent(key, value);
        return null == result ? value : result;
    }
}


