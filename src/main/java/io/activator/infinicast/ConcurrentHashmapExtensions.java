package io.activator.infinicast;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashmapExtensions {
    public static <K, V> V getOrAdd(K key, V value, ConcurrentHashMap<K,V> map) {
        V result= map.putIfAbsent(key, value);
        return null == result ? value : result;
    }
}


