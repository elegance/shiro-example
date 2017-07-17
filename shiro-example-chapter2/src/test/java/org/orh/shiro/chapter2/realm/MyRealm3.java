package org.orh.shiro.chapter2.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MyRealm3 implements Realm {

    public String getName() {
        return "myrealm3";
    }

    public boolean supports(AuthenticationToken token) {
        // 仅支持 UsernamePasswordToken 类型的 token
        return token instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 得到用户名、密码
        String username = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());
        
        if (!"ou".equals(username)) {
            throw new UnknownAccountException(); // 用户名错误
        }
        
        if (!"123".equals(password)) {
            throw new IncorrectCredentialsException();
        }
        
        System.out.println(getName() + " authenticated success!");
        
        // 如果身份验证成功，返回一个Authentication 实现
        return new SimpleAuthenticationInfo(username + "@163.com", password, getName());
    }

}
