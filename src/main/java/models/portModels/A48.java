package models.portModels;

import utils.DateUtil;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA48;
import models.driver.DriverHistoryLocation;
import models.driver.DriverInfo;
import models.order.Order;
import models.order.OrderLog;

import java.util.Date;
import java.util.List;

/**
 * 司机定位接口
 */
@TableBind(tableName = "A48")
public class A48 extends BaseA48<A48> {
    public static final A48 dao = new A48();

    public List<A48> findCompany() {

        return find("Select * from A48");
    }

    public List<A48> findCompany(int number1, int number2) {

        return find("Select * from A48 limit ?,?", number1, number2);
    }

    public List<A48> findDriverInfoLocations(String orderId) {
        return find("SELECT * FROM A48 WHERE orderId= '" + orderId + "' ");
    }


    public List<A48> findsql(String s) {

        return find("Select PositionTime,VehicleNo,Count(*) From a48  WHERE VehicleNo = ?  Group By PositionTime Having Count(*) >1 ", s);
    }


    public List<A48> findupdate(String s, Long s1) {

        return find("Select *  From a48  WHERE VehicleNo = ? and PositionTime= ?", s,s1);
    }

    public List<A48> findveho() {

        return find("SELECT DISTINCT VehicleNo FROM a48 ");
    }

    /**
     * 添加数据
     */
    public void add() {


        List<A34> orderList = A34.dao.findCompany();

        for (A34 a34 : orderList) {
            try {
                List<DriverHistoryLocation> locations = findLocation(a34.getOrderId());
                int i = 0;
                for (DriverHistoryLocation location : locations) {
                    if (location != null) {
                        A48 a48 = new A48();
                        a48.setLicenseId(a34.getLicenseId());//驾驶证
                        a48.setDriverRegionCode(340100);// 行政规划代码
                        a48.setVehicleNo(a34.getVehicleNo());//车牌号
                        a48.setPositionTime(DateUtil.dateToStamp(location.getRecoviceTime().toString()));//定位时间
                        a48.setLongitude(new Long(Constant.DCMFMT.format(location.getLongitude() / 1E-6)));//经度

                        a48.setLatitude(new Long(Constant.DCMFMT.format(location.getLatitude() / 1E-6)));//纬度
                        a48.setEncrypt(0);//坐标加密标识
                        a48.setDirection(new Long(Constant.DCMFMT.format(location.getOrientation())));//方向角
                        a48.setElevation(0);//海拔
                        a48.setSpeed(new Long(Constant.DCMFMT.format(location.getSpeed())));//速度
                        /*if (i % 5 != 0) {
                            a48.setBizStatus(1);//营运状态
                            a48.setOrderId(a34.getOrderId());
                        } else if (i % 3 != 0) {
                            a48.setBizStatus(2);//营运状态
                            a48.setOrderId(a34.getOrderId());
                        } else if (i % 4 != 0) {
                            a48.setBizStatus(3);//营运状态
                            a48.setOrderId("0");
                        } else {
                            a48.setBizStatus(4);//营运状态
                            a48.setOrderId("0");

                        }
*/
                        if (i <= 2) {
                            a48.setBizStatus(2);//营运状态
                            a48.setOrderId(a34.getOrderId());
                        } else if (i > 2) {
                            a48.setBizStatus(1);//营运状态
                            a48.setOrderId(a34.getOrderId());
                        }


                        if (i == locations.size() || i == (locations.size() - 1)) {
                            a48.setBizStatus(4);//营运状态
                            a48.setOrderId("0");
                        } else if (i == (locations.size() - 2) || i == (locations.size() - 3)) {
                            a48.setBizStatus(3);//营运状态
                            a48.setOrderId("0");
                        }


                        a48.save();
                        System.out.println("------------------------->>>>>ok");
                        i++;
                    }

                }
            } catch (Exception e) {
                System.out.println("--------------------->>>>>>>司机定位接口插入失败" + a34.getOrderId());
            }

        }


    }


    /**
     * 添加数据
     */
    public void addTestData() {
        List<A22> list1 = A22.dao.findDriverInfo();
        List<A46> a46List = A46.dao.findCompany();

        int size = 0;
        int count = 0;
        int i = 0;
        for (A46 a46 : a46List) {
            List<DriverHistoryLocation> locations = DriverHistoryLocation.dao.findDataAddA48(a46.getOrderId());//订单位置 时间需要修改
            for (DriverHistoryLocation location : locations) {
                String bgingT = StringUtil.stringToDate(a46.getBookDepTime().toString());
                String endT = StringUtil.stringToDate(a46.getOrderMatchTime().toString());
                String srt = location.get("PositionTime").toString();

                System.out.println("bgingT =>" + bgingT + "+++++++++++++++");
                System.out.println("endT =>" + endT + "+++++++++++++++");
                System.out.println("srt =>" + srt + "+++++++++++++++");
                if (size == list1.size()) {
                    i = 0;
                    size = 0;
                }/*
                A48 a48 = new A48();
                a48.setLicenseId(list1.get(i).get("LicenseId").toString());//驾驶证
                a48.setDriverRegionCode(340100);// 行政规划代码
                a48.setVehicleNo(list1.get(i).get("VehicleNo").toString());//车牌号
                a48.setPositionTime(new Long("150045" + (Random.getRandNum3(5, 4))));//定位时间
                a48.setLongitude(new Long("11728" + (Random.getRandNum3(9, 4))));//经度
                a48.setLatitude(new Long("3171" + (Random.getRandNum3(9, 4))));//纬度
                a48.setEncrypt(0);//坐标加密标识
                a48.setDirection(a48List.get(0).getDirection());//方向角
                a48.setElevation(0);//海拔
                a48.setSpeed(new Long(Random.getRandNum3(9, 2)));//速度
                if (i % 5 != 0) {
                    a48.setBizStatus(1);//营运状态
                    a48.setOrderId(list1.get(i).get("OrderId").toString());
                } else if (i % 3 != 0) {
                    a48.setBizStatus(2);//营运状态
                    a48.setOrderId(list1.get(i).get("OrderId").toString());
                } else if (i % 4 != 0) {
                    a48.setBizStatus(3);//营运状态
                    a48.setOrderId("0");
                } else {
                    a48.setBizStatus(4);//营运状态
                    a48.setOrderId("0");

                }

                a48.save();
                System.out.println("------------------------->>>>>ok");
                count++;
                size++;
                if (count == 8000) {
                    return;
                }*/


            }

        }


    }


    public void updateTest() {
        List<A48> a48s1 = find("SELECT OrderId FROM `a48` WHERE   OrderId <> '0'  GROUP BY OrderId ");

        for (A48 a481 : a48s1) {
            List<A48> OrderIds = find(" SELECT * FROM a48 WHERE OrderId='" + a481.getOrderId() + "' ");
            int time = 60;
            for (int i = 0; i < 60; i++) {

                A48 location = OrderIds.get(OrderIds.size() - 1);
                A48 a48 = new A48();
                a48.setLicenseId(location.getLicenseId());//驾驶证
                a48.setDriverRegionCode(340100);// 行政规划代码
                a48.setVehicleNo(location.getVehicleNo());//车牌号
                a48.setPositionTime(location.getPositionTime() + time);//定位时间
                a48.setLongitude(location.getLongitude() + i);//经度
                a48.setLatitude(location.getLatitude() + i);//纬度
                a48.setEncrypt(0);//坐标加密标识
                a48.setDirection(location.getDirection());//方向角
                a48.setElevation(0);//海拔
                a48.setSpeed(location.getSpeed());//速度
                if (i < 30) {
                    a48.setBizStatus(3);//营运状态
                    a48.setOrderId("0");
                } else {
                    a48.setBizStatus(4);//营运状态
                    a48.setOrderId("0");

                }
                a48.save();
                time += 60;
            }
            time = 0;

        }


    }


    public List<DriverHistoryLocation> findLocation(String orderId) {
        List<DriverHistoryLocation> driverHistoryLocations = null;
        List<Order> listOrders = Order.dao.findOrderAll(orderId);
        for (int i = 0; i < listOrders.size(); i++) {
            Order order = listOrders.get(i);
            DriverInfo driverInfo = DriverInfo.dao.findById(order.getDriver());
            List<OrderLog> orderLogs = OrderLog.dao.findByOrderAndLoginId(order.getId(), driverInfo.getLoginId());
            Date start = orderLogs.get(1).getOperationTime();
            Date end = orderLogs.get(orderLogs.size() - 1).getOperationTime();
            driverHistoryLocations = DriverHistoryLocation.dao.findByDriverAndTime1(order.getDriver(), start, end);
            System.out.println(driverHistoryLocations);
        }
        return driverHistoryLocations;

    }


}


