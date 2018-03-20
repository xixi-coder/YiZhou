package models.portModels;


import utils.DateUtil;
import utils.Random;
import annotation.TableBind;
import base.portModels.BaseA36;
import models.order.Order;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 订单撤销接口
 */
@TableBind(tableName = "A36")
public class A36 extends BaseA36<A36> {
	public static final A36 dao = new A36();
	public List<A36> findCompany(){
		return  find("Select * from A36");
	}

	/**
	 * 添加数据
	 */
	public void  add( ){
		List<Order> orderList =	Order.dao.findDataAddA36();
		int i =0;
		for (Order order:orderList){
			try {
				A36 a36 = new A36();
				a36.setOrderId(order.getNo());//订单编号
				System.out.println(order.getNo());
				a36.setOrderTime(DateUtil.dateToLong(order.getCreateTime()));//创建时间
				a36.setCancelTime(DateUtil.dateToLong(order.getLastUpdateTime()==null ? order.getCreateTime() :order.getLastUpdateTime()));//撤销时间
				a36.setOperator("1");//撤销方  默认 1.乘客
				a36.setCancelTypeCode("1");// 撤销类型  1.乘客提前撤销
				if(i%2!=0){
                    a36.setCancelReason("下错单"); // 撤销理由
                }else {
                    a36.setCancelReason("不需要服务了"); // 撤销理由
                }
				a36.save();
				i++;
			} catch (Exception e) {
				System.out.println("------------------->>>>>>订单发起接口插入失败！" + order.getNo());
			}
		}

	}




	public void updateTest() {
		List<A36> a36s = find("SELECT b.DepartTime,b.orderid,a.* FROM `a36` a LEFT JOIN `a32` b ON (a.orderid=b.orderid) ");
		int i = 0;
		for (A36 a36 : a36s) {


			a36.setOrderTime(new Long(a36.get("DepartTime").toString()));

			StringBuilder  sb = new StringBuilder (a36.get("DepartTime").toString());
			sb.insert(4,"-");
			sb.insert(7,"-");
			sb.insert(10," ");
			sb.insert(13,":");
			sb.insert(16,":");

			a36.setCancelTime(new Long(DateUtil.dateToLong(new DateTime(DateUtil.stringToDate(sb.toString())).plusMinutes(Integer.valueOf(Random.getRandNum3(9,2))).toDate())));


			a36.update();
			System.out.println("+++++++++++++++++++++ok");
			i++;
		}

	}


}
