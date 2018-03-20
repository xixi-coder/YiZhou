package jobs.yuyueorder;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.OrderService;

/**
 * Created by admin on 2016/12/2.
 */
@Listener(enableAsync = true)
public class CancelOrderListener implements ApplicationListener<CancelOrder> {
    @Override
    public void onApplicationEvent(CancelOrder cancelOrder) {
        String a = (String) cancelOrder.getSource();
        OrderService.getInstance().cancelyuyue();
    }
}
