package jobs.pushtocustomer;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * Created by admin on 2016/10/24.
 */
public class PushToPay extends ApplicationEvent {
    public PushToPay(Object source) {
        super(source);
    }
}
