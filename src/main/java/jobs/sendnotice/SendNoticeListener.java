package jobs.sendnotice;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.SendNoticeService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class SendNoticeListener implements ApplicationListener<SendNotice> {
    @Override
    public void onApplicationEvent(SendNotice sendNotice) {
        System.out.println("定时发送短信");
        SendNoticeService.getInstance().send();
    }
}
