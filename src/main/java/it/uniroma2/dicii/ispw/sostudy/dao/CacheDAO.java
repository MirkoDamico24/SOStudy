package it.uniroma2.dicii.ispw.sostudy.dao;


import java.util.HashMap;

public class CacheDAO<T,R>{
    private final HashMap<T, R> cache;

    public CacheDAO() {
        cache = new HashMap<>();
    }

    public void addToCache(T key, R value){
        cache.put(key, value);
    }

    public boolean containsKey(T key){
        return cache.containsKey(key);
    }

    public R getFromCache(T key){
        return cache.get(key);
    }

    public void clearCache(){
        cache.clear();
    }

    public void removeFromCache(T key){
        cache.remove(key);
    }


}
