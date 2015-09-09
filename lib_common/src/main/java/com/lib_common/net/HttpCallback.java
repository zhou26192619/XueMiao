package com.lib_common.net;

import org.apache.http.Header;

public interface HttpCallback<T> {
	public void onSuccess(int statusCode, Header[] headers, T t);

	public void onFailure(int statusCode, Header[] headers, String str,
						  Throwable throwable);

	public void onCancel();

	public void onFinish();

	public void onProgress(long bytesWritten, long totalSize);

	public void onRetry(int retryNo);

	public void onStart();
}
