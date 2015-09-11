package com.lib_common.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.view.View;

import com.google.gson.JsonObject;
import com.lib_common.BaseApp;
import com.lib_common.dialog.LoadingDialog;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.ToastUtil;

import org.apache.http.Header;

import butterknife.ButterKnife;

public class BaseActivity extends Activity implements ActivityObserver {
    private LoadingDialog loadingDialog;

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(this);
        }
        loadingDialog.showLoading();
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismissLoading();
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ((BaseApp) getApplicationContext()).addActivity(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dealIntent(bundle);
        }
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                try {
                    if (!isRequesting()) {
                        loadData();
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }
        });
    }

    public void setContentViews(int layoutResID) {
        setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 对传过来的数据进行处理
     *
     * @param bundle
     */
    public void dealIntent(Bundle bundle) {
    }

    /**
     * 数据加载后更新界面
     */
    public void updateView() {
    }

    /**
     * 数据加载,有数据请求的基本都要放在这里,以跟新界面
     */
    public void loadData() {
        setIsRequesting(true);
    }

    /**
     * 统一返回事件
     *
     * @param v
     */
    public void onBack(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((BaseApp) getApplicationContext()).removeActivity(this);
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 网络请求成功 后重写
     *
     * @param statusCode
     * @param headers
     * @param jo
     */
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {

    }

    /**
     * 网络请求不成功 后重写
     *
     * @param statusCode
     * @param headers
     * @param jo
     */
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {

    }

    /**
     * 网络异常 后重写
     *
     * @param statusCode
     * @param headers
     * @param str
     * @param throwable
     */
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {

    }

    /**
     * 请求结束后调用
     */
    @Override
    public void setRequestFinish() {
        setIsRequesting(false);
    }

    @Override
    public void setRequestCancel() {

    }

    @Override
    public void setRequestProgress(int bytesWritten, int totalSize) {

    }

    @Override
    public void setRequestRetry(int retryNo) {

    }

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
