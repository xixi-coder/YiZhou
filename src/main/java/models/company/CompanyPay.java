package models.company;

import annotation.TableBind;
import base.models.BaseCompanyPay;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/4/09.
 */
@TableBind(tableName = "dele_company_pay")
public class CompanyPay extends BaseCompanyPay<CompanyPay> {
    public static CompanyPay dao = new CompanyPay();

    /**
     * 网约车平台公司支付信息接口
     */
    public List<CompanyPay> companypay() {
        return find(SqlManager.sql("baseinfo.companypay"));
    }

}
