package cc.xuemiao.bean;

import java.io.Serializable;

public class HMCourseDetailBean implements Serializable {
	private static final long	serialVersionUID	= -2472952052097478691L;
	
	private HMCourseBean		course;
	private int					isSignup;
	private String				avg_care;
	private String				avg_improve;
	private String				avg_like;
	
	public HMCourseBean getCourse() {
		return course;
	}
	
	public void setCourse(HMCourseBean course) {
		this.course = course;
	}
	
	public int getIsSignup() {
		return isSignup;
	}
	
	public void setIsSignup(int isSignup) {
		this.isSignup = isSignup;
	}
	
	public String getAvg_care() {
		return avg_care;
	}
	
	public void setAvg_care(String avg_care) {
		this.avg_care = avg_care;
	}
	
	public String getAvg_improve() {
		return avg_improve;
	}
	
	public void setAvg_improve(String avg_improve) {
		this.avg_improve = avg_improve;
	}
	
	public String getAvg_like() {
		return avg_like;
	}
	
	public void setAvg_like(String avg_like) {
		this.avg_like = avg_like;
	}
	
}
