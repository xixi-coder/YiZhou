package services;

import base.Constant;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPushService {
    Logger logger = LoggerFactory.getLogger(JPushService.class);
    JPushClient memberClient;
    JPushClient driverClient;

    private JPushService() {
        final String memberAppKey = Constant.prop.get("jpush.member.appkey");
        final String memberMasterSecret = Constant.prop.get("jpush.member.secret");
        final String driverAppKey = Constant.prop.get("jpush.driver.appkey");
        final String driverMasterSecret = Constant.prop.get("jpush.driver.secret");
        ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(5);
        config.setConnectionTimeout(10 * 1000);    // 10 seconds
        config.setSSLVersion("SSLv3");
        this.memberClient = new JPushClient(memberMasterSecret, memberAppKey, null, config);
        this.driverClient = new JPushClient(driverMasterSecret, driverAppKey, null, config);
    }

    private static class JPushServiceHolder {
        static JPushService instance = new JPushService();
    }

    public static JPushService getInstance() {
        return JPushServiceHolder.instance;
    }

    public boolean sendMessageToDriver(int type, String content, String... registrationId) {
        logger.info("司机端jpush推送的用户registrationId:{}", registrationId);
        if (registrationId != null && !Strings.isNullOrEmpty(registrationId[0])) {
            return sendMessage(type + "", content, this.driverClient, registrationId);
        } else {
            return true;
        }
    }

    public boolean sendMessageToCustomer(int type, String content, String... registrationId) {
        logger.info("客户端jpush推送的用户registrationId:{}", registrationId);
        if (registrationId != null && !Strings.isNullOrEmpty(registrationId[0])) {
            return sendMessage(type + "", content, this.memberClient, registrationId);
        } else {
            return true;
        }
    }


    public boolean sendMessage(String type, String content, JPushClient client, String... registrationId) {
        try {
            PushResult pushResult = client.sendMessageWithRegistrationID(type, content, registrationId);
            return pushResult.isResultOK();
        } catch (APIConnectionException e) {
            e.printStackTrace();
            logger.error("极光推送发生错误;是否超时{};时间{}", e.isReadTimedout(), e.getDoneRetriedTimes());
            return false;
        } catch (APIRequestException e) {
            e.printStackTrace();
            logger.error("极光推送发生错误;错误信息{};错误代码{}", e.getErrorMessage(), e.getErrorCode());
            return false;
        }
    }

    public static void main(String[] args) throws APIConnectionException, APIRequestException {
        ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(5);
        config.setConnectionTimeout(10 * 1000);    // 10 seconds
        config.setSSLVersion("SSLv3");
        JPushClient memberClient = new JPushClient("dc9fdff4ab92fc17bec4eb6e", "c6f5ef6cbe4b92a85fcb53b9", null, config);
        memberClient.sendMessageWithRegistrationID("4", "{title:\"123\",content:\"123\"}", "1104a89792a15156278");
    }
}