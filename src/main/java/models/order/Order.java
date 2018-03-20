package models.order;

import annotation.TableBind;
import base.Constant;
import base.models.BaseOrder;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.xiaoleilu.hutool.date.DateUtil;
import kits.StringsKit;
import models.member.MemberInfo;
import models.member.MemberLogin;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/5.
 */
@TableBind(tableName = "dele_order")
public class Order extends BaseOrder<Order> {
    public static Order dao = new Order();
    
    
    /**
     * 通过订单查询更多跟订单相关的信息
     *
     * @param orderId
     * @return
     */
    
    /**
     * 代驾
     *
     * @param orderId
     * @return
     */
    public Order findMoreById(int orderId) {
        return findFirst(SqlManager.sql("order.findMoreById"), orderId);
    }
    
    /**
     * @param start
     * @param end
     * @return
     */
    public Order findCountByStatusServiceType1(boolean superAdmin, int companyId, String start, String end, int status, int serviceType) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("order.find1"), serviceType, status, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("order.find1"), "AND dd.company=?"), serviceType, status, start, end, companyId);
        }
    }
    
    
    public Order findCountByStatusServiceType2(boolean superAdmin, int companyId, String start, String end, int status, int serviceType) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("order.find2"), serviceType, status, start, end, Constant.CapitalOperationType.BAOXIAN);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("order.find2"), "AND dd.company=?"), serviceType, status, start, end, Constant.CapitalOperationType.BAOXIAN, companyId);
        }
    }
    
    public Order findCountByStatusServiceType3(boolean superAdmin, int companyId, String start, String end, int status, int serviceType) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("order.find4"), Constant.CapitalOperationType.BAOXIAN, serviceType, status, start, end,
            Constant.CompanyOperteType.BXF, serviceType, status, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("order.find4"), "AND dd.company=?"),
            Constant.CapitalOperationType.BAOXIAN, serviceType, status, start, end, companyId,
            Constant.CompanyOperteType.BXF, serviceType, status, start, end, companyId);
        }
    }
    
    
    /**
     * 订单来源统计
     */
    public Order findApp(boolean superAdmin, int type, String start, String end, int companyId) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("orderfrom.app"), type, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("orderfrom.app"), "and company = ?"), type, start, end, companyId);
        }
        
    }
    
    public Order findWeinxin(boolean superAdmin, int type, String start, String end, int companyId) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("orderfrom.weixin"), type, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("orderfrom.weixin"), "and company = ?"), type, start, end, companyId);
        }
        
    }
    
    public Order findPhone(boolean superAdmin, int type, String start, String end, int companyId) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("orderfrom.phone"), type, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("orderfrom.phone"), "and company = ?"), type, start, end, companyId);
        }
        
    }
    
    public Order findService(boolean superAdmin, int type, String start, String end, int companyId) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("orderfrom.service"), type, start, end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("orderfrom.service"), "and company = ?"), type, start, end, companyId);
        }
        
    }
    
    /**
     * 每月订单统计
     */
    public List<Order> findFinish(boolean superAdmin, int year, int type, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.year1"), Constant.OrderStatus.PAYED, year, type);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.year1"), " and dd.company = ?"), Constant.OrderStatus.PAYED, year, type, companyId);
        }
        
    }
    
    public List<Order> findDestroy(boolean superAdmin, int year, int type, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.year3"), Constant.OrderStatus.CANCEL, year, type);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.year3"), "and dd.company = ?"), Constant.OrderStatus.CANCEL, year, type, companyId);
        }
        
    }
    
    public List<Order> findAmount(boolean superAdmin, int year, int type, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.year2"), year, type);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.year2"), " and dd.company = ?"), year, type, companyId);
        }
        
    }
    
    /**
     * 每天订单统计
     */
    public List<Order> findFinish1(boolean superAdmin, int month, int type, int year, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.month1"), Constant.OrderStatus.PAYED, month, type, year);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.month1"), "and dd.company = ?"), Constant.OrderStatus.PAYED, month, type, year, companyId);
        }
        
    }
    
    public List<Order> findDestroy1(boolean superAdmin, int month, int type, int year, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.month3"), Constant.OrderStatus.CANCEL, month, type, year);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.month3"), "and dd.company = ?"), Constant.OrderStatus.CANCEL, month, type, year, companyId);
        }
        
    }
    
    public List<Order> findAmount1(boolean superAdmin, int month, int type, int year, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.month2"), month, type, year);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.month2"), " and dd.company = ?"), month, type, year, companyId);
        }
        
    }
    
    /**
     * 每小时订单统计
     */
    
    public List<Order> findFinish2(boolean superAdmin, int day, int type, int year, int month, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.day1"), Constant.OrderStatus.PAYED, day, type, year, month);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.day1"), " and dd.company = ?"), Constant.OrderStatus.PAYED, day, type, year, month, companyId);
        }
        
    }
    
    public List<Order> findDestroy2(boolean superAdmin, int day, int type, int year, int month, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.day3"), Constant.OrderStatus.CANCEL, day, type, year, month);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.day3"), " and dd.company = ?"), Constant.OrderStatus.CANCEL, day, type, year, month, companyId);
        }
        
    }
    
    public List<Order> findAmount2(boolean superAdmin, int day, int type, int year, int month, int companyId) {
        if (superAdmin) {
            return find(SqlManager.sql("order.day2"), day, type, year, month);
        } else {
            return find(StringsKit.replaceSql(SqlManager.sql("order.day2"), " and dd.company = ?"), day, type, year, month, companyId);
        }
        
    }
    
    
    /**
     * 通过客户id查询客户的总订单数
     *
     * @param id
     * @return
     */
    public int findCompleteCountByMember(Integer id, int complete) {
        Number c = findFirst(SqlManager.sql("order.findCompleteCountByMember"), id, complete).getNumber("c");
        return c == null ? 0 : c.intValue();
    }
    
    /**
     * 通过客户id查询客户的总订单数
     *
     * @param id
     * @return
     */
    public int findCountByMember(Integer id) {
        Number c = findFirst(SqlManager.sql("order.findCountByMember"), id).getNumber("c");
        return c == null ? 0 : c.intValue();
    }
    
    /**
     * 通过过日期查询这个期间的客户完成订单数量
     *
     * @param id
     * @param startOfMonth
     * @param endOfMonth
     * @return
     */
    public int findCompleteCountByMemberAndDate(Integer id, Date startOfMonth, Date endOfMonth, int complete) {
        Number c = findFirst(SqlManager.sql("order.findCompleteCountByMemberAndDate"), id, startOfMonth, endOfMonth, complete).getNumber("c");
        return c == null ? 0 : c.intValue();
    }
    
    /**
     * 通过过日期查询这个期间的客户订单数量
     *
     * @param id
     * @param startOfMonth
     * @param endOfMonth
     * @return
     */
    public int findCountByMemberAndDate(Integer id, Date startOfMonth, Date endOfMonth) {
        Number c = findFirst(SqlManager.sql("order.findCountByMemberAndDate"), id, startOfMonth, endOfMonth).getNumber("c");
        return c == null ? 0 : c.intValue();
    }
    
    /**
     * 查询司机完成的订单
     *
     * @param id
     * @param pageStart
     * @param pageSize
     * @return
     */
    public List<Order> findByDriver(int id, int status, int pageStart, int pageSize) {
        return find(SqlManager.sql("order.findByDriver"), id, status, pageStart, pageSize);
    }
    
    /**
     * 查询这个司机没有完成的订单信息
     *
     * @param id
     * @return
     */
    public List<Order> findByDriverNoComplete(Integer id) {
        /*1.查询不是预约的订单*/
        List<Order> a = find(SqlManager.sql("order.findByDriverNoComplete"), id, Constant.OrderStatus.CANCEL, Constant.OrderStatus.PAYED, Constant.OrderStatus.CREATE, Constant.OrderStatus.END, Constant.OrderStatus.PAYED, false);
        /*2.查询是预约的拼车单*/
        a.addAll(find(SqlManager.sql("order.findByDriverNoCompleteAndYuyue"), id, Constant.OrderStatus.ACCEPT, Constant.OrderStatus.CANCEL, Constant.OrderStatus.PAYED, Constant.OrderStatus.CREATE, Constant.OrderStatus.END, Constant.OrderStatus.PAYED, true));
        return a;
    }
    
    /**
     * 通过订单号查询订单信息
     *
     * @param trade_no
     * @return
     */
    public Order findByNo(String trade_no) {
        return findFirst(SqlManager.sql("order.findByNo"), trade_no);
    }
    
    /**
     * 我的历史订单信息
     *
     * @param id
     * @param start
     * @param end
     * @param pageStart
     * @param pageSize
     * @return
     */
    public List<Order> findByMemberAndDateServiceTypePage(Integer id, int serviceType, Date start, Date end, int pageStart, int pageSize) {
        if (serviceType == Constant.ServiceType.DaiJia) {
            return find(SqlManager.sql("order.findByMemberAndDateServiceTypePage").replace("distance", "real_distance distance"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        } else if (serviceType == Constant.ServiceType.KuaiChe) {
            return find(SqlManager.sql("order.findByMemberAndDateServiceTypePage").replace("distance", "IF(pd_flag,distance,real_distance) distance"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        } else {
            return find(SqlManager.sql("order.findByMemberAndDateServiceTypePage"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        }
    }
    
    public List<Order> findByDriverAndDateServiceTypePage(Integer id, int serviceType, Date start, Date end, int pageStart, int pageSize) {
        if (serviceType == Constant.ServiceType.DaiJia) {
            return find(SqlManager.sql("order.findByDriverAndDateServiceTypePage").replace("distance", "real_distance distance"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        } else if (serviceType == Constant.ServiceType.KuaiChe) {
            return find(SqlManager.sql("order.findByDriverAndDateServiceTypePage").replace("distance", "IF(pd_flag,distance,real_distance) distance"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        } else {
            return find(SqlManager.sql("order.findByDriverAndDateServiceTypePage"), id, serviceType, Constant.OrderStatus.PAYED, Constant.OrderStatus.PAYED, Constant.OrderStatus.END, start, end, pageStart, pageSize);
        }
    }
    
    /**
     * 查询会员没有支付的订单和未完成的订单
     *
     * @param id
     * @return
     */
    public List<Order> findByMemberForNoPay(Integer id) {
        List<Order> orders = Lists.newArrayList();
        orders.addAll(find(SqlManager.sql("order.findByMemberForNoPay"), id, Constant.OrderStatus.CREATE, Constant.OrderStatus.CANCEL, Constant.OrderStatus.PAYED, false));
        orders.addAll(find(SqlManager.sql("order.findByMemberForNoPayYuyue"), id, Constant.OrderStatus.ACCEPT, Constant.OrderStatus.CREATE, Constant.OrderStatus.CANCEL, Constant.OrderStatus.PAYED, true));
        orders.addAll(find(SqlManager.sql("order.findByMemberForZhuanXianNoPay"), id, Constant.OrderStatus.END));
        return orders;
    }
    
    public List<Order> findByMemberForExecute(Integer memberId, Integer id) {
        List<Order> orders = Lists.newArrayList();
        if (id == null) {
            orders.addAll(find(SqlManager.sql("order.findByMemberForExecute2"), memberId));
        } else {
            orders.addAll(find(SqlManager.sql("order.findByMemberForExecute"), memberId, id));
        }
        return orders;
    }
    
    
    /**
     * 查询会员有没有专线订单未出行
     *
     * @param id
     * @return
     */
    public List<Order> findByMemberForhasorder(Integer id) {
        
        return find(SqlManager.sql("order.findByMemberForhasorder"), id, Constant.OrderStatus.CANCEL, Constant.OrderStatus.PAYED, Constant.ServiceType.ZhuanXian);
    }
    
    /**
     * 查询会员支付完成的一个订单
     *
     * @param memberId
     * @return
     */
    public List<Order> findByMemberComplete(int memberId) {
        return find(SqlManager.sql("order.findByMemberComplete"), memberId, Constant.OrderStatus.PAYED, Constant.PayStatus.PAYED);
    }
    
    public List<Order> findByDriverComplete(int driverInfoId) {
        return find(SqlManager.sql("order.findByDriverComplete"), driverInfoId, Constant.OrderStatus.PAYED, Constant.PayStatus.PAYED);
    }
    
    /**
     * 查询完成的订单数量
     *
     * @param memberId
     * @return
     */
    public int findCountByMemberComplete(int memberId) {
        return findFirst(SqlManager.sql("order.findCountByMemberComplete"), memberId, Constant.OrderStatus.PAYED).getNumber("c").intValue();
    }
    
    /**
     * 查询完成的订单数量
     *
     * @param memberId
     * @param startTime
     * @param endTime
     * @return
     */
    public int findCountByMemberCompleteAndDate(int memberId, Date startTime, Date endTime) {
        return findFirst(SqlManager.sql("order.findByMemberCompleteAndDate"), memberId, Constant.OrderStatus.PAYED, startTime, endTime).getNumber("c").intValue();
    }
    
    public BigDecimal getRealDistance() {
        if (get("real_distance") == null) {
            return BigDecimal.ZERO;
        } else {
            return get("real_distance");
        }
    }
    
    /**
     * 获取未派单的预约订单
     *
     * @param start
     * @param end
     * @return
     */
    public List<Order> findByDateAndStatus(Date start, Date end) {
        return find(SqlManager.sql("order.findByDateAndStatus"), start, end, Constant.OrderStatus.CREATE, true);
    }
    
    /**
     * 查询司机是否有预约订单
     *
     * @param id
     * @return
     */
    public int findCountByDriverYuyue(Integer id) {
        Date now = DateTime.now().plusMinutes(-5).toDate();
        return Db.findFirst(SqlManager.sql("order.findCountByDriverYuyue"), true, id, Constant.OrderStatus.ACCEPT, now).getNumber("c").intValue();
    }
    
    /**
     * 查询司机是否有订单
     *
     * @param id
     * @return
     */
    public int findOrderByDriver(Integer id) {
        return Db.findFirst(SqlManager.sql("order.findOrderByDriver"), id, Constant.OrderStatus.ACCEPT, Constant.OrderStatus.START, Constant.OrderStatus.DRIVERWAIT).getNumber("c").intValue();
    }
    
    
    public List<Order> findYuyuedingdanByDriver(Integer id) {
        List<Object> params = Lists.newArrayList();
        params.add(id);
        params.add(Constant.OrderStatus.ACCEPT);
        params.add(true);
        params.add(DateTime.now().plusMinutes(-15).toDate());
        return find(SqlManager.sql("order.findYuyuedingdanByDriver"), params.toArray());
    }
    
    public List<Order> findYuyuedingdanByMember(Integer id) {
        List<Object> params = Lists.newArrayList();
        params.add(id);
        params.add(Constant.OrderStatus.ACCEPT);
        params.add(true);
        params.add(DateTime.now().plusMinutes(-15).toDate());
        return find(SqlManager.sql("order.findYuyuedingdanByMember"), params.toArray());
    }
    
    /**
     * 查询预约订单
     *
     * @param start
     * @param end
     * @return
     */
    public List<Order> findByYuyueAndDate(Date start, Date end) {
        return find(SqlManager.sql("order.findByYuyueAndDate"), Constant.OrderStatus.ACCEPT, true, start, end);
    }
    
    public List<Order> findNoInvoiceByMember(int memberId, int start, int pageSize) {
        return find(SqlManager.sql("order.findNoInvoiceByMember"), memberId, Constant.OrderStatus.PAYED, start, pageSize);
    }
    
    /**
     * 通过订单id查询
     *
     * @param orderIds
     * @return
     */
    @SuppressWarnings("MethodCanBeVariableArityMethod")
    public List<Order> findByIds(String[] orderIds) {
        if (orderIds.length > 0) {
            String where = Strings.repeat("?,", orderIds.length);
            where = where.substring(0, where.length() - 1);
            String sql = SqlManager.sql("order.findByIds");
            sql = sql.replace("INSQL", where);
            return find(sql, orderIds);
        } else {
            return null;
        }
    }
    
    public int pdOrderCount(int driverId, int status) {
        return findFirst(SqlManager.sql("order.pinche"), driverId, status).getNumber("c").intValue();
    }
    
    public List<Order> findByTraver(int status, int traver, int pd) {
        if (pd == 2) {
            return find("select * from dele_order where traverId = ? and status <= ? and status != 6 and pd_flag is null", traver, status);
        } else if (pd == 3) {
            return find("select * from dele_order where traverId = ? and status<= ? and status != 6", traver, status);
        } else {
            return find("select * from dele_order where traverId = ? and status <= ? and status != 6 and pd_flag = ?", traver, status, pd);
        }
    }
    
    public Order findByTraverId(int traverId) {
        return findFirst("select * from dele_order where traverId = ?", traverId);
    }
    
    /**
     * 客户查询自己的行程订单
     *
     * @return
     */
    public Order findByTraverId(int traverId, int status, int memberId) {
        return findFirst("select * from dele_order where traverId = ? and status = ? and member= ?", traverId, status, memberId);
    }
    
    /**
     * 根据司机该司机已接人数
     *
     * @param driverId
     * @return
     */
    
    public int findPeopleNumber(int driverId) {
        Order order = findFirst(SqlManager.sql("order.findPeopleNumberByDriverId"), driverId, Constant.OrderStatus.ACCEPT);
        int number;
        System.out.println(order.getPeople());
        if (order.getPeople() == null) {
            number = 0;
        } else {
            number = order.getNumber("number").intValue();
        }
        return number;
    }
    
    //查询该司机是有没有包车专线订单
    public Order findOrderPd(Order order) {
        Order orderpd = findFirst(SqlManager.sql("order.findOrderPd"), Constant.ServiceType.ZhuanXian, order.getId(), Constant.OrderStatus.ACCEPT);
        return orderpd;
    }
    
    //运营证获取发起订单信息
    public List<Order> findOrders() {
        return find(SqlManager.sql("orderport.findorders"));
    }
    
    //订单成功
    public List<Order> findSuccess() {
        return find(SqlManager.sql("orderport.match"));
    }
    
    //订单撤销
    public List<Order> findDestroy() {
        return find(SqlManager.sql("orderport.cancel"));
    }
    
    
    /**
     * 私人小客车合乘驾驶员行程发布信息
     *
     * @return
     */
    public List<Order> shareRoute() {
        return find(SqlManager.sql("orderport.shareRoute"));
    }
    
    /**
     * 私人小客车合乘订单发布信息
     *
     * @return
     */
    public List<Order> shareOrder() {
        return find(SqlManager.sql("orderport.shareOrder"));
    }
    
    /**
     * 私人小客车合乘订单支付信息
     *
     * @return
     */
    public List<Order> sharePay() {
        return find(SqlManager.sql("orderport.sharePay"));
    }
    
    /**
     * 统计不同类型的订单个数
     *
     * @return
     */
    public List<Order> findOrdersRateByAll() {
        return find(SqlManager.sql("order.findOrdersRateByAll"));
    }
    
    
    /**
     * 统计用户不同类型的订单个数
     *
     * @return
     */
    public List<Order> findOrdersRateById(int driverId) {
        return find(SqlManager.sql("order.findOrdersRateById"), driverId, driverId, driverId, driverId, driverId, driverId, driverId);
    }
    
    /**
     * 统计用户不同类型的订单个数  by Time
     *
     * @return
     */
    public List<Order> findOrdersRateByIdAndTime(int driverId, DateTime bTime, DateTime eTime) {
        String where = "  AND  create_time BETWEEN '" + bTime + "' AND '" + eTime + "' ";
        return find(StringsKit.replaceSql(SqlManager.sql("order.findOrdersRateByIdAndTime"), where), driverId, driverId, driverId, driverId, driverId, driverId, driverId);
    }
    
    public Order findOrder(TraverRecord traverRecord, MemberLogin memberLogin) {
        return findFirst(SqlManager.sql("order.findOrder"), traverRecord.getId(), MemberInfo.dao.findByLoginId(memberLogin.getId()).getId(), Constant.OrderStatus.CREATE);
    }
    
    
    /**
     * 交通部接口数据添加A36表
     *
     * @return
     */
    public List<Order> findDataAddA36() {
        return find(SqlManager.sql("order.findDataAddA36"));
        
    }
    
    /**
     * 交通部接口数据添加A32表
     *
     * @return
     */
    public List<Order> findDataAddA32() {
        return find(SqlManager.sql("order.findDataAddA32"));
        
    }
    
    /**
     * 根据订单号查询订单信息
     *
     * @return
     */
    public Order findOnebyNo(String no) {
        return findFirst("SELECT * FROM dele_order WHERE no= ?", no);
        
    }
    
    /**
     * 交通部接口数据添加A34表
     *
     * @return
     */
    public List<Order> findDataAddA34() {
        return find(SqlManager.sql("order.findDataAddA34"));
        
    }
    
    
    /**
     * 交通部接口数据添加A46表
     *
     * @return
     */
    public List<Order> findDataAddA46() {
        return find(SqlManager.sql("order.findDataAddA46"));
        
    }
    
    /**
     * 交通部接口数据添加A46表
     *
     * @return
     */
    public List<Order> findDataAddA48() {
        return find(SqlManager.sql("order.findDataAddA48"));
        
    }
    
    
    /**
     * 交通部接口数据添加A62表
     *
     * @return
     */
    public List<Order> findDataAddA62() {
        return find(SqlManager.sql("order.findDataAddA62"));
        
    }
    
    /**
     * 交通部接口数据添加A66表
     *
     * @return
     */
    public List<Order> findDataAddA66() {
        return find(SqlManager.sql("order.findDataAddA66"));
        
    }
    
    
    public List<Order> findOrderAll(String orderId) {
        return find(SqlManager.sql("order.findOrderAll"), orderId);
    }
    
    
    /**
     * 查询司机需要执行的顺风车订单
     *
     * @param driverId
     * @return
     */
    public int findCountShunByDid(int driverId) {
        return findFirst(SqlManager.sql("order.findCountShunByDid"), driverId).getNumber("c").intValue();
    }
    
    /**
     * 因为代驾莫名出现多个时间相同的订单，暂定将当前订单的其他相同订单去除
     *
     * @param ord
     * @return
     */
    public List<Order> findIllegalOrder(Order ord) {
        return find(SqlManager.sql("order.findIllegalOrder"), Constant.OrderStatus.ACCEPT, ord.getServiceType(), ord.getSetouttime(), ord.getDriver(), ord.getNo());
    }
    
    //用于完成订单统计
    public List<Order> findOrderByPayTime(Date time) {
        return find(SqlManager.sql("order.findOrderByPayTime"), time, DateUtil.date());
        
    }
    
    //获取司机进行中的数
    public int findByOnlineScanning(Integer driverId) {
        return findFirst(SqlManager.sql("order.findByOnlineScanning"), Constant.OrderStatus.PAYED, Constant.OrderStatus.CANCEL,Constant.OrderStatus.END,Constant.OrderStatus.CREATE,Constant.OrderStatus.SFCACCEPT, driverId).getNumber("c").intValue();
    }
    
    
    public List<Order> findByPhone(String phone) {
        return find(SqlManager.sql("order.findByPhone"), phone);
    }
}
