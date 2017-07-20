package org.orh.shiro.chapter6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.orh.shiro.chapter6.JdbcTemplateUtils;
import org.orh.shiro.chapter6.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 无接口，直接写实现类
 */
public class UserDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    public User createUser(final User user) {
        final String sql = "insert into sys_users(username, password, salt, locked) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getSalt());
                ps.setBoolean(4, user.getLocked());
                return ps;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    public void updateUser(User user) {
        String sql = "update sys_users set username=?, password=?, salt=?, locked=? where id=?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getSalt(), user.getLocked(), user.getId());
    }

    public void deleteUser(Long userId) {
        String sql = "delete from sys_users where id=?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * 关联用户与角色
     */
    public void correlationRoles(Long userId, Long... roleIds) {
        List<Object[]> batchParamters = Stream.of(roleIds).map(roleId -> {
            return new Object[] {userId, roleId};
        }).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into sys_users_roles(user_id, role_id) values(?,?)", batchParamters);
    }

    /**
     * 取消用户的一些角色
     */
    public void uncorrelationRoles(Long userId, Long... roleIds) {
        List<Object[]> batchParamters = Stream.of(roleIds).map(roleId -> {
            return new Object[] {userId, roleId};
        }).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("delete from sys_users_roles where user_id = ? and role_id = ?", batchParamters);
    }

    public User findOne(Long userId) {
        List<User> users = jdbcTemplate.query("select id, username, password, salt, locked from sys_users where id=?",
                new BeanPropertyRowMapper<User>(), userId);
        return users.stream().findFirst().orElse(null);
    }

    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query("select id, username, password, salt, locked from sys_users where username=?",
                new BeanPropertyRowMapper<User>(User.class), username);
        return users.stream().findFirst().orElse(null);
    }

    public Set<String> findRoles(String username) {
        String sql =
                "select role from sys_users u, sys_roles r,sys_users_roles ur where u.username=? and u.id=ur.user_id and r.id=ur.role_id";
        return new HashSet<String>(jdbcTemplate.queryForList(sql, String.class, username));
    }

    public Set<String> findPermissions(String username) {
        String sql =
                "select permission from sys_users u, sys_roles r, sys_permissions p, sys_users_roles ur, sys_roles_permissions rp where u.username=? and u.id=ur.user_id and r.id=ur.role_id and r.id=rp.role_id and p.id=rp.permission_id";
        return new HashSet<String>(jdbcTemplate.queryForList(sql, String.class, username));
    }
}
