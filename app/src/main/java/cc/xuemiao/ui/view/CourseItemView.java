package cc.xuemiao.ui.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;

import cc.xuemiao.R;
import cc.xuemiao.bean.HMCourseBean;


/**
 * 课程信息
 * 
 * @author m
 * 
 */
public class CourseItemView {
	private static String TAG = "CourseItemView";

	private View view;
	private Activity activity;

	private TextView tvClassName;

	private TextView tvTeacherName;

	private ImageView ivAvatar;

	private TextView tvEvaluation;

	private LinearLayout llClassTable;

	private LinearLayout llEvaluation;

	private LinearLayout llArchive;

	private LinearLayout llHomework;

	private HMCourseBean courseBean;

	private LinearLayout llCourseInfo;

	public CourseItemView(Activity activity, HMCourseBean courseBean) {
		super();
		this.activity = activity;
		this.courseBean = courseBean;
		try {
			view = View.inflate(activity, R.layout.view_course_item, null);
			init();
			initListener();
			updateView(courseBean);
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	private void init() {
		ivAvatar = (ImageView) view.findViewById(R.id.course_item_iv_avatar);
		tvClassName = (TextView) view
				.findViewById(R.id.course_item_tv_class_name);
		tvTeacherName = (TextView) view
				.findViewById(R.id.course_item_tv_teacher_name);
		tvEvaluation = (TextView) view
				.findViewById(R.id.course_item_tv_evaluation);
		llClassTable = (LinearLayout) view
				.findViewById(R.id.course_item_ll_class_table);
		llEvaluation = (LinearLayout) view
				.findViewById(R.id.course_item_ll_evaluation);
		llArchive = (LinearLayout) view
				.findViewById(R.id.course_item_ll_archive);
		llHomework = (LinearLayout) view
				.findViewById(R.id.course_item_ll_homework);
		llCourseInfo = (LinearLayout) view
				.findViewById(R.id.course_item_ll_course_info);
	}

	/**
	 * 更新显示界面
	 */
	public void updateView(HMCourseBean courseBean) {
		try {
			tvClassName.setText(courseBean.getName());
			tvTeacherName.setText(courseBean.getTeacherName());
//			tvEvaluation.setText("已有" + courseBean.getCommentCount() + "条评价");
			MImageLoader.displayWithDefaultOptions(activity, courseBean.getLogo(), ivAvatar);
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	private void initListener() {
		llArchive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onArchive(courseBean);
				}
			}
		});
		llClassTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onClassTable(courseBean);
				}
			}
		});
		llEvaluation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onEvaluation(courseBean);
				}
			}
		});
		llHomework.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onHomework(courseBean);
				}
			}
		});
		llCourseInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onCourseInfo(courseBean);
				}
			}
		});

	}

	public View getView() {
		return view;
	}

	private OnClickListener onClickListener;

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public interface OnClickListener {
		void onClassTable(HMCourseBean courseBean);

		void onCourseInfo(HMCourseBean courseBean);

		void onEvaluation(HMCourseBean courseBean);

		void onArchive(HMCourseBean courseBean);

		void onHomework(HMCourseBean courseBean);
	}
}
