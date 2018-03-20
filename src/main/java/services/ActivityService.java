package services;

import base.Constant;
import com.alibaba.fastjson.JSONObject;
import dto.activity.AwardDto;
import dto.pushDto.PushMap;
import kits.SmsKit;
import kits.StringsKit;
import models.activity.Activity;
import models.activity.Coupon;
import models.activity.MemberActivity;
import models.activity.MemberCoupon;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.member.*;
import models.order.Order;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GeTuiPushUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by admin on 2016/11/26.
 */
public class ActivityService {
    private static Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private ActivityService() {

    }

    private static class ActivityServiceHolder {
        static ActivityService instance = new ActivityService();
    }

    public static ActivityService getInstance() {
        return ActivityServiceHolder.instance;
    }

    /**
     * 参加注册活动
     *
     * @param memberLogin
     */
    public void joinRegisterActivity(MemberLogin memberLogin) {
        DateTime registerTime = new DateTime(memberLogin.getCreateTime());
        int companyId = MemberInfo.dao.findByLoginId(memberLogin.getId()).getCompany();
        List<Activity> activities = Activity.dao.findByEvent(Constant.ActivityEvent.REGISTER, registerTime, companyId);
        for (Activity activity : activities) {
            award(memberLogin.getId(), activity);
        }
    }

    /**
     * 参加打开app有奖活动
     *
     * @param memberLogin
     */
    public void joinOpenApp(MemberLogin memberLogin) {
        DateTime now = DateTime.now();
        int companyId = MemberInfo.dao.findByLoginId(memberLogin.getId()).getCompany();
        List<Activity> activities = Activity.dao.findByEvent(Constant.ActivityEvent.OPENAPP, now, companyId);
        for (Activity activity : activities) {
            if (MemberActivity.dao.isJoin(memberLogin.getId(), activity.getId())) {
                award(memberLogin.getId(), activity);
            }
        }
    }


    /**
     * 参加活动
     *
     * @param order
     */
    public void joinActivity(Order order) {
        joinOrderOfFristActivity(order);
        joinOrderOfCountActivity(order);
    }


    /**
     * 参加活动
     *
     * @param order
     */
    public void joinOrderOfFristActivity(Order order) {
        int companyId = order.getCompany();
        int driverId = order.getDriver();
        int memberId = order.getMember();
        int payType = order.getPayChannel();
        int fromType = order.getFromType();
        MemberInfo memberInfo = MemberInfo.dao.findById(memberId);
        DateTime orderTime = new DateTime(order.getCreateTime());
        List<Activity> activities = Activity.dao.findByEvent(Constant.ActivityEvent.FRISTORDER, orderTime, companyId);
        for (Activity activity : activities) {
            int count = Order.dao.findCountByMemberComplete(memberId);
            if (1 == count) {
                award(memberInfo.getLoginId(), activity);
            }
        }
    }

    /**
     * 参加活动
     *
     * @param order
     */
    public void joinOrderOfCountActivity(Order order) {
        int companyId = order.getCompany();
        int driverId = order.getDriver();
        int memberId = order.getMember();
        String payType = order.getPayChannel() + "";
        String serviceType = order.getServiceType() + "";
        String fromType = order.getFromType() + "";
        MemberInfo memberInfo = MemberInfo.dao.findById(memberId);
        DateTime orderTime = new DateTime(order.getCreateTime());
        List<Activity> activities = Activity.dao.findByEvent(Constant.ActivityEvent.TIMEORDER, orderTime, companyId);
        for (Activity activity : activities) {
            String paysType = activity.getPayType();
            String servicesType = activity.getServiceType();
            String orderType = activity.getOrderType();
            int minOrderCount = activity.getMinOrderCount();
            int count = Order.dao.findCountByMemberCompleteAndDate(memberId, activity.getStartTime(), activity.getEndTime());
            if (minOrderCount == count && servicesType.contains(serviceType) && paysType.contains(payType) && orderType.contains(fromType)) {
                award(memberInfo.getLoginId(), activity);
            } else {
                logger.error("没有满足活动要求");
            }
        }
    }

    /**
     * 获取活动的奖励项
     *
     * @param activity
     * @return
     */
    public AwardDto getAward(Activity activity) {
        AwardDto award = new AwardDto();
        award.setType(activity.getType());//余额类型
        if (activity.getType() == Constant.ActivityType.yue) {
            award.setRebate(activity.getRebate());//金额
        } else if (activity.getType() == Constant.ActivityType.coupon) {
            Coupon coupon = Coupon.dao.findById(activity.getCoupon());
            if (coupon == null) {
                logger.error("没有该优惠券");
                return null;
            }
            award.setCoupon(coupon);
        }
        return award;
    }

    /**
     * 将优惠券赠送给会员
     *
     * @param loginId
     * @param activity
     */
    public void award(int loginId, Activity activity) {
        AwardDto award = getAward(activity);
        if (award == null) {
            logger.error("没有任何奖励信息!");
            return;
        }
        String aname = activity.getName();
        String content;
        MemberInfo memberInfo = MemberInfo.dao.findByLoginId(loginId);
        CompanyAccount companyAccount = CompanyAccount.dao.findById(memberInfo.getCompany());
        if (award.getType() == Constant.ActivityType.yue) {
            MemberCapitalAccount memberAccount = MemberCapitalAccount.dao.findByLogid(loginId);
            CapitalLog capitalLog = new CapitalLog();
            capitalLog.setLoginId(loginId);
            capitalLog.setNo(StringsKit.getCaptMember());
            capitalLog.setCreateTime(DateTime.now().toDate());
            capitalLog.setAmount(award.getRebate());
            capitalLog.setRemark("参加活动:" + activity.getName() + "获得奖励");
            capitalLog.setOperater(loginId);
            capitalLog.setOperationType(Constant.CapitalOperationType.ACTIVITYJIANGLI);
            capitalLog.setStatus(Constant.CapitalStatus.OK);
            BigDecimal historyAmount = memberAccount.getAmount();
            memberAccount.setAmount(historyAmount.add(award.getRebate()));
            memberAccount.setLastUpdateAmount(award.getRebate());
            if (CompanyAccount.dao.amountEnough(companyAccount, award.getRebate(),Constant.CompanyAccountActivity.awardMember)) {
                MemberCapitalAccount.dao.saveAccountAndLog(memberAccount, capitalLog);
                content = "活动奖励余额:" + award.getRebate() + "元";
                CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.HUODONGJIANGLI, award.getRebate(), memberInfo.getCompany(), memberInfo.getLoginId(), Constant.DataAuditStatus.AUDITOK, 0, "客户下单赠送的现金到客户余额");
                companyActivity.save();
                notice(aname, memberInfo.getPhone(), content, loginId, activity.getNoticeType());
            } else {
                logger.info("公司id：{}的公司活动金额不足", memberInfo.getCompany());
            }

        } else if (award.getType() == Constant.ActivityType.coupon) {
            Coupon coupon = award.getCoupon();
            MemberCoupon memberCoupon = new MemberCoupon();
            if (coupon.getCouponType() == Constant.CouponType.Discount){
                String password = RandomStringUtils.randomNumeric(6);
                String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                memberCoupon.setCouponId(coupon.getId());
                memberCoupon.setStartTime(coupon.getStartTime());
                memberCoupon.setEndTime(coupon.getEndTime());
                memberCoupon.setTitle(coupon.getCouponTitle());
                memberCoupon.setDescription(coupon.getCouponDesc());
                memberCoupon.setAmount(coupon.getAmount());
                memberCoupon.setCouponSource("参与活动:" + activity.getName() + "赠送的");
                memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                memberCoupon.setNo(no);
                memberCoupon.setGainTime(DateTime.now().toDate());
                memberCoupon.setPassword(password);
                memberCoupon.setLoginId(loginId);
                if (CompanyAccount.dao.amountEnough(companyAccount, BigDecimal.ZERO,Constant.CompanyAccountActivity.awardMember)) {
                    memberCoupon.save();
                    CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.ACTIVITYCOUPON, coupon.getAmount(), memberInfo.getCompany(), memberInfo.getLoginId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "客户下单赠送的优惠券");
                    companyActivity.save();
                    content = "活动奖励优惠券:" + coupon.getCouponTitle() + "，最多可以折扣:" + coupon.getPercenAmount() + "元";
                    notice(aname, memberInfo.getPhone(), content, loginId, activity.getNoticeType());
                } else {
                    logger.info("公司id：{}的公司活动金额不足", memberInfo.getCompany());
                }
            }else {
                String password = RandomStringUtils.randomNumeric(6);
                String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddhhmmss")) + (RandomStringUtils.randomAlphanumeric(6)).toUpperCase();
                memberCoupon.setCouponId(coupon.getId());
                memberCoupon.setStartTime(coupon.getStartTime());
                memberCoupon.setEndTime(coupon.getEndTime());
                memberCoupon.setTitle(coupon.getCouponTitle());
                memberCoupon.setDescription(coupon.getCouponDesc());
                memberCoupon.setAmount(coupon.getAmount());
                memberCoupon.setCouponSource("参与活动:" + activity.getName() + "赠送的");
                memberCoupon.setStatus(Constant.CouponStatus.USEFUL);
                memberCoupon.setNo(no);
                memberCoupon.setGainTime(DateTime.now().toDate());
                memberCoupon.setPassword(password);
                memberCoupon.setLoginId(loginId);
                if (CompanyAccount.dao.amountEnough(companyAccount, coupon.getAmount(),Constant.CompanyAccountActivity.awardMember)) {
                    memberCoupon.save();
                    CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.ACTIVITYCOUPON, coupon.getAmount(), memberInfo.getCompany(), memberInfo.getLoginId(), Constant.DataAuditStatus.CREATE, memberCoupon.getId(), "客户下单赠送的优惠券");
                    companyActivity.save();
                    content = "活动奖励优惠券:" + coupon.getCouponTitle() + ",价值:" + coupon.getAmount() + "元";
                    notice(aname, memberInfo.getPhone(), content, loginId, activity.getNoticeType());
                } else {
                    logger.info("公司id：{}的公司活动金额不足", memberInfo.getCompany());
                }
            }

        }

    }
    
    public void notice(String aname, String phone, String content, int loginId, String type) {
        if (type.contains("1")) {
            smsMessage(aname, phone, content);
        }
        if (type.contains("2")) {
            pushMessage(aname, content, loginId);
        }
    }
    
    public void smsMessage(String aname, String phone, String content) {
        SmsKit.activity(aname, content, phone);
    }
    
    public void pushMessage(String aname, String content, int loginId) {
        //推送
        MemberLogin memberLogin = MemberLogin.dao.findById(loginId);
        content = "参加活动:" + aname + "," + content;
        MemberPushId memberPushId = MemberPushId.dao.findByMemberId(loginId);
        PushMap pushMap = new PushMap();
        pushMap.setUniquelyId(System.currentTimeMillis());
        pushMap.setPushType(Constant.PushType.JG);
        pushMap.setContent(content);
        pushMap.setTitle(Constant.JpushType.PushActivity);
        
        //JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushActivity, content, memberLogin.getRegistrationId());
        JPushService.getInstance().sendMessageToCustomer(Constant.JpushType.PushActivity, JSONObject.toJSONString(pushMap), memberPushId.getRegistrationId());
        pushMap.setPushType(Constant.PushType.GT);
        GeTuiPushUtil.getInstance().pushMsgToList(Constant.PushType.CH, Integer.toString(Constant.JpushType.PushActivity), JSONObject.toJSONString(pushMap), "推送通知", memberPushId.getCId());
        
        
    }
}
