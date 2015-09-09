package com.easemob.activity.frg;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.CommonPagerAdapter;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.listener.OnClickListener;
import com.lib_common.listener.OnPageChangeListener;
import com.lib_common.util.DateUtil;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.NoScrollListView;
import com.lib_common.widgt.NoScrollStaggeredGridLayoutManager;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCampaign;
import cc.xuemiao.api.HMApiCourse;
import cc.xuemiao.api.HMApiOrganization;
import cc.xuemiao.api.HMApiRecommend;
import cc.xuemiao.bean.HMCampaignBean;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.bean.HMOrganizationBean;
import cc.xuemiao.bean.HMRecommendBean;
import cc.xuemiao.ui.HMBaseFragment;
import cc.xuemiao.ui.HMCourseDetailAct;
import cc.xuemiao.ui.HMOrgDetailAct;
import cc.xuemiao.ui.campaign.HMCampaignDetailAct;
import cc.xuemiao.ui.campaign.HMCampaignDisplayAct;
import cc.xuemiao.ui.view.CursorView;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 首页
 *
 * @author loar
 */
public class IndexContactsFrg extends HMBaseFragment {

    @Bind(R.id.index_ll_notice)
    LinearLayout llNotice;

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_index, null);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(rootView,inflater,container,savedInstanceState);
        init();
        setListener();
        updateView();
        loadData();
    }

    private void init() {


    }


    private void setListener() {
    }

    protected void commendNewCourseViews(ViewHolder holder,
                                         List<HMCourseBean> datas, int position) {
        final HMCourseBean bean = datas.get(position);
        RelativeLayout rlRoot = holder.getViewById(
                R.id.index_new_course_rl_root, RelativeLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.index_new_course_iv_logo,
                ImageView.class);
        TextView tvName = holder.getViewById(R.id.index_new_course_tv_name,
                TextView.class);
        TextView tvOrgName = holder.getViewById(
                R.id.index_new_course_tv_org_name, TextView.class);
        TextView tvBrief = holder.getViewById(R.id.index_new_course_tv_brief,
                TextView.class);
        LinearLayout llBrief = holder.getViewById(
                R.id.index_new_course_ll_brief, LinearLayout.class);
        MImageLoader.display(getActivity(), bean.getLogo(), ivLogo, 0, true, 0);
        tvName.setText(bean.getName());
        tvOrgName.setText(bean.getOrganizationName());
        if (bean.getSummary() == null) {
            llBrief.setVisibility(View.GONE);
        } else {
            llBrief.setVisibility(View.VISIBLE);
            tvBrief.setText(bean.getSummary());
        }
        rlRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putString(HMCourseDetailAct.BUNDLE_KEY,
                        "" + bean.getId());
                HMNavUtil.goToNewAct(getActivity(), HMCourseDetailAct.class,
                        bundle);
            }
        });
    }


    @Override
    public void loadData() {
    }



    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        try {
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(getActivity(), jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(getActivity(), str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
    }
}
