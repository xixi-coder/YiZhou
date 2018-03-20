package models.portModels;


import utils.DateUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA42;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverInfo;
import models.order.OrderLog;
import models.order.OrderTrip;

import java.util.List;

/**
 * 出发
 */
@TableBind(tableName = "A42")
public class A42 extends BaseA42<A42> {
    public static final A42 dao = new A42();

    public List<A42> findCompany() {
        return find("Select * from A42");
    }


    public List<A42> findCompany2() {
        String sql ="" +
                "select   a42.id,a42.OrderId,\n" +
                "\t\t\t\ta32.DepartTime as t,\n" +
                "\t\t\t\ta42.DepTime\n" +
                "\n" +
                "\t\t\t\n" +
                "\t\t\t from a42  LEFT JOIN a32 on(a32.OrderId=a42.OrderId)\n" +
                "\n";
        return find(sql);
    }


    /**
     * 添加
     */
    public void add() {
        List<OrderLog> orderList = OrderLog.dao.findDataByA42(3);
        for (OrderLog order : orderList) {
            try {
                DriverInfo driverInfo = DriverInfo.dao.findDataById((Integer) order.get("driver"));
                if (driverInfo != null) {
                    if (driverInfo.getCertificateNo() != null && order.get("VehicleNo") != null) {
                        A42 a42 = new A42();
                        //                    LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime((Integer) order.get("driver"), DateUtil.dateFmt(order.getOperationTime(), "yyyy-MM-dd HH")); // 出发位置
                        //                    System.out.println(order.get("no"));
                        System.out.println(order.get("no") + "------------" + order.get("orderId"));
                        LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime((Integer) order.get("orderId"));
                        if (dep != null) {
                            a42.setOrderId(order.get("no").toString());//订单号
                            a42.setLicenseId(driverInfo.getCertificateNo());//驾驶证
                            a42.setFareType("1");
                            a42.setVehicleNo(order.get("VehicleNo").toString());//车牌号
                            a42.setEncrypt(0);
                            a42.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//出发点经度
                            a42.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//出发点纬度
                            a42.setDepTime(DateUtil.dateToLong(order.getOperationTime()));//派单时间
                            a42.setWaitMile(0);
                            a42.setWaitTime(0);
                            a42.save();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("------------------------>>>>>>>出发接口失败" + order.get("driver"));
            }

        }

    }


    public void addTestData() {
        List<A34> a34List = A34.dao.findCompany();

        for (A34 a34 : a34List) {
            List<A48> driverInfoLocations = A48.dao.findDriverInfoLocations(a34.getOrderId());
            try {
                    A42 a42 = new A42();
                    System.out.println(a34.getOrderId() + "------------");
                    a42.setOrderId(a34.getOrderId());//订单号
                    a42.setLicenseId(a34.getLicenseId());//驾驶证
                    a42.setFareType("1");
                    a42.setVehicleNo(a34.getVehicleNo());//车牌号
                    a42.setEncrypt(0);
                    a42.setDepLongitude(driverInfoLocations.get(0).getLongitude());//出发点经度
                    a42.setDepLatitude(driverInfoLocations.get(0).getLatitude());//出发点纬度
                    a42.setDepTime(a34.getDistributeTime());//派单时间
                    a42.setWaitMile(0);
                    a42.setWaitTime(0);
                    a42.save();
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + a34.getOrderId());
            }


        }


    }

    public void updateTest() {
        List<A42> list = findCompany2();


        for (A42 a42:list){

            a42.setDepTime(new Long(a42.get("t").toString()));
            a42.update();
            System.out.println("+++++++++++++123");

        }

    }

}
