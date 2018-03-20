package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOrderLog<M extends BaseOrderLog<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setOrderId(Integer orderId) {
		set("order_id", orderId);
	}

	public Integer getOrderId() {
		return get("order_id");
	}

	public void setOperationTime(java.util.Date operationTime) {
		set("operation_time", operationTime);
	}

	public java.util.Date getOperationTime() {
		return get("operation_time");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}

	public String getRemark() {
		return get("remark");
	}

	public void setLoginId(Integer loginId) {
		set("login_id", loginId);
	}

	public Integer getLoginId() {
		return get("login_id");
	}

	public void setOperater(String operater) {
		set("operater", operater);
	}

	public String getOperater() {
		return get("operater");
	}

	public void setAction(Integer action) {
		set("action", action);
	}

	public Integer getAction() {
		return get("action");
	}

}
