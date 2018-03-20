package controllers.admin.wechat;

import annotation.Controller;
import base.controller.BaseAdminController;
import models.wechat.WechatMenu;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by admin on 2016/9/30.
 */
@Controller("/admin/wechat/menu")
public class MenuController extends BaseAdminController {

    public void index() {
        List<WechatMenu> parent = WechatMenu.dao.findByParent(0);
        for (WechatMenu wechatMenu : parent) {
            List<WechatMenu> children = WechatMenu.dao.findByParent(wechatMenu.getId());
            wechatMenu.put("children", children);
        }
        setAttr("menu", parent);
    }

    public void save() {
        WechatMenu wechatMenu = getModel(WechatMenu.class, "menu");
        boolean isOk;
        wechatMenu.setLastUpdateTime(DateTime.now().toDate());
        if (wechatMenu.getId() == null) {
            isOk = wechatMenu.save();
        } else {
            isOk = wechatMenu.update();
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxFailure("保存失败！");
        }
    }

    public void item() {
        int id = getParaToInt("id", 0);
        WechatMenu wechatMenu = WechatMenu.dao.findById(id);
        if (wechatMenu == null) {
            renderAjaxNoData();
        } else {
            renderAjaxSuccess(wechatMenu);
        }
    }

    public void menucreate() {
        List<WechatMenu> wechatMenus = WechatMenu.dao.findAll();

    }
}
