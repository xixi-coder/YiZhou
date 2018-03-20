package kits.cache;

import base.Constant;
import com.google.common.collect.Lists;
import com.jfinal.json.Jackson;
import dto.DriverOnlineCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 管理司机状态
 *
 * @author Administrator
 */
public class DriverStatusCache {
    private static Logger logger = LoggerFactory.getLogger(DriverStatusCache.class);
    
    private DriverStatusCache() {
    }
    
    private static class DriverOrderCacheHolder {
        static DriverStatusCache instance = new DriverStatusCache();
    }
    
    public static DriverStatusCache getInstance() {
        return DriverOrderCacheHolder.instance;
    }
    
    /**
     * 为缓存司机状态List中添加司机
     *
     * @param driverOnlineCache
     */
    public static synchronized void saveDriverStatus(DriverOnlineCache driverOnlineCache) {
        if (!RedisClient.getInstance().exists(Constant.DRIVER_STATUS_CACHE)) {
            RedisClient.getInstance().lrange(Constant.DRIVER_STATUS_CACHE, 0, -1);
        }
        RedisClient.getInstance().lpush(Constant.DRIVER_STATUS_CACHE, Jackson.getJson().toJson(driverOnlineCache));
    }
    
    
    /**
     * 获取缓存中的司机信息
     *
     * @return
     */
    public static List<DriverOnlineCache> getList() {
        List<DriverOnlineCache> driverOnlineCaches = Lists.newArrayList();
        List<String> list = RedisClient.getInstance().lrange(Constant.DRIVER_STATUS_CACHE, 0, -1);
        if (list == null) {
            return null;
        }
        for (String s : list) {
            DriverOnlineCache driver = Jackson.getJson().parse(s, DriverOnlineCache.class);
           /* logger.info("获取缓存中的司机信息，LoginID:{},服务类型:{},当前状态:{},位置更新时间：{}",
            driver.getuId(), driver.getSubType(), driver.getServiceStatus(), new Date(driver.getOnlineTime()));*/
            driverOnlineCaches.add(driver);
        }
        return driverOnlineCaches;
    }
    
    
    /**
     * 获取缓存中的司机信息(一个)
     *
     * @return
     */
    public static DriverOnlineCache getDriverOnlineCache(int uId) {
        List<DriverOnlineCache> list = getList();
        if (list == null) {
            return null;
        }
        for (DriverOnlineCache cache : list) {
            if (cache.getuId().equals(uId)) {
                return cache;
            }
        }
        return null;
    }
    
    /**
     * 修改司机状态
     *
     * @param driverOnlineCache
     */
    public static synchronized void updateDriverStatus(DriverOnlineCache driverOnlineCache) {
        DriverOnlineCache cache = getDriverOnlineCache(driverOnlineCache.getuId());
        if (cache == null) {
            saveDriverStatus(driverOnlineCache);
            return;
        } else if (cache.getuId().equals(driverOnlineCache.getuId())) {
            RedisClient.getInstance().lrem(Constant.DRIVER_STATUS_CACHE, 0, Jackson.getJson().toJson(cache));
            saveDriverStatus(driverOnlineCache);
        }
    }
}
