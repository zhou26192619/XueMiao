package cc.xuemiao.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lib_common.dialog.TipDialog;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.login_register.HMLoginAct;
import cc.xuemiao.ui.login_register.HMModifyPasswordAct;
import cc.xuemiao.utils.HMNavUtil;


public class HMOptionAct extends HMBaseAct {


    @Bind(R.id.option_tv_phone)
    TextView tvPhone;

    private HMUserBean user;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_option);
        init();
    }

    private void init() {
        hvHeadView.setTitle("设置");
    }

    @Override
    public void loadData() {
        user = ((HMApp)getApplication()).getUserSP().getUserBean(HMUserBean.class);
        if (user != null) {
            tvPhone.setText(user.getParent().getPhone());
        }
    }

    @OnClick(R.id.option_ll_account)
    public void onAccount(View v) {
        HMNavUtil.goToNewAct(this, HMPhoneFillInAct.class);
    }

    @OnClick(R.id.option_ll_modify_pwd)
    public void onModifyPwd(View view) {
        HMNavUtil.goToNewAct(this, HMModifyPasswordAct.class);
    }

    @OnClick(R.id.option_ll_message)
    public void onMessage(View view) {
        HMNavUtil.goToNewAct(this, HMOptionMessageAct.class);
    }

    @OnClick(R.id.option_tv_logout)
    public void onLogout(View view) {
        TipDialog.getInstance(this).setContent("确认退出吗?", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        ((HMApp)getApplication()).getUserSP().clear();
                        dialog.dismiss();
                        HMNavUtil.goToNewAct(HMOptionAct.this, HMLoginAct.class);
                        ((HMApp)getApplication()).updateActivities();
                        onBack(null);
                    }
                }, new TipDialog.OnMCancelListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
