package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.main.HMIndexFrg;

/**
 * 推荐api
 *
 * @author loar
 */
public class HMApiRecommend extends HMApi {

    public static final String DETAIL = HOST + "webapi/common/recommend";

    public static HMApiRecommend getInstance() {
        return new HMApiRecommend();
    }

    /**
     * 获取推荐实体
     *
     * @param frg
     * @param params
     */
    public void getDetail(HMIndexFrg frg, RequestParams params) {
        getRequest(frg, DETAIL, params);
    }
}
