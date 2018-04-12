package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.zxing.WriterException;
import models.sys.Version;
import org.joda.time.DateTime;
import services.ZXinCodeService;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/4.
 */
@Controller("/admin/sys/version")
public class VersionController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable("select * ", " from dele_version where 1=1 -- where \n"));
    }

    public void del() {
        int id = getParaToInt("id", 0);
        Version version = Version.dao.findById(id);
        if (version.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void item() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            Version version = Version.dao.findById(id);
            setAttr("version", version);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            Version version = new Version();
            setAttr("version", version);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void save() throws IOException, WriterException {
        Version version = getModel(Version.class, "version");
        String downURL = version.getFilePath();
        String qcode = "";
        if (version.getType() == 1) {
            downURL = "app/download/1";
        } else {
            downURL = "app/download/2";
        }
        downURL = downURL.replace("\\", "/");
        if (version.getId() != null) {
            version.setUpdateTime(DateTime.now().toDate());

            if (version.update()) {
                renderAjaxSuccess("修改成功！");
            } else {
                renderAjaxError("修改失败！");
            }
        } else {
            version.setCreateTime(DateTime.now().toDate());
          /*  if (version.getOsType() == Constant.ANDROID) {
                String qcode = ZXinCodeService.getInstance().createByURL(downURL);
            }*/
            version.setQcode(qcode);
            if (version.save()) {
                renderAjaxSuccess("添加成功！");
            } else {
                renderAjaxError("添加失败！");
            }
        }
    }


}
