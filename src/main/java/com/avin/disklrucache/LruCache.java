package com.avin.disklrucache;

/**
 * Created by avin on 04/08/15.
 */
public interface LruCache {

    public String get(String key);

    public void put(String key, String value);

    public void purge(String key);

    public void purgeAll();

    public void finish();

}
