package cc.xuemiao.ui;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.dialog.GenderSelectDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.NavUtil;
import com.lib_common.util.PhotoUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiImage;
import cc.xuemiao.api.HMApiParent;
import cc.xuemiao.bean.HMParentBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 个人资料
 *
 * @author loar
 */
public class HMPersonalInfoAct extends HMBaseAct {

    public static final int CODE_REQUEST_NAME = 511;
    public static final int CODE_REQUEST_ACCOUNT_NAME = 512;
    public static final int CODE_REQUEST_NICKNAME = 513;
    public static final int CODE_REQUEST_PIC_CROP = 514;
    public static final int CODE_REQUEST = 510;
    public int CODE_RESULT;
    public static final String BUNDLE_KEY = "parentId";

    @Bind(R.id.personal_info_iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.personal_info_tv_account_name)
    TextView tvAccountName;

    @Bind(R.id.personal_info_ll_account_name)
    LinearLayout llAccountName;

    @Bind(R.id.personal_info_tv_nickname)
    TextView tvNickname;

    @Bind(R.id.personal_info_tv_name)
    TextView tvName;

    @Bind(R.id.personal_info_tv_gender)
    TextView tvGender;

    @Bind(R.id.personal_info_tv_age)
    TextView tvAge;

    @Bind(R.id.personal_info_tv_address)
    TextView tvAddress;

    private String parentId;

    protected HMParentBean parentBean;
    private HMUserBean user;
    private Uri tempUri;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_personal_info);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        parentId = bundle.getString(BUNDLE_KEY);
    }

    private void init() {
        hvHeadView.setTitle("个人资料");
        llAccountName.setClickable(false);
    }

    @Override
    public void updateView() {
        super.updateView();
        tvName.setText("未设置");
        tvAge.setText("未设置");
        tvAddress.setText("未设置");
        tvNickname.setText("未设置");
        if ("0".equals(user.getIsModify())) {
            llAccountName.setClickable(true);
        }
        if (parentBean != null) {
            if (!StringUtil.isEmpty(parentBean.getName())) {
                tvName.setText(parentBean.getName());
            }
            if (!StringUtil.isEmpty(user.getNikName())) {
                tvNickname.setText(user.getNikName());
            }
            tvAccountName.setText(user.getAccountName());
            MImageLoader.display(this, parentBean.getLogo(), ivAvatar, false,
                    MImageLoader.DEFAULT_ANGLE);
            if (GenderSelectDialog.FEMALE.equals(parentBean.getGender())) {
                tvGender.setText("女");
            } else {
                tvGender.setText("男");
            }
            try {
                if (parentBean.getBornDate() != null) {
                    int age = DateUtil.getAge(parentBean.getBornDate());
                    tvAge.setText(age + "");
                }
            } catch (Exception e) {
                ToastUtil.printErr(e);
            }
            if (!StringUtil.isEmpty(parentBean.getArea())) {
                tvAddress.setText(parentBean.getArea() + " "
                        + parentBean.getAddress());
            }
        }
    }

    @OnClick(R.id.personal_info_ll_address)
    public void onAddress(View v) {
        Intent intent = new Intent(this, HMSetAddressAct.class);
        startActivityForResult(intent, CODE_REQUEST);
    }

    @OnClick(R.id.personal_info_ll_account_name)
    public void onAccountName(View v) {
        Intent intent = new Intent(this, HMSetNameAct.class);
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TITLE, "账号设置");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TIP, "账号名(只能是数字和英文哦)");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TYPE,
                HMSetNameAct.BUNDLE_KEY_TYPE_ACCOUNT_NAME);
        startActivityForResult(intent, CODE_REQUEST_ACCOUNT_NAME);
    }

    @OnClick(R.id.personal_info_ll_nickname)
    public void onNickname(View v) {
        Intent intent = new Intent(this, HMSetNameAct.class);
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TITLE, "昵称设置");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TIP, "昵称");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TYPE,
                HMSetNameAct.BUNDLE_KEY_TYPE_NICKNAME);
        startActivityForResult(intent, CODE_REQUEST_NICKNAME);
    }

    @OnClick(R.id.personal_info_ll_name)
    public void onName(View v) {
        Intent intent = new Intent(this, HMSetNameAct.class);
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TITLE, "姓名设置");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TIP, "姓名");
        intent.putExtra(HMSetNameAct.BUNDLE_KEY_TYPE,
                HMSetNameAct.BUNDLE_KEY_TYPE_NAME);
        startActivityForResult(intent, CODE_REQUEST_NAME);
    }

    @OnClick(R.id.personal_info_iv_avatar)
    public void onClickAvatar(View v) {
        Bundle bundle = new Bundle();
        ArrayList<ImageListActivity.ImageBean> entities = new ArrayList<ImageBean>();
        ImageBean item = new ImageBean();
        item.setImageUrl(user.getLogo());
        entities.add(item);
        bundle.putSerializable(HMTouchImageAct.BUNDLE_KEY, entities);
        HMTouchImageAct.Config config = new HMTouchImageAct.Config();
        config.isNeedDelete = false;
        config.isNeedSave = true;
        bundle.putSerializable(HMTouchImageAct.BUNDLE_KEY_CONFIG, config);
        HMNavUtil.goToNewAct(this, HMTouchImageAct.class, bundle);
    }

    @OnClick(R.id.personal_info_ll_avatar)
    public void onAvatar(View v) {
        Intent intent = new Intent(this, ImageListActivity.class);
        ImageListActivity.Config config = new ImageListActivity.Config();
        config.maxSelectNum = 1;
        intent.putExtra(ImageListActivity.BUNDLE_KEY_CONFIG,
                config);
        startActivityForResult(intent, CODE_REQUEST);
    }

    @OnClick(R.id.personal_info_ll_age)
    public void onAge(View v) {
        int[] ytd;
        if (StringUtil.isEmpty(parentBean.getBornDate())) {
            ytd = DateUtil.getYTD(new Date());
        } else {
            ytd = DateUtil.getYTD(parentBean.getBornDate(), null);
        }
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        RequestParams params = new RequestParams();
                        params.put("bornDate", year + "-" + (monthOfYear + 1)
                                + "-" + dayOfMonth);
                        params.put("parentId", parentId);
                        HMApiParent
                                .getInstance().postUpdate(HMPersonalInfoAct.this, params);
                    }
                }, ytd[0], ytd[1], ytd[2]);
        datePickerDialog.show();
    }

    @OnClick(R.id.personal_info_ll_gender)
    public void onGender(View v) {
        GenderSelectDialog genderSelectDialog = new GenderSelectDialog(this,
                R.style.toast_box_dialog);
        genderSelectDialog.setGender(parentBean.getGender());
        genderSelectDialog.setSelectCallBack(new GenderSelectDialog.SelectCallBack() {

            @Override
            public void ok(String gender) {
                try {
                    RequestParams params = new RequestParams();
                    params.put("gender", gender);
                    params.put("parentId", parentId);
                    HMApiParent.getInstance().postUpdate(HMPersonalInfoAct.this, params);
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }
        });
        genderSelectDialog.show();
    }

    @Override
    public void loadData() {
        user = ((HMApp) getApplication()).getUser();
        if (user == null) {
            return;
        }
        parentBean = user.getParent();
        RequestParams params = new RequestParams();
        params.put("parentId", parentId);
        HMApiParent.getInstance().postDetail(this, params);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                              JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiParent.DETAIL)) {
            parentBean = GsonUtil.fromJsonObj(
                    jo.getAsJsonObject(HMApi.KEY_DATA), HMParentBean.class);
            user.setParent(parentBean);
            ((HMApp) getApplication()).saveUser(user);
            updateView();
        } else if (url.equals(HMApiParent.UPDATE)) {
            CODE_RESULT = RESULT_OK;
            loadData();
        } else if (url.equals(HMApiImage.UPLOAD_IMAGE)) {
            String logoUrl = jo.getAsJsonPrimitive(HMApi.KEY_DATA).getAsString();
            parentBean.setLogo(logoUrl);
            user.setLogo(logoUrl);
            user.setParent(parentBean);
            ((HMApp) getApplication()).getUserSP().saveUserBean(user);
            ToastUtil.toast(this, jo.get(HMApi.KEY_MSG));
            CODE_RESULT = RESULT_OK;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST
                && resultCode == HMSetAddressAct.CODE_RESULT_OK) {
            String province = data
                    .getStringExtra(HMSetAddressAct.RESULT_KEY_PROVINCE);
            String city = data.getStringExtra(HMSetAddressAct.RESULT_KEY_CITY);
            String area = data.getStringExtra(HMSetAddressAct.RESULT_KEY_AREA);
            String address = data
                    .getStringExtra(HMSetAddressAct.RESULT_KEY_ADDRESS);
            RequestParams params = new RequestParams();
            params.put("province", province);
            params.put("city", city);
            params.put("area", area);
            params.put("address", address);
            params.put("parentId", parentId);
            HMApiParent.getInstance().postUpdate(this, params);
        } else if (requestCode == CODE_REQUEST
                && resultCode == ImageListActivity.CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_OK) {
            List<ImageBean> entities = (ArrayList<ImageBean>) data
                    .getSerializableExtra(ImageListActivity.RESULT_NAME);
            if (entities == null || entities.size() == 0) {
                return;
            }
            tempUri = NavUtil.goToPicCrop(this, "file://"
                    + entities.get(0).getImageUrl(), CODE_REQUEST_PIC_CROP);
        } else if ((requestCode == CODE_REQUEST_NAME
                || requestCode == CODE_REQUEST_ACCOUNT_NAME || requestCode == CODE_REQUEST_NICKNAME)
                && resultCode == HMSetNameAct.CODE_RESULT_OK) {
            loadData();
        } else if (requestCode == CODE_REQUEST_PIC_CROP
                && resultCode == RESULT_OK) {
            RequestParams params = new RequestParams();
            String url = tempUri.getPath();
            InputStream is = PhotoUtil.uploadCompressBitmap(this, url);
            params.put(url, is,
                    url.substring(url.lastIndexOf("/") + 1, url.length()),
                    "image/*");
            params.put("accountId", user.getId());
            HMApiImage.getInstance().postUploadImage(this, params);
            showLoading();
        }
    }

    @Override
    public void onBack(View v) {
        setResult(CODE_RESULT);
        super.onBack(v);
    }
}
