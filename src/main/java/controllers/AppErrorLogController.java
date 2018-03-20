package controllers;

import annotation.Controller;
import base.controller.BaseApiController;
import com.jfinal.core.ActionKey;
import com.jfinal.upload.UploadFile;
import models.member.MemberLogin;
import models.sys.Applog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AlyFileUpload;
import utils.DateUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by BOGONj on 2016/8/23.
 */
@Controller("/appErrorLog")
public class AppErrorLogController extends BaseApiController {
    Logger logger = LoggerFactory.getLogger(AppErrorLogController.class);
    
    /**
     * app 错误日志
     */
    @ActionKey("/appErrorLog")
    public void appErrorLog() {
        Applog applog = new Applog();
        MemberLogin memberLogin = null;
        if (getLoginMember() != null) {
            memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        }
        applog.setAppType(getAppType());
        applog.setDeviceType(getDeviceType());
        UploadFile uf = getFile();
        File file = uf.getFile();
        String fileType = file.getName();
        Integer versionCode = 0;
        if (fileType.indexOf("C") != -1) {
            String[] strings = fileType.split("C");
            fileType = strings[1];
            versionCode = Integer.parseInt(strings[0]);
            applog.setVersionCode(versionCode);
        }
        String dateName = DateUtil.stampToDate(fileType.substring(0, fileType.lastIndexOf(".")));
        try {
            String fileName = (memberLogin != null ? memberLogin.getUserName() : "N") + "-" + dateName.replace(":", "--") + ".txt";
            String resultPath = AlyFileUpload.getIntance().uploadByInputStream(file, fileName);
            applog.setLogPath(resultPath);
            file.delete();
            applog.setCreateTime(new Date());
            if (applog.save()) {
                renderAjaxSuccess(resultPath);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            renderAjaxError("上传失败");
        }
    }
}
