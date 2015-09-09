package cc.xuemiao.bean;

import com.lib_common.bean.KVbean;

/**
 * Created by zhanghup on 2015/8/28.
 */
public class KeyValueBean
        extends KVbean {
    public String lat;
    public String lng;

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
}
