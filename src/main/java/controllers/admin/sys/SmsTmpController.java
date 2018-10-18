package controllers.admin.sys;

import com.google.common.collect.Lists;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;

import org.joda.time.DateTime;

import java.util.List;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import models.company.Company;
import models.sys.SmsTmp;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/9/9.
 */
@Controller("/admin/sys/smstmp")
public class SmsTmpController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("smstmp.column"), SqlManager.sql("smstmp.where")));
        } else {
            whereSql = " AND dc.id = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("smstmp.column"), SqlManager.sql("smstmp.where"), whereSql, params));
        }
        return;
    }

    public void del() {
        int id = getParaToInt("id", 0);
        SmsTmp smsTmp = SmsTmp.dao.findById(id);
        if (smsTmp.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        List<Company> companies = Company.dao.findByEnable();
        setAttr("company", companies);
        SmsTmp smsTmp;
        if (id > 0) {
            smsTmp = SmsTmp.dao.findById(id);
            setAttr("smstmp", smsTmp);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            smsTmp = new SmsTmp();
            setAttr("smstmp", smsTmp);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        SmsTmp smsTmp = getModel(SmsTmp.class, "smstmp");
        if (smsTmp.getId() != null) {
            if (smsTmp.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxFailure("修改失败！");
            }
        } else {
            smsTmp.setCreateTime(DateTime.now().toDate());
            if (smsTmp.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxFailure("添加失败！");
            }
        }
    }

    //启用和禁用
    public void disable() {
        int smsTmpId = getParaToInt(0, 0);
        if (smsTmpId > 0) {
            SmsTmp smsTmp=SmsTmp.dao.findById(smsTmpId);
            if (smsTmp.getStatus() == 0) {
                smsTmp.setStatus(1);
            } else {
                smsTmp.setStatus(0);
            }
            if (smsTmp.update()) {
                renderAjaxSuccess("操作成功！");
            } else {
                renderAjaxError("操作失败！");
            }
        } else {
            renderAjaxError("短信模板不存在");
        }
    }
}
