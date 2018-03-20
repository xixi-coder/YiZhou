package models.sys;

import annotation.TableBind;
import base.models.BaseRoleResource;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
@TableBind(tableName = "dele_role_resource")
public class RoleResources extends BaseRoleResource<RoleResources> {
    public static RoleResources dao = new RoleResources();

    /**
     * @param roleId
     * @return
     */
    public List<RoleResources> findResourcesByRole(int roleId) {
        return find(SqlManager.sql("roleResources.findResourcesByRole"), roleId);
    }

    /**
     * 判断资源是否使用
     *
     * @param resourceId
     * @return
     */
    public boolean isUseResource(int resourceId) {
        return findFirst(SqlManager.sql("roleResources.isUseResource"), resourceId).getNumber("c").intValue() > 0;
    }
}
