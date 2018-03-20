package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSchedule<M extends BaseSchedule<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setDriverId(Integer driverId) {
		set("driverId", driverId);
	}

	public Integer getDriverId() {
		return get("driverId");
	}

	public void setMemberId(Integer memberId) {
		set("memberId", memberId);
	}

	public Integer getMemberId() {
		return get("memberId");
	}

	public void setOrderId(Integer orderId) {
		set("orderId", orderId);
	}

	public Integer getOrderId() {
		return get("orderId");
	}

	public void setCarId(Integer carId) {
		set("carId", carId);
	}

	public Integer getCarId() {
		return get("carId");
	}

	public void setInsurance(Integer Insurance) {
		set("Insurance", Insurance);
	}

	public Integer getInsurance() {
		return get("Insurance");
	}

	public void setStatus(Integer status) {
		set("status", status);
	}

	public Integer getStatus() {
		return get("status");
	}

	public void setFlag(Integer flag) {
		set("flag", flag);
	}

	public Integer getFlag() {
		return get("flag");
	}

	public void setCompany(Integer company) {
		set("company", company);
	}

	public Integer getCompany() {
		return get("company");
	}

}
