package controllers.admin.sys;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.company.Company;
import models.company.CompanyService;
import models.royalty.RoyaltyStandard;
import models.sys.ChargeStandard;
import models.sys.ServiceType;
import models.sys.ServiceTypeItem;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/20.
 */
@Controller("/admin/sys/servicetype")
public class ServicetypeController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        renderJson(dataTable(SqlManager.sql("serviceTypeItem.column"), SqlManager.sql("serviceTypeItem.where")));
    }

    public void item() {
        int id = getParaToInt(0, 0);
        List<Company> companies = Company.dao.findByEnable();
        List<RoyaltyStandard> royaltyStandards;
        if (isSuperAdmin()) {
            royaltyStandards = RoyaltyStandard.dao.findAll();
        } else {
            royaltyStandards = RoyaltyStandard.dao.findByCompany(getCompanyId());
        }
        List<ChargeStandard> chargeStandards = ChargeStandard.dao.findAll();
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("company", companies);
        setAttr("royaltyStandards", royaltyStandards);
        setAttr("chargeStandards", chargeStandards);
        setAttr("serviceTypes", serviceTypes);
        ServiceTypeItem serviceTypeItem;
        if (id > 0) {
            serviceTypeItem = ServiceTypeItem.dao.findById(id);
            setAttr("serviceTypeItem", serviceTypeItem);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            serviceTypeItem = new ServiceTypeItem();
            setAttr("serviceTypeItem", serviceTypeItem);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void save() {
        ServiceTypeItem serviceTypeItem = getModel(ServiceTypeItem.class, "serviceTypeItem");
        if (serviceTypeItem.getId() != null) {
            serviceTypeItem.setLastUpdateTime(DateTime.now().toDate());
            if (serviceTypeItem.update()) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            serviceTypeItem.setCreateTime(DateTime.now().toDate());
            if (serviceTypeItem.save()) {
                renderAjaxSuccess("信息添加成功");
            } else {
                renderAjaxError("信息添加失败");
            }
        }
    }

    public void del() {
        int id = getParaToInt("id", 0);
        final ServiceTypeItem serviceTypeItem = ServiceTypeItem.dao.findById(id);
        final CompanyService companyService = CompanyService.dao.findByServiceType(id);
        boolean isOK = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return serviceTypeItem.delete() && companyService.delete();
            }
        });
        if (isOK) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void companyindex() {

    }

    /**
     * 分公司服务类型展示
     */
    public void listforcompany() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("serviceTypeItem.listforcompanycolumn"), SqlManager.sql("serviceTypeItem.listforcompanywhere")));
        } else {
            whereSql = " AND dc.id = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("serviceTypeItem.listforcompanycolumn"), SqlManager.sql("serviceTypeItem.listforcompanywhere"), whereSql, params));
        }
        return;
    }

    public void itemforcompany() {
        int id = getParaToInt(0, 0);
        CompanyService companyService;
        if (id > 0) {
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
            companyService = CompanyService.dao.findById(id);
        } else {
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
            companyService = new CompanyService();
        }
        setAttr("companyService", companyService);
        List<ServiceTypeItem> serviceTypeItems = ServiceTypeItem.dao.findByAll();
        setAttr("serviceTypeItems", serviceTypeItems);
        List<ChargeStandard> chargeStandards = ChargeStandard.dao.findAll();
        setAttr("chargeStandards", chargeStandards);
        List<RoyaltyStandard> royaltyStandards;
        if (isSuperAdmin()) {
            royaltyStandards = RoyaltyStandard.dao.findAll();
        } else {
            royaltyStandards = RoyaltyStandard.dao.findByCompany(getCompanyId());
        }
        setAttr("royaltyStandards", royaltyStandards);
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("company", companies);
        }
        render("companyitem.ftl");
    }

    public void companyservicesave() {
        CompanyService companyService = getModel(CompanyService.class, "companyService");
        boolean isOk;
        if (companyService.getId() == null) {
            companyService.setLastUpdateTime(DateTime.now().toDate());
            if (!isSuperAdmin()) {
                companyService.setCompany(getCompanyId());
            }
            isOk = companyService.save();
        } else {
            isOk = companyService.update();
        }
        if (isOk) {
            renderAjaxSuccess("保存成功！");
        } else {
            renderAjaxError("保存失败！");
        }
    }

    public void companyservicedel() {
        int id = getParaToInt("id", 0);
        CompanyService companyService = CompanyService.dao.findById(id);
        if (companyService.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }
}
