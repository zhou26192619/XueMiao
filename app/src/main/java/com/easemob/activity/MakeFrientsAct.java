package com.easemob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.JsonObject;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import com.easemob.api.HMApiChat;
import cc.xuemiao.api.HMApiParent;
import cc.xuemiao.ui.HMBaseAct;

/**
 * 设置姓名、名字
 *
 * @author loar
 */
public class MakeFrientsAct extends HMBaseAct {

    @Bind(R.id.set_name_et_name)
    EditText etName;

    private String toAddUsername;
    private String reason;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_set_name);
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
    }

    private void init() {
        hvHeadView.setTitle("添加好友");
        hvHeadView.setRight("查找", 0);
    }

    private void setListener() {
        hvHeadView.setOnClickRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUsers();
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();

    }

    private void findUsers() {
        RequestParams params=new RequestParams();
        HMApiChat.getInstance().findUser(this, params);
    }

    public void onMakeFriend(View v) {
        try {
            EMContactManager.getInstance().addContact(toAddUsername, reason);//需异步处理
        } catch (EaseMobException e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiParent.UPDATE)) {
            onBack(null);
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                 Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                              String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
    }

}
