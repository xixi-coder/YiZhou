package models.driver;

import annotation.TableBind;
import base.models.BaseWithdrawalsLog;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/12/21.
 */
@TableBind(tableName = "dele_withdrawals_log")
public class Withdrawals extends BaseWithdrawalsLog<Withdrawals> {
    public static Withdrawals dao = new Withdrawals();

    /**
     * 分页查询记录
     *
     * @param id
     * @param pageStart
     * @param pageSize
     * @return
     */
    public List<Withdrawals> findByLoginIdAndPage(Integer id, int pageStart, int pageSize) {
        return find(SqlManager.sql("withdrawalsLog.findByLoginIdAndPage"), id, pageStart, pageSize);
    }
}
