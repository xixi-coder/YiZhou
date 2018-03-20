package services;

import base.Constant;
import com.alibaba.fastjson.JSONObject;
import dto.pushDto.PushMap;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import models.sys.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GeTuiPushUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/11/4.
 */
public class NoticeService {
    static Logger logger = LoggerFactory.getLogger(NoticeService.class);
    
    
    private NoticeService() {
    
    }
    
    private static class NoticeServiceHolder {
        static NoticeService instance = new NoticeService();
    }
    
    public static NoticeService getInstance() {
        return NoticeServiceHolder.instance;
    }
    
    /**
     * 一分钟刷新看看有没有通知
     *
     * @param companyId
     * @param notice
     */
    public static void pushToAll(int companyId, Notice notice) {
        List<MemberInfo> memberInfos = MemberInfo.dao.findByCompanyAndLogined(companyId, Constant.LoginStatus.LOGOUTED);
        List<DriverInfo> driverInfos = DriverInfo.dao.findByCompanyAndLogined(companyId, Constant.LoginStatus.LOGOUTED);
        Map<Integer, String[]> pushIdsD = PushOrderService.getIntance().getPushIds(driverInfos);//获取在线的极光推送id
        Map<Integer, String[]> pushIdsM = PushOrderService.getIntance().getMemberRegistrationIds(memberInfos);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", notice.getTitle());
        jsonObject.put("content", notice.getContent());
        String[] registrations;
        String[] cId;
        Integer type = Constant.JpushType.PushNotice;
    
        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setContent(jsonObject);
        pushMap.setTitle(type);
        
        
        if (pushIdsD.get(1).length > 0) {
            logger.info("司机android端极光推送的ids:{}", pushIdsD.get(1).length + "");
            registrations = pushIdsD.get(1);
            JPushService.getInstance().sendMessageToDriver(type, JSONObject.toJSONString(pushMap), registrations);
        }
        if (pushIdsD.get(11).length > 0) {
            logger.info("司机android端个推推送的ids:{}", pushIdsD.get(11).length + "");
            pushMap.setPushType(Constant.PushType.GT);
            cId = pushIdsD.get(11);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, type.toString(), JSONObject.toJSONString(pushMap), null, cId);
        }
        if (pushIdsD.get(2).length > 0) {
            logger.info("司机ios端极光推送的ids:{}", pushIdsD.get(2).length + "");
            registrations = pushIdsD.get(2);
            JPushService.getInstance().sendMessageToDriver(type, JSONObject.toJSONString(pushMap), registrations);
        }
        if (pushIdsD.get(22).length > 0) {
            logger.info("司机ios端个推推送的ids:{}", pushIdsD.get(22).length + "");
            pushMap.setPushType(Constant.PushType.GT);
            cId = pushIdsD.get(22);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.SJ, type.toString(), JSONObject.toJSONString(pushMap), null, cId);
        }
        
        
        if (pushIdsM.get(1).length > 0) {
            logger.info("客户android端极光推送的ids:{}", pushIdsM.get(1).length + "");
            registrations = pushIdsM.get(1);
            JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushNotice, JSONObject.toJSONString(pushMap), registrations);
        }
        if (pushIdsM.get(11).length > 0) {
            logger.info("客户android端个推推送的ids:{}", pushIdsM.get(1).length + "");
            pushMap.setPushType(Constant.PushType.GT);
            cId = pushIdsM.get(11);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), null, cId);
        }
        if (pushIdsM.get(2).length > 0) {
            logger.info("客户ios端极光推送的ids:{}", pushIdsM.get(2).length + "");
            registrations = pushIdsM.get(2);
            JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushNotice, JSONObject.toJSONString(pushMap), registrations);
        }
        if (pushIdsM.get(2).length > 0) {
            logger.info("客户ios端个推推送的ids:{}", pushIdsM.get(2).length + "");
            pushMap.setPushType(Constant.PushType.GT);
            cId = pushIdsM.get(2);
            GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, type.toString(), JSONObject.toJSONString(pushMap), null, cId);
        }
    }
}
