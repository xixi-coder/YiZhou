package models.company;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseAd;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/5.
 */
@TableBind(tableName = "dele_ad")
public class Ad extends BaseAd<Ad> {
    public static Ad dao = new Ad();
    /**
     * 通过公司查询广告
     *
     * @param id
     * @param appType
     * @return
     */
    public List<Ad> findByCompanyAndType(int id, int appType, int type) {
        return find(SqlManager.sql("ad.findByCompanyAndType"), id, appType, type);
    }

    /**
     * 通过类型查询默认广告
     *
     * @param appType
     * @return
     */
    public List<Ad> findByDefaultAndType(int appType, int type) {
        return find(SqlManager.sql("ad.findByDefaultAndType"), appType, type);
    }
}
