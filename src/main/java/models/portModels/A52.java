package models.portModels;


import annotation.TableBind;
import base.portModels.BaseA52;
import models.member.MemberAppraise;

import java.util.List;

/**
 * 乘客评价接口
 */
@TableBind(tableName = "A52")
public class A52 extends BaseA52<A52> {
	public static final A52 dao = new A52();
	public List<A52> findCompany(){
		return  find("Select * from A52");
	}

	public List<A52> findCompany2(){
		String sql ="SELECT a52.id, a46.OrderId as oid,\n" +
				"\t\t\t\ta46.OrderMatchTime as t1,\n" +
				"\t\t\t\ta52.EvaluateTime \n" +
				"\t\n" +
				"\t\t\t\t\n" +
				"\n" +
				"\n" +
				"FROM a52 LEFT JOIN a46 ON a52.OrderId = a46.OrderId\n" +
				"\n";
		return  find(sql);
	}


	public void  add(){
		List<MemberAppraise> list = MemberAppraise.dao.findDataByA52();
		List<A32> list1 =A32.dao.findCompany();


		for (int i =0;i<=120;i++) {
			try {
				A52 a52 = new A52();
				a52.setOrderId(list1.get(i).getOrderId());
				a52.setEvaluateTime(list1.get(i).getDepartTime()+010000);
				a52.setServiceScore(5);
				a52.setDriverScore(5);
				a52.setVehicleScore(5);
				if(i%5==0){
					a52.setDetail("司机态度不错");
				}else {
					a52.setDetail("安全可靠");
				}

				a52.save();
				System.out.println("---------------------->>>>>>>>>>ok");
			} catch (Exception e) {
				System.out.println("----------------------------->>>>>>>>>>>乘客评价接口插入失败"+list1.get(i).getOrderId());
			}

		}

		/*for (MemberAppraise memberAppraise:list){
			try {
				A52 a52 = new A52();
				a52.setOrderId(memberAppraise.get("oid").toString());
				a52.setEvaluateTime(memberAppraise.getEvaluateTime());
				a52.setServiceScore(memberAppraise.getServiceScore());
				a52.setDriverScore(memberAppraise.getDriverScore());
				a52.setVehicleScore(memberAppraise.getVehicleScore());
				a52.setDetail(memberAppraise.getDetail());
				a52.save();
				System.out.println("---------------------->>>>>>>>>>ok");
			} catch (Exception e) {
				System.out.println("----------------------------->>>>>>>>>>>乘客评价接口插入失败"+memberAppraise.get("oid").toString());
			}

		}*/


	}

	public void   updateTest(){

		List<A52> list = findCompany2();

		for (A52 a52:list){
			a52.setEvaluateTime(Long.valueOf(a52.get("t1").toString()));
			a52.update();
			System.out.println(a52.getEvaluateTime()+"================"+a52.get("t1").toString());
		}

	}


}
