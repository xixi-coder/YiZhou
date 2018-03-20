package controllers.admin.menber.drvier;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.driver.Grade;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
@Controller("/admin/member/driver/grade")
public class GradeController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("grade.column"), SqlManager.sql("grade.where")));
        } else {
            whereSql = "AND ddi.company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("grade.column"), SqlManager.sql("grade.where"), whereSql, params));
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        Grade grade = Grade.dao.findById(id);
        setAttr("grade", grade);
    }
}
