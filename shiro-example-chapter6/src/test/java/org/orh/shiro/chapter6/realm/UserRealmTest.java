package org.orh.shiro.chapter6.realm;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Assert;
import org.junit.Test;
import org.orh.shiro.chapter6.BaseTest;

public class UserRealmTest extends BaseTest {

    @Test
    public void tesLoginSuccess() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().isAuthenticated());
    }

    @Test(expected = UnknownAccountException.class)
    public void testLoginFailWithUnknowUsername() {
        login("classpath:shiro.ini", u1.getUsername() + "_evil", password);
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void testLoginFailWithErrorPassword() {
        login("classpath:shiro.ini", u1.getUsername(), password + "_evil");
    }

    @Test(expected = LockedAccountException.class)
    public void testLoginFailWithLocked() {
        login("classpath:shiro.ini", u4.getUsername(), password);
    }

    @Test(expected = ExcessiveAttemptsException.class)
    public void testLoginFailWithLimitRetryCount() {
        for (int i = 1; i <= 5; i++) {
            try {
                login("classpath:shiro.ini", u3.getUsername(), password + "_evil");
            } catch (Exception e) {
            } // ignore incorrectCredentials
        }
        login("classpath:shiro.ini", u3.getUsername(), password + "_evil");
        // 需要清空缓存，或者更换用户，不然错误次数将受其他登录错误的影响
    }

    @Test
    public void testHasRole() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().hasRole("admin"));
    }

    @Test
    public void testNonRole() {
        login("classpath:shiro.ini", u2.getUsername(), password);
        Assert.assertFalse(subject().hasRole("admin"));
    }

    @Test
    public void testHasPermission() {
        login("classpath:shiro.ini", u1.getUsername(), password);
        Assert.assertTrue(subject().isPermittedAll("user:create", "menu:create"));
    }

    @Test
    public void testNonPermission() {
        login("classpath:shiro.ini", u2.getUsername(), password);
        Assert.assertFalse(subject().isPermittedAll("user:create"));
    }

}
