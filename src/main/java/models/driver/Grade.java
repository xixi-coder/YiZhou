package models.driver;

import annotation.TableBind;
import base.models.BaseGrade;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
@TableBind(tableName = "dele_grade")
public class Grade extends BaseGrade<Grade> {
    public static Grade dao = new Grade();

    /**
     * 查询订单的数量
     *
     * @param orderId
     * @return
     */
    public int countByOrder(int orderId) {
        return findFirst(SqlManager.sql("grade.countByOrder"), orderId).getNumber("c").intValue();
    }

    /**
     * 乘客评价信息接口
     *
     * @param
     * @return
     */
    public List<Grade> passenger() {
        return find(SqlManager.sql("rated.passenger"));
    }

    /**
     * 司机平均好评分数
     *
     * @param masterId
     * @return
     */
    public double driverFavorableRate(int masterId) {
        return findFirst(SqlManager.sql("grade.driverFavorableRate"), masterId).getNumber("avg") == null ? 0 : findFirst(SqlManager.sql("grade.driverFavorableRate"), masterId).getNumber("avg").doubleValue();
    }
}
