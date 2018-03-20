package dto;

import models.car.Car;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by BOGONj on 2016/9/8.
 */
public class JPushToMemberDto implements Serializable {
    private String phone;
    private String nickName;
    private String latitude;
    private String longitude;
    private int driverId;
    private Car car;
    private String headPortrait;
    private int gender;
    private double praise; //好评等级
    private Date time;
    
    public int getGender() {
        return gender;
    }
    
    public void setGender(int gender) {
        this.gender = gender;
    }
    
    public String getHeadPortrait() {
        return headPortrait;
    }
    
    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public int getDriverId() {
        return driverId;
    }
    
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
    
    
    public double getPraise() {
        return praise;
    }
    
    public void setPraise(double praise) {
        this.praise = praise;
    }
    
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
}
