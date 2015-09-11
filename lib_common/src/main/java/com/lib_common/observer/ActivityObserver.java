package com.lib_common.observer;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.google.gson.JsonObject;
import org.apache.http.Header;

/**
 * 请求统一的方法，和请求结果的处理
 * Created by loar on 2015/7/10.
 */
public interface ActivityObserver {

    /**
     *
     * @return
     */
    Context getContext();

    /**
     * 对传过来的数据进行处理
     *
     * @param bundle
     */
    void dealIntent(Bundle bundle);

    /**
     * 数据加载后更新界面
     */
    void updateView();

    /**
     * 数据加载,有数据请求的基本都要放在这里,以跟新界面
     */
    void loadData();


    /**
     * 网络请求成功 后重写
     *
     * @param statusCode
     * @param headers
     * @param jo
     */
    void setRequestSuc(String url, int statusCode,Header[] headers,
                       JsonObject jo);

    /**
     * 网络请求不成功 后重写
     *
     * @param statusCode
     * @param headers
     * @param jo
     */
    void setRequestNotSuc(String url, int statusCode,
                          Header[] headers, JsonObject jo);

    /**
     * 网络异常 后重写
     *
     * @param statusCode
     * @param headers
     * @param str
     * @param throwable
     */
    void setRequestErr(String url, int statusCode, Header[] headers,
                       String str, Throwable throwable);

    /**
     * 请求结束
     */
    void setRequestFinish();

    /**
     * 请求取消
     */
    void setRequestCancel();

    /**
     * @param bytesWritten
     * @param totalSize
     */
    void setRequestProgress(int bytesWritten, int totalSize);

    /**
     * @param retryNo
     */
    void setRequestRetry(int retryNo);
}
