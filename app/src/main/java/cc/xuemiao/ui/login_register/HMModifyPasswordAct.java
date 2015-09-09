package cc.xuemiao.ui.login_register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;

/**
 * 修改密码页
 *
 * @author m
 */
public class HMModifyPasswordAct extends HMBaseAct {

    @Bind(R.id.modify_password_tv_nickname)
    TextView tvNickname;

    @Bind(R.id.modify_password_et_old_password)
    EditText etOldPassword;

    @Bind(R.id.modify_password_et_new_password)
    EditText etNewPassword;

    @Bind(R.id.modify_password_et_new_password_comfirm)
    EditText etNewPasswordComfirm;

    @Bind(R.id.modify_password_tv_submit)
    TextView tvSubmit;

    private HMUserBean user;

    private String newPwd;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_modify_password);
        init();
    }

    private void init() {
        hvHeadView.setTitle("修改密码");
    }

    @Override
    public void updateView() {
        super.updateView();
        tvNickname.setText(user.getAccountName());
    }

    @Override
    public void loadData() {
        super.loadData();
        user = ((HMApp)getApplication()).getUser();
        updateView();
    }

    @OnClick(R.id.modify_password_tv_submit)
    public void onSubmit(final View v) {
        user = ((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        String oldPwd = etOldPassword.getText().toString();
        newPwd = etNewPassword.getText().toString();
        String newPwdConfirm = etNewPasswordComfirm.getText().toString();
        if (StringUtil.isEmpty(oldPwd)) {
            ToastUtil.getInstance().showShortToast(this, "请输入原密码");
            return;
        }
        if (StringUtil.isEmpty(newPwd)) {
            ToastUtil.getInstance().showShortToast(this, "请输入新密码");
            return;
        }
        if (StringUtil.isEmpty(newPwdConfirm)) {
            ToastUtil.getInstance().showShortToast(this, "请确认新密码");
            return;
        }
        if (!newPwd.matches(BaseConfig.PATTERN_PASSWORD)) {
            ToastUtil.toastAlways(this, "请输入正确的密码格式");
            return;
        }
        if (!newPwd.equals(newPwdConfirm)) {
            ToastUtil.getInstance().showShortToast(this, "新密码输入不一致");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("accountId", user.getId());
        params.put("oldPwd", oldPwd);
        params.put("newPwd", newPwd);
        HMApiUser.getInstance().postChangePwd(this, params);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiUser.CHANGE_PWD)) {
            user.setPassword(newPwd);
            ((HMApp)getApplication()).saveUser(user);
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
    }
}
