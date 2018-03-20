package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import dto.chargestandarddtos.ChargestandardDto;
import dto.chargestandarddtos.ChargestandardItemDto;
import dto.chargestandarddtos.MillItemDto;
import kits.StringsKit;
import kits.TimeKit;
import models.sys.ChargeStandard;
import models.sys.ChargeStandardItem;
import models.sys.ChargeStandardMileage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/18.
 */
@Controller("/admin/sys/chargestandard")
public class ChargestandardController extends BaseAdminController {
    public void index() {
        int type = getParaToInt(0,0);
        setAttr("type",type);
        render("list.ftl");
    }

    public void list() {
        int type = getParaToInt(0,0);
        String where;
        if (type==1) {
            where = "and type=1";
        }else {
            where = "and type is null";
        }
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("chargeStandard.column"), StringsKit.replaceSql(SqlManager.sql("chargeStandard.where"),where)));
        } else {
            String sql = " AND company = ?";
            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("chargeStandard.column"), StringsKit.replaceSql(SqlManager.sql("chargeStandard.where"),where), sql, params));
        }

    }

    public void item() {
        int id = getParaToInt(0, 0);
        int type = getParaToInt("type",0);
        setAttr("type",type);
        ChargeStandard chargeStandard;
        if (id > 0) {
            chargeStandard = ChargeStandard.dao.findById(id);
            setAttr("chargeStandard", chargeStandard);
            List<ChargeStandardItem> chargeStandardItems = ChargeStandardItem.dao.findByChargeStandard(chargeStandard.getId());
            for (ChargeStandardItem chargeStandardItem : chargeStandardItems) {
                List<ChargeStandardMileage> chargeStandardMileages = ChargeStandardMileage.dao.findByChargeStandardItem(chargeStandardItem.getId());
                chargeStandardItem.put("items", chargeStandardMileages);
                chargeStandardItem.put("start_hour", TimeKit.splictHours(chargeStandardItem.getStartTime())[0]);
                chargeStandardItem.put("start_minute", TimeKit.splictHours(chargeStandardItem.getStartTime())[1]);
                chargeStandardItem.put("end_hour", TimeKit.splictHours(chargeStandardItem.getEndTime())[0]);
                chargeStandardItem.put("end_minute", TimeKit.splictHours(chargeStandardItem.getEndTime())[1]);
            }
            setAttr("items", chargeStandardItems);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            chargeStandard = new ChargeStandard();
            setAttr("chargeStandard", chargeStandard);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void save() {
        final int type = getParaToInt(0,0);
        int id = getParaToInt("id", 0);
        String item = getPara("item");
        boolean isOk = false;
        if (id > 0) {
            final ChargeStandard chargeStandard = ChargeStandard.dao.findById(id);
            final List<ChargeStandardItem> chargeStandardItems = Lists.newArrayList();
            try {
                initList(item, chargeStandard, chargeStandardItems);
            } catch (Exception e) {
                e.printStackTrace();
                renderAjaxError("系统错误稍后再试！");
                return;
            }
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    chargeStandard.setLastUpdateTime(DateTime.now().toDate());
                    if (chargeStandard.update()
                            && Db.update(SqlManager.sql("chargeStandardItem.delByChargeStandard"), chargeStandard.getId()) >= 0
                            && Db.update(SqlManager.sql("chargeStandardMileage.delByChargeStandard"), chargeStandard.getId()) >= 0
                            ) {
                        for (ChargeStandardItem chargeStandardItem : chargeStandardItems) {
                            chargeStandardItem.setChargeStandard(chargeStandard.getId());
                            DateTime start = new DateTime(chargeStandardItem.getStartTime());
                            DateTime end = new DateTime(chargeStandardItem.getEndTime());
                            if (start.isAfter(end)) {
                                end = end.plusDays(1);
                            }
                            chargeStandardItem.setStartTime(start.toDate());
                            chargeStandardItem.setEndTime(end.toDate());
                            if (!chargeStandardItem.save()) {
                                return false;
                            }
                            List<ChargeStandardMileage> chargeStandardMileages = chargeStandardItem.get("mill");
                            for (ChargeStandardMileage chargeStandardMileage : chargeStandardMileages) {
                                chargeStandardMileage.setChargeStandard(chargeStandard.getId());
                                chargeStandardMileage.setChargeStandardItem(chargeStandardItem.getId());
                                if (!chargeStandardMileage.save()) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {
            final ChargeStandard chargeStandard = new ChargeStandard();
            final List<ChargeStandardItem> chargeStandardItems = Lists.newArrayList();
            try {
                initList(item, chargeStandard, chargeStandardItems);
            } catch (Exception e) {
                renderAjaxError("系统错误稍后再试！");
                return;
            }
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (type==1) {
                        chargeStandard.setType(1);
                    }
                    if (chargeStandard.save()) {
                        for (ChargeStandardItem chargeStandardItem : chargeStandardItems) {
                            chargeStandardItem.setChargeStandard(chargeStandard.getId());
                            DateTime start = new DateTime(chargeStandardItem.getStartTime());
                            DateTime end = new DateTime(chargeStandardItem.getEndTime());
                            if (start.isAfter(end)) {
                                end = end.plusDays(1);
                            }
                            chargeStandardItem.setStartTime(start.toDate());
                            chargeStandardItem.setEndTime(end.toDate());
                            if (!chargeStandardItem.save()) {
                                return false;
                            }
                            List<ChargeStandardMileage> chargeStandardMileages = chargeStandardItem.get("mill");
                            for (ChargeStandardMileage chargeStandardMileage : chargeStandardMileages) {
                                chargeStandardMileage.setChargeStandardItem(chargeStandardItem.getId());
                                chargeStandardMileage.setChargeStandard(chargeStandard.getId());
                                if (!chargeStandardMileage.save()) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        if (isOk) {
            renderAjaxSuccess("成功！");
        } else {
            renderAjaxError("失败！");
        }
    }

    public void del() {
        final int id = getParaToInt("id", 0);
        boolean isOk;
        if (id > 0) {
            final ChargeStandard chargeStandard = ChargeStandard.dao.findById(id);
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return chargeStandard.delete() && Db.update(SqlManager.sql("chargeStandardItem.delByChargeStandard"), id) >= 0
                            && Db.update(SqlManager.sql("chargeStandardMileage.delByChargeStandard"), id) >= 0;
                }
            });
            if (isOk) {
                renderAjaxSuccess("删除成功！");
            } else {
                renderAjaxError("删除失败！");
            }
        } else {
            renderAjaxError("删除错误！");
        }
    }

    private void initList(String item, ChargeStandard chargeStandard, List<ChargeStandardItem> chargeStandardItems) {
        ChargestandardDto chargestandardDto = JSON.parseObject(item, ChargestandardDto.class);
        chargeStandard.setCreateTime(DateTime.now().toDate());
        chargeStandard.setCreater(getUserId());
        chargeStandard.setName(chargestandardDto.getName());
        chargeStandard.setDesc(chargestandardDto.getDesc());
        if (isSuperAdmin()) {
            chargeStandard.setCompany(chargestandardDto.getCompany());
        } else {
            chargeStandard.setCompany(getCompanyId());
        }
        List<ChargestandardItemDto> chargestandardItemDtos = chargestandardDto.getCdItem();
        for (ChargestandardItemDto chargestandardItemDto : chargestandardItemDtos) {
            ChargeStandardItem chargeStandardItem = new ChargeStandardItem();
            chargeStandardItem.setStartTime(DateTime.parse(chargestandardItemDto.getStartTime(), DateTimeFormat.forPattern("HH:mm")).toDate());
            chargeStandardItem.setEndTime(DateTime.parse(chargestandardItemDto.getEndTime(), DateTimeFormat.forPattern("HH:mm")).toDate());
            chargeStandardItem.setName(chargestandardDto.getName());
            chargeStandardItem.setCreater(getUserId());
            chargeStandardItem.setCreateTime(DateTime.now().toDate());
            chargeStandardItem.setBaseAmount(new BigDecimal(chargestandardItemDto.getBaseTime()));
            chargeStandardItem.setMinAmount(new BigDecimal(chargestandardItemDto.getMinAmount()));

            chargeStandardItem.setQibufenzhong(new BigDecimal(chargestandardItemDto.getQibuhoufenzhonglei()));//起步后多少分钟内
            chargeStandardItem.setBuzufengzhong(new BigDecimal(chargestandardItemDto.getBuzufenzhong()));//每多少分钟
            chargeStandardItem.setFengzhongjiashoujine(new BigDecimal(chargestandardItemDto.getJiashoufeiyong()));//不足多少分钟
            chargeStandardItem.setMeiduoshaofengzhong(new BigDecimal(chargestandardItemDto.getMeiduoshaofenzhong()));//加收多少元

            chargeStandardItem.setBuzugonglishu(new BigDecimal(chargestandardItemDto.getBuzugongli()));//超过多少公里数
            chargeStandardItem.setGonglijiashoujine(new BigDecimal(chargestandardItemDto.getJiashou()));//每多少公里
            chargeStandardItem.setMeiduoshaogongli(new BigDecimal(chargestandardItemDto.getMeigongli()));//不足多少公里
            chargeStandardItem.setChaoguogonglishu(new BigDecimal(chargestandardItemDto.getChaoguogongli()));//加收多少元

            chargeStandardItem.setMianfeidenghoufenzhong(new BigDecimal(chargestandardItemDto.getMianfeidenghou()));//免费等侯多少分钟
            chargeStandardItem.setDenghouchaoguolijijiashoujine(new BigDecimal(chargestandardItemDto.getLijichaoshijiashoufeiyong()));//超时立即加收多少元
            chargeStandardItem.setDenghoumeiduoshaofengzhong(new BigDecimal(chargestandardItemDto.getChaoshimeiduoshaofenzhong()));//超时每多少分钟
            chargeStandardItem.setDenghoudiyuduoshaofenzhong(new BigDecimal(chargestandardItemDto.getChaoshibuzufenzhong()));//等候不足多少分钟
            chargeStandardItem.setDenghouchaoguojiashoujine(new BigDecimal(chargestandardItemDto.getChaoshijiashoufeiyong()));//加收多少元

            chargeStandardItem.setOneDiscount1(new BigDecimal(chargestandardItemDto.getOnediscount1()));
            chargeStandardItem.setOneDiscount2(new BigDecimal(chargestandardItemDto.getOnediscount2()));
            chargeStandardItem.setOneDiscount3(new BigDecimal(chargestandardItemDto.getOnediscount3()));
            chargeStandardItem.setOneDiscount4(new BigDecimal(chargestandardItemDto.getOnediscount4()));
            chargeStandardItem.setOneDiscount5(new BigDecimal(chargestandardItemDto.getOnediscount5()));
            chargeStandardItem.setOneDistance1(new Integer(chargestandardItemDto.getOnedistance1()));
            chargeStandardItem.setOneDistance2(new Integer(chargestandardItemDto.getOnedistance2()));
            chargeStandardItem.setOneDistance3(new Integer(chargestandardItemDto.getOnedistance3()));
            chargeStandardItem.setOneDistance4(new Integer(chargestandardItemDto.getOnedistance4()));
            chargeStandardItem.setOneDistance5(new Integer(chargestandardItemDto.getOnedistance5()));

            chargeStandardItem.setTwoDiscount1(new BigDecimal(chargestandardItemDto.getTwodiscount1()));
            chargeStandardItem.setTwoDiscount2(new BigDecimal(chargestandardItemDto.getTwodiscount2()));
            chargeStandardItem.setTwoDiscount3(new BigDecimal(chargestandardItemDto.getTwodiscount3()));
            chargeStandardItem.setTwoDiscount4(new BigDecimal(chargestandardItemDto.getTwodiscount4()));
            chargeStandardItem.setTwoDiscount5(new BigDecimal(chargestandardItemDto.getTwodiscount5()));
            chargeStandardItem.setTwoDistance1(new Integer(chargestandardItemDto.getTwodistance1()));
            chargeStandardItem.setTwoDistance2(new Integer(chargestandardItemDto.getTwodistance2()));
            chargeStandardItem.setTwoDistance3(new Integer(chargestandardItemDto.getTwodistance3()));
            chargeStandardItem.setTwoDistance4(new Integer(chargestandardItemDto.getTwodistance4()));
            chargeStandardItem.setTwoDistance5(new Integer(chargestandardItemDto.getTwodistance5()));

            chargeStandardItem.setThreeDiscount1(new BigDecimal(chargestandardItemDto.getThreediscount1()));
            chargeStandardItem.setThreeDiscount2(new BigDecimal(chargestandardItemDto.getThreediscount2()));
            chargeStandardItem.setThreeDiscount3(new BigDecimal(chargestandardItemDto.getThreediscount3()));
            chargeStandardItem.setThreeDiscount4(new BigDecimal(chargestandardItemDto.getThreediscount4()));
            chargeStandardItem.setThreeDiscount5(new BigDecimal(chargestandardItemDto.getThreediscount5()));
            chargeStandardItem.setThreeDistance1(new Integer(chargestandardItemDto.getThreedistance1()));
            chargeStandardItem.setThreeDistance2(new Integer(chargestandardItemDto.getThreedistance2()));
            chargeStandardItem.setThreeDistance3(new Integer(chargestandardItemDto.getThreedistance3()));
            chargeStandardItem.setThreeDistance4(new Integer(chargestandardItemDto.getThreedistance4()));
            chargeStandardItem.setThreeDistance5(new Integer(chargestandardItemDto.getThreedistance5()));
            List<MillItemDto> millItemDtos = chargestandardItemDto.getItem();
            List<ChargeStandardMileage> chargeStandardMileages = Lists.newArrayList();
            for (MillItemDto millItemDto : millItemDtos) {
                ChargeStandardMileage chargeStandardMileage = new ChargeStandardMileage();
                chargeStandardMileage.setStart(new BigDecimal(millItemDto.getStart()));
                chargeStandardMileage.setEnd(new BigDecimal(millItemDto.getEnd()));
                chargeStandardMileage.setJiajiajine(new BigDecimal(millItemDto.getMoney()));
                chargeStandardMileages.add(chargeStandardMileage);
            }
            chargeStandardItem.put("mill", chargeStandardMileages);
            chargeStandardItems.add(chargeStandardItem);
        }
    }
}
