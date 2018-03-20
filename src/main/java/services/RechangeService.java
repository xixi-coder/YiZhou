package services;

import base.Constant;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import kits.SmsKit;
import models.driver.DriverInfo;
import models.member.CapitalLog;
import models.member.MemberCapitalAccount;
import models.member.MemberInfo;
import models.vip.VipInfo;
import models.vip.VipLog;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

@SuppressWarnings("ALL")
public class RechangeService {
    private Logger logger = LoggerFactory.getLogger(RechangeService.class);
    
    public void rechange(final CapitalLog capitalLog) {
        Date now = DateTime.now().toDate();
        //VIP 充值
        if (capitalLog.getOperationType() == Constant.CapitalOperationType.CZOFaliVIP
        || capitalLog.getOperationType() == Constant.CapitalOperationType.CZOFweixinVIP) {
            final VipInfo vipInfo = new VipInfo();
            final VipLog vipLog = new VipLog();
            vipInfo.setAmount(capitalLog.getAmount());
            vipInfo.setLoginId(capitalLog.getLoginId());
            vipInfo.setHistoricalAmount(vipInfo.getHistoricalAmount().add(capitalLog.getAmount()));
            boolean isOk = false;
            if (VipInfo.dao.findByLoginId(capitalLog.getLoginId()) != null) {
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (vipInfo.update()) {
                            capitalLog.setStatus(Constant.CapitalStatus.OK);
                            vipLog.setVipId(vipInfo.getId());
                            vipLog.setLoginId(capitalLog.getLoginId());
                            vipLog.setLogContent(capitalLog.getRemark());
                            return capitalLog.update() && vipLog.save();
                        }
                        return false;
                    }
                });
            } else {
                isOk = Db.tx(new IAtom() {
                    @Override
                    public boolean run() throws SQLException {
                        if (vipInfo.save()) {
                            capitalLog.setStatus(Constant.CapitalStatus.OK);
                            vipLog.setVipId(vipInfo.getId());
                            vipLog.setLoginId(capitalLog.getLoginId());
                            vipLog.setLogContent(capitalLog.getRemark());
                            return capitalLog.update() && vipLog.save();
                        }
                        return false;
                    }
                });
            }
            if (isOk) {
                logger.info("充值VIP回调完成：充值单号：{}", capitalLog.getNo());
            } else {
                logger.error("充值VIP回调完成：充值单号：{}", capitalLog.getNo());
            }
            
        } else {
            
            final MemberCapitalAccount memberCapitalAccount = MemberCapitalAccount.dao.findByLogid(capitalLog.getLoginId());
            BigDecimal historyAmount = memberCapitalAccount.getAmount();
            memberCapitalAccount.setAmount(historyAmount.add(capitalLog.getAmount()));
            memberCapitalAccount.setLastUpdateTime(now);
            memberCapitalAccount.setLastUpdateAmount(capitalLog.getAmount());
            boolean isOk = Db.tx(new IAtom() {
                @Override
                public boolean run() throws SQLException {
                    if (memberCapitalAccount.update()) {
                        capitalLog.setStatus(Constant.CapitalStatus.OK);
                        return capitalLog.update();
                    }
                    return false;
                }
            });
            if (isOk) {
                logger.info("充值回调完成：充值单号：{}", capitalLog.getNo());
                if (capitalLog.getNo().startsWith(Constant.AliPayOrderType.CZCUSTOMER)) {
                    MemberInfo memberInfo = MemberInfo.dao.findByLoginId(memberCapitalAccount.getLoginId());
                    SmsKit.rechange(memberInfo.getPhone(), memberInfo.getRealName(), memberCapitalAccount.getAccount(), capitalLog.getAmount().toString(), memberInfo.getCompany());
                } else if (capitalLog.getNo().startsWith(Constant.AliPayOrderType.CZFRIVER)) {
                    DriverInfo driverInfo = DriverInfo.dao.findByLoginId(memberCapitalAccount.getLoginId());
                    SmsKit.rechange(driverInfo.getPhone(), driverInfo.getRealName(), memberCapitalAccount.getAccount(), capitalLog.getAmount().toString(), driverInfo.getCompany());
                }
            } else {
                logger.error("充值回调异常：充值单号：{}", capitalLog.getNo());
            }
            
            
        }
        
    }
    
    private RechangeService() {
    }
    
    private static class RechangeServiceHolder {
        static RechangeService instance = new RechangeService();
    }
    
    public static RechangeService getInstance() {
        return RechangeServiceHolder.instance;
    }
}