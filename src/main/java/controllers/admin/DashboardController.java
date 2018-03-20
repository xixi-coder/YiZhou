package controllers.admin;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Maps;
import models.order.Order;
import models.order.OrderTrip;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;

/**
 * Created by BOGONj on 2016/9/14.
 */
@Controller("/admin/dashboard")
public class DashboardController extends BaseAdminController {
    public void index() {
//        setAttr("ok", "hao de ");
    }

    public void sendMessage() {
        Map<String, Object> message = Maps.newHashMap();
        message.put("type", "MESSAGE");
        Order order = new Order();
        order.put("real_name", "张杰");
        order.put("id",1);
        order.put("time", DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss")));
        order.put("service_type", 1);
        message.put("data", order);
//        WebSocketService.getInstance().sendMessage(getUserId() + "", message);
        renderAjaxSuccess("aa");
    }

    public void text() throws InterruptedException {
        boolean ok = getParaToBoolean(0, false);
//        PushOrderService.getIntance().text(ok);
        renderText("aaa");
    }

    public void updateOrder() {
        int orderId = getParaToInt(0, 0);
        Order order = Order.dao.findById(orderId);
        order.setStatus(Constant.OrderStatus.ACCEPT);
    }

    public void a() {
        Order o = new Order();
        OrderTrip orderTrip = new OrderTrip();
        orderTrip.setOrderId(1);
        o.put("trip", orderTrip);
        renderAjaxSuccess(o);
    }
}
