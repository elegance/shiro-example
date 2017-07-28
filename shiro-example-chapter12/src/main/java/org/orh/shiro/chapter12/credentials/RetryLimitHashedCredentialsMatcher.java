package org.orh.shiro.chapter12.credentials;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    
    private static final Logger log = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);

    // 认证错误重试次数的缓存：         用户名:错误次数
    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        System.out.println("RetryLimitHashedCredentialsMatcher init。。。。");
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("passwordRetryCache",
                        CacheConfigurationBuilder
                                .newCacheConfigurationBuilder(String.class, AtomicInteger.class, ResourcePoolsBuilder.heap(3000))
                                .withExpiry(Expirations.timeToIdleExpiration(Duration.of(10, TimeUnit.MINUTES)))) // 锁定10分钟
                .build(true);
        passwordRetryCache = cacheManager.getCache("passwordRetryCache", String.class, AtomicInteger.class);

        this.setHashAlgorithmName("md5");
        this.setHashIterations(2);
        this.setStoredCredentialsHexEncoded(true);
    }
    
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        
        // retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }

        
        if (retryCount.incrementAndGet() > 5) { // 重试次数达到了5次
            log.info("Number of retries more than 5 times, will not be matching action, throw exception!");
            throw new ExcessiveAttemptsException();
        }

        log.trace("{} retryCount: {}", username, retryCount.get());

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(username); // clear retry cache
        }
        
        return matches;
    }
}
