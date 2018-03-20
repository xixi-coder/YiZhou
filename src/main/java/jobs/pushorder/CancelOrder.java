package jobs.pushorder;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * Created by admin on 2016/12/2.
 */
public class CancelOrder extends ApplicationEvent {
    public CancelOrder(Object source) {
        super(source);
    }
}
