package kits;

import base.Constant;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by BOGONj on 2016/9/1.
 */
public class StringsKit {
    public static String getSalt() {
        return RandomStringUtils.randomAlphanumeric(6).toLowerCase();
    }

    public static String getOrderNo() {
        return Constant.AliPayOrderType.NomorlOrder + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(6).toUpperCase();
    }

    public static String getCaptDriver() {
        return Constant.AliPayOrderType.CZFRIVER + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(4);
    }

    public static String getCaptMember() {
        return Constant.AliPayOrderType.CZCUSTOMER + DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + RandomStringUtils.randomAlphabetic(4);
    }

    public static String getCouponNo() {
        return RandomStringUtils.randomNumeric(12);
    }

    /**
     * 拼接sql
     *
     * @param sql
     * @param appendStr
     * @return
     */
    public static String replaceSql(String sql, String appendStr) {
        return sql.replace(Constant.SqlStrings.WHERE, " " + appendStr + " " + Constant.SqlStrings.WHERE);
    }
}
