package cc.xuemiao.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cc.xuemiao.R;


/**
 * 报名弹窗
 *
 * @author loar
 */
public class ReplyDialog extends Dialog {

    private Context context;
    private ImageView ivLogo;
    private EditText etText;
    private TextView tvOk;
    private TextView tvTitle;
    private TextView tvCancel;
    private View view;

    public ReplyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        try {
            view = View.inflate(context, R.layout.dialog_reply, null);
            etText = (EditText) view.findViewById(R.id.reply_et_text);
            tvOk = (TextView) view.findViewById(R.id.reply_tv_ok);
            tvCancel = (TextView) view.findViewById(R.id.reply_tv_cancel);
            tvTitle = (TextView) view.findViewById(R.id.reply_tv_title);
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
    }

    private void setListener() {
        tvOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onEditClickListener != null) {
                    onEditClickListener.ok(etText.getText().toString(), v);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onEditClickListener != null) {
                    onEditClickListener.cancel(v);
                }
            }
        });

    }

    public void resetText() {
        etText.setText("");
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setOKText(String text) {
        tvOk.setText(text);
    }

    private OnEditClickListener onEditClickListener;

    /**
     * 设置
     *
     * @param onEditClickListener
     */
    public void setOnEditClickListener(OnEditClickListener onEditClickListener) {
        this.onEditClickListener = onEditClickListener;
    }

    public interface OnEditClickListener {

        void ok(String text, View v);

        void cancel(View v);

    }

}
