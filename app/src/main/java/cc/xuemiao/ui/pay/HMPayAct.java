package cc.xuemiao.ui.pay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.pingplusplus.android.PaymentActivity;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiPay;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;

/**
 * 支付界面
 */
public class HMPayAct extends HMBaseAct {

	public static final String BUNDLE_KEY_ID = "id";
	public static final String BUNDLE_KEY_TITLE = "title";

	private static final int REQUEST_CODE_PAYMENT = 1;

	@Bind( R.id.common_head_tv_title)
	TextView tvTitle;

	@Bind( R.id.pay_tv_submit)
	TextView tvSubmit;

	@Bind( R.id.pay_rg_chanel)
	RadioGroup rgChannel;

	private String id;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentViews(R.layout.act_pay);
		init();
		setListener();
	}

	@Override
	public void dealIntent(Bundle bundle) {
		super.dealIntent(bundle);
		id = bundle.getString(BUNDLE_KEY_ID);
	}

	private void init() {
		tvTitle.setText("支付中心");
	}

	private void setListener() {
	}

	@OnClick(R.id.pay_tv_submit)
	public void onSubmit(View v) {
		HMUserBean user =((HMApp)getApplication()).getUser();
		if (user == null) {
			return;
		}
		int rbId = rgChannel.getCheckedRadioButtonId();
		RequestParams params = new RequestParams();
		params.put("accountId", user.getId());
		params.put("activityId", "16");
		params.put("content", "活动");
		params.put("amount", 10);
		if (rbId == R.id.pay_rb_alipay) {
			params.put("channel", HMApiPay.CHANNEL_ALIPAY);
		} else if (rbId == R.id.pay_rb_wx) {
			params.put("channel", HMApiPay.CHANNEL_WECHAT);
		}
		HMApiPay.getInstance().postPayCharge(this, params);
		tvSubmit.setClickable(false);
		showLoading();
	}

	public void setRequestSuc(String url, int statusCode, Header[] headers,
			JsonObject jo) {
		super.setRequestSuc(url, statusCode, headers, jo);
		String charge = jo.getAsJsonObject(HMApi.KEY_DATA).toString();
		Intent intent = new Intent();
		String packageName = getPackageName();
		ComponentName componentName = new ComponentName(packageName,
				packageName + ".wxapi.WXPayEntryActivity");
		intent.setComponent(componentName);
		intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
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
		tvSubmit.setClickable(true);
		hideLoading();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		tvSubmit.setClickable(true);
		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				/*
				 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
				String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
				String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
				ToastUtil.toast(this, result + errorMsg + extraMsg);
				if ("success".equals(result)) {
					onBack(null);
				}
			}
		}
	}
}
