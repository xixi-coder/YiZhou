package controllers.admin.count;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import kits.TimeKit;
import models.company.Company;
import models.order.Order;
import models.sys.ServiceType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
@Controller("/admin/count/orderfrom")
public class OrderFromController extends BaseAdminController {
    public void index() {
        int type = getParaToInt(0, 1);
        setAttr("type", type);
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        if (Strings.isNullOrEmpty(startTime)) {
            startTime = DateTime.now().millisOfDay().withMinimumValue().toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
        }
        if (Strings.isNullOrEmpty(endTime)) {
            endTime = DateTime.now().millisOfDay().withMaximumValue().toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
        }
        setAttr("startTime", startTime);
        setAttr("endTime", endTime);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }

        int company = getParaToInt("company", 0);
        setAttr("company", company);
        if (isSuperAdmin() && company != 0) {
            searchData(false, type, startTime, endTime, company);
        } else {
            searchData(isSuperAdmin(), type, startTime, endTime, getCompanyId());
        }
    }

    public void searchData(boolean isSuperAdmin, int type, String startTime, String endTime, int company) {
        Order order1 = Order.dao.findApp(isSuperAdmin, type, startTime, endTime, company);
        setAttr("order1", order1);

        Order order2 = Order.dao.findWeinxin(isSuperAdmin, type, startTime, endTime, company);
        setAttr("order2", order2);

        Order order3 = Order.dao.findPhone(isSuperAdmin, type, startTime, endTime, company);
        setAttr("order3", order3);

        Order order4 = Order.dao.findService(isSuperAdmin, type, startTime, endTime, company);
        setAttr("order4", order4);
    }
}
