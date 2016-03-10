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

		/** USB/UVC切换节点:40-USB 41-UVC */
		public static final String NODE_USB_UVC_SWITCH = "/sys/bus/i2c/devices/0-007f/Spk_Choose_Num";
	}

}
