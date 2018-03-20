package controllers.port.position;

import annotation.Controller;
import base.controller.BaseController;
import models.driver.DriverRealLocation;
import models.portModels.A48;
import models.portModels.A50;

import java.util.List;


/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/position")
public class IndexController extends BaseController {

    //1.驾驶员定位信息接口
    public void driver() {
       // List<DriverRealLocation> driver = DriverRealLocation.dao.driver();
        List<A48> driver= A48.dao.findCompany();
        renderPortAjaxSuccess("positionDriver", driver);
    }

    //2.车辆定位信息接口
    public void vehicle() {
      //  List<DriverRealLocation> vehicle = DriverRealLocation.dao.vehicle();
        List<A50> vehicle= A50.dao.findCompany();
        renderPortAjaxSuccess("positionVehicle", vehicle);
    }

}
