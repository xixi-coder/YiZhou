package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import models.sys.SysLog;
import org.joda.time.DateTime;

/**
 * Created by Administrator on 2016/10/8.
 */
@Controller("/admin/sys/syslog")
public class SysLogController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable("select *", " from dele_sys_log WHERE 1=1 -- where \n ORDER BY create_time DESC ", "select count(*) c", " from dele_sys_log WHERE 1=1 -- where"));
    }

    public void item() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            SysLog sysLog = SysLog.dao.findById(id);
            setAttr("syslog", sysLog);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            SysLog sysLog = new SysLog();
            setAttr("syslog", sysLog);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void save() {
        SysLog sysLog = getModel(SysLog.class, "syslog");
        if (sysLog.getId() != null) {
            if (sysLog.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxError("修改失败");
            }
        } else {
            sysLog.setCreateTime(DateTime.now().toDate());
            sysLog.setOperater(getUserId());
            sysLog.setIpAddress("0:0:0:0:0:0:0:1");
            if (sysLog.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxError("添加失败！");
            }
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        SysLog sysLog = SysLog.dao.findById(id);
        if (sysLog.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

}
