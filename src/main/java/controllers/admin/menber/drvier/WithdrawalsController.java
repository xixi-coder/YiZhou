package controllers.admin.menber.drvier;

import annotation.Controller;
import base.Constant;
import base.controller.BaseAdminController;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import kits.StringsKit;
import models.driver.Withdrawals;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberLogin;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by BOGON on 2016/12/19.
 */
@Controller("/admin/member/driver/withdrawals")
public class WithdrawalsController extends BaseAdminController {
    public void index() {
        render("list.ftl");
    }

    public void list() {
        String whereSql;
        List<Object> params = Lists.newArrayList();
        if (isSuperAdmin()) {
            renderJson(dataTable(SqlManager.sql("withdrawalsLog.column"), SqlManager.sql("withdrawalsLog.where")));
        } else {
            whereSql = "AND dwl.company = ? ";
            params.add(getCompanyId());
            renderJson(dataTable(SqlManager.sql("withdrawalsLog.column"), SqlManager.sql("withdrawalsLog.where"), whereSql, params));
        }
    }

    /**
     * 审核提现申请
     */
    public void audit() {
        int id = getParaToInt("id", 0);
        if (id == 0) {
            renderAjaxError("审核内容不存在！");
            return;
        }
        String remark = getPara("remark", "");
        int status = getParaToInt("status", Constant.DataAuditStatus.AUDITOK);
        final Withdrawals withdrawals = Withdrawals.dao.findById(id);
        MemberLogin memberLogin = MemberLogin.dao.findById(withdrawals.getLoginId());
        boolean isOk = false;
        if (status == Constant.DataAuditStatus.AUDITOK) {
            withdrawals.setStatus(Constant.DataAuditStatus.AUDITOK);
            withdrawals.setRemark(remark);
            final CapitalLog capitalLog = new CapitalLog();
            capitalLog.setAmount(withdrawals.getAmount().multiply(BigDecimal.valueOf(-1)));
            capitalLog.setLoginId(withdrawals.getLoginId());
            capitalLog.setStatus(Constant.CapitalStatus.OK);
            if (memberLogin.getType() == Constant.DRIVER) {
                capitalLog.setNo(StringsKit.getCaptDriver());
            } else if (memberLogin.getType() == Constant.MEMBER) {
                capitalLog.setNo(StringsKit.getCaptMember());
            }
            capitalLog.setRemark("提现出来的金额,提现申请的信息{" + withdrawals.getId() + "}");
            capitalLog.setCreateTime(DateTime.now().toDate());
            capitalLog.setOperater(getUserId());
            capitalLog.setOperationType(Constant.CapitalOperationType.TX);
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return capitalLog.save() && withdrawals.update();
                }
            });
        } else if (status == Constant.DataAuditStatus.AUDITFAIL) {
            withdrawals.setStatus(Constant.DataAuditStatus.AUDITFAIL);
            withdrawals.setRemark(remark);
            final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(withdrawals.getLoginId());
            BigDecimal historyTxAmount = memberCapitalAccount.getTxAmount();
            historyTxAmount = historyTxAmount.add(withdrawals.getAmount());
            memberCapitalAccount.setTxAmount(historyTxAmount);
            memberCapitalAccount.setLastUpdateAmount(historyTxAmount);
            BigDecimal historyAmount = memberCapitalAccount.getAmount();
            historyAmount = historyAmount.add(withdrawals.getAmount());
            memberCapitalAccount.setAmount(historyAmount);
            memberCapitalAccount.setLastUpdateAmount(historyAmount);
            isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    return memberCapitalAccount.update() && withdrawals.update();
                }
            });
        }
        if (isOk) {
            renderAjaxSuccess("审核成功！");
        } else {
            renderAjaxError("审核失败");
        }
    }
}
