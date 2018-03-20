package kits.cache;

import base.Constant;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import models.driver.DriverInfo;
import models.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 司机订单缓存类
 *
 * @author Administrator
 */
public class DriverOrderCache {
    static Logger logger = LoggerFactory.getLogger(DriverOrderCache.class);
    
    /**
     * 缓存前缀  driver_order 简称 普通订单
     */
    private static final String PREFIX = "D_O_";
    
    
    private static final String RESULT = "OK";
    
    private DriverOrderCache() {
    }
    
    private static class DriverOrderCacheHolder {
        static DriverOrderCache instance = new DriverOrderCache();
    }
    
    public static DriverOrderCache getInstance() {
        return DriverOrderCacheHolder.instance;
    }
    
    
    //司机创建订单缓存
    public boolean createOrderCache(Order dto, DriverInfo driverInfo) {
        if (!dto.getSetOutFlag()) {
            if (RESULT.equals(Redis.use(Constant.DRIVER_ORDER_CACHE).set(PREFIX + driverInfo.getPhone(), dto))) {
                logger.info("司机手机号为key-------》缓存司机订单信息成功，司机{},订单号：{}", driverInfo.getPhone(), dto.getNo());
                if (RESULT.equals(Redis.use(Constant.DRIVER_ORDER_CACHE).set(PREFIX + dto.getNo(), dto))) {
                    logger.info("订单号为key-------》缓存司机订单信息成功，订单号：{}", driverInfo.getPhone(), dto.getNo());
                    return true;
                } else {
                    logger.error("订单号为key-------》缓存司机订单信息失败，订单号：{}", driverInfo.getPhone(), dto.getNo());
                    return false;
                }
                
            } else {
                logger.error("司机手机号为key-------》缓存司机订单信息失败，司机{},订单号：{}", driverInfo.getPhone(), dto.getNo());
                return false;
            }
        }
        return false;
    }
    
    /**
     * 获取司机订单缓存
     */
    public List<Order> getOrderByCache(DriverInfo driverInfo) {
        Cache cache = Redis.use(Constant.DRIVER_ORDER_CACHE);
        if (cache.exists(PREFIX + driverInfo.getPhone())) {
            return cache.get(PREFIX + driverInfo.getPhone());
        }
        return null;
    }
    
    //获取司机订单缓存
    public Order getOrderByCache(String orderNo) {
        Cache cache = Redis.use(Constant.DRIVER_ORDER_CACHE);
        if (cache.exists(PREFIX + orderNo)) {
            return cache.get(PREFIX + orderNo);
        }
        return null;
    }
    
    //删除缓存
    public boolean del(DriverInfo driverInfo) {
        if (Redis.use(Constant.DRIVER_ORDER_CACHE).del(PREFIX + driverInfo.getPhone()) >= 0) {
            return true;
        } else {
            logger.error("清空司机订单缓存信息失败，司机{},", driverInfo.getPhone());
            return false;
        }
    }
    
    //删除缓存
    public boolean del(String orderNo) {
        if (Redis.use(Constant.DRIVER_ORDER_CACHE).del(PREFIX + orderNo) >= 0) {
            return true;
        } else {
            logger.error("清空订单缓存信息失败，单号{},", orderNo);
            return false;
        }
    }
    
}
