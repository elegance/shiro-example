package org.orh.shiro.chapter2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class LoginLogoutTest {

    @Test
    public void testAuthentication() {
 // 1. 得到一个根据 ini 创建的 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        // 2. 得到 SecurityManager 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到 Subject 及 创建用户名/密码身份验证Token
        Subject subject = SecurityUtils.getSubject(); // 此时 subject 还是一个未经 认证的subject
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");

        // 4. 登录
        try {
            subject.login(token); // subject.login 传入的参数是：AuthenticationToken，定义了
                                  // getPrincipal/getCredentials 方法，UsernamePasswordToken对其的实现其实就是返回用户名、密码

            Assert.assertEquals(true, subject.isAuthenticated()); // 断言用户已经 经过了 认证，即已经登录了
        } catch (AuthenticationException e) {
            // 5. 身份认证失败
            e.printStackTrace();
        }


        // 6. 退出登录
        subject.logout();
    }
    
    @Test
    public void testCustomRealm() {
        // 1. 得到一个根据 ini 创建的 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");

        // 2. 得到 SecurityManager 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到 Subject 及 创建用户名/密码身份验证Token
        Subject subject = SecurityUtils.getSubject(); // 此时 subject 还是一个未经 认证的subject
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");

        // 4. 登录
        try {
            subject.login(token); // subject.login 传入的参数是：AuthenticationToken，定义了
                                  // getPrincipal/getCredentials 方法，UsernamePasswordToken对其的实现其实就是返回用户名、密码

            Assert.assertEquals(true, subject.isAuthenticated()); // 断言用户已经 经过了 认证，即已经登录了
        } catch (AuthenticationException e) {
            // 5. 身份认证失败
            e.printStackTrace();
        }


        // 6. 退出登录
        subject.logout();
    }
    
    @Test
    public void testCustomMultiRealm() {
        // 1. 得到一个根据 ini 创建的 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-multi-realm.ini");

        // 2. 得到 SecurityManager 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到 Subject 及 创建用户名/密码身份验证Token
        Subject subject = SecurityUtils.getSubject(); // 此时 subject 还是一个未经 认证的subject
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");

        // 4. 登录
        try {
            subject.login(token); // subject.login 传入的参数是：AuthenticationToken，定义了
                                  // getPrincipal/getCredentials 方法，UsernamePasswordToken对其的实现其实就是返回用户名、密码

            Assert.assertEquals(true, subject.isAuthenticated()); // 断言用户已经 经过了 认证，即已经登录了
        } catch (AuthenticationException e) {
            // 5. 身份认证失败
            e.printStackTrace();
        }


        // 6. 退出登录
        subject.logout();
    }
    
    /**
     *  参照：shiro-jdbc-realm.ini ：
     *  org.apache.shiro.realm.jdbc.JdbcRealm 源码中可以看到默认会依赖：users、user_roles、roles_permissions 三个表
     *  这里使用的  h2 作为嵌入的数据库，你可以将 shiro.sql 在  h2 中执行 - h2 工具可以从这里下载：http://www.h2database.com/html/main.html
     */
    @Test
    public void testJDBCRealm() {
        // 1. 得到一个根据 ini 创建的 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");

        // 2. 得到 SecurityManager 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到 Subject 及 创建用户名/密码身份验证Token
        Subject subject = SecurityUtils.getSubject(); // 此时 subject 还是一个未经 认证的subject
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");

        // 4. 登录
        try {
            subject.login(token); // subject.login 传入的参数是：AuthenticationToken，定义了
                                  // getPrincipal/getCredentials 方法，UsernamePasswordToken对其的实现其实就是返回用户名、密码

            Assert.assertEquals(true, subject.isAuthenticated()); // 断言用户已经 经过了 认证，即已经登录了
        } catch (AuthenticationException e) {
            // 5. 身份认证失败
            e.printStackTrace();
        }


        // 6. 退出登录
        subject.logout();
    }
    
    @After
    public void tearDown() {
        ThreadContext.unbindSubject(); //退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }
}
