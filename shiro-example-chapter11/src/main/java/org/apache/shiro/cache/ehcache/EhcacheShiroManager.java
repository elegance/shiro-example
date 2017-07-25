package org.apache.shiro.cache.ehcache;

import java.net.URL;

import org.apache.shiro.ShiroException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义实现 Ehcache3 与 shiro 的桥接
 * 
 */
public class EhcacheShiroManager implements CacheManager, Initializable, Destroyable {

    private static final Logger log = LoggerFactory.getLogger(EhcacheShiroManager.class);

    org.ehcache.CacheManager manager;

    private boolean cacheManagerImplicitlyCreated = false;

    private String cacheManagerConfigFile;

    private XmlConfiguration cacheConfiguration = null;

    private String cacheTemplateName = "shiroCache";

    public String getCacheTemplateName() {
        return cacheTemplateName;
    }

    public void setCacheTemplateName(String cacheTemplateName) {
        this.cacheTemplateName = cacheTemplateName;
    }

    public boolean isCacheManagerImplicitlyCreated() {
        return cacheManagerImplicitlyCreated;
    }

    public void setCacheManagerImplicitlyCreated(boolean cacheManagerImplicitlyCreated) {
        this.cacheManagerImplicitlyCreated = cacheManagerImplicitlyCreated;
    }

    public String getCacheManagerConfigFile() {
        return cacheManagerConfigFile;
    }

    public void setCacheManagerConfigFile(String cacheManagerConfigFile) {
        this.cacheManagerConfigFile = cacheManagerConfigFile;
    }

    public EhcacheShiroManager() {}

    public org.ehcache.CacheManager getCacheManager() {
        return manager;
    }

    public void setCacheManager(org.ehcache.CacheManager manager) {
        this.manager = manager;
    }

    @Override
    public void destroy() throws Exception {
        if (cacheManagerImplicitlyCreated) {
            org.ehcache.CacheManager cacheMgr = getCacheManager();
            cacheMgr.close();
            cacheManagerImplicitlyCreated = false;
        }
    }

    @Override
    public void init() throws ShiroException {
        ensureCacheManager();
    }

    private org.ehcache.CacheManager ensureCacheManager() {
        if (this.manager == null) {
            log.debug("cacheManager property not set.  Constructing CacheManager instance... ");
        }

        if (cacheManagerConfigFile != null) {
            final URL url = getClass().getResource(cacheManagerConfigFile);

            cacheConfiguration = new XmlConfiguration(url);
            manager = CacheManagerBuilder.newCacheManager(cacheConfiguration);
            manager.init();
        } else {
            manager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        }
        log.trace("instantiated Ehcache CacheManager instance.");
        cacheManagerImplicitlyCreated = true;
        log.debug("implicit cacheManager created successfully.");
        return this.manager;
    }

    private org.ehcache.Cache<Object, Object> createCache(String name) {
        if (cacheConfiguration != null) {
            CacheConfigurationBuilder<Object, Object> configBuilder = null;
            try {
                configBuilder =
                        cacheConfiguration.newCacheConfigurationBuilderFromTemplate("cacheConfigTemplateName", Object.class, Object.class);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new CacheException(e);
            }
            return ensureCacheManager().createCache(name, configBuilder);
        } else {
            return ensureCacheManager().createCache(name,
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(2000)));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.trace("Acquiring EhCache instance named [" + name + "]");

        org.ehcache.Cache<Object, Object> cache = ensureCacheManager().getCache(name, Object.class, Object.class);

        if (cache == null) {
            log.info("Cache with name {} does not yet exist.  Creating now.", name);
            cache = createCache(name);
        } else {
            log.info("Using existing EhcacheShiro named [{}]", name);
        }
        return (Cache<K, V>) new EhcacheShiro<Object, Object>(cache);
    }

}
