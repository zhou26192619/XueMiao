package cc.xuemiao.bean;

import java.io.Serializable;
import java.util.List;

public class HMStudentBean implements Serializable {
	private static final long serialVersionUID = -2472952052097478691L;

	private String id;
	private List<HMCourseBean> courses;
	private HMChildBean child;
	private String signupId;

	public String getSignupId() {
		return signupId;
	}

	public void setSignupId(String signupId) {
		this.signupId = signupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<HMCourseBean> getCourses() {
		return courses;
	}

	public void setCourses(List<HMCourseBean> courses) {
		this.courses = courses;
	}

	public HMChildBean getChild() {
		return child;
	}

	public void setChild(HMChildBean child) {
		this.child = child;
	}

}
