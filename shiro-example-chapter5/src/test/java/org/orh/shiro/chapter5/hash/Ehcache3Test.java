package org.orh.shiro.chapter5.hash;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;

/**
 * 更多 ehcache 3 的内容请参考：http://www.ehcache.org/documentation/3.3/
 *
 */
public class Ehcache3Test {

    @Test
    public void programticConfigurationTest() {
        CacheManager cacheManager =
                CacheManagerBuilder
                        .newCacheManagerBuilder().withCache("preConfigured", CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100).build()))
                        .build(true);

        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100).build()));

        preConfigured.put(2L, "hello Ehcache");
        String value1 = preConfigured.get(2L);
        System.out.println(value1);

        myCache.put(1L, "da one");
        String value = myCache.get(1L);
        System.out.println(value);

        cacheManager.removeCache("preConfigured");

        cacheManager.close();
    }

    // xmlConfigurationTest :
    // http://www.ehcache.org/documentation/3.3/getting-started.html#configuring-with-xml

    /**
     * 存储层，与 ehcache 2一样，提供了多层的存储模型heap/non-heap/disk，低频率使用数据向slower tiers，热的高频数据move to faster tiers.
     */
    @Test
    public void storageTiersTest() {
        PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(new File(System.getProperty("java.io.tmpdir"), "myData")))
                .withCache("threeTieredCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                    .heap(10, EntryUnit.ENTRIES) // 堆内存，java Gc 控制
                                    .offheap(1, MemoryUnit.MB) // 非堆内存，不受GC 管控
                                    .disk(20, MemoryUnit.MB, true))) // 磁盘存
                .build(true);

        Cache<Long, String> threeTieredCache = persistentCacheManager.getCache("threeTieredCache", Long.class, String.class);
        threeTieredCache.put(1L, "stillAvaliableAfterRestart");
//        System.out.println(threeTieredCache.get(1L));
        
        persistentCacheManager.close();
    }
    
    /**
     * Expire is configured at Cache level, so start by defining a cache configuration
     * three types of expire:
     * 1. no expire : this means cache mappings will never expire,
     * 2. time-to-live: this means cache mappings will expire after a fixed duration following their creation,
     * 3. time-to-idle: this means cache mappings wile expire after a fixed duration following the time they were last accessed.
     * 
     * in additional, you can defining CustomerExpire
     * @throws InterruptedException 
     */
    @Test
    public void expireTest() throws InterruptedException {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        // use generally:

        // dictionary cache is never expire

        // token is "time-to-live"
        cacheManager.createCache("token",
                CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(20, TimeUnit.SECONDS)))); // suppose
                                                                                                           // ttl=20s

        // session is "time-to-idle"
        cacheManager.createCache("session",
                CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))
                        .withExpiry(Expirations.timeToIdleExpiration(Duration.of(10, TimeUnit.SECONDS)))); // suppose
                                                                                                           // tti=10s

        Cache<Long, String> tokenCache = cacheManager.getCache("token", Long.class, String.class);
        Cache<Long, String> sessionCache = cacheManager.getCache("session", Long.class, String.class);

        tokenCache.put(1L, UUID.randomUUID().toString());
        LocalDateTime tokenStart = LocalDateTime.now();

        sessionCache.put(1L, UUID.randomUUID().toString());
        
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            while (true) {
                String val = tokenCache.get(1L);
                long liveTime = java.time.Duration.between(tokenStart, LocalDateTime.now()).abs().getSeconds();

                System.out.printf("token: %s , live time : %s seconds!\n", val, liveTime);
                if (val == null) {
                    countDownLatch.countDown();
                    break;
                }
                sleepSeconds(5);
            }
        }, "tokenCache-access").start();

        new Thread(() -> {
            int iterationSleep = 2;
            LocalDateTime accessStart = LocalDateTime.now();

            while (true) {
                String val = sessionCache.get(1L);
                iterationSleep += 2; // 每次延长 2 s 来作测试

                long ago = java.time.Duration.between(accessStart, LocalDateTime.now()).abs().getSeconds();

                System.out.printf("session: %s , last accessed at %s seconds ago\n", val, ago);
                if (val == null) {
                    countDownLatch.countDown();
                    break;
                }
                accessStart = LocalDateTime.now();
                sleepSeconds(iterationSleep);
            }
        }, "sessionCache-access").start();
        
        countDownLatch.await();
    }

    private void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
