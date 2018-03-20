package models.car;

import annotation.TableBind;
import base.models.BaseCarInfo;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/4.
 */
@TableBind(tableName = "dele_car_info", pks = "car_id")
public class CarInfo extends BaseCarInfo<CarInfo> {
    public static CarInfo dao = new CarInfo();

    public CarInfo findByCar(int car) {
        return findFirst(SqlManager.sql("carInfo.findByCar"), car);
    }

    /**
     * 网约车车辆里程信息接口
     */
    public List<CarInfo> vehicletotalmile() {
        return find(SqlManager.sql("baseinfo.vehicletotalmile"));
    }


    //网约车车辆信息接口
    public List<CarInfo> findCarInfos(){
        return find(SqlManager.sql("carInfo.findCarInfos"));
    }

    public List<CarInfo> findCarDistance(){
        return find(SqlManager.sql("carInfo.findCarDistance"));
    }
}
