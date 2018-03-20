package base.models;

import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCarRealLocation<M extends BaseCarRealLocation<M>> extends BaseModel<M> implements IBean {

    public void setId(Long id) {
        set("id", id);
    }

    public Long getId() {
        return get("id");
    }

    public void setCarId(Long carId) {
        set("car_id", carId);
    }

    public Long getCarId() {
        return get("car_id");
    }

    public void setLongitude(Double longitude) {
        set("longitude", longitude);
    }

    public Double getLongitude() {
        return get("longitude");
    }

    public void setLatitude(Double latitude) {
        set("latitude", latitude);
    }

    public Double getLatitude() {
        return get("latitude");
    }

    public void setSpeed(Double speed) {
        set("speed", speed);
    }

    public Double getSpeed() {
        return get("speed");
    }

    public void setReciveTime(java.util.Date reciveTime) {
        set("recive_time", reciveTime);
    }

    public java.util.Date getReciveTime() {
        return get("recive_time");
    }

}
