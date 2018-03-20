package services;

import com.google.common.collect.Lists;
import models.sys.SmsServiceLog;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by admin on 2016/10/24.
 */
public class SmsHttpService {
    private static Logger logger = LoggerFactory.getLogger(SmsHttpService.class);

    private final String httpUrl = "http://120.26.66.24/msg/HttpBatchSendSM";
    private final String account = "hfdllxx_hy";
    private final String pswd = "hfdllxx_hy123";

    private SmsHttpService() {
    }

    private static class SmsHttpServiceHolder {
        static SmsHttpService instance = new SmsHttpService();
    }

    public static SmsHttpService getInstance() {
        return SmsHttpService.SmsHttpServiceHolder.instance;
    }

    public boolean sendSms(String msg, String... phone) {
//                return true;

        String[] phons = phone;
        String phoneStr = "";
        for (String p : phone) {
            phoneStr += p + ",";
        }
        if (phoneStr.endsWith(",")) {
            phoneStr = phoneStr.substring(0, phoneStr.length() - 1);
        }
        return send(phoneStr, msg);
    }

    public static boolean send(String phone, String content) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://120.26.66.24/msg/HttpBatchSendSM");
        SmsServiceLog smsServiceLog = new SmsServiceLog();
        smsServiceLog.setCreateTime(DateTime.now().toDate());
        smsServiceLog.setPhones(phone);
        smsServiceLog.setContent(content);

        List<NameValuePair> formparams = Lists.newArrayList();
        formparams.add(new BasicNameValuePair("account", "hfdllxx_hy"));
        formparams.add(new BasicNameValuePair("pswd", "hfdllxx_hy123"));
        formparams.add(new BasicNameValuePair("mobile", phone));
        formparams.add(new BasicNameValuePair("msg", content));
        formparams.add(new BasicNameValuePair("needstatus", "false"));
        formparams.add(new BasicNameValuePair("product", "85047007"));
        formparams.add(new BasicNameValuePair("extno", "1199 "));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    logger.info("发送返回的消息的返回结果{}", result);
                    smsServiceLog.setResult(result);
                    if (result.split(",")[1].equals("0")) {
                        smsServiceLog.save();
                        return true;
                    } else {
                        smsServiceLog.save();
                        return false;
                    }
                } else {
                    return false;
                }
            } finally {
                response.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
