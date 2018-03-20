package kits.cache;


import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 客户端工具
 *
 * @author Administrator
 */
public class RedisClient {
    
    public RedisClient() {
    }
    
    private static class RedisClientHolder {
        static RedisClient instance = new RedisClient();
    }
    
    public static RedisClient getInstance() {
        return RedisClientHolder.instance;
    }
    
    /**
     * 通过key删除（字节）
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 通过key删除
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.del(key);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(byte[] key, byte[] value, int liveTime) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.set(key, value);
        jedis.expire(key, liveTime);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, int liveTime) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.set(key, value);
        jedis.expire(key, liveTime);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.set(key, value);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.set(key, value);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = RedisUtils.getJedis();
        String value = jedis.get(key);
        RedisUtils.returnResource(jedis);
        return value;
    }
    
    /**
     * 获取redis value (byte [] )(反序列化)
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = RedisUtils.getJedis();
        byte[] value = jedis.get(key);
        RedisUtils.returnResource(jedis);
        return value;
    }
    
    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = RedisUtils.getJedis();
        Set<String> value = jedis.keys(pattern);
        RedisUtils.returnResource(jedis);
        return value;
    }
    
    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = RedisUtils.getJedis();
        boolean value = jedis.exists(key);
        RedisUtils.returnResource(jedis);
        return value;
    }
    
    /*******************redis list操作************************/
    /**
     * 往list中添加元素
     * Redis Lpush 命令将一个或多个值插入到列表头部。
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * <p>
     * <p>
     * 执行 LPUSH 命令后，列表的长度。
     *
     * @param key
     * @param value
     */
    public void lpush(String key, String... value) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.lpush(key, value);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * Redis Rpush 命令用于将一个或多个值插入到列表的尾部(最右边)。
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     * <p>
     * <p>
     * 执行 RPUSH 操作后，列表的长度。
     *
     * @param key
     * @param value
     */
    public void rpush(String key, String... value) {
        Jedis jedis = RedisUtils.getJedis();
        jedis.rpush(key, value);
        RedisUtils.returnResource(jedis);
    }
    
    /**
     * 数组长度
     * Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，
     * 返回 0 。 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return 列表的长度。
     */
    public Long llen(String key) {
        Jedis jedis = RedisUtils.getJedis();
        Long len = jedis.llen(key);
        RedisUtils.returnResource(jedis);
        return len;
    }
    
    /**
     * 获取下标为index的value
     * Redis Lindex 命令用于通过索引获取列表中的元素。你也可以使用负数下标，
     * 以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param index
     * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil
     */
    public String lindex(String key, Long index) {
        Jedis jedis = RedisUtils.getJedis();
        String str = jedis.lindex(key, index);
        RedisUtils.returnResource(jedis);
        return str;
    }
    
    /**
     * Redis Lpop 命令用于移除并返回列表的第一个元素
     *
     * @param key
     * @return 列表的第一个元素。 当列表 key 不存在时，返回 nil 。
     */
    public String lpop(String key) {
        Jedis jedis = RedisUtils.getJedis();
        String str = jedis.lpop(key);
        RedisUtils.returnResource(jedis);
        return str;
    }
    
    /**
     * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return 一个列表，包含指定区间内的元素。
     */
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = RedisUtils.getJedis();
        List<String> str = jedis.lrange(key, start, end);
        RedisUtils.returnResource(jedis);
        return str;
    }
    
    /**
     * Redis Lrem 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
     * COUNT 的值可以是以下几种：
     * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     * count = 0 : 移除表中所有与 VALUE 相等的值。
     *
     * @param arrayName
     * @param count
     * @param value
     * @return 被移除元素的数量。 列表不存在时返回 0 。
     */
    public boolean lrem(String arrayName, long count, String value) {
        Jedis jedis = RedisUtils.getJedis();
        long result = jedis.lrem(arrayName, count, value);
        RedisUtils.returnResource(jedis);
        if (result >= 0) {
            return true;
        }
        return false;
    }
    
    /*********************redis list操作结束**************************/
    
    
    /*********************redis Map操作开始**************************/
    //*******************************************Hash*******************************************//  
    public static class HashMap {
        public HashMap() {
        }
        
        
        private static class HashMapHolder {
            static HashMap instance = new HashMap();
        }
        
        public static HashMap getInstance() {
            return HashMapHolder.instance;
        }
        
        
        /**
         * 从hash中删除指定的存储
         *
         * @param key   key
         * @param fieid fieid 存储的名字
         * @return
         */
        public boolean hdel(String key, String fieid) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.hdel(key, fieid);
            RedisUtils.returnResource(jedis);
            if (s == 1) {
                return true;
            }
            return false;
        }
        
        /**
         * 从hash中删除指定的存储By key
         *
         * @param key
         * @return
         */
        public boolean hdel(String key) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.del(key);
            RedisUtils.returnResource(jedis);
            if (s == 1) {
                return true;
            }
            return false;
        }
        
        /**
         * 测试hash中指定的存储是否存在
         *
         * @param key   key
         * @param fieid fieid 存储的名字
         * @return
         */
        public boolean hexists(String key, String fieid) {
            Jedis sjedis = RedisUtils.getJedis();
            boolean s = sjedis.hexists(key, fieid);
            RedisUtils.returnResource(sjedis);
            return s;
        }
        
        
        /**
         * 返回hash中指定存储位置的值（非序列化的）
         *
         * @param key
         * @param fieid fieid 存储的名字
         * @return 存储对应的值
         */
        public String hget(String key, String fieid) {
            Jedis sjedis = RedisUtils.getJedis();
            String s = sjedis.hget(key, fieid);
            RedisUtils.returnResource(sjedis);
            return s;
        }
        
        /**
         * 返回hash中指定存储位置的值(序列化的)
         *
         * @param key
         * @param fieid fieid 存储的名字
         * @return
         */
        public byte[] hget(byte[] key, byte[] fieid) {
            Jedis sjedis = RedisUtils.getJedis();
            byte[] s = sjedis.hget(key, fieid);
            RedisUtils.returnResource(sjedis);
            return s;
        }
        
        /**
         * 以Map的形式返回hash中的存储和值
         *
         * @param key key
         * @return
         */
        public Map<String, String> hgetAll(String key) {
            Jedis sjedis = RedisUtils.getJedis();
            Map<String, String> map = sjedis.hgetAll(key);
            RedisUtils.returnResource(sjedis);
            return map;
        }
        
        /**
         * 添加一个对应关系
         *
         * @param key   key
         * @param fieid fieid
         * @param value value
         * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
         **/
        public long hset(String key, String fieid, String value) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.hset(key, fieid, value);
            RedisUtils.returnResource(jedis);
            return s;
        }
        
        /**
         * 添加一个对应关系(序列化)
         *
         * @param key
         * @param fieid
         * @param value
         * @return
         */
        public long hset(String key, String fieid, byte[] value) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.hset(key.getBytes(), fieid.getBytes(), value);
            RedisUtils.returnResource(jedis);
            return s;
        }
        
        /**
         * 添加对应关系，只有在fieid不存在时才执行
         *
         * @param key
         * @param fieid
         * @param value
         * @return 状态码 1成功，0失败fieid已存
         **/
        public long hsetnx(String key, String fieid, String value) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.hsetnx(key, fieid, value);
            RedisUtils.returnResource(jedis);
            return s;
        }
        
        /**
         * 获取hash中value的集合
         *
         * @param key
         * @return List<String>
         */
        public List<String> hvals(String key) {
            Jedis sjedis = RedisUtils.getJedis();
            List<String> list = sjedis.hvals(key);
            RedisUtils.returnResource(sjedis);
            return list;
        }
        
        /**
         * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型
         *
         * @param key
         * @param fieid 存储位置
         * @param value 要增加的值,可以是负数
         * @return 增加指定数字后，存储位置的值
         */
        public long hincrby(String key, String fieid, long value) {
            Jedis jedis = RedisUtils.getJedis();
            long s = jedis.hincrBy(key, fieid, value);
            RedisUtils.returnResource(jedis);
            return s;
        }
        
        /**
         * 返回指定hash中的所有存储名字,类似Map中的keySet方法
         *
         * @param key
         * @return Set<String> 存储名称的集合
         */
        public Set<String> hkeys(String key) {
            Jedis sjedis = RedisUtils.getJedis();
            Set<String> set = sjedis.hkeys(key);
            RedisUtils.returnResource(sjedis);
            return set;
        }
        
        /**
         * 获取hash中存储的个数，类似Map中size方法
         *
         * @param key
         * @return long 存储的个数
         */
        public long hlen(String key) {
            Jedis sjedis = RedisUtils.getJedis();
            long len = sjedis.hlen(key);
            RedisUtils.returnResource(sjedis);
            return len;
        }
        
        /**
         * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null
         *
         * @param key
         * @param fieids 存储位置
         * @return List<String>
         */
        public List<String> hmget(String key, String... fieids) {
            Jedis sjedis = RedisUtils.getJedis();
            List<String> list = sjedis.hmget(key, fieids);
            RedisUtils.returnResource(sjedis);
            return list;
        }
        
        public List<byte[]> hmget(byte[] key, byte[]... fieids) {
            Jedis sjedis = RedisUtils.getJedis();
            List<byte[]> list = sjedis.hmget(key, fieids);
            RedisUtils.returnResource(sjedis);
            return list;
        }
        
        
        /**
         * 添加对应关系，如果对应关系已存在，则覆盖
         *
         * @param key key
         * @param map <String,String> 对应关系
         * @return 状态，成功返回OK
         */
        public boolean hmset(String key, Map<String, String> map) {
            Jedis jedis = RedisUtils.getJedis();
            String s = jedis.hmset(key, map);
            RedisUtils.returnResource(jedis);
            if ("OK".equals(s)) {
                return true;
            }
            return false;
        }
        
        /**
         * 添加对应关系，如果对应关系已存在，则覆盖
         *
         * @param key
         * @param map <String,String> 对应关系
         * @return 状态，成功返回OK
         */
        public boolean hmset(byte[] key, Map<byte[], byte[]> map) {
            Jedis jedis = RedisUtils.getJedis();
            String s = jedis.hmset(key, map);
            RedisUtils.returnResource(jedis);
            if ("OK".equals(s)) {
                return true;
            }
            return false;
        }
        
    }
    
    /*********************redis Map操作结束**************************/
    
    
    /**
     * 清空redis 所有数据
     * Redis Flushdb 命令用于清空当前数据库中的所有 key。
     *
     * @return 总是返回 OK 。
     */
    public String flushDB() {
        Jedis jedis = RedisUtils.getJedis();
        String str = jedis.flushDB();
        RedisUtils.returnResource(jedis);
        return str;
    }
    
    /**
     * 查看redis里有多少数据
     *
     * @return 当前数据库的 key 的数量
     */
    public long dbSize() {
        Jedis jedis = RedisUtils.getJedis();
        long len = jedis.dbSize();
        RedisUtils.returnResource(jedis);
        return len;
    }
    
    /**
     * 检查是否连接成功
     *
     * @return 如果连接正常就返回一个 PONG ，否则返回一个连接错误。
     */
    public String ping() {
        Jedis jedis = RedisUtils.getJedis();
        String str = jedis.ping();
        RedisUtils.returnResource(jedis);
        return str;
    }
}