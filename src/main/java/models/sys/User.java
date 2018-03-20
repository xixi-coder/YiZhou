package models.sys;

import annotation.TableBind;
import base.Constant;
import base.models.BaseUser;
import com.google.common.base.Strings;
import kits.DaoKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
@TableBind(tableName = "dele_user")
public class User extends BaseUser<User> {
    public static User dao = new User();

    /**
     * 通过用户名查询用户
     *
     * @param userName
     * @return
     */
    public User findByUserName(String userName) {
        return findFirst(SqlManager.sql("user.findByUserName"), userName);
    }

    public int findByCount(String userName, int userId) {
        return findFirst(SqlManager.sql("user.findByCount"), userName, userId).getNumber("c").intValue();
    }

    /**
     * 查询数据返回给ztree
     *
     * @param name
     * @return
     */
    public List<User> findByLikeName(String name, int roleId) {
        String strSql;
        if (!Strings.isNullOrEmpty(name)) {
            strSql = SqlManager.sql("user.findByLikeName") + " AND (" +
                    "        name LIKE ? " +
                    "        OR username LIKE ? " +
                    "        OR phone LIKE ?)";
            return find(strSql, roleId, DaoKit.AllLike(name), DaoKit.AllLike(name), DaoKit.AllLike(name));
        } else {
            return find(SqlManager.sql("user.findByLikeName"), roleId);
        }

    }

    /**
     * 查询拥有该角色的用户
     *
     * @param roleId
     * @return
     */
    public List<User> findByRoleId(int roleId) {
        return find(SqlManager.sql("userRole.findByRoleId"), roleId);
    }

    /**
     * 查询角色是否使用
     *
     * @param roleId
     * @return
     */
    public boolean isUseRole(int roleId) {
        return findFirst(SqlManager.sql("user.isUseRole"), roleId).getNumber("c").intValue() > 0;
    }

    /**
     * 查询公司的用户
     *
     * @param companyId
     * @return
     */
    public List<User> findByCompany(int companyId) {
        return find(SqlManager.sql("user.findByCompany"), companyId);
    }

    /**
     * 查询会员管理的用户
     *
     * @param loginId
     * @return
     */
    public User findByLoingId(int loginId) {
        return findFirst(SqlManager.sql("user.findByLoingId"), loginId);
    }

    /**
     * @return
     */
    public Integer getType() {
        if (super.getType() == null) {
            return Constant.UserType.NORMAL;
        } else {
            return super.getType();
        }
    }

    /**
     * 查询联盟用户
     *
     * @param companyId
     * @param union
     * @return
     */
    public List<User> findByCompanyAndType(int companyId, int union) {
        return find(SqlManager.sql("user.findByCompanyAndType"), companyId, union);
    }
}
