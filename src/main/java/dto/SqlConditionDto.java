package dto;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by admin on 2016/10/15.
 */
public class SqlConditionDto {
    public static String LLIKE = "LLIKE";
    public static String RLIKE = "RLIKE";
    public static String LIKE = "LIKE";
    public static String GTE = "GTE";
    public static String GT = "GT";
    public static String LTE = "LTE";
    public static String LT = "LT";
    public static String BETWEEN = "BETWEEN";
    public static String CONDITION = "-";
    public static String DATETIME = "DT_";
    public static String DATE = "D_";
    public static String EQ = "EQ";
    public static String ISNULL = "ISNULL";
    public static String ISNOTNULL = "ISNOTNULL";
    public static String NEQ = "MEQ";

    public static String[] getCondition(String val, String val1) {
        String[] a = new String[2];
        if (StringUtils.equals("LLIKE", val)) {
            a[0] = " LIKE ? ";
            a[1] = "%" + val1;
            return a;
        } else if (StringUtils.equals("RLIKE", val)) {
            a[0] = " LIKE ? ";
            a[1] = val1 + "%";
            return a;
        } else if (StringUtils.equals("LIKE", val)) {
            a[0] = " LIKE ? ";
            a[1] = "%" + val1 + "%";
            return a;
        } else if (StringUtils.equals("GTE", val)) {
            a[0] = " >= ? ";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("GT", val)) {
            a[0] = " > ? ";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("LTE", val)) {
            a[0] = " <= ?";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("LT", val)) {
            a[0] = " < ? ";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("BETWEEN", val)) {
            a[0] = " BETWEEN ? AND ?";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("EQ", val)) {
            a[0] = " = ?";
            a[1] = val1;
            return a;
        } else if (StringUtils.equals("NEQ", val)) {
            a[0] = " != ?";
            a[1] = val1;
            return a;
        } else {
            return null;
        }
    }
}
