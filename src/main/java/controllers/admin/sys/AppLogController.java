package controllers.admin.sys;

import annotation.Controller;
import base.controller.BaseAdminController;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/9/6.
 */
@Controller("/admin/sys/appLog")
public class AppLogController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {

        String start = getPara("start");
        renderJson(dataTable(SqlManager.sql("appLog.column"), SqlManager.sql("appLog.where"), SqlManager.sql("appLog.columnPage"), SqlManager.sql("appLog.wherePage")));
    }

}
