package controllers.api;

import utils.AESOperator;
import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.redis.Redis;
import dto.JPushToMemberDto;
import jobs.pushorder.PushOrder;
import jobs.pushtocustomer.PushToCustomer;
import jobs.pushtocustomer.PushToPay;
import kits.SmsKit;
import models.car.Car;
import models.company.Company;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.special.SpecialCar;
import models.special.SpecialDriver;
import models.special.SpecialDriverOrder;
import net.dreamlu.event.EventKit;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import services.CalService;
import services.GetSpecialService;
import services.PushOrderService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * Created by BOGONj on 2016/9/1.
 */
@Controller("/api/special")
@Deprecated
public class SpecialController extends BaseApiController {
    /**
     * 获取专线行程
     */
    @ActionKey("/api/special/getSpecialCar")
    public void getSpecialCar() {
        
        if (getDriverInfo() != null) {
            
            if (getAppType() == Constant.DRIVER && !DriverInfo.dao.useFlag(getDriverInfo().getId())) {
                renderAjaxError("请选择车辆上线！");
                return;
            }
            
            if (getDriverInfo().getAllow2() != 1) {
                renderAjaxError("请打开接收专线消息！"); //TODO 关闭了专线消息
                return;
            }
            
            String type = String.valueOf(Constant.ServiceType.ZhuanXian);
            if (getDriverInfo().getType().indexOf(type) == -1) {
                renderAjaxError("您没有注册专线,无法查看！"); //TODO 关闭了专线消息
                return;
            }
        }
        int type = getAppType();//1:司机 2：乘客
        int lineType = getParaToInt("recordType", 47);//1.接机专线48 2.城际专线47 3.送机专线46
        String sCityCode = getPara("sCityCode", "");
        System.out.println(type + "路线" + lineType);
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        List<SpecialCar> specialCarList;
        specialCarList = GetSpecialService.getInstance().getLine(type, lineType, sCityCode, pageStart, pageSize);
        renderAjaxSuccess(specialCarList, "获取成功！");
    }
    
    /**
     * 司机选择专线行程
     */
    public void selectline() {
        if (getAppType() == Constant.DRIVER && !DriverInfo.dao.useFlag(getDriverInfo().getId())) {
            renderAjaxError("请选择车辆上线！");
            return;
        }
        int lineid = getParaToInt("id", 0);
        int totalPeople = getParaToInt("totalPeople", 5);
        MemberInfo memberInfo = getMemberInfo();
        DriverInfo driverInfo = getDriverInfo();
        int number = SpecialDriver.dao.findByDriverInfoId(driverInfo.getId()).size();
        int isexist = SpecialDriver.dao.lineexist(driverInfo.getId(), lineid).size();
        if (number < 5) {
            if (isexist < 1) {
                SpecialDriver specialDriver = new SpecialDriver();
                specialDriver.setSpecialCarId(lineid);
                specialDriver.setDriverInfoId(driverInfo.getId());
                specialDriver.setLineStatus(0);
                specialDriver.setPeople(totalPeople);
                specialDriver.setFlag(0);
                if (specialDriver.save()) {
                    System.out.println("选择线路成功");
                    renderAjaxSuccess("选择线路成功");
                } else {
                    System.out.println("选择线路失败");
                    renderAjaxFailure("选择线路失败");
                }
            } else {
                int i = SpecialCar.dao.updateline(driverInfo.getId(), lineid);
                if (i > 0) {
                    System.out.println("您已成功选择此专线");
                    renderAjaxSuccess("您已成功选择此专线");
                } else {
                    System.out.println("选择线路失败");
                    renderAjaxFailure("选择线路失败");
                }
            }
        } else {
            System.out.println("您选择线路已超过最大限制，不能继续选择！");
            renderAjaxFailure("您选择线路已超过最大限制，不能继续选择！");
        }
    }
    
    /**
     * 司机、乘客 获取个人专线行程
     */
    public void getline() {
        int type = getAppType();//1:司机 2：乘客
        int memberid;
        if (type == Constant.DRIVER) {
            memberid = getDriverInfo().getId();
        } else {
            memberid = getMemberInfo().getId();
        }
        int pageStart = getPageStart();
        int pageSize = getPageSize();
        List<SpecialCar> lineList = GetSpecialService.getInstance().getPersonalLine(type, memberid, pageStart, pageSize);
        if (lineList.size() == 0) {
            renderAjaxSuccess("暂无数据！");
        } else {
            if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
                for (SpecialCar line : lineList) {
                    if (line.get("phone") != null) {
                        String phone = AESOperator.getInstance().encrypt(line.get("phone").toString());
                        line.put("phone", phone);
                    }
                }
            }
            renderAjaxSuccess(lineList);
        }
    }
    
    /**
     * 取消订单
     */
    @Deprecated
    public void cancel() {
        Object o = getPara("id");
        Order order = Order.dao.findById(o);
//        if (order.getStatus()==Constant.OrderStatus.ACCEPT&& order != null){
//            OrderLog orderLog1 = OrderLog.dao.findByOrderAndPayAction(order.getId(),Constant.OrderStatus.ACCEPT);
//            Date stime = orderLog1.getOperationTime();
//            Date etime = DateTime.now().toDate();
//            if (etime.getTime()-stime.getTime()>1*60*60*1000){
//                renderAjaxError("订单已超时，无法取消！");
//                return;
//            }
//            EventKit.post(new CancelOrder(order));
//        }
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        } else if ((order.getStatus() == Constant.OrderStatus.START) || (order.getStatus() == Constant.OrderStatus.PAYED)
        || (order.getStatus() == Constant.OrderStatus.END) || (order.getStatus() == Constant.OrderStatus.DRIVERWAIT)
        || (order.getStatus() == Constant.OrderStatus.SFCACCEPT)) {
            renderAjaxError("司机已经出发，无法取消订单！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已取消，无需重复取消订单！");
            return;
        } else if ((order.getStatus() == Constant.OrderStatus.CREATE) || (order.getStatus() == Constant.OrderStatus.ACCEPT)) {
            //刚创建的订单和司机刚接单的订单是可以取消的  其他状态一律不予许取消
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
                    SpecialDriverOrder specialDriverOrder = new SpecialDriverOrder();
                    SpecialDriver specialDriver = SpecialDriver.dao.findByDriverAndOrder(order.getId(), order.getDriver());
                    specialDriverOrder = specialDriverOrder.findOver(specialDriver.getId(), order.getId());
                    specialDriverOrder.setOver(1);
                    DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
                    MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
                    if (order.getPdFlag()) {
                        memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                        List lineList = GetSpecialService.getInstance().getOrder(order.getDriver());
                        if (lineList.size() - 1 <= 0) {
                            specialDriver = SpecialDriver.dao.findByDriverAndOrder(order.getId(), order.getDriver());
                            specialDriver.setLineStatus(Constant.LineStatusByDriver.NOPASSAGES);
                        }
                    } else {
                        memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                        specialDriver = SpecialDriver.dao.findByDriverAndOrder(order.getId(), order.getDriver());
                        specialDriver.setLineStatus(Constant.LineStatusByDriver.NOPASSAGES);
                    }
                    memberLogin.update();
                    specialDriver.update();
                    specialDriverOrder.update();
                }
            }
            renderAjaxSuccess("取消订单成功！");
            PushOrderService.getIntance().pushDriverCancel(order);//推送
        }
    }
    
    /**
     * 司机 删除已选择专线行程
     */
    public void deleteline() {
        int type = getAppType();//1:司机 2：乘客
        int memberid;
        if (type == Constant.DRIVER) {
            memberid = getDriverInfo().getId();
            
        } else {
            memberid = getMemberInfo().getId();
        }
        int lineId = getParaToInt("id", 0);
        int lineList = SpecialCar.dao.deleteline(type, memberid, lineId);
        if (lineList > 0) {
            renderAjaxSuccess("删除行程成功！", "获取成功！");
        } else {
            renderAjaxFailure("删除该行程失败！");
        }
    }
    
    /**
     * 司机获取已接单行程
     */
    public void getOrder() {
        int driverId = getDriverInfo().getId();
        List<SpecialCar> lineList = GetSpecialService.getInstance().getOrder(driverId);
        
        if (getDeviceType() == Constant.iOS || getDeviceType() == Constant.ANDROID) {
            for (SpecialCar line : lineList) {
                if (line.get("phone") != null) {
                    String phone = line.get("phone");
                    line.put("phone", AESOperator.getInstance().encrypt(phone));
                }
            }
        }
        renderAjaxSuccess(lineList, "获取成功！");
    }
    
    /**
     * 乘客选择线路出行
     */
    public void selectLineByPassages() {
        int lineId = getParaToInt("id", 0);
        int flag = getParaToInt("type", 0);//1.拼车  2.包车
        int people = getParaToInt("people", 4);//包车默认四人
        String sCityCode = getPara("sCityCode", "");
        SpecialCar specialCar = SpecialCar.dao.findById(lineId);
        MemberLogin memberLogin = getLoginMember();
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberLogin.getId());
        if (memberInfo.getStatus() == Constant.MemberInfoStatus.BLACK) {
            renderAjaxError("用户存在异常,无法下单!");
            return;
        }
        Company company;
        if (Strings.isNullOrEmpty(sCityCode)) {//判断是否有cityCode，如果没有就用用户自己的公司
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(sCityCode);
        }
        if (company == null) {
            renderAjaxFailure("该地区无法使用专线！");
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
                String odcontent = toJSONString(jPushToMemberDto);
                JSONObject od = JSONObject.parseObject(odcontent);
                OrderTrip orderTrip = OrderTrip.dao.findById(order1.getId());
                String tt = toJSONString(orderTrip);
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
        List<Order> hasorder = Order.dao.findByMemberForhasorder(memberInfo.getId());
        if (hasorder != null && hasorder.size() > 0) {
            renderAjaxFailure("您有未完成专线订单，请前往执行中查看订单信息！", hasorder.get(0).getId());
            System.out.println("有未完成专线订单，新订单创建失败！");
            return;
        }
        Order order = GetSpecialService.getInstance().createOrder(specialCar, flag, sCityCode, memberInfo, memberLogin, people);
        if (order != null) {
            renderAjaxSuccess(order.getId(), "等待司机接单！");
        } else {
            renderAjaxError("下单失败！");
        }
    }
    
    /**
     * 司机接单了
     */
    public void grabsingle() {
        int orderId = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        final Order order = Order.dao.findById(orderId);
        int peopleNumber = order.findPeopleNumber(driverInfo.getId());//该司机已接人数
        int totalPeople = SpecialDriver.dao.findTotalPeople(driverInfo.getId(), orderId).getPeople();//该司机车辆可接人数
        if (orderId > 0) {
            if (totalPeople - peopleNumber < order.getPeople()) {
                renderAjaxError("该订单人数过多，您无法接此单！");
                return;
            }
            if (peopleNumber > 0 && !order.getPdFlag()) {
                renderAjaxError("您已接包车订单，无法再接订单！");
                return;
            }
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
            order.setDriver(driverInfo.getId());
            order.setCompany(driverInfo.getCompany());
            order.setStatus(Constant.OrderStatus.ACCEPT);
            final SpecialDriverOrder specialDriverOrder = new SpecialDriverOrder();
            SpecialDriver specialDriver = SpecialDriver.dao.findByDriverAndOrder(orderId, driverInfo.getId());
            specialDriverOrder.setOrderid(orderId);
            specialDriverOrder.setSpecialDriver(specialDriver.getId());
            specialDriverOrder.setOver(0);
            specialDriver.setLineStatus(Constant.LineStatusByDriver.HASPASSAGES);
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
            Boolean isOk = dealorder(order, orderLog, memberLogin, specialDriverOrder, specialDriver);
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
                renderAjaxSuccess("抢单成功！");
            } else {
                renderAjaxError("抢单失败！已被人抢走了！");
            }
        }
    }
    
    public synchronized boolean dealorder(final Order order, final OrderLog orderLog, final MemberLogin memberLogin, final SpecialDriverOrder specialDriverOrder, final SpecialDriver specialDriver) {
        Order Tmp = Order.dao.findById(order.getId());
        if (Tmp.getStatus() == Constant.OrderStatus.ACCEPT) {
            renderAjaxError("订单已经被别人抢走了。。。。。");
            return false;
        }
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return order.update() && orderLog.save() && memberLogin.update() && specialDriverOrder.save() && specialDriver.update();
            }
        });
    }
    
    
    //开始行程
    public void orderStart() {
        int id = getParaToInt("id", 0);
        int status = getParaToInt("orderStatus");
        if (status == Constant.LineStatusByDriver.NOPASSAGES) {
            renderAjaxError("您还没有乘客，无法出发！");
            return;
        }
        DateTime now = DateTime.now();
        DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
        List lineList = SpecialCar.dao.getOrderIdForAccept(getDriverInfo().getId());
        for (int i = 0; i < lineList.size(); i++) {
            Object object = JSONObject.toJSON(lineList.get(i));
            Map m = (Map) object;
            System.out.println(Integer.parseInt(m.get("id").toString()));
            int orderId = Integer.parseInt(m.get("id").toString());
            final Order order = Order.dao.findById(orderId);
            if (order.getStatus() == Constant.OrderStatus.START) {
                renderAjaxError("您已经开始行程！");
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
                }
                renderAjaxSuccess("开始行程成功");
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
                PushOrderService.getIntance().customerConfig(order);//推送
                //return;
            } else {
                renderAjaxError("开始行程失败");
            }
        }
        renderAjaxSuccess("行程已经开始！");
    }
    
    /**
     * 到达终点开始结算
     */
    public void orderArrived() {
        int Id = getParaToInt("id", 0);
        int payType = getParaToInt("payType", -1);
        int status = getParaToInt("orderStatus", 0);
        if (status == Constant.LineStatusByDriver.NOPASSAGES) {
            renderAjaxError("您还没有出发呢！");
            return;
        }
        DateTime now = DateTime.now();
        List lineList1 = SpecialCar.dao.getOrderIdForNoStart(getDriverInfo().getId());
        List lineList = SpecialCar.dao.getOrderIdForStart(getDriverInfo().getId());
        if (lineList1.size() > 0) {
            renderAjaxError("您还没有出发呢！");
            return;
        }
        for (int i = 0; i < lineList.size(); i++) {
            Object object = JSONObject.toJSON(lineList.get(i));
            Map m = (Map) object;
            int orderId = Integer.parseInt(m.get("id").toString());
            final Order order = Order.dao.findById(orderId);
            if (order == null) {
                renderAjaxError("订单不存在");
                return;
            }
            if (order.getStatus() == Constant.OrderStatus.CANCEL) {
                renderAjaxError("订单已经取消");
                return;
            }
            if (order.getStatus() == Constant.OrderStatus.END || order.getStatus() == Constant.OrderStatus.PAYED) {
                EventKit.post(new PushToPay(order));
                renderAjaxSuccess("订单已经结算成功!");
                return;
            }
            if ((order.getFromType() == Constant.FromType.WECHATTYPE
            || order.getFromType() == Constant.FromType.BUDAN
            || order.getFromType() == Constant.FromType.TELTYPE) && payType != Constant.PayType.Collection) {
                renderAjaxError("非APP订单，只能代付");
                return;
            }
            order.setRealDistance(order.getDistance());
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(getLoginMember().getId());
            OrderLog orderLog = OrderLog.dao.findByOrderAndActionAndLoginId(orderId, Constant.OrderStatus.START, getLoginMember().getId());
            final MemberLogin memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
            order.setStatus(Constant.OrderStatus.END);
            final OrderLog arrivedLog = new OrderLog();//到达终点
            arrivedLog.setAction(Constant.OrderAction.END);
            arrivedLog.setOrderId(orderId);
            arrivedLog.setOperationTime(now.toDate());
            arrivedLog.setRemark("司机已到达终点");
            arrivedLog.setLoginId(driverInfo.getLoginId());
            arrivedLog.setOperater(driverInfo.getPhone());
            order.setLastUpdateTime(now.toDate());
            order.setRemoteFee(new BigDecimal("0"));
            order.setRoadToll(new BigDecimal("0"));
            order.setOtherCharges(new BigDecimal("0"));
            order.setRealPay(order.getAmount());
            final OrderLog payLog = new OrderLog();
            order.setPayChannel(-1);//未设置支付类型
            order.setPayStatus(Constant.PayStatus.NOPAY);
            final OrderTrip orderTrip = OrderTrip.dao.findById(orderId);
            orderTrip.setEndTime(now.toDate());
            memberLogin.setLastUpdateTime(DateTime.now().toDate());
            final int i1 = SpecialDriver.dao.updateline(driverInfo.getId(), Id);
            SpecialDriverOrder specialDriverOrder = new SpecialDriverOrder();
            SpecialDriver specialDriver = SpecialDriver.dao.findByDriverAndOrder(order.getId(), order.getDriver());
            specialDriverOrder = specialDriverOrder.findOver(specialDriver.getId(), order.getId());
            specialDriverOrder.setOver(1);
            final SpecialDriverOrder finalSpecialDriverOrder = specialDriverOrder;
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (order.update() && arrivedLog.save() && orderTrip.update() && i1 > 0 && finalSpecialDriverOrder.update()) {
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
                renderAjaxSuccess("行程结束!");
            } else {
                renderAjaxError("结算失败");
            }
        }
    }
}
