package utils;

import base.Constant;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.Jackson;
import dto.DriverOnlineCache;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RedisTest {
    private Jedis jedis;
    private JedisPool jedisPool;
    
    @Before
    public void setup() {
        //连接redis服务器，192.168.0.100:6379
        jedis = new Jedis("127.0.0.1", 6379);
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(1024);
            config.setMaxIdle(200);
            config.setMaxWaitMillis(10000);
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, "127.0.0.1", 6379, 10000, null, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //权限认证
    }
    
    /**
     * redis存储字符串
     */
    @Test
    public void testString() {
        //-----添加数据----------
        jedis.set("name", "xinxin");//向key-->name中放入了value-->xinxin
        System.out.println(jedis.get("name"));//执行结果：xinxin
        
        jedis.append("name", " is my lover"); //拼接
        System.out.println(jedis.get("name"));
        
        jedis.del("name");  //删除某个键
        System.out.println(jedis.get("name"));
        //设置多个键值对
        jedis.mset("name", "liuling", "age", "23", "qq", "476777XXX");
        jedis.incr("age"); //进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        
    }
    
    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        
        
        //-----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("", "");
        map.put("name1", "xinxin");
        map.put("age1", "22");
        map.put("qq1", "123456");
        
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("name2", "xinxin");
        map2.put("age2", "22");
        map2.put("qq2", "123456");
        
        Map<String, String> mapAll = new HashMap<String, String>();
        mapAll.put("map1", JSONObject.toJSONString(map));
        mapAll.put("map2", JSONObject.toJSONString(map2));
        jedisPool.getResource().hmset("H_L_", map);
        
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        Map<String, String> result = jedisPool.getResource().hgetAll("H_L_");
        System.out.println(result.get("16313"));
        System.out.println( jedisPool.getResource().hget("H_L_","16313"));
        
        //删除map中的某个键值
        //jedis.hdel("user", "age");
        
        Iterator<String> iter = jedisPool.getResource().hkeys("H_L_").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedisPool.getResource().hmget("H_L_", key));
        }
    }
    
    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        DriverOnlineCache driverOnlineCache = new DriverOnlineCache(123, 44, 1231321L);
        if (!jedis.exists(Constant.DRIVER_STATUS_CACHE)) {
            jedis.lrange(Constant.DRIVER_STATUS_CACHE, 0, -1);
        }
        jedis.lpush(Constant.DRIVER_STATUS_CACHE, Jackson.getJson().toJson(driverOnlineCache));
    }
    
    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
       /* //添加
        jedis.sadd("user", "liuling");
        jedis.sadd("user", "xinxin");
        jedis.sadd("user", "ling");
        jedis.sadd("user", "zhangxinxin");
        jedis.sadd("user", "who");
        //移除noname
        jedis.srem("user", "who");
        System.out.println(jedis.smembers("user"));//获取所有加入的value
        System.out.println(jedis.sismember("user", "who"));//判断 who 是否是user集合的元素
        System.out.println(jedis.srandmember("user"));
        System.out.println(jedis.scard("user"));//返回集合的元素个数*/
        
        
        String str = "4;1;2;3;5;6";
        if (str.indexOf(2) != -1) {
            System.out.println("包含");
        } else {
            System.out.println("不包含");
        }
    }
    
    @Test
    public void test() throws InterruptedException {
        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");//先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果
        System.out.println(jedis.lrange("a", 0, -1));
    }
}