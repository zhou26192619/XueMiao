package cc.xuemiao.broadcast;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lib_common.util.FileManager;
import com.lib_common.util.ToastUtil;

import java.io.File;

import cc.xuemiao.R;


public class DownloadBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			long downloadId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			DownloadManager downloadManager = FileManager
					.getDownloadManager(context);
			Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
			FileManager.apkInstall(context, new File(uri.getPath()));
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
			FileManager.sendNotification(context, "学苗", "下载已完成",
					R.mipmap.logo, pi);
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}
}
