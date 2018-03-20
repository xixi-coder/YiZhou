package services;

import base.Constant;
import kits.StringsKit;
import models.activity.MemberCoupon;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2016/10/29.
 */
public class GiveCouponService {

    public void giveCouponByMoney(BigDecimal money) {

    }


    /**
     * 根据有效期和总金额和每张金额生成优惠券
     * @param money
     * @param days
     * @param eachMoney
     * @param loginId
     * @param fromType
     */
    public void giveCouponByMoneyAndTerm(BigDecimal money, int days, BigDecimal eachMoney, int loginId, String fromType) {
        DateTime now = DateTime.now();
        Date start = now.toDate();
        Date end = now.plusDays(days).toDate();
        BigDecimal[] result = money.divideAndRemainder(eachMoney);
        for (int i = 0; i < result[0].intValue(); i++) {
            createCoupon(loginId, money, start, end, fromType);
        }
        createCoupon(loginId,result[1],start,end,fromType);
    }

    public void createCoupon(int loginId, BigDecimal money, Date start, Date end, String fromType) {//创建优惠券
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setAmount(money);
        memberCoupon.setStartTime(start);
        memberCoupon.setEndTime(end);
        memberCoupon.setLoginId(loginId);
        memberCoupon.setDescription("后台自动赠送的优惠券");
        memberCoupon.setGainTime(DateTime.now().toDate());
        memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
        memberCoupon.setTitle(money.toString() + "优惠券");
        memberCoupon.setCouponSource(fromType);
        memberCoupon.setNo(StringsKit.getCouponNo());
        memberCoupon.save();
    }


    private GiveCouponService() {

    }

    private static class GiveCouponServiceHolder {
        static GiveCouponService instance = new GiveCouponService();
    }

    public static GiveCouponService getInstance() {
        return GiveCouponServiceHolder.instance;
    }
}
