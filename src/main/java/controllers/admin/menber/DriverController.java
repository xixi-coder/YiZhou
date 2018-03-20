package controllers.admin.menber;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.redis.Redis;
import kits.Md5Kit;
import kits.SmsKit;
import kits.StringsKit;
import kits.TimeKit;
import models.car.*;
import models.company.Company;
import models.company.CompanyAccount;
import models.company.CompanyActivity;
import models.driver.*;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberLogin;
import models.royalty.RoyaltyStandard;
import models.sys.Area;
import models.sys.ServiceType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2016/9/14.
 */
@Controller("/admin/member/driver")
public class DriverController extends BaseAdminController {
    public static Logger logger = Logger.getLogger(DriverController.class);

    public void index() {
        int count = 0;
        if (isSuperAdmin()) {
            count = DriverCar.dao.findCountNoAduit(isSuperAdmin(), getCompanyId(), 0);
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        } else {
            if (getUserType() == Constant.UserType.UNION) {
                count = DriverCar.dao.findCountNoAduit(isSuperAdmin(), getCompanyId(), getUserId());
            } else {
                count = DriverCar.dao.findCountNoAduit(isSuperAdmin(), getCompanyId(), 0);
            }
        }
        setAttr("count", count);

        render("list.ftl");
    }

    /**
     * 司机信息列表
     */
    public void list() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("driverInfo.column1"), SqlManager.sql("driverInfo.where1"), SqlManager.sql("driverInfo.columnPage"), SqlManager.sql("driverInfo.wherePage")));
        } else {
            if (getUserType() == Constant.UserType.NORMAL) {
                List<Object> params = Lists.newArrayList();
                String whereSql = StringsKit.replaceSql(SqlManager.sql("driverInfo.where1"), " AND info.company = ? ");
                String whereSqlPage = StringsKit.replaceSql(SqlManager.sql("driverInfo.wherePage"), " AND info.company = ? ");
                params.add(getCompanyId());
                renderJson(dataTable(SqlManager.sql("driverInfo.column1"), whereSql, params, SqlManager.sql("driverInfo.columnPage"), whereSqlPage));
            } else if (getUserType() == Constant.UserType.UNION) {
                List<Object> params = Lists.newArrayList();
                String whereSql = StringsKit.replaceSql(SqlManager.sql("driverInfo.where1"), " AND info.company = ? ");
                String whereSqlPage = StringsKit.replaceSql(SqlManager.sql("driverInfo.wherePage"), " AND info.company = ? ");

                params.add(getCompanyId());
                whereSql = StringsKit.replaceSql(whereSql, " AND info.creater = ? ");
                params.add(getUserId());
                renderJson(dataTable(SqlManager.sql("driverInfo.column1"), whereSql, params));
            }
        }
    }

    /**
     * 车辆品牌信息
     */
    public void carbrand() {

    }

    public void brandlist() {
        renderJson(dataTable(SqlManager.sql("carBrand.column"), SqlManager.sql("carBrand.where")));
    }

    /**
     * 车辆品牌管理
     */
    public void brandinfo() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            CarBrand carBrand = CarBrand.dao.findById(id);
            setAttr("carBrand", carBrand);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            CarBrand carBrand = new CarBrand();
            setAttr("carBrand", carBrand);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void savebrand() {
        CarBrand carBrand = getModel(CarBrand.class, "carBrand");
        carBrand.setFristPy(carBrand.getName());
        if (carBrand.getId() != null) {
            if (carBrand.update()) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            carBrand.setCreateTime(DateTime.now().toDate());
            if (carBrand.save()) {
                renderAjaxSuccess("车辆品牌添加成功！");
            } else {
                renderAjaxError("车辆品牌添加失败！");
            }
        }
    }

    public void delbrand() {
        int id = getParaToInt("id", 0);
        CarBrand carBrand = CarBrand.dao.findById(id);
        if (carBrand.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    /**
     * 车辆型号管理
     */
    public void carmodel() {
        int id = getParaToInt(0, 0);
        setAttr("id", id);
    }

    public void modellist() {
        int id = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable(SqlManager.sql("carModel.column"), SqlManager.sql("carModel.where"), params));
    }

    public void delmodel() {
        int id = getParaToInt("id", 0);
        CarModel carModel = CarModel.dao.findById(id);
        if (carModel.delete()) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    public void modelinfo() {
        int id = getParaToInt(0, 0);
        if (id > 0) {
            CarModel carModel = CarModel.dao.findById(id);
            setAttr("carModel", carModel);
            int id2 = getParaToInt(1, 0);
            setAttr("id", id2);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            CarModel carModel = new CarModel();
            setAttr("carModel", carModel);
            int id1 = getParaToInt("id", id);
            setAttr("id", id1);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    public void savemodel() {
        CarModel carModel = getModel(CarModel.class, "carModel");
        if (carModel.getId() != null) {
            if (carModel.update()) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            carModel.setCreateTime(DateTime.now().toDate());
            int id = getParaToInt("id", 0);
            CarBrand carBrand = CarBrand.dao.findById(id);
            carModel.setBrand(carBrand.getId());
            if (carModel.save()) {
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    /**
     * 司机证件信息
     */

    public void license() {
        int id = getParaToInt(0, 0);
        setAttr("id", id);
        DriverLicenseInfo driverLicenseInfo = DriverLicenseInfo.dao.findByDriver(id);
        DriverInfo driverInfo = DriverInfo.dao.findById(id);
        if (driverLicenseInfo != null) {
            setAttr("driverLicenseInfo", driverLicenseInfo);
            setAttr("driverInfo", driverInfo);
            int license = driverLicenseInfo.getId();
            AuditLog auditLog = AuditLog.dao.findByLicense(license);
            if (auditLog != null) {
                setAttr("auditFlag", true);
            } else {
                setAttr("auditFlag", false);
            }
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            DriverLicenseInfo driverLicenseInfo1 = new DriverLicenseInfo();
            DriverInfo driverInfo1 = new DriverInfo();
            setAttr("driverLicenseInfo", driverLicenseInfo1);
            setAttr("driverInfo", driverInfo1);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }

    }

    public void savelicense() {
        DriverLicenseInfo driverLicenseInfo = getModel(DriverLicenseInfo.class, "driverLicenseInfo");
        DriverInfo driverInfo = getModel(DriverInfo.class, "driverInfo");
        if (driverInfo.getId() == null) {
            driverInfo.save();
        } else {
            driverInfo.update();
        }

        if (driverLicenseInfo.getId() != null) {
            if (driverLicenseInfo.update()) {
                renderAjaxSuccess("图片修改成功！");
            } else {
                renderAjaxError("图片修改失败！");
            }
        } else {
            driverLicenseInfo.setCreateTime(DateTime.now().toDate());
            int id = getParaToInt(0, 0);
            driverLicenseInfo.setDriver(id);
            if (driverLicenseInfo.save()) {
                renderAjaxSuccess("图片添加成功！");
            } else {
                renderAjaxError("图片添加失败！");
            }
        }
    }

    /**
     * 司机证件审核
     */
    public void audit() {
        int id = getParaToInt("id", 0);
        if (id == 0) {
            renderAjaxError("请先上传证件！");
            return;
        }
        final DriverLicenseInfo driverLicenseInfo = DriverLicenseInfo.dao.findById(id);
        int driverId = driverLicenseInfo.getDriver();
        String remark = getPara("audit_remark");
        int okFlag = getParaToInt(0, 0);
        final DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        final AuditLog auditLog = new AuditLog();
        auditLog.setAuditer(getUserId());
        auditLog.setCompany(driverInfo.getCompany());
        auditLog.setAuditTime(DateTime.now().toDate());
        auditLog.setAuditRemark(remark);
        auditLog.setLicense(id);
        auditLog.setOkFlag(okFlag == 1 ? true : false);
        if (okFlag == 1) {
            driverInfo.setStatus(Constant.DataAuditStatus.AUDITOK);
            driverLicenseInfo.setStatus(Constant.DataAuditStatus.AUDITOK);
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            String salt = StringsKit.getSalt();
            memberLogin.setSalt(salt);
            String password = RandomStringUtils.randomNumeric(6);
            String md5Password = Md5Kit.MD5(password);
            String dbPassword = Md5Kit.MD5(md5Password + salt);
            memberLogin.setPassword(dbPassword);
            SmsKit.enroll(driverInfo.getRealName(), driverInfo.getPhone(), password);
            memberLogin.update();
        } else {
            driverInfo.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            driverLicenseInfo.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            SmsKit.audit(driverInfo.getPhone(), okFlag);
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return auditLog.save() && driverInfo.update() && driverLicenseInfo.update();
            }
        });
        if (isOk) {
            renderAjaxSuccess("审核完毕！");
        } else {
            renderAjaxSuccess("审核失败！");
        }
    }

    public void drivercar() {
        int id = getParaToInt(0, 0);
        setAttr("id", id);
        int action = getParaToInt("action", 0);
        setAttr("action", action);
    }

    /**
     * 司机车辆信息
     */
    public void carlist() {
        int id = getParaToInt("id", 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable(SqlManager.sql("driverInfo.column"), SqlManager.sql("driverInfo.where"), params));
    }

    /**
     * 车辆信息审核
     */
    public void caraudit() {
        int driverId = getParaToInt("id", 0);
        int status = getParaToInt(0, 0);
        int id = getParaToInt(1, 0);
        Car car = Car.dao.findById(id);
        if (car.getStatus() != null) {
            renderAjaxSuccess(driverId, "已经审核过,无需审核");
        } else {
            car.setStatus(status);
            if (car.update()) {
                renderAjaxSuccess(driverId, "审核完毕!");
            } else {
                renderAjaxError("审核失败!");
            }
        }

    }

    public void carinfo() {
        //根据司机注册类型显示添加车辆的可选类型
        int id1 = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(id1);
        String[] serviceTypes = driverInfo.getType().split(";");
        setAttr("serviceTypes", serviceTypes);

        int id = getParaToInt(0, 0);
        if (id > 0) {
            int id2 = getParaToInt(1, 0);
            setAttr("id", id2);
            CarInfo carInfo = CarInfo.dao.findByCar(id);
            Car car = Car.dao.findById(carInfo.getCarId());
            setAttr("carInfo", carInfo);
            setAttr("car", car);
            List<CarBrand> carBrands = CarBrand.dao.findAll();
            setAttr("carBrands", carBrands);
            if (car.getBrand() != null) {
                List<CarModel> models = CarModel.dao.findByBrand(car.getBrand());
                setAttr("models", models);
            }
            List<Area> province = Area.dao.findByLevelAndParent("province", 0 + "");
            setAttr("province", province);
            if (!Strings.isNullOrEmpty(carInfo.getProvince())) {
                List<Area> city = Area.dao.findByLevelAndParent("city", carInfo.getProvince());
                setAttr("city", city);
            }
            if (!Strings.isNullOrEmpty(carInfo.getCity())) {
                List<Area> county = Area.dao.findByLevelAndParent("district", carInfo.getCity());
                setAttr("county", county);
            }
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            setAttr("id", id1);
            CarInfo carInfo = new CarInfo();
            Car car = new Car();
            setAttr("carInfo", carInfo);
            setAttr("car", car);
            List<CarBrand> carBrands = CarBrand.dao.findAll();
            setAttr("carBrands", carBrands);
            List<Area> province = Area.dao.findByLevelAndParent("province", 0 + "");
            setAttr("province", province);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }
    }

    //车辆保险回显
    public void carInsurance() {
        String plateNo = getPara("plateNo");
        Insurance insurance = Insurance.dao.findByVno(plateNo);
        if (insurance == null) {
            insurance = new Insurance();
            insurance.setVehicleNo(plateNo);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        } else {
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        }
        setAttr("insurance", insurance);
    }

    //添加车辆保险
    public void insSaveOrUpdate() {
        final Insurance insurance = getModel(Insurance.class, "insurance");
        if (insurance.getId() != null) {
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return insurance.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            if (insurance.save()) {
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    public void savecar() {
        DriverCar driverCar = getModel(DriverCar.class, "driverCar");
        final CarInfo carInfo = getModel(CarInfo.class, "carInfo");
        final Car car = getModel(Car.class, "car");
        if (carInfo.getCarId() != null) {
            carInfo.setJoinTime(DateTime.now().toDate());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return carInfo.update() && car.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            car.setCreateTime(DateTime.now().toDate());
            if (car.save()) {
                carInfo.setCarId(car.getId());
                carInfo.save();
                driverCar.setCar(car.getId());
                int id = getParaToInt("id", 0);
                driverCar.setDriver(id);
                driverCar.save();
                renderAjaxSuccess("信息添加成功！");
            } else {
                renderAjaxError("信息添加失败！");
            }
        }
    }

    public void item() {
        //获取类型
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);

        int id = getParaToInt(0, 0);
        List<RoyaltyStandard> royaltyStandards;
        if (isSuperAdmin()) {
            royaltyStandards = RoyaltyStandard.dao.findAll();
        } else {
            royaltyStandards = RoyaltyStandard.dao.findByCompany(getCompanyId());
        }

        setAttr("roya", royaltyStandards);
        if (id > 0) {
            DriverInfo driverInfo = DriverInfo.dao.findById(id);
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            String type = driverInfo.getType();
            String[] types = type.split(";");
            List dtypes = Arrays.asList(types);
            setAttr("types", dtypes);
            setAttr("driverInfo", driverInfo);
            setAttr("memberLogin", memberLogin);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_EDIT);
        } else {
            DriverInfo driverInfo = new DriverInfo();
            MemberLogin memberLogin = new MemberLogin();
            List types = new ArrayList();
            setAttr("types", types);
            setAttr("driverInfo", driverInfo);
            setAttr("memberLogin", memberLogin);
            setAttr(Constant.ADMIN_ACTION, Constant.ADMIN_ACTION_ADD);
        }

    }

    /**
     * 删除司机车辆信息
     */
    public void delete() {
        int id = getParaToInt("id", 0);
        final CarInfo carInfo = CarInfo.dao.findByCar(id);
        final Car car = Car.dao.findById(carInfo.getCarId());
        final DriverCar driverCar = DriverCar.dao.findByCar(carInfo.getCarId());
        boolean isOK = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return carInfo.delete() && car.delete() && driverCar.delete();
            }
        });
        if (isOK) {
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    /**
     * 删除司机信息
     */

    public void del() {
        int id = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(id);
        int loginId = driverInfo.getLoginId();
        MemberLogin memberLogin = MemberLogin.dao.findById(loginId);
        if (driverInfo.delete() && memberLogin.delete()) {
//            String a = RandomStringUtils.randomAlphabetic();
            renderAjaxSuccess("删除成功！");
        } else {
            renderAjaxError("删除失败！");
        }
    }

    @Before(POST.class)
    public void save() {
        String[] type = getParaValues("c1");
        String drivertype = "";
        for (int i = 0; i < type.length; i++) {
            if (type.length - 1 == i) {
                drivertype += type[i];
            } else {
                drivertype += type[i] + ";";
            }
        }
        final DriverInfo driverInfo = getModel(DriverInfo.class, "driverInfo");
        driverInfo.setType(drivertype);
        if (!isSuperAdmin()) {
            driverInfo.setCompany(getCompanyId());
        }
        final MemberLogin memberLogin = getModel(MemberLogin.class, "memberLogin");
        if (driverInfo.getId() != null) {
            driverInfo.setLastUpdateTime(DateTime.now().toDate());
            driverInfo.setType(drivertype);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return driverInfo.update() && memberLogin.update();
                }
            });
            if (isOk) {
                renderAjaxSuccess("信息修改成功！");
            } else {
                renderAjaxError("信息修改失败！");
            }
        } else {
            driverInfo.setCreater(getUserId());
            String password = Md5Kit.MD5(getPara("password"));
            String salt = StringsKit.getSalt();
            memberLogin.setSalt(salt);
            String dbPassword = Md5Kit.MD5(password + salt);
            memberLogin.setPassword(dbPassword);
            memberLogin.setType(Constant.DRIVER);
            memberLogin.setCreateTime(DateTime.now().toDate());
            final MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
            memberCapitalAccount.setPhone(driverInfo.getPhone());
            memberCapitalAccount.setCreateTime(DateTime.now().toDate());
            memberCapitalAccount.setStatus(1);
            memberCapitalAccount.setAmount(BigDecimal.ZERO);
            memberCapitalAccount.setAccount(driverInfo.getPhone());
            memberCapitalAccount.setAmount(BigDecimal.ZERO);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (memberLogin.save()) {
                        driverInfo.setLoginId(memberLogin.getId());
                        memberCapitalAccount.setLoginId(memberLogin.getId());
                        return driverInfo.save() && memberCapitalAccount.save();
                    }
                    return false;
                }
            });
            if (isOk) {
                renderAjaxSuccess("司机添加成功！");
            } else {
                renderAjaxError("司机添加失败！");
            }
        }
    }

    /**
     * 验证车辆品牌是否存在
     */
    public void validBrand() {
        String name = getPara("carBrand.name");
        boolean isEdit = getParaToBoolean("action", false);
        CarBrand carBrand;
        Map<String, String> result = Maps.newHashMap();
        if (isEdit) {
            int id = getParaToInt("carBrand.id", 0);
            if (CarBrand.dao.findByCount(name, id) >= 1) {
                result.put("error", "车辆品牌已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            carBrand = CarBrand.dao.findByName(name);
            if (carBrand != null) {
                result.put("error", "车辆品牌已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);
    }

    /**
     * 验证车辆型号是否存在是否存在
     */
    public void validModel() {
        String name = getPara("carModel.name");
        boolean isEdit = getParaToBoolean("action", false);
        CarModel carModel;
        Map<String, String> result = Maps.newHashMap();
        if (isEdit) {
            int id = getParaToInt("carModel.id", 0);
            if (CarModel.dao.findByCount(name, id) >= 1) {
                result.put("error", "车辆型号已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            carModel = CarModel.dao.findByName(name);
            if (carModel != null) {
                result.put("error", "车辆型号已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);
    }

    /**
     * 验证用户名是否存在
     */
    public void validList() {
        String username = getPara("memberLogin.user_name");
        boolean isEdit = getParaToBoolean("action", false);
        MemberLogin memberLogin;
        Map<String, String> result = Maps.newHashMap();
        if (isEdit) {
            int userId = getParaToInt("memberLogin.id", 0);
            if (MemberLogin.dao.findByCount(username, userId, 1) >= 1) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        } else {
            memberLogin = MemberLogin.dao.findByUserName(username, 1);
            if (memberLogin != null) {
                result.put("error", "用户名已存在");
            } else {
                result.put("ok", "");
            }
        }
        renderJson(result);
    }

    /**
     * 修改用户密码
     */
    public void updatepd() {
        DriverInfo driverInfo = getModel(DriverInfo.class, "driverInfo");
        if (driverInfo.getId() != null) {
            String oldPw = Md5Kit.MD5(getPara("oldpassword"));
            driverInfo = DriverInfo.dao.findById(driverInfo.getId());
            int login_id = driverInfo.getLoginId();
            MemberLogin memberLogin = MemberLogin.dao.findById(login_id);
            if (StringUtils.equals(Md5Kit.MD5(oldPw + memberLogin.getSalt()), memberLogin.getPassword())) {
                String password = getPara("password");
                String repassword = getPara("repassword");
                if (StringUtils.equals(password, repassword)) {
                    String salt = StringsKit.getSalt();
                    String dbPassword = Md5Kit.MD5(Md5Kit.MD5(password) + salt);
                    memberLogin.setSalt(salt);
                    memberLogin.setPassword(dbPassword);
                    if (memberLogin.update()) {
                        renderAjaxSuccess("密码修改成功！");
                    } else {
                        renderAjaxError("密码修改失败！");
                    }
                } else {
                    renderAjaxError("两次输入的密码不一致！");
                }
            } else {
                renderAjaxError("旧密码不正确！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 重置密码
     */
    public void repw() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            DriverInfo driverInfo = DriverInfo.dao.findById(id);
            int login_id = driverInfo.getLoginId();
            MemberLogin memberLogin = MemberLogin.dao.findById(login_id);
            String salt = StringsKit.getSalt();
            String md5Pwd = Md5Kit.MD5("123123");
            String dbPw = Md5Kit.MD5(md5Pwd + salt);
            memberLogin.setPassword(dbPw);
            memberLogin.setSalt(salt);
            if (memberLogin.update()) {
                renderAjaxSuccess("重置密码成功！");
            } else {
                renderAjaxError("重置密码失败！");
            }
        } else {
            renderAjaxError("用户不存在！");
        }
    }

    /**
     * 账户流水
     */
    public void capital() {
        int id = getParaToInt(0, 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(id);
        int loginId = driverInfo.getLoginId();
        setAttr("id", loginId);
        setAttr("id1", id);
    }

    public void capitallist() {
        int id = getParaToInt(0, 0);
        List<Object> params = Lists.newArrayList();
        params.add(id);
        renderJson(dataTable("select *", "from dele_capital_log where login_id = ? AND operation_type!=51 AND status = 1 ORDER BY create_time DESC", params));
    }

    public void capitalpage() {
        int id = getParaToInt(0, 0);
        int id1 = getParaToInt(1, 0);
        setAttr("id", id1);
        CapitalLog capitalLog = CapitalLog.dao.findById(id);
        setAttr("capital", capitalLog);
    }

    /**
     * 司机列表导出
     */
    public void export() {
        String sql;
        List<Object> params = Lists.newArrayList();
        String[] header = {"姓名", "用户名", "家庭住址", "电话", "性别", "司机类型", "最后更新时间"};
        String[] column = {"real_name", "user_name", "address", "phone", "gender", "type", "last_login_time"};
        if (isSuperAdmin()) {
            sql = SqlManager.sql("driverInfo.column1") + SqlManager.sql("driverInfo.where1");
            poiRender(sql, header, column, "司机列表", "driver.xls");
        } else {
            params.add(getCompanyId());
            sql = SqlManager.sql("driverInfo.column1") + StringsKit.replaceSql(SqlManager.sql("driverInfo.where1"), "and info.company=?");
            poiRender(sql, params, header, column, "司机列表", "driver.xls");
        }
    }

    /**
     * 司机充值
     */
    public void recharge() {
        int id = getParaToInt(0, 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(id);
        Date now = DateTime.now().toDate();
        BigDecimal amount = new BigDecimal(getPara("amount", "0"));
        final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(driverInfo.getLoginId());
        BigDecimal historyAmount = memberCapitalAccount.getAmount();
        memberCapitalAccount.setAmount(historyAmount.add(amount));
        memberCapitalAccount.setLastUpdateAmount(amount);
        memberCapitalAccount.setLastUpdateTime(now);
        final CapitalLog capitalLog = new CapitalLog();
        String remark = getPara("remark");
        capitalLog.setLoginId(driverInfo.getLoginId());
        capitalLog.setAmount(amount);
        capitalLog.setRemark(remark);
        capitalLog.setOperater(getUserId());
        capitalLog.setCreateTime(DateTime.now().toDate());
        capitalLog.setStatus(Constant.CapitalStatus.OK);
        capitalLog.setOperationType(Constant.CapitalOperationType.CZOFHT);
        String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(4);
        capitalLog.setNo(no);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return capitalLog.save() && memberCapitalAccount.update();
            }
        });
        if (isOk) {
            SmsKit.rechange(driverInfo.getPhone(), driverInfo.getRealName(), memberCapitalAccount.getAccount(), amount.toString(), driverInfo.getCompany());
            renderAjaxSuccess("充值成功！");
        } else {
            renderAjaxError("充值失败！");
        }
    }

    public void frozen() {
        int driverId = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        String unitType = getPara("unitType", "");
        int counts = getParaToInt("counts", 0);
        String remark = getPara("remark");
        DateTime start = DateTime.now();
        DateTime end = DateTime.now();
        int frozenType = getParaToInt("frozenType", 0);
        if (unitType.equals("天")) {
            end = start.plusDays(counts);
        } else if (unitType.equals("小时")) {
            end = start.plusHours(counts);
        }
        boolean isOk = false;
        if (driverInfo != null) {
            String message;
            if (driverInfo.getStatus() == Constant.DriverStatus.FROZEN) {
                isOk = DriverInfo.dao.setNoFrozen(driverId);
                message = "解冻成功！";
            } else {
                isOk = driverInfo.setForen(driverId);
                if (isOk) {
                    FrozenLog frozenLog = new FrozenLog();
                    frozenLog.setRemark(remark);
                    frozenLog.setLoginId(driverInfo.getLoginId());
                    frozenLog.setCreateTime(DateTime.now().toDate());
                    frozenLog.setCreater(getUserId());
                    frozenLog.setEndTime(end.toDate());
                    frozenLog.setStartTime(start.toDate());
                    if (frozenType == 1) {
                        frozenLog.setStatus(0);
                    } else {
                        frozenLog.setStatus(1);
                    }
                    frozenLog.save();
                }
                message = "冻结成功！";
            }
            if (isOk) {
                renderAjaxSuccess(message);
            } else {
                renderAjaxError("操作失败");
            }
        } else {
            renderAjaxError("服务人员不存在！");
        }
    }

    public void offline() {
        int driverId = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        if (driverInfo != null) {
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
            if (Redis.use(Constant.LOGINMEMBER_CACHE).del(memberLogin.getCacheKey()) >= 0 && memberLogin.update()) {
                renderAjaxSuccess("下线成功！");
            } else {
                renderAjaxError("下线失败！");
            }
        } else {
            renderAjaxError("服务人员不存在！");
        }
    }

    public void resetDevice() {
        int driverId = getParaToInt("id", 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        if (driverInfo != null) {
            MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            memberLogin.setDeviceNo(null);
            memberLogin.setDeviceType(null);
            if (memberLogin.update()) {
                renderAjaxSuccess("重置设备成功！");
            } else {
                renderAjaxError("重置设备失败！");
            }
        } else {
            renderAjaxError("服务人员不存在！");
        }
    }

    public void aduitCar() {
        render("aduitcar.ftl");
    }

    public void aduitCarList() {
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("driverCar.carListColumn"), SqlManager.sql("driverCar.carListWhere")));
        } else {
            String where = StringsKit.replaceSql(SqlManager.sql("driverCar.carListWhere"), " AND dc.id = ? ");
            List<Object> param = Lists.newArrayList();
            param.add(getCompanyId());
            if (getUserType() == Constant.UserType.UNION) {
                where = StringsKit.replaceSql(where, " AND ddi.creater = ? ");
                param.add(getUserId());
            }
            renderJson(dataTable(SqlManager.sql("driverCar.carListColumn"), where, param));
        }
    }

    public void drivermap() {
        if (isSuperAdmin()) {
            List<Company> companies = Company.dao.findByEnable();
            setAttr("companies", companies);
        }
    }

    public void driverInfo() {
        int id = getParaToInt("id", 0);
        if (id != 0) {
            DriverInfo driverInfo = DriverInfo.dao.findById(id);
            DriverLicenseInfo driverLicenseInfo = DriverLicenseInfo.dao.findByDriver(id);
            driverInfo.put("driverLicenseInfo", driverLicenseInfo);
            Date startOfMonth = DateTime.now().dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
            Date endOfMonth = DateTime.now().dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
            Date startOfDay = DateTime.now().millisOfDay().withMinimumValue().toDate();
            Date endOfDay = DateTime.now().millisOfDay().withMaximumValue().toDate();
            Long monthCount = Db.queryLong(SqlManager.sql("order.findCountByDriverAndDate"), id, Constant.OrderStatus.PAYED, startOfMonth, endOfMonth);
            Long dayCount = Db.queryLong(SqlManager.sql("order.findCountByDriverAndDate"), id, Constant.OrderStatus.PAYED, startOfDay, endOfDay);
            driverInfo.put("monthCount", monthCount);
            driverInfo.put("dayCount", dayCount);
            Company company = Company.dao.findByCompanyId(driverInfo.getCompany());
            driverInfo.put("companyInfo", company);
            DriverRealLocation driverRealLocation = DriverRealLocation.dao.findByDriver(id);
            driverInfo.put("locationDate", new DateTime(driverRealLocation.getReciveTime()).toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss")));
            renderAjaxSuccess(driverInfo);
        }
    }

    public void resetCompany() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            DriverInfo driverInfo = DriverInfo.dao.findById(id);
            driverInfo.setCompany(null);
            driverInfo.setLastUpdateTime(DateTime.now().toDate());
            if (driverInfo.update()) {
                renderAjaxSuccess("操作成功！");
            } else {
                renderAjaxError("操作失败！");
            }
        } else {
            renderAjaxError("司机不存在");
        }
    }

    public void reward() {
        List<ServiceType> serviceTypes = ServiceType.dao.findAll();
        setAttr("serviceTypes", serviceTypes);
    }

    public void screen() {
        String driverInfo = getPara("driverInfo");
        List<Object> params = Lists.newArrayList();
        String whereSql = "";
        int company = getParaToInt("company", 0);
        if (isSuperAdmin() && company != 0) {
            whereSql += " AND company = ? ";
            params.add(company);
        }
        if (!isSuperAdmin()) {
            whereSql += " AND company = ? ";
            params.add(getCompanyId());
        }
        int minMoney = getParaToInt("minMoney", 0);
        if (minMoney != 0) {
            whereSql += " AND amount >= ? ";
            params.add(minMoney);
        }
        String stringStart = getPara("startDate");
        String stringEnd = getPara("endDate");
        if (!Strings.isNullOrEmpty(stringStart) && !Strings.isNullOrEmpty(stringEnd)) {
            whereSql += " AND create_time BETWEEN ? AND ? ";
            DateTime startDate = DateTime.parse(stringStart, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            DateTime endDate = DateTime.parse(stringEnd, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            params.add(startDate.toDate());
            params.add(endDate.toDate());
        }
        int orderType = getParaToInt("orderType", 0);
        if (orderType != 0) {
            whereSql += " AND type = ? ";
            params.add(orderType);
        }
        String sql = StringsKit.replaceSql(SqlManager.sql("driverInfo.rewardForDriver"), whereSql);
        int orderCount = getParaToInt("orderCount", 0);
        if (orderCount != 0) {
            sql += " AND oo.c >= ? ";
            params.add(orderCount);
        }
        List<DriverInfo> driverInfos = DriverInfo.dao.find(sql, params.toArray());
        renderJson(driverInfos);
    }

    public void rewardSave() {
        String driverInfos = getPara("driverInfo");
        JSONArray jsonArray = JSON.parseArray(driverInfos);
        final List<MemberCapitalAccount> capitalAccounts = Lists.newArrayList();
        final List<CapitalLog> capitalLogs = Lists.newArrayList();
        for (Object o : jsonArray) {
            BigDecimal realReward = BigDecimal.ZERO;
            JSONObject driverInfo = (JSONObject) o;
            MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(driverInfo.getInteger("id"));
            BigDecimal totalMoney = driverInfo.getBigDecimal("money");
            int rewardType = driverInfo.getInteger("rewardType");
            BigDecimal eachMoney = driverInfo.getBigDecimal("rewardMuch");
            BigDecimal rebate = driverInfo.getBigDecimal("rebate");
            if (rewardType == 1) {
                realReward = eachMoney;
            } else {
                realReward = totalMoney.multiply(rebate).divide(BigDecimal.valueOf(100), 2);
            }
            BigDecimal histrory = memberCapitalAccount.getAmount();
            memberCapitalAccount.setLastUpdateAmount(realReward);
            memberCapitalAccount.setAmount(histrory.add(realReward));
            memberCapitalAccount.setLastUpdateTime(DateTime.now().toDate());
            CapitalLog capitalLog = new CapitalLog();
            capitalLog.setNo(StringsKit.getCaptDriver());
            capitalLog.setStatus(Constant.CapitalStatus.OK);
            capitalLog.setOperationType(Constant.CapitalOperationType.REWARD);
            capitalLog.setLoginId(memberCapitalAccount.getLoginId());
            capitalLog.setRemark("后台奖励给司机");
            capitalLog.setCreateTime(DateTime.now().toDate());
            capitalLog.setAmount(realReward);
            capitalLog.setOrderId(0);
            DriverInfo dd = DriverInfo.dao.findByLoginId(driverInfo.getInteger("id"));
            CompanyAccount companyAccount = CompanyAccount.dao.findById(dd.getCompany());
            if (CompanyAccount.dao.amountEnough(companyAccount, realReward, Constant.CompanyAccountActivity.awardDriver)) {
                CompanyActivity companyActivity = CompanyActivity.dao.creat(Constant.CompanyAcitivyType.SIJIJIANGLI, realReward, dd.getCompany(), dd.getLoginId(), Constant.DataAuditStatus.AUDITOK, 0, "后台给司机奖励余额");
                companyActivity.save();
                capitalLogs.add(capitalLog);
                capitalAccounts.add(memberCapitalAccount);
            } else {
                renderAjaxError("公司活动金额不足");
                return;
            }

        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                for (MemberCapitalAccount capitalAccount : capitalAccounts) {
                    if (!capitalAccount.update()) {
                        return false;
                    }
                }
                for (CapitalLog capitalLog : capitalLogs) {
                    if (!capitalLog.save()) {
                        return false;
                    }
                }
                return true;
            }
        });
        if (isOk) {
            renderAjaxSuccess("操作成功！");
        } else {
            renderAjaxError("操作失败！");
        }
    }

    public void rechange() {
        render("prechange.ftl");
    }

    public void rescreen() {
        int company = getParaToInt("company", 0);
        String realName = getPara("realName");
        String phone = getPara("phone");
        int lessthan = getParaToInt("lessthan", 0);
        String startStr = getPara("startTime");
        String endStr = getPara("endTime");
        String[] serviceType = getParaValues("serviceType");
        String[] status = getParaValues("status");
        String whereSql = "";
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            if (company != 0) {
                whereSql += " AND ddi.company = ? ";
                params.add(company);
            }
        } else {
            whereSql += " AND ddi.company = ?";
            params.add(getCompanyId());
        }
        if (!Strings.isNullOrEmpty(realName)) {
            whereSql += " AND ddi.real_name = ?";
            params.add(realName);
        }
        if (!Strings.isNullOrEmpty(phone)) {
            whereSql += " AND ddi.phone = ?";
            params.add(phone);
        }
        if (!Strings.isNullOrEmpty(startStr)) {
            DateTime startTime = DateTime.parse(startStr, DateTimeFormat.forPattern(TimeKit.YYYYMMDD)).millisOfDay().withMinimumValue();
            DateTime endTime;
            if (!Strings.isNullOrEmpty(endStr)) {
                endTime = DateTime.parse(endStr, DateTimeFormat.forPattern(TimeKit.YYYYMMDD)).millisOfDay().withMaximumValue();
                whereSql += " AND dml.create_time BETWEEN ? AND ? ";
                params.add(startTime.toDate());
                params.add(endTime.toDate());
            } else {
                endTime = DateTime.now().millisOfDay().withMaximumValue();
                whereSql += " AND dml.create_time BETWEEN ? AND ? ";
                params.add(startTime.toDate());
                params.add(endTime.toDate());
            }
        }
        if (lessthan != 0) {
            whereSql += " AND dmca.amount <= ?";
            params.add(lessthan);
        }
        if (serviceType != null && serviceType.length > 0) {
            String sqlServiceType = " AND (";
            for (String s : serviceType) {
                sqlServiceType += " ddi.type LIKE ? OR";
                params.add("%" + s + "%");
            }
            sqlServiceType = sqlServiceType.substring(0, sqlServiceType.length() - 3) + ")";
            whereSql += sqlServiceType;
        }
        if (status != null && status.length > 0) {
            String sqlStatus = " AND (";
            for (String statu : status) {
                if (statu.equals("5")) {
                    sqlStatus += " ddi.status = ? OR ";
                    params.add(Constant.DriverStatus.FROZEN);
                } else if (statu.equals("-1")) {
                    sqlStatus += " dml.status = ? OR dml.status = ? OR dml.status = ? OR ";
                    params.add(4);
                    params.add(5);
                    params.add(6);
                } else {
                    sqlStatus += " dml.status =? OR ";
                    params.add(statu);
                }
            }
            sqlStatus = sqlStatus.substring(0, sqlStatus.length() - 3) + ")";
            whereSql += sqlStatus;
        }
        List<DriverInfo> driverInfos = DriverInfo.dao.find(StringsKit.replaceSql(SqlManager.sql("driverInfo.rechangeScreen"), whereSql), params.toArray());
        renderJson(driverInfos);
    }

    public void rechangeSave() {
        String[] loginIdses = getPara("loginIds").split(";");
        final List<MemberCapitalAccount> m = Lists.newArrayList();
        BigDecimal amount = new BigDecimal(getPara("money", "0"));
        final List<CapitalLog> cps = Lists.newArrayList();
        for (String s : loginIdses) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(Ints.tryParse(s));
            Date now = DateTime.now().toDate();

            final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(driverInfo.getLoginId());
            BigDecimal historyAmount = memberCapitalAccount.getAmount();
            memberCapitalAccount.setAmount(historyAmount.add(amount));
            memberCapitalAccount.setLastUpdateAmount(amount);
            memberCapitalAccount.setLastUpdateTime(now);
            m.add(memberCapitalAccount);
            final CapitalLog capitalLog = new CapitalLog();
            String remark = getPara("remark");
            capitalLog.setLoginId(driverInfo.getLoginId());
            capitalLog.setAmount(amount);
            capitalLog.setRemark(remark);
            capitalLog.setOperater(getUserId());
            capitalLog.setCreateTime(DateTime.now().toDate());
            capitalLog.setStatus(Constant.CapitalStatus.OK);
            capitalLog.setOperationType(Constant.CapitalOperationType.CZOFHT);
            String no = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(4);
            capitalLog.setNo(no);
            cps.add(capitalLog);
        }


        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                for (MemberCapitalAccount memberCapitalAccount : m) {
                    if (!memberCapitalAccount.update()) {
                        return false;
                    }
                }
                for (CapitalLog cp : cps) {
                    if (!cp.save()) {
                        return false;
                    }
                }
                return true;
            }
        });
        if (isOk) {
            for (MemberCapitalAccount memberCapitalAccount : m) {
                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberCapitalAccount.getLoginId());
                SmsKit.rechange(driverInfo.getPhone(), driverInfo.getRealName(), memberCapitalAccount.getAccount(), amount.toString(), driverInfo.getCompany());
            }

            renderAjaxSuccess("充值成功！");
        } else {
            renderAjaxError("充值失败！");
        }
    }

    public void weipaidandingdan() {
        List<Object> params = Lists.newArrayList();
        params.add(Constant.OrderStatus.CREATE);

    }

    public void delinst() {
        int id = getParaToInt("id", 0);
        if (id > 0) {
            DriverInfo driverInfo = DriverInfo.dao.findById(id);
            driverInfo.setIntroducerLoginId(null);
            if (driverInfo.update()) {
                renderAjaxSuccess("操作成功!");
            } else {
                renderAjaxError("操作失败!");
            }
        } else {
            renderAjaxError("司机不存在!");
        }
    }

    /**
     * 审核顺风车
     */
    public void shenHe() {
        int id = getParaToInt("id", 0);
        int status = getParaToInt(0, 0);
        DriverInfo driverInfo = DriverInfo.dao.findById(id);
        if (status == 1) {
            driverInfo.setType(driverInfo.getType() + ";" + 5);
            driverInfo.setLevel(null);
            driverInfo.update();
        } else {
            driverInfo.setLevel(null);
            driverInfo.update();
        }
        renderAjaxSuccess("审核完毕！");

    }
}
