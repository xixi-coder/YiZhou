package models.royalty;

import annotation.TableBind;
import base.models.BaseRoyaltyStandardMoneyItem;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/12.
 */
@TableBind(tableName = "dele_royalty_standard_money_item")
public class RoyaltyStandardMoneyItem extends BaseRoyaltyStandardMoneyItem<RoyaltyStandardMoneyItem> {
    public static RoyaltyStandardMoneyItem dao = new RoyaltyStandardMoneyItem();

    /**
     * 通过金额区间id查询详细区间
     *
     * @param id
     * @return
     */
    public List<RoyaltyStandardMoneyItem> findByMoney(Integer id) {
        return find(SqlManager.sql("royaltyStandardMoneyItem.findByMoney"), id);
    }

    /**
     * 查询符合该金额的提成想
     *
     * @param id
     * @param amount
     * @return
     */
    public List<RoyaltyStandardMoneyItem> findByMoneyAndAmount(Integer id, BigDecimal amount) {
        return find(SqlManager.sql("royaltyStandardMoneyItem.findByMoneyAndAmount"), id, amount, amount);
    }
}
