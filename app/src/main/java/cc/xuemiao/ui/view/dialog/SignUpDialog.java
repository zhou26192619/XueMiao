package cc.xuemiao.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.net.HttpJsonCallback;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiSignUp;
import cc.xuemiao.bean.HMChildBean;

/**
 * 报名弹窗
 *
 * @author Yaolan
 */
public class SignUpDialog extends Dialog {

    private Context context;
    private ListView listView;
    private TextView cancel;
    private TextView ok;
    private List<HMChildBean> childBeans;
    private ChildAdapter adapter;
    private String parentId;
    private String courseId;
    private String childId;

    public SignUpDialog(Context context, int theme, String parentId,
                        String courseId, List<HMChildBean> list) {
        super(context, theme);
        init(context, parentId, courseId, list);
    }

    protected SignUpDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener, String parentId, String courseId,
                           List<HMChildBean> list) {
        super(context, cancelable, cancelListener);
        init(context, parentId, courseId, list);
    }

    public SignUpDialog(Context context, String parentId, String courseId,
                        List<HMChildBean> list) {
        super(context);
        init(context, parentId, courseId, list);
    }

    private void init(Context context, String parentId, String courseId,
                      List<HMChildBean> list) {
        this.context = context;
        this.parentId = parentId;
        this.courseId = courseId;
        this.childBeans = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign_up);
        try {
            listView = (ListView) findViewById(R.id.sign_up_lv_children);
            ok = (TextView) findViewById(R.id.sign_up_tv_ok);
            cancel = (TextView) findViewById(R.id.sign_up_tv_cancel);
            adapter = new ChildAdapter();
            listView.setAdapter(adapter);
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    protected void signUp() {
        RequestParams params = new RequestParams();
        params.put("courseId", courseId);
        params.put("parentId", parentId);
        params.put("childId", childId);
        HMApiSignUp.getInstance().postSignUp(context, params, new HttpJsonCallback() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JsonObject jo) {
                super.onSuccess(statusCode, headers, jo);
                ToastUtil.toastAlways(context, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String str,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, str, throwable);
                ToastUtil.toastAlways(context, str);
                dismiss();
            }
        });
    }

    class ChildAdapter extends BaseAdapter {

        public ChildAdapter() {
            super();
            setData();
        }

        private void setData() {
            if (childBeans == null) {
                childBeans = new ArrayList<HMChildBean>();
            }
        }

        @Override
        public int getCount() {
            return childBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return childBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            VH vh = null;
            if (convertView == null) {
                vh = new VH();
                convertView = View.inflate(context,
                        R.layout.adapter_dialog_sign_up, null);
                vh.tvName = (TextView) convertView.findViewById(R.id.name);
                vh.ivAvatar = (ImageView) convertView.findViewById(R.id.logo);
                vh.ivChoose = (ImageView) convertView
                        .findViewById(R.id.checkBox);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            HMChildBean bean = childBeans.get(position);
            vh.update(bean);
            setListener(vh, bean, position);
            return convertView;
        }

        private void setListener(final VH vh, final HMChildBean bean,
                                 final int position) {
            vh.ivChoose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (HMChildBean item : childBeans) {
                        item.setChecked(false);
                    }
                    ok.setEnabled(false);
                    ok.setBackgroundResource(R.drawable.gray_rounded_corner);
                    boolean isChecked = (Boolean) vh.ivChoose.getTag();
                    if (!isChecked) {
                        childBeans.get(position).setChecked(true);
                        childId = bean.getId();
                        ok.setEnabled(true);
                        ok.setBackgroundResource(R.drawable.green_rounded_corner_selector);
                    }
                    vh.ivChoose.setTag(!isChecked);
                    notifyDataSetChanged();
                }
            });
        }

        class VH {
            public ImageView ivChoose;
            public TextView tvName;
            public ImageView ivAvatar;

            void update(HMChildBean bean) {
                ivChoose.setTag(bean.isChecked());
                if (bean.isChecked()) {
                    ivChoose.setImageResource(R.mipmap.checked_s);
                } else {
                    ivChoose.setImageResource(R.mipmap.checked);
                }
                tvName.setText(bean.getName());
            }
        }
    }
}
