package models.sys;

import annotation.TableBind;
import base.models.BaseBank;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
@TableBind(tableName = "dele_bank")
public class Bank extends BaseBank<Bank> {
    public static Bank dao = new Bank();

    /**
     * @param pageStart
     * @param pageSize
     * @param bankName
     * @return
     */
    public List<Bank> findByPage(int pageStart, int pageSize, String bankName, String bankBin) {
        List<Object> params = Lists.newArrayList();
        String sql = SqlManager.sql("bank.findByPage");
        if (!Strings.isNullOrEmpty(bankName)) {
            sql = StringsKit.replaceSql(sql, " AND bank_name LIKE ? ");
            params.add("%" + bankName + "%");
        }
        if(bankBin.length() >6){
            bankBin=bankBin.substring(0,6);
        }
        if (!Strings.isNullOrEmpty(bankBin) && bankBin.length() == 6) {
            sql = StringsKit.replaceSql(sql, " AND bank_bin = ? ");
            params.add(bankBin);
        }
        params.add(pageStart);
        params.add(pageSize);
        return find(sql, params.toArray());
    }
}
