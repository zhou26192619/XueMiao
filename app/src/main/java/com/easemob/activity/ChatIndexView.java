package com.easemob.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.activity.frg.IndexContactsFrg;
import com.easemob.activity.frg.IndexGroupsFrg;
import com.easemob.activity.frg.IndexSessionsFrg;
import com.easemob.api.HMApiChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cc.xuemiao.R;
import cc.xuemiao.ui.HMBaseFragment;
import cc.xuemiao.ui.main.HMMsgFrg;


public class ChatIndexView extends LinearLayout {

    private Context context;
    private ListView rlContainer;
    private TextView tvContacts;
    private TextView tvConversation;
    private TextView tvGroup;
    private EditText etSearch;

    private List<HMBaseFragment> frgs;

    public String[] tags = {
            IndexSessionsFrg.class.getCanonicalName(),
            IndexContactsFrg.class.getCanonicalName(),
            IndexGroupsFrg.class.getCanonicalName()};
    private List<EMGroup> groups;
    private CommonAdapter<EMGroup> sessionAdapter;
    private CommonAdapter<EMGroup> groupAdapter;
    private CommonAdapter<String> contactAdapter;
    private List<String> contacts;


    public ChatIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ChatIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatIndexView(Context context) {
        super(context, null);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.view_chat_index, this);
        rlContainer = (ListView) findViewById(R.id.chat_index_rl_container);
        tvContacts = (TextView) findViewById(R.id.chat_index_tv_contacts);
        tvConversation = (TextView) findViewById(R.id.chat_index_tv_conversation);
        tvGroup = (TextView) findViewById(R.id.chat_index_tv_group);
        etSearch = (EditText) findViewById(R.id.chat_index_et_search);

        groups = EMGroupManager.getInstance().getAllGroups();
        contacts=HMApiChat.getInstance().userList();


        sessionAdapter = new CommonAdapter<EMGroup>(context, groups, R.layout.chat_adapter_index_sessions) {
            @Override
            public void dealViews(ViewHolder holder, List<EMGroup> datas, int position) {
                dealSessionAdapterViews(holder, datas, position);
            }
        };
        contactAdapter = new CommonAdapter<String>(context, contacts, R.layout.chat_adapter_index_contacts) {
            @Override
            public void dealViews(ViewHolder holder, List<String> datas, int position) {
                dealContactAdapterViews(holder, datas, position);
            }
        };
        groupAdapter = new CommonAdapter<EMGroup>(context, groups, R.layout.chat_adapter_index_groups) {
            @Override
            public void dealViews(ViewHolder holder, List<EMGroup> datas, int position) {
                dealGroupAdapterViews(holder, datas, position);
            }
        };
        rlContainer.setAdapter(contactAdapter);
    }

    private void dealSessionAdapterViews(ViewHolder holder, List<EMGroup> datas, int position) {
        EMGroup bean = datas.get(position);
        ImageView ivLogo = holder.getViewById(R.id.chat_index_sessions_iv_logo, ImageView.class);
        TextView tvName = holder.getViewById(R.id.chat_index_sessions_tv_name, TextView.class);
        try {
            tvName.setText(bean.getGroupName());
        } catch (Exception e) {
            ToastUtil.toastAlways(context, e);
        }
    }

    private void dealGroupAdapterViews(ViewHolder holder, List<EMGroup> datas, int position) {
        EMGroup bean = datas.get(position);
        ImageView ivLogo = holder.getViewById(R.id.chat_index_groups_iv_logo, ImageView.class);
        TextView tvName = holder.getViewById(R.id.chat_index_groups_tv_name, TextView.class);
        try {
            tvName.setText(bean.getGroupName());
        } catch (Exception e) {
            ToastUtil.toastAlways(context, e);
        }
    }

    private void dealContactAdapterViews(ViewHolder holder, List<String> datas, int position) {
        String bean = datas.get(position);
        ImageView ivLogo = holder.getViewById(R.id.chat_index_contacts_iv_logo, ImageView.class);
        TextView tvName = holder.getViewById(R.id.chat_index_contacts_tv_name, TextView.class);
        try {
            tvName.setText(bean);
        } catch (Exception e) {
            ToastUtil.toastAlways(context, e);
        }
    }


}
