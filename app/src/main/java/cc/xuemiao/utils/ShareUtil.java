package cc.xuemiao.utils;


import com.lib_common.util.ToastUtil;

import cc.xuemiao.ui.HMBaseAct;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class ShareUtil {

    public static void shareTaget(final HMBaseAct act, String platform,
                                  OnekeyShare oks) {
        if (oks == null) {
            return;
        }
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

            @Override
            public void onShare(Platform platform, ShareParams paramsToShare) {
                ToastUtil.log("sharesdk", "onShare " + platform.getId() + "  "
                        + platform.getName());
                if (platform.getName().equals("ShortMessage")) {
                    paramsToShare.setImageUrl(null);
                    paramsToShare.setText(paramsToShare.getTitle() + " "
                            + paramsToShare.getUrl());
                } else if (platform.getName().equals("WechatMoments")) {
                    // paramsToShare.setTitle(paramsToShare.getText());
                } else if (platform.getName().equals("SinaWeibo")) {

                }
            }
        });
        // 启动分享GUI
        oks.show(act);
    }

    /**
     * 分享设置
     *
     * @param oks
     */
    public static void shareTaget(HMBaseAct act, OnekeyShare oks) {
        shareTaget(act, null, oks);
    }
}
