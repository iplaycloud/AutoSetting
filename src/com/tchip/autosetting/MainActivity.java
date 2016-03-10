package com.tchip.autosetting;

import com.tchip.autosetting.util.SettingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity {

	private Switch switchUSB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initialLayout();
	}

	private void initialLayout() {
		switchUSB = (Switch) findViewById(R.id.switchUSB);
		switchUSB.setChecked(SettingUtil.isUsbMode());
		switchUSB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SettingUtil.setUsbMode(isChecked);
			}
		});
	}
}
