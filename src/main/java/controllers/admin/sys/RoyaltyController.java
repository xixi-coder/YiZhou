package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import dto.royaltydtos.MoneyItemDto;
import dto.royaltydtos.MoneyModelDto;
import dto.royaltydtos.SimpleModelDto;
import kits.TimeKit;
import models.royalty.RoyaltyStandard;
import models.royalty.RoyaltyStandardEasy;
import models.royalty.RoyaltyStandardMoney;
import models.royalty.RoyaltyStandardMoneyItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/12.
 */
@Controller("/admin/sys/royalty")
public class RoyaltyController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("royaltyStandard.column"), SqlManager.sql("royaltyStandard.where")));
        } else {
            whereSql = " AND company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("royaltyStandard.column"), SqlManager.sql("royaltyStandard.where"), whereSql, params));
        }
        return;
    }

    public void item() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            RoyaltyStandard royaltyStandard = RoyaltyStandard.dao.findById(id);
            setAttr("royaltyStandard", royaltyStandard);
            if (royaltyStandard.getType() == 1) {
                List<RoyaltyStandardEasy> royaltyStandardEasyList = RoyaltyStandardEasy.dao.findByRoyaltyStandard(royaltyStandard.getId());
                for (RoyaltyStandardEasy royaltyStandardEasy : royaltyStandardEasyList) {
                    royaltyStandardEasy.put("start_hour", TimeKit.splictHours(royaltyStandardEasy.getStartTime())[0]);
                    royaltyStandardEasy.put("start_minute", TimeKit.splictHours(royaltyStandardEasy.getStartTime())[1]);
                    royaltyStandardEasy.put("end_hour", TimeKit.splictHours(royaltyStandardEasy.getEndTime())[0]);
                    royaltyStandardEasy.put("end_minute", TimeKit.splictHours(royaltyStandardEasy.getEndTime())[1]);
                }
                setAttr("royaltyStandardEasyList", royaltyStandardEasyList);
            } else if (royaltyStandard.getType() == 2) {
                List<RoyaltyStandardMoney> royaltyStandardMoneys = RoyaltyStandardMoney.dao.findByRoyaltyStandard(royaltyStandard.getId());
                for (RoyaltyStandardMoney royaltyStandardMoney : royaltyStandardMoneys) {
                    List<RoyaltyStandardMoneyItem> royaltyStandardMoneyItems = RoyaltyStandardMoneyItem.dao.findByMoney(royaltyStandardMoney.getId());
                    royaltyStandardMoney.put("item", royaltyStandardMoneyItems);
                }
                setAttr("royaltyStandardMoney", royaltyStandardMoneys);
            }
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            RoyaltyStandard royaltyStandard = new RoyaltyStandard();
            setAttr("royaltyStandard", royaltyStandard);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        String name = getPara("name");
        int type = getParaToInt("type", 0);
        int timePoint = getParaToInt("timePoint", 0);
        final int id = getParaToInt("id", 0);

        String itemStr = getPara("item");
        final RoyaltyStandard royaltyStandard;
        if (id > 0) {
            royaltyStandard = RoyaltyStandard.dao.findById(id);
        } else {
            royaltyStandard = new RoyaltyStandard();
        }
        royaltyStandard.setTimePoint(timePoint);
        royaltyStandard.setType(type);
        royaltyStandard.setName(name);
        royaltyStandard.setCreateTime(DateTime.now().toDate());
        royaltyStandard.setCreater(getUserId());
        royaltyStandard.setCompany(getCompanyId());
        boolean isOk;
        if (type == 1) {
            List<SimpleModelDto> simpleModelDto = JSON.parseArray(itemStr, SimpleModelDto.class);
            final List<RoyaltyStandardEasy> royaltyStandardEasyList = Lists.newArrayList();
            for (SimpleModelDto modelDto : simpleModelDto) {
                RoyaltyStandardEasy royaltyStandardEasy = new RoyaltyStandardEasy();
                royaltyStandardEasy.setType(Ints.tryParse(modelDto.getType()));
                DateTime start = DateTime.parse(modelDto.getStartTime(), DateTimeFormat.forPattern("HH:mm"));
                DateTime end = DateTime.parse(modelDto.getEndTime(), DateTimeFormat.forPattern("HH:mm"));
                if (start.isAfter(end)) {
                    end = end.plusDays(1);
                }
                royaltyStandardEasy.setStartTime(start.toDate());
                royaltyStandardEasy.setEndTime(end.toDate());
                royaltyStandardEasy.setFixedMoney(new BigDecimal(modelDto.getGuTiYuan()));
                royaltyStandardEasy.setGetMoney1(new BigDecimal(modelDto.getGuTiYuan()));
                royaltyStandardEasy.setLessThanMoney1(new BigDecimal(modelDto.getGuShaoYuYuan()));
                royaltyStandardEasy.setLessThanMoney2(new BigDecimal(modelDto.getBiLiShaoYuYuan()));
                royaltyStandardEasy.setRatio(new BigDecimal(modelDto.getBiLiTiYuan()));
                royaltyStandardEasy.setGetMoney2(new BigDecimal(modelDto.getBiLiTiCheng()));
                royaltyStandardEasyList.add(royaltyStandardEasy);
            }
            if (royaltyStandard.getId() != null) {
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (royaltyStandard.update() && Db.update(SqlManager.sql("royaltyStandardEasy.deleByRoyaltyStandard"), id) >= 0) {
                            for (RoyaltyStandardEasy royaltyStandardEasy : royaltyStandardEasyList) {
                                royaltyStandardEasy.setRoyaltyStandard(royaltyStandard.getId());
                                if (!royaltyStandardEasy.save()) {
                                    return false;
                                }
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            } else {
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (royaltyStandard.save()) {
                            for (RoyaltyStandardEasy royaltyStandardEasy : royaltyStandardEasyList) {
                                royaltyStandardEasy.setRoyaltyStandard(royaltyStandard.getId());
                                if (!royaltyStandardEasy.save()) {
                                    return false;
                                }
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }

        } else if (type == 2) {
            List<MoneyModelDto> moneyModelDto = JSON.parseArray(itemStr, MoneyModelDto.class);
            final List<RoyaltyStandardMoney> royaltyStandardMoneys = Lists.newArrayList();
            for (MoneyModelDto modelDto : moneyModelDto) {
                RoyaltyStandardMoney royaltyStandardMoney = new RoyaltyStandardMoney();
                DateTime start = DateTime.parse(modelDto.getStartTime(), DateTimeFormat.forPattern("HH:mm"));
                DateTime end = DateTime.parse(modelDto.getEndTime(), DateTimeFormat.forPattern("HH:mm"));
                if (start.isAfter(end)) {
                    end = end.plusDays(1);
                }
                royaltyStandardMoney.setStartTime(start.toDate());
                royaltyStandardMoney.setEndTime(end.toDate());
                List<MoneyItemDto> itemDtos = modelDto.getItem();
                List<RoyaltyStandardMoneyItem> royaltyStandardMoneyItems = Lists.newArrayList();
                for (MoneyItemDto itemDto : itemDtos) {
                    RoyaltyStandardMoneyItem royaltyStandardMoneyItem = new RoyaltyStandardMoneyItem();
                    royaltyStandardMoneyItem.setGetMoney(new BigDecimal(itemDto.getTiMoney()));
                    royaltyStandardMoneyItem.setType(Ints.tryParse(itemDto.getType()));
                    royaltyStandardMoneyItem.setEachMoney(new BigDecimal(itemDto.getEachMoney()));
                    royaltyStandardMoneyItem.setEndMoney(new BigDecimal(itemDto.getEndMoney()));
                    royaltyStandardMoneyItem.setStartMoney(new BigDecimal(itemDto.getStartMoney()));
                    royaltyStandardMoneyItems.add(royaltyStandardMoneyItem);
                }
                royaltyStandardMoney.put("item", royaltyStandardMoneyItems);
                royaltyStandardMoneys.add(royaltyStandardMoney);
            }
            if (royaltyStandard.getId() != null) {
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (royaltyStandard.update()
                                && Db.update(SqlManager.sql("royaltyStandardMoney.deleByRoyaltyStandard"), id) >= 0
                                && Db.update(SqlManager.sql("royaltyStandardMoneyItem.deleByRoyaltyStandard"), id) >= 0
                                ) {
                            for (RoyaltyStandardMoney royaltyStandardMoney : royaltyStandardMoneys) {
                                royaltyStandardMoney.setRoyaltyStandard(royaltyStandard.getId());
                                List<RoyaltyStandardMoneyItem> royaltyStandardMoneyItemList = royaltyStandardMoney.get("item");
                                if (royaltyStandardMoney.save()) {
                                    for (RoyaltyStandardMoneyItem royaltyStandardMoneyItem : royaltyStandardMoneyItemList) {
                                        royaltyStandardMoneyItem.setParent(royaltyStandardMoney.getId());
                                        royaltyStandardMoneyItem.setRoyaltyStandard(royaltyStandard.getId());
                                        if (!royaltyStandardMoneyItem.save()) {
                                            return false;
                                        }
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
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (royaltyStandard.save()) {
                            for (RoyaltyStandardMoney royaltyStandardMoney : royaltyStandardMoneys) {
                                royaltyStandardMoney.setRoyaltyStandard(royaltyStandard.getId());
                                List<RoyaltyStandardMoneyItem> royaltyStandardMoneyItemList = royaltyStandardMoney.get("item");
                                if (royaltyStandardMoney.save()) {
                                    for (RoyaltyStandardMoneyItem royaltyStandardMoneyItem : royaltyStandardMoneyItemList) {
                                        royaltyStandardMoneyItem.setParent(royaltyStandardMoney.getId());
                                        royaltyStandardMoneyItem.setRoyaltyStandard(royaltyStandard.getId());
                                        if (!royaltyStandardMoneyItem.save()) {
                                            return false;
                                        }
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

        } else {
            isOk = false;
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxError("保存失败！");
        }
    }

    public void del() {
        final int id = getParaToInt("id", 0);
        boolean isOk;
        if (id > 0) {
            final RoyaltyStandard royaltyStandard = RoyaltyStandard.dao.findById(id);
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return Db.update(SqlManager.sql("royaltyStandardMoney.deleByRoyaltyStandard"), id) >= 0
                            && Db.update(SqlManager.sql("royaltyStandardMoneyItem.deleByRoyaltyStandard"), id) >= 0
                            && Db.update(SqlManager.sql("royaltyStandardEasy.deleByRoyaltyStandard"), id) >= 0
                            && royaltyStandard.delete();
                }
            });
            if (isOk) {
                renderAjaxSuccess("删除成功！");
            } else {
                renderAjaxFailure("删除失败！");
            }
        } else {
            renderAjaxFailure("删除失败！");
        }
    }
}
