package com.marsman.app;

import java.io.File;

import android.app.Application;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.marsman.R;
import com.marsman.utils.MyLogger;

public class MarsManApplication extends Application {

	public int[] Mars_Mouse;
	public int[] Mars_Face;
	public int[] Mars_Eye;
	public int[] Mars_Hat;
	public int[] Mars_Norse;
	public int[] Mars_Glass;
	public int[] Mars_Hair;
	
	public static int witch;
	public static int height;
	public  String filePathString = "";
	
	private MyLogger myLogger = MyLogger.getLogger("MarsManApplication");

	private static  MarsManApplication application=null;
	public  MarsManApplication() {

		Mars_Mouse = new int[9];
		Mars_Mouse[0] = R.drawable.maozi;
		Mars_Mouse[1] = R.drawable.meimao;
		Mars_Mouse[2] = R.drawable.mouse;
		Mars_Mouse[3] = R.drawable.eye;
		Mars_Mouse[4] = R.drawable.face;
		Mars_Mouse[5] = R.drawable.glass;
		Mars_Mouse[6] = R.drawable.head;
		Mars_Mouse[7] = R.drawable.norse;
		Mars_Mouse[8] = R.drawable.maozi;
	}

	public static MarsManApplication getMarsManApplication(){
		if(application==null){
			application = new MarsManApplication();
		}
		return application;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		WindowManager windowManager = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		witch = dm.widthPixels;
		height = dm.heightPixels;
		createSDCardDir();
	}
	public void createSDCardDir() {
				// 创建一个文件夹对象，赋值为外部存储器的目录
				File sdcardDir = Environment.getExternalStoragePublicDirectory(
													Environment.DIRECTORY_PICTURES);
				// 得到一个路径，内容是sdcard的文件夹路径和名字
				filePathString = sdcardDir.getAbsolutePath() + File.separator+"mars";// newPath在程序中要声明
				File path1 = new File(filePathString);
				if (!path1.exists()) {
					// 若不存在，创建目录，可以在应用启动的时候创建
					path1.mkdirs();
					filePathString = path1.getAbsolutePath();
					myLogger.d("file====="+filePathString);
				}else{
					myLogger.d("存在file====="+filePathString);
				}
	}
	public String getSDPath() {
		File SDdir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			SDdir = Environment.getExternalStorageDirectory();
		}
		if (SDdir != null) {
			return SDdir.toString();
		} else {
			return null;
		}
	}
}
