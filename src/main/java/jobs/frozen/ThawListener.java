package jobs.frozen;

import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.ThawService;

/**
 * Created by admin on 2016/11/29.
 */
@Listener(enableAsync = true)
public class ThawListener implements ApplicationListener<Thaw> {
    @Override
    public void onApplicationEvent(Thaw frozen) {
        String a = (String) frozen.getSource();
        ThawService.getInstance().thaw();
    }
}
