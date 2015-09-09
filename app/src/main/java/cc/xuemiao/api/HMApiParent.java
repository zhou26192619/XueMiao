package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.HMPhoneFillInAct;

public class HMApiParent extends HMApi {
	public static final String BIND_PHONE = HOST
			+ "webapi/parent/bindPhone";
	public static final String DETAIL = HOST
			+ "webapi/parent/detail";
	public static final String UPDATE = HOST
			+ "webapi/parent/edit";

	public static HMApiParent getInstance() {
		return new HMApiParent();
	}

	public  void postBindPhone(HMPhoneFillInAct act, RequestParams params) {
		postRequest(act, BIND_PHONE, params);
	}

	public  void postDetail(HMBaseAct act, RequestParams params) {
		postRequest(act, DETAIL, params);
	}

	public  void postUpdate(HMBaseAct act, RequestParams params) {
		postRequest(act, UPDATE, params);
	}
}
