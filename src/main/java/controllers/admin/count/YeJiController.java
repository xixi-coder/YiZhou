package controllers.admin.count;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import kits.StringsKit;
import models.company.Company;
import models.sys.ServiceType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 * 服务人员业绩
 */
@Controller("/admin/count/yeji")
public class YeJiController extends BaseAdminController {
    public void index() {
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        render("list.ftl");
    }

    public void list() {
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        String start = getPara("startDate");
        String end = getPara("endDate");
        String fromSql = SqlManager.sql("order.where6");
        if (!Strings.isNullOrEmpty(start) && !Strings.isNullOrEmpty(end)) {
            fromSql = fromSql.replace("-- time", " AND dcl.create_time BETWEEN ? AND ? ");
            DateTime startTime = DateTime.parse(start, DateTimeFormat.forPattern("yyyy-MM-dd")).millisOfDay().withMinimumValue();
            DateTime endTime = DateTime.parse(end, DateTimeFormat.forPattern("yyyy-MM-dd")).millisOfDay().withMaximumValue();
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
            params.add(startTime.toDate());
            params.add(endTime.toDate());
        }
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.column6"), fromSql, params));
        } else {
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            whereSql = whereSql + " AND ddi.company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("order.column6"), fromSql, whereSql, params));
        }
    }

    public void item() {
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
        String username = getPara("username");
        setAttr("username", username);
        int type = getParaToInt("type", 1);
        setAttr("type", type);
    }

    public void orderlist() {
        String username = getPara("username");
        int type = getParaToInt("type", 1);
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(username);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("order.column7"), SqlManager.sql("order.where7"), params));
        } else {
            String whereSql = StringsKit.replaceSql(SqlManager.sql("order.where7"), " AND dd.company = ? ");
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("order.column7"), whereSql, params));
        }
    }

    public void export() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        String sql;
        int job_type = getParaToInt("s-ddi.job_type-EQ", 0);
        String[] header = {"用户名", "类型", "所属公司", "代驾完成单量", "代驾订单金额", "代驾提成", "专车完成单量", "专车订单金额", "专车提成", "出租车完成单量", "出租车订单金额", "出租车提成", "快车完成单量", "快车订单金额", "快车提成", "顺风车完成单量", "顺风车订单金额", "顺风车提成", "专线完成单量", "专线订单金额", "专线提成"};
        String[] column = {"user_name", "job_type", "name", "count", "sum", "tsum", "count1", "sum1", "tsum1", "count2", "sum2", "tsum2", "count3", "sum3", "tsum3"};
        if (isSuperAdmin()) {
            if (job_type == 0) {
                sql = SqlManager.sql("order.column6") + SqlManager.sql("order.where6");
            } else {
                params.add(job_type);
                sql = SqlManager.sql("order.column6") + SqlManager.sql("order.where6").replace("-- where", "and ddi.job_type = ?");
            }
        } else {
            if (job_type == 0) {
                whereSql = " AND ddi.company = ? ";
                params.add(getCompanyId());
                sql = SqlManager.sql("order.column6") + SqlManager.sql("order.where6").replace("-- where", whereSql);
            } else {
                whereSql = " AND ddi.company = ? and ddi.job_type = ?";
                params.add(job_type);
                params.add(getCompanyId());
                sql = SqlManager.sql("order.column6") + SqlManager.sql("order.where6").replace("-- where", whereSql);
            }
        }
        poiRender(sql, params, header, column, "服务人员业绩", "yeji.xls");
    }

}
