package cc.xuemiao.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.lib_common.activity.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.R;
import cc.xuemiao.ui.view.HeadView;

public abstract class HMBaseAct extends BaseActivity {

    @Nullable
    @Bind(R.id.headView)
    public HeadView hvHeadView;

    @Override
    public void setContentViews(int layoutResID) {
        super.setContentViews(layoutResID);
        if (hvHeadView != null) {
            hvHeadView.setOnClickLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack(v);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
