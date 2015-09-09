package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMCourseDetailAct;
import cc.xuemiao.ui.HMManageChildrenAct;
import cc.xuemiao.ui.login_register.HMRegisterChildMaterialAct;

public class HMApiChild extends HMApi {
	public static final String ADD_CHILD = HOST
			+ "webapi/children/addChild";
	public static final String LIST_BY_COURSE = HOST
			+ "webapi/children/getList";
	public static final String LIST_BY_PARENT_ID = HOST
			+ "webapi/children/getChildren";
	public static final String DELETE_BY_ID = HOST
			+ "webapi/children/delete";
	public static final String UPDATE = HOST
			+ "webapi/children/editChild";

	public static HMApiChild getInstance(){
		return new HMApiChild(); 
	}
	
	public  void postListByCourse(HMCourseDetailAct act,
										RequestParams params) {
		postRequest(act, LIST_BY_COURSE, params);
	}

	public  void postAddChild(HMRegisterChildMaterialAct act,
									RequestParams params) {
		postRequest(act, ADD_CHILD, params);
	}

	public  void postUpdate(HMRegisterChildMaterialAct act,
								  RequestParams params) {
		postRequest(act, UPDATE, params);
	}

	public  void postDeleteById(HMManageChildrenAct act,
									  RequestParams params) {
		postRequest(act, DELETE_BY_ID, params);
	}

	public  void postListByParentId(HMManageChildrenAct act,
										  RequestParams params) {
		postRequest(act, LIST_BY_PARENT_ID, params);
	}
}
