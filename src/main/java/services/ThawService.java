package services;

import models.driver.DriverInfo;
import models.driver.FrozenLog;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/11/26.
 */
public class ThawService {
    private static Logger logger = LoggerFactory.getLogger(ThawService.class);

    private ThawService() {

    }

    private static class ThawServiceHolder {
        static ThawService instance = new ThawService();
    }

    public static ThawService getInstance() {
        return ThawServiceHolder.instance;
    }

    public static void thaw() {
        Date start = DateTime.now().secondOfMinute().withMinimumValue().toDate();
        Date end = DateTime.now().secondOfMinute().withMaximumValue().toDate();
        List<FrozenLog> frozenLogs = FrozenLog.dao.findByDateAndStatus(0, start, end);
        for (FrozenLog frozenLog : frozenLogs) {
            DriverInfo driverInfo = DriverInfo.dao.findByLoginId(frozenLog.getLoginId());
            if (driverInfo == null) {
                continue;
            }
            DriverInfo.dao.setNoFrozen(driverInfo.getId());
            logger.info("解冻司机:" + driverInfo.getPhone());
        }
    }
}
