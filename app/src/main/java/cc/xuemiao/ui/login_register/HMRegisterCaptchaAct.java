package cc.xuemiao.ui.login_register;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.Timer;
import java.util.TimerTask;

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

/**
 * 注册页，验证码
 *
 * @author loar
 */
public class HMRegisterCaptchaAct extends HMBaseAct {

    @Bind(R.id.register_captcha_et_phone)
    EditText etPhone;

    @Bind(R.id.register_captcha_tv_get_captcha)
    TextView tvGetCaptcha;

    @Bind(R.id.register_captcha_et_captcha)
    EditText etCaptcha;

    @Bind(R.id.register_captcha_tv_submit)
    TextView tvSubmit;

    @Bind(R.id.register_et_password)
    EditText etPassword;

    @Bind(R.id.register_et_password_comfirm)
    EditText etPasswordComfirm;

    private String phone;

    private Timer timer;

    private String password;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_register_captcha);
        init();
        updateView();
    }

    private void init() {
        // phone = DensityUtil.getDeviceInfo(this).getPhoneNumber1();
        // etPhone.setText(phone);
        hvHeadView.setTitle("注册");
    }


    // onClickListener

    /**
     * 提交验证码
     *
     * @param v
     */
    @OnClick(R.id.register_captcha_tv_submit)
    public void onRegister(View v) {
        String captcha = etCaptcha.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        String psComfirm = etPasswordComfirm.getText().toString().trim();
        phone = etPhone.getText().toString().trim();

        if (StringUtil.isEmpty(etPhone.getText().toString())) {
            ToastUtil.getInstance().showShortToast(this, "请输入手机号码");
            return;
        }
        if (!StringUtil.isMobileNumber(etPhone.getText().toString())) {
            ToastUtil.getInstance().showShortToast(this, "请输入正确的号码");
            return;
        }
        if (StringUtil.isEmpty(captcha)) {
            ToastUtil.getInstance().showShortToast(this, "请输入验证码");
            return;
        }
        if (StringUtil.isEmpty(password)) {
            ToastUtil.getInstance().showShortToast(this, "请输入密码");
            return;
        }
        if (StringUtil.isEmpty(psComfirm)) {
            ToastUtil.getInstance().showShortToast(this, "请输入确认密码");
            return;
        }
        if (!password.matches(BaseConfig.PATTERN_PASSWORD)) {
            ToastUtil.toastAlways(this, "请输入正确的密码格式");
            return;
        }
        if (!password.equals(psComfirm)) {
            ToastUtil.getInstance().showShortToast(this, "密码不一致");
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        params.put("code", captcha);
        params.put("identity", HMApi.IDENTITY);
        HMApiUser.getInstance().postRegister(this, params);
    }

    /**
     * 获取验证码
     *
     * @param v
     */
    @OnClick(R.id.register_captcha_tv_get_captcha)
    public void onGetCaptcha(View v) {
        if (StringUtil.isEmpty(etPhone.getText().toString())) {
            ToastUtil.getInstance().showShortToast(this, "请输入手机号码");
            return;
        }
        if (!StringUtil.isMobileNumber(etPhone.getText().toString())) {
            ToastUtil.getInstance().showShortToast(this, "请输入正确的号码");
            return;
        }
        phone = etPhone.getText().toString();
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("identity", HMApi.IDENTITY);
        HMApiUser.getInstance().postCaptcha(this, params);

        tvGetCaptcha.setClickable(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timeHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (count > 0) {
                            tvGetCaptcha.setText("" + count);
                            count--;
                        } else {
                            setCaptchaAble();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    public void setCaptchaAble() {
        tvGetCaptcha.setText("获取验证码");
        count = 60;
        tvGetCaptcha.setClickable(true);
        timer.cancel();
    }

    private Handler timeHandler = new Handler();
    private int count = 60;

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers, JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiUser.REGISTER)) {
            HMUserBean userbean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApiUser.KEY_DATA),
                    HMUserBean.class);
            userbean.setPassword(password);
            ((HMApp) getApplication()).getUserSP()
                    .saveUserBean(userbean);

            // 聊天系统注册
            HMApiChat.getInstance().register(this, phone, password);

            // 成功后默认登录
            RequestParams params = new RequestParams();
            params.put("accountName", phone);
            params.put("password", password);
            HMApiUser.getInstance().postLoginOnBack(((HMApp) getApplication()),userbean);

            HMNavUtil.goToNewAct(this,
                    HMRegisterInfoAct.class);
            onBack(null);
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
        if (url.equals(HMApiUser.CAPTCHA)) {
            setCaptchaAble();
        }
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
        if (url.equals(HMApiUser.CAPTCHA)) {
            setCaptchaAble();
        }
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
    }
}
