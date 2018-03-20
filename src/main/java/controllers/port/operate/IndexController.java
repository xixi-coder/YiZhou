package controllers.port.operate;

import annotation.Controller;
import base.controller.BaseController;
import models.driver.DriverOnlineDetail;
import models.order.OrderLog;
import models.portModels.*;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/operate")
public class IndexController extends BaseController {

    //1.车辆经营上线接口
    public void login() {
       // List<DriverOnlineDetail> driverOnlineDetails = DriverOnlineDetail.dao.findLogin();
        List<A32> driverOnlineDetails= A32.dao.findCompany();
        renderPortAjaxSuccess("operateLogin", driverOnlineDetails);
    }

    //2.车辆经营下线接口
    public void logout() {
       // List<DriverOnlineDetail> driverOnlineDetails = DriverOnlineDetail.dao.findLogout();
        List<A34> driverOnlineDetails= A34.dao.findCompany();
        renderPortAjaxSuccess("operateLogout", driverOnlineDetails);
    }

    //3.经营出发接口
    public void depart() {
      //  List<OrderLog> orderLogs = OrderLog.dao.start();
        List<A36> orderLogs= A36.dao.findCompany();
        renderPortAjaxSuccess("operateDepart", orderLogs);
    }

    //4.经营到达接口
    public void arrive() {
      //  List<OrderLog> orderLogs = OrderLog.dao.arrive();
        List<A38> orderLogs= A38.dao.findCompany();
        renderPortAjaxSuccess("operateArrive", orderLogs);
    }

    //5.经营支付接口
    public void pay() {
       // List<OrderLog> orderLogs = OrderLog.dao.pay();
        List<A40> orderLogs= A40.dao.findCompany();
        renderPortAjaxSuccess("operatePay", orderLogs);
    }
}
