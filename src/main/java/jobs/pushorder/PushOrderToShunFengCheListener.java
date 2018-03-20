package jobs.pushorder;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class PushOrderToShunFengCheListener implements ApplicationListener<PushOrderToShunFengChe> {
    @Override
    public void onApplicationEvent(PushOrderToShunFengChe pushOrder) {
        Order order = (Order) pushOrder.getSource();
        PushOrderService.getIntance().pushToShunFengChe(order);
    }
}
