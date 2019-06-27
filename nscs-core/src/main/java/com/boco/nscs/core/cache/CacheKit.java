package com.boco.nscs.core.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Created by CC on 2017/8/30.
 */
public class CacheKit {

    public static Cache.ValueWrapper get(CacheManager cacheManager,String cacheName,String key){
        return  cacheManager.getCache(cacheName).get(key);
    }
    public static String getString(CacheManager cacheManager,String cacheName,String key){
        Cache.ValueWrapper wrapper = cacheManager.getCache(cacheName).get(key);
        if (wrapper!= null){
            return  wrapper.get().toString();
        }
        else
            return "";
    }
    public static void set(CacheManager cacheManager,String cacheName,String key,Object data){
        cacheManager.getCache(cacheName).put(key,data);
    }


}
