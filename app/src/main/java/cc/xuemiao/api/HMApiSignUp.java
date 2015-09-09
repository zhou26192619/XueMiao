package cc.xuemiao.api;

import android.content.Context;

import com.lib_common.net.HttpJsonCallback;
import com.lib_common.net.HttpRequestUtils;
import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;

public class HMApiSignUp extends HMApi {
    public static final String SIGN_UP = HOST
            + "webapi/signups/doSignup";

    public static  HMApiSignUp getInstance(){
        return  new HMApiSignUp();
    }
    public void postSignUp(Context context, RequestParams params, HttpJsonCallback callback) {
        HttpRequestUtils.post(context, SIGN_UP, params, callback);
    }
}
