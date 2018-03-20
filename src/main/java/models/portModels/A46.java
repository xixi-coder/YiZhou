package models.portModels;


import utils.DateUtil;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA46;
import dto.portDto.LongitudeAndLatitude;
import models.driver.DriverInfo;
import models.order.Order;
import models.order.OrderTrip;

import java.util.List;

/**
 * 订单支付接口
 */
@TableBind(tableName = "A46")
public class A46 extends BaseA46<A46> {
    public static final A46 dao = new A46();

    public List<A46> findCompany() {
        return find("Select * from A46");
    }
    public List<A46> findCompany2() {
        String sql ="\n" +
                "select   a46.id,a46.OrderId,\n" +
                "\t\t\t\t\ta46.DriveTime,\n" +
                "\t\t\t\ta42.DepTime  as t1,\n" +
                "\t\t\t\ta46.DepTime ,\n" +
                "\n" +
                "\t\t\t\ta46.DestTime ,\n" +
                "\t\t\t\ta44.DestTime as  t2\n" +
                "\n" +
                "\t\t\t\n" +
                "\t\t\t from a46  LEFT JOIN a42 on(a46.OrderId=a42.OrderId)\n" +
                "\t\t\t\t\t\t\t\t\tLEFT JOIN a44 on(a46.OrderId=a44.OrderId)";
        return find(sql);
    }

    public A46 findPayOrder(String orderId) {
        return findFirst("Select * from A46 where orderId='" + orderId + "'");
    }

    public void add() {
        List<Order> orderList = Order.dao.findDataAddA46();
        for (Order order : orderList) {
            try {
            /*LongitudeAndLatitude dep = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getSetouttime(), "yyyy-MM-dd HH")); // 出发位置
            LongitudeAndLatitude dest = DriverHistoryLocation.dao.findByDriverAndTime(order.getDriver(), DateUtil.dateFmt(order.getLastUpdateTime(), "yyyy-MM-dd HH"));//终点
            System.out.println(order.getNo());*/
                System.out.println(order.getNo() + "------------" + order.getId());
                LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                if (dep != null) {
                    DriverInfo driverInfo = DriverInfo.dao.findDataById(order.getDriver());
                    if (driverInfo != null) {
                        String PayType = "";
                        Integer PayStatus = 2;
                        if (order.getPayChannel() == 1) {
                            PayType = "支付宝支付";
                        } else if (order.getPayChannel() == 4) {
                            PayType = "支付宝支付";
                        } else {
                            PayType = "支付宝支付";
                        }
                        if (order.getStatus() == 5) {
                            PayStatus = 1;
                        } else if (order.getStatus() == 6) {
                            PayStatus = 0;
                        }

                        A46 a46 = new A46();
                        a46.setOrderId(order.getNo());//订单号*
                        a46.setOnArea(340100); // 上车行政区规划号 *
                        a46.setDriverName(driverInfo.getRealName());// 司机姓名 *
                        a46.setLicenseId(driverInfo.getCertificateNo());// 驾驶证*
                        a46.setFareType("1");//运价编码  ---》对应A4.6运价接口 *
                        a46.setVehicleNo(order.get("VehicleNo").toString());//车牌号 *
                        a46.setBookDepTime(DateUtil.dateToLong(order.getSetouttime())); //预计上车时间 *
                        a46.setWaitTime(new Long(0)); // 等待时间  秒
                        a46.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));// 上车 经度*
                        a46.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//上车 纬度 *
                        a46.setDepArea(order.getReservationAddress()); // 上车地点
                        a46.setDepTime(dep.getStartTime());// 上车时间 *
                        a46.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitudeEnd()))); // 下车 经度 *
                        a46.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitudeEnd())));// 下车 纬度 *
                        a46.setDestArea(order.getDestination()); // 下车地点
                        a46.setDestTime(DateUtil.dateToLong(order.getLastUpdateTime())); // 下车时间 *
                        a46.setBookModel(order.get("BookModel").toString());// 预定车型
                        a46.setModel(order.get("BookModel").toString()); // 实际使用车型
                        a46.setDriveMile(order.getDistance().longValue()); // 载客里程 km *
                        a46.setDriveTime(Long.valueOf(order.get("DriveTime").toString())); //载客时间 秒  *
                        a46.setWaitMile(new Long(0)); // 空行驶里程
                        a46.setFactPrice(order.getRealPay().longValue()); // 实收金额 *
                        a46.setPrice(order.getAmount().longValue()); // 应收金额
                        a46.setCashPrice(new Long(0)); // 现金支付金额
                        a46.setLineName(PayType); // 电子支付机构
                        a46.setLinePrice(order.getAmount().longValue());// 电子支付金额
                        a46.setPosName("暂无"); // POS机支付机构
                        a46.setPosPrice(new Long(0)); // POS机支付金额
                        a46.setBenfitPrice(order.getYhAmount().longValue()); // 优惠金额
                        a46.setBookTip(new Long(0)); // 预约服务费
                        a46.setPassengerTip(new Long(0)); // 附加费
                        a46.setPeakUpPrice(new Long(0));// 高峰加价金额
                        a46.setNightUpPrice(new Long(0)); //夜间加价金额
                        a46.setFarUpPrice(new Long(0)); //远途加价金额 *
                        a46.setOtherUpPrice(Double.valueOf(order.get("OtherUpPrice").toString()).longValue()); //其他加价金额 *
                        a46.setPayState(PayStatus.toString());//结算状态 *
                        a46.setPayTime(order.getPayTime() == null ? null : DateUtil.dateToLong(order.getPayTime()));//乘客结算时间
                        a46.setOrderMatchTime(order.getPayTime() == null ? null : DateUtil.dateToLong(order.getPayTime()));// 订单完成时间
                        a46.setInvoiceStatus("0");// 发票状态 *
                        a46.save();
                        System.out.println("-------------------->>>>>>>>OK");
                    }


                }
            } catch (Exception e) {
                System.out.println("-------------------->>>>>>>>订单支付接口插入失败" + order.getNo());
            }


        }


    }

    public void addTestData() {
        List<A34> a34List = A34.dao.findCompany();

        for (A34 a34 : a34List) {
            List<A48> driverInfoLocations = A48.dao.findDriverInfoLocations(a34.getOrderId());
            A22 DriverInfo = A22.dao.DriverInfo(a34.getLicenseId());
            A32 orderInfo = A32.dao.findData(a34.getOrderId());
            A16 carInfo = A16.dao.findCar(a34.getVehicleNo());
            A44 a44 = A44.dao.findA44(a34.getOrderId());
            Order order = Order.dao.findByNo(a34.getOrderId());
            Integer PayStatus = 2;
            if (order.getStatus() == 5) {
                PayStatus = 1;
            } else if (order.getStatus() == 6) {
                PayStatus = 0;
            }


            try {
                A46 a46 = new A46();
                a46.setOrderId(a34.getOrderId());//订单号*
                a46.setOnArea(340100); // 上车行政区规划号 *
                a46.setDriverName(DriverInfo.getDriverName());// 司机姓名 *
                a46.setLicenseId(a34.getLicenseId());// 驾驶证*
                a46.setFareType("1");//运价编码  ---》对应A4.6运价接口 *
                a46.setVehicleNo(a34.getVehicleNo());//车牌号 *
                a46.setBookDepTime(a34.getDistributeTime()); //预计上车时间 *
                a46.setWaitTime(new Long(0)); // 等待时间  秒
                a46.setDepLongitude(driverInfoLocations.get(0).getLongitude());// 上车 经度*
                a46.setDepLatitude(driverInfoLocations.get(0).getLatitude());//上车 纬度 *
                a46.setDepArea(orderInfo.getDeparture()); // 上车地点
                a46.setDepTime(orderInfo.getDepartTime());// 上车时间 *
                a46.setDestLongitude(driverInfoLocations.get(driverInfoLocations.size() - 1).getLongitude()); // 下车 经度 *
                a46.setDestLatitude(driverInfoLocations.get(driverInfoLocations.size() - 1).getLatitude());// 下车 纬度 *
                a46.setDestArea(orderInfo.getDestination()); // 下车地点
                a46.setDestTime(orderInfo.getDepartTime()); // 下车时间 *
                a46.setBookModel(carInfo.getModel());// 预定车型
                a46.setModel(carInfo.getModel()); // 实际使用车型
                a46.setDriveMile(a44.getDriveMile()); // 载客里程 km *
                a46.setDriveTime(new Long(a44.getDriveTime().toString())); //载客时间 秒  *
                a46.setWaitMile(new Long(0)); // 空行驶里程
                a46.setFactPrice(order.getRealPay().longValue()); // 实收金额 *
                a46.setPrice(order.getAmount().longValue()); // 应收金额
                a46.setCashPrice(new Long(0)); // 现金支付金额
                a46.setLineName("支付宝支付"); // 电子支付机构
                a46.setLinePrice(order.getAmount().longValue());// 电子支付金额
                a46.setPosName("暂无"); // POS机支付机构
                a46.setPosPrice(new Long(0)); // POS机支付金额
                a46.setBenfitPrice(order.getYhAmount().longValue()); // 优惠金额
                a46.setBookTip(new Long(0)); // 预约服务费
                a46.setPassengerTip(new Long(0)); // 附加费
                a46.setPeakUpPrice(new Long(0));// 高峰加价金额
                a46.setNightUpPrice(new Long(0)); //夜间加价金额
                a46.setFarUpPrice(new Long(0)); //远途加价金额 *
                a46.setOtherUpPrice(new Long(0)); //其他加价金额 *
                a46.setPayState(PayStatus.toString());//结算状态 *
                a46.setPayTime(order.getPayTime() == null ? null : DateUtil.dateToLong(order.getPayTime()));//乘客结算时间
                a46.setOrderMatchTime(order.getPayTime() == null ? null : DateUtil.dateToLong(order.getPayTime()));// 订单完成时间
                a46.setInvoiceStatus("0");// 发票状态 *
                a46.save();
                System.out.println("-------------------->>>>>>>>OK");


            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + a34.getOrderId());
            }


        }


    }

    public void updateTest() {


        List<A46> list = findCompany2();

        for (A46 a46 : list) {
            a46.setDepTime(Long.valueOf(a46.get("t1").toString()));

            a46.setDestTime(Long.valueOf(a46.get("t2").toString()));


            a46.update();
            System.out.println("--------------" + a46.getOrderId());

        }

    }

    public void Test() {

        List<A46> list = findCompany();

        for (A46 a46 : list) {


            boolean b = DateUtil.isValidDate(StringUtil.stringToDate(a46.getBookDepTime().toString()));

            boolean c = DateUtil.isValidDate(StringUtil.stringToDate(a46.getDepTime().toString()));

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
