package org.orh.shiro.chapter2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticatorTest {
    // 三个 realm ：
    // myRealm1: 用户名密码为 ou/123时成功，且返回身份/凭证为 ou/123
    // myRealm2: 用户名密码为 yang/123时成功，且返回身份/凭证为 yang/123
    // myRealm3: 用户名密码为 ou/123时成功，且返回身份/凭证为 ou@163.com/123， 和myRealm1不同的是返回的身份变了
    
    /**
     * 所有都需成功，返回所有认证的信息
     */
    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-all-success.ini"); // 其中 realms=myRealm1,myRealm3 所以 都能成功 (ou/123)
        
        Subject subject = SecurityUtils.getSubject();
        
        // 得到一个身份集合，其中 包含了 Realm 验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());
    }
    
    /**
     * 所有都需成功，出现失败则抛出异常
     */
    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {
        login("classpath:shiro-authenticator-all-fail.ini"); // 其中 realms=myRealm1,myRealm2 ，其中 myRealm2 会失败，因为其认证 yang/123
    }
    
    /**
     * 只要一个成功，返回所有认证成功的信息
     */
    @Test
    public void testAtLeastOneSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastOne-success.ini"); // 其中 realms=myRealm1,myRealm2,myRealm3，能成两个
        
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size());
        
    }
    
    /**
     * 有首次成功则返回成功的认证信息，否则抛出异常
     */
    @Test
    public void testFirstSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-first-success.ini"); // 其中 realms=myRealm1,myRealm2,myRealm3，能匹配上2个，实际上首次匹配一个后就会返回
        
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(1, principalCollection.asList().size()); // 区别与上一个方法的是这里是 1
    }
    
    /**
     * 自定义实现的 authenticationStrategy：AtLeastTwoAuthenticatorStrategy
     * 最少两个成功则代表成功，返回全部成功的信息
     */
    @Test
    public void testAtLeastTwoSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-atLeastTwo-success.ini");// 其中 realms=myRealm1,myRealm2,myRealm3,myRealm4，能匹配上3个
        
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(2, principalCollection.asList().size()); // 因为身份集合中 myRealm1与myRealm4是一样所以只算1个，myRealm2 也算一个，另外myReal3是失败的
    }
    
    /**
     * 自定义策略，只能有一个成功，如果超过一个将会跑错
     */
    @Test
    public void testOnlyOneStrategyWithSuccess() {
        login("classpath:shiro-authenticator-onlyone-success.ini"); // 其中 realms=myRealm1,myRealm2   => 一对一错 能满足策略的要求
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        Assert.assertEquals(1, principalCollection.asList().size());
    }
    
    
    // 1. 首选通用化登录逻辑
    private void login(String configFile) {
        // 1. 得到一个根据 ini 创建的 SecurityManager 工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);

        // 2. 得到 SecurityManager 并绑定给 SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // 3. 得到 Subject 及 创建用户名/密码身份验证Token
        Subject subject = SecurityUtils.getSubject(); // 此时 subject 还是一个未经 认证的subject
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "123");

        // 4. 登录
        subject.login(token); // subject.login 传入的参数是：AuthenticationToken，定义了
                              // getPrincipal/getCredentials
                              // 方法，UsernamePasswordToken对其的实现其实就是返回用户名、密码

    }

    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }
}
