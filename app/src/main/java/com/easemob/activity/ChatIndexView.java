package com.easemob.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib_common.fragment.BaseFragment;

import java.util.List;

import cc.xuemiao.R;
import cc.xuemiao.ui.main.HMCourseFrg;
import cc.xuemiao.ui.main.HMIndexFrg;
import cc.xuemiao.ui.main.HMMsgFrg;


public class ChatIndexView extends LinearLayout {

    private Context context;
    private RelativeLayout rlContainer;
    private TextView tvContacts;
    private TextView tvConversation;
    private TextView tvGroup;

    private List<BaseFragment> fragments;

    public String[] tags = {
            HMIndexFrg.class.getCanonicalName(),
            HMCourseFrg.class.getCanonicalName(),
            HMMsgFrg.class.getCanonicalName()};

    private int[] frgLayoutIds = {
            R.id.main_fl_0,
            R.id.main_fl_1,
            R.id.main_fl_2};

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
        rlContainer = (RelativeLayout) findViewById(R.id.chat_index_rl_container);
        tvContacts = (TextView) findViewById(R.id.chat_index_tv_contacts);
        tvConversation = (TextView) findViewById(R.id.chat_index_tv_conversation);
        tvGroup = (TextView) findViewById(R.id.chat_index_tv_group);
    }

}
