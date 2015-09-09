package cc.xuemiao.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.lib_common.util.PCAUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.adapter.SpinnerAdapter;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.bean.KeyValueBean;
import cc.xuemiao.utils.LocationUtil;

public class HMSetAddressAct extends HMBaseAct {

    public static final int CODE_RESULT_OK = 200;
    public static final int CODE_RESULT_ERR = 201;
    public static final String RESULT_KEY_PROVINCE = "province";
    public static final String RESULT_KEY_CITY = "city";
    public static final String RESULT_KEY_AREA = "area";
    public static final String RESULT_KEY_ADDRESS = "address";

    @Bind(R.id.set_address_tv_loc_address)
    TextView tvLocAddress;

    @Bind(R.id.set_address_sp_province)
    Spinner spProvince;

    @Bind(R.id.set_address_sp_city)
    Spinner spCity;

    @Bind(R.id.set_address_sp_area)
    Spinner spArea;

    @Bind(R.id.set_address_et_address)
    EditText etAddress;

    private List<KeyValueBean> provinces;

    private List<KeyValueBean> cities;

    private List<KeyValueBean> areas;

    private SpinnerAdapter provinceAdapter;

    private SpinnerAdapter cityAdapter;

    private SpinnerAdapter areaAdapter;

    protected String province;
    protected String city;
    protected String area;
    protected String address;

    private Map<String, Map<String, List<String>>> pca;

    protected String locProvince;
    protected String locAddress;
    protected String locCity;
    protected String locArea;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_set_address);
        init();
        setListener();
    }

    private void init() {
        hvHeadView.setTitle("地址设置");
        provinceAdapter = new SpinnerAdapter(this, provinces);
        cityAdapter = new SpinnerAdapter(this, cities);
        areaAdapter = new SpinnerAdapter(this, areas);
        spProvince.setAdapter(provinceAdapter);
        spCity.setAdapter(cityAdapter);
        spArea.setAdapter(areaAdapter);
    }

    private void setListener() {
        spProvince.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                province = provinces.get(position).key;
                getCities(province);
                getAreas(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                city = cities.get(position).key;
                getAreas(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spArea.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                area = areas.get(position).key;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void loadData() {
        pca = PCAUtil.getPCA(this, "address.json");
        getProvinces();
        HMUserBean user = ((HMApp)getApplication()).getUser();
        if (user != null) {
            defaultPCA(user.getParent().getProvince(), user.getParent()
                    .getCity(), user.getParent().getArea());
        }

        LocationUtil.getPositionInfoByGaode(this, 1000, new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
                            locProvince = aMapLocation.getProvince();
                            locCity = aMapLocation.getCity();
                            locArea = aMapLocation.getDistrict();
                            locAddress = aMapLocation.getStreet();
                            tvLocAddress.setText(locProvince + " " + locCity + " "
                                    + locArea + " " + locAddress);
                            LocationUtil.stopGaodeLocation();
                        }
                    }

                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    private void getProvinces() {
        KeyValueBean keyValueBean;
        provinces = new ArrayList<KeyValueBean>();
        Set<String> keySet = pca.keySet();
        for (String key : keySet) {
            keyValueBean = new KeyValueBean();
            keyValueBean.key = key;
            keyValueBean.value = key;
            provinces.add(keyValueBean);
        }
        provinceAdapter.setData(provinces);
    }

    private void getCities(String key) {
        province = key;
        KeyValueBean keyValueBean;
        cities = new ArrayList<KeyValueBean>();
        Map<String, List<String>> cs = pca.get(province);
        Set<String> cKeySet = cs.keySet();
        for (String k : cKeySet) {
            keyValueBean = new KeyValueBean();
            keyValueBean.key = k;
            keyValueBean.value = k;
            cities.add(keyValueBean);
        }
        cityAdapter.setData(cities);
    }

    private void getAreas(int position) {
        city = cities.get(position).key;
        KeyValueBean keyValueBean;
        areas = new ArrayList<KeyValueBean>();
        List<String> as = pca.get(province).get(city);
        for (String k : as) {
            keyValueBean = new KeyValueBean();
            keyValueBean.key = k;
            keyValueBean.value = k;
            areas.add(keyValueBean);
        }
        areaAdapter.setData(areas);
    }

    /**
     * 设置默认选中的省市区
     */
    private void defaultPCA(String province, String city, String area) {
        try {
            for (int i = 0; i < provinces.size(); i++) {
                if (provinces.get(i).key.equals(province)) {
                    spProvince.setSelection(i);
                    getCities(province);
                    break;
                }
            }
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).key.equals(city)) {
                    spCity.setSelection(i);
                    getAreas(i);
                    break;
                }
            }
            for (int i = 0; i < areas.size(); i++) {
                if (areas.get(i).key.equals(area)) {
                    spArea.setSelection(i);
                    break;
                }
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @OnClick({R.id.set_address_ll_loc_address, R.id.common_head_rl_right_icon})
    public void onAddress(View v) {
        if (v.getId() == R.id.set_address_ll_loc_address) {
            province = locProvince;
            city = locCity;
            area = locArea;
            address = locAddress;
        }
        if (v.getId() == R.id.common_head_rl_right_icon) {
            address = etAddress.getText().toString();
            if (StringUtil.isEmpty(address)) {
                ToastUtil.toastAlways(this, "地址不能为空哦！");
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra(RESULT_KEY_PROVINCE, province);
        data.putExtra(RESULT_KEY_CITY, city);
        data.putExtra(RESULT_KEY_AREA, area);
        data.putExtra(RESULT_KEY_ADDRESS, address);
        setResult(CODE_RESULT_OK, data);
        onBack(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationUtil.stopGaodeLocation();
    }
}
