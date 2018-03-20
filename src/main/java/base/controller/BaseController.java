package base.controller;

import base.Constant;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;

import java.util.Map;

/**
 * Created by BOGONm on 16/8/9.
 */
public class BaseController extends Controller {
    /**
     * 运营证请求成功
     */
    public void renderPortAjaxSuccess(String str, Object object) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("Source", Constant.Source);
        result.put("CompanyId", Constant.COMPANYID);
        result.put("IPCType", str);
        result.put(str, object);
        super.renderJson(result);
    }
    
    /**
     * 请求成功
     *
     * @param data
     */
    public void renderAjaxSuccess(Object data) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "OK");
        if (data == null) {
            result.put("data", null);
            super.renderJson(result);
            return;
        }
        if (String.class.isAssignableFrom(data.getClass())) {
            result.put("msg", data.toString());
            result.put("data", null);
        } else {
            result.put("msg", "");
            result.put("data", data);
        }
        super.renderJson(result);
    }
    
    /**
     * 请求成功
     *
     * @param data
     */
    public void renderAjaxSuccess(String status, Object data, String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", status);
        result.put("msg", message);
        result.put("data", data);
        super.renderJson(result);
    }
    
    /**
     * 请求成功
     *
     * @param data
     */
    public void renderAjaxSuccess(Object data, String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "OK");
        result.put("msg", message);
        result.put("data", data);
        super.renderJson(result);
    }
    
    /**
     * 请求失败
     *
     * @param message
     */
    public void renderAjaxFailure(String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "FAILURE");
        result.put("data", null);
        result.put("msg", message);
        super.renderJson(result);
    }
    
    /**
     * 请求失败
     *
     * @param message
     */
    public void renderAjaxFailure(String message, Object data) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "FAILURE");
        result.put("data", data);
        result.put("msg", message);
        super.renderJson(result);
    }
    
    /**
     * 请求错误
     *
     * @param message
     */
    public void renderAjaxError(String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "ERROR");
        result.put("data", null);
        result.put("msg", message);
        super.renderJson(result);
    }
    
    public void renderAjaxNoData() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "NODATA");
        result.put("data", null);
        result.put("msg", "NoData");
        super.renderJson(result);
    }
    
    /**
     * 标准返回
     *
     * @param code
     * @param message
     * @param isSuccess
     * @param dateMap   "code":200,"message":"success","isSuccess":true,"data"：{}
     */
    public void fail(int code, String message, boolean isSuccess, Map<String, Object> dateMap) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", "OK");
        result.put("code", code);
        result.put("message", message);
        result.put("isSuccess", isSuccess);
        result.put("data", dateMap);
        super.renderJson(result);
    }
    
    /**
     * 成功
     *
     * @param dateMap
     */
    public void ok(Map<String, Object> dateMap) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("code", 200);
        result.put("status", "OK");
        result.put("message", "success");
        result.put("isSuccess", true);
        result.put("data", dateMap);
        super.renderJson(result);
    }
    
    /**
     * 对接广告项目
     */
    public void resultSuccess(Object object) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", true);
        result.put("data", object);
        super.renderJson(result);
    }
    
    public void resultSuccess() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", true);
        super.renderJson(result);
    }
    
    public void resultError(String message) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", false);
        result.put("message", message);
        super.renderJson(result);
    }
    
    public void resultError() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("status", false);
        super.renderJson(result);
    }
}
