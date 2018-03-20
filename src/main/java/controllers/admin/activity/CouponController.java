package controllers.admin.activity;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.activity.Coupon;
import models.activity.MemberCoupon;
import models.company.Company;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.member.MemberInfo;
import models.sys.ServiceType;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/28.
 */
@Controller("/admin/activity/coupon")
public class CouponController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable("select dlc.*,dc.name ", " from dele_coupon dlc LEFT JOIN dele_company dc ON dlc.company = dc.id where 1=1 -- where \n ORDER BY create_time DESC"));
        } else {
            whereSql = " AND company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable("select dlc.*,dc.name ", " from dele_coupon dlc LEFT JOIN dele_company dc ON dlc.company = dc.id where 1=1 -- where \n ORDER BY create_time DESC", whereSql, params));
        }
        return;
    }

    public void del() {
        int id = getParaToInt("id", 0);
        Coupon coupon = Coupon.dao.findById(id);
        if (coupon.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        List<Company> companys = Company.dao.findByEnable();
        setAttr("company", companys);
        if (isSuperAdmin()) {
            List<ServiceType> serviceType = ServiceType.dao.findAll();
            setAttr("serviceType", serviceType);
        } else {
            List<ServiceType> serviceType = ServiceType.dao.getServiceType(getCompanyId());
            setAttr("serviceType", serviceType);
        }
        if (id > 0) {
            Coupon coupon = Coupon.dao.findById(id);
            setAttr("coupon", coupon);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            Coupon coupon = new Coupon();
            setAttr("coupon", coupon);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    /**
     * 下拉框级联
     */
    public void cascading() {
        int CompanyId = getParaToInt("id");
        List<Coupon> activities = Coupon.dao.findByCidAndDate(DateTime.now(), CompanyId);

        Map<String, Object> map = new HashMap();
        map.put("code", 200);
        map.put("data", activities);

        renderJson(map);

    }

    public void save() {
        Coupon coupon = getModel(Coupon.class, "coupon");
        coupon.setStartTime(new DateTime(coupon.getStartTime()).millisOfDay().withMinimumValue().toDate());
        coupon.setEndTime(new DateTime(coupon.getEndTime()).millisOfDay().withMaximumValue().toDate());
        coupon.setCreater(getUserId());
        if (!isSuperAdmin()) {
            coupon.setCompany(getCompanyId());
        }
        if (coupon.getId() != null) {
            if (coupon.update()) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            coupon.setCreateTime(DateTime.now().toDate());
            if (coupon.save()) {
                renderAjaxSuccess("信息增加成功！");
            } else {
                renderAjaxError("信息增加失败！");
            }
        }
    }

    /**
     * 优惠券列表
     */
    public void couponlist() {
        int id = getParaToInt(0, 0);
        setAttr("id1", id);
    }

    public void membercoupon() {
        int id = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable(SqlManager.sql("coupon.column3"), SqlManager.sql("coupon.where3"), params));
    }

    /**
     * 删除优惠券
     */
    public void coupondel() {
        int id = getParaToInt("id", 0);
        MemberCoupon memberCoupon = MemberCoupon.dao.findById(id);
        if (memberCoupon.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    /**
     * 生成优惠券
     */
    public void createcoupon() {
        int id = getParaToInt("id1", 0);
        final Coupon coupon = Coupon.dao.findById(id);
        String source = coupon.getCouponDesc();
        int amount = getParaToInt("amount", 0);
        CompanyAccount companyAccount = CompanyAccount.dao.findById(coupon.getCompany());
        if (coupon.getCouponType() == Constant.CouponType.Discount) {
            final List<MemberCoupon> members = Lists.newArrayList();
            for (int i = 0; i < amount; i++) {
                String password = RandomStringUtils.randomNumeric(6);
                String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                MemberCoupon memberCoupon = new MemberCoupon();
                memberCoupon.setCouponId(id);
                memberCoupon.setStartTime(coupon.getStartTime());
                memberCoupon.setEndTime(coupon.getEndTime());
                memberCoupon.setTitle(coupon.getCouponTitle());
                memberCoupon.setDescription(coupon.getCouponDesc());
                memberCoupon.setCouponSource(source);
                memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                memberCoupon.setNo(no);
                memberCoupon.setPassword(password);
                memberCoupon.setType(Constant.CouponType.Discount);
                members.add(memberCoupon);
            }
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    for (MemberCoupon member : members) {
                        if (!member.save()) {
                            return false;
                        }
                        CompanyActivity companyActivity = CompanyActivity.dao.creatPercent(Constant.CompanyAcitivyType.CODECOUPON, coupon.getCompany(), 0, Constant.DataAuditStatus.CREATE, member.getId(), "后台定向赠送优惠券");
                        if (!companyActivity.save()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
            if (isOk) {
                renderAjaxSuccess("优惠券生成成功！请保证公司活动金额充足，否则无法正常使用优惠券！");
            } else {
                renderAjaxError("优惠券生成失败！");
            }
        } else {
            BigDecimal totalAmount = coupon.getAmount().multiply(BigDecimal.valueOf(amount));
            if (CompanyAccount.dao.amountEnough(companyAccount, totalAmount, Constant.CompanyAccountActivity.createcoupon)) {
                final List<MemberCoupon> members = Lists.newArrayList();
                for (int i = 0; i < amount; i++) {
                    String password = RandomStringUtils.randomNumeric(6);
                    String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                    MemberCoupon memberCoupon = new MemberCoupon();
                    memberCoupon.setCouponId(id);
                    memberCoupon.setStartTime(coupon.getStartTime());
                    memberCoupon.setEndTime(coupon.getEndTime());
                    memberCoupon.setTitle(coupon.getCouponTitle());
                    memberCoupon.setDescription(coupon.getCouponDesc());
                    memberCoupon.setAmount(coupon.getAmount());
                    memberCoupon.setCouponSource(source);
                    memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                    memberCoupon.setNo(no);
                    memberCoupon.setPassword(password);
                    memberCoupon.setType(Constant.CouponType.Normal);
                    members.add(memberCoupon);
                }
                boolean isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        for (MemberCoupon member : members) {
                            if (!member.save()) {
                                return false;
                            }
                            CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.CODECOUPON, member.getAmount(), coupon.getCompany(), 0, Constant.DataAuditStatus.CREATE, member.getId(), "后台定向赠送优惠券");
                            if (!companyActivity.save()) {
                                return false;
                            }
                        }
                        return true;
                    }
                });
                if (isOk) {
                    renderAjaxSuccess("优惠券生成成功！");
                } else {
                    renderAjaxError("优惠券生成失败！");
                }
            } else {
                renderAjaxError("公司活动金额不足");
                return;
            }
        }
    }

    public void memberList() {
        String name = getPara("name");
        List<MemberInfo> memberInfos = MemberInfo.dao.findForCouponByCompany(isSuperAdmin(), getCompanyId(), name);
        renderJson(memberInfos);
    }

    /**
     * 奖励优惠券
     */
    public void award() {
        int couponId = getParaToInt("couponId", 0);
        String memberIds = getPara("memberIds");
        String[] ids = memberIds.split(",");
        final Coupon coupon = Coupon.dao.findById(couponId);
        final List<MemberCoupon> members = Lists.newArrayList();
        if (coupon.getCouponType() == Constant.CouponType.Discount) {
            for (int i = 0; i < ids.length; i++) {
                String password = RandomStringUtils.randomNumeric(6);
                String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                MemberCoupon memberCoupon = new MemberCoupon();
                memberCoupon.setCouponId(couponId);
                memberCoupon.setStartTime(coupon.getStartTime());
                memberCoupon.setEndTime(coupon.getEndTime());
                memberCoupon.setTitle(coupon.getCouponTitle());
                memberCoupon.setDescription(coupon.getCouponDesc());
                memberCoupon.setCouponSource("后台活动赠送");
                memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                memberCoupon.setNo(no);
                memberCoupon.setLoginId(Ints.tryParse(ids[i]));
                memberCoupon.setGainTime(DateTime.now().toDate());
                memberCoupon.setPassword(password);
                CompanyAccount account = CompanyAccount.dao.findById(coupon.getCompany());
                if (CompanyAccount.dao.amountEnough(account, BigDecimal.ZERO, Constant.CompanyAccountActivity.awardMember)) {
                    members.add(memberCoupon);
                } else {
                    renderAjaxError("公司活动金额不足");
                    return;
                }
            }
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    for (MemberCoupon member : members) {
                        if (!member.save()) {
                            return false;
                        }
                        CompanyActivity companyActivity = CompanyActivity.dao.creatPercent(Constant.CompanyAcitivyType.DINGXIANGCOUPON, coupon.getCompany(), member.getLoginId(), Constant.DataAuditStatus.CREATE, member.getId(), "后台定向赠送优惠券");
                        if (!companyActivity.save()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
            if (isOk) {
                renderAjaxSuccess("赠送优惠券成功！请保证公司活动金额充足，否则无法正常使用该券！");
            } else {
                renderAjaxError("赠送优惠券失败！");
            }
        } else {
            for (int i = 0; i < ids.length; i++) {
                String password = RandomStringUtils.randomNumeric(6);
                String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                MemberCoupon memberCoupon = new MemberCoupon();
                memberCoupon.setCouponId(couponId);
                memberCoupon.setStartTime(coupon.getStartTime());
                memberCoupon.setEndTime(coupon.getEndTime());
                memberCoupon.setTitle(coupon.getCouponTitle());
                memberCoupon.setDescription(coupon.getCouponDesc());
                memberCoupon.setAmount(coupon.getAmount());
                memberCoupon.setCouponSource("后台活动赠送");
                memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                memberCoupon.setNo(no);
                memberCoupon.setLoginId(Ints.tryParse(ids[i]));
                memberCoupon.setGainTime(DateTime.now().toDate());
                memberCoupon.setPassword(password);
                CompanyAccount account = CompanyAccount.dao.findById(coupon.getCompany());
                if (CompanyAccount.dao.amountEnough(account, coupon.getAmount(), Constant.CompanyAccountActivity.awardMember)) {
                    members.add(memberCoupon);
                } else {
                    renderAjaxError("公司活动金额不足");
                    return;
                }
            }
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    for (MemberCoupon member : members) {
                        if (!member.save()) {
                            return false;
                        }
                        CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.DINGXIANGCOUPON, member.getAmount(), coupon.getCompany(), member.getLoginId(), Constant.DataAuditStatus.CREATE, member.getId(), "后台定向赠送优惠券");
                        if (!companyActivity.save()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
            if (isOk) {
                renderAjaxSuccess("赠送优惠券成功！");
            } else {
                renderAjaxError("赠送优惠券失败！");
            }
        }
    }
}
