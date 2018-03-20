package models.sys;

import annotation.TableBind;
import base.models.BaseChargeStandardMileage;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by BOGONj on 2016/8/25.
 */
@TableBind(tableName = "dele_charge_standard_mileage")
public class ChargeStandardMileage extends BaseChargeStandardMileage<ChargeStandardMileage> {
    public static ChargeStandardMileage dao = new ChargeStandardMileage();

    /**
     * 通过收费标准项的id查询公里数
     *
     * @param id
     * @return
     */
    public List<ChargeStandardMileage> findByChargeStandardItem(int id) {
        return find(SqlManager.sql("chargeStandardMileage.findByChargeStandardItem"), id);
    }

    /**
     * 查询公里数的加价信息
     *
     * @param id
     * @param distance
     * @return
     */
    public List<ChargeStandardMileage> findByChargeStandardItemAndMileage(int id, BigDecimal distance) {
        return find(SqlManager.sql("chargeStandardMileage.findByChargeStandardItemAndMileage"), id, distance.toString());
    }
}
