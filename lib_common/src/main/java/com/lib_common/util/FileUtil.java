package com.lib_common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public class FileUtil {
	/**
	 * 
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 */
	public static File initFile(String dir, String fileName) {
		File fDir = new File(dir);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		File file = new File(fDir, fileName);
//		if (file.exists()) {
//			file.delete();
//		} else {
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return file;
	}

	// public static void saveFile(OutputStream os, String dir, String fileName)
	// {
	// try {
	// File file = initFile(dir, fileName);
	// os.flush();
	// os.close();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public static void saveBitmap(Bitmap bm, String dir, String fileName)
			throws IOException {
		File file = initFile(dir, fileName);
		FileOutputStream os = new FileOutputStream(file);
		bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
		os.flush();
		os.close();
	}
}
