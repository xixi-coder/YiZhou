package controllers.api;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import dto.JPushToMemberDto;
import dto.RateDto.OrdersRateDto;
import kits.Md5Kit;
import kits.StringsKit;
import kits.VerificationKit;
import kits.cache.HistoricalLocationCache;
import models.activity.Activity;
import models.activity.Coupon;
import models.activity.MemberActivity;
import models.activity.MemberCoupon;
import models.car.Car;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.count.DriverOrderStatistics;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.driver.Grade;
import models.member.*;
import models.order.Order;
import models.order.OrderTrip;
import models.sys.AdminSetting;
import models.vip.VipInfo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import services.OrderService;
import utils.AESOperator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static base.Constant.DECIMAL_FORMAT;

/**
 * Created by BOGONj on 2016/8/29.
 */
@Controller("/api/member")
public class MemberController extends BaseApiController {
    /**
     * 获取登陆用户信息
     */
    public void index() {
        MemberLogin memberLogin = getLoginMember();
        switch (getAppType()) {
            case Constant.MEMBER_TYPE_NORMAL:
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
                if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
                    if (memberInfo.getPhone() != null) {
                        String phone = AESOperator.getInstance().encrypt(memberInfo.getPhone());
                        memberInfo.setPhone(phone);
                    }
                    if (memberInfo.getEmail() != null) {
                        String email = AESOperator.getInstance().encrypt(memberInfo.getEmail());
                        memberInfo.setEmail(email);
                    }
                    if (memberInfo.getCertificateNo() != null) {
                        String certificateNo = AESOperator.getInstance().encrypt(memberInfo.getCertificateNo());
                        memberInfo.setCertificateNo(certificateNo);
                    }
                    if (memberInfo.getRealName() != null) {
                        String realName = AESOperator.getInstance().encrypt(memberInfo.getRealName());
                        memberInfo.setRealName(realName);
                    }
                    //是否是VIP用户
                    VipInfo vipInfo = VipInfo.dao.findByLoginId(memberInfo.getLoginId());
                    memberInfo.put("vipInfo", vipInfo);
                }
                renderAjaxSuccess(memberInfo);
                break;
            case Constant.MEMBER_TYPE_DRIVER:
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
                if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
                    if (driverInfo.getPhone() != null) {
                        String phone = AESOperator.getInstance().encrypt(driverInfo.getPhone());
                        driverInfo.setPhone(phone);
                    }
                    if (driverInfo.getEmail() != null) {
                        String email = AESOperator.getInstance().encrypt(driverInfo.getEmail());
                        driverInfo.setEmail(email);
                    }
                    if (driverInfo.getCertificateNo() != null) {
                        String certificateNo = AESOperator.getInstance().encrypt(driverInfo.getCertificateNo());
                        driverInfo.setCertificateNo(certificateNo);
                    }
                    if (driverInfo.getRealName() != null) {
                        String realName = AESOperator.getInstance().encrypt(driverInfo.getRealName());
                        driverInfo.setRealName(realName);
                    }
                }
                renderAjaxSuccess(driverInfo);
                break;
            default:
                renderAjaxError("未获取到APPtype");
        }
    }

    /**
     * 更新会员信息
     */
    public void update() {
        boolean isOk = false;
        MemberLogin memberLogin = getLoginMember();

        String realName;//真实姓名
        String certificateNo;//身份证号
        String address;//详细地址
        String email;//邮箱地址

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            realName = AESOperator.getInstance().decrypt(getPara("realName"));
            certificateNo = AESOperator.getInstance().decrypt(getPara("certificateNo"));
            address = AESOperator.getInstance().decrypt(getPara("address"));
            email = AESOperator.getInstance().decrypt(getPara("email"));
        } else {
            realName = getPara("realName");
            certificateNo = getPara("certificateNo");
            address = getPara("address");
            email = getPara("email");
        }

        String nickName = getPara("nickName");//昵称
        String birthday = getPara("birthday");//生日yyyy-MM-dd
        String province = getPara("province");//所属省份
        String city = getPara("city");//所属成城市
        String county = getPara("county");//所属县
        String postCode = getPara("postCode");//邮编
        String gender = getPara("gender");//性别1:男;0:女
        String headPortrait = getPara("headPortrait");//头像路径

        //实名认证
       /* if (!Strings.isNullOrEmpty(realName) && !Strings.isNullOrEmpty(certificateNo)) {
            JSONObject json = CertificationUtils.certificAtion(certificateNo, realName);
            if (json != null) {
                boolean isok = (Boolean) json.get("isok");
                if (!isok) {
                    renderAjaxError("实名认证失败！");
                    return;
                }
            } else {
                renderAjaxError("未知错误！");
                return;
            }
        }*/
        switch (getAppType()) {
            case Constant.MEMBER:
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
                if (!Strings.isNullOrEmpty(realName) && !Strings.isNullOrEmpty(certificateNo)) {
                    memberInfo.setRealName(realName);
                    memberInfo.setCertificateNo(certificateNo);
                }
                if (!Strings.isNullOrEmpty(nickName)) {
                    memberInfo.setNickName(nickName);
                }
                if (!Strings.isNullOrEmpty(province)) {
                    memberInfo.setProvince(province);
                }
                if (!Strings.isNullOrEmpty(city)) {
                    memberInfo.setCity(city);
                }
                if (!Strings.isNullOrEmpty(county)) {
                    memberInfo.setCounty(county);
                }
                if (!Strings.isNullOrEmpty(address)) {
                    memberInfo.setAddress(address);
                }
                if (!Strings.isNullOrEmpty(birthday)) {
                    DateTime dateTime = DateTime.parse(birthday);
                    memberInfo.setBirthday(dateTime.toDate());
                }
                if (!Strings.isNullOrEmpty(email)) {
                    memberInfo.setEmail(email);
                }
                if (!Strings.isNullOrEmpty(gender)) {
                    memberInfo.setGender(Ints.tryParse(gender));
                }
                if (!Strings.isNullOrEmpty(headPortrait)) {
                    memberInfo.setHeadPortrait(headPortrait);
                }
                if (!Strings.isNullOrEmpty(postCode)) {
                    memberInfo.setPostCode(postCode);
                }
                memberInfo.setLastUpdateTime(DateTime.now().toDate());
                isOk = memberInfo.update();
                break;
            case Constant.DRIVER:
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());

                if (!Strings.isNullOrEmpty(realName) && !Strings.isNullOrEmpty(certificateNo)) {
                    driverInfo.setRealName(realName);
                    driverInfo.setCertificateNo(certificateNo);
                }
                if (!Strings.isNullOrEmpty(nickName)) {
                    driverInfo.setNickName(nickName);
                }
                if (!Strings.isNullOrEmpty(province)) {
                    driverInfo.setProvince(province);
                }
                if (!Strings.isNullOrEmpty(city)) {
                    driverInfo.setCity(city);
                }
                if (!Strings.isNullOrEmpty(county)) {
                    driverInfo.setCounty(county);
                }
                if (!Strings.isNullOrEmpty(address)) {
                    driverInfo.setAddress(address);
                }
                if (!Strings.isNullOrEmpty(birthday)) {
                    DateTime dateTime = DateTime.parse(birthday);
                    driverInfo.setBirthday(dateTime.toDate());
                }
                if (!Strings.isNullOrEmpty(email)) {
                    driverInfo.setEmail(email);
                }
                if (!Strings.isNullOrEmpty(gender)) {
                    driverInfo.setGender(Ints.tryParse(gender));
                }
                if (!Strings.isNullOrEmpty(headPortrait)) {
                    driverInfo.setHeadPortrait(headPortrait);
                }
                if (!Strings.isNullOrEmpty(postCode)) {
                    driverInfo.setPostCode(postCode);
                }
                driverInfo.setLastUpdateTime(DateTime.now().toDate());
                isOk = driverInfo.update();
                break;
        }
        if (isOk) {
            renderAjaxSuccess("修改成功");
        } else {
            renderAjaxError("修改失败");
        }
    }

    /**
     * 修改密码
     */
    @ActionKey("/api/member/update/password")
    public void password() {
        MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        String userName = memberLogin.getUserName();

        //修改密码验证码输入三次错误提示
        if (getAppType() == Constant.DRIVER) {
            if (CacheKit.get(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName).toString()) >= 3) {
                    renderAjaxError("旧密码输错次数过多,请在主界面选择忘记密码");
                    return;
                }
            }
        } else {
            if (CacheKit.get(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName).toString()) >= 3) {
                    renderAjaxError("旧密码输错次数过多,请在主界面选择忘记密码");
                    return;
                }
            }
        }

        String password = getPara("password");
        String newPassword = getPara("newPassword");
        String rePassword = getPara("rePassword");
        if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(newPassword)) {
            renderAjaxError("密码不能为空！");
            return;
        }
        if (!StringUtils.equals(newPassword, rePassword)) {
            renderAjaxError("两次密码不一致！");
            return;
        }
        String salt = memberLogin.getSalt();
        password = Md5Kit.MD5(password + salt);
        if (StringUtils.equals(password, memberLogin.getPassword())) {
            salt = StringsKit.getSalt();
            memberLogin.setSalt(salt);
            String dbPassWord = Md5Kit.MD5(newPassword + salt);
            memberLogin.setPassword(dbPassWord);
            if (memberLogin.update()) {
                //重置次数
                if (getAppType() == Constant.DRIVER) {
                    CacheKit.put(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName, Constant.Security.SECURITY_COUNT);
                } else {
                    CacheKit.put(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName, Constant.Security.SECURITY_COUNT);
                }
                renderAjaxSuccess("密码修改成功！");
            } else {
                renderAjaxError("密码修改失败！");
            }
        } else {
            //记录修改验证码错误次数
            int i = Constant.Security.SECURITY_COUNT;
            if (getAppType() == Constant.DRIVER) {

                if (CacheKit.get(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName) != null) {
                    i = CacheKit.get(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName);
                    i++;
                }
                CacheKit.put(Constant.Security.SECURITY_DRIVER_UPDATEPASS, userName, i);
            } else {
                if (CacheKit.get(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName) != null) {
                    i = CacheKit.get(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName);
                    i++;
                }
                CacheKit.put(Constant.Security.SECURITY_MEMBER_UPDATEPASS, userName, i);
            }
            renderAjaxError("旧密码不正确！");
        }
    }

    /**
     * 修改手机号码
     */
    @ActionKey("/api/member/update/phone")
    public void phone() {
        final MemberLogin memberLogin = getLoginMember();
        final String phone = getPara("phone");
        if (VerificationKit.isMobile(phone)) {
            renderAjaxError("手机号格式错误！");
            return;
        }
        final String smsCode = getPara("code");
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null
                || !StringUtils.equals(smsCode, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
            renderAjaxError("验证码不正确");
            return;
        }
        boolean isOk = false;
        switch (memberLogin.getType()) {
            case Constant.MEMBER_TYPE_NORMAL:
                final MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
                if (StringUtils.equals(memberInfo.getPhone(), phone)) {
                    renderAjaxError("手机号和原来的一样！");
                    return;
                }
                if (MemberInfo.dao.findCountByPhone(phone) > 0) {
                    renderAjaxError("手机号已存在！");
                    return;
                }
                memberInfo.setLastUpdateTime(DateTime.now().toDate());
                memberInfo.setPhone(phone);
                memberLogin.setUserName(phone);
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        return memberInfo.update() && memberLogin.update();
                    }
                });
                break;
            case Constant.MEMBER_TYPE_DRIVER:
                final DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
                if (StringUtils.equals(driverInfo.getPhone(), phone)) {
                    renderAjaxError("手机号和原来的一样！");
                    return;
                }
                if (driverInfo.dao.findCountByPhone(phone) > 0) {
                    renderAjaxError("手机号已存在！");
                    return;
                }
                driverInfo.setLastUpdateTime(DateTime.now().toDate());
                driverInfo.setPhone(phone);
                memberLogin.setPassword(phone);
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        return driverInfo.update() && memberLogin.update();
                    }
                });
                break;
            default:
                break;
        }
        if (isOk) {
            renderAjaxSuccess("更换手机号码成功！");
        } else {
            renderAjaxFailure("更换手机号码失败！");
        }
    }

    /**
     * 重置密码
     */
    @ActionKey("/api/member/reset/password")
    public void resetpd() {
        final String phone;

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            phone = AESOperator.getInstance().decrypt(getPara("phone"));
        } else {
            phone = getPara("phone");
        }

        if (!VerificationKit.isMobile(phone)) {
            renderAjaxError("手机号格式错误！");
            return;
        }
        MemberLogin memberLogin = MemberLogin.dao.findByUserName(phone, getAppType());
        if (memberLogin == null) {
            renderAjaxError("用户不存在！");
            return;
        }
        String smsCode = getPara("code");

        //重置密码验证码输入三次错误提示重新获取
        if (getAppType() == Constant.DRIVER) {
            if (CacheKit.get(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone).toString()) >= 3) {
                    renderAjaxError("验证码输错次数过多,请重新获取验证码");
                    return;
                }
            }
        } else {
            if (CacheKit.get(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone) != null) {
                if (Integer.valueOf(CacheKit.get(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone).toString()) >= 3) {
                    renderAjaxError("验证码输错次数过多,请重新获取验证码");
                    return;
                }
            }
        }

        //重置密码验证码错误次数
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null
                || !StringUtils.equals(smsCode, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
            int i = Constant.Security.SECURITY_COUNT;
            if (getAppType() == Constant.DRIVER) {
                //记录验证码错误次数
                if (CacheKit.get(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone) != null) {
                    i = CacheKit.get(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone);
                    i++;
                }
                CacheKit.put(Constant.Security.SECURITY_DRIVER_RESETPDPASS, phone, i);
            } else {
                //记录验证码错误次数
                if (CacheKit.get(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone) != null) {
                    i = CacheKit.get(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone);
                    i++;
                }
                CacheKit.put(Constant.Security.SECURITY_MEMBER_RESETPDPASS, phone, i);
            }

            renderAjaxError("验证码不正确");
            return;
        }

        String password = getPara("password");
        String repassword = getPara("rePassword");
        if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(repassword)) {
            renderAjaxError("密码不能为空！");
            return;
        }
        if (!StringUtils.equals(repassword, password)) {
            renderAjaxError("两次输入的密码不一致！");
            return;
        }
        String salt = StringsKit.getSalt();
        String dbPassword = Md5Kit.MD5(password + salt);
        memberLogin.setSalt(salt);
        memberLogin.setPassword(dbPassword);
        if (memberLogin.update()) {
            if (getAppType() == Constant.DRIVER) {
                //重置登陆密码成功,重置次数为0
                CacheKit.put(Constant.Security.SECURITY_DRIVER_PASS, phone, Constant.Security.SECURITY_COUNT);
                CacheKit.put(Constant.Security.SECURITY_DRIVER_UPDATEPASS, phone, Constant.Security.SECURITY_COUNT);
            } else {
                //重置登陆密码成功,重置次数为0
                CacheKit.put(Constant.Security.SECURITY_MEMBER_PASS, phone, Constant.Security.SECURITY_COUNT);
                CacheKit.put(Constant.Security.SECURITY_MEMBER_UPDATEPASS, phone, Constant.Security.SECURITY_COUNT);
            }
            renderAjaxSuccess("密码重置成功！");
        } else {
            renderAjaxFailure("密码重置失败！");
        }
    }

    /**
     * 更新用户的位置
     * 1:把之前的位置转存到记录表
     * 2:记录当前的位置
     */
    @ActionKey("/api/member/update/location")
    public void location() {
        MemberLogin memberLogin = getLoginMember();
        String longitude = getPara("longitude");
        String latitude = getPara("latitude");
        String speed = getPara("speed");
        String orientation = getPara("orientation");//方位
        int type = getParaToInt("type", 4);
        String accuracy = getPara("accuracy", "-1");
        if (Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(speed)
                || Strings.isNullOrEmpty(orientation)
                ) {
            renderAjaxError("参数不能为空");
            return;
        }
        String timestamp = getPara("timestamp");
        Date reciveTime;
        if (Strings.isNullOrEmpty(timestamp)) {
            reciveTime = DateTime.now().toDate();
        } else {
            reciveTime = new DateTime(Longs.tryParse(timestamp)).toDate();
        }
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
        MemberRealLocation memberRealLocation = MemberRealLocation.dao.findByMember(memberInfo.getId());
        boolean isOk;
        if (memberRealLocation == null) {
            memberRealLocation = new MemberRealLocation();
            memberRealLocation.setMember(memberInfo.getId());
            memberRealLocation.setLatitude(Doubles.tryParse(latitude));
            memberRealLocation.setLongitude(Doubles.tryParse(longitude));
            memberRealLocation.setReciveTime(reciveTime);
            memberRealLocation.setSpeed(Doubles.tryParse(speed));
            memberRealLocation.setType(type);
            memberRealLocation.setOrientation(Doubles.tryParse(orientation));
            memberRealLocation.setAccuracy(Doubles.tryParse(accuracy));
            isOk = memberRealLocation.save();
        } else {
            final MemberHistoryLocation historyLocation = new MemberHistoryLocation();
            historyLocation.setReciveTime(memberRealLocation.getReciveTime());
            historyLocation.setLongitude(memberRealLocation.getLongitude());
            historyLocation.setLatitude(memberRealLocation.getLatitude());
            historyLocation.setSpeed(memberRealLocation.getSpeed());
            historyLocation.setMember(memberRealLocation.getMember());
            historyLocation.setType(memberRealLocation.getType());
            historyLocation.setOrientation(memberRealLocation.getOrientation());
            historyLocation.setAccuracy(memberRealLocation.getAccuracy());

            memberRealLocation.setMember(memberInfo.getId());
            memberRealLocation.setSpeed(Doubles.tryParse(speed));
            memberRealLocation.setLatitude(Doubles.tryParse(latitude));
            memberRealLocation.setLongitude(Doubles.tryParse(longitude));
            memberRealLocation.setMember(memberInfo.getId());
            memberRealLocation.setType(type);
            memberRealLocation.setOrientation(Doubles.tryParse(orientation));
            memberRealLocation.setReciveTime(reciveTime);
            memberRealLocation.setAccuracy(Doubles.tryParse(accuracy));
            final MemberRealLocation finalMemberRealLocation = memberRealLocation;
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return finalMemberRealLocation.update() && historyLocation.save();
                }
            });
        }
        if (isOk) {
            renderAjaxSuccess("更新位置成功！");
        } else {
            renderAjaxError("更新位置失败！");
        }
    }

    /**
     * 获取周围的车辆
     */
    @ActionKey("/api/member/cars")
    public void cars() {
        String latitude = getPara("latitude");//当前纬度
        String longitude = getPara("longitude");//当前经度
        int type = getParaToInt("type", 0);
        int serviceType = getParaToInt("serviceType", 0);
        if (Strings.isNullOrEmpty(latitude) ||
                Strings.isNullOrEmpty(longitude)
                ) {
            renderAjaxError("参数不能为空！");
            return;
        }
        List<DriverInfo> driverInfos = DriverInfo.dao.findByLocation(latitude, longitude, type, serviceType);
        renderAjaxSuccess(driverInfos);
    }

    /**
     * @api {get} /api/member/conpous  （会员接口）获取我的优惠券
     * @apiGroup conpous
     * @apiSuccessExample Success-Response:
     * HTTP/1.1
     * {
     * "status", "OK",
     * "data", data,
     * "msg", message,
     * }
     * @apiSuccess {number} couponId 优惠券ID
     * @apiSuccess {string} startTime 优惠券使用日期范围
     * @apiSuccess {string} endTime  优惠券使用日期范围
     * @apiSuccess {number} couponType 优惠券类型（1.普通券 2.折扣券）
     * @apiSuccess {number} baseAmount 满此价格可用（普通券/折扣券）
     * @apiSuccess {number} amount 优惠券金额（普通券）
     * @apiSuccess {number} serviceType 该券可用服务类型（0.通用优惠券 1.专车 2.代驾 3.出租车 4.快车 5.顺风车 6.城际专线 7.航空专线）
     * @apiSuccess {number} percent （折扣券）
     * @apiSuccess {number} percentAmount 最多可折扣金额（折扣券）
     * @apiSuccess {string} couponTitle 优惠券名称
     */
    public void conpous() {
        Date now = DateTime.now().toDate();
        List<MemberCoupon> memberCoupons = MemberCoupon.dao.findByLoginId(getLoginMember().getId(), now);
        renderAjaxSuccess(memberCoupons);
    }

    /**
     * @api {get} /api/member/getCouponForPay  （会员接口）支付订单获取可用优惠券
     * @apiGroup conpous
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {number} orderId 订单Id
     * @apiSuccessExample Success-Response:
     * HTTP/1.1
     * {
     * "status", "OK",
     * "data", data,
     * "msg", message,
     * }
     * @apiSuccess {number} couponId 优惠券ID
     * @apiSuccess {string} startTime 优惠券使用日期范围
     * @apiSuccess {string} endTime  优惠券使用日期范围
     * @apiSuccess {number} couponType 优惠券类型（1.普通券 2.折扣券）
     * @apiSuccess {number} baseAmount 满此价格可用（普通券/折扣券）
     * @apiSuccess {number} amount 优惠券金额（普通券）
     * @apiSuccess {number} serviceType 该券可用服务类型（0.通用优惠券 1.专车 2.代驾 3.出租车 4.快车 5.顺风车 6.城际专线 7.航空专线）
     * @apiSuccess {number} percent 折扣（折扣券）
     * @apiSuccess {number} percentAmount 最多可折扣金额（折扣券）
     * @apiSuccess {string} couponTitle 优惠券名称
     */
    public void getCouponForPay() {
        int orderId = getParaToInt("orderId");
        Order order = Order.dao.findById(orderId);
        List<MemberCoupon> memberCoupons = new ArrayList<>();
        if (null != order) {
            memberCoupons = MemberCoupon.dao.getCouponForPay(order, getLoginMember().getId());
        }
        renderAjaxSuccess(memberCoupons);
    }

    /**
     * 获取我的订单
     */
    public void getOrder() {
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        List<Order> orders = Order.dao.findByMemberForNoPay(memberInfo.getId());
        if (orders == null || orders.size() <= 0) {
            renderAjaxError("没有未完成的订单");
            return;
        }
        Order order = orders.get(0);

        //vip用户处理
        if (order.getServiceType() != Constant.ServiceType.DaiJia && order.getType() != Constant.ServiceItemType.DaiJia) {
            if (order.getVipActivityFlag() == Constant.VipActivityFlag.ACTIVITY_KQC) {
                AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
                if (order.getRealPay().compareTo(adminSetting.getVipSingleSpendingAmount()) <= 0) {
                    order.setRealPay(BigDecimal.ZERO);
                    order.setAmount(BigDecimal.ZERO);
                } else {
                    order.setRealPay(order.getRealPay().subtract(adminSetting.getVipSingleSpendingAmount()));
                    order.setAmount(order.getAmount().subtract(adminSetting.getVipSingleSpendingAmount()));
                }
            }
        }


        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        driverInfo = DriverInfo.dao.findById(driverInfo.getId());
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        Car car = Car.dao.findByDriver(driverInfo.getId());
        JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
        jPushToMemberDto.setDriverId(driverInfo.getId());
        jPushToMemberDto.setLatitude(driverRealLocation.getLatitude() + "");
        jPushToMemberDto.setLongitude(driverRealLocation.getLongitude() + "");
        jPushToMemberDto.setNickName(driverInfo.getRealName());
        jPushToMemberDto.setTime(order.getCreateTime());
        jPushToMemberDto.setPraise(Double.valueOf(DECIMAL_FORMAT.format(Grade.dao.driverFavorableRate(driverInfo.getId()))));//TODO 添加司机好评等级

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            jPushToMemberDto.setPhone(AESOperator.getInstance().encrypt(driverInfo.getPhone()));
        } else {
            jPushToMemberDto.setPhone(driverInfo.getPhone());
        }

        //添加车辆使用年限
        if (car != null) {
            if (car.getCertifyDateB() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startTime = sdf.format(car.getCertifyDateB());
                String endTime = sdf.format(new Date());
                String[] arg1 = startTime.split("-");
                String[] arg2 = endTime.split("-");
                int year1 = Integer.valueOf(arg1[0]);
                int year2 = Integer.valueOf(arg2[0]);
                int month1 = Integer.valueOf(arg1[1]);
                int month2 = Integer.valueOf(arg2[1]);
                int day1 = Integer.valueOf(arg1[2]);
                int day2 = Integer.valueOf(arg2[2]);
                int md = 0;
                if (year1 != year2) {
                    md = day2 > day1 ? 0 : -1;
                }
                int diffMonth = (year2 * 12 + month2) - (year1 * 12 + month1) + md;
                int yearNum = diffMonth / 12;
                int monthNum = diffMonth % 12;
                String carDurableYears = "";
                if (yearNum != 0) {
                    carDurableYears += yearNum + "年";
                }
                if (monthNum != 0) {
                    carDurableYears += monthNum + "个月";
                }
                car.put("carDurableYears", carDurableYears);
            } else {
                Random r = new Random();
                String carDurableYears = "";
                if (r.nextInt(5) != 0) {
                    carDurableYears += r.nextInt(5) + "年";
                }
                if (r.nextInt(12) != 0) {
                    carDurableYears += r.nextInt(5) + "个月";
                }
                car.put("carDurableYears", carDurableYears);
            }
        }

        jPushToMemberDto.setCar(car);
        jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
        String odcontent = JsonKit.toJson(jPushToMemberDto);
        JSONObject od = JSONObject.parseObject(odcontent);
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        String tt = JSONObject.toJSONString(orderTrip);
        JSONObject trip = JSONObject.parseObject(tt);
        od.put("trip", trip);
        od.put("reservationAddress", order.getReservationAddress());
        od.put("destination", order.getDestination());
        od.put("amount", order.getAmount());
        od.put("status", order.getStatus());
        od.put("orderId", order.getId());
        od.put("realPay", order.getRealPay() == null ? BigDecimal.ZERO : order.getRealPay());
        od.put("payChannel", order.getPayChannel());
        od.put("waitTime", order.getWaitTime());
        od.put("type", order.getServiceType());
        od.put("driverOrderCount", (DriverOrderStatistics.dao.findByDriverId(order.getDriver()) == null ? 0 : DriverOrderStatistics.dao.findByDriverId(order.getDriver()).getOrderNum()));

        renderAjaxSuccess(od);
    }

    /**
     * 获取执行中我的订单
     */
    public void executeOrder() {
        Integer id = getParaToInt("id");
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        List<Order> orders = Order.dao.findByMemberForExecute(memberInfo.getId(), id);
        if (orders == null || orders.size() <= 0) {
            renderAjaxError("没有未完成的订单");
            return;
        }
        Order order = orders.get(0);
        renderAjaxSuccess(order);
    }

    public void amount() {
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
        Map<String, Object> result = Maps.newHashMap();
        result.put("ye", memberCapitalAccount.getAmount());
        renderAjaxSuccess(result);
    }

    /**
     * 获取司机位置信息
     */
    public void getDriverLocation() {
        int driverId = 0;
        try {
            driverId = getParaToInt("driverId", 0);
        } catch (Exception e) {
            renderAjaxError("司机信息不存在");
        }
        if (driverId != 0) {
            DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverId);
            renderAjaxSuccess(driverRealLocation);
        } else {
            renderAjaxError("司机信息不存在");
        }
    }

    /**
     * 领取优惠券，通过劵号领取
     */
    public void exchange() {
        String code = getPara("code");
        MemberCoupon memberCoupon = MemberCoupon.dao.findByCodeAndDate(code, DateTime.now().toDate());
        if (memberCoupon == null) {
            renderAjaxError("券号不存在");
            return;
        }
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());

        memberCoupon.setLoginId(getLoginMember().getId());
        memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
        memberCoupon.setGainTime(DateTime.now().toDate());
        CompanyAccount companyAccount = CompanyAccount.dao.findById(memberInfo.getCompany());
        if (null == memberCoupon.getType() || memberCoupon.getType() == Constant.CouponType.Normal) {
            if (CompanyAccount.dao.amountEnough2(companyAccount, memberCoupon.getAmount())) {
                CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.CODECOUPON, memberCoupon.getAmount(), memberInfo.getCompany(), memberInfo.getLoginId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "后台给司机奖励余额");
                companyActivity.save();
                if (memberCoupon.update()) {
                    renderAjaxSuccess("优惠券领取成功!");
                } else {
                    renderAjaxError("领取失败！");
                }
            } else {
                renderAjaxError("领取失败！请联系当地公司");
            }
        } else {
            if (CompanyAccount.dao.amountEnough2(companyAccount, memberCoupon.getAmount())) {
                CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.CODECOUPON, BigDecimal.ZERO, memberInfo.getCompany(), memberInfo.getLoginId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "后台给司机奖励余额");
                companyActivity.save();
                if (memberCoupon.update()) {
                    renderAjaxSuccess("优惠券领取成功!");
                } else {
                    renderAjaxError("领取失败！");
                }
            } else {
                renderAjaxError("领取失败！请联系当地公司");
            }
        }
    }

    /**
     * 评价
     */
    public void grand() {
        int orderId = getParaToInt("orderId");
        String content = getPara("content");
        BigDecimal score = new BigDecimal(getPara("score", "0"));
        Order order = Order.dao.findById(orderId);
        if (order == null) {
            renderAjaxError("订单不存在！");
            return;
        }
        if (Grade.dao.countByOrder(orderId) >= 1) {
            renderAjaxError("订单已经评论！");
            return;
        }
        if (Strings.isNullOrEmpty(content)) {
            renderAjaxError("评价内容不存！");
            return;
        }
        if (score.compareTo(BigDecimal.ZERO) == 0) {
            renderAjaxError("评分格式不对！");
            return;
        }
        Grade grade = new Grade();
        grade.setScore(score.doubleValue());
        grade.setContent(content);
        grade.setOrder(orderId);
        grade.setMasterId(order.getDriver());
        grade.setMemberId(order.getMember());
        grade.setCreateTime(DateTime.now().toDate());
        if (grade.save()) {
            renderAjaxSuccess("评价成功！");
        } else {
            renderAjaxError("提交失败");
        }
    }

    /**
     * 查看有没有相关的打开app活动
     */
    public void joinActivity() {
        DateTime now = DateTime.now();
        int companyId = 0;
        if (getAppType() == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            companyId = memberInfo.getCompany() == null ? 0 : memberInfo.getCompany();
        } else if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            companyId = driverInfo.getId();
        }
        List<Activity> activities = Activity.dao.findByEvent(Constant.ActivityEvent.OPENAPP, now, companyId);
        List<Activity> realActivity = Lists.newArrayList();
        if (activities != null && activities.size() > 0) {
            for (Activity activity : activities) {
                if (MemberActivity.dao.isJoin(getLoginMember().getId(), activity.getId())) {
                    realActivity.add(activity);
                }
            }
            renderAjaxSuccess(realActivity);
        } else {
            renderAjaxError("没有相关活动");
        }
    }

    /**
     * 领取活动奖励
     */
    public synchronized void award() {
        int activityId = getParaToInt("activityId", 0);
        if (activityId != 0) {
            Activity activity = Activity.dao.findById(activityId);
            final MemberActivity memberActivity = MemberActivity.dao.create(getLoginMember().getId(), getAppType(), activityId);
            Coupon coupon = Coupon.dao.findById(activity.getCoupon());
            if (coupon.getCouponType() == Constant.CouponType.Discount) {
                final MemberCoupon memberCoupon = MemberCoupon.dao.create(coupon, "打开APP活动赠送", getLoginMember().getId());
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
                CompanyAccount companyAccount = CompanyAccount.dao.findById(memberInfo.getCompany());
                if (CompanyAccount.dao.amountEnough(companyAccount, BigDecimal.ZERO, Constant.CompanyAccountActivity.awardMember)) {
                    boolean isOk = Db.tx(new IAtom() {
                        @Override
                        public boolean run() throws SQLException {
                            return memberActivity.save() && memberCoupon.save();
                        }
                    });
                    CompanyActivity companyActivity = CompanyActivity.dao.creatPercent(Constant.CompanyAcitivyType.ACTIVITYCOUPON, memberInfo.getCompany(), getLoginMember().getId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "打开APP活动赠送优惠券");
                    companyActivity.save();
                    if (isOk) {
                        renderAjaxSuccess("领取成功!");
                    } else {
                        renderAjaxError("领取失败!");
                    }
                } else {
                    renderAjaxError("领取失败!稍后再试!");
                }
            } else {
                final MemberCoupon memberCoupon = MemberCoupon.dao.create(coupon, "打开APP活动赠送", getLoginMember().getId());
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
                CompanyAccount companyAccount = CompanyAccount.dao.findById(memberInfo.getCompany());
                if (CompanyAccount.dao.amountEnough(companyAccount, coupon.getAmount(), Constant.CompanyAccountActivity.awardMember)) {
                    boolean isOk = Db.tx(new IAtom() {
                        @Override
                        public boolean run() throws SQLException {
                            return memberActivity.save() && memberCoupon.save();
                        }
                    });
                    CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.ACTIVITYCOUPON, coupon.getAmount(), memberInfo.getCompany(), getLoginMember().getId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "打开APP活动赠送优惠券");
                    companyActivity.save();
                    if (isOk) {
                        renderAjaxSuccess("领取成功!");
                    } else {
                        renderAjaxError("领取失败!");
                    }
                } else {
                    renderAjaxError("领取失败!稍后再试!");
                }
            }

        } else {
            renderAjaxError("活动不存在!");
        }
    }

    /**
     * 司机好评率和接单率
     */
    public void favorableRateAndOrdersRate() {
        DecimalFormat df = new DecimalFormat("######0.00");
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        if (driverInfo == null) {
            driverInfo = new DriverInfo();
            driverInfo.setId(getParaToInt("driverId"));
        }
        double favorableRate = Grade.dao.driverFavorableRate(driverInfo.getId());
        String bTime = getPara("bTime");
        String eTime = getPara("eTime");
        //double favorableRate=Grade.dao.driverFavorableRate(235);
        OrderService orderService = OrderService.getInstance();
        OrdersRateDto ordersRateDto = orderService.OrdersRate(driverInfo.getId(), bTime, eTime);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("favorableRate", df.format(favorableRate));
        resultMap.put("ordersRateDto", ordersRateDto);
        ok(resultMap);
    }

    /**
     * 获取附近司机位置
     */
    /**
     * @api {POST} /api/member/cars_V2  获取附近司机位置V2
     * @apiGroup Member
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} latitude
     * @apiParam {String} longitude
     * @apiParam {String} type
     * @apiParam {String} serviceType
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/member/cars_V2")
    @Before(POST.class)
    public void getHistoricalLocation() {
        String latitude = getPara("latitude");//当前纬度
        String longitude = getPara("longitude");//当前经度
        int type = getParaToInt("type", 0);
        int serviceType = getParaToInt("serviceType", 0);
        if (Strings.isNullOrEmpty(latitude) ||
                Strings.isNullOrEmpty(longitude)
                ) {
            renderAjaxError("参数不能为空！");
            return;
        }
        List<DriverInfo> driverInfos = DriverInfo.dao.findByLocation(latitude, longitude, type, serviceType);
        List<String> loginIds = Lists.newArrayList();
        for (DriverInfo driverInfo : driverInfos) {
            loginIds.add(DriverInfo.dao.findById(driverInfo.getId()).getLoginId().toString());
        }
        renderAjaxSuccess(HistoricalLocationCache.getInstance().getAllById(loginIds));
    }

    /**
     * 获取司机位置信息V2
     */
    /**
     * @api {POST} /api/member/getDriverLocation_V2 获取接单司机位置信息V2
     * @apiGroup Member
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} driverId 司机id
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/member/getDriverLocation_V2")
    @Before(POST.class)
    public void getDriverLocation_V2() {
        int driverId = 0;
        try {
            driverId = getParaToInt("driverId", 0);
        } catch (Exception e) {
            renderAjaxError("司机信息不存在");
        }
        if (driverId != 0) {
            renderAjaxSuccess(HistoricalLocationCache.getInstance().getAllById(DriverInfo.dao.findById(driverId).getLoginId().toString()));
        } else {
            renderAjaxError("司机信息不存在");
        }
    }

}
