package models.portModels;


import utils.DateUtil;
import utils.Random;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA50;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverHistoryLocation;
import models.order.Order;

import java.util.List;

/**
 * 车辆定位
 */
@TableBind(tableName = "A50")
public class A50 extends BaseA50<A50> {
    public static final A50 dao = new A50();

    public List<A50> findCompany(){
        return  find("Select * from A50");
    }

    public List<A50> findCompany(int number1,int number2){
        return  find("Select * from A50 limit ?,?",number1,number2);
    }


    public List<A50> findsql(String s) {

        return find("Select PositionTime,VehicleNo,Count(*) From a50  WHERE VehicleNo = ?  Group By PositionTime Having Count(*) >1 ", s);
    }


    public List<A50> findupdate(String s, Long s1) {

        return find("Select *  From a50  WHERE VehicleNo = ? and PositionTime= ?", s,s1);
    }

    public List<A50> findveho() {

        return find("SELECT DISTINCT VehicleNo FROM a50 ");
    }


    /**
     * 添加数据
     */
    public void add() {
        List<Order> orderList = Order.dao.findDataAddA48();
        int i = 0;
        for (Order order : orderList) {
            try {
                List<LongitudeAndLatitude> list = DriverHistoryLocation.dao.findByDriverAndTimeToList(order.getDriver(), DateUtil.dateFmt(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), DateUtil.dateFmt(order.getLastUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                for (LongitudeAndLatitude location : list) {
                    if (location != null) {
                        A50 a50 = new A50();
                        a50.setVehicleNo(order.get("VehicleNo").toString());//车牌号
                        a50.setVehicleRegionCode(340100);// 行政规划代码
                        a50.setPositionTime(location.getRecoviceTime());//定位时间
                        a50.setLongitude(new Long(Constant.DCMFMT.format(location.getLongitude())));//经度
                        a50.setLatitude(new Long(Constant.DCMFMT.format(location.getLatitude())));//纬度
                        a50.setSpeed(new Long(Constant.DCMFMT.format(location.getSpeed())));//速度
                        a50.setDirection(new Long(Constant.DCMFMT.format(location.getOrientation())));//方向角
                        a50.setElevation(0);//海拔
                        a50.setMileage(0);//行驶里程  km
                        a50.setEncrypt(0);//坐标加密标识
                        if (i % 5 != 0) {
                            a50.setBizStatus(1);//营运状态
                            a50.setOrderId(order.getNo());
                        } else if (i % 3 != 0) {
                            a50.setBizStatus(2);//营运状态
                            a50.setOrderId(order.getNo());
                        } else if (i % 4 != 0) {
                            a50.setBizStatus(3);//营运状态
                            a50.setOrderId("0");
                        } else {
                            a50.setBizStatus(4);//营运状态
                            a50.setOrderId("0");

                        }
                        a50.save();
                        System.out.println("------------------------->>>>>ok");
                    }
                    i++;

                }
            } catch (Exception e) {
                System.out.println("------------------------>>>>>>车辆定位插入失败" + order.getNo());
            }

        }


    }


    /**
     * 添加数据
     */
    public void addTestData() {
        List<A48> a48List =A48.dao.findCompany();
        for (A48 a48:a48List) {
            A50 a50 = new A50();
            a50.setVehicleNo(a48.getVehicleNo());//车牌号
            a50.setVehicleRegionCode(340100);// 行政规划代码
            a50.setPositionTime(a48.getPositionTime());//定位时间
            a50.setLongitude(a48.getLongitude());//经度
            a50.setLatitude(a48.getLatitude());//纬度
            a50.setSpeed(a48.getSpeed());//速度
            a50.setDirection(a48.getDirection() );//方向角
            a50.setElevation(0);//海拔
            a50.setMileage(Integer.valueOf(Random.getRandNum3(9, 2)));//行驶里程  km
            a50.setEncrypt(0);//坐标加密标识
            a50.setBizStatus(a48.getBizStatus());//营运状态
            a50.setOrderId(a48.getOrderId());
            a50.save();
            System.out.println(a48.getOrderId()+" +++++++++++++++");
        }
    }


    public void updateTest() {
        List<A50> a50s1 = find("SELECT DISTINCT OrderId from a50 WHERE PositionTime BETWEEN '1501344000' AND  '1501430400'");

        for (A50 a501 : a50s1) {
            List<A50> OrderIds = find(" SELECT * FROM a50 WHERE OrderId='"+a501.getOrderId()+"'  AND   BizStatus =1 ");
            for (int i = 0; i < OrderIds.size(); i++) {

                if (OrderIds.size() > 30) {
                    if (i % 2 == 0) {
                        System.out.println("----------"+  OrderIds.get(i).delete());
                    }
                }
            }

        }


    }


}



