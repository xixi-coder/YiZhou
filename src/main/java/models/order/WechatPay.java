package models.order;

import annotation.TableBind;
import base.models.BaseWechatpayLog;

/**
 * Created by admin on 2016/10/22.
 */
@TableBind(tableName = "dele_wechatpay_log")
public class WechatPay extends BaseWechatpayLog<WechatPay> {
    public static WechatPay dao = new WechatPay();
}
