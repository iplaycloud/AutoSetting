package com.tchip.autosetting;

public interface Constant {
	/** Debug：打印Log */
	public static final boolean isDebug = true;
	/** 日志Tag */
	public static final String TAG = "AZ";

	/** SharedPreferences */
	public static final class MySP {
		/** 名称 */
		public static final String NAME = "AutoSetting";

	}

	/** 广播 */
	public static final class Broadcast {

	}

	public static final class Setting {

	}

	public static final class Module {

		/** 进入MagicActivity的密码 */
		public static final String MagicCode = "55555";

	}

	/** 路径 */
	public static final class Path {

		/** 字体目录 */
		public static final String FONT = "fonts/";

		/**
		 * 
		 * 
		 * 音频通道：0-系统到喇叭 1-蓝牙到喇叭 2-系统到FM 3-BT到FM
		 */
		public static final String NODE_SWITCH_AUDIO = "/sys/bus/i2c/devices/0-007f/Spk_Choose_Num";

		/** USB/UVC切换节点:0-USB 1-UVC */
		public static final String NODE_SWITCH_USB_UVC = "/sys/bus/i2c/devices/0-007f/Connect_To_PC";

		/** FM开关:0-下电 1-上电 */
		public static final String NODE_FM_ENABLE = "/sys/bus/i2c/devices/2-0011/enable_si4712";

		/** FM频率 */
		public static final String NODE_FM_FREQUENCY = "/sys/bus/i2c/devices/2-0011/setch_si4712";

	}

}
