package models.company;

import annotation.TableBind;
import base.models.BaseCompanyPermit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/4/09.
 */
@TableBind(tableName = "dele_company_permit")
public class CompanyPermit extends BaseCompanyPermit<CompanyPermit> {
    public static CompanyPermit dao = new CompanyPermit();

    /**
     * 网约车平台公司经营许可接口定义
     */
    public List<CompanyPermit> companypermit() {
        return find(SqlManager.sql("baseinfo.companypermit"));
    }

}
