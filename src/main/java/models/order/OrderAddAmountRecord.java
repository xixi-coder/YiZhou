package models.order;

import annotation.TableBind;
import base.models.BaseAlipayLog;
import base.models.BaseOrderAddAmountRecord;

/**
 * Created by admin on 2016/10/22.
 */
@TableBind(tableName = "dele_order_add_amount_record")
public class OrderAddAmountRecord extends BaseOrderAddAmountRecord<OrderAddAmountRecord> {
    public static OrderAddAmountRecord dao = new OrderAddAmountRecord();
}
