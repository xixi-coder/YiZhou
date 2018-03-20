package models.company;

import annotation.TableBind;
import base.models.BaseDistributionSetting;
import plugin.sqlInXml.SqlManager;

/**
 * Created by admin on 2016/10/7.
 */
@TableBind(tableName = "dele_distribution_setting")
public class Distribution extends BaseDistributionSetting<Distribution> {
    public static Distribution dao = new Distribution();
    /**
     * 通过公司和分销对象查询分销设置
     * @param company
     * @param style
     * @return
     */
    public Distribution findByCompanyAndStyle(int company, int style,int serviceType) {
        return findFirst(SqlManager.sql("distribution.findByCompanyAndStyle"),company,style,serviceType);
    }
}
