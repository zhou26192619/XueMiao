package com.easemob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.bean.HMUserBean;

/**
 * Created by LoaR on 15/9/8.
 */
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<EMMessage> datas;
    private HMUserBean user;

    public ChatAdapter(Context context, List<EMMessage> datas) {
        super();
        this.context = context;
        setDatas(datas);
    }


    public void setDatas(List<EMMessage> datas) {
        if (datas == null) {
            this.datas = new ArrayList<EMMessage>();
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<EMMessage> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TYPE_LEFT) {
           return setLeftView(position,convertView,parent);
        } else if (getItemViewType(position) == TYPE_RIGHT) {
            return setRightView(position, convertView, parent);
        }
        return null;
    }

    /**
     * 左视图
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View setLeftView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context,
                R.layout.chat_adapter_record_left, convertView,
                parent);
        ImageView ivLogo = holder.getViewById(
                R.id.chat_record_left_iv_logo, ImageView.class);
        TextView tvContent = holder.getViewById(
                R.id.chat_record_left_tv_content, TextView.class);
        try {
            EMMessage bean = datas.get(position);
            tvContent.setText(bean.getBody().toString());
            MImageLoader.display(context, bean.getFrom(),
                    ivLogo, MImageLoader.DEFAULT_IMAGE, true, 40);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        return holder.getConvertView();
    }

    /**
     * 左视图
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View setRightView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context,
                R.layout.chat_adapter_record_right, convertView,
                parent);
        ImageView ivLogo = holder.getViewById(
                R.id.chat_record_right_iv_logo, ImageView.class);
        TextView tvContent = holder.getViewById(
                R.id.chat_record_right_tv_content, TextView.class);
        try {
            EMMessage bean = datas.get(position);
            tvContent.setText(bean.getBody().toString());
            MImageLoader.display(context, bean.getFrom(),
                    ivLogo, MImageLoader.DEFAULT_IMAGE, true, 40);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        return holder.getConvertView();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private final int TYPE_LEFT = 0;
    private final int TYPE_RIGHT = 1;

    @Override
    public int getItemViewType(int position) {
        if (user == null) {
            user = HMApp.getInstance().getUser();
            if (user == null) {
                return TYPE_LEFT;
            }
        }
        EMMessage bean = datas.get(position);
        if (bean.getUserName().equals(user.getNikName())) {
            return TYPE_RIGHT;
        }
        return TYPE_LEFT;
    }

    // ==================== 数据页 ========

    private int pageSize = 15;
    private int pageIndex = 1;
    private int totalSize = pageSize + 1;
    private int firstPage = pageIndex;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置请求第一页的下标从firstopage开始
     *
     * @param firstPage
     */
    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
        setPageIndex(firstPage);
    }

    public void resetPageIndex() {
        this.pageIndex = firstPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * 下一页
     */
    public void nextPage() {
        this.pageIndex++;
    }

    /**
     * 上一页
     */
    public void prePage() {
        this.pageIndex--;
    }

    public boolean isFirstPage() {
        return firstPage == pageIndex;
    }

    /**
     * 根据总数判断是否还有下一页
     *
     * @return
     */
    public boolean hasMorePage() {
        return pageSize * (pageIndex - firstPage + 1) < totalSize;
    }

    /**
     * 根据返回的集合数量判断是否还有下一页
     *
     * @param count
     * @return
     */
    public boolean hasMorePage(int count) {
        return count == pageSize;
    }

    // ==================== 是否请求中 ========================================================================

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
