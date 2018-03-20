package models.portModels;


import utils.DateUtil;
import utils.Random;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA62;
import dto.portDto.LongitudeAndLatitude;
import models.order.Order;
import models.order.OrderTrip;

import java.util.List;

/**
 * 私人小客车和乘发布
 */
@TableBind(tableName = "A62")
public class A62 extends BaseA62<A62> {
    public static final A62 dao = new A62();

    public List<A62> findCompany() {
        return find("Select * from A62");
    }

    public List<A62> findCompany(int i,int g) {
        return find("Select * from A62 limit ?,?",i,g);
    }

    public A62 findRouteId(String orderid) {
        return findFirst("Select * from A62 where orderid ='" + orderid+"' ");
    }

    /**
     * 添加数据
     */
    public void add() {
        List<Order> orderList = Order.dao.findDataAddA62();

        for (Order order : orderList) {
           /* LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getCreateTime(), "yyyy-MM-dd HH")); // 出发位置
            LongitudeAndLatitude dest = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getLastUpdateTime(), "yyyy-MM-dd HH"));//终点
*/
            try {
                System.out.println(order.getNo() + "------------" + order.getId());
                LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                if (dep != null) {
                    A62 a62 = new A62();
                    a62.setAddress(340100);// 行政规划代码
                    a62.setRouteId(order.getNo());//订单号
                    a62.setDriverName(order.get("DriverName") == null ? "" : order.get("DriverName").toString());//司机姓名
                    a62.setDriverPhone(order.get("DriverPhone") == null ? "" : order.get("DriverPhone").toString());//司机手机
                    a62.setLicenseId(order.get("LicenseId") == null ? "" : order.get("LicenseId").toString());//行驶证
                    a62.setVehicleNo(order.get("VehicleNo") == null ? "" : order.get("VehicleNo").toString());//车牌号
                    a62.setDeparture(order.getReservationAddress());//开始地
//                    a62.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));
//                    a62.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));
//                    a62.setDestination(order.getDestination());//目的地
//                    a62.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitudeEnd())));
//                    a62.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitudeEnd())));
                    a62.setEncrypt(0);//坐标加密标识
                    a62.setRouteCreateTime(DateUtil.dateToLong(order.getCreateTime()));
                    a62.setRouteMile(order.get("RouteMile") == null ? 0 : new Long(Constant.DCMFMT.format((order.get("RouteMile")))));//里程
                    a62.setRouteNote("无");
                    a62.save();


                    //a62.save();
                    System.out.println("------------------------->>>>>ok");

                }
            } catch (Exception e) {
                System.out.println("----------------->>>>>>>>私人小客车和乘发布插入失败"+order.getNo());
            }

        }


    }

    public void addTestData() {
        List<A32> a32List = A32.dao.findCompany();
        List<A46> a46List = A46.dao.findCompany();
        List<A22> driverInfo = A22.dao.findDriverInfo();

        int count = 0;
        for (int i = 0; i < 200; i++) {
            if (i == driverInfo.size()) {
                i = 0;
            }
            try {


                A62 a62 = new A62();
                a62.setAddress(340100);// 行政规划代码
                a62.setRouteId("ROUTEID" + Random.getRandNum() + StringUtil.randomStr(6));//随机单号
                a62.setDriverName(driverInfo.get(i).getDriverName());//司机姓名
                a62.setDriverPhone(driverInfo.get(i).getDriverPhone());//司机手机
                a62.setLicenseId(driverInfo.get(i).get("LicenseId").toString());//行驶证
                a62.setVehicleNo(driverInfo.get(i).get("VehicleNo").toString());//车牌号
                a62.setDeparture(a32List.get(count).getDeparture());//开始地
//                a62.setDepLongitude(a32List.get(count).getDepLongitude());
//                a62.setDepLatitude(a32List.get(count).getDepLatitude());
//                a62.setDestination(a32List.get(count).getDestination());//目的地
//                a62.setDestLongitude(a32List.get(count).getDestLongitude());
//                a62.setDestLatitude(a32List.get(count).getDestLatitude());
                a62.setEncrypt(0);//坐标加密标识
                a62.setRouteCreateTime(a32List.get(count).getOrderTime());
                a62.setRouteMile(new Long(Random.getRandNum3(5,2)));//里程
                a62.setRouteNote("无");
                a62.save();
                System.out.println("------------------------->>>>>ok");
                count++;
                if(count==200){
                    return;
                }
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + a46List.get(count).getOrderId());
            }


        }


    }

}
