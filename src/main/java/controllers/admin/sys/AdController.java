package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import models.company.Ad;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/5.
 */
@Controller("/admin/sys/ad")
public class AdController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("ad.column"), SqlManager.sql("ad.where")));
        } else {
            whereSql = " AND company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("ad.column"), SqlManager.sql("ad.where"), whereSql, params));
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        Ad ad = Ad.dao.findById(id);
        if (ad.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            Ad ad = Ad.dao.findById(id);
            setAttr("ad", ad);
            String[] paths = ad.getPic() != null ? ad.getPic().split(";") : null;
            setAttr("paths", paths);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            Ad ad = new Ad();
            setAttr("ad", ad);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void save() {
        Ad ad = getModel(Ad.class, "ad");
        ad.setLastUpdate(DateTime.now().toDate());
        if (!isSuperAdmin()) {
            ad.setCompany(getCompanyId());
        }
        if (ad.getId() != null) {
            if (ad.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxError("修改失败！");
            }
        } else {
            if (ad.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxError("添加失败！");
            }
        }
    }
}
