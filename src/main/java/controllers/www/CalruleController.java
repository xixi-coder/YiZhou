package controllers.www;


import base.Constant;
import com.google.common.base.Strings;
import com.jfinal.core.Controller;
import models.company.Company;
import models.sys.ChargeStandard;
import models.sys.ServiceTypeItem;

import java.util.List;

/**
 * Created by admin on 2016/11/3.
 */
@annotation.Controller("/www/calrule")
public class CalruleController extends Controller {

    /**
     * 获取收费标准
     */
    public void index() {
        int serviceType = getParaToInt("serviceType", 1);
        String cityCode = getPara("cityCode");
        String companyId = getPara("companyId");
        Company company;
        if (cityCode != null || !Strings.isNullOrEmpty(cityCode)) {
            company = Company.dao.findByCity(cityCode);
        } else {
            company = Company.dao.findByCompanyId(Integer.valueOf(companyId));
        }
        if (company == null) {
            renderText("没有收费明细");
            return;
        }
        List<ChargeStandard> chargeStandards;
        if (serviceType == Constant.ServiceType.ZhuanXian) {
            chargeStandards = ChargeStandard.dao.findByCompanyAndServiceType(company.getId(), serviceType);
        } else {
            List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByType(serviceType);
            chargeStandards = ChargeStandard.dao.findByCompanyAndServiceType(company.getId(), serviceType);
            if (chargeStandards == null || chargeStandards.size() == 0) {
                for (ServiceTypeItem serviceTypeItem : serviceTypeItems) {
                    chargeStandards.add(ChargeStandard.dao.findById(serviceTypeItem.getChargeStandard()));
                }
            }
        }
        setAttr("chargeStandard", chargeStandards);
    }
}
