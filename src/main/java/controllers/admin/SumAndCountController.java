package controllers.admin;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import dto.SqlConditionDto;
import kits.StringsKit;
import kits.TimeKit;
import models.company.CompanyAccount;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/12.
 */
@Controller("/admin/total")
public class SumAndCountController extends BaseAdminController {
    Logger logger = LoggerFactory.getLogger(SumAndCountController.class);

    public void base(String column, String where, String tmpWhere, List<Object> params) {
        try {


            if (params == null) {
                params = Lists.newArrayList();
            }
            if (Strings.isNullOrEmpty(tmpWhere)) {
                tmpWhere = "";
            }
            Map<String, String[]> requestParam = getParaMap();
            Map<String, String> searchP = Maps.newHashMap();
            for (String s : requestParam.keySet()) {
                if (s.startsWith("s" + SqlConditionDto.CONDITION)) {//取到所有条件
                    if (!Strings.isNullOrEmpty(requestParam.get(s)[0])) {
                        searchP.put(s, requestParam.get(s)[0]);
                    }
                }
            }
            for (String s : searchP.keySet()) {
                String[] tmps = s.split(SqlConditionDto.CONDITION);//s_a.id_LIKE
                String paramVal = searchP.get(s);
                String[] tmpCon = SqlConditionDto.getCondition(tmps[2], paramVal);
                if (StringUtils.equals(tmps[2], SqlConditionDto.BETWEEN)) {
                    tmpWhere += " AND " + tmps[1] + tmpCon[0];
                    String[] tmpBetweenParams = paramVal.split(",");
                    String start = tmpBetweenParams[0];
                    String end = tmpBetweenParams.length == 1 ? "" : tmpBetweenParams[1];
                    Date startDate;
                    Date endDate;
                    if (paramVal.startsWith(SqlConditionDto.DATE)) {
                        start = start.replace(SqlConditionDto.DATE, "");
                        if (Strings.isNullOrEmpty(start)) {
                            startDate = DateTime.now().millisOfDay().withMinimumValue().toDate();
                        } else {
                            startDate = DateTime.parse(start, DateTimeFormat.forPattern(TimeKit.YYYYMMDD)).millisOfDay().withMinimumValue().toDate();
                        }
                        if (Strings.isNullOrEmpty(end)) {
                            endDate = DateTime.now().millisOfDay().withMaximumValue().toDate();
                        } else {
                            endDate = DateTime.parse(end, DateTimeFormat.forPattern(TimeKit.YYYYMMDD)).millisOfDay().withMaximumValue().toDate();
                        }
                        params.add(startDate);
                        params.add(endDate);
                    } else if (paramVal.startsWith(SqlConditionDto.DATETIME)) {
                        start = start.replace(SqlConditionDto.DATETIME, "");
                        if (Strings.isNullOrEmpty(start)) {
                            startDate = DateTime.now().millisOfDay().withMinimumValue().toDate();
                        } else {
                            startDate = DateTime.parse(start, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)).toDate();
                        }
                        if (Strings.isNullOrEmpty(end)) {
                            endDate = DateTime.now().millisOfDay().withMinimumValue().toDate();
                        } else {
                            endDate = DateTime.parse(end, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)).toDate();
                        }
                        logger.info("开始{}", startDate);
                        logger.info("结束{}", endDate);
                        params.add(startDate);
                        params.add(endDate);
                    } else {
                        params.add(start);
                        params.add(end);
                        logger.info("开始{}", start);
                        logger.info("结束{}", end);
                    }
                } else {
                    if (tmpCon[1].equals(SqlConditionDto.ISNULL)) {
                        tmpWhere += " AND " + tmps[1] + " IS NULL ";
                    } else if (tmpCon[1].equals(SqlConditionDto.ISNOTNULL)) {
                        tmpWhere += " AND " + tmps[1] + " IS NOT NULL ";
                    } else {
                        tmpWhere += " AND " + tmps[1] + tmpCon[0];
                        params.add(tmpCon[1]);
                    }

                }
            }
            where = StringsKit.replaceSql(where, tmpWhere);
            Number number = Db.queryNumber(column + where, params.toArray());
            renderAjaxSuccess(number);
        } catch (Exception e) {
            e.printStackTrace();
            renderAjaxError("系统错误！");
        }
    }

    public void base(String column, String where) {
        base(column, where, "", null);
    }

    public void base(String column, String where, List<Object> params) {
        base(column, where, "", params);
    }

    //订单总额
    public void dingdanzonge() {
        String whereSql = "";
        int type = getParaToInt("type");
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(false);
        String column = "Select SUM(amount) s ";
        String where = SqlManager.sql("order.where");
        if (isSuperAdmin()) {
            base(column, where, "", params);
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            base(column, where, whereSql, params);
        }
    }

    //订单搜索总额
    public void dingdansousuogzonge() {
        String whereSql = "";
        int type = getParaToInt("type");
        List<Object> params = Lists.newArrayList();
        params.add(type);
        params.add(false);
        String column = "Select SUM(amount) s ";
        String where = SqlManager.sql("order.where");
        if (isSuperAdmin()) {
            base(column, where, "", params);
        } else {
            whereSql = " AND ddo.company = ? ";
            params.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                whereSql = whereSql + " AND ddi.creater = ? ";
                params.add(getUserId());
            }
            base(column, where, whereSql, params);
        }
    }

    /**
     * 快车派单车辆
     */
    public void zxOrder() {
        String column = "SELECT COUNT(*) s ";
        String where = "FROM dele_schedule WHERE orderId != \"\" AND `status` = 0-- where \n";
        List<Object> params = Lists.newArrayList();
        String whereSql = "  AND 1=1 ";
        if (isSuperAdmin()) {
            base(column, where);
        } else {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 财务明细统计
     */
    public void caiwumingxi() {
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        String column = "SELECT SUM(dod.amount) ";
        if (isSuperAdmin()) {
            base(column, SqlManager.sql("orderDetail.where"));
        } else {
            whereSql = " AND dod.company = ?";
            params.add(getCompanyId());
            base(column, SqlManager.sql("orderDetail.where"), whereSql, params);
        }
    }

    /**
     * 财务统计保险费用
     */
    public void caiwumingxibaoxian() {
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        String column = "SELECT SUM(dod.amount) ";
        whereSql = " AND dod.type = ? ";
        params.add(Constant.CapitalOperationType.BAOXIAN);
        if (isSuperAdmin()) {
            base(column, SqlManager.sql("orderDetail.where"), whereSql, params);
        } else {
            whereSql = " AND dod.company = ?";
            params.add(getCompanyId());
            base(column, SqlManager.sql("orderDetail.where"), whereSql, params);
        }
    }


    /**
     * 账号明细统计
     */
    public void zhanghumingxi() {
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        String column = "SELECT SUM(amount) ";
        params.add(Constant.CapitalStatus.OK);
        int type = getParaToInt(0, 5);
        String fromSql = SqlManager.sql("capitalLog.where");
        if (type == 6) {
            fromSql = SqlManager.sql("capitalLog.where1");
        }
        if (isSuperAdmin()) {
            base(column, fromSql, params);
        } else {
            whereSql = " AND company  = ?";
            params.add(getCompanyId());
            base(column, fromSql, whereSql, params);
        }
    }

    /**
     * 账号预存款信息
     */
    public void yucunkuang() {
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        String column = "SELECT SUM(dmc.amount) ";
        int type = getParaToInt(0, 3);
        String fromSql = SqlManager.sql("memberCapital.where");
        if (type == 4) {
            fromSql = SqlManager.sql("memberCapital.where1");
        }
        if (isSuperAdmin()) {
            base(column, fromSql, params);
        } else {
            whereSql = " AND company  = ?";
            params.add(getCompanyId());
            base(column, fromSql, whereSql, params);
        }
    }

    /**
     * 预存款统计
     */
    public void yucunkuantongji() {
        String column = "SELECT SUM((SELECT\n" +
                "\t\t\tSUM(dcl.amount)\n" +
                "\t\tFROM\n" +
                "\t\t\tdele_capital_log dcl\n" +
                "\t\tWHERE\n" +
                "\t\t\tdcl.login_id = ddi.login_id\n" +
                "\t\tAND (\n" +
                "\t\t\tdcl.operation_type = 11\n" +
                "\t\t\tOR dcl.operation_type =12\n" +
                "\t\t) AND dcl.status=1)) s ";
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        int type = getParaToInt(0, 3);
        String fromSql = SqlManager.sql("memberCapital.where");
        if (type == 4) {
            column = "SELECT SUM((\n" +
                    "\t\tSELECT\n" +
                    "\t\t\tSUM(dcl.amount)\n" +
                    "\t\tFROM\n" +
                    "\t\t\tdele_capital_log dcl\n" +
                    "\t\tWHERE\n" +
                    "\t\t\tdcl.login_id = dmi.login_id\n" +
                    "\t\tAND (\n" +
                    "\t\t\tdcl.operation_type = 11\n" +
                    "\t\t\tOR dcl.operation_type =12\n" +
                    "\t\t) AND dcl.status=1\n" +
                    "\t)) s ";
            fromSql = SqlManager.sql("memberCapital.where1");
        }
        if (isSuperAdmin()) {
            base(column, fromSql, params);
        } else {
            whereSql = " AND company  = ?";
            params.add(getCompanyId());
            base(column, fromSql, whereSql, params);
        }
    }


    /**
     * 订单数统计
     */
    public void kehuxiaofeitongjidingdanshu() {
        int type = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        String column = "SELECT count(1) c";
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
        String whereSql = "";
        String fromSql = SqlManager.sql("consume.tongjiwhere");
        if (isSuperAdmin()) {
            base(column, fromSql, params);
        } else {
            whereSql = " AND dd.company  = ?";
            params.add(getCompanyId());
            base(column, fromSql, whereSql, params);
        }
    }

    /**
     * 订单金额统计
     */
    public void kehuxiaofeitongjidingdanjine() {
        int type = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        String column = "SELECT SUM(dd.amount) s ";
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
        String whereSql = "";
        String fromSql = SqlManager.sql("consume.tongjiwhere");
        if (isSuperAdmin()) {
            base(column, fromSql, params);
        } else {
            whereSql = " AND dd.company  = ? ";
            params.add(getCompanyId());
            base(column, fromSql, whereSql, params);
        }
    }

    /**
     * 订单数量
     */
    public void orderCount() {
        String column = "SELECT count(1) c ";
        String where = " FROM dele_order WHERE 1=1 -- where \n";
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        String whereSql = " AND status = ? ";
        if (isSuperAdmin()) {
            base(column, where, whereSql, params);
        } else {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 今日订单数量
     */
    public void todayOrderCount() {
        String column = "SELECT count(1) c ";
        String where = " FROM dele_order WHERE 1=1 -- where \n";
        Date todayStart = DateTime.now().millisOfDay().withMinimumValue().toDate();
        Date todayEnd = DateTime.now().millisOfDay().withMaximumValue().toDate();
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        String whereSql = " AND status = ? AND create_time BETWEEN ? AND ? ";
        params.add(todayStart);
        params.add(todayEnd);
        if (isSuperAdmin()) {
            base(column, where, whereSql, params);
        } else {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 订单收入
     */
    public void orderAmount() {
        String column = "SELECT SUM(amount) s ";
        String where = " FROM dele_order WHERE 1=1 -- where \n";
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        String whereSql = "  AND status = ? ";
        if (isSuperAdmin()) {
            base(column, where, whereSql, params);
        } else {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 今日订单收入
     */
    public void todayOrderAmount() {
        String column = "SELECT SUM(amount) s ";
        String where = " FROM dele_order WHERE 1=1 -- where \n";
        Date todayStart = DateTime.now().millisOfDay().withMinimumValue().toDate();
        Date todayEnd = DateTime.now().millisOfDay().withMaximumValue().toDate();
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.PAYED);
        String whereSql = "  AND status = ? AND create_time BETWEEN ? AND ? ";
        params.add(todayStart);
        params.add(todayEnd);
        if (isSuperAdmin()) {
            base(column, where, whereSql, params);
        } else {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 报警
     */
    public void alarmCount() {
        String column = "SELECT count(1) c";
        String where = " FROM dele_alarm WHERE 1=1 -- where \n";
        List<Object> params = Lists.newArrayList();
        params.add(0);
        String whereSql = " AND status = ? ";
        if (isSuperAdmin()) {
            base(column, where, whereSql, params);
        } else {
            whereSql += " AND company_id = ?  ";
            params.add(getCompanyId());
            base(column, where, whereSql, params);
        }
    }

    /**
     * 财务统计公用方法
     */
    public void caiwutongji(Date start, Date end) {
        String column = "SELECT SUM(amount) ";
        String fromSql = " FROM dele_order_detail WHERE 1=1 -- where ";
        String where = " AND audit_status = ? ";
        List<Object> params = Lists.newArrayList();
        params.add(true);
        if (start != null && end != null) {
            where += " AND create_time BETWEEN ? AND ? ";
            params.add(start);
            params.add(end);
        }
        if (isSuperAdmin()) {
            base(column, fromSql, where, params);
        } else {
            where += " AND company = ? ";
            params.add(getCompanyId());
            base(column, fromSql, where, params);
        }
    }

    /**
     * 总收入
     */
    public void zongshouru() {
        caiwutongji(null, null);
    }

    /**
     * 本年收入
     */
    public void yearshouru() {
        Date start = DateTime.now().dayOfYear().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date end = DateTime.now().dayOfYear().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        caiwutongji(start, end);
    }

    /**
     * 月收入
     */
    public void monuthshouru() {
        Date start = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
        Date end = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
        caiwutongji(start, end);
    }

    /**
     * 昨日收入
     */
    public void yestartdayshouru() {
        Date start = DateTime.now().plusDays(-1).millisOfDay().withMinimumValue().toDate();
        Date end = DateTime.now().plusDays(-1).millisOfDay().withMaximumValue().toDate();
        caiwutongji(start, end);
    }

    /**
     * 今日收入
     */
    public void todayshouru() {
        Date start = DateTime.now().millisOfDay().withMinimumValue().toDate();
        Date end = DateTime.now().millisOfDay().withMaximumValue().toDate();
        caiwutongji(start, end);
    }

    /**
     * 在线司机统计
     */
    public void zaixiansiji() {
        String column = "SELECT COUNT(1) ";
        String where = " FROM dele_member_login dml LEFT JOIN dele_driver_info ddi ON ddi.login_id=dml.id WHERE ddi.status = 1 -- where ";
        String tmpWhere = " AND dml.type = ? ";
        List<Object> params = Lists.newArrayList();
        params.add(Constant.DRIVER);
        tmpWhere += " AND dml.status = ? ";
        params.add(Constant.LoginStatus.RECIVEDORDER);
        if (!isSuperAdmin()) {
            tmpWhere += " AND ddi.company = ? ";
            params.add(getCompanyId());
        }
        base(column, where, tmpWhere, params);
    }

    /**
     * 离线司机统计
     */
    public void lixiansiji() {
        String column = "SELECT COUNT(1) ";
        String where = " FROM dele_member_login dml LEFT JOIN dele_driver_info ddi ON ddi.login_id=dml.id WHERE ddi.status = 1 -- where ";
        String tmpWhere = " AND dml.type = ? ";
        List<Object> params = Lists.newArrayList();
        params.add(Constant.DRIVER);
        tmpWhere += " AND (dml.status = ? OR dml.status = ?)";
        params.add(Constant.LoginStatus.LOGOUTED);
        params.add(Constant.LoginStatus.LOGINED);
        if (!isSuperAdmin()) {
            tmpWhere += " AND ddi.company = ? ";
            params.add(getCompanyId());
        }
        base(column, where, tmpWhere, params);
    }

    /**
     * 忙碌司机统计
     */
    public void manglusiji() {
        String column = "SELECT COUNT(1) ";
        String where = " FROM dele_member_login dml LEFT JOIN dele_driver_info ddi ON ddi.login_id=dml.id WHERE ddi.status = 1 -- where ";
        String tmpWhere = " AND dml.type = ? ";
        List<Object> params = Lists.newArrayList();
        params.add(Constant.DRIVER);
        tmpWhere += " AND (dml.status = ? OR dml.status = ? OR dml.status = ?)";
        params.add(Constant.LoginStatus.RECIVEDORDERPD);
        params.add(Constant.LoginStatus.RECIVEDORDERPDNO);
        params.add(Constant.LoginStatus.BUSY);
        if (!isSuperAdmin()) {
            tmpWhere += " AND ddi.company = ? ";
            params.add(getCompanyId());
        }
        base(column, where, tmpWhere, params);
    }

    /**
     * 公司剩余活动资金
     */
    public void companyActivityAmount() {
        CompanyAccount account = CompanyAccount.dao.findById(getCompanyId());
        renderAjaxSuccess(account.getActivityAmount());
    }

    public void companyActivityUsed() {
        List<Object> params = Lists.newArrayList();
        String tmpWhere = "";
        params.add(Constant.DataAuditStatus.AUDITOK);
        if (isSuperAdmin()) {
            if (getParaToInt("company", 0) != 0) {
                tmpWhere += " AND dca.company = ? ";
                params.add(getParaToInt("company"));
            }
        } else {
            tmpWhere += " AND dca.company = ? ";
            params.add(getCompanyId());
        }
        String column = "SELECT SUM(amount) ";
        String where = " FROM dele_company_activity dca WHERE status = ?  -- where ";
        base(column, where, tmpWhere, params);
    }

    public void companyActivityGrant() {
        List<Object> params = Lists.newArrayList();
        String tmpWhere = "";
        params.add(Constant.DataAuditStatus.CREATE);
        if (isSuperAdmin()) {
            if (getParaToInt("company", 0) != 0) {
                tmpWhere += " AND dca.company = ? ";
                params.add(getParaToInt("company"));
            }
        } else {
            tmpWhere += " AND dca.company = ? ";
            params.add(getCompanyId());
        }
        String column = "SELECT SUM(amount) ";
        String where = " FROM dele_company_activity dca WHERE status = ?  -- where ";
        base(column, where, tmpWhere, params);
    }

}
