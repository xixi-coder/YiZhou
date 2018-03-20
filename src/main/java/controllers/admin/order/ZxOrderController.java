package controllers.admin.order;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import base.datatable.DataTablePage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import jobs.pushorder.CancelOrder;
import jobs.pushorder.PushOrder;
import models.activity.MemberCoupon;
import models.company.Company;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import models.order.OrderLog;
import models.order.OrderTrip;
import models.sys.*;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.sqlInXml.SqlManager;
import services.CalService;
import services.PushOrderService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Controller("/admin/schedule/order")
public class ZxOrderController extends BaseAdminController {

    public void index() {
        List<ServiceType> serviceTypes;
        if (isSuperAdmin()){
            serviceTypes = ServiceType.dao.findZxAll();
        }else {
            serviceTypes = ServiceType.dao.findZxServiceType(getCompanyId());
        }
        setAttr("serviceTypes", serviceTypes);
        int type = getParaToInt("type", 6);
        setAttr("type", type);
        render("dispatchList.ftl");
    }

    public void list() {
        int type = getParaToInt("type", 6);
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(false);
        params.add(Constant.DispatchOrder.hand);
        params.add(Constant.OrderStatus.CREATE);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.column1"), SqlManager.sql("order.where1"), params));
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            renderJson(dataTable(SqlManager.sql("order.column1"), SqlManager.sql("order.where1"), whereSql, params));
        }
    }
}
