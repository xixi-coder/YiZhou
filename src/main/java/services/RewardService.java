package services;

import base.Constant;
import kits.StringsKit;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.company.Distribution;
import models.driver.DriverInfo;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.order.Order;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2016/10/28.
 */
public class RewardService {
    private Logger logger = LoggerFactory.getLogger(RewardService.class);

    public void rewardall(Order order) {
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        driverReward(driverInfo, order);
        memberReward(memberInfo, order);
    }

    /**
     * 司机的三级分销
     *
     * @param driverInfo
     */
    public void driverReward(DriverInfo driverInfo, Order order1){
        BigDecimal amount = order1.getRealPay();
        Distribution distribution = Distribution.dao.findByCompanyAndStyle(driverInfo.getCompany(), 2,order1.getServiceType());//查询司机的分销方式
        if (distribution != null){
            if (distribution.getType() == 1) {//一次性的
                List<Order> order = Order.dao.findByDriverComplete(driverInfo.getId());
                if (order != null && order.size() >= 1) {
                    return;
                }
            }
            int level = distribution.getLevel();
            int tuijianren = 0;
            if (driverInfo.getIntroducerLoginId() != null) {
                tuijianren = driverInfo.getIntroducerLoginId();
            }
            DriverInfo tuijianrenInfo = null;
            if (level == 1) {
                tuijianrenInfo = DriverInfo.dao.findByLoginId(tuijianren);
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardDCal(money, tuijianrenInfo, driverInfo);
            } else if (level == 2) {
                tuijianrenInfo = DriverInfo.dao.findByLoginId(tuijianren);
                if (tuijianrenInfo == null)
                    return;
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardDCal(money, tuijianrenInfo, driverInfo);
                int tmpId = 0;
                if (tuijianrenInfo.getIntroducerLoginId() != null) {
                    tmpId = tuijianrenInfo.getIntroducerLoginId();
                }
                DriverInfo tmpTuijianrenInfo = DriverInfo.dao.findByLoginId(tmpId);
                if (tmpTuijianrenInfo == null)
                    return;
                money = distribution.getSecondLevel();
                if (distribution.getSecondType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardDCal(money, tmpTuijianrenInfo, tuijianrenInfo);
            } else if (level == 3) {
                tuijianrenInfo = DriverInfo.dao.findByLoginId(tuijianren);
                if (tuijianrenInfo == null)
                    return;
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardDCal(money, tuijianrenInfo, driverInfo);
                int tmpId = 0;
                if (tuijianrenInfo.getIntroducerLoginId() != null) {
                    tmpId = tuijianrenInfo.getIntroducerLoginId();
                }
                DriverInfo tmpTuijianrenInfo = DriverInfo.dao.findByLoginId(tmpId);
                if (tmpTuijianrenInfo == null)
                    return;
                money = distribution.getSecondLevel();
                if (distribution.getSecondType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardDCal(money, tmpTuijianrenInfo, tuijianrenInfo);//第二个
                money = distribution.getThirdLevel();
                if (distribution.getThirdType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                int ttmpId = 0;
                if (tmpTuijianrenInfo.getIntroducerLoginId() != null) {
                    ttmpId = tmpTuijianrenInfo.getIntroducerLoginId();
                }
                tuijianrenInfo = DriverInfo.dao.findByLoginId(ttmpId);
                if (tuijianrenInfo == null)
                    return;
                rewardDCal(money, tuijianrenInfo, tmpTuijianrenInfo);//第三个
            }
        }
    }

    /**
     * 会员三级分销
     *
     * @param memberInfo
     */
    public void memberReward(MemberInfo memberInfo, Order order1) {
        BigDecimal amount = order1.getRealPay();
        Distribution distribution = Distribution.dao.findByCompanyAndStyle(memberInfo.getCompany(), 1,order1.getServiceType());//查询司机的分销方式
        if (distribution != null){
            if (distribution.getType() == 1) {//一次性的
                List<Order> order = Order.dao.findByMemberComplete(memberInfo.getId());
                if (order != null && order.size() > 1) {
                    return;
                }
            }
            int level = distribution.getLevel();
            if (memberInfo.getIntroducerLoginId() == null)
                return;
            int tuijianren = memberInfo.getIntroducerLoginId();
            MemberInfo tuijianrenInfo = null;
            if (level == 1) {
                tuijianrenInfo = MemberInfo.dao.findByLoginId(tuijianren);
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardMCal(money, tuijianrenInfo, memberInfo);
            } else if (level == 2) {
                tuijianrenInfo = MemberInfo.dao.findByLoginId(tuijianren);
                if (tuijianrenInfo == null)
                    return;
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardMCal(money, tuijianrenInfo, memberInfo);
                int tmpId = 0;
                if (tuijianrenInfo.getIntroducerLoginId() != null) {
                    tmpId = tuijianrenInfo.getIntroducerLoginId();
                }
                MemberInfo tmpTuijianrenInfo = MemberInfo.dao.findByLoginId(tmpId);
                if (tmpTuijianrenInfo == null)
                    return;
                money = distribution.getSecondLevel();
                if (distribution.getSecondType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardMCal(money, tmpTuijianrenInfo, tuijianrenInfo);
            } else if (level == 3) {
                tuijianrenInfo = MemberInfo.dao.findByLoginId(tuijianren);
                if (tuijianrenInfo == null)
                    return;
                BigDecimal money = distribution.getFirstLevel();
                if (distribution.getFirstType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardMCal(money, tuijianrenInfo, memberInfo);
                int tmpId = 0;
                if (tuijianrenInfo.getIntroducerLoginId() != null) {
                    tmpId = tuijianrenInfo.getIntroducerLoginId();
                }
                MemberInfo tmpTuijianrenInfo = MemberInfo.dao.findByLoginId(tmpId);
                if (tmpTuijianrenInfo == null) {
                    return;
                }
                money = distribution.getSecondLevel();
                if (distribution.getSecondType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                rewardMCal(money, tmpTuijianrenInfo, tuijianrenInfo);//第二个
                money = distribution.getThirdLevel();
                if (distribution.getThirdType() == 1) {//比例
                    money = amount.multiply(money).divide(BigDecimal.valueOf(100), 2);
                }
                int ttmpId = 0;
                if (tmpTuijianrenInfo.getIntroducerLoginId() != null) {
                    ttmpId = tmpTuijianrenInfo.getIntroducerLoginId();
                }
                tuijianrenInfo = MemberInfo.dao.findByLoginId(ttmpId);
                if (tuijianrenInfo == null)
                    return;
                rewardMCal(money, tuijianrenInfo, tmpTuijianrenInfo);//第三个
            }
        }
    }

    /**
     * 给推荐司机给司机增加奖励
     *
     * @param money
     * @param driverInfo
     */
    public void rewardDCal(BigDecimal money, DriverInfo tuijianren, DriverInfo driverInfo) {
        if (tuijianren == null) {
            return;
        }
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(tuijianren.getLoginId());
        int l = memberCapitalAccount.getLoginId();
        DriverInfo dd = DriverInfo.dao.findByLoginId(l);
        CapitalLog capitalLog = new CapitalLog();
        capitalLog.setLoginId(l);
        capitalLog.setNo(StringsKit.getCaptDriver());
        capitalLog.setCreateTime(DateTime.now().toDate());
        capitalLog.setAmount(money);
        capitalLog.setRemark("推荐司机:" + driverInfo.getPhone() + "获得奖励");
        capitalLog.setOperater(dd.getId());
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        capitalLog.setOperationType(Constant.CapitalOperationType.TUIJIANSIJIJIANGLI);
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        memberCapitalAccount.setAmount(historyAmount.add(money));
        memberCapitalAccount = memberCapitalAccount.calTxAmount(memberCapitalAccount, money);
        CompanyAccount account = CompanyAccount.dao.findById(tuijianren.getCompany());
        if (CompanyAccount.dao.amountEnough(account, money,Constant.CompanyAccountActivity.rewardDCal)) {
            MemberCapitalAccount.dao.saveAccountAndLog(memberCapitalAccount, capitalLog);
            CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.SANJIFENXIAO, money, tuijianren.getCompany(), l, Constant.DataAuditStatus.AUDITOK, 0, "三级分销奖励服务人员消耗");
            companyActivity.save();
        } else {
            logger.info("公司id：{}的公司活动金额不足", tuijianren.getCompany());
        }

    }

    /**
     * 给推荐会员增加奖励
     *
     * @param money
     * @param memberInfo
     */
    public void rewardMCal(BigDecimal money, MemberInfo tuijianren, MemberInfo memberInfo) {
        if (tuijianren == null) {
            return;
        }
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(tuijianren.getLoginId());
        int l = memberCapitalAccount.getLoginId();
        MemberInfo dd = MemberInfo.dao.findByLoginId(l);
        CapitalLog capitalLog = new CapitalLog();
        capitalLog.setLoginId(l);
        capitalLog.setNo(StringsKit.getCaptDriver());
        capitalLog.setCreateTime(DateTime.now().toDate());
        capitalLog.setAmount(money);
        capitalLog.setRemark("推荐客户" + memberInfo.getPhone() + "获得奖励");
        capitalLog.setOperater(dd.getId());
        capitalLog.setOperationType(Constant.CapitalOperationType.TUIJIANGHUIYUANJIANGLI);
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        memberCapitalAccount.setAmount(historyAmount.add(money));
        memberCapitalAccount = memberCapitalAccount.calTxAmount(memberCapitalAccount, money);
        CompanyAccount companyAccount = CompanyAccount.dao.findById(tuijianren.getCompany());
        if (CompanyAccount.dao.amountEnough(companyAccount, money,Constant.CompanyAccountActivity.rewardMCal)) {
            MemberCapitalAccount.dao.saveAccountAndLog(memberCapitalAccount, capitalLog);
            CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.SANJIFENXIAO, money, tuijianren.getCompany(), l, Constant.DataAuditStatus.AUDITOK, 0, "三级分销奖励客户消耗");
            companyActivity.save();
        } else {
            logger.info("公司id：{}的公司活动金额不足", tuijianren.getCompany());
        }
    }

    private RewardService() {
    }

    private static class RewardServiceHolder {
        static RewardService instance = new RewardService();
    }

    public static RewardService getInstance() {
        return RewardServiceHolder.instance;
    }
}
