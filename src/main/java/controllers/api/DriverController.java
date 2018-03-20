package controllers.api;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.redis.Redis;
import dto.CalculationDto;
import dto.DriverOnlineCache;
import dto.location.HistoricalLocation;
import exception.InsuranceException;
import jobs.activity.OrderActivity;
import jobs.pushtocustomer.PushToCustomer;
import jobs.pushtocustomer.PushToPay;
import jobs.reward.Reward;
import kits.*;
import kits.cache.DriverOrderCache;
import kits.cache.DriverStatusCache;
import kits.cache.HistoricalLocationCache;
import models.car.*;
import models.company.Company;
import models.count.MemberOrderStatistics;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.driver.DriverLicenseInfo;
import models.driver.DriverRealLocation;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.reject.RejectLog;
import models.sys.AdminSetting;
import models.sys.Area;
import models.sys.ZxLine;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.*;
import utils.AESOperator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by BOGONj on 2016/8/29.
 */
@Controller("/api/driver")
@SuppressWarnings("ALL")
public class DriverController extends BaseApiController {

    private static Logger logger = LoggerFactory.getLogger(controllers.admin.order.OrderController.class);

    /**
     * @api {POST} /api/driver/register   司机注册
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} name  姓名
     * @apiParam {String} gender 性别
     * @apiParam {String} tphone   推荐人手机
     * @apiParam {String} phone  手机号
     * @apiParam {String} type  类型
     * @apiParam {String} driverLicense 驾驶证照片url
     * @apiParam {String} drivingLicense 驶证照片url
     * @apiParam {String} qualification 从业资格证照片url
     * @apiParam {String} idCard 身份证照片url
     * @apiParam {String} smsCode  短信验证码
     * @apiParam {String} areaCode  区域代码
     * @apiParam {String} registerIp
     * @apiParam {String} registerIMEI
     * @apiParam {String} registerIMSI
     * @apiParam {String} macAddress
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/driver/register")
    @Before(POST.class)
    public void register() {
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

    /**
     * 已废弃的接口 添加车辆
     */
    @ActionKey("/api/driver/car/plus")
    public void plus() {
        String plateNumber = getPara("plateNumber");//车牌号
        String color = getPara("color");//车身颜色
        String vin = getPara("vin");//车架号
        String pictures = getPara("picture");
        String brand = getPara("brand");
        String model = getPara("model");
        int type = getParaToInt("type", 0);
        if (Strings.isNullOrEmpty(plateNumber) ||
                Strings.isNullOrEmpty(color) ||
                Strings.isNullOrEmpty(vin) ||
                Strings.isNullOrEmpty(pictures) ||
                Strings.isNullOrEmpty(brand) ||
                type == 0 ||
                Strings.isNullOrEmpty(model)) {
            renderAjaxError("参数不能为空！");
            return;
        }
        if (Car.dao.validateVin(vin) || Car.dao.validatePlateNumber(plateNumber)) {
            renderAjaxError("车牌号已被添加，无法重复添加");
            return;
        }
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final Car car = new Car();
        car.setBrand(Ints.tryParse(brand));
        car.setModelType(Ints.tryParse(model));
        car.setVin(vin);
        car.setPicture(pictures);
        car.setPlateNo(plateNumber);
        car.setColor(color);
        car.setType(type);
        final CarInfo carInfo = new CarInfo();
        carInfo.setJoinTime(DateTime.now().toDate());
        final DriverCar driverCar = new DriverCar();
        driverCar.setDriver(driverInfo.getId());
        driverCar.setUseFlag(false);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (!car.save()) {
                    return false;
                }
                carInfo.setCarId(car.getId());
                driverCar.setCar(car.getId());
                return driverCar.save() && carInfo.save();
            }
        });
        if (isOk) {
            renderAjaxSuccess("添加成功！");
        } else {
            renderAjaxError("添加失败！");
        }
    }


    /**
     * 获取司机所有车辆信息
     */
    @ActionKey("/api/driver/car/getCarList")
    public void getCarList() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        List<DriverInfo> list = DriverInfo.dao.findCarById(driverInfo.getId());
        renderAjaxSuccess(list);
    }

    /**
     * 获取司机所有车辆信息V2
     */
    /**
     * @api {get} /api/driver/car/getCarList_V2  获取司机所有车辆信息
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "msg": "",
     * "data": [
     * {
     * "carInfoId": 1220,
     * "brandName": "奥迪",
     * "distance": 111,
     * "color": "黑",
     * "carStatus": 1,
     * "city": "340100",
     * "county": "340104",
     * "typeName": "快车",
     * "plateNumber": "金A12311",
     * "type": 44,
     * "picture": null,
     * "modelName": "奥迪A3",
     * "province": "340000",
     * "model": 34,
     * "vin": "1231231",
     * "brand": 3,
     * "certifyDateB": "2017-12-07 00:00:00"
     * },
     * {
     * "carInfoId": 0,
     * "brandName": 0,
     * "distance": 0,
     * "color": 0,
     * "carStatus": 0,
     * "city": 0,
     * "county": 0,
     * "typeName": 0,
     * "plateNumber": 0,
     * "picture": 0,
     * "modelName": 0,
     * "province": 0,
     * "model": 0,
     * "vin": 0,
     * "brand": 0,
     * "type": 40,
     * "certifyDateB": 0
     * }
     * ],
     * "status": "OK"
     * }
     */
    @ActionKey("/api/driver/car/getCarList_V2")
    public void getCarList_V2() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        if (driverInfo == null) {
            renderAjaxError("司机信息获取失败！请重新登陆");
            // 直接注销登陆
            ApiAuthKit.logout(getRequest().getHeader(Constant.TOKEN), getRequest().getHeader(Constant.SECRET), getAppType());
            return;
        }
        List<DriverInfo> list = Lists.newArrayList();
        if (driverInfo.getType().indexOf(Constant.ServiceType.DaiJia + "") != -1) {
            DriverInfo driverInfo1 = new DriverInfo();
            driverInfo1.put("carInfoId", 0);
            driverInfo1.put("distance", 0);
            driverInfo1.put("province", 0);
            driverInfo1.put("city", 0);
            driverInfo1.put("county", 0);
            driverInfo1.put("brand", 0);
            driverInfo1.put("brandName", 0);
            driverInfo1.put("model", 0);
            driverInfo1.put("modelName", 0);
            driverInfo1.put("plateNumber", 0);
            driverInfo1.put("type", Constant.ServiceItemType.DaiJia);
            driverInfo1.put("typeName", 0);
            driverInfo1.put("vin", 0);
            driverInfo1.put("color", 0);
            driverInfo1.put("picture", 0);
            driverInfo1.put("carStatus", 0);
            driverInfo1.put("certifyDateB", 0);
            list.add(driverInfo1);
        }
        list.addAll(DriverInfo.dao.findCarById(driverInfo.getId(), Constant.DriverStatus.ShengHeTongGuo));
        renderAjaxSuccess(list);
    }


    /**
     * 删除司机车辆信息
     */
    @ActionKey("/api/driver/car/deleteCalByCarId")
    public void deleteCalByCarId() {
        int carInfoId = getParaToInt("carInfoId");
        final CarInfo carInfo = CarInfo.dao.findByCar(carInfoId);
        final Car car = Car.dao.findById(carInfo.getCarId());
        final DriverCar driverCar = DriverCar.dao.findByCar(carInfo.getCarId());
        boolean isOK = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return carInfo.delete() && car.delete() && driverCar.delete();
            }
        });
        if (isOK) {
            renderAjaxSuccess("删除成功!");
        } else {
            renderAjaxError("删除失败!");
        }
    }

    /**
     * 添加或者修改车辆
     */
    @ActionKey("/api/driver/car/saveOrUpdate")
    public void saveOrUpdate() {
        int carInfoId = getParaToInt("carInfoId", 0);
        String cityCode = getPara("cityCode");//城市code
        int brand = getParaToInt("brand", 0);//品牌的主键
        int model = getParaToInt("model");//型号的主键
        String plateNumber = getPara("plateNumber");//车牌号
        int type = getParaToInt("type", 0);//车辆类型
        String vin = getPara("vin");//车架号 识别码
        String distance = getPara("distance");//行驶里程
        String color = getPara("color");//车身颜色
        String pictures = getPara("picture");//车辆图片
        String transportation = getPara("transportation");//运输证
        String certifyDateB = getPara("certifyDateB");//车辆登记日期

        if (Strings.isNullOrEmpty(cityCode) ||
                brand == 0 ||
                model == 0 ||
                Strings.isNullOrEmpty(plateNumber) ||
                type == 0 ||
                Strings.isNullOrEmpty(vin) ||
                Strings.isNullOrEmpty(distance) ||
                Strings.isNullOrEmpty(color) ||
                Strings.isNullOrEmpty(pictures) ||
                Strings.isNullOrEmpty(transportation) ||
                Strings.isNullOrEmpty(certifyDateB)) {
            renderAjaxError("参数不能为空！");
            return;
        }

        if ((Car.dao.validateVin(vin) || Car.dao.validatePlateNumber(plateNumber)) && carInfoId == 0) {
            renderAjaxError("车牌号已被添加，无法重复添加");
            return;
        }

        DriverCar driverCar = new DriverCar();
        final CarInfo carInfo = new CarInfo();
        final Car car = new Car();

        //赋值
        car.setBrand(brand);
        car.setModelType(model);
        car.setPlateNo(plateNumber);
        car.setType(type);
        car.setVin(vin);
        car.setColor(color);
        car.setPicture(pictures);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(certifyDateB);
            car.setCertifyDateB(date);
        } catch (ParseException e) {
            renderAjaxError("时间格式不对！");
            return;
        }

        carInfo.setDistance(Long.valueOf(distance));

        //根据城市code查找相应的省市县
        Area area = Area.dao.findByAdCode(cityCode);
        if (area.getLevel().equals("province")) {
            carInfo.setProvince(area.getAdcode());
        }
        if (area.getLevel().equals("city")) {
            carInfo.setCity(area.getAdcode());
            area = Area.dao.findByAdCode(area.getParent());
            carInfo.setProvince(area.getAdcode());
        }
        if (area.getLevel().equals("district")) {
            carInfo.setCounty(area.getAdcode());
            area = Area.dao.findByAdCode(area.getParent());
            carInfo.setCity(area.getAdcode());
            area = Area.dao.findByAdCode(area.getParent());
            carInfo.setProvince(area.getAdcode());
        }

        //判断是保存还是新增
        if (carInfoId != 0) {
            car.setId(carInfoId);
            car.setStatus(null);//修改状态为未审核
            carInfo.setCarId(carInfoId);
            carInfo.setJoinTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return carInfo.update() && car.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            car.setCreateTime(DateTime.now().toDate());
            if (car.save()) {
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
                carInfo.setCarId(car.getId());
                carInfo.save();
                driverCar.setCar(car.getId());
                driverCar.setDriver(driverInfo.getId());
                driverCar.setUseFlag(false);
                driverCar.save();
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    /**
     * 获取车辆的品牌
     */
    @ActionKey("/api/driver/car/brands")
    public void brands() {
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        String name = getPara("name");
        List<CarBrand> carBrands = CarBrand.dao.findByPage(pageStart, pageSize, name);
        renderAjaxSuccess(carBrands);
    }

    /**
     * 获取车辆的型号
     */
    @ActionKey("/api/driver/car/models")
    public void models() {
        int brandId = getParaToInt("brandId", 0);
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        String name = getPara("name");
        if (brandId > 0) {
            List<CarModel> carModels = CarModel.dao.findByBrandAndPage(brandId, pageStart, pageSize, name);
            renderAjaxSuccess(carModels);
        } else {
            renderAjaxNoData();
        }
    }

    /**
     * 更新司机的位置
     */
    @Before(POST.class)
    @ActionKey("/api/driver/update/location")
    public void location() {
        MemberLogin memberLogin = getLoginMember();

        int mStatus = ApiAuthKit.loginout(memberLogin.getId());
        if (mStatus == -1) {//不是上线状态不写GPS数据
            renderAjaxSuccess("未上线无需更新位置！");
            logger.info("未上线无需更新位置！");
            return;
        }

        String longitude = getPara("longitude");
        String latitude = getPara("latitude");
        String speed = getPara("speed");
        String orientation = getPara("orientation");
        int type = getParaToInt("type", 4);
        String accuracy = getPara("accuracy", "-1");
        if (Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(speed)
                || Strings.isNullOrEmpty(orientation)
                ) {
            renderAjaxError("参数不能为空");
        }
        String timestamp = getPara("timestamp");
        Date reciveTime;
        if (Strings.isNullOrEmpty(timestamp)) {
            reciveTime = DateTime.now().toDate();
        } else {
            reciveTime = new DateTime(Longs.tryParse(timestamp)).toDate();
        }
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        boolean isOk;
        if (driverRealLocation == null) {
            driverRealLocation = new DriverRealLocation();
            driverRealLocation.setSpeed(Doubles.tryParse(speed));
            driverRealLocation.setLongitude(Doubles.tryParse(longitude));
            driverRealLocation.setLatitude(Doubles.tryParse(latitude));
            driverRealLocation.setDriver(driverInfo.getId());
            driverRealLocation.setReciveTime(reciveTime);
            driverRealLocation.setType(type);
            driverRealLocation.setOrientation(Doubles.tryParse(orientation));
            driverRealLocation.setAccuracy(Doubles.tryParse(accuracy));
            isOk = driverRealLocation.save();
        } else {
            final DriverHistoryLocation driverHistoryLocation = new DriverHistoryLocation();
            driverHistoryLocation.setDriver(driverRealLocation.getDriver());
            driverHistoryLocation.setLatitude(driverRealLocation.getLatitude());
            driverHistoryLocation.setLongitude(driverRealLocation.getLongitude());
            driverHistoryLocation.setOrientation(driverRealLocation.getOrientation());
            driverHistoryLocation.setSpeed(driverRealLocation.getSpeed());
            driverHistoryLocation.setType(driverRealLocation.getType());
            driverHistoryLocation.setRecoviceTime(driverRealLocation.getReciveTime());
            driverHistoryLocation.setAccuracy(driverRealLocation.getAccuracy());

            driverRealLocation.setSpeed(Doubles.tryParse(speed));
            driverRealLocation.setLongitude(Doubles.tryParse(longitude));
            driverRealLocation.setLatitude(Doubles.tryParse(latitude));
            driverRealLocation.setDriver(driverInfo.getId());
            driverRealLocation.setReciveTime(reciveTime);
            driverRealLocation.setType(type);
            driverRealLocation.setOrientation(Doubles.tryParse(orientation));
            driverRealLocation.setAccuracy(Doubles.tryParse(accuracy));

            final DriverRealLocation finalDriverRealLocation = driverRealLocation;
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return driverHistoryLocation.save() && finalDriverRealLocation.update();
                }
            });
        }
        if (isOk) {
            //通过司机更新位置来实时更新司机上线存活时间
            renderAjaxSuccess("更新位置成功！");
        } else {
            renderAjaxError("更新位置失败！");
        }
    }

    /**
     * 更新司机的位置
     */
    /**
     * @api {post} /api/driver/update/location_V2  更新司机的位置
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} longitude   经纬度
     * @apiParam {String} latitude   经纬度
     * @apiParam {String} speed   速度
     * @apiParam {String} orientation   方向
     * @apiParam {String} type   定位类型
     * @apiParam {String} accuracy  准确性
     * @apiParam {String} timestamp   时间戳
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @Before(POST.class)
    @ActionKey("/api/driver/update/location_V2")
    public void location_V2() {
        MemberLogin memberLogin = getLoginMember();

        int mStatus = ApiAuthKit.loginout(memberLogin.getId());
        if (mStatus == -1) {//不是上线状态不写GPS数据
            renderAjaxSuccess("未上线无需更新位置！");
            logger.info("未上线无需更新位置！");
            return;
        }

        String longitude = getPara("longitude", "0");
        String latitude = getPara("latitude", "0");
        String speed = getPara("speed", "0");
        String orientation = getPara("orientation", "0");
        int type = getParaToInt("type", 4);
        String accuracy = getPara("accuracy", "-1");
        if (Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(latitude)
                || Strings.isNullOrEmpty(speed)
                || Strings.isNullOrEmpty(orientation)
                ) {
            renderAjaxError("参数不能为空");
        }
        String timestamp = getPara("timestamp");
        Date reciveTime;
        if (Strings.isNullOrEmpty(timestamp)) {
            reciveTime = DateTime.now().toDate();
        } else {
            reciveTime = new DateTime(Longs.tryParse(timestamp)).toDate();
        }
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        boolean isOk;
        if (driverRealLocation == null) {
            driverRealLocation = new DriverRealLocation();
            driverRealLocation.setSpeed(Doubles.tryParse(speed));
            driverRealLocation.setLongitude(Doubles.tryParse(longitude));
            driverRealLocation.setLatitude(Doubles.tryParse(latitude));
            driverRealLocation.setDriver(driverInfo.getId());
            driverRealLocation.setReciveTime(reciveTime);
            driverRealLocation.setType(type);
            driverRealLocation.setOrientation(Doubles.tryParse(orientation));
            driverRealLocation.setAccuracy(Doubles.tryParse(accuracy));
            isOk = driverRealLocation.save();
        } else {
            final DriverHistoryLocation driverHistoryLocation = new DriverHistoryLocation();
            driverHistoryLocation.setDriver(driverRealLocation.getDriver());
            driverHistoryLocation.setLatitude(driverRealLocation.getLatitude());
            driverHistoryLocation.setLongitude(driverRealLocation.getLongitude());
            driverHistoryLocation.setOrientation(driverRealLocation.getOrientation());
            driverHistoryLocation.setSpeed(driverRealLocation.getSpeed());
            driverHistoryLocation.setType(driverRealLocation.getType());
            driverHistoryLocation.setRecoviceTime(driverRealLocation.getReciveTime());
            driverHistoryLocation.setAccuracy(driverRealLocation.getAccuracy());

            driverRealLocation.setSpeed(Doubles.tryParse(speed));
            driverRealLocation.setLongitude(Doubles.tryParse(longitude));
            driverRealLocation.setLatitude(Doubles.tryParse(latitude));
            driverRealLocation.setDriver(driverInfo.getId());
            driverRealLocation.setReciveTime(reciveTime);
            driverRealLocation.setType(type);
            driverRealLocation.setOrientation(Doubles.tryParse(orientation));
            driverRealLocation.setAccuracy(Doubles.tryParse(accuracy));

            final DriverRealLocation finalDriverRealLocation = driverRealLocation;
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return driverHistoryLocation.save() && finalDriverRealLocation.update();
                }
            });
        }
        if (isOk) {
            //通过司机更新位置来实时更新司机上线存活时间
            DriverOnlineCache driverOnlineCache = DriverStatusCache.getDriverOnlineCache(getLoginMember().getId());
            if (driverOnlineCache != null) {
                driverOnlineCache.setOnlineTime(System.currentTimeMillis());
            } else {
                driverOnlineCache = new DriverOnlineCache(getLoginMember().getId(), -1, Constant.LoginStatus.LOGINED, System.currentTimeMillis(), Doubles.tryParse(longitude), Doubles.tryParse(latitude));
            }
            DriverStatusCache.updateDriverStatus(driverOnlineCache);
            renderAjaxSuccess("更新位置成功！");
        } else {
            renderAjaxError("更新位置失败！");
        }
    }

    /**
     * 司机接单
     */
    public void grabsingle() {
        int orderId = getParaToInt("orderId", 0);
        if (orderId > 0) {
            final Order order = Order.dao.findById(orderId);
            if (order.getStatus() == Constant.OrderStatus.CANCEL) {
                renderAjaxError("订单已经取消");
                return;
            }
            if (order.getStatus() != Constant.OrderStatus.CREATE) {
                renderAjaxError("订单已经被别人抢走了");
                return;
            }
            final MemberLogin memberLogin = getLoginMember();
            if (!order.getSetOutFlag()) {
                if (order.getPdFlag()) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.BUSY);
                }
            }
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            order.setDriver(driverInfo.getId());
            order.setCompany(driverInfo.getCompany());
            order.setStatus(Constant.OrderStatus.ACCEPT);
            if(order.getFromType() == Constant.FromType.WECHATTYPE){
                order.setFromCompany(driverInfo.getCompany());
            }
            final OrderLog orderLog = new OrderLog();
            orderLog.setOperationTime(DateTime.now().toDate());
            orderLog.setOrderId(orderId);
            Car car = Car.dao.findByDriver(driverInfo.getId());
            if (car != null && order.getServiceType() != Constant.ServiceType.DaiJia) {//防止代驾单有车辆信息
                order.setCar(car.getId());
                orderLog.setRemark(driverInfo.getRealName() + "司机接单了!");
            } else {
                orderLog.setRemark(driverInfo.getRealName() + "司机接单了!");
            }
            orderLog.setLoginId(getLoginMember().getId());
            orderLog.setOperater(driverInfo.getNickName());
            orderLog.setAction(Constant.OrderStatus.ACCEPT);
            Boolean isOk = dealorder(order, orderLog, memberLogin);
            if (isOk) {
                MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
                MemberLogin memberLogin1 = MemberLogin.dao.findById(memberInfo.getLoginId());
                MemberLogin customerLogin = null;
                if (memberLogin1 != null && memberLogin1.getCacheKey() != null) {
                    customerLogin = Redis.use(Constant.LOGINMEMBER_CACHE).get(memberLogin1.getCacheKey());
                }
                if (customerLogin != null) {
                    customerLogin.setCacheKey(memberLogin1.getCacheKey() == null ? null : memberLogin1.getCacheKey());
                }
                order.put("driverInfo", driverInfo);
                order.put("memberLogin", customerLogin);
                if (memberInfo.getLoginId() != null) {
                    PushToCustomer pushToCustomer = new PushToCustomer(order);
                    EventKit.post(pushToCustomer);
                }
                SmsKit.driverjiedan(order);
                SmsKit.neworder(order);
                //缓存订单信息
                // DriverOrderCache.getInstance().createOrderCache(order, driverInfo);
                // MemberOrderCache.getInstance().createOrderCache(order, MemberInfo.dao.findById(order.getMember()));
                //修改pushlog
                RejectLog rejectLog = RejectLog.dao.findRejectLog(order.getId(), order.getDriver());
                rejectLog.setStatus(1);
                rejectLog.update();
                renderAjaxSuccess("抢单成功！");
            } else {
                renderAjaxError("抢单失败！已被人抢走了！");
            }
        }
    }

    public synchronized boolean dealorder(final Order order, final OrderLog orderLog, final MemberLogin memberLogin) {
        Order Tmp = Order.dao.findById(order.getId());
        if (Tmp.getStatus() == Constant.OrderStatus.ACCEPT) {
            renderAjaxError("订单已经被别人抢走了。。。。。");
            return false;
        }
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return order.update() && orderLog.save() && memberLogin.update();
            }
        });
    }

    /**
     * 司机上线需要使用那一辆汽车
     */
    public void online() {
        final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        final DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final int carId = getParaToInt("carId", 0);
        if (carId == 0) {
            if (memberLogin.getStatus() != Constant.LoginStatus.LOGINED && memberLogin.getStatus() != Constant.LoginStatus.LOGOUTED) {
                if (OnlineService.getInstance().offline(memberLogin, driverInfo)) {
                    ApiAuthKit.loginclaer(getLoginMember().getId());  // todo 下线清除
                    renderAjaxSuccess("下线成功了！");
                } else {
                    renderAjaxError("下线失败了！");
                }
            } else {
                ApiAuthKit.loginclaer(getLoginMember().getId());  // todo 下线清除
                renderAjaxSuccess("下线成功了！");
            }
        } else {
            if (driverInfo.getType().indexOf(Constant.ServiceType.DaiJia) != -1) {
                MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberLogin.getId());
                AdminSetting adminSetting;
                try {
                    adminSetting = AdminSetting.dao.findByCompanyId(driverInfo.getCompany());
                } catch (Exception e) {
                    adminSetting = AdminSetting.dao.findFirst();
                }
                int a = adminSetting.getMinAmount();
                if (memberCapitalAccount.getAmount().compareTo(BigDecimal.valueOf(a)) < 0) {
                    renderAjaxError("您的余额低于最低上线金额,最低上线金额:" + a + "元!");
                    return;
                }
            }
            final DriverCar car = DriverCar.dao.findByCarAndDriver(carId, driverInfo.getId());
            if (car == null) {
                renderAjaxError("数据不存在！");
                return;
            }
            Map<String, Object> results = OnlineService.getInstance().online(memberLogin, driverInfo, car);
            Car carInfo = Car.dao.findById(car.getCar());
            Boolean isOk = (Boolean) results.get("isOk");
            Map<String, Integer> result = (Map<String, Integer>) results.get("result");
            if (isOk) {
                ApiAuthKit.loginput(getLoginMember().getId(), carInfo.getType());// todo 上线缓存
                renderAjaxSuccess(result);
            } else {
                renderAjaxError("上线失败了！");
            }
        }
    }

    public void djOnline() {
        final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        final DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberLogin.getId());
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(driverInfo.getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }

        int a = 0;
        try {
            a = adminSetting.getMinAmount();
        } catch (NullPointerException e) {
            e.printStackTrace();
            ApiAuthKit.loginclaer(getLoginMember().getId());  // 下线清除
            renderAjaxError("状态异常，已强制下线！");
        }
        int type = getParaToInt("status", 1);
        if (type == 0) {//下线
            if (memberLogin.getStatus() != Constant.LoginStatus.LOGINED && memberLogin.getStatus() != Constant.LoginStatus.LOGOUTED) {
                if (OnlineService.getInstance().offline(memberLogin, driverInfo)) {
                    ApiAuthKit.loginclaer(getLoginMember().getId());  // 下线清除
                    renderAjaxSuccess("下线成功了！");
                } else {
                    renderAjaxError("下线失败了！");
                }
            } else {
                ApiAuthKit.loginclaer(getLoginMember().getId());  // 下线清除
                renderAjaxSuccess("下线成功了！");
            }
        } else {//上线
            if (memberCapitalAccount.getAmount().compareTo(BigDecimal.valueOf(a)) < 0) {
                renderAjaxError("您的余额低于最低上线金额,最低上线金额:" + a + "元!");
                return;
            }
            Map<String, Object> results = OnlineService.getInstance().online(memberLogin, driverInfo, null);
            Boolean isOk = (Boolean) results.get("isOk");
            Map<String, Integer> result = (Map<String, Integer>) results.get("result");
            if (isOk) {
                ApiAuthKit.loginput(getLoginMember().getId(), Constant.ServiceItemType.DaiJia);// todo 上线缓存 代驾
                renderAjaxSuccess(result);
            } else {
                renderAjaxError("上线失败了！");
            }
        }
    }

    /**
     * 司机上线需要使用那一辆汽车
     */
    /**
     * @api {get} /api/driver/online_V2  司机上线下线
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} carId
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "msg": "上线成功",
     * "data": null,
     * "status": "OK"
     * }
     */
    public void online_V2() {
        final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        final DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final int carId = getParaToInt("carId", 0);
        int orders = Order.dao.findByOnlineScanning(driverInfo.getId());
        if (orders != 0) {
            renderAjaxError("下线失败了！你有未完成订单无法下线");
            return;
        }
        if (carId == 0) {
            if (memberLogin.getStatus() != Constant.LoginStatus.LOGINED && memberLogin.getStatus() != Constant.LoginStatus.LOGOUTED) {
                if (OnlineService.getInstance().offline(memberLogin, driverInfo)) {
                    //下线（代驾）
                    ApiAuthKit.loginclaer(getLoginMember().getId());  // todo 下线清除
                    DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), Constant.ServiceType.NoService, Constant.LoginStatus.LOGINED, System.currentTimeMillis()));
                    renderAjaxSuccess("下线成功了！");
                } else {
                    renderAjaxError("下线失败了！");
                }
            } else {
                //下线（代驾）
                ApiAuthKit.loginclaer(getLoginMember().getId());  // todo 下线清除
                DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), Constant.ServiceType.NoService, Constant.LoginStatus.LOGINED, System.currentTimeMillis()));
                renderAjaxSuccess("下线成功了！");
            }
        } else {
            if (driverInfo.getType().indexOf(Constant.ServiceType.DaiJia) != -1) {
                MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberLogin.getId());
                AdminSetting adminSetting;
                try {
                    adminSetting = AdminSetting.dao.findByCompanyId(driverInfo.getCompany());
                } catch (Exception e) {
                    adminSetting = AdminSetting.dao.findFirst();
                }
                int a = adminSetting.getMinAmount();
                if (memberCapitalAccount.getAmount().compareTo(BigDecimal.valueOf(a)) < 0) {
                    renderAjaxError("您的余额低于最低上线金额,最低上线金额:" + a + "元!");
                    return;
                }
            }
            final DriverCar car = DriverCar.dao.findByCarAndDriver(carId, driverInfo.getId());
            if (car == null) {
                renderAjaxError("数据不存在！");
                return;
            }
            Map<String, Object> results = OnlineService.getInstance().online(memberLogin, driverInfo, car);
            Car carInfo = Car.dao.findById(car.getCar());
            Boolean isOk = (Boolean) results.get("isOk");
            if (isOk) {
                //上线缓存（代驾）
                DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), carInfo.getType(), Constant.LoginStatus.RECIVEDORDER, System.currentTimeMillis()));
                ApiAuthKit.loginput(getLoginMember().getId(), carInfo.getType());// todo 上线缓存
                renderAjaxSuccess("上线成功");
            } else {
                renderAjaxError("上线失败了！");
            }
        }
    }

    /**
     * @api {get} /api/driver/djOnline_V2  代驾司机上线下线
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} status    0下线   1上线
     * @apiSuccessExample Success-Response:
     * {
     * "msg": "上线成功",
     * "data": null,
     * "status": "OK"
     * }
     * HTTP/1.1 200 OK
     */
    public void djOnline_V2() {
        final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        final DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        int orders = Order.dao.findByOnlineScanning(driverInfo.getId());
        if (orders != 0) {
            renderAjaxError("下线失败了！你有未完成订单无法下线");
            return;
        }

        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberLogin.getId());
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(driverInfo.getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }

        int a = 0;
        try {
            a = adminSetting.getMinAmount();
        } catch (NullPointerException e) {
            e.printStackTrace();
            // 下线清除
            ApiAuthKit.loginclaer(getLoginMember().getId());
            DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), Constant.ServiceItemType.DaiJia, Constant.LoginStatus.LOGINED, System.currentTimeMillis()));
            renderAjaxError("状态异常，已强制下线！");
        }
        int type = getParaToInt("status", 1);
        if (type == 0) {//下线
            if (memberLogin.getStatus() != Constant.LoginStatus.LOGINED && memberLogin.getStatus() != Constant.LoginStatus.LOGOUTED) {
                if (OnlineService.getInstance().offline(memberLogin, driverInfo)) {
                    // 下线清除
                    ApiAuthKit.loginclaer(getLoginMember().getId());
                    DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), Constant.ServiceItemType.DaiJia, Constant.LoginStatus.LOGINED, System.currentTimeMillis()));
                    renderAjaxSuccess("下线成功了！");
                } else {
                    renderAjaxError("下线失败了！");
                }
            } else {
                ApiAuthKit.loginclaer(getLoginMember().getId());  // 下线清除
                renderAjaxSuccess("下线成功了！");
            }
        } else {//上线
            if (memberCapitalAccount.getAmount().compareTo(BigDecimal.valueOf(a)) < 0) {
                renderAjaxError("您的余额低于最低上线金额,最低上线金额:" + a + "元!");
                return;
            }
            Map<String, Object> results = OnlineService.getInstance().online(memberLogin, driverInfo, null);
            Boolean isOk = (Boolean) results.get("isOk");
            if (isOk) {
                // todo 上线缓存 代驾
                ApiAuthKit.loginput(getLoginMember().getId(), Constant.ServiceItemType.DaiJia);
                DriverStatusCache.updateDriverStatus(new DriverOnlineCache(memberLogin.getId(), Constant.ServiceItemType.DaiJia, Constant.LoginStatus.RECIVEDORDER, System.currentTimeMillis()));
                renderAjaxSuccess("上线成功");
            } else {
                renderAjaxError("上线失败了！");
            }
        }
    }

    /**
     * 获取可以接单的车辆
     */
    public void cars() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        List<DriverCar> driverCars = DriverCar.dao.findByDriver(driverInfo.getId());
        renderAjaxSuccess(driverCars);
    }

    /**
     * 获取收入信息
     */
    public void amount() {
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
        DateTime now = DateTime.now();
        Date startOfMonth = now.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date endOfMonth = now.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        Date startOfToday = now.millisOfDay().withMinimumValue().toDate();
        Date endOfToday = now.millisOfDay().withMaximumValue().toDate();
        Date startOfYestarday = now.plusDays(-1).millisOfDay().withMinimumValue().toDate();
        Date endOfYestarday = now.plusDays(-1).millisOfDay().withMaximumValue().toDate();
        Map<String, Object> result = Maps.newHashMap();
        result.put("ye", memberCapitalAccount.getAmount());
        Number month = CapitalLog.dao.findByStartAndEndWithAccount(startOfMonth, endOfMonth, memberCapitalAccount.getLoginId());
        result.put("month", month);
        Number today = CapitalLog.dao.findByStartAndEndWithAccount(startOfToday, endOfToday, memberCapitalAccount.getLoginId());
        result.put("today", today);
        Number yesterday = CapitalLog.dao.findByStartAndEndWithAccount(startOfYestarday, endOfYestarday, memberCapitalAccount.getLoginId());
        result.put("yesterday", yesterday);
        renderAjaxSuccess(result);
    }

    /**
     * 司机点击出发
     */
    @Before(POST.class)
    public void orderStart() {
        int orderId = getParaToInt("id", 0);
        DateTime now = DateTime.now();
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final Order order = Order.dao.findById(orderId);
        if (order.getStatus() == Constant.OrderStatus.START) {
            PushToCustomer pushToCustomer = new PushToCustomer(order);
            EventKit.post(pushToCustomer);
            renderAjaxSuccess("开始行程成功");
            return;
        }
        if (order == null) {
            renderAjaxError("订单不存在！");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已经取消");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.PAYED) {
            renderAjaxError("订单已经支付");
            return;
        }
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        if (order.getStatus() == Constant.OrderStatus.DRIVERWAIT) {//如果是等候的订单，计算等待订单的
            DateTime waitDate = new DateTime(order.getLastUpdateTime());
            BigDecimal waitMinutes = new BigDecimal(Minutes.minutesBetween(waitDate, now).getMinutes());
            BigDecimal waitAmount = CalService.getInstance().calculationWaitSetUp1(order.getServiceType(), order.getCompany(), memberInfo, new DateTime(order.getSetouttime()), waitMinutes);
            order.setWaitAmount(waitAmount);
            order.setWaitTime(waitMinutes);
        }
        order.setStatus(Constant.OrderStatus.START);//订单开始
        if (order.getSetOutFlag()) {
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            memberLogin.setStatus(Constant.LoginStatus.BUSY);
            memberLogin.update();
        }
        order.setLastUpdateTime(DateTime.now().toDate());
        final OrderLog orderLog = new OrderLog();
        orderLog.setRemark(driverInfo.getRealName() + "已经开始了行程");
        orderLog.setOperationTime(now.toDate());
        orderLog.setOrderId(orderId);
        orderLog.setOperater(driverInfo.getPhone());
        orderLog.setLoginId(getLoginMember().getId());
        orderLog.setAction(Constant.OrderAction.START);
        final OrderTrip orderTrip = OrderTrip.dao.findById(orderId);
        orderTrip.setStartTime(now.toDate());
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return order.update() && orderLog.save() && orderTrip.update();
            }
        });
        if (isOk) {
            if (order.getServiceType() == Constant.ServiceType.DaiJia) {
                SmsKit.baoxian(memberInfo.getRealName(), memberInfo.getPhone(), memberInfo.getCompany());
                //因为代驾莫名出现多个时间相同的订单，暂定将当前订单的其他相同订单去除
                List<Order> ordList = Order.dao.findIllegalOrder(order);
                if (order != null) {
                    for (Order ord : ordList) {
                        ord.setStatus(Constant.OrderStatus.CANCEL);
                        //订单日志
                        OrderLog neworderLog = new OrderLog();
                        neworderLog.setOperationTime(DateTime.now().toDate());
                        neworderLog.setOrderId(order.getId());
                        neworderLog.setRemark("不合法的订单");
                        neworderLog.setAction(Constant.OrderAction.CANCEL);
                        neworderLog.save();
                        ord.update();
                    }
                }
            }
            PushToCustomer pushToCustomer = new PushToCustomer(order);
            EventKit.post(pushToCustomer);
            renderAjaxSuccess("开始行程成功");
        } else {
            renderAjaxError("开始行程失败");
        }
    }

    /**
     * 到达终点开始结算
     */
    /**
     * @api {get} /api/driver/orderArrived  司机到达终点开始结算
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {int} orderType  小分类，舒适型和经济型
     * @apiParam {int} type  大类型
     * @apiParam {String}   id  订单id
     * @apiParam {String}   roadToll
     * @apiParam {String}   remoteFee
     * @apiParam {String}   otherCharges
     * @apiParam {String}   payType
     * @apiParam {String}   distance
     * @apiParam {String}   taxiAmount   出租车实际价格设置
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    public void orderArrived() {
        int orderId = getParaToInt("id", 0);
        String roadToll = getPara("roadToll", "0");
        String remoteFee = getPara("remoteFee", "0");
        String otherCharges = getPara("otherCharges", "0");
        int payType = getParaToInt("payType", 1);
        BigDecimal distance = new BigDecimal(getPara("distance", "0"));
        DateTime now = DateTime.now();
        final Order order = Order.dao.findById(orderId);


        DateTime setouttime = new DateTime(order.getSetouttime());
        if (order.getSetOutFlag()) {
            if (setouttime.isAfterNow()) {
                setouttime = DateTime.now();
            }
        }
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }

        //越权问题
        String userName = getLoginMember().getUserName();
        DriverInfo driver = DriverInfo.dao.findById(order.getDriver());
        if (!userName.equals(driver.getPhone())) {
            renderAjaxError("当前用户身份异常！");
            return;
        }

        if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已经取消");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.END || order.getStatus() == Constant.OrderStatus.PAYED) {
            //EventKit.post(new PushToPay(order));
            JSONObject jsonObject = new JSONObject();
            String message = "该订单已经结算成功，您可以直接返回主界面！";
            jsonObject.put(message, "message");
            renderAjaxSuccess(jsonObject);
            DriverOrderCache.getInstance().del(order.getNo());
            DriverOrderCache.getInstance().del(DriverInfo.dao.findById(order.getDriver()));
            return;
        }
        if ((order.getFromType() == Constant.FromType.WECHATTYPE
                || order.getFromType() == Constant.FromType.BUDAN
                || order.getFromType() == Constant.FromType.TELTYPE) && payType != Constant.PayType.Collection) {
            renderAjaxError("非APP订单，只能代付");
            return;
        }
        Date orderStartDate = null;
        if (order.getServiceType() == Constant.ServiceType.DaiJia || (order.getServiceType() == Constant.ServiceType.KuaiChe && !order.getPdFlag())) {
            if (distance.compareTo(order.getRealDistance()) <= 0) {
                distance = order.getRealDistance();
            }
        } else {
            distance = order.getDistance();
        }

        order.setRealDistance(distance);
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        /*final int eachDistance = 10;//每个点之间的距离
        if (Strings.isNullOrEmpty(distanceStr)) {//如果距离为空

            if (orderLog == null) {
                renderAjaxError("改订单无法结算");
                return;
            }

            List<DriverHistoryLocation> historyLocations = DriverHistoryLocation.dao.findByDriverAndTime(driverInfo.getId(), orderLog.getOperationTime(), now.toDate());
            distance = new BigDecimal(eachDistance * historyLocations.size()).divide(new BigDecimal(1000), 2);
        }*/
        OrderLog orderLog = OrderLog.dao.findByOrderAndActionAndLoginId(orderId, Constant.OrderStatus.START, getLoginMember().getId());
        orderStartDate = orderLog.getOperationTime();
        BigDecimal waitMinutes = new BigDecimal(getPara("waitMinutes", "0"));
        if (waitMinutes.compareTo(BigDecimal.ZERO) == 0) {
            try {
                waitMinutes = order.getWaitTime();
                if (waitMinutes == null) {
                    waitMinutes = BigDecimal.ZERO;
                }
            } catch (NullPointerException e) {
                System.out.println("获取等待时间异常");
                e.printStackTrace();
            }
        }
        if (waitMinutes.compareTo(BigDecimal.ZERO) != 0) {
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            BigDecimal waitAmount = CalService.getInstance().calculationWaitSetUp1(order.getType(), order.getCompany(), memberInfo, setouttime, waitMinutes);
            order.setWaitAmount(waitAmount);
            order.setWaitTime(waitMinutes);
        }
        BigDecimal minutes = new BigDecimal(Minutes.minutesBetween(new DateTime(orderStartDate), now).getMinutes());
        List<CalculationDto> dto = CalService.getInstance().calculationDtoSetUp1(order.getServiceType(), Company.dao.findByCompanyId(order.getFromCompany()), MemberInfo.dao.findById(order.getMember()), setouttime.toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)), distance, minutes);
        final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        CalculationDto real = null;
        for (CalculationDto calculationDto : dto) {
            if (calculationDto.getTypeId() == order.getType()) {
                real = calculationDto;
                break;
            }
        }
        if (order.getServiceType() == Constant.ServiceType.KuaiChe) {
            if (order.getPdFlag()) {
                for (CalculationDto calculationDto : dto) {
                    if (calculationDto.getTypeId() == 1) {
                        real = calculationDto;
                        break;
                    }
                }
            } else {
                for (CalculationDto calculationDto : dto) {
                    if (calculationDto.getTypeId() == 0) {
                        real = calculationDto;
                        break;
                    }
                }
            }
        }
        if (real == null) {
            renderAjaxError("结算失败！");
            return;
        }
        BigDecimal addAmount = order.getAddAmount() == null ? BigDecimal.ZERO : order.getAddAmount();//获取加价的金额
        real.setTotalAmount(real.getTotalAmount().add(addAmount));
        real.setAddAmount(addAmount);
        order.setAmount(real.getTotalAmount());
        order.setStatus(Constant.OrderStatus.END);
        final OrderLog arrivedLog = new OrderLog();//到达终点
        arrivedLog.setAction(Constant.OrderAction.END);
        arrivedLog.setOrderId(orderId);
        arrivedLog.setOperationTime(now.toDate());
        arrivedLog.setRemark("司机已到达终点");
        arrivedLog.setLoginId(driverInfo.getLoginId());
        arrivedLog.setOperater(driverInfo.getPhone());
        order.setLastUpdateTime(now.toDate());
        order.setRemoteFee(new BigDecimal(remoteFee));
        order.setRoadToll(new BigDecimal(roadToll));
        order.setOtherCharges(new BigDecimal(otherCharges));
        BigDecimal realPay = order.getWaitAmount();
        realPay = realPay
                .add(real.getTotalAmount())
                .add(order.getRemoteFee())
                .add(order.getRoadToll())
                .add(order.getOtherCharges());


        //出租车实际价格设置
        if (order.getServiceType() == Constant.ServiceType.Taxi) {
            BigDecimal taxiAmount = new BigDecimal(getPara("taxiAmount", "0"));
            order.setRealPay(taxiAmount);
            order.setAmount(taxiAmount);
        } else {
            order.setRealPay(realPay);
        }
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian || order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
            ZxLine zxLine = ZxLine.dao.findById(order.getZxLine());
            CalculationDto calculationDto = CalZxLineService.getInstance().calculationDtoSetUp2(zxLine, new DateTime(order.getSetouttime()), order.getPdFlag(), order.getPeople(), order.getDistance());
            order.setBaseAmount(calculationDto.getZxLinePrice());
            order.setDistanceAmount(calculationDto.getTotalAmount().subtract(calculationDto.getZxLinePrice()));
            order.setTimeOutAmount(BigDecimal.ZERO);
            order.setAmount(order.getYgAmount());
            order.setRealPay(order.getYgAmount());
        } else {
            order.setBaseAmount(real.getBaseAmount());
            order.setDistanceAmount(real.getDistanceAmount());
            order.setTimeOutAmount(real.getTimeAmount());
        }
        final OrderLog payLog = new OrderLog();

        //扣保险
        try {
            OrderService.getInstance().deductionInsurance(order);
        } catch (InsuranceException e) {
            renderAjaxError(e.getMessage());
            logger.error(e.getMessage());
            return;
        }

        if (Constant.PayType.Collection == payType) {//代付直接计算总共支付多少钱
            order.setPayChannel(payType);
            /*MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
            if (realPay.compareTo(memberCapitalAccount.getAmount()) > 0) {}*/
            if (realPay.compareTo(CalCommissionService.getInstance().royaltyCalculation(order)) <= 0) {
                renderAjaxError("代付失败余额不足，请选择其他支付方式");
                return;
            }
            int tmpMinutes = 0;
            OrderLog createLog = OrderLog.dao.findByOrderAndPayAction(orderId, Constant.OrderAction.CREATE);
            if (createLog != null) {
                tmpMinutes = Minutes.minutesBetween(new DateTime(createLog.getOperationTime()), new DateTime(now)).getMinutes();
            }
            order.setConsumeTime(BigDecimal.valueOf(tmpMinutes));
            order.setStatus(Constant.OrderStatus.PAYED);
            order.setPayStatus(Constant.PayStatus.PAYED);
            order.setPayTime(DateTime.now().toDate());
            payLog.setAction(Constant.OrderAction.PAYED);
            payLog.setOrderId(orderId);
            payLog.setOperationTime(now.toDate());
            payLog.setRemark("司机司机已经代收");
            payLog.setLoginId(driverInfo.getLoginId());
            payLog.setOperater(driverInfo.getPhone());
            CalCommissionService.getInstance().daishoucommission(order);//计算提成
            if (order.getPdFlag()) {
                List<Order> orders = Order.dao.findByDriverNoComplete(driverInfo.getId());
                if (orders == null || orders.size() == 0) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                }

            } else {
                memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
            }
            EventKit.post(new Reward(order));//计算分销奖励
            order.setCoupon(0);
            EventKit.post(new OrderActivity(order));//参加活动
        } else {
            order.setPayChannel(-1);//未设置支付类型
            order.setPayStatus(Constant.PayStatus.NOPAY);
        }

        final OrderTrip orderTrip = OrderTrip.dao.findById(orderId);
        orderTrip.setEndTime(now.toDate());
        memberLogin.setLastUpdateTime(DateTime.now().toDate());
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (order.update() && arrivedLog.save() && orderTrip.update()) {
                    if (payLog.getAction() != null) {
                        return payLog.save() && memberLogin.update();
                    } else {
                        return true;
                    }
                }
                return false;
            }
        });
        if (isOk) {
            EventKit.post(new PushToPay(order));
            if (payType == Constant.PayType.Collection) {
                SmsKit.ordercomplete(order);
            }
            //结算完成之后判断司机预存款是否大于上线金额
            MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberLogin.getId());
            AdminSetting adminSetting = AdminSetting.dao.findFirst();
            int a = 0;
            try {
                a = adminSetting.getMinAmount();
            } catch (NullPointerException e) {
                renderAjaxError("请查看公司上线金额");
            }
            if (memberCapitalAccount.getAmount().compareTo(BigDecimal.valueOf(a)) < 0) {
                renderAjaxSuccess(real, "需要下线");
                DriverOrderCache.getInstance().del(order.getNo());
                DriverOrderCache.getInstance().del(DriverInfo.dao.findById(order.getDriver()));
                return;
            }
            renderAjaxSuccess(real);
            DriverOrderCache.getInstance().del(order.getNo());
            DriverOrderCache.getInstance().del(DriverInfo.dao.findById(order.getDriver()));
        } else {
            renderAjaxError("结算失败");
        }
    }

    /**
     * 司机开始等待
     */
    public void orderWait() {
        int orderId = getParaToInt("id", 0);
        DateTime now = DateTime.now();
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final Order order = Order.dao.findById(orderId);
        if (order.getStatus() == Constant.OrderStatus.START) {
            renderAjaxSuccess("无需再调用");
            return;
        }
        if (order == null) {
            renderAjaxError("订单不存在！");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已经取消");
            return;
        }
        order.setStatus(Constant.OrderStatus.DRIVERWAIT);//订单开始
        order.setLastUpdateTime(now.toDate());
        final OrderLog orderLog = new OrderLog();
        orderLog.setRemark(driverInfo.getRealName() + "已经到达预约地");
        orderLog.setOperationTime(now.toDate());
        orderLog.setOrderId(orderId);
        orderLog.setOperater(driverInfo.getPhone());
        orderLog.setLoginId(getLoginMember().getId());
        orderLog.setAction(Constant.OrderAction.DRIVERARRIVE);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return order.update() && orderLog.save();
            }
        });
        if (isOk) {
            PushToCustomer pushToCustomer = new PushToCustomer(order);
            EventKit.post(pushToCustomer);
            SmsKit.driverarrived(order);
            renderAjaxSuccess("已经到达预约地");
        } else {
            renderAjaxError("已经到达预约地");
        }
    }

    /**
     * 司机中途修改等待时间和实际距离
     */
    public void updateDistance() {
        final Order order = Order.dao.findById(getPara("id"));
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }

        //越权问题
        String userName = getLoginMember().getUserName();
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        if (!userName.equals(driverInfo.getPhone())) {
            renderAjaxError("当前用户身份异常！");
            return;
        }

        final String realDistanceStr = getPara("realDistance", "0");
        final String waitTime = getPara("waitTime", "0");
        BigDecimal realDistance = new BigDecimal(realDistanceStr);
        BigDecimal wait = new BigDecimal(waitTime);
        BigDecimal orderRealDistance = order.getRealDistance() == null ? BigDecimal.ZERO : order.getRealDistance();
        if (realDistance.compareTo(orderRealDistance) >= 0) {
            order.setRealDistance(realDistance);
        }
        if (wait.compareTo(BigDecimal.ZERO) != 0) {
            order.setWaitTime(wait);
        }
        boolean isOk = order.update();
        if (isOk) {
            renderAjaxSuccess("更新成功!");
        } else {
            renderAjaxError("更新失败!");
        }
    }

    /**
     * 司机修改终点
     */
    public void updateEndPoint() {
        final Order order = Order.dao.findById(getPara("id"));
        final String distanceStr = getPara("distance", "0");
        BigDecimal distance = new BigDecimal(distanceStr);
        if (distance.compareTo(BigDecimal.ZERO) != 0) {
            order.setDistance(distance);
        }
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        double lat = Doubles.tryParse(getPara("lat"));
        double lon = Doubles.tryParse(getPara("lon"));
        String title = getPara("title");
        final OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        if (lat != 0 && lon != 0) {
            orderTrip.set("end_latitude", lat);
            orderTrip.set("end_longitude", lon);
            orderTrip.update();
        }
        if (!Strings.isNullOrEmpty(title)) {
            order.setDestination(title);
        }
        final OrderLog orderLog = new OrderLog();
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        orderLog.setOperater(driverInfo.getPhone());
        orderLog.setOperationTime(DateTime.now().toDate());
        orderLog.setLoginId(getLoginMember().getId());
        orderLog.setOrderId(order.getId());
        orderLog.setRemark(driverInfo.getRealName() + "修改了终点！");
        orderLog.setAction(Constant.OrderAction.EDITEND);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return order.update() && orderLog.save();
            }
        });
        if (isOk) {
            renderAjaxSuccess("修改终点成功了");
        } else {
            renderAjaxError("修改终点失败了");
        }
    }

    /**
     * 重新进入
     */
    public void getOrder() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        List<Order> orders = Order.dao.findByDriverNoComplete(driverInfo.getId());
        for (Order order : orders) {
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
            order.put("nick_name", memberInfo.getNickName());
            order.put("head_portrait", memberInfo.getHeadPortrait());
            order.put("status", order.getStatus());
            orderTrip.put("endLatitude", orderTrip.getEndLatitude());
            orderTrip.put("endLongitude", orderTrip.getEndLongitude());
            orderTrip.put("startLatitude", orderTrip.getStartLatitude());
            orderTrip.put("startLongitude", orderTrip.getStartLongitude());
            orderTrip.put("member", orderTrip.getMember());
            orderTrip.put("orderId", orderTrip.getOrderId());
            order.put("trip", orderTrip);
            //会员的下单数
            order.put("memberOrderCount", (MemberOrderStatistics.dao.findByMemberId(order.getMember()) == null ? 0 : MemberOrderStatistics.dao.findByMemberId(order.getMember()).getOrderNum()));
        }
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (Order order : orders) {
                if (order.get("phone") != null) {
                    String phone = AESOperator.getInstance().encrypt(order.get("phone").toString());
                    order.put("phone", phone);
                }
            }
        }
        renderAjaxSuccess(orders);
    }

    /**
     * 检查车辆状态
     */
    public void updateCarStatus() {
        boolean status;
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        if (Redis.use(Constant.USECAR_ID_CACHE).exists(Constant.CARSTR + driverInfo.getLoginId())) {
            Integer carId = Redis.use(Constant.USECAR_ID_CACHE).get(Constant.CARSTR + driverInfo.getLoginId());
            //Redis.use(Constant.USECAR_ID_CACHE).setex(Constant.CARSTR + driverInfo.getLoginId(), Constant.CAR_TIMEOUT, carId);
            status = true;
        } else {
            driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            OnlineService.getInstance().offline(getLoginMember(), driverInfo);
            status = false;
        }
        renderAjaxSuccess(status + "");
    }

    /**
     * 获取司机的返利信息
     */
    public void fanli() {
        List<CapitalLog> capitalLogs = CapitalLog.dao.findByLoginIdAndFanli(getLoginMember().getId(), getPageStart(), getPageSize());
        renderAjaxSuccess(capitalLogs);
    }

    /**
     * 是否存在预约订单
     */
    public void hasyuyue() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        boolean has = Order.dao.findCountByDriverYuyue(driverInfo.getId()) > 0;
        renderAjaxSuccess(has + "");
    }

    /**
     * 获取预约订单
     */
    public void getYuyueOrder() {
        int orderId = getParaToInt("orderId", 0);
        Order order = Order.dao.findById(orderId);
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        orderTrip.put("endLatitude", orderTrip.getEndLatitude());
        orderTrip.put("endLongitude", orderTrip.getEndLongitude());
        orderTrip.put("startLatitude", orderTrip.getStartLatitude());
        orderTrip.put("startLongitude", orderTrip.getStartLongitude());
        orderTrip.put("member", orderTrip.getMember());
        orderTrip.put("orderId", orderTrip.getOrderId());
        String json = JSONObject.toJSONString(order);
        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.put("trip", orderTrip);
        jsonObject.put("nick_name", memberInfo.getNickName());
        jsonObject.put("head_portrait", memberInfo.getHeadPortrait());
        jsonObject.put("status", order.getStatus());
        renderAjaxSuccess(jsonObject);
    }

    /**
     * 申请注册顺风车
     */
    public void freeRide() {
        DriverInfo driverInfo = getDriverInfo();
        driverInfo.setLevel(Constant.SHUNFENGCHE_SHENHE);
        if (driverInfo.update()) {
            renderAjaxSuccess("申请成功！请等待审核..");
        } else {
            renderAjaxError("申请失败！");
        }
    }

    /**
     * 开启 or 关闭 顺风车消息
     */
    public void allow() {
        int param = getParaToInt("allow");
        DriverInfo driverInfo = getDriverInfo();
        driverInfo.setAllow(param);
        if (driverInfo.update()) {
            renderAjaxSuccess("操作成功！");
        } else {
            renderAjaxError("操作失败！");
        }
    }

    /**
     * 开启 or 关闭 专线消息
     */
    public void allow2() {
        int param = getParaToInt("allow");
        DriverInfo driverInfo = getDriverInfo();
        driverInfo.setAllow2(param);
        if (driverInfo.update()) {
            renderAjaxSuccess("操作成功！");
        } else {
            renderAjaxError("操作失败！");
        }
    }

    /**
     * 通过司机ID获取司机注册的类型
     */
    @ActionKey("/api/driver/findDriverTypeById")
    public void findDriverTypeById() {
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        Map<String, Object> dateMap = new HashMap<>();
        if (driverInfo != null) {
            dateMap.put("driverType", driverInfo.getType());
            fail(200, "OK", true, dateMap);
        } else {
            renderAjaxError("无法获取司机信息!");
        }
    }

    /**
     * 更新司机的车辆位置
     */
    /**
     * @api {Post} /api/driver/updateHistoricalLocation   更新司机的车辆位置v2
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {String} traceLatLngList
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @ActionKey("/api/driver/updateHistoricalLocation")
    @Before(POST.class)
    public void updateHistoricalLocation() {
        String traceLatLngList = getPara("traceLatLngList", "");
        if (!Strings.isNullOrEmpty(traceLatLngList)) {
            HistoricalLocation historicalLocation = analysis(traceLatLngList);
            if (HistoricalLocationCache.getInstance().saveOrUpdate(historicalLocation)) {
                //通过司机更新位置来实时更新司机上线存活时间
                DriverOnlineCache driverOnlineCache = DriverStatusCache.getDriverOnlineCache(getLoginMember().getId());
                if (driverOnlineCache != null) {
                    driverOnlineCache.setOnlineTime(System.currentTimeMillis());
                } else {
                    driverOnlineCache = new DriverOnlineCache(getLoginMember().getId(), -1, Constant.LoginStatus.LOGINED, System.currentTimeMillis());
                }
                DriverStatusCache.updateDriverStatus(driverOnlineCache);
                renderAjaxSuccess("更新位置成功！");
            } else {
                renderAjaxError("更新位置失败!");
            }
        } else {
            renderAjaxError("更新位置失败!原因：traceLatLngList为空");
        }


    }

    /**
     * 司机拒单（就是司机点×的接口）
     */
    /**
     * @api {Post} /api/driver/refuse/order  司机拒单（就是司机点×的接口）
     * @apiGroup driver
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiHeader {String} secret
     * @apiHeader {String} toke
     * @apiParam {int}  orderId
     * @apiSuccessExample Success-Response:
     * 参数错误
     * or
     * 拒单失败
     * or
     * 拒单完成
     */
    @ActionKey("/api/driver/refuse/order")
    @Before(POST.class)
    public void refuse() {
        int orderId = getParaToInt("orderId", 0);
        int driverId = getDriverInfo().getId();
        if (orderId == 0 || driverId == 0) {
            renderAjaxError("参数错误！");
            return;
        }
        RejectLog rejectLog = RejectLog.dao.findRejectLog(orderId, driverId);
        if (rejectLog == null) {
            renderAjaxError("拒单失败！");
            return;
        }
        rejectLog.setStatus(2);
        rejectLog.update();
        renderAjaxSuccess("拒单完成！");
    }

    /**
     * 解析 经纬度json
     *
     * @param jsonArray
     * @return
     */
    private HistoricalLocation analysis(String jsonArray) {
        JSONArray array = JSONObject.parseArray(jsonArray);
        HistoricalLocation location = new HistoricalLocation();
        location.setLoginId(getLoginMember().getId());
        List<HistoricalLocation.traceLatLng> traceLatLngList = Lists.newArrayList();
        for (int i = 0; i < array.size(); i++) {
            HistoricalLocation.traceLatLng traceLatLng = new HistoricalLocation.traceLatLng();
            traceLatLngList.add(array.getObject(i, HistoricalLocation.traceLatLng.class));
        }
        location.setTraceLatLngList(traceLatLngList);
        return location;
    }

}
