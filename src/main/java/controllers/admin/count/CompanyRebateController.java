package controllers.admin.count;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import kits.StringsKit;
import models.company.Company;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/5.
 */
@Controller("/admin/count/companyrebate")
public class CompanyRebateController extends BaseAdminController {
    public void index() {
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("companyRebate.column"), SqlManager.sql("companyRebate.where"),SqlManager.sql("companyRebate.columnPage"), SqlManager.sql("companyRebate.wherePage")));
        } else {
            whereSql = "AND dcr.company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("companyRebate.column"), SqlManager.sql("companyRebate.where"), whereSql, params,SqlManager.sql("companyRebate.columnPage"), SqlManager.sql("companyRebate.wherePage")));
        }
        return;
    }

    public void export() {
        String start = getPara("start1");
        String end = getPara("end1");
        String sql;
        List<Object> params = Lists.newArrayList();
        String[] header = {"公司", "返利金额", "返利订单", "返利时间", "返利司机"};
        String[] column = {"name", "amount", "no", "rebate_time", "user_name"};
        if (isSuperAdmin()) {
            if(start==null || end==null){
                sql = SqlManager.sql("companyRebate.column") + SqlManager.sql("companyRebate.where");
                poiRender(sql, header, column, "公司返利订单统计", "rebate.xls");
            }else {
                params.add(start);
                params.add(end);
                sql = SqlManager.sql("companyRebate.column")+ StringsKit.replaceSql(SqlManager.sql("companyRebate.where"),"AND dcr.rebate_time BETWEEN ? AND ? ");
                poiRender(sql, params, header, column, "公司返利订单统计", "rebate.xls");
            }

        } else {
            if(start==null || end==null){
                params.add(getCompanyId());
                sql = SqlManager.sql("companyRebate.column") + SqlManager.sql("companyRebate.where").replace("-- where", "and dcr.company=?");
                poiRender(sql, params, header, column, "公司返利订单统计", "rebate.xls");
            }else {
                params.add(start);
                params.add(end);
                params.add(getCompanyId());
                sql = SqlManager.sql("companyRebate.column")+ StringsKit.replaceSql(SqlManager.sql("companyRebate.where"),"AND dcr.rebate_time BETWEEN ? AND ? and dcr.company=? ");
                poiRender(sql, params, header, column, "公司返利订单统计", "rebate.xls");
            }
        }
    }
}
