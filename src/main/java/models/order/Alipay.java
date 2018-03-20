package models.order;

import annotation.TableBind;
import base.models.BaseAlipayLog;

/**
 * Created by admin on 2016/10/22.
 */
@TableBind(tableName = "dele_alipay_log")
public class Alipay extends BaseAlipayLog<Alipay> {
    public static Alipay dao = new Alipay();
}
