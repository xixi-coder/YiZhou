package services;

import base.Constant;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import dto.CalculationDto;
import jobs.pushorder.PushOrder;
import jobs.pushorder.PushOrderToAllShunFengChe;
import jobs.pushorder.PushOrderToShunFengChe;
import kits.StringsKit;
import kits.TimeKit;
import models.company.Company;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.order.TraverRecord;
import models.sys.RecordLog;
import net.dreamlu.event.EventKit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public class GetTraverService {
    private Logger logger = LoggerFactory.getLogger(GetTraverService.class);
    private static GetTraverService getTraverService;

    public List<TraverRecord> getTraver(Double j, Double w, int type, int record_type, String sCityCode, String eCityCode, int pageStart, int pageSize, MemberInfo memberInfo) {
        List<TraverRecord> traverRecords = TraverRecord.dao.recordList(j, w, memberInfo, type, record_type, sCityCode, eCityCode, DateTime.now().toDate(), pageStart, pageSize);
        return traverRecords;
    }

    public List<TraverRecord> getDriverTraver(Double j, Double w, int record_type, int driverId, String sCityCode, String eCityCode, int pageStart, int pageSize) {
        List<TraverRecord> traverRecords = TraverRecord.dao.findDriverTraver(j, w, record_type, driverId, sCityCode, eCityCode, pageStart, pageSize);
        return traverRecords;
    }


    public List<TraverRecord> getTraverTwo(Double j, Double w, int type, int record_type, String sCityCode, String eCityCode, int pageStart, int pageSize, MemberInfo memberInfo, double startLat, double startLon, double endLat, double endLon, Date sTime, Date eTime) {
        List<TraverRecord> traverRecords = TraverRecord.dao.recordListTwo(j, w, memberInfo, type, record_type, sCityCode, eCityCode, pageStart, pageSize, startLat, startLon, endLat, endLon, sTime, eTime);
        return traverRecords;
    }


    public List<TraverRecord> getDriverTraverTwo(Double j, Double w, int record_type, int driverId, String sCityCode, String eCityCode, int pageStart, int pageSize, double startLat, double startLon, double endLat, double endLon, Date sTime, Date eTime) {
        List<TraverRecord> traverRecords = TraverRecord.dao.findDriverTraverTwo(j, w, record_type, driverId, sCityCode, eCityCode, pageStart, pageSize, startLat, startLon, endLat, endLon, sTime, eTime);
        return traverRecords;
    }


    //生成顺风车订单
    public Order createOrder(int type, TraverRecord traverRecord, MemberLogin memberLogin, int people) {
        Company company;
        Order order2 = Order.dao.findOrder(traverRecord, memberLogin);
        RecordLog recordLog2 = RecordLog.dao.findOrder(traverRecord, memberLogin);
        if (order2 != null && recordLog2 != null) {
            order2.deleteById(order2.getId());
            recordLog2.deleteById(recordLog2.getId());
        }
        if (Strings.isNullOrEmpty(traverRecord.getStartAdcode())) {//判断是否有cityCode，如果没有就用用户自己的公司
            if (traverRecord.getType() == 1) {
                company = Company.dao.findByCompanyId(DriverInfo.dao.findById(traverRecord.getDriverId()).getCompany());
            } else {
                company = Company.dao.findByCompanyId(MemberInfo.dao.findById(traverRecord.getMemberId()).getCompany());
            }
        } else {
            company = Company.dao.findByCity(traverRecord.getStartAdcode());
        }

        final int orderType = traverRecord.getRecordType();
        BigDecimal distance = traverRecord.getDistance();
        Double startLongitude = traverRecord.getStartLongitude();
        Double startLatitude = traverRecord.getStartLatitude();
        Double endLongitude = traverRecord.getEndLongitude();
        Double endLatitude = traverRecord.getEndLatitude();
        String reservationAddress = traverRecord.getReservationAddress();
        String destination = traverRecord.getDestination();
        String cityCode = traverRecord.getStartAdcode();
        Date time;
        final Order order = new Order();

        final RecordLog recordLog = new RecordLog();
        recordLog.setPeople(people == 0 ? 1 : people);
        recordLog.setRecordId(traverRecord.getId());
        recordLog.setLoginId(memberLogin.getId());
        recordLog.setCreateTime(DateTime.now().toDate());
        recordLog.setStatus(Constant.TraverStatus.CREATE);
        recordLog.setType(2);
        String no = StringsKit.getOrderNo();
        order.setNo(no);
        order.setSetOutFlag(false);
        order.setFromType(Constant.FromType.APPTYPE);
        if (type != 3 && type != 0) {
            boolean pdFlag = (type == 2 ? false : true);
            order.setPdFlag(pdFlag);//是否拼单
        }
        order.setFromCompany(company.getId());//设置订单的公司
        order.setCompany(company.getId());//设置订单的公司
        order.setCreateTime(DateTime.now().toDate());
        order.setDistance(traverRecord.getDistance());
        order.setMember(MemberInfo.dao.findByLoginId(memberLogin.getId()).getId());
        order.setPhone(MemberInfo.dao.findByLoginId(memberLogin.getId()).getPhone());
        if (type != 3) {
            order.setType(traverRecord.getRecordType() == 1 ? Constant.ServiceItemType.ShiNei : traverRecord.getRecordType() == 2 ? Constant.ServiceItemType.KuaCheng : 0);
        } else {
            order.setType(Constant.ServiceItemType.DaiHuo);
        }
        order.setServiceType(Constant.ServiceType.ShunFengChe);
        order.setRemark("通过APP下单");
        order.setStatus(Constant.OrderStatus.CREATE);
        order.setPayStatus(Constant.PayStatus.NOPAY);
        order.setRealDistance(BigDecimal.ZERO);
        order.setDestination(destination);
        order.setPeople(people);
        order.setDistance(distance);
        order.setDriver(traverRecord.getDriverId());
        order.setReservationAddress(reservationAddress);
        order.setTraverId(traverRecord.getId());
        BigDecimal price = price(traverRecord, people, type, company, MemberInfo.dao.findByLoginId(memberLogin.getId()));
        if (traverRecord.getType() == 1) {
            order.setYgAmount(price);
            order.setAmount(price);
            if (type == 2) {

                recordLog.setAmount(price);
            } else if (type == 1) {

                recordLog.setPinCheAmount(price);
            } else if (type == 3) {

                recordLog.setJiHuoAmount(price);
            } else {
                order.setYgAmount(BigDecimal.ZERO);
                order.setAmount(BigDecimal.ZERO);
            }
        } else {
            order.setYgAmount(traverRecord.getAmount());
        }
        order.put("regisId", MemberLogin.dao.findById(DriverInfo.dao.findById(traverRecord.getDriverId()).getLoginId()).getRegistrationId());
        order.put("traverId", traverRecord.getId());
        order.put("setout_time1", traverRecord.getSetoutTime1());
        order.put("setout_time2", traverRecord.getSetoutTime2());
        final OrderLog orderLog = new OrderLog();
        orderLog.setAction(Constant.OrderStatus.CREATE);
        orderLog.setLoginId(memberLogin.getId());
        orderLog.setOperater(memberLogin.getUserName());
        orderLog.setRemark(memberLogin.getUserName() + "创建了一个订单");
        orderLog.setOperationTime(DateTime.now().toDate());
        final OrderTrip orderTrip = new OrderTrip();
        orderTrip.setStartLatitude(startLatitude);
        orderTrip.setStartLongitude(startLongitude);
        orderTrip.setEndLatitude(endLatitude);
        orderTrip.setEndLongitude(endLongitude);
        if (type == 2) {
            orderTrip.setMember(traverRecord.getMemberId());
        } else {
            orderTrip.setMember(traverRecord.getDriverId());
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (order.save()) {
                    orderTrip.setOrderId(order.getId());
                    orderLog.setOrderId(order.getId());
                    return orderTrip.save() && orderLog.save() && recordLog.save();
                } else {
                    return false;
                }
            }
        });
        if (isOk) {
            logger.info("顺风车订单推送手机号{}", DriverInfo.dao.findById(order.getDriver()).getPhone());
            EventKit.post(new PushOrderToShunFengChe(order));
            return order;
        } else {
            return null;
        }
    }

    public Order createChenJiOrder(int type, TraverRecord traverRecord, MemberLogin memberLogin) {
        Company company;
        if (Strings.isNullOrEmpty(traverRecord.getStartAdcode())) {//判断是否有cityCode，如果没有就用用户自己的公司
            if (traverRecord.getType() == 1) {
                company = Company.dao.findByCompanyId(DriverInfo.dao.findById(traverRecord.getDriverId()).getCompany());
            } else {
                company = Company.dao.findByCompanyId(MemberInfo.dao.findById(traverRecord.getMemberId()).getCompany());
            }
        } else {
            company = Company.dao.findByCity(traverRecord.getStartAdcode());
        }
        final int orderType = traverRecord.getRecordType();
        BigDecimal distance = traverRecord.getDistance();
        Double startLongitude = traverRecord.getStartLongitude();
        Double startLatitude = traverRecord.getStartLatitude();
        Double endLongitude = traverRecord.getEndLongitude();
        Double endLatitude = traverRecord.getEndLatitude();
        String reservationAddress = traverRecord.getReservationAddress();
        String destination = traverRecord.getDestination();
        String cityCode = traverRecord.getStartAdcode();
        Date time;
        final Order order = new Order();
        String no = StringsKit.getOrderNo();
        order.setNo(no);
        order.setSetOutFlag(true);
        order.setFromType(Constant.FromType.APPTYPE);
        order.setPdFlag(traverRecord.getFlag());
        order.setFromCompany(company.getId());//设置订单的公司
        order.setCompany(company.getId());//设置订单的公司
        order.setCreateTime(DateTime.now().toDate());
        order.setDistance(traverRecord.getDistance());
        order.setMember(traverRecord.getMemberId());
        order.setPhone(MemberInfo.dao.findById(traverRecord.getMemberId()).getPhone());
        if (type != 3) {
            order.setType(traverRecord.getRecordType() == 1 ? Constant.ServiceItemType.ShiNei : traverRecord.getRecordType() == 2 ? Constant.ServiceItemType.KuaCheng : 0);
        } else {
            order.setType(Constant.ServiceItemType.DaiHuo);
        }
        order.setServiceType(Constant.ServiceType.ShunFengChe);
        order.setRemark("通过APP下单");
        order.setStatus(Constant.OrderStatus.CREATE);
        order.setPayStatus(Constant.PayStatus.NOPAY);
        order.setRealDistance(BigDecimal.ZERO);
        order.setDestination(destination);
        order.setPeople(traverRecord.getPeople());
        order.setDistance(distance);
        order.setDriver(traverRecord.getDriverId());
        order.setReservationAddress(reservationAddress);
        order.setTraverId(traverRecord.getId());
        if (traverRecord.getAmount() != null) {
            order.setAmount(traverRecord.getAmount());
        } else if (traverRecord.getPinCheAmount() != null) {
            order.setAmount(traverRecord.getPinCheAmount());
        } else {
            order.setAmount(traverRecord.getJiHuoAmount());
        }
        order.put("setout_time1", traverRecord.getSetoutTime1());
        order.put("setout_time2", traverRecord.getSetoutTime2());
        order.put("sCity", traverRecord.getReservationCity());
        order.put("eCity", traverRecord.getDestinationCity());
        order.put("sCityCode", traverRecord.getStartAdcode());
        order.put("eCityCode", traverRecord.getEndAdcode());
        final OrderLog orderLog = new OrderLog();
        orderLog.setAction(Constant.OrderStatus.CREATE);
        orderLog.setLoginId(MemberInfo.dao.findById(traverRecord.getMemberId()).getLoginId());
        orderLog.setOperater(memberLogin.getUserName());
        orderLog.setRemark(memberLogin.getUserName() + "创建了一个订单");
        orderLog.setOperationTime(DateTime.now().toDate());
        final OrderTrip orderTrip = new OrderTrip();
        orderTrip.setStartLatitude(startLatitude);
        orderTrip.setStartLongitude(startLongitude);
        orderTrip.setEndLatitude(endLatitude);
        orderTrip.setEndLongitude(endLongitude);
        if (type == 2) {
            orderTrip.setMember(traverRecord.getMemberId());
        } else {
            orderTrip.setMember(traverRecord.getDriverId());
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (order.save()) {
                    orderTrip.setOrderId(order.getId());
                    orderLog.setOrderId(order.getId());
                    return orderTrip.save() && orderLog.save();
                } else {
                    return false;
                }
            }
        });
        if (isOk) {
            EventKit.post(new PushOrderToAllShunFengChe(order));
            return order;
        } else {
            return null;
        }
    }


    public BigDecimal price(TraverRecord traverRecord, int people, int type, Company company, MemberInfo memberInfo) {
        List<CalculationDto> calculationDtos = CalService.getInstance().calculationDtoSetUp2(company, memberInfo, traverRecord.getDistance(), people);
        BigDecimal price = BigDecimal.ZERO;
        for (CalculationDto calculationDto : calculationDtos) {
            if (type != 3) {
                if (calculationDto.getPdFlag() == (type == 1 ? 1 : 0) && calculationDto.getTypeId() == (traverRecord.getRecordType() == 1 ? Constant.ServiceItemType.ShiNei : traverRecord.getRecordType() == 2 ? Constant.ServiceItemType.KuaCheng : traverRecord.getRecordType() == 3 ? Constant.ServiceItemType.DaiHuo : 0)) {
                    price = calculationDto.getTotalAmount();
                    break;
                }
            } else {
                if (calculationDto.getPdFlag() == 0 && calculationDto.getTypeId() == Constant.ServiceItemType.DaiHuo) {
                    price = calculationDto.getTotalAmount();
                    break;
                }
            }
        }
        return price;
    }


    private GetTraverService() {
    }

    public static GetTraverService
    getInstance() {
        if (getTraverService == null) {
            getTraverService = new GetTraverService();
        }
        return getTraverService;
    }
}
