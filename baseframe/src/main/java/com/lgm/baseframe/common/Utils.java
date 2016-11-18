package com.lgm.baseframe.common;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by LGM on 2015/12/2.
 */
public class Utils {

	public static String encryptMD5(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF-8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	public static Bitmap getViewBitmap(View v) {
		if (v.getWidth() == 0 || v.getHeight() == 0)
			return null;
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
		Canvas c = new Canvas(b);
		v.draw(c);
		return ThumbnailUtils.extractThumbnail(b,b.getWidth()/4,b.getHeight()/4);
	}

//	public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {
//		final Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
//		final RenderScript rs = RenderScript.create(context);
//		final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
//		final Allocation output = Allocation.createTyped(rs, input.getType());
//		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//		script.setRadius(radius);
//		script.setInput(input);
//		script.forEach(output);
//		output.copyTo(bitmap);
//
//		sentBitmap.recycle();
//		rs.destroy();
//		input.destroy();
//		output.destroy();
//		script.destroy();
//
//		return bitmap;
//	}

	// 返回十六进制字符�?
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}

	public static void showShortToast(Context context, String text) {
		try {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void showAlertDialog(Context context, String title,
	                                   String message, String positive, String negative,
	                                   final PositiveListener pListener) {
		if (TextUtils.isEmpty(message)) {
			return;
		}
		// final Context fContext = context;
		// final String fMessage = message.replace("-","");
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// Main.this.finish();
				if (pListener == null) {
					return;
				}
				pListener.OnPositiveClick();

			}
		});
		builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public interface PositiveListener {
		public void OnPositiveClick();
	}


	/**
	 * 列表对话框
	 *
	 * @param context
	 * @param names
	 * @param listener
	 */
	public static void showItemDialog(Context context, String title,
	                                  String[] names, DialogInterface.OnClickListener listener) {
		if (names == null || names.length < 1) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setItems(names, listener);
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}
		builder.show();
	}

	/**
	 * 列表对话框
	 *
	 * @param context
	 * @param names
	 * @param listener
	 */
	public static void showItemDialog(Context context, String[] names,
	                                  DialogInterface.OnClickListener listener) {
		showItemDialog(context, null, names, listener);
	}


	/**
	 * <p>
	 * 获取一个当前正在运行任务中屏幕顶部的Activity ComponentName
	 * </p>
	 *
	 * @return 返回屏幕顶部Activity ComponentName, 未获取到Activity则返回null
	 */
	public static ComponentName getRunningTopActivity(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取当前正在运行的程序
		try {
			List<ActivityManager.RunningTaskInfo> runningTaskInfoList = am.getRunningTasks(1);
			ActivityManager.RunningTaskInfo taskInfo = runningTaskInfoList.get(0);
			return taskInfo.topActivity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getVersionName(Context context) {
		try {
			String pkName = context.getApplicationContext().getPackageName();
			String versionName = context.getApplicationContext()
					.getPackageManager().getPackageInfo(pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
		}
		return null;
	}

}
