package utils;

/**
 * Created by Administrator on 2017/7/20.
 */
public class Random {


    /**
     * 随机生成6位数值
     * @return
     */
    public static int getRandNum() {
        int randNum = 1 + (int)(Math.random() * ((999999 - 1) + 1));
        return randNum;
    }

    /**
     * 随机生成i位数值 i 9  99  999
     * @return
     */
    public static Integer getRandNum2(int i) {
        int randNum = 1 + (int)(Math.random() * ((i - 1) + 1));
        return randNum;
    }


    /**
     * //产生1~i之间的任意一个整型数据  s位数

     * @return
     */
    public static String getRandNum3(int i,int s) {
        String str ="";
        for(int j=0;j<s;j++){
            Integer randNum =(int)(Math.random()*i+1);
           str += randNum.toString();

        }

        return str;
    }


}
