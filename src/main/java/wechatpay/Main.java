package wechatpay;


import org.joda.time.DateTime;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        try {
            //金额转化为分为单位
            String money = "0.1";
            float sessionmoney = Float.parseFloat(money);
            String finalmoney = String.format("%.2f", sessionmoney);
            finalmoney = finalmoney.replace(".", "");

            //商户相关资料
            String appid = "wxd79e00cf03529e97";
            String partner = "1430650802";
            //获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
            String currTime = TenpayUtil.getCurrTime();
            //8位日期
            String strTime = currTime.substring(8, currTime.length());
            //四位随机数
            String strRandom = TenpayUtil.buildRandom(4) + "";
            //10位序列号,可以自行调整。
            String strReq = strTime + strRandom;
            //商户号
            String mch_id = partner;
            //设备号   非必输
            //String device_info = "WEB";
            //随机数
            String nonce_str = "1423238985";
            //商品描述
            //String body = describe;

            //商品描述根据情况修改
            String body = "彝州出行-打车服务费";
            //商户订单号
            String out_trade_no = "2017020202222";
            String spbill_create_ip = "121.40.142.141";
            int intMoney = Integer.parseInt(finalmoney);
            //这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
            String notify_url = "http://192.168.1.200:8080/testPay/aa.htm";
            String trade_type = "APP";
            SortedMap<String, String> packageParams = new TreeMap<String, String>();
            packageParams.put("appid", appid);
            packageParams.put("mch_id", mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", out_trade_no);
            packageParams.put("total_fee", intMoney + "");
            packageParams.put("notify_url", notify_url);
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("trade_type", trade_type);
            String sign = createSign(packageParams);
            String xml = "<xml>" +
                    "<appid>" + appid + "</appid>" +
                    "<mch_id>" + mch_id + "</mch_id>" +
                    "<nonce_str>" + nonce_str + "</nonce_str>" +
                    "<sign>" + sign + "</sign>" +
                    "<body>" + body + "</body>" +
                    "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                    "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                    "<total_fee>" + intMoney + "</total_fee>" +
                    "<notify_url>" + notify_url + "</notify_url>" +
                    "<trade_type>" + trade_type + "</trade_type>" +
                    "</xml>";
            System.out.println(xml);
            String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            Map<String, Object> dataMap2 = new HashMap<String, Object>();
            try {
                Map map = GetWxOrderno.getInstance().getPayNo(createOrderURL, xml);
                String prepay_id = map.get("prepay_id").toString();
                System.out.println("APP获取到的预支付ID：{}" + prepay_id);
                ;
                //获取prepay_id后，拼接最后请求支付所需要的package
                SortedMap<String, String> finalpackage = new TreeMap<String, String>();
                String timestamp = DateTime.now().getMillis() / 1000 + "";
                finalpackage.put("appid", appid);
                finalpackage.put("noncestr", nonce_str);
                finalpackage.put("package", "Sign=WXPay");
                finalpackage.put("partnerid", partner);
                finalpackage.put("prepayid", prepay_id);
                finalpackage.put("timestamp", timestamp);
                //要签名
                String finalsign = createSign(finalpackage);
                finalpackage.put("sign", finalsign);
                for (String s : finalpackage.keySet()) {
                    System.out.println(s + "的值:" + finalpackage.get(s));
                }
                System.out.println(finalpackage);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } catch (Exception e) {
//            Util.log(e.getMessage());
        }

    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(Map<String, String> packageParams) {
        /**
         * 商户参数
         */
        String key = "wug18erh59dcvwrgghw4fwe84rw1g8we";
        String charset = "UTF-8";

        //商户相关资料
        String appid = "wxd79e00cf03529e97";
        String partner = "1430650802";

        String notify_url = "http://192.168.1.200:8082/testPay/aa.htm";
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
        sb.append("key=" + key);
        System.out.println("签名前的内容:{}" + sb);
        String sign = MD5Util.MD5Encode(sb.toString(), charset)
                .toUpperCase();
        System.out.println("PACKAGE签名后的内容:{}" + sign);
        return sign;
    }

}
