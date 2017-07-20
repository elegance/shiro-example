package org.orh.shiro.chapter6;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.junit.Test;
import org.orh.shiro.chapter6.entity.Permission;
import org.orh.shiro.chapter6.entity.Role;
import org.orh.shiro.chapter6.entity.User;
import org.orh.shiro.chapter6.service.PermissionService;
import org.orh.shiro.chapter6.service.RoleService;
import org.orh.shiro.chapter6.service.UserService;

public abstract class BaseTest {
    protected PermissionService permissionService = new PermissionService();
    protected RoleService roleService = new RoleService();
    protected UserService userService = new UserService();

    protected String password = "123";

    protected Permission p1;
    protected Permission p2;
    protected Permission p3;
    protected Role r1;
    protected Role r2;
    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;

    @Before
    public void setUp() {
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_users");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_roles");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_permissions");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_users_roles");
        JdbcTemplateUtils.jdbcTemplate().update("delete from sys_roles_permissions");

        // 1、新增权限
        p1 = Permission.builder().description("用户新增模块").permission("user:create").build();
        p2 = Permission.builder().description("用户修改模块").permission("user:update").build();
        p3 = Permission.builder().description("菜单新增模块").permission("menu:create").build();
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);

        // 2、新增角色
        r1 = Role.builder().role("admin").description("管理员").build();
        r2 = Role.builder().role("user").description("用户").build();
        roleService.createRole(r1);
        roleService.createRole(r2);

        // 3、关联 角色-权限
        roleService.correlationPermissions(r1.getId(), p1.getId(), p2.getId(), p3.getId());
        roleService.correlationPermissions(r2.getId(), p1.getId(), p2.getId());

        // 4、新增用户
        u1 = User.builder().username("ou").password(password).build();
        u2 = User.builder().username("li").password(password).build();
        u3 = User.builder().username("wu").password(password).build();
        u4 = User.builder().username("wang").password(password).locked(Boolean.TRUE).build();
        
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        
        // 5、关联 用户-角色
        userService.correlationRoles(u1.getId(), r1.getId());

    }

    @Test
    public void tearDown() {
        ThreadContext.unbindSubject(); // 退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }

    public void login(String configFile, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);

        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }

    public Subject subject() {
        return SecurityUtils.getSubject();
    }
}
