package com.lib_common.net;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class HttpRequestUtils {
    private static final String TAG_URL = "HttpRequestUrl";
    private static final String TAG_RESULT = "HttpRequestResult";
    private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

    public static AsyncHttpClient getInstance() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }

    public static void setTimeout(int value) {
        getInstance().setTimeout(value);
    }

    public static void post(Context context, String url, Header[] headers,
                            RequestParams params, String contentType, HttpCallback<?> callback) {
        getInstance().post(context, url, headers, params, contentType,
                getAsyncHttpResponseHandler(url, callback));
    }

    public static void post(Context context, String url, Header[] headers,
                            HttpEntity entity, String contentType, HttpCallback<?> callback) {
        getInstance().post(context, url, headers, entity, contentType,
                getAsyncHttpResponseHandler(url, callback));
    }

    public static void post(Context context, String url, HttpEntity entity,
                            String contentType, HttpCallback<?> callback) {
        getInstance().post(context, url, entity, contentType,
                getAsyncHttpResponseHandler(url, callback));
    }

    public static void post(Context context, String url, RequestParams params,
                            HttpCallback<?> callback) {
        ToastUtil.log(TAG_URL + url, url + "?" + params.toString());
        getInstance().post(context, url, params,
                getAsyncHttpResponseHandler(url, callback));
    }

    public static void get(Context context, String url, RequestParams params,
                           HttpCallback<?> callback) {
        ToastUtil.log(TAG_URL + url, url + "?" + params);
        getInstance().get(context, url, params,
                getAsyncHttpResponseHandler(url, callback));
    }

    public static void get(Context context, String url, HttpCallback<?> callback) {
        ToastUtil.log(TAG_URL + url, url);
        // 其实应该调多惨的方法的
        getInstance().get(context, url,
                getAsyncHttpResponseHandler(url, callback));
    }

    private static AsyncHttpResponseHandler getAsyncHttpResponseHandler(
            final String url, final HttpCallback<?> callback) {
        return new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                try {
                    String str = null;
                    if (bytes == null) {
                        str = "{'msg':'没有任何信息'}";
                    } else {
                        str = new String(bytes);
                    }
                    ToastUtil.log(TAG_RESULT + url, str);
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onSuccess(statusCode, headers, str);
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            JsonParser parser = new JsonParser();
                            JsonObject jo = parser.parse(str).getAsJsonObject();
                            hsc.onSuccess(statusCode, headers, jo);
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] bytes, Throwable throwable) {
                try {
                    String str = "";
                    if (bytes != null) {
                        str = new String(bytes);
                    } else {
                        str = throwable.getMessage();
                    }
                    ToastUtil.log(TAG_RESULT + url, str);
                    str = "网络好像开小差了，拜托，再试试呗";
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onFailure(statusCode, headers, str, throwable);
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            // JSONObject jo = JSONObject.parseObject(str);
                            hsc.onFailure(statusCode, headers, str, throwable);
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onCancel() {
                super.onCancel();
                try {
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onCancel();
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            hsc.onCancel();
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    ToastUtil.log(TAG_RESULT + url, "finish");
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onFinish();
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            hsc.onFinish();
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                try {
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onProgress(bytesWritten, totalSize);
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            hsc.onProgress(bytesWritten, totalSize);
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
                try {
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onRetry(retryNo);
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            hsc.onRetry(retryNo);
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                try {
                    if (callback != null) {
                        if (callback instanceof HttpStringCallback) {
                            HttpStringCallback hsc = (HttpStringCallback) callback;
                            hsc.onStart();
                        } else if (callback instanceof HttpJsonCallback) {
                            HttpJsonCallback hsc = (HttpJsonCallback) callback;
                            hsc.onStart();
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }

        };
    }
}
