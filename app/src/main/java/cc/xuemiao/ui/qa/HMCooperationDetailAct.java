package cc.xuemiao.ui.qa;

import java.util.List;


import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.adapter.CooperationDetailAdapter;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiQA;
import cc.xuemiao.bean.HMQABean;
import cc.xuemiao.bean.HMQABean.HMAnswerBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.view.dialog.EditDialog;
import cc.xuemiao.ui.view.dialog.ReplyDialog;

/**
 * 互助的回答列表页
 * 
 * @author loar
 * 
 */
public class HMCooperationDetailAct extends HMBaseAct {

	public static final String BUNDLE_KEY = "bean";


	@Bind(R.id.cooperation_detail_iv_list)
	PullToRefreshListView lvList;

	@Bind(R.id.cooperation_detail_iv_reply)
	ImageView ivReply;

	private HMQABean qaBean;

	private CooperationDetailAdapter adapter;
	private List<HMAnswerBean> answerBeans;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.act_cooperation_detail);
		init();
		showLoading();
	}

	@Override
	public void dealIntent(Bundle bundle) {
		super.dealIntent(bundle);
		qaBean = (HMQABean) bundle.getSerializable(BUNDLE_KEY);
	}

	private void init() {
		MImageLoader.displayWithDefaultOptions(this, "" + R.mipmap.ask,
				ivReply);
		adapter = new CooperationDetailAdapter(this, qaBean, answerBeans);
		lvList.setAdapter(adapter);
		lvList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadMore();
			}
		});
		lvList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				reLoad();
			}
		});

		adapter.setOnReplyListener(new CooperationDetailAdapter.OnReplyListener() {

			@Override
			public boolean reply(HMAnswerBean bean, View v) {
				showReplyDialog("@" + bean.getAccount().getAccountName() + " ");
				return true;
			}
		});
	}

	private EditDialog dialog;

	private void showReplyDialog(final String at) {
		final HMUserBean user = ((HMApp)getApplication()).getUser();
		if (user == null) {
			return;
		}
		if (dialog == null) {
			dialog = new EditDialog(this, R.style.toast_box_dialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOKText("提交");
			dialog.setOnEditClickListener(new EditDialog.OnEditClickListener() {

				@Override
				public void ok(String text, View v) {
					if (StringUtil.isEmpty(text)) {
						ToastUtil.toastAlways(HMCooperationDetailAct.this,
								"拜托，不要什么都不写，好不好！");
						return;
					}
					RequestParams params = new RequestParams();
					params.put("accountId", user.getId());
					params.put("content", at + text);
					params.put("questionId", qaBean.getId());
//					HMApiQA.postAddReply(HMCooperationDetailAct.this,params);
					dialog.dismiss();
				}

				@Override
				public void cancel(View v) {
					dialog.dismiss();
				}
			});
		}
		dialog.setTitle(StringUtil.isEmpty(at) ? "回复" : at);
		dialog.show();
	}

	public void onReply(View v) {
		showReplyDialog("");
	}

	@Override
	public void loadData() {
		super.loadData();
		RequestParams params = new RequestParams();
		params.put("questionId", qaBean.getId());
		params.put("pageSize", adapter.getPageSize());
		params.put("pageIndex", adapter.getPageIndex());
	}

	private void loadMore() {
		if (adapter.hasMorePage(adapter.getTotalSize())) {
			loadData();
		} else {
			ToastUtil.toastAlways(this, "抱歉，小苗给不了更多的啦！");
		}
	}

	private void reLoad() {
		adapter.resetPageIndex();
		loadData();
	}

	public void setRequestSuc(String url, int statusCode, Header[] headers,
			JsonObject jo) {
		super.setRequestSuc(url, statusCode, headers, jo);
		if (url.equals(HMApiQA.GET_ALL_REPLY)) {
			List<HMAnswerBean> list = GsonUtil.fromJsonArr(
					jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(
							HMApi.KEY_LIST),
					new TypeToken<List<HMAnswerBean>>() {
					});
			adapter.setTotalSize(list.size());
			if (adapter.isFirstPage()) {
				answerBeans = list;
			} else {
				answerBeans.addAll(list);
			}
			adapter.nextPage();
			adapter.setData(answerBeans);
		} else if (url.equals(HMApiQA.ADD_REPLY)) {
			dialog.setEditText("");
			reLoad();
			ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
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
		lvList.onRefreshComplete();
	}
}
