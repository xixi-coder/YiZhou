package controllers.admin.sys;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import kits.Md5Kit;
import kits.StringsKit;
import models.company.Company;
import models.driver.DriverInfo;
import models.sys.Role;
import models.sys.User;
import plugin.sqlInXml.SqlManager;

/**
 * Created by BOGONm on 16/8/10.
 */
@Controller("/admin/sys/user")
public class UserController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("user.column"), SqlManager.sql("user.where")));
        } else {
            if (getUserType() == Constant.UserType.NORMAL) {
                String where = StringsKit.replaceSql(SqlManager.sql("user.where"), " AND company = ? ");
                List<Object> params = Lists.newArrayList();
                params.add(getCompanyId());
                renderJson(dataTable(SqlManager.sql("user.column"), where, params));
            }
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        int loginId = getParaToInt(1, 0);
        User user;
        List<Company> companies = Company.dao.findByEnable();
        setAttr("company", companies);
        List<Role> roles = Role.dao.findAll();
        setAttr("roles", roles);
        if (id > 0) {
            user = User.dao.findById(id);
            setAttr("user", user);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            user = new User();
            if (loginId != 0) {
                user = User.dao.findByLoingId(loginId);
                if (user == null) {
                    user = new User();
                    DriverInfo driverInfo = DriverInfo.dao.findByLoginId(loginId);
                    user.setPhone(driverInfo.getPhone());
                    user.setName(driverInfo.getRealName());
                    user.setCompany(driverInfo.getCompany());
                    user.setType(Constant.UserType.UNION);//联盟的用户
                }
            } else {
                user.setType(Constant.UserType.NORMAL);
            }
            user.setLoginId(loginId);
            setAttr("user", user);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        User user = getModel(User.class, "user");
        if (user.getId() != null) {
            if (user.update()) {
                renderAjaxSuccess("保存成功！");
            } else {
                renderAjaxError("保存失败！");
            }
        } else {
            if (!isSuperAdmin()) {
                user.setCompany(getCompanyId());
            }
            String password = getPara("password");
            String repassword = getPara("repassword");
            if (Strings.isNullOrEmpty(password)) {
                renderAjaxError("密码不能为空！");
                return;
            }
            if (!StringUtils.equals(password, repassword)) {
                renderAjaxError("确认确认密码不一致！");
                return;
            }
            String salt = StringsKit.getSalt();
            user.setSalt(salt);
            String dbPassword = Md5Kit.MD5(password + salt);
            user.setPassword(dbPassword);
            user.setStatus(Constant.DataAuditStatus.AUDITOK);
            user.setCreateTime(new Date());
            if (user.save()) {
                renderAjaxSuccess("新增成功！");
            } else {
                renderAjaxError("新增失败！");
            }
        }
    }

    /**
     * 验证用户名的唯一性
     */
    public void validUser() {
        String username = getPara("user.username");
        boolean isEdit = getParaToBoolean("action", false);
        User user;
        Map<String, String> result = Maps.newHashMap();
        if (isEdit) {
            int userId = getParaToInt("user.id", 0);
            if (User.dao.findByCount(username, userId) >= 1) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            user = User.dao.findByUserName(username);
            if (user != null) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);

    }

    /**
     * 修改用户密码
     */
    public void updatepd() {
//        User user = getModel(User.class, "user");
//        if (user.getId() != null) {
//            String oldPw = getPara("oldpassword");
//            user = User.dao.findById(user.getId());
//            if (StringUtils.equals(Md5Kit.MD5(oldPw + user.getSalt()), user.getPassword())) {
//                String password = getPara("password");
//                String repassword = getPara("repassword");
//                if (StringUtils.equals(password, repassword)) {
//                    String salt = StringsKit.getSalt();
//                    String dbPassword = Md5Kit.MD5(password + salt);
//                    user.setSalt(salt);
//                    user.setPassword(dbPassword);
//                    if (user.update()) {
//                        renderAjaxSuccess("密码修改成功！");
//                    } else {
//                        renderAjaxError("密码修改失败！");
//                    }
//                } else {
//                    renderAjaxError("两次输入的密码不一致！");
//                }
//            } else {
//                renderAjaxError("旧密码不正确！");
//            }
//        } else {
//            renderAjaxError("用户不存在！");
//        }
        User ReceiveUser = getModel(User.class, "user");
        Integer userId = ReceiveUser.getId();
        if(userId != null){
            User user = User.dao.findById(userId);
            if(user != null){
                String oldPw = Md5Kit.MD5(getPara("oldpassword")+user.getSalt());
                if(StringUtils.equals(oldPw, user.getPassword())){
                    String password = getPara("password");
                    String repassword = getPara("repassword");
                    if (StringUtils.equals(password, repassword)) {
                        String salt = StringsKit.getSalt();
                        String dbPassword = Md5Kit.MD5(repassword + salt);
                        user.setSalt(salt);
                        user.setPassword(dbPassword);
                        if (user.update()) {
                            renderAjaxSuccess("密码修改成功！");
                        } else {
                            renderAjaxError("密码修改失败！");
                        }
                    } else {
                        renderAjaxError("两次输入的密码不一致！");
                    }
                }
            }else {
                renderAjaxError("用户不存在！");
            }
        }else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 重置密码
     */
    public void repw() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            User user = User.dao.findById(id);
            String salt = StringsKit.getSalt();
            String dbPw = Md5Kit.MD5("123@123" + salt);
            user.setPassword(dbPw);
            user.setSalt(salt);
            if (user.update()) {
                renderAjaxSuccess("重置密码成功！");
            } else {
                renderAjaxError("重置密码失败！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 删除用户
     */
    public void del() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            User user = User.dao.findById(id);
            if (user.delete()) {
                renderAjaxSuccess("删除成功！");
            } else {
                renderAjaxError("删除失败！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 为ztree查询用户
     */
    public void searchZtree() {
        String name = getPara("name");
        int roleId = getParaToInt("roleId", 0);
        if (roleId > 0) {
            List<User> users = User.dao.findByLikeName(name, roleId);
            renderAjaxSuccess(users);
        } else {
            renderAjaxNoData();
        }
    }

    public void driverList() {
        int userId = getParaToInt("userId", 0);
        User user = User.dao.findById(userId);
        int company = user.getCompany();
        if (!isSuperAdmin()) {
            company = getCompanyId();
        }
        String name = getPara("name");
        List<DriverInfo> driverInfos = DriverInfo.dao.findByCreaterAndCompanyForZtree(userId, company, name);
        renderJson(driverInfos);
    }

    public void saveOwnDriver() {
        String driverIds = getPara("driverIds");
        final int userId = getParaToInt("userId", 0);
        if (!Strings.isNullOrEmpty(driverIds)) {
            final String[] driverIdsArray = driverIds.split(",");
            final Object[][] params = new Object[driverIdsArray.length][2];
            for (int i = 0; i < driverIdsArray.length; i++) {
                params[i][0] = userId;
                params[i][1] = driverIdsArray[i];
            }
            boolean isOk = false;
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (Db.update("update dele_driver_info set creater = 0 where creater = ? ", userId) >= 0) {
                        Db.batch(SqlManager.sql("driverInfo.updateCreater"), params, driverIdsArray.length);
                        return true;
                    }
                    return false;
                }
            });
            if (isOk)
                renderAjaxSuccess("操作成功！");
            else
                renderAjaxError("操作失败！");
        } else if (userId != 0) {
            if (Db.update("update dele_driver_info set creater = 0 where creater = ? ", userId) >= 0) {
                renderAjaxSuccess("操作成功！");
            } else {
                renderAjaxError("操作失败！");
            }
        } else {
            renderAjaxError("操作失败！");
        }
    }

    public void disable() {
        int userId = getParaToInt(0, 0);
        if (userId > 0) {
            User user = User.dao.findById(userId);
            if (user.getStatus() == Constant.DataAuditStatus.AUDITOK) {
                user.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            } else {
                user.setStatus(Constant.DataAuditStatus.AUDITOK);
            }
            if (user.update()) {
                renderAjaxSuccess("操作成功！");
            } else {
                renderAjaxError("操作失败！");
            }
        } else {
            renderAjaxError("用户不存在");
        }
    }
}
