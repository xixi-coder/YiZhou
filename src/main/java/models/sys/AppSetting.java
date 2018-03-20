package models.sys;

import annotation.TableBind;
import base.models.BaseAppSetting;

/**
 * Created by Administrator on 2016/10/12.
 */
@TableBind(tableName = "dele_app_setting")
public class AppSetting extends BaseAppSetting<AppSetting> {
    public static AppSetting dao = new AppSetting();

    public AppSetting findFirst() {
        return findFirst("select * from dele_app_setting limit 1");
    }

}
