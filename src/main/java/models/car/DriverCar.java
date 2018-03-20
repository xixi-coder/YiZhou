package models.car;

import annotation.TableBind;
import base.Constant;
import base.models.BaseDriverCar;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */

@TableBind(tableName = "dele_driver_car", pks = "car")
public class DriverCar extends BaseDriverCar<DriverCar> {
    public static DriverCar dao = new DriverCar();

    /**
     * 查询司机使用的车辆
     *
     * @param driver
     * @return
     */
    public List<DriverCar> findByDriver(int driver) {

        return find(SqlManager.sql("driverCar."), driver, Constant.DriverCarStatus.ShengHeTongGuo);
    }

    public DriverCar findByCar(int car) {
        return findFirst(SqlManager.sql("driverCar.findByCar"), car);
    }


    /**
     * 查询司机的车辆
     *
     * @param car
     * @param driver
     * @return
     */
    public DriverCar findByCarAndDriver(int car, int driver) {
        return findFirst(SqlManager.sql("driverCar.findByCarAndDriver"), car, driver);
    }

    /**
     * 查询正在使用的车辆
     *
     * @param b
     * @return
     */
    public DriverCar findByDriverAndStatus(int driver, boolean b) {
        return findFirst(SqlManager.sql("driverCar.findByDriverAndStatus"), driver, b);
    }

    public int findCountNoAduit(boolean superAdmin, int companyId, int userId) {
        if (!superAdmin) {
            if (userId != 0) {
                String sqlStr = SqlManager.sql("driverCar.findCountNoAduit");
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.creater = ? ");
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company = ? ");
                return findFirst(sqlStr, userId, companyId).getNumber("c").intValue();
            } else {
                return findFirst(StringsKit.replaceSql(SqlManager.sql("driverCar.findCountNoAduit"), " AND ddi.company  = ? "), companyId).getNumber("c").intValue();
            }
        } else {
            return findFirst(SqlManager.sql("driverCar.findCountNoAduit")).getNumber("c").intValue();
        }
    }
}
