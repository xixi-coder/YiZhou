package utils;

/**
 * Created by Administrator on 2017/7/20.
 */
public class StringUtil {


    //把一个字符串中的大写转为小写，小写转换为大写：思路1
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else if(Character.isLowerCase(c)){
                    sb.append(Character.toUpperCase(c));
                }
            }
        }

        return sb.toString();
    }

    /**
     * 随机生成 j 位大写字母
     * @return
     */
    public static String randomStr(int j){
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String str ="";
        for(int i = 0;i<j ;i++){
            str +=  chars.charAt((int) (Math.random() * 26));
        }
        return exChange(str);

    }

    /**
     * yyyyMMddHHmmSS
     * 转成带格式的
     * @param str
     * @return
     */
    public  static  String stringToDate(String str ){
        StringBuilder sb = new StringBuilder(str);
        sb.insert(4, "-");
        sb.insert(7, "-");
        sb.insert(10, " ");
        sb.insert(13, ":");
        sb.insert(16, ":");
        return  sb.toString();
    }

}
