package com.tchip.autosetting.ui;

import java.io.File;

import com.tchip.autosetting.R;
import com.tchip.autosetting.util.HintUtil;
import com.tchip.autosetting.util.SettingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ImageSensorModifyActivity extends Activity {

	private Context context;
	private static final String NODE_LIGHT_DAY = "/sys/bus/i2c/devices/0-007f/light_day";
	private static final String NODE_LIGHT_NIGHT_1 = "/sys/bus/i2c/devices/0-007f/light_night";
	private static final String NODE_LIGHT_NIGHT_2 = "/sys/bus/i2c/devices/0-007f/light_night2";
	private static final String NODE_CONTRAST_DAY = "/sys/bus/i2c/devices/0-007f/contrast_day";
	private static final String NODE_CONTRAST_NIGHT = "/sys/bus/i2c/devices/0-007f/contrast_night";

	private EditText editLightDay, editLightNight, editLightNight2;
	private EditText editContrastDay, editContrastNight;
	private Button btnApply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_image_sensor_modify);
		setTitle(getResources().getString(R.string.magic_open_img_sensor));

		initialLayout();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initialLayout() {
		editLightDay = (EditText) findViewById(R.id.editLightDay);
		editLightNight = (EditText) findViewById(R.id.editLightNight);
		editLightNight2 = (EditText) findViewById(R.id.editLightNight2);

		editContrastDay = (EditText) findViewById(R.id.editContrastDay);
		editContrastNight = (EditText) findViewById(R.id.editContrastNight);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnApply.setOnClickListener(new MyOnClickListener());
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnApply:
				// 亮度(白天)
				String strLightDay = editLightDay.getText().toString();
				if (strLightDay != null && strLightDay.trim().length() > 0) {
					int intLight = Integer.parseInt(strLightDay);
					if (intLight >= 0 && intLight <= 255) {
						SettingUtil.SaveFileToNode(new File(NODE_LIGHT_DAY),
								strLightDay);
					} else {
						HintUtil.showToast(context,
								getString(R.string.image_sensor_0_255));
					}
				}
				// 亮度(晚上1)
				String strLightNight = editLightNight.getText().toString();
				if (strLightNight != null && strLightNight.trim().length() > 0) {
					int intLight = Integer.parseInt(strLightNight);
					if (intLight >= 0 && intLight <= 255) {
						SettingUtil.SaveFileToNode(
								new File(NODE_LIGHT_NIGHT_1), strLightNight);
					} else {
						HintUtil.showToast(context,
								getString(R.string.image_sensor_0_255));
					}
				}
				// 亮度(晚上2)
				String strLightNight2 = editLightNight2.getText().toString();
				if (strLightNight2 != null
						&& strLightNight2.trim().length() > 0) {
					int intLight = Integer.parseInt(strLightNight2);
					if (intLight >= 0 && intLight <= 255) {
						SettingUtil.SaveFileToNode(
								new File(NODE_LIGHT_NIGHT_2), strLightNight2);
					} else {
						HintUtil.showToast(context,
								getString(R.string.image_sensor_0_255));
					}
				}
				// 对比度(白天)
				String strContrastDay = editContrastDay.getText().toString();
				if (strContrastDay != null
						&& strContrastDay.trim().length() > 0) {
					int intContrast = Integer.parseInt(strContrastDay);
					if (intContrast >= 0 && intContrast <= 255) {
						SettingUtil.SaveFileToNode(new File(NODE_CONTRAST_DAY),
								strContrastDay);
					} else {
						HintUtil.showToast(context,
								getString(R.string.image_sensor_0_255));
					}
				}
				// 对比度(晚上)
				String strContrastNight = editContrastNight.getText()
						.toString();
				if (strContrastNight != null
						&& strContrastNight.trim().length() > 0) {
					int intContrast = Integer.parseInt(strContrastNight);
					if (intContrast >= 0 && intContrast <= 255) {
						SettingUtil
								.SaveFileToNode(new File(NODE_CONTRAST_NIGHT),
										strContrastNight);
					} else {
						HintUtil.showToast(context,
								getString(R.string.image_sensor_0_255));
					}
				}
				break;

			default:
				break;
			}
		}
	}

}
