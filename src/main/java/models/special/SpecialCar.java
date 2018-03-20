package models.special;

import annotation.TableBind;
import base.Constant;
import base.models.BaseSpecialCar;
import com.jfinal.plugin.activerecord.Db;
import models.driver.DriverInfo;
import models.member.MemberInfo;
import plugin.sqlInXml.SqlManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOGONj on 2016/8/23.
 */
@TableBind(tableName = "dele_special_car")
public class SpecialCar extends BaseSpecialCar<SpecialCar> {
    public static SpecialCar dao = new SpecialCar();

    /**
     * 根据当前城市获取行程列表
     * @param type
     * @param lineType
     * @param sCityCode
     * @param pageStart
     * @param pageSize
     * @return
     */

    public List<SpecialCar> lineListByCity(int type,int lineType,String sCityCode,int pageStart,int pageSize) {
        if (type == Constant.DRIVER) {
            return find(SqlManager.sql("special.driverGetList"), Constant.LineStatus.ENABLE, lineType, sCityCode, pageStart, pageSize);
        } else {
            if (sCityCode == null){
                List<SpecialCar> specialCars = find(SqlManager.sql("special.findLineAll"), Constant.LineStatus.ENABLE, lineType, pageStart, pageSize);
                return specialCars;
            }else {
                return find(SqlManager.sql("special.findLineByCityCode"), Constant.LineStatus.ENABLE, lineType,sCityCode, pageStart, pageSize);
            }
        }
    }

    /**
     * 乘客获取所有获取行程列表
     * @param type
     * @param lineType
     * @param pageStart
     * @param pageSize
     * @return
     */

    public List<SpecialCar> memberGetLineList(int type, int lineType, MemberInfo memberInfo,int pageStart, int pageSize) {
                List<SpecialCar> specialCars = find(SqlManager.sql("special.findLineAll"), Constant.LineStatus.ENABLE, lineType,pageStart, pageSize);
                return specialCars;
    }


    /**
     * 返回所有行程
     * @param type
     * @param lineType
     * @param pageStart
     * @param pageSize
     * @return
     */
    public List<SpecialCar> lineList(int type, int lineType, DriverInfo driverInfo, int pageStart, int pageSize) {
        List list = find(SqlManager.sql("special.findAll"),Constant.LineStatus.ENABLE,lineType,driverInfo.getCompany(),pageStart,pageSize);
        return list;
    }

    /**
     * 根据司机ID获取专线行程列表
     * @param type
     * @param driverId
     * @param pageStart
     * @param pageSize
     * @return
     */

    public List<SpecialCar> lineListByDriverId(int type, int driverId, int pageStart, int pageSize) {

        return find(SqlManager.sql("special.lineListByDriverId"), driverId, pageStart, pageSize);

    }









    /**
     * 根据司机ID获取已接订单
     * @param driverId

     * @return
     */

    public List<SpecialCar> getOrder(int driverId) {

        return find(SqlManager.sql("special.getOrderByDriverId"), driverId,Constant.LineStatus.ENABLE);

    }



    /**
     * 根据司机ID获取已接订单ID
     * @param driverId
     * @return
     */

    public List<SpecialCar> getOrderIdForAccept(int driverId) {

        return find(SqlManager.sql("special.getOrderIdByDriverId"), driverId,Constant.ServiceType.ZhuanXian,Constant.OrderStatus.ACCEPT);

    }


    /**
     * 根据司机ID获取未出发订单ID
     * @param driverId
     * @return
     */

    public List<SpecialCar> getOrderIdForNoStart(int driverId) {

        return find(SqlManager.sql("special.getOrderIdByDriverId"), driverId,Constant.ServiceType.ZhuanXian,Constant.OrderStatus.ACCEPT);

    }

    /**
     * 根据司机ID获取已出发订单ID
     * @param driverId
     * @return
     */

    public List<SpecialCar> getOrderIdForStart(int driverId) {

        return find(SqlManager.sql("special.getOrderIdByDriverId"), driverId,Constant.ServiceType.ZhuanXian,Constant.OrderStatus.START);

    }

    /**
     * 删除个人专线
     * @param type
     * @param memberid
     * @param lineId
     * @return
     */

    public int deleteline(int type, int memberid, int lineId) {
        int deleteline = Db.update(SqlManager.sql("special.deleteline"), memberid, lineId);
        return deleteline;
    }

    /**
     * 将之前已选择但删除专线状态改变
     * @param memberid
     * @param lineId
     * @return
     */

    public int updateline( int memberid, int lineId) {
        int updateline = Db.update(SqlManager.sql("special.updateline"), memberid, lineId);
        return updateline;
    }

    /**
     * 根据会员ID获取专线行程列表（司机已接单行程）
     * @param type
     * @param memberId
     * @param pageStart
     * @param pageSize
     * @return
     */

    public List<SpecialCar> lineListByMemberId(int type, int memberId, int pageStart, int pageSize) {
        return find(SqlManager.sql("special.lineListByMemberId"), memberId,Constant.OrderStatus.PAYED,Constant.OrderStatus.CANCEL, pageStart, pageSize);
    }

    /**
     * 根据会员ID获取专线行程列表(司机未接单行程)
     * @param type
     * @param memberId
     * @param pageStart
     * @param pageSize
     * @return
     */

    public List<SpecialCar> lineListByMemberId1(int type, int memberId, int pageStart, int pageSize) {
        List<SpecialCar> lineList = new ArrayList<SpecialCar>();
        lineList = find(SqlManager.sql("special.lineListByMemberId1"), memberId,Constant.ServiceType.ZhuanXian, Constant.OrderStatus.CREATE,pageStart, pageSize);
        return lineList;
    }

    /**
     * 获取该专线司机regisId
     * @param lineId
     * @return
     */
    public List getRegisId(int lineId) {

        return find(SqlManager.sql("special.getRegisId"), lineId);

    }

    /**
     * 删除个人专线
     * @param lineId
     * @return
     */

    public int updateflag(int lineId) {
        return Db.update(SqlManager.sql("special.updateflag"),lineId);

    }

}
