package cc.xuemiao.api;

import com.easemob.api.HMApiChat;
import com.google.gson.JsonObject;
import com.lib_common.net.HttpJsonCallback;
import com.lib_common.net.HttpRequestUtils;
import com.lib_common.util.CommonToolsUtil;
import com.lib_common.util.GsonUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import cc.xuemiao.HMApp;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.HMSetNameAct;
import cc.xuemiao.ui.login_register.HMLoginAct;
import cc.xuemiao.ui.login_register.HMModifyPasswordAct;
import cc.xuemiao.ui.login_register.HMRegisterMaterialAct;

public class HMApiUser extends HMApi {
    public static final String REGISTER = HOST + "webapi/account/register";
    public static final String LOGIN = HOST + "webapi/parent/login";
    public static final String CAPTCHA = HOST + "webapi/parent/getCode";
    public static final String COMMIT_INFO = HOST + "webapi/account/edit";
    public static final String COMMIT_MATERIAL = HOST + "webapi/parent/edit";

    public static final String UPDATE_ACCOUNT_NAME = HOST
            + "webapi/account/editAccountName";

    public static final String CHANGE_PWD = HOST + "webapi/account/resetPwd";

    public static HMApiUser getInstance() {
        return new HMApiUser();
    }

    public void postLogin(HMLoginAct act, RequestParams params) {
        params.put("identity", IDENTITY);
        params.put("version", CommonToolsUtil.getVersionName(act));
        postRequest(act, LOGIN, params);
    }

    public void postLoginOnBack(final HMApp app, final HMUserBean user) {
        RequestParams params = new RequestParams();
        params.put("accountName", user.getAccountName());
        params.put("password", user.getPassword());
        params.put("identity", IDENTITY);
        params.put("version", CommonToolsUtil.getVersionName(app));
        HttpRequestUtils.post(app, LOGIN, params, new HttpJsonCallback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JsonObject jo) {
                super.onSuccess(statusCode, headers, jo);
                if (checkRes(jo)) {
                    final HMUserBean userBean = GsonUtil.fromJsonObj(
                            jo.getAsJsonObject(KEY_DATA), HMUserBean.class);
                    userBean.setPassword(user.getPassword());
                    app.saveUser(userBean);
                    app.updateActivities();
                    HMApiChat.getInstance().login(app,"1","");
                }
            }
        });
    }

    public void postCaptcha(HMBaseAct act, RequestParams params) {
        postRequest(act, CAPTCHA, params);
    }

    public void postRegister(HMBaseAct activity,
                             RequestParams params) {
        postRequest(activity, REGISTER, params);
    }

    public void postCommitInfo(HMBaseAct act, RequestParams params) {
        postRequest(act, COMMIT_INFO, params);
    }

    public void postCommitMaterial(final HMRegisterMaterialAct act,
                                   RequestParams params) {
        postRequest(act, COMMIT_MATERIAL, params);
    }

    public void postChangePwd(HMModifyPasswordAct act,
                              RequestParams params) {
        postRequest(act, CHANGE_PWD, params);
    }

    public void postUpdateAccountName(HMSetNameAct act, RequestParams params) {
        postRequest(act, UPDATE_ACCOUNT_NAME, params);
    }

}
