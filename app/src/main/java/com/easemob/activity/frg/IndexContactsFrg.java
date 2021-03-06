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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.api.HMApiChat;
import com.easemob.exceptions.EaseMobException;
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
 * 联系人
 *
 * @author loar
 */
public class IndexContactsFrg extends HMBaseFragment {

    @Bind(R.id.chat_index_contacts_lv_contacts)
    ListView lvContacts;

    private List<String> names;
    private CommonAdapter<String> adapter;

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_frg_index_contacts, null);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(rootView, inflater, container, savedInstanceState);
        init();
        setListener();
        updateView();
        loadData();
    }

    private void init() {
        try {
            names = HMApiChat.getInstance().userList();
            adapter = new CommonAdapter<String>(getActivity(), names, R.layout.chat_adapter_index_contacts) {

                @Override
                public void dealViews(ViewHolder holder, List<String> datas, int position) {
                    dealAdapterViews(holder, datas, position);
                }
            };
            lvContacts.setAdapter(adapter);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    private void setListener() {
    }

    private void dealAdapterViews(ViewHolder holder, List<String> datas, int position) {
        String bean = datas.get(position);
        try {
            TextView tvName = holder.getViewById(R.id.chat_index_contacts_tv_name, TextView.class);
            tvName.setText(bean);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
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
