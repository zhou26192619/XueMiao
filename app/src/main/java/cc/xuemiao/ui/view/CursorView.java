package cc.xuemiao.ui.view;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lib_common.util.DensityUtil;
import com.lib_common.util.ToastUtil;

public class CursorView extends LinearLayout {

    private List<View> cursors = new ArrayList<View>();

    public CursorView(Context context) {
        super(context);
    }

    public CursorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, int count, int drawable, int width,
                     int height, int margin) {
        removeAllViews();
        LayoutParams params = new LayoutParams(DensityUtil.dip2px(
                context, width), DensityUtil.dip2px(context, height), 1);
        margin = DensityUtil.dip2px(context, margin);
        params.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < count; i++) {
            View child = new View(context);
            child.setBackgroundResource(drawable);
            cursors.add(child);
            child.setLayoutParams(params);
            addView(child);
        }
    }

    public void setSelectCursors(int position) {
        try {
            for (View v : cursors) {
                v.setSelected(false);
            }
            cursors.get(position).setSelected(true);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }
}
