package jobs.pay;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * Created by admin on 2016/10/7.
 */
public class OrderPay extends ApplicationEvent {
    public OrderPay(Object source) {
        super(source);
    }

}
