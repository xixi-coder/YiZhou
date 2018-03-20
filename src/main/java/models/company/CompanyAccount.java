package models.company;

import annotation.TableBind;
import base.Constant;
import base.models.BaseCompanyAccount;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import kits.StringsKit;
import models.order.Order;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created by admin on 2016/10/24.
 */
@TableBind(tableName = "dele_company_account", pks = "company")
public class CompanyAccount extends BaseCompanyAccount<CompanyAccount> {
    public static CompanyAccount dao = new CompanyAccount();




    /**
     * 判断资金是否足够并扣除
     *
     * @param companyAccount
     * @param amount
     * @return
     */
    public synchronized boolean amountEnough(final CompanyAccount companyAccount, BigDecimal amount ,int type) {
        BigDecimal historyAmount = companyAccount.getActivityAmount() == null ? BigDecimal.ZERO : companyAccount.getActivityAmount();
        boolean isOk;
        if (historyAmount.subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
            final CompanyAccountLog companyAccountLog = new CompanyAccountLog();
            companyAccountLog.setAmount(amount);
            companyAccountLog.setCompany(companyAccount.getCompany());
            String remark;
            if (type == Constant.CompanyAccountActivity.createcoupon){
                remark = "活动部分,扣除公司活动金额："+amount+"; 公司原活动金额："+historyAmount+",剩余活动金额:"+historyAmount.subtract(amount);
            }else if (type == Constant.CompanyAccountActivity.awardMember){
                remark = "优惠券，奖励给会员,扣除公司活动金额："+amount+"; 公司原活动金额："+historyAmount+",剩余活动金额:"+historyAmount.subtract(amount);
            }else if (type == Constant.CompanyAccountActivity.awardDriver){
                remark = "优惠券，奖励给司机,扣除公司活动金额："+amount+"; 公司原活动金额："+historyAmount+",剩余活动金额:"+historyAmount.subtract(amount);
            }else if (type == Constant.CompanyAccountActivity.rewardMCal){
                remark = "会员推荐分销活动,扣除公司活动金额："+amount+"; 公司原活动金额："+historyAmount+",剩余活动金额:"+historyAmount.subtract(amount);
            }else if (type == Constant.CompanyAccountActivity.rewardDCal){
                remark = "司机推荐分销活动,扣除公司活动金额："+amount+"; 公司原活动金额："+historyAmount+",剩余活动金额:"+historyAmount.subtract(amount);
            }else {
                remark = "未知类型";
            }
            companyAccountLog.setRemark(remark);
            companyAccountLog.setOpeartType(Constant.CompanyOperteType.HDJEZC);
            companyAccountLog.setServiceType(-1);//活动金额支出标记
            companyAccount.setActivityAmount(historyAmount.subtract(amount));
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return companyAccountLog.save()&&companyAccount.update();
                }
            });
            return isOk;
        } else {
            return false;
        }
    }

    public synchronized boolean amountEnough2(CompanyAccount companyAccount, BigDecimal amount) {
        BigDecimal historyAmount = companyAccount.getActivityAmount() == null ? BigDecimal.ZERO : companyAccount.getActivityAmount();
        if (historyAmount.subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void addActivityAmount(int companyId, BigDecimal amout) {
        CompanyAccount companyAccount = CompanyAccount.dao.findById(companyId);
        BigDecimal historyAmount = companyAccount.getActivityAmount() == null ? BigDecimal.ZERO : companyAccount.getActivityAmount();
        companyAccount.setActivityAmount(historyAmount.add(amout));
        companyAccount.update();
    }
}
