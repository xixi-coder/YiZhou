package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import kits.StringsKit;
import models.company.Company;
import models.sys.Notice;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;
import services.SendNoticeService;

import java.util.List;

/**
 * Created by admin on 2016/11/4.
 */
@Controller("/admin/sys/notice")
public class NoticeController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("notice.cloumn"), SqlManager.sql("notice.where"),SqlManager.sql("notice.cloumnPage"), SqlManager.sql("notice.wherePage")));
        } else {
            String whereSql = SqlManager.sql("notice.where");
            String whereSqlPage = SqlManager.sql("notice.wherePage");
            whereSql = StringsKit.replaceSql(whereSql, " AND company = ? ");
            whereSqlPage = StringsKit.replaceSql(whereSqlPage, " AND company = ? ");

            List<Object> params = Lists.newArrayList();
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("notice.cloumn"), whereSql, params,SqlManager.sql("notice.cloumnPage"), whereSqlPage));
        }
    }

    public void item() {
        int noticeId = getParaToInt(0, 0);
        Notice notice;
        if (isSuperAdmin()) {
            setAttr("company", Company.dao.findByEnable());
        }
        if (noticeId == 0) {
            notice = new Notice();
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        } else {
            notice = Notice.dao.findById(noticeId);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        }
        setAttr("notice", notice);
    }

    public void save() {
        Notice notice = getModel(Notice.class, "notice");
        boolean isOk;
        String reciverInfo = getPara("memberInfo");
        JSONObject jsonObject = JSON.parseObject(reciverInfo);
        notice.setReciver(jsonObject.getString("ids"));
        notice.setReciveType(jsonObject.getInteger("type"));
        if (!isSuperAdmin()) {
            notice.setCompany(getCompanyId());
        }
        if (notice.getId() == null) {
            notice.setCreateTime(DateTime.now().toDate());
            if (notice.getType() == 2 && notice.getSendTime() == null) {
                notice.setSendTime(DateTime.now().plusSeconds(-5).toDate());
            }
            isOk = notice.save();
        } else {
            isOk = notice.update();
        }

        if (isOk) {
            renderAjaxSuccess("保存成功");
            if (notice.getType() == 2) {
                if (notice.getSendTime().before(DateTime.now().toDate())) {
                    SendNoticeService.getInstance().send(notice);
                }
            }
        } else {
            renderAjaxError("保存失败");
        }
    }

    public void del() {
        int noticeId = getParaToInt(0, 0);
        if (noticeId == 0) {
            renderAjaxError("删除信息不存在");
        } else {
            if (Notice.dao.deleteById(noticeId)) {
                Db.update(SqlManager.sql("memberMessage.delByNoticeId"), noticeId);
                renderAjaxSuccess("删除成功");
            } else {
                renderAjaxError("删除失败");
            }

        }
    }

    /**
     * 通知过去指定用户的里表
     */
    public void memberList() {
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            params.add(getParaToInt(1, 0));
        } else {
            params.add(getCompanyId());
        }
        String condition = getPara("condition");
        int type = getParaToInt(0, 1);
        String whereSql;
        if (type == 1) {
            whereSql = SqlManager.sql("driverInfo.noticeDriverListWhere");
        } else {
            whereSql = SqlManager.sql("memberInfo.noticeMemberListWhere");
        }
        if (!Strings.isNullOrEmpty(condition)) {
            whereSql = StringsKit.replaceSql(whereSql, " AND (nick_name LIKE ? OR real_name LIKE ? OR phone LIKE ?) ");
            condition = "%" + condition + "%";
            params.add(condition);
            params.add(condition);
            params.add(condition);
        }
        if (type == 1) {
            renderJson(Db.find(SqlManager.sql("driverInfo.noticeDriverListCloumn") + whereSql, params.toArray()));
        } else {
            renderJson(Db.find(SqlManager.sql("memberInfo.noticeMemberListCloumn") + whereSql, params.toArray()));
        }
    }

    public void lookup() {
        int noticId = getParaToInt(0, 0);
        Notice notice = Notice.dao.findById(noticId);
        List<Record> memberLists = Lists.newArrayList();
        if (notice != null && ((notice.getReciveType() != null && notice.getReciveType() == -4) || (notice.getReciveType() != null && notice.getReciveType() == -5))) {
            if (notice.getReciveType() == -4) {//指定乘客
                String[] memberIds = notice.getReciver().split(";");
                String inWhere = Strings.repeat("?,", memberIds.length);
                if (memberIds.length > 0) {
                    inWhere = inWhere.substring(0, inWhere.length() - 1);
                }
                List<Object> params = Lists.newArrayList();
                for (String memberId : memberIds) {
                    params.add(memberId);
                }
                String sqlStr = StringsKit.replaceSql(SqlManager.sql("memberLogin.findByLoginIdIn"), " AND dmi.id IN (" + inWhere + ")");
                memberLists = Db.find(sqlStr, params.toArray());
            } else if (notice.getReciveType() == -5) {//指定司机
                String[] memberIds = notice.getReciver().split(";");
                String inWhere = Strings.repeat("?,", memberIds.length);
                if (memberIds.length > 0) {
                    inWhere = inWhere.substring(0, inWhere.length() - 1);
                }
                List<Object> params = Lists.newArrayList();
                for (String memberId : memberIds) {
                    params.add(memberId);
                }
                String sqlStr = StringsKit.replaceSql(SqlManager.sql("memberLogin.findByLoginIdIn"), " AND ddi.id IN (" + inWhere + ")");
                memberLists = Db.find(sqlStr, params.toArray());
            }

            String members = "";
            for (Record record : memberLists) {
                members += record.getStr("name") + "-" + record.getStr("phone") + ";";
            }
            notice.put("members", members);
        }
        setAttr("notice", notice);
    }
}
