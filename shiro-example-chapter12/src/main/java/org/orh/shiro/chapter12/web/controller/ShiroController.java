package org.orh.shiro.chapter12.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.orh.shiro.chapter12.entity.Permission;
import org.orh.shiro.chapter12.entity.Role;
import org.orh.shiro.chapter12.entity.User;
import org.orh.shiro.chapter12.service.PermissionService;
import org.orh.shiro.chapter12.service.RoleService;
import org.orh.shiro.chapter12.service.UserService;
import org.orh.shiro.chapter12.web.bean.RetCode;
import org.orh.shiro.chapter12.web.bean.RetMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 无页面应用，秉承一个策略，后台只返回 json 文本信息，不做跳转页面的，不然思维容易混乱
 */
@RestController
public class ShiroController {

    private static final Logger log = LoggerFactory.getLogger(ShiroController.class);

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    RoleService roleService;

    @RequestMapping("/")
    public RetMsg index() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return RetMsg.success("首页-欢迎:" + subject.getPrincipal());
        }
        return RetMsg.success("首页：尚未进行登录认证。");
    }

    @RequestMapping("/unauthorized")
    public ResponseEntity<RetMsg> unauthorized(HttpServletRequest req) {

        return new ResponseEntity<>(RetMsg.error(RetCode.UNAUTHORIZED_ERR), HttpStatus.UNAUTHORIZED);
    }

    // 使用默认的 authc 登录请求注意：让人混淆的几种情况
    // 1. 未登录认证时，shiro 的 FormAuthenticationFilter 会自动执行登录
    // 1.1 如果登录成功，则会跳转至 "successUrl"，默认是 "/"
    // 1.2 如果登录失败，这回跳转至 "loginUrl"，这里这是 "/login"，注意跳转将会携带客户端的请求方式，即login是POST请求，则会POST跳转至改地址
    // 2. 已经登录过时
    // 2.1 此时，FormAuthenticationFilter 不会处理，会按客户端的方式请求地址

    @RequestMapping("/login")
    public ResponseEntity<RetMsg> login(HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            return new ResponseEntity<>(RetMsg.success("您已经成功登录，请勿重复登录!"), HttpStatus.OK);
        } else {
            String errorClassName = (String) req.getAttribute("shiroLoginFailure");
            if (UnknownAccountException.class.getName().equals(errorClassName)) {
                // 正常的登录：HTTP 状态码返回OK，消息体内代码表示错误，表示登录未成功
                return new ResponseEntity<>(RetMsg.error("用户名/密码错误!"), HttpStatus.OK);
            } else if (IncorrectCredentialsException.class.getName().equals(errorClassName)) {
                // 正常的登录：HTTP 状态码返回OK，消息体内代码表示错误，表示登录未成功
                return new ResponseEntity<>(RetMsg.error("用户名/密码错误!"), HttpStatus.OK);
            } else if (ExcessiveAttemptsException.class.getName().equals(errorClassName)) {
                return new ResponseEntity<>(RetMsg.error("错误次数过多!"), HttpStatus.OK);
            } else if (errorClassName != null) {
                // 正常的登录：HTTP 状态码返回OK，消息体内代码表示错误，表示登录未成功
                log.info("login failed:" + errorClassName);
                return new ResponseEntity<>(RetMsg.error("未知密码错误!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(RetMsg.error("未登录或会话已超时！"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/public/init")
    public RetMsg init() {
        String password = "123";

        Permission p1 = Permission.builder().description("用户新增模块").permission("user:create").build();
        Permission p2 = Permission.builder().description("用户修改模块").permission("user:update").build();
        Permission p3 = Permission.builder().description("菜单新增模块").permission("menu:create").build();
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);

        // 2、新增角色
        Role r1 = Role.builder().role("admin").description("管理员").build();
        Role r2 = Role.builder().role("user").description("用户").build();
        roleService.createRole(r1);
        roleService.createRole(r2);

        // 3、关联 角色-权限
        roleService.correlationPermissions(r1.getId(), p1.getId(), p2.getId(), p3.getId());
        roleService.correlationPermissions(r2.getId(), p1.getId(), p2.getId());

        // 4、新增用户
        User u1 = User.builder().username("ou").password(password).build();
        User u2 = User.builder().username("li").password(password).build();
        User u3 = User.builder().username("wu").password(password).build();
        User u4 = User.builder().username("wang").password(password).locked(Boolean.TRUE).build();

        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);

        // 5、关联 用户-角色
        userService.correlationRoles(u1.getId(), r1.getId());
        return RetMsg.success();
    }
}
