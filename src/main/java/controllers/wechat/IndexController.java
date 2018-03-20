package controllers.wechat;

import base.Constant;
import base.controller.BaseController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.ehcache.CacheKit;
import com.xiaoleilu.hutool.date.DateUtil;
import jobs.pushorder.CancelOrder;
import jobs.pushorder.PushOrder;
import kits.SmsKit;
import kits.VerificationKit;
import models.activity.MemberCoupon;
import models.company.Ad;
import models.company.Company;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.sys.ServiceTypeItem;
import models.sys.SmsLog;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import services.SmsHttpService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by admin on 2016/10/13.
 */
@annotation.Controller("/wechat")
public class IndexController extends BaseController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    
    public void index() {
    
    }
    
    //获取司机信息
    public void nearby() {
        String lon = getPara("lon");
        String lat = getPara("lat");
        String cityCode = getPara("city_code");
        Company company = Company.dao.findByCity(cityCode);
        DriverInfo driverInfo = DriverInfo.dao.findByCountByLocationAndCompany(lon, lat, company.getId(), Constant.ServiceType.DaiJia);
        renderAjaxSuccess(driverInfo);
    }
    
    //获取微信广告
    public void ads() {
        String cityCode = getPara("city_code");
        Company company = Company.dao.findByCity(cityCode);
        List<Ad> ads;
        if (company != null) {
            ads = Ad.dao.findByCompanyAndType(company.getId(), 3, 3);
            renderAjaxSuccess(ads);
        } else {
            ads = Ad.dao.findByCompanyAndType(2, 3, 3);
            renderAjaxSuccess(ads);
        }
    }
    
    public void create() {
        final Order order = getModel(Order.class, "order");
        order.setDistance(BigDecimal.ZERO);
        final MemberInfo memberInfo;
        final MemberLogin memberLogin;
        final String phone = getPara("phone");
        final String smsCode = getPara("security");//验证码
        final String old = getPara("old");
        
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxError("联系方式不能为空");
            return;
        }
        //验证码
        if (old.equals("0")) {
            if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null
            || !StringUtils.equals(smsCode, CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString())) {
                renderAjaxError("请填写正确的验证码");
                return;
            }
        }
        
        if (MemberInfo.dao.findByPhone(getPara("phone")) == null) {
            memberInfo = new MemberInfo();
            memberInfo.setPhone(getPara("phone"));
            memberInfo.setRealName("先生/女士");
            memberLogin = new MemberLogin();
            memberLogin.setUserName(memberInfo.getPhone());
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setType(Constant.MEMBER);
            memberLogin.setCreateTime(DateTime.now().toDate());
        } else {
            memberLogin = null;
            memberInfo = MemberInfo.dao.findByPhone(getPara("phone"));
        }
        final OrderTrip orderTrip = getModel(OrderTrip.class, "trip");
        if (memberInfo.getPhone() == null) {
            renderAjaxError("客户手机不能为空!");
            return;
        }
        if (orderTrip.getStartLongitude() == null ||
        orderTrip.getStartLatitude() == null
        ) {
            renderAjaxError("订单的开始没有选择");
            return;
        }
        /*if (order.getYgAmount() == null) {
            renderAjaxError("预估价格不能为空！");
            return;
        }
        if (order.getSetouttime() == null) {
            renderAjaxError("预约时间不能为空！");
            return;
        }*/
        List<Order> lists = Order.dao.findByPhone(phone);
        if (lists.size() > 0) {
            renderAjaxSuccess(lists.get(0).getId(), "您有未完成订单！请点击行程信息查看");
            return;
        }
        
        final int companyId;
        String cityCode = getPara("city_code");
        Company company = Company.dao.findByCity(cityCode);
        companyId = company.getId();
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (memberInfo.getId() == null) {
                    memberInfo.setCompany(companyId);
                    if (!memberLogin.save()) {
                        return false;
                    }
                    MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
                    memberCapitalAccount.setLoginId(memberLogin.getId());
                    memberCapitalAccount.setAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setPhone(memberInfo.getPhone());
                    memberCapitalAccount.setAccount(memberInfo.getPhone());
                    memberCapitalAccount.setTxAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setCreateTime(DateTime.now().toDate());
                    memberInfo.setLoginId(memberLogin.getId());
                    if (!memberInfo.save() || !memberCapitalAccount.save()) {
                        return false;
                    }
                }
                String no = Constant.AliPayOrderType.NomorlOrder + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(6).toUpperCase();
                order.setNo(no);
                order.setCreateTime(DateTime.now().toDate());
                order.setSetouttime(DateTime.now().toDate());
                order.setSetOutFlag(false);
                order.setMember(memberInfo.getId());
                order.setPhone(memberInfo.getPhone());
                order.setPdFlag(false);
                order.setRealDistance(BigDecimal.ZERO);
                order.setStatus(Constant.OrderStatus.CREATE);
                order.setServiceType(Constant.ServiceType.DaiJia);
                List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.DaiJia);
                order.setType(serviceTypeItems.get(0).getId());
                order.setFromType(Constant.FromType.WECHATTYPE);
                order.setFromCompany(companyId);
                order.setCompany(companyId);
                OrderLog orderLog = new OrderLog();
                orderLog.setAction(Constant.OrderAction.CREATE);
                //orderLog.setOperater();
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setRemark("微信公众号创建了订单");
                orderLog.setLoginId(memberInfo.getLoginId());
                if (!order.save()) {
                    return false;
                }
                orderTrip.setOrderId(order.getId());
                orderLog.setOrderId(order.getId());
                return orderLog.save() && orderTrip.save();
            }
        });
        if (isOk) {
            try {
                EventKit.post(new PushOrder(order));
            } catch (Exception e) {
                logger.error("出现错误，错误信息{}", e.getMessage());
            }
            renderAjaxSuccess(order.getId(), "正在等待司机接单！五分钟后将无法取消");
        } else {
            renderAjaxError("下单失败！");
        }
    }
    
    /**
     * 微信下单发送验证码
     */
    public void smsCode() {
        String phone = getPara("phone");
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxError("联系方式不能为空");
        }
        if (VerificationKit.isMobile(phone)) {
            if (SmsKit.weinxin(phone)) {
                renderAjaxSuccess("发送成功");
            } else {
                renderAjaxError("短信发送失败");
            }
        } else {
            renderAjaxFailure("手机号码格式错误");
        }
    }
    
    /*
    取消订单
     */
    public void undo() {
        final String orderId = getPara("orderId");
        if (Strings.isNullOrEmpty(orderId)) {
            renderAjaxError("参数有误，请重试！！");
            return;
        }
        final Order order = Order.dao.findById(orderId);
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.CANCEL) {
            renderAjaxError("订单已取消，无需重复操作！！");
            return;
        } else if (order.getStatus() == Constant.OrderStatus.START && order != null) {
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
            Date sTime = orderLog1.getOperationTime();
            Date eTime = DateTime.now().toDate();
            if ((eTime.getTime() - sTime.getTime()) > Constant.ORDER_UNDO_TIMEOUT) {
                renderAjaxError("订单已超时，无法取消！");
                return;
            }
            EventKit.post(new CancelOrder(order));
        }
        
        OrderLog orderLog = new OrderLog();
        orderLog.setOperationTime(DateTime.now().toDate());
        orderLog.setOrderId(order.getId());
        orderLog.setRemark(order.getPhone() + "(乘客)取消了订单");
        orderLog.setAction(Constant.OrderAction.CANCEL);
        orderLog.setLoginId(-2);//-2为微信叫车
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
        }
        renderAjaxSuccess("取消订单成功！");
        
        
    }
    
    /**
     * 行程日志
     */
    public void lockup() {
        final int orderId = getParaToInt("orderId", 0);
        if (orderId == 0) {
            renderAjaxError("参数有误，请重试！！");
            return;
        }
        List<OrderLog> allOrderLogs = OrderLog.dao.findByOrder(orderId);
        Order order = Order.dao.findById(orderId);
        for (OrderLog orderLog : allOrderLogs) {
            
            orderLog.put("time", DateUtil.format(orderLog.getOperationTime(), "HH:mm"));
            if (orderLog.getAction() == 2) {
                DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
                DriverRealLocation location = DriverRealLocation.dao.findByDriver(order.getDriver());
                orderLog.put("driverName", driverInfo.getNickName());
                orderLog.put("phone", driverInfo.getPhone());
                orderLog.put("lon", location.getLongitude());
                orderLog.put("lat", location.getLatitude());
            }
        }
        renderAjaxSuccess(removeDuplicateWithOrder(allOrderLogs));
    }
    
    // 删除ArrayList中重复元素，保持顺序 
    public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        return list;
    }
    
}
