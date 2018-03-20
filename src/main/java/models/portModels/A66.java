package models.portModels;

import utils.DateUtil;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA66;
import dto.portDto.LongitudeAndLatitude;
import models.order.Order;
import models.order.OrderTrip;

import java.util.List;

/**
 * 私人小客车合乘支付
 */
@TableBind(tableName = "A66")
public class A66 extends BaseA66<A66> {
    public static final A66 dao = new A66();

    public List<A66> findCompany() {
        return find("Select * from A66 where id = 7740");
    }





    public List<A66> findCompany2() {

        String sql = "SELECT \t\ta66.id,\n" +
                "\t\t\t\ta66.RouteId,\n" +
                "\t\t\t \n" +
                "\t\t\ta64.BookDepartTime as t,\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\n" +
                "\t\t\ta66.BookDepartTime\n" +
                "\t\t\t\n" +
                "\n" +
                "\n" +
                "from a66 LEFT JOIN a64 on (a66.RouteId=a64.RouteId)\n" +
                "\t\t\t\t\n" +
                " ";
        return find(sql);
    }





    /**
     * 添加数据
     */
    public void add() {
        List<Order> orderList = Order.dao.findDataAddA66();
        int i = 0;
        for (Order order : orderList) {
            if (order.getReservationAddress() != null && order.getDestination() != null) {


           /* LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getCreateTime(), "yyyy-MM-dd HH")); // 出发位置
            LongitudeAndLatitude dest = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getLastUpdateTime(), "yyyy-MM-dd HH"));//终点
*/
                try {
                    System.out.println(order.getNo() + "------------" + order.getId());
                    LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                    if (dep != null) {

                        String PayType = "";
                        Integer PayStatus = 2;
                        try {
                            if (order.getPayChannel() == 1) {
                                PayType = "支付宝支付";
                            } else if (order.getPayChannel() == 4) {
                                PayType = "支付宝支付";
                            } else {
                                PayType = "支付宝支付";
                            }
                        } catch (Exception e) {
                            PayType = "支付宝支付";
                        }
                        if (order.getStatus() == 5) {
                            PayStatus = 1;
                        } else if (order.getStatus() == 6) {
                            PayStatus = 0;
                        }


                        A66 a66 = new A66();
                        a66.setAddress(340100);// 行政规划代码
                        a66.setRouteId(order.getNo());
                        a66.setOrderId(order.getNo());//订单号
                        a66.setDriverPhone(order.get("DriverPhone") == null ? "" : order.get("DriverPhone").toString());//司机手机
                        a66.setLicenseId(order.get("LicenseId") == null ? "" : order.get("LicenseId").toString());//行驶证
                        a66.setVehicleNo(order.get("VehicleNo") == null ? "" : order.get("VehicleNo").toString());//车牌号
                        a66.setFareType("1");
                        a66.setBookDepartTime(order.getCreateTime() == null ? null : DateUtil.dateToLong(order.getCreateTime()));
                        a66.setDepartTime(order.getCreateTime() == null ? null : DateUtil.dateToLong(order.getCreateTime()));
                        a66.setDeparture(order.getReservationAddress());//开始地
                        a66.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));
                        a66.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));
                        a66.setDestTime(order.getLastUpdateTime() == null ? null : DateUtil.dateToLong(order.getLastUpdateTime()));
                        a66.setDestination(order.getDestination());//目的地
                        a66.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitudeEnd())));
                        a66.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitudeEnd())));
                        a66.setEncrypt(0);//坐标加密标识
                        a66.setDriveMile(order.get("RouteMile") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("RouteMile"))));//里程
                        a66.setDriveTime(order.get("DriveTime") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("DriveTime"))));//载客时间
                        a66.setFactPrice(order.get("FactPrice") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("FactPrice"))));//实际价格
                        a66.setPrice(order.get("Price") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("Price"))));//应收金额
                        a66.setCashPrice(new Long(0));//现金金额
                        a66.setLineName(PayType); // 电子支付机构
                        a66.setLinePrice(order.get("Price") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("Price"))));//电子支付金额
                        a66.setBenfitPrice(order.get("BenfitPrice") == null ? 0 : new Long(Constant.DCMFMT.format(order.get("BenfitPrice"))));//优惠金额
                        a66.setShareFuelFee(0);
                        a66.setShareHighwayToll(0);
                        a66.setPassengerTip(0);
                        a66.setShareOther(0);
                        a66.setPayState(PayStatus.toString());//结算状态 *
                        int num = 1;
                        if (i % 3 != 0) {
                            num = 2;
                        }
                        a66.setPassengerNum(num);//人数
                        a66.setPayTime(order.getLastUpdateTime() == null ? null : DateUtil.dateToLong(order.getLastUpdateTime())); //支付时间
                        a66.setOrderMatchTime(order.getLastUpdateTime() == null ? null : DateUtil.dateToLong(order.getLastUpdateTime()));//完成时间

                        a66.save();
                        i++;
                        System.out.println("------------------------->>>>>ok");

                    }
                } catch (Exception e) {

                    System.out.println("------------------------->>>>>私人小客车合乘支付插入失败" + order.getNo());
                }
            }
        }


    }


    public void addTestData() {
        List<A64> a64List = A64.dao.findCompany();

        int i = 0;
        for (A64 a64 : a64List) {
            try {
                A46 a46 = A46.dao.findPayOrder(a64.getOrderId());
                A22 DriverInfo = A22.dao.DriverInfo(a46.getLicenseId());
                A66 a66 = new A66();
                a66.setAddress(340100);// 行政规划代码
                a66.setRouteId(a64.getRouteId());
                a66.setOrderId(a64.getOrderId());//订单号
                a66.setDriverPhone(DriverInfo.getDriverPhone());//司机手机
                a66.setLicenseId(DriverInfo.getLicenseId());//行驶证
                a66.setVehicleNo(a46.getVehicleNo());//车牌号
                a66.setFareType("2");
                a66.setBookDepartTime(a64.getBookDepartTime());
                a66.setDepartTime(a46.getDepTime());
                a66.setDeparture(a46.getDepArea());//开始地
                a66.setDepLongitude(a46.getDepLongitude());
                a66.setDepLatitude(a46.getDepLatitude());
                a66.setDestTime(a46.getDestTime());
                a66.setDestination(a46.getDestArea());//目的地
                a66.setDestLongitude(a46.getDestLongitude());
                a66.setDestLatitude(a46.getDestLatitude());
                a66.setEncrypt(0);//坐标加密标识
                a66.setDriveMile(a46.getDriveMile());//里程
                a66.setDriveTime(a46.getDriveTime());//载客时间
                a66.setFactPrice(a46.getFactPrice());//实际价格
                a66.setPrice(a46.getPrice());//应收金额
                a66.setCashPrice(a46.getCashPrice());//现金金额
                a66.setLineName(a46.getLineName()); // 电子支付机构
                a66.setLinePrice(a46.getLinePrice());//电子支付金额
                a66.setBenfitPrice(a46.getBenfitPrice());//优惠金额
                a66.setShareFuelFee(0);
                a66.setShareHighwayToll(0);
                a66.setPassengerTip(0);
                a66.setShareOther(0);
                a66.setPayState(a46.getPayState());//结算状态 *
                int num = 1;
                if (i % 3 != 0) {
                    num = 2;
                }
                a66.setPassengerNum(num);//人数
                a66.setPayTime(a46.getPayTime());//支付时间
                a66.setOrderMatchTime(a46.getOrderMatchTime());//完成时间

                a66.save();
                i++;
                System.out.println("------------------------->>>>>ok");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    public void updateTest() {


        List<A66> list = findCompany2();

        for (A66 a66 : list) {
            a66.setBookDepartTime(Long.valueOf(a66.get("t").toString()));


            a66.update();
            System.out.println(a66.getBookDepartTime()+"--------------" +a66.get("t").toString());

        }


    }


    public void Test() {

        List<A66> list = findCompany();

        for (A66 a46 : list) {


            boolean b = DateUtil.isValidDate(StringUtil.stringToDate(a46.getBookDepartTime().toString()));

            boolean c = DateUtil.isValidDate(StringUtil.stringToDate(a46.getDepartTime().toString()));

            boolean d = DateUtil.isValidDate(StringUtil.stringToDate(a46.getDestTime().toString()));

            boolean e = DateUtil.isValidDate(StringUtil.stringToDate(a46.getPayTime().toString()));


            boolean f = DateUtil.isValidDate(StringUtil.stringToDate(a46.getOrderMatchTime().toString()));


            if (!b || !c || !d || !e || !f) {
                System.out.println(b + "     ");
                System.out.println(c + "     ");
                System.out.println(d + "     ");
                System.out.println(e + "     ");
                System.out.println(f + "     ");

                System.out.println(a46.getOrderId() + "            ++++++  ");
            }


        }

    }

}
