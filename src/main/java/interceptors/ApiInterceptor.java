package interceptors;

import base.Constant;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import dto.MessageDto;
import kits.ApiAuthKit;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ApiInterceptor implements Interceptor {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);
    
    @Override
    public void intercept(Invocation invocation) {
        Controller c = invocation.getController();
        List<String> whiteList = Lists.newArrayList();
        whiteList.add("/api/loginBySms");
        whiteList.add("/appErrorLog");
        whiteList.add("/api/getServiceType");
        whiteList.add("/api/driver/register");
        whiteList.add("/api/ad");
        whiteList.add("/api/file");
        whiteList.add("/api/getSms");
        whiteList.add("/api/order/calamount");
        whiteList.add("/api/member/cars");
        whiteList.add("/api/member/cars_V2");
        whiteList.add("/api/login");
        whiteList.add("/api/getVersion");
        whiteList.add("/api/smscode");
        whiteList.add("/api/register");
        whiteList.add("/api/member/reset/password");
        whiteList.add("/api/zxLine/getzxLine");
        whiteList.add("/api/zxLine/getzxLinePrice");
        String appType = c.getRequest().getHeader(Constant.APPTYPE);
        System.out.println(invocation.getActionKey());
        if (!whiteList.contains(invocation.getActionKey())) {
            String secret = c.getRequest().getHeader(Constant.SECRET);
            String token = c.getRequest().getHeader(Constant.TOKEN);
            
            if (Strings.isNullOrEmpty(secret)) {
                logger.error("===============================>>>>>>>>>>>>secret is null!");
            }
            if (Strings.isNullOrEmpty(token)) {
                logger.error("===============================>>>>>>>>>>>>token is null!");
            }
            
            
            if (Strings.isNullOrEmpty(appType)) {
                c.renderJson(new MessageDto("参数不正确", "ERROR"));
                return;
            }
            if (Strings.isNullOrEmpty(secret) || Strings.isNullOrEmpty(token)) {
                c.renderJson(new MessageDto("未登陆401", "NOAUTH"));
                return;
            }
            if (!ApiAuthKit.isLogin(secret, token, appType)) {
                c.renderJson(new MessageDto("未登陆402", "NOAUTH"));
                return;
            }
        }
        if (Strings.isNullOrEmpty(appType)) {
            c.renderJson(new MessageDto("参数不正确-appType不能为空", "ERROR"));
            return;
        }
        invocation.invoke();
    }
}
