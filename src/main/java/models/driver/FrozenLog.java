package models.driver;

import annotation.TableBind;
import base.models.BaseFrozenLog;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/12/19.
 */
@TableBind(tableName = "dele_frozen_log")
public class FrozenLog extends BaseFrozenLog<FrozenLog> {
    public static FrozenLog dao = new FrozenLog();

    /**
     * 查询未处理的冻结记录
     *
     * @param id
     * @param start
     * @param end
     * @return
     */
    public List<FrozenLog> findByDateAndStatus(int id, Date start, Date end) {
        return find(SqlManager.sql("frozenLog.findByDateAndStatus"),id,start,end);
    }
}
