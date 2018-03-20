package jobs.rechange;

import models.member.CapitalLog;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.RechangeService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class RechangeListener implements ApplicationListener<Rechange> {
    @Override
    public void onApplicationEvent(Rechange orderPay) {
        CapitalLog capitalLog = (CapitalLog) orderPay.getSource();
        RechangeService.getInstance().rechange(capitalLog);
    }
}
