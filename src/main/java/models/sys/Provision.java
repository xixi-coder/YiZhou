package models.sys;

import annotation.TableBind;
import base.models.BaseProvision;
import plugin.sqlInXml.SqlManager;

/**
 * Created by admin on 2016/11/18.
 */
@TableBind(tableName = "dele_provision")
public class Provision extends BaseProvision<Provision> {
    public static Provision dao = new Provision();

    public Provision findByAppTypeAndPType(int appType, int provisionType) {
        return findFirst(SqlManager.sql("provision.findByAppTypeAndPType"), appType, provisionType);
    }
}
