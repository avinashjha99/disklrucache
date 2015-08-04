package com.avin.disklrucache;

/**
 * Created by avin on 04/08/15.
 */
public class LruCacheFactory {


    public static LruCache getLruCache(){
         return LruCacheImpl.getInstance();
    }

}
