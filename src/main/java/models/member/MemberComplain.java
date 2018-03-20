package models.member;

import annotation.TableBind;
import base.models.BaseMemberComplain;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_member_complain")
public class MemberComplain extends BaseMemberComplain<MemberComplain> {
    public static MemberComplain dao = new MemberComplain();

    /**
     * 乘客投诉信息接口
     *
     * @param
     * @return
     */
    public List<MemberComplain> passengercomplaint() {
        return find(SqlManager.sql("rated.passengercomplaint"));
    }

    /**
     * 根据订单号查询信息
     *
     * @param OrderId
     * @return
     */
    public MemberComplain findOneByOrderId(String OrderId) {
        return findFirst("SELECT * FROM dele_member_complain WHERE OrderId = ?", OrderId);
    }

    /**
     * 交通部接口数据添加A46表
     *
     * @return
     */
    public List<MemberComplain> findDataAddA54() {
        return find("SELECT * FROM dele_member_complain ");
    }
}
