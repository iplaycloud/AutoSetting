package com.tchip.autosetting.util;

import com.tchip.autosetting.ui.QuickSettingActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

public class OpenUtil {

	public enum MODULE_TYPE {

		/** 设置 */
		AUTO_SETTING,
		
		/** 性能监视器 */
		CPU_INFO,

		/** 设备测试 */
		DEVICE_TEST,

		/** 工程模式 */
		ENGINEER_MODE,

		/** MTKLogger */
		MTK_LOGGER,

		/** 快速设置 */
		QUICK_SETTING,

		/** 关于 */
		SETTING_ABOUT,

		/** 应用 */
		SETTING_APP,

		/** 流量使用情况 */
		SETTING_DATA_USAGE,

		/** 日期和时间 */
		SETTING_DATE,

		/** 显示设置 */
		SETTING_DISPLAY,

		/** FM发射设置 */
		SETTING_FM,

		/** 位置 */
		SETTING_LOCATION,

		/** 音量设置 */
		SETTING_VOLUME,

		/** 备份和重置 */
		SETTING_RESET,

		/** 存储设置 */
		SETTING_STORAGE,

		/** 系统设置 */
		SETTING_SYSTEM,

		/** Wi-Fi */
		WIFI,

		/** Wi-Fi热点 */
		WIFI_AP,
	}

	public static void openModule(Activity activity, MODULE_TYPE moduleTye) {
		if (!ClickUtil.isQuickClick(1000)) {
			try {
				switch (moduleTye) {
				case AUTO_SETTING:
					ComponentName componentSetting = new ComponentName(
							"com.tchip.autosetting",
							"com.tchip.autosetting.ui.MainActivity");
					Intent intentSetting = new Intent();
					intentSetting.setComponent(componentSetting);
					activity.startActivity(intentSetting);
					break;
					
				case CPU_INFO:
					Intent intentCPUInfo = new Intent(Intent.ACTION_VIEW);
					intentCPUInfo.setClassName("eu.chainfire.perfmon",
							"com.common.activity.MainActivity");
					intentCPUInfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intentCPUInfo);
					break;

				case DEVICE_TEST:
					Intent intentDeviceTest = new Intent(Intent.ACTION_VIEW);
					intentDeviceTest.setClassName("com.DeviceTest",
							"com.DeviceTest.DeviceTest");
					intentDeviceTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intentDeviceTest);
					break;

				case ENGINEER_MODE:
					Intent intentEngineerMode = new Intent(Intent.ACTION_VIEW);
					intentEngineerMode.setClassName(
							"com.mediatek.engineermode",
							"com.mediatek.engineermode.EngineerMode");
					intentEngineerMode.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intentEngineerMode);
					break;

				case MTK_LOGGER:
					Intent intentMtkLogger = new Intent(Intent.ACTION_VIEW);
					intentMtkLogger.setClassName("com.mediatek.mtklogger",
							"com.mediatek.mtklogger.MainActivity");
					intentMtkLogger.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intentMtkLogger);

					break;

				case QUICK_SETTING:
					Intent intentQuickSetting = new Intent(activity,
							QuickSettingActivity.class);
					intentQuickSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intentQuickSetting);
					break;

				case SETTING_ABOUT:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS));
					break;

				case SETTING_APP:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
					break;

				case SETTING_DATA_USAGE:
					activity.startActivity(new Intent(
							"android.settings.DATA_USAGE_SETTINGS"));
					break;

				case SETTING_DATE:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_DATE_SETTINGS));
					break;
					
				case SETTING_DISPLAY:
					break;

				case SETTING_FM:
					activity.startActivity(new Intent(
							"android.settings.FM_SETTINGS"));
					break;

				case SETTING_LOCATION:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					break;
					
				case SETTING_VOLUME:
					break;

				case SETTING_RESET:
					activity.startActivity(new Intent(
							"android.settings.BACKUP_AND_RESET_SETTINGS"));
					break;

				case SETTING_STORAGE:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS));
					break;

				case SETTING_SYSTEM:
					ComponentName componentSettingSystem = new ComponentName(
							"com.android.settings",
							"com.android.settings.Settings");
					Intent intentSettingSystem = new Intent();
					intentSettingSystem.setComponent(componentSettingSystem);
					activity.startActivity(intentSettingSystem);
					break;

				case WIFI:
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
					break;

				case WIFI_AP:
					activity.startActivity(new Intent(
							"android.settings.TETHER_WIFI_SETTINGS"));
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
