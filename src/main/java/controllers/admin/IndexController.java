package controllers.admin;

import base.Constant;
import base.controller.BaseAdminController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.render.CaptchaRender;
import kits.IpKit;
import models.sys.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by BOGONm on 16/8/8.
 */
@annotation.Controller("/admin")
public class IndexController extends BaseAdminController {
    static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @ActionKey("/admin/index")
    public void index() {
    }

    @ActionKey("/")
    public void login() {
    }

    @Clear
    public void logout() {
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        CacheKit.remove(Constant.MENU_ECACHE, userName + "_resoucres");
        SecurityUtils.getSubject().logout();
        redirect("/");
    }

    @Before(POST.class)
    @ActionKey("/admin/save")
    public void save() {
        String username = getPara("username");
        String passworkd = getPara("password");
//        String captcha = getPara("captcha");
//        if (!CaptchaRender.validate(this, captcha)) {
//            renderAjaxError("验证码不正确!");
//            return;
//        }

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, passworkd);
        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
            User user = User.dao.findByUserName(username);
            user.setLastLoginIp(IpKit.getRealIp(getRequest()));
            user.update();
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            renderAjaxFailure(e.getMessage());
            return;
        }
        renderAjaxSuccess("OK");
    }

    public void captcha() {
        CaptchaRender img = new CaptchaRender();
        render(img);
    }
}
