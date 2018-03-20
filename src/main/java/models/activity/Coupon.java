package models.activity;

import annotation.TableBind;
import base.Constant;
import base.datatable.DataTablePage;
import base.models.BaseCoupon;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import plugin.sqlInXml.SqlManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
@TableBind(tableName = "dele_coupon")
public class Coupon extends BaseCoupon<Coupon> {
    public static Coupon dao = new Coupon();

    /**
     * 查询公司的优惠券
     *
     * @param company
     * @param isAdmin
     * @return
     */
    public List<Coupon> findByCompanyAndDate(int company, boolean isAdmin, Date date) {
        String sqlStr = SqlManager.sql("coupon.findByCompany");
        if (isAdmin) {
            return find(sqlStr, date);
        } else {
            sqlStr = sqlStr + " AND company = ? ";
            return find(sqlStr, date, company);
        }
    }

    public List<Coupon> findByCompanyAndDate(int company, Date date) {
        String sqlStr = SqlManager.sql("coupon.findByCompany");
            sqlStr = sqlStr + " AND company = ? ";
            return find(sqlStr, date, company);
        }

    /**
     * 查询公司下的优惠劵
     * @param registerTime
     * @param companId
     * @return
     */
    public List<Coupon> findByCidAndDate( DateTime registerTime, int companId) {
        return find(SqlManager.sql("coupon.findByCidAndDate"), companId, registerTime.toDate());
    }


}
