package models.member;

import annotation.TableBind;
import base.models.BaseMemberRealLocation;
import plugin.sqlInXml.SqlManager;

/**
 * Created by BOGONj on 2016/9/2.
 */
@TableBind(tableName = "dele_member_real_location",pks = "id,member")
public class MemberRealLocation extends BaseMemberRealLocation<MemberRealLocation> {
    public static MemberRealLocation dao = new MemberRealLocation();

    /**
     * 通过会员id查询到当前位置的记录
     *
     * @param memberId
     * @return
     */
    public MemberRealLocation findByMember(int memberId) {
        return findFirst(SqlManager.sql("memberRealLocation.findByMember"), memberId);
    }
}
