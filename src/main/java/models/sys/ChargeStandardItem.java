package models.sys;

import annotation.TableBind;
import base.models.BaseChargeStandardItem;
import kits.TimeKit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/25.
 */
@TableBind(tableName = "dele_charge_standard_item")
public class ChargeStandardItem extends BaseChargeStandardItem<ChargeStandardItem> {
    public static ChargeStandardItem dao = new ChargeStandardItem();

    /**
     * 通过收费标准id查询拥有的子项
     *
     * @param id
     * @return
     */
    public List<ChargeStandardItem> findByChargeStandard(int id) {
        return find(SqlManager.sql("chargeStandardItem.findByChargeStandard"), id);
    }

    /**
     * 查询顺风车收费标准子项
     */
    public ChargeStandardItem findByCharge(int id) {
        return findFirst("select * from dele_charge_standard_item where charge_standard = ?",id);
    }

    /**
     * 查詢z
     *
     * @param id
     * @param setOutTime
     * @return
     */
    public ChargeStandardItem findByChargeStandardAndTime(int id, DateTime setOutTime) {
        setOutTime = DateTime.parse(setOutTime.toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
        ChargeStandardItem chargeStandardItem = findFirst(SqlManager.sql("chargeStandardItem.findByChargeStandardAndTime"), id, setOutTime.toDate());
        if(chargeStandardItem==null){
                chargeStandardItem = findFirst(SqlManager.sql("chargeStandardItem.findByChargeStandardAndTime"), id, setOutTime.plusDays(1).toDate());
        }if (chargeStandardItem==null){
            chargeStandardItem = findFirst(SqlManager.sql("chargeStandardItem.findByChargeStandardAndTime"), id, setOutTime.plusMinutes(-1).toDate());
        }
        return chargeStandardItem;
    }

}
