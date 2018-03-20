package kits;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * Created by admin on 2016/9/27.
 */
public class TimeKit {
    public static String[] splictHours(Date date) {
        DateTime dateTime = new DateTime(date);
        String sqlTime = dateTime.toString(DateTimeFormat.forPattern("HH:mm"));
        return sqlTime.split(":");
    }

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String HHMMSS = "HH:mm:ss";
    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

    public static boolean t(Long setOutTime) {
        long t = setOutTime - System.currentTimeMillis();
        if (t > 600) {
            return true;
        }
        return false;
    }
}
