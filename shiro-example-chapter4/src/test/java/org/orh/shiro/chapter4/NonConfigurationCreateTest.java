package org.orh.shiro.chapter4;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;

public class NonConfigurationCreateTest {

    /**
     * 此 等价于 shiro-config.ini 结合 ConfigurationTest.java
     */
    @Test
    public void test() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        
        // 设置 authenticator
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);
        
        // 设置 authorizer
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);
        
        // 设置Realm 
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:file:~/.h2/shiro-jdbc-authorizer");  // 注意：这里依赖的是 chapter3 中的 shiro-jdbc-authorizer 数据库
        ds.setValidationQuery("select 1");
        // h2 嵌入式的数据库可以不设定用户名、密码

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);
        securityManager.setRealm(jdbcRealm);
        
        // 将 SecurityManager 设置到 SecurityUtils 方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");
        subject.login(token);
        
        Assert.assertTrue(subject.isAuthenticated());
    }
}
