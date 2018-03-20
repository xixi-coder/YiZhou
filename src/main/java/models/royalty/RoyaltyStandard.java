package models.royalty;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseRoyaltyStandard;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/12.
 */
@TableBind(tableName = "dele_royalty_standard")
public class RoyaltyStandard extends BaseRoyaltyStandard<RoyaltyStandard> {
    public static RoyaltyStandard dao = new RoyaltyStandard();


    public List<RoyaltyStandard> findAll() {
        return find(SqlManager.sql("royaltyStandard.findAll"));
    }

    public List<RoyaltyStandard> findByCompany(int companyId) {
        return find(SqlManager.sql("royaltyStandard.findByCompany"), companyId);
    }
}
