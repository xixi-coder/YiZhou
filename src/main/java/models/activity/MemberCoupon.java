package models.activity;

import annotation.TableBind;
import base.Constant;
import base.models.BaseMemberCoupon;
import models.order.Order;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
@TableBind(tableName = "dele_member_coupon")
public class MemberCoupon extends BaseMemberCoupon<MemberCoupon> {
    public static MemberCoupon dao = new MemberCoupon();

    public List<MemberCoupon> findByCouponId(int couponId) {
        return find(SqlManager.sql("memberCoupon.findByCouponId"), couponId);
    }

    /**
     * 查询一张未使用的优惠价
     */
    public MemberCoupon findByCouponIdOne(int couponId) {
        return findFirst(SqlManager.sql("memberCoupon.findByCouponIdOne"), couponId, Constant.DataAuditStatus.CREATE);
    }

    /**
     * 查询我的优惠券
     *
     * @param id
     * @return
     */
    public List<MemberCoupon> findByLoginId(Integer id, Date date) {
        return find(SqlManager.sql("memberCoupon.findByLoginId"), id, Constant.CouponStatus.USEFUL, date);
    }

    /**
     * 查询订单可用优惠券
     *
     * @return
     */
    public List<MemberCoupon> getCouponForPay(Order order,int loginId) {
        return find(SqlManager.sql("memberCoupon.getCouponForPay"),loginId,Constant.CouponStatus.USEFUL,order.getRealPay(),order.getServiceType(),DateTime.now().toDate(),loginId,Constant.CouponStatus.USEFUL,order.getRealPay(),Constant.ServiceType.AllService,DateTime.now().toDate());
    }

    /**
     * 他兑换优惠券
     *
     * @param code
     * @param date
     * @return
     */
    public MemberCoupon findByCodeAndDate(String code, Date date) {
        return findFirst(SqlManager.sql("memberCoupon.findByCodeAndDate"), code, date);
    }

    /**
     * 创建优惠券
     *
     * @param coupon
     * @param source
     */
    public MemberCoupon create(Coupon coupon, String source, int logindId) {
        String password = RandomStringUtils.randomNumeric(6);
        String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setCouponId(coupon.getId());
        memberCoupon.setLoginId(logindId);
        memberCoupon.setStartTime(coupon.getStartTime());
        memberCoupon.setEndTime(coupon.getEndTime());
        memberCoupon.setTitle(coupon.getCouponTitle());
        memberCoupon.setDescription(coupon.getCouponDesc());
        memberCoupon.setAmount(coupon.getAmount());
        memberCoupon.setCouponSource(source);
        memberCoupon.setGainTime(DateTime.now().toDate());
        memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
        memberCoupon.setNo(no);
        memberCoupon.setPassword(password);
        return memberCoupon;
    }

    /**
     * 查询过期的
     *
     * @param start
     * @param end
     * @return
     */
    public List<MemberCoupon> findByDate(Date start, Date end) {
        return find(SqlManager.sql("memberCoupon.findByDate"), start, end, Constant.DataAuditStatus.AUDITOK);
    }


    /**
     * 查询过期的未使用的
     *
     * @param start
     * @param end
     * @return
     */
    public List<MemberCoupon> findByDate2(Date start, Date end) {
        return find(SqlManager.sql("memberCoupon.findByDate2"), start, end, Constant.DataAuditStatus.CREATE);
    }

}
