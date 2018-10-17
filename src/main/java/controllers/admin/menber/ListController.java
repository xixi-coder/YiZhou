package controllers.admin.menber;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import kits.Md5Kit;
import kits.SmsKit;
import kits.StringsKit;
import models.driver.DriverInfo;
import models.driver.Grade;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.sys.CallBack;
import models.sys.ChargeStandard;
import models.sys.User;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/9/12.
 */
@Controller("/admin/member/list")
public class ListController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }
    
    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("memberInfo.column"), SqlManager.sql("memberInfo.where"), SqlManager.sql("memberInfo.columnPage"), SqlManager.sql("memberInfo.wherePage")));
        } else {
            whereSql = " AND info.company  = ?";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("memberInfo.column"), SqlManager.sql("memberInfo.where"), whereSql, params, SqlManager.sql("memberInfo.columnPage"), SqlManager.sql("memberInfo.wherePage")));
        }
        return;
    }
    
    public void item() {
        List<ChargeStandard> chargeStandards = ChargeStandard.dao.findAll();
        setAttr("chargeStandards", chargeStandards);
        int id = getParaToInt(0, 0);
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(id);
            MemberLogin memberLogin = MemberLogin.dao.findById(memberInfo.getLoginId());
            setAttr("memberInfo", memberInfo);
            setAttr("memberLogin", memberLogin);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            MemberInfo memberInfo = new MemberInfo();
            MemberLogin memberLogin = new MemberLogin();
            setAttr("memberInfo", memberInfo);
            setAttr("memberLogin", memberLogin);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
        
    }
    
    public void del() {
        int id = getParaToInt("id", 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(id);
        int loginId = memberInfo.getLoginId();
        MemberLogin memberLogin = MemberLogin.dao.findById(loginId);
        if (memberInfo.delete() && memberLogin.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }
    
    public void save() {
        final MemberInfo memberInfo = getModel(MemberInfo.class, "memberInfo");
        if (!isSuperAdmin()) {
            memberInfo.setCompany(getCompanyId());
        }
        final MemberLogin memberLogin = getModel(MemberLogin.class, "memberLogin");
        if (memberInfo.getId() != null) {
            memberInfo.setLastUpdateTime(DateTime.now().toDate());
            memberInfo.setStatus(Constant.MemberInfoStatus.OK);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return memberInfo.update() && memberLogin.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            String password = Md5Kit.MD5(getPara("password"));
            String salt = StringsKit.getSalt();
            memberLogin.setSalt(salt);
            String dbPassword = Md5Kit.MD5(password + salt);
            memberLogin.setPassword(dbPassword);
            memberLogin.setType(Constant.MEMBER);
            memberLogin.setCreateTime(DateTime.now().toDate());
            final MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
            memberCapitalAccount.setPhone(memberInfo.getPhone());
            memberCapitalAccount.setCreateTime(DateTime.now().toDate());
            memberCapitalAccount.setStatus(1);
            memberCapitalAccount.setAmount(BigDecimal.ZERO);
            memberCapitalAccount.setAccount(memberInfo.getPhone());
            memberCapitalAccount.setAmount(BigDecimal.ZERO);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (memberLogin.save()) {
                        memberCapitalAccount.setLoginId(memberLogin.getId());
                        memberInfo.setLoginId(memberLogin.getId());
                        return memberInfo.save() && memberCapitalAccount.save();
                    }
                    return false;
                }
            });
            if (isOk) {
                renderAjaxSuccess("会员添加成功！");
            } else {
                renderAjaxError("会员添加失败！");
            }
        }
    }
    
    public void updatepd() {
//        MemberInfo memberInfo = getModel(MemberInfo.class, "memberInfo");
//        if (memberInfo.getId() != null) {
//            String oldPw = Md5Kit.MD5(getPara("oldpassword"));
//            memberInfo = MemberInfo.dao.findById(memberInfo.getId());
//            int login_id = memberInfo.getLoginId();
//            MemberLogin memberLogin = MemberLogin.dao.findById(login_id);
//            if (StringUtils.equals(Md5Kit.MD5(oldPw + memberLogin.getSalt()), memberLogin.getPassword())) {
//                String password = getPara("password");
//                String repassword = getPara("repassword");
//                if (StringUtils.equals(password, repassword)) {
//                    String salt = StringsKit.getSalt();
//                    String dbPassword = Md5Kit.MD5(Md5Kit.MD5(password) + salt);
//                    memberLogin.setSalt(salt);
//                    memberLogin.setPassword(dbPassword);
//                    if (memberLogin.update()) {
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
    
    public void validList() {
        String username = getPara("memberLogin.user_name");
        boolean isEdit = getParaToBoolean("action", false);
        MemberLogin memberLogin;
        Map<String, String> result = Maps.newHashMap();
        if (isEdit) {
            int userId = getParaToInt("memberLogin.id", 0);
            if (MemberLogin.dao.findByCount(username, userId, 2) >= 1) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            memberLogin = MemberLogin.dao.findByUserName(username, 2);
            if (memberLogin != null) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);
        
    }
    
    /**
     * 重置密码
     */
    public void repw() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(id);
            int login_id = memberInfo.getLoginId();
            MemberLogin memberLogin = MemberLogin.dao.findById(login_id);
            String salt = StringsKit.getSalt();
            String password = Md5Kit.MD5("123123");
            String dbPw = Md5Kit.MD5( password + salt);
            memberLogin.setPassword(dbPw);
            memberLogin.setSalt(salt);
            if (memberLogin.update()) {
                renderAjaxSuccess("重置密码成功！");
            } else {
                renderAjaxError("重置密码失败！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }
    
    /**
     * 账户流水
     */
    public void capital() {
        int id = getParaToInt(0, 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(id);
        int loginId = memberInfo.getLoginId();
        setAttr("id", loginId);
        setAttr("id1", id);
    }
    
    public void capitallist() {
        int id = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable("select *", " from dele_capital_log where login_id = ? AND status = 1 ORDER BY create_time DESC", params));
    }
    
    public void capitalpage() {
        int id = getParaToInt(0, 0);
        int id1 = getParaToInt(1, 0);
        setAttr("id", id1);
        CapitalLog capitalLog = CapitalLog.dao.findById(id);
        setAttr("capital", capitalLog);
    }
    
    
    public void vipCapitalToList() {
        int id = getParaToInt(0, 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(id);
        int loginId = memberInfo.getLoginId();
        setAttr("id", loginId);
        render("vipCapital.ftl");
    }
    
    public void vipCapitalList() {
        int id = getParaToInt(0, 0);
        String column = "SELECT c.id,i.phone,.c.log_content,c.use_amount,c.create_time ";
        String where = "FROM vip_log c LEFT JOIN `dele_member_info` i ON ( c.login_id =i.login_id)  WHERE c.login_id =  " + id;
        renderJson(dataTable(column, where));
    }
    
    
    /**
     * 评论信息
     */
    public void grade() {
        int id = getParaToInt(0, 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(id);
        int loginId = memberInfo.getLoginId();
        setAttr("id", loginId);
        setAttr("id2", id);
    }
    
    public void gradelist() {
        int id = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable(SqlManager.sql("memberInfo.column1"), SqlManager.sql("memberInfo.where1"), params));
    }
    
    public void gradepage() {
        int id1 = getParaToInt(1, 0);
        int id = getParaToInt(0, 0);
        Grade grade = Grade.dao.findById(id);
        int master_id = grade.getMasterId();
        int member_id = grade.getMemberId();
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(master_id);
        DriverInfo driver = DriverInfo.dao.findByLoginId(member_id);
        MemberInfo member = MemberInfo.dao.findByLoginId(member_id);
        setAttr("grade", grade);
        setAttr("memberInfo", memberInfo);
        setAttr("driver", driver);
        setAttr("member", member);
        setAttr("id", id1);
    }
    
    public void export() {
        String sql;
        List<Object> params = Lists.newArrayList();
        String[] header = {"姓名", "用户名", "家庭住址", "电话", "性别", "最后更新时间", "最后登陆时间"};
        String[] column = {"real_name", "user_name", "address", "phone", "gender", "last_update_time", "last_login_time"};
        if (isSuperAdmin()) {
            sql = SqlManager.sql("memberInfo.column") + SqlManager.sql("memberInfo.where");
            poiRender(sql, header, column, "会员列表", "member.xls");
        } else {
            params.add(getCompanyId());
            sql = SqlManager.sql("memberInfo.column") + StringsKit.replaceSql(SqlManager.sql("memberInfo.where"), "and company=?");
            poiRender(sql, params, header, column, "会员列表", "member.xls");
        }
    }
    
    /**
     * 会员充值
     */
    public void recharge() {
        int id = getParaToInt(0, 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(id);
        final CapitalLog capitalLog = new CapitalLog();
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        Date now = DateTime.now().toDate();
        final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberInfo.getLoginId());
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        if (historyAmount == null) {
            historyAmount = BigDecimal.ZERO;
        }
        memberCapitalAccount.setAmount(historyAmount.add(amount));
        memberCapitalAccount.setLastUpdateAmount(amount);
        memberCapitalAccount.setLastUpdateTime(now);
        String remark = getPara("remark");
        capitalLog.setLoginId(memberInfo.getLoginId());
        capitalLog.setAmount(amount);
        capitalLog.setRemark(remark);
        capitalLog.setOperater(getUserId());
        capitalLog.setCreateTime(now);
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        capitalLog.setOperationType(Constant.CapitalOperationType.CZOFHT);
        String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(4);
        capitalLog.setNo(no);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return capitalLog.save() && memberCapitalAccount.update();
            }
        });
        if (isOk) {
            SmsKit.rechange(memberInfo.getPhone(), memberInfo.getRealName(), memberCapitalAccount.getAccount(), amount.toString(), memberInfo.getCompany());
            renderAjaxSuccess("充值成功！");
        } else {
            renderAjaxError("充值失败！");
        }
    }
    
    public void resetDevice() {
        int driverId = getParaToInt("id", 0);
        MemberInfo memberInfo = MemberInfo.dao.findById(driverId);
        if (memberInfo != null) {
            MemberLogin memberLogin = MemberLogin.dao.findById(memberInfo.getLoginId());
            memberLogin.setDeviceNo(null);
            memberLogin.setDeviceType(null);
            if (memberLogin.update()) {
                renderAjaxSuccess("重置设备成功！");
            } else {
                renderAjaxError("重置设备失败！");
            }
        } else {
            renderAjaxError("客户信息不存在！");
        }
    }
    
    public void black() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(id);
            if (memberInfo.getStatus() == Constant.MemberInfoStatus.BLACK) {
                memberInfo.setStatus(Constant.MemberInfoStatus.OK);
            } else if (memberInfo.getStatus() == Constant.MemberInfoStatus.OK) {
                memberInfo.setStatus(Constant.MemberInfoStatus.BLACK);
            }
            memberInfo.setLastUpdateTime(DateTime.now().toDate());
            if (memberInfo.update()) {
                renderAjaxSuccess("操作成功!");
            } else {
                renderAjaxError("操作失败");
            }
        } else {
            renderAjaxError("会员不存在!");
        }
    }
    
    public void resetCompany() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(id);
            memberInfo.setCompany(null);
            memberInfo.setLastUpdateTime(DateTime.now().toDate());
            if (memberInfo.update()) {
                renderAjaxSuccess("操作成功！");
            } else {
                renderAjaxError("操作失败！");
            }
        } else {
            renderAjaxError("会员不存在!");
        }
    }
    
    /**
     * 删除推荐人
     */
    public void delinst() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(id);
            memberInfo.setIntroducerLoginId(null);
            if (memberInfo.update()) {
                renderAjaxSuccess("操作成功!");
            } else {
                renderAjaxError("操作失败!");
            }
        } else {
            renderAjaxError("会员不存在!");
        }
    }
    
    /**
     * 后台添加推荐人
     */
    public void addinst() {
        int id = getParaToInt("id", 0);
        String phone = getPara("phone");
        if (id > 0) {
            MemberInfo memberInfo = MemberInfo.dao.findByPhone(phone);
            if (memberInfo == null) {
                renderAjaxError("推荐人不存在");
                return;
            }
            MemberInfo self = MemberInfo.dao.findById(id);
            if (memberInfo.getPhone().equals(self.getPhone())) {
                renderAjaxError("推荐人不能是自己");
                return;
            }
            self.setIntroducerLoginId(memberInfo.getLoginId());
            if (self.update()) {
                renderAjaxSuccess("添加成功!");
            } else {
                renderAjaxError("添加失败!");
            }
        } else {
            renderAjaxError("会员信息不存在!");
        }
    }
    
    /**
     * 投诉回复
     */
    public void complain() {
        int id = getParaToInt(0, 0);
        setAttr("id", id);
    }
    
    public void complainList() {
        int id = getParaToInt(0, 0);
        MemberInfo memberinfo = MemberInfo.dao.findById(id);
        List<Object> params = Lists.newArrayList();
        String whereSql = " AND cb.login_id = ?";
        params.add(memberinfo.getLoginId());
        renderJson(dataTable(SqlManager.sql("callBack.column"), SqlManager.sql("callBack.where"), whereSql, params));
    }
    
    
    public void complainpage() {
        int id = getParaToInt(0, 0);
        setAttr("callBack", CallBack.dao.findById(id));
    }
    
    public void complainSave() {
        final CallBack callback = getModel(CallBack.class, "callBack");
        callback.setUserId(getUserId());
        if (callback.update()) {
            renderAjaxSuccess("操作成功!");
        } else {
            renderAjaxError("操作失败");
        }
    }
}
