package models.member;

import annotation.TableBind;
import base.models.BaseMemberAppraise;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_member_appraise")
public class MemberAppraise extends BaseMemberAppraise<MemberAppraise> {
    public static MemberAppraise dao = new MemberAppraise();

    /**
     * 根据订单号查询信息
     *
     * @param OrderId
     * @return
     */
    public MemberAppraise findOneByOrderId(String OrderId) {
        return findFirst("SELECT * FROM dele_member_appraise WHERE OrderId = ? ", OrderId);
    }


    /**
     * 交通部A52接口
     *
     * @return
     */
    public List<MemberAppraise> findDataByA52() {
        return find("SELECT o.no as oid,a.* FROM `dele_member_appraise` AS a LEFT JOIN  `dele_order` AS  o  ON(a.OrderId =o.id ) ");
    }

}
