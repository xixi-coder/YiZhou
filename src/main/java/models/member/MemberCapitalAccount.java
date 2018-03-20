package models.member;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseMemberCapitalAccount;
import com.google.common.collect.Lists;
import kits.StringsKit;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
@TableBind(tableName = "dele_member_capital_account", pks = "login_id")
public class MemberCapitalAccount extends BaseMemberCapitalAccount<MemberCapitalAccount> {
    public static MemberCapitalAccount dao = new MemberCapitalAccount();

    /**
     * 查询账号信息
     *
     * @param id
     * @return
     */
    public MemberCapitalAccount findByLogid(Integer id) {
        return findFirst(SqlManager.sql("memberCapital.findByLogid"), id);
    }

    public java.math.BigDecimal getAmount() {
        if (get("amount") == null) {
            return BigDecimal.ZERO;
        }
        return get("amount");
    }

    public void saveAccountAndLog(MemberCapitalAccount memberCapitalAccount, CapitalLog capitalLog) {
        capitalLog.setCreateTime(DateTime.now().toDate());
        memberCapitalAccount.setLastUpdateTime(DateTime.now().toDate());
        memberCapitalAccount.update();
        capitalLog.save();
    }

    /**
     * 计算提现金额
     *
     * @param memberCapitalAccount
     * @param amount
     * @return
     */
    public synchronized MemberCapitalAccount calTxAmount(MemberCapitalAccount memberCapitalAccount, BigDecimal amount) {
        BigDecimal txAmount = memberCapitalAccount.getTxAmount() == null ? BigDecimal.ZERO : memberCapitalAccount.getTxAmount();
        txAmount = txAmount.add(amount);
        if (txAmount.compareTo(BigDecimal.ZERO) > 0) {
            memberCapitalAccount.setTxAmount(txAmount);
        } else {
            memberCapitalAccount.setTxAmount(BigDecimal.ZERO);
        }
        memberCapitalAccount.setLastUpdateTxAmount(amount);
        return memberCapitalAccount;
    }
}
