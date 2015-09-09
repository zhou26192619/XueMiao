package cc.xuemiao.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

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
import cc.xuemiao.api.HMApiParent;
import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMUserBean;

/**
 * 设置姓名、名字
 *
 * @author loar
 */
public class HMSetNameAct extends HMBaseAct {

    public static final int CODE_RESULT_OK = 150518;
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_TIP = "tip";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final int BUNDLE_KEY_TYPE_NICKNAME = 2;
    public static final int BUNDLE_KEY_TYPE_ACCOUNT_NAME = 1;
    public static final int BUNDLE_KEY_TYPE_NAME = 0;

    @Bind(R.id.set_name_et_name)
    EditText etName;

    private HMUserBean user;

    private String name;
    private String title;
    private String tip;
    private int type;
    private String pattern;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_set_name);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        title = bundle.getString(BUNDLE_KEY_TITLE);
        tip = bundle.getString(BUNDLE_KEY_TIP);
        type = bundle.getInt(BUNDLE_KEY_TYPE);
    }

    private void init() {
        hvHeadView.setTitle(title);
        etName.setHint(tip);
        if (type == BUNDLE_KEY_TYPE_NAME) {
            etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    11)});
            pattern = BaseConfig.PATTERN_NAME;
        } else if (type == BUNDLE_KEY_TYPE_ACCOUNT_NAME) {
            etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    15)});
            pattern = BaseConfig.PATTERN_ACCOUNT_NAME;
            etName.setHint(R.string.HINT_PATTERN_ACCOUNT_NAME);
        } else if (type == BUNDLE_KEY_TYPE_NICKNAME) {
            etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    15)});
            pattern = BaseConfig.PATTERN_NICKNAME;
        }
    }

    @OnClick(R.id.common_head_rl_right_icon)
    public void onRightIcon(View v) {
        user = ((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        name = etName.getText().toString().trim();
        if (StringUtil.isEmpty(name)) {
            ToastUtil.toastAlways(this, "姓名不能为空");
            return;
        }
        if (!name.matches(pattern)) {
            ToastUtil.toastAlways(this, "格式不正确，请重新填写");
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("accountId", user.getId());
        if (type == BUNDLE_KEY_TYPE_ACCOUNT_NAME) {
            params.put("accountName", name);
            HMApiUser.getInstance().postUpdateAccountName(this, params);
        } else if (type == BUNDLE_KEY_TYPE_NAME) {
            params.put("name", name);
            params.put("parentId", user.getRoleId());
            HMApiParent.getInstance().postUpdate(this, params);
        } else if (type == BUNDLE_KEY_TYPE_NICKNAME) {
            params.put("nikName", name);
            HMApiUser.getInstance().postCommitInfo(this, params);
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiParent.UPDATE)) {
            user.getParent().setName(name);
        } else if (url.equals(HMApiUser.UPDATE_ACCOUNT_NAME)) {
            user.setAccountName(name);
        } else if (url.equals(HMApiUser.COMMIT_INFO)) {
            user.setNikName(name);
        }
        ((HMApp)getApplication()).getUserSP().saveUserBean(user);
        setResult(CODE_RESULT_OK);
        onBack(null);
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
