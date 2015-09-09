package cc.xuemiao.ui.main;

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
 * @author m
 */
public class HMIndexFrg extends HMBaseFragment {

    /**
     * 推荐活动
     */
    @Bind(R.id.index_vp_scroll_campains)
    ViewPager vpScrollCampaigns;

    @Bind(R.id.index_lv_new_campaign)
    NoScrollListView lvNewCampaigns;

    @Bind(R.id.index_gv_new_org)
    GridView gvNewOrgs;

    @Bind(R.id.index_rv_new_course)
    RecyclerView rvNewCourses;

    @Bind(R.id.index_cv_cursors)
    CursorView cvCursors;

    @Bind(R.id.index_rl_campaign)
    RelativeLayout rlCampaign;

    @Bind(R.id.index_rl_course)
    RelativeLayout rlCourse;

    @Bind(R.id.index_rl_org)
    RelativeLayout rlOrg;

    @Bind(R.id.index_sv)
    ScrollView sv;

    @Bind(R.id.index_iv_org_logo_in_recommend)
    ImageView ivOrgInRecommend;
    @Bind(R.id.index_iv_campaign_logo_in_recommend)
    ImageView ivCampaignInRecommend;
    @Bind(R.id.index_iv_course_logo_in_recommend)
    ImageView ivCourseInRecommend;
    @Bind(R.id.index_tv_org_name_in_recommend)
    TextView tvOrgInRecommend;
    @Bind(R.id.index_tv_campaign_name_in_recommend)
    TextView tvCampaignInRecommend;
    @Bind(R.id.index_tv_course_name_in_recommend)
    TextView tvCourseInRecommend;
    @Bind(R.id.index_rl_org_in_recommend)
    RelativeLayout rlOrgInRecommend;
    @Bind(R.id.index_rl_campaign_in_recommend)
    RelativeLayout rlCampaignInRecommend;
    @Bind(R.id.index_rl_course_in_recommend)
    RelativeLayout rlCourseInRecommend;
    @Bind(R.id.index_tv_notice)
    TextView tvNotice;
    @Bind(R.id.index_iv_advertising)
    ImageView ivAdvertising;
    @Bind(R.id.index_rl_new_course)
    RelativeLayout rlNewCourse;
    @Bind(R.id.index_ll_notice)
    LinearLayout llNotice;

    private CommonPagerAdapter<HMCampaignBean> scrollCampaignsAdapter;
    private List<HMCampaignBean> scrollCampainBeans = new ArrayList<HMCampaignBean>();

    private CommonAdapter<HMCampaignBean> newCampaignsAdapter;
    private List<HMCampaignBean> newCampainBeans = new ArrayList<HMCampaignBean>();

    private CommonAdapter<HMOrganizationBean> newOrgsAdapter;
    private List<HMOrganizationBean> newOrgBeans = new ArrayList<HMOrganizationBean>();

    private RecyclerAdapter<HMCourseBean> newCoursesAdapter;
    private List<HMCourseBean> newCourseBeans = new ArrayList<HMCourseBean>();

//    private List<NoticeBean> noticebeans = new ArrayList<NoticeBean>();

    private Timer timer;

    private HMRecommendBean recommend;

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
        scrollCampaignsAdapter = new CommonPagerAdapter<HMCampaignBean>(
                getActivity(), scrollCampainBeans,
                R.layout.adapter_index_sroll_campaign) {

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public void dealViews(ViewHolder holder, List<HMCampaignBean> datas,
                                  int position) {
                commendScrollCampaignViews(holder, datas, position);
            }

        };

        newCampaignsAdapter = new CommonAdapter<HMCampaignBean>(getActivity(),
                newCampainBeans, R.layout.adapter_index_new_campaign) {

            @Override
            public void dealViews(ViewHolder holder, List<HMCampaignBean> datas,
                                  int position) {
                commendNewCampaignViews(holder, datas, position);
            }
        };
        lvNewCampaigns.setAdapter(newCampaignsAdapter);

        newOrgsAdapter = new CommonAdapter<HMOrganizationBean>(getActivity(),
                newOrgBeans, R.layout.adapter_index_new_org) {

            @Override
            public void dealViews(ViewHolder holder,
                                  List<HMOrganizationBean> datas, int position) {
                commendNewOrgViews(holder, datas, position);
            }
        };
        gvNewOrgs.setAdapter(newOrgsAdapter);

        newCoursesAdapter = new RecyclerAdapter<HMCourseBean>(
                getActivity(), newCourseBeans,
                R.layout.adapter_index_new_course) {

            @Override
            public void onBindViewHolder(ViewHolder holder,
                                         List<HMCourseBean> datas, int position) {
                commendNewCourseViews(holder, datas, position);
            }
        };
        rvNewCourses.setHasFixedSize(false);
        NoScrollStaggeredGridLayoutManager mStaggeredGridLayoutManager = new
                NoScrollStaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);// 表示两列，并且是竖直方向的瀑布流
        rvNewCourses.setLayoutManager(mStaggeredGridLayoutManager);
        rvNewCourses.setAdapter(newCoursesAdapter);

        newTimer();

        MImageLoader.display(getActivity(), HMApi.HOST
                        + "fileroot/ad/advertising.png", ivAdvertising, false,
                R.mipmap.default_campaign_logo,
                R.mipmap.default_campaign_logo, true, 0);
    }

    private void newTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                carouselHandler.sendEmptyMessage(0);
            }
        }, 0, 3000);
    }

    private void setListener() {
        vpScrollCampaigns.setOnPageChangeListener(new OnPageChangeListener(
                vpScrollCampaigns) {

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == Integer.MAX_VALUE) {
                    vpScrollCampaigns.setCurrentItem(scrollCampainBeans.size() * 200);
                }
                currentPage = position;
                cvCursors.setSelectCursors(currentPage
                        % scrollCampainBeans.size());
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        tvNotice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v, long clickTime) {
//                if (noticebeans.size() > 0) {
//                    NoticeBean bean = noticebeans.get(0);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("nikname", bean.getNikname());
//                    bundle.putString("title", bean.getTitle());
//                    bundle.putString("date", bean.getDate());
//                    bundle.putString("content", bean.getContent());
//                    bundle.putString("courseName", bean.getCourseName());
//                    HMNavUtil.goToNewAct(HMIndexFrg.this.getActivity(),
//                            HMNoticeAct.class, bundle);
//                }
            }
        });

        rlCampaign.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putInt(HMCampaignDisplayAct.BUNDLE_KEY_TYPE,
                        HMCampaignDisplayAct.TYPE_ORG);
                HMNavUtil.goToNewAct(getActivity(), HMCampaignDisplayAct.class,
                        bundle);
            }
        });
        rlCourse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
            }
        });
        rlOrg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
            }
        });
        rlCampaignInRecommend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCampaignDetailAct.BUNDLE_KEY,
                        recommend.getActivity());
                HMNavUtil.goToNewAct(getActivity(), HMCampaignDetailAct.class,
                        bundle);
            }
        });
        rlCourseInRecommend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putString(HMCourseDetailAct.BUNDLE_KEY, recommend
                        .getCourse().getId());
                HMNavUtil.goToNewAct(getActivity(), HMCourseDetailAct.class,
                        bundle);
            }
        });
        rlOrgInRecommend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putString(HMOrgDetailAct.BUNDLE_KEY, recommend
                        .getOrganization().getId());
                HMNavUtil.goToNewAct(getActivity(), HMOrgDetailAct.class,
                        bundle);
            }
        });
    }

    private int currentPage;
    private Handler carouselHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (scrollCampainBeans.size() == 0) {
                return;
            }
            // if (currentPage >= scrollCampainBeans.size() - 1) {
            // currentPage = 0;
            // } else {
            // currentPage++;
            // }
            vpScrollCampaigns.setCurrentItem(currentPage, true);
            cvCursors.setSelectCursors(currentPage % scrollCampainBeans.size());
            currentPage++;
        }

        ;
    };

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

    protected void commendNewOrgViews(ViewHolder holder,
                                      List<HMOrganizationBean> datas, int position) {
        final HMOrganizationBean bean = datas.get(position);
        RelativeLayout rlRoot = holder.getViewById(R.id.index_new_org_rl_root,
                RelativeLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.index_new_org_iv_logo,
                ImageView.class);
        TextView tvName = holder.getViewById(R.id.index_new_org_tv_name,
                TextView.class);
        MImageLoader.display(getActivity(), bean.getLogo(), ivLogo,
                R.mipmap.default_campaign_logo, true,
                DensityUtil.dip2px(getActivity(), 20));
        tvName.setText(bean.getName());
        rlRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putString(HMOrgDetailAct.BUNDLE_KEY, "" + bean.getId());
                HMNavUtil.goToNewAct(getActivity(), HMOrgDetailAct.class,
                        bundle);
            }
        });
    }

    protected void commendNewCampaignViews(ViewHolder holder,
                                           List<HMCampaignBean> datas, int position) {
        final HMCampaignBean bean = datas.get(position);
        LinearLayout llRoot = holder.getViewById(
                R.id.index_new_campaign_ll_root, LinearLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.index_new_campaign_iv_logo,
                ImageView.class);
        TextView tvName = holder.getViewById(R.id.index_new_campaign_tv_name,
                TextView.class);
        TextView tvTimeInfo = holder.getViewById(
                R.id.index_new_campaign_tv_time_info, TextView.class);
        MImageLoader.display(getActivity(), bean.getLogo(), ivLogo,
                R.mipmap.default_campaign_logo_s, true, 0);
        tvName.setText(bean.getName());
        String st = DateUtil.datetimeTurnPattern(bean.getActivityBeginTime(),
                null, "EEEE MM.dd HH:mm");
        // String et = DateUtil.datetimeTurnPattern(bean.getActivityEndTime(),
        // null, "MM.dd HH:mm");
        String timeInfo = st;
        // String[] ets = et.split(" ");
        // if (st.split(" ")[0].equals(ets[0])) {
        // timeInfo = timeInfo + " - " + ets[ets.length - 1];
        // } else {
        // timeInfo = timeInfo + " - " + et;
        // }
        tvTimeInfo.setText(timeInfo);
        llRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCampaignDetailAct.BUNDLE_KEY, bean);
                HMNavUtil.goToNewAct(getActivity(), HMCampaignDetailAct.class,
                        bundle);
            }
        });
    }

    protected void commendScrollCampaignViews(ViewHolder holder,
                                              List<HMCampaignBean> datas, int position) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        final HMCampaignBean bean = datas.get(position % datas.size());
        ImageView ivLogo = holder.getViewById(
                R.id.index_sroll_campaign_iv_logo, ImageView.class);
        TextView tvTags = holder.getViewById(R.id.index_sroll_campaign_tv_tags,
                TextView.class);
        MImageLoader.display(getActivity(), bean.getLogo(), ivLogo, true,
                R.mipmap.default_campaign_logo,
                R.mipmap.default_campaign_logo, true, 0);
        tvTags.setText(bean.getTag().replace(",", " "));
        ivLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMCampaignDetailAct.BUNDLE_KEY, bean);
                HMNavUtil.goToNewAct(getActivity(), HMCampaignDetailAct.class,
                        bundle);
            }
        });
    }

    private boolean isLoading;

    @Override
    public void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        HMApiCourse.getInstance().getNewCourseList(this, null);
        HMApiCampaign.getInstance().getNewList(this, null);
        HMApiOrganization.getInstance().postNewList(this, null);
        HMApiRecommend.getInstance().getDetail(this, null);
//        HMApiNotice.getLastNotice(this, null);
    }

    private void updateCourseView() {
        if (newCourseBeans == null || newCourseBeans.size() == 0) {
            rlNewCourse.setVisibility(View.GONE);
        } else {
            rlNewCourse.setVisibility(View.VISIBLE);
        }
        newCourseBeans.add(0, new HMCourseBean());
        newCourseBeans.add(new HMCourseBean());
        newCoursesAdapter.setDatas(newCourseBeans);
        sv.scrollTo(0, 0);
    }

    private void updateCampaignView() {
        newCampaignsAdapter.setDatas(newCampainBeans);
        sv.scrollTo(0, 0);
    }

    private void updateOrgView() {
        newOrgsAdapter.setDatas(newOrgBeans);
    }

    private void updateScrollCampaignView() {
        cvCursors.init(getActivity(), scrollCampainBeans.size(),
                R.drawable.cursor_selector, 6, 6, 4);
        vpScrollCampaigns.setAdapter(scrollCampaignsAdapter);
        vpScrollCampaigns.setCurrentItem(scrollCampainBeans.size() * 200);
    }

    private void updateRecommendView(HMRecommendBean recommend) {
        MImageLoader.displayWithDefaultOptions(getActivity(), recommend
                .getOrganization().getLogo(), ivOrgInRecommend);
        MImageLoader.displayWithDefaultOptions(getActivity(), recommend
                .getActivity().getLogo(), ivCampaignInRecommend);
        MImageLoader.displayWithDefaultOptions(getActivity(), recommend
                .getCourse().getLogo(), ivCourseInRecommend);
        tvCampaignInRecommend.setText(recommend.getActivity().getName());
        tvCourseInRecommend.setText(recommend.getCourse().getName());
        tvOrgInRecommend.setText(recommend.getOrganization().getName());
    }

    private void updateNoticeView() {
//        String text = "暂无";
//        if (noticebeans.size() > 0) {
//            text = "【系统通知】" + noticebeans.get(0).getTitle();
//        }
//        tvNotice.setText(text);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        try {
            if (url.equals(HMApiCourse.NEW_COURSE_LIST)) {
                newCourseBeans = GsonUtil.fromJsonArr(
                        jo.getAsJsonArray(HMApi.KEY_DATA),
                        new TypeToken<List<HMCourseBean>>() {
                        });
                updateCourseView();
            } else if (url.equals(HMApiCampaign.NEW_ACTIVITY_LIST)) {
                newCampainBeans = GsonUtil.fromJsonArr(
                        jo.getAsJsonArray(HMApi.KEY_DATA),
                        new TypeToken<List<HMCampaignBean>>() {
                        });
                int length = newCampainBeans.size() > 3 ? 3 : newCampainBeans
                        .size();
                scrollCampainBeans.removeAll(scrollCampainBeans);
                for (int i = 0; i < length; i++) {
                    scrollCampainBeans.add(newCampainBeans.get(i));
                }
                updateCampaignView();
                updateScrollCampaignView();
            } else if (url.equals(HMApiOrganization.NEW_LIST)) {
                newOrgBeans = GsonUtil.fromJsonArr(
                        jo.getAsJsonArray(HMApi.KEY_DATA),
                        new TypeToken<List<HMOrganizationBean>>() {
                        });
                updateOrgView();
            } else if (url.equals(HMApiRecommend.DETAIL)) {
                recommend = GsonUtil
                        .fromJsonObj(jo.getAsJsonObject(HMApi.KEY_DATA),
                                HMRecommendBean.class);
                updateRecommendView(recommend);
//            } else if (url.equals(HMApiNotice.GET_LAST)) {
//                noticebeans = GsonUtil.fromJsonArr(
//                        jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(
//                                HMApi.KEY_LIST),
//                        new TypeToken<List<NoticeBean>>() {
//                        });
//                updateNoticeView();
            }
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
        isLoading = false;
    }

    private String mPageName = "HMIndexFrg";

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        if (timer == null) {
            newTimer();
        }
    }


}
