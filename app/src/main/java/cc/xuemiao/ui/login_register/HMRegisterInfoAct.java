package cc.xuemiao.ui.login_register;

import android.os.Bundle;
import android.view.KeyEvent;
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
import cc.xuemiao.utils.HMNavUtil;

/**
 * 注册信息页面，账号密码设置页
 * 
 * @author m
 * 
 */
public class HMRegisterInfoAct extends HMBaseAct {

	@Bind( R.id.common_head_rl_back)
	RelativeLayout rlBack;
	
	@Bind( R.id.common_head_tv_title)
	TextView tvTitle;

	@Bind( R.id.register_et_nickname)
	EditText etNickname;

	@Bind( R.id.register_tv_submit)
	TextView tvSubmit;

	private String nickname;

	private HMUserBean userBean;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentViews(R.layout.act_register);
//		setTouchOutsideToHideKeyboard();
		tvTitle.setText("昵称填写");
		rlBack.setVisibility(View.INVISIBLE);
		etNickname.setText(nickname);
	}

	@Override
	public void loadData() {
		super.loadData();
		userBean =((HMApp)getApplication()).getUser();
		if (userBean == null) {
			return;
		}
		etNickname.setText(userBean.getNikName());
	}

	@OnClick(R.id.register_tv_submit)
	public void onRegister(final View v) {
		nickname = etNickname.getText().toString();
		if (StringUtil.isEmpty(nickname)) {
			ToastUtil.getInstance().showShortToast(this, "请输入用户名");
			return;
		}
		if (!nickname.matches(BaseConfig.PATTERN_NICKNAME)) {
			ToastUtil.toastAlways(this, "请输入正确格式的账号");
			return;
		}
		HMUserBean userBean =((HMApp)getApplication()).getUserSP()
				.getUserBean(HMUserBean.class);
		showLoading();
		RequestParams params = new RequestParams();
		params.put("accountId", userBean.getId());
		params.put("nikName", nickname);
		HMApiUser.getInstance().postCommitInfo(this, params);
	}

	@Override
	public void setRequestSuc(String url, int statusCode, Header[] headers,
			JsonObject jo) {
		super.setRequestSuc(url, statusCode, headers, jo);
		// 更新环信上的昵称
		if (userBean == null) {
			userBean = ((HMApp)getApplication()).getUser();
		}
		userBean.setNikName(nickname);
		((HMApp)getApplication()).getUserSP().saveUserBean(userBean);
		HMNavUtil.goToNewAct(this, HMRegisterMaterialAct.class);
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

	public void onAuth(View v) {
		ToastUtil.getInstance().showShortToast(this, "功能开发中...");
		switch (v.getId()) {
		case R.id.qq:

			break;
		case R.id.weixin:

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
