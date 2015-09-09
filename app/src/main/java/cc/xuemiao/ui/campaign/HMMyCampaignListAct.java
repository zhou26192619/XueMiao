package cc.xuemiao.ui.campaign;

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
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCampaign;
import cc.xuemiao.bean.HMCampaignSignUpBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 活动列表
 *
 * @author loar
 */
public class HMMyCampaignListAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "Id";

    @Bind(R.id.my_campaign_list_lv_campaigns)
    PullToRefreshListView lvCampaigns;

    private List<HMCampaignSignUpBean> datas = new ArrayList<HMCampaignSignUpBean>();

    private CommonAdapter<HMCampaignSignUpBean> adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_my_campaign_list);
        init();
        setListeners();
        updateView();
    }

    private void setListeners() {
        lvCampaigns.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                reload();
            }
        });

        lvCampaigns
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        loadMore();
                    }
                });
    }

    private void init() {
        hvHeadView.setTitle("我的活动");
        adapter = new CommonAdapter<HMCampaignSignUpBean>(this, datas,
                R.layout.adapter_my_campaign_list) {

            @Override
            public void dealViews(ViewHolder holder, List<HMCampaignSignUpBean> datas,
                                  int position) {
                commondAdapterViews(holder, datas, position);
            }
        };
        lvCampaigns.setAdapter(adapter);
    }

    protected void commondAdapterViews(ViewHolder holder,
                                       List<HMCampaignSignUpBean> datas, int position) {
        final HMCampaignSignUpBean bean = datas.get(position);
        LinearLayout llRoot = holder.getViewById(R.id.my_campaign_list_ll_root,
                LinearLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.my_campaign_list_iv_logo,
                ImageView.class);
        TextView tvStatus = holder.getViewById(R.id.my_campaign_list_tv_status,
                TextView.class);
        TextView tvDateTime = holder.getViewById(
                R.id.my_campaign_list_tv_date_time, TextView.class);
        TextView tvTitle = holder.getViewById(R.id.my_campaign_list_tv_title,
                TextView.class);
        try {
            tvTitle.setText(bean.getActivity().getName());
            tvDateTime.setText(DateUtil.datetimeTurnPattern(bean.getActivity()
                    .getActivityBeginTime(), "yyyy-MM-dd HH:mm:ss", "EEEE")
                    + " "
                    + DateUtil.datetimeTurnPattern(bean.getActivity()
                            .getActivityBeginTime(), "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"));
            if (DateUtil.compare2Dates(bean
                    .getActivity().getActivityEndTime(),DateUtil.datetimeToString(new Date(),null)) <= 0) {
                tvStatus.setText("已结束");
                tvStatus.setBackgroundResource(R.color.gray);
            } else {
                if (HMApiCampaign.STATUS_SIGN_UP_ING.equals(bean.getStatus())) {
                    tvStatus.setText("报名中");
                    tvStatus.setBackgroundResource(R.color.orange);
                } else if (HMApiCampaign.STATUS_SIGN_UP_ED.equals(bean
                        .getStatus())) {
                    tvStatus.setText("已通过");
                    tvStatus.setBackgroundResource(R.color.green);
                } else if (HMApiCampaign.STATUS_SIGN_UP_REVOKE.equals(bean
                        .getStatus())) {
                    tvStatus.setText("已撤销");
                    tvStatus.setBackgroundResource(R.color.gray);
                }
            }
            MImageLoader.display(this, bean.getActivity().getOrganization()
                    .getLogo(), ivLogo, true, MImageLoader.DEFAULT_ANGLE);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        llRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCampaignDetailAct.BUNDLE_KEY, bean.getActivity());
                HMNavUtil.goToNewAct(HMMyCampaignListAct.this, HMCampaignDetailAct.class, bundle);
            }
        });
    }

    @Override
    public void updateView() {
        try {
            if (datas != null) {
                adapter.setDatas(datas);
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    private int pageIndex = 1;
    private int pageSize = 20;
    private int totalCount;

    private HMUserBean user;

    public void loadData() {
        user =((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("accountId", user.getId());
        HMApiCampaign.getInstance().postMyList(this, params);
    }

    private void reload() {
        pageIndex = 1;
        loadData();
    }

    private void loadMore() {
        if (pageIndex * pageSize < totalCount) {
            loadData();
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiCampaign.MY_ACTIVITIES)) {
            List<HMCampaignSignUpBean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonArray(HMApi.KEY_DATA),
                    new TypeToken<List<HMCampaignSignUpBean>>() {
                    });
            if (pageIndex == 1) {
                datas = list;
            } else {
                datas.addAll(list);
            }
            updateView();
            pageIndex++;
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
        lvCampaigns.onRefreshComplete();
    }
}
