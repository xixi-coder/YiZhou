package models.sys;

import annotation.TableBind;
import base.models.BaseRole;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
@TableBind(tableName = "dele_role")
public class Role extends BaseRole<Role> {
    public static Role dao = new Role();

    public List<Role> findAll() {
        return find(SqlManager.sql("role.findAll"));
    }
}
