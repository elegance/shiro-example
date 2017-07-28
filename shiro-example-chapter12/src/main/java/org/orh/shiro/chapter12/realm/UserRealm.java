package org.orh.shiro.chapter12.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.orh.shiro.chapter12.credentials.RetryLimitHashedCredentialsMatcher;
import org.orh.shiro.chapter12.entity.User;
import org.orh.shiro.chapter12.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    
    @Autowired
    public UserRealm(RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher) {  // 凭证匹配器 -- 指定时间内限定错误重试次数
        this.setCredentialsMatcher(retryLimitHashedCredentialsMatcher);
        this.setCachingEnabled(true);
    }

    /**
     * 授权信息 - authorization
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));
        return authorizationInfo;
    }

    /**
     * 认证信息 - authentication
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException(); // 没找到账号
        }
        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); // 账号锁定
        }
        // 交给 AuthorizingRealm 使用 CredentialsMatcher 进行密码匹配，如果觉得人家的不好，可以在此判断或自定义实现
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), // 用户名
                user.getPassword(), // 密码
                ByteSource.Util.bytes(user.getCredentialsSalt()), // salt = username + salt
                getName());

        return simpleAuthenticationInfo;
    }

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorization() {
        getAuthorizationCache().clear();
    }

    public void clearAllCacheAuthenticatoinInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCacheAuthenticatoinInfo();
        clearAllCachedAuthorization();
    }

}
