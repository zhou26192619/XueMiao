package cc.xuemiao.bean;

import java.io.Serializable;

public class HMLBSBean implements Serializable {
    private static final long serialVersionUID = -2472952052097478691L;

    private String ak = "7wWsza2zzPbIkaLmWLXEooZ0";
    private int geoTableId = 98566;
    private double radius = 1000;
    private String tag = "";
    private String sort = "distance:1";
    private int page_index = 0;
    private int page_size = 20;
    private double lat;
    private double lng;
    private String title;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public int getGeoTableId() {
        return geoTableId;
    }

    public void setGeoTableId(int geoTableId) {
        this.geoTableId = geoTableId;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setPageIndex(int page_index) {
        this.page_index = page_index;
    }

    public void nextPage() {
        this.page_index++;
    }

    public int getPageIndex() {
        return page_index;
    }

    public void setPageSize(int page_size) {
        this.page_size = page_size;
    }

    public int getPageSize() {
        return page_size;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }
}
