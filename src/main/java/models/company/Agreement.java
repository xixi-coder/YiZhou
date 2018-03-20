package models.company;

import annotation.TableBind;
import base.models.BaseServiceAgreement;
import plugin.sqlInXml.SqlManager;

/**
 * Created by admin on 2016/10/12.
 */
@TableBind(tableName = "dele_service_agreement")
public class Agreement extends BaseServiceAgreement<Agreement> {
    public static Agreement dao = new Agreement();

    /**
     * 查询协议信息
     * @param company
     * @param sytle
     * @return
     */
    public Agreement findByCompanyType(int company, int sytle) {
        return findFirst(SqlManager.sql("agreement.findByCompanyType"),company,sytle);
    }
}
