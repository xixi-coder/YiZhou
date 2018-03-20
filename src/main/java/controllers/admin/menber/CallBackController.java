package controllers.admin.menber;

import annotation.Controller;
import base.controller.BaseAdminController;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/12/15.
 */
@Controller("/admin/member/callback")
public class CallBackController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable(SqlManager.sql("memberLogin.callback1"),SqlManager.sql("memberLogin.callback2")));
    }
}
