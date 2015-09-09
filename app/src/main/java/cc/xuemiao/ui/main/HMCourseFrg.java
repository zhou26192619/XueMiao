package cc.xuemiao.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiStudent;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.bean.HMStudentBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseFragment;
import cc.xuemiao.ui.HMMyCourseListAct;
import cc.xuemiao.ui.campaign.HMMyCampaignListAct;
import cc.xuemiao.ui.login_register.HMRegisterChildMaterialAct;
import cc.xuemiao.ui.view.IndexLastInfoView;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 首页
 *
 * @author m
 */
public class HMCourseFrg extends HMBaseFragment {
    @Bind(R.id.index_lv_students)
    LinearLayout lvStudents;
    @Bind(R.id.frg_course_ll_my_campaign)
    LinearLayout llMyCampaign;
    @Bind(R.id.frg_course_ll_my_course)
    LinearLayout llMyCourse;
    @Bind(R.id.frg_course_ptrsv)
    PullToRefreshScrollView ptrsv;

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToastUtil.log("frg", "HMCourseFrg");
        return inflater.inflate(R.layout.frg_course, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(rootView,inflater,container,savedInstanceState);
        init();
        initListener();
        updateView();
        loadData();
    }

    private void init() {
        // 上拉、下拉设定
        ptrsv.setMode(Mode.PULL_FROM_START);
        // 下拉监听函数
        ptrsv.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {

            }
        });
    }

    /**
     * 更新显示界面
     */
    public void updateView() {
        try {
            lvStudents.removeAllViews();
            if (studentBeans != null && studentBeans.size() > 0) {
                // adapter.setData();
                for (HMStudentBean bean : studentBeans) {
                    View view = new IndexLastInfoView(getContext(),
                            bean).getView();
                    lvStudents.addView(view);
                }
            } else {
                // 一个小孩也没有
                TextView textView = new TextView(getActivity());
                lvStudents.addView(textView);
                textView.setText("点击添加小孩");
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(45);
                textView.setHeight(DensityUtil.dip2px(getActivity(), 128));
                textView.setBackgroundResource(R.drawable.orange_dark_straight_corner_selector);
                textView.setPadding(DensityUtil.dip2px(getActivity(), 15), 0,
                        0, 0);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (((HMApp)getActivity().getApplication()).getUser() == null) {
                            return;
                        }
                        ;
                        Bundle bundle = new Bundle();
                        bundle.putInt(
                                HMRegisterChildMaterialAct.BUNDLE_KEY_TYPE,
                                HMRegisterChildMaterialAct.TYPE_ADD);
                        HMNavUtil.goToNewAct(getActivity(),
                                HMRegisterChildMaterialAct.class, bundle);
                    }
                });
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }

    }

    private void initListener() {
    }

    private List<HMStudentBean> studentBeans;
    /**
     * 当前显示的课程
     */
    protected HMCourseBean currentCourse;
    private HMUserBean user;
    private boolean isLoading;

    public void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        user = ((HMApp)getActivity().getApplication()).getUserSP().getUserBean(HMUserBean.class);
        if (user == null) {
            ptrsv.onRefreshComplete();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("parentId", user.getRoleId());
        params.put("size", 8);
        HMApiStudent.getInstance().postListByParentId(this, params);
    }

    @OnClick(R.id.frg_course_ll_my_campaign)
    public void onMyCampaign(View view) {
        user = ((HMApp)getActivity().getApplication()).getUser();
        if (user == null) {
            return;
        }
        HMNavUtil.goToNewAct(getActivity(), HMMyCampaignListAct.class);
    }

    @OnClick(R.id.frg_course_ll_my_course)
    public void onMyCourse(View view) {
        user = ((HMApp)getActivity().getApplication()).getUser();
        if (user == null) {
            return;
        }
        HMNavUtil.goToNewAct(getActivity(), HMMyCourseListAct.class);
    }

    public void setRequestEndSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        isLoading = false;
        ptrsv.onRefreshComplete();
        try {
            studentBeans = GsonUtil.fromJsonArr(
                    jo.getAsJsonArray(HMApi.KEY_DATA),
                    new TypeToken<List<HMStudentBean>>() {
                    });
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        updateView();
    }

    public void setRequestEndNotSuc(String url, int statusCode,
                                    Header[] headers, JsonObject jo) {
        isLoading = false;
        ptrsv.onRefreshComplete();
        if (!"2001".equals(jo.getAsJsonPrimitive(HMApi.KEY_STATUS).getAsString())) {
            ToastUtil.toastAlways(getActivity(), jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
        }
    }

    public void setRequestEndErr(String url, int statusCode, Header[] headers,
                                 String str, Throwable throwable) {
        isLoading = false;
        ptrsv.onRefreshComplete();
        ToastUtil.toastAlways(getActivity(), str);
    }

    private String mPageName = "HMCourseFrg";

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
}
