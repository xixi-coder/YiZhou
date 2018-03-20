package controllers.admin.wechat;

import annotation.Controller;
import base.controller.BaseAdminController;
import models.wechat.WechatAccount;
import org.joda.time.DateTime;

/**
 * Created by admin on 2016/9/30.
 */
@Controller("admin/wechat/account")
public class AccountController extends BaseAdminController {
    public void index() {
        WechatAccount wechatAccount = WechatAccount.dao.findFrist();
        setAttr("wechatAccount", wechatAccount);
    }

    public void save() {
        final WechatAccount wechatAccount = getModel(WechatAccount.class, "wechatAccount");
        if (wechatAccount.getId() == null) {
            wechatAccount.setLastUpdateTime(DateTime.now().toDate());
            if (wechatAccount.save()) {
                renderAjaxSuccess("保存成功！");
            } else {
                renderAjaxFailure("保存失败！");
            }
        } else {
            wechatAccount.setLastUpdateTime(DateTime.now().toDate());
            if (wechatAccount.update()) {
                renderAjaxSuccess("保存成功！");
            } else {
                renderAjaxFailure("保存失败！");
            }
        }
    }
}
