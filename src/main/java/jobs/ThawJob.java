package jobs;

import base.Constant;
import jobs.frozen.Thaw;
import jobs.yuyueorder.CancelOrder;
import jobs.yuyueorder.Yuyueduanxintixing;
import jobs.yuyueorder.Yuyueduanxintixing5;
import net.dreamlu.event.EventKit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import utils.IpUtils;

/**
 * Created by admin on 2016/11/14.
 */
public class ThawJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = IpUtils.getLocalIP();
        if (ip.equals(Constant.MASTER_2_IP)) {
            EventKit.post(new Thaw("1"));//解除冻结
            EventKit.post(new CancelOrder("1"));//取消预约订单
            EventKit.post(new Yuyueduanxintixing("1"));//预约订单提醒
            EventKit.post(new Yuyueduanxintixing5("1"));//预约订单后5分钟提醒
        }
        
    }
}
