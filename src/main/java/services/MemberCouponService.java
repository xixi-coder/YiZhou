package services;

import base.Constant;
import models.activity.MemberCoupon;
import models.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MemberCouponService {
    private Logger logger = LoggerFactory.getLogger(MemberCouponService.class);


    public void PayWithCoupon(Order order,int coupon){
        MemberCoupon memberCoupon = MemberCoupon.dao.findById(coupon);
        if (coupon != 0 && memberCoupon == null) {
//            renderAjaxError("优惠券不存在");
            return;
        } else if (memberCoupon != null) {
            if (memberCoupon.getStatus() == Constant.CouponStatus.DISABLE || memberCoupon.getStatus() == Constant.CouponStatus.USED) {
//                renderAjaxError("优惠券不可用");
                return;
            }
        }
        if (memberCoupon != null) {
            BigDecimal realPay = order.getRealPay().subtract(memberCoupon.getAmount());
            if (realPay.compareTo(BigDecimal.ZERO) < 0) {
                order.setYhAmount(order.getRealPay());
                order.setRealPay(BigDecimal.ZERO);
            } else {
                order.setRealPay(order.getRealPay().subtract(memberCoupon.getAmount()));
                order.setYhAmount(memberCoupon.getAmount());
            }
            order.setCoupon(coupon);
        }
    }

    private MemberCouponService() {
    }

    private static class CalServiceHolder {
        static MemberCouponService instance = new MemberCouponService();
    }

    public static MemberCouponService getInstance() {
        return CalServiceHolder.instance;
    }
}