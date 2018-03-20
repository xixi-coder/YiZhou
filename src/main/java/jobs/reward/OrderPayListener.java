package jobs.reward;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.RewardService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class OrderPayListener implements ApplicationListener<Reward> {
    @Override
    public void onApplicationEvent(Reward orderPay) {
        Order order = (Order) orderPay.getSource();
        RewardService.getInstance().rewardall(order);
    }
}
