package controllers.www;

import annotation.Controller;
import base.Constant;
import base.controller.BaseController;
import com.jfinal.core.ActionKey;
import models.sys.Provision;
import models.sys.Version;


/**
 * Created by admin on 2016/11/18.
 */
@Controller("/app")
public class AppdownloadController extends BaseController {
    @ActionKey("/app/download")
    public void download() {
        int type = getParaToInt(0, 1);
        int osType = getParaToInt(1, 1);
        String downURL = "";
        if (type == 1) {//司机端
            Version driverClient = Version.dao.findMaxVersion(1, osType);
            downURL = driverClient.getFilePath();
            downURL = downURL.replace("\\", "/");
            if (osType == Constant.iOS) {
                downURL = driverClient.getFilePath();
            }
        } else {//客户端
            Version memberClient = Version.dao.findMaxVersion(2, osType);
            downURL = memberClient.getFilePath();
            downURL = downURL.replace("\\", "/");
            if (osType == Constant.iOS) {
                downURL = memberClient.getFilePath();
            }
        }
        redirect(downURL);
    }

    @ActionKey("/app/provision")
    public void provision() {
        int appType = getParaToInt("appType", 1);
        int provisionType = getParaToInt("provisionType", 1);
        Provision provision = Provision.dao.findByAppTypeAndPType(appType, provisionType);
        setAttr("provision", provision);
    }
}
