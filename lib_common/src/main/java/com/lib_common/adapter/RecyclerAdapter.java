package com.lib_common.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.ViewGroup;

public abstract class RecyclerAdapter<T> extends Adapter<ViewHolder> {

    private Context context;
    private List<T> datas;
    private int layoutId;

    public RecyclerAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        setDatas(datas);
    }

    public void setDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        onBindViewHolder(vh, datas, position);
    }

    public abstract void onBindViewHolder(ViewHolder vh, List<T> datas,
                                          int position);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return ViewHolder.getInstance(context, layoutId, null, viewGroup);
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
        this.pageSize = firstPage;
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
     * @return
     */
    public boolean hasMorePage() {
        return pageSize * (pageIndex - firstPage + 1) < totalSize;
    }

    /**
     * 根据返回的集合数量判断是否还有下一页
     * @param count
     * @return
     */
    public boolean hasMorePage(int count) {
        return count == pageSize;
    }

    // ==================== 是否请求中 ========

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

}
