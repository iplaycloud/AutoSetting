package com.tchip.autosetting.ui;

import com.tchip.autosetting.Constant;
import com.tchip.autosetting.R;
import com.tchip.autosetting.util.HintUtil;
import com.tchip.autosetting.util.OpenUtil;
import com.tchip.autosetting.util.ProviderUtil;
import com.tchip.autosetting.util.ProviderUtil.Name;
import com.tchip.autosetting.util.SettingUtil;
import com.tchip.autosetting.util.TelephonyUtil;
import com.tchip.autosetting.util.OpenUtil.MODULE_TYPE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class QuickSettingActivity extends Activity {
	private Context context;

	private AudioManager audioManager;
	private WifiManager wifiManager;
	private int secondCount = 1;

	private ImageView imageWifi;
	private ImageView imageData;
	private ImageView imageBluetooth;
	private ImageView imageLocation;
	private ImageView imageAirplane;
	private ImageView imageSetting;

	/** WiFi状态监听器 **/
	private IntentFilter intentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_quick_setting);

		initialLayout();
		new Thread(new AutoFinishThread()).start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(quickReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (quickReceiver != null) {
			unregisterReceiver(quickReceiver);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initialLayout() {
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		MyOnLongClickListener myOnLongClickListener = new MyOnLongClickListener();
		// 亮度SeekBar
		SeekBar seekBarBright = (SeekBar) findViewById(R.id.seekBarBright);
		seekBarBright.setMax(Constant.Setting.MAX_BRIGHTNESS);
		seekBarBright.setProgress(SettingUtil.getBrightness(context));
		seekBarBright.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				SettingUtil.setBrightness(context, seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				secondCount = 1;
				SettingUtil.setBrightness(context, progress);
			}
		});

		// 媒体音量SeekBar
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		SeekBar seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
		seekBarVolume.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBarVolume.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		seekBarVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						seekBar.getProgress(), 0);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				secondCount = 1;
			}
		});

		imageWifi = (ImageView) findViewById(R.id.imageWifi);
		imageWifi.setOnClickListener(myOnClickListener);
		imageWifi.setOnLongClickListener(myOnLongClickListener);
		imageData = (ImageView) findViewById(R.id.imageData);
		imageData.setOnClickListener(myOnClickListener);
		imageBluetooth = (ImageView) findViewById(R.id.imageBluetooth);
		imageBluetooth.setOnClickListener(myOnClickListener);
		imageBluetooth.setOnLongClickListener(myOnLongClickListener);
		imageLocation = (ImageView) findViewById(R.id.imageLocation);
		imageLocation.setOnClickListener(myOnClickListener);
		imageAirplane = (ImageView) findViewById(R.id.imageAirplane);
		imageAirplane.setOnClickListener(myOnClickListener);
		imageSetting = (ImageView) findViewById(R.id.imageSetting);
		imageSetting.setOnClickListener(myOnClickListener);

		updateIconState();

		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intentFilter.setPriority(Integer.MAX_VALUE);
	}

	/** 数据流量是否打开 */
	private boolean isMobileDataOn(Context context) {
		String strMobileData = ProviderUtil.getValue(context,
				Name.SET_MOBILE_DATA);
		if (null != strMobileData && strMobileData.trim().length() > 0
				&& "1".equals(strMobileData)) {
			return true;
		} else
			return false;
	}

	private void updateIconState() {
		// Wi-Fi
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		imageWifi.setImageDrawable(getResources().getDrawable(
				wifiManager.isWifiEnabled() ? R.drawable.quick_setting_wifi_on
						: R.drawable.quick_setting_wifi_off, null));
		// 数据流量
		if (TelephonyUtil.isAirplaneModeOn(context)) {
			imageData.setImageDrawable(getResources().getDrawable(
					R.drawable.quick_setting_data_off, null));
		} else {
			imageData.setImageDrawable(getResources().getDrawable(
					isMobileDataOn(context) ? R.drawable.quick_setting_data_on
							: R.drawable.quick_setting_data_off, null));
		}
		// 蓝牙
		boolean isBluetoothEnable = "1".equals(Settings.System.getString(
				getContentResolver(), "bt_enable"));
		imageBluetooth.setImageDrawable(getResources().getDrawable(
				isBluetoothEnable ? R.drawable.quick_setting_bluetooth_on
						: R.drawable.quick_setting_bluetooth_off, null));
		// GPS
		imageLocation.setImageDrawable(getResources().getDrawable(
				SettingUtil.isGpsOn(context) ? R.drawable.quick_setting_gps_on
						: R.drawable.quick_setting_gps_off, null));
		// Airplane Mode
		imageAirplane
				.setImageDrawable(getResources()
						.getDrawable(
								TelephonyUtil.isAirplaneModeOn(context) ? R.drawable.quick_setting_airplane_on
										: R.drawable.quick_setting_airplane_off,
								null));
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			secondCount = 1;
			switch (v.getId()) {
			case R.id.imageWifi:
				wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
				break;

			case R.id.imageData:
				if (TelephonyUtil.isAirplaneModeOn(context)) {
					HintUtil.showToast(
							context,
							getResources().getString(
									R.string.quick_setting_airplane_on));
				} else {
					sendBroadcast(new Intent(
							isMobileDataOn(context) ? Constant.Broadcast.MOBILE_DATA_OFF
									: Constant.Broadcast.MOBILE_DATA_ON));
				}
				break;

			case R.id.imageBluetooth:
				sendBroadcast(new Intent(Constant.Broadcast.BT_STATUS_CHANGE));
				break;

			case R.id.imageLocation:
				sendBroadcast(new Intent(
						SettingUtil.isGpsOn(context) ? Constant.Broadcast.GPS_OFF
								: Constant.Broadcast.GPS_ON));
				break;

			case R.id.imageAirplane:
				sendBroadcast(new Intent(
						TelephonyUtil.isAirplaneModeOn(context) ? Constant.Broadcast.AIRPLANE_OFF
								: Constant.Broadcast.AIRPLANE_ON));
				break;

			case R.id.imageSetting:
				finish();
				Intent intentSetting = new Intent(QuickSettingActivity.this,
						MainActivity.class);
				startActivity(intentSetting);
				break;

			default:
				break;
			}

		}

	}

	class MyOnLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			secondCount = 1;
			switch (v.getId()) {
			case R.id.imageWifi:
				OpenUtil.openModule(QuickSettingActivity.this, MODULE_TYPE.WIFI);
				break;

			case R.id.imageBluetooth:
				OpenUtil.openModule(QuickSettingActivity.this,
						MODULE_TYPE.DIALER);
				break;

			default:
				break;
			}
			return false;
		}

	}

	private BroadcastReceiver quickReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
				int wifi_state = intent.getIntExtra("wifi_state", 0);
				switch (wifi_state) {
				case WifiManager.WIFI_STATE_DISABLING:
				case WifiManager.WIFI_STATE_UNKNOWN:
				case WifiManager.WIFI_STATE_DISABLED:
					imageWifi.setImageDrawable(getResources().getDrawable(
							R.drawable.quick_setting_wifi_off, null));
					break;

				case WifiManager.WIFI_STATE_ENABLING:
				case WifiManager.WIFI_STATE_ENABLED:
					imageWifi.setImageDrawable(getResources().getDrawable(
							R.drawable.quick_setting_wifi_on, null));
					break;
				}
			} else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
				imageAirplane
						.setImageDrawable(getResources()
								.getDrawable(
										intent.getBooleanExtra("state", false) ? R.drawable.quick_setting_airplane_on
												: R.drawable.quick_setting_airplane_off,
										null));
			}
		}
	};

	/**
	 * 无操作3秒后关闭音量调节界面
	 */
	class AutoFinishThread implements Runnable {

		@Override
		public void run() {
			while (secondCount < 10) {
				try {
					Thread.sleep(1000);
					secondCount++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (secondCount >= 8) {
					Message messageFinish = new Message();
					messageFinish.what = 1;
					autoFinishHandler.sendMessage(messageFinish);
				} else {
					Message messageFinish = new Message();
					messageFinish.what = 2;
					autoFinishHandler.sendMessage(messageFinish);
				}
			}

		}
	}

	final Handler autoFinishHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				finish();
				break;

			case 2: // Update Icon
				updateIconState();
				break;

			default:
				break;
			}
		}
	};

}
