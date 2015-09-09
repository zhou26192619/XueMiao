package cc.xuemiao.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.NavUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.MyWebView;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiOrganization;
import cc.xuemiao.bean.HMImage;
import cc.xuemiao.bean.HMLBSBean;
import cc.xuemiao.bean.HMOrganizationBean;
import cc.xuemiao.ui.campaign.HMCampaignDisplayAct;
import cc.xuemiao.ui.common.HMMapAct;
import cc.xuemiao.ui.view.GridImageView;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 机构详情
 *
 * @author loar
 */
public class HMOrgDetailAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "organId";

    @Bind(R.id.org_detail_tv_name)
    TextView tvName;

    @Bind(R.id.org_detail_wv_introduction)
    MyWebView wvIntroduction;

    @Bind(R.id.org_detail_tv_phone)
    TextView tvPhone;

    @Bind(R.id.org_detail_tv_address)
    TextView tvAddress;

    @Bind(R.id.org_detail_ll_head)
    LinearLayout llHead;

    @Bind(R.id.org_detail_ll_hide)
    LinearLayout llHide;

    @Bind(R.id.org_detail_iv_logo)
    ImageView ivLogo;

    @Bind(R.id.org_detail_ll_courses)
    LinearLayout llCourses;

    @Bind(R.id.org_detail_ll_address)
    LinearLayout llAddress;

    @Bind(R.id.org_detail_ll_campaign)
    LinearLayout llCampaign;

    @Bind(R.id.org_detail_ll_info)
    LinearLayout llInfo;

    @Bind(R.id.org_detail_ll_phone)
    LinearLayout llPhone;

    private HMOrganizationBean organizationBean;

    private String organId;

    private int headH;
    private int headHpx;
    private int minHeadHpx;
    /**
     * 头部缩小的倍数
     */
    private float scaleMultiple = 2.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViews(R.layout.act_org_detail);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        organId = bundle.getString(BUNDLE_KEY);
    }

    private void init() {
        hvHeadView.setTitle("机构详情");
        llHead.post(new Runnable() {

            @Override
            public void run() {
                headHpx = llHead.getHeight();
                headH = DensityUtil.px2dip(HMOrgDetailAct.this, headHpx);
                // minInfoHpx = (int) (infoHpx / scaleMultiple);
                minHeadHpx = llInfo.getHeight();
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            wvIntroduction.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wvIntroduction.getSettings().setLoadsImagesAutomatically(false);
        }
        wvIntroduction.setOnScroolChangeListener(new MyWebView.ScrollInterface() {

            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                if (((headHpx - t) < minHeadHpx)
                        && ((headHpx - oldt) < minHeadHpx)) {
                    return;
                }
                int height = (headHpx - t) < minHeadHpx ? minHeadHpx : headHpx
                        - t;
                RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, height);
                llHead.setLayoutParams(infoParams);
                // int width = height - llInfo.getPaddingTop() * 2;
                // LinearLayout.LayoutParams logoParams = new
                // LinearLayout.LayoutParams(
                // width, width);
                // ivLogo.setLayoutParams(logoParams);
                float alpha = height * 1.0f / minHeadHpx - 1;
                llHide.setAlpha(alpha);
                if (alpha == 0) {
                    llHide.setVisibility(View.GONE);
                } else {
                    llHide.setVisibility(View.VISIBLE);
                }
                // float size = 16f * (1 + (1 - alpha) * 0.5f);
                // ToastUtil.log("onSChanged", alpha + " = " + size);
                // tvName.setTextSize(size);
            }
        });
    }

    @Override
    public void updateView() {
        super.updateView();
        if (organizationBean != null) {
            tvName.setText(organizationBean.getName());
            tvPhone.setText(organizationBean.getPhone());
            tvAddress.setText(organizationBean.getProvince()
                    + organizationBean.getCity() + organizationBean.getArea()
                    + organizationBean.getAddress());
            MImageLoader.displayWithDefaultOptions(this,
                    organizationBean.getLogo(), ivLogo);
            if (StringUtil.isEmpty(organizationBean.getContent())) {
                wvIntroduction.setVisibility(View.GONE);
            } else {
                String html = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head lang=\"en\">"
                        + "<meta charset=\"UTF-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">"
                        + "<title></title><style>body{font-size: 14px; height: auto; padding-top:"
                        + headH
                        + "px}img{width: 100%;height: auto;}</style></head><body>"
                        + organizationBean.getContent() + "</body></html>";
                wvIntroduction.loadDataWithBaseURL(null, html, "text/html",
                        "UTF-8", null);
                wvIntroduction.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadData() {
        showLoading();
        RequestParams params = new RequestParams();
        params.put("id", organId);
        HMApiOrganization.getInstance().postDetail(this, params);
    }

    @OnClick(R.id.org_detail_ll_courses)
    public void onShowClass(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(HMCourseListAct.BUNDLE_KEY_ID, organId);
        bundle.putInt(HMCourseListAct.BUNDLE_KEY_TYPE,
                HMCourseListAct.TYPE_ORGAN);
        HMNavUtil.goToNewAct(this, HMCourseListAct.class, bundle);
    }

    @OnClick(R.id.org_detail_ll_address)
    public void onAddress(View v) {
        HMLBSBean lbsBean = new HMLBSBean();
        lbsBean = new HMLBSBean();
        if (organizationBean.getLat() != null) {
            lbsBean.setLat(Double.valueOf(organizationBean.getLat()));
        }
        if (organizationBean.getLng() != null) {
            lbsBean.setLng(Double.valueOf(organizationBean.getLng()));
        }
        lbsBean.setTitle(organizationBean.getName());
        lbsBean.setContent(organizationBean.getAddress());
        Bundle bundle = new Bundle();
        bundle.putSerializable(HMMapAct.BUNDLE_KEY, lbsBean);
        bundle.putSerializable(HMMapAct.BUNDLE_KEY_TITLE,
                organizationBean.getArea() + organizationBean.getAddress());
        HMNavUtil.goToNewAct(this, HMMapAct.class, bundle);
    }

    @OnClick(R.id.org_detail_ll_phone)
    public void onPhone(View v) {
        TipDialog.getInstance(this).setContent("要打电话吗?", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + organizationBean.getPhone()));
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

    @OnClick(R.id.org_detail_ll_campaign)
    public void onCampaign(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(HMCampaignDisplayAct.BUNDLE_KEY, organId);
        bundle.putInt(HMCampaignDisplayAct.BUNDLE_KEY_TYPE,
                HMCampaignDisplayAct.TYPE_ORG);
        NavUtil.goToNewAct(this,
                HMCampaignDisplayAct.class, bundle);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiOrganization.DETAIL)) {
            organizationBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMOrganizationBean.class);
            updateView();
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

//    @OnClick(R.id.common_head_rl_back)
//    public void onBack(View v) {
//        super.onBack(v);
//    }
}
