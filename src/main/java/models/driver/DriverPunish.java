package models.driver;

import annotation.TableBind;
import base.models.BaseDriverPunish;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
@TableBind(tableName = "dele_driver_punish")
public class DriverPunish extends BaseDriverPunish<DriverPunish> {
    public static DriverPunish dao = new DriverPunish();

    /***
     *驾驶员处罚信息接口
     * */
    public List<DriverPunish> driverpunish() {
        return find(SqlManager.sql("rated.driverpunish"));
    }

}
