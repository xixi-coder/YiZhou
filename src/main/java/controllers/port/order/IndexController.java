package controllers.port.order;

import annotation.Controller;
import base.controller.BaseController;
import models.order.Order;
import models.portModels.A42;
import models.portModels.A44;
import models.portModels.A46;

import java.util.List;


/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/order")
public class IndexController extends BaseController {

    //1.订单发起接口
    public void create() {
       // List<Order> orders = Order.dao.findOrders();
        List<A42> orders= A42.dao.findCompany();
        renderPortAjaxSuccess("orderCreate", orders);
    }

    //2.订单成功接口
    public void match() {
       // List<Order> orders = Order.dao.findSuccess();
        List<A44> orders= A44.dao.findCompany();
        renderPortAjaxSuccess("orderMatch", orders);
    }

    //3.订单撤销接口
    public void cancel() {
        //List<Order> orders = Order.dao.findDestroy();
        List<A46> orders= A46.dao.findCompany();
        renderPortAjaxSuccess("orderCancel", orders);
    }

}
