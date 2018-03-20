package base.controller;

import base.Constant;
import com.google.common.primitives.Ints;
import com.jfinal.aop.Before;
import interceptors.ApiInterceptor;
import kits.ApiAuthKit;
import kits.PageKit;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.member.MemberLogin;

/**
 * Created by Administrator on 2016/8/20.
 */
@Before(ApiInterceptor.class)
public class BaseApiController extends BaseController {
    
    protected String getDeviceNo() {
        return getRequest().getHeader("deviceno");
    }
    
    protected int getAppType() {
        return Ints.tryParse(getRequest().getHeader(Constant.APPTYPE));
    }
    
    protected MemberLogin getLoginMember() {
        String secret = getRequest().getHeader(Constant.SECRET);
        String token = getRequest().getHeader(Constant.TOKEN);
        String appType = getRequest().getHeader(Constant.APPTYPE);
        return ApiAuthKit.getLoginMember(token, secret, appType);
    }
    
    protected int getPageStart() {
        return PageKit.getStart(getParaToInt("pageIndex", 1), getPageSize());
    }
    
    protected int getPageSize() {
        return getParaToInt("pageSize", 10);
    }
    
    /**
     * 设备类型1:android;2:ios;3:微信;4:小程序
     *
     * @return
     */
    protected int getDeviceType() {
        return Ints.tryParse(getRequest().getHeader("devicetype"));
    }
    
    protected DriverInfo getDriverInfo() {
        return DriverInfo.dao.findByLoginId(getLoginMember().getId());
    }
    
    protected MemberInfo getMemberInfo() {
        return MemberInfo.dao.findByLoginId(getLoginMember().getId());
    }
    
    //计算是不是预约时间    大于15分钟就是预约
    protected boolean t(Long setOutTime) {
        long t = setOutTime - System.currentTimeMillis();
        if (t > (15 * 60 * 1000)) {
            return true;
        }
        return false;
    }
}
