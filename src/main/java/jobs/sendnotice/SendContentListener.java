package jobs.sendnotice;

import models.sys.Notice;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.SendNoticeService;

/**
 * Created by admin on 2016/11/14.
 */
@Listener(enableAsync = true)
public class SendContentListener implements ApplicationListener<SendContent> {
    @Override
    public void onApplicationEvent(SendContent sendNotice) {
        Notice notice = (Notice) sendNotice.getSource();
        SendNoticeService.getInstance().send(notice);
    }
}
