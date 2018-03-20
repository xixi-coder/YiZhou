package models.portModels;


import utils.DateUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA34;
import dto.portDto.LongitudeAndLatitude;
import models.order.Order;
import models.order.OrderTrip;

import java.util.List;

/**
 * 订单成功接口
 */
@TableBind(tableName = "A34")
public class A34 extends BaseA34<A34> {
    public static final A34 dao = new A34();

    public List<A34> findCompany() {
        return find("Select * from A34");
    }

    /**
     * 添加
     */
    public void add() {
        List<Order> orderList = Order.dao.findDataAddA34();
        for (Order order : orderList) {
            try {
                A34 a34 = new A34();
                System.out.println(order.getNo() + "------------" + order.getId());
                LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                if (dep != null) {
                    a34.setOrderId(order.getNo());//订单号
                    a34.setLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//出发点经度
                    a34.setLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//出发点纬度
                    a34.setEncrypt(0);
                    a34.setLicenseId(order.get("LicenseId").toString());//驾驶证
                    a34.setDriverPhone(order.get("DriverPhone").toString());//司机手机
                    a34.setVehicleNo(order.get("VehicleNo").toString());//车牌号
                    a34.setDistributeTime(DateUtil.dateToLong(order.getSetouttime()));//派单时间
                    a34.save();
                }
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + order.getNo());
            }
        }

    }


    public void addTestData() {
        List<Order> orderList = Order.dao.findDataAddA34();
        List<A22> driverInfo = A22.dao.findDriverInfo();
        int i = 0;
        for (Order order : orderList) {
            if (i == driverInfo.size()) {
                i = 0;
            }
            try {
                if (order.getStatus() == 6) {
                    A36 a36 = new A36();
                    a36.setOrderId(order.getNo());//订单编号
                    System.out.println(order.getNo()+" 撤销     ++++");
                    a36.setOrderTime(DateUtil.dateToLong(order.getCreateTime()));//创建时间
                    a36.setCancelTime(DateUtil.dateToLong(order.getLastUpdateTime()));//撤销时间
                    a36.setOperator("1");//撤销方  默认 1.乘客
                    a36.setCancelTypeCode("1");// 撤销类型  1.乘客提前撤销
                    if (i % 2 != 0) {
                        a36.setCancelReason("下错单"); // 撤销理由
                    } else {
                        a36.setCancelReason("不需要服务了"); // 撤销理由
                    }
                    a36.save();
                } else {
                    A34 a34 = new A34();
                    System.out.println(order.getNo() + " 成功     ++++");
                    a34.setOrderId(order.getNo());//订单号
                    a34.setLongitude(new Long(Constant.DCMFMT.format(order.get("DepLongitude") == null ? 0 : Double.valueOf(order.get("DepLongitude").toString()) / 1E-6)));//出发点经度
                    a34.setLatitude(new Long(Constant.DCMFMT.format(order.get("DepLatitude") == null ? 0 : Double.valueOf(order.get("DepLatitude").toString()) / 1E-6)));//出发点纬度
                    a34.setEncrypt(0);
                    a34.setLicenseId(driverInfo.get(i).getLicenseId());//驾驶证
                    a34.setDriverPhone(driverInfo.get(i).getDriverPhone());//司机手机
                    a34.setVehicleNo(driverInfo.get(i).get("VehicleNo").toString());//车牌号
                    a34.setDistributeTime(DateUtil.dateToLong(order.getCreateTime()));//派单时间
                    a34.save();
                }
                i++;
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + order.getNo());
            }


        }


    }


    public void updateTest() {
        List<A34> a34s = find("SELECT b.DepartTime,b.orderid,a.* FROM `a34` a LEFT JOIN `a32` b ON (a.orderid=b.orderid) ");
        int i = 0;
        for (A34 a34 : a34s) {


            a34.setDistributeTime(new Long(a34s.get(i).get("DepartTime").toString()));
            a34.update();
            System.out.println("+++++++++++++++++++++ok");
            i++;
        }

    }

}
