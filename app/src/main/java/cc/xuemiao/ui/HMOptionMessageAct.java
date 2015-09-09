package cc.xuemiao.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cc.xuemiao.R;


/**
 * 消息提醒设置
 * 
 * @author loar
 * 
 */
public class HMOptionMessageAct extends HMBaseAct {
	private static String TAG = "HMOptionMessageAct";

	@Bind( R.id.option_message_ll_message_remind)
	LinearLayout llMessageRemind;
	@Bind( R.id.option_message_ll_remind_with_content)
	LinearLayout llRemindWithContent;
	@Bind( R.id.option_message_ll_silence_time)
	LinearLayout llSilenceTime;
	@Bind( R.id.option_message_ll_vibration)
	LinearLayout llVibration;
	@Bind( R.id.option_message_ll_voice)
	LinearLayout llVoice;

	@Bind( R.id.option_message_cb_voice)
	CheckBox cbVoice;
	@Bind( R.id.option_message_cb_vibration)
	CheckBox cbVibration;
	@Bind( R.id.option_message_tv_silence_time)
	TextView tvSilenceTime;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentViews(R.layout.act_option_message);
		init();
		initListener();
	}

	private void init() {
		hvHeadView.setTitle("消息提醒");
	}

	private void initListener() {
	}
}
