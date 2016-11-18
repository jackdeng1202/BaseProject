package com.lgm.baseframe.common;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志工具
 * 
 * @author GM
 * 
 */
public class LogUtil {

	/**
	 * 日志打印开关
	 */
	public static boolean prtLog = true;

	/**
	 * {@link Log#v(String, String)}
	 */
	public static void v(String tag, String msg) {
		if (prtLog) {
			Log.v(tag, msg);
			//writeToFile(tag, msg);
		}
	}

	/**
	 * {@link Log#w(String, String)}
	 * 
	 */
	public static void w(String tag, String msg) {
		if (prtLog) {
			Log.w(tag, msg);
		}
	}

	/**
	 * {@link Log#d(String, String)}
	 */

	public static void d(String tag, String msg) {
		if (prtLog) {
			Log.d(tag, msg);
		}
	}

	/**
	 * {@link Log#e(String, String)}
	 * 
	 */
	public static void e(String tag, String msg) {
		if (prtLog) {
			Log.e(tag, msg);
		}
	}

	/**
	 * {@link Log#i(String, String)}
	 */
	public static void i(String tag, String msg) {
		if (prtLog) {
			Log.i(tag, msg);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 将日志输出到sd卡指定文件 按日期输出
	 * 
	 * @param msg
	 *            内容
	 * @param tag
	 *            标签
	 */
	public static void writeToFile(String tag, String msg) {
		deleteLog();
		FileWriter filerWriter = null;
		BufferedWriter bufWriter = null;
		Date nowtime = new Date();
		// String needWriteFiel = logfile.format(nowtime);
		/* 设置输出内容 */
		String needWriteMessage = myLogSdf.format(nowtime) + "    " + tag
				+ "    " + msg;
		/* 新建文件夹 */
		File filePath = new File(PathUtil.getLogPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		/* 新建文件 */
		File file = new File(PathUtil.getLogPath() + "/" + "zuanbank_"
				+ Build.MODEL + "-" + Build.ID + (nowtime.getYear() + 1900)
				+ "-" + (nowtime.getMonth() + 1) + "-" + nowtime.getDate()
				+ ".txt");
		try {
			/* 写入文件 */
			// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			filerWriter = new FileWriter(file, true);
			bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/* 关闭流 */
			try {
				if (bufWriter != null) {
					bufWriter.close();
				}
				if (filerWriter != null) {
					filerWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除指定日志日志文件
	 */
	public static void deleteLog() {
		Date date;
		Date dateNow;
		Calendar cal = Calendar.getInstance();
		Calendar calNow = Calendar.getInstance();
		/* 指定日志文件夹 */
		File filepath = new File(PathUtil.getLogPath());
		File[] files;
		if (filepath.exists()) {
			if (filepath.isDirectory()) {
				files = filepath.listFiles();
				/* 遍历文件 检测日期 符合条件的文件将删除 */
				for (int i = 0; i < files.length; i++) {
					date = new Date(files[i].lastModified());
					dateNow = new Date();
					cal.setTime(date);
					calNow.setTime(dateNow);
					int dec = calNow.get(Calendar.DAY_OF_YEAR)
							- cal.get(Calendar.DAY_OF_YEAR);
					// 删除指定日期的文件
					if (dec >= 3) {
						files[i].delete();
					}
				} // end for
			} // end if (filepath.isDirectory())
		} // end if (filepath.exists())
	}

	/**
	 * 删除指定路径的文件
	 * 
	 * @param path
	 *            指定路径
	 */
	public static void deleteFile(String path) {
		File filepath = new File(path);
		if (filepath.exists()) {
			if (filepath.isFile()) {
				filepath.delete();
			}
		}
	}
	
	
	/**
	 * 打印异常信息
	 * 
	 * @param arg1
	 * @return
	 */
	public static void writeErrorInfo(String tag,Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		LogUtil.writeToFile("tag", error);
	}


}
