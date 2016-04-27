package com.tchip.autosetting.ui;

import com.tchip.autosetting.Constant;
import com.tchip.autosetting.R;
import com.tchip.autosetting.util.OpenUtil;
import com.tchip.autosetting.util.SettingUtil;
import com.tchip.autosetting.util.OpenUtil.MODULE_TYPE;
import com.tchip.autosetting.util.TypefaceUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends Activity {

	private Switch switchFM;
	private Switch switchUVC;
	private Switch switchWifi, switchParking;
	private EditText textInput;
	private Button btnSet;
	private Button itemSystemSetting; // 系统设置
	private Button itemAbout, itemDeviceTest, itemEngineerMode, itemApp,
			itemDataUsage, itemDate;

	private WifiManager wifiManager;
	/** WiFi状态监听器 */
	private IntentFilter wifiIntentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		initialLayout();
	}

	private void initialLayout() {
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		// 时钟:Magic
		TextClock textClock = (TextClock) findViewById(R.id.textClock);
		textClock.setTypeface(TypefaceUtil.get(this, Constant.Path.FONT
				+ "Font-Helvetica-Neue-LT-Pro.otf"));
		textClock.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intentMagic = new Intent(MainActivity.this,
						MagicActivity.class);
				startActivity(intentMagic);
				return false;
			}
		});
		// 显示
		RelativeLayout itemDisplay = (RelativeLayout) findViewById(R.id.itemDisplay);
		itemDisplay.setOnClickListener(myOnClickListener);
		// 声音
		RelativeLayout itemSound = (RelativeLayout) findViewById(R.id.itemSound);
		itemSound.setOnClickListener(myOnClickListener);
		// Wi-Fi
		// 热点分享
		// 流量使用情况
		// APN设置
		// 碰撞侦测
		// 停车守卫
		// 存储设置
		// USB连接设置
		// 日期和时间
		// 恢复出厂设置
		// 系统升级
		// 关于设备
		RelativeLayout itemAbout = (RelativeLayout) findViewById(R.id.itemAbout);
		itemAbout.setOnClickListener(myOnClickListener);

		switchFM = (Switch) findViewById(R.id.switchFM);
		switchFM.setChecked(SettingUtil.isFMEnable());
		switchFM.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SettingUtil.setFMEnable(isChecked);
			}
		});

		switchUVC = (Switch) findViewById(R.id.switchUVC);
		switchUVC.setChecked(SettingUtil.isUVCEnable());
		switchUVC.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SettingUtil.setUVCEnable(isChecked);
			}
		});

		textInput = (EditText) findViewById(R.id.textInput);
		btnSet = (Button) findViewById(R.id.btnSet);
		btnSet.setOnClickListener(myOnClickListener);

		Button btnQuickSetting = (Button) findViewById(R.id.btnQuickSetting);
		btnQuickSetting.setOnClickListener(myOnClickListener);

		itemSystemSetting = (Button) findViewById(R.id.itemSystemSetting);
		itemSystemSetting.setOnClickListener(myOnClickListener);

		itemDeviceTest = (Button) findViewById(R.id.itemDeviceTest);
		itemDeviceTest.setOnClickListener(myOnClickListener);

		itemEngineerMode = (Button) findViewById(R.id.itemEngineerMode);
		itemEngineerMode.setOnClickListener(myOnClickListener);

		itemDataUsage = (Button) findViewById(R.id.itemDataUsage);
		itemDataUsage.setOnClickListener(myOnClickListener);

		// Below is OLD

		itemApp = (Button) findViewById(R.id.itemApp);
		itemApp.setOnClickListener(myOnClickListener);

		// Wi-Fi
		RelativeLayout layoutRippleWifi = (RelativeLayout) findViewById(R.id.layoutRippleWifi);
		layoutRippleWifi.setOnClickListener(new MyOnClickListener());
		switchWifi = (Switch) findViewById(R.id.switchWifi);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		switchWifi.setChecked(wifiManager.isWifiEnabled());

		switchWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked != wifiManager.isWifiEnabled()) {
					wifiManager.setWifiEnabled(isChecked);
				}
			}
		});

		wifiIntentFilter = new IntentFilter();
		wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifiIntentFilter.setPriority(Integer.MAX_VALUE);

		// 热点共享
		RelativeLayout layoutWifiAp = (RelativeLayout) findViewById(R.id.layoutWifiAp);
		layoutWifiAp.setOnClickListener(myOnClickListener);

		// 流量使用情况
		RelativeLayout layoutRippleTraffic = (RelativeLayout) findViewById(R.id.layoutRippleTraffic);
		layoutRippleTraffic.setOnClickListener(myOnClickListener);

		// APN设置
		RelativeLayout layoutApnSetting = (RelativeLayout) findViewById(R.id.layoutApnSetting);
		layoutApnSetting
				.setVisibility(Constant.Module.hasAPNSetting ? View.VISIBLE
						: View.GONE);
		layoutApnSetting.setOnClickListener(myOnClickListener);

		// 碰撞侦测
		RelativeLayout layoutGravity = (RelativeLayout) findViewById(R.id.layoutGravity);
		layoutGravity.setOnClickListener(myOnClickListener);

		// 停车侦测开关
		switchParking = (Switch) findViewById(R.id.switchParking);
		switchParking.setChecked(false/* isParkingMonitorOn() */); // FIXME
		switchParking.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { // FIXME
				// SettingUtil.setParkingMonitor(MainActivity.this, isChecked);
			}
		});
		RelativeLayout layoutRippleParking = (RelativeLayout) findViewById(R.id.layoutRippleParking);
		layoutRippleParking.setOnClickListener(myOnClickListener);

		// 存储
		RelativeLayout layoutRippleStorage = (RelativeLayout) findViewById(R.id.layoutRippleStorage);
		layoutRippleStorage.setOnClickListener(myOnClickListener);

		// USB连接设置
		RelativeLayout layoutRippleUsb = (RelativeLayout) findViewById(R.id.layoutRippleUsb);
		layoutRippleUsb.setOnClickListener(myOnClickListener);

		// 日期
		RelativeLayout itemDate = (RelativeLayout) findViewById(R.id.itemDate);
		itemDate.setOnClickListener(myOnClickListener);

		// 恢复出厂设置
		RelativeLayout layoutRippleReset = (RelativeLayout) findViewById(R.id.layoutRippleReset);
		layoutRippleReset.setOnClickListener(myOnClickListener);

		// OTA
		RelativeLayout layoutOTA = (RelativeLayout) findViewById(R.id.layoutOTA);
		layoutOTA.setOnClickListener(myOnClickListener);

	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.itemDisplay:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_DISPLAY);
				break;
				
			case R.id.itemSound:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_VOLUME);
				break;

			// FIXME:Below is OLD
			case R.id.btnSet:
				String strContent = textInput.getText().toString();
				if (strContent.trim().length() > 0 && strContent != null) {
					SettingUtil.writeAudioNode(strContent);
				} else {
					Toast.makeText(MainActivity.this, "请输入", Toast.LENGTH_SHORT)
							.show();
				}
				break;

			case R.id.btnQuickSetting:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.QUICK_SETTING);
				break;

			case R.id.itemSystemSetting:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_SYSTEM);
				break;

			case R.id.itemDeviceTest:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.DEVICE_TEST);
				break;

			case R.id.itemEngineerMode:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.ENGINEER_MODE);
				break;

			case R.id.itemApp:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.SETTING_APP);
				break;

			case R.id.itemDataUsage:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_DATA_USAGE);
				break;

			case R.id.itemDate:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.SETTING_DATE);
				break;

			case R.id.itemAbout:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_ABOUT);
				break;

			default:
				break;
			}

		}
	}
}
