package jobs.activity;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.ActivityService;

/**
 * Created by admin on 2016/11/29.
 */
@Listener(enableAsync = true)
public class OrderActivityListener implements ApplicationListener<OrderActivity> {
    @Override
    public void onApplicationEvent(OrderActivity orderActivity) {
        Order oder = (Order) orderActivity.getSource();
        ActivityService.getInstance().joinActivity(oder);
    }
}
