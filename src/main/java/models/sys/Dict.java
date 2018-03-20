package models.sys;

import annotation.TableBind;
import base.models.BaseDict;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/26.
 */
@TableBind(tableName = "dele_dict")
public class Dict extends BaseDict<Dict> {
    public static Dict dao = new Dict();

    /**
     * 通过类型查询
     *
     * @param type
     * @return
     */
    public List<Dict> findByType(String type) {
        return find(SqlManager.sql("dict.findByType"), type);
    }
}
