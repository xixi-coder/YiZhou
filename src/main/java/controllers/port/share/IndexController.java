package controllers.port.share;

import annotation.Controller;
import base.controller.BaseController;
import models.portModels.A60;
import models.portModels.A62;
import models.portModels.A64;
import models.portModels.A66;

import java.util.List;


/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/share")
public class IndexController extends BaseController {

    //1.私人小客车合乘信息 服务平台基本信息
    public void company() {
       // List<baseInfoCompany> baseInfoCompanyList = baseInfoCompany.dao.findAll();
        List<A60> baseInfoCompanyList= A60.dao.findCompany();
        renderPortAjaxSuccess("shareCompany", baseInfoCompanyList);
    }

    //2.私人小客车合乘驾驶员行程发布信息
    public void route() {
       // List<Order> orderList = Order.dao.shareRoute();
        List<A62> orderList= A62.dao.findCompany(0,1000);
        renderPortAjaxSuccess("shareRoute", orderList);
    }

    //3.私人小客车合乘订单发布信息
    public void order() {
       // List<Order> orderList = Order.dao.shareOrder();
        List<A64> orderList= A64.dao.findCompany();
        renderPortAjaxSuccess("shareOrder", orderList);
    }

    // 私人小客车合乘订单支付信息
    public void pay() {
        //List<Order> orderList = Order.dao.sharePay();
        List<A66> orderList= A66.dao.findCompany();
        renderPortAjaxSuccess("sharePay", orderList);
    }

}
