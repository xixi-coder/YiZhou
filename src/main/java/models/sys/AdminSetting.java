package models.sys;

import annotation.TableBind;
import base.Constant;
import base.models.BaseAdminSetting;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/10/10.
 */
@TableBind(tableName = "dele_admin_setting")
public class AdminSetting extends BaseAdminSetting<AdminSetting> {
    public static AdminSetting dao = new AdminSetting();

    public AdminSetting findFirst() {
        return findFirst("select * from dele_admin_setting where companyId = 0");
    }

    public AdminSetting findByCompanyId(int companyId) {
        AdminSetting adminSetting = findFirst("select * from dele_admin_setting where companyId = ?",companyId);
        if (adminSetting == null){
            adminSetting = findFirst();
        }
        return adminSetting;
    }

    public AdminSetting findByCompanyIdBack(int companyId) {
        AdminSetting adminSetting = findFirst("select * from dele_admin_setting where companyId = ?",companyId);
        return adminSetting;
    }

    public BigDecimal finInsuranceByServiceType(int serviceType,int companyId) {
        BigDecimal insurance;
        AdminSetting adminSetting = findByCompanyId(companyId);
        if (adminSetting == null){
            adminSetting = findFirst();
        }
        switch (serviceType) {
            case Constant.ServiceItemType.DaiJia:
                insurance = adminSetting.getDjInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getDjInsuranceAmount();
                break;
            case Constant.ServiceItemType.KuaiChe:
                insurance = adminSetting.getKcInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getKcInsuranceAmount();
                break;
            case Constant.ServiceItemType.Taxi:
                insurance = adminSetting.getCzInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getCzInsuranceAmount();
                break;
            case Constant.ServiceItemType.ShangWu:
                insurance = adminSetting.getSwInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getSwInsuranceAmount();
                break;
            case Constant.ServiceItemType.HaoHua:
                insurance = adminSetting.getHhInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getHhInsuranceAmount();
                break;
            case Constant.ServiceItemType.ShuShi:
                insurance = adminSetting.getSsInsuranceAmount() == null ? BigDecimal.ZERO : adminSetting.getSsInsuranceAmount();
                break;
            default:
                insurance = BigDecimal.ZERO;
                break;
        }
        return insurance;
    }

}
