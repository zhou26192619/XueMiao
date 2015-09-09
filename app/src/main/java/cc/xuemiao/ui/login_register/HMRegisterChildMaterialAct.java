package cc.xuemiao.ui.login_register;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.DateUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.NavUtil;
import com.lib_common.util.PhotoUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiChild;
import cc.xuemiao.api.HMApiImage;
import cc.xuemiao.bean.HMChildBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.GenderUtil;

/**
 * 添加小孩资料填写
 *
 * @author m
 */
public class HMRegisterChildMaterialAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "childBean";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final int TYPE_ADD = 0;
    public static final int TYPE_UPDATE = 1;

    public static final int CODE_REQUEST = 50602;
    public static final int CODE_REQUEST_PIC_CROP = 50603;

    @Bind(R.id.register_child_material_iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.register_child_material_et_name)
    EditText etName;

    @Bind(R.id.register_child_material_ll_date)
    LinearLayout llDate;

    @Bind(R.id.register_child_material_tv_date)
    TextView tvDate;

    @Bind(R.id.register_child_material_rg_gender)
    RadioGroup rgGender;
    @Bind(R.id.register_child_material_rb_male)
    RadioButton rbMale;
    @Bind(R.id.register_child_material_rb_female)
    RadioButton rbFemale;

    @Bind(R.id.register_child_material_tv_submit)
    TextView tvSubmit;

    private String gender = GenderUtil.GENDER_MALE;

    private DatePickerDialog datePickerDialog;

    private int[] ytd;
    private HMChildBean childBean;

    private HMUserBean user;
    private int type;
    private Uri tempUri;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_register_child_material);
        init();
        initListenrs();
        updateView();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        childBean = (HMChildBean) bundle.getSerializable(BUNDLE_KEY);
        type = bundle.getInt(BUNDLE_KEY_TYPE);
    }

    private void init() {
        try {
            hvHeadView.setTitle("小孩资料填写");
            if (childBean == null) {
                initDateDialog(DateUtil.datetimeToString(new Date(),
                        "yyyy-MM-dd"));
            } else {
                initDateDialog(childBean.getBornDate());
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    /**
     * 初始化日期选择器
     *
     * @param str
     */
    private void initDateDialog(String str) {
        try {
            ytd = DateUtil.getYTD(str,null);
            tvDate.setText(ytd[0] + "." + (ytd[1] + 1) + "." + ytd[2]);
            datePickerDialog = new DatePickerDialog(
                    HMRegisterChildMaterialAct.this, new OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    tvDate.setText(year + "." + (monthOfYear + 1) + "."
                            + dayOfMonth);
                }
            }, ytd[0], ytd[1], ytd[2]);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    public void updateView() {
        if (childBean != null) {
            etName.setText(childBean.getName());
            if ("f".equals(childBean.getGender())) {
                rgGender.check(rbFemale.getId());
            } else {
                rgGender.check(rbMale.getId());
            }
            MImageLoader.displayWithDefaultOptions(this, childBean.getLogo(),
                    ivAvatar);
            initDateDialog(childBean.getBornDate());
        } else {
            childBean = new HMChildBean();
        }
    }

    private void initListenrs() {
        rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.register_child_material_rb_female) {
                    gender = GenderUtil.GENDER_FEMALE;
                } else if (checkedId == R.id.register_child_material_rb_male) {
                    gender = GenderUtil.GENDER_MALE;
                }
            }
        });
        llDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                datePickerDialog.show();
            }
        });
        ivAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HMRegisterChildMaterialAct.this,
                        ImageListActivity.class);
                ImageListActivity.Config config = new ImageListActivity.Config();
                config.maxSelectNum = 1;
                intent.putExtra(
                        ImageListActivity.BUNDLE_KEY_CONFIG,
                        config);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });
    }

    // onClickListener
    @OnClick(R.id.register_child_material_tv_submit)
    public void onSubmit(final View v) {
        if (type == TYPE_ADD) {
            addChild();
        } else if (type == TYPE_UPDATE) {
            updateChild();
        }
    }

    private void addChild() {
        String name = etName.getText().toString();
        String date = tvDate.getText().toString().replace(".", "-");
        if (StringUtil.isEmpty(name)) {
            ToastUtil.getInstance().showShortToast(this, "请输入您的名字");
            return;
        }
        if (!name.matches(BaseConfig.PATTERN_NAME)) {
            ToastUtil.toastAlways(this, "请输入正确格式的名字");
            return;
        }
        showLoading();
        HMUserBean userBean =((HMApp)getApplication()).getUserSP()
                .getUserBean(HMUserBean.class);
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("gender", gender);
        params.put("bornDate", date);
        params.put("parentId", userBean.getRoleId());
        HMApiChild.getInstance().postAddChild(this, params);
    }

    private void updateChild() {
        String name = etName.getText().toString();
        String date = tvDate.getText().toString().replace(".", "-");
        if (StringUtil.isEmpty(name)) {
            ToastUtil.getInstance().showShortToast(this, "请输入您的名字");
            return;
        }
        if (!name.matches(BaseConfig.PATTERN_NAME)) {
            ToastUtil.toastAlways(this, "请输入正确格式的名字");
            return;
        }
        user = ((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        showLoading();
        RequestParams params = new RequestParams();
        params.put("childId", childBean.getId());
        params.put("name", name);
        params.put("gender", gender);
        params.put("bornDate", date);
        params.put("parentId", user.getRoleId());
        HMApiChild.getInstance().postUpdate(this, params);
    }

    private void uploadLogo() {
        user =((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        if (childBean.getLogo() != null
                && !childBean.getLogo().startsWith("http://")) {
            RequestParams params = new RequestParams();
            params.put(
                    "logo",
                    PhotoUtil.uploadCompressBitmap(this, childBean.getLogo()),
                    childBean.getLogo().substring(
                            childBean.getLogo().lastIndexOf("/") + 1,
                            childBean.getLogo().length()), "image/*");
            params.put("accountId", user.getId());
            params.put("childId", childBean.getId());
            HMApiImage.getInstance().postUploadChildLogo(this, params);
        } else {
            hideLoading();
            ((HMApp)getApplication()).updateActivities();
            onBack(null);
        }
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiChild.ADD_CHILD)) {
            String id = jo.getAsJsonPrimitive(HMApi.KEY_DATA).getAsString();
            childBean.setId(id);
            uploadLogo();
        } else if (url.equals(HMApiChild.UPDATE)) {
            uploadLogo();
        } else if (url.equals(HMApiImage.UPLOAD_CHILD_LOGO)) {
            ((HMApp)getApplication()).updateActivities();
            onBack(null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST
                && resultCode == ImageListActivity.CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_OK) {
            ArrayList<ImageListActivity.ImageBean> entities = (ArrayList<ImageListActivity.ImageBean>) data
                    .getSerializableExtra(ImageListActivity.RESULT_NAME);
            if (entities != null && entities.size() > 0) {
                tempUri = NavUtil.goToPicCrop(this,
                        "file://" + entities.get(0).getImageUrl(),
                        CODE_REQUEST_PIC_CROP);
            }
        } else if (requestCode == CODE_REQUEST_PIC_CROP
                && resultCode == RESULT_OK) {
            String url = tempUri.getPath();
            childBean.setLogo(url);
            childBean.setLogo(url);
            MImageLoader.displayWithDefaultOptions(this, url, ivAvatar);
        }
    }
}
