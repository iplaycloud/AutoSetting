package com.tchip.autosetting.ui;

import com.tchip.autosetting.Constant;
import com.tchip.autosetting.R;
import com.tchip.autosetting.util.HintUtil;
import com.tchip.autosetting.util.OpenUtil;
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
import android.view.View;
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
		imageData = (ImageView) findViewById(R.id.imageData);
		imageData.setOnClickListener(myOnClickListener);
		imageBluetooth = (ImageView) findViewById(R.id.imageBluetooth);
		imageBluetooth.setOnClickListener(myOnClickListener);
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

	private void updateIconState() {
		// Wi-Fi
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		imageWifi.setImageDrawable(getResources().getDrawable(
				wifiManager.isWifiEnabled() ? R.drawable.quick_setting_wifi_on
						: R.drawable.quick_setting_wifi_off, null));
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
				break;

			case R.id.imageBluetooth:
				break;

			case R.id.imageLocation:
				break;

			case R.id.imageAirplane:
				HintUtil.showToast(context,
						"Need WRITE_SECURE_SETTING Permission");
				// TelephonyUtil.setAirplaneMode(context,
				// !TelephonyUtil.isAirplaneModeOn(context));
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

			default:
				break;
			}
		}
	};

}
