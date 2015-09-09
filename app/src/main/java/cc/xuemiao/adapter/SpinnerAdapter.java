package cc.xuemiao.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lib_common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cc.xuemiao.bean.KeyValueBean;


public class SpinnerAdapter extends BaseAdapter {
    private List<KeyValueBean> datas;
    private Context context;

    public SpinnerAdapter(Context context, List<KeyValueBean> datas) {
        super();
        this.context = context;
        setData(datas);
    }

    public void setData(List<KeyValueBean> datas) {
        if (datas == null) {
            this.datas = new ArrayList<KeyValueBean>();
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public KeyValueBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KeyValueBean bean = datas.get(position);
        if (convertView == null) {
            TextView tv = new TextView(context);
            tv.setText(bean.key);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(params);
            tv.setPadding(DensityUtil.dip2px(context, 6),
                    DensityUtil.dip2px(context, 6),
                    DensityUtil.dip2px(context, 6),
                    DensityUtil.dip2px(context, 6));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLines(1);
//			tv.setEllipsize(TruncateAt.END);
            convertView = tv;
        } else {
            ((TextView) convertView).setText(bean.key);
        }
        return convertView;
    }
}
