package models.sys;

import annotation.TableBind;
import base.models.BaseSmsLog;
import org.joda.time.DateTime;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_sms_log")
public class SmsLog extends BaseSmsLog<SmsLog> {
    public static SmsLog dao = new SmsLog();

    public SmsLog builder(String phone, int smsTmpId, String content, int company) {
        SmsLog smsLog = new SmsLog();
        smsLog.setPhone(phone);
        smsLog.setSendTime(DateTime.now().toDate());
        smsLog.setTmp(smsTmpId);
        smsLog.setStatus("发送成功");
        smsLog.setContent(content);
        smsLog.setCompany(company);
        return smsLog;
    }
}
