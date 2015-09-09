package cc.xuemiao.bean;

import java.io.Serializable;

public class HMNikNameBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id; // 主键
	private String nikname; // 昵称
	private String logo;
	private String identity;
	
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNikname() {
		return nikname;
	}
	public void setNikname(String nikname) {
		this.nikname = nikname;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
