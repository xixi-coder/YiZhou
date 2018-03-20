package kits;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/20.
 */
public class VerificationKit {
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        try {
            Pattern p = null;
            Matcher m = null;
            boolean b = false;
            p = Pattern.compile("^1[3-9]\\d{9}$"); // 验证手机号
            m = p.matcher(str);
            b = m.matches();
            return b;
        } catch (Exception e) {
            return false;
        }
    }
}
