package cc.xuemiao.ui.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;

import java.util.List;

import cc.xuemiao.R;
import cc.xuemiao.bean.KeyValueBean;


public class TypeSelectorView extends LinearLayout {

    private Context context;
    private View vDivider;
    private TextView tvType;
    private TextView tvType2;
    private List<KeyValueBean> typeDatas;
    private List<KeyValueBean> typeDatas2;
    private PopupWindow popupWindow;
    private CommonAdapter<KeyValueBean> adapter;
    private ListView listView;
    private int clickTypeId;
    private int typeWidth;
    private int padding = 10;

    public TypeSelectorView(Context context) {
        super(context);
        init(context);
    }

    public TypeSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.view_type_selector, this);
        tvType = (TextView) findViewById(R.id.type_selector_tv_type);
        tvType2 = (TextView) findViewById(R.id.type_selector_tv_type2);
        vDivider = findViewById(R.id.type_selector_v_divider);
        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickTypeId = v.getId();
                if (clickTypeId == R.id.type_selector_tv_type) {
                    showWindow(v, typeDatas);
                } else if (clickTypeId == R.id.type_selector_tv_type2) {
                    showWindow(v, typeDatas2);
                }
            }
        };
        tvType.setOnClickListener(clickListener);
        tvType2.setOnClickListener(clickListener);
        tvType.post(new Runnable() {

            @Override
            public void run() {
                typeWidth = tvType.getWidth();
            }
        });
    }

    private void showWindow(View parent, List<KeyValueBean> datas) {
        if (adapter == null || listView == null) {
            listView = new ListView(context);
            listView.setBackgroundResource(R.drawable.orange_border_rounded_corner);
            adapter = new CommonAdapter<KeyValueBean>(context, datas,
                    R.layout.adapter_dialog_spinner) {

                @Override
                public void dealViews(ViewHolder holder,
                                      List<KeyValueBean> datas, final int position) {
                    final KeyValueBean bean = datas.get(position);
                    TextView tvName = holder.getViewById(
                            R.id.dialog_spinner_tv_name, TextView.class);
                    LinearLayout llRoot = holder.getViewById(
                            R.id.dialog_spinner_ll_root, LinearLayout.class);
                    tvName.setText(bean.key);
                    llRoot.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (clickTypeId == R.id.type_selector_tv_type) {
                                tvType.setText(bean.key);
                                if (onTabChangedCallback != null) {
                                    onTabChangedCallback.onTypeChanged(v, bean,
                                            position);
                                }
                            } else if (clickTypeId == R.id.type_selector_tv_type2) {
                                tvType2.setText(bean.key);
                                if (onTabChangedCallback != null) {
                                    onTabChangedCallback.onType2Changed(v,
                                            bean, position);
                                }
                            }
                            popupWindow.dismiss();
                        }
                    });
                }
            };
            listView.setAdapter(adapter);
            LayoutParams params = new LayoutParams(typeWidth - padding * 2,
                    LayoutParams.WRAP_CONTENT);
            listView.setLayoutParams(params);
        }
        if (popupWindow == null) {
            LayoutParams llP = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(llP);
            ll.addView(listView);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(ll, typeWidth - padding * 2,
                    LayoutParams.WRAP_CONTENT);
            // 使其聚集
            popupWindow.setFocusable(true);
            // 设置允许在外点击消失
            popupWindow.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        adapter.setDatas(datas);
        popupWindow.showAsDropDown(parent, padding, padding);
    }

    public void setTypeDatas(List<KeyValueBean> typeDatas, int selectIndex) {
        this.typeDatas = typeDatas;
        tvType.setText(this.typeDatas.get(selectIndex).key);
    }

    public void setTypeDatas2(List<KeyValueBean> typeDatas2, int selectIndex) {
        this.typeDatas2 = typeDatas2;
        tvType2.setText(this.typeDatas2.get(selectIndex).key);
    }

    public void setTypeVisibility(int visibility) {
        vDivider.setVisibility(visibility);
        tvType.setVisibility(visibility);
    }

    public void setType2Visibility(int visibility) {
        vDivider.setVisibility(visibility);
        tvType2.setVisibility(visibility);
    }

    private OnTabChangedCallback onTabChangedCallback;

    public void setOnTabChangedCallback(
            OnTabChangedCallback onTabChangedCallback) {
        this.onTabChangedCallback = onTabChangedCallback;
    }

    public interface OnTabChangedCallback {
        public void onTypeChanged(View view, KeyValueBean bean, int index);

        public void onType2Changed(View view, KeyValueBean bean, int index);
    }

}
