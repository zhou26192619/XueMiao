package cc.xuemiao.ui.login_register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import com.easemob.api.HMApiChat;
import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;

public class HMLoginAct extends HMBaseAct {

    @Bind(R.id.login_et_account)
    EditText etAccout;

    @Bind(R.id.login_et_password)
    EditText etPassword;

    private String password;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_login);
        hvHeadView.setTitle("登录");
    }

    @Override
    public void updateView() {
        super.updateView();
    }

    @OnClick(R.id.login_tv_login)
    public void onLogin(View v) {
        String accountName = etAccout.getText().toString();
        password = etPassword.getText().toString();
        if (StringUtil.isEmpty(accountName)) {
            ToastUtil.getInstance().showShortToast(this, "请输入用户名或手机号");
            return;
        }
//		if (!accountName.matches(BaseConfig.PATTERN_ACCOUNT_NAME)
//				&& !accountName.matches(BaseConfig.PATTERN_PHONE_NUMBER)) {
//			ToastUtil.toastAlways(this, "账号或手机号格式可能不对哦!");
//			return;
//		}
        if (StringUtil.isEmpty(password)) {
            ToastUtil.getInstance().showShortToast(this, "请输入密码");
            return;
        }
        if (!password.matches(BaseConfig.PATTERN_PASSWORD)) {
            ToastUtil.toastAlways(this, "密码格式好像不对哦!");
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("accountName", accountName);
        params.put("password", password);
        HMApiUser.getInstance().postLogin(this, params);
    }

    @OnClick(R.id.login_tv_register)
    public void onRegister(View v) {
        HMNavUtil.goToNewAct(this, HMRegisterCaptchaAct.class);
        onBack(v);
    }

    @OnClick({R.id.weibo, R.id.qq, R.id.weixin})
    public void onAuth(View v) {
        ToastUtil.getInstance().showShortToast(this, "功能开发中...");
        switch (v.getId()) {
            case R.id.weibo:

                break;
            case R.id.qq:

                break;
            case R.id.weixin:

                break;

            default:
                break;
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        hideLoading();
        if (url.equals(HMApiUser.LOGIN)) {
            HMUserBean userBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMUserBean.class);
            userBean.setPassword(password);

            HMApiChat.getInstance().login(this, userBean.getId(), password);

            ((HMApp) getApplication()).getUserSP().saveUserBean(userBean);
            ((HMApp) getApplication()).updateActivities();
            onBack(null);
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
    }
}
