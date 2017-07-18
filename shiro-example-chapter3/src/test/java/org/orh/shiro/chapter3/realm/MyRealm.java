package org.orh.shiro.chapter3.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.orh.shiro.chapter3.permission.BitPermission;

public class MyRealm extends AuthorizingRealm {

    // 根据用户身身份获取授权信息
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("role1");
        authorizationInfo.addRole("role2");
        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
        
        authorizationInfo.addStringPermission("+user2+10");
        authorizationInfo.addStringPermission("user2:*");
        return authorizationInfo;
    }

    // 表示获取身份认证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
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
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    
}
