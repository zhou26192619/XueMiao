package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;

/**
 * 图片上传
 *
 * @author LoaR
 */
public class HMApiImage extends HMApi {
    public static final String UPLOAD_IMAGE = HOST
            + "webapi/parent/face";
    public static final String DELETE_IMAGE_BY_ID = HOST
            + "webapi/parent/face";
    public static final String UPLOAD_CHILD_LOGO = HOST
            + "webapi/children/face";

    public static HMApiImage getInstance() {
        return new HMApiImage();
    }

    public  void postUploadImage(HMBaseAct act, RequestParams params) {
        postRequest(act, UPLOAD_IMAGE, params);
    }

    public static void deleteImageById(HMBaseAct act, RequestParams params) {
    }

    /**
     * 上传小孩头像
     *
     * @param act
     * @param params
     */
    public  void postUploadChildLogo(HMBaseAct act, RequestParams params) {
        postRequest(act, UPLOAD_CHILD_LOGO, params);
    }
}
