package cc.xuemiao.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
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
import cc.xuemiao.api.HMApiParent;
import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMUserBean;

/**
 * 用户手机绑定
 *
 * @author m
 */
public class HMPhoneFillInAct extends HMBaseAct {

    @Bind(R.id.phone_fill_in_tv_phone)
    TextView tvPhone;

    @Bind(R.id.phone_fill_in_et_phone)
    EditText etPhone;

    @Bind(R.id.phone_fill_in_tv_get_captcha)
    TextView tvGetCaptcha;
    @Bind(R.id.phone_fill_in_tv_submit)
    TextView tvSubmit;
    @Bind(R.id.phone_fill_in_et_captchas)
    EditText etCaptcha;

    private Timer timer;

    private String phone;

    private HMUserBean user;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_phone_fill_in);
        init();
    }

    private void init() {
        hvHeadView.setTitle("账号绑定");
        user = ((HMApp)getApplication()).getUser();
        if (user != null && user.getParent().getPhone() != null) {
            tvPhone.setText(user.getParent().getPhone());
        }
    }

    @Override
    public void updateView() {
        super.updateView();
    }

    @Override
    public void loadData() {
        super.loadData();
        updateView();
    }

    @OnClick(R.id.phone_fill_in_tv_submit)
    public void onSubmit(final View v) {
        user =((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        String captcha = etCaptcha.getText().toString().trim();
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
        String code = etCaptcha.getText().toString();
        RequestParams params = new RequestParams();
        params.put("parentId", user.getRoleId());
        params.put("phone", phone);
        params.put("code", code);
        HMApiParent.getInstance().postBindPhone(this, params);
    }

    /**
     * 获取验证码
     *
     * @param v
     */
    @OnClick(R.id.phone_fill_in_tv_get_captcha)
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

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiUser.CAPTCHA)) {

        } else if (url.equals(HMApiParent.BIND_PHONE)) {
            ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
            user.getParent().setPhone(phone);
            ((HMApp)getApplication()).getUserSP().saveUserBean(user);
            ((HMApp)getApplication()).updateActivity(HMOptionAct.class);
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

    private void setCaptchaAble() {
        timer.cancel();
        tvGetCaptcha.setText("获取验证码");
        count = 60;
        tvGetCaptcha.setClickable(true);
    }

    private Handler timeHandler = new Handler();
    private int count = 60;
}
