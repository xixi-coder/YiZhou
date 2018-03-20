package controllers.admin.activity;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import kits.StringsKit;
import models.activity.Activity;
import models.activity.Coupon;
import models.sys.RoleResources;
import models.sys.ServiceType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
@Controller("/admin/activity/list")
public class ActivityController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("activity.column"), SqlManager.sql("activity.where"),SqlManager.sql("activity.columnPage"), SqlManager.sql("activity.wherePage")));
        } else {
            whereSql = " AND da.company = ? ";
            whereSql = StringsKit.replaceSql(SqlManager.sql("activity.where"),whereSql);
            String whereSqlPage = " AND da.company = ? ";
            whereSqlPage = StringsKit.replaceSql(SqlManager.sql("activity.where"),whereSqlPage);
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("activity.column"), whereSql, params,SqlManager.sql("activity.columnPage"),whereSqlPage));
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        Activity activity = Activity.dao.findById(id);
        if (activity.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void item() {
        //获取类型
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);

        Date now = DateTime.now().toDate();
        List<Coupon> coupons = Coupon.dao.findByCompanyAndDate(getCompanyId(), isSuperAdmin(), now);
        setAttr("coupons", coupons);
        int id = getParaToInt(0, 0);
        Activity activity;
        List nTypes = Lists.newArrayList();
        List dtypes = Lists.newArrayList();
        List ptypes = Lists.newArrayList();
        List fromTypes = Lists.newArrayList();
        if (id > 0) {
            activity = Activity.dao.findById(id);
            setAttr("activity", activity);
            String type = activity.getServiceType();

            if (!Strings.isNullOrEmpty(type)) {
                String[] types = type.split(";");
                dtypes = Arrays.asList(types);
            }
            setAttr("types", dtypes);
            String payType = activity.getPayType();

            if (!Strings.isNullOrEmpty(payType)) {
                String[] types = payType.split(";");
                ptypes = Arrays.asList(types);
            }
            setAttr("ptypes", ptypes);
            String nType = activity.getNoticeType();

            if (!Strings.isNullOrEmpty(nType)) {
                String[] types = nType.split(";");
                nTypes = Arrays.asList(types);
            }
            setAttr("nTypes", nTypes);
            String sOrderType = activity.getOrderType();
            if (!Strings.isNullOrEmpty(sOrderType)) {
                String[] types = sOrderType.split(";");
                fromTypes = Arrays.asList(types);
            }
            setAttr("fromTypes", fromTypes);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            setAttr("types", dtypes);
            setAttr("ptypes", ptypes);
            activity = new Activity();
            setAttr("activity", activity);
            setAttr("nTypes", nTypes);
            setAttr("fromTypes", fromTypes);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        Activity activity = getModel(Activity.class, "activity");
        if (activity.getEvent() != Constant.ActivityEvent.REGISTER) {
            if (activity.getEvent() != Constant.ActivityEvent.OPENAPP) {
                String[] type = getParaValues("c1");
                String drivertype = "";
                if (type != null) {
                    for (int i = 0; i < type.length; i++) {
                        if (type.length - 1 == i) {
                            drivertype += type[i];
                        } else {
                            drivertype += type[i] + ";";
                        }
                    }
                }
                activity.setServiceType(drivertype);
                String[] ptype = getParaValues("c2");
                String payType = "";
                if (ptype != null) {
                    for (int i = 0; i < ptype.length; i++) {
                        if (ptype.length - 1 == i) {
                            payType += ptype[i];
                        } else {
                            payType += ptype[i] + ";";
                        }
                    }
                }

                activity.setPayType(payType);
                String[] fromType = getParaValues("c4");
                String orderType = "";
                if (fromType != null) {
                    for (int i = 0; i < fromType.length; i++) {
                        if (fromType.length - 1 == i) {
                            orderType += fromType[i];
                        } else {
                            orderType += fromType[i] + ";";
                        }
                    }
                }
                activity.setOrderType(orderType);
            }
            String[] ntype = getParaValues("c3");
            String noticType = "";
            if (ntype != null) {
                for (int i = 0; i < ntype.length; i++) {
                    if (ntype.length - 1 == i) {
                        noticType += ntype[i];
                    } else {
                        noticType += ntype[i] + ";";
                    }
                }

            }
            activity.setNoticeType(noticType);

        }
        String coupons = getPara("coupons");
        final List<RoleResources> roleResources = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(coupons)) {
            final String[] resources = coupons.split(",");
            for (String resource : resources) {
                RoleResources roleResources1 = new RoleResources();
                roleResources1.setResource(Ints.tryParse(resource));
                roleResources.add(roleResources1);
            }
        }
        activity.setStartTime(DateTime.parse(getPara("start_time"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")).toDate());
        activity.setEndTime(DateTime.parse(getPara("end_time"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")).toDate());
        if (!isSuperAdmin()) {
            activity.setCompany(getCompanyId());
        }
        if (activity.getId() != null) {
            if (activity.update()) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            activity.setCreateTime(DateTime.now().toDate());
            if (activity.save()) {
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    public void changecompany() {
        Date now = DateTime.now().toDate();
        int company = getParaToInt("company");
        List<Coupon> coupons = Coupon.dao.findByCompanyAndDate(company, now);

        //renderAjaxSuccess(coupons);
        JSONObject jsonObject = new JSONObject();
        boolean flag = true;
        jsonObject.put("flag", flag);
        //jsonObject.put("coupons",coupons);
        renderJson(jsonObject);
    }

    public void export() {
        String sql = "select * from dele_activity";
        String[] header = {"主键", "名称"};
        String[] column = {"id", "name"};
        poiRender(sql, header, column, "测试", "test.xls");
    }
}
