package com.lgm.baseframe.common;

import android.os.Environment;

import java.io.File;

/**
 * 路径获取类
 * 
 * @author hanguoxin
 * 
 */
public class PathUtil {

	private static String appname = "carfriend";

	/**
	 * SD路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();
			return sdDir.getAbsolutePath();
		}
		return "/mnt/sdcard";
	}

	/**
	 * 获取项目SD路径
	 */
	public static String getMainPath() {
		String mainPath = getSDPath() + File.separator + appname;
		File file = new File(mainPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return mainPath;
	}

	/**
	 * 图片路径
	 * 
	 * @return
	 */
	public static String getSDImagePath() {
		String imagePath = getMainPath() + "/image/";
		File file = new File(imagePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return imagePath;
	}
	
	/**
	 * 图片路径
	 * 
	 * @return
	 */
	public static String getLogPath() {
		String imagePath = getMainPath() + "/log/";
		File file = new File(imagePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return imagePath;
	}


	/**
	 * 安装包存放路径
	 */
	public static String getAppPath() {
		String appPath = getMainPath() + "/app/";
		File file = new File(appPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return appPath;
	}
	
	/**
	 * @return
	 */
	public static String getTempImgPath() {
		return getSDImagePath()+System.currentTimeMillis() + ".jpg";
	}

}
