package controllers;

import annotation.Controller;
import base.Constant;
import base.controller.BaseController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.upload.UploadFile;
import jobs.pay.OrderPay;
import jobs.rechange.Rechange;
import jobs.reward.Reward;
import models.driver.DriverInfo;
import models.member.CapitalLog;
import models.member.MemberLogin;
import models.order.Alipay;
import models.order.Order;
import models.order.WechatPay;
import models.sys.Area;
import models.sys.Attachment;
import models.sys.Notice;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ActivityService;
import services.FileService;
import services.JPushService;
import services.OnlineService;
import wechatpay.GetWxOrderno;
import wechatpay.WechatPayKit;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by BOGONj on 2016/8/23.
 */
@Controller("/file")
public class FileController extends BaseController {
    Logger logger = LoggerFactory.getLogger(FileController.class);


    public void index() {
        Attachment attachment = new Attachment();
        attachment.setUploadTime(DateTime.now().toDate());
        UploadFile uf = getFile();
        File file = uf.getFile();
        String uploadDir = Constant.properties.getProperty("attachment.dir", "");
        uploadDir = FileService.getInstance().mkdir(uploadDir);
        String fileType = file.getName();
        if (fileType.lastIndexOf(".") > -1) {
            fileType = fileType.substring(fileType.lastIndexOf(".") + 1, fileType.length());
        } else {
            fileType = "";
        }
        attachment.setType(fileType);
        String path = "";
        if (fileType.equals("apk")) {
            path = "sjdd_app" + RandomStringUtils.randomNumeric(4) + "." + fileType;
        } else {
            path = UUID.randomUUID().toString() + "." + fileType;
        }

        File t = new File(uploadDir + path);
        attachment.setName(t.getName());
        attachment.setPath((uploadDir.substring(uploadDir.indexOf("attachment"), uploadDir.length()) + t.getName()).replace("\\", "/"));
        attachment.setSize(BigDecimal.valueOf(file.length() / 1000));
        try {
            if (t.createNewFile()) {
                FileService.getInstance().fileChannelCopy(file, t);
                file.delete();
                if (attachment.save()) {
                    renderAjaxSuccess(attachment);
                } else {
                    renderAjaxError("上传失败");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file.delete();
            renderAjaxError("上传失败");
        }
    }


    public void edit() {
        Attachment attachment = new Attachment();
        attachment.setUploadTime(DateTime.now().toDate());
        UploadFile uf = getFile();
        File file = uf.getFile();
        String uploadDir = Constant.properties.getProperty("attachment.dir", "");
        uploadDir = FileService.getInstance().mkdir(uploadDir);
        String fileType = file.getName();
        if (fileType.lastIndexOf(".") > -1) {
            fileType = fileType.substring(fileType.lastIndexOf(".") + 1, fileType.length());
        } else {
            fileType = "";
        }
        attachment.setType(fileType);
        String path = UUID.randomUUID().toString() + "." + fileType;
        File t = new File(uploadDir + path);
        attachment.setName(t.getName());
        attachment.setPath((uploadDir.substring(uploadDir.indexOf("attachment"), uploadDir.length()) + t.getName()).replace("\\", "/"));
        attachment.setSize(BigDecimal.valueOf(file.length() / 1000));
        try {
            if (t.createNewFile()) {
                FileService.getInstance().fileChannelCopy(file, t);
                file.delete();
                if (attachment.save()) {
                    JSONObject obj = new JSONObject();
                    obj.put("error", 0);
                    //ip访问
//                    String savePath = File.separator + Constant.properties.getProperty("app.name", "Jfinal") + File.separator + attachment.getPath();
//                    //域名访问
                    String savePath = File.separator + attachment.getPath();
                    savePath = savePath.replace("\\", "/");
                    obj.put("url", savePath);
                    renderJson(obj.toJSONString());
                } else {
                    JSONObject obj = new JSONObject();
                    obj.put("error", 1);
                    obj.put("message", "上传失败！");
                    renderJson(obj.toJSONString());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            file.delete();
            JSONObject obj = new JSONObject();
            obj.put("error", 1);
            obj.put("message", "上传失败！");
            renderJson(obj.toJSONString());
        }
    }

    public void fileManager() {
        renderJson();
    }

    public void test() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            File file = new File("C:\\Users\\Administrator\\Desktop\\sql.json");
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineTxt);
            }
            read.close();
            System.out.printf(stringBuffer.toString());
            JSONArray jsonArray = JSON.parseArray(stringBuffer.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject a = (JSONObject) jsonArray.get(i);
                Area area = new Area();
                area.setAdcode(a.getString("adcode"));
                area.setLevel(a.getString("level"));
                area.setCitycode(a.getString("citycode"));
                area.setName(a.getString("name"));
                area.setParent("0");
                area.save();
            }
            renderAjaxSuccess("hao");
        } catch (Exception e) {
            e.printStackTrace();
            renderAjaxSuccess("buhao");
        }
    }

    @ActionKey("/common/smsphone")
    public void smscode() {
        String phone = getPara("phone");
        if (Strings.isNullOrEmpty(phone)) {
            renderText("手机号码不能为空!");
            return;
        }
        if (CacheKit.get(Constant.Sms.SMS_CACHE, phone) == null) {
            renderText("验证码不存在!");
            return;
        }
        renderText(CacheKit.get(Constant.Sms.SMS_CACHE, phone).toString());
    }

    public void jpushdriver() {
        renderText(JPushService.getInstance().sendMessageToDriver(1, "测试的内容", "1a0018970aae19c61ee") + "");
    }

    public void jpushmember() {
        renderText(JPushService.getInstance().sendMessageToCustomer(1, "测试的内容", "1a0018970aae19c61ee") + "");
    }

    public void alipaynotic() {
        logger.info("------------------------------支付宝回掉开始-------------------------");
        String notifyTime = getPara("notify_time");
        Alipay alipay = new Alipay();
        String notify_type = getPara("notify_type");
        alipay.setNotifyType(notify_type);
        String notify_id = getPara("notify_id");
        alipay.setNotifyId(notify_id);
        String app_id = getPara("app_id");
        alipay.setAppId(app_id);
        String sign_type = getPara("sign_type");
        alipay.setSignType(sign_type);
        String sign = getPara("sign");
        alipay.setSign(sign);
        String trade_no = getPara("trade_no");
        alipay.setTradeNo(trade_no);
        String out_trade_no = getPara("out_trade_no");
        alipay.setOutTradeNo(out_trade_no);
        String out_biz_no = getPara("out_biz_no");
        alipay.setOutBizNo(out_biz_no);
        String buyer_id = getPara("buyer_id");
        alipay.setBuyerId(buyer_id);
        String buyer_logon_id = getPara("buyer_logon_id");
        alipay.setBuyerLogonId(buyer_logon_id);
        String seller_id = getPara("seller_id");
        alipay.setSellerId(seller_id);
        String seller_email = getPara("seller_email");
        alipay.setSellerEmail(seller_email);
        String trade_status = getPara("trade_status");
        alipay.setTradeStatus(trade_status);
        String total_amount = getPara("total_amount");
        alipay.setTotalAmount(total_amount);
        String receipt_amount = getPara("receipt_amount");
        alipay.setReceiptAmount(receipt_amount);
        String invoice_amount = getPara("invoice_amount");
        alipay.setInvoiceAmount(invoice_amount);
        String buyer_pay_amount = getPara("buyer_pay_amount");
        alipay.setBuyerPayAmount(buyer_pay_amount);
        String point_amount = getPara("point_amount");
        alipay.setPointAmount(point_amount);
        String refund_fee = getPara("refund_fee");
        alipay.setRefundFee(refund_fee);
        String subject = getPara("subject");
        alipay.setSubject(subject);
        String body = getPara("body");
        alipay.setBody(body);
        String gmt_create = getPara("gmt_create");
        alipay.setGmtCreate(gmt_create);
        String gmt_payment = getPara("gmt_payment");
        alipay.setGmtPayment(gmt_payment);
        String gmt_refund = getPara("gmt_refund");
        alipay.setGmtRefund(gmt_refund);
        String gmt_close = getPara("gmt_close");
        alipay.setGmtClose(gmt_close);
        String fund_bill_list = getPara("fund_bill_list");
        alipay.setFundBillList(fund_bill_list);
        alipay.save();
        logger.info("{}", notifyTime);
        logger.info("------------------------------支付宝回掉结束-------------------------");
//        Map<String, String> paramsMap = Maps.newHashMap(); //将异步通知中收到的待验证所有参数都存放到map中
//        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, ALIPAY_PUBLIC_KEY, CHARSET) //调用SDK验证签名
//        if(signVerfied){
//            // TODO 验签成功后
//            //按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
//        }else{
//            // TODO 验签失败则记录异常日志，并在response中返回failure.
//        }
        //判断是否成功
        if (alipay.getTradeStatus().equals("TRADE_SUCCESS")) {
            if (out_trade_no.startsWith(Constant.AliPayOrderType.NomorlOrder)) {//普通的订单
                Order order = Order.dao.findByNo(out_trade_no);
                if (order != null) {
                    order.setRealPay(new BigDecimal(receipt_amount));//实收金额
                    EventKit.post(new OrderPay(order));//支付订单
                    EventKit.post(new Reward(order));//计算分销奖励
                    renderText("success");
                } else {
                    renderText("filed");
                }
            } else {
                CapitalLog capitalLog = CapitalLog.dao.findByNo(out_trade_no);
                if (capitalLog != null) {
                    EventKit.post(new Rechange(capitalLog));
                    renderText("success");
                } else {
                    renderText("filed");
                }
            }
        } else {
            renderText("filed");
        }
    }

    public void wechatpaynotice() {
        logger.info("------------------------------微信支付回掉开始-------------------------");
        String okRespoXml = "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
        String errorRespoXml = "\"<xml>\\n\" +\n" +
                "                        \"  <return_code><![CDATA[FAILED]]></return_code>\\n\" +\n" +
                "                        \"  <return_msg><![CDATA[FAILED]]></return_msg>\\n\" +\n" +
                "                        \"</xml>\"";
        try {
            InputStream xml = getRequest().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(xml));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    xml.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.info("微信支付回调的参数" + sb.toString());
            Map<String, String> xmlMap = GetWxOrderno.doXMLParse(sb.toString());
            WechatPay wechatPay = new WechatPay();
            wechatPay.setAppid(xmlMap.get("appid") == null ? "" : xmlMap.get("appid"));
            wechatPay.setMchId(xmlMap.get("mch_id") == null ? "" : xmlMap.get("mch_id"));
            wechatPay.setDeviceInfo(xmlMap.get("device_info") == null ? "" : xmlMap.get("device_info"));
            wechatPay.setNonceStr(xmlMap.get("nonce_str") == null ? "" : xmlMap.get("nonce_str"));
            wechatPay.setSign(xmlMap.get("sign") == null ? "" : xmlMap.get("sign"));
            wechatPay.setResultCode(xmlMap.get("result_code") == null ? "" : xmlMap.get("result_code"));
            wechatPay.setReturnCode(xmlMap.get("return_code") == null ? "" : xmlMap.get("return_code"));
            wechatPay.setErrCode(xmlMap.get("err_code") == null ? "" : xmlMap.get("err_code"));
            wechatPay.setErrCodeDes(xmlMap.get("err_code_des") == null ? "" : xmlMap.get("err_code_des"));
            wechatPay.setIsSubscribe(xmlMap.get("is_subscribe") == null ? "" : xmlMap.get("is_subscribe"));
            wechatPay.setTradeType(xmlMap.get("trade_type") == null ? "" : xmlMap.get("trade_type"));
            wechatPay.setBankType(xmlMap.get("bank_type") == null ? "" : xmlMap.get("bank_type"));
            wechatPay.setTotalFee(xmlMap.get("total_fee") == null ? "" : xmlMap.get("total_fee"));
            wechatPay.setCashFee(xmlMap.get("cash_fee") == null ? "" : xmlMap.get("cash_fee"));
            wechatPay.setCashFeeType(xmlMap.get("cash_fee_type") == null ? "" : xmlMap.get("cash_fee_type"));
            wechatPay.setTransactionId(xmlMap.get("transaction_id") == null ? "" : xmlMap.get("transaction_id"));
            wechatPay.setOutTradeNo(xmlMap.get("out_trade_no") == null ? "" : xmlMap.get("out_trade_no"));
            wechatPay.setAttach(xmlMap.get("attach") == null ? "" : xmlMap.get("attach"));
            wechatPay.setTimeEnd(xmlMap.get("time_end") == null ? "" : xmlMap.get("time_end"));
            wechatPay.setCreateTime(DateTime.now().toDate());
            wechatPay.save();
            if (xmlMap.get("return_code").indexOf("SUCCESS") != -1) {
                String out_trade_no = xmlMap.get("out_trade_no").substring(0, xmlMap.get("out_trade_no").indexOf("_"));
                if (out_trade_no.startsWith(Constant.AliPayOrderType.NomorlOrder)) {//普通的订单
                    Order order = Order.dao.findByNo(out_trade_no);
                    if (order != null) {
                        order.setRealPay(new BigDecimal(xmlMap.get("total_fee")).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));//实收金额
                        EventKit.post(new OrderPay(order));//支付订单
                        EventKit.post(new Reward(order));//计算分销奖励
                        renderText(okRespoXml);
                    } else {
                        renderText(errorRespoXml);
                    }
                } else {
                    CapitalLog capitalLog = CapitalLog.dao.findByNo(out_trade_no);
                    if (capitalLog != null) {
                        EventKit.post(new Rechange(capitalLog));
                        renderText(okRespoXml);
                    } else {
                        renderText(errorRespoXml);
                    }
                }
            } else {
                renderText(errorRespoXml);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("------------------------------微信支付回掉结束-------------------------");
    }

    public void pushNotice() {
        Notice notice = Notice.dao.findById(1);
//        NoticeService.getInstance().pushToAll(2, notice);
        renderText("123123");
    }

    public void aaaa() {
        Order order = Order.dao.findById(2343);
        MemberLogin memberLogin = MemberLogin.dao.findById(116);
        ActivityService.getInstance().joinRegisterActivity(memberLogin);
        renderText("123123");
    }

    @ActionKey("/auto")
    public void auto() {
        List<DriverInfo> driverInfos = Lists.newArrayList();
        driverInfos.add(DriverInfo.dao.findById(53));
        DateTime date = DateTime.parse(getPara("time"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        OnlineService.autoRecovery(date, driverInfos);
        renderText("操作成功！");
    }

    @ActionKey("/wechatpay")
    public void wechatpay() {
        Order order = Order.dao.findById(2963);
        Map<String, String> params = WechatPayKit.getWechatPayForApp(order, 2);
        renderAjaxSuccess(params);
    }
}
