package controllers.admin.count;

import annotation.Controller;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.company.Company;
import models.order.Order;
import models.sys.ServiceType;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 * 订单统计
 */
@Controller("/admin/count/order")
public class OrderController extends BaseAdminController {
    public void index() {
        List<String> years = Lists.newArrayList();
        DateTime now = DateTime.now();
        years.add(now.plusYears(-1).getYear() + "");
        years.add(now.plusYears(-2).getYear() + "");
        years.add(now.plusYears(-3).getYear() + "");
        years.add(now.plusYears(0).getYear() + "");
        years.add(now.plusYears(1).getYear() + "");
        setAttr("years", years);
        int year = getParaToInt("year", 2018);
        int type = getParaToInt(0, 1);
        setAttr("type", type);
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
        setAttr("year", year);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
            setAttr("status",0);
        }else {
            setAttr("company",getCompanyId());
            setAttr("status",1);
        }

        int company = getParaToInt("company", 0);
        setAttr("company", company);
        if (isSuperAdmin()) {
            if (company != 0) {
                searchData1(false, type, year, company);
            } else {
                searchData1(true, type, year, company);
            }
        } else {
            searchData1(false, type, year, getCompanyId());
        }
    }

    public void month() {
        int month = getParaToInt(0, 1);
        int type = getParaToInt(1, 1);
        ServiceType serviceType = ServiceType.dao.findById(type);
        setAttr("serviceType", serviceType);
        int year = getParaToInt(2, 2018);
        setAttr("type", type);
        setAttr("month", month);
        int daysOfMonth = Days.daysBetween(new DateTime(year, month, 1, 0, 0, 0).dayOfMonth().withMinimumValue(), new DateTime(year, month, 1, 0, 0, 0).dayOfMonth().withMaximumValue()).getDays();
        setAttr("days", daysOfMonth);
        setAttr("year", year);
        int company = getParaToInt(3, 0);
        setAttr("company", company);
        if (isSuperAdmin()) {
            searchData2(true, type, year, month, company);
        } else {
            searchData2(isSuperAdmin(), type, year, month, getCompanyId());
        }
    }

    public void day() {
        int day = getParaToInt(0, 0);
        int type = getParaToInt(1, 1);
        ServiceType serviceType = ServiceType.dao.findById(type);
        setAttr("serviceType", serviceType);
        int month = getParaToInt(3, 1);
        int year = getParaToInt(2, 2018);
        setAttr("type", type);
        setAttr("day", day);
        setAttr("year", year);
        setAttr("month", month);

        int company = getParaToInt(4, 0);
        setAttr("company", company);

        if (isSuperAdmin()) {
            searchData3(true,day,type, year, month,company);
        } else {
            searchData3(isSuperAdmin(), day, type, year, month, getCompanyId());
        }
    }

    public void searchData1(boolean isSuperAdmin, int type, int year, int company) {
        List<Order> orders = Order.dao.findAmount(isSuperAdmin, year, type, company);
        setAttr("orders", orders);
        List<Order> orders1 = Order.dao.findFinish(isSuperAdmin, year, type, company);
        setAttr("orders1", orders1);
        List<Order> orders2 = Order.dao.findDestroy(isSuperAdmin, year, type, company);
        setAttr("orders2", orders2);
    }

    public void searchData2(boolean isSuperAdmin, int type, int year, int month, int company) {
        List<Order> orders = Order.dao.findAmount1(isSuperAdmin, month, type, year, company);
        setAttr("orders", orders);
        List<Order> orders1 = Order.dao.findFinish1(isSuperAdmin, month, type, year, company);
        setAttr("orders1", orders1);
        List<Order> orders2 = Order.dao.findDestroy1(isSuperAdmin, month, type, year, company);
        setAttr("orders2", orders2);
    }

    public void searchData3(boolean isSuperAdmin, int day, int type, int year, int month, int company) {
        List<Order> orders = Order.dao.findAmount2(isSuperAdmin, day, type, year, month, company);
        setAttr("orders", orders);
        List<Order> orders1 = Order.dao.findFinish2(isSuperAdmin, day, type, year, month, company);
        setAttr("orders1", orders1);
        List<Order> orders2 = Order.dao.findDestroy2(isSuperAdmin, day, type, year, month, company);
        setAttr("orders2", orders2);
    }
}
