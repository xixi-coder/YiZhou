package services;

import base.Constant;
import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import jobs.pushorder.PushOrderToSpecial;
import kits.StringsKit;
import kits.TimeKit;
import models.company.Company;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.special.SpecialCar;
import models.special.SpecialLineOrder;
import net.dreamlu.event.EventKit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public class GetSpecialService {
    private Logger logger = LoggerFactory.getLogger(GetSpecialService.class);
    private static GetSpecialService getSpecialService;
    public List<SpecialCar> getLine(int type, int lineType, String sCityCode, int pageStart, int pageSize) {

        List<SpecialCar> specialCarList = SpecialCar.dao.lineListByCity(type,lineType,sCityCode,pageStart,pageSize);
//        if (specialCarList.size()<=0) {
//            if (type == Constant.DRIVER){
//                specialCarList = SpecialCar.dao.lineList(type,lineType,driverInfo,pageStart,pageSize);
//            }else {
//                specialCarList = SpecialCar.dao.memberGetLineList(type,lineType,memberInfo,pageStart,pageSize);
//            }
//        }
        return specialCarList;
    }

    //获取个人专线行程列表
    public List<SpecialCar> getPersonalLine (int type, int memberId, int pageStart, int pageSize) {
        List<SpecialCar> lineList = new ArrayList<SpecialCar>();

        if (type == Constant.DRIVER){
            lineList = SpecialCar.dao.lineListByDriverId(type,memberId,pageStart,pageSize);
        }else {
            List<SpecialCar> lineList1;
            lineList = SpecialCar.dao.lineListByMemberId(type,memberId,pageStart,pageSize);
            lineList1 = SpecialCar.dao.lineListByMemberId1(type,memberId,pageStart,pageSize);
            lineList.addAll(lineList1);
        }
        return lineList;
    }

    //获取已接订单列表
    public List<SpecialCar> getOrder (int driverId) {
        List<SpecialCar> lineList = new ArrayList<SpecialCar>();

        lineList = SpecialCar.dao.getOrder(driverId);

        return lineList;
    }

    //删除个人行程
    public List<SpecialCar> deletePersonalLine (int type,int memberid, int lineId) {
        List<SpecialCar> lineList = new ArrayList<SpecialCar>();

        int deleteline = SpecialCar.dao.deleteline(type,memberid,lineId);

        return lineList;
    }

    //生成专线订单
    public Order createOrder(final SpecialCar specialCar, int flag, String sCityCode, MemberInfo memberInfo, MemberLogin memberLogin, int people) {
        Company company;
        if (Strings.isNullOrEmpty(sCityCode)) {//判断是否有cityCode，如果没有就用用户自己的公司
            company = Company.dao.findByCompanyId(memberInfo.getCompany());
        } else {
            company = Company.dao.findByCity(sCityCode);
        }
        final int orderType = specialCar.getType();//专线订单
        final Order order = new Order();
        String no = StringsKit.getOrderNo();
        order.setNo(no);
        order.setPeople(people);
        order.setFromType(Constant.FromType.APPTYPE);
        Date time = DateTime.now().toDate();
        boolean type = (flag == 1?true:false);
        if (specialCar.getSetoutTime1() != null && specialCar.getSetoutTime2() != null){
            DateTime time1 = new DateTime(DateTime.now());
            DateTime time2,time3;
            time1 = DateTime.parse(time1.toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
            time2 = DateTime.parse(new DateTime(specialCar.getSetoutTime1()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
            time3 = DateTime.parse(new DateTime(specialCar.getSetoutTime2()).toString(DateTimeFormat.forPattern(TimeKit.HHMMSS)), DateTimeFormat.forPattern(TimeKit.HHMMSS));
            if(time1.isAfter(time2) && time1.isBefore(time3)){
                if (flag == 1){
                    BigDecimal p = specialCar.getSharingEsyimatedPrice();
                    BigDecimal price = BigDecimal.valueOf(Double.valueOf(people)).multiply(p);
                    order.setAmount(price);//订单价格
                }else {
                    order.setAmount(specialCar.getEstimatedPrice());//订单价格
                }
            }else {
                if (flag == 1){
                    BigDecimal p = specialCar.getSharingPrice();
                    BigDecimal price = BigDecimal.valueOf(Double.valueOf(people)).multiply(specialCar.getSharingPrice());
                    order.setAmount(price);//订单价格
                }else {
                    order.setAmount(specialCar.getIndexPrice());//订单价格
                }
            }
        }else {
            if (flag == 1){
                BigDecimal p = specialCar.getSharingPrice();
                BigDecimal price = BigDecimal.valueOf(Double.valueOf(people)).multiply(specialCar.getSharingPrice());
                order.setAmount(price);//订单价格
            }else {
                order.setAmount(specialCar.getIndexPrice());//订单价格
            }
        }

        order.setPdFlag(type);//是否拼单
        order.setFromCompany(company.getId());//设置订单的公司
        order.setCompany(company.getId());//设置订单的公司
        order.setCreateTime(DateTime.now().toDate());
        order.setDistance(BigDecimal.valueOf(specialCar.getDistance()));
        order.setMember(memberInfo.getId());
        order.setPhone(memberInfo.getPhone());
        order.setType(orderType);
        order.setServiceType(Constant.ServiceType.ZhuanXian);
        order.setRemark("通过APP下单");
        order.setSetOutFlag(false);
        order.setStatus(Constant.OrderStatus.CREATE);
        order.setPayStatus(Constant.PayStatus.NOPAY);
        order.setRealDistance(BigDecimal.valueOf(specialCar.getDistance()));
        order.setDestination(specialCar.getDestination());
        order.setReservationAddress(specialCar.getReservationAddress());
        order.put("lineId",specialCar.getId());
        final OrderLog orderLog = new OrderLog();
        orderLog.setAction(Constant.OrderStatus.CREATE);
        orderLog.setLoginId(memberLogin.getId());
        orderLog.setOperater(memberLogin.getUserName());
        orderLog.setRemark(memberLogin.getUserName() + "创建了一个订单");
        orderLog.setOperationTime(time);
        final OrderTrip orderTrip = new OrderTrip();
        orderTrip.setStartLatitude(specialCar.getStartLatitude());
        orderTrip.setStartLongitude(specialCar.getStartLongitude());
        orderTrip.setEndLatitude(specialCar.getEndLatitude());
        orderTrip.setEndLongitude(specialCar.getEndLongitude());
        orderTrip.setMember(memberInfo.getId());
        final SpecialLineOrder specialLineOrder = new SpecialLineOrder();
        specialLineOrder.setCreatetime(DateTime.now().toDate());
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (order.save()) {
                    orderTrip.setOrderId(order.getId());
                    orderLog.setOrderId(order.getId());
                    specialLineOrder.setOrderId(order.getId());
                    specialLineOrder.setSpecialcarId(specialCar.getId());
                    return orderTrip.save() && orderLog.save() && specialLineOrder.save();
                } else {
                    return false;
                }
            }
        });
        if (isOk) {
            EventKit.post(new PushOrderToSpecial(order));
            return order;
        } else {
            return null;
        }
    }


    private GetSpecialService() {
    }

    public static GetSpecialService getInstance(){
        if (getSpecialService == null) {
            getSpecialService = new GetSpecialService();
        }
        return getSpecialService;
    }
}
