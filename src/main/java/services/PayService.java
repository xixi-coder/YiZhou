package services;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import net.dreamlu.event.EventKit;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

public class PayService {
    private Logger logger = LoggerFactory.getLogger(PayService.class);

    public void pay(final Order order) {
        Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                Date now = DateTime.now().toDate();
                MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
                OrderLog orderLog = new OrderLog();
                orderLog.setAction(Constant.OrderAction.PAYED);
                orderLog.setOperationTime(now);
                orderLog.setLoginId(memberInfo.getLoginId());
                orderLog.setRemark("乘客支付了订单");
                orderLog.setOrderId(order.getId());

                if (!orderLog.save()) {
                    throw new RuntimeException("订单日志信息保存失败，直接回滚！！");
                }
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
                if (!memberLogin.update()) {
                    throw new RuntimeException("用户登陆信息保存失败，直接回滚！！");
                }
                logger.info("乘客支付订单了时间:{},支付金额:{}", DateTime.now().toString());
                order.setConsumeTime(BigDecimal.valueOf(minutes));
                order.setPayStatus(Constant.OrderStatus.PAYED);
                order.setStatus(Constant.PayStatus.PAYED);
                order.setPayTime(now);
                if (!order.update()) {
                    throw new RuntimeException("订单信息保存失败，直接回滚！！");
                }
                if (order.getCoupon() != null) {
                    MemberCoupon memberCoupon = MemberCoupon.dao.findById(order.getCoupon());//更新优惠券信息
                    memberCoupon.setStatus(Constant.CouponStatus.USED);
                    memberCoupon.setUseTime(DateTime.now().toDate());
                    if (!memberCoupon.update()) {
                        throw new RuntimeException("用户优惠卷信息保存失败，直接回滚！！");
                    }
                    CompanyActivity companyActivity = CompanyActivity.dao.findByCouponId(memberCoupon.getId());
                    companyActivity.setStatus(Constant.DataAuditStatus.AUDITOK);
                    if (!companyActivity.update()) {
                        throw new RuntimeException("账户信息保存失败，直接回滚！！");
                    }
                }
                SmsKit.ordercomplete(order);//发送短信
                EventKit.post(new OrderActivity(order));//参加活动
                //给司机和乘客添加订单统计
                OrderService.countCompleteOrder(order);
                return true;
            }
        });

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