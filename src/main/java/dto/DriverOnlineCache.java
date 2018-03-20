package dto;

import java.io.Serializable;

/**
 * 司机上线缓存类
 *
 * @author Administrator
 */
public class DriverOnlineCache implements Serializable {
    private static final long serialVersionUID = 5574682714624905015L;
    /**
     * 缓存登陆id（MemberLogId）
     */
    private Integer uId;
    /**
     * 服务小类型
     */
    private Integer subType;
    
    /**
     * 当前服务状态
     */
    private Integer serviceStatus;
    
    /**
     * 缓存开始的时间戳
     */
    private Long onlineTime;
    
    private Double lng;
    
    private Double lat;
    
    public DriverOnlineCache() {
    }
    
    public DriverOnlineCache(Integer uId, Integer subType, Long onlineTime) {
        this.uId = uId;
        this.subType = subType;
        this.onlineTime = onlineTime;
    }
    
    public DriverOnlineCache(Integer uId, Integer subType, Integer serviceStatus, Long onlineTime) {
        this.uId = uId;
        this.subType = subType;
        this.serviceStatus = serviceStatus;
        this.onlineTime = onlineTime;
    }
    
    public DriverOnlineCache(Integer uId, Integer subType, Integer serviceStatus, Long onlineTime, Double lng, Double lat) {
        this.uId = uId;
        this.subType = subType;
        this.serviceStatus = serviceStatus;
        this.onlineTime = onlineTime;
        this.lng = lng;
        this.lat = lat;
    }
    
    public Integer getuId() {
        return uId;
    }
    
    public void setuId(Integer uId) {
        this.uId = uId;
    }
    
    public Long getOnlineTime() {
        return onlineTime;
    }
    
    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }
    
    public Integer getSubType() {
        return subType;
    }
    
    public void setSubType(Integer subType) {
        this.subType = subType;
    }
    
    public Integer getServiceStatus() {
        return serviceStatus;
    }
    
    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    
    public Double getLng() {
        return lng;
    }
    
    public void setLng(Double lng) {
        this.lng = lng;
    }
    
    public Double getLat() {
        return lat;
    }
    
    public void setLat(Double lat) {
        this.lat = lat;
    }
}
