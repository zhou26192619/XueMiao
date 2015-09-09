package cc.xuemiao.ui.campaign;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.easemob.util.NetUtils;
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
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCampaign;
import cc.xuemiao.bean.HMCampaignBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;
import cc.xuemiao.utils.LocationUtil;

/**
 * 活动列表陈列
 *
 * @author loar
 */
public class HMCampaignDisplayAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "Id";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final int TYPE_NEAR = 0;
    public static final int TYPE_ORG = 1;

    @Bind(R.id.campaign_display_lv_campaigns)
    PullToRefreshListView lvCampaigns;

    private List<HMCampaignBean> datas = new ArrayList<HMCampaignBean>();

    private CommonAdapter<HMCampaignBean> adapter;

    /**
     * 附近机构列表
     */
    private ArrayList<HMCampaignBean> cIds;
    private String id;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_campaign_display);
        init();
        setListeners();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        id = bundle.getString(BUNDLE_KEY);
        type = bundle.getInt(BUNDLE_KEY_TYPE);
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
        hvHeadView.setTitle("活动列表");
        adapter = new CommonAdapter<HMCampaignBean>(this, datas,
                R.layout.adapter_campaign_display) {

            @Override
            public void dealViews(ViewHolder holder, List<HMCampaignBean> datas,
                                  int position) {
                commondAdapterViews(holder, datas, position);
            }
        };
        lvCampaigns.setAdapter(adapter);

        if (NetUtils.hasNetwork(this) && !NetUtils.isWifiConnection(this)) {
            ToastUtil.toastAlways(this, "小苗发现您当前处于非wifi网络环境下，继续使用可能产生流量");
        }
    }

    protected void commondAdapterViews(ViewHolder holder,
                                       List<HMCampaignBean> datas, int position) {
        final HMCampaignBean bean = datas.get(position);
        LinearLayout llRoot = holder.getViewById(R.id.campaign_display_ll_root,
                LinearLayout.class);
        TextView tvOrgName = holder.getViewById(
                R.id.campaign_display_tv_org_name, TextView.class);
        TextView tvTags = holder.getViewById(R.id.campaign_display_tv_tags,
                TextView.class);
        TextView tvTitle = holder.getViewById(R.id.campaign_display_tv_title,
                TextView.class);
        TextView tvTimeInfo = holder.getViewById(
                R.id.campaign_display_tv_time_info, TextView.class);
        TextView tvSignUpNum = holder.getViewById(
                R.id.campaign_display_tv_sign_up_number, TextView.class);
        TextView tvSurplusNum = holder.getViewById(
                R.id.campaign_display_tv_surplus_number, TextView.class);
        TextView tvDistance = holder.getViewById(
                R.id.campaign_display_tv_distance, TextView.class);
        ImageView ivType = holder.getViewById(R.id.campaign_display_iv_type,
                ImageView.class);
        ImageView ivLogo = holder.getViewById(R.id.campaign_display_iv_logo,
                ImageView.class);
        try {
            String mark = StringUtil.isEmpty(bean.getLandmark()) ? "" : bean
                    .getLandmark() + " | ";
            tvTitle.setText(mark + bean.getName());
            tvTimeInfo.setText(DateUtil.datetimeTurnPattern(
                            bean.getActivityBeginTime(), "yyyy-MM-dd HH:mm:ss", "EEEE MM.dd")
//                    + " "
//                    + DateUtil.datetimeTurnPattern(bean.getActivityBeginTime(),
//                    "yyyy-MM-dd HH:mm:ss", "MM.dd")
            );
            MImageLoader.display(this, bean.getLogo(), ivLogo,
                    R.mipmap.default_campaign_logo,
                    R.mipmap.default_campaign_logo, true, 0);
            if (HMApiCampaign.TYPE_CAMPAIN_FEE_FREE.equals(bean
                    .getActivityType())) {
                ivType.setImageResource(R.mipmap.pay_free);
                ivType.setVisibility(View.VISIBLE);
            } else {
                ivType.setVisibility(View.GONE);
            }
            tvTags.setText(bean.getTag().replace(",", " "));
            tvSignUpNum.setText(bean.getSignupNum());
            tvOrgName.setText(bean.getOrganization().getName());
            tvSurplusNum
                    .setText(Integer.valueOf(bean.getMemberNum() == null ? "0"
                            : bean.getMemberNum())
                            - Integer.valueOf(bean.getActivityPassNum()) + "");
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        llRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCampaignDetailAct.BUNDLE_KEY, bean);
                HMNavUtil.goToNewAct(HMCampaignDisplayAct.this,
                        HMCampaignDetailAct.class, bundle);
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

    private int pageIndex = 0;
    private int pageSize = 20;
    private int totalCount;

    public void loadData() {
        showLoading();
        if (type == TYPE_NEAR) {
            LocationUtil.getPositionInfoByGaode(this, 1000, new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
                        LocationUtil.stopGaodeLocation();
                    }
                }

                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (type == TYPE_ORG) {
            RequestParams params = new RequestParams();
            params.put("organizationId", id);
            HMApiCampaign.getInstance().postListByOrganizationId(this, params);
        }
    }

    private void loadCampains() {
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < cIds.size(); i++) {
            ids.append(cIds.get(i).getId() + ",");
        }
        ids.deleteCharAt(ids.length() - 1);
        RequestParams params = new RequestParams();
        params.put("activityIds", ids.toString());
        HMApiCampaign.getInstance().postListByOrganizationId(this, params);
    }

    private void reload() {
        pageIndex = 0;
        loadData();
    }

    private void loadMore() {
        if (pageIndex * (pageSize + 1) < totalCount) {
            loadData();
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        try {
            List<HMCampaignBean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonArray(HMApi.KEY_DATA),
                    new TypeToken<List<HMCampaignBean>>() {
                    });
            if (pageIndex == 0) {
                datas = list;
            } else {
                datas.addAll(list);
            }
            if (url.equals(HMApiCampaign.LIST_BY_ACTIVITY_IDS)) {
                pageIndex++;
            } else if (url.equals(HMApiCampaign.LIST_BY_ORGANIZATION_ID)) {

            }
            updateView();
        } catch (Exception e) {
            ToastUtil.printErr(e);
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

    @Override
    public void onPause() {
        super.onPause();
        LocationUtil.stopGaodeLocation();
    }
}
