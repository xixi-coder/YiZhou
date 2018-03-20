package kits;

import base.Constant;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import models.company.Company;
import models.company.CompanyAccount;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.order.Order;
import models.sys.SmsLog;
import models.sys.SmsTmp;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.SmsHttpService;

import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/20.
 */
public class SmsKit {
    private static Logger logger = LoggerFactory.getLogger(SmsKit.class);
    
    /**
     * 注册短信发送
     *
     * @return boolean 是否发送成功
     */
    public static boolean register(String phone) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_REGISTER);
        String smsCode = RandomStringUtils.randomNumeric(6);
        String content = smsTmp.getContent().replace("CODE", smsCode).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp, content, smsCode);
    }
    
    /**
     * 登录
     *
     * @param phone
     * @param companyId
     * @return
     */
    public static boolean Login(String phone, int companyId) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_REGISTER);
        String smsCode = RandomStringUtils.randomNumeric(6);
        String content = smsTmp.getContent().replace("CODE", smsCode).replace("COMPANY", Constant.COMPANY);
        boolean isOk = false;
        if (companyId == 0) {
            isOk = send(phone, smsTmp, content, smsCode);
        } else {
            isOk = send(phone, Constant.Sms.SMS_TYPE_REGISTER, content, companyId);
            CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
            CacheKit.put(Constant.Sms.SMS_CACHE, phone, smsCode);
        }
        return isOk;
    }
    
    
    private static boolean send(String phone, SmsTmp smsTmp, String content, String smsCode) {
        try {
            SmsLog.dao.builder(phone, smsTmp.getId(), content, 0).save();
            CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
            CacheKit.put(Constant.Sms.SMS_CACHE, phone, smsCode);
            return SmsHttpService.getInstance().sendSms(content, phone);
        } catch (Exception e) {
            logger.error("短信注册短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
    private static boolean send(final String phone, final int smsTmpId, final String content, final int company) {
        try {
            final CompanyAccount companyAccount = CompanyAccount.dao.findById(company);
            if (companyAccount == null) {
                SmsLog.dao.builder(phone, smsTmpId, content, company).save();
                return SmsHttpService.getInstance().sendSms(content, phone);
            } else {
                int lastCount = companyAccount.getSmsCount() == null ? 0 : companyAccount.getSmsCount();
                if (lastCount <= 0) {
                    return true;
                } else {
                    companyAccount.setSmsCount(lastCount - 1);
                    boolean isOk = Db.tx(new IAtom() {
                        @Override
                        public boolean run() throws SQLException {
                            return companyAccount.update() && SmsHttpService.getInstance().sendSms(content, phone) && SmsLog.dao.builder(phone, smsTmpId, content, company).save();
                        }
                    });
                    return isOk;
                }
            }
        } catch (Exception e) {
            logger.error("短信注册短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
    /**
     * 更换手机号发送
     *
     * @return boolean 是否发送成功
     */
    public static boolean changePhone(String phone) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_CHANGEPH);
        String smsCode = RandomStringUtils.randomNumeric(6);
        String content = smsTmp.getContent().replace("CODE", smsCode).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp, content, smsCode);
    }
    
    /**
     * 忘记密码
     *
     * @param phone
     * @return
     */
    public static boolean forgetPassword(String phone) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_FORGETPW);
        String smsCode = RandomStringUtils.randomNumeric(6);
        String content = smsTmp.getContent().replace("CODE", smsCode).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp, content, smsCode);
    }
    
    /**
     * 司机接单收到的短信
     *
     * @param order
     * @return
     */
    public static boolean driverjiedan(Order order) {
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERJIEDAN, order.getCompany());
        if (smsTmp == null) {
            logger.info("{}:公司的司机接单模板没有设置", order.getCompany());
            return true;
        }
        String content = smsTmp.getContent().replace("NAME", driverInfo.getNickName() == null ? "" : driverInfo.getNickName()).replace("PHONE", driverInfo.getPhone() == null ? "" : driverInfo.getPhone()).replace("COMPANY", Constant.COMPANY);
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        return send(memberInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
    }
    
    /**
     * 司机到达短信通知
     *
     * @param order
     * @return
     */
    public static boolean driverarrived(Order order) {
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERDAODA, order.getCompany());
        if (smsTmp == null) {
            logger.info("{}:公司的司机到达短信通知模板没有设置", order.getCompany());
            return true;
        }
        String content = smsTmp.getContent().replace("NAME", driverInfo.getRealName()).replace("PLACE", order.getReservationAddress()).replace("PHONE", driverInfo.getPhone()).replace("COMPANY", Constant.COMPANY);
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        return send(memberInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
    }
    
    /**
     * 订单完成
     *
     * @param order
     * @return
     */
    public static boolean ordercomplete(Order order) {
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERCOMPLETEORDER, order.getCompany());
        if (smsTmp == null) {
            logger.info("{}:公司的订单完成短信通知模板没有设置", order.getCompany());
            return true;
        }
        String type = "";
        switch (order.getPayChannel()) {
            case Constant.PayType.Alipay:
                type = "支付宝";
                break;
            case Constant.PayType.Collection:
                type = "代付";
                break;
            case Constant.PayType.YE:
                type = "余额";
                break;
            case Constant.PayType.WECHAT:
                type = "微信";
                break;
        }
        Company company = Company.dao.findByCompanyId(order.getCompany());
        DecimalFormat format = new DecimalFormat("####.##");
        String minutes = order.getWaitTime() == null ? "0" : format.format(order.getWaitTime());
        String distance = order.getRealDistance() == null ? "0" : format.format(order.getDistance() == null ? 0 : order.getDistance());
        if (order.getServiceType() == Constant.ServiceType.DaiJia) {
            distance = order.getRealDistance() == null ? "0" : format.format(order.getRealDistance() == null ? 0 : order.getRealDistance());
        }
        if (order.getServiceType() == Constant.ServiceType.KuaiChe) {
            if (order.getPdFlag()) {
                distance = order.getRealDistance() == null ? "0" : format.format(order.getDistance());
            } else {
                distance = order.getRealDistance() == null ? "0" : format.format(order.getRealDistance());
                
            }
        }
        String amount = order.getRealPay() == null ? "0" : format.format(order.getRealPay());
        String content = smsTmp.getContent()
        .replace("NO", order.getNo())
        .replace("MINUTES", minutes)
        .replace("TYPE", type)
        .replace("DISTANCE", distance)
        .replace("AMOUNT", amount)
        .replace("TEL", company.getPhone())
        .replace("COMPANY", Constant.COMPANY);
        return send(memberInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
    }
    
    /**
     * 司机有新订单短信通知
     *
     * @param order
     * @return
     */
    public static boolean neworder(Order order) {
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERNEWORDER, order.getCompany());
        if (smsTmp == null) {
            logger.info("{}:公司的司机有新订单短信模板没有设置", order.getCompany());
            return true;
        }
        String serviceType = "";
        switch (order.getServiceType()) {
            case Constant.ServiceType.DaiJia:
                serviceType = "代驾";
                break;
            case Constant.ServiceType.ZhuanChe:
                serviceType = "专车";
                break;
            case Constant.ServiceType.KuaiChe:
                serviceType = "快车";
                break;
            case Constant.ServiceType.Taxi:
                serviceType = "出租车";
                break;
        }
        String content = smsTmp.getContent()
        .replace("TYPE", serviceType)
        .replace("NO", order.getNo())
        .replace("TIME", new DateTime(order.getSetouttime()).toString(DateTimeFormat.forPattern("MM月dd HH:mm")))
        .replace("PLACE", order.getReservationAddress())
        .replace("COMPANY", Constant.COMPANY);
        return send(driverInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
    }
    
    /**
     * 充值
     *
     * @param phone
     * @param realName
     * @param account
     * @param amount
     * @param company
     * @return
     */
    public static boolean rechange(String phone, String realName, String account, String amount, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERRECHANGE, company);
        if (smsTmp == null) {
            logger.info("{}:公司的短信充值短信模板没有设置", company);
            return true;
        }
        String content = smsTmp.getContent().replace("NAME", realName).replace("ACCOUNT", account).replace("AMOUNT", amount).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, company);
    }
    
    public static boolean enroll(String name, String phone, String password) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_DRIVERREGISTER);
        String content = smsTmp.getContent().replace("NICKNAME", name).replace("ACCOUNT", phone).replace("PASSWORD", password).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, 0);
    }
    
    public static boolean nomorlSms(String phone, String content, int companyId) {
        return send(phone, 0, content, companyId);
    }
    
    /**
     * 取消订单短信通知
     *
     * @param order
     * @return
     */
    public static boolean ordercancel(Order order) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_ORDERCANCEL);
        if (smsTmp == null) {
            logger.info("取消订单的短信模板没有设置。。。。。。。");
            return true;
        }
        String content = smsTmp.getContent().replace("NO", order.getNo()).replace("COMPANY", Constant.COMPANY);
        if (order.getDriver() != null) {
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            send(driverInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
        }
        if (order.getMember() != null) {
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            send(memberInfo.getPhone(), smsTmp.getId(), content, order.getCompany());
        }
        return true;
    }
    
    /**
     * 参加活动短信
     *
     * @param activityName
     * @param contents
     * @param phone
     * @return
     */
    public static boolean activity(String activityName, String contents, String phone) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_ACTIVITY);
        if (smsTmp == null) {
            logger.info("参与活动短信模板没有设置。。。。。。。");
            return true;
        }
        String content = smsTmp.getContent().replace("CONTENT", contents).replace("ACTIVITY", activityName).replace("COMPANY", Constant.COMPANY);
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(phone);
        send(memberInfo.getPhone(), smsTmp.getId(), content, memberInfo.getCompany());
        return true;
    }
    
    /**
     * 司机月不足
     *
     * @param realName
     * @param phone
     * @param amount
     * @param minamount
     * @param company
     * @return
     */
    public static boolean amountNotEnough(String realName, String phone, String amount, String minamount, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_AMOUNTNOTENOUGH);
        if (smsTmp == null) {
            logger.info("参与活动短信模板没有设置。。。。。。。");
            return true;
        }
        String content = smsTmp.getContent()
        .replace("NAME", realName)
        .replace("ACCOUNT", phone)
        .replace("AMOUNT", amount)
        .replace("MINAMOUNT", minamount)
        .replace("COMPANY", Constant.COMPANY);
        send(phone, smsTmp.getId(), content, company);
        return true;
    }
    
    
    /**
     * 保险单号短信
     *
     * @param realName
     * @param phone
     * @param company
     * @return
     */
    public static boolean baoxian(String realName, String phone, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_BAOXIAN);
        if (smsTmp == null) {
            return true;
        }
        String content = smsTmp.getContent().replace("NAME", realName).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, company);
    }
    
    /**
     * 提前15分钟提醒司机有预约订单
     *
     * @param order
     * @param phone
     * @param company
     * @return
     */
    public static boolean yuyuetosiji(Order order, String phone, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_YUYUETOSIJI);
        String content = smsTmp.getContent().replace("NO", order.getNo()).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, company);
    }
    
    /**
     * 预约订单通知会员
     *
     * @param order
     * @param phone
     * @param company
     * @return
     */
    public static boolean yuyuetohuiyuan(Order order, String phone, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_YUYUETOHUIYUAN);
        String content = smsTmp.getContent().replace("NO", order.getNo()).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, company);
    }
    
    /**
     * 预约取消
     *
     * @param order
     * @param phone
     * @param company
     * @return
     */
    public static boolean yuyuetosiji5(Order order, String phone, int company) {
        SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_YUYUETOSIJI5);
        String content = smsTmp.getContent().replace("NO", order.getNo()).replace("COMPANY", Constant.COMPANY);
        return send(phone, smsTmp.getId(), content, company);
    }
    
    /**
     * 微信下单
     *
     * @return
     */
    public static boolean weinxin(String phone) {
        try {
            SmsTmp smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_WEIXIN);
            String smsCode = RandomStringUtils.randomNumeric(6);
            String content = smsTmp.getContent().replace("CODE", smsCode).replace("COMPANY", Constant.COMPANY);
            return weisend(phone, smsTmp, content, smsCode);
        } catch (Exception e) {
            logger.error("微信下单短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
    private static boolean weisend(String phone, SmsTmp smsTmp, String content, String smsCode) {
        try {
            SmsLog.dao.builder(phone, smsTmp.getId(), content, 0).save();
            CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
            CacheKit.put(Constant.Sms.SMS_CACHE, phone, smsCode);
            return SmsHttpService.getInstance().sendSms(content, phone);
        } catch (Exception e) {
            logger.error("微信下单短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
    /**
     * 司机证件审核
     *
     * @return
     */
    public static boolean audit(String phone, int okFlag) {
        try {
            SmsTmp smsTmp;
            if (okFlag == 1) {
                smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_AUDIT_OK);
            } else {
                smsTmp = SmsTmp.dao.findByType(Constant.Sms.SMS_TYPE_AUDIT_ERROR);
            }
            String content = smsTmp.getContent().replace("COMPANY", Constant.COMPANY);
            return auditSend(phone, smsTmp, content);
        } catch (Exception e) {
            logger.error("审核短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
    private static boolean auditSend(String phone, SmsTmp smsTmp, String content) {
        try {
            SmsLog.dao.builder(phone, smsTmp.getId(), content, 0).save();
            CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
            return SmsHttpService.getInstance().sendSms(content, phone);
        } catch (Exception e) {
            logger.error("审核下单短信模板没有设置，请联系管理员设置模板");
            return false;
        }
    }
    
}
