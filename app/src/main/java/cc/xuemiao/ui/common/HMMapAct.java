package cc.xuemiao.ui.common;

import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.bean.HMLBSBean;
import cc.xuemiao.ui.HMBaseAct;

public class HMMapAct extends HMBaseAct {
    public static final String BUNDLE_KEY = "HMLBSBean";
    public static final String BUNDLE_KEY_TITLE = "title";

    @Bind(R.id.map_mv_mapview)
    MapView mvMapView;
    private String title;
    private AMap aMap;
    private HMLBSBean lbsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViews(R.layout.act_map);
        mvMapView.onCreate(savedInstanceState);
        aMap = mvMapView.getMap();
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        title = bundle.getString(BUNDLE_KEY_TITLE);
        lbsBean = (HMLBSBean) bundle.getSerializable(BUNDLE_KEY);
    }

    private void init() {
        hvHeadView.setTitle("地图");
        aMap = mvMapView.getMap();
        LatLng latLng = new LatLng(lbsBean.getLat(), lbsBean.getLng());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        addMarkersToMap(latLng, lbsBean.getTitle(), lbsBean.getContent());
    }

    private void onMarkClick(Marker marker) {
    }

    /**
     * 在地图上添加marker
     */
    private Marker addMarkersToMap(LatLng latLng, String title, String snippet) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.title(title).snippet(snippet);
        markerOption.draggable(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.address));
        Marker marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
        return marker;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mvMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvMapView.onSaveInstanceState(outState);
    }
}
