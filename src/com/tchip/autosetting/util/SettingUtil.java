package com.tchip.autosetting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.tchip.autosetting.Constant;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;

public class SettingUtil {

	private static File nodeUsbUvcSwitch = new File(
			Constant.Path.NODE_SWITCH_USB_UVC);

	private static File nodeSwitchAudio = new File(
			Constant.Path.NODE_SWITCH_AUDIO);

	private static File nodeFMEnable = new File(Constant.Path.NODE_FM_ENABLE);
	private static File nodeFMFrequency = new File(
			Constant.Path.NODE_FM_FREQUENCY);

	public static void setUVCEnable(boolean isUVCOn) {
		MyLog.v("[SettingUtil]setFMEnable:" + isUVCOn);
		SaveFileToNode(nodeUsbUvcSwitch, isUVCOn ? "1" : "0");
	}

	public static boolean isUVCEnable() {
		return getFileInt(nodeUsbUvcSwitch) == 1;
	}

	public static void setFMEnable(boolean isFmOn) {
		MyLog.v("[SettingUtil]setFMEnable:" + isFmOn);
		SaveFileToNode(nodeFMEnable, isFmOn ? "1" : "0");
	}

	public static boolean isFMEnable() {
		return getFileInt(nodeFMEnable) == 1;
	}

	public static void writeAudioNode(String content) {
		MyLog.v("[SettingUtil]writeAudioNode:" + content);
		SaveFileToNode(nodeSwitchAudio, content);
	}

	public static void setUsbMode(boolean isUsbOn) {
		MyLog.v("[SettingUtil]setUsbMode:" + isUsbOn);
		SaveFileToNode(nodeUsbUvcSwitch, isUsbOn ? "40" : "41");
	}

	private static boolean isUsbMode() {

		int fileValue = 0;
		String strValue = "";
		if (nodeUsbUvcSwitch.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(nodeUsbUvcSwitch), "utf-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					strValue += lineTXT.toString();
				}
				read.close();

				fileValue = Integer.parseInt(strValue);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]isUsbMode: FileNotFoundException");
			} catch (IOException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]isUsbMode: IOException");
			}
		}
		MyLog.v("[SettingUtil]isUsbMode,fileValue:" + fileValue);

		return fileValue == 40;
	}

	public static void setScreenOffTime(Context context, int time) {
		boolean isSuccess = Settings.System.putInt(
				context.getContentResolver(),
				android.provider.Settings.System.SCREEN_OFF_TIMEOUT, time);

		MyLog.v("[SettingUtil]setScreenOffTime " + time + ",isSuccess:"
				+ isSuccess);

	}

	public static int getScreenOffTime(Context context) {
		try {
			return Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
			return 155;
		}
	}

	public static void SaveFileToNode(File file, String value) {
		if (file.exists()) {
			try {
				StringBuffer strbuf = new StringBuffer("");
				strbuf.append(value);
				OutputStream output = null;
				OutputStreamWriter outputWrite = null;
				PrintWriter print = null;
				try {
					output = new FileOutputStream(file);
					outputWrite = new OutputStreamWriter(output);
					print = new PrintWriter(outputWrite);
					print.print(strbuf.toString());
					print.flush();
					output.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					MyLog.e("[SaveFileToNode]FileNotFoundException:"
							+ e.toString());
				}
			} catch (IOException e) {
				MyLog.e("[SaveFileToNode]IOException:" + e.toString());
			}
		} else {
			MyLog.e("SaveFileToNode:File:" + file + "not exists");
		}
	}

	/**
	 * 点亮屏幕
	 * 
	 * @param context
	 */
	public static void lightScreen(Context context) {
		// 获取电源管理器对象
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");

		wl.acquire(); // 点亮屏幕
		wl.release(); // 释放

		// 得到键盘锁管理器对象
		KeyguardManager km = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);

		// 参数是LogCat里用的Tag
		KeyguardLock kl = km.newKeyguardLock("ZMS");

		kl.disableKeyguard();
	}

	/**
	 * Camera自动调节亮度节点
	 * 
	 * 1：开 0：关;默认打开
	 */
	public static File fileAutoLightSwitch = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/back_car_status");

	/**
	 * 设置Camera自动调节亮度开关
	 */
	public static void setAutoLight(Context context, boolean isAutoLightOn) {
		if (isAutoLightOn) {
			SaveFileToNode(fileAutoLightSwitch, "1");
		} else {
			SaveFileToNode(fileAutoLightSwitch, "0");
		}
		MyLog.v("[SettingUtil]setAutoLight:" + isAutoLightOn);
	}

	/**
	 * ACC状态节点
	 */
	public static File fileAccStatus = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/acc_car_status");

	/**
	 * 获取ACC状态
	 * 
	 * @return 0:ACC下电
	 * 
	 *         1:ACC上电
	 */
	public static int getAccStatus() {
		return getFileInt(fileAccStatus);
	}

	public static int getFileInt(File file) {

		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				InputStreamReader fr = new InputStreamReader(is);
				int ch = 0;
				if ((ch = fr.read()) != -1)
					return Integer.parseInt(String.valueOf((char) ch));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 获取背光亮度值
	 */
	public static int getLCDValue() {
		/** 背光值节点 **/
		File fileLCDValue = new File("/sys/class/leds/lcd-backlight/brightness");

		String strValue = "";
		if (fileLCDValue.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(fileLCDValue), "utf-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					strValue += lineTXT.toString();
				}
				read.close();

				return Integer.parseInt(strValue);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]getLCDValue: FileNotFoundException");
			} catch (IOException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]getLCDValue: IOException");
			}
		}
		return -5;
	}

	/**
	 * 电子狗电源开关节点
	 * 
	 * 1-打开
	 * 
	 * 0-关闭
	 */
	public static File fileEDogPower = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/edog_car_status");

	/**
	 * 设置电子狗电源开关
	 * 
	 * @param isEDogOn
	 */
	public static void setEDogEnable(boolean isEDogOn) {

		MyLog.v("[SettingUtil]setEDogEnable:" + isEDogOn);
		if (isEDogOn) {
			SaveFileToNode(fileEDogPower, "1");
		} else {
			SaveFileToNode(fileEDogPower, "0");
		}
	}

	/**
	 * 获取Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取设备IMEI
	 * 
	 * @param context
	 * @return
	 */
	public String getImei(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			MyLog.e("WifiPreference IpAddress:" + ex.toString());
		}
		return null;
	}

}
