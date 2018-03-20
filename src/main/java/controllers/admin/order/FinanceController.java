package controllers.admin.order;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.order.OrderDetail;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2016/10/11.
 */
@Controller("/admin/finance")
public class FinanceController extends BaseAdminController {
    public void index() {
        setAttr("type", getParaToInt(0, 1));
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt(0, 1);
        List<Object> params = Lists.newArrayList();
        switch (type) {
            case 1:
                String whereSql;
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("orderDetail.column"), SqlManager.sql("orderDetail.where"), SqlManager.sql("orderDetail.columnPage"), SqlManager.sql("orderDetail.wherePage")));
                } else {
                    whereSql = " AND dod.company = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("orderDetail.column"), SqlManager.sql("orderDetail.where"), whereSql, params, SqlManager.sql("orderDetail.columnPage"), SqlManager.sql("orderDetail.wherePage")));
                }
                return;
            case 2:
                String whereSql1;
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("orderDetail.columnDriver"), SqlManager.sql("orderDetail.whereDriver"), SqlManager.sql("orderDetail.columnDriverPage"), SqlManager.sql("orderDetail.whereDriverPage")));
                } else {
                    whereSql = " AND ddi.company = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("orderDetail.columnDriver"), SqlManager.sql("orderDetail.whereDriver"), whereSql, params, SqlManager.sql("orderDetail.columnDriverPage"), SqlManager.sql("orderDetail.whereDriverPage")));
                }
                return;
            case 3:
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("memberCapital.column"), SqlManager.sql("memberCapital.where"), SqlManager.sql("memberCapital.columnPage"), SqlManager.sql("memberCapital.wherePage")));
                } else {
                    whereSql = " AND company  = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("memberCapital.column"), SqlManager.sql("memberCapital.where"), whereSql, params, SqlManager.sql("memberCapital.columnPage"), SqlManager.sql("memberCapital.wherePage")));
                }
                return;
            case 4:
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("memberCapital.column1"), SqlManager.sql("memberCapital.where1"), SqlManager.sql("memberCapital.columnPage1"), SqlManager.sql("memberCapital.wherePage1")));
                } else {
                    whereSql = " AND company  = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("memberCapital.column1"), SqlManager.sql("memberCapital.where1"), whereSql, params, SqlManager.sql("memberCapital.columnPage1"), SqlManager.sql("memberCapital.wherePage1")));
                }
                return;
            case 5:
                params.add(Constant.CapitalStatus.OK);
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("capitalLog.column"), SqlManager.sql("capitalLog.where"), params, SqlManager.sql("capitalLog.columnPage"), SqlManager.sql("capitalLog.wherePage")));
                } else {
                    whereSql = " AND company  = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("capitalLog.column"), SqlManager.sql("capitalLog.where"), whereSql, params, SqlManager.sql("capitalLog.columnPage"), SqlManager.sql("capitalLog.wherePage")));
                }
                return;
            case 6:
                String whereSql5;
                params.add(Constant.CapitalStatus.OK);
                if (isSuperAdmin()) {
                    renderJson(dataTable(SqlManager.sql("capitalLog.column1"), SqlManager.sql("capitalLog.where1"), params, SqlManager.sql("capitalLog.columnPage1"), SqlManager.sql("capitalLog.wherePage1")));
                } else {
                    whereSql5 = " AND company  = ?";
                    params.add(getCompanyId());
                    renderJson(dataTable(SqlManager.sql("capitalLog.column1"), SqlManager.sql("capitalLog.where1"), whereSql5, params, SqlManager.sql("capitalLog.columnPage1"), SqlManager.sql("capitalLog.wherePage1")));
                }
                setAttr("type", type);
        }
    }

    public void depositlist() {
        setAttr("type", getParaToInt(0, 1));
    }

    public void loglist() {
        setAttr("type", getParaToInt(0, 1));
    }

    public void audit() {
        int status = getParaToInt(0, 0);
        int id = getParaToInt(1, 0);
        double amount = getParaToInt("amount");
        OrderDetail orderDetail = OrderDetail.dao.findById(id);
        orderDetail.setAuditStatus(status);
        orderDetail.setAmount(BigDecimal.valueOf(amount));
        if (orderDetail.update()) {
            renderAjaxSuccess("审核完毕！");
        } else {
            renderAjaxError("审核失败！");
        }
    }
}
