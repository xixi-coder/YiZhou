package models.portModels;


import annotation.TableBind;
import base.portModels.BaseQuertz;

/**
 * 任务管理器
 */
@TableBind(tableName = "quertz")
public class Quertz extends BaseQuertz<Quertz> {
    public static final Quertz dao = new Quertz();

    /**
     * 查询最近需要更新的司机数据
     *
     * @return
     */
    public Quertz findDrivers() {
        return findFirst("SELECT driverId FROM `quertz` WHERE dUpdateTiem= (SELECT MAX(dUpdateTiem) FROM quertz)");
    }

    /**
     * 查询最近需要更新的订单数据
     *
     * @return
     */
    public Quertz findOrderNo() {
        return findFirst("SELECT orderNo FROM `quertz` WHERE dUpdateTiem= (SELECT MAX(dUpdateTiem) FROM quertz)");
    }
}
