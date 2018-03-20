package kits;

/**
 * Created by BOGONj on 2016/8/30.
 */
public class DaoKit {
    public static String Llike(String val) {
        return "%" + val;
    }

    public static String Rlike(String val) {
        return val + "%";
    }

    public static String AllLike(String val) {
        return "%" + val + "%";
    }
}
