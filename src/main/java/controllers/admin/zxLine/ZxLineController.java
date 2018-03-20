package controllers.admin.zxLine;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import models.sys.Area;
import models.sys.ServiceTypeItem;
import models.sys.ZxLine;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
@Controller("/admin/zxLine")
public class ZxLineController extends BaseAdminController {

    DateTime start,end;
    public void index() {
        List<ServiceTypeItem> serviceTypesItem = ServiceTypeItem.dao.findZXType();
        setAttr("serviceTypesItem", serviceTypesItem);
        int type = getParaToInt(0, Constant.ServiceItemType.KuaChengZhuanXian);
        setAttr("type", type);
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt(0,Constant.ServiceItemType.KuaChengZhuanXian);
        String whereSql;
        List<Object> params = Lists.newArrayList();
        params.add(type);
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("zxLine.column"), SqlManager.sql("zxLine.where"), params,SqlManager.sql("zxLine.columnPage"), SqlManager.sql("zxLine.wherePage")));
        } else {
            whereSql = " AND dc.id = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("zxLine.column"), SqlManager.sql("zxLine.where"), whereSql, params,SqlManager.sql("zxLine.columnPage"), SqlManager.sql("zxLine.wherePage")));
        }
    }

    public void item() {
        List<ServiceTypeItem> serviceTypesItem = ServiceTypeItem.dao.findZXType();
        setAttr("serviceTypesItem", serviceTypesItem);
        int id = getParaToInt(0, 0);
        List<Area> province = Area.dao.findByLevelAndParent("province", 0 + "");
        if (id > 0) {
            ZxLine zxLine = ZxLine.dao.findById(id);
            setAttr("zxLine",zxLine);
            setAttr("province", province);
            if (!Strings.isNullOrEmpty(zxLine.getStartProvince())) {
                List<Area> start_city = Area.dao.findByLevelAndParent("city", zxLine.getStartProvince());
                setAttr("start_city", start_city);
            }
            if (!Strings.isNullOrEmpty(zxLine.getStartCity())) {
                List<Area> start_county = Area.dao.findByLevelAndParent("district", zxLine.getStartCity());
                setAttr("start_county", start_county);
            }

            if (!Strings.isNullOrEmpty(zxLine.getEndProvince())) {
                List<Area> end_city = Area.dao.findByLevelAndParent("city", zxLine.getEndProvince());
                setAttr("end_city", end_city);
            }
            if (!Strings.isNullOrEmpty(zxLine.getEndCity())) {
                List<Area> end_county = Area.dao.findByLevelAndParent("district", zxLine.getEndCity());
                setAttr("end_county", end_county);
            }
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            ZxLine zxLine = new ZxLine();
            setAttr("zxLine",zxLine);
            setAttr("province", province);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    //创建专线
    public void save() {
        int zxLineId = getParaToInt("id",0);
        ZxLine zxLine1 = ZxLine.dao.findById(zxLineId);
        String start_province = getPara("start_province","0");
        String end_province = getPara("end_province","0");
        String start_city = getPara("start_city","0");
        String end_city = getPara("end_city","0");
        String end_county = getPara("end_county","0");
        String start_county = getPara("start_county","0");
        if (zxLine1 == null){
            ZxLine zxLine = new ZxLine();
            String setouttime1 = getPara("setouttime1");
            String setouttime2 = getPara("setouttime2");
            String setouttime3 = getPara("setouttime3");
            String setouttime4 = getPara("setouttime4");


            String starttime = setouttime1+":"+setouttime3;
            String endtime = setouttime2+":"+setouttime4;
            start = new DateTime(DateTime.parse(starttime,DateTimeFormat.forPattern("HH:mm")).toDate());
            end = new DateTime(DateTime.parse(endtime,DateTimeFormat.forPattern("HH:mm")).toDate());
            if (start.isAfter(end)) {
                end = end.plusDays(1);
            }
            zxLine.setSetoutTime1(start.toDate());
            zxLine.setSetoutTime2(end.toDate());
            zxLine.setPrice(BigDecimal.valueOf(Long.parseLong(getPara("price"))));
            zxLine.setSharingPrice(BigDecimal.valueOf(Long.parseLong(getPara("sharing_price"))));
            zxLine.setPriceSpecial(BigDecimal.valueOf(Long.parseLong(getPara("price_special"))));
            zxLine.setSharingPriceSpecial(BigDecimal.valueOf(Long.parseLong(getPara("sharing_price_special"))));
            zxLine.setStartProvince(start_province);
            zxLine.setEndProvince(end_province);
            zxLine.setStartCity(start_city);
            zxLine.setEndCity(end_city);
            zxLine.setEndCounty(end_county);
            zxLine.setStartCounty(start_county);
            if (start_county.equals("0")){
                if (start_city.equals("0")){
                    if (start_province.equals("0")){
                        renderAjaxError("未选择出发地点！");
                    }else {
                        zxLine.setStartProvince(start_province);
                    }
                }else {
                    zxLine.setStartCode(start_city);
                }
            }else {
                zxLine.setStartCode(start_county);
            }
            if (end_county.equals("0")){
                if (end_city.equals("0")){
                    if (end_province.equals("0")){
                        renderAjaxError("未选择终点！");
                    }else {
                        zxLine.setEndProvince(end_province);
                    }
                }else {
                    zxLine.setEndCode(end_city);
                }
            }else {
                zxLine.setEndCode(end_county);
            }
            zxLine.setStartAddressDetail(getPara("start_address_detail"));
            zxLine.setEndAddressDetail(getPara("end_address_detail"));
            zxLine.setType(getParaToInt("lineType"));
            zxLine.setDistance(Double.parseDouble(String.valueOf(getParaToInt("distance"))));
            zxLine.setUpdateTime(new DateTime().toDate());
            zxLine.setCreateTime(new DateTime().toDate());
            if (isSuperAdmin()) {
                zxLine.setCompanyId(getParaToInt("company"));
            }else {
                zxLine.setCompanyId(getCompanyId());
            }
            if (zxLine.save()){
                renderAjaxSuccess("创建成功");
            }else {
                renderAjaxFailure("创建失败");
            }
        }else {
            String setouttime1 = getPara("setouttime1");
            String setouttime2 = getPara("setouttime2");
            String setouttime3 = getPara("setouttime3");
            String setouttime4 = getPara("setouttime4");
            String starttime = setouttime1+":"+setouttime3;
            String endtime = setouttime2+":"+setouttime4;
            start = new DateTime(DateTime.parse(starttime,DateTimeFormat.forPattern("HH:mm")).toDate());
            end = new DateTime(DateTime.parse(endtime,DateTimeFormat.forPattern("HH:mm")).toDate());
            if (start.isAfter(end)) {
                end = end.plusDays(1);
            }
            zxLine1.setSetoutTime1(start.toDate());
            zxLine1.setSetoutTime2(end.toDate());
            zxLine1.setPrice(BigDecimal.valueOf(Long.parseLong(getPara("price"))));
            zxLine1.setSharingPrice(BigDecimal.valueOf(Long.parseLong(getPara("sharing_price"))));
            zxLine1.setPriceSpecial(BigDecimal.valueOf(Long.parseLong(getPara("price_special"))));
            zxLine1.setSharingPriceSpecial(BigDecimal.valueOf(Long.parseLong(getPara("sharing_price_special"))));
            zxLine1.setStartProvince(start_province);
            zxLine1.setEndProvince(end_province);
            zxLine1.setStartCity(start_city);
            zxLine1.setEndCity(end_city);
            zxLine1.setEndCounty(end_county);
            zxLine1.setStartCounty(start_county);
            if (start_county.equals("0")){
                if (start_city.equals("0")){
                    if (start_province.equals("0")){
                        renderAjaxError("未选择出发地点！");
                    }else {
                        zxLine1.setStartProvince(start_province);
                    }
                }else {
                    zxLine1.setStartCode(start_city);
                }
            }else {
                zxLine1.setStartCode(start_county);
            }
            if (end_county.equals("0")){
                if (end_city.equals("0")){
                    if (end_province.equals("0")){
                        renderAjaxError("未选择终点！");
                    }else {
                        zxLine1.setEndProvince(end_province);
                    }
                }else {
                    zxLine1.setEndCode(end_city);
                }
            }else {
                zxLine1.setEndCode(end_county);
            }
            zxLine1.setStartAddressDetail(getPara("start_address_detail"));
            zxLine1.setEndAddressDetail(getPara("end_address_detail"));
            zxLine1.setType(getParaToInt("lineType"));
            zxLine1.setDistance(Double.parseDouble(String.valueOf(getParaToInt("distance"))));
            if (isSuperAdmin()) {
                zxLine1.setCompanyId(getParaToInt("company"));
            }else {
                zxLine1.setCompanyId(getCompanyId());
            }
            if (zxLine1.update()){
                renderAjaxSuccess("修改成功");
            }else {
                renderAjaxFailure("修改失败");
            }
        }

    }
    //删除路线，更改flag状态
    public void del() {
        int id = getParaToInt("id",0);
        if (ZxLine.dao.updateflag(id) > 0) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxFailure("删除失败！");
        }

    }
}
