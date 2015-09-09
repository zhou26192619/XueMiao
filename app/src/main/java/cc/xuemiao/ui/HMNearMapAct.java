package cc.xuemiao.ui;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.util.GpsUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiOrganization;
import cc.xuemiao.bean.HMOrganizationBean;
import cc.xuemiao.utils.HMNavUtil;
import cc.xuemiao.utils.LocationUtil;

public class HMNearMapAct extends HMBaseAct {

    public static final String BUNDLE_DISTANCE = "distance";
    public static final int TYPE_ORG = 0;

    @Bind(R.id.near_map_mv_mapview)
    MapView mvMapView;

    private double distance;
    private AMap aMap;

    private LocationSource.OnLocationChangedListener onLocationChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViews(R.layout.act_near_map);
        mvMapView.onCreate(savedInstanceState);// 必须要写
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        distance = bundle.getDouble(BUNDLE_DISTANCE);
    }

    private void init() {
        hvHeadView.setTitle("地图");
        aMap = mvMapView.getMap();
        if (!GpsUtil.isGpsEnable(this)) {
            Toast.makeText(this, "亲，请打开GPS以便获取更精确的信息", Toast.LENGTH_LONG)
                    .show();
        }

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.default_face));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener changedListener) {
                onLocationChangedListener = changedListener;
            }

            @Override
            public void deactivate() {
                onLocationChangedListener = null;
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    private void setListener() {
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                HMOrganizationBean bean = (HMOrganizationBean) marker.getObject();
                Bundle bundle = new Bundle();
                bundle.putString(HMOrgDetailAct.BUNDLE_KEY, bean.getId());
                HMNavUtil.goToNewAct(HMNearMapAct.this, HMOrgDetailAct.class, bundle);
            }
        });

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
                .fromResource(R.mipmap.overlay));
        Marker marker = aMap.addMarker(markerOption);
        return marker;
    }

    @Override
    public void loadData() {
        super.loadData();
        showLoading();
        LocationUtil.getPositionInfoByGaode(HMNearMapAct.this, 2000, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {

                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 14));
                    if (onLocationChangedListener != null) {
                        onLocationChangedListener.onLocationChanged(aMapLocation);
                    }

                    RequestParams params = new RequestParams();
                    params.put("lat", aMapLocation.getLatitude());
                    params.put("lng", aMapLocation.getLongitude());
                    params.put("distance", distance);
                    params.put("pageSize", 100);
                    params.put("pageIndex", 1);
                    // if (type == TYPE_ORG) {
                    HMApiOrganization.getInstance().postListByDistanece(HMNearMapAct.this, params);
//                     } else if (type == TYPE_COURSE) {
//                     HMApiCourse
//                     .getCourseByDistanece(HMNearListAct.this, params);
//                     }
                    LocationUtil.stopGaodeLocation();
                }
            }

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        List<HMOrganizationBean> list = GsonUtil.fromJsonArr(jo
                        .getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(HMApi.KEY_LIST),
                new TypeToken<List<HMOrganizationBean>>() {
                });

        aMap.clear();
        for (int i = 0; i < list.size(); i++) {
            HMOrganizationBean info = list.get(i);
            LatLng latLng = new LatLng(Double.valueOf(info.getLat()), Double.valueOf(info.getLng()));
            Marker marker = addMarkersToMap(latLng, info.getName(), info.getBrief());
            marker.setObject(info);
            if (i == 0) {
                marker.showInfoWindow();
            }
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
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
        LocationUtil.stopGaodeLocation();
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
