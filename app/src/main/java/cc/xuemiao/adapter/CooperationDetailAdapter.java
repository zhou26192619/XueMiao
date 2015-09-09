package cc.xuemiao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;

import cc.xuemiao.R;
import cc.xuemiao.bean.HMQABean;
import cc.xuemiao.bean.HMQABean.HMAnswerBean;


/**
 * 我的互助问答的适配器
 *
 * @author admin
 */
public class CooperationDetailAdapter extends BaseAdapter {

    private Context context;
    /**
     * 日记列表集合
     */
    private List<HMAnswerBean> answerBeans;
    private HMQABean qaBean;

    public CooperationDetailAdapter(Context context, HMQABean qaBean,
                                    List<HMAnswerBean> answerBeans) {
        super();
        this.context = context;
        this.qaBean = qaBean;
        setData(answerBeans);
    }

    public void setData(List<HMAnswerBean> answerBeans) {
        if (answerBeans == null) {
            this.answerBeans = new ArrayList<HMQABean.HMAnswerBean>();
        } else {
            this.answerBeans = answerBeans;
        }
        notifyDataSetChanged();
    }

    public void addData(List<HMAnswerBean> answerBeans) {
        if (answerBeans != null) {
            this.answerBeans.addAll(answerBeans);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return answerBeans.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return answerBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private final int FIRST_TYPE = 0;
    private final int OTHERS_TYPE = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_TYPE;
        } else {
            return OTHERS_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == FIRST_TYPE) {
            convertView = setFirstView(position, convertView, parent);
        } else if (getItemViewType(position) == OTHERS_TYPE) {
            convertView = setItemView(position - 1, convertView, parent);
        }
        return convertView;
    }

    /**
     * 设置第一条的布局（因为和普通的不同）
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View setFirstView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context,
                R.layout.adapter_cooperation_detail_question, convertView,
                parent);
        ImageView ivLogo = holder.getViewById(
                R.id.adapter_cooperation_detail_iv_logo, ImageView.class);
        TextView tvContent = holder.getViewById(
                R.id.adapter_cooperation_detail_tv_content, TextView.class);
        TextView tvDatetime = holder.getViewById(
                R.id.adapter_cooperation_detail_tv_datetime, TextView.class);
        TextView tvName = holder.getViewById(
                R.id.adapter_cooperation_detail_tv_name, TextView.class);
        try {
            tvContent.setText(qaBean.getQuestionContent());
            tvDatetime.setText(qaBean.getCreateDate());
            tvName.setText(qaBean.getAccount().getNikName());
            MImageLoader.display(context, qaBean.getAccount().getParent().getLogo(),
                    ivLogo, MImageLoader.DEFAULT_IMAGE, true, 40);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        return holder.getConvertView();
    }

    /**
     * 设置list每条数据的布局
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View setItemView(int position, View convertView, ViewGroup parent) {
        final HMAnswerBean bean = answerBeans.get(position);
        ViewHolder holder = ViewHolder
                .getInstance(context,
                        R.layout.adapter_cooperation_detail_answer,
                        convertView, parent);
        TextView tvContent = holder.getViewById(
                R.id.cooperation_answer_tv_content, TextView.class);
        TextView tvDatetime = holder.getViewById(
                R.id.cooperation_answer_tv_datetime, TextView.class);
        final ImageView ivReply = holder.getViewById(
                R.id.cooperation_answer_iv_reply, ImageView.class);
        LinearLayout llRoot = holder.getViewById(
                R.id.cooperation_answer_ll_root, LinearLayout.class);
        TextView tvName = holder.getViewById(R.id.cooperation_answer_tv_name,
                TextView.class);
        try {
            ivReply.setVisibility(View.GONE);
            tvDatetime.setText(bean.getCreateDate());
            tvName.setText(bean.getAccount().getNikName());
            String[] ss = bean.getAnswerContent().split(" ");
            if (ss.length == 2) {
                StringUtil.setDifferentFontTextView(tvContent, ss[0],
                        Color.parseColor("#ffaf01"), 12, " " + ss[1],
                        Color.parseColor("#939393"), 12, context);
            } else {
                tvContent.setText(bean.getAnswerContent());
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
        llRoot.setOnClickListener(new com.lib_common.listener.OnClickListener() {

            @Override
            public void onClick(View v, long clickTime) {
                if (onReplyListener != null) {
                    onReplyListener.reply(bean, v);
                }
            }
        });
        return holder.getConvertView();
    }

    private OnReplyListener onReplyListener;

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public interface OnReplyListener {

        boolean reply(HMAnswerBean bean, View v);

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
        return pageSize * (pageIndex - firstPage) < totalSize;
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
}
