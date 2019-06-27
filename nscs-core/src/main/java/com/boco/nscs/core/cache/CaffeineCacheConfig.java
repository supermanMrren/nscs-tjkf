package com.boco.nscs.core.cache;

import cn.hutool.json.JSONUtil;
import com.boco.nscs.core.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
//@ConditionalOnProperty(prefix = "app", name = "cache-enable", havingValue = "true")
@ConditionalOnProperty(prefix = "spring.cahce", name = "type", havingValue = "caffeine")
public class CaffeineCacheConfig  extends CachingConfigurerSupport {
    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;
    //logger
    private static final Logger logger = LoggerFactory.getLogger(CaffeineCacheConfig.class);

    public CaffeineCacheConfig() {
        logger.debug("user cache caffeine");
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getSimpleName());
                sb.append("-");
                sb.append(method.getName());
                for (Object obj : params) {
                    if (obj==null){
                        continue;
                    }
                    if (ToolUtil.isSimpleValueType(obj.getClass())) {
                        sb.append(obj.toString());
                    } else {
                        sb.append(JSONUtil.toJsonStr(obj).hashCode());
                    }
                }
                String str = sb.toString();
                logger.debug("gen key:{}",str);
                return str;
            }
        };
    }

    //结合Caches枚举实现单独设置每个缓存参数
//    @Nullable
//    @Override
//    public CacheManager cacheManager() {
//        logger.info("init CaffeineCache");
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
//        for (Caches c : Caches.values()) {
//            caches.add(new CaffeineCache(c.name(),
//                    Caffeine.newBuilder().recordStats()
//                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
//                            .maximumSize(c.getMaxSize())
//                            .build())
//            );
//        }
//        cacheManager.setCaches(caches);
//        return cacheManager;
//    }

    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        //广告缓存 有效期3600*2
        ad(7200,50),
        //有效期2个小时 , 最大容量1000
        listCustomers(7200, 1000),
        ;

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        //最大數量
        private int maxSize = DEFAULT_MAXSIZE;
        //过期时间（秒）
        private int ttl = DEFAULT_TTL;

        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }
}