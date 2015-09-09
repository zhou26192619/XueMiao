package com.easemob.api;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.activity.MakeFrientsAct;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cc.xuemiao.api.HMApi;
import cc.xuemiao.ui.HMBaseAct;

public class HMApiChat extends HMApi {
    public static  final String REGISTER = HOST_CHAT
            + "webapi/children/addChild";
    public static  final String LOGIN = HOST_CHAT
            + "webapi/children/addChild";
    public static  final String LOGOUT = HOST_CHAT
            + "webapi/children/addChild";
    public static  final String SEND_MSG = HOST_CHAT
            + "webapi/children/addChild";
    public static  final String FIND_USER = HOST_CHAT
            + "webapi/children/addChild";

    public static HMApiChat getInstance() {
        return new HMApiChat();
    }

    public void login(HMBaseAct act,
                          String userName, String pwd) {
        pwd = "123456";
        EMChatManager.getInstance().login(userName, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMGroupManager.getInstance().loadAllGroups();
                EMChatManager.getInstance().loadAllConversations();
                ToastUtil.log("HMApiChat", "登陆聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                ToastUtil.log("HMApiChat", "登陆聊天服务器失败！");
            }
        });
    }

    public void register(HMBaseAct act,
                             String username, String pwd) {
        try {
            pwd = "123456";
            EMChatManager.getInstance().createAccountOnServer(username, pwd);
        } catch (EaseMobException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == EMError.NONETWORK_ERROR) {
                ToastUtil.toastAlways(act, "网络异常，请检查网络！");
            } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                ToastUtil.toastAlways(act, "用户已存在！");
            } else if (errorCode == EMError.UNAUTHORIZED) {
                ToastUtil.toastAlways(act, "注册失败，无权限！");
            } else {
                ToastUtil.toastAlways(act, "注册失败: " + e.getMessage());
            }
        }
    }

    public void logout(HMBaseAct act,
                           RequestParams params) {
        EMChatManager.getInstance().logout();//此方法为同步方法
    }

    public void findUser(MakeFrientsAct act, RequestParams params) {
        postRequest(act, null, params);
    }

    public void updateNickname(String nickname) {
        EMChatManager.getInstance().updateCurrentUserNick(nickname);
    }

    public void addContact(String addUsername,String reason) throws EaseMobException {
        EMContactManager.getInstance().addContact(addUsername,reason);//需异步执行
    }

}
