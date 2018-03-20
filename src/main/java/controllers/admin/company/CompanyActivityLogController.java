package controllers.admin.company;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONm on 16/8/10.
 */
@Controller("/admin/company/activitylog")
public class CompanyActivityLogController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("companyActivity.column"), SqlManager.sql("companyActivity.where"), SqlManager.sql("companyActivity.columnPage"), SqlManager.sql("companyActivity.wherePage")));
        } else {
            String tmpStr = " AND dc.id = ? ";
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("companyActivity.column"), SqlManager.sql("companyActivity.where"), tmpStr, params, SqlManager.sql("companyActivity.columnPage"), SqlManager.sql("companyActivity.wherePage")));
        }
    }
}
