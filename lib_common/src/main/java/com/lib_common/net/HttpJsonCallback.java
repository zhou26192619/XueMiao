package com.lib_common.net;

import com.google.gson.JsonObject;
import com.lib_common.util.ToastUtil;

import org.apache.http.Header;


public class HttpJsonCallback implements HttpCallback<JsonObject> {

    @Override
    public void onSuccess(int statusCode, Header[] headers, JsonObject jo) {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String str,
                          Throwable throwable) {
        ToastUtil.logE("HttpJsonCallback err =", throwable.getMessage());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {

    }

    @Override
    public void onRetry(int retryNo) {

    }

    @Override
    public void onStart() {

    }

}
