package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import models.company.Company;
import models.sys.AdminSetting;
import models.sys.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
@Controller("/admin/sys/adminsetting")
public class AdminSettingController extends BaseAdminController {
    public void index() {
        int index = getParaToInt(0, 1);
        int companyId = getParaToInt(1, 0);
        setAttr("index", 1);
        if (companyId == 0){
            List<Company> company = Company.dao.findByEnable();
            setAttr("companies",company);
        }else {
            Company company1 = Company.dao.findByCompanyId(companyId);
            List<Company> company = new ArrayList();
            company.add(company1);
            setAttr("companies",company);
        }
        AdminSetting adminSetting = AdminSetting.dao.findByCompanyIdBack(companyId);
        if (adminSetting == null){
            renderAjaxError("该公司没有系统设置，请联系后台大神处理！");
            return;
        }
        setAttr("adminSetting", adminSetting);
        if (index == 6) {
            Version driverClient = Version.dao.findMaxVersion(1,Constant.ANDROID);
            setAttr("driverClient", driverClient);
            Version memberClient = Version.dao.findMaxVersion(2,Constant.ANDROID);
            setAttr("memberClient", memberClient);
            render("appdown.ftl");
        }
    }

    public void save() {
        int index = getParaToInt(0, 1);
        AdminSetting adminSetting = getModel(AdminSetting.class, "adminSetting");
        if (adminSetting.getId() != null) {
            if (adminSetting.update()) {
                renderAjaxSuccess(index, "修改成功！");
            } else {
                renderAjaxError("修改失败！");
            }
        } else {
            if (adminSetting.save()) {
                renderAjaxSuccess(index, "添加成功！");
            } else {
                renderAjaxError("添加失败!");
            }
        }
    }
}
