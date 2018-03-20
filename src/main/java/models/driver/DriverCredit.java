package models.driver;

import annotation.TableBind;
import base.models.BaseDriverCredit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
@TableBind(tableName = "dele_driver_credit")
public class DriverCredit extends BaseDriverCredit<DriverCredit> {
    public static DriverCredit dao = new DriverCredit();

    /***
     *驾驶员信誉信息接口
     * */
    public List<DriverCredit> driver() {
        return find(SqlManager.sql("rated.driver"));
    }

}
