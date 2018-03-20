package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.sys.Resources;
import plugin.sqlInXml.SqlManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by BOGONm on 16/8/10.
 */
@Controller("/admin/sys/resource")
public class ResourceController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable(SqlManager.sql("resources.column"), SqlManager.sql("resources.where")));
    }

    public void item() {
        int id = getParaToInt(0, 0);
        List<Resources> maxResource = Resources.dao.findByParent(0, "");
        setAttr("parents", maxResource);
        if (id > 0) {
            Resources resources = Resources.dao.findById(id);
            setAttr("resource", resources);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            Resources resources = new Resources();
            setAttr("resource", resources);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    @Before(POST.class)
    public void save() {
        Resources resources = getModel(Resources.class, "resource");
        resources.setStatus(1);
        if (resources.getId() != null) {
            if (resources.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxFailure("修改失败！");
            }
        } else {
            if (resources.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxFailure("添加失败！");
            }
        }
    }

    /**
     * 验证编码是否重复
     */
    public void validCode() {
        Resources resources = getModel(Resources.class, "resource");
        Map<String, String> result = Maps.newHashMap();
        if (resources.getId() != null) {
            if (Resources.dao.findByCountExcludedSelf(resources.getId(), resources.getCode()) > 0) {
                result.put("error", "编码已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            if (Resources.dao.findByCount(resources.getCode()) > 0) {
                result.put("error", "编码已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);
    }

    /**
     * 为ztree提供数据
     */
    public void parent() {
        int id = getParaToInt("id", -1);
        List<Resources> resources;
        if (id > -1) {
            resources = Resources.dao.findForZtreeByStyle(id);
        } else {
            resources = Resources.dao.findForZtreeByStyle();
        }
        renderAjaxSuccess(resources);
    }

    /**
     * 删除资源
     */
    public void del() {
        final int id = getParaToInt("id", 0);
        boolean isOk;
        if (id > 0) {
            final Resources resources = Resources.dao.findById(id);
            List<Resources> chirld = Resources.dao.findByParent(id, "");
            if (chirld != null && chirld.size() > 0) {
                renderAjaxError("存在子菜单无法删除！");
                return;
            }
//            if (RoleResources.dao.isUseResource(id)) {
//                renderAjaxError("资源已经被使用无法删除！");
//                return;
//            }
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return resources.delete() && Db.update(SqlManager.sql("roleResources.delByResource"), id) >= 0;
                }
            });
            if (isOk) {
                renderAjaxSuccess("删除成功！");
            } else {
                renderAjaxError("删除失败！");
            }
        } else {
            renderAjaxError("资源不存在！");
        }
    }
}
