package models.driver;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.redis.Redis;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import annotation.TableBind;
import base.Constant;
import base.models.BaseDriverInfo;
import kits.StringsKit;
import models.member.MemberLogin;
import models.order.Order;
import plugin.sqlInXml.SqlManager;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_driver_info")
public class DriverInfo extends BaseDriverInfo<DriverInfo> {
    public static DriverInfo dao = new DriverInfo();

    /**
     * 根据司机id查询车辆信息
     */
    public List<DriverInfo> findCarById(int id) {
        return find(SqlManager.sql("driverInfo.findCarById"), id);
    }

    /**
     * 根据司机id查询可上线车辆信息
     */
    public List<DriverInfo> findCarById(int id,Integer status) {
        return find(SqlManager.sql("driverInfo.findCarById1"), id,status);
    }

    /**
     * 查询手机号码是否存在
     *
     * @param phone
     * @return
     */
    public int findCountByPhone(String phone) {
        return findFirst(SqlManager.sql("driverInfo.findCountByPhone"), phone).getNumber("c").intValue();
    }

    /**
     * 通过登陆id查询司机信息
     *
     * @param memberLoginId
     * @return
     */
    public DriverInfo findByLoginId(int memberLoginId) {
        return findFirst(SqlManager.sql("driverInfo.findByLoginId"), memberLoginId);
    }

    /**
     * 查询范围内的车辆,查询车辆信息
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public List<DriverInfo> findByLocation(String latitude, String longitude, int type, int serviceType) {
        List<Object> params = Lists.newArrayList();
        params.add(latitude);
        params.add(longitude);
        params.add(Constant.LoginStatus.RECIVEDORDER);
        params.add(Constant.DataAuditStatus.AUDITOK);
        String whereSql = SqlManager.sql("driverInfo.findByLocation");
        if (type == Constant.ServiceType.DaiJia) {
            whereSql = SqlManager.sql("driverInfo.findByLocationForDj");
            params.add(serviceType);
            params.add(0);
            params.add(10);
            return find(whereSql, params.toArray());
        }
        whereSql = StringsKit.replaceSql(whereSql, " AND bc.status = ? ");
        params.add(Constant.DataAuditStatus.AUDITOK);
        params.add(serviceType);
        params.add(0);
        params.add(10);
        return find(whereSql, params.toArray());
    }

    /**
     * 查询范围内的代驾司机
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
//    public List<DriverInfo> findByLocationForDj(String a, String b, String c, String d) {
//        return find(SqlManager.sql("driverInfo.findByLocationForDj"), 1, "%" + Constant.ServiceType.DaiJia + "%", a, b, c, d);
//    }

    /**
     * 通过距离查询司机信息
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public List<DriverInfo> findByDistance(int type, boolean pdFlag, String latitude, String longitude, int companyId, Double start, Double end) {
        if (type == Constant.ServiceType.DaiJia) {
            return find(SqlManager.sql("driverInfo.findByDistanceForDaiJia"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, companyId, type, start, end);
        } else {
            if (pdFlag) {
                List<DriverInfo> record = find(SqlManager.sql("driverInfo.findByDistance"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, companyId, type, start, end);
                List<DriverInfo> records = find(SqlManager.sql("driverInfo.findByDistanceForPd"), latitude, longitude, Constant.LoginStatus.RECIVEDORDERPD, Constant.DriverStatus.ShengHeTongGuo, companyId, type, 0, 1);
                record.addAll(records);
                return record;
            } else {
                return find(SqlManager.sql("driverInfo.findByDistance"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, companyId, type, start, end);
            }
        }
    }

    /**
     * 通过距离查询司机信息
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public List<DriverInfo> findByDistance(int type, boolean pdFlag, String latitude, String longitude, Double start, Double end) {
        if (type == Constant.ServiceType.DaiJia) {
            return find(SqlManager.sql("driverInfo.findByDistanceForDaiJiaNoLimit"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type, start, end);
        } else {
            if (type == Constant.ServiceItemType.KuaChengZhuanXian){
                type = Constant.ServiceItemType.KuaChengZhuanXianShangXian ;//如果是专线，则推送给memberLogin 中的service_type为59（城际专线上线类型）
            }
            if (type == Constant.ServiceItemType.HangKongZhuanXianJieJI || Constant.ServiceItemType.HangKongZhuanXianSongJi == type){
                type = Constant.ServiceItemType.HangKongZhuanXianShangXian;//如果是专线，则推送给memberLogin 中的service_type为58（航空专线上线类型）
            }
            if (pdFlag) {
                List<DriverInfo> driverInfos = find(SqlManager.sql("driverInfo.findByDistanceNoLimit"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type, start, end);
                List<DriverInfo> driverInfoList = find(SqlManager.sql("driverInfo.findByDistanceForPdNoLimit"), latitude, longitude, Constant.LoginStatus.RECIVEDORDERPD, Constant.DriverStatus.ShengHeTongGuo, type, 0, 1);
                driverInfoList.addAll(driverInfos);
                return driverInfoList;
            } else {
                return find(SqlManager.sql("driverInfo.findByDistanceNoLimit"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type, start, end);
            }
        }
    }

    /**
     * 查询专线司机
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public List<DriverInfo> findByZxLine(Order order, String latitude, String longitude) {
        int type = order.getType();
        if (type == Constant.ServiceItemType.KuaChengZhuanXian){
            type = Constant.ServiceItemType.KuaChengZhuanXianShangXian ;//如果是专线，则推送给memberLogin 中的service_type为59（城际专线上线类型）
            if (order.getPdFlag()) {
                List<DriverInfo> ZxLinedriverInfos = find(SqlManager.sql("driverInfo.ZxLinedriverInfos"), latitude, longitude, Constant.LoginStatus.RECIVEDORDERPD, Constant.DriverStatus.ShengHeTongGuo, type,order.getZxLine(),Constant.OrderStatus.ACCEPT,Constant.OrderStatus.START);
                List<DriverInfo> driverInfos = find(SqlManager.sql("driverInfo.findByZxLine"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type);
                if (ZxLinedriverInfos.size() > 0){
                    driverInfos.addAll(0,ZxLinedriverInfos);
                }
                return driverInfos;
            } else {
                return find(SqlManager.sql("driverInfo.findByZxLine"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type);
            }
        }
        else  {
            type = Constant.ServiceItemType.HangKongZhuanXianShangXian;//如果是专线，则推送给memberLogin 中的service_type为58（航空专线上线类型）
            return find(SqlManager.sql("driverInfo.findByZxLine"), latitude, longitude, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, type);
        }
    }

    /**
     * 通过ids查询司机
     *
     * @param params
     * @return
     */
    public List<DriverInfo> findByIds(List<Object> params) {
        String sqlWhere = Strings.repeat("?,", params.size());
        sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 1);
        sqlWhere = " AND ddi.id in (" + sqlWhere + ") ";
        return find(StringsKit.replaceSql(SqlManager.sql("driverInfo.findByIds"), sqlWhere), params.toArray());
    }

    /**
     * 通过手机号查询司机信息
     *
     * @param phone
     * @return
     */
    public DriverInfo findByPhone(String phone) {
        return findFirst(SqlManager.sql("driverInfo.findByPhone"), phone);
    }


    /**
     * 查询我的推荐人
     *
     * @param id
     * @return
     */
    public List<DriverInfo> findByTj(Integer id) {
        return find(SqlManager.sql("driverInfo.findByTj"), id);
    }

    /**
     * 设置冻结
     *
     * @param driverId
     */
    public boolean setForen(int driverId) {
        final DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        driverInfo.setStatus(Constant.DriverStatus.FROZEN);
        final MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
        memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
        memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
        String cacheKey = memberLogin.getCacheKey() == null ? "" : memberLogin.getCacheKey();
        Redis.use(Constant.USECAR_ID_CACHE).del(Constant.CARSTR + driverInfo.getLoginId());
        Redis.use(Constant.LOGINMEMBER_CACHE).del(cacheKey);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return driverInfo.update() && memberLogin.update();
            }
        });
        return isOk;
    }

    /**
     * 解除冻结
     *
     * @param driverId
     * @return
     */
    public boolean setNoFrozen(final int driverId) {
        final Date now = DateTime.now().toDate();
        final DriverInfo driverInfo = DriverInfo.dao.findById(driverId);
        if (driverInfo.getStatus() == Constant.DriverStatus.FROZEN) {
            driverInfo.setStatus(Constant.DriverStatus.ShengHeTongGuo);
            final MemberLogin memberLogin = MemberLogin.dao.findById(driverInfo.getLoginId());
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
            memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return driverInfo.update()
                            && memberLogin.update()
                            && Db.update(SqlManager.sql("frozenLog.updateStatusByDriverAndDate"), 1, driverInfo.getLoginId(), now) >= 0;
                }
            });
            return isOk;
        } else {
            return true;
        }

    }

    /**
     * 查询这个公司在线的司机
     *
     * @param companyId
     * @param logined
     * @return
     */
    public List<DriverInfo> findByCompanyAndLogined(int companyId, int logined) {
        return find(SqlManager.sql("driverInfo.findByCompanyAndLogined"), companyId, logined);
    }

    /**
     * 查询附近的司机
     *
     * @param lon
     * @param lat
     * @param id
     * @return
     */
    public DriverInfo findByCountByLocationAndCompany(String lon, String lat, Integer id, int type) {
        int distance = 4;
        return findFirst(SqlManager.sql("driverInfo.findByCountByLocationAndCompany"), lat, lon, Constant.LoginStatus.RECIVEDORDER, Constant.DriverStatus.ShengHeTongGuo, id, "%" + type + "%", 0, distance);
    }

    /**
     * 查询公司的司机
     *
     * @param userId
     * @param companyId
     * @param name
     * @return
     */
    public List<DriverInfo> findByCreaterAndCompanyForZtree(int userId, int companyId, String name) {
        List<Object> params = Lists.newArrayList();
        params.add(userId);
        params.add(companyId);
        String sqlStr = SqlManager.sql("driverInfo.findByCreaterAndCompanyForZtree");
        if (!Strings.isNullOrEmpty(name)) {
            name = "%" + name + "%";
            params.add(name);
            params.add(name);
            params.add(name);
            sqlStr = sqlStr + "AND (ddi.real_name LIKE ? OR ddi.nick_name LIKE ? OR ddi.phone LIKE ?)";
        }
        return find(sqlStr, params.toArray());
    }

    public int getRoya(int serviceType, DriverInfo driverInfo) {
        int result = 0;
        switch (serviceType) {
            case Constant.ServiceItemType.DaiJia:
                result = driverInfo.getDjRe();
                break;
            case Constant.ServiceItemType.KuaiChe:
                result = driverInfo.getKcRe();
                break;
            case Constant.ServiceItemType.ShangWu:
                result = driverInfo.getZcShangwuRe();
                break;
            case Constant.ServiceItemType.HaoHua:
                result = driverInfo.getZcHaohuaRe();
                break;
            case Constant.ServiceItemType.ShuShi:
                result = driverInfo.getZcSushiRe();
                break;
            case Constant.ServiceItemType.Taxi:
                result = driverInfo.getTexiRe();
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }

    public Integer getDjRe() {
        if (super.getDjRe() == null) {
            return 0;
        } else {
            return super.getDjRe();
        }
    }

    public Integer getKcRe() {
        if (super.getKcRe() == null) {
            return 0;
        } else {
            return super.getKcRe();
        }
    }

    public Integer getZcShangwuRe() {
        if (super.getZcShangwuRe() == null) {
            return 0;
        } else {
            return super.getZcShangwuRe();
        }
    }

    public Integer getZcHaohuaRe() {
        if (super.getZcHaohuaRe() == null) {
            return 0;
        } else {
            return super.getZcHaohuaRe();
        }
    }

    public Integer getZcSushiRe() {
        if (super.getZcSushiRe() == null) {
            return 0;
        } else {
            return super.getZcSushiRe();
        }
    }

    public Integer getTexiRe() {
        if (super.getTexiRe() == null) {
            return 0;
        } else {
            return super.getTexiRe();
        }
    }

    public List<DriverInfo> findByCompany(int companyId, int status) {
        return find(SqlManager.sql("driverInfo.findByCompany"), companyId, status);
    }

    /**
     * 查询上线或者是忙碌的司机
     *
     * @return
     */
    public List<DriverInfo> findForOnLine() {
        return find(SqlManager.sql("driverInfo.findForOnLine"), Constant.LoginStatus.LOGOUTED, Constant.LoginStatus.LOGINED);
    }


    //通过订单查询出该专线所有司机
    public List<DriverInfo> getDriver(Order order) {
        List<DriverInfo> driverInfos = new ArrayList<DriverInfo>();
        if (order.getPdFlag()) {
            driverInfos = find(SqlManager.sql("driverInfo.findAllDriver"), order.getId(), Constant.LineStatusByDriver.HASPASSAGES);
            driverInfos.addAll(find(SqlManager.sql("driverInfo.findAllDriver1"), order.getId(), Constant.LineStatusByDriver.NOPASSAGES, Constant.LineStatus.ENABLE));
            int i = 0;
            Iterator<DriverInfo> iterators = driverInfos.iterator();
            while (iterators.hasNext()) {
                DriverInfo driverInfo = iterators.next();
                DriverInfo driverInfo1 = findFirst(SqlManager.sql("driverInfo.findAllDriver3"), driverInfo.getId(), Constant.OrderStatus.ACCEPT, Constant.ServiceType.ZhuanXian);
                if (driverInfo1 != null) {
                    if (driverInfo1.getId() == driverInfo.getId()) {
                        iterators.remove();
                    }
                }
            }
            return driverInfos;
        } else {
            return find(SqlManager.sql("driverInfo.findAllDriver2"), order.getId(), Constant.LineStatusByDriver.NOPASSAGES);
        }
    }

    public boolean useFlag(int driverId) {
        if (findFirst("SELECT * FROM dele_driver_info d\n" +
                "LEFT JOIN dele_driver_car c ON d.id = c.driver\n" +
                "WHERE d.id = ? AND c.use_flag =1", driverId) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询所有匹配的城际顺风车行程司机
     */
    public List<DriverInfo> findChenJiDriver(String sCityCode, String eCityCode) {
        return find(SqlManager.sql("traver.findDriver"), sCityCode, eCityCode);
    }

    /**
     * 网约车平台公司运营规模信息接口
     */
    public DriverInfo findDriverAndCar() {
        return findFirst(SqlManager.sql("baseinfo.companyStat"));
    }

    /**
     * 驾驶员基本信息接口定义
     */
    public List<DriverInfo> driver() {
        return find(SqlManager.sql("baseinfo.driver"));
    }

    /**
     * 网约车驾驶员统计接口
     */
    public List<DriverInfo> driverstat() {
        return find(SqlManager.sql("baseinfo.driverstat"));
    }


    //增加驾驶员信息接口
    public List<DriverInfo> driverInfos() {
        List<DriverInfo> list;
        list = find(SqlManager.sql("baseinfo.findDriverInfos"));
        return list;
    }


    public List<DriverInfo> driverInfoPhone() {
        List<DriverInfo> list;
        list = find(SqlManager.sql("baseinfo.driverInfoPhone"));
        return list;
    }

    /**
     * byId
     *
     * @param driverId
     * @return
     */
    public DriverInfo findDataById(int driverId) {
        return findFirst(SqlManager.sql("baseinfo.findDataById"), driverId);
    }


    /**
     * 广告项目,获取司机信息
     *
     * @param phone
     * @return
     */
    public List<DriverInfo> adProjectGetPhone(String phone) {
        return find(SqlManager.sql("driverInfo.adProjectGetPhone"),phone);
    }

    //驾驶员信誉信息新增
    public List<DriverInfo> ratedDriver() {
        return find(SqlManager.sql("baseinfo.ratedDriver"));
    }

    //驾驶员处罚
    public List PunishDriver() {
        return find(SqlManager.sql("baseinfo.PunishDriver"));
    }
}
