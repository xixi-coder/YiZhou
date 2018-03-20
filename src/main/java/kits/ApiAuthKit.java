package kits;

import base.Constant;
import com.google.common.collect.Maps;
import com.jfinal.plugin.redis.Redis;
import models.member.MemberLogin;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/20.
 */
public class ApiAuthKit {

    static Logger logger = LoggerFactory.getLogger(ApiAuthKit.class);

    /**
     * 判断用户是否登陆
     *
     * @param token
     * @param sercet
     * @param appType
     * @return
     */
    public static boolean isLogin(String sercet, String token, String appType) {
        String cacheKey = Md5Kit.MD5(sercet + token + appType);
        try {
            MemberLogin loginMember = Redis.use(Constant.LOGINMEMBER_CACHE).get(cacheKey);
            if (loginMember != null) {
                Redis.use(Constant.LOGINMEMBER_CACHE).setex(cacheKey, Constant.LOGIN_TIMEOUT, loginMember);
                return true;
            } else {
                Redis.use(Constant.LOGINMEMBER_CACHE).del(cacheKey);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取登陆用户信息
     *
     * @param token
     * @param sercet
     * @param appType
     * @return
     */
    public static MemberLogin getLoginMember(String token, String sercet, String appType) {
        try {
            String cacheKey = Md5Kit.MD5(sercet + token + appType);
            MemberLogin loginMember = Redis.use(Constant.LOGINMEMBER_CACHE).get(cacheKey);
            return loginMember;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 登陆，把登陆信息放入redis
     *
     * @param loginMember
     * @return
     */
    public static Map<String, String> login(MemberLogin loginMember) {
        try {
            String sercet = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
            String token = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
            int appType = loginMember.getNumber("appType").intValue();
            String key = Md5Kit.MD5(sercet + token + appType);
            Redis.use(Constant.LOGINMEMBER_CACHE).setex(key, Constant.LOGIN_TIMEOUT, loginMember);
            loginMember.setLastLoginTime(DateTime.now().toDate());
            loginMember.setLoginStatus(Constant.LoginStatus.LOGINED);//设置状态为登陆状态
            if (loginMember.getStatus() != null && loginMember.getStatus() == Constant.LoginStatus.LOGOUTED) {
                loginMember.setStatus(Constant.LoginStatus.LOGINED);//设置状态为登陆状态
            } else if (loginMember.getStatus() == null) {
                loginMember.setStatus(Constant.LoginStatus.LOGINED);//设置状态为登陆状态
            }
            loginMember.setDeviceType(appType);
            loginMember.setCacheKey(key);
            loginMember.update();
            Map<String, String> result = Maps.newHashMap();
            result.put(Constant.SECRET, sercet);
            result.put(Constant.TOKEN, token);
            result.put(Constant.APPTYPE, appType + "");
            result.put("loginId", loginMember.getId() + "");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 登出，需要删除redis里面的用户信息
     *
     * @param token
     * @param sercet
     * @param appType
     * @return
     */
    public static boolean logout(String token, String sercet, int appType) {

        try {
            String cacheKey = Md5Kit.MD5(sercet + token + appType);
            MemberLogin tmp = Redis.use(Constant.LOGINMEMBER_CACHE).get(cacheKey);
            if (tmp == null) {
                return true;
            }
            MemberLogin memberLogin = MemberLogin.dao.findById(tmp.getId());
            memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);//设置为登出状态
            memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);//设置为登出状态
            return Redis.use(Constant.LOGINMEMBER_CACHE).del(cacheKey) >= 0 && memberLogin.update();
        } catch (NullPointerException e) {
            logger.error("退出操作，用户信息获取失败！");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上线成功  存代驾还是其他 （存小服务类型）
     *
     * @param uid
     * @param utype
     */
    public static void loginput(Integer uid, Integer utype) {

        Redis.use(Constant.LOGINMEMBER_CACHE).set(uid, utype);

    }


    /**
     * 下线成功 清除缓存
     *
     * @param uid
     */
    public static void loginclaer(Integer uid) {
        Redis.use(Constant.LOGINMEMBER_CACHE).set(uid, -1);
    }

    /**
     * 获取  UID 的状态
     *
     * @param uid
     * @return
     */
    public static int loginout(Integer uid) {
        int utype = -1;
        try {
            utype = Redis.use(Constant.LOGINMEMBER_CACHE).get(uid);
        } catch (NullPointerException e) {
        }
        return utype;
    }

}
