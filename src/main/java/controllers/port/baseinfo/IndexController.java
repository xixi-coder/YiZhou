package controllers.port.baseinfo;

import annotation.Controller;
import base.controller.BaseController;
import jobs.SendJson;
import models.portModels.*;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller("/baseinfo")
public class IndexController extends BaseController {
    public void index() {
        renderText("aaa");
    }

    //1.网约车平台公司基本信息接口s
    public void company() {
        //BaseInfoCompany baseinfoCompany = BaseInfoCompany.dao.findCompany();
        List baseinfoCompany= A4.dao.findCompany();
        renderPortAjaxSuccess("baseInfoCompany", baseinfoCompany);
    }

    //2.网约车平台公司运营规模信息接口
    public void companystat() {
       // DriverInfo driverInfo = DriverInfo.dao.findDriverAndCar();
        List driverInfo= A6.dao.findCompany();
        renderPortAjaxSuccess("baseInfoDriverInfo", driverInfo);
    }

    //3.网约车平台公司支付信息接口
    public void companypay() {
        //List<CompanyPay> companypay = CompanyPay.dao.companypay();
        List<A8> baseInfoCompanyPay= A8.dao.findCompany();
        renderPortAjaxSuccess("baseInfoCompanyPay", baseInfoCompanyPay);
    }

    //4.网约车平台公司服务机构接口
    public void companyservice() {
       // List<CompanyServe> companyservice = CompanyServe.dao.companyservice();
        List<A10> companyservice= A10.dao.findCompany();
        renderPortAjaxSuccess("baseInfoCompanyService", companyservice);
    }

    //5.网约车平台公司经营许可接口
    public void companypermit() {
       // List<CompanyPermit> companypermit = CompanyPermit.dao.companypermit();
        List<A12> baseInfoCompanyPermit= A12.dao.findCompany();
        renderPortAjaxSuccess("baseInfoCompanyPermit", baseInfoCompanyPermit);
    }

    //6.网约车平台公司运价信息接口
    public void companyfare() {
        //List<CompanyService> companyfare = CompanyService.dao.companyfare();
        List<A14> companyfare= A14.dao.findCompany();
        renderPortAjaxSuccess("baseInfoCompanyFare", companyfare);
    }

    //7.车辆基本信息接口
    public void vehicle() {
        //List<Car> vehicle = Car.dao.vehicle();
        List<A16> vehicle= A16.dao.findCompany();
        renderPortAjaxSuccess("baseInfoVehicle", vehicle);
    }

    //8.车辆保险信息接口
    public void vehicleinsurance() {
      //  List<Car> vehicleinsurance = Car.dao.vehicleinsurance();
        List<A18> vehicleinsurance= A18.dao.findCompany();
        renderPortAjaxSuccess("baseInfoVehicleInsurance", vehicleinsurance);
    }

    //9.网约车车辆里程信息接口
    public void vehicletotalmile() {
       // List<CarInfo> vehicletotalmile = CarInfo.dao.vehicletotalmile();
        List<A20> vehicletotalmile= A20.dao.findCompany();
        renderPortAjaxSuccess("baseInfoVehicleTotalmile", vehicletotalmile);
    }

    //10.驾驶员基本信息接口
    public void driver() {
       // List<DriverInfo> driver = DriverInfo.dao.driver();
        List<A22> driver= A22.dao.findCompany();
        renderPortAjaxSuccess("baseInfoDriver", driver);
    }

    //11.网约车驾驶员培训信息接口
    public void drivereducate() {
       // List<DriverTrain> drivereducate = DriverTrain.dao.drivereducate();
        List<A24> drivereducate= A24.dao.findCompany();
        renderPortAjaxSuccess("baseInfoDriverEducate", drivereducate);
    }

    //12.驾驶员移动终端信息接口
    public void driverapp() {
       // List<DriverApp> driverapp = DriverApp.dao.driverapp();
        List<A26> driverapp= A26.dao.findCompany();
        renderPortAjaxSuccess("baseInfoDriverApp", driverapp);
    }

    //13.网约车驾驶员统计接口
    public void driverstat() {
       // List<DriverInfo> driverstat = DriverInfo.dao.driverstat();
        List<A28> driverstat= A28.dao.findCompany();
        renderPortAjaxSuccess("baseInfoDriverStat", driverstat);
    }

    //14.乘客基本信息接口
    public void passenger() {
       // List<MemberLogin> passenger = MemberLogin.dao.passenger();
        List<A30> passenger= A30.dao.findCompany();
        renderPortAjaxSuccess("baseInfoPassenger", passenger);
    }

    public  void  SendJson(){
        SendJson sendJson = new SendJson();
        renderPortAjaxSuccess("SendJson", sendJson.send());
    }

    public  void  SendJson1(){
        SendJson sendJson = new SendJson();
        renderPortAjaxSuccess("SendJson1", sendJson.send1());
    }

    public  void  sendJsonForLocation(){
        SendJson sendJson = new SendJson();
        renderPortAjaxSuccess("SendJson", sendJson.sendJsonForLocation());
    }

    public  void  sendJsonerror(){
        SendJson sendJson = new SendJson();
        renderPortAjaxSuccess("SendJson", sendJson.sendJsonerror());
    }



}
