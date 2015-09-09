package cc.xuemiao.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseFragment;
import cc.xuemiao.ui.HMManageChildrenAct;
import cc.xuemiao.ui.HMMyCourseListAct;
import cc.xuemiao.ui.HMOptionAct;
import cc.xuemiao.ui.HMPersonalInfoAct;
import cc.xuemiao.ui.campaign.HMMyCampaignListAct;
import cc.xuemiao.utils.HMNavUtil;

public class HMMineFrg extends HMBaseFragment {

    public static final int CODE_REQUEST = 514;

    @Bind(R.id.mine_iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.mine_tv_name)
    TextView tvName;
    @Bind(R.id.mine_tv_account_name)
    TextView tvAccountName;


    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToastUtil.log("frg", "HMMineFrg");
        return inflater.inflate(R.layout.frg_mine, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init(rootView,inflater,container,savedInstanceState);
        loadData();
    }

    public void updateView() {
        try {
            HMUserBean userBean = ((HMApp)getActivity().getApplication()).getUserSP()
                    .getUserBean(HMUserBean.class);
            tvName.setText("未设置");
            tvAccountName.setText("未设置");
            if (userBean != null) {
                String name = userBean.getNikName();
                if (!StringUtil.isEmpty(name)) {
                    tvName.setText(name);
                }
                if (!StringUtil.isEmpty(userBean.getAccountName())) {
                    tvAccountName.setText(userBean.getAccountName());
                }
                MImageLoader
                        .display(getActivity(), userBean.getParent().getLogo(),
                                ivAvatar, false, MImageLoader.DEFAULT_ANGLE);
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public void loadData() {
        updateView();
    }

    // click listener
    @OnClick(R.id.mine_ll_account_name)
    public void onAccount(View v) {
        HMUserBean user = ((HMApp)getActivity().getApplication()).getUser();
        if (user == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(HMPersonalInfoAct.BUNDLE_KEY, user.getRoleId());
        Intent intent = new Intent(getActivity(), HMPersonalInfoAct.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_REQUEST);
    }

    @OnClick(R.id.mine_ll_manage_children)
    public void onManageChildren(View v) {
        HMNavUtil.goToNewAct(getActivity(), HMManageChildrenAct.class);
    }

    @OnClick(R.id.mine_ll_courses)
    public void onCourses(View v) {
        HMNavUtil.goToNewAct(getActivity(), HMMyCourseListAct.class);
    }

    @OnClick(R.id.mine_ll_option)
    public void onOption(View v) {
        HMNavUtil.goToNewAct(getActivity(), HMOptionAct.class);
    }

    @OnClick(R.id.mine_ll_my_campaign)
    public void onCampaign(View v) {
        HMNavUtil.goToNewAct(getActivity(), HMMyCampaignListAct.class);
    }

    @OnClick(R.id.mine_ll_notice)
    public void onNotice(View v) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST) {
            updateView();
        }
    }

    private String mPageName = "HMMineFrg";

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
