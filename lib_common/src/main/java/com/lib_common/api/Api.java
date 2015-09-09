package com.lib_common.api;

import com.google.gson.JsonObject;
import com.lib_common.activity.BaseActivity;
import com.lib_common.fragment.BaseFragment;
import com.lib_common.net.HttpJsonCallback;
import com.lib_common.net.HttpRequestUtils;
import com.lib_common.observer.ActivityObserver;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class Api {
    //	 public static final String HOST = "http://www.xuemiao.cc/";
    public static final String HOST = "http://115.29.207.3:8080/HeMiao/";
//	 public static final String HOST = "http://192.168.199.170:8080/HeMiao/";

    public static final String HOST_VERSION = HOST;

    public static final String HOST_CHAT = HOST;

    public static final String HOST_NOTICE = HOST;


    // *****json key******
    public static final String STATUS_OK = "2000";
    public static final String KEY_STATUS = "code";
    public static final String KEY_DATA = "datas";
    public static final String KEY_LIST = "resultList";
    public static final String KEY_TOTAL_COUNT = "totalSize";
    public static final String KEY_PAGE_INDEX = "pageIndex";
    public static final String KEY_PAGE_SIZE = "pageSize";
    public static final String KEY_MSG = "msg";

    // ********************

    public static boolean checkRes(JsonObject obj) {
        if (STATUS_OK.equals(obj.getAsJsonPrimitive(KEY_STATUS).getAsString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * post请求
     *
     * @param act    实现ActivityObserver的activity
     * @param url
     * @param params
     */
    public void postRequest(final ActivityObserver act, final String url,
                            RequestParams params) {
        HttpRequestUtils.post(act.getContext(), url, params, new HttpJsonCallback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JsonObject jo) {
                super.onSuccess(statusCode, headers, jo);
                if (checkRes(jo)) {
                    act.setRequestSuc(url, statusCode, headers, jo);
                } else {
                    act.setRequestNotSuc(url, statusCode, headers, jo);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String str,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, str, throwable);
                act.setRequestErr(url, statusCode, headers, str, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                act.setRequestFinish();
            }
        });
    }

    public void postRequestByNotice(final ActivityObserver act,
                                    final String url, RequestParams params) {
        HttpRequestUtils.post(act.getContext(), url, params, new HttpJsonCallback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JsonObject jo) {
                super.onSuccess(statusCode, headers, jo);
                act.setRequestSuc(url, statusCode, headers, jo);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String str,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, str, throwable);
                act.setRequestErr(url, statusCode, headers, str, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                act.setRequestFinish();
            }
        });
    }

    public void getRequest(final BaseFragment bf, final String url,
                           RequestParams params) {
        HttpRequestUtils.get(bf.getActivity(), url, params,
                new HttpJsonCallback() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JsonObject jo) {
                        super.onSuccess(statusCode, headers, jo);
                        if (checkRes(jo)) {
                            bf.setRequestSuc(url, statusCode, headers, jo);
                        } else {
                            bf.setRequestNotSuc(url, statusCode, headers, jo);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          String str, Throwable throwable) {
                        super.onFailure(statusCode, headers, str, throwable);
                        bf.setRequestErr(url, statusCode, headers, str,
                                throwable);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        bf.setRequestFinish();
                    }
                });
    }
}
