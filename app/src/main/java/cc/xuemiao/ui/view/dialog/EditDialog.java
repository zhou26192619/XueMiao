package cc.xuemiao.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cc.xuemiao.R;


/**
 * 报名弹窗
 * 
 * @author Yaolan
 * 
 */
public class EditDialog extends Dialog {

	private Context context;
	private ImageView ivLogo;
	private EditText etText;
	private TextView tvOk;
	private TextView tvTitle;
	private ImageView ivCancel;
	private View view;

	public EditDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		try {
			view = View.inflate(context, R.layout.dialog_edit, null);
			ivLogo = (ImageView) view.findViewById(R.id.edit_iv_logo);
			etText = (EditText) view.findViewById(R.id.edit_et_text);
			tvOk = (TextView) view.findViewById(R.id.edit_tv_ok);
			ivCancel = (ImageView) view.findViewById(R.id.edit_iv_cancel);
			tvTitle = (TextView) view.findViewById(R.id.edit_tv_title);
			setListener();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	private void setListener() {
		tvOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onEditClickListener != null) {
					onEditClickListener.ok(etText.getText().toString(), v);
				}
			}
		});
		ivCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onEditClickListener != null) {
					onEditClickListener.cancel(v);
				}
			}
		});
	}

	public void setEditText(String text) {
		etText.setText(text);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setOKText(String text) {
		tvOk.setText(text);
	}

	private OnEditClickListener onEditClickListener;

	public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
		this.onEditClickListener = onEditClickListener;
	}

	public interface OnEditClickListener {

		void ok(String text, View v);

		void cancel(View v);

	}

}
