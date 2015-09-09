package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;
import cc.xuemiao.ui.HMOrgDetailAct;
import cc.xuemiao.ui.main.HMIndexFrg;

public class HMApiOrganization extends HMApi {
    public static final String GET_LIST = HOST + "webapi/organizations/getList";
    public static final String DETAIL = HOST
            + "webapi/organizations/detail";
    public static final String LIST_BY_DISTANCE = HOST
            + "webapi/organizations/nearBy";
    public static final String NEW_LIST = HOST
            + "webapi/common/newOrganizationList";

    public static HMApiOrganization getInstance() {
        return new HMApiOrganization();
    }

    public  void postDetail(HMOrgDetailAct act, RequestParams params) {
        postRequest(act, DETAIL, params);
    }

    public  void postListByDistanece(HMBaseAct act, RequestParams params) {
        postRequest(act, LIST_BY_DISTANCE, params);
    }

    public  void postNewList(HMIndexFrg frg, RequestParams params) {
        getRequest(frg, NEW_LIST, params);
    }
}
