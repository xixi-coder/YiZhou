package controllers.api;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Redis;
import jobs.activity.RegisterActivity;
import kits.*;
import models.car.DriverCar;
import models.company.Ad;
import models.company.Company;
import models.driver.DriverInfo;
import models.driver.Withdrawals;
import models.member.*;
import models.sys.*;
import models.vip.VipInfo;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.OnlineService;
import utils.AESOperator;
import utils.PortUtil;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * IndexController
 *
 * @author Administrator
 * @date 2016/8/20
 */
@Controller("/api")
@SuppressWarnings("All")
public class IndexController extends BaseApiController {

    static Logger logger = LoggerFactory.getLogger(IndexController.class);

    public void index() {
        renderText("aaa");
    }

    /**
     * 版本安全机制
     */
    @ActionKey("/api/getVersion")
    public void getVersion() {
        String versionStr = getPara("version");
        Version version = Version.dao.findVersionByNo(getAppType(), getDeviceType(), versionStr);
        if (version == null) {
            renderAjaxError("暂无数据");
        } else {
            renderAjaxSuccess(version);
        }
    }

    /**
     * @api {get} /api/getSms 获取登录短信
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} phone 手机号
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/getSms")
    public void getSms() {
        String phone = getPara("phone");
        try {
            phone = AESOperator.getInstance().decrypt(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!StringUtils.isEmpty(phone) && VerificationKit.isMobile(phone)) {
            int appType = getAppType();
            MemberLogin memberLogin = MemberLogin.dao.findByUserName(phone, appType);
            if (memberLogin != null) {
                if (appType == 1) {
                    renderAjaxError("司机端无法使用！");
                    return;
                } else {
                    MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());

                    //设置免登陆接口

                    if (phone.equals("18355407487")) {
                        CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
                        CacheKit.put(Constant.Sms.SMS_CACHE, phone, "123123");
                        renderAjaxSuccess("A", "", "尊敬的管理员请使用验证码：123123");
                        return;
                    } else if (phone.equals("18256889573")) {
                        CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
                        CacheKit.put(Constant.Sms.SMS_CACHE, phone, "123123");
                        renderAjaxSuccess("A", "", "尊敬的管理员请使用验证码：123123");
                        return;
                    }

                    if (memberInfo != null) {
                        SmsKit.Login(phone, memberInfo.getCompany() == null ? 0 : memberInfo.getCompany());
                    } else {
                        SmsKit.Login(phone, 0);
                    }
                }
            } else {
                SmsKit.Login(phone, 0);
            }
            Integer smsCode = Redis.use(Constant.LOGINMEMBER_CACHE).get("C" + phone);
            smsCode = smsCode == null ? 0 : smsCode;

            //获取验证码次数限制
            if (smsCode >= 3) {
                renderAjaxError("您获取验证码过于频繁请稍后再试！");
                return;
            }
            Redis.use(Constant.LOGINMEMBER_CACHE).setex("C" + phone, 200, ++smsCode);
            renderAjaxSuccess("发送成功！");
        } else {
            renderAjaxError("请输入正确的手机号码！");
        }
        return;
    }

    /**
     * 登陆接口
     */
    /**
     * @api {post} /api/login 用密码登录
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} phone
     * @apiParam {String} password 密码(16位md5)
     * @apiParam {String} landingIp  ip地址 *（*为可选字段）
     * @apiParam {String} landingIMEI  IMEI *
     * @apiParam {String} landingIMSI  IMSI  *
     * @apiParam {String} macAddress  mac地址  *
     * @apiParam {String} landingLongitude  经纬度  *
     * @apiParam {String} landingLatitude  经纬度  *
     * @apiParam {String} port  端口  *
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @Before(POST.class)
    public void login() {
        PortUtil.getIpAddr(getRequest());
        PortUtil.getPort(getRequest());
        String phone;
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            phone = AESOperator.getInstance().decrypt(getPara("phone"));
            if (Strings.isNullOrEmpty(phone)) {
                phone = AESOperator.getInstance().decrypt(getPara("username"));
            }
        } else {
            phone = getPara("phone");
        }
        String password = getPara("password");

        MemberLogin memberLogin = MemberLogin.dao.findByUserName(phone, getAppType());
        if (memberLogin == null) {
            renderAjaxError("用户名不存在！");
            return;
        }

        //登陆密码输入三次错误提示重置密码
        if (getAppType() == Constant.DRIVER) {
            if (CacheKit.get(Constant.Security.SECURITY_DRIVER_PASS, phone) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_DRIVER_PASS, phone).toString()) >= 3) {
                    renderAjaxError("输入密码错误次数过多，请重置密码");
                    return;
                }
            }
        } else {
            if (CacheKit.get(Constant.Security.SECURITY_MEMBER_PASS, phone) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_MEMBER_PASS, phone).toString()) >= 3) {
                    renderAjaxError("输入密码错误次数过多，请重置密码");
                    return;
                }
            }
        }

        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(MemberInfo.dao.findByLoginId(memberLogin.getId()).getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }
        boolean bind = false;
        if (adminSetting != null) {
            bind = adminSetting.getBindDeviceFlag();
        }
        memberLogin.setIp(IpKit.getRealIp(getRequest()));
        if (memberLogin.getDeviceNo() == null) {
            memberLogin.setDeviceNo(getDeviceNo());
            memberLogin.update();
        }

        if (bind) {
            if (!StringUtils.equals(getDeviceNo(), memberLogin.getDeviceNo()) && getAppType() == Constant.DRIVER) {
                renderAjaxError("登陆设备有变动,无法登陆！");
                return;
            }
        }

        if (memberLogin != null) {
            if (memberLogin.getCacheKey() != null) {
                if (Redis.use(Constant.LOGINMEMBER_CACHE).exists(memberLogin.getCacheKey())) {
                    Redis.use(Constant.LOGINMEMBER_CACHE).del(memberLogin.getCacheKey());
                    //renderAjaxError("用户已登录，无法重复登录");
                    //return;
                }
            }
            if (getAppType() == Constant.DRIVER) {
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
                if (driverInfo.getStatus() == null) {
                    renderAjaxError("账号未通过审核!");
                    return;
                }
                if (driverInfo.getStatus() == null || driverInfo.getStatus() == Constant.DriverStatus.FROZEN) {
                    renderAjaxError("账号被冻结!");
                    return;
                }
            }
            String dbPassword = memberLogin.getPassword();
            String salt = memberLogin.getSalt();
            if (StringUtils.equals(Md5Kit.MD5(password + salt), dbPassword)) {
                memberLogin.put("deviceType", getDeviceType());//放入设备类型
                memberLogin.setDeviceNo(getDeviceNo());
                memberLogin.setDeviceType(getDeviceType());
                //重置次数
                if (getAppType() == Constant.DRIVER) {
                    CacheKit.put(Constant.Security.SECURITY_DRIVER_PASS, phone, Constant.Security.SECURITY_COUNT);
                } else {
                    CacheKit.put(Constant.Security.SECURITY_MEMBER_PASS, phone, Constant.Security.SECURITY_COUNT);
                }

                //landing_ip  landing_IMEI landing_IMSI macAddress landingLongitude landingLatitude port

                String landingIp = getPara("landingIp", "0.0.0.0");
                String landingIMEI = getPara("landingIMEI", "00000000000000");
                String landingIMSI = getPara("landingIMSI", "00000000000000");
                String macAddress = getPara("macAddress", "30-84-54-9A-7E-0B");


                memberLogin.setLandingIp(landingIp);
                memberLogin.setLandingImei(landingIMEI);
                memberLogin.setLandingImsi(landingIMSI);
                memberLogin.setMacAddress(macAddress);

                memberLogin.setPort(String.valueOf(getRequest().getRemotePort())); //取得客户端的端口

                memberLogin.setLandingLongitude(Double.valueOf(getPara("landingLongitude", "0")));
                memberLogin.setLandingLatitude(Double.valueOf(getPara("landingLatitude", "0")));
                renderAjaxSuccess(ApiAuthKit.login(memberLogin));
            } else {
                //记录登陆密码错误次数
                int i = Constant.Security.SECURITY_COUNT;
                //判断登陆类型
                if (getAppType() == Constant.DRIVER) {
                    if (CacheKit.get(Constant.Security.SECURITY_DRIVER_PASS, phone) != null) {
                        i = CacheKit.get(Constant.Security.SECURITY_DRIVER_PASS, phone);
                        i++;
                    }
                    CacheKit.put(Constant.Security.SECURITY_DRIVER_PASS, phone, i);
                } else {
                    if (CacheKit.get(Constant.Security.SECURITY_MEMBER_PASS, phone) != null) {
                        i = CacheKit.get(Constant.Security.SECURITY_MEMBER_PASS, phone);
                        i++;
                    }
                    CacheKit.put(Constant.Security.SECURITY_MEMBER_PASS, phone, i);
                }
                renderAjaxError("用户名或者密码错误");
            }
        } else {
            renderAjaxError("用户名或者密码错误");
        }
    }

    /**
     * @api {post} /api/loginBySms  用验证码登录
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} phone 手机号
     * @apiParam {String} smsCode  验证码
     * @apiParam {String} landingIp  ip地址 *（*为可选字段）
     * @apiParam {String} landingIMEI  IMEI *
     * @apiParam {String} landingIMSI  IMSI  *
     * @apiParam {String} macAddress  mac地址  *
     * @apiParam {String} landingLongitude  经纬度  *
     * @apiParam {String} landingLatitude  经纬度  *
     * @apiParam {String} port  端口  *
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @Before(POST.class)
    public void loginBySms() {
        PortUtil.getIpAddr(getRequest());
        PortUtil.getPort(getRequest());
        String phone;
        String landingIp = getPara("landingIp", "0.0.0.0");
        String landingIMEI = getPara("landingIMEI", "00000000000000");
        String landingIMSI = getPara("landingIMSI", "000000000000");
        String macAddress = getPara("macAddress", "30-84-54-9A-7E-0B");
        String port = String.valueOf(getRequest().getRemotePort());//取得客户端的端口
        double landingLongitude = Double.valueOf(getPara("landingLongitude", "0"));
        double landingLatitude = Double.valueOf(getPara("landingLatitude", "0"));
        int smsCode = getParaToInt("smsCode", 0);
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            phone = AESOperator.getInstance().decrypt(getPara("phone"));
        } else {
            phone = getPara("phone");
        }
        //测试验证码
        //CacheKit.put(Constant.Sms.SMS_CACHE, phone, smsCode);
        Object c = CacheKit.get(Constant.Sms.SMS_CACHE, phone);
        int cacheCode = c == null ? 0 : Integer.valueOf(c.toString());
        int smsCount = 0;
        if (smsCount >= 3) {
            renderAjaxError("验证码失效，请重新获取！");
            CacheKit.remove(Constant.Sms.SMS_CACHE, phone);
            return;
        }
        if (smsCode != cacheCode) {
            renderAjaxError("登陆失败,验证码错误！");
            CacheKit.put(Constant.Sms.SMS_CACHE, "E" + phone, ++smsCount);
            return;
        } else {
            CacheKit.remove(Constant.Sms.SMS_CACHE, "E" + phone);
        }

        MemberLogin memberLogin = MemberLogin.dao.findByUserName(phone, getAppType());
        if (memberLogin == null && getAppType() == Constant.MEMBER) {
            boolean isOK = MemberLogin.dao.register(phone, "qwer1234", getDeviceNo(), getAppType(), 0, landingIp, landingIMSI, landingIMEI, macAddress, port);
            if (isOK) {
                logger.info("首次用短信登陆自动注册！");
                memberLogin = MemberLogin.dao.findByUserName(phone, getAppType());
            } else {
                renderAjaxError("登陆失败");
                return;
            }
        }

        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(MemberInfo.dao.findByLoginId(memberLogin.getId()).getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }
        boolean bind = false;
        if (adminSetting != null) {
            bind = adminSetting.getBindDeviceFlag();
        }
        try {

            memberLogin.setIp(IpKit.getRealIp(getRequest()));
            if (memberLogin.getDeviceNo() == null) {
                memberLogin.setDeviceNo(getDeviceNo());
                memberLogin.update();
            }
        } catch (NullPointerException e) {
            logger.error("新用户登陆");
        }


        if (bind) {
            if (!StringUtils.equals(getDeviceNo(), memberLogin.getDeviceNo()) && getAppType() == Constant.DRIVER) {
                renderAjaxError("登陆设备有变动,无法登陆！");
                return;
            }
        }
        if (memberLogin != null) {
            if (memberLogin.getCacheKey() != null) {
                if (Redis.use(Constant.LOGINMEMBER_CACHE).exists(memberLogin.getCacheKey())) {
                    Redis.use(Constant.LOGINMEMBER_CACHE).del(memberLogin.getCacheKey());
                }
            }
            if (getAppType() == Constant.DRIVER) {
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
                if (driverInfo.getStatus() == null) {
                    renderAjaxError("账号未通过审核!");
                    return;
                }
                if (driverInfo.getStatus() == null || driverInfo.getStatus() == Constant.DriverStatus.FROZEN) {
                    renderAjaxError("账号被冻结!");
                    return;
                }
            }
            memberLogin.put("deviceType", getDeviceType());//放入设备类型
            memberLogin.setDeviceNo(getDeviceNo());
            memberLogin.setDeviceType(getDeviceType());
            //重置次数
            if (getAppType() == Constant.DRIVER) {
                CacheKit.put(Constant.Security.SECURITY_DRIVER_PASS, phone, Constant.Security.SECURITY_COUNT);
            } else {
                CacheKit.put(Constant.Security.SECURITY_MEMBER_PASS, phone, Constant.Security.SECURITY_COUNT);
            }
            memberLogin.setLandingIp(landingIp);
            memberLogin.setLandingImei(landingIMEI);
            memberLogin.setLandingImsi(landingIMSI);
            memberLogin.setMacAddress(macAddress);
            memberLogin.setPort(port); //取得客户端的端口
            memberLogin.setLandingLongitude(landingLongitude);
            memberLogin.setLandingLatitude(landingLatitude);
            renderAjaxSuccess(ApiAuthKit.login(memberLogin));
        }

    }


    /**
     * 注册
     */
    @Before(POST.class)
    public void register() {
        String phone;
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            phone = AESOperator.getInstance().decrypt(getPara("phone"));
        } else {
            phone = getPara("phone");
        }
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxError("手机号码不存在！");
            return;
        }
        if (!VerificationKit.isMobile(phone)) {
            renderAjaxError("手机号码格式错误");
            return;
        }
        String password = getPara("password");
        String rePassword = getPara("rePassword");
        String smsCode = getPara("code");

        //注册验证码输入三次错误提示重新获取
        if (CacheKit.get(Constant.Security.SECURITY_MEMBER_REGISTER, phone) != null) {
            if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_MEMBER_REGISTER, phone).toString()) >= 3) {
                renderAjaxError("验证码输错次数过多,请重新获取验证码");
                return;
            }
        }

        //验证验证码
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null
                || !StringUtils.equals(smsCode, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
            //记录注册验证码错误次数
            int i = Constant.Security.SECURITY_COUNT;
            if (CacheKit.get(Constant.Security.SECURITY_MEMBER_REGISTER, phone) != null) {
                i = CacheKit.get(Constant.Security.SECURITY_MEMBER_REGISTER, phone);
                i++;
            }
            CacheKit.put(Constant.Security.SECURITY_MEMBER_REGISTER, phone, i);
            renderAjaxError("验证码不正确");
            return;
        }

        if (Strings.isNullOrEmpty(password)) {
            renderAjaxError("密码不能为空");
            return;
        }
        if (!StringUtils.equals(password, rePassword)) {
            renderAjaxError("两次输入的密码不一致！");
            return;
        }
        String tPhone = getPara("tphone");//推荐人手机号
        int tId = 0;
        if (!Strings.isNullOrEmpty(tPhone)) {
            if (!VerificationKit.isMobile(tPhone)) {
                renderAjaxError("推荐人手机号码格式不正确!");
                return;
            }
            MemberInfo tMember = MemberInfo.dao.findByPhone(tPhone);
            if (tMember == null) {
                renderAjaxError("推荐人不存在!");
                return;
            }
            tId = tMember.getLoginId();
        }
        if (MemberLogin.dao.register(phone, password, getDeviceNo(), getAppType(), tId, getPara("registerIp"), getPara("registerIMSI"), getPara("registerIMEI"), getPara("macAddress"), String.valueOf(getRequest().getRemotePort()))) {
            renderAjaxSuccess("注册成功");
        } else {
            renderAjaxFailure("注册失败");


        }
    }

    /**
     * 注册时发送短信验证码
     */
    /**
     * @api {post} /api/smscode   注册时发送短信验证码
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} phone  手机号
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    public void smscode() {
        String phone;

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            phone = AESOperator.getInstance().decrypt(getPara("phone"));
        } else {
            phone = getPara("phone");
        }
        int type = getParaToInt("type", 1);
        int c;
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxFailure("手机号码不存在");
            return;
        }

        //判断注册司机的状态
        if (getAppType() == Constant.DRIVER && getDeviceType() != 3) {
            DriverInfo driver = DriverInfo.dao.findByPhone(phone);
            if (driver != null) {
                if (driver.getStatus() != null) {
                    if (driver.getStatus() != 1) {
                        renderAjaxError("当前用户状态不是审核通过");
                        return;
                    }
                } else {
                    renderAjaxError("当前用户状态是未审核");
                    return;
                }
            }
        }

        //防止短信轰炸
        if (CacheKit.get(Constant.Security.SECURITY, phone) != null) {
            Date date = CacheKit.get(Constant.Security.SECURITY, phone);
            Date newDate = new Date();
            if ((newDate.getTime() - date.getTime()) / 1000 < 60) {
                renderAjaxFailure("短信获取过于频繁,请稍等");
                return;
            }
        }
        CacheKit.put(Constant.Security.SECURITY, phone, new Date());

        if (VerificationKit.isMobile(phone)) {
            switch (type) {
                case Constant.Sms.SMS_TYPE_REGISTER:
                    c = MemberLogin.dao.findCountByPhoneAndType(phone, getAppType());
                    if (c > 0) {
                        renderAjaxFailure("手机号码已存在");
                        return;
                    }
                    if (SmsKit.register(phone)) {
                        if (getAppType() == Constant.DRIVER) {
                            //注册短信发送重置次数
                            CacheKit.put(Constant.Security.SECURITY_DRIVER_REGISTER, phone, Constant.Security.SECURITY_COUNT);
                        } else {
                            //注册短信发送重置次数
                            CacheKit.put(Constant.Security.SECURITY_MEMBER_REGISTER, phone, Constant.Security.SECURITY_COUNT);
                        }
                        renderAjaxSuccess("发送成功");
                    } else {
                        renderAjaxFailure("短信发送失败");
                    }
                    break;
                case Constant.Sms.SMS_TYPE_CHANGEPH:
                    c = MemberLogin.dao.findCountByPhoneAndType(phone, getAppType());
                    if (c > 0) {
                        renderAjaxFailure("手机号码已存在");
                        return;
                    }
                    if (SmsKit.changePhone(phone)) {
                        renderAjaxSuccess("发送成功");
                    } else {
                        renderAjaxFailure("短信发送失败");
                    }
                    break;
                case Constant.Sms.SMS_TYPE_FORGETPW:
                    if (SmsKit.forgetPassword(phone)) {
                        if (getAppType() == Constant.DRIVER) {
                            //重置短信发送重置次数
                            CacheKit.put(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone, Constant.Security.SECURITY_COUNT);
                        } else {
                            //重置短信发送重置次数
                            CacheKit.put(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone, Constant.Security.SECURITY_COUNT);
                        }
                        renderAjaxSuccess("发送成功");
                    } else {
                        renderAjaxFailure("短信发送失败");
                    }
                    break;
                default:
                    break;
            }
        } else {
            renderAjaxFailure("手机号码格式错误");
        }
    }

    /**
     * 退出接口
     */
    public void logout() {
        ApiAuthKit.logout(getRequest().getHeader(Constant.TOKEN), getRequest().getHeader(Constant.SECRET), getAppType());
        renderAjaxSuccess("注销成功！");
    }

    /**
     * 获取服务类型
     */
    @ActionKey("/api/service/type")
    public void serviceType() {
       /*修改成获取当地公司的服务项,需要上传cityCode来判断是哪个公司
        Company company = Company.dao.findByCity(getPara("cityCode"));
        if(company==null){
        }*/
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        for (ServiceType serviceType : serviceTypes) {
            List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(serviceType.getId());
            serviceType.put("item", serviceTypeItems);
        }
        renderAjaxSuccess(serviceTypes);
    }

    /**
     * 获取版本信息
     */
    @ActionKey("/api/update")
    public void update() {
        Version version = Version.dao.findMaxVersion(getAppType(), getDeviceType());
        if (version == null) {
            renderAjaxNoData();
        } else {
            //获取version的size
            String uploadDir = Constant.properties.getProperty("attachment.dir", "");
            File file = new File(uploadDir.substring(0, uploadDir.indexOf("attachment")) + version.getFilePath());
            if (file.exists() && file.isFile()) {
                version.put("length", file.length());
            }
            //加密
            if (version.getFilePath() != null) {
                version.setFilePath(AESOperator.getInstance().encrypt(version.getFilePath()));
            }
            renderAjaxSuccess(version);
        }
    }

    /**
     * 广告接口
     */
    @Clear
    public void ad() {
        String cityCode = getPara("cityCode");
        int type = getParaToInt("type", 0);
        int appType;
        try {
            appType = getAppType();
        } catch (Exception e) {
            renderAjaxError("参数正确!");
            return;
        }
        Company company = Company.dao.findByCity(cityCode);
        List<Ad> ads;
        if (company != null) {
            ads = Ad.dao.findByCompanyAndType(company.getId(), appType, type);
            renderAjaxSuccess(ads);
        } else {
            //默认合肥
            ads = Ad.dao.findByCompanyAndType(2, appType, type);
            renderAjaxSuccess(ads);
        }
    }

    /**
     * 更新地区信息
     */
    public void updateCityCode() {
        String cityCode = getPara("cityCode");
        if (Strings.isNullOrEmpty(cityCode)) {
            renderAjaxError("参数不能为空!");
            return;
        }
        boolean isOk = false;
        switch (getAppType()) {
            case Constant.DRIVER:
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
                if (driverInfo.getCompany() != null) {
                    renderAjaxError("无需重复更新");
                    return;
                }
                EventKit.post(new RegisterActivity(getLoginMember()));//参加注册的活动
                if (driverInfo.getCompany() == null) {
                    Company company = Company.dao.findByCity(cityCode);
                    if (company == null) {
                        renderAjaxError("该地区无法使用！");
                        return;
                    }
                    driverInfo.setCompany(company.getId());
                    isOk = driverInfo.update();
                }
                break;
            case Constant.MEMBER:
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
                if (memberInfo.getCompany() != null) {
                    renderAjaxError("无需重复更新");
                    return;
                }
                if (memberInfo.getCompany() == null) {
                    Company company = Company.dao.findByCity(cityCode);
                    if (company == null) {
                        renderAjaxError("该地区无法使用！");
                        return;
                    }
                    memberInfo.setCompany(company.getId());
                    isOk = memberInfo.update();
                }
                break;
            default:
                break;
        }
        if (isOk) {
            EventKit.post(new RegisterActivity(getLoginMember()));//参加注册的活动
            renderAjaxSuccess("更新成功！");
        } else {
            renderAjaxError("更新失败！");
        }
    }

    /**
     * 获取用户的账户信息
     */
    public void accountbalance() {
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
        if (memberCapitalAccount == null) {
            memberCapitalAccount = new MemberCapitalAccount();
            memberCapitalAccount.setLoginId(getLoginMember().getId());
            memberCapitalAccount.setAmount(BigDecimal.ZERO);
            memberCapitalAccount.setPhone(getLoginMember().getUserName());
            memberCapitalAccount.setCreateTime(DateTime.now().toDate());
            memberCapitalAccount.setLastUpdateAmount(BigDecimal.ZERO);
            memberCapitalAccount.save();
        }
        renderAjaxSuccess(memberCapitalAccount);
    }

    /**
     * 获取账户信息
     */
    public void accountlog() {
        List<CapitalLog> logs = CapitalLog.dao.findByLoginId(getLoginMember().getId(), getPageStart(), getPageSize());
        renderAjaxSuccess(logs);
    }

    /**
     * 获取充值选项
     */
    public void rechangeItem() {
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(getMemberInfo().getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }
        renderAjaxSuccess(adminSetting);
    }

    /**
     * 我推荐的人信息
     */
    public void myreferee() {
        if (getAppType() == Constant.DRIVER) {
            List<DriverInfo> driverInfos = DriverInfo.dao.findByTj(getLoginMember().getId());
            renderAjaxSuccess(driverInfos);
        } else if (getAppType() == Constant.MEMBER) {
            List<MemberInfo> memberInfos = MemberInfo.dao.findByTj(getLoginMember().getId());
            renderAjaxSuccess(memberInfos);
        }
    }

    /**
     * 获取所属公司的信息
     */
    public void getSelfCompany() {
        Company company;
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(driverInfo.getCompany());
        } else {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        }
        renderAjaxSuccess(company);
    }

    /**
     * 获取收费标准
     */
    public void calRule() {
        Company company;
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(driverInfo.getCompany());
        } else {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        }
        int type = getParaToInt("serviceType", 1);
        ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company.getId(), type);
        renderAjaxSuccess(chargeStandard);
    }

    /**
     * 公告
     */
    @ActionKey("/api/notice")
    public void notice() {
        int typeId = getParaToInt("typeId", 1);
        Company company;
        List<Notice> notices = Lists.newArrayList();
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(driverInfo.getCompany());
            if (company != null) {
                notices = Notice.dao.findByCompanyAndDate(company.getId(), typeId, -3, DateTime.now().toDate());
            }
        } else {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
            if (company != null) {
                notices = Notice.dao.findByCompanyAndDate(company.getId(), typeId, -2, DateTime.now().toDate());
            }
        }
        renderAjaxSuccess(notices);
    }

    public void message() {
        List<MemberMessage> messageList = MemberMessage.dao.findByLoginId(getLoginMember().getId(), DateTime.now().plusDays(-7).toDate(), DateTime.now().toDate());
        renderAjaxSuccess(messageList);
    }

    /**
     * 意见反馈
     */
    public void callback() {
        CallBack c = new CallBack();
        c.setLoginId(getLoginMember().getId());
        c.setContent(getPara("content"));
        c.setAppType(getAppType());
        c.setCreateTime(DateTime.now().toDate());
        if (c.save()) {
            renderAjaxSuccess("提交成功,感谢您的反馈!");
        } else {
            renderAjaxError("提交失败!");
        }
    }

    /**
     * 获取意见反馈
     */
    public void getCallBack() {
        List<CallBack> callBacklist = CallBack.dao.findByLoginId(getMemberInfo().getLoginId());
        renderAjaxSuccess(callBacklist);
    }

    /**
     * 更新推送消息状态
     */
    public void updateMessage() {
        int id = getParaToInt("id", 0);
        if (id != 0) {
            MemberMessage memberMessage = MemberMessage.dao.findById(id);
            memberMessage.setReadFlag(1);
            if (memberMessage.update()) {
                renderAjaxSuccess("更新成功");
            } else {
                renderAjaxError("更新失败");
            }
        } else {
            renderAjaxError("更新失败");
        }
    }

    /**
     * 提现密码修改
     */
    public void tixianmimaxiugai() {
        String idCard;
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            idCard = AESOperator.getInstance().decrypt(getPara("idCard"));
        } else {
            idCard = getPara("idCard");
        }

        String password = getPara("password");
        String repassword = getPara("repassword");
        if (!password.equals(repassword)) {
            renderAjaxError("密码不一致！");
            return;
        }
        if (Strings.isNullOrEmpty(idCard)) {
            renderAjaxError("身份证号未填写！");
            return;
        }
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            if (!idCard.equals(driverInfo.getCertificateNo())) {
                renderAjaxError("身份证号不正确！");
                return;
            }
            driverInfo.setTxPassword(password);
            driverInfo.update();
            renderAjaxSuccess("修改成功！");
        } else if (getAppType() == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            if (!idCard.equals(memberInfo.getCertificateNo())) {
                renderAjaxError("身份证号不正确！");
                return;
            }
            memberInfo.setTxPassword(password);
            memberInfo.update();
            renderAjaxSuccess("修改成功！");
        }
    }

    /**
     * 验证提现密码
     */
    public void yanzhengtxmima() {
        String password = getPara("password");
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            if (!password.equals(driverInfo.getTxPassword())) {
                renderAjaxError("提现密码不正确！");
                return;
            }
            renderAjaxSuccess("验证通过！");
        } else if (getAppType() == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            if (!password.equals(memberInfo.getTxPassword())) {
                renderAjaxError("提现密码不正确！");
                return;
            }
            renderAjaxSuccess("验证通过！");
        }
    }

    /**
     * 获取可提现的金额
     */
    public void tixianjine() {
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
        BigDecimal txAmount = memberCapitalAccount.getTxAmount() == null ? BigDecimal.ZERO : memberCapitalAccount.getTxAmount();
        renderAjaxSuccess(txAmount.toString());
    }

    /**
     * 提现申请
     */
    public void tixian() {
        String bankNo;
        String backName;

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            bankNo = AESOperator.getInstance().decrypt(getPara("bankNo"));
            backName = AESOperator.getInstance().decrypt(getPara("backName"));
        } else {
            bankNo = getPara("bankNo");
            backName = getPara("bankName");
        }

        String khBack = getPara("khBack");
        String bankId = getPara("bankId");
        String amountStr = getPara("amount", "0");
        BigDecimal amount = new BigDecimal(amountStr);
        MemberCapitalAccount memberCapitalAccount = null;
        BigDecimal ketixianAmount = BigDecimal.ZERO;
        int company = 0;
        AdminSetting adminSetting;
        if (getAppType() == Constant.DRIVER) {
            company = DriverInfo.dao.findByLoginId(getLoginMember().getId()).getCompany();
            adminSetting = AdminSetting.dao.findByCompanyId(company);
            if (adminSetting == null) {
                renderAjaxError("未知的所属公司");
                return;
            } else {
                if (adminSetting.getTixianAmount() == null) {
                    if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
                        renderAjaxError("提现金额必须大于100");
                        return;
                    }
                } else {
                    if (amount.compareTo(BigDecimal.valueOf(adminSetting.getTixianAmount())) < 0) {
                        renderAjaxError("提现金额必须大于:" + adminSetting.getTixianAmount());
                        return;
                    }
                }
            }
        } else if (getAppType() == Constant.MEMBER) {
            company = MemberInfo.dao.findByLoginId(getLoginMember().getId()).getCompany();
            adminSetting = AdminSetting.dao.findByCompanyId(company);
            if (adminSetting == null) {
                renderAjaxError("未知的所属公司");
                return;
            } else {
                if (adminSetting.getTixianAmount() == null) {
                    if (amount.compareTo(BigDecimal.valueOf(100)) < 0) {
                        renderAjaxError("提现金额必须大于100");
                        return;
                    }
                } else {
                    if (amount.compareTo(BigDecimal.valueOf(adminSetting.getTixianAmount())) < 0) {
                        renderAjaxError("提现金额必须大于:" + adminSetting.getTixianAmount());
                        return;
                    }
                }
            }
        }
        memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
        ketixianAmount = memberCapitalAccount.getTxAmount() == null ? BigDecimal.ZERO : memberCapitalAccount.getTxAmount();
        if (ketixianAmount.compareTo(amount) < 0) {
            renderAjaxError("可提现金额不足！");
            return;
        }
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        memberCapitalAccount = MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, amount.multiply(BigDecimal.valueOf(-1)));
        memberCapitalAccount.setAmount(historyAmount.subtract(amount));
        memberCapitalAccount.setLastUpdateAmount(amount.multiply(BigDecimal.valueOf(-1)));
        final Withdrawals withdrawals = new Withdrawals();
        withdrawals.setAmount(amount);
        withdrawals.setStatus(0);
        withdrawals.setCreateTime(DateTime.now().toDate());
        withdrawals.setLoginId(memberCapitalAccount.getLoginId());
        withdrawals.setBankName(backName);
        withdrawals.setBankNo(bankNo);
        withdrawals.setFormType(Constant.FromType.APPTYPE);
        withdrawals.setCompany(company);
        withdrawals.setBankType(bankId);
        withdrawals.setKhBank(khBack);
        final MemberCapitalAccount finalMemberCapitalAccount = memberCapitalAccount;
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return finalMemberCapitalAccount.update() && withdrawals.save();
            }
        });
        if (isOk) {
            renderAjaxSuccess("申请成功！请耐心等待审核完成！");
        } else {
            renderAjaxError("申请失败！稍后再试！");
        }
    }

    /**
     * 查询银行名称信息
     */
    public void banks() {
        String bankName = getPara("bankName");
        String bankBin = getPara("bankBin", "0");
        List<Bank> banks = Bank.dao.findByPage(getPageStart(), getPageSize(), bankName, bankBin);
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(banks));
        renderAjaxSuccess(jsonArray);
    }

    /**
     * 提现历史
     */
    public void tixianjilu() {
        List<Withdrawals> withdrawalses = Withdrawals.dao.findByLoginIdAndPage(getLoginMember().getId(), getPageStart(), getPageSize());
        JSONArray jsonArray = JSON.parseArray(JsonKit.toJson(withdrawalses));
        renderAjaxSuccess(jsonArray);
    }

    /**
     * 判断是否登录
     */
    /**
     * @api {GET} /api/isLogin 判断是否存在登录信息（判断缓存中是否存在用户信息）
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "msg": "OK",
     * "data": null,
     * "status": "OK"
     * }
     */
    @ActionKey("/api/isLogin")
    public void isLogin() {
        MemberLogin login = getLoginMember();
        if (login != null) {
            renderAjaxSuccess("已登录");
        } else {
            renderAjaxError("未登录");
        }
    }

    /**
     * 判断是否上线
     */
    public synchronized void isOnline() {
        MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());

        DriverInfo driverInfo = null;
        try {
            driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
        } catch (NullPointerException e) {
            renderAjaxError("未登录");
            return;
        }


        int rStatus = ApiAuthKit.loginout(getLoginMember().getId());
        //如果redis中没有上线缓存数据  就强制下线当前司机
        if (rStatus == -1) {
            OnlineService.getInstance().offline(memberLogin, driverInfo);
            renderAjaxFailure("未上线");
            return;
        }
        if (memberLogin.getStatus() != Constant.LoginStatus.LOGOUTED && memberLogin.getStatus() != Constant.LoginStatus.LOGINED) {
            DriverCar driverCar = DriverCar.dao.findByDriverAndStatus(driverInfo.getId(), true);
            OnlineService.getInstance().offline(memberLogin, driverInfo);
            memberLogin = MemberLogin.dao.findById(memberLogin.getId());//这个操作6了，我也不知道为啥这么写  先留着
            Map<String, Object> results;
            if (driverCar != null) {
                results = OnlineService.getInstance().online(memberLogin, driverInfo, driverCar);
            } else {
                results = OnlineService.getInstance().online(memberLogin, driverInfo, null);
            }
            Boolean isOk = (Boolean) results.get("isOk");
            Map<String, Integer> result = (Map<String, Integer>) results.get("result");
            if (isOk) {
                try {
                    int typecode = ApiAuthKit.loginout(getLoginMember().getId());
                    result.put("type", typecode);
                } catch (NullPointerException e) {
                    result.put("type", -1);
                }

                renderAjaxSuccess(result);
            } else {
                renderAjaxFailure("未上线");
            }
        } else {
            renderAjaxFailure("未上线");
        }
    }

    /**
     * 报警接口
     */
    public void alarm() {
        Alarm alarm = new Alarm();
        alarm.setAlarmLoginId(getMemberInfo().getLoginId());
        alarm.setAlarmName(getMemberInfo().getRealName());
        alarm.setCompanyId(getMemberInfo().getCompany());
        alarm.setBackPhone(getMemberInfo().getPhone());
        alarm.setAlarmMessage(getPara("alarmMessage"));
        alarm.setCreateTime(new Date());
        alarm.setStatus(0);
        if (alarm.save()) {
            renderAjaxSuccess("消息已发送后台,请等待后台救援");
        } else {
            renderAjaxError("未知错误");
        }
    }

    /**
     * 添加pushId
     */
    /**
     * @api {post} /api/setPushId  设置推送id
     * @apiGroup A_基本接口
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} rId  极光
     * @apiParam {String} cId  个推
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "code": 200,
     * "data":“”,
     * "message": "已登录",
     * "isSuccess": true
     * }
     */
    @ActionKey("/api/setPushId")
    @Before(POST.class)
    public void setPushIds() {
        String rId = getPara("rId");
        String cId = getPara("cId");
        if (Strings.isNullOrEmpty(rId) && Strings.isNullOrEmpty(cId)) {
            renderAjaxError("极光id和个推id必须有一个不能为空！");
            return;
        }
        final MemberPushId memberPushId = new MemberPushId();
        memberPushId.setMemberId(getLoginMember().getId());
        memberPushId.setRegistrationId(rId);
        memberPushId.setCId(cId);
        memberPushId.setDeviceType(getDeviceType());
        MemberPushId memberPushId1 = MemberPushId.dao.findByMemberId(getLoginMember().getId());
        if (memberPushId1 != null) {
            memberPushId.setId(memberPushId1.getId());
            if (memberPushId.update()) {
                renderAjaxSuccess("修改完成");
            }
        } else if (memberPushId.save()) {
            renderAjaxSuccess("添加完成");
        }
    }

    /**
     * @api {get} /api/getServiceType  3.7.获取服务大类
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} cityCode  城市code
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/getServiceType")
    public void getServiceType() {
        String cityCode = getPara("cityCode");
        Company company;
        if (!Strings.isNullOrEmpty(cityCode)) {
            company = Company.dao.findCompanyByCity(cityCode);
            if (company == null) {
                logger.error("获取公司信息失败！使用默认值！");
                renderAjaxSuccess(getServiceTypeAndItem(ServiceType.dao.findAll()), "获取公司信息失败！使用默认值！");
                return;
            } else {
                List<ServiceType> list = ServiceType.dao.getServiceType(company.getId());
                if (list.size() < 1) {
                    logger.info("{},该地区没有服务了，使用默认值！", company.getName());
                    list = ServiceType.dao.findAll();
                }
                renderAjaxSuccess(getServiceTypeAndItem(list));
                return;
            }
        } else {
            logger.info("获取cityCode失败,使用默认值！");
            renderAjaxSuccess(getServiceTypeAndItem(ServiceType.dao.findAll()), "获取公司信息失败！使用默认值！");
        }

    }

    /**
     * @api {get} /api/getVipAmount  获取vip价格
     * @apiGroup A_IndexController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/getVipAmount")
    public void getVipAmount() {
        AdminSetting setting = AdminSetting.dao.findByCompanyId(getMemberInfo().getCompany());
        VipInfo vipInfo = VipInfo.dao.findByLoginId(getMemberInfo().getLoginId());
        Map resultMap = Maps.newHashMap();
        resultMap.put("vipAmount", setting.getVipAmount());
        resultMap.put("vipSingleSpendingAmount", setting.getVipSingleSpendingAmount());
        resultMap.put("balance", vipInfo == null ? 0 : vipInfo.getAmount());
        renderAjaxSuccess(resultMap);
    }

    /**
     * 给大类添加小类
     *
     * @param list
     * @return
     */
    private List<ServiceType> getServiceTypeAndItem(List<ServiceType> list) {
        for (ServiceType sysServiceType : list) {
            List<ServiceTypeItem> sysServiceTypeItem = ServiceTypeItem.dao.appFindByType(sysServiceType.getId());
            sysServiceType.put("item", sysServiceTypeItem);
        }
        return list;
    }

}