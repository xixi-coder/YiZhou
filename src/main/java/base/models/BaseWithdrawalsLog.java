package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWithdrawalsLog<M extends BaseWithdrawalsLog<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setAmount(java.math.BigDecimal amount) {
		set("amount", amount);
	}

	public java.math.BigDecimal getAmount() {
		return get("amount");
	}

	public void setRealAmount(java.math.BigDecimal realAmount) {
		set("real_amount", realAmount);
	}

	public java.math.BigDecimal getRealAmount() {
		return get("real_amount");
	}

	public void setLoginId(Integer loginId) {
		set("login_id", loginId);
	}

	public Integer getLoginId() {
		return get("login_id");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}

	public String getRemark() {
		return get("remark");
	}

	public void setFormType(Integer formType) {
		set("form_type", formType);
	}

	public Integer getFormType() {
		return get("form_type");
	}

	public void setAuditer(Integer auditer) {
		set("auditer", auditer);
	}

	public Integer getAuditer() {
		return get("auditer");
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

	public void setAuditTime(java.util.Date auditTime) {
		set("audit_time", auditTime);
	}

	public java.util.Date getAuditTime() {
		return get("audit_time");
	}

	public void setBankNo(String bankNo) {
		set("bank_no", bankNo);
	}

	public String getBankNo() {
		return get("bank_no");
	}

	public void setBankName(String bankName) {
		set("bank_name", bankName);
	}

	public String getBankName() {
		return get("bank_name");
	}

	public void setCompany(Integer company) {
		set("company", company);
	}

	public Integer getCompany() {
		return get("company");
	}

	public void setBankType(String bankType) {
		set("bank_type", bankType);
	}

	public String getBankType() {
		return get("bank_type");
	}

	public void setKhBank(String khBank) {
		set("kh_bank", khBank);
	}

	public String getKhBank() {
		return get("kh_bank");
	}

}
