package cc.xuemiao.bean;

import java.io.Serializable;

public class HMVersionBean implements Serializable {
	private static final long serialVersionUID = -5686943315696485570L;

	private int id;
	private String version;
	private String updateUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

}
