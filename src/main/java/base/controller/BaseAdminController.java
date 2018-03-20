package base.controller;

import base.datatable.DataTablePage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import dto.SqlConditionDto;
import interceptors.ShiroInterceptor;
import kits.StringsKit;
import kits.TimeKit;
import kits.excel.PoiRender;
import models.company.Company;
import models.sys.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/20.
 */
@Before(ShiroInterceptor.class)

public class BaseAdminController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected int getDraw() {
        return getParaToInt("draw", 1);
    }

    protected int getPageSize() {
        return getParaToInt("length", 9);
    }

    protected int getStart() {
        return getParaToInt("start", 0);
    }

    protected User getUser() {
        return User.dao.findByUserName((String) SecurityUtils.getSubject().getPrincipal());
    }

    protected int getUserId() {
        return getUser().getId();
    }

    protected Company getCompany() {
        Company company = Company.dao.findByUser(getUserId());
        return company;
    }

    protected boolean isSuperAdmin() {
        return getUser().getSuperAdmin() == null ? false : getUser().getSuperAdmin();
    }

    protected int getUserType() {
        return getUser().getType();
    }

    protected int getCompanyId() {
        return getCompany().getId();
    }

    public DataTablePage dataTable(String column, String where, List<Object> params) {
        return dataTable(column, where, "", params);
    }

    public DataTablePage dataTable(String column, String where, List<Object> params,String columnPage, String wherePage) {
        return dataTable(column, where, "", params,columnPage,wherePage);
    }

    public DataTablePage dataTable(String column, String where, String tmpWhere, List<Object> params) {
        int pageStart = getStart();
        int pageSize = getPageSize();
        int draw = getDraw();
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
        where = StringsKit.replaceSql(where,tmpWhere);
        Record record = Db.findFirst("select count(1) c from (" +column + where+ ") a", params.toArray());
        int count = record == null ? 0 : record.getNumber("c").intValue();
        List<Record> data;
        if (count != 0) {
            where = where + " \r LIMIT ?,?";
            params.add(pageStart);
            params.add(pageSize);
            data = Db.find(column + where, params.toArray());
        } else {
            data = Lists.newArrayList();
        }
        DataTablePage dataTablePage = new DataTablePage(data, draw, count, count, null);
        return dataTablePage;
    }

    public DataTablePage dataTable(String column, String where, String tmpWhere, List<Object> params,String columnPage, String wherePage) {
        int pageStart = getStart();
        int pageSize = getPageSize();
        int draw = getDraw();
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
        List<Record> data;
        where = StringsKit.replaceSql(where,tmpWhere);
        wherePage = StringsKit.replaceSql(wherePage,tmpWhere);
        Record record = Db.findFirst((columnPage + wherePage), params.toArray());
        int count = record == null ? 0 : record.getNumber("c").intValue();
        if (count != 0) {
            where = where + " \r LIMIT ?,?";
            params.add(pageStart);
            params.add(pageSize);
            System.out.println("执行数据库语句为："+column + where+"--------参数为："+params.toArray().toString());
            data = Db.find(column + where, params.toArray());
        } else {
            data = Lists.newArrayList();
        }
        DataTablePage dataTablePage = new DataTablePage(data, draw, count, count, null);
        return dataTablePage;
    }

    public DataTablePage dataTable(String column, String where) {
        String customerWhere = "";
        List<Object> params = Lists.newArrayList();
        return dataTable(column, where, customerWhere, params);
    }

    public DataTablePage dataTable(String column, String where,String columnPage, String wherePage) {
        String customerWhere = "";
        List<Object> params = Lists.newArrayList();
        return dataTable(column, where, customerWhere, params,columnPage, wherePage);
    }

    public void poiRender(String sql, String[] header, String[] columns, String sheetName, String fileName) {
        List<Record> list = Db.find(sql);
        Render poirender = PoiRender.me(list).fileName(fileName).headers(header).sheetName(sheetName).columns(columns);
        render(poirender);
    }

    public void poiRender(String sql, List<Object> params, String[] header, String[] columns, String sheetName, String fileName) {
        List<Record> list = Db.find(sql, params.toArray());
        Render poirender = PoiRender.me(list).fileName(fileName).headers(header).sheetName(sheetName).columns(columns);
        render(poirender);
    }

    public void poiRender(List<Record> list, String[] header, String[] columns, String sheetName, String fileName) {
        Render poirender = PoiRender.me(list).fileName(fileName).headers(header).sheetName(sheetName).columns(columns);
        render(poirender);
    }
}
