package kits;

import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by BOGONm on 16/8/9.
 */
public class Md5KitTest extends TestCase {
    public void testMD5() throws Exception {
        String password = "123123";
        System.out.printf(Md5Kit.MD5(password));
    }

    public void testMD5API() throws Exception {
        String password = "123123";
        String salt = "acfa";
        String md5 = Md5Kit.MD5(password) + salt;
        System.out.println(md5);
//        System.out.printf(Md5Kit.MD5(md5));
    }

    public void testRSU() throws Exception {
        System.out.println(RandomStringUtils.randomAlphanumeric(32));
        String strDate = "2015-8-3 12:22:33";
        System.out.printf(DateTime.parse(strDate, DateTimeFormat.forPattern(TimeKit.YYYYMMDDHHMMSS)).toString());
    }
}