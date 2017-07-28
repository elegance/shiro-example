package org.orh.shiro.chapter12.conf;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.orh.shiro.chapter12.realm.UserRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {

    // Realm 实现
    @Autowired
    private UserRealm userRealm;

    // 安全管理器
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    // Shiro 的 Web 过滤器
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login"); // filte中的 登录接口
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        // 拦截器链. 地址映射
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/unauthroized", "authc");

        filterChainDefinitionMap.put("/public/**", "anon"); // 公共资源
        filterChainDefinitionMap.put("/user/**", "user"); // 身份认证/记住密码均ok
        filterChainDefinitionMap.put("/mgt/**", "authc,roles[admin]"); // 身份认证且角色为admin - 不可记住身份

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
