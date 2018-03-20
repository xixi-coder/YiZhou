package controllers.admin.order;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.order.InvoiceRec;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.List;

import static com.jfinal.plugin.activerecord.Db.find;

/**
 * Created by admin on 2017/1/8.
 */
@Controller("/admin/order/invoice")
public class InvoiceController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("invoiceRec.cloum"), SqlManager.sql("invoiceRec.where")));
        } else {
            String tmp = " AND dmi.company = ? ";
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("invoiceRec.cloum"), SqlManager.sql("invoiceRec.where"), tmp, params));
        }

    }

    public void add() {
        InvoiceRec invoiceRec = getModel(InvoiceRec.class, "invoiceRec");
        invoiceRec.setStatus(Constant.DataAuditStatus.AUDITOK);
        invoiceRec.setInvoiceTime(DateTime.now().toDate());
        if (invoiceRec.update()) {
            renderAjaxSuccess("开票成功！");
        } else {
            renderAjaxError("开票失败！稍后再试");
        }
    }

    public void findlistByid() {
        String id = getPara("id");
        setAttr("dataMap", find(SqlManager.sql("invoiceRec.findByid"), id));
        renderJson(find(SqlManager.sql("invoiceRec.findByid"), id));


    }
}