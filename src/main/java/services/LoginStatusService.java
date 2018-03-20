package services;

import base.Constant;
import com.jfinal.plugin.redis.Redis;
import models.car.DriverCar;
import models.driver.DriverInfo;
import models.member.MemberLogin;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/6.
 */
public class LoginStatusService {
    private Logger logger = LoggerFactory.getLogger(LoginStatusService.class);

    private LoginStatusService() {
    }

    private static class LoginStatusServiceeHolder {
        static LoginStatusService intance = new LoginStatusService();
    }

    public static LoginStatusService getIntance() {
        return LoginStatusServiceeHolder.intance;
    }

    public void updateStatus() {
        logger.info("执行了时间：{}", DateTime.now().toDateTime());
        List<MemberLogin> memberLoginList = MemberLogin.dao.findLogin();
        for (MemberLogin memberLogin : memberLoginList) {
            if (memberLogin.getCacheKey() == null)
                continue;
            if (!Redis.use(Constant.LOGINMEMBER_CACHE).exists(memberLogin.getCacheKey())) {
//                updateWithWS(memberLogin);
//                updateCar(memberLogin);
                memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
                memberLogin.update();
            }
//            else {
//                if (!Redis.use(Constant.USECAR_ID_CACHE).exists(Constant.CARSTR + memberLogin.getId())
//                        &&memberLogin.getStatus()== Constant.LoginStatus.RECIVEDORDER) {
//                    updateCar(memberLogin);
//                }
////                updateWithWS(memberLogin);
//            }
        }
    }

    public void updateCar(MemberLogin memberLogin) {
        if (memberLogin.getType() == Constant.DRIVER) {
            if(memberLogin.getServiceType()==Constant.ServiceItemType.DaiJia){
                return;
            }
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
            DriverCar driverCar = DriverCar.dao.findByDriverAndStatus(driverInfo.getId(), true);
            if (driverCar != null && !Redis.use(Constant.USECAR_ID_CACHE).exists(Constant.CARSTR + driverInfo.getId())) {
                OnlineService.getInstance().offline(memberLogin, driverInfo);
            }
        }
    }

    public void updateWithWS(MemberLogin memberLogin) {
        if (memberLogin.getType() == Constant.DRIVER) {
//            logger.info("loginId={}WS连接是否过期了{}",memberLogin.getId(),ApiWebSocketService.getInstance().exit(memberLogin.getCacheKey()));
//            if (!ApiWebSocketService.getInstance().exit(memberLogin.getCacheKey())
//                    &&memberLogin.getStatus()== Constant.LoginStatus.RECIVEDORDER) {
//                DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberLogin.getId());
//                OnlineService.getInstance().offline(memberLogin, driverInfo, new Date().getTime() + "", new Date().getTime() + "");
//            }
        }
    }
}
