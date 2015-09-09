package cc.xuemiao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.xuemiao.R;


public class HeadView extends RelativeLayout {

	private Context context;
	private TextView tvTitle;
	private TextView tvRight;
	private TextView tvLeft;
	private RelativeLayout rlLeft;
	private RelativeLayout rlRight;

	public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public HeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HeadView(Context context) {
		super(context, null);
	}

	private void init(Context context) {
		this.context = context;
		inflate(context, R.layout.view_head, this);
		tvTitle = (TextView) findViewById(R.id.head_tv_title);
		tvRight = (TextView) findViewById(R.id.head_tv_right);
		tvLeft = (TextView) findViewById(R.id.head_tv_left);
		rlLeft = (RelativeLayout) findViewById(R.id.head_rl_left);
		rlRight = (RelativeLayout) findViewById(R.id.head_rl_right);

	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setLeft(String text, int bgResid) {
		tvLeft.setText(text);
		if (bgResid != 0) {
			tvLeft.setBackgroundResource(bgResid);
		}
	}

	public void setRight(String text, int bgResid) {
		tvRight.setText(text);
		if (bgResid != 0) {
			tvRight.setBackgroundResource(bgResid);
		}
	}
	public void setItemVisibility(int leftVisibility, int titleVisibility,int rightVisibility) {
		rlLeft.setVisibility(leftVisibility);
		tvTitle.setVisibility(titleVisibility);
		rlRight.setVisibility(rightVisibility);
	}
	private OnClickListener onClickLeftListener;
	private OnClickListener onClickRightListener;

	public void setOnClickLeftListener(OnClickListener onClickLeftListener) {
		this.onClickLeftListener = onClickLeftListener;
		rlLeft.setVisibility(View.VISIBLE);
		rlLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (HeadView.this.onClickLeftListener != null) {
					HeadView.this.onClickLeftListener.onClick(v);
				}
			}
		});
	}

	public void setOnClickRightListener(OnClickListener onClickRightListener) {
		this.onClickRightListener = onClickRightListener;
		rlRight.setVisibility(View.VISIBLE);
		rlRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (HeadView.this.onClickRightListener != null) {
					HeadView.this.onClickRightListener.onClick(v);
				}
			}
		});
	}

	public interface OnClickLeftListener {
		void onClick(View v);
	}

	public interface OnClickRightListener {
		void onClick(View v);
	}

}
