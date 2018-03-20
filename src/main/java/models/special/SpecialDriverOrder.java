package models.special;

import annotation.TableBind;
import base.models.BaseSpecialDriver;
import base.models.BaseSpecialdriverOrder;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/23.
 */
@TableBind(tableName = "dele_specialdriver_order")
public class SpecialDriverOrder extends BaseSpecialdriverOrder<SpecialDriverOrder> {
    public static SpecialDriverOrder dao = new SpecialDriverOrder();

    //设置司机接单状态
    public SpecialDriverOrder findOver(int specialdriverid,int OrderId){
        return findFirst(SqlManager.sql("specialdriverorder.findOver"),specialdriverid,OrderId);
    }
}
