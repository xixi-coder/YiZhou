package models.royalty;

import annotation.TableBind;
import base.models.BaseRoyaltyStandardMoney;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/12.
 */
@TableBind(tableName = "dele_royalty_standard_money")
public class RoyaltyStandardMoney extends BaseRoyaltyStandardMoney<RoyaltyStandardMoney> {
    public static RoyaltyStandardMoney dao = new RoyaltyStandardMoney();

    /**
     * 通过提成id查询金额区间详情
     *
     * @param id
     * @return
     */
    public List<RoyaltyStandardMoney> findByRoyaltyStandard(Integer id) {
        return find(SqlManager.sql("royaltyStandardMoney.findByRoyaltyStandard"), id);
    }
}
