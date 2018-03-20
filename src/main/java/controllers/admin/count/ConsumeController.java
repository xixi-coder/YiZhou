package controllers.admin.count;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import kits.StringsKit;
import models.company.Company;
import models.sys.ServiceType;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 * 客户消费统计
 */
@Controller("/admin/count/consume")
public class ConsumeController extends BaseAdminController {
    public void index() {
        int type = getParaToInt(0, 1);
        setAttr("type", type);
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt("type", 1);
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        switch (type) {
            case 1:
                params.add(1);
                break;
            case 2:
                params.add(2);
                break;
            case 3:
                params.add(3);
                break;
            case 4:
                params.add(4);
                break;
            case 5:
                params.add(5);
                break;
            case 6:
                params.add(6);
                break;
        }

        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("consume.column"), SqlManager.sql("consume.where"), params));
        } else {
            String whereSql1 = " AND dmi.company  = ?";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("consume.column"), SqlManager.sql("consume.where"), whereSql1, params));
        }
    }

    public void export() {
        String startTime = getPara("start1");
        String endTime = getPara("end1");
        int type = getParaToInt("type", 0);
        int level = getParaToInt("s-dmi.level-EQ", 0);
        int company = getParaToInt("s-dmi.company-EQ", 0);
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        switch (type) {
            case 1:
                params.add(Constant.ServiceType.ZhuanChe);
                break;
            case 2:
                params.add(Constant.ServiceType.DaiJia);
                break;
            case 3:
                params.add(Constant.ServiceType.Taxi);
                break;
            case 4:
                params.add(Constant.ServiceType.KuaiChe);
                break;
            case 5:
                params.add(Constant.ServiceType.ShunFengChe);
                break;
            case 6:
                params.add(Constant.ServiceType.ZhuanXian);
                break;
        }
        /*params.add(startTime);
        params.add(endTime);*/
        String sql;
        if (isSuperAdmin()) {
            sql = SqlManager.sql("consume.column") + SqlManager.sql("consume.where");
        } else {
            params.add(getCompanyId());
            sql = SqlManager.sql("consume.column") + StringsKit.replaceSql(SqlManager.sql("consume.where"), " and dmi.company=? ");
        }
        String[] header = {"客户姓名", "用户名", "所属公司", "完成订单量（单）", "完成订单金额（元）"};
        String[] column = {"real_name", "user_name", "name", "count", "sum"};
        poiRender(sql, params, header, column, "客户消费统计", "consume.xls");
    }
}
