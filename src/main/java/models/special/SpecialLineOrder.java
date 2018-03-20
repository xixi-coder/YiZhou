package models.special;

import annotation.TableBind;
import base.models.BaseSpeciallineOrder;

/**
 * Created by BOGONj on 2016/8/23.
 */
@TableBind(tableName = "dele_specialline_order")
public class SpecialLineOrder extends BaseSpeciallineOrder<SpecialLineOrder> {
    public static SpecialLineOrder dao = new SpecialLineOrder();


}
