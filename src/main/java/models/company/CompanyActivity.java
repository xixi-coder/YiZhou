package models.company;

import annotation.TableBind;
import base.models.BaseCompanyActivity;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;

/**
 * Created by admin on 2016/12/25.
 */
@TableBind(tableName = "dele_company_activity")
public class CompanyActivity extends BaseCompanyActivity<CompanyActivity> {
    public static CompanyActivity dao = new CompanyActivity();

    public CompanyActivity creat(int type, BigDecimal amount, int company, int loginId, int status, int couponId, String remark) {
        CompanyActivity companyActivity = new CompanyActivity();
        companyActivity.setType(type);
        companyActivity.setAmount(amount);
        companyActivity.setCompany(company);
        companyActivity.setLoginId(loginId);
        companyActivity.setStatus(status);
        companyActivity.setCouponId(couponId);
        companyActivity.setRemark(remark);
        companyActivity.setCreateTime(DateTime.now().toDate());
        return companyActivity;
    }

    public CompanyActivity creatPercent(int type, int company, int loginId, int status, int couponId, String remark) {
        CompanyActivity companyActivity = new CompanyActivity();
        companyActivity.setType(type);
        companyActivity.setCompany(company);
        companyActivity.setLoginId(loginId);
        companyActivity.setStatus(status);
        companyActivity.setCouponId(couponId);
        companyActivity.setRemark(remark);
        companyActivity.setCreateTime(DateTime.now().toDate());
        return companyActivity;
    }

    /**
     * 获取公司活动优惠券
     * @param id
     * @return
     */
    public CompanyActivity findByCouponId(Integer id) {
        return findFirst(SqlManager.sql("companyActivity.findByCouponId"),id);
    }
}
