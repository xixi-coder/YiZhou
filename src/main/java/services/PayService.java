package services;

import base.Constant;
import jobs.activity.OrderActivity;
import kits.SmsKit;
import models.activity.MemberCoupon;
import models.company.CompanyActivity;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import net.dreamlu.event.EventKit;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PayService {
    private Logger logger = LoggerFactory.getLogger(PayService.class);

    public void pay(Order order) {
        Date now = DateTime.now().toDate();
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        OrderLog orderLog = new OrderLog();
        orderLog.setAction(Constant.OrderAction.PAYED);
        orderLog.setOperationTime(now);
        orderLog.setLoginId(memberInfo.getLoginId());
        orderLog.setRemark("乘客支付了订单");
        orderLog.setOrderId(order.getId());
        orderLog.save();
        CalCommissionService.getInstance().commission(order);
        OrderLog createLog = OrderLog.dao.findByOrderAndPayAction(order.getId(), Constant.OrderAction.CREATE);
        int minutes = 0;
        if (createLog == null) {
            minutes = Minutes.minutesBetween(new DateTime(createLog.getOperationTime()), new DateTime(now)).getMinutes();
        }
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
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
        memberLogin.setLastUpdateTime(DateTime.now().toDate());
        memberLogin.update();
        logger.info("乘客支付订单了时间:{},支付金额:{}", DateTime.now().toString());
        order.setConsumeTime(BigDecimal.valueOf(minutes));
        order.setPayStatus(Constant.OrderStatus.PAYED);
        order.setStatus(Constant.PayStatus.PAYED);
        order.setPayTime(now);
        order.update();
        if (order.getCoupon() != null) {
            MemberCoupon memberCoupon = MemberCoupon.dao.findById(order.getCoupon());//更新优惠券信息
            memberCoupon.setStatus(Constant.CouponStatus.USED);
            memberCoupon.setUseTime(DateTime.now().toDate());
            memberCoupon.update();
            CompanyActivity companyActivity = CompanyActivity.dao.findByCouponId(memberCoupon.getId());
            companyActivity.setStatus(Constant.DataAuditStatus.AUDITOK);
            companyActivity.update();
        }
        SmsKit.ordercomplete(order);//发送短信
        EventKit.post(new OrderActivity(order));//参加活动
    }


    private PayService() {
    }

    private static class PayServiceHolder {
        static PayService instance = new PayService();
    }

    public static PayService getInstance() {
        return PayServiceHolder.instance;
    }
}