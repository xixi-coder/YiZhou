package models.driver;

import annotation.TableBind;
import base.models.BaseDriverApp;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
@TableBind(tableName = "dele_driver_app")
public class DriverApp extends BaseDriverApp<DriverApp> {
    public static DriverApp dao = new DriverApp();

    public List<DriverApp> driverapp() {
        return find(SqlManager.sql("baseinfo.driverapp"));
    }

}
