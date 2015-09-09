package cc.xuemiao.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lib_common.fragment.BaseFragment;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.ui.view.HeadView;

/**
 * Created by loar on 2015/8/31.
 */
public abstract class HMBaseFragment extends BaseFragment {
    @Nullable
    @Bind(R.id.headView)
    HeadView hvHeadView;

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (hvHeadView != null) {
            hvHeadView.setItemVisibility(View.GONE, View.VISIBLE, View.GONE);
        }
    }
}
