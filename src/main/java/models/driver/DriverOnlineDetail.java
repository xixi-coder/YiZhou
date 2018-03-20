package models.driver;

import annotation.TableBind;
import base.Constant;
import base.models.BaseDriverOnlineDetail;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import kits.StringsKit;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/10/13.
 */
@TableBind(tableName = "dele_driver_online_detail")
public class DriverOnlineDetail extends BaseDriverOnlineDetail<DriverOnlineDetail> {
    public static DriverOnlineDetail dao = new DriverOnlineDetail();

    /**
     * 创建在线对象
     *
     * @param driverId
     * @param time
     * @param autoFlag
     * @param operatType
     * @return
     */
    public DriverOnlineDetail create(int driverId, int time, boolean autoFlag, int operatType, Date now) {
        DriverOnlineDetail driverOnlineDetail = new DriverOnlineDetail();

        if (time >= 1439){
            driverOnlineDetail.setTime(0);
        }else {
            driverOnlineDetail.setTime(time);
        }
        driverOnlineDetail.setDate(now);
        driverOnlineDetail.setDriver(driverId);
        driverOnlineDetail.setOperatTime(now);
        driverOnlineDetail.setAutoFlag(autoFlag);
        driverOnlineDetail.setOperatType(operatType);
        return driverOnlineDetail;
    }

    public DriverOnlineDetail findCurrentByDriverAndType(int driverInfoId, int online) {
        return findFirst(SqlManager.sql("driverOnline.findCurrentByDriverAndType"), driverInfoId, online);
    }

    public int findTotalTimeByDriverAndDate(int driverInfoId, Date start, Date end) {
        return findFirst(SqlManager.sql("driverOnline.findTotalTimeByDriverAndDate"), driverInfoId, start, end).getNumber("total").intValue();
    }

    /**
     * 通过当月的开始日期和结束日期查询所在公司的司机在线情况
     *
     * @param startOfMonth
     * @param endOfMonth
     * @param superAdmin
     * @param companyId
     * @return
     */
    public List<DriverOnlineDetail> findByMonthForDetail(DateTime startOfMonth, DateTime endOfMonth, boolean superAdmin, int companyId, int start, int pageSize, String userName, int userId) {
        String sqlStr;
        sqlStr = SqlManager.sql("driverOnline.findByMonthForDetail");
        List<Object> params = Lists.newArrayList();
        params.add(startOfMonth.toDate());
        params.add(endOfMonth.toDate());
        if (superAdmin) {
            if (!Strings.isNullOrEmpty(userName)) {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.real_name LIKE ? ");
                params.add("%" + userName + "%");
            }
        } else {
            if (Strings.isNullOrEmpty(userName)) {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company = ? ");
                params.add(companyId);
            } else {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company = ? AND ddi.real_name LIKE ?");
                params.add(companyId);
                params.add("%" + userName + "%");
            }
        }
        if (userId != 0) {
            sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.creater = ? ");
            params.add(userId);
        }
        params.add(start);
        params.add(pageSize);
        return find(sqlStr, params.toArray());
    }

    /**
     * 通过当月的开始日期和结束日期查询所在公司的司机在线情况
     *
     * @param startOfMonth
     * @param endOfMonth
     * @param superAdmin
     * @param companyId
     * @param start
     * @param pageSize
     * @param userName
     * @return
     */
    public List<DriverOnlineDetail> findByMonthForDetail(DateTime startOfMonth, DateTime endOfMonth, boolean superAdmin, int companyId, int start, int pageSize, String userName) {
        return findByMonthForDetail(startOfMonth, endOfMonth, superAdmin, companyId, start, pageSize, userName, 0);
    }


    /**
     * 查询数量
     *
     * @param startOfMonth
     * @param endOfMonth
     * @param superAdmin
     * @param companyId
     * @return
     */
    public int findCountByMonthForDetail(DateTime startOfMonth, DateTime endOfMonth, boolean superAdmin, int companyId, String userName, int userId) {
        DriverOnlineDetail d;
        String sqlStr = SqlManager.sql("driverOnline.findCountByMonthForDetail");
        List<Object> params = Lists.newArrayList();
        params.add(startOfMonth.toDate());
        params.add(endOfMonth.toDate());
        if (superAdmin) {
            if (!Strings.isNullOrEmpty(userName)) {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.real_name LIKE ? ");
                params.add("%" + userName + "%");
            }
        } else {
            if (Strings.isNullOrEmpty(userName)) {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company = ? ");
                params.add(companyId);
            } else {
                sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.company = ? AND ddi.real_name LIKE ? ");
                params.add(companyId);
                params.add("%" + userName + "%");
            }
        }
        if (userId != 0) {
            sqlStr = StringsKit.replaceSql(sqlStr, " AND ddi.creater = ? ");
            params.add(userId);
        }
        d = findFirst(sqlStr, params.toArray());
        return d.getNumber("c") == null ? 0 : d.getNumber("c").intValue();
    }

    public int findCountByMonthForDetail(DateTime startOfMonth, DateTime endOfMonth, boolean superAdmin, int companyId, String userName) {
        return findCountByMonthForDetail(startOfMonth, endOfMonth, superAdmin, companyId, userName, 0);
    }
    //运营证查询司机上线信息
    public List<DriverOnlineDetail> findLogin() {
        return find(SqlManager.sql("operate.login"));
    }

    //查询司机下线信息
    public List<DriverOnlineDetail> findLogout() {
        return find(SqlManager.sql("operate.logout"));
    }

    /**
     * 交通部车辆上线and下线接口
     * @return
     */
    public List<DriverOnlineDetail> findDataByA38AndA40(int type) {
        return find(SqlManager.sql("operate.findDataByA38"),type);
    }

}
