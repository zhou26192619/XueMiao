package cc.xuemiao.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class LocationUtil {
    private static LocationManagerProxy locationProxy;
    private static AMapLocationListener aMapLocationListener;
//    private static LocationClient locationClient;


//    public static LocationClient getLocationClientByBaidu(Context context) {
//        locationClient = new LocationClient(context);
//        return locationClient;
//    }

//    /**
//     * 获取经纬度
//     *
//     * @param city
//     * @param address
//     * @param callback
//     */
//    public static void getLonLatByBaidu(String city, String address,
//                                 final OnGetGeoCodeCallback callback) {
//        try {
//            // 创建地理编码检索实例
//            final GeoCoder geoCoder = GeoCoder.newInstance();
//            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
//
//                // 地理编码查询结果回调函数
//                @Override
//                public void onGetGeoCodeResult(GeoCodeResult result) {
//                    if (callback != null) {
//                        callback.onGetGeoCodeResult(result);
//                    }
//                    geoCoder.destroy();
//                }
//
//                @Override
//                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
//                    if (callback != null) {
//                        callback.onGetReverseGeoCodeResult(arg0);
//                    }
//                }
//            };
//            // 设置地理编码检索监听者
//            geoCoder.setOnGetGeoCodeResultListener(listener);
//            GeoCodeOption gco = new GeoCodeOption();
//            gco.city(city);
//            gco.address(address);
//            geoCoder.geocode(gco);
//        } catch (Exception e) {
//            ToastUtil.printErr(e);
//        }
//    }
//
//    public interface OnGetGeoCodeCallback {
//        void onGetGeoCodeResult(GeoCodeResult result);
//
//        void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0);
//    }
//
//    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>定位>>>>>>>>>>>>>>>>>>>>
//
//    /**
//     * 百度获取所在省份和城市
//     *
//     * @param context
//     * @return
//     */
//    public static void getPositionInfoByBaidu(Context context,
//                                              final LocationCallBack callback) {
//        locationClient = getLocationClient(context);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);
//        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
//        // option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
//        option.setScanSpan(0);// 设置发起定位请求的间隔时间为20000ms
//        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
//        // option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
//        locationClient.setLocOption(option);
//        locationClient.registerLocationListener(new BDLocationListener() {
//
//            @Override
//            public void onReceiveLocation(BDLocation location) {
//                // 61 ： GPS定位结果
//                // 62 ： 扫描整合定位依据失败。此时定位结果无效。
//                // 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
//                // 65 ： 定位缓存的结果。
//                // 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
//                // 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
//                // 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
//                // 161： 表示网络定位结果
//                // 162~167： 服务端定位失败。
//                int code = location.getLocType();
//                ToastUtil.log("getPositionInfoByBaidu", code);
//                if (code == 61 || code == 65 || code == 66 || code == 161) {
//                    callback.onSuccess(location);
//                } else {
//                    callback.onFailed();
//                }
//                callback.onFinish();
//                stopLocation();
//            }
//        });
//        locationClient.start();
//    }
//
//    public interface LocationCallBack {
//        void onSuccess(BDLocation location);
//
//        void onFailed();
//
//        void onFinish();
//    }
//
//    /**
//     * 停止定位
//     */
//    public static void stopBaiduLocation() {
//        if (locationClient != null) {
//            locationClient.stop();
//        }
//    }


    public static void getPositionInfoByGaode(Context context, long interval, AMapLocationListener locationListener) {
        locationProxy = LocationManagerProxy.getInstance(context);
        aMapLocationListener = locationListener;
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        locationProxy.requestLocationData(LocationProviderProxy.AMapNetwork, interval, 15, locationListener);
        locationProxy.setGpsEnable(false);
    }

    public static void stopGaodeLocation() {
        if (locationProxy != null) {
            if (aMapLocationListener != null) {
                locationProxy.removeUpdates(aMapLocationListener);
            }
            locationProxy.destroy();
        }
        locationProxy = null;
        aMapLocationListener=null;
    }

}
