package models.sys;

import annotation.TableBind;
import base.Constant;
import base.models.BaseServiceTypeItem;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/20.
 */
@TableBind(tableName = "dele_service_type_item")
public class ServiceTypeItem extends BaseServiceTypeItem<ServiceTypeItem> {
    public static ServiceTypeItem dao = new ServiceTypeItem();


    public List<ServiceTypeItem> findByType(int id) {
        return find(SqlManager.sql("serviceTypeItem.findByType"), id);
    }


    public List<ServiceTypeItem> findByAll() {
        return find(SqlManager.sql("serviceTypeItem.findByAll"));
    }
    
    public List<ServiceTypeItem> appFindByType(int id) {
        return find(SqlManager.sql("serviceTypeItem.appFindByType"), id);
    }

    public List<ServiceTypeItem> findZXType() {
        return find(SqlManager.sql("serviceTypeItem.findZXType"), Constant.ServiceType.HangKongZhuanXian,Constant.ServiceType.ZhuanXian);
    }

}
