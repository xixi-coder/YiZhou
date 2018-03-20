package models.count;

import annotation.TableBind;
import base.models.BaseMemberOrderStatistics;
import plugin.sqlInXml.SqlManager;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
@TableBind(tableName = "member_order_statistics")
public class MemberOrderStatistics extends BaseMemberOrderStatistics<MemberOrderStatistics> {
    public static final MemberOrderStatistics dao = new MemberOrderStatistics();
    
    public MemberOrderStatistics findByMemberId(int memberId) {
        return findFirst(SqlManager.sql("memberOrderStatistics.findByMemberId"),memberId);
    }
}
