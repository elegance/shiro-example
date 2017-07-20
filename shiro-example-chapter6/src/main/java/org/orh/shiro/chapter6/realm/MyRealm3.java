package org.orh.shiro.chapter6.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.orh.shiro.chapter6.entity.User;

public class MyRealm3 implements Realm {

    @Override
    public String getName() {
        return "c";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = User.builder().username("ou").password("123").build();

        return new SimpleAuthenticationInfo(
                user, // 身份 User 类型
                "123", // 凭证
                getName());
    }

}
