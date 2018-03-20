package models.company;

import annotation.TableBind;
import base.models.BaseCompanyServe;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/4/09.
 */
@TableBind(tableName = "dele_company_serve")
public class CompanyServe extends BaseCompanyServe<CompanyServe> {
    public static CompanyServe dao = new CompanyServe();

    /**
     * 网约车平台公司服务机构接口
     */
    public List<CompanyServe> companyservice() {
        return find(SqlManager.sql("baseinfo.companyservice"));
    }

}
