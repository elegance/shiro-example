package org.orh.shiro.chapter3;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Assert;
import org.junit.Test;

public class PermissionTest implements BaseTest {

    @Test
    public void isPermitted() {
        login("classpath:shiro-permission.ini", "ou", "123");

        // 判断拥有权限: user:create
        Assert.assertTrue(subject().isPermitted("user:create"));

        // 判断拥有权限：user:update and user:delete
        Assert.assertTrue(subject().isPermittedAll("user:update", "user:delete"));

        // 判断没有权限：user:view
        Assert.assertFalse(subject().isPermitted("user:view"));
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission() {
        login("classpath:shiro-permission.ini", "ou", "123");

        // 断言拥有权限：user:create
        subject().checkPermission("user:create");

        // 断言拥有权限：user:create and user:update
        subject().checkPermissions("user:delete", "user:update");

        // 断言拥有权限：user:view 失败抛出异常
        subject().checkPermission("user:view");
    }
    
    @Test
    public void testWildcardPermission1() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermissions("system:user:update", "system:user:delete");
        subject().checkPermission("system:user:update,delete");
    }
    
    @Test
    public void testWildcardPermission2() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermissions("system:user:create,delete,update,view");
        subject().checkPermission("system:user:*");
        subject().checkPermission("system:user");
    }
    
    @Test
    public void testWhildcardPermission3() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermission("user:view");
        subject().checkPermission("system:user:view");
    }
    
    @Test
    public void testWhildcardPermission4() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermission("user:view:1");
        subject().checkPermission("user:delete,update:1");
        subject().checkPermissions("user:update:1", "user:delete:1");
        subject().checkPermissions("user:update:1", "user:delete:1", "user:view:1");
        subject().checkPermissions("user:auth:1", "user:auth:2");
    }
    
    @Test
    public void testWhildcardPermission5() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermission("menu:view:1");
        subject().checkPermission("organization");
        subject().checkPermission("organization:view");
        subject().checkPermission("organization:view:1");
    }
    @Test
    public void testWhildcardPermission6() {
        login("classpath:shiro-permission.ini", "li", "123");
        
        subject().checkPermission("menu:view:1");
        subject().checkPermission(new WildcardPermission("menu:view:1"));
    }
}
