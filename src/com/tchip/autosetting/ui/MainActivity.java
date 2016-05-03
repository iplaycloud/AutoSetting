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
	private Button  itemApp;

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
		RelativeLayout itemWifi = (RelativeLayout) findViewById(R.id.itemWifi);
		itemWifi.setOnClickListener(myOnClickListener);
		// 热点分享
		RelativeLayout itemWifiAp = (RelativeLayout) findViewById(R.id.itemWifiAp);
		itemWifiAp.setOnClickListener(myOnClickListener);
		// 流量使用情况
		RelativeLayout itemDataUsage = (RelativeLayout) findViewById(R.id.itemDataUsage);
		itemDataUsage.setOnClickListener(myOnClickListener);
		// APN设置
		RelativeLayout itemAPN = (RelativeLayout) findViewById(R.id.itemAPN);
		itemAPN.setVisibility(Constant.Module.hasAPNSetting ? View.VISIBLE
				: View.GONE);
		itemAPN.setOnClickListener(myOnClickListener);
		// 碰撞侦测
		RelativeLayout itemCrash = (RelativeLayout) findViewById(R.id.itemCrash);
		itemCrash.setOnClickListener(myOnClickListener);
		// 停车守卫
		RelativeLayout itemParkMonitor = (RelativeLayout) findViewById(R.id.itemParkMonitor);
		itemParkMonitor.setOnClickListener(myOnClickListener);
		// 存储
		RelativeLayout itemStorage = (RelativeLayout) findViewById(R.id.itemStorage);
		itemStorage.setOnClickListener(myOnClickListener);
		// USB连接设置
		RelativeLayout itemUsb = (RelativeLayout) findViewById(R.id.itemUsb);
		itemUsb.setOnClickListener(myOnClickListener);
		// 日期
		RelativeLayout itemDate = (RelativeLayout) findViewById(R.id.itemDate);
		itemDate.setOnClickListener(myOnClickListener);
		// 恢复出厂设置
		RelativeLayout itemReset = (RelativeLayout) findViewById(R.id.itemReset);
		itemReset.setOnClickListener(myOnClickListener);
		// OTA
		RelativeLayout itemOTA = (RelativeLayout) findViewById(R.id.itemOTA);
		itemOTA.setOnClickListener(myOnClickListener);
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

		// Below is OLD

		itemApp = (Button) findViewById(R.id.itemApp);
		itemApp.setOnClickListener(myOnClickListener);

		// Wi-Fi
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

	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.itemDisplay:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.DISPLAY);
				break;

			case R.id.itemSound:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.VOLUME);
				break;

			case R.id.itemWifi:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.WIFI);
				break;

			case R.id.itemDataUsage:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.DATA_USAGE);
				break;

			case R.id.itemAPN:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.APN);
				break;

			case R.id.itemCrash:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.CRASH);
				break;

			case R.id.itemParkMonitor:
				// TODO: Change switch state
				break;

			case R.id.itemStorage:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.STORAGE);
				break;

			case R.id.itemUsb:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.USB);
				break;

			case R.id.itemDate:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.DATE);
				break;

			case R.id.itemReset:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.RESET);
				break;

			case R.id.itemOTA:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.OTA);
				break;

			case R.id.itemAbout:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.ABOUT);
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

			case R.id.itemApp:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.APP);
				break;

			default:
				break;
			}

		}
	}
}
