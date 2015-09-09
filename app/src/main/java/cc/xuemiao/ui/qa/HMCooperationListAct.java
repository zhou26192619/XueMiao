package cc.xuemiao.ui.qa;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiQA;
import cc.xuemiao.api.HMApiSearch;
import cc.xuemiao.bean.HMQABean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 互助问答搜索列表
 *
 * @author loar
 */
public class HMCooperationListAct extends HMBaseAct {

    public static final String BUNDLE_KEY_KEYWORDS = "keywords";
    public static final String BUNDLE_KEY_TYPE = "type";

    @Bind(R.id.cooperation_list_tv_search)
    TextView tvSearch;

    @Bind(R.id.cooperation_list_et_search)
    EditText etSearch;

    @Bind(R.id.cooperation_list_ptrlv_list)
    PullToRefreshListView lvList;

    private CommonAdapter<HMQABean> adapter;
    private String keywords;
    private int questionType;
    private List<HMQABean> QABeans;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.act_cooperation_list);
        init();
        initListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        questionType = bundle.getInt(BUNDLE_KEY_TYPE, HMApiQA.TYPE_COOPERATION);
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
                HMNavUtil.goToNewAct(HMCooperationListAct.this,
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
        if (StringUtil.isEmpty(keywords)) {
            return;
        }
        adapter.resetPageIndex();
        load();
    }

    private void loadMore() {
        if (adapter.hasMorePage(adapter.getTotalSize())) {
            load();
            return;
        }
        ToastUtil.toastAlways(this, "没有更多了哦！");
    }

    private void load() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", adapter.getPageIndex());
        params.put("pageSize", adapter.getPageSize());
        params.put("keywords", keywords);
        params.put("questionType", questionType);
        HMApiSearch.getInstance().postSearchQuestions(this, params);
    }

    @Override
    public void updateView() {
        adapter.setDatas(QABeans);
    }

    public void onSearch(View v) {
        keywords = etSearch.getText().toString();
        if (StringUtil.isEmpty(keywords)) {
            ToastUtil.toastAlways(this, "什么都不写,这可为难死小苗了啊!");
            return;
        }
        showLoading();
        loadData();
    }

    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiSearch.SEARCH_QUESTION)) {
            List<HMQABean> list = null;
            if (jo.has(HMApi.KEY_DATA)) {
                list = GsonUtil.fromJsonArr(jo.getAsJsonObject(HMApi.KEY_DATA)
                                .getAsJsonArray(HMApi.KEY_LIST),
                        new TypeToken<List<HMQABean>>() {
                        });
                adapter.setTotalSize(list.size());
            }
            if (adapter.isFirstPage()) {
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
