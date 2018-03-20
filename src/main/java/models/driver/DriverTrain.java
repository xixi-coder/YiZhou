package models.driver;

import annotation.TableBind;
import base.models.BaseDriverTrain;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_driver_train")
public class DriverTrain extends BaseDriverTrain<DriverTrain> {
    public static DriverTrain dao = new DriverTrain();

    /**
     * 网约车驾驶员培训信息接口
     *
     * @param phone
     * @return
     */
    public List<DriverTrain> drivereducate() {
        return find(SqlManager.sql("baseinfo.drivereducate"));
    }
}
