package controllers.admin.count;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import models.company.Company;
import models.company.CompanyRebate;
import models.order.Order;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 * 快车订单统计
 */
@Controller("/admin/count/korder")
public class KOrderController extends BaseAdminController {
    public void index() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String startTime = getPara("startTime");
        String endTime = getPara("endTime");
        if (startTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date start = calendar.getTime();
            startTime = df.format(start);
        }
        setAttr("startTime", startTime);
        if (endTime == null) {
            Date end = DateTime.now().toDate();
            endTime = df.format(end);
        }
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
        Order order1 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.KuaiChe);
        setAttr("order1", order1);
        Order order2 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.KuaiChe);
        setAttr("order2", order2);
        Order order8 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.PAYED, Constant.ServiceType.KuaiChe);
        setAttr("order8", order8);
        CompanyRebate order3 = CompanyRebate.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, Constant.ServiceType.KuaiChe, startTime, endTime);
        setAttr("order3", order3);
        Order order4 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.KuaiChe);
        setAttr("order4", order4);
        Order order5 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.KuaiChe);
        setAttr("order5", order5);
        Order order9 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.START, Constant.ServiceType.KuaiChe);
        setAttr("order9", order9);
        Order order6 = Order.dao.findCountByStatusServiceType1(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.KuaiChe);
        setAttr("order6", order6);
        Order order7 = Order.dao.findCountByStatusServiceType2(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.KuaiChe);
        setAttr("order7", order7);
        Order order10 = Order.dao.findCountByStatusServiceType3(isSuperAdmin, companyId, startTime, endTime, Constant.OrderStatus.CANCEL, Constant.ServiceType.KuaiChe);
        setAttr("order10", order10);

    }

}
