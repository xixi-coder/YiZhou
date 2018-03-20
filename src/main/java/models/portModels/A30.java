package models.portModels;


import utils.DateUtil;
import annotation.TableBind;
import base.Constant;
import base.portModels.BaseA30;
import models.member.MemberInfo;

import java.util.Date;
import java.util.List;

/**
 * 乘客基本信息
 */
@TableBind(tableName = "A30")
public class A30 extends BaseA30<A30> {
    public static final A30 dao = new A30();

    public List<A30> findCompany() {
        return find("Select * from A30");
    }

    /**
     * 添加数据
     */
    public void setInfos() {
        List<MemberInfo> memberInfoList = MemberInfo.dao.findMemberInfos();
        for (MemberInfo memberInfo : memberInfoList) {
            try {
                A30 a30 = new A30();
                a30.setPassengerPhone((String) memberInfo.get("PassengerPhone"));//乘客手机号
                a30.setPassengerName((String) memberInfo.get("PassengerName"));//乘客昵称
                a30.setRegisterDate(DateUtil.dateToLongYMD((Date) memberInfo.get("RegisterDate")));
                int g = memberInfo.get("PassengerGender",1);
                switch (g){
                    case 1 : a30.setPassengerGender("1");//乘客性别
                        break;

                    case 2 : a30.setPassengerGender("9");//乘客性别
                        break;

                    case 0 : a30.setPassengerGender("2");//乘客性别
                        break;

                    default:a30.setPassengerGender("9");//乘客性别
                        break;
                }
                int g1 = memberInfo.get("State");
                switch (g1){
                    case 1 : a30.setState(0);//0.有效
                        break;

                    case 0 : a30.setState(1);//0.失效
                        break;

                    default: a30.setState(0);//0.有效
                        break;
                }

                a30.setFlag(Constant.Flag.ADD);//1.新增
                a30.setUpdateTime(DateUtil.UpdateTime());//更新时间
                a30.save();
                System.out.println("乘客手机号码："+memberInfo.get("PassengerPhone")+"插入A30成功");
                // update("update quertz set page="+page+" ,size="+size+"  where  tableId=30 ;");
            }catch (Exception e){
                System.out.println("乘客手机号码："+memberInfo.get("PassengerPhone")+"插入A30错误");
                continue;
            }

        }

    }


}
