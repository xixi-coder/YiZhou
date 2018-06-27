
package controllers.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.redis.Redis;

import net.dreamlu.event.EventKit;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import dto.CalculationDto;
import dto.JPushToMemberDto;
import dto.alipaydto.BizContentDto;
import jobs.activity.OrderActivity;
import jobs.pushorder.CancelOrder;
import jobs.pushorder.PushOrder;
import jobs.pushtocustomer.PushToCustomer;
import jobs.pushtocustomer.PushToCustomerConfig;
import jobs.pushtocustomer.PushToPay;
import jobs.reward.Reward;
import kits.OrderInfoKit;
import kits.SmsKit;
import kits.StringsKit;
import kits.TimeKit;
import kits.cache.MemberOrderCache;
import models.activity.Coupon;
import models.activity.MemberCoupon;
import models.car.Car;
import models.company.Company;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.member.MemberRealLocation;
import models.order.InvoiceOrder;
import models.order.InvoiceRec;
import models.order.Order;
import models.order.OrderAddAmountRecord;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.order.TraverRecord;
import models.sys.AdminSetting;
import models.sys.ChargeStandard;
import models.sys.ChargeStandardItem;
import models.sys.RecordLog;
import models.sys.Schedule;
import models.sys.ServiceTypeItem;
import models.vip.VipInfo;
import models.vip.VipLog;
import services.CalCommissionService;
import services.CalService;
import services.GetTraverService;
import services.OrderService;
import services.PushOrderService;
import utils.AESOperator;
import utils.DateUtil;
import wechatpay.WechatPayKit;

/**
 * Created by BOGONj on 2016/9/1.
 */
@Controller("/api/order")
@SuppressWarnings("All")
public class OrderController extends BaseApiController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * @api {get} /api/order/calamount  获取预估价格
     * @apiGroup D_OrderController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {String} type  服务项目的大项类型
     * @apiParam {String} cityCode     城市code
     * @apiParam {String} setOutTime   预约时间
     * @apiParam {String} time   大约用时
     * @apiParam {String} distance  距离
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "code": 200,
     * "data": {},
     * "message": "success",
     * "isSuccess": true
     * }
     */
    @ActionKey("/api/order/calamount")
    public void calAmount() {
        int type = getParaToInt("type", 1);//服务项目的大项类型
        MemberInfo memberInfo = null;
        if (getLoginMember() != null) {
            memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        }
        String cityCode = getPara("cityCode");
        Long strSetOutTime = getParaToLong("setOutTime", System.currentTimeMillis());
        String setOutTime = null;
        if (t(strSetOutTime)) {
            setOutTime = DateUtil.stampToDate(strSetOutTime);
        }

        BigDecimal distance = new BigDecimal(getPara("distance"));


        Company company;
        if (Strings.isNullOrEmpty(cityCode)) {
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(cityCode);
        }
        if (company == null) {
            renderAjaxFailure("该地区无法使用！");
            return;
        }
        BigDecimal times = new BigDecimal(getPara("time") == null ? "0" : getPara("time"));
        renderAjaxSuccess(CalService.getInstance().calculationDtoSetUp1(type, company, memberInfo, setOutTime, distance, times));
    }

    /**
     * 顺风车计算价格
     */
    public void shunAmount() {
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        String cityCode = getPara("cityCode");
        BigDecimal distance = new BigDecimal(getPara("distance"));
        int people = getParaToInt("people", 1);
        Company company;
        if (Strings.isNullOrEmpty(cityCode)) {//判断是否有cityCode，如果没有就用用户自己的公司
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(cityCode);
        }
        if (company == null) {
            renderAjaxFailure("该地区无法使用！");
            return;
        }
        renderAjaxSuccess(CalService.getInstance().calculationDtoSetUp2(company, memberInfo, distance, people));
    }

    /**
     * 发布行程
     */
    public void traver() {
        if (getAppType() == Constant.DRIVER && !DriverInfo.dao.useFlag(getDriverInfo().getId())) {
            renderAjaxError("请选择车辆上线！");
            return;
        }

        int type = getAppType();//1:司机发布 2：乘客发布
        int record_type = getParaToInt("recordType", 0);//1:市内 2：跨城 3：带货
        String sCityCode = getPara("sCityCode");
        String eCityCode = getPara("eCityCode");
        if (record_type == 1 && !sCityCode.equals(eCityCode)) {
            renderAjaxError("市内顺风车只能选择同一个城市！");
            return;
        }
        if (record_type == 2 && sCityCode.equals(eCityCode)) {
            renderAjaxError("城际顺风车只能选择不同城市！");
            return;
        }
        String sCity = getPara("sCity");
        String eCity = getPara("eCity");
        String startTime = getPara("timeStart");
        BigDecimal distance = new BigDecimal(getPara("distance"));
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        String startLongitude = getPara("startLongitude");
        String startLatitude = getPara("startLatitude");
        String endLongitude = getPara("endLongitude");
        String endLatitude = getPara("endLatitude");
        String sWhere = getPara("reservationAddress");
        String eWhere = getPara("destination");
        String endTime = getPara("timeEnd");
        if (Strings.isNullOrEmpty(sCityCode) ||
                Strings.isNullOrEmpty(eCityCode) ||
                Strings.isNullOrEmpty(sCity) ||
                Strings.isNullOrEmpty(eCity) ||
                Strings.isNullOrEmpty(startTime) ||
                type == 0 || record_type == 0) {
            renderAjaxError("参数不能为空！");
            return;
        }
        TraverRecord traverRecord = new TraverRecord();
        if (type == 2) {
            int people = getParaToInt("people", 0);
            traverRecord.setPeople(people);
            traverRecord.setMess(getPara("mess"));
            int pgFlag = getParaToInt("pdFlag", 0);
            traverRecord.setFlag(pgFlag == 1 ? true : false);
            traverRecord.setMemberId(getMemberInfo().getId());
            if (record_type == 3) {
                traverRecord.setJiHuoAmount(amount);
            } else if (pgFlag == 1) {
                traverRecord.setPinCheAmount(amount);
            } else {
                traverRecord.setAmount(amount);
            }
        } else {
            BigDecimal pinCheAmount = new BigDecimal(getPara("pinCheAmount"));
            BigDecimal jiHuoAmount = new BigDecimal(getPara("jiHuoAmount"));
            traverRecord.setPinCheAmount(pinCheAmount);
            traverRecord.setJiHuoAmount(jiHuoAmount);
            traverRecord.setAmount(amount);
            traverRecord.setPeople(0);
            traverRecord.setTotalPeople(getParaToInt("totalPeople", 4));
            traverRecord.setDriverId(getDriverInfo().getId());
        }
        if (record_type == 3) {
            String consigneeName = getPara("consigneeName");
            String consigneePhone;
            if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
                consigneePhone = AESOperator.getInstance().decrypt(getPara("consigneePhone"));
            } else {
                consigneePhone = getPara("consigneePhone");
            }
            if (Strings.isNullOrEmpty(consigneeName) || Strings.isNullOrEmpty(consigneePhone)) {
                renderAjaxError("收货人信息不能为空！");
                return;
            }
            traverRecord.setConsigneeName(consigneeName);
            traverRecord.setConsigneePhone(consigneePhone);
        }
        traverRecord.setStatus(Constant.TraverStatus.CREATE);
        traverRecord.setCreateTime(DateTime.now().toDate());
        traverRecord.setDistance(distance);
        traverRecord.setStartAdcode(sCityCode);
        traverRecord.setEndAdcode(eCityCode);
        Date stime = DateTime.parse(startTime, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMM)).toDate();
        traverRecord.setSetoutTime1(stime);
        Date etime = DateTime.parse(endTime, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMM)).toDate();
        traverRecord.setSetoutTime2(etime);
        traverRecord.setType(type);
        traverRecord.setReservationCity(sCity);
        traverRecord.setDestinationCity(eCity);
        traverRecord.setReservationAddress(sWhere);
        traverRecord.setDestination(eWhere);
        traverRecord.setRecordType(record_type);
        traverRecord.setStartLatitude(new Double(startLatitude));
        traverRecord.setEndLatitude(new Double(endLatitude));
        traverRecord.setStartLongitude(new Double(startLongitude));
        traverRecord.setEndLongitude(new Double(endLongitude));
        if (traverRecord.save()) {
            RecordLog recordLog = new RecordLog();
            recordLog.setCreateTime(DateTime.now().toDate());
            recordLog.setRecordId(traverRecord.getId());
            recordLog.setLoginId(getLoginMember().getId());
            recordLog.setStatus(Constant.TraverStatus.CREATE);
            recordLog.setType(1);
            if (type == 2) {
                if (traverRecord.getAmount() != null) {
                    recordLog.setAmount(traverRecord.getAmount());
                } else if (traverRecord.getPinCheAmount() != null) {
                    recordLog.setPinCheAmount(traverRecord.getPinCheAmount());
                } else if (traverRecord.getJiHuoAmount() != null) {
                    recordLog.setJiHuoAmount(traverRecord.getJiHuoAmount());
                }
            } else {
                recordLog.setAmount(traverRecord.getAmount());
                recordLog.setPinCheAmount(traverRecord.getPinCheAmount());
                recordLog.setJiHuoAmount(traverRecord.getJiHuoAmount());
            }
            recordLog.save();
            if (record_type == 2 && type == 2) {
                recordLog.setPeople(traverRecord.getPeople());
                recordLog.update();
                Order order = GetTraverService.getInstance().createChenJiOrder(type, traverRecord, getLoginMember());
                if (order != null) {
                    renderAjaxSuccess("发布成功！等待司机接单");
                }
            } else {
                renderAjaxSuccess("发布成功！");
            }
        } else {
            renderAjaxError("发布失败！");
        }
    }

    /**
     * 乘客端获取行程
     */
    public void getTraver() {
        int type = getAppType();//1:司机 2：乘客
        int recordType = getParaToInt("recordType", 0);//1:市内 2：跨城 3：带货
        String sCityCode = getPara("sCityCode");
        String eCityCode = getPara("eCityCode");
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        MemberRealLocation memberRealLocation = MemberRealLocation.dao.findByMember(getMemberInfo().getId());
        List<TraverRecord> traverRecords = null;
        if (memberRealLocation == null) {
            traverRecords = GetTraverService.getInstance().getTraver(0.0, 0.0, type, recordType, sCityCode, eCityCode, pageStart, pageSize, getMemberInfo());
        } else {
            traverRecords = GetTraverService.getInstance().getTraver(memberRealLocation.getLatitude(), memberRealLocation.getLongitude(), type, recordType, sCityCode, eCityCode, pageStart, pageSize, getMemberInfo());

        }

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (TraverRecord traverRecord : traverRecords) {
                if (traverRecord.getPhone() != null) {
                    String phone = AESOperator.getInstance().encrypt(traverRecord.getPhone());
                    traverRecord.setPhone(phone);
                }
            }
        }
        renderAjaxSuccess(traverRecords);
    }

    /**
     * @api {get} /api/order/findSFCList  搜索顺风车列表
     * @apiGroup D_OrderController
     * @apiVersion 2.0.0
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {int} recordType    1 市内；2 城际；3 带货
     * @apiParam {String} sAddress   搜索起点
     * @apiParam {String} eAddress   搜索终点
     * @apiParam {int} searchTimeType  0 一小时内  1 今天   2 明天
     * @apiParam {int} pageIndex    当前页
     * @apiParam {int} pageSize  每页的条数
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "code": 200,
     * "data": {},
     * "message": "success",
     * "isSuccess": true
     * }
     */
    @Deprecated
    public void findSFCList() {
        List<TraverRecord> lists = Lists.newArrayList();
        int recordType = getParaToInt("recordType", 1);
        String sAddress = getPara("sAddress", "");
        double startLat = Double.valueOf(getPara("startLat", "0"));
        double startLong = Double.valueOf(getPara("startLong", "0"));
        String eAddress = getPara("eAddress", "");
        double endLat = Double.valueOf(getPara("endLat", "0"));
        double endLong = Double.valueOf(getPara("endLong", "0"));

        int searchTimeType = getParaToInt("searchTimeType", 0);
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        int appType = getAppType();
        // 0 一小时内  1 今天   2 明天
        Date sTime = DateUtil.date();
        Date eTime = null;
        switch (searchTimeType) {
            case 0:
                eTime = DateUtil.offsetHour(sTime, 1);
                break;
            case 1:
                sTime = DateUtil.beginOfDay(sTime);
                eTime = DateUtil.endOfDay(sTime);
                break;
            case 2:
                sTime = DateUtil.beginOfDay(DateUtil.yesterday());
                eTime = DateUtil.endOfDay(DateUtil.yesterday());
                break;
            default:
                eTime = DateUtil.offsetHour(sTime, 1);
                break;
        }


        lists = TraverRecord.dao.findSFCList(recordType, sAddress, eAddress, sTime, eTime, appType, pageStart, pageSize);
        renderAjaxSuccess(lists);
    }

    /**
     * @api {get} /api/order/findSFCListTwo  搜索顺风车列表Two
     * @apiGroup D_OrderController
     * @apiVersion 2.0.0
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {int} recordType    1 市内；2 城际；3 带货
     * @apiParam {String} sCityCode   起点城市code
     * @apiParam {String} eCityCode   终点城市code
     * @apiParam {String} startLat   搜索起点经纬度
     * @apiParam {String} startLon   搜索起点点经纬度
     * @apiParam {String} endLat   搜索终点经纬度
     * @apiParam {String} endLon   搜索终点经纬度
     * @apiParam {int} searchTimeType  0 一小时内  1 今天   2 明天
     * @apiParam {int} pageIndex    当前页
     * @apiParam {int} pageSize  每页的条数
     * @apiSuccessExample Success-Response:
     */
    public void findSFCListTwo() {
        List<TraverRecord> lists = Lists.newArrayList();
        int recordType = getParaToInt("recordType", 1);
        double startLat = Double.valueOf(getPara("startLat", "0"));
        double startLon = Double.valueOf(getPara("startLon", "0"));
        double endLat = Double.valueOf(getPara("endLat", "0"));
        double endLon = Double.valueOf(getPara("endLon", "0"));
        int searchTimeType = getParaToInt("searchTimeType", 0);
        String sCityCode = getPara("sCityCode", "340100");
        String eCityCode = getPara("eCityCode", "340100");
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        int appType = getAppType();
        // 0 一小时内  1 今天   2 明天
        Date sTime = DateUtil.date();
        Date eTime = null;
        switch (searchTimeType) {
            case 0:
                eTime = DateUtil.offsetHour(sTime, 1);
                break;
            case 1:
                sTime = DateUtil.beginOfDay(sTime);
                eTime = DateUtil.endOfDay(sTime);
                break;
            case 2:
                sTime = DateUtil.beginOfDay(DateUtil.tomorrow());
                eTime = DateUtil.endOfDay(DateUtil.tomorrow());
                break;
            default:
                sTime = DateUtil.beginOfDay(sTime);
                eTime = DateUtil.endOfDay(sTime);
                break;
        }
        List<TraverRecord> traverRecords = null;

        if (appType == 1) {
            DriverInfo driverInfo = getDriverInfo();
            DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
            if (driverRealLocation == null) {
                traverRecords = GetTraverService.getInstance().getDriverTraverTwo(0.0, 0.0, recordType, driverInfo.getId(), sCityCode, eCityCode, pageStart, pageSize, startLat, startLon, endLat, endLon, sTime, eTime);
            } else {
                traverRecords = GetTraverService.getInstance().getDriverTraverTwo(driverRealLocation.getLatitude(), driverRealLocation.getLongitude(), recordType, driverInfo.getId(), sCityCode, eCityCode, pageStart, pageSize, startLat, startLon, endLat, endLon, sTime, eTime);
            }
        } else {
            MemberRealLocation memberRealLocation = MemberRealLocation.dao.findByMember(getMemberInfo().getId());
            if (memberRealLocation == null) {
                traverRecords = GetTraverService.getInstance().getTraverTwo(0.0, 0.0, appType, recordType, sCityCode, eCityCode, pageStart, pageSize, getMemberInfo(), startLat, startLon, endLat, endLon, sTime, eTime);
            } else {
                traverRecords = GetTraverService.getInstance().getTraverTwo(memberRealLocation.getLatitude(), memberRealLocation.getLongitude(), appType, recordType, sCityCode, eCityCode, pageStart, pageSize, getMemberInfo(), startLat, startLon, endLat, endLon, sTime, eTime);
            }
        }
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (TraverRecord traverRecord : traverRecords) {
                if (traverRecord.getPhone() != null) {
                    String phone = AESOperator.getInstance().encrypt(traverRecord.getPhone());
                    traverRecord.setPhone(phone);
                }
            }
        }
        renderAjaxSuccess(traverRecords);
    }

    /**
     * 司机获取行程
     */
    public void getCustomerTraver() {
        if (getAppType() == Constant.DRIVER && !DriverInfo.dao.useFlag(getDriverInfo().getId())) {
            renderAjaxError("请选择车辆上线！");
            return;
        }
        if (getDriverInfo() != null) {
            if (getDriverInfo().getAllow() != 1) {
                renderAjaxError("请打开接收顺风车消息！");
                return;
            }
        }
        int recordType = getParaToInt("recordType", 0);
        String sCityCode = getPara("sCityCode");
        String eCityCode = getPara("eCityCode");
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        DriverInfo driverInfo = getDriverInfo();
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        List<TraverRecord> traverRecords = null;
        if (driverRealLocation == null) {
            traverRecords = GetTraverService.getInstance().getDriverTraver(0.0, 0.0, recordType, driverInfo.getId(), sCityCode, eCityCode, pageStart, pageSize);
        } else {
            traverRecords = GetTraverService.getInstance().getDriverTraver(driverRealLocation.getLatitude(), driverRealLocation.getLongitude(), recordType, driverInfo.getId(), sCityCode, eCityCode, pageStart, pageSize);
        }

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (TraverRecord traverRecord : traverRecords) {
                if (traverRecord.getPhone() != null) {
                    String phone = AESOperator.getInstance().encrypt(traverRecord.getPhone());
                    traverRecord.setPhone(phone);
                }
            }
        }
        renderAjaxSuccess(traverRecords);
    }

    /**
     * 乘客确认同行
     */
    public void confirmPeer() {
        int id = getParaToInt("customerRecordId", 0);
        int people = getParaToInt("people", 1);
        TraverRecord traverRecord = TraverRecord.dao.findById(id);
        if (traverRecord == null) {
            renderAjaxError("该行程已经取消，请选择其他行程！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.CANCEL) {
            renderAjaxError("该行程已取消，请选择其他行程！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.ACTION) {
            renderAjaxError("该行程已经出发，请选择其他行程！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.END) {
            renderAjaxError("该行程已经结束，请选择其他行程！");
            return;
        }
        int type = getParaToInt("type", 0); // 1:拼车 2：包车 3：寄货
        if (type == 1) {
            if (RecordLog.dao.findBaoChe(id, DriverInfo.dao.findById(traverRecord.getDriverId()).getLoginId())) {
                renderAjaxError("该司机已接包车订单,请选择其他行程！");
                return;
            }
        } else if (type == 2) {
            if (RecordLog.dao.findPinChe(id, DriverInfo.dao.findById(traverRecord.getDriverId()).getLoginId())) {
                renderAjaxError("该司机已接拼车订单，无法包车！");
                return;
            }
        }
        int driverRecordId = getParaToInt("driverRecordId", 0);
        if (driverRecordId != 0) {
            TraverRecord traverRecord1 = TraverRecord.dao.findById(driverRecordId);
            traverRecord1.setPeople(traverRecord1.getPeople() == 0 ? traverRecord.getPeople() : traverRecord1.getPeople() + traverRecord.getPeople());
            traverRecord1.update();
        } else {
            /*if (RecordLog.dao.findByRecordId(traverRecord.getId(), getLoginMember().getId()) != null) {
                renderAjaxError("您已加入该行程，无需重复选择");
               return;
            }*/
            if (traverRecord.getPeople() + people > traverRecord.getTotalPeople() && type != 3) {
                renderAjaxError("该行程座位不足，请减少人数或者选择别的行程");
                return;
            }
            if (type == 2) {
                if (RecordLog.dao.findByBaoChe(traverRecord.getId())) {
                    renderAjaxError("该行程包车客户已满，请选择拼车或其他行程！");
                    return;
                }
            }
            traverRecord.update();
        }
        MemberLogin memberLogin = getLoginMember();
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
        if (memberInfo.getStatus() == Constant.MemberInfoStatus.BLACK) {
            renderAjaxError("用户存在异常,无法下单!");
            return;
        }
        Company company;
        if (Strings.isNullOrEmpty(traverRecord.getStartAdcode())) {//判断是否有cityCode，如果没有就用用户自己的公司
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(traverRecord.getStartAdcode());
        }
        if (company == null) {
            renderAjaxFailure("该地区无法使用顺风车！");
            return;
        }
        List<Order> orderLists = Order.dao.findByMemberForNoPay(memberInfo.getId());
        if (orderLists != null && orderLists.size() > 0) {
            List<JSONObject> realOrders = Lists.newArrayList();
            for (Order order1 : orderLists) {
                DriverInfo driverInfo = DriverInfo.dao.findById(order1.getDriver());
                driverInfo = DriverInfo.dao.findById(driverInfo.getId());
                DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
                Car car = Car.dao.findByDriver(driverInfo.getId());
                JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
                jPushToMemberDto.setDriverId(driverInfo.getId());
                jPushToMemberDto.setLatitude(driverRealLocation.getLatitude() + "");
                jPushToMemberDto.setLongitude(driverRealLocation.getLongitude() + "");
                jPushToMemberDto.setNickName(driverInfo.getRealName());
                jPushToMemberDto.setPhone(driverInfo.getPhone());
                jPushToMemberDto.setCar(car);
                jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
                String odcontent = JSONObject.toJSONString(jPushToMemberDto);
                JSONObject od = JSONObject.parseObject(odcontent);
                OrderTrip orderTrip = OrderTrip.dao.findById(order1.getId());
                String tt = JSONObject.toJSONString(orderTrip);
                JSONObject trip = JSONObject.parseObject(tt);
                od.put("trip", trip);
                od.put("reservationAddress", order1.getReservationAddress());
                od.put("destination", order1.getDestination());
                od.put("amount", order1.getAmount());
                od.put("status", order1.getStatus());
                od.put("orderId", order1.getId());
                od.put("realPay", order1.getRealPay() == null ? BigDecimal.ZERO : order1.getRealPay());
                od.put("payChannel", order1.getPayChannel());
                realOrders.add(od);
            }
            renderAjaxFailure("有未完成的订单", realOrders);
            return;
        }
        Order order = GetTraverService.getInstance().createOrder(type, traverRecord, getLoginMember(), people);
        if (order != null) {
            renderAjaxSuccess(order.getId(), "你的订单已经创建,等待司机确认");
        } else {
            renderAjaxError("下单失败！");
        }
    }

    /**
     * 司机选择乘客
     */
    public void choseCustomer() {
        int id = getParaToInt("customerRecordId", 0);
        if (id == 0) {
            renderAjaxError("该乘客已经在行程中了，请选择其他乘客！");
            return;
        }
        int memberId = getParaToInt("memberId", 0);
        final TraverRecord traverRecord = TraverRecord.dao.findById(id);
        if (traverRecord.getStatus() == Constant.TraverStatus.CANCEL) {
            renderAjaxSuccess("该行程已取消，请选择其他乘客！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.ACCEPT || traverRecord.getStatus() == Constant.TraverStatus.ACTION) {
            renderAjaxSuccess("该乘客已经加入行程，请选择其他乘客！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.END) {
            renderAjaxSuccess("该行程已结束，请选择其他行程！");
            return;
        }

        //查询司机需要执行的顺风车订单
        if (Order.dao.findCountShunByDid(getDriverInfo().getId()) > 0) {
            renderAjaxError("您有未完成订单,请完成当前订单");
            return;
        }

        MemberInfo memberInfo;
        if (RecordLog.dao.findByRecordId(traverRecord.getId(), getLoginMember().getId()) != null) {
            renderAjaxError("您已选择该乘客，无需重复选择");
            return;
        }
        if (traverRecord.getMemberId() == null && memberId != 0) {
            memberInfo = MemberInfo.dao.findById(memberId);
        } else {
            memberInfo = MemberInfo.dao.findById(traverRecord.getMemberId());
        }
        MemberLogin memberLogin = MemberLogin.dao.findById(memberInfo.getLoginId());
        DriverInfo driverInfo = getDriverInfo();
        final RecordLog recordLog = new RecordLog();
        if (traverRecord.decide(id, driverInfo.getId())) {
            recordLog.setLoginId(memberLogin.getId());
            RecordLog recordLog1 = RecordLog.dao.findByRecordIdAndLoginId(traverRecord.getId(), memberLogin.getId());
            if (recordLog1 == null) {
                renderAjaxError("该乘客已加入行程，请选择其他乘客！");
                return;
            }
            recordLog.setPeople(recordLog1.getPeople());
            traverRecord.setPeople(recordLog1.getPeople() + traverRecord.getPeople());
            if (recordLog1.getAmount() != null) {
                recordLog.setAmount(recordLog1.getAmount());
            } else if (recordLog1.getPinCheAmount() != null) {
                recordLog.setPinCheAmount(recordLog1.getPinCheAmount());
            } else if (recordLog1.getJiHuoAmount() != null) {
                recordLog.setJiHuoAmount(recordLog1.getJiHuoAmount());
            }
            if (RecordLog.dao.findByCount(traverRecord.getId(), recordLog.getLoginId()) > 1) {
                renderAjaxError("您已选择该乘客，无需重复选择");
                return;
            }
            recordLog1.delete();
        } else {
            recordLog.setLoginId(getLoginMember().getId());
            if (traverRecord.getAmount() != null) {
                recordLog.setAmount(traverRecord.getAmount());
            } else if (traverRecord.getJiHuoAmount() != null) {
                recordLog.setJiHuoAmount(traverRecord.getJiHuoAmount());
            } else if (traverRecord.getPinCheAmount() != null) {
                recordLog.setPinCheAmount(traverRecord.getPinCheAmount());
            }
            traverRecord.setDriverId(getDriverInfo().getId());
        }
        recordLog.setType(2);
        recordLog.setRecordId(traverRecord.getId());
        recordLog.setCreateTime(DateTime.now().toDate());
        recordLog.setStatus(Constant.TraverStatus.CREATE);
        if (memberInfo != null) {
            recordLog.save();
            if (PushOrderService.getIntance().pushToSFCCustomer(traverRecord, driverInfo, memberLogin, recordLog.getId())) {
                if (!traverRecord.decide(recordLog.getRecordId(), getDriverInfo().getId())) {
                    traverRecord.setStatus(Constant.TraverStatus.ACCEPT);
                }
                recordLog.setStatus(Constant.TraverStatus.ACCEPT);
                boolean isOK = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        return recordLog.update() && traverRecord.update();
                    }
                });
                if (isOK) {
                    renderAjaxSuccess("加入行程成功！");
                } else {
                    renderAjaxError("添加失败！");
                }
            }
        } else {
            renderAjaxError("乘客不存在！");
        }
    }

    /**
     * 乘客确认司机选择
     */
    public void customerComfirm() {
        int id = getParaToInt("orderId", 0);
        int logId = getParaToInt("logId", 0);
        int status = getParaToInt("status", 0);
        final Order order = Order.dao.findById(id);
        TraverRecord traverRecord = TraverRecord.dao.findById(order.getTraverId());
        final RecordLog recordLog = RecordLog.dao.findById(logId);
        order.put("status", status);
        EventKit.post(new PushToCustomerConfig(order));
        if (status == 1) {
            traverRecord.setStatus(Constant.TraverStatus.ACCEPT);
            recordLog.setStatus(Constant.TraverStatus.ACCEPT);
            boolean isOK = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return order.update() && recordLog.update();
                }
            });
            if (isOK) {
                renderAjaxSuccess("成功加入司机行程");
            } else {
                renderAjaxError("确认失败！");
            }
        } else {
            renderAjaxSuccess("成功拒绝了司机");
        }

    }

    /**
     * 司机修改人数
     */
    public void changeMan() {
        int id = getParaToInt("traverId", 0);
        int people = getParaToInt("people", 0);
        TraverRecord traverRecord = TraverRecord.dao.findById(id);
        if (people < traverRecord.getPeople()) {
            renderAjaxError("人数不能小于已有乘客人数！");
            return;
        }
        traverRecord.setTotalPeople(people);
        if (traverRecord.update()) {
            renderAjaxSuccess("人数修改成功！");
        } else {
            renderAjaxError("人数修改失败！");
        }
    }

    /**
     * 查看行程
     */
    public void historyRecord() {
        int status = getParaToInt("status", 0);
        if (getAppType() == Constant.DRIVER && status == 2) {
            if (!DriverInfo.dao.useFlag(getDriverInfo().getId())) {
                renderAjaxError("请选择车辆上线！");
                return;
            }
        }
        int type = getAppType();
        List<TraverRecord> traverRecords = TraverRecord.dao.findHistory(type, getLoginMember().getId(), status, getPageStart(), getPageSize());

        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (TraverRecord traverRecord : traverRecords) {
                if (traverRecord.get("driverPhone") != null) {
                    String phone = AESOperator.getInstance().encrypt(traverRecord.get("driverPhone").toString());
                    traverRecord.put("driverPhone", phone);
                }

            }
        }
        renderAjaxSuccess(traverRecords, "获取成功！");
    }

    /**
     * 结束行程
     */
    public void endRecord() {
        int id = getParaToInt("id", 0);
        RecordLog recordLog = RecordLog.dao.findById(id);
        if (recordLog == null) {
            renderAjaxError("行程已经结束，请下拉刷新");
        }
        recordLog.setStatus(Constant.TraverStatus.END);
        if (recordLog.update()) {
            renderAjaxSuccess("行程结束成功！");
        } else {
            renderAjaxError("行程结束失败！");
        }
    }

    /**
     * 获取乘客位置信息
     */
    public void memberLocation() {
        int id = getParaToInt("id", 0);
        MemberRealLocation memberRealLocation = MemberRealLocation.dao.findByMember(id);
        if (memberRealLocation == null) {
            renderAjaxError("未能获取乘客位置信息！");
            return;
        } else {
            renderAjaxSuccess(memberRealLocation, "获取成功！");
        }
    }

    /**
     * 查看行程客户信息
     */
    public void showCustomer() {
        int id = getParaToInt("id", 0);
        int pd = getParaToInt("pd", 3);
        RecordLog recordLog = RecordLog.dao.findById(id);
        int recordId = recordLog.getRecordId();
        if (pd == 3 && TraverRecord.dao.decide(recordLog.getRecordId(), getDriverInfo().getId()) && TraverRecord.dao.findById(recordLog.getRecordId()).getMemberId() == null) {
            renderAjaxError("该行程暂无客户！");
            return;
        }
        List<RecordLog> recordLogs = RecordLog.dao.findByRecordsId(pd, recordId, recordLog.getLoginId());
        if (recordLogs == null) {
            renderAjaxError("该行程没有客户！");
            return;
        }
        List<MemberInfo> memberInfos = Lists.newArrayList();
        for (RecordLog record : recordLogs) {
            TraverRecord traverRecord = TraverRecord.dao.findById(record.getRecordId());
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(record.getLoginId());
            if (memberInfo != null) {
                memberInfo.put("people", record.getPeople());
                if (record.getAmount() != null) {
                    memberInfo.put("money", record.getAmount());
                    memberInfo.put("pd", 0);
                } else if (record.getPinCheAmount() != null) {
                    memberInfo.put("money", record.getPinCheAmount());
                    memberInfo.put("pd", 1);
                } else {
                    memberInfo.put("money", record.getJiHuoAmount());
                    memberInfo.put("pd", 2);
                }
                memberInfos.add(memberInfo);
            } else {
                MemberInfo memberInfo1 = MemberInfo.dao.findById(TraverRecord.dao.findById(record.getRecordId()).getMemberId());
                if (memberInfo1 != null) {
                    memberInfo1.put("people", traverRecord.getPeople());
                    if (record.getAmount() != null) {
                        memberInfo1.put("money", record.getAmount());
                        memberInfo1.put("pd", 0);
                    } else if (record.getPinCheAmount() != null) {
                        memberInfo1.put("money", record.getPinCheAmount());
                        memberInfo1.put("pd", 1);
                    } else {
                        memberInfo1.put("money", record.getJiHuoAmount());
                        memberInfo1.put("pd", 2);
                    }
                    memberInfos.add(memberInfo1);
                } else {
                    renderAjaxError("该行程没有客户！");
                    return;
                }
            }
        }
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (MemberInfo memberInfo : memberInfos) {
                if (memberInfo.get("phone") != null) {
                    String phone = AESOperator.getInstance().encrypt(memberInfo.get("phone").toString());
                    memberInfo.put("phone", phone);
                }
            }
        }
        renderAjaxSuccess(memberInfos);
    }

    /**
     * 取消行程
     */
    public void shunFengCheCancel() {
        int id = getParaToInt("id", 0);
        int pd = getParaToInt("pd", 3);
        int type = getAppType();
        final RecordLog recordLog = RecordLog.dao.findById(id);
        if (recordLog == null) {
            renderAjaxError("行程已取消，请下拉刷新");
            return;
        }
        final TraverRecord traverRecord = TraverRecord.dao.findById(recordLog.getRecordId());
        if (traverRecord.getStatus() == Constant.TraverStatus.ACTION) {
            renderAjaxError("该行程已经出发，无法取消！");
            return;
        }
        if (type == Constant.MEMBER) {
            if (recordLog.getType() == 1) {
                recordLog.setStatus(Constant.TraverStatus.CANCEL);
                traverRecord.setStatus(Constant.TraverStatus.CANCEL);
                RecordLog recordLog1 = RecordLog.dao.findByRecordIdAndLoginId1(recordLog.getRecordId(), getLoginMember().getId());
                if (recordLog1 != null) {
                    recordLog1.delete();
                }
                recordLog.update();
                Order order = Order.dao.findByTraverId(recordLog.getRecordId(), Constant.OrderStatus.SFCACCEPT, getMemberInfo().getId());
                if (order != null) {
                    EventKit.post(new CancelOrder(order));
                    MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
                    OrderLog orderLog = new OrderLog();
                    orderLog.setOperationTime(DateTime.now().toDate());
                    orderLog.setOrderId(order.getId());
                    orderLog.setRemark(memberInfo.getRealName()
                            + "(乘客)取消了订单");
                    orderLog.setAction(Constant.OrderAction.CANCEL);
                    orderLog.setLoginId(memberInfo.getLoginId());
                    order.setStatus(Constant.OrderStatus.CANCEL);
                    order.setLastUpdateTime(DateTime.now().toDate());
                    order.update();
                    orderLog.save();
                }
            } else {
                RecordLog recordLog1 = RecordLog.dao.findRecord(recordLog.getRecordId(), getLoginMember().getId());
                if (recordLog1 != null) {
                    recordLog1.delete();
                }
                Order order = Order.dao.findByTraverId(recordLog.getRecordId(), Constant.OrderStatus.SFCACCEPT, getMemberInfo().getId());
                if (order == null) {
                    renderAjaxError("该行程在执行中,无法取消！");
                    return;
                }
                traverRecord.setPeople(traverRecord.getPeople() - order.getPeople() <= 0 ? 0 : traverRecord.getPeople() - order.getPeople());
                EventKit.post(new CancelOrder(order));
                MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
                OrderLog orderLog = new OrderLog();
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setOrderId(order.getId());
                orderLog.setRemark(memberInfo.getRealName()
                        + "(乘客)取消了订单");
                orderLog.setAction(Constant.OrderAction.CANCEL);
                orderLog.setLoginId(memberInfo.getLoginId());
                order.setStatus(Constant.OrderStatus.CANCEL);
                order.setLastUpdateTime(DateTime.now().toDate());
                order.update();
                orderLog.save();
            }
            traverRecord.update();
            renderAjaxSuccess("你的行程成功取消！");
            return;
        }
        if (type == Constant.DRIVER) {
            if (pd == 3 && TraverRecord.dao.decide(recordLog.getRecordId(), getDriverInfo().getId())) {
                traverRecord.setStatus(Constant.TraverStatus.CANCEL);
                boolean isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        return traverRecord.update() && recordLog.delete();
                    }
                });
                if (isOk) {
                    renderAjaxSuccess("你的行程成功取消！");
                    return;
                } else {
                    renderAjaxError("取消失败！");
                    return;
                }
            } else {
                renderAjaxError("该行程已有乘客，无法取消行程！");
                return;
            }
        }
        /*List<Order> orders = Order.dao.findByTraver(Constant.OrderStatus.ACCEPT,recordLog.getRecordId(),pd);
        for (Order order:orders) {
            EventKit.post(new CancelOrder(order));
            if (getAppType() == Constant.DRIVER) {
                DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
                OrderLog orderLog = new OrderLog();
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setOrderId(order.getId());
                orderLog.setRemark(driverInfo.getRealName() + "(司机)取消了订单");
                orderLog.setAction(Constant.OrderAction.CANCEL);
                orderLog.setLoginId(driverInfo.getLoginId());
                order.setDriver(null);
                order.setStatus(Constant.OrderStatus.CANCEL);
                order.setLastUpdateTime(DateTime.now().toDate());
                order.update();
                orderLog.save();
                MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            } else {
                MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
                OrderLog orderLog = new OrderLog();
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setOrderId(order.getId());
                orderLog.setRemark(memberInfo.getRealName()
                        + "(乘客)取消了订单");
                orderLog.setAction(Constant.OrderAction.CANCEL);
                orderLog.setLoginId(memberInfo.getLoginId());
                order.setStatus(Constant.OrderStatus.CANCEL);
                order.setLastUpdateTime(DateTime.now().toDate());
                order.update();
                orderLog.save();
            }
        }

        traverRecord.update();
        if (recordLog.delete()) {
            renderAjaxSuccess("行程取消成功！");
        }else {
            renderAjaxError("行程取消失败！");
        }*/
    }

    /**
     * 司机接单
     */
    public void getOrder() {
        int orderId = getParaToInt("id", 0);
        if (orderId > 0) {
            final Order order = Order.dao.findById(orderId);
            RecordLog recordLog = RecordLog.dao.findByRecordIdAndLoginId(order.getTraverId(), MemberInfo.dao.findById(order.getMember()).getLoginId());
            if (order.getStatus() == Constant.OrderStatus.CANCEL) {
                renderAjaxError("订单已经取消");
                return;
            }
            if (order.getStatus() == Constant.OrderStatus.SFCACCEPT) {
                renderAjaxError("订单已被抢走接");
                return;
            }
            final MemberLogin memberLogin = getLoginMember();
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            order.setDriver(driverInfo.getId());
            order.setCompany(driverInfo.getCompany());
            order.setStatus(Constant.OrderStatus.SFCACCEPT);
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
            if (!order.getSetOutFlag()) {
                recordLog.setStatus(Constant.TraverStatus.ACCEPT);
                recordLog.update();
            }
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
                TraverRecord traverRecord = TraverRecord.dao.findById(order.getTraverId());
                if (!traverRecord.decide(order.getTraverId(), getDriverInfo().getId())) {
                    traverRecord.setStatus(Constant.TraverStatus.ACCEPT);
                }
                if (order.getPdFlag() != null && order.getPdFlag()) {
                    traverRecord.setPeople(order.getPeople() + traverRecord.getPeople());
                }
                traverRecord.update();
                if (order.getSetOutFlag()) {
                    traverRecord.setDriverId(getDriverInfo().getId());
                    RecordLog recordLog1 = new RecordLog();
                    recordLog1.setLoginId(getLoginMember().getId());
                    if (order.getPdFlag()) {
                        recordLog1.setPinCheAmount(order.getAmount());
                    } else {
                        recordLog1.setAmount(order.getAmount());
                    }
                    recordLog1.setPeople(order.getPeople());
                    recordLog1.setCreateTime(DateTime.now().toDate());
                    recordLog1.setRecordId(order.getTraverId());
                    recordLog1.setStatus(Constant.TraverStatus.ACCEPT);
                    recordLog1.setType(2);
                    recordLog1.save();
                    order.setSetOutFlag(false);
                    order.update();
                    traverRecord.update();
                }
                SmsKit.driverjiedan(order);
                SmsKit.neworder(order);
                renderAjaxSuccess("接单成功！");
            } else {
                renderAjaxError("订单已被乘客取消！");
            }
        }
    }

    public synchronized boolean dealorder(final Order order, final OrderLog orderLog, final MemberLogin memberLogin) {
        Order Tmp = Order.dao.findById(order.getId());
        if (Tmp.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已经被取消");
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
     * 顺风车出发
     */
    public void orderStart() {
        int id = getParaToInt("id", 0);
        int pd = getParaToInt("pd", 0);
        boolean pdflag = true;
        RecordLog recordLog = RecordLog.dao.findById(id);
        TraverRecord traverRecord = TraverRecord.dao.findById(recordLog.getRecordId());
        MemberLogin memberLogin = MemberLogin.dao.findById(recordLog.getLoginId());
        if (id == 0) {
            renderAjaxError("行程已经被取消，请下拉刷新");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.ACTION && pd != 2) {
            renderAjaxError("行程已经出发！");
            return;
        }
        if (traverRecord.getStatus() == Constant.TraverStatus.CANCEL) {
            renderAjaxError("行程已经取消,请下拉刷新");
            return;
        }
        if (memberLogin.getStatus() == Constant.LoginStatus.BUSY && pd != 2 && pd != 3) {
            renderAjaxError("正在行程中，无法开始新的行程！");
            return;
        }

        if (pd == 3 && recordLog.getType() == 1) {
            renderAjaxError("行程没有客户,无法出行！");
            return;
        }

        List<Order> orders = Order.dao.findByTraver(Constant.OrderStatus.SFCACCEPT, recordLog.getRecordId(), pd);
        if (orders.size() == 0) {
            if (pd == 2) {
                renderAjaxError("行程已经出发！");
            } else {
                renderAjaxError("没有可执行订单！");
            }
            return;
        }

        for (Order order : orders) {
            if (order.getStatus() == Constant.OrderStatus.START) {
                PushToCustomer pushToCustomer = new PushToCustomer(order);
                EventKit.post(pushToCustomer);
                renderAjaxError("行程已经开始！");
                return;
            }
            EventKit.post(new PushToCustomerConfig(order));
            DateTime now = DateTime.now();
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            order.setStatus(Constant.OrderStatus.START);//订单开始
            order.setLastUpdateTime(DateTime.now().toDate());
            final OrderLog orderLog = new OrderLog();
            orderLog.setRemark(driverInfo.getRealName() + "已经开始了行程");
            orderLog.setOperationTime(now.toDate());
            orderLog.setOrderId(order.getId());
            orderLog.setOperater(driverInfo.getPhone());
            orderLog.setLoginId(getLoginMember().getId());
            orderLog.setAction(Constant.OrderAction.START);
            order.update();
            orderLog.save();
            PushToCustomer pushToCustomer = new PushToCustomer(order);
            EventKit.post(pushToCustomer);
        }
        if (pd != 2) {
            traverRecord.setStatus(Constant.TraverStatus.ACTION);
        } else {
            traverRecord.setTag("daihuo");
        }
        if (traverRecord.update() && pd != 2) {
            memberLogin.setStatus(Constant.LoginStatus.BUSY);
            if (memberLogin.update()) {
                renderAjaxSuccess("出发成功！");
            } else {
                renderAjaxError("出发失败！");
            }
        } else if (pd == 2) {
            renderAjaxSuccess("带货出发成功！");
        } else {
            renderAjaxError("出发失败！");
        }
    }

    /**
     * 到达终点点击结算
     */
    public void orderArrived() {
        int id = getParaToInt("id", 0);
        int pd = getParaToInt("pd", 0);
        int payType = getParaToInt("payType", 1);
        RecordLog recordLog = RecordLog.dao.findById(id);
        List<RecordLog> recordLogs = RecordLog.dao.findByRecord(recordLog.getRecordId(), getLoginMember().getId());
        if (recordLog == null) {
            renderAjaxError("行程已经结束，请下拉刷新！");
            return;
        }
        TraverRecord traverRecord = TraverRecord.dao.findById(recordLog.getRecordId());
        if (traverRecord.getStatus() == Constant.TraverStatus.END && pd != 2) {
            renderAjaxSuccess("行程已经结束！");
            return;
        }
        if ((traverRecord.getStatus() == Constant.TraverStatus.ACCEPT & pd != 2) || (traverRecord.getStatus() == Constant.TraverStatus.CREATE & traverRecord.getTag() == null)) {
            renderAjaxSuccess("行程没有开始，无法结算！");
            return;
        }
        if ((traverRecord.getTag() == null && pd == 2)) {
            renderAjaxError("行程已经结束！");
            return;
        }
        List<Order> orders = Order.dao.findByTraver(Constant.OrderStatus.START, recordLog.getRecordId(), pd);
        if (orders.size() == 0 || recordLogs.size() == 0) {
            if (pd == 2) {
                renderAjaxError("行程已经结束！请下拉刷新");
            } else {
                renderAjaxError("没有可执行订单！");
            }
            return;
        }
        for (RecordLog recordLog1 : recordLogs) {
            recordLog1.setStatus(Constant.TraverStatus.END);
            recordLog1.update();
        }
        for (Order order : orders) {
            DateTime now = DateTime.now();
            if (order.getStatus() == Constant.OrderStatus.END || order.getStatus() == Constant.OrderStatus.PAYED) {
                EventKit.post(new PushToPay(order));
                traverRecord.setCompleted(1);
                renderAjaxSuccess("结算成功!");
                MemberOrderCache.getInstance().del(order.getNo());
                MemberOrderCache.getInstance().del(MemberInfo.dao.findById(order.getMember()));
                return;
            }
            if ((order.getFromType() == Constant.FromType.WECHATTYPE
                    || order.getFromType() == Constant.FromType.BUDAN
                    || order.getFromType() == Constant.FromType.TELTYPE) && payType != Constant.PayType.Collection) {
                renderAjaxError("非APP订单，只能代付");
                return;
            }
            if (order.getStatus() == Constant.OrderStatus.PAYED) {
                renderAjaxError("订单已经结算过了！");
                return;
            }
            order.setRealDistance(order.getDistance());
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            OrderLog orderLog = OrderLog.dao.findByOrderAndActionAndLoginId(order.getId(), Constant.OrderStatus.START, getLoginMember().getId());
            order.setStatus(Constant.OrderStatus.END);
            final OrderLog arrivedLog = new OrderLog();//到达终点
            arrivedLog.setAction(Constant.OrderAction.END);
            arrivedLog.setOrderId(order.getId());
            arrivedLog.setOperationTime(now.toDate());
            arrivedLog.setRemark("司机已到达终点");
            arrivedLog.setLoginId(driverInfo.getLoginId());
            arrivedLog.setOperater(driverInfo.getPhone());
            order.setLastUpdateTime(now.toDate());
            if (order.getServiceType() == Constant.ServiceType.ZhuanXian || order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
                order.setAmount(order.getYgAmount());
                order.setRealPay(order.getYgAmount());
            } else {
                if (orders.size() == 1 && traverRecord.decide(traverRecord.getId(), order.getDriver()) && pd != 2 && traverRecord.getRecordType() == 1) {
                    BigDecimal amount = CalService.getInstance().noSuccess(order.getDistance(), order.getPeople(), order.getCompany(), MemberInfo.dao.findById(order.getMember()));
                    order.setRealPay(amount);
                    order.setAmount(amount);
                } else {
                    order.setRealPay(order.getAmount());
                }
            }
            final OrderLog payLog = new OrderLog();
            if (Constant.PayType.Collection == payType) {//代付直接计算总共支付多少钱
                order.setPayChannel(payType);
                MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(getLoginMember().getId());
                if (order.getAmount().compareTo(memberCapitalAccount.getAmount()) > 0) {
                    renderAjaxError("代付失败余额不足，请选择其他支付方式");
                    return;
                }
                int tmpMinutes = 0;
                OrderLog createLog = OrderLog.dao.findByOrderAndPayAction(order.getId(), Constant.OrderAction.CREATE);
                if (createLog != null) {
                    tmpMinutes = Minutes.minutesBetween(new DateTime(createLog.getOperationTime()), new DateTime(now)).getMinutes();
                }
                order.setConsumeTime(BigDecimal.valueOf(tmpMinutes));
                order.setStatus(Constant.OrderStatus.PAYED);
                order.setPayStatus(Constant.PayStatus.PAYED);
                order.setPayTime(DateTime.now().toDate());
                order.put("orderType", 2);
                payLog.setAction(Constant.OrderAction.PAYED);
                payLog.setOrderId(order.getId());
                payLog.setOperationTime(now.toDate());
                payLog.setRemark("司机司机已经代收");
                payLog.setLoginId(driverInfo.getLoginId());
                payLog.setOperater(driverInfo.getPhone());
                CalCommissionService.getInstance().daishoucommission(order);//计算提成
                if (order.getPdFlag()) {
                    List<Order> orders1 = Order.dao.findByDriverNoComplete(driverInfo.getId());
                    if (orders1 == null || orders1.size() == 0) {
                        getLoginMember().setStatus(Constant.LoginStatus.RECIVEDORDER);
                    } else {
                        getLoginMember().setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                    }

                } else {
                    getLoginMember().setStatus(Constant.LoginStatus.RECIVEDORDER);
                }
                EventKit.post(new Reward(order));//计算分销奖励
                order.setCoupon(0);
                EventKit.post(new OrderActivity(order));//参加活动
            } else {
                order.setPayChannel(-1);//未设置支付类型
                order.setPayStatus(Constant.PayStatus.NOPAY);
            }
            getLoginMember().setLastUpdateTime(DateTime.now().toDate());
            order.update();
            arrivedLog.save();
            if (payLog.getAction() != null) {
                payLog.save();
                getLoginMember().update();
            }
            EventKit.post(new PushToPay(order));
            if (payType == Constant.PayType.Collection) {
                SmsKit.ordercomplete(order);
            }
            traverRecord.setCompleted(1);
            renderAjaxSuccess("结算成功！");
            MemberOrderCache.getInstance().del(order.getNo());
            MemberOrderCache.getInstance().del(MemberInfo.dao.findById(order.getMember()));
        }
        if (pd != 2) {
            traverRecord.setStatus(Constant.TraverStatus.END);
        } else {
            traverRecord.setTag("");
        }
        traverRecord.update();
        recordLog.setStatus(Constant.TraverStatus.END);
        recordLog.setType(3);
        recordLog.update();
        traverRecord.update();
    }

    /**
     * 订单创建
     */
    /**
     * @api {post} /api/order/create 订单创建（下单接口）
     * @apiGroup D_OrderController
     * @apiVersion 2.0.0
     * @apiHeader {String} Content-Type application/json.
     * @apiHeader {String} appType 请求的类型 1:表示司机端;2:表示普通会员
     * @apiHeader {String} deviceno 设备序列号
     * @apiHeader {String} devicetype 设备序类型   1:android;2:ios
     * @apiParam {int} orderType  小分类，舒适型和经济型
     * @apiParam {int} type  大类型
     * @apiParam {String}  distance
     * @apiParam {String}  startLongitude
     * @apiParam {String}  startLatitude
     * @apiParam {String}  endLongitude
     * @apiParam {String}  endLatitude
     * @apiParam {String}  reservationAddress
     * @apiParam {String}  destination
     * @apiParam {String}  cityCode
     * @apiParam {int} zxLineId
     * @apiParam {String}  mandatoryPhone  委托人手机号
     * @apiParam {String}  remark 下单方式  只针对H5
     * @apiParam {String}  tag 下单方式  只针对H5
     * @apiParam {int} byMeter  出租车是否打表来接 1.是打表来接   0.不是
     * @apiParam {double} addAmount  出租车调度费用
     * @apiParam {boolean} pdFlag
     * @apiParam {String} amount
     * @apiParam {int}  people
     * @apiParam {String}   orderIp
     * @apiParam {String}  orderIMSI
     * @apiParam {String}  orderIMEI
     * @apiParam {String}   macAddres
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     */
    @Before(POST.class)
    public void create() {
        MemberLogin memberLogin = getLoginMember();
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
        if (memberInfo.getStatus() == Constant.MemberInfoStatus.BLACK) {
            renderAjaxError("用户存在异常,无法下单!");
            return;
        }
        List<Order> oLists = Order.dao.findByMemberForExecute(memberInfo.getId(), null);
        if (oLists.size() > 0) {
            for (Order order : oLists) {
                if (order.getSetOutFlag() != true) {
                    order.setStatus(6);
                    order.update();
                    logger.info("消除订单 ：" + order.getId());
                    renderAjaxError("消除之前订单:" + order.getId() + ",请重新创建。");
                    return;
                }
            }
        }
        List<Order> orderLists = Order.dao.findByMemberForNoPay(memberInfo.getId());
        if (orderLists != null && orderLists.size() > 0) {
            List<JSONObject> realOrders = Lists.newArrayList();
            for (Order order1 : orderLists) {
                DriverInfo driverInfo = DriverInfo.dao.findById(order1.getDriver());
                driverInfo = DriverInfo.dao.findById(driverInfo.getId());
                DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
                Car car = Car.dao.findByDriver(driverInfo.getId());
                JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
                jPushToMemberDto.setDriverId(driverInfo.getId());
                jPushToMemberDto.setLatitude(driverRealLocation.getLatitude() + "");
                jPushToMemberDto.setLongitude(driverRealLocation.getLongitude() + "");
                jPushToMemberDto.setNickName(driverInfo.getRealName());
                jPushToMemberDto.setPhone(driverInfo.getPhone());
                jPushToMemberDto.setCar(car);
                jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
                String odcontent = JSONObject.toJSONString(jPushToMemberDto);
                JSONObject od = JSONObject.parseObject(odcontent);
                OrderTrip orderTrip = OrderTrip.dao.findById(order1.getId());
                String tt = JSONObject.toJSONString(orderTrip);
                JSONObject trip = JSONObject.parseObject(tt);
                od.put("trip", trip);
                od.put("reservationAddress", order1.getReservationAddress());
                od.put("destination", order1.getDestination());
                od.put("amount", order1.getAmount());
                od.put("status", order1.getStatus());
                od.put("orderId", order1.getId());
                od.put("realPay", order1.getRealPay() == null ? BigDecimal.ZERO : order1.getRealPay());
                od.put("payChannel", order1.getPayChannel());
                realOrders.add(od);
            }
            renderAjaxFailure("有未完成的订单", realOrders);
            return;
        }
        // TODO: 2016/10/28 未完成的订单要提示
        final int orderType = getParaToInt("orderType", 0);//小分类，舒适型和经济型
        final int type = getParaToInt("type", 0);//大类型
        final String distance = getPara("distance");
        String startLongitude = getPara("startLongitude");
        String startLatitude = getPara("startLatitude");
        String endLongitude = getPara("endLongitude");
        String endLatitude = getPara("endLatitude");
        String reservationAddress = getPara("reservationAddress");
        String destination = getPara("destination");
        String cityCode = getPara("cityCode");
        int zxLineId = getParaToInt("zxLineId", 0);
        String mandatoryPhone = AESOperator.getInstance().decrypt(getPara("mandatoryPhone"));  // 委托人手机号
        String remark = getPara("remark");//下单方式  只针对H5
        String tag = getPara("tag");//下单方式  只针对H5
       /* if (!Strings.isNullOrEmpty(mandatoryPhone)) {
            memberInfo.setPhone(mandatoryPhone);
        }*/


        //出租车是否打表来接
        int byMeter = getParaToInt("byMeter", 0);
        String addAmount = getPara("addAmount", "");

        final Company company = Company.dao.findByCity(cityCode);
        //创建代驾订单时判断公司保险费是否正常
       /* if (orderType == Constant.ServiceItemType.DaiJia) {
            CompanyAccount account = CompanyAccount.dao.findById(company.getId());
            AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(company.getId());
            if (!(adminSetting.getInsuranceOverdraftAmount().compareTo(account.getInsuranceAmount()) < 1)
            || (adminSetting.getInsuranceOverdraftAmount().compareTo(account.getInsuranceAmount()) == 0)) {
                renderAjaxError("公司透支保险费上限！！请联系管理人员");
                return;
            }
        }*/

        boolean pdFlag = false;

        if (type == Constant.ServiceType.KuaiChe || type == Constant.ServiceType.ZhuanXian) {
            pdFlag = getParaToBoolean("pdFlag", false);
        }
        if (Strings.isNullOrEmpty(startLongitude)
                || Strings.isNullOrEmpty(startLatitude)
                || Strings.isNullOrEmpty(distance)
                || orderType == 0
                || Strings.isNullOrEmpty(endLongitude)
                || Strings.isNullOrEmpty(endLatitude)
                || Strings.isNullOrEmpty(reservationAddress)
                || Strings.isNullOrEmpty(destination)
                ) {
            renderAjaxError("参数不能为空！");
            return;
        }
        BigDecimal amount = new BigDecimal(getPara("amount") == null ? "0" : getPara("amount"));
        if (company == null) {
            renderAjaxFailure("该地区无法使用该服务！");
            return;
        }
        Long strSetOutTime = getParaToLong("setOutTime", System.currentTimeMillis());
        String setOutTime = null;
        if (t(strSetOutTime)) {
            setOutTime = DateUtil.stampToDate(strSetOutTime);
        }
        Date time;
        final Order order = new Order();
        int people = getParaToInt("people", 1);
        if (pdFlag || type == Constant.ServiceType.ZhuanChe || type == Constant.ServiceType.Taxi || type == Constant.ServiceType.HangKongZhuanXian) {
            if (pdFlag && type != Constant.ServiceType.ZhuanXian) {
                if (people > 3) {
                    renderAjaxError("拼车人数最多不超过3人！");
                    return;
                }
            }
        }
        order.setPeople(people);
        if (Strings.isNullOrEmpty(setOutTime)) {
            time = DateTime.now().toDate();
            order.setSetOutFlag(false);
        } else {
            time = DateTime.parse(setOutTime, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)).toDate();
            order.setSetOutFlag(true);
        }

        String no = StringsKit.getOrderNo();
        order.setNo(no);
        order.setFromType(Constant.FromType.APPTYPE);
        order.setYgAmount(amount);//预估的价格
        order.setPdFlag(pdFlag);//是否拼单
        order.setFromCompany(company.getId());//设置订单的公司
        order.setCompany(company.getId());//设置订单的公司
        order.setCreateTime(DateTime.now().toDate());
        order.setDistance(new BigDecimal(distance));
        order.setMember(memberInfo.getId());

        if (!Strings.isNullOrEmpty(mandatoryPhone)) {
            order.setPhone(mandatoryPhone);
        } else {
            order.setPhone(memberInfo.getPhone());
        }
        order.setType(orderType);
        order.setServiceType(type);
        order.setSetouttime(time);
        //出租车是否打表来接
        order.setByMeter(byMeter);
        if (!Strings.isNullOrEmpty(addAmount)) {
            //出租车设置调度费
            BigDecimal taxiAmount = new BigDecimal(addAmount);
            if (taxiAmount.compareTo(BigDecimal.ZERO) > 0) {
                order.setAddFlag(true);
                order.setAddAmount(taxiAmount);
            }
        }

        //添加vip标识
        if (type != Constant.ServiceType.DaiJia && orderType != Constant.ServiceItemType.DaiJia) {
            VipInfo vipInfo = VipInfo.dao.findByLoginId(memberLogin.getId());
            AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
            if (vipInfo != null) {
                if (vipInfo.getAmount().compareTo(adminSetting.getVipSingleSpendingAmount()) >= 1) {
                    order.setVipActivityFlag(Constant.VipActivityFlag.ACTIVITY_KQC);
                }
            }
        }


        //order_ip order_IMSI order_IMEI macAddress port
        order.setOrderIp(getPara("orderIp"));
        order.setOrderImsi(getPara("orderIMSI"));
        order.setOrderImei(getPara("orderIMEI"));
        order.setMacAddress(getPara("macAddress"));
        order.setPort(String.valueOf(getRequest().getRemotePort()));

        if (remark == null) {
            order.setFromType(Constant.FromType.APPTYPE);
            order.setRemark("通过APP下单");
        } else if (remark.equals("H5")) {
            order.setFromType(Constant.FromType.WECHATH5TYPE);
            order.setRemark("通过微信H5下单");
        }
        if (type == Constant.ServiceType.HangKongZhuanXian || type == Constant.ServiceType.ZhuanXian) {
            order.setZxLine(zxLineId);
        }
        order.setStatus(Constant.OrderStatus.CREATE);
        order.setPayStatus(Constant.PayStatus.NOPAY);
        order.setRealDistance(BigDecimal.ZERO);
        order.setDestination(destination);
        order.setReservationAddress(reservationAddress);
        final OrderLog orderLog = new OrderLog();
        orderLog.setAction(Constant.OrderStatus.CREATE);
        orderLog.setLoginId(memberLogin.getId());
        orderLog.setOperater(memberLogin.getUserName());
        orderLog.setRemark(memberInfo.getNickName() + "创建了一个订单");
        orderLog.setOperationTime(DateTime.now().toDate());
        final OrderTrip orderTrip = new OrderTrip();
        orderTrip.setStartLatitude(Doubles.tryParse(startLatitude));
        orderTrip.setStartLongitude(Doubles.tryParse(startLongitude));
        orderTrip.setEndLatitude(Doubles.tryParse(endLatitude));
        orderTrip.setEndLongitude(Doubles.tryParse(endLongitude));
        orderTrip.setMember(memberInfo.getId());
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                boolean schedule1 = false;
                //判断是否有未完成订单
                MemberLogin memberLogin = getLoginMember();
                MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
                List<Order> oLists = Order.dao.findByMemberForExecute(memberInfo.getId(), null);
                if (oLists.size() > 0) {
                    return false;
                }
                if (type == Constant.ServiceType.ZhuanXian) {
                    AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(company.getId());
                    if (adminSetting.getZxCjDispatch() == Constant.DispatchOrder.hand) {
                        //城际专线订单后台派单
                        order.setAutoDispatchOrder(Constant.DispatchOrder.hand);
                    } else {
                        order.setAutoDispatchOrder(Constant.DispatchOrder.auto);
                    }
                }
                if (type == Constant.ServiceType.HangKongZhuanXian) {
                    AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(company.getId());
                    if (adminSetting.getZxHkDispatch() == Constant.DispatchOrder.hand) {
                        //航空专线订单后台派单
                        order.setAutoDispatchOrder(Constant.DispatchOrder.hand);
                    } else {
                        order.setAutoDispatchOrder(Constant.DispatchOrder.auto);
                    }
                }
                if (order.save()) {
                    orderTrip.setOrderId(order.getId());
                    orderLog.setOrderId(order.getId());
                    if (order.getAutoDispatchOrder() != null && order.getAutoDispatchOrder() != Constant.DispatchOrder.auto) {
                        final Schedule schedule = new Schedule();
                        schedule.setOrderId(order.getId());
                        schedule.setStatus(Constant.ScheduleStatus.wait);
                        schedule.setCompany(order.getCompany());
                        schedule1 = !schedule.save();
                    }
                    return orderTrip.save() && orderLog.save() && !schedule1;
                } else {
                    return false;
                }
            }
        });
        if (isOk) {
            if (order.getAutoDispatchOrder() == null || order.getAutoDispatchOrder() == Constant.DispatchOrder.auto) {
                EventKit.post(new PushOrder(order));
            }
            renderAjaxSuccess("下单成功", order.getId().toString());
        } else {
            renderAjaxError("下单失败！");
        }
    }

    /**
     * 支付订单获取订单信息
     */
    public void pay() {
        int orderId = getParaToInt("orderId", 0);
        final int type = getParaToInt("payType", 1);
        int typeWeb = getParaToInt("typeWeb", 0);
        final int coupon = getParaToInt("couponId", 0);
        final Order order = Order.dao.findById(orderId);
        BigDecimal percent;
        BigDecimal percentAmount;
        if (order == null) {
            renderAjaxSuccess("订单不存在！");
            return;
        }

        //越权问题
        int loginId = getLoginMember().getId();
        MemberInfo menber = MemberInfo.dao.findById(order.getMember());
        if (menber.getLoginId() != loginId) {
            renderAjaxError("当前用户身份异常！");
            return;
        }

        MemberCoupon memberCoupon = null;
        if (order.getCoupon() == null) {
            memberCoupon = MemberCoupon.dao.findById(coupon);
            if (coupon != 0 && memberCoupon == null) {
                renderAjaxError("优惠券不存在");
                return;
            } else if (memberCoupon != null) {
                if (memberCoupon.getStatus() == Constant.CouponStatus.DISABLE || memberCoupon.getStatus() == Constant.CouponStatus.USED) {
                    renderAjaxError("优惠券不可用");
                    return;
                }
            }
            if (memberCoupon != null) {
                Coupon coupon1 = Coupon.dao.findById(memberCoupon.getCouponId());
                CompanyAccount companyAccount = CompanyAccount.dao.findById(order.getCompany());
                if (coupon1.getCouponType() == Constant.CouponType.Normal) {
                    if (coupon1.getServiceType() == Constant.ServiceType.AllService) {
                        if (coupon1.getBaseAmount().compareTo(order.getRealPay()) <= 0) {
                            if (order.getRealPay().compareTo(coupon1.getAmount()) < 0) {
                                order.setRealPay(BigDecimal.ZERO);
                            } else {
                                order.setRealPay(order.getRealPay().subtract(coupon1.getAmount()));
                            }
                            order.setYhAmount(coupon1.getAmount());
                            memberCoupon.setAmount(coupon1.getAmount());
                        } else {
                            renderAjaxFailure("该优惠券需满" + coupon1.getBaseAmount() + "可用！");
                        }
                    } else {
                        if (coupon1.getServiceType() == order.getServiceType()) {
                            if (coupon1.getBaseAmount().compareTo(order.getRealPay()) <= 0) {
                                if (order.getRealPay().compareTo(coupon1.getAmount()) < 0) {
                                    order.setRealPay(BigDecimal.ZERO);
                                } else {
                                    order.setRealPay(order.getRealPay().subtract(coupon1.getAmount()));
                                }
                                order.setYhAmount(coupon1.getAmount());
                                memberCoupon.setAmount(coupon1.getAmount());
                            } else {
                                renderAjaxFailure("该优惠券需满" + coupon1.getBaseAmount() + "可用！");
                            }
                        } else {
                            renderAjaxFailure("该优惠券不可用于此订单!");
                            return;
                        }
                    }
                    memberCoupon.setType(Constant.CouponType.Normal);
                    memberCoupon.update();
                }
                if (coupon1.getCouponType() == Constant.CouponType.Discount) {
                    if (coupon1.getServiceType() == Constant.ServiceType.AllService) {
                        if (coupon1.getBaseAmount().compareTo(order.getRealPay()) <= 0) {
                            if (null == coupon1.getPercent()) {
                                renderAjaxError("该优惠券未设置折扣，无法使用！请联系当地运营商！");
                                return;
                            } else {
                                percent = coupon1.getPercent();
                            }
                            percentAmount = order.getRealPay().subtract(order.getRealPay().multiply(percent.divide(BigDecimal.valueOf(10))));
                            if (null != coupon1.getPercenAmount()) {
                                if (percentAmount.compareTo(coupon1.getPercenAmount()) >= 0) {
                                    percentAmount = coupon1.getPercenAmount();
                                }
                            }
                            if (!CompanyAccount.dao.amountEnough(companyAccount, percentAmount, Constant.CompanyAccountActivity.createcoupon)) {
                                renderAjaxFailure("公司活动金额不足，优惠券不可用！请联系当地运营商！");
                                return;
                            }
                            if (order.getRealPay().compareTo(percentAmount) < 0) {
                                order.setRealPay(BigDecimal.ZERO);
                            } else {
                                order.setRealPay(order.getRealPay().subtract(percentAmount));
                            }
                            memberCoupon.setAmount(percentAmount);
                            order.setYhAmount(percentAmount);
                        } else {
                            renderAjaxFailure("该优惠券需满" + coupon1.getBaseAmount() + "可用！");
                            return;
                        }
                    } else {
                        if (coupon1.getServiceType() == order.getServiceType()) {
                            if (coupon1.getBaseAmount().compareTo(order.getRealPay()) <= 0) {
                                if (null == coupon1.getPercent()) {
                                    renderAjaxError("该优惠券未设置折扣，无法使用！请联系当地运营商！");
                                    return;
                                } else {
                                    percent = coupon1.getPercent();
                                }
                                percentAmount = order.getRealPay().subtract(order.getRealPay().multiply(percent.divide(BigDecimal.valueOf(10))));
                                if (null != coupon1.getPercenAmount()) {
                                    if (percentAmount.compareTo(coupon1.getPercenAmount()) >= 0) {
                                        percentAmount = coupon1.getPercenAmount();
                                    }
                                }
                                if (!CompanyAccount.dao.amountEnough(companyAccount, percentAmount, Constant.CompanyAccountActivity.createcoupon)) {
                                    renderAjaxFailure("公司活动金额不足，优惠券不可用！请联系当地运营商！");
                                    return;
                                }
                                if (order.getRealPay().compareTo(percentAmount) < 0) {
                                    order.setRealPay(BigDecimal.ZERO);
                                } else {
                                    order.setRealPay(order.getRealPay().subtract(percentAmount));
                                }
                                order.setYhAmount(percentAmount);
                                memberCoupon.setAmount(percentAmount);
                            } else {
                                renderAjaxFailure("该优惠券需满" + coupon1.getBaseAmount() + "可用！");
                            }
                        } else {
                            renderAjaxFailure("该优惠券不可用于此订单!");
                            return;
                        }
                    }
                    memberCoupon.setType(Constant.CouponType.Discount);
                    memberCoupon.update();
                }
                order.setCoupon(coupon);
            }
        } else {
            memberCoupon = MemberCoupon.dao.findById(order.getCoupon());
        }


        //vip用户处理
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        final VipInfo vipInfo = VipInfo.dao.findByLoginId(memberInfo.getLoginId());
        final VipLog vipLog = new VipLog();
        if (order.getServiceType() != Constant.ServiceType.DaiJia && order.getType() != Constant.ServiceItemType.DaiJia) {
            if (order.getVipActivityFlag() == Constant.VipActivityFlag.ACTIVITY_KQC) {
                AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
                if (vipInfo != null) {
                    order.setRealPay(order.getRealPay().subtract(adminSetting.getVipSingleSpendingAmount()));
                    vipInfo.setAmount(vipInfo.getAmount().subtract(adminSetting.getVipSingleSpendingAmount()));
                    vipLog.setLoginId(memberInfo.getLoginId());
                    vipLog.setVipId(vipInfo.getId());
                    vipLog.setUseAmount(adminSetting.getVipSingleSpendingAmount());
                    vipLog.setLogContent("VIP消费金额" + adminSetting.getVipSingleSpendingAmount() + ",订单号：" + order.getNo());
                }
            }
        }

        if (order.getStatus() == Constant.OrderStatus.PAYED) {
            renderAjaxSuccess("订单已支付！无需重复支付！");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxSuccess("订单已取消！无需支付！");
            return;
        }
        order.setPayChannel(type);
        order.update();
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        if (order.getPayChannel() == Constant.PayType.Alipay) {
            BizContentDto bizContentDto = new BizContentDto();
            bizContentDto.setOut_trade_no(order.getNo());
            Map<String, Object> result = Maps.newHashMap();
            bizContentDto.setProduct_code("QUICK_MSECURITY_PAY");
            result.put("order", order);
            result.put("orderType", Constant.AliPayOrderType.NomorlOrder);
            bizContentDto.setBody(JSON.toJSONString(result));
            bizContentDto.setSubject("支付订单" + order.getNo() + "费用");
            bizContentDto.setTotal_amount(order.getRealPay().toString());
            String content = JSON.toJSONString(bizContentDto);
            Map<String, String> params = null;
            if (typeWeb == 1) {
                params = OrderInfoKit.buildOrderParamMapForWeb(content);
            } else {
                params = OrderInfoKit.buildOrderParamMap(content);
            }
            String orderParam = OrderInfoKit.buildOrderParam(params);
            String sign = OrderInfoKit.getSign(params);
            final String orderInfo = orderParam + "&" + sign;
            if (order.getCoupon() != null && order.getCoupon() != 0) {
                memberCoupon.setStatus(Constant.CouponStatus.DISABLE);
                memberCoupon.update();
            } else {
                order.setCoupon(0);
            }
            renderAjaxSuccess(orderInfo);
        } else if (order.getPayChannel() == Constant.PayType.WECHAT) {
            //APP微信支付
            Map<String, String> params = WechatPayKit.getWechatPayForApp(order, 2);
            if (order.getCoupon() != null && order.getCoupon() != 0) {
                memberCoupon.setStatus(Constant.CouponStatus.DISABLE);
                memberCoupon.update();
            } else {
                order.setCoupon(0);
            }
            renderAjaxSuccess(params);
        } else if (order.getPayChannel() == Constant.PayType.WECHATTHEPUBLIC) {
            //微信公众号支付
            Map<String, String> params = WechatPayKit.getWechatPayForPublicAccounts(order, getPara("code"));
            if (order.getCoupon() != null && order.getCoupon() != 0) {
                memberCoupon.setStatus(Constant.CouponStatus.DISABLE);
                memberCoupon.update();
            } else {
                order.setCoupon(0);
            }
            renderAjaxSuccess(params);
        } else if (order.getPayChannel() == Constant.PayType.LITEAPP) {
            //微信小程序支付
            Map<String, String> params = WechatPayKit.getWechatPayForLiteApp(order, getPara("code"));
            if (order.getCoupon() != null && order.getCoupon() != 0) {
                memberCoupon.setStatus(Constant.CouponStatus.DISABLE);
                memberCoupon.update();
            } else {
                order.setCoupon(0);
            }
            renderAjaxSuccess(params);
        } else if (order.getPayChannel() == Constant.PayType.YE) {
            //代付直接是乘客给司机现金，然后司机余额扣除
            Date now = DateTime.now().toDate();
            final OrderLog orderLog = new OrderLog();
            orderLog.setAction(Constant.OrderAction.PAYED);
            orderLog.setOperationTime(now);
            orderLog.setLoginId(memberInfo.getLoginId());
            orderLog.setRemark("乘客支付了订单");
            orderLog.setOrderId(order.getId());
            int minutes = 0;
            OrderLog createLog = OrderLog.dao.findByOrderAndPayAction(orderId, Constant.OrderAction.CREATE);
            if (createLog != null) {
                minutes = Minutes.minutesBetween(new DateTime(createLog.getOperationTime()), new DateTime(now)).getMinutes();
            }
            order.setConsumeTime(BigDecimal.valueOf(minutes));
            order.setPayStatus(Constant.PayStatus.PAYED);
            order.setStatus(Constant.OrderStatus.PAYED);
            order.setPayChannel(Constant.PayType.YE);
            order.setPayTime(now);
            final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(memberInfo.getLoginId());
            BigDecimal historyAmount = memberCapitalAccount.getAmount();
            if (order.getRealPay().compareTo(historyAmount) > 0) {
                renderAjaxError("账户余额不足");
                return;
            }
            CalCommissionService.getInstance().commission(order);
            memberCapitalAccount.setAmount(historyAmount.subtract(order.getRealPay()));
            memberCapitalAccount.setLastUpdateAmount(order.getRealPay().multiply(new BigDecimal("-1")));
            memberCapitalAccount.setLastUpdateTime(DateTime.now().toDate());
            MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, order.getRealPay().multiply(new BigDecimal("-1")));
            final CapitalLog capitalLog = new CapitalLog();
            capitalLog.setLoginId(getLoginMember().getId());
            capitalLog.setOrderId(0);
            capitalLog.setCreateTime(now);
            capitalLog.setAmount(order.getRealPay().multiply(new BigDecimal("-1")));
            capitalLog.setOperater(0);
            capitalLog.setRemark("自己消费了" + order.getRealPay().toString() + "元,订单号:【" + order.getNo() + "】");
            capitalLog.setStatus(Constant.CapitalStatus.OK);
            String no = "";
            if (getAppType() == Constant.DRIVER) {
                no = StringsKit.getCaptDriver();
            } else if (getAppType() == Constant.MEMBER) {
                no = StringsKit.getCaptMember();
            }
            capitalLog.setNo(no);
            switch (order.getServiceType()) {
                case Constant.ServiceType.DaiJia:
                    capitalLog.setOperationType(Constant.CapitalOperationType.DJDAISHOU);
                    break;
                case Constant.ServiceType.KuaiChe:
                    capitalLog.setOperationType(Constant.CapitalOperationType.KCDAISHOU);
                    break;
                case Constant.ServiceType.Taxi:
                    capitalLog.setOperationType(Constant.CapitalOperationType.CZDAISHOU);
                    break;
                case Constant.ServiceType.ZhuanChe:
                    capitalLog.setOperationType(Constant.CapitalOperationType.ZCDAISHOU);
                    break;
                case Constant.ServiceType.ShunFengChe:
                    capitalLog.setOperationType(Constant.CapitalOperationType.SHUNFENGCHE);
                    break;
                case Constant.ServiceType.ZhuanXian:
                    capitalLog.setOperationType(Constant.CapitalOperationType.ZHUANXIAN);
                    break;
                case Constant.ServiceType.HangKongZhuanXian:
                    capitalLog.setOperationType(Constant.CapitalOperationType.HANGKONGZHUANXIAN);
                    break;
                default:
                    break;
            }
            final MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            if (order.getPdFlag() != null && order.getPdFlag()) {
                List<Order> orders = Order.dao.findByDriverNoComplete(driverInfo.getId());
                if (orders == null || orders.size() == 0) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                }
            } else {
                memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
            }
            memberLogin.setLastUpdateTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (type == Constant.PayType.YE) {
                        SmsKit.ordercomplete(order);//发送短信
                        if (order.getCoupon() != null && order.getCoupon() != 0) {
                            MemberCoupon memberCoupon = MemberCoupon.dao.findById(order.getCoupon());
                            memberCoupon.setStatus(Constant.CouponStatus.USED);
                            memberCoupon.setUseTime(DateTime.now().toDate());
                            CompanyActivity companyActivity = CompanyActivity.dao.findByCouponId(memberCoupon.getId());
                            companyActivity.setStatus(Constant.DataAuditStatus.AUDITOK);
                            companyActivity.setAmount(order.getYhAmount());
                            return capitalLog.save() && memberCapitalAccount.update() && order.update() && orderLog.save() && memberLogin.update() && memberCoupon.update() && companyActivity.update();
                        } else {
                            order.setCoupon(0);
                            return capitalLog.save() && memberCapitalAccount.update() && order.update() && orderLog.save() && memberLogin.update();
                        }
                    } else {
                        return capitalLog.save() && memberCapitalAccount.update() && order.update() && orderLog.save() && memberLogin.update();
                    }
                }
            });

            if (isOk) {
                EventKit.post(new Reward(order));//计算分销奖励
                EventKit.post(new OrderActivity(order));//参加活动
                //vip订单处理
                if (vipInfo != null) {
                    if (order.getServiceType() != Constant.ServiceType.DaiJia && order.getType() != Constant.ServiceItemType.DaiJia) {
                        boolean vipIsOk = Db.tx(new IAtom() {
                            @Override
                            public boolean run() throws SQLException {
                                return vipInfo.update() && vipLog.save();
                            }
                        });
                    }
                }
                //给司机和乘客添加订单统计
                OrderService.countCompleteOrder(order);
                renderAjaxSuccess("支付成功了！");
                MemberOrderCache.getInstance().del(order.getNo());
                MemberOrderCache.getInstance().del(MemberInfo.dao.findById(order.getMember()));
            } else {
                renderAjaxError("支付失败了！");
            }
        }
    }

    //计算总价格
    public void totalAmount() {
        Order order = Order.dao.findById(getPara("id"));
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        DateTime setouttime = new DateTime(order.getSetouttime());
        if (order.getSetOutFlag()) {
            if (setouttime.isAfterNow()) {
                setouttime = DateTime.now();
            }
        }
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        BigDecimal distance;
        //BigDecimal distance1 = new BigDecimal(getPara("distance", "0"));
        if (order.getServiceType() == Constant.ServiceType.DaiJia || (order.getServiceType() == Constant.ServiceType.KuaiChe && !order.getPdFlag())) {
            //if (Strings.isNullOrEmpty(getPara("distance")) || getPara("distance").equals("0")) {
            distance = order.getRealDistance();
            //} else {
            //distance = new BigDecimal(getPara("distance"));
            //}
        } else {
            distance = order.getDistance();
        }
        CalculationDto calculationDto = new CalculationDto();
        if (Constant.ServiceType.ZhuanXian == order.getServiceType() || Constant.ServiceType.HangKongZhuanXian == order.getServiceType()) {
            calculationDto.setTotalAmount(order.getYgAmount());
            calculationDto.setDistanceAmount(BigDecimal.ZERO);
            calculationDto.setTimeAmount(BigDecimal.ZERO);
            calculationDto.setBaseAmount(BigDecimal.ZERO);
            calculationDto.setWaitAmount(BigDecimal.ZERO);
            calculationDto.setAddAmount(BigDecimal.ZERO);
            calculationDto.setDiscount(BigDecimal.ZERO);
        } else {
            BigDecimal waitMinutes = new BigDecimal(Strings.isNullOrEmpty(getPara("waitMinutes")) ? "0" : getPara("waitMinutes"));
            int minutes = Minutes.minutesBetween(new DateTime(order.getLastUpdateTime()), DateTime.now()).getMinutes();
            BigDecimal times = new BigDecimal(minutes);
            Company company = Company.dao.findByCompanyId(order.getFromCompany());
            calculationDto = CalService.getInstance().calculationDtoByServiceItem(order.getType(), order.getServiceType(), company, distance, times, setouttime, memberInfo, order.getPdFlag());// TODO 收费标准按下单时间计算
            //calculationDto.setWaitAmount(CalService.getInstance().calculationWaitSetUp1(order.getServiceType(), order.getFromCompany(), memberInfo, order.getSetouttime(), waitMinutes));
            calculationDto.setWaitAmount(CalService.getInstance().calculationWaitSetUp1(order.getType(), order.getFromCompany(), memberInfo, setouttime, waitMinutes));
            calculationDto.setTotalAmount(calculationDto.getTotalAmount().add(calculationDto.getWaitAmount()));
            ChargeStandard chargeStandard = ChargeStandard.dao.findByCompanyAndType(company.getId(), order.getType());
            if (chargeStandard == null) {
                chargeStandard = ChargeStandard.dao.findDefaultByServiceType(order.getServiceType());
            }
            BigDecimal addAmount = order.getAddAmount() == null ? BigDecimal.ZERO : order.getAddAmount();
            calculationDto.setAddAmount(addAmount);
            calculationDto.setTotalAmount(calculationDto.getTotalAmount().add(addAmount));
            ChargeStandardItem chargeStandardItem = ChargeStandardItem.dao.findByChargeStandardAndTime(chargeStandard.getId(), setouttime);
            if (calculationDto.getTotalAmount().compareTo(chargeStandardItem.getMinAmount()) <= 0) {
                calculationDto.setTotalAmount(chargeStandardItem.getMinAmount());
            }
        }
        renderAjaxSuccess(calculationDto);
    }

    /**
     * 充值(包括VIP充值)
     */
    public void recharge() {
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        final CapitalLog capitalLog = new CapitalLog();
        capitalLog.setOperationType(Constant.CapitalOperationType.CZOFali);
        String remark = "通过支付宝充值了" + amount;
        String no = "";
        if (getAppType() == Constant.DRIVER) {
            no = StringsKit.getCaptDriver();
        } else if (getAppType() == Constant.MEMBER) {
            no = StringsKit.getCaptMember();
        }
        if (getParaToBoolean("vip", false)) {
            AdminSetting setting = AdminSetting.dao.findByCompanyId(getMemberInfo().getCompany());
            amount = setting.getVipAmount();
            remark = "通过支付宝充值vip:" + amount;
            no = "VIP-" + no;
            capitalLog.setOperationType(Constant.CapitalOperationType.CZOFaliVIP);
        }
        Date now = DateTime.now().toDate();
        capitalLog.setLoginId(getLoginMember().getId());
        capitalLog.setOrderId(0);
        capitalLog.setCreateTime(now);
        capitalLog.setAmount(amount);
        capitalLog.setOperater(0);
        capitalLog.setRemark(remark);
        capitalLog.setNo(no);
        capitalLog.setStatus(Constant.CapitalStatus.ERROR);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return capitalLog.save();
            }
        });
        if (isOk) {
            BizContentDto bizContentDto = new BizContentDto();
            bizContentDto.setOut_trade_no(capitalLog.getNo());
            Map<String, Object> result = Maps.newHashMap();
            bizContentDto.setProduct_code("QUICK_MSECURITY_PAY");
            result.put("order", capitalLog);
            bizContentDto.setBody(JSON.toJSONString(result));
            bizContentDto.setSubject("充值支付订单" + capitalLog.getNo() + "费用");
            bizContentDto.setTotal_amount(capitalLog.getAmount().toString());
            String content = JSON.toJSONString(bizContentDto);
            Map<String, String> params = null;
            params = OrderInfoKit.buildOrderParamMap(content);
            String orderParam = OrderInfoKit.buildOrderParam(params);
            String sign = OrderInfoKit.getSign(params);
            final String orderInfo = orderParam + "&" + sign;
            renderAjaxSuccess(orderInfo);
        } else {
            renderAjaxError("错误");
        }
    }

    public void wxrecharge() {
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        Date now = DateTime.now().toDate();
        String remark = "通过微信充值" + amount;
        String no = "";
        if (getAppType() == Constant.DRIVER) {
            no = StringsKit.getCaptDriver();
        } else if (getAppType() == Constant.MEMBER) {
            no = StringsKit.getCaptMember();
        }

        final CapitalLog capitalLog = new CapitalLog();
        capitalLog.setLoginId(getLoginMember().getId());
        capitalLog.setOrderId(0);
        capitalLog.setCreateTime(now);
        capitalLog.setAmount(amount);
        capitalLog.setOperater(0);
        capitalLog.setOperationType(Constant.CapitalOperationType.CZOFweixin);

        if (getParaToBoolean("vip", false)) {
            AdminSetting setting = AdminSetting.dao.findByCompanyId(getMemberInfo().getCompany());
            amount = setting.getVipAmount();
            capitalLog.setAmount(amount);
            remark = "通过微信充值vip:" + amount;
            no = "VIP-" + no;
            capitalLog.setOperationType(Constant.CapitalOperationType.CZOFweixinVIP);
        }
        capitalLog.setRemark(remark);
        capitalLog.setNo(no);
        capitalLog.setStatus(Constant.CapitalStatus.ERROR);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return capitalLog.save();
            }
        });

        if (isOk) {
           /* if (getParaToBoolean("vip", false)) {
                renderAjaxSuccess(WechatPayKit.getWechatRechargeForWeb(capitalLog));
                return;
            }*/
            renderAjaxSuccess(getAppType() == Constant.DRIVER ? WechatPayKit.getWechatRechargeForApp(capitalLog, 1) : WechatPayKit.getWechatRechargeForApp(capitalLog, 2));
        } else {
            renderAjaxError("错误");
        }
    }

    public void cancel() {
        final Order order = Order.dao.findById(getPara("orderId"));

        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }

        //越权问题
        String userName = getLoginMember().getUserName();
        MemberInfo menber = MemberInfo.dao.findById(order.getMember());
        if (!userName.equals(menber.getPhone())) {
            renderAjaxError("当前用户身份异常！");
            return;
        }

        if (order.getStatus() == Constant.OrderStatus.START && order != null) {
            renderAjaxError("司机已经出发，无法取消订单！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.END) {
            renderAjaxError("司机已经到达终点，无法取消订单！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.PAYED) {
            renderAjaxError("订单已结算！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.START && order != null) {
            renderAjaxError("司机已经出发，无法取消订单！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.ACCEPT && order != null) {
            OrderLog orderLog1 = OrderLog.dao.findByOrderAndPayAction(order.getId(), Constant.OrderStatus.ACCEPT);
            if (orderLog1 == null) {
                renderAjaxError("无法取消该订单哦！");
                return;
            }
            Date stime = orderLog1.getOperationTime();
            Date etime = DateTime.now().toDate();
            if (etime.getTime() - stime.getTime() > 3 * 60 * 1000) {
                renderAjaxError("订单已超时，无法取消！");
                return;
            }
            EventKit.post(new CancelOrder(order));
        }


        if (order.getServiceType() == Constant.ServiceType.ZhuanXian || order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
            Schedule.dao.updateByOrderId(order.getId());
        }
   /* if (order.getStatus() != Constant.OrderStatus.CREATE) {
        renderAjaxError("订单无法取消");
        return;
    }*/
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            OrderLog orderLog = new OrderLog();
            orderLog.setOperationTime(DateTime.now().toDate());
            orderLog.setOrderId(order.getId());
            orderLog.setRemark(driverInfo.getRealName() + "(司机)取消了订单");
            orderLog.setAction(Constant.OrderAction.CANCEL);
            orderLog.setLoginId(driverInfo.getLoginId());
            order.setDriver(null);
            order.setStatus(Constant.OrderStatus.CREATE);
            order.setLastUpdateTime(DateTime.now().toDate());
            order.update();
            orderLog.save();
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            if (order.getPdFlag()) {
                int num = Order.dao.pdOrderCount(order.getDriver(), Constant.OrderStatus.ACCEPT);
                if (num > 1) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                }
            } else {
                memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
            }
            EventKit.post(new PushOrder(order));
        } else {
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            OrderLog orderLog = new OrderLog();
            orderLog.setOperationTime(DateTime.now().toDate());
            orderLog.setOrderId(order.getId());
            orderLog.setRemark(memberInfo.getRealName() + "(乘客)取消了订单");
            orderLog.setAction(Constant.OrderAction.CANCEL);
            orderLog.setLoginId(memberInfo.getLoginId());
            order.setStatus(Constant.OrderStatus.CANCEL);
            order.setLastUpdateTime(DateTime.now().toDate());
            order.update();
            orderLog.save();
            if (order.getDriver() != null) {
                DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
                MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
                if (order.getPdFlag()) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                }
                memberLogin.update();
                MemberOrderCache.getInstance().del(order.getNo());
                MemberOrderCache.getInstance().del(MemberInfo.dao.findById(order.getMember()));
            }
        }
        renderAjaxSuccess("取消订单成功！");
    }

    public void myOrder() {
        Date start;
        Date end;
        int type = getParaToInt("type", 1);
        int serviceType = getParaToInt("serviceType");
        int pageStart = getPageStart();
        DateTime now = DateTime.now();
        if (type == 1) {//今天
            start = now.millisOfDay().withMinimumValue().toDate();
            end = now.millisOfDay().withMaximumValue().toDate();
        } else if (type == 2) {//昨天
            start = now.plusDays(-1).millisOfDay().withMinimumValue().toDate();
            end = now.plusDays(-1).millisOfDay().withMaximumValue().toDate();
        } else if (type == 3) {//上月
            start = now.plusMonths(-1).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
            end = now.plusMonths(-1).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        } else {//本月
            start = now.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
            end = now.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        }
        if (getAppType() == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            List<Order> orders = Order.dao.findByMemberAndDateServiceTypePage(memberInfo.getId(), serviceType, start, end, pageStart, 100);
            renderAjaxSuccess(orders);
        } else if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            List<Order> orders = Order.dao.findByDriverAndDateServiceTypePage(driverInfo.getId(), serviceType, start, end, pageStart, 100);
            renderAjaxSuccess(orders);
        }
    }

    public void orderItem() {

    }

    public void drivercreate() {
        Date now = DateTime.now().toDate();

        String phone = getPara("phone");
        String start = getPara("start");
        String startLat = getPara("startLatitude");
        String startLon = getPara("startLongitude");
        String end = getPara("end");
        String endLat = getPara("endLatitude");
        String endLon = getPara("endLongitude");
        String distance = getPara("distance");
        String times = getPara("time");
        String remark = getPara("remark");

        if (Strings.isNullOrEmpty(start) ||
                Strings.isNullOrEmpty(startLat) ||
                Strings.isNullOrEmpty(startLon)) {
            renderAjaxError("未获取起点位置,订单创建失败");
            return;
        }
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxError("手机号不能为空");
            return;
        }
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        boolean has = Order.dao.findOrderByDriver(driverInfo.getId()) > 0;
        if (has) {
            renderAjaxError("您有未完成订单!");
            return;
        }

        MemberInfo memberInfo = MemberInfo.dao.creatMember(phone, Constant.FromType.BUDAN, driverInfo.getCompany());
        if (startLat == null || startLon == null) {
            renderAjaxError("请稍等，正在获取您的位置信息！");
        }
        if (memberInfo == null) {
            renderAjaxError("创建订单失败!");
            return;
        }
        final Order order = new Order();
        order.setDriver(driverInfo.getId());
        order.setMember(memberInfo.getId());
        order.setCreateTime(now);
        order.setNo(StringsKit.getOrderNo());
        order.setReservationAddress(start);
        order.setCompany(driverInfo.getCompany());
        order.setPhone(phone);
        order.setStatus(Constant.OrderStatus.ACCEPT);//todo
        order.setSetOutFlag(false);
        order.setSetouttime(now);
        order.setServiceType(Constant.ServiceType.DaiJia);
        order.setFromType(Constant.FromType.BUDAN);
        order.setPdFlag(false);
        order.setRemark(remark);
        order.setDistance(new BigDecimal(distance));
        order.setFromCompany(driverInfo.getCompany());
        order.setRealDistance(BigDecimal.ZERO);

        //order_ip order_IMSI order_IMEI macAddress port
        order.setOrderIp(getPara("orderIp"));
        order.setOrderImsi(getPara("orderIMSI"));
        order.setOrderImei(getPara("orderIMEI"));
        order.setMacAddress(getPara("macAddress"));
        order.setPort(String.valueOf(getRequest().getRemotePort()));

        Company company = Company.dao.findByCompanyId(driverInfo.getCompany());
        List<CalculationDto> calculationDto = CalService.getInstance().calculationDtoSetUp1(Constant.ServiceType.DaiJia, company, memberInfo, null, new BigDecimal(distance), new BigDecimal(times));
        order.setYgAmount(calculationDto.get(0).getTotalAmount());
        List<ServiceTypeItem> serviceTypeItem = ServiceTypeItem.dao.findByType(Constant.ServiceType.DaiJia);
        order.setType(serviceTypeItem.get(0).getId());
        final OrderTrip orderTrip = new OrderTrip();
        orderTrip.setStartLatitude(new BigDecimal(startLat).doubleValue());
        orderTrip.setStartLongitude(new BigDecimal(startLon).doubleValue());
        orderTrip.setMember(memberInfo.getId());
        order.setDestination(end);
        if (!Strings.isNullOrEmpty(endLat) && !Strings.isNullOrEmpty(endLon)) {
            orderTrip.setEndLongitude(new BigDecimal(endLon).doubleValue());
            orderTrip.setEndLatitude(new BigDecimal(endLat).doubleValue());
        }
        final OrderLog orderLog = new OrderLog();
        orderLog.setLoginId(getLoginMember().getId());
        orderLog.setRemark("司机在" + start + "创建的订单");
        orderLog.setAction(Constant.OrderAction.CREATE);
        orderLog.setOperationTime(now);
        orderLog.setOperater(driverInfo.getPhone());
        final MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
        memberLogin.setStatus(Constant.LoginStatus.BUSY);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (order.save()) {
                    orderTrip.setOrderId(order.getId());
                    orderLog.setOrderId(order.getId());
                    return orderLog.save() && orderTrip.save() && memberLogin.update();
                }
                return false;
            }
        });
        if (isOk) {
            order.put("serviceType", order.getServiceType());
            order.put("nick_name", memberInfo.getNickName());
            order.put("head_portrait", memberInfo.getHeadPortrait());
            orderTrip.put("endLatitude", orderTrip.getEndLatitude());
            orderTrip.put("endLongitude", orderTrip.getEndLongitude());
            orderTrip.put("startLatitude", orderTrip.getStartLatitude());
            orderTrip.put("startLongitude", orderTrip.getStartLongitude());
            orderTrip.put("member", orderTrip.getMember());
            orderTrip.put("orderId", orderTrip.getOrderId());
            order.put("reservationAddress", order.getReservationAddress());
            order.put("trip", orderTrip);
            renderAjaxSuccess(order);
        } else {
            renderAjaxError("创建订单失败！");
        }
    }

    /**
     * 计算等待时间
     */
    public void waitAmount() {
        BigDecimal waitMinutes = new BigDecimal(Strings.isNullOrEmpty(getPara("waitMinutes")) ? "0" : getPara("waitMinutes"));
        Order order = Order.dao.findById(getPara("id"));

        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        renderAjaxSuccess(CalService.getInstance().calculationWaitSetUp1(order.getServiceType(), order.getCompany(), memberInfo, new DateTime(order.getSetouttime()), waitMinutes));
    }

    /**
     * 查询自己的车辆
     */
    public void myCars() {
        List<Car> carList = Lists.newArrayList();
        carList = Car.dao.findByDriverAndStatus(getDriverInfo().getId(), Constant.DataAuditStatus.AUDITOK);
        renderAjaxSuccess(carList);
    }

    /**
     * 获取预约订单
     */
    public void yuyuedingdan() {
        List<Order> orders = new ArrayList<>();
        if (getAppType() == Constant.DRIVER) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            orders = Order.dao.findYuyuedingdanByDriver(driverInfo.getId());
        } else if (getAppType() == Constant.MEMBER) {
            MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
            orders = Order.dao.findYuyuedingdanByMember(memberInfo.getId());
        }
        //加密
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (Order ors : orders) {
                if (ors != null) {
                    if (ors.get("phone") != null) {
                        ors.put("H5phone", ors.get("phone").toString());
                        String phone = AESOperator.getInstance().encrypt(ors.get("phone").toString());
                        ors.put("phone", phone);
                    }
                }
            }
        }
        renderAjaxSuccess(orders);
    }

    /**
     * 获取未开票的订单
     */
    public void noinvoice() {
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(getLoginMember().getId());
        List<Order> orders = Order.dao.findNoInvoiceByMember(memberInfo.getId(), getPageStart(), getPageSize());
        renderAjaxSuccess(orders);
    }

    public synchronized void invoice() {
        String ids = getPara("orderIds", "");
        String invoiceHeader = getPara("invoiceHeader");
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        String content = getPara("content");
        String postCode = getPara("postCode");
        String postAddress = getPara("postAddress");
        String addressee = getPara("addressee");
        String addresseePhone;
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            addresseePhone = AESOperator.getInstance().decrypt(getPara("addresseePhone"));
        } else {
            addresseePhone = getPara("addresseePhone");
        }
        String[] orderIds = ids.split(",");
        if (orderIds.length > 0) {
            BigDecimal orderAmount = BigDecimal.ZERO;
            List<Order> orders = Order.dao.findByIds(orderIds);
            final List<InvoiceOrder> invoiceOrders = Lists.newArrayList();
            for (Order order : orders) {
                orderAmount = orderAmount.add(order.getRealPay());
                InvoiceOrder invoiceOrder = new InvoiceOrder();
                invoiceOrder.setOrderId(order.getId());
                invoiceOrders.add(invoiceOrder);
            }
            if (amount.compareTo(orderAmount) < 0) {
                renderAjaxError("开票金额超过所选订单总金额！");
                return;
            }
            final InvoiceRec invoiceRec = new InvoiceRec();
            invoiceRec.setAddressee(addressee);
            invoiceRec.setLoginId(getLoginMember().getId());
            invoiceRec.setContent(content);
            invoiceRec.setAmount(amount);
            invoiceRec.setAddresseePhone(addresseePhone);
            invoiceRec.setInvoiceHeader(invoiceHeader);
            invoiceRec.setPostAddress(postAddress);
            invoiceRec.setPostCode(postCode);
            invoiceRec.setStatus(Constant.DataAuditStatus.CREATE);
            invoiceRec.setCreateTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (invoiceRec.save()) {
                        for (InvoiceOrder invoiceOrder : invoiceOrders) {
                            invoiceOrder.setInvoiceId(invoiceRec.getId());
                            if (!invoiceOrder.save()) {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (isOk) {
                renderAjaxSuccess("开票成功！等待邮寄");
            } else {
                renderAjaxError("系统错误！稍后再试");
            }
        } else {
            renderAjaxError("请选择需要开票的订单");
        }
    }

    /**
     * 加价接口
     */
    public void addamount() {
        final int orderId = getParaToInt("id", 0);
        BigDecimal amount = new BigDecimal(getPara("addamount", "0"));
        Order order = Order.dao.findById(orderId);
        BigDecimal addAmount = order.getAddAmount() == null ? BigDecimal.ZERO : order.getAddAmount();
        if (order.getStatus() != Constant.OrderStatus.CREATE) {
            renderAjaxError("订单无法加价！");
        } else {
            order.setAddAmount(addAmount.add(amount));
            order.setAmount(addAmount.add(amount));
            order.setAddFlag(true);
            OrderAddAmountRecord addAmountRecord = new OrderAddAmountRecord();
            addAmountRecord.setAmount(amount);
            addAmountRecord.setCreateTime(DateTime.now().toDate());
            addAmountRecord.setOrderId(orderId);
            if (Order.dao.findById(orderId).getStatus() != Constant.OrderStatus.CREATE) {
                renderAjaxError("订单无法加价！");
            } else {
                order.update();
                addAmountRecord.save();
                EventKit.post(new PushOrder(order));
                renderAjaxSuccess("加价成功!");
            }
        }
    }

    /**
     * app支付回调修改状态
     */
    public void modified() {
        final int orderId = getParaToInt("id", 0);
        Order order = Order.dao.findById(orderId);
        if (order.getStatus() == Constant.OrderStatus.END) {
            order.setStatus(Constant.OrderStatus.PAYED);
            order.update();
            renderAjaxSuccess("状态修改成功!");
        } else if (order.getStatus() == Constant.OrderStatus.PAYED) {
            renderAjaxSuccess("状态不用修改!");
        } else {
            renderAjaxError("订单状态错误!");
        }
    }

    /**
     * h5 获取预约订单是否接单了
     */
    public void h5findyuyueStatus() {
        final int orderId = getParaToInt("orderId", 0);
        if (orderId != 0) {
            Order order = Order.dao.findById(orderId);
            if (order.getStatus() == Constant.OrderStatus.ACCEPT) {
                renderAjaxSuccess(true);
                return;
            }
            renderAjaxSuccess(false);
            return;
        }
        renderAjaxError("订单信息错误！");
        return;
    }


}