package jobs.pushorder;

import kits.SmsKit;
import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.PushOrderService;

/**
 * Created by admin on 2016/12/2.
 */
@Listener(enableAsync = true)
public class CancelOrderListener implements ApplicationListener<CancelOrder> {
    @Override
    public void onApplicationEvent(CancelOrder cancelOrder) {
        Order order = (Order) cancelOrder.getSource();
        PushOrderService.getIntance().pushDriverCancel(order);//推送
        SmsKit.ordercancel(order);//短信通知
    }
}
