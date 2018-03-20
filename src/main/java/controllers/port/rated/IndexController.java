package controllers.port.rated;

import annotation.Controller;
import base.controller.BaseController;
import models.portModels.A52;
import models.portModels.A54;
import models.portModels.A56;
import models.portModels.A58;

import java.util.List;


/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/rated")
public class IndexController extends BaseController {

    //1.乘客评价信息接口
    public void passenger() {
       // List<Grade> grade = Grade.dao.passenger();
        List<A52> grade= A52.dao.findCompany();
        renderPortAjaxSuccess("ratedPassenger",grade);
    }

    //2.乘客投诉信息接口
    public void passengercomplaint() {
        //List<MemberComplain> passengercomplaint = MemberComplain.dao.passengercomplaint();
        List<A54> passengercomplaint= A54.dao.findCompany();
        renderPortAjaxSuccess("ratedPassengerComplaint",passengercomplaint);
    }

    //3.驾驶员处罚信息接口
    public void driverpunish() {
       // List<DriverPunish> driverpunish = DriverPunish.dao.driverpunish();
        List<A56> driverpunish= A56.dao.findCompany();
        renderPortAjaxSuccess("ratedDriverPunish",driverpunish);
    }

    //4.驾驶员信誉信息接口
    public void driver() {
       // List<DriverCredit> driver = DriverCredit.dao.driver();
        List<A58> driver= A58.dao.findCompany();
        renderPortAjaxSuccess("ratedDriver",driver);
    }

}
