package jobs;

import base.Constant;
import jobs.sendnotice.SendNotice;
import models.sys.Notice;
import net.dreamlu.event.EventKit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.IpUtils;

/**
 * Created by admin on 2016/11/14.
 */
public class SendNoticeJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = IpUtils.getLocalIP();
        if (ip.equals(Constant.MASTER_2_IP)) {
            EventKit.post(new SendNotice(new Notice()));
        }
    }
}
