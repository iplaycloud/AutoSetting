package com.tchip.autosetting.ui;

import com.tchip.autosetting.Constant;
import com.tchip.autosetting.R;
import com.tchip.autosetting.util.OpenUtil;
import com.tchip.autosetting.util.ProviderUtil;
import com.tchip.autosetting.util.ProviderUtil.Name;
import com.tchip.autosetting.util.SettingUtil;
import com.tchip.autosetting.util.OpenUtil.MODULE_TYPE;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MagicActivity extends Activity {
	private Context context;
	private EditText textPass;
	private RelativeLayout layoutMagic;

	private Switch switchFM;
	private Switch switchUVC;
	private Switch switchAccOffWake;

	private EditText textInput;
	private Button btnSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(null);
		context = getApplicationContext();
		setContentView(R.layout.activity_magic);

		initialLayout();
	}

	private void initialLayout() {
		MyOnClickListener myOnClickListener = new MyOnClickListener();
		layoutMagic = (RelativeLayout) findViewById(R.id.layoutMagic);

		textPass = (EditText) findViewById(R.id.textPass);
		textPass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String strInput = textPass.getText().toString();
				if (Constant.Module.MagicCode.equals(strInput)) {
					layoutMagic.setVisibility(View.VISIBLE);
				}
			}
		});

		// Row 1
		Button btnDeviceTest = (Button) findViewById(R.id.btnDeviceTest);
		btnDeviceTest.setOnClickListener(myOnClickListener);
		Button btnEngineerMode = (Button) findViewById(R.id.btnEngineerMode);
		btnEngineerMode.setOnClickListener(myOnClickListener);
		Button btnSystemSetting = (Button) findViewById(R.id.btnSystemSetting);
		btnSystemSetting.setOnClickListener(myOnClickListener);
		Button btnMtkLogger = (Button) findViewById(R.id.btnMtkLogger);
		btnMtkLogger.setOnClickListener(myOnClickListener);
		Button btnCPUInfo = (Button) findViewById(R.id.btnCPUInfo);
		btnCPUInfo.setOnClickListener(myOnClickListener);
		// Row 2
		Button btnDeveloperSetting = (Button) findViewById(R.id.btnDeveloperSetting);
		btnDeveloperSetting.setOnClickListener(myOnClickListener);
		Button btnApplication = (Button) findViewById(R.id.btnApplication);
		btnApplication.setOnClickListener(myOnClickListener);
		Button btnCamera = (Button) findViewById(R.id.btnCamera);
		btnCamera.setOnClickListener(myOnClickListener);
		switchAccOffWake = (Switch) findViewById(R.id.switchAccOffWake);
		switchAccOffWake.setChecked("1".equals(ProviderUtil.getValue(context,
				Name.DEBUG_ACCOFF_WAKE, "0")));
		switchAccOffWake
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						ProviderUtil.setValue(context, Name.DEBUG_ACCOFF_WAKE,
								isChecked ? "1" : "0");
						SettingUtil.setAccOffWake(isChecked);
					}
				});

		// Row 3
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
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnDeviceTest:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.DEVICE_TEST);
				break;

			case R.id.btnEngineerMode:
				OpenUtil.openModule(MagicActivity.this,
						MODULE_TYPE.ENGINEER_MODE);
				break;

			case R.id.btnSystemSetting:
				OpenUtil.openModule(MagicActivity.this,
						MODULE_TYPE.SYSTEM_SETTING);
				break;

			case R.id.btnMtkLogger:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.MTK_LOGGER);
				break;

			case R.id.btnCPUInfo:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.CPU_INFO);
				break;

			case R.id.btnDeveloperSetting:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.DEV_SETTING);
				break;

			case R.id.btnApplication:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.APP);
				break;

			case R.id.btnCamera:
				OpenUtil.openModule(MagicActivity.this,
						MODULE_TYPE.SYSTEM_CAMERA);
				break;

			case R.id.btnSet:
				String strContent = textInput.getText().toString();
				if (strContent.trim().length() > 0 && strContent != null) {
					SettingUtil.writeAudioNode(strContent);
				} else {
					Toast.makeText(MagicActivity.this, "请输入",
							Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}

		}

	}

}
