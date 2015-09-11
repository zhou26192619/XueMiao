package cc.xuemiao;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lib_common.BaseApp;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.NavUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import java.util.Iterator;
import java.util.List;

import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.login_register.HMLoginAct;

public class HMApp extends BaseApp {

    private static HMApp app;

    public static HMApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(BaseConfig.IS_DEBUG);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase("com.easemob.chatuidemo")) {
            Log.e(TAG, "enter the service process!");
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        loginOnBack();
    }

    public void loginOnBack() {
        ToastUtil.log(TAG, "loginOnBack");
        user = getUserSP().getUserBean(HMUserBean.class);
        if (user != null) {
            return;
        }
        HMApiUser.getInstance().postLoginOnBack(this, (HMUserBean) user);
    }

    public boolean hasLoggedIn() {
        return getUserSP().getUserBean(HMUserBean.class) != null;
    }

    public HMUserBean getUser() {
        if (user == null) {
            user = getUser(HMUserBean.class);
        }
        if (user == null) {
            NavUtil.goToNewAct(this, HMLoginAct.class);
        }
        return (HMUserBean) user;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
