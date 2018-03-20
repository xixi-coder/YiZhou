package jobs.yuyueorder;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.OrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class Yuyueduanxintixing5Listener implements ApplicationListener<Yuyueduanxintixing5> {
    @Override
    public void onApplicationEvent(Yuyueduanxintixing5 yuyueduanxintixing5) {
        String a = (String) yuyueduanxintixing5.getSource();
        OrderService.getInstance().yuyuetixing2();
    }
}
