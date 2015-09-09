package cc.xuemiao.api;

import com.lib_common.fragment.BaseFragment;
import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;

/**
 * 问答api
 *
 * @author loar
 */
public class HMApiQA extends HMApi {
    /**
     * 问答的所属对象类型
     */
    public static final int TYPE_COURSE = 0;
    public static final int TYPE_CAMPAIGN = 1;
    public static final int TYPE_COOPERATION = 2;

    public static final String MY_LIST = HOST + "webapi/questions/my";
    public static final String LIST = HOST
            + "webapi/questions/other";
    public static final String GET_ALL_REPLY = HOST + "webapi/answer/list";
    public static final String NEW_QUESTION = HOST + "webapi/questions/add";
    public static final String NEW_QUESTION_LIST = HOST
            + "webapi/common/newQuestionList";
    public static final String ADD_REPLY = HOST + "webapi/answer/reply";
    public static HMApiQA getInstance() {
        return new HMApiQA();
    }
    public  void postList(HMBaseAct act,
                                RequestParams params) {
        postRequest(act, LIST, params);
    }

    public  void postList(BaseFragment bf, RequestParams params) {
        postRequest(bf, LIST, params);
    }

    public  void postAllReply(HMBaseAct act, RequestParams params) {
        postRequest(act, GET_ALL_REPLY, params);
    }

    public  void postAddReply(HMBaseAct act, RequestParams params) {
        postRequest(act, ADD_REPLY, params);
    }

    public  void postNewQuestion(HMBaseAct act, RequestParams params) {
        postRequest(act, NEW_QUESTION, params);
    }

    public  void postNewQuestion(BaseFragment bf, RequestParams params) {
        postRequest(bf, NEW_QUESTION, params);
    }

    public  void getNewQuestionList(BaseFragment bf, RequestParams params) {
        getRequest(bf, NEW_QUESTION_LIST, params);
    }

    public  void postMyList(HMBaseAct act, RequestParams params) {
        postRequest(act, MY_LIST, params);
    }

    public  void postMyList(BaseFragment bf, RequestParams params) {
        postRequest(bf, MY_LIST, params);
    }

}
