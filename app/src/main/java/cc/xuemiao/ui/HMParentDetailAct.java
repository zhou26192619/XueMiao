package cc.xuemiao.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import butterknife.Bind;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiParent;
import cc.xuemiao.bean.HMParentBean;
import cc.xuemiao.utils.GenderUtil;

/**
 * 家长详细资料
 *
 * @author m
 */
public class HMParentDetailAct extends HMBaseAct {

    public static final int CODE_REQUEST = 518;
    public static final String BUNDLE_KEY = "parentId";
    public static final String BUNDLE_KEY_ACCOUNT_ID = "accountId";
    public static final String BUNDLE_KEY_ACCOUNT_NIK = "nikname";

    @Bind(R.id.parent_detail_iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.parent_detail_tv_name)
    TextView tvName;

    @Bind(R.id.parent_detail_tv_address)
    TextView tvAddress;

    @Bind(R.id.parent_detail_tv_distance)
    TextView tvDistance;

    @Bind(R.id.parent_detail_tv_make_friend)
    TextView llFriend;

    @Bind(R.id.parent_detail_ll_call)
    LinearLayout llCall;

    @Bind(R.id.parent_detail_tv_message)
    TextView tvMessage;

    @Bind(R.id.parent_detail_tv_info)
    TextView tvInfo;

    @Bind(R.id.parent_detail_iv_gender)
    ImageView ivGender;

    @Bind(R.id.parent_detail_tv_age)
    TextView tvAge;

    @Bind(R.id.parent_detail_ll_head)
    LinearLayout llHead;

    @Bind(R.id.parent_detail_ll_root_in_body)
    LinearLayout llRootInBody;

    private HMParentBean parentBean;

    private String parentId;
    private String accountID;
    private String nikname;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_parent_detail);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        parentId = bundle.getString(BUNDLE_KEY);
        accountID = bundle.getString(BUNDLE_KEY_ACCOUNT_ID);
        nikname = bundle.getString(BUNDLE_KEY_ACCOUNT_NIK);
    }

    private void init() {
        hvHeadView.setTitle("家长资料");
    }

    @Override
    public void updateView() {
        try {
            if (parentBean != null) {
                tvName.setText(nikname);
                parentId = parentBean.getId();
                if (parentBean.getAccountId() != null)
                    accountID = parentBean.getAccountId();
                String addr = parentBean.getProvince() + parentBean.getCity()
                        + parentBean.getArea();
                if (StringUtil.isEmpty(parentBean.getProvince())
                        || StringUtil.isEmpty(parentBean.getCity())
                        || StringUtil.isEmpty(parentBean.getArea())) {
                    addr = "未设置";
                }
                tvAddress.setText(addr);
                // etInfo.setText(parentBean.getBrief());
                GenderUtil.setGenderImage(parentBean.getGender(), ivGender);
                tvAge.setText((DateUtil.getAge(parentBean.getBornDate()))
                        + "岁");
                MImageLoader.display(this, parentBean.getLogo(), ivAvatar,
                        false, MImageLoader.DEFAULT_ANGLE);
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public void loadData() {
        RequestParams params = new RequestParams();
        if (accountID == null) {
            params.put("id", parentId);
        } else {
            params.put("accountId", accountID);
        }
        HMApiParent.getInstance().postDetail(this, params);
    }

    public void send(View v) {
        try {
//            if (accountID != null) {
//                Intent intent = new Intent(HMParentDetailAct.this,
//                        ChatActivity.class);
//                intent.putExtra("userId", accountID);
//                intent.putExtra("nikname", nikname);
//                startActivity(intent);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void add(View v) {
        try {
            if (accountID != null)
                addUser(accountID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void onCall(View v) {
        if (parentBean.getPhone() == null) {
            ToastUtil.toastAlways(this, "该用户未填写电话号哦!");
            return;
        }
        TipDialog.getInstance(this).setContent("要打电话吗?", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + parentBean.getPhone()));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }, new TipDialog.OnMCancelListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void addUser(final String userid) {
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiParent.DETAIL)) {
            parentBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMParentBean.class);
            if (parentBean != null) {
                updateView();
            }
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
}
