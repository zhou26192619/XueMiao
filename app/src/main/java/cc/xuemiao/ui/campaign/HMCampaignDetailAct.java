package cc.xuemiao.ui.campaign;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCampaign;
import cc.xuemiao.bean.HMCampaignBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.ShareUtil;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.JsonObject;
import com.lib_common.config.BaseConfig;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.TimeUtil;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.MyWebView;
import com.loopj.android.http.RequestParams;

/**
 * 活动详情
 *
 * @author loar
 */
public class HMCampaignDetailAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "campaignBean";

    @Bind(R.id.campaign_detail_rl_share)
    RelativeLayout rlShare;

    @Bind(R.id.campaign_detail_wv_content)
    MyWebView wvContent;

    @Bind(R.id.campaign_detail_ll_head)
    LinearLayout llHead;

    @Bind(R.id.campaign_detail_tv_tag)
    TextView tvTag;

    @Bind(R.id.campaign_detail_tv_address)
    TextView tvAddress;

    @Bind(R.id.campaign_detail_tv_title)
    TextView tvTitle;

    @Bind(R.id.campaign_detail_tv_time_info)
    TextView tvTimeInfo;

    @Bind(R.id.campaign_detail_tv_org_name)
    TextView tvOrgName;

    @Bind(R.id.campaign_detail_tv_surplus_number)
    TextView tvSurplusNum;

    @Bind(R.id.campaign_detail_tv_price)
    TextView tvPrice;

    @Bind(R.id.campaign_detail_tv_price_unit)
    TextView tvPriceUnit;

    @Bind(R.id.campaign_detail_ll_sign_up)
    LinearLayout llSignUp;

    @Bind(R.id.campaign_detail_tv_remaining_time)
    TextView tvRemainingTime;

    @Bind(R.id.campaign_detail_iv_logo)
    ImageView ivLogo;

    private int windowW;
    /**
     * 头部最小高度
     */
    private int sh;
    /**
     * 最大高度
     */
    private int lh;

    private int totalCount;
    private Handler timeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what <= 0) {
                llSignUp.setClickable(false);
                llSignUp.setBackgroundResource(R.color.gray);
            } else {
                llSignUp.setClickable(true);
                llSignUp.setBackgroundResource(R.color.orange);
            }
            tvRemainingTime.setText("报名倒计时: "
                    + TimeUtil.getTimeFormat(msg.what));
        }

        ;
    };

    private HMCampaignBean campainBean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_campaign_detail);
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        campainBean = (HMCampaignBean) bundle.getSerializable(BUNDLE_KEY);
    }

    private void setListener() {
        wvContent.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        wvContent.setOnScroolChangeListener(new MyWebView.ScrollInterface() {

            @Override
            public void onSChanged(int l, int t, int oldl, int oldt) {
                t = lh - t;
                if (sh <= t && t <= lh) {
                    int heigth = t <= sh ? sh : t >= lh ? lh : t;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            windowW, heigth);
                    llHead.setLayoutParams(params);
                }
            }
        });
    }

    private void init() {
        llSignUp.setClickable(false);
        windowW = DensityUtil.getScreenW(this);
        sh = DensityUtil.dip2px(HMCampaignDetailAct.this, 100);
        lh = DensityUtil.dip2px(HMCampaignDetailAct.this, 300);

        if (Build.VERSION.SDK_INT >= 19) {
            wvContent.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wvContent.getSettings().setLoadsImagesAutomatically(false);
        }
        WebViewClient client = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!wvContent.getSettings().getLoadsImagesAutomatically()) {
                    wvContent.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        };
        wvContent.setWebViewClient(client);
        WebSettings settings = wvContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
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

    private Timer timer = new Timer();

    private String html;

    private HMUserBean user;

    @Override
    public void updateView() {
        try {
            // 倒计时
            totalCount = (int) ((DateUtil.stringToDatetime(
                    campainBean.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss")
                    .getTime() - new Date().getTime()) / 1000);
            totalCount = totalCount < 0 ? 0 : totalCount;
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    timeHandler.sendEmptyMessage(totalCount);
                    if (totalCount <= 0) {
                        timer.cancel();
                    }
                    totalCount--;
                }
            }, 0, 1000);

            html = "<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\"><head lang=\"en\">"
                    + "<meta charset=\"UTF-8\" name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">"
                    + "<title></title><style>body{font-size: 14px; height: auto;}img{width: 100%;height: auto;}</style></head><body>"
                    + campainBean.getContent() + "</body></html>";
            // wvContent.loadData(html, "text/html;charset=\"UTF-8\"", "UTF-8");
            // wvContent.loadUrl("http://m.dianhua.cn/detail/31ccb426119d3c9eaa794df686c58636121d38bc?apikey=jFaWGVHdFVhekZYWTBWV1ZHSkZOVlJWY&app=com.yulore.yellowsdk_ios&uid=355136051337627");
            wvContent.loadDataWithBaseURL(null, html, "text/html", "UTF-8",
                    null);

            MImageLoader.display(this, campainBean.getLogo(), ivLogo,
                    R.mipmap.default_campaign_logo,
                    R.mipmap.default_campaign_logo, true, 0);
            tvTitle.setText(campainBean.getName());
            tvTag.setText(campainBean.getTag().replace(",", " "));
            tvAddress.setText(campainBean.getAddress());
            StringBuffer timeInfo = new StringBuffer();
            timeInfo.append(DateUtil.datetimeTurnPattern(
                    campainBean.getActivityBeginTime(), "yyyy-MM-dd HH:mm:ss",
                    "EEEE")
                    + " "
                    + DateUtil.datetimeTurnPattern(
                    campainBean.getActivityBeginTime(),
                    "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm") + "-");
            if (campainBean.getActivityBeginTime().split(" ")[0]
                    .equals(campainBean.getActivityEndTime().split(" ")[0])) {
                timeInfo.append(DateUtil.datetimeTurnPattern(
                        campainBean.getActivityEndTime(),
                        "yyyy-MM-dd HH:mm:ss", "HH:mm"));
            } else {
                timeInfo.append(DateUtil.datetimeTurnPattern(
                        campainBean.getActivityEndTime(),
                        "yyyy-MM-dd HH:mm:ss", "EEEE")
                        + " "
                        + DateUtil.datetimeTurnPattern(
                        campainBean.getActivityEndTime(),
                        "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm"));
            }
            tvTimeInfo.setText(timeInfo.toString());
            tvAddress.setText(campainBean.getProvince() + campainBean.getCity()
                    + campainBean.getArea() + campainBean.getAddress());
            if (Double.valueOf(campainBean.getActivityFee()) == 0) {
                tvPrice.setText("免费");
                tvPriceUnit.setVisibility(View.GONE);
            } else {
                tvPrice.setText(campainBean.getActivityFee().replace(".0", ""));
            }
            tvSurplusNum.setText(Integer.valueOf(campainBean.getMemberNum())
                    - Integer.valueOf(campainBean.getActivityPassNum()) + "");
            tvOrgName.setText(campainBean.getOrganization().getName());
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    public void loadData() {
        showLoading();
        RequestParams params = new RequestParams();
        params.put("activityId", campainBean.getId());
        HMApiCampaign.getInstance().postDetail(this, params);
    }

    @OnClick(R.id.campaign_detail_rl_share)
    public void onShare(View v) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(campainBean.getName());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(HMApi.HOST
                + "activity/shareActivity.action?activityId="
                + campainBean.getId());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(campainBean.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        oks.setImageUrl(campainBean.getLogo());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(HMApi.HOST + "activity/shareActivity.action?activityId="
                + campainBean.getId());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://fir.im/ax5y");

        // 去除注释，演示在九宫格设置自定义的图标
        // Bitmap logo = BitmapFactory.decodeResource(getResources(),
        // R.drawable.ic_launcher);
        // String label = getResources().getString(R.string.app_name);
        // OnClickListener listener = new OnClickListener() {
        // public void onClick(View v) {
        // String text = "Customer Logo -- ShareSDK "
        // + ShareSDK.getSDKVersionName();
        // Toast.makeText(HMCampaignDetailAct.this, text,
        // Toast.LENGTH_SHORT).show();
        // }
        // };
        // oks.setCustomerLogo(logo, logo, label, listener);
        // 为EditPage设置一个背景的View
        // oks.setEditPageBackground();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 设置是否是直接分享
        oks.setSilent(false);

        ShareUtil.shareTaget(this, oks);
    }

    @OnClick(R.id.campaign_detail_ll_sign_up)
    public void onSignUp(View v) {
        user =((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        TipDialog.getInstance(this).setTitle("报名确认", null, null)
                .setContent("确认报名本次活动吗?", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                        RequestParams params = new RequestParams();
                        params.put("accountId", user.getId());
                        params.put("activityId", campainBean.getId());
                        HMApiCampaign.getInstance().postSignUp(HMCampaignDetailAct.this, params);
                    }
                }, new TipDialog.OnMCancelListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiCampaign.DETAIL)) {
            campainBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMCampaignBean.class);
            updateView();
        } else if (url.equals(HMApiCampaign.SIGN_UP)) {
            ToastUtil.toastAlways(HMCampaignDetailAct.this, "报名成功");
            campainBean
                    .setSignupNum(Integer.valueOf(campainBean.getSignupNum())
                            + 1 + "");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvContent.destroy();
    }

    @Nullable
    @OnClick(R.id.campaign_detail_iv_back)
    public void onBack(View v) {
        super.onBack(v);
    }
}
