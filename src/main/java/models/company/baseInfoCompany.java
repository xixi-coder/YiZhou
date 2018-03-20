package models.company;

import annotation.TableBind;
import base.models.BaseBaseinfoCompany;
import base.models.BaseInfoCompany;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19 0019.
 */
@TableBind(tableName = "dele_baseinfo_company")
public class baseInfoCompany extends BaseBaseinfoCompany<baseInfoCompany> {
    public static baseInfoCompany dao = new baseInfoCompany();

    /**
     *私人小客车合乘信息 服务平台基本信息
     * @return
     */
    public List<baseInfoCompany> findAll() {
        return find(SqlManager.sql("baseInfoCompany.findAll"));
    }



}
