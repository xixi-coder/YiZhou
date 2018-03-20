package models.sys;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseSmsTmp;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_sms_tmp")
public class SmsTmp extends BaseSmsTmp<SmsTmp> {
    public static SmsTmp dao = new SmsTmp();

    /**
     * 通过类型查询短信模板
     *
     * @param smsType
     * @return
     */
    public SmsTmp findByType(int smsType) {
        return findFirst(SqlManager.sql("smstmp.findByType"), smsType);
    }

    public SmsTmp findByType(int smsType, int compnay) {

//        return findFirst(SqlManager.sql("smstmp.findByType").replace(Constant.SqlStrings.WHERE, " AND company = ? "), smsType, compnay);
        return findFirst(SqlManager.sql("smstmp.findByType"), smsType);
    }

}
