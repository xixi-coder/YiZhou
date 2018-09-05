package controllers.admin.order;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

import net.dreamlu.event.EventKit;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import base.datatable.DataTablePage;
import jobs.pushorder.CancelOrder;
import jobs.pushorder.PushOrder;
import models.activity.MemberCoupon;
import models.company.Company;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.member.MemberAppraise;
import models.member.MemberCapitalAccount;
import models.member.MemberComplain;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.reject.RejectLog;
import models.sys.AdminSetting;
import models.sys.Schedule;
import models.sys.ServiceType;
import models.sys.ServiceTypeItem;
import models.sys.User;
import plugin.sqlInXml.SqlManager;
import services.CalService;
import services.PushOrderService;
import utils.DateUtil;

/**
 * Created by admin on 2016/9/23.
 */
@Controller("/admin/order")
public class OrderController extends BaseAdminController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    public void index() {
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
        int type = getParaToInt("type", 2);
        setAttr("type", type);
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt("type", 2);
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(false);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.where"), params, SqlManager.sql("order.columnPage"), SqlManager.sql("order.wherePage")));
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            renderJson(dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.where"), whereSql, params, SqlManager.sql("order.columnPage"), SqlManager.sql("order.wherePage")));
        }
    }

    public void lockup() {
        int orderId = getParaToInt(0, 0);
        Order order = Order.dao.findMoreById(orderId);
        if (orderId == 0) {
            String no = getPara("no");
            order = Order.dao.findByNo(no);
            orderId = order.getId();
        }

        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        order.put("memberInfo", memberInfo);
        Company company = Company.dao.findByCompanyId(order.getCompany());
        order.put("companyInfo", company);
        MemberCoupon couponInfo = MemberCoupon.dao.findById(order.getCoupon() == null ? 0 : order.getCoupon());
        order.put("couponInfo", couponInfo);
        DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
        List<OrderLog> orderLog;
        if (driverInfo == null) {
            orderLog = Lists.newArrayList();
        } else {
            orderLog = OrderLog.dao.findByOrderAndLoginId(orderId, driverInfo.getLoginId());
        }
        OrderLog createLog = OrderLog.dao.findByOrderAndPayAction(orderId, Constant.OrderAction.CREATE);
        OrderLog payLog = OrderLog.dao.findByOrderAndPayAction(orderId, Constant.OrderAction.PAYED);
        orderLog.add(0, createLog);
        orderLog.add(payLog);
        List<OrderLog> allOrderLogs = OrderLog.dao.findByOrder(orderId);

        if(null == order.get("consume_time")){
            Date startDate = new DateTime(allOrderLogs.get(2).get("operation_time")).toDate();
            Date endDate = new DateTime(allOrderLogs.get(allOrderLogs.size()-1).get("operation_time")).toDate();
            long endTime = endDate.getTime();
            long startTime = startDate.getTime();
            double tmpMinutes = (double)(endTime - startTime);
            double time = tmpMinutes/(1000 * 60);
            if(time >= 60){
                time = time /60;
                order.set("consume_time",time + "分钟");
            }else {
                DecimalFormat df = new DecimalFormat("#.00");
                String format = df.format(time);
                if(format.startsWith(".")){
                    order.set("consume_time", "0" + format+ "秒");
                }else {
                    order.set("consume_time", format+ "秒");
                }

            }
        }else {
            order.set("consume_time",order.get("consume_time") + "分钟");
        }

        setAttr("order",order);
        setAttr("orderLog", orderLog);
        setAttr("allOrderlogs", allOrderLogs);
        render("item.ftl");
    }

    //根据订单号获取评价信息
    public void getAppraiseByNo() {
        String no = getPara("no");
        MemberAppraise memberAppraise = MemberAppraise.dao.findOneByOrderId(no);
        if (memberAppraise == null) {
            memberAppraise = new MemberAppraise();
            memberAppraise.setOrderId(no);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        } else {
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        }
        setAttr("memberAppraise", memberAppraise);
        render("appraiseitem.ftl");
    }

    //评价新增或者保存
    public void appraiseSaveOrUpdate() {
        final MemberAppraise memberAppraise = getModel(MemberAppraise.class, "memberAppraise");
        if (memberAppraise.getId() != null) {
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return memberAppraise.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            Order order = Order.dao.findOnebyNo(memberAppraise.getOrderId());
            Calendar c = Calendar.getInstance();
            c.setTime(order.getLastUpdateTime());
            c.add(Calendar.MINUTE, (int) (Math.random() * 300));
            long evaluateTime = DateUtil.dateToLong(c.getTime());
            memberAppraise.setEvaluateTime(evaluateTime);
            if (memberAppraise.save()) {
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    //根据订单号获取投诉信息
    public void getComplainByNo() {
        String no = getPara("no");
        MemberComplain memberComplain = MemberComplain.dao.findOneByOrderId(no);
        if (memberComplain == null) {
            memberComplain = new MemberComplain();
            memberComplain.setOrderId(no);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        } else {
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        }
        setAttr("memberComplain", memberComplain);
        render("complainitem.ftl");
    }

    //投诉新增或者保存
    public void complainSaveOrUpdate() {
        final MemberComplain memberComplain = getModel(MemberComplain.class, "memberComplain");
        if (memberComplain.getId() != null) {
            String date = new SimpleDateFormat("YYYYMMDDhhmmss").format(new Date());

            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return memberComplain.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            Order order = Order.dao.findOnebyNo(memberComplain.getOrderId());
            Calendar c = Calendar.getInstance();
            c.setTime(order.getLastUpdateTime());
            c.add(Calendar.MINUTE, (int) (Math.random() * 300));
            long evaluateTime = DateUtil.dateToLong(c.getTime());
            memberComplain.setComplaintTime(evaluateTime);
            if (memberComplain.save()) {
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        Order order = Order.dao.findById(id);
        order.setDelFlag(true);
        if (order.update()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxFailure("删除失败！");
        }

    }

    public void add() {
        int id = getParaToInt("id", 0);
        Order order = Order.dao.findById(id);
        List<DriverInfo> driverInfos = order.get("drivers");
        if (order.getFromType() == Constant.FromType.TELTYPE && driverInfos == null) {
            renderAjaxError("司机不能为空！");
            return;
        } else if (order.getFromType() == Constant.FromType.TELTYPE && driverInfos != null) {
            PushOrderService.getIntance().pushToDriverSetList(order, driverInfos);
        } else {
            EventKit.post(new PushOrder(order));
        }
        renderAjaxSuccess("订单追加成功！");

    }

    public void location() {
        int orderId = getParaToInt(0, 0);
        if (orderId > 0) {
            Order order = Order.dao.findById(orderId);
            if (order.getDriver() == null) {
                renderAjaxNoData();
                return;
            }
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            List<OrderLog> orderLogs = OrderLog.dao.findByOrderAndLoginId(orderId, driverInfo.getLoginId());
            if (orderLogs.size() < 2) {
                renderAjaxNoData();
                return;
            }
            Date start = orderLogs.get(1).getOperationTime();
            Date end = orderLogs.get(orderLogs.size() - 1).getOperationTime();
            List<DriverHistoryLocation> driverHistoryLocations = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), start, end);
            renderJson(driverHistoryLocations);
        } else {
            renderAjaxNoData();
        }
    }

    public void create() {

    }

    public void createitem() {
        int memberId = getParaToInt(0, 0);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        if (memberId == 0) {
        } else {

        }
    }

    public void memberInfo() {
        String phone = getPara("phone");
        if (Strings.isNullOrEmpty(phone)) {
            renderAjaxNoData();
            return;
        }
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(phone);
        if (memberInfo == null) {
            renderAjaxError("暂无数据");
            return;
        }
        int totalOrderCount = Order.dao.findCountByMember(memberInfo.getId());
        Date startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMinimumValue().toDate();
        int totalOrderCountOfThisMonth = Order.dao.findCountByMemberAndDate(memberInfo.getId(), startOfMonth, endOfMonth);
        int realTotalOrderCount = Order.dao.findCompleteCountByMember(memberInfo.getId(), Constant.OrderStatus.PAYED);
        int realTotalOrderCountOfThisMonth = Order.dao.findCompleteCountByMemberAndDate(memberInfo.getId(), startOfMonth, endOfMonth, Constant.OrderStatus.PAYED);
        memberInfo.put("totalOrderCount", totalOrderCount);
        memberInfo.put("totalOrderCountOfThisMonth", totalOrderCountOfThisMonth);
        memberInfo.put("realTotalOrderCount", realTotalOrderCount);
        memberInfo.put("realTotalOrderCountOfThisMonth", realTotalOrderCountOfThisMonth);
        MemberInfo instMember = MemberInfo.dao.findByLoginId(memberInfo.getIntroducerLoginId() == null ? -1 : memberInfo.getIntroducerLoginId());
        if (instMember == null) {
            memberInfo.put("instPhone", "");
        } else {
            memberInfo.put("instPhone", instMember.getPhone());
        }
        renderAjaxSuccess(memberInfo);
    }

    public void celAmount() {
        int memberId = getParaToInt("mid", 0);
        MemberInfo memberInfo;
        if (memberId == 0) {
            memberInfo = null;
        } else {
            memberInfo = MemberInfo.dao.findById(memberId);
        }

        BigDecimal distance = new BigDecimal(Strings.isNullOrEmpty(getPara("distance")) ? "0" : getPara("distance"));
        if (distance.compareTo(BigDecimal.ZERO) > 0) {
            String strSetOutTime = getPara("setOutTime");
            if (!Strings.isNullOrEmpty(strSetOutTime)) {
                strSetOutTime += ":00";
            }
            Company company;
            if (isSuperAdmin()) {
                company = Company.dao.findByCompanyId(getParaToInt("company"));
            } else {
                company = getCompany();
            }
            BigDecimal times = new BigDecimal(Strings.isNullOrEmpty(getPara("time")) ? "0" : getPara("time"));
            renderAjaxSuccess(CalService.getInstance().calculationDtoSetUp1(Constant.ServiceType.DaiJia, company, memberInfo, strSetOutTime, distance, times));
        } else {
            renderAjaxError("计算距离错误，请重试！");
        }

    }

    public void memberlist() {
        String lon = getPara("lon");
        String lat = getPara("lat");
        if (Strings.isNullOrEmpty(lon) || Strings.isNullOrEmpty(lat)) {
            renderJson(new DataTablePage());
            return;
        } else {
            lon = lon.replace(",", ".");
            lat = lat.replace(",", ".");
        }
        List<Object> params = Lists.newArrayList();
        Date startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        Date startOfDay = DateTime.now().millisOfDay().withMinimumValue().toDate();
        Date endOfDay = DateTime.now().millisOfDay().withMaximumValue().toDate();
        params.add(lat);
        params.add(lon);
        params.add(startOfDay);
        params.add(endOfDay);
        params.add(startOfMonth);
        params.add(endOfMonth);
        params.add(Constant.LoginStatus.RECIVEDORDER);
        params.add(Constant.LoginStatus.RECIVEDORDER);
        params.add(Constant.DriverStatus.ShengHeTongGuo);
        int companyId;
        if (isSuperAdmin()) {
            companyId = getParaToInt("company");
        } else {
            companyId = getCompanyId();
        }
        params.add(companyId);
        params.add("%" + Constant.ServiceType.DaiJia + "%");
        params.add(Constant.ServiceItemType.DaiJia);
        renderJson(dataTable(SqlManager.sql("driverInfo.webColumn"), SqlManager.sql("driverInfo.webWhere"), params, SqlManager.sql("driverInfo.webColumnPage"), SqlManager.sql("driverInfo.webWherePage")));
    }

    public void memberlistZx() {
        OrderTrip orderTrip = OrderTrip.dao.findById(getPara("orderId"));
        if (orderTrip == null) {
            renderJson(new DataTablePage());
            return;
        }
        Double lon = orderTrip.getStartLongitude();
        Double lat = orderTrip.getStartLatitude();
        List<Object> params = Lists.newArrayList();
        Date startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        Date startOfDay = DateTime.now().millisOfDay().withMinimumValue().toDate();
        Date endOfDay = DateTime.now().millisOfDay().withMaximumValue().toDate();
        params.add(lat);
        params.add(lon);
        params.add(startOfDay);
        params.add(endOfDay);
        params.add(startOfMonth);
        params.add(endOfMonth);
        params.add(Constant.LoginStatus.RECIVEDORDER);
        params.add(Constant.LoginStatus.RECIVEDORDERPD);
        params.add(Constant.DriverStatus.ShengHeTongGuo);
        int companyId;
        if (isSuperAdmin()) {
            companyId = getParaToInt("company");
        } else {
            companyId = getCompanyId();
        }
        params.add(companyId);
        Order order = Order.dao.findById(getPara("orderId"));
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian) {
            params.add("%" + Constant.ServiceType.ZhuanXian + "%");
            params.add(Constant.ServiceItemType.KuaChengZhuanXianShangXian);
        }
        if (order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
            params.add("%" + Constant.ServiceType.HangKongZhuanXian + "%");
            params.add(Constant.ServiceItemType.HangKongZhuanXianShangXian);
        }
        try {
            DataTablePage DataTablePage = dataTable(SqlManager.sql("driverInfo.webColumn"), SqlManager.sql("driverInfo.webWhere"), params, SqlManager.sql("driverInfo.webColumnPage"), SqlManager.sql("driverInfo.webWherePage"));
            renderJson(DataTablePage);
        } catch (Exception e) {
            e.printStackTrace();
            renderAjaxError("查询快车司机失败");
        }

    }

    public void createbyadmin() {
        final Order order = getModel(Order.class, "order");
        final MemberInfo memberInfo;
        final MemberLogin memberLogin;
        if (MemberInfo.dao.findByPhone(getPara("memberInfo.phone")) == null) {
            memberInfo = new MemberInfo();
            memberInfo.setPhone(getPara("memberInfo.phone"));
            memberInfo.setRealName(getPara("memberInfo.real_name", "先生/女士"));
            memberInfo.setNickName(getPara("memberInfo.nick_name", "先生/女士"));
            memberLogin = new MemberLogin();
            memberLogin.setUserName(memberInfo.getPhone());
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setType(Constant.MEMBER);
            memberLogin.setCreateTime(DateTime.now().toDate());
        } else {
            memberLogin = null;
            memberInfo = MemberInfo.dao.findByPhone(getPara("memberInfo.phone"));
        }
        final OrderTrip orderTrip = getModel(OrderTrip.class, "trip");
        String driverIds = getPara("driverIds");
        if (Strings.isNullOrEmpty(driverIds)) {
            renderAjaxError("未选择接单司机");
            return;
        }
        if (memberInfo.getNickName() == null) {
            renderAjaxError("客户的姓名不能为空!");
            return;
        }
        if (memberInfo.getPhone() == null) {
            renderAjaxError("客户手机不能为空!");
            return;
        }
        if (orderTrip.getStartLongitude() == null ||
                orderTrip.getStartLatitude() == null
                ) {
            renderAjaxError("订单的起点没有选择");
            return;
        }
        /*if (order.getYgAmount() == null) {
            renderAjaxError("预估价格不能为空！");
            return;
        }*/

        //是否为预约订单
        if (getPara("setouttime") == null || getPara("setouttime").isEmpty()) {
            order.setSetouttime(DateTime.now().toDate());
            order.setSetOutFlag(false);
        } else {
            Date setOutTime = DateTime.parse(getPara("setouttime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")).toDate();
            //选择时间不能小于半个小时之内
            if (setOutTime.getTime() <= (DateTime.now().toDate().getTime() + 1800000)) {
                renderAjaxError("预约订单时间请选择至少半小时之后的时间");
                return;
            }
            order.setSetouttime(setOutTime);
            order.setSetOutFlag(true);
        }

        MemberInfo instMember = MemberInfo.dao.findByPhone(getPara("inst_phone"));
        if (instMember != null && !getPara("inst_phone").equals(memberInfo.getPhone())) {
            memberInfo.setIntroducerLoginId(instMember.getLoginId());
            memberInfo.setLastUpdateTime(DateTime.now().toDate());
            if (memberInfo.getId() != null) {
                memberInfo.update();
            }
        }
        final int companyId;
        if (isSuperAdmin()) {
            companyId = getParaToInt("company");
        } else {
            companyId = getCompanyId();
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (memberInfo.getId() == null) {
                    MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
                    if (!memberLogin.save()) {
                        return false;
                    }
                    memberCapitalAccount.setLoginId(memberLogin.getId());
                    memberCapitalAccount.setAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setPhone(memberInfo.getPhone());
                    memberCapitalAccount.setAccount(memberInfo.getPhone());
                    memberCapitalAccount.setTxAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setCreateTime(DateTime.now().toDate());
                    memberInfo.setCompany(companyId);
                    memberInfo.setLoginId(memberLogin.getId());
                    if (!memberInfo.save() || !memberCapitalAccount.save()) {
                        return false;
                    }
                }
                String no = Constant.AliPayOrderType.NomorlOrder + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(6).toUpperCase();
                order.setNo(no);
                order.setCreateTime(DateTime.now().toDate());
                order.setMember(memberInfo.getId());
                order.setPhone(memberInfo.getPhone());
                order.setPdFlag(false);
                order.setRealDistance(BigDecimal.ZERO);
                order.setStatus(Constant.OrderStatus.CREATE);
                order.setServiceType(Constant.ServiceType.DaiJia);
                List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.DaiJia);
                order.setType(serviceTypeItems.get(0).getId());
                order.setFromType(Constant.FromType.TELTYPE);
                order.setCompany(companyId);
                order.setFromCompany(companyId);
                OrderLog orderLog = new OrderLog();
                orderLog.setAction(Constant.OrderAction.CREATE);
                orderLog.setOperater(getUser().getUsername());
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setRemark(getUser().getUsername() + "后台创建了订单");
                orderLog.setLoginId(memberInfo.getLoginId());
                if (!order.save()) {
                    return false;
                }
                orderTrip.setOrderId(order.getId());
                orderLog.setOrderId(order.getId());
                return orderLog.save() && orderTrip.save();
            }
        });
        if (isOk) {
            order.put("trip", orderTrip);
            order.put("timeOut", AdminSetting.dao.findFirst().getOrderReciveTime());
            String[] driverId = driverIds.split(",");
            List<Object> params = Lists.newArrayList();
            Collections.addAll(params, driverId);
            List<DriverInfo> driverInfos = DriverInfo.dao.findByIds(params);
            order.put("drivers", driverInfos);
            try {
                PushOrderService.getIntance().pushToDriverSetList(order, driverInfos);
            } catch (Exception e) {
                logger.error("出现错误，错误信息{}", e.getMessage());
            }
            renderAjaxSuccess("成功！");
        } else {
            renderAjaxError("失败！");
        }
    }

    public void changeOrder() {
        int orderId = getParaToInt("orderId");
        Order order1 = Order.dao.findById(orderId);
        if (order1.getStatus() != Constant.OrderStatus.CREATE) {
            renderAjaxError("订单并未处于未派单状态，无法继续转单，请返回!");
            return;
        } else {
            cancelC(orderId);
        }
        final Order order = getModel(Order.class, "order");
        final MemberInfo memberInfo;
        final MemberLogin memberLogin;
        if (MemberInfo.dao.findByPhone(getPara("memberInfo.phone")) == null) {
            memberInfo = new MemberInfo();
            memberInfo.setPhone(getPara("memberInfo.phone"));
            memberInfo.setRealName(getPara("memberInfo.real_name"));
            memberInfo.setNickName(getPara("memberInfo.nick_name"));
            memberLogin = new MemberLogin();
            memberLogin.setUserName(memberInfo.getPhone());
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setType(Constant.MEMBER);
            memberLogin.setCreateTime(DateTime.now().toDate());
        } else {
            memberLogin = null;
            memberInfo = MemberInfo.dao.findByPhone(getPara("memberInfo.phone"));
        }
        final OrderTrip orderTrip = new OrderTrip();
        try {
            orderTrip.setEndLatitude(Double.valueOf(getPara("trip.end_latitude").replace(",", ".")));
            orderTrip.setEndLongitude(Double.valueOf(getPara("trip.end_longitude").replace(",", ".")));
        } catch (NumberFormatException n) {
            n.printStackTrace();
        }


        orderTrip.setStartLatitude(Double.valueOf(getPara("trip.start_latitude").replace(",", ".")));

        orderTrip.setStartLongitude(Double.valueOf(getPara("trip.start_longitude").replace(",", ".")));

        String driverIds = getPara("driverIds");
        if (Strings.isNullOrEmpty(driverIds)) {
            renderAjaxError("未选择接单司机");
            return;
        }
        if (memberInfo.getNickName() == null) {
            renderAjaxError("客户的姓名不能为空!");
            return;
        }
        if (memberInfo.getPhone() == null) {
            renderAjaxError("客户手机不能为空!");
            return;
        }
        if (orderTrip.getStartLongitude() == null ||
                orderTrip.getStartLatitude() == null
                ) {
            renderAjaxError("订单的起点没有选择");
            return;
        }
        if (order.getYgAmount() == null) {
            renderAjaxError("预估价格不能为空！");
            return;
        }
        if (getPara("setouttime") == null) {
            renderAjaxError("预约时间不能为空！");
            return;
        }
        Date setOutTime = DateTime.parse(getPara("setouttime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")).toDate();
        order.setSetouttime(setOutTime);
        if (order.getSetouttime().before(DateTime.now().toDate())) {
            order.setSetouttime(DateTime.now().plusMinutes(20).toDate());
        }
        MemberInfo instMember = MemberInfo.dao.findByPhone(getPara("inst_phone"));
        if (instMember != null && !getPara("inst_phone").equals(memberInfo.getPhone())) {
            memberInfo.setIntroducerLoginId(instMember.getLoginId());
            memberInfo.setLastUpdateTime(DateTime.now().toDate());
            if (memberInfo.getId() != null) {
                memberInfo.update();
            }
        }
        final int companyId;
        if (isSuperAdmin()) {
            companyId = getParaToInt("company");
        } else {
            companyId = getCompanyId();
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (memberInfo.getId() == null) {
                    MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
                    if (!memberLogin.save()) {
                        return false;
                    }
                    memberCapitalAccount.setLoginId(memberLogin.getId());
                    memberCapitalAccount.setAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setPhone(memberInfo.getPhone());
                    memberCapitalAccount.setAccount(memberInfo.getPhone());
                    memberCapitalAccount.setTxAmount(BigDecimal.ZERO);
                    memberCapitalAccount.setCreateTime(DateTime.now().toDate());
                    memberInfo.setCompany(companyId);
                    memberInfo.setLoginId(memberLogin.getId());
                    if (!memberInfo.save() || !memberCapitalAccount.save()) {
                        return false;
                    }
                }
                String no = Constant.AliPayOrderType.NomorlOrder + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(6).toUpperCase();
                order.setNo(no);
                order.setCreateTime(DateTime.now().toDate());
                order.setSetOutFlag(true);
                order.setMember(memberInfo.getId());
                order.setPhone(memberInfo.getPhone());
                order.setPdFlag(false);
                order.setRealDistance(BigDecimal.ZERO);
                order.setStatus(Constant.OrderStatus.CREATE);
                order.setServiceType(Constant.ServiceType.DaiJia);
                List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.DaiJia);
                order.setType(serviceTypeItems.get(0).getId());
                order.setFromType(Constant.FromType.TELTYPE);
                order.setCompany(companyId);
                order.setFromCompany(companyId);
                OrderLog orderLog = new OrderLog();
                orderLog.setAction(Constant.OrderAction.CREATE);
                orderLog.setOperater(getUser().getUsername());
                orderLog.setOperationTime(DateTime.now().toDate());
                orderLog.setRemark(getUser().getUsername() + "后台改派了订单");
                orderLog.setLoginId(memberInfo.getLoginId());
                if (!order.save()) {
                    return false;
                }
                orderTrip.setOrderId(order.getId());
                orderLog.setOrderId(order.getId());
                return orderLog.save() && orderTrip.save();
            }
        });
        if (isOk) {
            order.put("trip", orderTrip);
            order.put("timeOut", AdminSetting.dao.findFirst().getOrderReciveTime());
            String[] driverId = driverIds.split(",");
            List<Object> params = Lists.newArrayList();
            Collections.addAll(params, driverId);
            List<DriverInfo> driverInfos = DriverInfo.dao.findByIds(params);
            order.put("drivers", driverInfos);
            try {
                PushOrderService.getIntance().pushToDriverSetList(order, driverInfos);
            } catch (Exception e) {
                logger.error("出现错误，错误信息{}", e.getMessage());
            }
            renderAjaxSuccess("成功！");
        } else {
            renderAjaxError("失败！");
        }
    }

    /**
     * 取消订单
     */
    public void cancel() {
        Order order = Order.dao.findById(getPara("orderId"));
        String reason = getPara("reason");//销单原因
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian || order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
            Schedule.dao.updateByOrderId(order.getId());
        }
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        if (order.getStatus() == Constant.OrderStatus.PAYED) {
            renderAjaxError("订单已支付，无法取消订单！");
            return;
        }
        order.put("reason", reason);
        OrderLog orderLog = new OrderLog();
        User user = getUser();
        orderLog.setOperationTime(DateTime.now().toDate());
        orderLog.setOrderId(order.getId());
        orderLog.setRemark(user.getUsername() + "(后台)取消了订单" + "{" + reason + "}");
        orderLog.setAction(Constant.OrderAction.CANCEL);
        orderLog.setOperater(user.getName());
        orderLog.save();
        if (order.getDriver() != null) {
            try {
                DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
                MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
                if (order.getPdFlag()) {
                    int num = Order.dao.pdOrderCount(order.getDriver(), Constant.OrderStatus.ACCEPT);
                    if (num > 1) {
                        memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                    } else {
                        memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                    }
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
                }
                order.setStatus(Constant.OrderStatus.CANCEL);
                order.setLastUpdateTime(DateTime.now().toDate());
                order.update();
                memberLogin.update();
            } catch (NullPointerException n) {
                logger.error("ID为{}的司机被删除，无法找到司机！", order.getDriver());
            }
        }
        if (order.getServiceType() == Constant.ServiceType.ZhuanXian || order.getServiceType() == Constant.ServiceType.HangKongZhuanXian) {
            Schedule.dao.updateByOrderId(order.getId());
        }
        if (order.getDriver() == null) {
            order.setStatus(Constant.OrderStatus.CANCEL);
            order.setLastUpdateTime(DateTime.now().toDate());
            order.update();
        }
        EventKit.post(new CancelOrder(order));
        renderAjaxSuccess("取消订单成功！");
    }

    /**
     * 取消订单
     */
    public void cancelC(int orderID) {
        Order order = Order.dao.findById(orderID);
        if (order == null) {
            renderAjaxError("订单不存在");
            return;
        }
        order.put("reason", "转派订单");
        OrderLog orderLog = new OrderLog();
        User user = getUser();
        orderLog.setOperationTime(DateTime.now().toDate());
        orderLog.setOrderId(order.getId());
        orderLog.setRemark(user.getUsername() + "转派了该订单");
        orderLog.setAction(Constant.OrderAction.CANCEL);
        orderLog.setOperater(user.getName());
        orderLog.save();
        order.setStatus(Constant.OrderStatus.CANCEL);
        order.setLastUpdateTime(DateTime.now().toDate());
        order.update();

    }

    /**
     * 改派航空专线订单
     */
    public void changeHKOrder() {
        int id = getParaToInt(0);
        Order order = Order.dao.findById(id);
        Company company = Company.dao.findByCompanyId(order.getFromCompany());
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(order.getPhone());
        OrderTrip trip = OrderTrip.dao.findById(order.getId());
        setAttr("order", order);
        setAttr("company", company);
        setAttr("memberInfo", memberInfo);
        setAttr("trip", trip);
        render("changeHKOrder.ftl");
    }

    /**
     * 改派城际专线
     */
    public void changeCJOrder() {
        int id = getParaToInt(0);
        Order order = Order.dao.findById(id);
        Company company = Company.dao.findByCompanyId(order.getFromCompany());
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(order.getPhone());
        OrderTrip trip = OrderTrip.dao.findById(order.getId());
        setAttr("order", order);
        setAttr("company", company);
        setAttr("memberInfo", memberInfo);
        setAttr("trip", trip);
        render("changeCJOrder.ftl");
    }

    /**
     * 改派代驾订单
     */
    public void change() {
        int id = getParaToInt(0);
        Order order = Order.dao.findById(id);
        Company company = Company.dao.findByCompanyId(order.getFromCompany());
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(order.getPhone());
        OrderTrip trip = OrderTrip.dao.findById(order.getId());
        setAttr("order", order);
        setAttr("company", company);
        setAttr("memberInfo", memberInfo);
        setAttr("trip", trip);
        render("changeitem.ftl");
    }

    //专线派单
    public void dispatchOrder() {
        Order order = Order.dao.findById(getParaToInt(0, 0));
/*        if (order.getStatus() != Constant.OrderStatus.CREATE){
            renderAjaxError("已有司机接单！请刷新页面！");
        }*/
        Company company = Company.dao.findByCompanyId(order.getFromCompany());
        MemberInfo memberInfo = MemberInfo.dao.findByPhone(order.getPhone());
        OrderTrip trip = OrderTrip.dao.findById(order.getId());
        boolean pdflag = order.getPdFlag();
        boolean setoutflag = order.getSetOutFlag();
        String pd_flag = "不拼车订单";
        String set_out_flag = "实时";
        if (pdflag) {
            pd_flag = "拼车订单";
        }
        if (setoutflag) {
            set_out_flag = "预约";
        }
        setAttr("order", order);
        setAttr("company", company);
        setAttr("memberInfo", memberInfo);
        setAttr("trip", trip);
        setAttr("pd_flag", set_out_flag + pd_flag);
    }

    //专线订单派单
    public void dispatchZxOrder() {
        Order order = Order.dao.findById(getPara("orderId"));
        String driverIds = getPara("driverIds");
        if (order.getStatus() != Constant.OrderStatus.CREATE) {
            renderAjaxError("订单状态已更改，请返回刷新页面！");
            return;
        }
        if (Strings.isNullOrEmpty(driverIds)) {
            renderAjaxError("未选择接单司机!");
            return;
        }
        OrderTrip orderTrip = OrderTrip.dao.findById(order.getId());
        order.put("trip", orderTrip);
        order.put("timeOut", AdminSetting.dao.findFirst().getOrderReciveTime());
        String[] driverId = driverIds.split(",");
        List<Object> params = Lists.newArrayList();
        Collections.addAll(params, driverId);
        List<DriverInfo> driverInfos = DriverInfo.dao.findByIds(params);
        order.put("drivers", driverInfos);
        try {
            PushOrderService.getIntance().pushToDriverSetList(order, driverInfos);
        } catch (Exception e) {
            logger.error("出现错误，错误信息{}", e.getMessage());
        }
        renderAjaxSuccess("成功！");
    }

    /**
     * 查询联盟用户
     */
    public void unionuser() {
        List<User> users = Lists.newArrayList();
        if (!isSuperAdmin()) {
            users = User.dao.findByCompanyAndType(getCompanyId(), Constant.UserType.UNION);
            renderAjaxSuccess(users);
        } else {
            renderAjaxNoData();
        }
    }

    /**
     * 导出
     */
    public void excelList() {
        int type = getParaToInt("type");
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(false);
        DataTablePage dataList;
        if (isSuperAdmin()) {
            dataList = dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.where"), params);
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            dataList = dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.where"), whereSql, params);
        }
        //缓存
        CacheKit.put(Constant.EXCEL, getUser(), dataList.getData());
        renderJson();
    }

    public void excel() {
        String[] header = {"订单号", "所属公司", "预约时间", "预约地", "客户名称", "客户电话", "目的地", "服务人员", "服务人员电话", "结算时间", "里程", "结算金额"};
        String[] column = {"no", "company_name", "setouttime", "reservation_address", "mrealName", "mPhone", "destination", "drealName", "dPhone", "pay_time", "real_distance", "real_pay"};
        List<Record> list = CacheKit.get(Constant.EXCEL, getUser());
        CacheKit.remove(Constant.EXCEL, getUser());
        poiRender(list, header, column, "订单统计", " orders.xls");
    }

    //findPushList
    public void findPushList() {
        int orderId = getParaToInt("orderId");
        renderAjaxSuccess(RejectLog.dao.findRejectLogList(orderId));
    }

    public void goTimelyOrder() {
        Map<Integer, String> statusMap = Maps.newHashMap();
        statusMap.put(Constant.OrderStatus.CREATE, "新订单");
        statusMap.put(Constant.OrderStatus.ACCEPT, "司机接单");
        statusMap.put(Constant.OrderStatus.START, "订单开始");
        statusMap.put(Constant.OrderStatus.PAYED, "订单已支付");
        statusMap.put(Constant.OrderStatus.CANCEL, "订单取消");
        statusMap.put(Constant.OrderStatus.END, "到达终点");
        int type = getParaToInt("statusType", 1);
        setAttr("statusType", type);
        setAttr("statusMap", statusMap);
        render("timely/list.ftl");
    }

    public void listByStatus() {
        int status = getParaToInt("statusType", 1);
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(status);
        params.add(false);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.whereByStatus"), params, SqlManager.sql("order.columnPage"), SqlManager.sql("order.wherePage")));
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            renderJson(dataTable(SqlManager.sql("order.column"), SqlManager.sql("order.whereByStatus"), whereSql, params, SqlManager.sql("order.columnPage"), SqlManager.sql("order.wherePage")));
        }
    }
}
