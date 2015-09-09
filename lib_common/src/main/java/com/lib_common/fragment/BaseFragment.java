package com.lib_common.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.google.gson.JsonObject;
import com.lib_common.observer.ActivityObserver;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements ActivityObserver {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = onContentView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        init(rootView, inflater, container, savedInstanceState);
        return rootView;
    }

    /**
     * 只能设置布局视图view，其他操作请在init等方法中执行
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public abstract View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void dealIntent(Bundle bundle) {

    }

    @Override
    public void loadData() {
        setIsRequesting(true);

    }

    @Override
    public void updateView() {

    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers, String str, Throwable throwable) {

    }

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

    @Override
    public void setRequestNotSuc(String url, int statusCode, Header[] headers, JsonObject jo) {

    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers, JsonObject jo) {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
