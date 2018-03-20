package controllers.www;

import annotation.Controller;
import base.controller.BaseController;
import com.google.common.base.Strings;
import kits.VerificationKit;
import models.driver.DriverInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@Controller("/sjdd/driver")
public class AdProjectController extends BaseController {

    //验证
    public void verify() {
        String phone = getPara("phone");
        if (Strings.isNullOrEmpty(phone)) {
            resultError("没有获取到phone");
            return;
        }
        if (!VerificationKit.isMobile(phone)) {
            resultError("手机号码格式错误");
            return;
        }
        DriverInfo driver = DriverInfo.dao.findByPhone(phone);
        if (driver != null) {
            if (driver.getStatus() == 1) {
                resultSuccess();
            } else {
                resultError();
            }
        } else {
            resultError();
            return;
        }
    }

    //司机信息
    public void driverInformation() {
        String phone = getPara("phone");
        if (Strings.isNullOrEmpty(phone)) {
            resultError("没有获取到phone");
            return;
        }
        if (!VerificationKit.isMobile(phone)) {
            resultError("手机号码格式错误");
            return;
        }
        List<DriverInfo> driverInfos = DriverInfo.dao.adProjectGetPhone(phone);
        if (driverInfos.size() > 0) {
            resultSuccess(driverInfos);
            return;
        } else {
            resultError();
            return;
        }
    }
}
