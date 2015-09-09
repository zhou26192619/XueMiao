package cc.xuemiao.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiTeacher;
import cc.xuemiao.bean.HMImage;
import cc.xuemiao.bean.HMTeacherBean;
import cc.xuemiao.ui.view.GridImageView;
import cc.xuemiao.utils.GenderUtil;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 老师详细资料
 *
 * @author m
 */
public class HMTeacherDetailAct extends HMBaseAct {

    public static final int CODE_REQUEST = 518;
    public static final String BUNDLE_KEY = "teacherId";
    public static final String BUNDLE_ACCOUNTID = "accountId";
    public static final String BUNDLE_KEY_PERMISSION = "isMyself";

    @Bind(R.id.teacher_detail_iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.teacher_detail_tv_name)
    TextView tvName;

    @Bind(R.id.teacher_detail_tv_address)
    TextView tvAddress;

    @Bind(R.id.teacher_detail_tv_distance)
    TextView tvDistance;

    @Bind(R.id.teacher_detail_ll_openned_courses)
    LinearLayout llOpennedCourses;

    @Bind(R.id.teacher_detail_ll_make_friend)
    LinearLayout llShare;

    @Bind(R.id.teacher_detail_ll_call)
    LinearLayout llCall;

    @Bind(R.id.teacher_detail_ll_message)
    LinearLayout llMessage;

    @Bind(R.id.teacher_detail_et_info)
    EditText etInfo;

    @Bind(R.id.teacher_detail_iv_gender)
    ImageView ivGender;

    @Bind(R.id.teacher_detail_tv_age)
    TextView tvAge;

    @Bind(R.id.teacher_detail_ll_head)
    LinearLayout llHead;
    @Bind(R.id.teacher_detail_ll_root_in_body)
    LinearLayout llRootInBody;
    @Bind(R.id.teacher_detail_ll_images)
    LinearLayout llImages;

    private HMTeacherBean teacherBean;

    private String teacherId;
    private String accountID;
    private boolean isMyself;
    private GridImageView imagesView;
    private ArrayList<ImageBean> entities;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_teacher_detail);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        teacherId = bundle.getString(BUNDLE_KEY);
        accountID = bundle.getString(BUNDLE_ACCOUNTID);
        isMyself = bundle.getBoolean(BUNDLE_KEY_PERMISSION, false);
    }

    private void init() {
        hvHeadView.setTitle("老师资料");

        imagesView = new GridImageView(this, entities, 4, 90);
        llImages.addView(imagesView.getView());
        imagesView.setClickCallback(new GridImageView.OnClickCallback() {

            @Override
            public void click(View v, ImageBean bean, int position) {
                Bundle bundle = new Bundle();
                HMTouchImageAct.Config config = new HMTouchImageAct.Config();
                config.isNeedSave = true;
                bundle.putSerializable(HMTouchImageAct.BUNDLE_KEY_CONFIG,
                        config);
                bundle.putSerializable(HMTouchImageAct.BUNDLE_KEY, entities);
                bundle.putInt(HMTouchImageAct.BUNDLE_KEY_POSITION, position);
                HMNavUtil.goToNewAct(HMTeacherDetailAct.this,
                        HMTouchImageAct.class, bundle);
            }
        });
    }

    @Override
    public void updateView() {
        try {
            if (teacherBean != null) {
                teacherId = teacherBean.getId();
                tvName.setText(teacherBean.getName());
                accountID = teacherBean.getAccountId();
                String addr = teacherBean.getProvince() + teacherBean.getCity()
                        + teacherBean.getArea() + teacherBean.getAddress();
                if (StringUtil.isEmpty(teacherBean.getProvince())
                        || StringUtil.isEmpty(teacherBean.getCity())
                        || StringUtil.isEmpty(teacherBean.getArea())
                        || StringUtil.isEmpty(teacherBean.getAddress())) {
                    addr = "未设置";
                }
                tvAddress.setText(addr);
                etInfo.setText(teacherBean.getBrief());
                GenderUtil.setGenderImage(teacherBean.getGender(), ivGender);
                tvAge.setText(DateUtil.getAge(teacherBean.getBornDate())
                        + "岁");
                // 九宫格图片设置，图片类数据整理
                entities = new ArrayList<ImageBean>();
                if (teacherBean.getImages() != null
                        && teacherBean.getImages().size() > 0) {
                    for (int i = 0; i < teacherBean.getImages().size(); i++) {
                        HMImage im = teacherBean.getImages().get(i);
                        ImageBean item = new ImageBean();
                        item.setImageUrl(im.getHost() + im.getUrl());
                        entities.add(item);
                    }
                    imagesView.setData(entities);
                    llImages.setVisibility(View.VISIBLE);
                } else {
                    llImages.setVisibility(View.GONE);
                }
                MImageLoader.display(this, teacherBean.getLogo(), ivAvatar,
                        false, MImageLoader.DEFAULT_ANGLE);
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    public void loadData() {
        RequestParams params = new RequestParams();
        if (accountID == null) {
            params.put("id", teacherId);
        } else {
            params.put("accountId", accountID);
        }
        HMApiTeacher.getInstance().postDetail(this, params);
    }

    @OnClick(R.id.teacher_detail_ll_message)
    public void onMessage(View v) {
    }

    @OnClick(R.id.teacher_detail_ll_call)
    public void onCall(View v) {
        if (teacherBean.getPhone() == null) {
            ToastUtil.toastAlways(this, "该用户未填写电话号哦!");
            return;
        }
        TipDialog.getInstance(this).setContent("要打电话吗?", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {

                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + teacherBean.getPhone()));
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

    @OnClick(R.id.teacher_detail_ll_make_friend)
    public void add(View v) {
        try {
            if (accountID != null)
                addUser(accountID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @OnClick(R.id.teacher_detail_ll_openned_courses)
    public void onOpennedCourse(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(HMCourseListAct.BUNDLE_KEY_ID, teacherId);
        bundle.putInt(HMCourseListAct.BUNDLE_KEY_TYPE,
                HMCourseListAct.TYPE_TEACHER);
        HMNavUtil.goToNewAct(this, HMCourseListAct.class, bundle);
    }

    public void addUser(final String userid) {
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiTeacher.DETAIL)) {
            teacherBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMTeacherBean.class);
            if (teacherBean != null) {
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
