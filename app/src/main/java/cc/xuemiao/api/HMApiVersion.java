package cc.xuemiao.api;

import com.lib_common.config.BaseConfig;
import com.lib_common.observer.ActivityObserver;
import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;

public class HMApiVersion extends HMApi {
    public static final int PLATFORM = 1;

    public static final String VERSION = HOST_VERSION
            + "webapi/update/getVersion";

    public static final String DEVICE_INFO = HOST_VERSION
            + "webapi/update/device";

    public static HMApiVersion getInstance() {
        return new HMApiVersion();
    }

    /**
     * 获取最新版本
     *
     * @param act
     * @param params
     */
    public void postVersion(ActivityObserver act, RequestParams params) {
        params.put("platform", PLATFORM);
        postRequestByNotice(act, VERSION, params);
    }

    /**
     * 发送设备信息给服务器
     *
     * @param act
     * @param params
     */
    public void postDeviceInfo(HMBaseAct act, RequestParams params) {
        params.put("platform", BaseConfig.PLATFORM);
        postRequest(act, DEVICE_INFO, params);
    }
}
