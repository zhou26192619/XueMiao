package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.main.HMIndexFrg;

/**
 * 活动相关api
 *
 * @author loar
 */
public class HMApiCampaign extends HMApi {
    /**
     * 活动类型
     */
    public static final String TYPE_CAMPAIN_FEE_FREE = "0";
    public static final String TYPE_CAMPAIN_FEE_PAY = "1";
    /**
     * 报名状态:报名中
     */
    public static final String STATUS_SIGN_UP_ING = "0";
    /**
     * 报名状态:报名通过
     */
    public static final String STATUS_SIGN_UP_ED = "1";
    /**
     * 报名状态:报名已撤销
     */
    public static final String STATUS_SIGN_UP_REVOKE = "2";

    public static final String LIST_BY_ORGANIZATION_ID = HOST
            + "webapi/activities/listByOrganizationId";
    public static final String DETAIL = HOST + "webapi/activities/detail";
    public static final String LIST_BY_ACTIVITY_IDS = HOST
            + "webapi/activities/listByActivityIds";
    public static final String SIGN_UP = HOST + "webapi/activities/doSignup";
    public static final String MY_ACTIVITIES = HOST
            + "webapi/activities/getMyActivities";
    public static final String LIST = HOST + "webapi/activities/list";
    public static final String NEW_ACTIVITY_LIST = HOST
            + "webapi/common/newActivityList";

    public static HMApiCampaign getInstance(){
        return new HMApiCampaign();
    }

    public void postListByOrganizationId(HMBaseAct act,
                                                RequestParams params) {
        postRequest(act, LIST_BY_ORGANIZATION_ID, params);
    }

    public  void postDetail(HMBaseAct act, RequestParams params) {
        postRequest(act, DETAIL, params);
    }

    public  void postListByActivityIds(HMBaseAct act, RequestParams params) {
        postRequest(act, LIST_BY_ACTIVITY_IDS, params);
    }

    public  void postSignUp(HMBaseAct act, RequestParams params) {
        postRequest(act, SIGN_UP, params);
    }

    public  void postMyList(HMBaseAct act, RequestParams params) {
        postRequest(act, MY_ACTIVITIES, params);
    }

    public  void getNewList(HMIndexFrg frg, RequestParams params) {
        getRequest(frg, NEW_ACTIVITY_LIST, params);
    }

}
