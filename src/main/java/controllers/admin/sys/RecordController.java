package controllers.admin.sys;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
@Controller("/admin/sys/record")
public class RecordController extends BaseAdminController {
    public void index() {
        render("list");
    }

    public void list() {
        int type = getParaToInt(0,0);
        if (isSuperAdmin()) {
            if (type==0) {
                renderJson(dataTable(SqlManager.sql("traver.column1"),SqlManager.sql("traver.where1")));
            }else {
                renderJson(dataTable(SqlManager.sql("traver.column2"),SqlManager.sql("traver.where2")));
            }
        }else {
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            if (type==0) {
                renderJson(dataTable(SqlManager.sql("traver.column1"), StringsKit.replaceSql(SqlManager.sql("traver.where1"),"and dcompany = ?"),params));
            }else {
                renderJson(dataTable(SqlManager.sql("traver.column2"), StringsKit.replaceSql(SqlManager.sql("traver.where2"),"and dcompany = ?"),params));
            }
        }
    }

}
