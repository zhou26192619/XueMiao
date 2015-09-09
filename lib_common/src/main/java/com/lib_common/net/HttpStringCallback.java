package com.lib_common.net;

import com.lib_common.util.ToastUtil;

import org.apache.http.Header;

public class HttpStringCallback implements HttpCallback<String> {

	@Override
	public void onSuccess(int statusCode, Header[] headers, String t) {

	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String t,
			Throwable throwable) {
		ToastUtil.logE("HttpStringCallback err =", throwable.getMessage());
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
