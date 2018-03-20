package models.company;

import annotation.TableBind;
import base.models.BaseCompanyService;
import models.sys.ServiceType;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by admin on 2016/9/22.
 */
@TableBind(tableName = "dele_company_service")
public class CompanyService extends BaseCompanyService<CompanyService> {
    public static CompanyService dao = new CompanyService();
    
    
    public CompanyService findByServiceType(int serviceTypeId) {
        return findFirst(SqlManager.sql("companyService.findByServiceType"), serviceTypeId);
    }
    
    /**
     * 获取公司的服务类型
     *
     * @param company
     * @param serviceItem
     * @return
     */
    public CompanyService findByCompanyAndServiceType(int company, int serviceItem) {
        return findFirst(SqlManager.sql("companyService.findByCompanyAndServiceType"), company, serviceItem);
    }
    
    /**
     * 网约车平台公司运价信息接口
     *
     * @param company
     * @param serviceItem
     * @return
     */
    public List<CompanyService> companyfare() {
        return find(SqlManager.sql("baseinfo.companyfare"));
    }
    
    
    public List<CompanyService> serviceTypeDisabled(int companyId, int typeId) {
        return find(SqlManager.sql("serviceType.serviceTypeDisabled"), companyId, typeId);
    }
    
    
}


