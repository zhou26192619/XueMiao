package cc.xuemiao.ui.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.helper.HMHXSDKHelper;
import com.lib_common.bean.DeviceInfoBean;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.CommonToolsUtil;
import com.lib_common.util.StringUtil;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApiVersion;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.main.HMMainAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 扉页、欢迎页
 *
 * @author loar
 */
public class HMWelcomeAct extends HMBaseAct {

    private final String IS_FIRST_IN = "isFirstIn";

    private Handler handler = new Handler();

    private SharedPreferences preferences;

    private boolean isFirstIn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_welcome);
//        ButterKnife.bind(this);
        MobclickAgent.setDebugMode(false);
        MobclickAgent.updateOnlineConfig(this);
        init();
    }

    private void init() {
        // 使用SharedPreferences来记录程序的使用次数
        preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean(IS_FIRST_IN, true);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                HMNavUtil.goToNewAct(HMWelcomeAct.this, HMMainAct.class);
                finish();
                if (isFirstIn) {
                    preferences.edit().putBoolean(IS_FIRST_IN, false).commit();
                } else {

                }
            }
        }, BaseConfig.TIME_WELCOME_ACT_DURATION);
    }

    @Override
    public void loadData() {
        super.loadData();
        HMUserBean userBean = ((HMApp)getApplication()).getUserSP()
                .getUserBean(HMUserBean.class);
        if (userBean == null || StringUtil.isEmpty(userBean.getId())) {
            return;
        }
        DeviceInfoBean info = CommonToolsUtil.getDeviceInfo(this, userBean.getId());
        RequestParams params = new RequestParams();
        params.put("accountId", info.getAccountId());
        params.put("currentVersion", info.getCurrentVersion());
        params.put("deviceId", info.getDeviceId());
        params.put("deviceSoftwareVersion", info.getDeviceSoftwareVersion());
        params.put("lastLoginPlatform", info.getLastLoginPlatform());
        params.put("mac", info.getMac());
        params.put("OSRelease", info.getOSRelease());
        params.put("phoneModel", info.getPhoneModel());
        params.put("subscriberId", info.getSubscriberId());
        HMApiVersion.getInstance().postDeviceInfo(this, params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
