package jobs.activity;

import models.member.MemberLogin;
import models.order.Order;
import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import services.ActivityService;
import services.PayService;

/**
 * Created by admin on 2016/10/7.
 */
@Listener(enableAsync = true)
public class RegisterActivityListener implements ApplicationListener<RegisterActivity> {
    @Override
    public void onApplicationEvent(RegisterActivity registerActivity) {
        MemberLogin memberLogin = (MemberLogin) registerActivity.getSource();
        ActivityService.getInstance().joinRegisterActivity(memberLogin);
    }
}
