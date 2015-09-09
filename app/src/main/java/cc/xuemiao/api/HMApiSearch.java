package cc.xuemiao.api;

import com.lib_common.activity.BaseActivity;
import com.lib_common.fragment.BaseFragment;
import com.loopj.android.http.RequestParams;

public class HMApiSearch extends HMApi {
    public static final String SEARCH_QUESTION = HOST
            + "webapi/questions/search";

    public static HMApiSearch getInstance() {
        return new HMApiSearch();
    }

    public void postSearchQuestions(BaseActivity act,
                                    RequestParams params) {
        postRequest(act, SEARCH_QUESTION, params);
    }

    public void postSearchQuestions(BaseFragment bf, RequestParams params) {
        postRequest(bf, SEARCH_QUESTION, params);
    }
}
