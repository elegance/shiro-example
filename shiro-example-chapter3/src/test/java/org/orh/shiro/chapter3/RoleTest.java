package org.orh.shiro.chapter3;

import java.util.Arrays;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Assert;
import org.junit.Test;

public class RoleTest implements BaseTest {

    @Test
    public void hasRole() {
        login("classpath:shiro-role.ini", "ou", "123");

        // 判断拥有角色：role1
        Assert.assertTrue(subject().hasRole("role1"));

        // 判断拥有角色：role1 and role2
        Assert.assertTrue(subject().hasAllRoles(Arrays.asList("role1", "role2")));

        // 判断拥有角色：role1 and role2 and !role3
        boolean[] result = subject().hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertTrue(result[0] && result[1] && !result[2]);
    }
    
    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:shiro-role.ini", "yang", "123");
        
        // 断言有角色：role1
        subject().checkRole("role1");
        
        // 断言有角色：role1 and role3  （失败抛出异常）
        subject().checkRoles("role1", "role3");
    }
}
