package models.member;

import annotation.TableBind;
import base.Constant;
import base.models.BaseMemberLogin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import jobs.activity.RegisterActivity;
import kits.Md5Kit;
import models.driver.DriverInfo;
import net.dreamlu.event.EventKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_member_login")
public class MemberLogin extends BaseMemberLogin<MemberLogin> {
    public static MemberLogin dao = new MemberLogin();

    /**
     * 获取用户登陆信息
     *
     * @param username
     * @return
     */
    public MemberLogin findByUserName(String username, int appType) {
        return findFirst(SqlManager.sql("memberLogin.findByUserName"), username, appType);
    }

    public int findByCount(String userName, int userId, int type) {
        return findFirst(SqlManager.sql("memberLogin.findByCount"), userName, userId, type).getNumber("c").intValue();
    }


    public MemberLogin findByName(String username) {
        return findFirst(SqlManager.sql("memberLogin.findByName"), username);
    }

    /**
     * 保存注册信息
     *
     * @param phone
     * @param password
     * @param deviceNo
     * @param memberType
     * @return
     */
    public boolean register(final String phone, String password, String deviceNo, final int memberType, final int tId, final String registerIp, final String registerIMSI, final String registerIMEI,final String macAddress,final String port) {
        final MemberLogin memberLogin = new MemberLogin();
        memberLogin.setDeviceNo(deviceNo);
        String salt = RandomStringUtils.randomAlphabetic(6).toLowerCase();
        String dbPassword = Md5Kit.MD5(password + salt);
        memberLogin.setPassword(dbPassword);
        memberLogin.setUserName(phone);
        memberLogin.setType(memberType);
        memberLogin.setStatus(0);
        memberLogin.setSalt(salt);
        memberLogin.setCreateTime(DateTime.now().toDate());
        final MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
        memberCapitalAccount.setPhone(phone);
        memberCapitalAccount.setCreateTime(DateTime.now().toDate());
        memberCapitalAccount.setStatus(1);
        memberCapitalAccount.setAmount(BigDecimal.ZERO);
        memberCapitalAccount.setAccount(phone);
        boolean isOk;
        MemberLogin me = MemberLogin.dao.findByUserName(phone, memberType);
        if (me != null) {
            return false;
        }
        isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (memberLogin.save()) {
                    memberCapitalAccount.setLoginId(memberLogin.getId());
                    if (memberType == Constant.MEMBER_TYPE_NORMAL) {
                        final MemberInfo memberInfo = new MemberInfo();
                        MemberInfo tmp = MemberInfo.dao.findByLoginId(tId);
                        if (tmp!=null&&tmp.getCompany() != null) {
                            memberInfo.setCompany(tmp.getCompany());
                        }
                        memberInfo.setPhone(phone);
                        memberInfo.setIntroducerLoginId(tId);
                        memberInfo.setRealName("先生/女士");
                        memberInfo.setNickName("先生/女士");
                        memberInfo.setFromType(Constant.FromType.APPTYPE);
                        memberInfo.setLoginId(memberLogin.getId());
                        memberInfo.setLevel(1);
                        memberInfo.setStatus(Constant.MemberInfoStatus.OK);
                        // registerIpd registerIMSI registerIMEI identifying port
                        memberInfo.setRegisterIp(registerIp);
                        memberInfo.setRegisterImei(registerIMEI);
                        memberInfo.setRegisterImsi(registerIMSI);
                        memberInfo.setMacAddress(macAddress);
                        memberInfo.setPort(port);
                        return memberInfo.save() && memberCapitalAccount.save();
                    } else {
                        final DriverInfo driverInfo = new DriverInfo();
                        DriverInfo tmp = DriverInfo.dao.findByLoginId(tId);
                        if (tmp!=null&&tmp.getCompany() != null) {
                            driverInfo.setCompany(tmp.getCompany());
                        }
                        driverInfo.setPhone(phone);
                        driverInfo.setRealName("师傅");
                        driverInfo.setNickName("师傅");
                        driverInfo.setIntroducerLoginId(tId);
                        driverInfo.setType(memberType + "");
                        driverInfo.setFromType(Constant.FromType.APPTYPE);
                        driverInfo.setLoginId(memberLogin.getId());
                        driverInfo.setRegisterIp(registerIp);
                        driverInfo.setRegisterImei(registerIMEI);
                        driverInfo.setRegisterImsi(registerIMSI);
                        return driverInfo.save() && memberCapitalAccount.save();
                    }
                }
                return false;
            }
        });
        return isOk;
    }

    /**
     * 查询手机号是否重复
     *
     * @param phone
     * @param appType
     * @return
     */
    public int findCountByPhoneAndType(String phone, int appType) {
        switch (appType) {
            case Constant.MEMBER_TYPE_DRIVER:
                return DriverInfo.dao.findCountByPhone(phone);
            case Constant.MEMBER_TYPE_NORMAL:
                return MemberInfo.dao.findCountByPhone(phone);
            default:
                return 0;
        }
    }

    /**
     * 查询已经登陆的会员
     *
     * @return
     */
    public List<MemberLogin> findLogin() {
        return find(SqlManager.sql("memberLogin.findLogin"), Constant.LoginStatus.LOGOUTED);
    }

    public MemberLogin getMemberLogin(int id) {
        return findFirst(SqlManager.sql("SELECT d.* FROM dele_member_login d\n" +
                "LEFT JOIN dele_driver_info i ON d.id = i.login_id\n" +
                "WHERE i.id = ?"),id);
    }


    public MemberLogin findMemberLoginByMemberInfoId(int memberId){
        return findFirst(SqlManager.sql("memberLogin.findMemberLoginByMemberInfoId"),memberId);
    }

    /**
     * 乘客基本信息接口
     * */
    public List<MemberLogin> passenger() {
        return find(SqlManager.sql("baseinfo.passenger"));
    }
}
