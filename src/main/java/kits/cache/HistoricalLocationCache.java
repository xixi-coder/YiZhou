package kits.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import dto.location.HistoricalLocation;

import java.util.List;
import java.util.Map;

public class HistoricalLocationCache {
    Log log = LogFactory.get();
    
    /**
     * 缓存Map 名称
     */
    private final static String PREFIX = "H_L_";
    
    public HistoricalLocationCache() {
    }
    
    private static class HistoricalLocationCacheHolder {
        static HistoricalLocationCache instance = new HistoricalLocationCache();
    }
    
    public static HistoricalLocationCache getInstance() {
        return HistoricalLocationCacheHolder.instance;
    }
    
    
    public boolean saveOrUpdate(HistoricalLocation location) {
        synchronized (this) {
            if (!RedisClient.getInstance().exists(PREFIX)) {
                Map<String, String> paraMap = Maps.newHashMap();
                //redis 不允许存储map为空
                paraMap.put("", "");
                RedisClient.HashMap.getInstance().hmset(PREFIX, paraMap);
            }
            if (RedisClient.HashMap.getInstance().hexists(PREFIX, location.getLoginId().toString())) {
                RedisClient.HashMap.getInstance().hdel(location.getLoginId().toString());
            }
            try {
                RedisClient.HashMap.getInstance().hset(PREFIX, location.getLoginId().toString(), JSONObject.toJSONString(location));
            } catch (Exception e) {
                e.printStackTrace();
                log.error("存储附近司机位置失败！");
                return false;
            }
            
            return true;
            
        }
    }
    
    public Map<String, String> getAll() {
        return RedisClient.HashMap.getInstance().hgetAll(PREFIX);
    }
    
    //通过指定的KEY获取value
    public List<HistoricalLocation> getAllById(List<String> loginId) {
        List<HistoricalLocation> list = Lists.newArrayList();
        for (String s : loginId) {
            if (!Strings.isNullOrEmpty(s)) {
                HistoricalLocation historicalLocation = JSON.parseObject(RedisClient.HashMap.getInstance().hget(PREFIX, s), HistoricalLocation.class);
                if (historicalLocation != null) {
                    list.add(historicalLocation);
                }
            }
        }
        return list;
    }
    
    
    //通过指定的KEY获取value
    public HistoricalLocation getAllById(String loginId) {
        if (!Strings.isNullOrEmpty(loginId)) {
            HistoricalLocation historicalLocation = JSON.parseObject(RedisClient.HashMap.getInstance().hget(PREFIX, loginId), HistoricalLocation.class);
            if (historicalLocation != null) {
                return historicalLocation;
            }
        }
        return null;
    }
}
