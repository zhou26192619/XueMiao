package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMTeacherDetailAct;

public class HMApiTeacher extends HMApi {
	public static final String DETAIL = HOST
			+ "webapi/teacher/detail";

	public static HMApiTeacher getInstance() {
		return new HMApiTeacher();
	}

	public  void postDetail(HMTeacherDetailAct act, RequestParams params) {
		postRequest(act, DETAIL, params);
	}
}
