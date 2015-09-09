package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.HMCourseDetailAct;
import cc.xuemiao.ui.HMCourseListAct;
import cc.xuemiao.ui.main.HMIndexFrg;

/**
 * 教师课程信息接口
 *
 * @author m
 */
public class HMApiCourse extends HMApi {
    public static final String MY_LIST = HOST
            + "webapi/courses/getMyCourses";
    public static final String DETAIL = HOST
            + "webapi/courses/getCourse";

    public static final String LIST_BY_PARENT_ID = HOST
            + "webapi/courses/getCoursesByParentId";

    public static final String LIST_BY_DISTANCE = HOST
            + "webapi/courses/nearBy";

    public static final String NEW_COURSE_LIST = HOST
            + "webapi/common/newCourseList";

    public static HMApiCourse getInstance() {
        return new HMApiCourse();
    }
    public  void postDetail(final HMCourseDetailAct act,
                                  RequestParams params) {
        postRequest(act, DETAIL, params);
    }

    public  void postMyList(HMCourseListAct act, RequestParams params) {
        postRequest(act, MY_LIST, params);
    }

    public  void postListByParentId(HMBaseAct act, RequestParams params) {
        postRequest(act, LIST_BY_PARENT_ID, params);
    }

    public  void getNewCourseList(HMIndexFrg frg,
                                        RequestParams params) {
        getRequest(frg, NEW_COURSE_LIST, params);
    }
}
