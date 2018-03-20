package controllers.admin.count;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import kits.TimeKit;
import models.company.Company;
import models.company.CompanyRebate;
import models.order.Order;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 * 代驾订单统计
 */
@Controller("/admin/count/shunorder")
public class ShunOrderController extends BaseAdminController {
    public void index() {
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        if (Strings.isNullOrEmpty(startTime)) {
            startTime = DateTime.now().plusDays(-7).millisOfDay().withMinimumValue().toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
        }
        if (Strings.isNullOrEmpty(endTime)) {
            endTime = DateTime.now().millisOfDay().withMaximumValue().toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS));
        }
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        setAttr("startTime", startTime);
        setAttr("endTime", endTime);

        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }

        int company = getParaToInt("company", 0);
        setAttr("company", company);
        if (isSuperAdmin()) {
            if (company != 0) {
                searchData(false, company, startTime, endTime);
            } else {
                searchData(true, company, startTime, endTime);
            }
        } else {
            searchData(false, getCompanyId(), startTime, endTime);
        }
    }

    public void searchData(boolean isSuperAdmin, int companyId, String startTime, String endTime) {
        Order order1 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.ShunFengChe);
        setAttr("order1", order1);
        Order order2 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.ShunFengChe);
        setAttr("order2", order2);
        Order order8 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.ShunFengChe);
        setAttr("order8",order8);
        CompanyRebate order3 = CompanyRebate.dao.findCountByStatusServiceType3(isSuperAdmin, companyId , Constant.ServiceType.ShunFengChe,startTime,endTime);
        setAttr("order3", order3);
        Order order4 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.ShunFengChe);
        setAttr("order4", order4);
        Order order5 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.ShunFengChe);
        setAttr("order5", order5);
        Order order9 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.ShunFengChe);
        setAttr("order9",order9);
        Order order6 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.ShunFengChe);
        setAttr("order6", order6);
        Order order7 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.ShunFengChe);
        setAttr("order7", order7);
        Order order10 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.ShunFengChe);
        setAttr("order10",order10);

    }
}
