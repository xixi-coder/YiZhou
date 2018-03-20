package models.portModels;


import utils.DateUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA64;
import dto.portDto.LongitudeAndLatitude;
import models.order.Order;
import models.order.OrderTrip;

import java.util.List;

/**
 * 私人小客车合乘订单
 */
@TableBind(tableName = "A64")
public class A64 extends BaseA64<A64> {
    public static final A64 dao = new A64();

    public List<A64> findCompany() {
        return find("Select * from A64");
    }


    /**
     * 添加数据
     */
    public void add() {
        List<Order> orderList = Order.dao.findDataAddA62();
        int i = 0;
        for (Order order : orderList) {
           /* LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getCreateTime(), "yyyy-MM-dd HH")); // 出发位置
            LongitudeAndLatitude dest = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getLastUpdateTime(), "yyyy-MM-dd HH"));//终点
*/
            try {
                System.out.println(order.getNo() + "------------" + order.getId());
                LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                if (dep != null) {
                    A64 a64 = new A64();
                    a64.setAddress(340100);// 行政规划代码
                    a64.setRouteId(order.getNo());
                    a64.setOrderId(order.getNo());//订单号
                    a64.setBookDepartTime(order.getSetouttime() == null ? null : DateUtil.dateToLong(order.getSetouttime()));
                    a64.setDeparture(order.getReservationAddress());//开始地
                    a64.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));
                    a64.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));
                    a64.setDestination(order.getDestination());//目的地
                    a64.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitudeEnd())));
                    a64.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitudeEnd())));
                    a64.setEncrypt(0);//坐标加密标识
                    a64.setOrderEnsureTime(order.getSetouttime() == null ? null : DateUtil.dateToLong(order.getSetouttime()));//订单确认时间
                    int num = 1;
                    if (i % 2 != 0) {
                        num = 2;
                    }
                    a64.setPassengerNum(num);//人数
                    a64.setPassengerNote("");


                    a64.save();
                    i++;
                    System.out.println("------------------------->>>>>ok");

                }
            } catch (Exception e) {
                System.out.println("---------------->>>>>>私人小客车合乘订单插入失败" + order.getNo());
            }

        }


    }


    public void addTestData() {
        List<A32> a32List = A32.dao.findCompany();
        List<A46> a46List = A46.dao.findCompany();
        List<A22> driverInfo = A22.dao.findDriverInfo();
        List<A62> a62List= A62.dao.findCompany(0,1000);
        int count = 0;
        int i =0;
        for (A62 a62:a62List){
            if (i == driverInfo.size()) {
                i = 0;
            }
            try {
                List<A48> driverInfoLocations = A48.dao.findDriverInfoLocations(a46List.get(count).getOrderId());
                A64 a64 = new A64();
                a64.setAddress(340100);// 行政规划代码
                a64.setRouteId(a62.getRouteId());
                a64.setOrderId(a46List.get(count).getOrderId());//订单号
                a64.setBookDepartTime(a46List.get(count).getBookDepTime());
                a64.setDeparture(a32List.get(count).getDeparture());//开始地
                a64.setDepLongitude(driverInfoLocations.get(0).getLongitude());//经度
                a64.setDepLatitude(driverInfoLocations.get(0).getLatitude());//纬度
                a64.setDestination(a32List.get(count).getDestination());//目的地
                a64.setDestLongitude(driverInfoLocations.get(driverInfoLocations.size()-1).getLongitude());//经度
                a64.setDestLatitude(driverInfoLocations.get(driverInfoLocations.size()-1).getLatitude());//纬度
                a64.setEncrypt(0);//坐标加密标识
                a64.setOrderEnsureTime(a32List.get(count).getOrderTime());//订单确认时间
                int num = 1;
                if (i % 2 != 0) {
                    num = 2;
                }
                a64.setPassengerNum(num);//人数
                a64.setPassengerNote("");


                a64.save();
                System.out.println("------------------------->>>>>ok");
                count++;
                i++;
                if(count==200){
                    return;
                }
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + a46List.get(count).getOrderId());
            }



        }
















    }


}
