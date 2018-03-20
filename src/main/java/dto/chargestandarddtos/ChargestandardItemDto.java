package dto.chargestandarddtos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BOGONj on 2016/9/19.
 */
public class ChargestandardItemDto implements Serializable {
    private String baseTime;//起步价格
    private String minAmount;//最低消费

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    private String startTime;//开始时间
    private String endTime;//结束时间


    private String chaoguogongli;//超过多少公里数
    private String meigongli;//每多少公里
    private String buzugongli;//不足多少公里
    private String jiashou;//加收多少元

    private String qibuhoufenzhonglei;//起步后多少分钟内
    private String meiduoshaofenzhong;//每多少分钟
    private String buzufenzhong;//不足多少分钟
    private String jiashoufeiyong;//加收多少元

    private String mianfeidenghou;//免费等侯多少分钟
    private String lijichaoshijiashoufeiyong;//超时立即加收多少元
    private String chaoshimeiduoshaofenzhong;//超时每多少分钟
    private String chaoshibuzufenzhong;//等候不足多少分钟
    private String chaoshijiashoufeiyong;//加收多少元

    private String onedistance1;
    private String onediscount1;
    private String onedistance2;
    private String onediscount2;

    public String getOnedistance1() {
        return onedistance1;
    }

    public void setOnedistance1(String onedistance1) {
        this.onedistance1 = onedistance1;
    }

    public String getOnediscount1() {
        return onediscount1;
    }

    public void setOnediscount1(String onediscount1) {
        this.onediscount1 = onediscount1;
    }

    public String getOnedistance2() {
        return onedistance2;
    }

    public void setOnedistance2(String onedistance2) {
        this.onedistance2 = onedistance2;
    }

    public String getOnediscount2() {
        return onediscount2;
    }

    public void setOnediscount2(String onediscount2) {
        this.onediscount2 = onediscount2;
    }

    public String getOnedistance3() {
        return onedistance3;
    }

    public void setOnedistance3(String onedistance3) {
        this.onedistance3 = onedistance3;
    }

    public String getOnediscount3() {
        return onediscount3;
    }

    public void setOnediscount3(String onediscount3) {
        this.onediscount3 = onediscount3;
    }

    public String getOnedistance4() {
        return onedistance4;
    }

    public void setOnedistance4(String onedistance4) {
        this.onedistance4 = onedistance4;
    }

    public String getOnediscount4() {
        return onediscount4;
    }

    public void setOnediscount4(String onediscount4) {
        this.onediscount4 = onediscount4;
    }

    public String getOnedistance5() {
        return onedistance5;
    }

    public void setOnedistance5(String onedistance5) {
        this.onedistance5 = onedistance5;
    }

    public String getOnediscount5() {
        return onediscount5;
    }

    public void setOnediscount5(String onediscount5) {
        this.onediscount5 = onediscount5;
    }

    public String getTwodistance1() {
        return twodistance1;
    }

    public void setTwodistance1(String twodistance1) {
        this.twodistance1 = twodistance1;
    }

    public String getTwodiscount1() {
        return twodiscount1;
    }

    public void setTwodiscount1(String twodiscount1) {
        this.twodiscount1 = twodiscount1;
    }

    public String getTwodistance2() {
        return twodistance2;
    }

    public void setTwodistance2(String twodistance2) {
        this.twodistance2 = twodistance2;
    }

    public String getTwodiscount2() {
        return twodiscount2;
    }

    public void setTwodiscount2(String twodiscount2) {
        this.twodiscount2 = twodiscount2;
    }

    public String getTwodistance3() {
        return twodistance3;
    }

    public void setTwodistance3(String twodistance3) {
        this.twodistance3 = twodistance3;
    }

    public String getTwodiscount3() {
        return twodiscount3;
    }

    public void setTwodiscount3(String twodiscount3) {
        this.twodiscount3 = twodiscount3;
    }

    public String getTwodistance4() {
        return twodistance4;
    }

    public void setTwodistance4(String twodistance4) {
        this.twodistance4 = twodistance4;
    }

    public String getTwodiscount4() {
        return twodiscount4;
    }

    public void setTwodiscount4(String twodiscount4) {
        this.twodiscount4 = twodiscount4;
    }

    public String getTwodistance5() {
        return twodistance5;
    }

    public void setTwodistance5(String twodistance5) {
        this.twodistance5 = twodistance5;
    }

    public String getTwodiscount5() {
        return twodiscount5;
    }

    public void setTwodiscount5(String twodiscount5) {
        this.twodiscount5 = twodiscount5;
    }

    public String getThreedistance1() {
        return threedistance1;
    }

    public void setThreedistance1(String threedistance1) {
        this.threedistance1 = threedistance1;
    }

    public String getThreediscount1() {
        return threediscount1;
    }

    public void setThreediscount1(String threediscount1) {
        this.threediscount1 = threediscount1;
    }

    public String getThreedistance2() {
        return threedistance2;
    }

    public void setThreedistance2(String threedistance2) {
        this.threedistance2 = threedistance2;
    }

    public String getThreediscount2() {
        return threediscount2;
    }

    public void setThreediscount2(String threediscount2) {
        this.threediscount2 = threediscount2;
    }

    public String getThreedistance3() {
        return threedistance3;
    }

    public void setThreedistance3(String threedistance3) {
        this.threedistance3 = threedistance3;
    }

    public String getThreediscount3() {
        return threediscount3;
    }

    public void setThreediscount3(String threediscount3) {
        this.threediscount3 = threediscount3;
    }

    public String getThreedistance4() {
        return threedistance4;
    }

    public void setThreedistance4(String threedistance4) {
        this.threedistance4 = threedistance4;
    }

    public String getThreediscount4() {
        return threediscount4;
    }

    public void setThreediscount4(String threediscount4) {
        this.threediscount4 = threediscount4;
    }

    public String getThreedistance5() {
        return threedistance5;
    }

    public void setThreedistance5(String threedistance5) {
        this.threedistance5 = threedistance5;
    }

    public String getThreediscount5() {
        return threediscount5;
    }

    public void setThreediscount5(String threediscount5) {
        this.threediscount5 = threediscount5;
    }

    private String onedistance3;
    private String onediscount3;
    private String onedistance4;
    private String onediscount4;
    private String onedistance5;
    private String onediscount5;

    private String twodistance1;
    private String twodiscount1;
    private String twodistance2;
    private String twodiscount2;
    private String twodistance3;
    private String twodiscount3;
    private String twodistance4;
    private String twodiscount4;
    private String twodistance5;
    private String twodiscount5;

    private String threedistance1;
    private String threediscount1;
    private String threedistance2;
    private String threediscount2;
    private String threedistance3;
    private String threediscount3;
    private String threedistance4;
    private String threediscount4;
    private String threedistance5;
    private String threediscount5;


    public String getLijichaoshijiashoufeiyong() {
        return lijichaoshijiashoufeiyong;
    }

    public void setLijichaoshijiashoufeiyong(String lijichaoshijiashoufeiyong) {
        this.lijichaoshijiashoufeiyong = lijichaoshijiashoufeiyong;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getBuzufenzhong() {
        return buzufenzhong;
    }

    public void setBuzufenzhong(String buzufenzhong) {
        this.buzufenzhong = buzufenzhong;
    }

    public String getBuzugongli() {
        return buzugongli;
    }

    public void setBuzugongli(String buzugongli) {
        this.buzugongli = buzugongli;
    }

    public String getChaoguogongli() {
        return chaoguogongli;
    }

    public void setChaoguogongli(String chaoguogongli) {
        this.chaoguogongli = chaoguogongli;
    }

    public String getChaoshibuzufenzhong() {
        return chaoshibuzufenzhong;
    }

    public void setChaoshibuzufenzhong(String chaoshibuzufenzhong) {
        this.chaoshibuzufenzhong = chaoshibuzufenzhong;
    }

    public String getChaoshijiashoufeiyong() {
        return chaoshijiashoufeiyong;
    }

    public void setChaoshijiashoufeiyong(String chaoshijiashoufeiyong) {
        this.chaoshijiashoufeiyong = chaoshijiashoufeiyong;
    }

    public String getChaoshimeiduoshaofenzhong() {
        return chaoshimeiduoshaofenzhong;
    }

    public void setChaoshimeiduoshaofenzhong(String chaoshimeiduoshaofenzhong) {
        this.chaoshimeiduoshaofenzhong = chaoshimeiduoshaofenzhong;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getJiashou() {
        return jiashou;
    }

    public void setJiashou(String jiashou) {
        this.jiashou = jiashou;
    }

    public String getJiashoufeiyong() {
        return jiashoufeiyong;
    }

    public void setJiashoufeiyong(String jiashoufeiyong) {
        this.jiashoufeiyong = jiashoufeiyong;
    }

    public String getMeiduoshaofenzhong() {
        return meiduoshaofenzhong;
    }

    public void setMeiduoshaofenzhong(String meiduoshaofenzhong) {
        this.meiduoshaofenzhong = meiduoshaofenzhong;
    }

    public String getMeigongli() {
        return meigongli;
    }

    public void setMeigongli(String meigongli) {
        this.meigongli = meigongli;
    }

    public String getMianfeidenghou() {
        return mianfeidenghou;
    }

    public void setMianfeidenghou(String mianfeidenghou) {
        this.mianfeidenghou = mianfeidenghou;
    }

    public String getQibuhoufenzhonglei() {
        return qibuhoufenzhonglei;
    }

    public void setQibuhoufenzhonglei(String qibuhoufenzhonglei) {
        this.qibuhoufenzhonglei = qibuhoufenzhonglei;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<MillItemDto> getItem() {
        return item;
    }

    public void setItem(List<MillItemDto> item) {
        this.item = item;
    }

    private List<MillItemDto> item;
}
