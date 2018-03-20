package controllers.admin.sys;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.sys.Alarm;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
@Controller("/admin/sys/alarm")
public class AlarmController extends BaseAdminController {

    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("alarm.column"), SqlManager.sql("alarm.where")));
        } else {
            String sql = " AND al.company_id = ?";
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("alarm.column"), SqlManager.sql("alarm.where"), sql, params));
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        Alarm alarm = Alarm.dao.findById(id);
        setAttr("alarm", alarm);
    }

    public void save() {
        Alarm alarm = getModel(Alarm.class, "alarm");
        alarm.setUserId(getUserId());
        alarm.setUpdateTime(new Date());
        if (alarm.update()) {
            renderAjaxSuccess("回复成功");
        } else {
            renderAjaxError("回复失败");
        }

    }
}