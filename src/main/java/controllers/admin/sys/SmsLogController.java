package controllers.admin.sys;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.sys.SmsLog;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
@Controller("/admin/sys/smslog")
public class SmsLogController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("smslog.column"), SqlManager.sql("smslog.where"), SqlManager.sql("smslog.columnPage"), SqlManager.sql("smslog.wherePage")));
        } else {
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            String tmpWhere = " AND company = ? ";
            renderJson(dataTable(SqlManager.sql("smslog.column"), SqlManager.sql("smslog.where"), tmpWhere, params, SqlManager.sql("smslog.columnPage"), SqlManager.sql("smslog.wherePage")));
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        SmsLog smslog = SmsLog.dao.findById(id);
        setAttr("smslog", smslog);
    }

}
