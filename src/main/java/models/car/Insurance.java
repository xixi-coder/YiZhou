package models.car;

import annotation.TableBind;
import base.models.BaseInsurance;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */
@TableBind(tableName = "dele_insurance")
public class Insurance extends BaseInsurance<Insurance> {

    public static Insurance dao = new Insurance();

    /**
     * 查询车辆的保险信息
     *
     * @param Vno
     * @return
     */
    public Insurance findByVno(String Vno) {
        return findFirst(SqlManager.sql("insurance.findByVno"), Vno);
    }


    public List<Insurance> findInfos(){
        return find(SqlManager.sql("insurance.findInfos"));
    }

}
