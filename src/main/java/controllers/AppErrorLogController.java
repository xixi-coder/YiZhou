package controllers;

import annotation.Controller;
import base.Constant;
import base.controller.BaseApiController;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.Prop;
import com.jfinal.upload.UploadFile;
import models.member.MemberLogin;
import models.sys.Applog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.FileService;
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
        Applog applog =new Applog();
        MemberLogin memberLogin=null;
        if(getLoginMember() != null){
            memberLogin = MemberLogin.dao.findById(getLoginMember().getId());
        }
        applog.setAppType( getAppType());
        applog.setDeviceType(getDeviceType());
        UploadFile uf = getFile();
        File file = uf.getFile();
        String uploadDir = Constant.properties.getProperty("apperrorlog.dir", "");
        String uploadHost = Constant.properties.getProperty("apperrorlog.host", "");
        uploadDir = FileService.getInstance().mkdir(uploadDir);
        String fileType = file.getName();
        Integer versionCode = 0;
        if(fileType.indexOf("C")!=-1){
            String[]  strings =  fileType.split("C");
            fileType = strings[1];
            versionCode = Integer.parseInt(strings[0]);
            applog.setVersionCode(versionCode);
        }

        String dateName = DateUtil.stampToDate(fileType.substring(0, fileType.lastIndexOf(".")));
        String filePath= uploadDir.substring(uploadDir.indexOf("appLog"), uploadDir.length());
        File t = new File(uploadDir +(memberLogin!=null ?memberLogin.getUserName():"N")+"-"+ dateName.replace(":","--")+".txt");
        try {
            if (t.createNewFile()) {
                FileService.getInstance().fileChannelCopy(file, t);
                file.delete();
                applog.setLogPath(uploadHost +filePath+(memberLogin!=null ?memberLogin.getUserName() : "N") + "-" + dateName.replace(":", "--") + ".txt");
                applog.setCreateTime(new Date());
                if(applog.save()){
                    renderAjaxSuccess(uploadHost+filePath + (memberLogin != null ? memberLogin.getUserName() : "N") + "-" + dateName.replace(":", "--") + ".txt");
                }else {
                    throw new IOException();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            renderAjaxError("上传失败");
        }
    }
}
