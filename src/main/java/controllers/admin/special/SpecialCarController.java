package controllers.admin.special;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import controllers.admin.order.OrderController;
import kits.TimeKit;
import models.company.Company;
import models.member.MemberInfo;
import models.special.SpecialCar;
import models.sys.Area;
import models.sys.ServiceTypeItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.sqlInXml.SqlManager;
import services.CalService;

import java.math.BigDecimal;
import java.util.List;

import static org.apache.log4j.varia.ExternallyRolledFileAppender.OK;

/**
 * Created by Administrator on 2017/2/23.
 */
@Controller("/admin/special")
public class SpecialCarController extends BaseAdminController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);
    DateTime start, end;

    public void index() {
        List<ServiceTypeItem> serviceTypesItem = ServiceTypeItem.dao.findByType(Constant.ServiceType.ZhuanXian);
        setAttr("serviceTypesItem", serviceTypesItem);
        int type = getParaToInt("type", Constant.ServiceItemType.KuaChengZhuanXian);
        setAttr("type", type);
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt("type", Constant.ServiceItemType.KuaChengZhuanXian);
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(type);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("special.column"), SqlManager.sql("special.where"), params));
        } else {
            whereSql = " AND dsc.company_id = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("special.column"), SqlManager.sql("special.where"), whereSql, params));
        }
    }

    //创建专线
    public void createbyadmin() {
        SpecialCar specialCar = new SpecialCar();
        Area area = new Area();
        String setouttime1 = getPara("special.setouttime1");
        String setouttime2 = getPara("special.setouttime2");
        String setouttime3 = getPara("special.setouttime3");
        String setouttime4 = getPara("special.setouttime4");
        String starttime = setouttime1 + ":" + setouttime3;
        String endtime = setouttime2 + ":" + setouttime4;
        System.out.println(starttime + "----" + endtime);
        start = new DateTime(DateTime.parse(starttime, DateTimeFormat.forPattern("HH:mm")).toDate());
        end = new DateTime(DateTime.parse(endtime, DateTimeFormat.forPattern("HH:mm")).toDate());
        if (start.isAfter(end)) {
            end = end.plusDays(1);
        }
        specialCar.setSetoutTime1(start.toDate());
        specialCar.setSetoutTime2(end.toDate());
        specialCar.setEstimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.estimated_price"))));
        specialCar.setSharingEsyimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.sharing_esyimated_price"))));
        specialCar.setCreateTime(DateTime.now().toDate());
        specialCar.setDestination(getPara("special.destination"));
        specialCar.setReservationAddress(getPara("special.reservation_address"));
        specialCar.setStartLongitude(Double.valueOf(getPara("special.start_longitude")));
        specialCar.setStartLatitude(Double.valueOf(getPara("special.start_latitude")));
        specialCar.setEndLongitude(Double.valueOf(getPara("special.end_longitude")));
        specialCar.setEndLatitude(Double.valueOf(getPara("special.end_latitude")));
        specialCar.setStatus(Constant.LineStatus.ENABLE);
        specialCar.setDistance(Double.valueOf(getPara("special.distance")));
        specialCar.setTag(getPara("special.tag"));
        specialCar.setFlag(false);
        specialCar.setIndexPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.index_price"))));
        specialCar.setSharingPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.sharing_price"))));
        specialCar.setType(getParaToInt("serviceTypeItems"));
        specialCar.setRecordType(Constant.ServiceType.ZhuanXian);
        String needtime = getPara("special.time", "60");
        try {
            double time = Double.parseDouble(needtime);
            specialCar.setNeedTime(Integer.valueOf((int) time) / 60 + "小时" + Integer.valueOf((int) time) % 60 + "分钟");
        } catch (NumberFormatException e) {
            specialCar.setNeedTime("未知");
            e.printStackTrace();
            logger.error("数据类型转换错误！");
        }

        String startcity = getPara("special.startcity");
        String endcity = getPara("special.endcity");
        String startadcode = getPara("special.startadcode");
        String endadcode = getPara("special.endadcode");

        if (Strings.isNullOrEmpty(startcity)) {
            area = area.findByAdCode(startadcode);
            if (area == null) {
                area = Area.dao.findByCityCode(startadcode);
            }
            startcity = area.getName();
        } else {
            area = area.findByName(startcity);
            specialCar.setStartAdcode(area.getAdcode());
        }
        if (Strings.isNullOrEmpty(endcity)) {
            area = area.findByAdCode(endadcode);
            if (area == null) {
                area = Area.dao.findByCityCode(endadcode);
            }
            endcity = area.getName();
        } else {
            area = area.findByName(endcity);
            specialCar.setEndAdcode(area.getAdcode());
        }
        specialCar.setReservationCity(startcity);
        specialCar.setDestinationCity(endcity);
        if (isSuperAdmin()) {
            specialCar.setCompanyId(getParaToInt("company"));
        } else {
            specialCar.setCompanyId(getCompanyId());
        }
        if (specialCar.save()) {
            JSONObject json = new JSONObject();
            json.put("status", OK);
            json.put("msg", "创建成功");
            json.put("serviceTypeItems", getPara("serviceTypeItems"));
            renderJson(json);
        } else {
            renderAjaxFailure("创建失败");
        }
    }

    public void updateline() {
        int special = getParaToInt(0, 0);
        SpecialCar specialCar = SpecialCar.dao.findById(special);
        specialCar.put("start_hour", TimeKit.splictHours(specialCar.getSetoutTime1())[0]);
        specialCar.put("start_minute", TimeKit.splictHours(specialCar.getSetoutTime1())[1]);
        specialCar.put("end_hour", TimeKit.splictHours(specialCar.getSetoutTime2())[0]);
        specialCar.put("end_minute", TimeKit.splictHours(specialCar.getSetoutTime2())[1]);
        List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.ZhuanXian);

        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
        setAttr("specialCars", specialCar);
        setAttr("serviceTypeItems", serviceTypeItems);
        render("item.ftl");
    }

    public void updateline1() {
        String setouttime1 = getPara("special.setouttime1");
        String setouttime2 = getPara("special.setouttime2");
        String setouttime3 = getPara("special.setouttime3");
        String setouttime4 = getPara("special.setouttime4");
        String starttime = setouttime1 + ":" + setouttime3;
        String endtime = setouttime2 + ":" + setouttime4;
        System.out.println(starttime + "----" + endtime);
        SpecialCar specialCar = new SpecialCar();
        specialCar = specialCar.findById(getParaToInt("special.id"));
        start = new DateTime(DateTime.parse(starttime, DateTimeFormat.forPattern("HH:mm")).toDate());
        end = new DateTime(DateTime.parse(endtime, DateTimeFormat.forPattern("HH:mm")).toDate());
        if (start.isAfter(end)) {
            end = end.plusDays(1);
        }

        specialCar.setEstimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.estimated_price"))));
        specialCar.setSharingEsyimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.sharing_esyimated_price"))));
        specialCar.setCreateTime(DateTime.now().toDate());
        specialCar.setDestination(getPara("special.destination"));
        specialCar.setReservationAddress(getPara("special.reservation_address"));
        specialCar.setTag(getPara("special.tag"));
        specialCar.setIndexPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.index_price"))));
        specialCar.setSharingPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.sharing_price"))));
        specialCar.setType(getParaToInt("serviceTypeItems"));
        specialCar.setUpdateTime(DateTime.now().toDate());
        specialCar.setSetoutTime1(start.toDate());
        specialCar.setSetoutTime2(end.toDate());
//        specialCar.setEstimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.estimated_price"))));
//        specialCar.setSharingEsyimatedPrice(BigDecimal.valueOf(Long.parseLong(getPara("special.sharing_esyimated_price"))));
        if (isSuperAdmin()) {
            specialCar.setCompanyId(getParaToInt("company"));
        } else {
            specialCar.setCompanyId(getCompanyId());
        }
        if (specialCar.update()) {
            JSONObject json = new JSONObject();
            json.put("status", OK);
            json.put("msg", "修改成功");
            json.put("serviceTypeItems", getPara("serviceTypeItems"));
            renderJson(json);

        } else {
            renderAjaxFailure("修改失败");
        }
    }

    //删除路线，更改flag状态
    public void del() {
        int id = getParaToInt("id", 0);
        int i = SpecialCar.dao.updateflag(id);
        if (i > 0) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxFailure("删除失败！");
        }

    }

    public void linestatus() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            SpecialCar specialCar = SpecialCar.dao.findById(id);
            if (specialCar.getStatus() == Constant.LineStatus.ENABLE) {
                specialCar.setStatus(Constant.LineStatus.DISABLE);
            } else if (specialCar.getStatus() == Constant.LineStatus.DISABLE) {
                specialCar.setStatus(Constant.LineStatus.ENABLE);
            }
            specialCar.setUpdateTime(DateTime.now().toDate());
            if (specialCar.update()) {
                renderAjaxSuccess("操作成功!");
            } else {
                renderAjaxError("操作失败");
            }
        } else {
            renderAjaxError("该路线不存在!");
        }
    }

    public void create() {

    }

    public void createitem() {
        int memberId = getParaToInt(0, 0);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.ZhuanXian);
            setAttr("companies", companies);
            setAttr("serviceTypeItems", serviceTypeItems);
        } else {
            List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(Constant.ServiceType.ZhuanXian);
            setAttr("serviceTypeItems", serviceTypeItems);
        }
    }


    public void celAmount() {
        int memberId = getParaToInt("mid", 0);
        MemberInfo memberInfo;
        if (memberId == 0) {
            memberInfo = null;
        } else {
            memberInfo = MemberInfo.dao.findById(memberId);
        }

        BigDecimal distance = new BigDecimal(Strings.isNullOrEmpty(getPara("distance")) ? "0" : getPara("distance"));
/*        String strSetOutTime = getPara("setOutTime");
        if (!Strings.isNullOrEmpty(strSetOutTime)) {
            strSetOutTime += ":00";
        }*/
        DateTime strSetOutTime = DateTime.now();

        Company company;
        if (isSuperAdmin()) {
            company = Company.dao.findByCompanyId(getParaToInt("company"));
        } else {
            company = getCompany();
        }
        BigDecimal times = new BigDecimal(Strings.isNullOrEmpty(getPara("time")) ? "0" : getPara("time"));
        try {
            renderAjaxSuccess(CalService.getInstance().calculationDtoSetUp1(Constant.ServiceType.ZhuanXian, company, memberInfo, strSetOutTime.toString(DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)), distance, times));
        } catch (NullPointerException e) {
            e.printStackTrace();
            renderAjaxError("未获取到收费标准！");
        }

    }

}
