package models.driver;

import annotation.TableBind;
import base.Constant;
import base.models.BaseDriverRealLocation;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Sqls;
import kits.StringsKit;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/4.
 */
@TableBind(tableName = "dele_driver_real_location", pks = "id,driver")
public class DriverRealLocation extends BaseDriverRealLocation<DriverRealLocation> {
    public static DriverRealLocation dao = new DriverRealLocation();

    /**
     * 通过司机id查询司机的位置
     *
     * @param driverId
     * @return
     */
    public DriverRealLocation findByDriver(int driverId) {
        return findFirst(SqlManager.sql("driverRealLocation.findByDriver"), driverId);
    }

    /**
     * 查询司机实时的位置
     *
     * @param comapnyId
     * @param status
     * @return
     */
    public List<DriverRealLocation> findByCompanyAndStatus(int comapnyId, String[] status) {
        String sqlStr = SqlManager.sql("driverRealLocation.findByCompanyAndStatus");
        List<Object> params = Lists.newArrayList();
        if (comapnyId != 0) {
            sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company=? ");
            params.add(comapnyId);
        }
        if (status != null && status.length != 0) {
            String sql = "";
            for (String s : status) {
                if (!Strings.isNullOrEmpty(s)) {
                    sql += " OR dml.status=? ";
                    params.add(s);
                }
            }
            if (sql.length() > 3) {
                sql = sql.substring(3, sql.length());
                sqlStr = StringsKit.replaceSql(sqlStr, " AND (" + sql + ")");
            }
        } else {
            return null;
        }
        return find(sqlStr, params.toArray());
    }

    /**
     * 驾驶员定位信息接口
     */
    public List<DriverRealLocation> driver() {
        return find(SqlManager.sql("position.driver"));
    }

    /**
     * 车辆定位信息接口
     */
    public List<DriverRealLocation> vehicle() {
        return find(SqlManager.sql("position.vehicle"));
    }

}
