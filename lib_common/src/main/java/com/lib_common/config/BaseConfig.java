package com.lib_common.config;

/**
 * 基本相关配置
 * 
 * @author loar
 * 
 */
public class BaseConfig {
	public static final String PLATFORM = "android";
	public static final boolean IS_DEBUG = true;
	public static final int NOTIFICATION_UNIQUE_CODE = 1565;
	public static final int TIME_WELCOME_ACT_DURATION = 1500;
	public static final String MIME_TYPE = "com.hemiao.download.file";

	//聊天的用户名和密码
	public static final String CHAT_USER_NAME = "";
	public static final String CHAT_PASSWORD = "";

	/**
	 * 官方下载页面
	 */
	public static String SITE_DOWNLOAD_URL = "http://www.xuemiao.cc/downloadParent.html";
	/**
	 * 官方logo地址
	 */
	public static String SITE_LOGO_URL = "http://115.29.207.3:8080/fileroot/app/logo/parent.png";

	// ======================begin=====百度云的table ============================
	public static final int GEOTABLE_ORG = 110751; // 机构百度云数据库
	public static final int GEOTABLE_CLASS = 110750; // 课程百度云数据库
	// public static final int GEOTABLE_ORG = 95582; // 机构百度云数据库
	// public static final int GEOTABLE_CLASS = 98850; // 课程百度云数据库
	public static final String GEOTABLE_ID = "com.hemiao.download.file";// 活动百度云数据库（正式服）
	// ======================end=====百度云的table ============================

	/**
	 * 手机sd卡根目录
	 */
	public static final String ROOT_DIR = android.os.Environment
			.getExternalStorageDirectory().getPath();
	/**
	 * 默认图片保存目录名
	 */
	public static final String DEFAULT_IMAGE_DIR_NAME = "/HeMiao";
	/**
	 * 默认图片保存目录
	 */
	public static final String DEFAULT_IMAGE_DIR = ROOT_DIR
			+ DEFAULT_IMAGE_DIR_NAME + "/";

	// ======================begin=====密码姓名正则 ============================
	public static final String PATTERN_NAME = "^[0-9a-zA-Z\u4e00-\u9fa5]{2,11}$";
	public static final String PATTERN_ACCOUNT_NAME = "^[a-zA-Z][0-9a-zA-Z]{5,14}$";
	public static final String PATTERN_NICKNAME = "^[0-9a-zA-Z\u4e00-\u9fa5]{1,10}$";
	public static final String PATTERN_PASSWORD = "^[0-9a-zA-Z]{6,20}$";
	public static final String PATTERN_PHONE_NUMBER = "^((\\+{0,1}86){0,1})((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
	// ======================end=====密码姓名正则 ============================

}
