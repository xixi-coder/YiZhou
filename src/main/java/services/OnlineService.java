package services;

import base.Constant;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import models.car.Car;
import models.car.DriverCar;
import models.driver.DriverInfo;
import models.driver.DriverOnlineDetail;
import models.member.MemberLogin;
import models.order.Order;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.sqlInXml.SqlManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/27.
 */
public class OnlineService {
    
    private static Logger logger = LoggerFactory.getLogger(OnlineService.class);
    
    public Map<String, Object> online(final MemberLogin memberLogin, DriverInfo driverInfo, final DriverCar driverCar) {
        if (driverCar != null) {
            driverCar.setUseFlag(true);
            Car carInfo = Car.dao.findById(driverCar.getCar());
            memberLogin.setServiceType(carInfo.getType());
        } else {
            memberLogin.setServiceType(Constant.ServiceItemType.DaiJia);
        }
        Map<String, Object> results = Maps.newHashMap();
        Date now = DateTime.now().toDate();
        final DriverOnlineDetail driverOnlineDetail;
        driverOnlineDetail = DriverOnlineDetail.dao.create(driverInfo.getId(), 0, false, Constant.OnlineOperatType.online, now);
        Map<String, Integer> result = Maps.newHashMap();
        Date start = new DateTime(now).millisOfDay().withMinimumValue().toDate();
        Date end = new DateTime(now).millisOfDay().withMaximumValue().toDate();
        int tatolTime = DriverOnlineDetail.dao.findTotalTimeByDriverAndDate(driverInfo.getId(), start, end);
        if (tatolTime >= 1380 || tatolTime < 0) {
            tatolTime = 0;
        }
        if (memberLogin.getStatus() == Constant.LoginStatus.RECIVEDORDER
        || memberLogin.getStatus() == Constant.LoginStatus.RECIVEDORDERPDNO
        || memberLogin.getStatus() == Constant.LoginStatus.RECIVEDORDERPD
        || memberLogin.getStatus() == Constant.LoginStatus.BUSY
        ) {
            result.put("serviceTime", tatolTime);
            results.put("result", result);
            results.put("isOk", true);
            return results;
        }
        result.put("serviceTime", tatolTime);
        results.put("result", result);
        List<Order> hasOrder = Order.dao.findByDriverNoComplete(driverInfo.getId());
        if (hasOrder != null && hasOrder.size() > 0) {
            if (hasOrder.get(0).getBoolean("pdFlag") != null && hasOrder.get(0).getBoolean("pdFlag") == true) {
                if (hasOrder.size() > 1) {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPDNO);
                } else {
                    memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDERPD);
                }
            } else {
                memberLogin.setStatus(Constant.LoginStatus.BUSY);
            }
        } else {
            memberLogin.setStatus(Constant.LoginStatus.RECIVEDORDER);
        }
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (driverCar != null) {
                    return driverCar.update()
                    && memberLogin.update()
                    && Db.update(SqlManager.sql("driverCar.updateExcludeThis"), false, driverCar.getDriver(), driverCar.getCar()) >= 0
                    && driverOnlineDetail.save();
                } else {
                    return memberLogin.update()
                    && driverOnlineDetail.save();
                }
                
            }
        });
        results.put("isOk", isOk);
        return results;
    }
    
    public boolean offline(MemberLogin memberLogin, final DriverInfo driverInfo) {
        logger.info("司机{}下线了==================================", driverInfo.getId());
        memberLogin = MemberLogin.dao.findById(memberLogin.getId());
        Date now = DateTime.now().toDate();
        memberLogin.setStatus(Constant.LoginStatus.LOGINED);
        final DriverOnlineDetail driverOnlineDetail;
        Integer onlineTime;
        Map<String, Object> result = getOnlineTime(driverInfo, now);
        if (result == null) {
            return false;
        }
        onlineTime = 0;
        //(Integer) result.get("onlineTime");
        driverOnlineDetail = DriverOnlineDetail.dao.create(driverInfo.getId(), onlineTime, false, Constant.OnlineOperatType.downline, now);
        final MemberLogin finalMemberLogin = memberLogin;
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return finalMemberLogin.update()
                && Db.update(SqlManager.sql("driverCar.updateAll"), false, driverInfo.getId()) >= 0
                && driverOnlineDetail.save();
            }
        });
        return isOk;
    }
    
    //作废
    @Deprecated
    public Map<String, Object> getOnlineTime(DriverInfo driverInfo, Date now) {
        int onlineTime;
        /**
         *的上线的记录
         */
        DriverOnlineDetail dd = DriverOnlineDetail.dao.findCurrentByDriverAndType(driverInfo.getId(), Constant.OnlineOperatType.online);
        /**
         * 把最新的下线记录删除
         */
        if (dd != null) {
            Map<String, Object> result = Maps.newHashMap();
            DateTime start = new DateTime(dd.getOperatTime());
            DateTime end = new DateTime(now);
            if (start.getDayOfMonth() > end.getDayOfMonth()) {
                onlineTime = Minutes.minutesBetween(start.plusDays(-1), end).getMinutes();
            } else {
                onlineTime = Minutes.minutesBetween(start, end).getMinutes();
            }
            result.put("onlineTime", onlineTime);
            return result;
        } else {
            return null;
        }
        
    }
    
    private OnlineService() {
    }
    
    public static void autoRecovery(DateTime now, List<DriverInfo> driverInfos) {
        DateTime yesterday = now.plusDays(-1);
        for (DriverInfo driverInfo : driverInfos) {
            /**
             * 不会存在跨好几天，只能今天昨天
             */
            DriverOnlineDetail yesterdayOnline = DriverOnlineDetail.dao.findCurrentByDriverAndType(driverInfo.getId(), Constant.OnlineOperatType.online);
            DateTime currentOnlineTime = new DateTime(yesterdayOnline.getOperatTime());
            if (currentOnlineTime.dayOfYear().get() == now.dayOfYear().get()) {
                continue;
            }
            int minus = Minutes.minutesBetween(new DateTime(yesterdayOnline.getOperatTime()), yesterday.millisOfDay().withMaximumValue()).getMinutes();
            Date yesterdayOffLineDate = yesterday.millisOfDay().withMaximumValue().plusSeconds(-20).toDate();
            
            DriverOnlineDetail yesterdayOffLine = DriverOnlineDetail.dao.create(driverInfo.getId(), minus, true, Constant.OnlineOperatType.downline, yesterdayOffLineDate);
            yesterdayOffLine.save();
            DriverOnlineDetail todayOnline = DriverOnlineDetail.dao.create(driverInfo.getId(), 0, true, Constant.OnlineOperatType.online, now.minuteOfDay().withMinimumValue().toDate());
            todayOnline.save();
        }
    }
    
    private static class OnlineServiceHolder {
        static OnlineService instance = new OnlineService();
    }
    
    public static OnlineService getInstance() {
        return OnlineServiceHolder.instance;
    }
    
    public static void main(String[] args) {
        DateTime start = new DateTime(Longs.tryParse("1479178937000"));
        DateTime end = new DateTime(Longs.tryParse("1479178741000"));
        System.out.printf(start.toString() + end.toString() + "");
    }
}
