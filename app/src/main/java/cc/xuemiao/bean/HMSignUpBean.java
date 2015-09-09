package cc.xuemiao.bean;

import java.io.Serializable;

/**
 * 报名状态实体类
 * 
 * @author loar
 * 
 */
public class HMSignUpBean implements Serializable {
	private static final long serialVersionUID = 3826116610826838290L;

	private String id; // 报名主键
	private String signupTime; // 报名时间
	private String signupStatus; // 报名状态
	private HMParentBean parent;
	private HMCourseBean course;
	private HMChildBean child;
	private String remarks; // 备注信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSignupTime() {
		return signupTime;
	}

	public void setSignupTime(String signupTime) {
		this.signupTime = signupTime;
	}

	public String getSignupStatus() {
		return signupStatus;
	}

	public void setSignupStatus(String signupStatus) {
		this.signupStatus = signupStatus;
	}

	public HMParentBean getParent() {
		return parent;
	}

	public void setParent(HMParentBean parent) {
		this.parent = parent;
	}

	public HMCourseBean getCourse() {
		return course;
	}

	public void setCourse(HMCourseBean course) {
		this.course = course;
	}

	public HMChildBean getChild() {
		return child;
	}

	public void setChild(HMChildBean child) {
		this.child = child;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
