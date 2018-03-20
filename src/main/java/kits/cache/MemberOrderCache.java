package kits.cache;

import base.Constant;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import models.member.MemberInfo;
import models.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户订单缓存类
 *
 * @author Administrator
 */
public class MemberOrderCache {
    static Logger logger = LoggerFactory.getLogger(MemberOrderCache.class);
    
    /**
     * 缓存前缀  member_order 简称  普通订单
     */
    private static final String PREFIX = "M_O_";
    private static final String RESULT = "OK";
    
    private MemberOrderCache() {
    }
    
    private static class MemberOrderCacheHolder {
        static MemberOrderCache instance = new MemberOrderCache();
    }
    
    public static MemberOrderCache getInstance() {
        return MemberOrderCacheHolder.instance;
    }
    
    
    /**
     * 用户创建订单缓存
     *
     * @param dto
     * @param memberInfo
     * @return
     */
    public boolean createOrderCache(Order dto, MemberInfo memberInfo) {
        if (!dto.getSetOutFlag()) {
            if (RESULT.equals(Redis.use(Constant.MEMBER_ORDER_CACHE).set(PREFIX + memberInfo.getPhone(), dto))) {
                logger.info("用户手机号为key-------》缓存用户订单信息成功，用户{},订单号：{}", memberInfo.getPhone(), dto.getNo());
                if (RESULT.equals(Redis.use(Constant.MEMBER_ORDER_CACHE).set(PREFIX + dto.getNo(), dto))) {
                    logger.info("订单号为key-------》缓存用户订单信息成功，订单号：{}", memberInfo.getPhone(), dto.getNo());
                    return true;
                } else {
                    logger.error("订单号为key-------》缓存用户订单信息失败，订单号：{}", memberInfo.getPhone(), dto.getNo());
                    return false;
                }
            } else {
                logger.error("用户手机号为key-------》缓存用户订单信息失败，用户{},订单号：{}", memberInfo.getPhone(), dto.getNo());
                return false;
            }
        }
        return false;
    }
    
    //获取用户订单缓存
    public Object getOrderByCache(MemberInfo memberInfo) {
        Cache cache = Redis.use(Constant.MEMBER_ORDER_CACHE);
        if (cache.exists(PREFIX + memberInfo.getPhone())) {
            return cache.get(PREFIX + memberInfo.getPhone());
        }
        return null;
    }
    
    //获取用户订单缓存
    public Order getOrderByCache(String orderNo) {
        Cache cache = Redis.use(Constant.MEMBER_ORDER_CACHE);
        if (cache.exists(PREFIX + orderNo)) {
            return cache.get(PREFIX + orderNo);
        }
        return null;
    }
    
    //删除缓存
    public boolean del(MemberInfo memberInfo) {
        if (Redis.use(Constant.MEMBER_ORDER_CACHE).del(PREFIX + memberInfo.getPhone()) >= 0) {
            return true;
        } else {
            logger.error("清空用户订单缓存信息失败，用户{},", memberInfo.getPhone());
            return false;
        }
    }
    
    //删除缓存
    public boolean del(String orderNo) {
        if (Redis.use(Constant.MEMBER_ORDER_CACHE).del(PREFIX + orderNo) >= 0) {
            return true;
        } else {
            logger.error("清空订单缓存信息失败，单号{},", orderNo);
            return false;
        }
    }
    
}
