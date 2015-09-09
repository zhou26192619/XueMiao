package com.lib_common.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lib_common.R;
import com.lib_common.dialog.DirPathsDialog;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.NavUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.PhotoUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 图片选择器
 * 
 * @author Zhoujiwei
 * 
 */
public class ImageListActivity extends BaseActivity {
	/**
	 * 完成按钮
	 */
	private LinearLayout llOK;
	/**
	 * 图片列表
	 */
	private GridView gvImages;
	/**
	 * 返回
	 */
	private ImageView ivBack;
	/**
	 * 选择的张数
	 */
	private TextView tvProgress;
	/**
	 * 目录显示
	 */
	private TextView tvDir;
	/**
	 * 目录显示选择
	 */
	private LinearLayout llDir;
	/**
	 * 
	 */
	private RelativeLayout rlRoot;
	/**
	 * 预览框按钮
	 */
	private LinearLayout llPreview;
	/**
	 * 预览框按钮
	 */
	private TextView tvPreviewNum;

	private ProgressDialog mProgressDialog;
	/**
	 * 包含图片的目录集合以及目录下的图片路径集合
	 */
	private HashMap<String, List<ImageBean>> mDirFilePaths = new HashMap<String, List<ImageBean>>();
	/**
	 * 包含图片的目录集合（用于获取目录下的图片集合）
	 */
	private List<String> mDirPaths = new ArrayList<String>();
	/**
	 * 用户选择的图片集合
	 */
	private ArrayList<ImageBean> hasSelectedPictures = new ArrayList<ImageBean>();
	/**
	 * 
	 */
	private PictureListLoaderDeviceAdapter adapter;
	/**
	 */
	public final static String BUNDLE_KEY_CONFIG = "config";
	public final static String BUNDLE_KEY = "entities";
	/**
	 * 结果成功码
	 */
	public final static int CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_OK = 2015031710;
	/**
	 * 结果取消码
	 */
	public final static int CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_CANCEL = 2015031711;
	private int CODE_RESULT = CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_OK;
	/**
	 * 返回结果的key名
	 */
	public final static String RESULT_NAME = "hasSelectedPictures";
	/**
	 * sd卡的跟目录
	 */
	private String rootPath = Environment.getExternalStorageDirectory()
			.getPath();
	/**
	 * 合并所有图片的key
	 */
	private String allPictureKey = rootPath + "/所有图片";
	/**
	 * 刷新显示图片
	 */
	private Handler fileSelecthandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int position = msg.what;
			adapter.setData(mDirFilePaths.get(mDirPaths.get(position)));
			String[] ss = mDirPaths.get(position).split("/", 4);
			tvDir.setText(ss[ss.length - 1]);
		};
	};
	/**
	 * 文件夹选择弹窗
	 */
	private DirPathsDialog dirPathsDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_list_loader_device);
		hasSelectedPictures = (ArrayList<ImageBean>) getIntent()
				.getSerializableExtra(BUNDLE_KEY);
		config = (Config) getIntent().getSerializableExtra(BUNDLE_KEY_CONFIG);
		if (hasSelectedPictures == null) {
			hasSelectedPictures = new ArrayList<ImageBean>();
		}
		findView();
		init();
	}

	private void findView() {
		llOK = (LinearLayout) findViewById(R.id.picture_list_loader_device_ll_ok);
		gvImages = (GridView) findViewById(R.id.picture_list_loader_device_gv_images);
		ivBack = (ImageView) findViewById(R.id.picture_list_loader_device_iv_back);
		tvProgress = (TextView) findViewById(R.id.picture_list_loader_device_tv_progress);
		tvDir = (TextView) findViewById(R.id.picture_list_loader_device_tv_dir);
		llDir = (LinearLayout) findViewById(R.id.picture_list_loader_device_ll_dir);
		rlRoot = (RelativeLayout) findViewById(R.id.picture_list_loader_device_rl_root);
		llPreview = (LinearLayout) findViewById(R.id.picture_list_loader_device_ll_preview);
		tvPreviewNum = (TextView) findViewById(R.id.picture_list_loader_device_tv_preview_num);
	}

	public void init() {
		setData();
		// 获取手机的图片信息
		getImages();
		setListener();
		initDirPathsDiolog();
	}

	private void setData() {
		if (config == null) {
			config = new Config();
		}

		// 设置选择进度
		tvProgress.setText("(" + hasSelectedPictures.size() + "/"
				+ config.maxSelectNum + ")");

		// 先添加所有图片的可以，保证在第一位
		mDirPaths.add(allPictureKey);

		adapter = new PictureListLoaderDeviceAdapter(null);
		gvImages.setAdapter(adapter);
	}

	private void setListener() {
		rlRoot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 什么都不干，只是为了屏蔽下点击事件
			}
		});

		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		llOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (hasSelectedPictures.size() != 0) {
				// 选择完后执行的
				setMyResult();
				finish();
				// }
			}
		});
		llDir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 点击展示
				dirPathsDialog.show();
			}
		});
		llPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hasSelectedPictures.size() != 0) {
				}
			}
		});
	}

	/**
	 * 设置返回的结果(onActivityForResult的结果)
	 */
	protected void setMyResult() {
		// if (hasSelectedPictures.size() == 0) {
		// CODE_RESULT = CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_CANCEL;
		// } else {
		CODE_RESULT = CODE_PICTURE_LIST_LOADER_DEVICE_RESULT_OK;
		// }
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(RESULT_NAME, hasSelectedPictures);
		intent.putExtras(bundle);
		setResult(CODE_RESULT, intent);
	}

	/**
	 * 选择目录对话框
	 */
	private void initDirPathsDiolog() {
		dirPathsDialog = new DirPathsDialog(this, R.style.myDialogStyleBottom,
				mDirFilePaths, mDirPaths);
		dirPathsDialog.setOnClickDirListener(new DirPathsDialog.OnClickDirListener() {

			@Override
			public void clickDirListener(String item, int position) {
				fileSelecthandler.sendEmptyMessage(position);
				dirPathsDialog.dismiss();
			}
		});
		dirPathsDialog.setCanceledOnTouchOutside(true);
		dirPathsDialog.getWindow()
				.setWindowAnimations(R.style.dialogWindowAnim);
		// 设置显示的位置
		WindowManager.LayoutParams lp = dirPathsDialog.getWindow()
				.getAttributes();
		lp.x = 0;
		lp.y = DensityUtil.dip2px(this, 55);
		// lp.width =
		// DensityUtil.getScreenW(ImageListActivity.this);
		// dirPathsDialog.getWindow().setAttributes(lp);

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {
			@Override
			public void run() {
				// 默认添加所有图片集合，以及照相的对象,整合所有的所有的图片
				ArrayList<ImageBean> allPicture = new ArrayList<ImageBean>();
				ImageBean entity = new ImageBean();
				entity.setImageUrl("" + R.mipmap.photograph_icon_camera);
				allPicture.add(entity);

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImageListActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED + " DESC");

				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					ImageBean imageBean = new ImageBean();
					imageBean.setImageUrl(path);
					allPicture.add(imageBean);
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (!mDirFilePaths.containsKey(dirPath)) {
						mDirFilePaths.put(dirPath,
								new ArrayList<ImageBean>());
						mDirPaths.add(dirPath);
					}
					if (mDirFilePaths.get(dirPath).contains(
							imageBean)) {
						continue;
					} else {
						mDirFilePaths.get(dirPath).add(imageBean);
					}
				}
				mCursor.close();
				mDirFilePaths.put(allPictureKey, allPicture);

				// 通知Handler扫描图片完成
				fileSelecthandler.sendEmptyMessage(0);
				mProgressDialog.dismiss();
			}
		}).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 接受照相返回的图片
		if (requestCode == PhotoUtil.REQUESTCODE_CAMERA
				&& resultCode == Activity.RESULT_OK) {
			String url = PhotoUtil.OnPhotoResult(this, requestCode, resultCode,
					data);
			ImageBean entity = new ImageBean();
			entity.setImageUrl(url);
			hasSelectedPictures = new ArrayList<ImageBean>();
			hasSelectedPictures.add(entity);
			setMyResult();
			finish();
		}
	}

	class PictureListLoaderDeviceAdapter extends BaseAdapter {

		private List<ImageBean> shortcutPictureEntities;

		public PictureListLoaderDeviceAdapter(
				List<ImageBean> shortcutPictureEntities) {
			super();
			setData(shortcutPictureEntities);
		}

		private void setData(List<ImageBean> shortcutPictureEntities) {
			if (shortcutPictureEntities == null) {
				this.shortcutPictureEntities = new ArrayList<ImageBean>();
			} else {
				this.shortcutPictureEntities = shortcutPictureEntities;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return this.shortcutPictureEntities.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(
						ImageListActivity.this,
						R.layout.picture_list_loader_device_adapter, null);
				holder.ivImage = (ImageView) convertView
						.findViewById(R.id.picture_list_loader_device_adapter_iv_image);
				holder.ivChoice = (ImageView) convertView
						.findViewById(R.id.picture_list_loader_device_adapter_iv_choice);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			ImageBean item = this.shortcutPictureEntities
					.get(position);
			// 加载图片
			setImage(item, holder);
			// 图片显示的状态
			initChooseState(holder, item);
			// 设置单击事件
			setListener(holder, item, position);
			return convertView;
		}

		private void setListener(final Holder holder,
				final ImageBean item, final int position) {
			holder.ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (item.getImageUrl().contains(rootPath)) {
						Bundle bundle = new Bundle();
						bundle.putSerializable(
								TouchImageAct.BUNDLE_KEY,
								(ArrayList<ImageBean>) shortcutPictureEntities);
						bundle.putInt(TouchImageAct.BUNDLE_KEY_POSITION,
								position);
						NavUtil.goToNewAct(
								ImageListActivity.this,
								TouchImageAct.class, bundle);
					} else {
						// 调用相机
						PhotoUtil
								.callCamera(ImageListActivity.this);
					}
				}
			});
			holder.ivChoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 选择操作
					choosePicture(holder, item);
					tvProgress.setText("(" + hasSelectedPictures.size() + "/"
							+ config.maxSelectNum + ")");
					tvPreviewNum.setText("(" + hasSelectedPictures.size() + ")");
				}
			});
		}

		/**
		 * 初始化图片选择状态
		 * 
		 * @param item
		 * @param holder
		 * 
		 */
		protected void initChooseState(Holder holder, ImageBean item) {
			holder.ivChoice
					.setImageResource(R.mipmap.picture_list_loader_device_select_photo_n);
			holder.ivImage.setColorFilter(null);
			for (ImageBean en : hasSelectedPictures) {
				if (en.getImageUrl().equals(item.getImageUrl())) {
					holder.ivChoice
							.setImageResource(R.mipmap.picture_list_loader_device_select_photo_s);
					holder.ivImage
							.setColorFilter(Color.parseColor("#9f000000"));
				}
			}
		}

		/**
		 * 点击选择时的操作
		 * 
		 * @param item
		 * @param holder
		 * 
		 */
		protected void choosePicture(Holder holder, ImageBean item) {
			if (hasSelectedPictures.contains(item)) {
				holder.ivChoice
						.setImageResource(R.mipmap.picture_list_loader_device_select_photo_n);
				hasSelectedPictures.remove(item);
				holder.ivImage.setColorFilter(null);
			} else {
				if (hasSelectedPictures.size() < config.maxSelectNum) {
					holder.ivChoice
							.setImageResource(R.mipmap.picture_list_loader_device_select_photo_s);
					hasSelectedPictures.add(item);
					holder.ivImage
							.setColorFilter(Color.parseColor("#9f000000"));
				} else {
					Toast.makeText(ImageListActivity.this,
							"最多选" + config.maxSelectNum + "张图片哦！", Toast.LENGTH_SHORT).show();
				}
			}
		}

		/**
		 * 查询 判断是否选择过
		 * 
		 * @param imageBean
		 * @return
		 */
		protected boolean isSelected(ImageBean imageBean) {
			for (int i = 0; i < hasSelectedPictures.size(); i++) {
				if (imageBean.getImageUrl().equals(
						hasSelectedPictures.get(i).getImageUrl())) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 根据url加载图片
		 * 
		 * @param holder
		 * @param entity
		 */
		private void setImage(ImageBean entity, Holder holder) {
			holder.ivChoice.setVisibility(View.VISIBLE);
			MImageLoader.displayWithDefaultOptions(
					ImageListActivity.this, ""
							+ R.mipmap.default_icon,
					holder.ivImage);
			if (entity.getImageUrl().contains(rootPath)) {
				MImageLoader.displayWithDefaultOptions(
						ImageListActivity.this,
						"" + entity.getImageUrl(), holder.ivImage);
			} else {
				MImageLoader.displayWithDefaultOptions(
						ImageListActivity.this,
						"" + entity.getImageUrl(), holder.ivImage);
				holder.ivChoice.setVisibility(View.GONE);
			}
			holder.ivImage.setTag(entity.getImageUrl());
		}

		@Override
		public Object getItem(int arg0) {
			return shortcutPictureEntities.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
	}

	class Holder {
		public ImageView ivChoice;
		public ImageView ivImage;
	}

	/**
	 * 配置
	 */
	private Config config;

	@SuppressWarnings("serial")
	public static class Config implements Serializable {
		/**
		 * 图片最大选择数量限制
		 */
		public int maxSelectNum = 9;

	}

	public static class ImageBean implements Serializable {
		private static final long serialVersionUID = 1380122567053484369L;
		private int id;
		private String imageUrl;
		private String name;
		private String tag;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}
	}
}
