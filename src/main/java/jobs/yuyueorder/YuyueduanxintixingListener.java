package jobs.yuyueorder;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.OrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class YuyueduanxintixingListener implements ApplicationListener<Yuyueduanxintixing> {
    @Override
    public void onApplicationEvent(Yuyueduanxintixing yuyueduanxintixing) {
        String a = (String) yuyueduanxintixing.getSource();
        OrderService.getInstance().yuyuetixing1();
    }
}
