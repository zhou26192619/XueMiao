package cc.xuemiao.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib_common.util.DateUtil;
import com.lib_common.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cc.xuemiao.R;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.bean.HMStudentBean;
import cc.xuemiao.ui.HMCourseDetailAct;
import cc.xuemiao.utils.HMNavUtil;


/**
 * 首页课程信息列表
 * 
 * @author m
 * 
 */
public class IndexLastInfoView {
	private static String TAG = "HMIndexFrg";

	private View view;
	private Context context;
	private HMStudentBean studentBean;

	private ListView lvCourses;
	private RelativeLayout rlLatestInfo;
	private TextView tvName;
	private TextView tvClassInfo;
	private TextView tvFromTime;
	private TextView tvToTime;
	private List<HMCourseBean> courseBeans;
	private IndexLastInfoAdapter adapter;
	/**
	 * 当前显示的课程
	 */
	protected HMCourseBean currentCourse;

	private TextView tvClassName;

	private LinearLayout llNoCourse;

	private LinearLayout llClassTime;

	private LinearLayout llClassName;

	private ImageView IvEnter;

	/**
	 * 最多显示的条数(包括当前条数)
	 */
	private int totalSize = 4;

	public IndexLastInfoView(Context context, HMStudentBean studentBean) {
		super();
		this.context = context;
		this.studentBean = studentBean;
		try {
			view = View.inflate(context, R.layout.view_index_last_info, null);
			init();
			initListener();
			updateView();
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	private void init() {
		lvCourses = (ListView) view
				.findViewById(R.id.index_last_info_lv_courses);
		rlLatestInfo = (RelativeLayout) view
				.findViewById(R.id.index_last_info_rl_latest_info);
		llClassTime = (LinearLayout) view
				.findViewById(R.id.index_last_info_ll_class_time);
		llClassName = (LinearLayout) view
				.findViewById(R.id.index_last_info_ll_class_name);
		llNoCourse = (LinearLayout) view
				.findViewById(R.id.index_last_info_ll_no_course);
		tvName = (TextView) view.findViewById(R.id.index_last_info_tv_name);
		tvClassInfo = (TextView) view
				.findViewById(R.id.index_last_info_tv_class_info);
		tvToTime = (TextView) view
				.findViewById(R.id.index_last_info_tv_to_time);
		tvFromTime = (TextView) view
				.findViewById(R.id.index_last_info_tv_from_time);
		tvClassName = (TextView) view
				.findViewById(R.id.index_last_info_tv_class_name);
		IvEnter = (ImageView) view.findViewById(R.id.index_last_info_iv_enter);
		adapter = new IndexLastInfoAdapter();
		lvCourses.setAdapter(adapter);

		List<HMCourseBean> cs = studentBean.getCourses();
		if (cs != null && cs.size() > 0) {
			cleanData(cs);
			if (currentCourse != null) {
				rlLatestInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString(HMCourseDetailAct.BUNDLE_KEY,
								currentCourse.getId());
						bundle.putString("courseName", currentCourse.getName());
						HMNavUtil.goToNewAct(context, HMCourseDetailAct.class,
								bundle);
					}
				});
			} else {
				llNoCourse.setVisibility(View.VISIBLE);
				llClassName.setVisibility(View.GONE);
				llClassTime.setVisibility(View.GONE);
				IvEnter.setVisibility(View.GONE);

			}
		} else {
			llNoCourse.setVisibility(View.VISIBLE);
			llClassName.setVisibility(View.GONE);
			llClassTime.setVisibility(View.GONE);
			IvEnter.setVisibility(View.GONE);

		}
	}

	/**
	 * 对数据进行过滤
	 * 
	 * @param list
	 */
	protected void cleanData(List<HMCourseBean> list) {
		List<HMCourseBean> beans = new ArrayList<HMCourseBean>();
		for (HMCourseBean item : list) {
			Date dt = DateUtil.stringToDatetime(item.getTeachDate() + " "
							+ item.getTeachTime().get(0).get("endTime"),
					"yyyy-MM-dd HH:mm:ss");
			if (dt.compareTo(new Date()) < 0) {
				// list.remove(item);
			} else {
				beans.add(item);
			}
		}
		int temp = beans.size() <= totalSize ? beans.size() : totalSize;
		List<HMCourseBean> cbs = new ArrayList<HMCourseBean>();
		for (int i = 0; i < temp; i++) {
			cbs.add(beans.get(i));
		}
		if (cbs.size() > 0) {
			HMCourseBean c = cbs.get(0);
			if (DateUtil.compare2Dates(
					DateUtil.datetimeToString(new Date(), "yyyy-MM-dd"),
					c.getTeachDate()) == 0) {
				currentCourse = cbs.remove(0);
			} else {
				if (cbs.size() == totalSize) {
					cbs.remove(totalSize-1);
				}
			}
		}
		courseBeans = cbs;
	}

	/**
	 * 更新显示界面
	 */
	private void updateView() {
		try {
			String teachDate = null;
			String week = null;
			tvName.setText(studentBean.getChild().getName());
			if (currentCourse != null) {
				String strBT = currentCourse.getTeachTime().get(0)
						.get("beginTime");
				String strET = currentCourse.getTeachTime().get(0)
						.get("endTime");
				week = currentCourse.getTeachTime().get(0).get("week");
				tvToTime.setText(strET.substring(0, strET.lastIndexOf(":")));
				tvFromTime.setText(strBT.substring(0, strBT.lastIndexOf(":")));
				teachDate = currentCourse.getTeachDate();
				tvClassName.setText(currentCourse.getName());
			} else {
				teachDate = DateUtil.datetimeToString(new Date(), "yyyy-MM-dd");
				week = DateUtil.datetimeToString(new Date(), "EEEE");
			}
			ToastUtil.log(TAG, teachDate + " " + week);
			tvClassInfo.setText("今天 "
					+ teachDate.replace("-", ".").substring(
							teachDate.indexOf("-") + 1, teachDate.length())
					+ " " + week);
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	private void initListener() {
	}

	public View getView() {
		return view;
	}

	// *****************

	class IndexLastInfoAdapter extends BaseAdapter {

		public IndexLastInfoAdapter() {
			super();
			setData();
		}

		private void setData() {
			if (courseBeans == null) {
				courseBeans = new ArrayList<HMCourseBean>();
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return courseBeans.size();
		}

		@Override
		public Object getItem(int position) {
			return courseBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			VH vh = null;
			try {
				if (convertView == null) {
					convertView = View.inflate(context,
							R.layout.adapter_view_index_last_info, null);
					vh = new VH();
					vh.tvName = (TextView) convertView
							.findViewById(R.id.index_last_info_tv_course_name);
					vh.tvDay = (TextView) convertView
							.findViewById(R.id.index_last_info_tv_day);
					vh.tvWeek = (TextView) convertView
							.findViewById(R.id.index_last_info_tv_week);
					vh.tvTime = (TextView) convertView
							.findViewById(R.id.index_last_info_tv_time);
					vh.ivCourseLogo = (ImageView) convertView
							.findViewById(R.id.index_last_info_iv_course_logo);
					convertView.setTag(vh);
				} else {
					vh = (VH) convertView.getTag();
				}
				final HMCourseBean bean = courseBeans.get(position);
				vh.update(bean);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("courseId", bean.getId());
						bundle.putString("courseName", bean.getName());
						HMNavUtil.goToNewAct(context, HMCourseDetailAct.class,
								bundle);
					}
				});
			} catch (Exception e) {
				ToastUtil.printErr(e);
			}
			return convertView;
		}

		class VH {
			public ImageView ivCourseLogo;
			public TextView tvTime;
			public TextView tvWeek;
			public TextView tvDay;
			public TextView tvName;

			void update(HMCourseBean bean) {
				try {
					tvName.setText(bean.getName());
					// MImageLoader.displayWithDefaultOptions(context,
					// bean.getLogo(), ivCourseLogo);
					Map<String, String> teacher = bean.getTeachTime().get(0);
					tvWeek.setText(teacher.get("week"));
					String dayNick = DateUtil
							.displayDateNick(bean.getTeachDate(), DateUtil
									.datetimeToString(new Date(), "yyyy-MM-dd"));
					tvDay.setText(dayNick.replace("-", ".").substring(
							dayNick.indexOf("-") + 1, dayNick.length()));

					String strBT = teacher.get("beginTime");
					String strET = teacher.get("endTime");
					tvTime.setText(strBT.substring(0, strBT.lastIndexOf(":"))
							+ "-" + strET.substring(0, strET.lastIndexOf(":")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
