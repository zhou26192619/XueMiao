package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.main.HMCourseFrg;

public class HMApiStudent extends HMApi {
    public static final String LIST_BY_PARENT_ID = HOST
            + "webapi/courses/getChildrenCourses";

    public static HMApiStudent getInstance() {
        return new HMApiStudent();
    }
    public  void postListByParentId(HMCourseFrg frg,
                                          RequestParams params) {
        postRequest(frg, LIST_BY_PARENT_ID, params);
    }

}
