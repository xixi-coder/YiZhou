package jobs;

import base.Constant;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import models.portModels.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by admin on 2017/4/10.
 * 数据推送
 */
public class SendJson implements Job {

    //测试数据推送地址为
//    String url = "172.16.5.101:8080"; //交通部
//    String url = "112.124.105.30:8080"; //测试
String url = "61.132.202.69:8080"; //交通部合肥

    StringBuffer smsg = new StringBuffer();
    StringBuffer emsg = new StringBuffer();

    String startDate = "";
    String endDate = "";

//    int number1 = 0;
//    int number2 = 50000;

//    int number1 = 10000;
//    int number2 = 20000;
//
//    int number1 = 20000;
//    int number2 = 30000;

//    int number1 = 30000;
//    int number2 = 40000;

//    int number1 = 40000;
//    int number2 = 50000;
//
//    int number1 = 50000;
//    int number2 = 60000;
//
//    int number1 = 60000;
//    int number2 = 70000;
//
//    int number1 = 70000;
//    int number2 = 80000;
//
//    int number1 = 80000;
//    int number2 = 90000;
//
//    int number1 = 90000;
//    int number2 = 100000;
//
//    int number1 = 100000;
//    int number2 = 110000;

    int s = 0;
    int e = 0;


    public  static  Map<String, Object> putJson(){
        Map<String, Object> baseInfoCompanyService = new HashMap<String, Object>();
        baseInfoCompanyService.put("CompanyId", "sjdd");
        baseInfoCompanyService.put("Address", 340100);
        baseInfoCompanyService.put("ServiceName", "安徽华格科技有限公司");
        baseInfoCompanyService.put("ServiceNo", "91340100MA2N12W73B(1-1)");
        baseInfoCompanyService.put("DetailAddress", "安徽省合肥市包河区闽商大厦2901");
        baseInfoCompanyService.put("ResponsibleName", "黄群");
        baseInfoCompanyService.put("ResponsiblePhone", "13671878755");
        baseInfoCompanyService.put("ManagerName", "黄群");
        baseInfoCompanyService.put("ManagerPhone", "13671878755");
        baseInfoCompanyService.put("ContactPhone", "13671878755");
        baseInfoCompanyService.put("MailAddress", "安徽省合肥市包河区闽商大厦2901");
        baseInfoCompanyService.put("State", 0);
        baseInfoCompanyService.put("Flag", 1);
        baseInfoCompanyService.put("CreateDate", 20170626);
        baseInfoCompanyService.put("UpdateTime", 20161101151703l);

        return baseInfoCompanyService;

    }








    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String startDate = strDate(new Date());
        System.out.println("数据推送定时任务开始");
        System.out.println("开始时间: " + startDate + "\n");
        //运行并打印信息
        System.out.println(send());
        String endDate = strDate(new Date());
        System.out.println("\n数据推送定时任务结束");
        System.out.println("结束时间: " + endDate);
        System.out.println("总耗时: " + wasteTime(startDate, endDate) + "\n");
    }


    public String send1(){
        //1.网约车平台公司基本信息接口
        startDate = strDate(new Date());
        /*BaseInfoCompany baseInfoCompany = BaseInfoCompany.dao.findCompany();*/
        List baseInfoCompany= A4.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/company", json("baseInfoCompany", baseInfoCompany)) > 0) {
            smsg.append("baseInfoCompany\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompany\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompan接口耗时:" + wasteTime(startDate, endDate));
        return "成功！";
    }

    //推送
    public  String send() {


       //1.网约车平台公司基本信息接口
        startDate = strDate(new Date());
        /*BaseInfoCompany baseInfoCompany = BaseInfoCompany.dao.findCompany();*/
        List baseInfoCompany= A4.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/company", json("baseInfoCompany", baseInfoCompany)) > 0) {
            smsg.append("baseInfoCompany\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompany\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompan接口耗时:" + wasteTime(startDate, endDate));


      //2.网约车平台公司运营规模信息接口
        startDate = strDate(new Date());
        /*DriverInfo baseInfoCompanyStat = DriverInfo.dao.findDriverAndCar();*/
        List baseInfoCompanyStat= A6.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/companystat", json("baseInfoCompanyStat", baseInfoCompanyStat)) > 0) {
            smsg.append("baseInfoCompanyStat\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompanyStat\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompanyStat接口耗时:" + wasteTime(startDate, endDate));

        //3.网约车平台公司支付信息接口
        startDate = strDate(new Date());
       /* List<CompanyPay> baseInfoCompanyPay = CompanyPay.dao.companypay();*/
        List<A8> baseInfoCompanyPay= A8.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/companypay", json("baseInfoCompanyPay", baseInfoCompanyPay)) > 0) {
            smsg.append("baseInfoCompanyPay\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompanyPay\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompanyPay接口耗时:" + wasteTime(startDate, endDate));

       //4.网约车平台公司服务机构接口
        startDate = strDate(new Date());
       /* List<CompanyServe> baseInfoCompanyService = CompanyServe.dao.companyservice();
        System.out.println(baseInfoCompanyService.size());*/
        List<A10> baseInfoCompanyService= A10.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/companyservice", json("baseInfoCompanyService", baseInfoCompanyService )) > 0) {
            smsg.append("baseInfoCompanyService\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompanyService\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompanyService接口耗时:" + wasteTime(startDate, endDate));

        //5.网约车平台公司经营许可接口
        startDate = strDate(new Date());
       /* List<CompanyPermit> baseInfoCompanyPermit = CompanyPermit.dao.companypermit();
        System.out.println(baseInfoCompanyPermit.size());*/
        List<A12> baseInfoCompanyPermit= A12.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/companypermit", json("baseInfoCompanyPermit", baseInfoCompanyPermit )) > 0) {
            smsg.append("baseInfoCompanyPermit\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompanyPermit\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompanyPermit接口耗时:" + wasteTime(startDate, endDate));

        //6.网约车平台公司运价信息接口
        startDate = strDate(new Date());
        /*List<CompanyService> baseInfoCompanyFare = CompanyService.dao.companyfare();
        System.out.println(baseInfoCompanyFare.size());*/
        List<A14> baseInfoCompanyFare= A14.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/companyfare", json("baseInfoCompanyFare", baseInfoCompanyFare )) > 0) {
            smsg.append("baseInfoCompanyFare\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoCompanyFare\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoCompanyFare接口耗时:" + wasteTime(startDate, endDate));

        //7.车辆基本信息接口
        startDate = strDate(new Date());
       /* List<Car> baseInfoVehicle = Car.dao.vehicle();
        System.out.println(baseInfoVehicle.size());*/
        List<A16> baseInfoVehicle= A16.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/vehicle", json("baseInfoVehicle", baseInfoVehicle)) > 0) {
            smsg.append("baseInfoVehicle\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoVehicle\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoVehicle接口耗时:" + wasteTime(startDate, endDate));

        //8.车辆保险信息接口
        startDate = strDate(new Date());
        /*List<Car> baseInfoVehicleInsurance = Car.dao.vehicleinsurance();
        System.out.println(baseInfoVehicleInsurance.size());*/
        List<A18> baseInfoVehicleInsurance= A18.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/vehicleinsurance", json("baseInfoVehicleInsurance", baseInfoVehicleInsurance )) > 0) {
            smsg.append("baseInfoVehicleInsurance\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoVehicleInsurance\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoVehicleInsurance接口耗时:" + wasteTime(startDate, endDate));

        //9.网约车车辆里程信息接口
        startDate = strDate(new Date());
       /* List<CarInfo> baseInfoVehicleTotalmile = CarInfo.dao.vehicletotalmile();
        System.out.println(baseInfoVehicleTotalmile.size());*/
        List<A20> baseInfoVehicleTotalmile= A20.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/vehicletotalmile", json("baseInfoVehicleTotalMile", baseInfoVehicleTotalmile )) > 0) {
            smsg.append("baseInfoVehicleTotalMile\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoVehicleTotalMile\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoVehicleTotalMile接口耗时:" + wasteTime(startDate, endDate));

        //10.驾驶员基本信息接口
        startDate = strDate(new Date());
        /*List<DriverInfo> baseInfoDriver = DriverInfo.dao.driver();
        System.out.println(baseInfoDriver.size());*/
        List<A22> baseInfoDriver= A22.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/driver", json("baseInfoDriver", baseInfoDriver )) > 0) {
            smsg.append("baseInfoDriver\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoDriver\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoDriver接口耗时:" + wasteTime(startDate, endDate));

        //11.网约车驾驶员培训信息接口
        startDate = strDate(new Date());
       /* List<DriverTrain> baseInfoDriverEducate = DriverTrain.dao.drivereducate();
        System.out.println(baseInfoDriverEducate.size());*/
        List<A24> baseInfoDriverEducate= A24.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/drivereducate", json("baseInfoDriverEducate", baseInfoDriverEducate )) > 0) {
            smsg.append("baseInfoDriverEducate\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoDriverEducate\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoDriverEducate接口耗时:" + wasteTime(startDate, endDate));

        //12.驾驶员移动终端信息接口
        startDate = strDate(new Date());
       /* List<DriverApp> baseInfoDriverApp = DriverApp.dao.driverapp();
        System.out.println(baseInfoDriverApp.size());*/
        List<A26> baseInfoDriverApp= A26.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/driverapp", json("baseInfoDriverApp", baseInfoDriverApp )) > 0) {
            smsg.append("baseInfoDriverApp\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoDriverApp\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoDriverApp接口耗时:" + wasteTime(startDate, endDate));

        //13.网约车驾驶员统计接口
        startDate = strDate(new Date());
        /*List<DriverInfo> baseInfoDriverStat = DriverInfo.dao.driverstat();
        System.out.println(baseInfoDriverStat.size());*/
        List<A28> baseInfoDriverStat= A28.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/driverstat", json("baseInfoDriverStat", baseInfoDriverStat )) > 0) {
            smsg.append("baseInfoDriverStat\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoDriverStat\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoDriverStat接口耗时:" + wasteTime(startDate, endDate));


        //14.乘客基本信息接口
        startDate = strDate(new Date());
        /*List<MemberLogin> baseInfoPassenger = MemberLogin.dao.passenger();
        System.out.println(baseInfoPassenger.size());*/
        List<A30> baseInfoPassenger= A30.dao.findCompany();
        if (sendPost_JSON(url+"/baseinfo/passenger", json("baseInfoPassenger", baseInfoPassenger )) > 0) {
            smsg.append("baseInfoPassenger\n");
            s = s + 1;
        } else {
            emsg.append("baseInfoPassenger\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("baseInfoPassenger接口耗时:" + wasteTime(startDate, endDate));


        //15.订单发起接口
        startDate = strDate(new Date());
        /*List<DriverOnlineDetail> operateLogin = DriverOnlineDetail.dao.findLogin();
        System.out.println(operateLogin.size());*/
        List<A32> orderCreate= A32.dao.findCompany();
        if (sendPost_JSON(url+"/order/create", json("orderCreate", orderCreate )) > 0) {
            smsg.append("orderCreate\n");
            s = s + 1;
        } else {
            emsg.append("orderCreate\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("orderCreate接口耗时:" + wasteTime(startDate, endDate));

        //16.订单成功接口
        startDate = strDate(new Date());
       /* List<DriverOnlineDetail> operateLogout = DriverOnlineDetail.dao.findLogout();
        System.out.println(operateLogout.size());*/
        List<A34> orderMatch= A34.dao.findCompany();
        if (sendPost_JSON(url+"/order/match", json("orderMatch", orderMatch )) > 0) {
            smsg.append("orderMatch\n");
            s = s + 1;
        } else {
            emsg.append("orderMatch\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("orderMatch接口耗时:" + wasteTime(startDate, endDate));

        //17.订单撤销接口
        startDate = strDate(new Date());
        /*List<OrderLog> operateDepart = OrderLog.dao.start();
        System.out.println(operateDepart.size());*/
        List<A36> orderCancel= A36.dao.findCompany();
        if (sendPost_JSON(url+"/order/cancel", json("orderCancel", orderCancel)) > 0) {
            smsg.append("orderCancel\n");
            s = s + 1;
        } else {
            emsg.append("orderCancel\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("orderCancel接口耗时:" + wasteTime(startDate, endDate));


        //18.车辆上线接口
        startDate = strDate(new Date());
        /*List<OrderLog> operateArrive = OrderLog.dao.arrive();
        System.out.println(operateArrive.size());*/

        List<A38> operateLogin= A38.dao.findCompany();
        if (sendPost_JSON(url+"/operate/login", json("operateLogin", operateLogin)) > 0) {
            smsg.append("operateLogin\n");
            s = s + 1;
        } else {
            emsg.append("operateLogin\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("operateLogin接口耗时:" + wasteTime(startDate, endDate));

        //19.车辆下线接口
        startDate = strDate(new Date());
        /*List<OrderLog> operatePay = OrderLog.dao.pay();
        System.out.println(operatePay.size());*/
        List<A40> operateLogout= A40.dao.findCompany();
        if (sendPost_JSON(url+"/operate/logout", json("operateLogout", operateLogout )) > 0) {
            smsg.append("operateLogout\n");
            s = s + 1;
        } else {
            emsg.append("operateLogout\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("operateLogout接口耗时:" + wasteTime(startDate, endDate));

        //20.出发接口
        startDate = strDate(new Date());
       /* List<Order> orderCreate = Order.dao.findOrders();
        System.out.println(orderCreate.size());*/
        List<A42> operateDepart= A42.dao.findCompany();
        if (sendPost_JSON(url+"/operate/depart", json("operateDepart", operateDepart)) > 0) {
            smsg.append("operateDepart\n");
            s = s + 1;
        } else {
            emsg.append("operateDepart\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("operateDepart接口耗时:" + wasteTime(startDate, endDate));

        //21.到达接口
        startDate = strDate(new Date());
        /*List<Order> orderMatch = Order.dao.findSuccess();
        System.out.println(orderMatch.size());*/
        List<A44> operateArrive= A44.dao.findCompany();
        if (sendPost_JSON(url+"/operate/arrive", json("operateArrive", operateArrive )) > 0) {
            smsg.append("operateArrive\n");
            s = s + 1;
        } else {
            emsg.append("operateArrive\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("operateArrive接口耗时:" + wasteTime(startDate, endDate));

        //22.支付接口
        startDate = strDate(new Date());
        /*List<Order> orderCancel = Order.dao.findDestroy();
        System.out.println(orderCancel.size());*/
        List<A46> operatePay= A46.dao.findCompany();
        if (sendPost_JSON(url+"/operate/pay", json("operatePay", operatePay )) > 0) {
            smsg.append("operatePay\n");
            s = s + 1;
        } else {
            emsg.append("operatePay\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("operatePay接口耗时:" + wasteTime(startDate, endDate));

       /* //23.驾驶员定位信息接口 //todo ok
        startDate = strDate(new Date());
        *//*List<DriverRealLocation> positionDriver = DriverRealLocation.dao.driver();
        System.out.println(positionDriver.size());*//*
        List<A48> positionDriver= A48.dao.findCompany();
        if (sendPost_JSON(url+"/position/driver", json("positionDriver", positionDriver )) > 0) {
            smsg.append("positionDriver\n");
            s = s + 1;
        } else {
            emsg.append("positionDriver\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("positionDriver接口耗时:" + wasteTime(startDate, endDate));

        //24.车辆定位信息接口
        startDate = strDate(new Date());
       *//* List<DriverRealLocation> positionVehicle = DriverRealLocation.dao.vehicle();
        System.out.println(positionVehicle.size());*//*
        List<A50> positionVehicle= A50.dao.findCompany();
        if (sendPost_JSON(url+"/position/vehicle", json("positionVehicle", positionVehicle )) > 0) {
            smsg.append("positionVehicle\n");
            s = s + 1;
        } else {
            emsg.append("positionVehicle\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("positionVehicle接口耗时:" + wasteTime(startDate, endDate));*/

        //25.乘客评价信息接口
        startDate = strDate(new Date());
        /*List<Grade> ratedPassenger = Grade.dao.passenger();
        System.out.println(ratedPassenger.size());*/
        List<A52> ratedPassenger= A52.dao.findCompany();
        if (sendPost_JSON(url+"/rated/passenger", json("ratedPassenger", ratedPassenger )) > 0) {
            smsg.append("ratedPassenger\n");
            s = s + 1;
        } else {
            emsg.append("ratedPassenger\n");
            e = e + 1;
    }
        endDate = strDate(new Date());
        System.out.println("ratedPassenger接口耗时:" + wasteTime(startDate, endDate));

        //26.乘客投诉信息接口
        startDate = strDate(new Date());
        /*List<MembeComplain> ratedPassengerComplaint = MembeComplain.dao.passengercomplaint();
        System.out.println(ratedPassengerComplaint.size());*/
        List<A54> ratedPassengerComplaint= A54.dao.findCompany();
        if (sendPost_JSON(url+"/rated/passengercomplaint", json("ratedPassengerComplaint", ratedPassengerComplaint)) > 0) {
            smsg.append("ratedPassengerComplaint\n");
            s = s + 1;
        } else {
            emsg.append("ratedPassengerComplaint\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("ratedPassengerComplaint接口耗时:" + wasteTime(startDate, endDate));

        //27.驾驶员处罚信息接口
        startDate = strDate(new Date());
       /* List<DriverPunish> ratedDriverPunish = DriverPunish.dao.driverpunish();
        System.out.println(ratedDriverPunish.size());*/
        List<A56> ratedDriverPunish= A56.dao.findCompany();
        if (sendPost_JSON(url+"/rated/driverpunish", json("ratedDriverPunish", ratedDriverPunish)) > 0) {
            smsg.append("ratedDriverPunish\n");
            s = s + 1;
        } else {
            emsg.append("ratedDriverPunish\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("ratedDriverPunish接口耗时:" + wasteTime(startDate, endDate));

        //28.驾驶员信誉信息接口
        startDate = strDate(new Date());
        /*List<DriverCredit> ratedDriver = DriverCredit.dao.driver();
        System.out.println(ratedDriver.size());*/
        List<A58> ratedDriver= A58.dao.findCompany();
        if (sendPost_JSON(url+"/rated/driver", json("ratedDriver", ratedDriver )) > 0) {
            smsg.append("ratedDriver\n");
            s = s + 1;
        } else {
            emsg.append("ratedDriver\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("ratedDriver接口耗时:" + wasteTime(startDate, endDate));

        //29.私人小客车合乘信息 服务平台基本信息
        startDate = strDate(new Date());
       /* List<models.company.baseInfoCompany> shareCompany = models.company.baseInfoCompany.dao.findAll();
        System.out.println(shareCompany.size());*/
        List<A60> shareCompany= A60.dao.findCompany();
        if (sendPost_JSON(url+"/share/company", json("shareCompany", shareCompany )) > 0) {
            smsg.append("shareCompany\n");
            s = s + 1;
        } else {
            emsg.append("shareCompany\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("shareCompany接口耗时:" + wasteTime(startDate, endDate));

        //30.私人小客车合乘驾驶员行程发布信息
        startDate = strDate(new Date());
       /* List<Order> shareRoute = Order.dao.shareRoute();
        System.out.println(shareRoute.size());*/
        List<A62> shareRoute= A62.dao.findCompany(0,1000);
        if (sendPost_JSON(url+"/share/route", json("shareRoute", shareRoute)) > 0) {
            smsg.append("shareRoute\n");
            s = s + 1;
        } else {
            emsg.append("shareRoute\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("shareRoute接口耗时:" + wasteTime(startDate, endDate));

        //31.私人小客车合乘订单发布信息
        startDate = strDate(new Date());
        /*List<Order> shareOrder = Order.dao.shareOrder();
        System.out.println(shareOrder.size());*/
        List<A64> shareOrder= A64.dao.findCompany();
        if (sendPost_JSON(url+"/share/order", json("shareOrder", shareOrder )) > 0) {
            smsg.append("shareOrder\n");
            s = s + 1;
        } else {
            emsg.append("shareOrder\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("shareOrder接口耗时:" + wasteTime(startDate, endDate));

        //32.私人小客车合乘订单支付信息
        startDate = strDate(new Date());
      /*  List<Order> sharePay = Order.dao.sharePay();
        System.out.println(sharePay.size());*/
        List<A66> sharePay= A66.dao.findCompany();
        if (sendPost_JSON(url+"/share/pay", json("sharePay", sharePay )) > 0) {
            smsg.append("sharePay\n");
            s = s + 1;
        } else {
            emsg.append("sharePay\n");
            e = e + 1;
        }
        endDate = strDate(new Date());
        System.out.println("sharePay接口耗时:" + wasteTime(startDate, endDate));
        String mst = "接口推送信息:" + s + "个成功:" + smsg + "\n\t" + e + "个失败:" + emsg;
        return mst;
    }

    public String sendJsonerror() {
        int number1 = 0;
        int number2 = 1;



        //30.私人小客车合乘驾驶员行程发布信息
        startDate = strDate(new Date());
       /* List<Order> shareRoute = Order.dao.shareRoute();
        System.out.println(shareRoute.size());*/
        List<A62> shareRoute= A62.dao.findCompany();
        for (int i = 0;i < 207;i++){
            List list = new ArrayList();
            list.add(shareRoute.get(i));
            if (sendPost_JSON(url+"/share/route", json("shareRoute", list)) > 0) {
                smsg.append("shareRoute\n");
                s = s + 1;
            } else {
                emsg.append("shareRoute\n");
                e = e + 1;
            }
            endDate = strDate(new Date());
            System.out.println("shareRoute接口耗时:" + wasteTime(startDate, endDate));
//            number1 = number2+1;
//            number2 += 10;
            System.out.println("推送数据条数据id："+shareRoute.get(i).getRouteId());

            try
            {
                startDate = strDate(new Date());
                Thread.sleep(2000);
                System.out.println("开始运行sleep！");
                endDate = strDate(new Date());
                System.out.println("sleep-耗时:"+wasteTime(startDate, endDate));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


        String mst = "接口推送信息:" + s + "个成功:" + smsg + "\n\t" + e + "个失败:" + emsg;
        return mst;


    }



    //单独推送定位信息(分批推送)
    public String sendJsonForLocation(){
        int number1 = 0;
        int number2 = 5000;
        for (int i = 0;i<3;i++){

            //23.驾驶员定位信息接口 //todo ok
            startDate = strDate(new Date());

//        List<DriverRealLocation> positionDriver = DriverRealLocation.dao.driver();
//        System.out.println(positionDriver.size());

            List<A48> positionDriver= A48.dao.findCompany(number1,number2);
            System.out.println(json("positionDriver", positionDriver ));
            if (sendPost_JSON(url+"/position/driver", json("positionDriver", positionDriver )) > 0) {
                smsg.append("positionDriver\n");
                s = s + 1;
            } else {
                emsg.append("positionDriver\n");
                e = e + 1;
            }
            endDate = strDate(new Date());
            System.out.println("positionDriver接口耗时:" + wasteTime(startDate, endDate));


            //24.车辆定位信息接口
            startDate = strDate(new Date());
//        List<DriverRealLocation> positionVehicle = DriverRealLocation.dao.vehicle();
//        System.out.println(positionVehicle.size());
            List<A50> positionVehicle= A50.dao.findCompany(number1,number2);
            System.out.println(json("positionVehicle", positionVehicle ));
            if (sendPost_JSON(url+"/position/vehicle", json("positionVehicle", positionVehicle )) > 0) {
                smsg.append("positionVehicle\n");
                s = s + 1;
            } else {
                emsg.append("positionVehicle\n");
                e = e + 1;
            }
            endDate = strDate(new Date());
            System.out.println("positionVehicle接口耗时:" + wasteTime(startDate, endDate));


            number1 = number2+1;
            number2 += 5000;
            System.out.println("推送数据条数范围："+number1+"--------"+number2);

            try
            {
                startDate = strDate(new Date());
                Thread.sleep(10000);
                System.out.println("开始运行sleep！");
                endDate = strDate(new Date());
                System.out.println("sleep-耗时:"+wasteTime(startDate, endDate));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
        String mst = "接口推送信息:" + s + "个成功:" + smsg + "\n\t" + e + "个失败:" + emsg;
        return mst;

    }

    /*//单独推送定位信息(一次性推送)
    public String sendJsonForLocationOnce(){
        int number1 = 0;
        int number2 = 15000;

            //23.驾驶员定位信息接口 //todo ok
            startDate = strDate(new Date());
        *//*List<DriverRealLocation> positionDriver = DriverRealLocation.dao.driver();
        System.out.println(positionDriver.size());*//*
            List<A48> positionDriver= A48.dao.findCompany(number1,number2);
            if (sendPost_JSON(url+"/position/driver", json("positionDriver", positionDriver )) > 0) {
                smsg.append("positionDriver\n");
                s = s + 1;
            } else {
                emsg.append("positionDriver\n");
                e = e + 1;
            }
            endDate = strDate(new Date());
            System.out.println("positionDriver接口耗时:" + wasteTime(startDate, endDate));

            //24.车辆定位信息接口
            startDate = strDate(new Date());
       *//* List<DriverRealLocation> positionVehicle = DriverRealLocation.dao.vehicle();
        System.out.println(positionVehicle.size());*//*
            List<A50> positionVehicle= A50.dao.findCompany(number1,number2);
            if (sendPost_JSON(url+"/position/vehicle", json("positionVehicle", positionVehicle )) > 0) {
                smsg.append("positionVehicle\n");
                s = s + 1;
            } else {
                emsg.append("positionVehicle\n");
                e = e + 1;
            }
            endDate = strDate(new Date());
            System.out.println("positionVehicle接口耗时:" + wasteTime(startDate, endDate));


            System.out.println("推送数据条数范围："+number1+"--------"+number2);

        String mst = "接口推送信息:" + s + "个成功:" + smsg + "\n\t" + e + "个失败:" + emsg;
        return mst;

    }
*/

    /**
     * 时间格式
     */
    private static String defaultDatePattern = "yyyy/MM/dd HH:mm:ss";
    static SimpleDateFormat sdf = new SimpleDateFormat(defaultDatePattern);

    public static String strDate(Date date) {
        return sdf.format(date);
    }

    public static String wasteTime(String startDate, String endDate) {
        try {
            java.util.Date date1 = sdf.parse(startDate);
            java.util.Date date2 = sdf.parse(endDate);
            long l = date2.getTime() - date1.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return "" + hour + "小时" + min + "分" + s + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 推送格式
     */
    public static JSONObject json(String str, Object object) {
        JSONObject result = new JSONObject();
        result.put("Source", Constant.Source);
        result.put("CompanyId", Constant.COMPANYID);
        result.put("IPCType", str);
        //result.put(str,putJson());
        result.put(str,object);
        return result;
    }

    public static String Gson( Object object){
        Gson result = new Gson();
        String jsonObject =  result.toJson(object);
        return jsonObject;
    }

    /**
     * 工商部接口
     * IP地址 VPN登录成功后 测试数据推送地址为：172.16.5.101:8080
     * FTP文件上传地址为： 172.16.11.41
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static int sendPost_JSON(String url, JSONObject obj) {

        int result = 0;
        BufferedReader in = null;
        OutputStream out = null;
        HttpURLConnection conn = null;
        try {
            java.net.URL realUrl = new URL("http://"+url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("Charsert", "UTF-8"); //设置请求编码
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = conn.getOutputStream();
            // 发送请求参数
            out.write((obj.toString()).getBytes());
//            System.out.println(obj.toString());
            System.out.println("推送的Url："+url);

            // flush输出流的缓冲
            out.flush();
            result = conn.getResponseCode();
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        if (result==200 || result==201) {

            System.out.print("------------推送成功"+result+"\n");
        }else {

            System.out.print("--------------推送失败"+result+"\n");
        }
        System.out.println("");
        return result;
    }

    //测试
    public static void main(String[] args) {

/*
        String startDate = strDate(new Date());
        System.out.println("数据推送定时任务开始");
        System.out.println("开始时间: " + startDate + "\n");
        //运行并打印信息
        // List<Order> shareOrder = Order.dao.shareOrder();
        SendJson sendJson = new SendJson();
        System.out.println(sendJson.send());
        // System.out.println(shareOrder.get(0).getId());
        String endDate = strDate(new Date());
        System.out.println("\n数据推送定时任务结束");
        System.out.println("结束时间: " + endDate);
        System.out.println("总耗时: " + wasteTime(startDate, endDate) + "\n");
*/


      // new SendJson().sendJsonerror();
    }


}
