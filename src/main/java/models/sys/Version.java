package models.sys;

import annotation.TableBind;
import base.datatable.DataTablePage;
import base.models.BaseVersion;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/10/4.
 */
@TableBind(tableName = "dele_version")
public class Version extends BaseVersion<Version> {
    public static Version dao = new Version();

    /**
     * 查找最新的版本号
     *
     * @return
     */
    public Version findMaxVersion(int type, int osType) {
        return findFirst(SqlManager.sql("version.findMaxVersion"), type, osType);
    }

    /**
     * 根据版本号查找相应的版本信息
     *
     * @return
     */
    public Version findVersionByNo(int type, int osType,String version) {
        return findFirst(SqlManager.sql("version.findVersionByNo"), type, osType,version);
    }
}