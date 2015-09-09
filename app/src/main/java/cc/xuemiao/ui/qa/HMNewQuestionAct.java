package cc.xuemiao.ui.qa;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiQA;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;

/**
 * 课程列表
 * 
 * @author m
 * 
 */
public class HMNewQuestionAct extends HMBaseAct {

	public static final String BUNDLE_KEY_ID = "id";
	public static final String BUNDLE_KEY_TYPE = "type";

	@Bind( R.id.common_head_tv_title)
	TextView tvTitle;

	@Bind( R.id.common_head_tv_questions)
	TextView tvQuestions;

	@Bind( R.id.new_question_tv_submit)
	TextView tvSubmit;

	@Bind( R.id.new_question_et_content)
	EditText etContent;

	private String id;
	private int type;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentViews(R.layout.act_new_question);
		init();
	}

	@Override
	public void dealIntent(Bundle bundle) {
		super.dealIntent(bundle);
		id = bundle.getString(BUNDLE_KEY_ID);
		type = bundle.getInt(BUNDLE_KEY_TYPE, HMApiQA.TYPE_COURSE);
	}

	private void init() {
		tvTitle.setText("提问");
	}

	public void onSubmit(View v) {
		HMUserBean user = ((HMApp)getApplication()).getUser();
		if (user == null) {
			return;
		}
		String content = etContent.getText().toString();
		if (StringUtil.isEmpty(content)) {
			ToastUtil.toastAlways(this, "什么都不写,人家理解不了了!");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("objectId", id);
		params.put("content", content);
		params.put("qAccountId", user.getId());
		params.put("questionType", type);
		HMApiQA.getInstance().postNewQuestion(this, params);
		showLoading();
	}

	public void setRequestSuc(String url, int statusCode, Header[] headers,
			JsonObject jo) {
		super.setRequestSuc(url, statusCode, headers, jo);
		ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
		onBack(null);
		((HMApp)getApplication()).updateActivity(HMQAListAct.class);
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
