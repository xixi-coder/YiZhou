package interceptors;

import base.Constant;
import base.shiro.HasAnyRolesFreeMarkerMethod;
import base.shiro.HasPermissionFreeMarkerMethod;
import base.shiro.HasRoleFreeMarkerMethod;
import com.google.common.collect.Lists;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import jobs.syslog.SysLog;
import kits.IpKit;
import models.company.CompanyAccount;
import models.sys.Resources;
import models.sys.User;
import net.dreamlu.event.EventKit;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by BOGONm on 16/8/9.
 */
public class ShiroInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        Controller c = invocation.getController();
        List<String> whiteList = Lists.newArrayList();
        whiteList.add("/admin/login");
        whiteList.add("/admin/logout");
        whiteList.add("/admin/save");
        whiteList.add("/admin/captcha");
        c.setAttr("websocket", Constant.properties.getProperty("app.wshost"));
        if (invocation.getActionKey().contains("/admin") && !whiteList.contains(invocation.getActionKey())) {
            models.sys.SysLog sysLog = new models.sys.SysLog();
            sysLog.setUrl(invocation.getActionKey());
            sysLog.put("tmpParams", c.getParaMap());
            if (SecurityUtils.getSubject().getPrincipal() != null) {
                sysLog.setOperater(User.dao.findByUserName((String) SecurityUtils.getSubject().getPrincipal()).getId());
            }
            sysLog.setCreateTime(DateTime.now().toDate());
            sysLog.setIpAddress(IpKit.getRealIp(c.getRequest()));
            if(sysLog!=null){
                EventKit.post(new SysLog(sysLog));
            }
            c.setAttr("hasRole", HasRoleFreeMarkerMethod.getInstance());
            c.setAttr("hasAnyRoles", HasAnyRolesFreeMarkerMethod.getInstance());
            c.setAttr("hasPermission", HasPermissionFreeMarkerMethod.getInstance());
            String userName = (String) SecurityUtils.getSubject().getPrincipal();
            User loginUser = User.dao.findByUserName(userName);
            List<Resources> resources;
            if (loginUser != null) {
                CompanyAccount companyAccount = CompanyAccount.dao.findById(loginUser.getCompany());//公司的短信剩余数量
                c.setAttr("_smsCount", companyAccount.getSmsCount());
                c.setAttr("_insuranceAmount", companyAccount.getInsuranceAmount());
                loginUser.remove("password").remove("salt");
                if (CacheKit.get(Constant.MENU_ECACHE, loginUser.getUsername() + "_resoucres") == null) {
                    resources = Resources.dao.findByUser(loginUser, 0);
                    for (Resources resource : resources) {
                        List<Resources> chirld = Resources.dao.findByUser(loginUser, resource.getId());
                        resource.put("_CHIRLD_", chirld);
                    }
                    CacheKit.put(Constant.MENU_ECACHE, loginUser.getUsername() + "_resoucres", resources);
                } else {
                    resources = CacheKit.get(Constant.MENU_ECACHE, loginUser.getUsername() + "_resoucres");
                }
                c.setAttr("_RESOURCES_", resources);
                c.setAttr("_USER_", loginUser);
            } else {
                c.redirect("/");
                return;
            }

        }
        // 执行正常逻辑
        invocation.invoke();
    }
}
