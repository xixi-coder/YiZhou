package services;

import base.Constant;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import dto.pushDto.PushMap;
import jobs.sendnotice.SendContent;
import kits.SmsKit;
import kits.StringsKit;
import models.member.MemberMessage;
import models.member.MemberPushId;
import models.sys.Notice;
import net.dreamlu.event.EventKit;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.sqlInXml.SqlManager;
import utils.GeTuiPushUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/11/14.
 */
public class SendNoticeService {
    Logger logger = LoggerFactory.getLogger(SendNoticeService.class);
    
    public void send() {
        DateTime now = DateTime.now();
        Date sendTime = now.secondOfMinute().withMinimumValue().toDate();
        List<Notice> notices = Notice.dao.findNoticeAndSmsByDate(sendTime);
        System.out.printf("notices" + notices.size());
        for (Notice notice : notices) {
            EventKit.post(new SendContent(notice));
        }
    }
    
    public void send(Notice notice) {
        List<Record> memberLists = Lists.newArrayList();
        if (notice.getReciveType() == -1) {//全部司机和乘客
            memberLists = Db.find(SqlManager.sql("memberInfo.noticeMemberList"), notice.getCompany());
            List<Record> driverLists = Db.find(SqlManager.sql("driverInfo.noticeDriverList"), notice.getCompany());
            memberLists.addAll(driverLists);
        } else if (notice.getReciveType() == -2) {//全部乘客
            memberLists = Db.find(SqlManager.sql("memberInfo.noticeMemberList"), notice.getCompany());
        } else if (notice.getReciveType() == -3) {//全部司机
            memberLists = Db.find(SqlManager.sql("driverInfo.noticeDriverList"), notice.getCompany());
        } else if (notice.getReciveType() == -4) {//指定乘客
            String[] memberIds = notice.getReciver().split(";");
            String inWhere = Strings.repeat("?,", memberIds.length);
            if (memberIds.length > 0) {
                inWhere = inWhere.substring(0, inWhere.length() - 1);
            }
            List<Object> params = Lists.newArrayList();
            params.add(notice.getCompany());
            for (String memberId : memberIds) {
                params.add(memberId);
            }
            String sqlStr = StringsKit.replaceSql(SqlManager.sql("memberInfo.noticeMemberList"), " AND dmi.id IN (" + inWhere + ")");
            memberLists = Db.find(sqlStr, params.toArray());
        } else if (notice.getReciveType() == -5) {//指定司机
            String[] memberIds = notice.getReciver().split(";");
            String inWhere = Strings.repeat("?,", memberIds.length);
            if (memberIds.length > 0) {
                inWhere = inWhere.substring(0, inWhere.length() - 1);
            }
            List<Object> params = Lists.newArrayList();
            params.add(notice.getCompany());
            for (String memberId : memberIds) {
                params.add(memberId);
            }
            String sqlStr = StringsKit.replaceSql(SqlManager.sql("driverInfo.noticeDriverList"), " AND ddi.id IN (" + inWhere + ")");
            memberLists = Db.find(sqlStr, params.toArray());
        }
        sendSmsOrNotice(notice, memberLists);
    }
    
    public void sendSmsOrNotice(Notice notice, List<Record> memberLists) {
        if (notice.getSendType() == 1) {//短信发送
            sendSms(memberLists, notice.getContent());
        } else {//推送
            sendNotice(memberLists, notice.getContent(), notice.getTitle(), notice.getId());
        }
    }
    
    public void sendNotice(List<Record> info, String content, String title, int noticeId) {
        for (Record record : info) {
            MemberMessage memberMessage = new MemberMessage();
            memberMessage.setLoginId(record.getInt("login_id"));
            memberMessage.setContent(content);
            memberMessage.setTitle(title);
            memberMessage.setCreateTime(DateTime.now().toDate());
            memberMessage.setNoticeId(noticeId);
            memberMessage.setReadFlag(0);
            memberMessage.save();
            content = content.replace("NAME", record.getStr("real_name"));
            
            MemberPushId memberPushId = MemberPushId.dao.findByMemberId(record.getInt("login_id"));
            PushMap pushMap = new PushMap();
            pushMap.setUniquelyId(System.currentTimeMillis());
            pushMap.setPushType(Constant.PushType.JG);
            pushMap.setContent(content);
            pushMap.setTitle(Constant.JpushType.PushNotice);
            
            
            if (record.getInt("type") != null && record.getInt("type") == Constant.DRIVER) {
                pushMap.setTitle(Constant.JpushType.PushNotice);
                //JPushService.getInstance().sendMessageToDriver(Constant.JpushType.PushNotice, content, record.getStr("registration_id"));
                JPushService.getInstance().sendMessageToDriver(Constant.JpushType.PushNotice, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
                pushMap.setPushType(Constant.PushType.GT);
                GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, Integer.toString(Constant.JpushType.PushNotice), JSONObject.toJSONString(pushMap), "推送通知", memberPushId.getCId());
            } else if (record.getInt("type") != null && record.getInt("type") == Constant.MEMBER) {
                pushMap.setTitle(Constant.JpushType.PushNotice);
                //JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushNotice, content, record.getStr("registration_id"));
                JPushService.getInstance().sendMessageToDriver(Constant.JpushType.PushNotice, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
                pushMap.setPushType(Constant.PushType.GT);
                GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, Integer.toString(Constant.JpushType.PushNotice), JSONObject.toJSONString(pushMap), "推送通知", memberPushId.getCId());
                
            }
        }
    }
    
    public void sendSms(List<Record> info, String content) {
        for (Record record : info) {
            String xcontent = Constant.properties.getProperty("SMS.Signature", "【彝州出行】") + content.replace("NAME", record.getStr("real_name"));
            SmsKit.nomorlSms(record.getStr("phone"), xcontent, record.getNumber("company").intValue());
        }
    }
    
    private SendNoticeService() {
    
    }
    
    private static class SendNoticeServiceHolder {
        static SendNoticeService instance = new SendNoticeService();
    }
    
    public static SendNoticeService getInstance() {
        return SendNoticeServiceHolder.instance;
    }
    
    public static void main(String[] args) {
        String a = Strings.repeat("?,", 2);
        System.out.printf(a.substring(0, a.length() - 1));
    }
}
