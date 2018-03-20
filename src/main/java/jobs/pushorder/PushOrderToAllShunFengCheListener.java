package jobs.pushorder;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class PushOrderToAllShunFengCheListener implements ApplicationListener<PushOrderToAllShunFengChe> {
    @Override
    public void onApplicationEvent(PushOrderToAllShunFengChe pushOrder) {
        Order order = (Order) pushOrder.getSource();
        PushOrderService.getIntance().pushChenjiDriver(order);
    }
}
