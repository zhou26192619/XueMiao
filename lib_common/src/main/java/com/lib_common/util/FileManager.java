package com.lib_common.util;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lib_common.config.BaseConfig;

import java.io.File;
import java.util.Date;


public class FileManager {

	public static DownloadManager getDownloadManager(Context context) {
		String serviceString = Context.DOWNLOAD_SERVICE;
		return (DownloadManager) context.getSystemService(serviceString);
	}

	/**
	 * 建立文件下载链接
	 * 
	 * @param context
	 * @param url
	 * @param resId
	 *            图标
	 * @return
	 */
	public static long downloadFile(Context context, String url, int resId) {
		DownloadManager downloadManager = getDownloadManager(context);
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setTitle("学苗");
		FileUtil.initFile(BaseConfig.DEFAULT_IMAGE_DIR, "a.txt");
		request.setDestinationInExternalPublicDir("/HeMiao", "xuemiao.apk");
		request.setVisibleInDownloadsUi(true);
		// request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
		// request.setMimeType(BaseConfig.MIME_TYPE);
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		long reference = downloadManager.enqueue(request);
		sendNotification(context, "学苗", "学苗下载开始了哦！", resId);
		return reference;
	}

	/**
	 * 安装apk
	 * 
	 * @param file
	 */
	public static void apkInstall(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param resId
	 *            图标
	 */
	public static void sendNotification(Context context, String title,
			String content, int resId) {
		sendNotification(context, title, content, resId, null);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param resId
	 *            图标
	 */
	public static void sendNotification(Context context, String title,
			String content, int resId, PendingIntent pi) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = resId;
		notification.tickerText = content;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.when = new Date().getTime();// 显示的时间
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// 设置通知的标题和内容
		notification.setLatestEventInfo(context, title, content, pi);
		// RemoteViews contentView=new
		// RemoteViews(context.getPackageName(),R.layout.picture_list_loader_device);
		// contentView.setImageViewResource(R.id.,R.drawable.icon);
		// contentView.setTextViewText(R.id.text,"下载开始了");
		// notification.contentView=contentView;
		// 使用自定义下拉视图时，不需要再调用setLatestEventInfo()方法
		// 但是必须定义contentIntent
		// notification.contentIntent=pi;
		nm.notify(BaseConfig.NOTIFICATION_UNIQUE_CODE, notification);
	}
}
