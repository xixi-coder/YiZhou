package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import models.company.Company;
import models.sys.Provision;
import models.sys.ServiceType;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/11/18.
 */
@Controller("/admin/sys/provision")
public class ProvisionController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable(SqlManager.sql("provision.cloumn"), SqlManager.sql("provision.where")));
    }

    public void item() {
        //获取类型
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);

        int noticeId = getParaToInt(0, 0);
        Provision provision;
        if (isSuperAdmin()) {
            setAttr("company", Company.dao.findByEnable());
        }
        if (noticeId == 0) {
            provision = new Provision();
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        } else {
            provision = Provision.dao.findById(noticeId);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        }
        setAttr("provision", provision);
    }

    public void save() {
        Provision provision = getModel(Provision.class, "provision");
        boolean isOk;
        if (provision.getId() == null) {
            provision.setCreateTime(DateTime.now().toDate());
            provision.setOperater(getUserId());
            provision.setCompany(getCompanyId());
            provision.setUpdateTime(DateTime.now().toDate());
            isOk = provision.save();
        } else {
            provision.setOperater(getUserId());
            provision.setCompany(getCompanyId());
            provision.setUpdateTime(DateTime.now().toDate());
            isOk = provision.update();
        }
        if (isOk) {
            renderAjaxSuccess("操作成功！");
        } else {
            renderAjaxSuccess("操作失败！");
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        if (Provision.dao.deleteById(id)) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxSuccess("删除失败！");
        }
    }
}
