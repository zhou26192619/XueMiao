package cc.xuemiao.bean;

import java.io.Serializable;

public class HMImage implements Serializable {

	private static final long serialVersionUID = -2493520442405413331L;

	private String id;
	private String url;
	private String host;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
