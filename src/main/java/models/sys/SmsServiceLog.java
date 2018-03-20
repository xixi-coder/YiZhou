package models.sys;

import annotation.TableBind;
import base.models.BaseSmsServiceLog;

/**
 * Created by admin on 2016/10/25.
 */
@TableBind(tableName = "dele_sms_service_log")
public class SmsServiceLog extends BaseSmsServiceLog<SmsServiceLog> {
    public static SmsServiceLog dao = new SmsServiceLog();
}
