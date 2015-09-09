package cc.xuemiao.ui.main;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiQA;
import cc.xuemiao.bean.HMQABean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseFragment;
import cc.xuemiao.ui.qa.HMCooperationDetailAct;
import cc.xuemiao.ui.qa.HMCooperationListAct;
import cc.xuemiao.ui.view.dialog.EditDialog;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 互助问答模块
 *
 * @author loar
 */
public class HMCooperationFrg extends HMBaseFragment {

    @Bind(R.id.cooperation_iv_for_help)
    ImageView ivForHelp;

    @Bind(R.id.cooperation_iv_list)
    PullToRefreshListView lvList;

    @Bind(R.id.cooperation_tv_type)
    TextView tvType;

    @Bind(R.id.common_main_head_iv_search)
    ImageView ivSearch;

    private CommonAdapter<HMQABean> adapter;
    private List<HMQABean> datas;

    private EditDialog dialog;

    private PopupWindow popupWindow;

    private ArrayList<String> types;
    private String type;
    private final String TYPE_NAME_ALL = "全部";
    private final String TYPE_NAME_MINE = "我的";

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToastUtil.log("frg", "HMCooperationFrg");
        return inflater.inflate(R.layout.frg_cooperation, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(rootView,inflater,container,savedInstanceState);
        init();
        loadData();
    }

    private void init() {
        MImageLoader.displayWithDefaultOptions(getActivity(), ""
                + R.mipmap.ask, ivForHelp);
        adapter = new CommonAdapter<HMQABean>(getActivity(), datas,
                R.layout.adapter_cooperation) {

            @Override
            public void dealViews(ViewHolder holder, List<HMQABean> datas,
                                  int position) {
                LinearLayout llRoot = holder.getViewById(
                        R.id.cooperation_ll_root, LinearLayout.class);
                ImageView ivLogo = holder.getViewById(R.id.cooperation_iv_logo,
                        ImageView.class);
                TextView tvContent = holder.getViewById(
                        R.id.cooperation_tv_content, TextView.class);
                TextView tvDatetime = holder.getViewById(
                        R.id.cooperation_tv_datetime, TextView.class);
                TextView tvName = holder.getViewById(R.id.cooperation_tv_name,
                        TextView.class);
                final HMQABean bean = datas.get(position);
                try {
                    tvContent.setText(bean.getQuestionContent());
                    tvDatetime.setText(bean.getCreateDate());
                    tvName.setText(bean.getAccount().getNikName());
                    MImageLoader.displayWithDefaultOptions(getActivity(), bean
                            .getAccount().getParent().getLogo(), ivLogo);
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
                llRoot.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(
                                HMCooperationDetailAct.BUNDLE_KEY, bean);
                        HMNavUtil.goToNewAct(getActivity(),
                                HMCooperationDetailAct.class, bundle);
                    }
                });
            }
        };
        lvList.setAdapter(adapter);
        lvList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                loadMore();
            }
        });
        lvList.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        types = new ArrayList<String>();
        types.add(TYPE_NAME_ALL);
        types.add(TYPE_NAME_MINE);
        tvType.setText(types.get(0));
        type = types.get(0);
        tvType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showWindow(tvType);
            }
        });
    }

    private void showWindow(View parent) {
        if (popupWindow == null) {
            ListView listView = new ListView(getActivity());
            listView.setBackgroundResource(R.drawable.orange_border_rounded_corner);
            listView.setAdapter(new CommonAdapter<String>(getActivity(), types,
                    R.layout.adapter_dialog_spinner) {

                @Override
                public void dealViews(ViewHolder holder, List<String> datas,
                                      int position) {
                    final String bean = datas.get(position);
                    TextView tvName = holder.getViewById(
                            R.id.dialog_spinner_tv_name, TextView.class);
                    LinearLayout llRoot = holder.getViewById(
                            R.id.dialog_spinner_ll_root, LinearLayout.class);
                    tvName.setText(bean);
                    llRoot.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            tvType.setText(bean);
                            type = bean;
                            popupWindow.dismiss();
                            loadData();
                        }
                    });
                }
            });
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            listView.setLayoutParams(params);
            LinearLayout.LayoutParams llP = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setLayoutParams(llP);
            ll.addView(listView);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(ll, DensityUtil.dip2px(getActivity(),
                    70), LayoutParams.WRAP_CONTENT);
            // 使其聚集
            popupWindow.setFocusable(true);
            // 设置允许在外点击消失
            popupWindow.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindow.showAsDropDown(parent, 0, 10);
    }

    @Override
    public void loadData() {
        super.loadData();
        if (adapter.isRequesting()) {
            return;
        }
        adapter.setIsRequesting(true);
        adapter.resetPageIndex();
        load();
    }

    private void loadMore() {
        if (adapter.hasMorePage(adapter.getTotalSize())) {
            loadData();
            return;
        }
        ToastUtil.toastAlways(getActivity(), "没有更多了哦！");
    }

    private void load() {
        RequestParams params = new RequestParams();
        params.put("pageSize", adapter.getPageSize());
        params.put("pageIndex", adapter.getPageIndex());
        params.put("questionType", 2);
        if (type.equals(TYPE_NAME_ALL)) {
        } else if (type.equals(TYPE_NAME_MINE)) {
            HMUserBean user = ((HMApp) getActivity().getApplication()).getUser();
            if (user == null) {
                return;
            }
            params.put("qAccountId", user.getId());
        }
    }

    public void onForHelp(View v) {
        final HMUserBean user = ((HMApp) getActivity().getApplication()).getUser();
        if (user == null) {
            return;
        }
        if (dialog == null) {
            dialog = new EditDialog(getActivity(), R.style.toast_box_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnEditClickListener(new EditDialog.OnEditClickListener() {

                @Override
                public void ok(String text, View v) {
                    if (StringUtil.isEmpty(text)) {
                        ToastUtil
                                .toastAlways(getActivity(), "什么都没写，别人很难帮到你的哦!");
                        return;
                    }
                    RequestParams params = new RequestParams();
                    params.put("qAccountId", user.getId());
                    params.put("content", text);
                    params.put("questionType", HMApiQA.TYPE_COOPERATION);
                    dialog.dismiss();
                }

                @Override
                public void cancel(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    public void onSearch(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt(HMCooperationListAct.BUNDLE_KEY_TYPE,
                HMApiQA.TYPE_COOPERATION);
        HMNavUtil.goToNewAct(getActivity(), HMCooperationListAct.class, bundle);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiQA.LIST)
                || url.equals(HMApiQA.MY_LIST)) {
            List<HMQABean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonObject(HMApi.KEY_DATA).getAsJsonArray(
                            HMApi.KEY_LIST), new TypeToken<List<HMQABean>>() {
                    });
            adapter.setTotalSize(list.size());
            if (adapter.isFirstPage()) {
                datas = list;
            } else {
                datas.addAll(list);
            }
            adapter.nextPage();
            adapter.setDatas(datas);
        } else if (url.equals(HMApiQA.NEW_QUESTION)) {
            ToastUtil.toastAlways(getActivity(), jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
            dialog.setEditText(null);
            loadData();
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
        lvList.onRefreshComplete();
        adapter.setIsRequesting(false);
    }

    private String mPageName = "HMCooperationFrg";

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
