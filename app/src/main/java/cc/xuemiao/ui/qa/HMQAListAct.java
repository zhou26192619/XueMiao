package cc.xuemiao.ui.qa;


import android.graphics.Color;
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
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
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
 * 课程列表
 *
 * @author m
 */
public class HMQAListAct extends HMBaseAct {

    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_TYPE = "type";


    @Bind(R.id.common_head_tv_questions)
    TextView tvQuestions;

    @Bind(R.id.qa_list_tv_all)
    TextView tvAll;

    @Bind(R.id.qa_list_tv_mine)
    TextView tvMine;

    @Bind(R.id.qa_list_ptrlv_courses)
    PullToRefreshListView ptrlvCourses;

    private String id;
    private String orgName = "官方";

    private List<HMQABean> QABeans;
    private List<HMQABean> myQABeans;
    private List<HMQABean> otherQABeans;

    private CommonAdapter<HMQABean> adapter;

    private int state = R.id.qa_list_tv_all;

    private HMUserBean user;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_qa_list);
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
                R.layout.adapter_qa_list) {

            @Override
            public void dealViews(ViewHolder holder, List<HMQABean> datas,
                                  int position) {
                commendAdapterViews(holder, datas, position);
            }
        };
        ptrlvCourses.setAdapter(adapter);
    }

    protected void commendAdapterViews(ViewHolder holder, List<HMQABean> datas,
                                       int position) {
        HMQABean bean = datas.get(position);
        ImageView ivLogo = holder.getViewById(R.id.qa_list_iv_logo,
                ImageView.class);
        TextView tvReply = holder.getViewById(R.id.qa_list_tv_reply,
                TextView.class);
        TextView tvContent = holder.getViewById(R.id.qa_list_tv_content,
                TextView.class);
        TextView tvUserName = holder.getViewById(R.id.qa_list_tv_user_name,
                TextView.class);
        TextView tvDateTime = holder.getViewById(R.id.qa_list_tv_datetime,
                TextView.class);
        try {
            MImageLoader.display(this, bean.getParent().getLogo(), ivLogo,
                    true, MImageLoader.DEFAULT_ANGLE);
            if (!StringUtil.isEmpty(bean.getAnswerContent())) {
                StringUtil.setDifferentFontTextView(tvReply, orgName,
                        Color.parseColor("#ff9600"), 14,
                        " 回复:" + bean.getAnswerContent(),
                        Color.parseColor("#848884"), 14, this);
            }
            tvContent.setText(bean.getQuestionContent());
            tvUserName.setText(bean.getParent().getName());
            tvDateTime.setText(bean.getCreateDate());
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    private void initListener() {
        ptrlvCourses.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
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

    @Override
    public void loadData() {
        user = ((HMApp) getApplication()).getUser();
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
        if (state == R.id.qa_list_tv_all) {
            HMApiQA.getInstance().postList(this, params);
        } else if (state == R.id.qa_list_tv_mine) {
            HMApiQA.getInstance().postMyList(this, params);
        }
        showLoading();
    }

    private void loadMore() {
        if (adapter.hasMorePage()) {
            load();
            return;
        }
        ToastUtil.toastAlways(this, "没有更多了哦！");
    }

    @Override
    public void updateView() {
        adapter.setDatas(QABeans);
    }

    public void onAll(View v) {
        changeTitleState(v.getId());
    }

    public void onMine(View v) {
        changeTitleState(v.getId());
    }

    public void onQuestions(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(HMNewQuestionAct.BUNDLE_KEY_ID, id);
        HMNavUtil.goToNewAct(this, HMNewQuestionAct.class, bundle);
    }

    private void changeTitleState(int id) {
        if (state == id) {
            return;
        }
        state = id;
        adapter.resetPageIndex();
        if (id == R.id.qa_list_tv_all) {
            tvAll.setBackgroundResource(R.drawable.white_half_corner_left);
            tvMine.setBackgroundResource(R.drawable.white_light_half_corner_right);
            tvMine.setTextColor(Color.parseColor("#ffffff"));
            tvAll.setTextColor(Color.parseColor("#00c92a"));
            if (otherQABeans != null && adapter.isFirstPage()) {
                QABeans = otherQABeans;
                updateView();
                ptrlvCourses.onRefreshComplete();
                return;
            }
        } else if (id == R.id.qa_list_tv_mine) {
            tvAll.setBackgroundResource(R.drawable.white_light_half_corner_left);
            tvMine.setBackgroundResource(R.drawable.white_half_corner_right);
            tvAll.setTextColor(Color.parseColor("#ffffff"));
            tvMine.setTextColor(Color.parseColor("#00c92a"));
            if (myQABeans != null && adapter.isFirstPage()) {
                QABeans = myQABeans;
                updateView();
                ptrlvCourses.onRefreshComplete();
                return;
            }
        }
        loadData();
    }

    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        hideLoading();
        ptrlvCourses.onRefreshComplete();
        if (url.equals(HMApiQA.LIST)
                || url.equals(HMApiQA.MY_LIST)) {
            List<HMQABean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(
                            HMApi.KEY_LIST), new TypeToken<List<HMQABean>>() {
                    });
            adapter.setTotalSize(jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonPrimitive(
                    HMApi.KEY_TOTAL_COUNT).getAsInt());
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
        ptrlvCourses.onRefreshComplete();
        hideLoading();
    }

}
