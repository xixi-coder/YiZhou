package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDriverRealLocation<M extends BaseDriverRealLocation<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setDriver(Integer driver) {
		set("driver", driver);
	}

	public Integer getDriver() {
		return get("driver");
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

	public void setOrientation(Double orientation) {
		set("orientation", orientation);
	}

	public Double getOrientation() {
		return get("orientation");
	}

	public void setType(Integer type) {
		set("type", type);
	}

	public Integer getType() {
		return get("type");
	}

	public void setAccuracy(Double accuracy) {
		set("accuracy", accuracy);
	}

	public Double getAccuracy() {
		return get("accuracy");
	}

}
