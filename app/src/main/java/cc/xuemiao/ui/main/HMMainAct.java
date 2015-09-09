package cc.xuemiao.ui.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.google.gson.JsonObject;
import com.lib_common.dialog.TipDialog;
import com.lib_common.fragment.BaseFragment;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.CommonToolsUtil;
import com.lib_common.util.FileManager;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiVersion;
import cc.xuemiao.bean.HMVersionBean;
import cc.xuemiao.ui.login_register.HMLoginAct;
import cc.xuemiao.ui.view.NavBarView;
import cc.xuemiao.utils.HMNavUtil;

public class HMMainAct extends FragmentActivity implements ActivityObserver {

    @Bind(R.id.nav_bar)
    NavBarView navBar;

    private List<BaseFragment> fragments;

    public String[] tags = {
            HMIndexFrg.class.getCanonicalName(),
            HMCourseFrg.class.getCanonicalName(),
            HMMsgFrg.class.getCanonicalName(),
            HMCooperationFrg.class.getCanonicalName(),
            HMMineFrg.class.getCanonicalName()};

    private int[] frgLayoutIds = {
            R.id.main_fl_0,
            R.id.main_fl_1,
            R.id.main_fl_2,
            R.id.main_fl_3,
            R.id.main_fl_4};

    private FragmentManager fm;

    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.bind(this);
        ((HMApp) getApplication()).addActivity(this);
        fm = getSupportFragmentManager();
        try {
            versionName = CommonToolsUtil.getVersionName(this);
            RequestParams params = new RequestParams();
            params.put("version", versionName);
            HMApiVersion.getInstance().postVersion(this, params);

//            Intent intent = getIntent();
//            if (null != intent) {
//                if (intent.hasExtra("goNotice")) {
//                    Intent i = new Intent(this, HMNoticeListAct.class);
//                    this.startActivity(i);
//                }
//            }
            initFragments();
            setListener();
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void dealIntent(Bundle bundle) {

    }

    private void initFragments() {
        fragments = new ArrayList<BaseFragment>();
        try {
            FragmentTransaction ft = fm.beginTransaction();
            for (int i = 0; i < tags.length; i++) {
                BaseFragment f = (BaseFragment) Class.forName(tags[i])
                        .newInstance();
                if (tags[i].equals(HMMsgFrg.class.getCanonicalName())) {
//					msg = (HMMsgFrg) f;
                }
                fragments.add(f);
                ft.add(frgLayoutIds[i], f, tags[i]);
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            ToastUtil.printErr(e);
        } finally {
            selectTab(0);
        }
    }

    private void setListener() {
        navBar.setOnTabChangedCallback(new NavBarView.OnTabChangedCallback() {
            @Override
            public boolean onTabChanged(View v, int index) {
                if ((R.id.nav_bar_rl_tab2 == v.getId() || R.id.nav_bar_rl_tab4 == v
                        .getId()) && !((HMApp) getApplication()).hasLoggedIn()) {
                    HMNavUtil.goToNewAct(HMMainAct.this, HMLoginAct.class);
                    return false;
                } else {
                    selectTab(index);
                }
                return true;
            }
        });
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadData() {
        for (BaseFragment frg : fragments) {
            frg.loadData();
        }
        selectTab(0);
    }

    public void updateOneFrg(int index) {
        fragments.get(index).loadData();
        selectTab(index);
    }

    public void selectTab(int selectedIndex) {
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            ft.hide(fragment);
        }
        ft.show(fragments.get(selectedIndex)).commitAllowingStateLoss();
        navBar.selectTab(selectedIndex);
    }


    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        final HMVersionBean version = GsonUtil.fromJsonObj(
                jo.getAsJsonObject(HMApi.KEY_DATA), HMVersionBean.class);
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        String tip = "";
        if (conMan.getActiveNetworkInfo().getType() != ConnectivityManager.TYPE_WIFI) {
            tip = "(当前不是wifi状态)";
        }
        if (!versionName.equals(version.getVersion())) {
            TipDialog
                    .getInstance(this)
                    .setTitle("有新版本哦，要更新吗?", null, null)
                    .setContent("亲爱的苗妈苗爸~\n小苗发现了程序猿GG发布的新版本哦~\n快更新试试吧!" + tip,
                            null, null).setOnListener(new TipDialog.OnMOKListener() {

                @Override
                public void onClick(TipDialog dialog, View view) {
                    FileManager.downloadFile(HMMainAct.this,
                            version.getUpdateUrl(), R.mipmap.logo);
                    dialog.dismiss();
                }
            }, new TipDialog.OnMCancelListener() {

                @Override
                public void onClick(TipDialog dialog, View view) {
                    dialog.dismiss();
                }
            }).show();

        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode, Header[] headers, JsonObject jo) {

    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers, String str, Throwable throwable) {

    }

    @Override
    public void setRequestFinish() {

    }

    @Override
    public void setRequestCancel() {

    }

    @Override
    public void setRequestProgress(int bytesWritten, int totalSize) {

    }

    @Override
    public void setRequestRetry(int retryNo) {

    }

    private long ct;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long lt = new Date().getTime();
            if ((lt - ct) > 1000) {
                ToastUtil.toastAlways(this, "双击退出哦");
                ct = lt;
                return true;
            } else {
                finish();
            }
            ct = lt;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 不能删
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((HMApp) getApplication()).removeActivity(this);
    }
}
