package controllers.admin.sys;

import annotation.Controller;
import base.controller.BaseAdminController;
import models.sys.AppSetting;
import org.joda.time.DateTime;

/**
 * Created by Administrator on 2016/10/10.
 */
@Controller("/admin/sys/appsetting")
public class AppSettingController extends BaseAdminController {
    public void index() {
        AppSetting appSetting = AppSetting.dao.findFirst();
        setAttr("index", 4);
        setAttr("appSetting", appSetting);
    }

    public void save() {
        AppSetting appSetting = getModel(AppSetting.class, "appSetting");
        if (appSetting.getId() != null) {
            appSetting.setLastUpdate(DateTime.now().toDate());
            appSetting.setLastOperater(getUserId());
            if (appSetting.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxError("修改失败！");
            }
        } else {
            if (appSetting.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxError("添加失败");
            }
        }
    }
}
