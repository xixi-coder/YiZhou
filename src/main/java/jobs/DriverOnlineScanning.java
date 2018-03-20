package jobs;

import base.Constant;
import com.xiaoleilu.hutool.util.NetUtil;
import dto.DriverOnlineCache;
import kits.ApiAuthKit;
import kits.cache.DriverStatusCache;
import models.driver.DriverInfo;
import models.member.MemberLogin;
import models.order.Order;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IpUtils;

import java.util.List;

public class DriverOnlineScanning implements Job {
    private Logger logger = LoggerFactory.getLogger(DriverOnlineScanning.class);
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String ip = IpUtils.getLocalIP();
        logger.info("=====扫描开始==本机IP:{}===>>>>>", ip);
        if (ip.equals(Constant.MASTER_2_IP)) {
            
            logger.info("执行扫描线上司机是否处于活动状态,时间：{}", DateTime.now().toDateTime());
            List<DriverOnlineCache> list = DriverStatusCache.getList();
            if (list != null) {
                for (DriverOnlineCache driverOnlineCache : list) {
                    int orders = 0;
                    try {
                        orders = Order.dao.findByOnlineScanning(DriverInfo.dao.findByLoginId(driverOnlineCache.getuId()).getId());
                    } catch (Exception e) {
                        logger.error("执行扫描线上司机时获取用户订单数失败！！！");
                    }
                    //扫描没有订单  清除僵尸司机
                    if (orders == 0) {
                        if (t(driverOnlineCache.getOnlineTime())) {
                            final MemberLogin memberLogin = new MemberLogin();
                            memberLogin.setId(driverOnlineCache.getuId());
                            memberLogin.setStatus(Constant.LoginStatus.LOGINED);
                            memberLogin.update();
                            driverOnlineCache.setSubType(-1);
                            driverOnlineCache.setServiceStatus(Constant.LoginStatus.LOGINED);
                            driverOnlineCache.setOnlineTime(System.currentTimeMillis());
                            DriverStatusCache.updateDriverStatus(driverOnlineCache);
                            ApiAuthKit.loginclaer(memberLogin.getId());
                        }
                        logger.info("清理僵尸司机----->{}", driverOnlineCache.getuId());
                    }
                }
                logger.info("清理僵尸司机个数----->{}", list.size());
            }
        }
    }
    
    /**
     * 扫描时间超过30分钟的司机，将他们下线
     *
     * @param onlineTime
     * @return
     */
    private boolean t(Long onlineTime) {
        long t = System.currentTimeMillis() - onlineTime;
        int outTime = 30 * 60 * 1000;
        if (t > outTime) {
            return true;
        }
        return false;
    }
    
}
