package com.tchip.autosetting;

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

			default:
				break;
			}

		}

	}
}
