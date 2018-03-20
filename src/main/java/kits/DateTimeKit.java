package kits;

import org.joda.time.DateTime;

/**
 * Created by BOGONj on 2016/9/1.
 */

public class DateTimeKit {

    /**
     * 合并日期和时间
     *
     * @param date
     * @param time
     * @return
     */
    public static DateTime mrgeDateWithTime(DateTime date, DateTime time) {
        DateTime newDateTime = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(), time.getMillisOfDay(), time.getSecondOfDay());
        return newDateTime;
    }
}
