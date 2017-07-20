package org.orh.shiro.chapter6.service;

import org.orh.shiro.chapter6.dao.PermissionDao;
import org.orh.shiro.chapter6.entity.Permission;

public class PermissionService {

    private PermissionDao permissionDao = new PermissionDao();

    public Permission createPermission(Permission permission) {
        return permissionDao.createPermission(permission);
    }

    public void deletePermission(Long permissionId) {
        permissionDao.deletePermission(permissionId);
    }
}
