package jobs.pushorder;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class PushOrderToSpecialListener implements ApplicationListener<PushOrderToSpecial> {
    @Override
    public void onApplicationEvent(PushOrderToSpecial pushOrder) {
        Order order = (Order) pushOrder.getSource();
        PushOrderService.getIntance().pushOrderToSpecialDriver(order);
    }
}
