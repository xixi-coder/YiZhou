package models.sys;

import annotation.TableBind;
import base.datatable.DataTablePage;
import base.models.BaseChargeStandard;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/8/25.
 */
@TableBind(tableName = "dele_charge_standard")
public class ChargeStandard extends BaseChargeStandard<ChargeStandard> {
    public static ChargeStandard dao = new ChargeStandard();

    /**
     * 通过公司id查询专车收费标准
     *
     * @param companyId
     * @param type
     * @return
     */
    public ChargeStandard findByCompanyAndType(int companyId, int type) {
        return findFirst(SqlManager.sql("chargeStandard.findByCompanyAndType"), companyId, type);
    }
    /**
     * 通过公司id查询专车收费标准
     *
     * @param companyId
     * @param type
     * @return
     */
    public List<ChargeStandard> findByCompanyAndServiceType(int companyId, int type) {
        return find(SqlManager.sql("chargeStandard.findByCompanyAndServiceType"), companyId, type);
    }

    /**
     * 查询所有的收费标准
     *
     * @return
     */
    public List<ChargeStandard> findAll() {
        return find(SqlManager.sql("chargeStandard.findAll"));
    }

    /**
     * 查询服务默认的收费标准
     *
     * @param type
     * @return
     */
    public ChargeStandard findDefaultByServiceType(int type) {
        return findFirst(SqlManager.sql("chargeStandard.findDefaultByServiceType"), type);
    }
}
