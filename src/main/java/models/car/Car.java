package models.car;

import annotation.TableBind;
import base.models.BaseBaseCar;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/4.
 */
@TableBind(tableName = "dele_base_car")
public class Car extends BaseBaseCar<Car> {
    public static Car dao = new Car();

    /**
     * 查询司机使用的车辆
     *
     * @param driver
     * @param status
     * @return
     */
    public List<Car> findByDriverAndStatus(int driver, int status) {
        return find(SqlManager.sql("car.findByDriverAndStatus"), driver, status);
    }

    /**
     * 查询司机正在接单的车辆
     *
     * @param driver
     * @return
     */
    public Car findByDriver(int driver) {
        return findFirst(SqlManager.sql("car.findByDriver"), driver);
    }

    /**
     * 验证车架号
     *
     * @param vin
     * @return
     */
    public boolean validateVin(String vin) {
        return findFirst(SqlManager.sql("car.validateVin"), vin) != null;
    }

    /**
     * 验证车牌号
     *
     * @param plateNumber
     * @return
     */
    public boolean validatePlateNumber(String plateNumber) {
        return findFirst(SqlManager.sql("car.validatePlateNumber"), plateNumber) != null;
    }

    /**
     * 车辆保险信息定义
     */
    public List<Car> vehicleinsurance() {
        return find(SqlManager.sql("baseinfo.vehicleinsurance"));
    }

    /**
     * 车辆基本信息定义
     **/
    public List<Car> vehicle() {
        return find(SqlManager.sql("baseinfo.vehicle"));
    }
}
