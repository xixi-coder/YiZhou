package jobs.pushtocustomer;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class PushToCustomerConfigListener implements ApplicationListener<PushToCustomerConfig> {
    @Override
    public void onApplicationEvent(PushToCustomerConfig pushOrder) {
        Order order = (Order) pushOrder.getSource();
        PushOrderService.getIntance().customerConfig(order);
    }
}
