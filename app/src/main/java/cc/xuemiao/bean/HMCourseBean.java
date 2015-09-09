package cc.xuemiao.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HMCourseBean implements Serializable {
    private static final long serialVersionUID = -2472952052097478691L;

    private String id;
    private String name;
    private String tuition;
    private String publishTime;
    private String startTime;
    private String stopTime;
    private String teachDate;
    private List<Map<String, String>> teachTime;
    private String[] weeks;
    private String courseNum;
    private String completeNum;
    private String introduction;
    private String province;
    private String city;
    private String area;
    private String address;
    private String status;
    private String teacherName;
    private String teacherId;
    private String organizationName;
    private String organizationId;
    private String identity;
    private String imageId;
    private String isCommentHide;
    private String avgLike;
    private String avgCare;
    private String avgImprove;
    private String commentCount;
    private String logo;
    private List<HMImage> images;
    private String studentNum;
    private String lat;
    private String lng;
    private double distance;
    private String summary;
    private String courseContent;

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String[] getWeeks() {
        return weeks;
    }

    public void setWeeks(String[] weeks) {
        this.weeks = weeks;
    }

    public String getTeachDate() {
        return teachDate;
    }

    public void setTeachDate(String teachDate) {
        this.teachDate = teachDate;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTuition() {
        return tuition;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public List<Map<String, String>> getTeachTime() {
        return teachTime;
    }

    public void setTeachTime(List<Map<String, String>> teachTime) {
        this.teachTime = teachTime;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(String completeNum) {
        this.completeNum = completeNum;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIsCommentHide() {
        return isCommentHide;
    }

    public void setIsCommentHide(String isCommentHide) {
        this.isCommentHide = isCommentHide;
    }

    public String getAvgLike() {
        return avgLike;
    }

    public void setAvgLike(String avgLike) {
        this.avgLike = avgLike;
    }

    public String getAvgCare() {
        return avgCare;
    }

    public void setAvgCare(String avgCare) {
        this.avgCare = avgCare;
    }

    public String getAvgImprove() {
        return avgImprove;
    }

    public void setAvgImprove(String avgImprove) {
        this.avgImprove = avgImprove;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public List<HMImage> getImages() {
        return images;
    }

    public void setImages(List<HMImage> images) {
        this.images = images;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
