package models.special;

import annotation.TableBind;
import base.models.BaseSpecialDriver;
import com.jfinal.plugin.activerecord.Db;
import plugin.sqlInXml.SqlManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOGONj on 2016/8/23.
 */
@TableBind(tableName = "dele_special_driver")
public class SpecialDriver extends BaseSpecialDriver<SpecialDriver> {
    public static SpecialDriver dao = new SpecialDriver();

    /**
     *
     * @param id
     * @return
     */
    public List<SpecialDriver> findByDriverInfoId(int id){
        return find(SqlManager.sql("special.driverId"),id);
    }

    public List<SpecialDriver> findByLineStatus(int status){
        return find(SqlManager.sql("special.lineStatus"),status);
    }

    public List<SpecialDriver> lineexist(int id,int lineId){
        return find(SqlManager.sql("special.lineexist"),id,lineId);
    }

    public List<SpecialDriver> findByLineId(int id){
        return find(SqlManager.sql("special.lineId"),id);
    }


    /**
     * 获取司机状态
     * @param driverId
     * @param orderId
     * @return
     */

    public SpecialDriver findLineStatus(int driverId, int orderId) {

        return findFirst(SqlManager.sql("specialdriver.findLineStatus"), driverId,orderId);

    }

    /**
     * 查询数据
     * @param memberid
     * @param lineId
     * @return
     */

    public int updateline(int memberid, int lineId) {
        int updateline = Db.update(SqlManager.sql("specialdriver.updateline"), memberid, lineId);
        return updateline;
    }


    /**
     * 更新专线司机状态
     * @param driverId
     * @param OrderId
     * @return
     */
    public SpecialDriver findByDriverAndOrder(int OrderId,int driverId) {

        SpecialDriver specialDriver = findFirst(SqlManager.sql("specialdriver.findByDriverAndOrder"),OrderId, driverId);
        return specialDriver;
    }

    /**
     * 根据司机该司机能接总人数
     * @param driverId
     * @param orderId
     * @return
     */

    public SpecialDriver findTotalPeople(int driverId,int orderId) {

        SpecialDriver specialDriver = findFirst(SqlManager.sql("specialdriver.findTotalPeopleByDriverId"), driverId,orderId);
        System.out.println(specialDriver.getPeople());
        return specialDriver;
    }
}
