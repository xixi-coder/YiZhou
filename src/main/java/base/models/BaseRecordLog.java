package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRecordLog<M extends BaseRecordLog<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setLoginId(Integer loginId) {
		set("login_id", loginId);
	}

	public Integer getLoginId() {
		return get("login_id");
	}

	public void setRecordId(Integer recordId) {
		set("record_id", recordId);
	}

	public Integer getRecordId() {
		return get("record_id");
	}

	public void setStatus(Integer status) {
		set("status", status);
	}

	public Integer getStatus() {
		return get("status");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setType(Integer type) {
		set("type", type);
	}

	public Integer getType() {
		return get("type");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

	public void setPeople(Integer people) {
		set("people", people);
	}

	public Integer getPeople() {
		return get("people");
	}

	public void setPinCheAmount(java.math.BigDecimal pinCheAmount) {
		set("pinCheAmount", pinCheAmount);
	}

	public java.math.BigDecimal getPinCheAmount() {
		return get("pinCheAmount");
	}

	public void setJiHuoAmount(java.math.BigDecimal jiHuoAmount) {
		set("jiHuoAmount", jiHuoAmount);
	}

	public java.math.BigDecimal getJiHuoAmount() {
		return get("jiHuoAmount");
	}

}
