package com.easemob.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.adapter.ChatAdapter;
import com.easemob.api.HMApiChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.R;
import cc.xuemiao.ui.HMBaseAct;

/**
 * person to person 单聊聊天
 *
 * @author loar
 */
public class ChatP2PAct extends HMBaseAct {

    public static final String BUNDLE_KEY_USER_NAME = "username";

    @Bind(R.id.chat_p2p_et_content)
    EditText etContent;

    @Bind(R.id.chat_p2p_lv_record)
    ListView lvRecord;

    @Bind(R.id.chat_p2p_tv_send)
    TextView tvSend;

    private String username;
    private String content;
    private ChatAdapter adapter;
    private List<EMMessage> messages = new ArrayList<EMMessage>();
    private EMMessage message;
    private android.os.Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.chat_act_p2p);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        username = bundle.getString(BUNDLE_KEY_USER_NAME, "学苗小微");
    }

    private void init() {
        username = "58";
        hvHeadView.setTitle(username);
        adapter = new ChatAdapter(this, messages);
        lvRecord.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();

    }

    @OnClick(R.id.chat_p2p_tv_send)
    public void onSend(View view) {
        content = etContent.getText().toString();
        if (StringUtil.isEmpty(content)) {
            ToastUtil.toastAlways(this, "还没写什么呢!");
            return;
        }
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //创建一条文本消息
        message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(username);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        etContent.setText(null);
                        messages.add(message);
                        adapter.setDatas(messages);
                    }
                });

            }

            @Override
            public void onError(int i, final String s) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        ToastUtil.toastAlways(ChatP2PAct.this, s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


}
