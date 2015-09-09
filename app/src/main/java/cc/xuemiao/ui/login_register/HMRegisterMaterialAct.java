package cc.xuemiao.ui.login_register;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.DateUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiUser;
import cc.xuemiao.bean.HMParentBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.utils.HMNavUtil;

/**
 * 家长个人资料填写
 *
 * @author m
 */
public class HMRegisterMaterialAct extends HMBaseAct {
    @Bind(R.id.common_head_rl_back)
    RelativeLayout ivback;

    @Bind(R.id.common_head_tv_title)
    TextView tvTitle;

    @Bind(R.id.register_material_et_name)
    EditText etName;

    @Bind(R.id.register_material_ll_date)
    LinearLayout llDate;

    @Bind(R.id.register_material_tv_date)
    TextView tvDate;

    @Bind(R.id.register_material_rg_gender)
    RadioGroup rgGender;

    @Bind(R.id.register_material_tv_submit)
    TextView tvSubmit;

    private String gender = "m";

    private DatePickerDialog datePickerDialog;

    private int[] ytd;

    private String name;

    private String date;

    private HMUserBean user;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_register_material);
//        setTouchOutsideToHideKeyboard();
        init();
        initListenrs();
    }

    private void init() {
        tvTitle.setText("家长资料填写");
        ivback.setVisibility(View.INVISIBLE);
        ytd = DateUtil.getYTD(new Date());
        tvDate.setText((ytd[0] - 24) + "." + (ytd[1] + 1) + "." + ytd[2]);
        datePickerDialog = new DatePickerDialog(HMRegisterMaterialAct.this,
                new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvDate.setText(year + "." + (monthOfYear + 1) + "."
                                + dayOfMonth);
                    }
                }, (ytd[0] - 24), ytd[1], ytd[2]);
    }

    private void initListenrs() {
        rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.register_material_rb_female) {
                    gender = "f";
                }
                if (checkedId == R.id.register_material_rb_male) {
                    gender = "m";
                }
            }
        });
        llDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                datePickerDialog.show();
            }
        });
    }

    // onClickListener
    @OnClick(R.id.register_material_tv_submit)
    public void onSubmit(View v) {
        user =((HMApp)getApplication()).getUser();
        if (user == null) {
            return;
        }
        name = etName.getText().toString();
        date = tvDate.getText().toString().replace(".", "-");
        if (StringUtil.isEmpty(name)) {
            ToastUtil.toastAlways(this, "请输入您的名字");
            return;
        }
        if (!name.matches(BaseConfig.PATTERN_NAME)) {
            ToastUtil.toastAlways(this, "请输入正确的名字格式");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("accountId", user.getId());
        params.put("name", name);
        params.put("gender", gender);
        params.put("bornDate", date);
        HMApiUser.getInstance().postCommitMaterial(this, params);
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        HMParentBean parentBean = new HMParentBean();
        parentBean.setName(name);
        parentBean.setBornDate(date);
        parentBean.setGender(gender);
        user.setParent(parentBean);
        ((HMApp)getApplication()).getUserSP().saveUserBean(user);
        Bundle bundle = new Bundle();
        bundle.putInt(HMRegisterChildMaterialAct.BUNDLE_KEY_TYPE,
                HMRegisterChildMaterialAct.TYPE_ADD);
        HMNavUtil.goToNewAct(this, HMRegisterChildMaterialAct.class, bundle);
        onBack(null);
        ((HMApp)getApplication()).updateActivities();
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                    Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsBoolean());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                                 String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
