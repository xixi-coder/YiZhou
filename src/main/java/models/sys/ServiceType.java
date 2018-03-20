package models.sys;

import annotation.TableBind;
import base.models.BaseServiceType;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/20.
 */
@TableBind(tableName = "dele_service_type")
public class ServiceType extends BaseServiceType<ServiceType> {
    public static ServiceType dao = new ServiceType();
    
    public List<ServiceType> findAll() {
        return find(SqlManager.sql("serviceType.findAll"));
    }
    
    
    public List<ServiceType> getServiceType(int company) {
        return find(SqlManager.sql("serviceType.getServiceType"), company);
    }
    
    public List<ServiceType> findZxServiceType(int company) {
        return find(SqlManager.sql("serviceType.findZxServiceType"), company);
    }
    
    
    public List<ServiceType> findZxAll() {
        return find(SqlManager.sql("serviceType.findZxAll"));
    }
    
}
