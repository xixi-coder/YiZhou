package models.order;

import annotation.TableBind;
import base.models.BaseOrderLog;
import plugin.sqlInXml.SqlManager;

import java.util.List;

/**
 * Created by BOGONj on 2016/9/5.
 */
@TableBind(tableName = "dele_order_log")
public class OrderLog extends BaseOrderLog<OrderLog> {
    public static OrderLog dao = new OrderLog();

    public List<OrderLog> findByOrder(int orderId) {
        return find(SqlManager.sql("orderLog.findByOrder"), orderId);
    }

    /**
     * 通过订单和司机查询订单日志
     *
     * @param orderId
     * @param driver
     * @return
     */
    public List<OrderLog> findByOrderAndLoginId(int orderId, int driver) {
        return find(SqlManager.sql("orderLog.findByOrderAndLoginId"), orderId, driver);
    }

    /**
     * 通订单的action查询订单的操作日期
     * @param orderId
     * @param start
     * @param id
     * @return
     */
    public OrderLog findByOrderAndActionAndLoginId(int orderId, int start, Integer id) {
        return findFirst(SqlManager.sql("orderLog.findByOrderAndActionAndLoginId"), orderId, start, id);
    }

    /**
     * 查询支付的订单日志
     * @param orderId
     * @param payed
     * @return
     */
    public OrderLog findByOrderAndPayAction(int orderId, int payed) {
        return findFirst(SqlManager.sql("orderLog.findByOrderAndPayAction"),orderId,payed);
    }

    //运营证经营出发
    public List<OrderLog> start() {
        return find(SqlManager.sql("operate.start"));
    }

    //到达终点
    public List<OrderLog> arrive() {
        return find(SqlManager.sql("operate.arrive"));
    }

    //支付
    public  List<OrderLog> pay() {
        return find(SqlManager.sql("operate.pay"));
    }

    //运营证经营出发
    public List<OrderLog> findDataByA42(int action) {
        return find(SqlManager.sql("operate.findDataByA42"),action);
    }
}
