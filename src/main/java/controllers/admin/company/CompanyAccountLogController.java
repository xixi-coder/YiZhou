package controllers.admin.company;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

@Controller("/admin/company/accountlog")
public class CompanyAccountLogController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("companyAccount.column"), SqlManager.sql("companyAccount.where"),SqlManager.sql("companyAccount.columnPage"), SqlManager.sql("companyAccount.wherePage")));
        } else {
            String tmpStr = " AND dc.id = ? ";
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("companyAccount.column"), SqlManager.sql("companyAccount.where"), tmpStr, params,SqlManager.sql("companyAccount.columnPage"), SqlManager.sql("companyAccount.wherePage")));
        }
    }
}
