package com.tchip.autosetting.ui;

import com.tchip.autosetting.R;
import com.tchip.autosetting.R.id;
import com.tchip.autosetting.R.layout;
import com.tchip.autosetting.util.OpenUtil;
import com.tchip.autosetting.util.SettingUtil;
import com.tchip.autosetting.util.OpenUtil.MODULE_TYPE;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends Activity {

	private Switch switchFM;
	private Switch switchUVC;
	private EditText textInput;
	private Button btnSet;
	private Button itemSystemSetting; // 系统设置
	private Button itemAbout, itemDeviceTest, itemEngineerMode, itemApp,
			itemDataUsage,itemDate;

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

		itemAbout = (Button) findViewById(R.id.itemAbout);
		itemAbout.setOnClickListener(myOnClickListener);

		itemDeviceTest = (Button) findViewById(R.id.itemDeviceTest);
		itemDeviceTest.setOnClickListener(myOnClickListener);

		itemEngineerMode = (Button) findViewById(R.id.itemEngineerMode);
		itemEngineerMode.setOnClickListener(myOnClickListener);

		itemApp = (Button) findViewById(R.id.itemApp);
		itemApp.setOnClickListener(myOnClickListener);

		itemDataUsage = (Button) findViewById(R.id.itemDataUsage);
		itemDataUsage.setOnClickListener(myOnClickListener);
		
		itemDate = (Button) findViewById(R.id.itemDate);
		itemDate.setOnClickListener(myOnClickListener);
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
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

			case R.id.itemAbout:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_ABOUT);
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

			default:
				break;
			}

		}

	}
}
