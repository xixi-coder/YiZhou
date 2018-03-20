package utils;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import dto.TemplateParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/10/7.
 */
@SuppressWarnings("All")
public class GeTuiPushUtil {
    private static Logger logger = LoggerFactory.getLogger(GeTuiPushUtil.class);
    //加载配置文件
    //司机端
    private static String d_appid;
    private static String d_appkey;
    private static String d_mastersecret;
    private static String d_taskname;  //任务别名_toApp
    //客户端
    private static String m_appid;
    private static String m_appkey;
    private static String m_mastersecret;
    private static String m_taskname;  //任务别名_toApp

    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    static {
        InputStream inputStream = GeTuiPushUtil.class.getClassLoader().getResourceAsStream("push.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            Object d_appidObject = properties.get("G.D.APPID");
            Object d_appkeyObject = properties.get("G.D.APPKEY");
            Object d_tasknameObject = properties.get("G.D.TASKNAME");
            Object d_masterSecretObject = properties.get("G.D.MASTERSECRET");
            Object m_appidObject = properties.get("G.M.APPID");
            Object m_appkeyObject = properties.get("G.M.APPKEY");
            Object m_tasknameObject = properties.get("G.M.TASKNAME");
            Object m_masterSecretObject = properties.get("G.M.MASTERSECRET");
            if (d_appidObject == null) {
                throw new RuntimeException("d_appid在push.properties中不存在");
            }
            if (d_appkeyObject == null) {
                throw new RuntimeException("d_appkey在push.properties中不存在");
            }
            if (d_masterSecretObject == null) {
                throw new RuntimeException("d_masterSecret在push.properties中不存在");
            }
            if (d_tasknameObject == null) {
                throw new RuntimeException("d_TASKNAME在push.properties中不存在");
            }

            if (m_appidObject == null) {
                throw new RuntimeException("m_appid在push.properties中不存在");
            }
            if (m_appkeyObject == null) {
                throw new RuntimeException("m_appkey在push.properties中不存在");
            }
            if (m_masterSecretObject == null) {
                throw new RuntimeException("m_masterSecret在push.properties中不存在");
            }
            if (m_tasknameObject == null) {
                throw new RuntimeException("m_TASKNAME在push.properties中不存在");
            }

            d_appid = d_appidObject.toString();
            d_appkey = d_appkeyObject.toString();
            d_taskname = d_tasknameObject.toString();
            d_mastersecret = d_masterSecretObject.toString();

            m_appid = m_appidObject.toString();
            m_appkey = m_appkeyObject.toString();
            m_taskname = m_tasknameObject.toString();
            m_mastersecret = m_masterSecretObject.toString();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private static class GeTuiPushUtilHolder {
        static GeTuiPushUtil instance = new GeTuiPushUtil();
    }

    public static GeTuiPushUtil getInstance() {
        return GeTuiPushUtilHolder.instance;
    }

    // 设置通知消息模板
    /*     * 1. APPID
     * 2. appKey
     * 3. 要传送到客户端的 msg
     * 3.1 标题栏：key = title,
     * 3.2 通知栏内容： key = titleText,
     * 3.3 穿透内容：key = transText
     */
    private static NotificationTemplate getNotifacationTemplate(String title, String titleText, String APPID, String APPKEY) {
        // 在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        // 穿透消息设置为，1 强制启动应用
        template.setTransmissionType(1);
        // 设置穿透内容
        //System.out.println(para.getTitle() + "::" + para.getTitleText() + "::" + para.getTransText());
        template.setTransmissionContent(JSONObject.toJSON(titleText).toString());
        // 设置style
        Style0 style = new Style0();
        // 设置通知栏标题和内容
        style.setTitle(title);
        style.setText(titleText);
        // 设置通知，响铃、震动、可清除
        // style.setRing(para.isRing());
        // style.setVibrate(para.isVibrate());
        //  style.setClearable(para.isClearable());
        // 设置
        template.setStyle(style);

        return template;
    }

    /**
     * 设置应用群 通知消息模板
     *
     * @param para
     * @param openURL
     * @return
     */
    private static LinkTemplate linkTemplateDemo(TemplateParameter para, String openURL, String APPID, String APPKEY) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        // 设置通知栏标题与内容
        template.setTitle(para.getTitle());
        template.setText(para.getTitleText());
        // 配置通知栏图标
        template.setLogo("http://106.14.28.8:8080/dateimg/img/mh.png");
        // 配置通知栏网络图标，填写图标URL地址
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(para.isRing());
        template.setIsVibrate(para.isVibrate());
        template.setIsClearable(para.isClearable());
        // 设置打开的网址地址
        template.setUrl(openURL);
        return template;
    }

    /**
     * 透传消息模版
     *
     * @param appMsgInfo
     * @return
     */
    private static TransmissionTemplate transmissionTemplateDemo(String context, String APPID, String APPKEY) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(context);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }

    /**
     * iOS 穿透模板
     *
     * @return
     */
    private static TransmissionTemplate getTemplate(TemplateParameter parameter, String APPID, String APPKEY) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(APPID);
        template.setAppkey(APPKEY);
        template.setTransmissionContent(JSONObject.toJSON(parameter).toString());
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");

        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));

        //自定义类型参数
       /* payload.addCustomMsg("type", appMsgInfo.getSendType());
        payload.addCustomMsg("link", appMsgInfo.getLink());//推送url路径*/

        //字典模式使用APNPayload.DictionaryAlertMsg
        payload.setAlertMsg(getDictionaryAlertMsg(parameter));

        //字典模式使用APNPayload.DictionaryAlertMsg
        //payload.setAlertMsg(getDictionaryAlertMsg());

        // 添加多媒体资源
       /* payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
        .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
        .setOnlyWifi(true));*/

        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(TemplateParameter parameter) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setTitle(parameter.getTitle());//推送标题
        alertMsg.setBody(parameter.getTitleText());//推送内容
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image"); // iOS8.2以上版本支持 alertMsg.setTitle("Title");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }

    // 对单个用户推送消息
    /*  * 1. cid
     * 2. 要传到客户端的 msg
     * 2.1 标题栏：key = title,
     * 2.2 通知栏内容： key = titleText,
     * 2.3 穿透内容：key = transText*/
    public boolean pushMsgToSingle(Integer type, String openURL, TemplateParameter para) throws RuntimeException {
        IGtPush push = null;
        String APPID = null;
        String APPKEY = null;
        if (type == 1) {  //1是司机端 2是客户端
            push = new IGtPush(host, d_appid, d_mastersecret);
            APPID = d_appid;
            APPKEY = d_appkey;
        } else {
            push = new IGtPush(host, m_appid, m_mastersecret);
            APPID = m_appid;
            APPKEY = m_appkey;
        }

        LinkTemplate template = linkTemplateDemo(para, openURL, APPID, APPKEY);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(APPID);
        target.setClientId(para.getcId()[0]);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
            logger.info("个推单推--推送的用户cId:{}", para.getcId()[0]);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                return true;
            }
            logger.error("个推推送异常------>pushMsgToSingle");
        } else {
            logger.error("个推服务器响应异常------>pushMsgToSingle");
        }
        return false;
    }

    /**
     * iOS 推送
     *
     * @param openURL
     * @param para
     * @throws RuntimeException
     */
    public boolean pushMsgToiOSSingle(Integer type, String openURL, TemplateParameter para) throws RuntimeException {

        IGtPush push = null;
        String APPID = null;
        String APPKEY = null;
        if (type == 1) {  //1是司机端 2是客户端
            push = new IGtPush(host, d_appid, d_mastersecret);
            APPID = d_appid;
            APPKEY = d_appkey;
        } else {
            push = new IGtPush(host, m_appid, m_mastersecret);
            APPID = m_appid;
            APPKEY = m_appkey;
        }
        TransmissionTemplate template = getTemplate(para, APPID, APPKEY);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(APPID);
        target.setClientId(para.getcId()[0]);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
            logger.info("个推iOS--推送的用户cId:{}", para.getcId()[0]);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                return true;
            }
            logger.error("个推iOS推送异常------>pushMsgToiOSSingle");
        } else {
            logger.error("个推服务器响应异常------>pushMsgToiOSSingle");
        }
        return false;
    }

    /**
     * 应用级推送
     *
     * @param para
     * @param phoneTypeList 手机类型
     * @param provinceList  省份
     * @param tagList       自定义tag
     * @return
     */
    public boolean pushToApp(Integer type, TemplateParameter para, String openURL, List<String> phoneTypeList, List<String> provinceList, List<String> tagList) {
        IGtPush push = null;
        String APPID = null;
        String APPKEY = null;
        String TASKNAME = null;
        if (type == 1) {  //1是司机端 2是客户端
            push = new IGtPush(host, d_appid, d_mastersecret);
            APPID = d_appid;
            APPKEY = d_appkey;
            TASKNAME = d_taskname;
        } else {
            push = new IGtPush(host, m_appid, m_mastersecret);
            APPID = m_appid;
            APPKEY = m_appkey;
            TASKNAME = m_taskname;
        }
        LinkTemplate template = linkTemplateDemo(para, openURL, APPID, APPKEY);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();
        List<String> APPIDList = new ArrayList<String>();
        APPIDList.add(APPID);
        message.setAppIdList(APPIDList);
        cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
        cdt.addCondition(AppConditions.REGION, provinceList);
        cdt.addCondition(AppConditions.TAG, tagList);
        message.setConditions(cdt);
        IPushResult ret = push.pushMessageToApp(message, TASKNAME);

        if (ret != null) {
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                return true;
            }
            logger.error("个推应用级推送推送异常------>pushToApp");
        } else {
            logger.error("个推服务器响应异常------>pushToApp");
        }
        return false;
    }


    public boolean pushMsgToOne(Integer type, String title, String titleText, String orderNo, String... cId) {

        if (cId == null) {
            logger.info("订单号：{}，推送失败，原因：没有个推id", orderNo);
            return false;
        }
        if (type == 1) {
            for (String c : cId) {
                try {
                    pushMsgToDriver(titleText, c);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        } else {
            for (String c : cId) {
                try {
                    pushMsgToMember(titleText, c);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * 推送单条
     *
     * @param para
     * @return
     */
    public boolean pushMsgToOne(Integer type, String title, String titleText, String orderNo, String cId) {
        if (cId == null) {
            logger.info("订单号：{}，推送失败，原因：没有个推id", orderNo);
            return false;
        }
        //1是司机端 2是客户端
        if (type == 1) {
            try {
                pushMsgToDriver(titleText, cId);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                pushMsgToMember(titleText, cId);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void pushMsgToDriver(String titleText, String cId) {
        IGtPush push = new IGtPush(host, d_appkey, d_mastersecret);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmissionTemplateDemo(titleText, d_appid, d_appkey));
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(d_appid);
        target.setClientId(cId);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            logger.info(ret.getResponse().toString());
        } else {
            logger.error("个推推送异常,pushMsgToDriver");
            throw new RuntimeException("个推推送异常,pushMsgToMember");
        }
    }

    public void pushMsgToMember(String titleText, String cId) {
        IGtPush push = new IGtPush(host, m_appkey, m_mastersecret);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmissionTemplateDemo(titleText, m_appid, m_appkey));
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(m_appid);
        target.setClientId(cId);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            logger.info(ret.getResponse().toString());
        } else {
            logger.info("个推推送异常,pushMsgToMember");
            throw new RuntimeException("个推推送异常,pushMsgToMember");
        }
    }


    /**
     * 列表用户推送
     *
     * @param para
     * @return
     */
    public boolean pushMsgToList(Integer type, String title, String titleText, String orderNo, String... cId) {

        if (cId == null) {
            logger.info("订单号：{}，推送失败，原因：没有个推id", orderNo);
            return false;
        }
        if (type == 1) {  //1是司机端 2是客户端
            return pushMsgToDriverList(title, titleText, cId);
        } else {
            return pushMsgToMemberList(title, titleText, cId);
        }

    }

    public boolean pushMsgToDriverList(String title, String titleText, String... cId) {

        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        //1是司机端
        IGtPush push = new IGtPush(host, d_appkey, d_mastersecret);

        TransmissionTemplate template = transmissionTemplateDemo(titleText, d_appid, d_appkey);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();

        for (String str : cId) {
            Target target = new Target();
            target.setAppId(d_appid);
            target.setClientId(str);
            targets.add(target);
        }


        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        if (ret != null) {
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                logger.info(ret.getResponse().toString());
                return true;
            }
            logger.error("个推列表用户推送异常------>pushMsgToList");
        } else {
            logger.error("个推服务器响应异常------>pushMsgToList");
        }
        return false;

    }

    public boolean pushMsgToMemberList(String title, String titleText, String... cId) {

        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        //1是司机端
        IGtPush push = new IGtPush(host, m_appkey, m_mastersecret);

        TransmissionTemplate template = transmissionTemplateDemo(titleText, m_appid, m_appkey);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();

        for (String str : cId) {
            Target target = new Target();
            target.setAppId(m_appid);
            target.setClientId(str);
            targets.add(target);
        }


        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        if (ret != null) {
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                logger.info(ret.getResponse().toString());
                return true;
            }
            logger.error("个推列表用户推送异常------>pushMsgToList");
        } else {
            logger.error("个推服务器响应异常------>pushMsgToList");
        }
        return false;

    }


    /**
     * 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
       /* IGtPush push = new IGtPush(host, d_appkey, d_mastersecret);
        //LinkTemplate template = linkTemplateDemo();
        TemplateParameter parameter = new TemplateParameter();
        parameter.setVibrate(true);
        parameter.setClearable(true);
        parameter.setRing(true);
        parameter.setTransText("穿透消息！！！！！");
        parameter.setTitle("这是标题");
        parameter.setTitleText("消息体");
        parameter.setTransmissionType(1);
        
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmissionTemplateDemo(JSONObject.toJSON(parameter).toString(), d_appid, d_appkey));
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(d_appid);
        target.setClientId("59f0a805a5cad623bc8eafcb8cff7772");
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }*/

        GeTuiPushUtil.getInstance().pushMsgToList(2, "123123", "123123", "123123", "adadf955be8dd9b30ade655f05061468", "e208f124621a90831ac4060a466c40ba");
    }


}







