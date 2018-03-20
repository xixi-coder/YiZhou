package dto;

import java.io.Serializable;

/**
 * 消息模板参数类
 * Created by Administrator on 2017/10/7.
 */
public class TemplateParameter implements Serializable {
    
    private static final long serialVersionUID = 437244923917851932L;
    
    private String orderNo;
    private String title;                //标题
    private String titleText;            //消息内容
    private String transText;            //
    private String type;                 // 推送类型
    private String[] registrationId;     // 极光id
    private String[] cId;                // 个推id
    private String deviceIds;            //阿里的deviceId    多个用逗号分隔
    private Object user;
    
    private int transmissionType;        //穿透消息设置为，1 强制启动应用
    private boolean isRing;              //设置  响铃、
    private boolean isVibrate;           //设置  震动、
    private boolean isClearable;         //设置  可清除、
    
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitleText() {
        return titleText;
    }
    
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
    
    public String getTransText() {
        return transText;
    }
    
    public void setTransText(String transText) {
        this.transText = transText;
    }
    
    public int getTransmissionType() {
        return transmissionType;
    }
    
    public void setTransmissionType(int transmissionType) {
        this.transmissionType = transmissionType;
    }
    
    public boolean isRing() {
        return isRing;
    }
    
    public void setRing(boolean ring) {
        isRing = ring;
    }
    
    public boolean isVibrate() {
        return isVibrate;
    }
    
    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }
    
    public boolean isClearable() {
        return isClearable;
    }
    
    public void setClearable(boolean clearable) {
        isClearable = clearable;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String[] getRegistrationId() {
        return registrationId;
    }
    
    public void setRegistrationId(String[] registrationId) {
        this.registrationId = registrationId;
    }
    
    public String[] getcId() {
        return cId;
    }
    
    public void setcId(String[] cId) {
        this.cId = cId;
    }
    
    public String getDeviceIds() {
        return deviceIds;
    }
    
    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }
    
    public Object getUser() {
        return user;
    }
    
    public void setUser(Object user) {
        this.user = user;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
