package models.member;

import annotation.TableBind;
import base.Constant;
import base.models.BaseCapitalLog;
import com.google.common.collect.Lists;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/10/5.
 */
@TableBind(tableName = "dele_capital_log")
public class CapitalLog extends BaseCapitalLog<CapitalLog> {
    public static CapitalLog dao = new CapitalLog();


    /**
     * 查询账户操作明细
     *
     * @param id
     * @param pageStart
     * @param pageSize
     * @return
     */
    public List<CapitalLog> findByLoginId(Integer id, int pageStart, int pageSize) {
        return find(SqlManager.sql("capitalLog.findByLoginId"), id, Constant.CapitalStatus.OK,pageStart, pageSize);
    }

    /**
     * 通过充值记录的流水号查询充值记录
     *
     * @param out_trade_no
     * @return
     */
    public CapitalLog findByNo(String out_trade_no) {
        return findFirst(SqlManager.sql("capitalLog.findByNo"), out_trade_no);
    }

    /**
     * 按照时间查询收入
     *
     * @param startOfMonth
     * @param endOfMonth
     * @param loginId
     * @return
     */
    public Number findByStartAndEndWithAccount(Date startOfMonth, Date endOfMonth, Integer loginId) {
        List<Object> params = Lists.newArrayList();
        params.add(Constant.CapitalOperationType.DJDAISHOU);
        params.add(Constant.CapitalOperationType.KCDAISHOU);
        params.add(Constant.CapitalOperationType.ZCDAISHOU);
        params.add(Constant.CapitalOperationType.CZDAISHOU);
        params.add(Constant.CapitalOperationType.CASHSHOURU);
        params.add(Constant.CapitalOperationType.SHUNFENGCHE);
        params.add(Constant.CapitalOperationType.ZHUANXIAN);
        params.add(Constant.CapitalOperationType.HANGKONGZHUANXIAN);
        params.add(startOfMonth);
        params.add(endOfMonth);
        params.add(loginId);
        params.add(Constant.CapitalStatus.OK);
        return findFirst(SqlManager.sql("capitalLog.findByStartAndEndWithAccount"), params.toArray()).getNumber("s");
    }

    public List<CapitalLog> findByLoginIdAndFanli(Integer id, int pageStart, int pageSize) {
        return find(SqlManager.sql("capitalLog.findByLoginIdAndFanli"), id, Constant.CapitalOperationType.TUIJIANSIJIJIANGLI, pageStart, pageSize);
    }
}
