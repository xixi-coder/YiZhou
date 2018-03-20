package models.activity;

import annotation.TableBind;
import base.models.BaseMemberActivity;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

/**
 * Created by admin on 2016/12/15.
 */
@TableBind(tableName = "dele_member_activity")
public class MemberActivity extends BaseMemberActivity<MemberActivity> {
    public static MemberActivity dao = new MemberActivity();

    /**
     * 获取是否参加活动
     *
     * @param loginId
     * @param activityId
     * @return true没有参数
     */
    public boolean isJoin(int loginId, int activityId) {
        return findFirst(SqlManager.sql("memberActivity.memberJoinTimes"), activityId, loginId).getNumber("c").intValue() < 1;
    }

    /**
     * 构建会员活动
     *
     * @param loginId
     * @param appType
     * @param activityId
     * @return
     */
    public MemberActivity create(int loginId, int appType, int activityId) {
        MemberActivity memberActivity = new MemberActivity();
        memberActivity.setAppType(appType);
        memberActivity.setActivityId(activityId);
        memberActivity.setLoginId(loginId);
        memberActivity.setCreateTime(DateTime.now().toDate());
        return memberActivity;
    }
}
