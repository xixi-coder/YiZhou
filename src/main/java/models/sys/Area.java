package models.sys;

import annotation.TableBind;
import base.models.BaseArea;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/1.
 */
@TableBind(tableName = "dele_area")
public class Area extends BaseArea<Area> {
    public static Area dao = new Area();

    /**
     * 按照父类查询
     *
     * @param level
     * @param parent
     * @return
     */
    public List<Area> findByLevelAndParent(String level, String parent) {
        return find(SqlManager.sql("area.findByLevelAndParent"), level, parent);
    }

    /**
     * 通过父级id查询
     *
     * @param parent
     * @return
     */
    public Area findByParent(String parent) {
        return findFirst(SqlManager.sql("area.findByParent"), parent);
    }

    /**
     * 通过adcode查询区域信息
     *
     * @param adCode
     * @return
     */
    public Area findByAdCode(String adCode) {
        return findFirst(SqlManager.sql("area.findByAdCode"), adCode);
    }

    /**
     * 通过Name查询adCode
     *
     * @param Name
     * @return
     */
    public Area findByName(String Name) {
        return findFirst(SqlManager.sql("area.findByName"), Name);
    }

    /**
     * 通过citycode查询区域信息
     *
     * @param cityCode
     * @return
     */
    public Area findByCityCode(String cityCode) {
        return findFirst(SqlManager.sql("area.findByCityCode"), cityCode);
    }
}
