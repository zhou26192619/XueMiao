package cc.xuemiao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cc.xuemiao.R;


public class NavBarView extends RelativeLayout {
    public static boolean unConversion = false;
    public static boolean unApply = false;

    private RelativeLayout rlTab0;
    private RelativeLayout rlTab1;
    private RelativeLayout rlTab2;
    private RelativeLayout rlTab3;
    private RelativeLayout rlTab4;

    private ImageView ivTab0;
    private ImageView ivTab1;
    private ImageView ivTab2;
    private ImageView ivTab3;
    private ImageView ivTab4;

    private static ImageView unread;

    private int lastSelectedIndex;

    public List<ImageView> iconViews = new ArrayList<ImageView>();

    public NavBarView(Context context) {
        super(context, null);
    }

    public NavBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.view_nav_bar, this);
        unread = (ImageView) rootView.findViewById(R.id.unread_msg);
        ivTab0 = (ImageView) rootView.findViewById(R.id.nav_bar_iv_tab0);
        ivTab1 = (ImageView) rootView.findViewById(R.id.nav_bar_iv_tab1);
        ivTab2 = (ImageView) rootView.findViewById(R.id.nav_bar_iv_tab2);
        ivTab3 = (ImageView) rootView.findViewById(R.id.nav_bar_iv_tab3);
        ivTab4 = (ImageView) rootView.findViewById(R.id.nav_bar_iv_tab4);
        rlTab0 = (RelativeLayout) rootView.findViewById(R.id.nav_bar_rl_tab0);
        rlTab1 = (RelativeLayout) rootView.findViewById(R.id.nav_bar_rl_tab1);
        rlTab2 = (RelativeLayout) rootView.findViewById(R.id.nav_bar_rl_tab2);
        rlTab3 = (RelativeLayout) rootView.findViewById(R.id.nav_bar_rl_tab3);
        rlTab4 = (RelativeLayout) rootView.findViewById(R.id.nav_bar_rl_tab4);

        ivTab0.setSelected(true);
        iconViews.add(ivTab0);
        iconViews.add( ivTab1);
        iconViews.add( ivTab2);
        iconViews.add( ivTab3);
        iconViews.add(ivTab4);
        rlTab0.setTag(0);
        rlTab1.setTag(1);
        rlTab2.setTag(2);
        rlTab3.setTag(3);
        rlTab4.setTag(4);

        OnClickListener listener=new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClick(v);
            }
        };
        rlTab0.setOnClickListener(listener);
        rlTab1.setOnClickListener(listener);
        rlTab2.setOnClickListener(listener);
        rlTab3.setOnClickListener(listener);
        rlTab4.setOnClickListener(listener);
    }

    public void onTabClick(View v) {
        Integer p = (Integer) v.getTag();
        if (onTabChangedCallback != null) {
            if (onTabChangedCallback.onTabChanged(v, p)) {
                selectTab(p);
            }
        }
    }

    public void selectTab(int index) {
        unSelectedAllIcons();
        iconViews.get(index).setSelected(true);
    }

    private void unSelectedAllIcons() {
        for (ImageView iv : iconViews) {
            iv.setSelected(false);
        }
    }

    public static void setUnRead() {
        if (!unConversion && !unApply) {
            unread.setVisibility(View.GONE);
        } else {
            unread.setVisibility(View.VISIBLE);
        }
    }

    private OnTabChangedCallback onTabChangedCallback;

    public void setOnTabChangedCallback(
            OnTabChangedCallback onTabChangedCallback) {
        this.onTabChangedCallback = onTabChangedCallback;
    }

    public interface OnTabChangedCallback {
        public boolean onTabChanged(View view, int index);
    }
}
