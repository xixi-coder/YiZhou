package jobs.pushtocustomer;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class PushToCustomerListener implements ApplicationListener<PushToCustomer> {
    @Override
    public void onApplicationEvent(PushToCustomer pushOrder) {
        Order order = (Order) pushOrder.getSource();
        PushOrderService.getIntance().pushToCustomer(order);
    }
}
