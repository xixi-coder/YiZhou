package utils;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
public class DateUtil extends com.xiaoleilu.hutool.date.DateUtil {


    /**
     * 日期转字符串
     *
     * @param paramDate
     * @param dateFormat
     * @return
     */
    public static String dateToStr(Date paramDate, String dateFormat) {
        if (paramDate == null || dateFormat == null || dateFormat == "") {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(paramDate);
    }

    /**
     * 获取当前月的第一天: 采用Calendar类实现
     *
     * @return
     */
    public static Date getFirstDayInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDate(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }


    /*
     * 将时间戳转换为时间
     */
    public static Long dateToStamp(String s) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(s);
            return new Long(date.getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s, String fmt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 判断字符串是不是日期格式
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    /**
     * 将长时间格式时间转换为Long yyyyMMddHHmmss
     *
     * @param dateDate
     * @return
     */
    public static Long dateToLong(java.util.Date dateDate) {
        String dateString = "";
        if (dateDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            dateString = formatter.format(dateDate);
        }
        return Long.parseLong(dateString);
    }

    /**
     * 将长时间格式时间转换为Long YYYYMMDD
     *
     * @param dateDate
     * @return
     */
    public static Long dateToLongYMD(java.util.Date dateDate) {
        String dateString = "";
        if (dateDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            dateString = formatter.format(dateDate);
        } else {
            dateString = "19900101";
        }
        return Long.parseLong(dateString);
    }


    /**
     * 获取当前时间格式为：YYYYMMMMDDHHSS
     */
    public static Long UpdateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return Long.parseLong(formatter.format(DateTime.now().toDate()));
    }

    /**
     * 日期格式转换
     *
     * @param dateDate
     * @param fmt      格式
     * @return
     */
    public static String dateFmt(java.util.Date dateDate, String fmt) {
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    public static Date stringToDate(String fmt) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(fmt);
            return date;
        } catch (ParseException e) {
            System.out.println(e.getMessage() + ":ParseException----------------->>>>>>");
        }


        return null;

    }
}
