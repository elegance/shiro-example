package org.orh.shiro.chapter6.realm;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.orh.shiro.chapter6.BaseTest;
import org.orh.shiro.chapter6.entity.User;

public class PrincipalCollectionTest extends BaseTest {
    
    @Test
    public void test() {
        // 因为 所有的 realm 中都没有进行验证，相当于 每个 Realm都验证成功了
        login("classpath:shiro-multiRealm.ini", "ou", "123");
        
        Subject subject = subject();
        
        // 获取Primary Principal （即第一个）
        Object primaryPrincipal1 = subject.getPrincipal();

        PrincipalCollection principalCollection = subject.getPrincipals();
        Object primaryPrincipal2 = principalCollection.getPrimaryPrincipal();
        
        // 来瞧瞧，两种不同方式获取的 primaryPrincipal 是否相等
        Assert.assertEquals(primaryPrincipal1, primaryPrincipal2);
        
        // 返回realms的名称 ：a b c
        Set<String> realmNames = principalCollection.getRealmNames();
        System.out.println(realmNames);
        
        // 因为MyRealm1 与 MyRealm2 返回的都是 字符串 “ou” ，所以排重了
        Set<Object> principals = principalCollection.asSet(); // asList 和 asSet 结果一样
        System.out.println(principals);

        // 根据Realm名字获取
        Collection<User> user = principalCollection.fromRealm("c");
        System.out.println(user);
    }

}
