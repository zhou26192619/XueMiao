package cc.xuemiao.bean;

import java.io.Serializable;

public class HMRecommendBean implements Serializable {
	private static final long serialVersionUID = 6973744910916949012L;
	private HMOrganizationBean organization;
	private HMCampaignBean activity;
	private HMCourseBean course;

	public HMOrganizationBean getOrganization() {
		return organization;
	}

	public void setOrganization(HMOrganizationBean organization) {
		this.organization = organization;
	}

	public HMCampaignBean getActivity() {
		return activity;
	}

	public void setActivity(HMCampaignBean activity) {
		this.activity = activity;
	}

	public HMCourseBean getCourse() {
		return course;
	}

	public void setCourse(HMCourseBean course) {
		this.course = course;
	}

}
