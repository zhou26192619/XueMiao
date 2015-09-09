package cc.xuemiao.bean;

import java.io.Serializable;

public class HMCampaignSignUpBean implements Serializable {
	private static final long serialVersionUID = -5686943315696485570L;

	private String id;
	private HMCampaignBean activity;
	private String status;
	private String joinDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HMCampaignBean getActivity() {
		return activity;
	}

	public void setActivity(HMCampaignBean activity) {
		this.activity = activity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
}
