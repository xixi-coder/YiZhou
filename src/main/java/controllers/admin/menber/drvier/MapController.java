package controllers.admin.menber.drvier;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import models.driver.DriverInfo;
import models.driver.DriverRealLocation;
import models.order.Order;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;
import services.PushOrderService;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 2016/12/14.
 */
@Controller("/admin/member/driver/map")
public class MapController extends BaseAdminController {
    public void index() {
    }

    /**
     * 司机实时位置
     */
    public void driversRealLocation() {
        String[] status = null;
        if (!Strings.isNullOrEmpty(getPara("status"))) {
            status = getPara("status").split(",");
        }
        List<DriverRealLocation> driverRealLocationList;
        if (isSuperAdmin()) {
            int comapnyId = getParaToInt("company", 0);
            driverRealLocationList = DriverRealLocation.dao.findByCompanyAndStatus(comapnyId, status);
        } else {
            driverRealLocationList = DriverRealLocation.dao.findByCompanyAndStatus(getCompanyId(), status);
        }
        renderAjaxSuccess(driverRealLocationList);
    }

    /**
     * 未派单订单
     */
    public void weipaidandingdan() {
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.CREATE);
        params.add(DateTime.now().plusMinutes(20).toDate());
        String tmpWhere = " AND do.status = ? AND do.create_time > ?";
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.weipaidandingdancolumn"), SqlManager.sql("order.weipaidandingdanwhere"), tmpWhere, params));
        } else {
            params.add(getCompanyId());
            tmpWhere += " AND do.company = ? ";
            renderJson(dataTable(SqlManager.sql("order.weipaidandingdancolumn"), SqlManager.sql("order.weipaidandingdanwhere"), tmpWhere, params));
        }

    }

    /**
     * 执行订单的司机
     */
    public void zhixingzhongsiji() {
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.ACCEPT);
        params.add(Constant.OrderStatus.END);
        params.add(Constant.OrderStatus.START);
        params.add(Constant.OrderStatus.DRIVERWAIT);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("driverInfo.zhixingzhongsijicolumn"), SqlManager.sql("driverInfo.zhixingzhongsijiwhere"), params));
        } else {
            params.add(getCompanyId());
            String tmpWhere = " AND ddi.company = ? ";
            renderJson(dataTable(SqlManager.sql("driverInfo.zhixingzhongsijicolumn"), SqlManager.sql("driverInfo.zhixingzhongsijiwhere"), tmpWhere, params));
        }
    }

    /**
     * 空闲司机
     */
    public void kongxiansiji() {
        String lon = getPara("lon");
        String lat = getPara("lat");
        int serviceType = getParaToInt("service", 0);
        String driverName = getPara("driverName");

        List<Object> params = Lists.newArrayList();
        params.add(lat);
        params.add(lon);
        params.add(Constant.DriverStatus.ShengHeTongGuo);
        params.add(Constant.LoginStatus.RECIVEDORDER);
        params.add(serviceType);
        String tmpSql = "";
        if (!Strings.isNullOrEmpty(driverName)) {
            tmpSql = " AND ( nick_name LIKE ? OR real_name LIKE ? OR phone LIKE ? )";
            params.add("%" + driverName + "%");
            params.add("%" + driverName + "%");
            params.add("%" + driverName + "%");
        }
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("driverInfo.kongxiansijicolumn"), SqlManager.sql("driverInfo.kongxiansijiwhere"), tmpSql, params));
        } else {
            params.add(getCompanyId());
            tmpSql += " AND company = ? ";
            renderJson(dataTable(SqlManager.sql("driverInfo.kongxiansijicolumn"), SqlManager.sql("driverInfo.kongxiansijiwhere"), tmpSql, params));
        }
    }

    /**
     * 派单司机
     *
     * @throws IOException
     */
    public void paidan() throws IOException {
        int orderId = getParaToInt("orderId", 0);
        int driverId = getParaToInt("driverId", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        Order order = Order.dao.findById(orderId);
        List<DriverInfo> driverInfos = Lists.newArrayList();
        driverInfos.add(driverInfo);
        PushOrderService.getIntance().pushToDriverSetList(order, driverInfos);
        renderAjaxSuccess("派单成功!");
    }
}
