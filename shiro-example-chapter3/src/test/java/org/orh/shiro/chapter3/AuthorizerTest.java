package org.orh.shiro.chapter3;

import org.junit.Assert;
import org.junit.Test;

public class AuthorizerTest implements BaseTest {

    @Test
    public void testIsPermmited() {
        login("classpath:shiro-authorizer.ini", "ou", "123");

        // 判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));

        // 通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2")); // 新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8")); // 查看权限
        Assert.assertTrue(subject().isPermitted("+user1+10")); // 新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4")); // 没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view")); // 通过MyRolePermissionResolver 解析得到权限
    }

    @Test
    public void testIsPermitted2() {
        login("classpath:shiro-jdbc-authorizer.ini", "ou", "123");

        // 判断拥有权限：user:create
        Assert.assertTrue(subject().isPermitted("user1:update"));
        Assert.assertTrue(subject().isPermitted("user2:update"));
        // 通过二进制位的方式表示权限
        Assert.assertTrue(subject().isPermitted("+user1+2"));// 新增权限
        Assert.assertTrue(subject().isPermitted("+user1+8"));// 查看权限
        Assert.assertTrue(subject().isPermitted("+user2+10"));// 新增及查看

        Assert.assertFalse(subject().isPermitted("+user1+4"));// 没有删除权限

        Assert.assertTrue(subject().isPermitted("menu:view"));// 通过MyRolePermissionResolver解析得到的权限
    }
}
