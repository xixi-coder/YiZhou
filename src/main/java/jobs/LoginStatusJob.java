package jobs;

import base.Constant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.LoginStatusService;
import utils.IpUtils;

/**
 * Created by BOGONj on 2016/9/8.
 */
public class LoginStatusJob implements Job {
    private Logger logger = LoggerFactory.getLogger(LoginStatusJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = IpUtils.getLocalIP();
        logger.info("===============本机IP:{}========>>>>>>>>>",ip);
        if (ip.equals(Constant.MASTER_2_IP)) {
            LoginStatusService.getIntance().updateStatus();
        }
    }
}
