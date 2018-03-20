package services;

import base.Constant;
import models.activity.MemberCoupon;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */
public class RecoveryCouponService {
    public synchronized void recovery() {
        Date start = DateTime.now().plusDays(-1).millisOfDay().withMinimumValue().toDate();
        Date end = DateTime.now().plusDays(-1).millisOfDay().withMaximumValue().toDate();
        List<MemberCoupon> memberCoupons = MemberCoupon.dao.findByDate2(start, end);
        for (MemberCoupon memberCoupon : memberCoupons) {
            memberCoupon.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            CompanyActivity companyActivity = CompanyActivity.dao.findByCouponId(memberCoupon.getId());
            if (companyActivity == null) {
                continue;
            }
            companyActivity.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            memberCoupon.update();
            companyActivity.update();
            CompanyAccount.dao.addActivityAmount(companyActivity.getCompany(), companyActivity.getAmount());
        }
    }

    private RecoveryCouponService() {
    }

    private static class RecoveryCouponServiceHolder {
        static RecoveryCouponService instance = new RecoveryCouponService();
    }

    public static RecoveryCouponService getInstance() {
        return RecoveryCouponServiceHolder.instance;
    }
}
