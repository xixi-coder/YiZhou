package services;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.redis.Redis;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import base.Constant;
import dto.JPushToMemberDto;
import dto.pushDto.PushMap;
import dto.pushorder.DispatchSetting;
import kits.StringsKit;
import models.car.Car;
import models.count.DriverOrderStatistics;
import models.count.MemberOrderStatistics;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.driver.Grade;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.member.MemberPushId;
import models.order.Order;
import models.order.OrderTrip;
import models.order.TraverRecord;
import models.reject.RejectLog;
import models.sys.AdminSetting;
import models.sys.RecordLog;
import utils.AESOperator;
import utils.GeTuiPushUtil;

/**
 * Created by BOGONj on 2016/9/6.
 */
@SuppressWarnings("ALL")
public class PushOrderService {
    Logger logger = LoggerFactory.getLogger(PushOrderService.class);

    private PushOrderService() {
    }

    private static class PushOrderServiceHolder {
        static PushOrderService intance = new PushOrderService();
    }

    public static PushOrderService getIntance() {
        return PushOrderServiceHolder.intance;
    }

    /**
     * 城际专线取出司机的ID
     * @param order 订单
     * @param driverInfos   司机的信息
     */
    public void pushToDriverSet(Order order, DriverInfo driverInfos) {
        logger.info("推送接口---------》pushToDriverSet");
        List<DriverInfo> tmp = Lists.newArrayList();
        tmp.add(driverInfos);
        Map<Integer, String[]> pushIds = getPushIds(tmp);
        if (pushIds.get(1).length > 0) {
            logger.info("极光推送的ids:{}", pushIds.get(1));
            logger.info("个推推送的ids:{}", pushIds.get(11));
            pushToDriverSetOne(order, pushIds.get(1), pushIds.get(11), 1);
        }
        if (pushIds.get(2).length > 0) {
            logger.info("极光推送的ids:{}", pushIds.get(2));
            logger.info("个推推送的ids:{}", pushIds.get(22));
            pushToDriverSetOne(order, pushIds.get(2), pushIds.get(22), 2);
        }


    }

    public void pushToDriverSetList(Order order, List<DriverInfo> driverInfos) {
        Map<Integer, String[]> pushIds = getPushIds(driverInfos);
        logger.info("推送接口---------》pushToDriverSetList");
        if (pushIds.get(1).length > 0) {
            logger.info("极光推送的ids:{}", pushIds.get(1).toString());
            logger.info("个推推送的ids:{}", pushIds.get(11).toString());
            pushToDriverSetOne(order, pushIds.get(1), pushIds.get(11), 1);
        }
        if (pushIds.get(2).length > 0) {
            logger.info("极光推送的ids:{}", pushIds.get(2).toString());
            logger.info("个推推送的ids:{}", pushIds.get(22).toString());
            pushToDriverSetOne(order, pushIds.get(2), pushIds.get(22), 2);
        }
        //记录推送记录
        rejectLog(driverInfos, order.getId());
    }

    public Map<Integer, String[]> getPushIds(List<DriverInfo> driverInfos) {
        List<String> iosR = Lists.newArrayList();
        List<String> iosC = Lists.newArrayList();
        List<String> androidR = Lists.newArrayList();
        List<String> androidC = Lists.newArrayList();
        for (DriverInfo driverInfo : driverInfos) {
            MemberLogin tmp = MemberLogin.dao.findById(driverInfo.getLoginId());
            if (tmp.getCacheKey() == null) {
                continue;
            }
            MemberLogin memberLogin = Redis.use(Constant.LOGINMEMBER_CACHE).get(tmp.getCacheKey());
            if (memberLogin != null) {
                int type = memberLogin.getInt("deviceType");
                MemberPushId pushId = MemberPushId.dao.findByMemberId(memberLogin.getId());
                logger.info("=========推送的用户名为:{}", memberLogin.getUserName());
                switch (type) {
                    case 1://android
                        if (pushId != null) {
                            if (pushId.getDeviceType() == 1) {
                                androidR.add(pushId.getRegistrationId());
                                androidC.add(pushId.getCId());
                            }
                        }
                        break;
                    case 2://ios
                        if (pushId != null) {
                            if (pushId.getDeviceType() == 2) {
                                iosR.add(pushId.getRegistrationId());
                                iosC.add(pushId.getCId());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        String[] androidRegR = new String[androidR.size()];
        String[] androidRegC = new String[androidC.size()];
        String[] iosRegR = new String[iosR.size()];
        String[] iosRegC = new String[iosC.size()];
        androidR.toArray(androidRegR);
        androidC.toArray(androidRegC);
        iosR.toArray(iosRegR);
        iosC.toArray(iosRegC);
        Map<Integer, String[]> register = Maps.newHashMap();
        register.put(1, androidRegR);
        register.put(11, androidRegC);
        register.put(2, iosRegR);
        register.put(22, iosRegC);
        return register;
    }

    public Map<Integer, String[]> getMemberRegistrationIds(List<MemberInfo> memberInfos) {
        List<String> iosR = Lists.newArrayList();
        List<String> iosC = Lists.newArrayList();
        List<String> androidR = Lists.newArrayList();
        List<String> androidC = Lists.newArrayList();
        for (MemberInfo memberInfo : memberInfos) {
            MemberLogin tmp = MemberLogin.dao.findById(memberInfo.getLoginId());
            if (tmp.getCacheKey() == null) {
                continue;
            }
            MemberLogin memberLogin = Redis.use(Constant.LOGINMEMBER_CACHE).get(tmp.getCacheKey());
            if (memberLogin != null) {
                int type = memberLogin.getInt("deviceType");
                MemberPushId pushId = MemberPushId.dao.findByMemberId(memberLogin.getId());
                logger.info("=========推送的用户名为:{}", memberLogin.getUserName());
                switch (type) {
                    case 1://android
                        if (pushId != null) {
                            if (pushId.getDeviceType() == 1) {
                                androidR.add(pushId.getRegistrationId());
                                androidC.add(pushId.getCId());
                            }
                        }
                        break;
                    case 2://ios
                        if (pushId != null) {
                            if (pushId.getDeviceType() == 2) {
                                iosR.add(pushId.getRegistrationId());
                                iosC.add(pushId.getCId());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        String[] androidRegR = new String[androidR.size()];
        String[] androidRegC = new String[androidC.size()];
        String[] iosRegR = new String[iosR.size()];
        String[] iosRegC = new String[iosC.size()];
        androidR.toArray(androidRegR);
        androidC.toArray(androidRegC);
        iosR.toArray(iosRegR);
        iosC.toArray(iosRegC);
        Map<Integer, String[]> register = Maps.newHashMap();
        register.put(1, androidRegR);
        register.put(11, androidRegC);
        register.put(2, iosRegR);
        register.put(22, iosRegC);
        return register;
    }

    public void pushToDriverSetOne(Order order, String[] registrationIds, String[] cId, int type) {
        //加密用户信息
        order.setPhone(AESOperator.getInstance().encrypt(order.getPhone()));

        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        Integer title = Constant.JpushType.PushOrderToDriver;
        OrderTrip orderTrip = order.get("trip");
        String tripContent = JSONObject.toJSONString(orderTrip);
        String odcontent = JSONObject.toJSONString(order);
        JSONObject od = JSONObject.parseObject(odcontent);
        JSONObject tp = JSONObject.parseObject(tripContent);
        if (order.getServiceType() == Constant.ServiceType.ShunFengChe) {
            title = Constant.JpushType.PushOrderToShunFengCheDriver;
            TraverRecord traverRecord = TraverRecord.dao.findById(order.getTraverId());
            od.put("mess", traverRecord.getMess());
        }
        od.put("sCity", order.get("sCity"));
        od.put("eCity", order.get("eCity"));
        od.put("trip", tp);
        od.put("nick_name", memberInfo.getNickName());
        od.put("head_portrait", memberInfo.getHeadPortrait());
        od.put("timeOut", order.getNumber("timeOut") == null ? 10 : order.getNumber("timeOut").intValue());
        //会员的下单数
        od.put("memberOrderCount", (MemberOrderStatistics.dao.findByMemberId(order.getMember()) == null ? 0 : MemberOrderStatistics.dao.findByMemberId(order.getMember()).getOrderNum()));

        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(title);
        pushMap.setContent(od);
        JPushService.getInstance().sendMessageToDriver(title, JSONObject.toJSON(pushMap).toString(), registrationIds);
//        pushMap.setPushType(Constant.PushType.GT);
//        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, title.toString(), JSONObject.toJSONString(pushMap), order.getNo(), cId);
    }

    public void pushOrderToAllDriver(Order order) {//通过后台设置规则查询附近的司机
        logger.info("派单开始了============================={}", order.getServiceType());
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());//查询订单的行程
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
        } catch (Exception e) {
            logger.error("系统设置错误！");
            adminSetting = AdminSetting.dao.findFirst();
        }
        if (Constant.ServiceType.ZhuanXian == order.getServiceType() || Constant.ServiceType.HangKongZhuanXian == order.getServiceType()) {
            order = Order.dao.findById(order.getId());
            //推送唯一标识
            order.put("trip", orderTrip);
            if (order.getStatus() != Constant.OrderStatus.CREATE) {
                return;
            }
            order.put("timeOut", adminSetting.getOrderReciveTime() == null ? 5 : adminSetting.getOrderReciveTime());
            List<DriverInfo> driverInfos = Lists.newArrayList();
            //本区域的司机可接单
            driverInfos = DriverInfo.dao.findByZxLine(order, orderTrip.getStartLatitude() + "", orderTrip.getStartLongitude() + "");
            order.put("driverInfos", driverInfos);
            rejectLog(driverInfos, order.getId());
            for (DriverInfo driverInfo : driverInfos) {
                logger.info("派单给{}", driverInfo.getId());
                order = Order.dao.findById(order.getId());
                //推送唯一标识
                order.put("trip", orderTrip);
                order.put("timeOut", adminSetting.getOrderReciveTime() == null ? 20 : adminSetting.getOrderReciveTime());
                if (order.getStatus() != Constant.OrderStatus.CREATE) {
                    break;
                }
                pushToDriverSet(order, driverInfo);
                try {
                    Thread.sleep(1000 * 20);
                } catch (InterruptedException e) {
                    logger.error("推送订单失败了，订单号:{},当前日期:{}", order.getNo(), DateTime.now().toDateTime());
                    e.printStackTrace();
                }
            }
            //20秒之后再查一次订单,如果订单还处于未接单的状态,推送给所有司机
            String no = order.getNo();
            Order orderbyNo = Order.dao.findByNo(no);
            if(orderbyNo.getStatus() == Constant.OrderStatus.CREATE){
                pushToDriverSetList(order, driverInfos);
            }

        } else {
            if (adminSetting.getAutoDispatch()) {
                //是自动派单
                List<DispatchSetting> dispatchSettings = Lists.newArrayList();
                double dispatchMill1 = adminSetting.getDispatchMill1();
                double dispatchMillDefault = adminSetting.getDispatchMillDefault();
                DispatchSetting dispatchSetting1 = new DispatchSetting();
                dispatchSetting1.setStartDistance(dispatchMillDefault);
                dispatchSetting1.setEndDistance(dispatchMill1);
                dispatchSetting1.setTimeOut(adminSetting.getDispatchTimeOut1());
                dispatchSettings.add(dispatchSetting1);
                double dispatchMill2 = adminSetting.getDispatchMill2();
                DispatchSetting dispatchSetting2 = new DispatchSetting();
                dispatchSetting2.setStartDistance(dispatchMill1);
                dispatchSetting2.setEndDistance(dispatchMill2);
                dispatchSetting2.setTimeOut(adminSetting.getDispatchTimeOut2());
                dispatchSettings.add(dispatchSetting2);
                double dispatchMill3 = adminSetting.getDispatchMill3();
                DispatchSetting dispatchSetting3 = new DispatchSetting();
                dispatchSetting3.setStartDistance(dispatchMill2);
                dispatchSetting3.setEndDistance(dispatchMill3);
                dispatchSetting3.setTimeOut(adminSetting.getDispatchTimeOut3());
                dispatchSettings.add(dispatchSetting3);
                for (DispatchSetting dispatchSetting : dispatchSettings) {
                    logger.info("派单{}", dispatchSetting.getTimeOut());
                    order = Order.dao.findById(order.getId());
                    if (order.getStatus() != Constant.OrderStatus.CREATE) {
                        break;
                    }
                    List<DriverInfo> driverInfos = Lists.newArrayList();
                    if (true) {
                        //只能本地区司机接单
                        driverInfos = DriverInfo.dao.findByDistance(order.getType(), order.getPdFlag(), orderTrip.getStartLatitude() + "", orderTrip.getStartLongitude() + "", dispatchSetting.getStartDistance(), dispatchSetting.getEndDistance());
                    } else {
                        //在范围的本公司的司机都能接单
                        driverInfos = DriverInfo.dao.findByDistance(order.getType(), order.getPdFlag(), orderTrip.getStartLatitude() + "", orderTrip.getStartLongitude() + "", order.getFromCompany(), dispatchSetting.getStartDistance(), dispatchSetting.getEndDistance());
                    }
                    logger.info("派单给{}个司机", driverInfos.size());
                    order.put("driverInfos", driverInfos);
                    //记录推送记录
                    rejectLog(driverInfos, order.getId());
                    for (DriverInfo driverInfo : driverInfos) {
                        logger.info("派单给{}", driverInfo.getId());
                        order = Order.dao.findById(order.getId());
                        //推送唯一标识
                        order.put("trip", orderTrip);
                        order.put("timeOut", adminSetting.getOrderReciveTime() == null ? 5 : adminSetting.getOrderReciveTime());
                        if (order.getStatus() != Constant.OrderStatus.CREATE) {
                            break;
                        }
                        pushToDriverSet(order, driverInfo);
                        try {
                            Thread.sleep(1000 * dispatchSetting.getTimeOut());
                        } catch (InterruptedException e) {
                            logger.error("推送订单失败了，订单号:{},当前日期:{}", order.getNo(), DateTime.now().toDateTime());
                            e.printStackTrace();
                        }
                    }
                }
            } else if (adminSetting.getGrabSingleFlag()) {
                //是抢单
                //是自动派单
                List<DispatchSetting> dispatchSettings = Lists.newArrayList();

                double dispatchMill1 = adminSetting.getGrabSingleMill1();
                double grabSingleMillDefault = adminSetting.getGrabSingleMillDefault();
                DispatchSetting dispatchSetting1 = new DispatchSetting();
                dispatchSetting1.setStartDistance(grabSingleMillDefault);
                dispatchSetting1.setEndDistance(dispatchMill1);
                dispatchSetting1.setTimeOut(adminSetting.getGrabSingleTimeOut1());
                dispatchSettings.add(dispatchSetting1);

                double dispatchMill2 = adminSetting.getGrabSingleMill2();
                DispatchSetting dispatchSetting2 = new DispatchSetting();
                dispatchSetting2.setStartDistance(dispatchMill1);
                dispatchSetting2.setEndDistance(dispatchMill2);
                dispatchSetting2.setTimeOut(adminSetting.getGrabSingleTimeOut2());
                dispatchSettings.add(dispatchSetting2);

                double dispatchMill3 = adminSetting.getGrabSingleMill3();
                DispatchSetting dispatchSetting3 = new DispatchSetting();
                dispatchSetting3.setStartDistance(dispatchMill2);
                dispatchSetting3.setEndDistance(dispatchMill3);
                dispatchSetting3.setTimeOut(adminSetting.getGrabSingleTimeOut3());
                dispatchSettings.add(dispatchSetting3);
                for (DispatchSetting dispatchSetting : dispatchSettings) {
                    logger.info("抢单{}", dispatchSetting.getTimeOut());
                    order = Order.dao.findById(order.getId());
                    //推送唯一标识
                    order.put("trip", orderTrip);
                    if (order.getStatus() != Constant.OrderStatus.CREATE) {
                        break;
                    }
                    order.put("timeOut", adminSetting.getOrderReciveTime() == null ? 5 : adminSetting.getOrderReciveTime());
                    List<DriverInfo> driverInfos = Lists.newArrayList();
                    if (true) {
                        //本区域的司机可接单
                        driverInfos = DriverInfo.dao.findByDistance(order.getType(), order.getPdFlag(), orderTrip.getStartLatitude() + "", orderTrip.getStartLongitude() + "", dispatchSetting.getStartDistance(), dispatchSetting.getEndDistance());
                    } else {
                        //本区域的司机可接单
                        driverInfos = DriverInfo.dao.findByDistance(order.getType(), order.getPdFlag(), orderTrip.getStartLatitude() + "", orderTrip.getStartLongitude() + "", order.getFromCompany(), dispatchSetting.getStartDistance(), dispatchSetting.getEndDistance());
                    }
                    order.put("driverInfos", driverInfos);
                    pushToDriverSetList(order, driverInfos);
                    try {
                        Thread.sleep(1000 * dispatchSetting.getTimeOut());
                    } catch (InterruptedException e) {
                        logger.error("推送订单失败了，订单号:{},当前日期:{}", order.getNo(), DateTime.now().toDateTime());
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void pushToShunFengChe(Order order) {
        logger.info("派单开始了=============================");
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
        } catch (Exception e) {
            logger.error("系统设置错误！");
            adminSetting = AdminSetting.dao.findFirst();
        }
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());//查询订单的行程
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        Integer title = Constant.JpushType.PushOrderToShunFengCheDriver;
        String tripContent = JSONObject.toJSONString(orderTrip);
        String odcontent = JSONObject.toJSONString(order);
        JSONObject od = JSONObject.parseObject(odcontent);
        JSONObject tp = JSONObject.parseObject(tripContent);


        //推送唯一标识
        od.put("trip", tp);
        od.put("timeOut", adminSetting.getOrderReciveTime() == null ? 5 : adminSetting.getOrderReciveTime());
        od.put("orderType", 2);
        od.put("setout_time1", order.get("setout_time1"));
        od.put("setout_time2", order.get("setout_time2"));
        od.put("nick_name", memberInfo.getNickName());
        od.put("head_portrait", memberInfo.getHeadPortrait());

        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(title);
        pushMap.setContent(od);


        String regisId;
        String cId = null;
        if (order.get("regisId") == null) {
            regisId = MemberPushId.dao.findByMemberId(MemberLogin.dao.findById(DriverInfo.dao.findById(order.getDriver()).getLoginId()).getId()).getRegistrationId();
            cId = MemberPushId.dao.findByMemberId(MemberLogin.dao.findById(DriverInfo.dao.findById(order.getDriver()).getLoginId()).getId()).getCId();
        } else {
            regisId = order.get("regisId");
        }
        logger.info("派单给regisId{}", order.get("regisId"));
        JPushService.getInstance().sendMessageToDriver(title, JSONObject.toJSONString(pushMap), regisId.toString());
//        pushMap.setPushType(Constant.PushType.GT);
//        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, title.toString(), JSONObject.toJSONString(pushMap), order.getNo(), cId);
    }

    public void pushChenjiDriver(Order order) {
        String sCityCode = order.get("sCityCode");
        String eCityCode = order.get("eCityCode");
        String sCity = order.get("sCity");
        String eCity = order.get("eCity");

        logger.info("派单开始了=============================");
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
        } catch (Exception e) {
            logger.error("系统设置错误！");
            adminSetting = AdminSetting.dao.findFirst();
        }
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());//查询订单的行程

        order.put("trip", orderTrip);
        order.put("timeOut", adminSetting.getOrderReciveTime() == null ? 5 : adminSetting.getOrderReciveTime());
        order.put("orderType", 2);
        order.put("sCity", sCity);
        order.put("eCity", eCity);
        List<DriverInfo> driverInfos = DriverInfo.dao.findChenJiDriver(sCityCode, eCityCode);
        pushToDriverSetList(order, driverInfos);
    }

    public void customerConfig(Order order) {
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        Car car = Car.dao.findByDriver(driverInfo.getId());
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
                if("".equals(carDurableYears)){
                    carDurableYears = 0 +"个月";
                }
                car.put("carDurableYears", carDurableYears);
            }
        }
        JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
        jPushToMemberDto.setDriverId(driverInfo.getId());
        jPushToMemberDto.setLatitude(driverRealLocation.getLatitude() + "");
        jPushToMemberDto.setLongitude(driverRealLocation.getLongitude() + "");
        jPushToMemberDto.setNickName(driverInfo.getRealName());
        jPushToMemberDto.setPhone(driverInfo.getPhone());
        jPushToMemberDto.setCar(car);
        jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
        jPushToMemberDto.setPraise(Grade.dao.driverFavorableRate(driverInfo.getId()));//TODO 添加司机好评等级
        Integer type = Constant.JpushType.PushToCustomerStart;
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        String tt = JSONObject.toJSONString(orderTrip);
        JSONObject trip = JSONObject.parseObject(tt);
        String content = JsonKit.toJson(jPushToMemberDto);
        JSONObject od = JSONObject.parseObject(content);

        od.put("orderType", 2);
        od.put("trip", trip);
        od.put("reservationAddress", order.getReservationAddress());
        od.put("destination", order.getDestination());
        od.put("setOutFlag", order.getSetOutFlag());
        od.put("orderId", order.getId());

        MemberPushId memberPushId = customerRegistrationId(order);


        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(type);
        pushMap.setContent(od);


        logger.info("通知乘客的手机号:{}", MemberInfo.dao.findById(order.getMember()).getPhone());
        logger.info("通知乘客的regisId:{}", MemberLogin.dao.findById(MemberInfo.dao.findById(order.getMember()).getLoginId()).getRegistrationId());
        JPushService.getInstance().sendMessageToCustomer(type, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), memberPushId.getCId());
    }

    /**
     * 司机接单，出发，等待后推送客户端
     *
     * @param order
     * @return
     */
    public boolean pushToCustomer(Order order) {
        logger.info("司机接单后推送客户端:{}", order.getServiceType());
        DriverInfo driverInfo = order.get("driverInfo");
        if (driverInfo == null) {
            driverInfo = DriverInfo.dao.findById(order.getDriver());
        } else {
            driverInfo = DriverInfo.dao.findById(driverInfo.getId());
        }
        MemberLogin memberLogin = order.get("memberLogin");
        if (memberLogin != null) {
            logger.info("WS推送的key:{}", memberLogin.getCacheKey());
        } else {
            memberLogin = MemberLogin.dao.findById(MemberInfo.dao.findById(order.getMember()).getLoginId());
        }
        DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(driverInfo.getId());
        Car car = Car.dao.findByDriver(driverInfo.getId());
        JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
        jPushToMemberDto.setDriverId(driverInfo.getId());
        jPushToMemberDto.setLatitude(driverRealLocation.getLatitude() + "");
        jPushToMemberDto.setLongitude(driverRealLocation.getLongitude() + "");
        jPushToMemberDto.setNickName(driverInfo.getRealName());
        jPushToMemberDto.setPhone(driverInfo.getPhone());
        jPushToMemberDto.setCar(car);
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
                if("".equals(carDurableYears)){
                    carDurableYears = 0 + "个月";
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
        jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
        jPushToMemberDto.setPraise(Grade.dao.driverFavorableRate(driverInfo.getId()));//TODO 添加司机好评等级
        Integer type = Constant.JpushType.PushToMember;


        switch (order.getStatus()) {
            case Constant.OrderStatus.ACCEPT:
                type = Constant.JpushType.PushToMember;
                break;
            case Constant.OrderStatus.START:
                type = Constant.JpushType.PushDriverSetOffToMember;
                break;
            case Constant.OrderStatus.DRIVERWAIT:
                type = Constant.JpushType.PushDriverWaitToMember;
                break;
            default:
                logger.error("获取订单状态失败！no:{},status:{}", order.getNo(), order.getStatus());
                break;

        }

        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        String tt = JSONObject.toJSONString(orderTrip);
        JSONObject trip = JSONObject.parseObject(tt);
        String content = JsonKit.toJson(jPushToMemberDto);
        JSONObject od = JSONObject.parseObject(content);
        if (order.getServiceType() == Constant.ServiceType.ShunFengChe) {
            od.put("orderType", 2);
        }
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian) {
            od.put("orderType", 3);
        }
        od.put("vipActivityFlag", order.getVipActivityFlag());
        od.put("trip", trip);
        od.put("reservationAddress", order.getReservationAddress());
        od.put("destination", order.getDestination());
        od.put("setOutFlag", order.getSetOutFlag());
        od.put("orderId", order.getId());
        od.put("status", order.getStatus());
        //司机的接单数
        od.put("driverOrderCount", (DriverOrderStatistics.dao.findByDriverId(order.getDriver()) == null ? 0 : DriverOrderStatistics.dao.findByDriverId(order.getDriver()).getOrderNum()));


        MemberPushId memberPushId = MemberPushId.dao.findByMemberId(memberLogin.getId());

        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(type);
        pushMap.setContent(od);

        logger.info("通知乘客的手机号:{}", MemberInfo.dao.findById(order.getMember()).getPhone());
        logger.info("通知乘客的regisId:{}", MemberLogin.dao.findById(MemberInfo.dao.findById(order.getMember()).getLoginId()).getRegistrationId());
        JPushService.getInstance().sendMessageToCustomer(type, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), memberPushId.getCId());
        System.out.println(JSONObject.toJSONString(pushMap));

        return true;
    }

    public boolean pushToSFCCustomer(TraverRecord traverRecord, DriverInfo driverInfo, MemberLogin memberLogin, int logId) {
        final Order order = new Order();
        String no = StringsKit.getOrderNo();
        order.setNo(no);
        order.setSetOutFlag(false);
        order.setFromType(Constant.FromType.APPTYPE);
        order.setFromCompany(driverInfo.getCompany());//设置订单的公司
        order.setDistance(traverRecord.getDistance());
        RecordLog recordLog = RecordLog.dao.findByRecordIdAndLoginId(traverRecord.getId(), memberLogin.getId());
        if (recordLog.getAmount() != null) {
            order.setAmount(recordLog.getAmount());
        } else if (recordLog.getPinCheAmount() != null) {
            order.setAmount(recordLog.getPinCheAmount());
        } else if (recordLog.getJiHuoAmount() != null) {
            order.setAmount(recordLog.getJiHuoAmount());
        } else {
            order.setAmount(BigDecimal.ZERO);
        }
        order.setStatus(Constant.OrderStatus.SFCACCEPT);
        order.setPayStatus(Constant.PayStatus.NOPAY);
        order.setMember(traverRecord.getMemberId());
        order.setCreateTime(DateTime.now().toDate());
        order.setPhone(MemberInfo.dao.findByLoginId(memberLogin.getId()).getPhone());
        order.setCompany(driverInfo.getCompany());
        order.setType(traverRecord.getRecordType() == 1 ? Constant.ServiceItemType.ShiNei : traverRecord.getRecordType() == 2 ? Constant.ServiceItemType.KuaCheng : traverRecord.getRecordType() == 3 ? Constant.ServiceItemType.DaiHuo : 0);
        order.setDriver(driverInfo.getId());
        order.setServiceType(Constant.ServiceType.ShunFengChe);
        if (traverRecord.getRecordType() != 3) {
            order.setPdFlag(traverRecord.getFlag());
        }
        order.setReservationAddress(traverRecord.getReservationAddress());
        order.setDestination(traverRecord.getDestination());
        order.setTraverId(traverRecord.getId());
        order.setDistance(traverRecord.getDistance());
        order.save();
        Car car = Car.dao.findByDriver(driverInfo.getId());
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
        JPushToMemberDto jPushToMemberDto = new JPushToMemberDto();
        jPushToMemberDto.setDriverId(driverInfo.getId());
        jPushToMemberDto.setNickName(driverInfo.getRealName());
        jPushToMemberDto.setPhone(driverInfo.getPhone());
        jPushToMemberDto.setCar(car);
        jPushToMemberDto.setHeadPortrait(driverInfo.getHeadPortrait());
        jPushToMemberDto.setPraise(Grade.dao.driverFavorableRate(driverInfo.getId()));//TODO 添加司机好评等级
        Integer type = Constant.JpushType.PushToShunFengCheCustomer;
        String content = JsonKit.toJson(jPushToMemberDto);
        JSONObject od = JSONObject.parseObject(content);
        od.put("customerRecordId", traverRecord.getId());
        od.put("reservationAddress", traverRecord.getReservationAddress());
        od.put("destination", traverRecord.getDestination());
        od.put("timeStart", traverRecord.getSetoutTime1());
        od.put("timeEnd", traverRecord.getSetoutTime2());
        od.put("orderId", order.getId());
        od.put("logId", logId);
        od.put("orderType", 2);
        logger.info("顺风车推送确认客户的用户名:{}", memberLogin.getUserName());
        MemberPushId memberPushId = MemberPushId.dao.findByMemberId(memberLogin.getId());
        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(type);
        pushMap.setContent(od);

        JPushService.getInstance().sendMessageToCustomer(type, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), memberPushId.getCId());
        return true;
    }

    /**
     * 推送给客户提示需要支付订单
     *
     * @param order
     * @return
     */
    public boolean pushToPay(Order order) {


        //vip活动用户处理
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

        Integer type = Constant.JpushType.PushMemberPay;
        MemberPushId memberPushId = customerRegistrationId(order);
        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(type);
        pushMap.setContent(order);

        logger.info("支付通知乘客的手机号:{}", MemberInfo.dao.findById(order.getMember()).getPhone());
        JPushService.getInstance().sendMessageToCustomer(type, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), memberPushId.getCId());
        return true;
    }

    /**
     * 通过订单获取用户的推送id
     *
     * @param order
     * @return
     */
    public MemberPushId customerRegistrationId(Order order) {
        MemberLogin memberLogin = MemberLogin.dao.findById(MemberInfo.dao.findById(order.getMember()).getLoginId());
        MemberPushId memberPushId = MemberPushId.dao.findByMemberId(memberLogin.getId());
        return memberPushId;
    }

    /**
     * //     * 推送告知订单被销单
     * //     *
     * //     * @param order
     * //     * @throws IOException
     * //
     */
    public void pushDriverCancel(Order order) {
        if (order.getDriver() == null) {
            return;
        }
        if (order.getServiceType() == Constant.ServiceType.ShunFengChe) {
            order.put("orderType", 2);
        }
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian) {
            order.put("orderType", 3);
        }
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        logger.info("推送给司机提示该订单已被销单了,");
        List<DriverInfo> tmp = Lists.newArrayList();
        tmp.add(driverInfo);
        Map<Integer, String[]> pushIds = getPushIds(tmp);


        Integer title = Constant.JpushType.PushDriverCancel;
        MemberPushId memberPushId = customerRegistrationId(order);

        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(title);
        pushMap.setContent(order);


        if (pushIds.get(1).length > 0) {
            logger.info("极光推送的id:{}", pushIds.get(1).toString());
            JPushService.getInstance().sendMessageToDriver(title, JSONObject.toJSONString(pushMap), pushIds.get(1));
        }
        if (pushIds.get(11).length > 0) {
            logger.info("个推推送的id:{}", pushIds.get(11).toString());
            pushMap.setPushType(Constant.PushType.GT);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, title.toString(), JSONObject.toJSONString(pushMap), order.getNo(), pushIds.get(11));
        }
        if (pushIds.get(2).length > 0) {
            logger.info("极光推送的id:{}", pushIds.get(2).toString());
            JPushService.getInstance().sendMessageToDriver(title, JSONObject.toJSONString(pushMap), pushIds.get(2));
        }
        if (pushIds.get(2).length > 0) {
            logger.info("个推推送的id:{}", pushIds.get(22).toString());
            pushMap.setPushType(Constant.PushType.GT);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, title.toString(), JSONObject.toJSONString(pushMap), order.getNo(), pushIds.get(11));
        }

        logger.info("极光推送乘客订单取消了：单号：{}，会员ID：{}", order.getNo(), memberPushId.getMemberId());
        JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushDriverCancel, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        logger.info("个推推送乘客订单取消了：单号：{}，会员ID：{}", order.getNo(), memberPushId.getMemberId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, title.toString(), JSONObject.toJSONString(pushMap), order.getNo(), memberPushId.getCId());
    }

    //推送专线订单
    public void pushOrderToSpecialDriver(Order order) {
        logger.info("专线订单开始推送");
        List<DriverInfo> driverInfos = Lists.newArrayList();
        driverInfos = DriverInfo.dao.getDriver(order);
        Map<Integer, String[]> pushIds = getPushIds(driverInfos);
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        Integer type = Constant.JpushType.PushToZhuanXian;
        OrderTrip orderTrip = order.get("trip");
        String tripContent = JSONObject.toJSONString(orderTrip);
        String odcontent = JSONObject.toJSONString(order);
        JSONObject od = JSONObject.parseObject(odcontent);
        JSONObject tp = JSONObject.parseObject(tripContent);

        od.put("trip", tp);
        od.put("orderType", 3);
        od.put("nick_name", memberInfo.getNickName());
        od.put("head_portrait", memberInfo.getHeadPortrait());
        od.put("timeOut", order.getNumber("timeOut") == null ? 120 : order.getNumber("timeOut").intValue());


        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setTitle(type);
        pushMap.setContent(od);
        //记录推送记录
        rejectLog(driverInfos, order.getId());
        if (pushIds.get(1).length > 0) {
            logger.info("极光推送的id:{}", pushIds.get(1).toString());
            JPushService.getInstance().sendMessageToDriver(type, JSONObject.toJSONString(pushMap), pushIds.get(1));
        }
        if (pushIds.get(11).length > 0) {
            logger.info("个推推送的id:{}", pushIds.get(11).toString());
            pushMap.setPushType(Constant.PushType.GT);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), pushIds.get(11));
        }
        if (pushIds.get(2).length > 0) {
            logger.info("极光推送的id:{}", pushIds.get(2).toString());
            JPushService.getInstance().sendMessageToDriver(type, JSONObject.toJSONString(pushMap), pushIds.get(2));
        }
        if (pushIds.get(2).length > 0) {
            logger.info("个推推送的id:{}", pushIds.get(22).toString());
            pushMap.setPushType(Constant.PushType.GT);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, type.toString(), JSONObject.toJSONString(pushMap), order.getNo(), pushIds.get(11));
        }
    }


    /**
     * 记录 派单人员
     *
     * @param driverInfos
     */
    private void rejectLog(List<DriverInfo> driverInfos, int orderid) {
        if (driverInfos.size() > 0) {
            for (DriverInfo driverInfo : driverInfos) {
                //存储当前 派单的司机
                RejectLog reject = new RejectLog();
                reject.setOrderId(orderid);
                reject.setDriverId(driverInfo.getId());
                reject.setStatus(0);
                reject.setCreateTime(new Date());
                reject.save();
            }

        }
    }
}
