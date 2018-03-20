package services;

import base.Constant;
import kits.SmsKit;
import models.company.CompanyAccount;
import models.company.CompanyAccountLog;
import models.company.CompanyRebate;
import models.company.CompanyService;
import models.driver.DriverInfo;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.order.Order;
import models.order.OrderDetail;
import models.royalty.RoyaltyStandard;
import models.royalty.RoyaltyStandardEasy;
import models.royalty.RoyaltyStandardMoney;
import models.royalty.RoyaltyStandardMoneyItem;
import models.sys.AdminSetting;
import models.sys.ServiceTypeItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CalCommissionService {
    private Logger logger = LoggerFactory.getLogger(CalCommissionService.class);

    /**
     * @param order
     * @return
     */
    public boolean commission(Order order) {//使用余额支付是用这个方式算提成
        BigDecimal amount = commissionAmount(order);//计算出需要提成的金额
        RoyaltyStandard royaltyStandard = getRoyaltyStandard(order);
        BigDecimal royaltAmount;
        if (royaltyStandard.getType() == 1) {
            royaltAmount = simpleCommission(royaltyStandard, amount, order);
        } else {
            royaltAmount = moneyCommission(royaltyStandard, amount, order);
        }
        operaterCompany(order, royaltAmount, royaltyStandard);
        operaterDriver(order, royaltAmount, royaltyStandard);
        return false;
    }


    /**
     * 使用代收调用的是这个接口
     *
     * @param order
     * @return
     */
    public boolean daishoucommission(Order order) {//使用代收用这个方式算提成
        BigDecimal amount = commissionAmount(order);//计算出需要提成的金额
        RoyaltyStandard royaltyStandard = getRoyaltyStandard(order);
        BigDecimal royaltAmount;
        BigDecimal insuranceAmount = AdminSetting.dao.finInsuranceByServiceType(order.getType(), order.getCompany());
        if (royaltyStandard.getType() == 1) {
            royaltAmount = simpleCommission(royaltyStandard, amount, order);
        } else {
            royaltAmount = moneyCommission(royaltyStandard, amount, order);
        }
        operaterCompany(order, royaltAmount, royaltyStandard);

        logger.info("给司机扣除提成操作司机id{},扣除金额{}", order.getDriver(), royaltAmount.toString());
        DateTime now = DateTime.now();
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(driverInfo.getLoginId());
        AdminSetting adminSetting;
        try {
            adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
        } catch (Exception e) {
            adminSetting = AdminSetting.dao.findFirst();
        }

        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        if (order.getServiceType() != Constant.ServiceType.DaiJia) {
            memberCapitalAccount.setAmount(historyAmount.subtract(royaltAmount));//从余额扣除司机的提成部分
            memberCapitalAccount.setLastUpdateAmount(royaltAmount.multiply(BigDecimal.valueOf(-1)));
            memberCapitalAccount = MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, royaltAmount.multiply(BigDecimal.valueOf(-1)));
            if (memberCapitalAccount.getAmount().compareTo(new BigDecimal(adminSetting.getMinAmount())) < 0) {
                SmsKit.amountNotEnough(driverInfo.getRealName(), driverInfo.getPhone(), memberCapitalAccount.getAmount().toString(), adminSetting.getMinAmount().toString(), driverInfo.getCompany());
                DriverInfo.dao.setForen(driverInfo.getId());
            }
        } else {
            memberCapitalAccount.setAmount(historyAmount.subtract(royaltAmount.add(insuranceAmount)));//从余额扣除司机的提成部分
            memberCapitalAccount.setLastUpdateAmount(royaltAmount.add(insuranceAmount).multiply(BigDecimal.valueOf(-1)));
            // memberCapitalAccount = MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, royaltAmount.add(insuranceAmount).multiply(BigDecimal.valueOf(-1)));
        }
        memberCapitalAccount.setLastUpdateTime(now.toDate());
        memberCapitalAccount.update();
        CapitalLog capitalLog = new CapitalLog();
        capitalLog.setAmount(royaltAmount.multiply(BigDecimal.valueOf(-1)));
        capitalLog.setCreateTime(now.toDate());
        switch (order.getServiceType()) {
            case Constant.ServiceType.DaiJia:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFDJ);
                break;
            case Constant.ServiceType.KuaiChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFKC);
                break;
            case Constant.ServiceType.Taxi:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFCZ);
                break;
            case Constant.ServiceType.ZhuanChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFZC);
                break;
            case Constant.ServiceType.ZhuanXian:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFZX);
                break;
            case Constant.ServiceType.ShunFengChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.TCOFSF);
                break;
        }
        String no = "DDSR" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
        capitalLog.setNo(no);
        capitalLog.setLoginId(driverInfo.getLoginId());
        capitalLog.setOrderId(order.getId());
        capitalLog.setRemark("扣除提成部分,订单号:【" + order.getNo() + "】支付的订单费用");
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        capitalLog.save();

        CapitalLog capitalLog1 = new CapitalLog();
        capitalLog1.setAmount(amount.subtract(royaltAmount.add(insuranceAmount)));
        capitalLog1.setCreateTime(now.toDate());
        capitalLog1.setOperationType(Constant.CapitalOperationType.CASHSHOURU);
        no = "XJSR" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
        capitalLog1.setNo(no);
        capitalLog1.setLoginId(driverInfo.getLoginId());
        capitalLog1.setOrderId(order.getId());
        capitalLog1.setRemark("现金收入,来自订单号:【" + order.getNo() + "】");
        capitalLog1.setStatus(Constant.CapitalStatus.OK);
        capitalLog1.save();

        if (order.getServiceType() != Constant.ServiceType.DaiJia) {
            return false;
        } else {
            CapitalLog capitalLog2 = new CapitalLog();
            capitalLog2.setAmount(insuranceAmount.multiply(BigDecimal.valueOf(-1)));
            capitalLog2.setCreateTime(now.toDate());
            capitalLog2.setOperationType(Constant.CapitalOperationType.BAOXIAN);
            no = "DDSRBX" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
            capitalLog2.setNo(no);
            capitalLog2.setLoginId(driverInfo.getLoginId());
            capitalLog2.setOrderId(order.getId());
            capitalLog2.setRemark("扣除订单:【" + order.getNo() + "】保险费用");
            capitalLog2.setStatus(Constant.CapitalStatus.OK);
            capitalLog2.save();
        }
        return false;
    }

    /**
     * 计算司机代收订单的提成
     *
     * @param order
     * @return
     */
    public BigDecimal royaltyCalculation(Order order) {
        BigDecimal amount = commissionAmount(order);//计算出需要提成的金额
        RoyaltyStandard royaltyStandard = getRoyaltyStandard(order);
        BigDecimal royaltAmount;
        BigDecimal insuranceAmount = AdminSetting.dao.finInsuranceByServiceType(order.getType(), order.getCompany());
        royaltAmount = simpleCommission(royaltyStandard, amount, order);
        return royaltAmount.setScale(2, BigDecimal.ROUND_DOWN);
    }

    public boolean operaterDriver(Order order, BigDecimal royaltAmount, RoyaltyStandard royaltyStandard) {//余额支付操作司机账号
        logger.info("给司机扣除提成操作司机id{},扣除金额{}", order.getDriver(), royaltAmount.toString());
        DateTime now = DateTime.now();
        BigDecimal insuranceAmount = AdminSetting.dao.finInsuranceByServiceType(order.getType(), order.getCompany());
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        BigDecimal totalAmount = order.getAmount().add(order.getWaitAmount()).add(order.getRoadToll()).add(order.getOtherCharges()).add(order.getRemoteFee());
        MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(driverInfo.getLoginId());
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        BigDecimal amount = totalAmount.subtract(royaltAmount);
        CapitalLog capitalLog = new CapitalLog();
        if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            capitalLog.setAmount(amount);
            memberCapitalAccount.setAmount(amount.add(historyAmount).subtract(insuranceAmount));//总的金额-提成的金额-保险费=司机赚取的金额
            memberCapitalAccount.setLastUpdateAmount(amount.subtract(insuranceAmount));
            //设置提成的金额
            memberCapitalAccount = MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, amount.subtract(insuranceAmount));//设置可提现的金额
            AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
            if (adminSetting == null) {
                adminSetting = AdminSetting.dao.findFirst();
            }
            if (memberCapitalAccount.getAmount().compareTo(new BigDecimal(adminSetting.getMinAmount())) < 0) {
                DriverInfo.dao.setForen(driverInfo.getId());
            }
        } else {
            capitalLog.setAmount(amount);
            memberCapitalAccount.setAmount(amount.add(historyAmount));//总的金额-提成的金额=司机赚取的金额(不是代驾)
            memberCapitalAccount.setLastUpdateAmount(amount);
            //设置提成的金额
            memberCapitalAccount = MemberCapitalAccount.dao.calTxAmount(memberCapitalAccount, amount);//设置可提现的金额
        }

        memberCapitalAccount.setLastUpdateTime(now.toDate());
        memberCapitalAccount.update();

        capitalLog.setCreateTime(now.toDate());
        switch (order.getServiceType()) {
            case Constant.ServiceType.DaiJia:
                capitalLog.setOperationType(Constant.CapitalOperationType.DJDAISHOU);
                break;
            case Constant.ServiceType.KuaiChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.KCDAISHOU);
                break;
            case Constant.ServiceType.Taxi:
                capitalLog.setOperationType(Constant.CapitalOperationType.CZDAISHOU);
                break;
            case Constant.ServiceType.ZhuanChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.ZCDAISHOU);
                break;
            case Constant.ServiceType.ShunFengChe:
                capitalLog.setOperationType(Constant.CapitalOperationType.SHUNFENGCHE);
                break;
            case Constant.ServiceType.ZhuanXian:
                capitalLog.setOperationType(Constant.CapitalOperationType.ZHUANXIAN);
                break;
        }
        String no = "DDSR" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
        capitalLog.setNo(no);
        capitalLog.setLoginId(driverInfo.getLoginId());
        capitalLog.setOrderId(order.getId());
        capitalLog.setRemark("订单号:【" + order.getNo() + "】支付的订单费用,已经按照" + royaltyStandard.getName() + "方式扣除了提成");
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        capitalLog.save();

        if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            CapitalLog capitalLog1 = new CapitalLog();//扣除保险费
            capitalLog1.setAmount(insuranceAmount.multiply(BigDecimal.valueOf(-1)));
            capitalLog1.setCreateTime(now.toDate());
            switch (order.getServiceType()) {
                case Constant.ServiceType.DaiJia:
                    capitalLog1.setOperationType(Constant.CapitalOperationType.DJDAISHOU);
                    break;
                case Constant.ServiceType.KuaiChe:
                    capitalLog1.setOperationType(Constant.CapitalOperationType.KCDAISHOU);
                    break;
                case Constant.ServiceType.Taxi:
                    capitalLog1.setOperationType(Constant.CapitalOperationType.CZDAISHOU);
                    break;
                case Constant.ServiceType.ZhuanChe:
                    capitalLog1.setOperationType(Constant.CapitalOperationType.ZCDAISHOU);
                    break;
                case Constant.ServiceType.ShunFengChe:
                    capitalLog.setOperationType(Constant.CapitalOperationType.SHUNFENGCHE);
                    break;
                case Constant.ServiceType.ZhuanXian:
                    capitalLog.setOperationType(Constant.CapitalOperationType.ZHUANXIAN);
                    break;
                case Constant.ServiceType.HangKongZhuanXian:
                    capitalLog.setOperationType(Constant.CapitalOperationType.HANGKONGZHUANXIAN);
                    break;
                default:
                    break;
            }
            no = "DDBXF" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
            capitalLog1.setNo(no);
            capitalLog1.setLoginId(driverInfo.getLoginId());
            capitalLog1.setOrderId(order.getId());
            capitalLog1.setRemark("订单号:【" + order.getNo() + "】扣除的保险费");
            capitalLog1.setStatus(Constant.CapitalStatus.OK);
            capitalLog1.save();
        }
        return false;
    }

    /**
     * 给公司账户增加提成
     *
     * @param order
     * @param royaltAmount
     * @return
     */
    public boolean operaterCompany(Order order, BigDecimal royaltAmount, RoyaltyStandard royaltyStandard) {
        logger.info("给公司增加提成:公司的id{},提成金额{}", order.getCompany(), royaltAmount.toString());
        // BigDecimal insuranceAmount = AdminSetting.dao.finInsuranceByServiceType(order.getType(),order.getCompany());
        CompanyRebate companyRebate = new CompanyRebate();
        DateTime now = DateTime.now();
        companyRebate.setAmount(royaltAmount);
        companyRebate.setCompany(order.getCompany());
        companyRebate.setDriver(order.getDriver());
        companyRebate.setOrderId(order.getId());
        companyRebate.setRebateTime(now.toDate());
        companyRebate.setServiceType(order.getServiceType());
        switch (order.getServiceType()) {
            case Constant.ServiceType.DaiJia:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFDJ);
                break;
            case Constant.ServiceType.KuaiChe:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFKC);
                break;
            case Constant.ServiceType.ZhuanChe:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFZC);
                break;
            case Constant.ServiceType.Taxi:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFCZ);
                break;
            case Constant.ServiceType.ZhuanXian:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFZX);
                break;
            case Constant.ServiceType.ShunFengChe:
                companyRebate.setRebateType(Constant.CapitalOperationType.TCOFSF);
                break;
        }
        companyRebate.save();
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        /*if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            CompanyRebate companyRebate1 = new CompanyRebate();//保险费
            companyRebate1.setAmount(insuranceAmount);
            companyRebate1.setCompany(order.getCompany());
            companyRebate1.setDriver(order.getDriver());
            companyRebate1.setServiceType(order.getServiceType());
            companyRebate1.setOrderId(order.getId());
            companyRebate1.setRebateTime(now.toDate());
            companyRebate1.setRebateType(Constant.CapitalOperationType.BAOXIAN);
            companyRebate1.save();
        }*/
        CompanyAccount companyAccount = CompanyAccount.dao.findById(order.getCompany());
        BigDecimal historyAmount = companyAccount.getAmount() == null ? BigDecimal.ZERO : companyAccount.getAmount();
        
        /*if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            companyAccount.setAmount(historyAmount.add(royaltAmount.add(insuranceAmount)));
            companyAccount.setLastAmont(royaltAmount.add(insuranceAmount));
        } else {
            companyAccount.setAmount(historyAmount.add(royaltAmount));
            companyAccount.setLastAmont(royaltAmount);
        }*/

        companyAccount.setAmount(historyAmount.add(royaltAmount));
        companyAccount.setLastAmont(royaltAmount);
        companyAccount.setLastUpdate(now.toDate());
        companyAccount.update();
        CompanyAccountLog companyAccountLog = new CompanyAccountLog();
        companyAccountLog.setAmount(royaltAmount);
        companyAccountLog.setCompany(order.getCompany());
        companyAccountLog.setServiceType(order.getServiceType());
        String remark = "司机提成,来自订单:【" + order.getNo() + "】,服务人员:"
                + driverInfo.getRealName() + ",客户:" + memberInfo.getNickName() + ",提成方式:" + royaltyStandard.getName();
        companyAccountLog.setRemark(remark);
        companyAccountLog.setOpeartType(Constant.CompanyOperteType.TCD);
        companyAccountLog.save();
       /* if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            CompanyAccountLog companyAccountLog1 = new CompanyAccountLog();
            companyAccountLog1.setAmount(insuranceAmount);
            companyAccountLog1.setCompany(order.getCompany());
            companyAccountLog1.setServiceType(order.getServiceType());
            companyAccountLog1.setRemark("保险费,来自订单:【" + order.getNo() + "】,服务人员:" + driverInfo.getRealName());
            companyAccountLog1.setOpeartType(Constant.CompanyOperteType.BXF);
            companyAccountLog1.save();
        }*/
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setAmount(royaltAmount);
        orderDetail.setRemark(remark);
        orderDetail.setOrderId(order.getId());
        orderDetail.setLoginId(driverInfo.getLoginId());
        orderDetail.setCompany(order.getCompany());
        orderDetail.setAuditStatus(1);
        orderDetail.setCreateTime(now.toDate());
        switch (order.getServiceType()) {
            case Constant.ServiceType.DaiJia:
                orderDetail.setType(Constant.CapitalOperationType.TCOFDJ);
                break;
            case Constant.ServiceType.KuaiChe:
                orderDetail.setType(Constant.CapitalOperationType.TCOFKC);
                break;
            case Constant.ServiceType.ZhuanChe:
                orderDetail.setType(Constant.CapitalOperationType.TCOFZC);
                break;
            case Constant.ServiceType.Taxi:
                orderDetail.setType(Constant.CapitalOperationType.TCOFCZ);
                break;
            case Constant.ServiceType.ZhuanXian:
                orderDetail.setType(Constant.CapitalOperationType.TCOFZX);
                break;
            case Constant.ServiceType.ShunFengChe:
                orderDetail.setType(Constant.CapitalOperationType.TCOFSF);
                break;
        }
        String no = "DDMX" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
        orderDetail.setNo(no);
        orderDetail.save();
       /* if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            OrderDetail orderDetail1 = new OrderDetail();
            orderDetail1.setAmount(insuranceAmount);
            orderDetail1.setRemark("订单:【" + order.getNo() + "】的保险费");
            orderDetail1.setOrderId(order.getId());
            orderDetail1.setLoginId(driverInfo.getLoginId());
            orderDetail1.setCompany(order.getCompany());
            orderDetail1.setAuditStatus(1);
            orderDetail1.setCreateTime(now.toDate());
            orderDetail1.setType(Constant.CapitalOperationType.BAOXIAN);
            no = "DDMXBX" + now.toString("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
            orderDetail1.setNo(no);
            orderDetail1.save();
        }*/
        return false;
    }

    /**
     * 安照比例提成
     *
     * @param royaltyStandard
     * @param amount
     * @param order
     * @return
     */
    public BigDecimal simpleCommission(RoyaltyStandard royaltyStandard, BigDecimal amount, Order order) {
        BigDecimal royaltyAmount;
        List<RoyaltyStandardEasy> royaltyStandardEasys = RoyaltyStandardEasy.dao.findByRoyaltyStandard(royaltyStandard.getId());
        RoyaltyStandardEasy realRoya = null;
        DateTime orderPayDateTime = new DateTime(order.getPayTime());
        Date orderPayTime = DateTime.parse(orderPayDateTime.toString(DateTimeFormat.forPattern("HH:mm:ss")), DateTimeFormat.forPattern("HH:mm:ss")).toDate();
        for (RoyaltyStandardEasy standardEasy : royaltyStandardEasys) {
            Date start = standardEasy.getStartTime();
            Date end = standardEasy.getEndTime();
            if (start.before(end)) {
                if (orderPayTime.after(start) && orderPayTime.before(end)) {
                    realRoya = standardEasy;
                    break;
                }
            } else {
                end = new DateTime(start).plusDays(1).toDate();
                if (orderPayTime.after(start) && orderPayTime.before(end)) {
                    realRoya = standardEasy;
                    break;
                } else {
                    orderPayTime = new DateTime(orderPayTime).plusDays(1).toDate();
                    if (orderPayTime.after(start) && orderPayTime.before(end)) {
                        realRoya = standardEasy;
                        break;
                    }
                }
            }
        }
        if (realRoya.getType() == 2) {//比例提成
            if (amount.compareTo(realRoya.getLessThanMoney2()) <= 0) {
                royaltyAmount = realRoya.getGetMoney2();
            } else {
                royaltyAmount = realRoya.getRatio().multiply(amount).divide(new BigDecimal(100), 2);
            }
        } else {//金额提成
            if (amount.compareTo(realRoya.getLessThanMoney1()) <= 0) {
                royaltyAmount = realRoya.getGetMoney1();
            } else {
                royaltyAmount = realRoya.getFixedMoney();
            }
        }
        return royaltyAmount;
    }

    /**
     * 金额区间计算提成
     *
     * @param royaltyStandard
     * @param amount
     * @param order
     * @return
     */
    public BigDecimal moneyCommission(RoyaltyStandard royaltyStandard, BigDecimal amount, Order order) {
        BigDecimal royaltyAmount = BigDecimal.ZERO;
        RoyaltyStandardMoney realRoya = null;
        List<RoyaltyStandardMoney> royaltyStandardMoneys = RoyaltyStandardMoney.dao.findByRoyaltyStandard(royaltyStandard.getId());
        DateTime orderPayDateTime = new DateTime(order.getPayTime());
        Date orderPayTime = new DateTime(DateTime.parse(orderPayDateTime.toString(DateTimeFormat.forPattern("HH:mm:ss")), DateTimeFormat.forPattern("HH:mm:ss"))).toDate();
        for (RoyaltyStandardMoney s : royaltyStandardMoneys) {
            Date start = new DateTime(s.getStartTime()).toDate();
            Date end = new DateTime(s.getEndTime()).toDate();
            if (start.before(end)) {
                if (orderPayTime.after(start) && orderPayTime.before(end)) {
                    realRoya = s;
                    break;
                }
            } else {
                end = new DateTime(start).plusDays(1).toDate();
                if (orderPayTime.after(start) && orderPayTime.before(end)) {
                    realRoya = s;
                    break;
                } else {
                    orderPayTime = new DateTime(orderPayTime).plusDays(1).toDate();
                    if (orderPayTime.after(start) && orderPayTime.before(end)) {
                        realRoya = s;
                        break;
                    }
                }
            }
        }
        List<RoyaltyStandardMoneyItem> royaltyStandardMoneyItems = RoyaltyStandardMoneyItem.dao.findByMoneyAndAmount(realRoya.getId(), amount);
        for (RoyaltyStandardMoneyItem royaltyStandardMoneyItem : royaltyStandardMoneyItems) {
            if (royaltyStandardMoneyItem.getEachMoney().compareTo(amount) <= 0) {
                BigDecimal tmp = royaltyStandardMoneyItem.getEndMoney().subtract(royaltyStandardMoneyItem.getStartMoney());
                BigDecimal[] than = tmp.divideAndRemainder(royaltyStandardMoneyItem.getEachMoney());
                if (royaltyStandardMoneyItem.getType() == 2) {
                    royaltyAmount = royaltyAmount.add(than[0].multiply(royaltyStandardMoneyItem.getEachMoney().multiply(royaltyStandardMoneyItem.getGetMoney())).divide(new BigDecimal(100), 2));
                } else {
                    royaltyAmount = royaltyAmount.add(than[0].multiply(royaltyStandardMoneyItem.getGetMoney()));
                }
            } else if (royaltyStandardMoneyItem.getEachMoney().compareTo(amount) > 0 && royaltyStandardMoneyItem.getStartMoney().compareTo(amount) <= 0) {
                BigDecimal tmp = amount.subtract(royaltyStandardMoneyItem.getStartMoney());
                BigDecimal[] than = tmp.divideAndRemainder(royaltyStandardMoneyItem.getEachMoney());
                if (royaltyStandardMoneyItem.getType() == 2) {
                    royaltyAmount = royaltyAmount.add(than[0].multiply(royaltyStandardMoneyItem.getEachMoney().multiply(royaltyStandardMoneyItem.getGetMoney())).divide(new BigDecimal(100), 2));
                } else {
                    royaltyAmount = royaltyAmount.add(than[0].multiply(royaltyStandardMoneyItem.getGetMoney()));
                }
            }
        }
        return royaltyAmount;
    }

    /**
     * 计算需要提成的金额
     *
     * @param order
     * @return
     */
    public BigDecimal commissionAmount(Order order) {//获取需要提成的金额
        BigDecimal amount = order.getWaitAmount();
        return amount.add(order.getAmount());
    }

    /**
     * 获取提成的标准
     *
     * @param order
     * @return
     */
    public RoyaltyStandard getRoyaltyStandard(Order order) {//获取到这个订单的提成标准
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        RoyaltyStandard royaltyStandard;
        if (order.getType() == Constant.ServiceItemType.DaiJia) {
            if (driverInfo.getDjRe() != null && driverInfo.getDjRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getDjRe());
                return royaltyStandard;
            }
        }
        if (order.getType() == Constant.ServiceItemType.KuaiChe) {
            if (driverInfo.getKcRe() != null && driverInfo.getKcRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getKcRe());
                return royaltyStandard;
            }
        }
        if (order.getType() == Constant.ServiceItemType.ShangWu) {
            if (driverInfo.getZcShangwuRe() != null && driverInfo.getZcShangwuRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getZcShangwuRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }
        if (order.getType() == Constant.ServiceItemType.ShuShi) {
            if (driverInfo.getZcSushiRe() != null && driverInfo.getZcSushiRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getZcSushiRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }
        if (order.getType() == Constant.ServiceItemType.HaoHua) {
            if (driverInfo.getZcHaohuaRe() != null && driverInfo.getZcHaohuaRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getZcHaohuaRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }
        if (order.getType() == Constant.ServiceItemType.Taxi) {
            if (driverInfo.getTexiRe() != null && driverInfo.getTexiRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getTexiRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.KuaChengZhuanXian) {
            if (driverInfo.getZhuanxianRe() != null && driverInfo.getZhuanxianRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getZhuanxianRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.HangKongZhuanXianJieJI) {
            if (driverInfo.getHkzhuanxianRe() != null && driverInfo.getHkzhuanxianRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getHkzhuanxianRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.HangKongZhuanXianSongJi) {
            if (driverInfo.getHkzhuanxianRe() != null && driverInfo.getHkzhuanxianRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getHkzhuanxianRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.DaiHuo) {
            if (driverInfo.getShunfengcheRe() != null && driverInfo.getShunfengcheRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getShunfengcheRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.KuaCheng) {
            if (driverInfo.getShunfengcheRe() != null && driverInfo.getShunfengcheRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getShunfengcheRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }

        if (order.getType() == Constant.ServiceItemType.ShiNei) {
            if (driverInfo.getShunfengcheRe() != null && driverInfo.getShunfengcheRe() != 0) {
                royaltyStandard = RoyaltyStandard.dao.findById(driverInfo.getShunfengcheRe());
                logger.info("司机的提成标准是{}", royaltyStandard.getName());
                return royaltyStandard;
            }
        }
        int company = order.getCompany();
        CompanyService companyService = CompanyService.dao.findByCompanyAndServiceType(company, order.getType());
        if (companyService == null) {
            ServiceTypeItem serviceTypeItem = ServiceTypeItem.dao.findById(order.getType());
            royaltyStandard = RoyaltyStandard.dao.findById(serviceTypeItem.getRoyaltyStandard());
        } else {
            if (order.getFromType() == Constant.FromType.BUDAN) {
                royaltyStandard = RoyaltyStandard.dao.findById(companyService.getRoyaltyStandardForCreate());
                if (royaltyStandard == null) {
                    logger.error("未获取到自创订单提成，使用公司听单提成！");
                    royaltyStandard = RoyaltyStandard.dao.findById(companyService.getRoyaltyStandard());
                }
            } else {
                royaltyStandard = RoyaltyStandard.dao.findById(companyService.getRoyaltyStandard());
            }
        }
        return royaltyStandard;
    }


    private CalCommissionService() {
    }

    private static class CalServiceHolder {
        static CalCommissionService instance = new CalCommissionService();
    }

    public static CalCommissionService getInstance() {
        return CalServiceHolder.instance;
    }
}