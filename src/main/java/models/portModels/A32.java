package models.portModels;


import utils.DateUtil;
import utils.Random;
import utils.StringUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA32;
import dto.portDto.LongitudeAndLatitude;
import models.order.Order;
import models.order.OrderTrip;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 订单发起接口
 */
@TableBind(tableName = "A32")
public class A32 extends BaseA32<A32> {
    public static final A32 dao = new A32();

    public List<A32> findCompany() {
        return find("Select * from A32");
    }


    public List<A32> findTestData() {
        return find(" SELECT * FROM A32 WHERE id NOT IN (2024,2025) ");
    }

    public A32 findData(String orderId) {
        return findFirst(" SELECT * FROM A32 WHERE orderId='" + orderId+"' ");
    }


    public void add() {
        List<Order> orderList = Order.dao.findDataAddA34();
        for (Order order:orderList) {
            try {
                A32 a32 = new A32();
                System.out.println(order.getNo() + "------------"+order.getId());
                a32.setOrderId(order.getNo());//订单号
                a32.setDepartTime(DateUtil.dateToLong(order.getSetouttime()));//预计用车时间
                a32.setOrderTime(DateUtil.dateToLong(order.getCreateTime()));//订单发起时间
                a32.setPassengerNote("无");//乘客备注
                a32.setDeparture(order.get("Departure").toString());//出发地
                a32.setDestination(order.get("Destination").toString());//目的地
                a32.setDepLongitude(new Long(Constant.DCMFMT.format(order.get("DepLongitude") == null ? 0 : Double.valueOf(order.get("DepLongitude").toString()) / 1E-6)));//出发点经度
                a32.setDepLatitude(new Long(Constant.DCMFMT.format(order.get("DepLatitude") == null ? 0 : Double.valueOf(order.get("DepLatitude").toString()) / 1E-6)));//出发点纬度
                a32.setDestLongitude(new Long(Constant.DCMFMT.format(order.get("DestLongitude") == null ? 0 : Double.valueOf(order.get("DestLongitude").toString()) / 1E-6)));//目的地经度
                a32.setDestLatitude(new Long(Constant.DCMFMT.format(order.get("DestLongitude") == null ? 0 : Double.valueOf(order.get("DestLongitude").toString()) / 1E-6)));//目的地纬度
                a32.setEncrypt(0);
                a32.setFareType("1");
                a32.save();
            } catch (Exception e) {
                System.out.println("------------------->>>>>>订单发起接口插入失败！" + order.getNo());
            }

        }


    }



    public void addTestData() {

        for (int i =0;i<700;i++){
            List<Order> orderList = Order.dao.findDataAddA32();
            orderList.remove(0);//只留一个订单
            for (Order order : orderList) {
                order.setNo("SJDD20170719"+ Random.getRandNum()+ StringUtil.randomStr(6));//随机生成订单号
                try {
                    A32 a32 = new A32();
                    System.out.println(order.getNo() + "------------" + order.getId());
                    LongitudeAndLatitude dep = OrderTrip.dao.findByDriverAndTime(order.getId());
                    if (dep != null) {
                        a32.setOrderId(order.getNo());//订单号
                        a32.setDepartTime(DateUtil.dateToLong(order.getSetouttime()));//预计用车时间
                        a32.setOrderTime(dep.getStartTime());//订单发起时间
                        a32.setPassengerNote("无");//乘客备注
                        a32.setDeparture(order.getReservationAddress());//出发地
                        a32.setDestination(order.getDestination());//目的地
                        a32.setDepLongitude(new Long(Constant.DCMFMT.format(dep.getLongitude())));//出发点经度
                        a32.setDepLatitude(new Long(Constant.DCMFMT.format(dep.getLatitude())));//出发点纬度
                        a32.setDestLongitude(new Long(Constant.DCMFMT.format(dep.getLongitudeEnd())));//目的地经度
                        a32.setDestLatitude(new Long(Constant.DCMFMT.format(dep.getLatitudeEnd())));//目的地纬度
                        a32.setEncrypt(0);
                        a32.setFareType("1");
                        a32.save();
                    }
                } catch (Exception e) {
                    System.out.println("------------------->>>>>>订单发起接口插入失败！" + order.getNo());
                }

            }

        }





    }

    public void  updateTest(){
        List<A32> a32List = findCompany();

        for (A32 a32:a32List){
            a32.setDepartTime(new Long("201707" + Random.getRandNum3(1, 1)+Random.getRandNum3(9, 1)+ Random.getRandNum3(5, 6)));

            StringBuilder  sb = new StringBuilder (a32.getDepartTime().toString());
            sb.insert(4,"-");
            sb.insert(7,"-");
            sb.insert(10," ");
            sb.insert(13,":");
            sb.insert(16,":");

            System.out.println(sb.toString());


            a32.setOrderTime(new Long(DateUtil.dateToLong(new DateTime( DateUtil.stringToDate(sb.toString())).plusHours(1).toDate())));
            a32.update();
            System.out.println("------------------->>>>>>ok！" +a32.getOrderId());

        }

    }


    public void Test() {

        List<A32> list = findCompany();

        for (A32 a46 : list) {



        boolean b=    DateUtil.isValidDate(StringUtil.stringToDate(a46.getDepartTime().toString()));
        boolean c=   DateUtil.isValidDate(StringUtil.stringToDate(a46.getOrderTime().toString()));

            if(!b||!c){

                System.out.println(a46.getOrderId() + "            ++++++  ");
            }





        }

    }



}
