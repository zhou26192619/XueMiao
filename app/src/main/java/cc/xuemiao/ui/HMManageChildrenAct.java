package cc.xuemiao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.CommonSwipeAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiChild;
import cc.xuemiao.bean.HMChildBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.login_register.HMRegisterChildMaterialAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 小孩管理
 *
 * @author loar
 */
public class HMManageChildrenAct extends HMBaseAct {

    @Bind(R.id.manage_children_lv_children)
    ListView lvChildren;

    protected List<HMChildBean> childBeans;
    private CommonSwipeAdapter<HMChildBean> adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_manage_children);
        init();
    }

    private void init() {
        hvHeadView.setTitle("小孩管理");
        hvHeadView.setRight("", R.mipmap.add);
        hvHeadView.setOnClickRightListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd(v);
            }
        });
        adapter = new CommonSwipeAdapter<HMChildBean>(this, childBeans,
                R.layout.adapter_manage_children, R.id.manage_child_sl) {

            @Override
            public void dealViews(ViewHolder holder, List<HMChildBean> beans,
                                  int position) {
                mDealView(holder, beans, position);
            }

        };
        lvChildren.setAdapter(adapter);
        lvChildren.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SwipeLayout sl = ((SwipeLayout) (lvChildren.getChildAt(position
                        - lvChildren.getFirstVisiblePosition())));
                sl.close(true);
            }

        });
    }

    @Override
    public void updateView() {
        super.updateView();
        adapter.setDatas(childBeans);
    }

    public void onAdd(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt(HMRegisterChildMaterialAct.BUNDLE_KEY_TYPE,
                HMRegisterChildMaterialAct.TYPE_ADD);
        HMNavUtil.goToNewAct(this, HMRegisterChildMaterialAct.class, bundle);
    }

    @Override
    public void loadData() {
        HMUserBean user =((HMApp)getApplication()).getUserSP().getUserBean(HMUserBean.class);
        if (user == null) {
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("parentId", user.getRoleId());
        HMApiChild.getInstance().postListByParentId(this, params);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiChild.LIST_BY_PARENT_ID)) {
            childBeans = GsonUtil.fromJsonArr(
                    jo.getAsJsonArray(HMApi.KEY_DATA),
                    new TypeToken<List<HMChildBean>>() {
                    });
            updateView();
        } else if (url.equals(HMApiChild.DELETE_BY_ID)) {
            ((HMApp)getApplication()).updateActivities();
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                    Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                                 String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
    }

    protected void mDealView(ViewHolder holder, List<HMChildBean> beans,
                             int position) {
        final HMChildBean bean = beans.get(position);
        TextView tvName = holder.getViewById(R.id.manage_children_tv_name,
                TextView.class);
        TextView tvInfo = holder.getViewById(R.id.manage_children_tv_info,
                TextView.class);
        ImageView ivLogo = holder.getViewById(R.id.manage_children_iv_logo,
                ImageView.class);
        ImageView ivDelete = holder.getViewById(R.id.manage_children_iv_delete,
                ImageView.class);
        ImageView ivEdit = holder.getViewById(R.id.manage_children_iv_edit,
                ImageView.class);
        ivEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HMRegisterChildMaterialAct.BUNDLE_KEY,
                        bean);
                bundle.putInt(HMRegisterChildMaterialAct.BUNDLE_KEY_TYPE,
                        HMRegisterChildMaterialAct.TYPE_UPDATE);
                HMNavUtil.goToNewAct(HMManageChildrenAct.this,
                        HMRegisterChildMaterialAct.class, bundle);
            }
        });
        ivDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TipDialog.getInstance(HMManageChildrenAct.this)
                        .setContent("确认删除吗?", null, null)
                        .setOnListener(new TipDialog.OnMOKListener() {

                            @Override
                            public void onClick(TipDialog dialog, View view) {
                                RequestParams params = new RequestParams();
                                params.put("childId", bean.getId());
                                HMApiChild.getInstance().postDeleteById(
                                        HMManageChildrenAct.this, params);
                                dialog.dismiss();
                            }
                        }, new TipDialog.OnMCancelListener() {

                            @Override
                            public void onClick(TipDialog dialog, View view) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        try {
            tvName.setText(bean.getName());
            String sex = null;
            if ("m".equals(bean.getGender())) {
                sex = "男";
            } else {
                sex = "女";
            }
            tvInfo.setText("(" + sex + ","
                    + DateUtil.getAge(bean.getBornDate()) + "岁)");
            MImageLoader.display(this, bean.getLogo(), ivLogo, false,
                    MImageLoader.DEFAULT_ANGLE);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }
}
