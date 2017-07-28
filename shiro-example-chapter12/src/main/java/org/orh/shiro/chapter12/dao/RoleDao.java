package org.orh.shiro.chapter12.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.orh.shiro.chapter12.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Role createRole(final Role role) {
        final String sql = "insert into sys_roles(role, description, available) values(?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"});
                psst.setString(1, role.getRole());
                psst.setString(2, role.getDescription());
                psst.setBoolean(3, role.getAvailable());
                return psst;
            }
        }, keyHolder);
        role.setId(keyHolder.getKey().longValue());

        return role;
    }

    public void deleteRole(Long roleId) {
        // 首先把和role关联的相关表数据删掉
        String sql = "delete from sys_users_roles where role_id=?";
        jdbcTemplate.update(sql, roleId);

        sql = "delete from sys_roles where id=?";
        jdbcTemplate.update(sql, roleId);
    }

    /**
     * 关联角色与权限
     */
    public void correlationPermissions(Long roleId, Long... permissionIds) {
        List<Object[]> batchParamters = Stream.of(permissionIds).map(permissionId -> {
            return new Object[] {roleId, permissionId};
        }).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into sys_roles_permissions(role_id, permission_id) values(?,?)", batchParamters);
    }

    /**
     * 取消角色与权限的关联
     */
    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
        List<Object[]> batchParamters = Stream.of(permissionIds).map(permissionId -> {
            return new Object[] {roleId, permissionId};
        }).collect(Collectors.toList());

        jdbcTemplate.batchUpdate("delete from sys_roles_permissions where role_id=? and permission_id=?", batchParamters);
    }
}
