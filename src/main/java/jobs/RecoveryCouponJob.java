package jobs;

import base.Constant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import services.RecoveryCouponService;
import utils.IpUtils;

/**
 * Created by admin on 2016/12/29.
 */
public class RecoveryCouponJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = IpUtils.getLocalIP();
        if (ip.equals(Constant.MASTER_2_IP)) {
            RecoveryCouponService.getInstance().recovery();
        }
    }
}
