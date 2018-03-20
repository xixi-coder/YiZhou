package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.sys.Resources;
import models.sys.Role;
import models.sys.RoleResources;
import models.sys.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGONm on 16/8/10.
 */
@Controller("/admin/sys/role")
public class RoleController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSqlStr = " AND company = ? ";
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("role.column"), SqlManager.sql("role.where")));
        } else {
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("role.column"), SqlManager.sql("role.where"), whereSqlStr, params));
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        Role role;
        List<Resources> resources = Lists.newArrayList();
        if (!isSuperAdmin()) {
            resources = Resources.dao.findByUserId(getUserId(), 0);
        } else {
            resources = Resources.dao.findByParent(0, "");
        }

        for (Resources resource : resources) {
            List<Resources> children = Lists.newArrayList();
            List<Resources> tmpchildren = Lists.newArrayList();
            if (!isSuperAdmin()) {
                tmpchildren = Resources.dao.findByUserId(getUserId(), resource.getId());
            } else {
                tmpchildren = Resources.dao.findByParent(resource.getId(), "");
            }

            children.addAll(tmpchildren);
            if (tmpchildren.size() > 0) {
                for (Resources child : tmpchildren) {
                    if (!isSuperAdmin()) {
                        children.addAll(Resources.dao.findByUserId(getUserId(), child.getId()));
                    } else {
                        children.addAll(Resources.dao.findByParent(child.getId(), ""));
                    }
                }
            }
            resource.put("children", children);
        }
        setAttr("resources", resources);
        if (id > 0) {
            role = Role.dao.findById(id);
            setAttr("role", role);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            role = new Role();
            setAttr("role", role);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        final Role role = getModel(Role.class, "role");
        role.setStatus(1);
        String resourceIds = getPara("resourcesIds");
        final List<RoleResources> roleResources = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(resourceIds)) {
            final String[] resources = resourceIds.split(",");
            for (String resource : resources) {
                RoleResources roleResources1 = new RoleResources();
                roleResources1.setResource(Ints.tryParse(resource));
                roleResources.add(roleResources1);
            }
        }
        if (!isSuperAdmin()) {
            role.setCompany(getCompanyId());
        }
        boolean isOk;
        if (role.getId() != null) {
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (!role.update()) {
                        return false;
                    }
                    if (Db.update(SqlManager.sql("roleResources.delByRoleId"), role.getId()) < 0) {
                        return false;
                    }
                    for (RoleResources roleResource : roleResources) {
                        roleResource.setRoleId(role.getId());
                        if (!roleResource.save()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        } else {
            role.setCreateTime(DateTime.now().toDate());
            role.setCreateUser(getUserId());
            role.setCode(DateTime.now().toString("yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(4));
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (!role.save()) {
                        return false;
                    }
                    for (RoleResources roleResource : roleResources) {
                        roleResource.setRoleId(role.getId());
                        if (!roleResource.save()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxError("保存失败！");
        }
    }

    /**
     * 删除角色
     */
    public void del() {
        final int id = getParaToInt("id", 0);
        if (id > 0) {
            final Role user = Role.dao.findById(id);
            //判断是否使用
            if (User.dao.isUseRole(id)) {
                renderAjaxError("角色已经被使用，无法删除！");
                return;
            }
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (user.delete()) {
                        return Db.update(SqlManager.sql("roleResources.delByRoleId"), id) > 0;
                    }
                    return false;
                }
            });
            if (isOk) {
                renderAjaxSuccess("删除成功！");
            } else {
                renderAjaxError("删除失败！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 查询已经存在的资源
     */
    public void resource() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            List<RoleResources> roleResources = RoleResources.dao.findResourcesByRole(id);
            renderAjaxSuccess(roleResources);
        }
    }

    /**
     * 角色配置用户
     */
    @Before(POST.class)
    public void saveUser() {
        final int id = getParaToInt("roleId", 0);
        String userStrs = getPara("userStrs");
        final List<User> users = Lists.newArrayList();
        String[] userStrsArray;
        if (!Strings.isNullOrEmpty(userStrs)) {
            userStrsArray = userStrs.split(",");
            for (String s : userStrsArray) {
                User user = User.dao.findById(s);
                user.setRole(id);
                users.add(user);
            }
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (Db.update(SqlManager.sql("userRole.deleByRole"), id) < 0) {
                    return false;
                } else {
                    for (User userRole : users) {
                        if (!userRole.update()) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        });
        if (isOk) {
            renderAjaxSuccess("授权成功！");
        } else {
            renderAjaxFailure("授权失败！");
        }
    }
}
