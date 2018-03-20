package models.member;

import annotation.TableBind;
import base.models.BaseMemberHistoryLocation;
import plugin.sqlInXml.SqlManager;

/**
 * Created by BOGONj on 2016/9/2.
 */
@TableBind(tableName = "dele_member_history_location")
public class MemberHistoryLocation extends BaseMemberHistoryLocation<MemberHistoryLocation> {
    public static MemberHistoryLocation dao = new MemberHistoryLocation();

    /**
     * 通过会员id查询到当前位置的记录
     *
     * @param memberId
     * @return
     */
    public MemberHistoryLocation findByMember(int memberId) {
        return findFirst(SqlManager.sql("memberRealLocation.findByMember"));
    }
}
