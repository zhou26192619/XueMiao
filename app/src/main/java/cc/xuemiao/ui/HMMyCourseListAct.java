package cc.xuemiao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cc.xuemiao.HMApp;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiCourse;
import cc.xuemiao.bean.HMCourseBean;
import cc.xuemiao.bean.HMStudentBean;
import cc.xuemiao.bean.HMUserBean;
import cc.xuemiao.ui.view.CourseItemView;
import cc.xuemiao.utils.HMNavUtil;

public class HMMyCourseListAct extends HMBaseAct {

	@Bind(R.id.my_course_list_elv_courses)
	ExpandableListView elvCourse;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentViews(R.layout.act_my_course_list);
		init();
		setListener();
	}

	private void init() {
		hvHeadView.setTitle("我的课程");
		adapter = new MyCourseAdapter();
		elvCourse.setAdapter(adapter);
	}

	private void setListener() {
		elvCourse.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}

	@Override
	public void updateView() {
		super.updateView();
		if (studentBeans != null && studentBeans.size() > 0) {
			adapter.setData();
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				elvCourse.expandGroup(i);
			}
		}
	}

	private List<HMStudentBean> studentBeans;
	private MyCourseAdapter adapter;

	@Override
	public void loadData() {
		HMUserBean user =((HMApp)getApplication()).getUserSP().getUserBean(HMUserBean.class);
		if (user == null) {
			return;
		}
		showLoading();
		RequestParams params = new RequestParams();
		params.put("parentId", user.getRoleId());
		HMApiCourse.getInstance().postListByParentId(this, params);
	}

	@Override
	public void setRequestSuc(String url, int statusCode, Header[] headers,
			JsonObject jo) {
		super.setRequestSuc(url, statusCode, headers, jo);
		studentBeans = GsonUtil.fromJsonArr(jo.getAsJsonArray(HMApi.KEY_DATA),
				new TypeToken<List<HMStudentBean>>() {
				});
		updateView();
	}

	@Override
	public void setRequestNotSuc(String url, int statusCode,
			Header[] headers, JsonObject jo) {
		super.setRequestNotSuc(url, statusCode, headers, jo);
		ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
	}

	@Override
	public void setRequestErr(String url, int statusCode, Header[] headers,
			String str, Throwable throwable) {
		super.setRequestErr(url, statusCode, headers, str, throwable);
		ToastUtil.toastAlways(this, str);
	}

	@Override
	public void setRequestFinish() {
		super.setRequestFinish();
		hideLoading();
	}

	// *****************

	class MyCourseAdapter extends BaseExpandableListAdapter {

		public MyCourseAdapter() {
			super();
			setData();
		}

		private void setData() {
			if (studentBeans == null) {
				studentBeans = new ArrayList<HMStudentBean>();
			}
			notifyDataSetChanged();
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return studentBeans.get(groupPosition).getCourses()
					.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean arg2, View convertView, ViewGroup arg4) {
			final HMCourseBean bean = studentBeans.get(groupPosition)
					.getCourses().get(childPosition);
			CourseItemView holder = null;
			if (convertView == null) {
				holder = new CourseItemView(HMMyCourseListAct.this, bean);
				convertView = holder.getView();
				convertView.setTag(holder);
			} else {
				holder = (CourseItemView) convertView.getTag();
			}
			holder.updateView(bean);
			holder.setOnClickListener(new CourseItemView.OnClickListener() {
				@Override
				public void onClassTable(HMCourseBean courseBean) {
				}

				@Override
				public void onCourseInfo(HMCourseBean courseBean) {
					Bundle bundle = new Bundle();
					bundle.putString(HMCourseDetailAct.BUNDLE_KEY,
							courseBean.getId());
					HMNavUtil.goToNewAct(HMMyCourseListAct.this,
							HMCourseDetailAct.class, bundle);
				}

				@Override
				public void onEvaluation(HMCourseBean courseBean) {
				}

				@Override
				public void onArchive(HMCourseBean courseBean) {
				}

				@Override
				public void onHomework(HMCourseBean courseBean) {
				}
			});
			return convertView;
		}

		@Override
		public int getChildrenCount(int arg0) {
			return studentBeans.get(arg0).getCourses().size();
		}

		@Override
		public Object getGroup(int arg0) {
			return studentBeans.get(arg0);
		}

		@Override
		public int getGroupCount() {
			return studentBeans.size();
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup arg3) {
//			GroupHolder holder = null;
//			if (convertView == null) {
//				convertView = View.inflate(HMMyCourseListAct.this,
//						R.layout.adapter_my_course_group, null);
//				holder = new GroupHolder();
//				holder.tvName = (TextView) convertView
//						.findViewById(R.id.my_course_tv_name);
//				convertView.setTag(holder);
//			} else {
//				holder = (GroupHolder) convertView.getTag();
//			}
//			HMStudentBean bean = studentBeans.get(groupPosition);
//			holder.update(bean);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

		class GroupHolder {

			public TextView tvName;
			public ImageView ivIndicator;
			public TextView tvDate;

			void update(HMStudentBean bean) {
				tvName.setText(bean.getChild().getName());
			}
		}
	}
}
