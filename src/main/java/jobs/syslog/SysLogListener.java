package jobs.syslog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import kits.ReadFileKit;
import models.sys.Resources;
import models.sys.User;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

import java.util.Map;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class SysLogListener implements ApplicationListener<SysLog> {
    @Override
    public void onApplicationEvent(SysLog sysLog) {
        String remark;
        String action = "";
        String model = "";
        String opeaterName = "";
        String content = "";
        models.sys.SysLog sysLog1 = (models.sys.SysLog) sysLog.getSource();
        Map<String, String[]> params = sysLog1.get("tmpParams");
        Resources r = Resources.dao.findByUrl(sysLog1.getUrl());
        if (r != null) {
            model = r.getName();
        }
        String filePath = SysLogListener.class.getClassLoader().getResource("sys-log.json").getPath();
        JSONArray jsonArray = JSON.parseArray(ReadFileKit.ReadFile(filePath));
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String tmpUrl = jsonObject.getString("url");
            String tmpAction = jsonObject.getString("action");
            if (sysLog1.getUrl().indexOf(tmpUrl) > 0) {
                action = tmpAction;
                break;
            }
        }
        Map<String, String[]> tmpParams = Maps.newHashMap();
        for (String s : params.keySet()) {
            if (!s.contains("columns[") && !s.contains("search[") && !s.equals("_")) {
                tmpParams.put(s, params.get(s));
            }
        }
        //只保存新增修改信息
        if (!Strings.isNullOrEmpty(action)) {
            content = JSONObject.toJSONString(tmpParams);
            User user = User.dao.findById(sysLog1.getOperater());
            if (user == null) {
                return;
            }
            opeaterName = user.getName();
            remark = opeaterName + "在" + model + "中" + action + "，内容:[" + content + "]。";
            sysLog1.setRemark(remark);
            sysLog1.setParams(content);
            sysLog1.save();
        }
    }
}
