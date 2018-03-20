package models.sys;

import annotation.TableBind;
import base.models.BaseCallBack;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/12/15.
 */
@TableBind(tableName = "dele_call_back")
public class CallBack extends BaseCallBack<CallBack> {
    public static CallBack dao = new CallBack();

    /**
     * 根据loginId查询
     *
     * @param loginId
     * @return
     */

    public List<CallBack> findByLoginId(int loginId) {
        return find(SqlManager.sql("callBack.findByLoginId"), loginId);
    }
}
