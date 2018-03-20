package jobs.pay;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PayService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class OrderPayListener implements ApplicationListener<OrderPay> {
    @Override
    public void onApplicationEvent(OrderPay orderPay) {
        Order order = (Order) orderPay.getSource();
        PayService.getInstance().pay(order);
    }
}
