package cc.xuemiao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCourse;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 课程列表
 *
 * @author m
 */
public class HMCourseListAct extends HMBaseAct {

    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final int TYPE_TEACHER = 1;
    public static final int TYPE_ORGAN = 0;

    @Bind(R.id.course_list_ptrlv_courses)
    PullToRefreshListView ptrlvCourses;

    private String id;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_course_list);
        init();
        initListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        id = bundle.getString(BUNDLE_KEY_ID);
        type = bundle.getInt(BUNDLE_KEY_TYPE);
    }

    private void init() {
        hvHeadView.setTitle("课程列表");
        adapter = new CommonAdapter<HMCourseBean>(this, courseBeans, R.layout.adapter_index_new_course) {
            @Override
            public void dealViews(ViewHolder holder, List<HMCourseBean> datas, int position) {
                commondCoursesAdapterViews(holder, datas, position);
            }
        };
        ptrlvCourses.setAdapter(adapter);
    }

    private void commondCoursesAdapterViews(ViewHolder holder, List<HMCourseBean> datas, int position) {
        final HMCourseBean bean = datas.get(position);
        RelativeLayout rlRoot = holder.getViewById(
                R.id.index_new_course_rl_root, RelativeLayout.class);
        ImageView ivLogo = holder.getViewById(R.id.index_new_course_iv_logo,
                ImageView.class);
        ImageView ivType = holder.getViewById(R.id.index_new_course_iv_type,
                ImageView.class);
        TextView tvName = holder.getViewById(R.id.index_new_course_tv_name,
                TextView.class);
        TextView tvOrgName = holder.getViewById(
                R.id.index_new_course_tv_org_name, TextView.class);
        TextView tvBrief = holder.getViewById(R.id.index_new_course_tv_brief,
                TextView.class);
        LinearLayout llBrief = holder.getViewById(
                R.id.index_new_course_ll_brief, LinearLayout.class);
        MImageLoader.display(this, bean.getLogo(), ivLogo, 0, true, 0);
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
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(HMCourseDetailAct.BUNDLE_KEY,
                        "" + bean.getId());
                HMNavUtil.goToNewAct(HMCourseListAct.this,
                        HMCourseDetailAct.class, bundle);
            }
        });
    }

    @Override
    public void updateView() {
        adapter.setDatas(courseBeans);
    }

    private void initListener() {
        ptrlvCourses.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                reloadData();
            }
        });

        ptrlvCourses
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        loadMore();
                    }
                });
    }


    private List<HMCourseBean> courseBeans;
    private CommonAdapter<HMCourseBean> adapter;

    @Override
    public void loadData() {
        if (id == null) {
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("pageIndex", adapter.getPageIndex());
        params.put("pageSize", adapter.getPageSize());
        if (type == TYPE_ORGAN) {
            params.put("organizationId", id);
        } else if (type == TYPE_TEACHER) {
            params.put("teacherId", id);
        }
        HMApiCourse.getInstance().postMyList(this, params);
    }

    private void reloadData() {
        adapter.setPageIndex(1);
        loadData();
    }

    private void loadMore() {
        if (adapter.hasMorePage()) {
            loadData();
            return;
        }
        ToastUtil.toastAlways(this, "没有更多了哦！");
    }

    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiCourse.MY_LIST)) {
            List<HMCourseBean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(
                            HMApi.KEY_LIST),
                    new TypeToken<List<HMCourseBean>>() {
                    });
            adapter.setTotalSize(jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonPrimitive(
                    HMApi.KEY_TOTAL_COUNT).getAsInt());

            if (adapter.isFirstPage()) {
                courseBeans = list;
            } else {
                courseBeans.addAll(list);
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
        hideLoading();
        ptrlvCourses.onRefreshComplete();
    }
}
