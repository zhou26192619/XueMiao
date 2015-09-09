package com.lib_common;

import android.app.Application;

import com.lib_common.bean.UserBean;
import com.lib_common.disk.UserSP;
import com.lib_common.observer.ActivityObserver;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseApp extends Application {
    protected String TAG = "BaseApp";
    protected List<ActivityObserver> acts = new ArrayList<ActivityObserver>();
    protected UserSP userSP;
    protected static UserBean user;

    @Override
    public void onCreate() {
        super.onCreate();
        requestOnBack();
    }

    /**
     * 创建app是默认调用的方法
     */
    public void requestOnBack() {
    }

    public <T> T getUser(Class<T> t) {
        T user = getUserSP().getUserBean(t);
        return user;
    }

    public void saveUser(final UserBean userBean) {
        user = userBean;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserSP().saveUserBean(userBean);
            }
        }).start();
    }

    public UserSP getUserSP() {
        if (userSP == null) {
            userSP = new UserSP(this);
        }
        return userSP;
    }

    public void addActivity(ActivityObserver act) {
        acts.add(act);
        ToastUtil.log(TAG, "activity name = "
                + act.getClass().getCanonicalName());
    }

    public void removeActivity(ActivityObserver act) {
        acts.remove(act);
    }

    public void updateActivities() {
        for (ActivityObserver act : acts) {
            act.loadData();
        }
    }

    public void updateActivity(Class<? extends ActivityObserver> c) {
        for (ActivityObserver act : acts) {
            if (act.getClass().getCanonicalName().equals(c.getCanonicalName())) {
                act.loadData();
                break;
            }
        }
    }

    /**
     * 获取指定的activity对象
     *
     * @param c
     * @return 有可能为空
     */
    public ActivityObserver getActivity(Class<? extends ActivityObserver> c) {
        for (ActivityObserver act : acts) {
            if (act.getClass().getCanonicalName().equals(c.getCanonicalName())) {
                return act;
            }
        }
        return null;
    }
}
