package jobs;

import base.Constant;
import models.driver.DriverInfo;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import services.OnlineService;
import utils.IpUtils;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/8.
 */
public class TimeTotalJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = IpUtils.getLocalIP();
        if (ip.equals(Constant.MASTER_2_IP)) {
            List<DriverInfo> driverInfos = DriverInfo.dao.findForOnLine();
            OnlineService.getInstance().autoRecovery(DateTime.now(), driverInfos);
        }
    }
}