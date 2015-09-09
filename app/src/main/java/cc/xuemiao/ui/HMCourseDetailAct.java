package cc.xuemiao.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.MyWebView;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiChild;
import cc.xuemiao.api.HMApiCourse;
import cc.xuemiao.bean.HMChildBean;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.bean.HMLBSBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.common.HMMapAct;
import cc.xuemiao.ui.view.dialog.SignUpDialog;
import cc.xuemiao.utils.HMNavUtil;

public class HMCourseDetailAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "courseBean";

    @Bind(R.id.course_detail_tv_course_name)
    TextView tvCourseName;

    @Bind(R.id.course_detail_tv_price)
    TextView tvTuition;

    @Bind(R.id.course_detail_tv_address)
    TextView tvAddress;

    @Bind(R.id.course_detail_tv_phone)
    TextView tvPhone;

    @Bind(R.id.course_detail_tv_org_name)
    TextView tvOrgName;

    @Bind(R.id.course_detail_tv_summary)
    TextView tvSummary;

    @Bind(R.id.course_detail_wv_introduction)
    MyWebView wvIntroduction;

    @Bind(R.id.course_detail_iv_logo)
    ImageView ivLogo;

    @Bind(R.id.course_detail_ll_info)
    LinearLayout llInfo;

    @Bind(R.id.course_detail_ll_head)
    LinearLayout llHead;

    @Bind(R.id.course_detail_ll_hide)
    LinearLayout llHide;

    private String courseId;

    protected SignUpDialog dialog;

    protected HMCourseBean courseBean;

    private String parentId;

    private int headH;
    private int headHpx;
    private int minHeadHpx;
    /**
     * 头部缩小的倍数
     */
    private float scaleMultiple = 2.0f;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_course_detail);
        init();
        setListener();
    }

    private void setListener() {
        llHead.post(new Runnable() {

            @Override
            public void run() {
                headHpx = llHead.getHeight();
                headH = DensityUtil.px2dip(HMCourseDetailAct.this, headHpx);
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
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        courseId = bundle.getString(BUNDLE_KEY);
    }

    private void init() {
        hvHeadView.setTitle("课程详情");
        WebViewClient client = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!wvIntroduction.getSettings().getLoadsImagesAutomatically()) {
                    wvIntroduction.getSettings().setLoadsImagesAutomatically(
                            true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        };
        wvIntroduction.setWebViewClient(client);
        WebSettings settings = wvIntroduction.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(BaseConfig.DEFAULT_IMAGE_DIR);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public void updateView() {
        super.updateView();
        if (courseBean != null) {
            tvCourseName.setText(courseBean.getName());
            tvTuition.setText("学费：" + courseBean.getTuition().replace(".0", "") + "元/期");
            tvAddress.setText(courseBean.getProvince() + courseBean.getCity()
                    + courseBean.getArea() + courseBean.getAddress());
            if (StringUtil.isEmpty(courseBean.getOrganizationName())) {
                tvOrgName.setText("个人");
            } else {
                tvOrgName.setText(courseBean.getOrganizationName());
            }
            MImageLoader.displayWithDefaultOptions(this, courseBean.getLogo(),
                    ivLogo);
            if (StringUtil.isEmpty(courseBean.getCourseContent())) {
                wvIntroduction.setVisibility(View.GONE);
            } else {
                String html = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head lang=\"en\">"
                        + "<meta charset=\"UTF-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">"
                        + "<title></title><style>body{font-size: 14px; height: auto; padding-top:"
                        + headH
                        + "px}img{width: 100%;height: auto;}</style></head><body>"
                        + courseBean.getCourseContent() + "</body></html>";
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
        params.put("courseId", courseId);
        params.put("identity", HMApi.IDENTITY);
        HMApiCourse.getInstance().postDetail(this, params);
    }

    public void showDialog(List<HMChildBean> list) {
        dialog = new SignUpDialog(HMCourseDetailAct.this,
                R.style.toast_box_dialog, parentId, courseId, list);
        dialog.show();
    }


    @OnClick(R.id.course_detail_ll_sign_up)
    public void onSignUp(View view) {
        HMUserBean user = ((HMApp) getApplication()).getUser();
        if (user == null) {
            return;
        }
        parentId = user.getParent().getId();
        RequestParams params = new RequestParams();
        params.put("parentId", parentId);
        params.put("courseId", courseId);
        HMApiChild.getInstance().postListByCourse(this, params);
    }

    @OnClick(R.id.course_detail_ll_organization)
    public void onOrgan(View v) {
        if (StringUtil.isEmpty(courseBean.getOrganizationName())) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(HMOrgDetailAct.BUNDLE_KEY,
                courseBean.getOrganizationId());
        HMNavUtil.goToNewAct(this, HMOrgDetailAct.class, bundle);
    }

    @OnClick(R.id.course_detail_ll_address)
    public void onAddress(View v) {
        HMLBSBean lbsBean = new HMLBSBean();
        lbsBean = new HMLBSBean();
        if (courseBean.getLat() != null) {
            lbsBean.setLat(Double.valueOf(courseBean.getLat()));
        }
        if (courseBean.getLng() != null) {
            lbsBean.setLng(Double.valueOf(courseBean.getLng()));
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(HMMapAct.BUNDLE_KEY, lbsBean);
        bundle.putSerializable(HMMapAct.BUNDLE_KEY_TITLE, courseBean.getArea()
                + courseBean.getAddress());
        HMNavUtil.goToNewAct(this, HMMapAct.class, bundle);
    }

    @OnClick(R.id.course_detail_ll_qa)
    public void ask(View v) {
        String teacherID = courseBean.getTeacherId();

        //获取accountID
//        HMApiChat.getAccountIdByTeahcerId(teacherID,

    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiCourse.DETAIL)) {
            courseBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMCourseBean.class);
            updateView();
        } else if (url.equals(HMApiChild.LIST_BY_COURSE)) {
            List<HMChildBean> list = GsonUtil.fromJsonArr(
                    jo.getAsJsonArray(HMApi.KEY_DATA),
                    new TypeToken<List<HMChildBean>>() {
                    });
            if (list != null && list.size() == 0) {
                ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
            } else {
                showDialog(list);
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
