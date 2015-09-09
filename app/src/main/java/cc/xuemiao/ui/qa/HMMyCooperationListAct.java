package cc.xuemiao.ui.qa;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiQA;
import cc.xuemiao.bean.HMQABean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 问答的回答列表页
 *
 * @author loar
 */
public class HMMyCooperationListAct extends HMBaseAct {

    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_ORG_NAME = "name";
    public static final String BUNDLE_KEY_TYPE = "type";

    @Bind(R.id.my_cooperation_list_ptrlv_list)
    PullToRefreshListView lvList;

    private String id;

    private List<HMQABean> QABeans;
    private List<HMQABean> myQABeans;
    private List<HMQABean> otherQABeans;

    private CommonAdapter<HMQABean> adapter;

//	private int state = R.id.my_cooperation_list_tv_q;

    private HMUserBean user;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_my_cooperation_list);
        init();
        initListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        id = bundle.getString(BUNDLE_KEY_ID);
        type = bundle.getInt(BUNDLE_KEY_TYPE, HMApiQA.TYPE_COURSE);
    }

    private void init() {
        adapter = new CommonAdapter<HMQABean>(this, QABeans,
                R.layout.adapter_cooperation) {

            @Override
            public void dealViews(ViewHolder holder, List<HMQABean> datas,
                                  int position) {
                commendAdapterViews(holder, datas, position);
            }
        };
        lvList.setAdapter(adapter);
        showLoading();
    }

    protected void commendAdapterViews(ViewHolder holder, List<HMQABean> datas,
                                       int position) {
        final HMQABean bean = datas.get(position);
        LinearLayout llRoot = holder.getViewById(R.id.cooperation_ll_root,
                LinearLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.cooperation_iv_logo,
                ImageView.class);
        TextView tvContent = holder.getViewById(R.id.cooperation_tv_content,
                TextView.class);
        TextView tvDatetime = holder.getViewById(R.id.cooperation_tv_datetime,
                TextView.class);
        TextView tvName = holder.getViewById(R.id.cooperation_tv_name,
                TextView.class);
        try {
            tvContent.setText(bean.getQuestionContent());
            tvDatetime.setText(bean.getCreateDate());
            tvName.setText(bean.getAccount().getAccountName());
            MImageLoader.display(this, bean.getAccount().getLogo(), ivLogo,
                    MImageLoader.DEFAULT_IMAGE, true, 40);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        llRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCooperationDetailAct.BUNDLE_KEY, bean);
                HMNavUtil.goToNewAct(HMMyCooperationListAct.this,
                        HMCooperationDetailAct.class, bundle);
            }
        });
    }

    private void initListener() {
        lvList.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });

        lvList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                loadMore();
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        if (user == null) {
            user = ((HMApp) getApplication()).getUser();
        }
        if (user == null) {
            return;
        }
        adapter.resetPageIndex();
        load();
    }

    private void load() {
        RequestParams params = new RequestParams();
        params.put("objectId", id);
        params.put("qAccountId", user.getId());
        params.put("questionType", type);
        params.put("pageSize", adapter.getPageSize());
        params.put("pageIndex", adapter.getPageIndex());
    }

    private void loadMore() {
        if (adapter.hasMorePage(adapter.getTotalSize())) {
            load();
            return;
        }
        ToastUtil.toastAlways(this, "没有更多了哦！");
    }

    @Override
    public void updateView() {
        adapter.setDatas(QABeans);
    }

//	public void onQ(View v) {
//		changeTitleState(v.getId());
//	}
//
//	public void onA(View v) {
//		changeTitleState(v.getId());
//	}

//	private void changeTitleState(int id) {
//		if (state == id) {
//			return;
//		}
//		state = id;
//		adapter.resetPageIndex();
//		if (id == R.id.my_cooperation_list_tv_q) {
//			tvAll.setBackgroundResource(R.drawable.white_half_corner_left);
//			tvMine.setBackgroundResource(R.drawable.white_light_half_corner_right);
//			tvMine.setTextColor(Color.parseColor("#ffffff"));
//			tvAll.setTextColor(Color.parseColor("#00c92a"));
//			if (otherQABeans != null && adapter.isFirstPage()) {
//				QABeans = otherQABeans;
//				updateView();
//				lvList.onRefreshComplete();
//				return;
//			}
//		} else if (id == R.id.my_cooperation_list_tv_a) {
//			tvAll.setBackgroundResource(R.drawable.white_light_half_corner_left);
//			tvMine.setBackgroundResource(R.drawable.white_half_corner_right);
//			tvAll.setTextColor(Color.parseColor("#ffffff"));
//			tvMine.setTextColor(Color.parseColor("#00c92a"));
//			if (myQABeans != null && adapter.isFirstPage()) {
//				QABeans = myQABeans;
//				updateView();
//				return;
//			}
//		}
//		loadData();
//	}

    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiQA.LIST)
                || url.equals(HMApiQA.MY_LIST)) {
            List<HMQABean> list = null;
            if (jo.has(HMApi.KEY_DATA)) {
                list = GsonUtil.fromJsonArr(jo.getAsJsonObject(HMApi.KEY_DATA)
                                .getAsJsonArray(HMApi.KEY_LIST),
                        new TypeToken<List<HMQABean>>() {
                        });
                adapter.setTotalSize(list.size());
            }
            if (adapter.isFirstPage()) {
                if (url.equals(HMApiQA.LIST)) {
                    otherQABeans = list;
                } else if (url.equals(HMApiQA.MY_LIST)) {
                    myQABeans = list;
                }
                QABeans = list;
            } else {
                QABeans.addAll(list);
            }
            adapter.nextPage();
            updateView();
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
        lvList.onRefreshComplete();
        hideLoading();
    }
}
