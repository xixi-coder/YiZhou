package wechatpay;

import utils.HttpUtils;
import base.Constant;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import models.member.CapitalLog;
import models.member.MemberInfo;
import models.member.MemberLogin;
import models.order.Order;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static base.Constant.properties;

/**
 * Created by admin on 2017/2/12.
 */
public class WechatPayKit {
    
    public static Logger logger = LoggerFactory.getLogger(WechatPayKit.class);
    private static String charset = "UTF-8";
    
    /***
     *客户端商户相关资料
     */
    private static String key = properties.getProperty("wechatpay.key");
    private static String appid = properties.getProperty("wechatpay.appid");
    private static String partner = properties.getProperty("wechatpay.mch_id");

    /***
     *司机端商户相关资料
     */
    private static String dappid = properties.getProperty("wechatpay.driver.appid");
    private static String dpartner = properties.getProperty("wechatpay.driver.mch_id");
    private static String dkey = properties.getProperty("wechatpay.driver.key");
    
    /**
     * 微信证书地址
     */
    private static String weiXinCertPath = properties.getProperty("wechatpay.weixin.certpath");
    private static String notify_url = properties.getProperty("wechatpay.notify_url");

    /**
     * 微信公众号
     */
    private static String publicAccountsAppId = properties.getProperty("wechatpay.publicaccounts.appid");
    private static String publicAccountsPartner = properties.getProperty("wechatpay.publicaccounts.mch_id");
    private static String publicAccountsKey = properties.getProperty("wechatpay.publicaccounts.key");
    private static String publicAccountsSecret = properties.getProperty("wechatpay.publicaccounts.secret");
    
    /**
     * 微信小程序
     */
    private static String liteAppId = properties.getProperty("wechatpay.liteapp.appid");
    private static String liteAppMchId = properties.getProperty("wechatpay.liteapp.mch_id");
    private static String liteAppKey = properties.getProperty("wechatpay.liteapp.key");
    private static String liteAppSecret = properties.getProperty("wechatpay.liteapp.secret");
    
    
    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 1.司机端 2.客户端 3.公众号
     */
    public static String createSign(Map<String, String> packageParams, int apptyep) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
            && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if (apptyep == 1) {
            sb.append("key=" + dkey);
        } else if (apptyep == 2) {
            sb.append("key=" + key);
        } else if (apptyep == 3) {
            sb.append("key=" + publicAccountsKey);
        }
        logger.info("apptyep是" + apptyep + ",签名前的内容:{}", sb);
        String sign = MD5Util.MD5Encode(sb.toString(), charset)
        .toUpperCase();
        logger.info("PACKAGE签名后的内容:{}", sign);
        return sign;
    }
    
    /**
     * 根据Code获取OpenId
     */
    public static String getOpenIdByCode(String code, String appId, String secret) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String param = "appid=" + appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        HttpUtils http = new HttpUtils();
        String returnStr = http.requestPost(url, param);
        JSONObject jsStr = JSONObject.parseObject(returnStr);
        return (String) jsStr.get("openid");
    }
    
    /**
     * APP在线支付
     */
    public static Map<String, String> getWechatPayForApp(Order order, int apptype) {
        BigDecimal realAmount = order.getRealPay();
        int total_fee = realAmount.multiply(BigDecimal.valueOf(100)).intValue();
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        MemberLogin m = MemberLogin.dao.findById(memberInfo.getLoginId());
        String spbill_create_ip = m.getIp();
        String body = Constant.COMPANY + "-在线支付" + order.getNo() + "的订单费用";
        String out_trade_no = order.getNo();
        String onecStr = RandomStringUtils.randomAlphanumeric(30);
        if (apptype == 1) { // 1是司机端  2是客户端
            return getWCDriverpay(total_fee, spbill_create_ip, body, out_trade_no, "APP", onecStr);
        }
        return getWCpay(total_fee, spbill_create_ip, body, out_trade_no, "APP", onecStr);
    }
    
    /**
     * 微信公众号支付
     */
    public static Map<String, String> getWechatPayForPublicAccounts(Order order, String code) {
        String openId = getOpenIdByCode(code, publicAccountsAppId, publicAccountsSecret);
        BigDecimal realAmount = order.getRealPay();
        int total_fee = realAmount.multiply(BigDecimal.valueOf(100)).intValue();
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        MemberLogin m = MemberLogin.dao.findById(memberInfo.getLoginId());
        String spbill_create_ip = m.getIp();
        String body = Constant.COMPANY + "-公众号" + order.getNo() + "的订单费用";
        String out_trade_no = order.getNo();
        Map<String, String> map = new HashMap<>();
        map.put("appId", publicAccountsAppId);
        map.put("appMchId", publicAccountsPartner);
        map.put("appKey", publicAccountsKey);
        map.put("appCertPath", weiXinCertPath);
        map.put("appNotifyUrl", notify_url);
        return getWCpayByJsApi(map, total_fee, spbill_create_ip, body, out_trade_no, "JSAPI", openId);
    }
    
    /**
     * 微信小程序支付
     */
    public static Map<String, String> getWechatPayForLiteApp(Order order, String code) {
        String openId = getOpenIdByCode(code, liteAppId, liteAppSecret);
        BigDecimal realAmount = order.getRealPay();
        int total_fee = realAmount.multiply(BigDecimal.valueOf(100)).intValue();
        MemberInfo memberInfo = MemberInfo.dao.findById(order.getMember());
        MemberLogin m = MemberLogin.dao.findById(memberInfo.getLoginId());
        String spbill_create_ip = m.getIp();
        String body = Constant.COMPANY + "-小程序" + order.getNo() + "的订单费用";
        String out_trade_no = order.getNo();
        Map<String, String> map = new HashMap<>();
        map.put("appId", liteAppId);
        map.put("appMchId", liteAppMchId);
        map.put("appCertPath", weiXinCertPath);
        map.put("appKey", liteAppKey);
        map.put("appNotifyUrl", notify_url);
        return getWCpayByJsApi(map, total_fee, spbill_create_ip, body, out_trade_no, "JSAPI", openId);
    }
    
    /**
     * 在线充值
     */
    public static Map<String, String> getWechatRechargeForApp(CapitalLog capitalLog, int apptype) {
        BigDecimal realAmount = capitalLog.getAmount();
        int total_fee = realAmount.multiply(BigDecimal.valueOf(100)).intValue();
        MemberLogin m = MemberLogin.dao.findById(capitalLog.getLoginId());
        String spbill_create_ip = m.getIp();
        String body = Constant.COMPANY + "-在线充值" + capitalLog.getNo() + "的订单费用";
        String out_trade_no = capitalLog.getNo();
        String onecStr = RandomStringUtils.randomAlphanumeric(30);
        if (apptype == 1) {
            String dbody = Constant.COMPANY + "-在线充值" + capitalLog.getNo() + "的订单费用";
            return getWCDriverpay(total_fee, spbill_create_ip, dbody, out_trade_no, "APP", onecStr);
        }
        return getWCpay(total_fee, spbill_create_ip, body, out_trade_no, "APP", onecStr);
    }
    
    /**
     * H5充值
     */
    public static Map<String, String> getWechatRechargeForWeb(CapitalLog capitalLog) {
        BigDecimal realAmount = capitalLog.getAmount();
        int total_fee = realAmount.multiply(BigDecimal.valueOf(100)).intValue();
        MemberLogin m = MemberLogin.dao.findById(capitalLog.getLoginId());
        String spbill_create_ip = m.getIp();
        String body = Constant.COMPANY + "-在线充值" + capitalLog.getNo() + "的订单费用";
        String out_trade_no = capitalLog.getNo();
        String onecStr = RandomStringUtils.randomAlphanumeric(30);
        return getWCpayByH5(total_fee, spbill_create_ip, body, out_trade_no, onecStr);
    }
    
    /**
     * 乘客APP
     */
    public static Map<String, String> getWCpay(int totalFee, String spbillCreateIp, String body, String outTradeNo, String tradeType, String onecStr) {
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", partner);
        packageParams.put("nonce_str", onecStr);
        packageParams.put("body", body);
        outTradeNo = outTradeNo + "_" + RandomStringUtils.randomAlphabetic(3);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("total_fee", totalFee + "");
        packageParams.put("notify_url", notify_url);
        packageParams.put("spbill_create_ip", spbillCreateIp);
        packageParams.put("trade_type", tradeType);
        String sign = WechatPayKit.createSign(packageParams, 2);
        String xml = "<xml>" +
        "<appid>" + appid + "</appid>" +
        "<mch_id>" + partner + "</mch_id>" +
        "<nonce_str>" + onecStr + "</nonce_str>" +
        "<sign>" + sign + "</sign>" +
        "<body>" + body + "</body>" +
        "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
        "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip>" +
        "<total_fee>" + totalFee + "</total_fee>" +
        "<notify_url>" + notify_url + "</notify_url>" +
        "<trade_type>" + tradeType + "</trade_type>" +
        "</xml>";
        logger.info("xml:{}", xml);
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            Map map = GetWxOrderno.getInstance().getPayNo(createOrderURL, xml);
            String prepay_id = map.get("prepay_id").toString();
            logger.info("APP获取到的预支付ID：{}", prepay_id);
            //获取prepay_id后，拼接最后请求支付所需要的package
            SortedMap<String, String> finalpackage = new TreeMap<String, String>();
            String timestamp = DateTime.now().getMillis() / 1000 + "";
            finalpackage.put("appid", appid);
            finalpackage.put("noncestr", onecStr);
            finalpackage.put("package", "Sign=WXPay");
            finalpackage.put("partnerid", partner);
            finalpackage.put("prepayid", prepay_id);
            finalpackage.put("timestamp", timestamp);
            //要签名
            String finalsign = WechatPayKit.createSign(finalpackage, 2);
            finalpackage.put("sign", finalsign);
            return finalpackage;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
    }
    
    /**
     * 司机APP
     */
    public static Map<String, String> getWCDriverpay(int totalFee, String spbillCreateIp, String body, String outTradeNo, String tradeType, String onecStr) {
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", dappid);
        packageParams.put("mch_id", dpartner);
        packageParams.put("nonce_str", onecStr);
        packageParams.put("body", body);
        outTradeNo = outTradeNo + "_" + RandomStringUtils.randomAlphabetic(3);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("total_fee", totalFee + "");
        packageParams.put("notify_url", notify_url);
        packageParams.put("spbill_create_ip", spbillCreateIp);
        packageParams.put("trade_type", tradeType);
        String sign = WechatPayKit.createSign(packageParams, 1);
        String xml = "<xml>" +
        "<appid>" + dappid + "</appid>" +
        "<mch_id>" + dpartner + "</mch_id>" +
        "<nonce_str>" + onecStr + "</nonce_str>" +
        "<sign>" + sign + "</sign>" +
        "<body>" + body + "</body>" +
        "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
        "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip>" +
        "<total_fee>" + totalFee + "</total_fee>" +
        "<notify_url>" + notify_url + "</notify_url>" +
        "<trade_type>" + tradeType + "</trade_type>" +
        "</xml>";
        logger.info("xml:{}", xml);
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            Map map = GetWxOrderno.getInstance().getPayNo(createOrderURL, xml);
            String prepay_id = map.get("prepay_id").toString();
            logger.info("APP获取到的预支付ID：{}", prepay_id);
            //获取prepay_id后，拼接最后请求支付所需要的package
            SortedMap<String, String> finalpackage = new TreeMap<String, String>();
            String timestamp = DateTime.now().getMillis() / 1000 + "";
            finalpackage.put("appid", dappid);
            finalpackage.put("noncestr", onecStr);
            finalpackage.put("package", "Sign=WXPay");
            finalpackage.put("partnerid", dpartner);
            finalpackage.put("prepayid", prepay_id);
            finalpackage.put("timestamp", timestamp);
            //要签名
            String finalsign = WechatPayKit.createSign(finalpackage, 1);
            finalpackage.put("sign", finalsign);
            return finalpackage;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
    }
    
    /**
     * 微信JSAPI支付,小程序,公众号支付
     */
    public static Map<String, String> getWCpayByJsApi(Map<String, String> map, int totalFee, String spbillCreateIp, String body, String outTradeNo, String tradeType, String openid) {
        WxPayUtil config = null;
        try {
            config = new WxPayUtil(map.get("appId"), map.get("appMchId"), map.get("appKey"), map.get("appCertPath"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        outTradeNo = outTradeNo + "_" + RandomStringUtils.randomAlphabetic(3);
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", totalFee + "");
        data.put("spbill_create_ip", spbillCreateIp);
        data.put("notify_url", map.get("appNotifyUrl"));
        data.put("trade_type", tradeType);
        data.put("openid", openid);
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            String timestamp = DateTime.now().getMillis() / 1000 + "";
            resp.put("timestamp", timestamp);
            resp.put("key", map.get("appKey"));
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * H5
     */
    public static Map<String, String> getWCpayByH5(int totalFee, String spbillCreateIp, String body, String outTradeNo, String onecStr) {
        String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"app_name\": \"VIP充值\",\"package_name\": \"com.tencent.tmgp.sgame\"}}";
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", "wx826b8548114650e1");
        packageParams.put("mch_id", "1482823302");
        packageParams.put("nonce_str", onecStr);
        packageParams.put("body", body);
        outTradeNo = outTradeNo + "_" + RandomStringUtils.randomAlphabetic(3);
        packageParams.put("out_trade_no", outTradeNo);
        packageParams.put("total_fee", totalFee + "");
        packageParams.put("notify_url", notify_url);
        packageParams.put("spbill_create_ip", spbillCreateIp);
        packageParams.put("trade_type", "MWEB");
        packageParams.put("scene_info", scene_info);
        String sign = WechatPayKit.createSign(packageParams, 2);
        String xml = "<xml>" +
        "<appid>" + appid + "</appid>" +
        "<mch_id>" + partner + "</mch_id>" +
        "<nonce_str>" + onecStr + "</nonce_str>" +
        "<sign>" + sign + "</sign>" +
        "<body>" + body + "</body>" +
        "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
        "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip>" +
        "<total_fee>" + totalFee + "</total_fee>" +
        "<notify_url>" + notify_url + "</notify_url>" +
        "<trade_type>" + "MWEB" + "</trade_type>" +
        "<scene_info>" + scene_info + "</scene_info>" +
        "</xml>";
        logger.info("xml:{}", xml);
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            Map map = GetWxOrderno.getInstance().getPayNo(createOrderURL, xml);
            logger.info(map.get("return_msg").toString());
            String prepay_id = map.get("prepay_id").toString();
            String mweb_url = map.get("mweb_url").toString();
            logger.info("APP获取到的预支付ID：{}", prepay_id);
            logger.info("--------------------mweb_url：{}", mweb_url);
            //获取prepay_id后，拼接最后请求支付所需要的package
            SortedMap<String, String> finalpackage = new TreeMap<String, String>();
            finalpackage.put("mweburl", mweb_url);
            finalpackage.put("prepayid", prepay_id);
            return finalpackage;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
    }
}
