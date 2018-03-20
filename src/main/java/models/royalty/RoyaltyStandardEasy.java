package models.royalty;

import annotation.TableBind;
import base.models.BaseRoyaltyStandardEasy;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/12.
 */
@TableBind(tableName = "dele_royalty_standard_easy")
public class RoyaltyStandardEasy extends BaseRoyaltyStandardEasy<RoyaltyStandardEasy> {
    public static RoyaltyStandardEasy dao = new RoyaltyStandardEasy();

    /**
     * 通过提成id查询简单模式信息
     *
     * @param id
     * @return
     */
    public List<RoyaltyStandardEasy> findByRoyaltyStandard(Integer id) {
        return find(SqlManager.sql("royaltyStandardEasy.findByRoyaltyStandard"), id);
    }

}
