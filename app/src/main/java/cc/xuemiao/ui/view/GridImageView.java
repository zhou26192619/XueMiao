package cc.xuemiao.ui.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.widgt.NoScrollGridView;

import java.util.List;

import cc.xuemiao.R;


/**
 * 九宫格 图片
 * 
 * @author loar
 * 
 */
public class GridImageView {
	private Context context;
	private GridView view;
	private List<ImageBean> entities;
	private CommonAdapter<ImageBean> adapter;
	private int height;

	public GridImageView(Context context, List<ImageBean> entities,
			int numColumns, int height) {
		super();
		this.context = context;
		this.entities = entities;
		this.height = height;
		view = new NoScrollGridView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		view.setColumnWidth(GridView.AUTO_FIT);
		view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		view.setNumColumns(numColumns);
		view.setLayoutParams(params);
		view.setVerticalSpacing(DensityUtil.dip2px(context, 3));
		view.setHorizontalSpacing(DensityUtil.dip2px(context, 3));
		init();
	}

	private void init() {
		adapter = new CommonAdapter<ImageBean>(context, entities,
				R.layout.adapter_grid_image_view) {

			@Override
			public void dealViews(ViewHolder holder,
					final List<ImageBean> datas, final int position) {
				final ImageBean bean = datas.get(position);
				ImageView iv = holder.getViewById(
						R.id.grid_image_view_iv_image, ImageView.class);
				MImageLoader.display(context, bean.getImageUrl(), iv, false, 0);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,
								height));
				iv.setLayoutParams(params);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (clickCallback != null) {
							clickCallback.click(v, bean, position);
						}
					}
				});
			}
		};
		view.setAdapter(adapter);
	}

	public void setData(List<ImageBean> entities) {
		adapter.setDatas(entities);
	}

	public View getView() {
		return view;
	}

	private OnClickCallback clickCallback;

	public void setClickCallback(OnClickCallback clickCallback) {
		this.clickCallback = clickCallback;
	}

	public interface OnClickCallback {
		void click(View v, ImageBean bean, int position);
	}
}
