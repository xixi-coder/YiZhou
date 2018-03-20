package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.company.*;
import models.driver.DriverInfo;
import models.sys.AdminSetting;
import models.sys.Area;
import models.sys.ServiceType;
import org.springframework.beans.BeanUtils;

import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by BOGONm on 16/8/10.
 */
@Controller("/admin/sys/company")
public class CompanyController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable(SqlManager.sql("company.column"), SqlManager.sql("company.where")));
    }

    public void item() {
        int pkId = getParaToInt(0, 0);
        Company company;
        if (pkId > 0) {
            company = Company.dao.findById(pkId);
            List<Company> companies = Company.dao.findAllByCompanyId(company.getId());
            setAttr("areas", companies);
            setAttr("company", company);
            List<Area> province = Area.dao.findByLevelAndParent("province", 0 + "");
            setAttr("province", province);
            if (!Strings.isNullOrEmpty(company.getProvince())) {
                List<Area> city = Area.dao.findByLevelAndParent("city", company.getProvince());
                setAttr("city", city);
            }
            if (!Strings.isNullOrEmpty(company.getCity())) {
                List<Area> county = Area.dao.findByLevelAndParent("district", company.getCity());
                setAttr("county", county);
            }
            //公司服务类型
            List<ServiceType> list = ServiceType.dao.getServiceType(company.getId());
            List<ServiceType> listAll = ServiceType.dao.findAll();
            setAttr("serviceTypes", list);
            setAttr("serviceTypesAll", listAll);

            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            company = new Company();
            setAttr("company", company);
            List<Area> province = Area.dao.findByLevelAndParent("province", 0 + "");
            setAttr("province", province);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public synchronized void save() throws InvocationTargetException, IllegalAccessException {
        final Company company = getModel(Company.class, "company");
        String areasString = getPara("area");
        JSONArray jsonArray = JSON.parseArray(areasString);
        final List<Company> companies = Lists.newArrayList();
        if (company.getPkId() != null) {
            final int companyId = company.getId();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Company company1 = new Company();
                //创建时间
                Company c = Company.dao.findById(company.getPkId());
                if (c != null) {
                    if (c.getCreateTime() != null) {
                        company1.setCreateTime(c.getCreateTime());
                    }
                }
                company.remove("pk_id");
                company1.setAddress(company.getAddress());
                company1.setId(companyId);
                company1.setLastUpdateTime(company.getLastUpdateTime());
                company1.setDescription(company.getDescription());
                company1.setLatitude(company.getLatitude());
                company1.setLongitude(company.getLongitude());
                company1.setName(company.getName());
                company1.setPhone(company.getPhone());
                company1.setPicture(company.getPicture());
                company1.setQq(company.getQq());
                company1.setWebsite(company.getWebsite());
                company1.setProvince(jsonObject.getString("province"));
                company1.setCity(jsonObject.getString("city"));
                company1.setCounty(jsonObject.getString("county"));
                companies.add(company1);
            }
            company.setLastUpdateTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (Company.dao.deleByCompanyId(companyId) >= 0) {
                        for (Company tmp : companies) {
                            tmp.remove("pk_id");
                            if (!tmp.save()) {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (isOk) {
                renderAjaxSuccess("保存成功！");
            } else {
                renderAjaxError("保存失败！");
            }
        } else {
            final int companyId = Company.dao.findMaxId() + 1;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                company.remove("pk_id");
                company.setId(companyId);
                Company company1 = new Company();
                company1.setId(companyId);
                company1.setAddress(company.getAddress());
                company1.setLastUpdateTime(company.getLastUpdateTime());
                company1.setDescription(company.getDescription());
                company1.setLatitude(company.getLatitude());
                company1.setLongitude(company.getLongitude());
                company1.setName(company.getName());
                company1.setPhone(company.getPhone());
                company1.setPicture(company.getPicture());
                company1.setQq(company.getQq());
                company1.setWebsite(company.getWebsite());
                company1.setProvince(jsonObject.getString("province"));
                company1.setCity(jsonObject.getString("city"));
                company1.setCounty(jsonObject.getString("county"));
                company1.setCreateTime(new Date());
                companies.add(company1);
            }
            final CompanyAccount companyAccount = new CompanyAccount();
            companyAccount.setAmount(BigDecimal.ZERO);
            companyAccount.setLastAmont(BigDecimal.ZERO);
            companyAccount.setLastUpdate(DateTime.now().toDate());
            companyAccount.setSmsCount(0);
            final AdminSetting adminSetting = new AdminSetting();
            AdminSetting adminSetting1 = AdminSetting.dao.findFirst();
            BeanUtils.copyProperties(adminSetting1, adminSetting);
            adminSetting.setId(null);
            adminSetting.setCompanyId(companyId);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    for (Company tmp : companies) {
                        if (!tmp.save()) {
                            return false;
                        }
                    }
                    companyAccount.setCompany(companyId);
                    if (!companyAccount.save()) {
                        return false;
                    }
                    if (!adminSetting.save()) {
                        return false;
                    }
                    return true;
                }
            });
            if (isOk) {
                renderAjaxSuccess("新增成功！");
            } else {
                renderAjaxError("新增失败！");
            }
        }
    }

    /**
     * 删除公司
     */
    public void del() {
        final int id = getParaToInt("id", 0);
        if (id > 0) {
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (Company.dao.deleByCompanyId(id) >= 0) {
                        CompanyAccount companyA = CompanyAccount.dao.findById(id);
                        if (companyA.delete()) {
                            return true;
                        }
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
            renderAjaxError("公司不存在！");
        }
    }

    public void distribution() {
        setAttr("type", getParaToInt(1, 1));
        int company = getParaToInt(0, 0);
        setAttr("company", company);
        int style = getParaToInt(2, 1);
        int serviceType = getParaToInt(3, 0);
        setAttr("serviceType", serviceType);
        setAttr("style", style);
        if (style > 2 || style < 1) {
            redirect("/admin/sys/company");
            return;
        }
        Distribution distribution = Distribution.dao.findByCompanyAndStyle(company, style, serviceType);
        if (distribution == null) {
            distribution = new Distribution();
            distribution.setStyle(style);
            distribution.setLevel(0);
        }
        List<Company> companys = Company.dao.findByEnable();
        List servicetype = ServiceType.dao.getServiceType(company);
        setAttr("servicetype", servicetype);
        setAttr("companys", companys);
        setAttr("distribution", distribution);
    }

    public void agreement() {
        int company = getParaToInt(0, 0);
        setAttr("company", company);
        int type = getParaToInt(1, 1);
        setAttr("type", type);
        int style = getParaToInt(2, 1);
        setAttr("style", style);
        Agreement agreement = Agreement.dao.findByCompanyType(company, style);
        setAttr("agreement", agreement);
    }

    public void saveAgreement() {
        final Agreement agreement = getModel(Agreement.class, "agreement");
        boolean isOk;
        agreement.setLastUpdate(DateTime.now().toDate());
        agreement.setOperater(getUserId());
        if (agreement.getId() == null) {
            isOk = agreement.save();
        } else {
            isOk = agreement.update();
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxError("保存失败！");
        }
    }

    public void saveDistribution() {
        final Distribution distribution = getModel(Distribution.class, "distribution");
        distribution.setCreateTime(DateTime.now().toDate());
        distribution.setCreater(getUserId());
        boolean isOk;
        if (distribution.getId() == null) {
            isOk = distribution.save();
        } else {
            Distribution distribution1 = Distribution.dao.findByCompanyAndStyle(distribution.getCompany(), distribution.getStyle(), distribution.getServiceType());
            if (distribution1 == null) {
                isOk = distribution.save();
            } else {
                isOk = distribution.update();
            }
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxError("保存失败！");
        }
    }

    /**
     * 短信充值
     */
    public void rechangeSms() {
        int id = getParaToInt("id", 0);
        if (id != 0) {
            BigDecimal money = new BigDecimal(getPara("money"));
            CompanyAccount companyAccount = CompanyAccount.dao.findById(id);
            if (money.compareTo(BigDecimal.ZERO) > 0) {
                int count = money.divide(BigDecimal.valueOf(0.09), 5).intValue();
                int historyCount = companyAccount.getSmsCount() == null ? 0 : companyAccount.getSmsCount();
                companyAccount.setSmsCount(count + historyCount);
                if (companyAccount.update()) {
                    renderAjaxSuccess("充值成功");
                }
            } else {
                renderAjaxError("充值失败！");
            }
        } else {
            renderAjaxError("公司信息不存在！");
        }
    }

    /**
     * 活动金额充值
     */
    public void rechangeActivityAmount() {
        int id = getParaToInt("id", 0);
        if (id != 0) {
            BigDecimal money = new BigDecimal(getPara("money"));
            CompanyAccount companyAccount = CompanyAccount.dao.findById(id);
            if (money.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal historyAmount = companyAccount.getActivityAmount() == null ? BigDecimal.ZERO : companyAccount.getActivityAmount();
                companyAccount.setActivityAmount(historyAmount.add(money));
                if (companyAccount.update()) {
                    renderAjaxSuccess("充值成功");
                }
            } else {
                renderAjaxError("充值失败！");
            }
        } else {
            renderAjaxError("公司信息不存在！");
        }
    }

    /**
     * 活动金额充值
     */
    public void rechangeInsuranceAmount() {
        int id = getParaToInt("id", 0);
        if (getPara("money") == null) {
            renderAjaxError("金额输入错误！");
        }
        if (id != 0) {
            BigDecimal money = new BigDecimal(getPara("money"));
            CompanyAccount companyAccount = CompanyAccount.dao.findById(id);
            if (money.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal historyAmount = companyAccount.getInsuranceAmount() == null ? BigDecimal.ZERO : companyAccount.getInsuranceAmount();
                companyAccount.setInsuranceAmount(historyAmount.add(money));
                if (companyAccount.update()) {
                    renderAjaxSuccess("充值成功");
                }
            } else {
                renderAjaxError("充值失败！");
            }
        } else {
            renderAjaxError("公司信息不存在！");
        }
    }

    /**
     * 获取可用的公司
     */
    public void allcompany() {
        List<Company> companys = Company.dao.findByEnable();
        renderJson(companys);
    }

    public void disable() {
        int companyId = getParaToInt(0, 0);
        if (companyId > 0) {
            if (Db.update(SqlManager.sql("user.disableByCompany"), Constant.DataAuditStatus.AUDITFAIL, companyId) >= 0) {
                renderAjaxSuccess("禁用全部后台账户成功!");
            } else {
                renderAjaxError("禁用失败，稍后再试!");
            }
        } else {
            renderAjaxError("公司信息不存在");
        }
    }

    public void able() {
        int companyId = getParaToInt(0, 0);
        if (companyId > 0) {
            if (Db.update(SqlManager.sql("user.disableByCompany"), Constant.DataAuditStatus.AUDITOK, companyId) >= 0) {
                renderAjaxSuccess("启用全部后台账户成功!");
            } else {
                renderAjaxError("启用失败，稍后再试!");
            }
        } else {
            renderAjaxError("公司信息不存在");
        }
    }

    public void frozen() {
        int companyId = getParaToInt(0, 0);
        if (companyId > 0) {
            List<DriverInfo> driverInfos = DriverInfo.dao.findByCompany(companyId, Constant.DriverStatus.ShengHeTongGuo);
            for (DriverInfo driverInfo : driverInfos) {
                DriverInfo.dao.setForen(driverInfo.getId());
            }
            renderAjaxSuccess("冻结成功!");
        } else {
            renderAjaxError("公司信息不存在");
        }
    }

    public void nofrozen() {
        int companyId = getParaToInt(0, 0);
        if (companyId > 0) {
            List<DriverInfo> driverInfos = DriverInfo.dao.findByCompany(companyId, Constant.DriverStatus.FROZEN);
            for (DriverInfo driverInfo : driverInfos) {
                DriverInfo.dao.setNoFrozen(driverInfo.getId());
            }
            renderAjaxSuccess("解结成功!");
        } else {
            renderAjaxError("公司信息不存在");
        }
    }

    /**
     * 禁用和开启服务类型接口
     */
    public void serviceTypeDisabled() {
        int serviceType = getParaToInt("serviceType", 0);
        int company = getParaToInt("company", 0);
        if (serviceType == 0 || company == 0) {
            renderAjaxError("信息错误");
            return;
        }

        try {
            List<CompanyService> list = CompanyService.dao.serviceTypeDisabled(company, serviceType);
            for (CompanyService type : list) {
                if (type.getStatus() == 1) {
                    type.setStatus(0);
                } else {
                    type.setStatus(1);
                }
                type.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderAjaxSuccess("操作失败!");
            return;
        }

        renderAjaxSuccess("操作成功!");


    }
}
