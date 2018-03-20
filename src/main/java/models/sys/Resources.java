package models.sys;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseResources;
import com.google.common.base.Strings;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
@TableBind(tableName = "dele_resources")
public class Resources extends BaseResources<Resources> {
    public static Resources dao = new Resources();

    /**
     * 查询用户有的资源
     *
     * @param user
     * @return
     */
    public List<Resources> findByUser(User user, int parent) {
        return find(SqlManager.sql("resources.findByUser"), user.getId(), parent, Constant.MENU);
    }

    /**
     * 通过父级查询子级
     *
     * @param pid
     * @return
     */
    public List<Resources> findByParent(int pid, String style) {
        if (Strings.isNullOrEmpty(style)) {
            return find(SqlManager.sql("resources.findByParent"), pid);
        } else {
            return find(SqlManager.sql("resources.findByParent") + " AND style = ?", pid, style);
        }
    }

    /**
     * 查询用户拥有的资源
     * @param userId
     * @param pid
     * @return
     */
    public List<Resources> findByUserId(int userId, int pid) {
        if (pid == 0) {
            return find(SqlManager.sql("resources.findByUserId"), 0, userId);
        } else {
            return find(SqlManager.sql("resources.findByUserId"), pid, userId);
        }
    }

    /**
     * 排除本身的数量
     *
     * @param pid
     * @return
     */
    public int findByCountExcludedSelf(int pid, String code) {
        return findFirst(SqlManager.sql("resources.findByCountExcludedSelf"), pid, code).getNumber("c").intValue();
    }

    /**
     * 查询数量
     *
     * @param code
     * @return
     */
    public int findByCount(String code) {
        return findFirst(SqlManager.sql("resources.findByCount"), code).getNumber("c").intValue();
    }

    /**
     * 为ztree提供数据
     *
     * @return
     */
    public List<Resources> findForZtreeByStyle() {
        return find(SqlManager.sql("resources.findForZtreeByStyle"));
    }

    public List<Resources> findForZtreeByStyle(int id) {
        return find(SqlManager.sql("resources.findForZtreeByStyleExculedSelf"), id);
    }

    /**
     * 查询用户拥有的权限
     *
     * @param userId
     * @return
     */
    public List<Resources> findCodeByUser(int userId) {
        return find(SqlManager.sql("resources.findCodeByUser"), userId);
    }

    /**
     * 通过地址查询跳转地址
     *
     * @param url
     * @return
     */
    public Resources findByUrl(String url) {
        return findFirst(SqlManager.sql("resources.findByUrl"), url);
    }
}
