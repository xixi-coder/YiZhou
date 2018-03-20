package base.models;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseServiceAgreement<M extends BaseServiceAgreement<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}

	public Integer getId() {
		return get("id");
	}

	public void setCompany(Integer company) {
		set("company", company);
	}

	public Integer getCompany() {
		return get("company");
	}

	public void setContent(String content) {
		set("content", content);
	}

	public String getContent() {
		return get("content");
	}

	public void setLastUpdate(java.util.Date lastUpdate) {
		set("last_update", lastUpdate);
	}

	public java.util.Date getLastUpdate() {
		return get("last_update");
	}

	public void setOperater(Integer operater) {
		set("operater", operater);
	}

	public Integer getOperater() {
		return get("operater");
	}

	public void setType(Integer type) {
		set("type", type);
	}

	public Integer getType() {
		return get("type");
	}

}
