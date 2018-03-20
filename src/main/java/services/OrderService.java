package services;

import exception.InsuranceException;
import models.company.CompanyAccount;
import models.company.CompanyAccountLog;
import models.company.CompanyRebate;
import models.order.OrderDetail;
import models.sys.AdminSetting;
import org.apache.commons.lang3.RandomStringUtils;
import utils.BigDecimalMath;
import utils.DateUtil;
import base.Constant;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import dto.RateDto.OrdersRateDto;
import kits.SmsKit;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.order.Order;
import models.order.OrderLog;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/12/22.
 */
public class OrderService {
    
    public void yuyuetixing1() {
        Date start = DateTime.now().plusMinutes(15).secondOfMinute().withMinimumValue().toDate();
        Date end = DateTime.now().plusMinutes(15).secondOfMinute().withMaximumValue().toDate();
        List<Order> orderList = Order.dao.findByYuyueAndDate(start, end);
        for (Order order : orderList) {
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            SmsKit.yuyuetosiji(order, driverInfo.getPhone(), driverInfo.getCompany());
            SmsKit.yuyuetohuiyuan(order, memberInfo.getPhone(), memberInfo.getCompany());
        }
    }
    
    public void yuyuetixing2() {
        Date start = DateTime.now().plusMinutes(-10).secondOfMinute().withMinimumValue().toDate();
        Date end = DateTime.now().plusMinutes(-10).secondOfMinute().withMaximumValue().toDate();
        List<Order> orderList = Order.dao.findByYuyueAndDate(start, end);
        for (Order order : orderList) {
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            SmsKit.yuyuetosiji5(order, driverInfo.getPhone(), driverInfo.getCompany());
        }
    }
    
    /**
     * 销单功能
     */
    public void cancelyuyue() {
        Date start = DateTime.now().plusMinutes(-15).secondOfMinute().withMinimumValue().toDate();
        Date end = DateTime.now().plusMinutes(-15).secondOfMinute().withMaximumValue().toDate();
        List<Order> orderList = Order.dao.findByYuyueAndDate(start, end);
        for (final Order order : orderList) {
            MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
            final OrderLog orderLog = new OrderLog();
            orderLog.setOperationTime(DateTime.now().toDate());
            orderLog.setOrderId(order.getId());
            orderLog.setRemark("预约时间已过，系统自动取消了订单");
            orderLog.setAction(Constant.OrderAction.CANCEL);
            orderLog.setLoginId(memberInfo.getLoginId());
            order.setStatus(Constant.OrderStatus.CANCEL);
            order.setLastUpdateTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return order.update() && orderLog.save();
                }
            });
            SmsKit.ordercancel(order);
        }
    }
    
    private OrderService() {
    }
    
    private static class OrderServiceHolder {
        static OrderService instance = new OrderService();
    }
    
    public static OrderService getInstance() {
        return OrderServiceHolder.instance;
    }
    
    
    /**
     * 统计用户不同类型的订单接单率
     *
     * @param driverId
     * @return
     */
    public OrdersRateDto OrdersRate(int driverId, String bTime, String eTime) {
        List<Order> orderListAll = Order.dao.findOrdersRateByAll();
        List<Order> orderList = null;
        
        if ((bTime == null && eTime == null) || (bTime == "" && eTime == "")) {
            orderList = Order.dao.findOrdersRateById(driverId);
        } else {
            orderList = Order.dao.findOrdersRateByIdAndTime(driverId, new DateTime(DateUtil.stampToDate(bTime, "yyyy-MM-dd'T'HH:mm:ss.SSS")), new DateTime(DateUtil.stampToDate(eTime, "yyyy-MM-dd'T'HH:mm:ss.SSS")));
        }
        OrdersRateDto dto = new OrdersRateDto();
        for (int i = 0; i < orderListAll.size(); i++) {
            orderListAll.get(i).get("zc").toString();
            orderList.get(i).get("zc").toString();
            dto.setZc(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("zc").toString()), Double.valueOf(orderListAll.get(i).get("zc").toString()), 2) * 100);
            dto.setDj(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("dj").toString()), Double.valueOf(orderListAll.get(i).get("dj").toString()), 2) * 100);
            dto.setCzc(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("czc").toString()), Double.valueOf(orderListAll.get(i).get("czc").toString()), 2) * 100);
            dto.setKc(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("kc").toString()), Double.valueOf(orderListAll.get(i).get("kc").toString()), 2) * 100);
            dto.setSfc(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("sfc").toString()), Double.valueOf(orderListAll.get(i).get("sfc").toString()), 2) * 100);
            dto.setZx(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("zx").toString()), Double.valueOf(orderListAll.get(i).get("zx").toString()), 2) * 100);
            dto.setALLs(BigDecimalMath.div(Double.valueOf(orderList.get(i).get("alls").toString()), Double.valueOf(orderListAll.get(i).get("alls").toString()), 2) * 100);
            dto.setCount(Integer.parseInt(orderList.get(i).get("alls").toString()));
        }
        return dto;
    }
    
    /**
     * 扣除保险
     *
     * @param order
     * @throws InsuranceException
     */
    public void deductionInsurance(Order order) throws InsuranceException {
        boolean insuranceOK = true;
        String serviceBigType = "";
        switch (order.getServiceType()) {
            case Constant.ServiceType.ZhuanChe:
                serviceBigType = "专车";
                break;
            case Constant.ServiceType.DaiJia:
                serviceBigType = "代驾";
                break;
            case Constant.ServiceType.Taxi:
                serviceBigType = "出租车";
                break;
            case Constant.ServiceType.KuaiChe:
                serviceBigType = "快车";
                break;
            case Constant.ServiceType.ShunFengChe:
                serviceBigType = "顺风车";
                break;
            case Constant.ServiceType.ZhuanXian:
                serviceBigType = "专线";
                break;
            case Constant.ServiceType.HangKongZhuanXian:
                serviceBigType = "航空专线";
                break;
            default:
                break;
        }
        
        
        final CompanyAccount account = CompanyAccount.dao.findById(order.getCompany());
        AdminSetting adminSetting = AdminSetting.dao.findByCompanyId(order.getCompany());
        final DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        final CompanyAccountLog accountLog = new CompanyAccountLog();
        final OrderDetail orderDetail1 = new OrderDetail();
        BigDecimal insuranceAmount = AdminSetting.dao.finInsuranceByServiceType(order.getType(), order.getCompany());
        if (insuranceAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (!(adminSetting.getInsuranceOverdraftAmount().compareTo(account.getInsuranceAmount()) < 1) || (adminSetting.getInsuranceOverdraftAmount().compareTo(account.getInsuranceAmount()) == 0)) {
                throw new InsuranceException("公司透支保险费上限！！请联系管理人员");
            }
            CompanyRebate companyRebate1 = new CompanyRebate();//保险费
            companyRebate1.setAmount(insuranceAmount);
            companyRebate1.setCompany(order.getCompany());
            companyRebate1.setDriver(order.getDriver());
            companyRebate1.setServiceType(order.getServiceType());
            companyRebate1.setOrderId(order.getId());
            companyRebate1.setRebateTime(com.xiaoleilu.hutool.date.DateUtil.date());
            companyRebate1.setRebateType(Constant.CapitalOperationType.BAOXIAN);
            companyRebate1.save();
            
            account.setInsuranceAmount(account.getInsuranceAmount().subtract(insuranceAmount));
            accountLog.setAmount(insuranceAmount);
            accountLog.setCompany(order.getCompany());
            accountLog.setServiceType(order.getServiceType());
            accountLog.setOpeartType(Constant.CompanyOperteType.BXF);
            accountLog.setRemark("[" + serviceBigType + "]保险费,来自订单:【" + order.getNo() + "】,服务人员:" + DriverInfo.dao.findById(order.getDriver()).getRealName() + ",金额：" + insuranceAmount + "元");
            
            orderDetail1.setAmount(insuranceAmount);
            orderDetail1.setRemark("订单:【" + order.getNo() + "】的[" + serviceBigType + "]保险费：" + insuranceAmount + "元");
            orderDetail1.setOrderId(order.getId());
            orderDetail1.setLoginId(driverInfo.getLoginId());
            orderDetail1.setCompany(order.getCompany());
            orderDetail1.setAuditStatus(1);
            orderDetail1.setCreateTime(com.xiaoleilu.hutool.date.DateUtil.date());
            orderDetail1.setType(Constant.CapitalOperationType.BAOXIAN);
            String no = "DDMXBX" + DateUtil.dateFmt(com.xiaoleilu.hutool.date.DateUtil.date(), "yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(6).toUpperCase();
            orderDetail1.setNo(no);
            insuranceOK = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return account.update() && accountLog.save() && orderDetail1.save();
                }
            });
        }
        
        
        if (!insuranceOK) {
            throw new InsuranceException("公司透支保险费上限！！请联系管理人员");
        }
    }
}
