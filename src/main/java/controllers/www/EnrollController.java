package controllers.www;

import utils.AESOperator;
import annotation.Controller;
import base.Constant;
import base.controller.BaseController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import kits.Md5Kit;
import kits.SmsKit;
import kits.StringsKit;
import kits.VerificationKit;
import models.company.Company;
import models.driver.DriverInfo;
import models.driver.DriverLicenseInfo;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.sys.Area;
import models.sys.ServiceType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/20.
 */
@Controller("/driver/enroll")
public class EnrollController extends BaseController {
    public void index() {
        setAttr("registerIp", getPara("registerIp", ""));
        setAttr("registerIMSI", getPara("registerIMSI", ""));
        setAttr("registerIMEI", getPara("registerIMEI", ""));
        setAttr("macAddress", getPara("macAddress", ""));
        setAttr("port", getPara("port", ""));
        String phone = getPara("phone", "");
        setAttr("phone", phone);
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("type", serviceTypes);
        setAttr("websocket", Constant.properties.getProperty("app.host"));
        List<Area> areaList = Area.dao.findByLevelAndParent("province", 0 + "");
        setAttr("province", areaList);
        render("/views/www/index.ftl");
    }

    public void save() {
        String name = getPara("name");
        int gender = getParaToInt("gender", 1);
        String tuijiashoujihao = getPara("tphone");
        int tuijianrenId = 0;
        if (!Strings.isNullOrEmpty(tuijiashoujihao)) {
            if (!VerificationKit.isMobile(tuijiashoujihao)) {
                renderAjaxError("推荐人手机号不正确!");
                return;
            }
            DriverInfo driverInfo = DriverInfo.dao.findByPhone(tuijiashoujihao);
            if (driverInfo == null) {
                renderAjaxError("推荐人不存在");
                return;
            }
            tuijianrenId = driverInfo.getLoginId();
        }
        String phone = getPara("phone");
        if (!VerificationKit.isMobile(phone)) {
            renderAjaxError("手机号不正确!");
            return;
        }

        String type = getPara("type");
        String driverLicense = getPara("driverLicense");
        String drivingLicense = getPara("drivingLicense");
        String qualification = getPara("qualification");

        String idCard = getPara("idCard");
        String smsCode = getPara("smsCode");
        int countByPhoneAndType = MemberLogin.dao.findCountByPhoneAndType(phone, Constant.DRIVER);
        if (countByPhoneAndType > 0) {
            renderAjaxError("手机号码已存在!");
            return;
        }
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null) {
            renderAjaxFailure("重新发送验证码!");
            return;
        }

        //注册验证码输入三次错误提示重新获取
        if (CacheKit.get(Constant.Security.SECURITY_DRIVER_REGISTER, phone) != null) {
            if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_DRIVER_REGISTER, phone).toString()) >= 3) {
                renderAjaxError("验证码输错次数过多,请重新获取验证码");
                return;
            }
        }

        if (!StringUtils.equals(smsCode, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
            //记录注册验证码错误次数
            int i = Constant.Security.SECURITY_COUNT;
            if (CacheKit.get(Constant.Security.SECURITY_DRIVER_REGISTER, phone) != null) {
                i = CacheKit.get(Constant.Security.SECURITY_DRIVER_REGISTER, phone);
                i++;
            }
            CacheKit.put(Constant.Security.SECURITY_DRIVER_REGISTER, phone, i);
            renderAjaxError("短信验证码不正确！");
            return;
        }

        final MemberLogin memberLogin = new MemberLogin();
        memberLogin.setUserName(phone);
        String salt = StringsKit.getSalt();
        memberLogin.setSalt(salt);
        String password = RandomStringUtils.randomNumeric(6);
        String md5Password = Md5Kit.MD5(password);
        String dbPassword = Md5Kit.MD5(md5Password + salt);
        memberLogin.setPassword(dbPassword);
        memberLogin.setType(Constant.DRIVER);
        memberLogin.setCreateTime(DateTime.now().toDate());
        memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
        memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
        final DriverInfo driverInfo = new DriverInfo();
        driverInfo.setPhone(phone);
        driverInfo.setRealName(name);
        if (name.length() > 2) {
            name = name.substring(0, 2);
            if (Constant.fristName.contains(name)) {
                driverInfo.setNickName(name + "师傅");
            } else {
                name = name.substring(0, 1);
                driverInfo.setNickName(name + "师傅");
            }
        } else if (name.length() >= 1) {
            name = name.substring(0, 1);
            driverInfo.setNickName(name + "师傅");
        } else {
            driverInfo.setNickName("师傅");
        }
        String areaCode = getPara("areaCode");
        Company company = Company.dao.findByCity(areaCode);
        if (company == null) {
            driverInfo.setCompany(1);
        } else {
            driverInfo.setCompany(company.getId());
        }
        driverInfo.setGender(gender);
        driverInfo.setType(type);
        if (tuijianrenId != 0) {
            driverInfo.setIntroducerLoginId(tuijianrenId);
        }

        // registerIp registerIMEI registerIMSI macAddress
        driverInfo.setRegisterIp(getPara("registerIp"));
        driverInfo.setRegisterImei(getPara("registerIMEI"));
        driverInfo.setRegisterImsi(getPara("registerIMSI"));
        driverInfo.setMacAddress(getPara("macAddress"));
        driverInfo.setPort(String.valueOf(getRequest().getRemotePort()));

        final DriverLicenseInfo driverLicenseInfo = new DriverLicenseInfo();
        driverLicenseInfo.setDriverLicense(driverLicense);
        driverLicenseInfo.setIdCard(idCard);
        driverLicenseInfo.setDrivingLicense(drivingLicense);
        driverLicenseInfo.setQualificationCertificate(qualification);
        driverLicenseInfo.setCreateTime(DateTime.now().toDate());
        driverLicenseInfo.setStatus(Constant.DataAuditStatus.CREATE);
        final MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
        memberCapitalAccount.setPhone(phone);
        memberCapitalAccount.setCreateTime(DateTime.now().toDate());
        memberCapitalAccount.setStatus(1);
        memberCapitalAccount.setAccount(phone);

        //如果查到用户，则更新数据
        DriverInfo driver = DriverInfo.dao.findByPhone(phone);
        if (driver != null) {
            driverInfo.setStatus(null);
            driverInfo.setId(driver.getId());
            driverInfo.setIntroducerLoginId(driver.getIntroducerLoginId());
            MemberLogin member = MemberLogin.dao.findByUserName(phone, Constant.DRIVER);
            if (member != null) {
                memberLogin.setId(member.getId());
                memberCapitalAccount.setLoginId(member.getId());
            }
            DriverLicenseInfo license = DriverLicenseInfo.dao.findByDriver(driver.getId());
            if (license != null) {
                driverLicenseInfo.setId(license.getId());
            }
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (driverInfo.getId() != null && memberLogin.getId() != null && driverLicenseInfo != null) {
                    if (memberLogin.update()) {
                        memberCapitalAccount.setLoginId(memberLogin.getId());
                        driverInfo.setLoginId(memberLogin.getId());
                        if (driverInfo.update()) {
                            driverLicenseInfo.setDriver(driverInfo.getId());
                            if (driverLicenseInfo.getId() == null) {
                                return driverLicenseInfo.save() && memberCapitalAccount.update();
                            } else {
                                return driverLicenseInfo.update() && memberCapitalAccount.update();
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    if (memberLogin.save()) {
                        memberCapitalAccount.setLoginId(memberLogin.getId());
                        driverInfo.setLoginId(memberLogin.getId());
                        if (driverInfo.save()) {
                            driverLicenseInfo.setDriver(driverInfo.getId());
                            return driverLicenseInfo.save() && memberCapitalAccount.save();
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        });
        if (driverInfo.getId() != null && memberLogin.getId() != null && driverLicenseInfo != null) {
            if (isOk) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxError("修改失败！");
            }
        } else {
            if (isOk) {
                renderAjaxSuccess("报名成功！");
            } else {
                renderAjaxError("报名失败！");
            }
        }
    }

    @ActionKey("/share")
    public void share() throws Exception {
        //临时解决办法,base64加密问题
        String phone = AESOperator.getInstance().decrypt(getPara("phone").replace(" ", "+"));
        int type = getParaToInt("type");
        setAttr("phone", phone);
        setAttr("type", type);
        render("/views/www/share.ftl");
    }

    @ActionKey("/share/wechat")
    public void wechat() {
        int type = getParaToInt("type");
        setAttr("type", type);
        render("/views/www/wechat.ftl");
    }

    public synchronized void register() {
        String tphone = getPara("tphone").trim();
        int type = getParaToInt("type");
        String phone = getPara("phone").trim();
        String code = getPara("code").trim();
        if (tphone.equals(phone)) {
            renderAjaxError("自己不能推荐自己");
            return;
        }
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null
                || !StringUtils.equals(code, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
            renderAjaxError("验证码不正确");
            return;
        }
        int tLoginId;
        if (type == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByPhone(tphone);
            tLoginId = driverInfo.getLoginId();
        } else {
            MemberInfo memberInfo = MemberInfo.dao.findByPhone(tphone);
            tLoginId = memberInfo.getLoginId();
        }
        MemberLogin me = MemberLogin.dao.findByUserName(phone, type);
        if (me != null) {
            renderAjaxError("手机号已被注册！");
            return;
        }
        boolean isOk = MemberLogin.dao.register(phone, RandomStringUtils.randomNumeric(4), null, type, tLoginId, getPara("registerIp"), getPara("registerIMSI"), getPara("registerIMEI"), getPara("macAddress"), String.valueOf(getRequest().getRemotePort()));
        MemberLogin memberLogin = MemberLogin.dao.findByUserName(phone, type);
        String salt = StringsKit.getSalt();
        memberLogin.setSalt(salt);
        String password = RandomStringUtils.randomNumeric(6);
        String md5Password = Md5Kit.MD5(password);
        String dbPassword = Md5Kit.MD5(md5Password + salt);
        memberLogin.setPassword(dbPassword);
        memberLogin.update();
        if (type == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByPhone(phone);
            SmsKit.enroll(memberInfo.getRealName(), memberInfo.getPhone(), password);
        }
        if (isOk) {
            renderAjaxSuccess("成功！");
        } else {
            renderAjaxError("操作失败！");
        }
    }

    /**
     * 获取省市县
     */
    public void area() {
        String parent = getPara("parent");
        String level = getPara("level");
        List<Area> areaList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(parent) && Strings.isNullOrEmpty(level)) {
            areaList = Area.dao.findByLevelAndParent("province", 0 + "");
        } else {
            areaList = Area.dao.findByLevelAndParent(level, parent);
        }
        renderAjaxSuccess(areaList);
    }

    public void t() {
        System.out.printf("===");
        CacheKit.put(Constant.Sms.SMS_CACHE, "123456", "123456");
        System.out.printf("===");
    }
}
