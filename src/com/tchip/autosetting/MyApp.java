package com.tchip.autosetting;


import android.app.Application;

public class MyApp extends Application {

	/** 碰撞侦测开关:默认打开 **/
	public static boolean isCrashOn = Constant.GravitySensor.DEFAULT_ON;

	/** 碰撞侦测级别 **/
	public static int crashSensitive = Constant.GravitySensor.SENSITIVE_DEFAULT;

	@Override
	public void onCreate() {
		super.onCreate();
	}

}
