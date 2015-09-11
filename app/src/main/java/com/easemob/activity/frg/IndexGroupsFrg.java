package com.easemob.activity.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.api.HMApiChat;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.JsonObject;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.ToastUtil;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.ui.HMBaseFragment;

/**
 * 群组
 *
 * @author loar
 */
public class IndexGroupsFrg extends HMBaseFragment {

    @Bind(R.id.chat_index_groups_lv_groups)
    ListView lvGroups;

    private List<EMGroup> groups;
    private CommonAdapter<EMGroup> adapter;

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_frg_index_groups, null);
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
            groups = EMGroupManager.getInstance().getAllGroups();
            adapter = new CommonAdapter<EMGroup>(getActivity(), groups, R.layout.chat_adapter_index_contacts) {

                @Override
                public void dealViews(ViewHolder holder, List<EMGroup> datas, int position) {
                    dealAdapterViews(holder, datas, position);
                }
            };
            lvGroups.setAdapter(adapter);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    private void setListener() {
    }

    private void dealAdapterViews(ViewHolder holder, List<EMGroup> datas, int position) {
        EMGroup bean = datas.get(position);
        try {
            TextView tvName = holder.getViewById(R.id.chat_index_contacts_tv_name, TextView.class);
            tvName.setText(bean.getGroupName());
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
