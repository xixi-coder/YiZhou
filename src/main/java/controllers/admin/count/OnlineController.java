package controllers.admin.count;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import models.company.Company;
import models.driver.DriverOnlineDetail;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/18.
 */
@Controller("/admin/count/online")
public class OnlineController extends BaseAdminController {
    public void index() {
        setAttr("currentMonth", DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM")));
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        render("list.ftl");
    }

    public void countOfMonth() {
        String date = getPara("month");
        DateTime startOfMonth;
        DateTime endOfMonth;
        if (Strings.isNullOrEmpty(date)) {
            startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        } else {
            startOfMonth = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM")).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM")).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }
        int coutOfMonth = Days.daysBetween(startOfMonth, endOfMonth).getDays() + 1;
        renderAjaxSuccess(coutOfMonth);
    }

    public void list() {
        String userName = getPara("name");
        String date = getPara("month");
        DateTime startOfMonth;
        DateTime endOfMonth;
        if (Strings.isNullOrEmpty(date)) {
            startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        } else {
            startOfMonth = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM")).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            endOfMonth = DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM")).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
        }
        Map<String, Object> result = Maps.newHashMap();
        int company = getParaToInt("company", 0);
        int count;
        if (isSuperAdmin() && company != 0) {
            count = DriverOnlineDetail.dao.findCountByMonthForDetail(startOfMonth, endOfMonth, false, company, userName);
        } else {
            if (getUserType() == Constant.UserType.UNION) {
                count = DriverOnlineDetail.dao.findCountByMonthForDetail(startOfMonth, endOfMonth, isSuperAdmin(), getCompanyId(), userName,getUserId());
            } else {
                count = DriverOnlineDetail.dao.findCountByMonthForDetail(startOfMonth, endOfMonth, isSuperAdmin(), getCompanyId(), userName);
            }
        }

        result.put("count", count);
        int start = getParaToInt("start", 0);
        int pageSize = getPageSize();
        start = (start - 1) * pageSize;

        List<DriverOnlineDetail> driverOnlineDetail;
        if (isSuperAdmin() && company != 0) {
            driverOnlineDetail = DriverOnlineDetail.dao.findByMonthForDetail(startOfMonth, endOfMonth, false, company, start, pageSize, userName);
        } else {
            if (getUserType() == Constant.UserType.UNION) {
                driverOnlineDetail = DriverOnlineDetail.dao.findByMonthForDetail(startOfMonth, endOfMonth, isSuperAdmin(), getCompanyId(), start, pageSize, userName,getUserId());
            } else {
                driverOnlineDetail = DriverOnlineDetail.dao.findByMonthForDetail(startOfMonth, endOfMonth, isSuperAdmin(), getCompanyId(), start, pageSize, userName);
            }
        }
        result.put("driverOnlineDetails", driverOnlineDetail);
        int coutOfMonth = Days.daysBetween(startOfMonth, endOfMonth).getDays() + 1;
        result.put("countOfMonth", coutOfMonth);
        renderAjaxSuccess(result);
    }
}
