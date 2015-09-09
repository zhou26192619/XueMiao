package cc.xuemiao.ui.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cc.xuemiao.R;


public class OverlayClickView {
	private View view;
	private Context context;
	private View llRoot;
	private TextView tvContent;
	private TextView tvTitle;

	public OverlayClickView(Context context) {
		this.context = context;
		this.init();
	}

	private void init() {
		view = View.inflate(context, R.layout.view_overlay_click, null);
		llRoot = view.findViewById(R.id.overlay_click_ll_root);
		tvContent = (TextView) view.findViewById(R.id.overlay_click_tv_content);
		tvTitle = (TextView) view.findViewById(R.id.overlay_click_tv_title);

		llRoot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.click();
				}
			}
		});
	}

	public TextView getTvTitle() {
		return tvTitle;
	}

	public TextView getTvContent() {
		return tvContent;
	}

	public View getView() {
		return view;
	}

	private OnOverlayClickListener clickListener;

	public void setClickListener(OnOverlayClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface OnOverlayClickListener {
		void click();
	}

}