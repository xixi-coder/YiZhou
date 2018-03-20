package models.portModels;


import utils.DateUtil;
import utils.Random;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA44;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverInfo;
import models.order.OrderLog;
import models.order.OrderTrip;

import java.util.List;

/**
 * 到达
 */
@TableBind(tableName = "A44")
public class A44 extends BaseA44<A44> {
    public static final A44 dao = new A44();

    public List<A44> findCompany() {
        return find("Select * from A44");
    }


    public List<A44> findCompany2() {
        String sql ="select   a44.id,a44.OrderId,\n" +
                "\t\t\t\ta42.DepTime as  t,\n" +
                "\t\t\t\ta44.DestTime\n" +
                "\n" +
                "\t\t\t\n" +
                "\t\t\t from a44  LEFT JOIN a42 on(a44.OrderId=a42.OrderId)\n" +
                "\n";
        return find(sql);
    }



    public A44 findA44(String orderId) {
        return findFirst("Select * from A44 where orderId = '"+orderId+"'");
    }


    /**
     * 添加
     */
    public void add() {
        List<OrderLog> orderList = OrderLog.dao.findDataByA42(4);
        for (OrderLog order : orderList) {
            try {
                DriverInfo driverInfo = DriverInfo.dao.findDataById((Integer) order.get("driver"));
                if (driverInfo != null) {
                    if (driverInfo.getCertificateNo() != null && order.get("VehicleNo") != null) {
                        A44 a44 = new A44();
                        /*LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime((Integer) order.get("driver"), DateUtil.dateFmt(order.getOperationTime(), "yyyy-MM-dd HH")); // 出发位置
                        System.out.println(order.get("no"));*/
                        System.out.println(order.get("no") + "------------" + order.get("orderId"));
                        LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime((Integer) order.get("orderId"));
                        if (dep != null) {
                            a44.setOrderId(order.get("no").toString());//订单号
                            a44.setEncrypt(0);
                            a44.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//出发点经度
                            a44.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//出发点纬度
                            a44.setDestTime(DateUtil.dateToLong(order.getOperationTime()));//下车时间
                            a44.setDriveMile(order.get("DriveMile") == null ? null : new Long(Constant.DCMFMT.format(order.get("DriveMile"))));//里程

                            a44.setDriveTime(order.get("DriveTime") == null ? 0 : Integer.valueOf(order.get("DriveTime").toString()));
                            a44.save();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("------------------------>>>>>>>到达接口失败" + order.get("driver"));
            }

        }

    }


    public void updateTest() {

        List<A42> list= A42.dao.findCompany();

        for (A42 a42:list){
            A46 a46 = A46.dao.findPayOrder(a42.getOrderId());
            System.out.println("------>>>>>>-"+a42.getOrderId());
            a42.setDepTime(a46.getDepTime());
            a42.setDepLongitude(a46.getDepLongitude());
            a42.setDepLatitude(a46.getDepLatitude());
            a42.update();

        }

    }


//    public void updateTest() {
//        List<A44> list = findCompany2();
//
//        for (A44 a44:list){
//            a44.setDestTime(Long.valueOf(a44.get("t").toString()));
//
//            a44.update();
//            System.out.println(a44.getDestTime()+"==============="+a44.get("t").toString());
//        }
//
//
//    }



    public void addTestData() {
        List<A34> a34List = A34.dao.findCompany();

        for (A34 a34 : a34List) {
            List<A48> driverInfoLocations = A48.dao.findDriverInfoLocations(a34.getOrderId());
            try {
                A44 a44 = new A44();
                System.out.println(a34.getOrderId() + "------------");
                a44.setOrderId(a34.getOrderId());//订单号
                a44.setEncrypt(0);
                a44.setDestLongitude(driverInfoLocations.get(driverInfoLocations.size()-1).getLongitude());//出发点经度
                a44.setDestLatitude(driverInfoLocations.get(driverInfoLocations.size()-1).getLatitude());//出发点纬度
                a44.setDestTime(new Long(DateUtil.stampToDate(driverInfoLocations.get(driverInfoLocations.size()-1).getPositionTime().toString()+"000","yyyyMMddHHmmss")));//下车时间
                a44.setDriveMile(new Long(Random.getRandNum3(4,2)));//里程
                Long DriveTime = (driverInfoLocations.get(driverInfoLocations.size()-1).getPositionTime())- new Long(driverInfoLocations.get(0).getPositionTime());
                a44.setDriveTime(new Integer(DriveTime.toString()));
                a44.save();
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + a34.getOrderId());
            }


        }


    }


}
