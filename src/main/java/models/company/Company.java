package models.company;

import annotation.TableBind;
import base.datatable.DataTablePage;
import base.models.BaseCompany;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import models.sys.Area;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/25.
 */
@TableBind(tableName = "dele_company", pks = "pk_id")
public class Company extends BaseCompany<Company> {
    public static Company dao = new Company();
    
    public List<Company> findByEnable() {
        return find(SqlManager.sql("company.findByEnable"));
    }
    
    /**
     * 通过城市查询公司
     *
     * @param cityCode
     * @return
     */
    public Company findByCity(String cityCode) {
        Company company = findFirst(SqlManager.sql("company.findByCity"), cityCode, cityCode);
        if (company == null) {
            Area area = Area.dao.findByAdCode(cityCode);
            if (area == null) {
                return null;
            }
            company = findFirst(SqlManager.sql("company.findByCity"), area.getAdcode(), area.getAdcode());
            if (company == null) {
                area = Area.dao.findByAdCode(area.getParent());
                if (area == null) {
                    return null;
                }
                company = findFirst(SqlManager.sql("company.findByCity"), area.getAdcode(), area.getAdcode());
                if (company == null) {
                    area = Area.dao.findByAdCode(area.getParent());
                    if (area == null) {
                        return null;
                    }
                    company = findFirst(SqlManager.sql("company.findByProvince"), area.getAdcode(), area.getAdcode());
                }
            }
        }
//        if (company == null) {//直接用总公司
//            company = Company.dao.findById(1);
//        }
        return company;
    }
    
    /**
     * 通过城市查询公司
     *
     * @param cityCode
     * @return
     */
    public Company findCompanyByCity(String cityCode) {
        Company company = findFirst(SqlManager.sql("company.findByCity"), cityCode, cityCode);
        if (company == null) {
            Area area = Area.dao.findByAdCode(cityCode);
            if (area == null) {
                return null;
            }
            company = findFirst(SqlManager.sql("company.findByCity"), area.getAdcode(), area.getAdcode());
            if (company == null) {
                area = Area.dao.findByAdCode(area.getParent());
                if (area == null) {
                    return null;
                }
                company = findFirst(SqlManager.sql("company.findByProvince"), area.getAdcode(), area.getAdcode());
                
            }
        }
        return company;
    }
    
    
    /**
     * 查询公司信息
     *
     * @param province
     * @param city
     * @param county
     * @return
     */
//    public Company findByArea(String province, String city, String county) {
//        String sql = SqlManager.sql("company.findByArea");
//        List<Object> params = Lists.newArrayList();
//        params.add(province);
//        params.add(city);
//        if (Strings.isNullOrEmpty(county)) {
//            sql += " WHERE province = ? AND city = ? LIMIT 1";
//        } else {
//            sql += " WHERE province = ? AND city = ? AND county = ? LIMIT 1";
//            params.add(county);
//        }
//        return findFirst(sql, params.toArray());
//    }
    
    /**
     * 通过用户查询所属公司信息
     *
     * @param userId
     * @return
     */
    public Company findByUser(int userId) {
        return findFirst(SqlManager.sql("company.findByUser"), userId);
    }
    
    /**
     * 查询公司通过id
     *
     * @param id
     * @return
     */
    public Company findByCompanyId(int id) {
        return findFirst(SqlManager.sql("company.findByCompanyId"), id);
    }
    
    /**
     * 查询所有信息
     *
     * @param companyId
     * @return
     */
    public List<Company> findAllByCompanyId(int companyId) {
        return find(SqlManager.sql("company.findAllByCompanyId"), companyId);
    }
    
    /**
     * 查询最大id
     *
     * @return
     */
    public int findMaxId() {
        return findFirst(SqlManager.sql("company.findMaxId")).getId();
    }
    
    /**
     * 查询所有公司
     *
     * @return
     */
    public List<Company> findAllCompany() {
        return find(SqlManager.sql("company.findAllCompany"));
    }
    
    
    /**
     * 删除公司根据id
     *
     * @param companyId
     * @return
     */
    public int deleByCompanyId(int companyId) {
        return Db.update("delete from dele_company where id = ? ", companyId);
    }
}
