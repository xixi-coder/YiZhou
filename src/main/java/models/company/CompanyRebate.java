package models.company;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseCompanyRebate;
import com.google.common.collect.Lists;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */

@TableBind(tableName = "dele_company_rebate")
public class CompanyRebate extends BaseCompanyRebate<CompanyRebate> {
    public static CompanyRebate dao = new CompanyRebate();

    public CompanyRebate findCountByStatusServiceType3(boolean superAdmin, int companyId, int serviceType,String start,String end) {
        if (superAdmin) {
            return findFirst(SqlManager.sql("order.find3"), serviceType,start,end);
        } else {
            return findFirst(StringsKit.replaceSql(SqlManager.sql("order.find3"), " AND company=?"),serviceType, start,end,companyId );
        }
    }


}
