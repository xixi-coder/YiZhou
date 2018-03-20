package models.member;

import annotation.TableBind;
import base.Constant;
import base.models.BaseMemberInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import kits.Md5Kit;
import kits.StringsKit;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
@TableBind(tableName = "dele_member_info")
public class MemberInfo extends BaseMemberInfo<MemberInfo> {
    public static MemberInfo dao = new MemberInfo();
    public static String get;

    /**
     * 查询手机号是否存在
     *
     * @param phone
     * @return
     */
    public int findCountByPhone(String phone) {
        return findFirst(SqlManager.sql("memberInfo.findCountByPhone"), phone).getNumber("c").intValue();
    }

    /**
     * 通过登陆id查询用户信息
     *
     * @param memberLoginId
     * @return
     */
    public MemberInfo findByLoginId(int memberLoginId) {
        return findFirst(SqlManager.sql("memberInfo.findByLoginId"), memberLoginId);
    }


    /**
     * 通过手机号查询会员
     *
     * @param phone
     * @return
     */
    public MemberInfo findByPhone(String phone) {
        return findFirst(SqlManager.sql("memberInfo.findByPhone"), phone);
    }

    public MemberInfo creatMember(String phone, int budan, int company) {
        MemberInfo tmp = MemberInfo.dao.findByPhone(phone);
        if (tmp != null) {
            return tmp;
        }
        Date now = DateTime.now().toDate();
        final MemberInfo memberInfo = new MemberInfo();
        memberInfo.setNickName("先生/女士");
        memberInfo.setRealName("先生/女士");
        memberInfo.setPhone(phone);
        memberInfo.setFromType(budan);
        memberInfo.setCompany(company);
        memberInfo.setLevel(1);
        final MemberLogin memberLogin = new MemberLogin();
        memberLogin.setUserName(phone);
        memberLogin.setStatus(Constant.LoginStatus.LOGOUTED);
        memberLogin.setLoginStatus(Constant.LoginStatus.LOGOUTED);
        String pwd = Md5Kit.MD5(RandomStringUtils.randomAlphanumeric(6));
        String salt = RandomStringUtils.randomAlphabetic(4);
        String dbPwd = Md5Kit.MD5(pwd + salt);
        memberLogin.setPassword(dbPwd);
        memberLogin.setSalt(salt);
        memberLogin.setCreateTime(DateTime.now().toDate());
        memberLogin.setType(Constant.MEMBER);
        final MemberCapitalAccount memberCapitalAccount = new MemberCapitalAccount();
        memberCapitalAccount.setAmount(BigDecimal.ZERO);
        memberCapitalAccount.setPhone(phone);
        memberCapitalAccount.setAccount(phone);
        memberCapitalAccount.setLastUpdateTime(now);
        boolean isOk = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (memberLogin.save()) {
                    memberInfo.setLoginId(memberLogin.getId());
                    memberCapitalAccount.setLoginId(memberLogin.getId());
                    return memberCapitalAccount.save() && memberInfo.save();
                }
                return false;
            }
        });
        if (isOk) {
            return memberInfo;
        } else {
            return null;
        }
    }

    /**
     * 查询我的推荐人
     *
     * @param id
     * @return
     */
    public List<MemberInfo> findByTj(Integer id) {
        return find(SqlManager.sql("memberInfo.findByTj"), id);
    }

    /**
     * 查询改公司在线的会员
     *
     * @param companyId
     * @param logined
     * @return
     */
    public List<MemberInfo> findByCompanyAndLogined(int companyId, int logined) {
        return find(SqlManager.sql("memberInfo.findByCompanyAndLogined"), companyId, logined);
    }

    /**
     * 查询会员
     *
     * @param superAdmin
     * @param companyId
     * @param name
     * @return
     */
    public List<MemberInfo> findForCouponByCompany(boolean superAdmin, int companyId, String name) {
        String sqlWhere = SqlManager.sql("memberInfo.findForCouponByCompany");
        List<Object> params = Lists.newArrayList();
        if (!superAdmin) {
            sqlWhere = StringsKit.replaceSql(sqlWhere, " AND company = ? ");
            params.add(companyId);
        }
        if (!Strings.isNullOrEmpty(name)) {
            sqlWhere = StringsKit.replaceSql(sqlWhere, " AND (real_name LIKE ? OR nick_name LIKE ? OR phone LIKE ?) ");
            params.add("%" + name + "%");
            params.add("%" + name + "%");
            params.add("%" + name + "%");
        }
        return find(sqlWhere, params.toArray());
    }

    /**
     *网约车接口乘客基本信息
     */
    public List<MemberInfo> findDataAddA30(int page,int size){
        return find(SqlManager.sql("memberInfo.findDataAddA30"), page, size);
    }


    public List<MemberInfo> findMemberInfos(){
        return find(SqlManager.sql("memberInfo.findMemberInfos"));
    }
}
