package models.company;

import annotation.TableBind;
import base.models.BaseCompanyAccountLog;

/**
 * Created by admin on 2016/10/24.
 */
@TableBind(tableName = "dele_company_account_log")
public class CompanyAccountLog extends BaseCompanyAccountLog<CompanyAccountLog> {
    public static CompanyAccountLog dao = new CompanyAccountLog();
}
