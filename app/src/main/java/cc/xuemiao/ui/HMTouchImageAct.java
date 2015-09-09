package cc.xuemiao.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;

import butterknife.Bind;
import butterknife.OnClick;
import cc.xuemiao.R;
import cc.xuemiao.api.HMApi;
import cc.xuemiao.api.HMApiImage;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.config.BaseConfig;
import com.lib_common.util.DateUtil;
import com.lib_common.util.FileUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图片放大浏览
 *
 * @author loar
 */
public class HMTouchImageAct extends HMBaseAct {

    public static final String BUNDLE_KEY = "imageUrl";
    public static final String BUNDLE_KEY_CONFIG = "config";
    public static final String BUNDLE_KEY_POSITION = "position";
    public static final String BUNDLE_KEY_RESULT = "result";
    public static final int CODE_RESULT_OK = 150518;
    public int CODE_RESULT;

    @Bind(R.id.touch_image_vp_images)
    ViewPager vpImages;

    @Bind(R.id.touch_image_iv_delete)
    ImageView ivDelete;

    @Bind(R.id.touch_image_iv_save)
    ImageView ivSave;

    private ArrayList<ImageListActivity.ImageBean> entites;

    private ImageAdapter adapter;
    private int position;
    private Config config;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.act_touch_image);
        try {
            init();
            setListener();
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        entites = (ArrayList<ImageListActivity.ImageBean>) bundle
                .getSerializable(BUNDLE_KEY);
        position = bundle.getInt(BUNDLE_KEY_POSITION);
        config = (Config) bundle.getSerializable(BUNDLE_KEY_CONFIG);
        if (config == null) {
            config = new Config();
        }
    }

    private void init() {
        adapter = new ImageAdapter();
        vpImages.setAdapter(adapter);
        vpImages.setCurrentItem(position);
        if (config != null) {
            if (config.isNeedDelete) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.GONE);
            }
            if (config.isNeedSave) {
                ivSave.setVisibility(View.VISIBLE);
            } else {
                ivSave.setVisibility(View.GONE);
            }
        }
    }

    private void setListener() {
        vpImages.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void updateView() {
        super.updateView();
        adapter.setData();
    }

    private void doDeleteImage() {
        hideLoading();
        CODE_RESULT = CODE_RESULT_OK;
        entites.remove(position);
        if (entites.size() == 0) {
            onBack(null);
            return;
        }
        updateView();
    }

    @OnClick(R.id.touch_image_iv_delete)
    public void onDelete(View v) {
        showLoading();
        ImageListActivity.ImageBean image = entites.get(position);
        if (image.getImageUrl().contains("http://")) {
            position = vpImages.getCurrentItem();
            RequestParams params = new RequestParams();
            params.put("imageId", entites.get(position).getId());
            HMApiImage.deleteImageById(this, params);
        } else {
            doDeleteImage();
        }
    }

    private Handler saveHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            hideLoading();
            if (msg.what == 0) {
                ToastUtil.toastAlways(HMTouchImageAct.this, "保存失败，请重试");
            } else if (msg.what == 1) {
                ToastUtil.toastAlways(HMTouchImageAct.this, "文件已保存在："
                        + BaseConfig.DEFAULT_IMAGE_DIR);
            }
        }

        ;
    };

    /**
     * 保存图片
     *
     * @param v
     */
    @OnClick(R.id.touch_image_iv_save)
    public void onSave(View v) {
        ImageListActivity.ImageBean image = entites.get(position);
        if (!image.getImageUrl().startsWith("http://")) {
            ToastUtil.toastAlways(this, "这是本地图片哦！");
            return;
        }
        showLoading();
        MImageLoader.getInstance(this).loadImage(image.getImageUrl(),
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        saveHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  final Bitmap bm) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                String fileName = "down"
                                        + DateUtil.datetimeToString(new Date(),
                                        "yyyyMMddHHmmss") + ".jpg";
                                try {
                                    FileUtil.saveBitmap(bm,
                                            BaseConfig.DEFAULT_IMAGE_DIR,
                                            fileName);
                                    saveHandler.sendEmptyMessage(1);
                                } catch (IOException e) {
                                    ToastUtil.printErr(e);
                                    saveHandler.sendEmptyMessage(0);
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {

                    }
                });
    }

    @Override
    public void setRequestSuc(String url, int statusCode, Header[] headers,
                                 JsonObject jo) {
        super.setRequestSuc(url, statusCode, headers, jo);
        if (url.equals(HMApiImage.DELETE_IMAGE_BY_ID)) {
            doDeleteImage();
        }
    }

    @Override
    public void setRequestNotSuc(String url, int statusCode,
                                    Header[] headers, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, headers, jo);
        ToastUtil.toastAlways(this, jo.getAsJsonPrimitive(HMApi.KEY_MSG).getAsString());
    }

    @Override
    public void setRequestErr(String url, int statusCode, Header[] headers,
                                 String str, Throwable throwable) {
        super.setRequestErr(url, statusCode, headers, str, throwable);
        ToastUtil.toastAlways(this, str);
    }

    @Override
    public void setRequestFinish() {
        super.setRequestFinish();
        hideLoading();
    }

    @Override
    public void onBack(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_RESULT, entites);
        intent.putExtras(bundle);
        setResult(CODE_RESULT, intent);
        super.onBack(v);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && config.isNeedDelete) {
            onBack(null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ImageAdapter extends PagerAdapter {

        public ImageAdapter() {
            setData();
        }

        private void setData() {
            if (entites == null) {
                entites = new ArrayList<ImageListActivity.ImageBean>();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { // 获得size
            return entites == null ? 0 : entites.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object) {
            View v = (View) object;
            PhotoViewAttacher mAttacher = (PhotoViewAttacher) v.getTag();
            mAttacher.cleanup();
            ((ViewPager) view).removeView(v);
        }

        @Override
        public Object instantiateItem(View view, int position) {
            PhotoView iv = new PhotoView(HMTouchImageAct.this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBack(v);
                }
            });
            final PhotoViewAttacher mAttacher = new PhotoViewAttacher(iv);
            iv.setTag(mAttacher);
            // 点击图片的监听
            mAttacher.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    onBack(view);
                }
            });
            // 点击
            mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View view, float x, float y) {
                    onBack(view);
                }
            });
            ImageListActivity.ImageBean item = entites.get(position);
            MImageLoader.display(HMTouchImageAct.this, item.getImageUrl(), iv,
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {
                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1,
                                                    FailReason arg2) {
                        }

                        @Override
                        public void onLoadingComplete(String arg0, View arg1,
                                                      Bitmap arg2) {
                            mAttacher.update();
                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                        }
                    });
            ((ViewPager) view).addView(iv);
            return iv;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public static class Config implements Serializable {
        private static final long serialVersionUID = -57228307704225976L;

        public boolean isNeedDelete;
        public boolean isNeedSave;
    }
}
