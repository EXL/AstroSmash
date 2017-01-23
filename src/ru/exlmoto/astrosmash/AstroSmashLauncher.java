/************************************************************************************
** The MIT License (MIT)
**
** Copyright (c) 2016 Serg "EXL" Koles
**
** Permission is hereby granted, free of charge, to any person obtaining a copy
** of this software and associated documentation files (the "Software"), to deal
** in the Software without restriction, including without limitation the rights
** to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
** copies of the Software, and to permit persons to whom the Software is
** furnished to do so, subject to the following conditions:
**
** The above copyright notice and this permission notice shall be included in all
** copies or substantial portions of the Software.
**
** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
** IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
** AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
** LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
** OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
** SOFTWARE.
************************************************************************************/

package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import ru.exlmoto.astrosmash.AstroSmashEngine.InfoStrings;

public class AstroSmashLauncher extends Activity {

	public static final int SCALE_120P = 0;
	public static final int SCALE_176P = 1;
	public static final int SCALE_240P = 2;
	public static final int SCALE_480P = 3;

	public static final int HISCORE_PLAYERS = 10;

	public static final int VIBRATE_SHORT = 20;
	public static final int VIBRATE_LONG = 70;

	public static int SOUND_HIT;
	public static int SOUND_UFO;
	public static int SOUND_SHIP;
	public static int SOUND_SHOT;

	public static SharedPreferences settingsStorage = null;

	public static class AstroSmashSettings {

		public static boolean autoFire = true;
		public static boolean sound = true;
		public static boolean vibro = true;
		public static boolean antialiasing = true;
		public static boolean showTouchRect = true;
		public static boolean colorizeStars = false;
		public static boolean drawFps = false;
		public static boolean doubleFire = false;

		public static int graphicsScale = SCALE_240P;

		public static String[] playerNames = { "Zorge.R", "baat", "Osta", "Armhalfer", "J()KER",
				"a1batross", "mvb06", "NoPH8", "PUSYA", "Neko-mata" };
		public static final int[] playerScores = { 100000, 90000, 80000, 70000, 60000, 50000, 40000, 30000, 20000, 10000 };
	}

	private AlertDialog aboutDialog = null;
	private AlertDialog helpDialog = null;

	private CheckBox autoFireCheckBox = null;
	private CheckBox soundCheckBox = null;
	private CheckBox vibroCheckBox = null;
	private CheckBox antialiasingCheckBox = null;
	private CheckBox showTouchRectCheckBox = null;
	private CheckBox colorizeStarsCheckBox = null;
	private CheckBox drawFpsCheckBox = null;
	private CheckBox doubleFireCheckBox = null;

	private RadioButton radioButton120 = null;
	private RadioButton radioButton176 = null;
	private RadioButton radioButton240 = null;
	private RadioButton radioButton480 = null;

	private static TextView playerNamesView;
	private static TextView playerScoresView;

	private static Vibrator vibrator = null;
	private static SoundPool soundPool = null;
	private static ToneGenerator toneGenerator = null;

	public void fillSettingsByLayout() {
		AstroSmashSettings.autoFire = autoFireCheckBox.isChecked();
		AstroSmashSettings.sound = soundCheckBox.isChecked();
		AstroSmashSettings.vibro = vibroCheckBox.isChecked();
		AstroSmashSettings.antialiasing = antialiasingCheckBox.isChecked();
		AstroSmashSettings.showTouchRect = showTouchRectCheckBox.isChecked();
		AstroSmashSettings.colorizeStars = colorizeStarsCheckBox.isChecked();
		AstroSmashSettings.drawFps = drawFpsCheckBox.isChecked();
		AstroSmashSettings.doubleFire = doubleFireCheckBox.isChecked();

		if (radioButton120.isChecked()) {
			AstroSmashSettings.graphicsScale = SCALE_120P;
		} else if (radioButton176.isChecked()) {
			AstroSmashSettings.graphicsScale = SCALE_176P;
		} else if (radioButton240.isChecked()) {
			AstroSmashSettings.graphicsScale = SCALE_240P;
		} else if (radioButton480.isChecked()) {
			AstroSmashSettings.graphicsScale = SCALE_480P;
		}
	}

	public void fillLayoutBySettings() {
		autoFireCheckBox.setChecked(AstroSmashSettings.autoFire);
		soundCheckBox.setChecked(AstroSmashSettings.sound);
		vibroCheckBox.setChecked(AstroSmashSettings.vibro);
		antialiasingCheckBox.setChecked(AstroSmashSettings.antialiasing);
		showTouchRectCheckBox.setChecked(AstroSmashSettings.showTouchRect);
		colorizeStarsCheckBox.setChecked(AstroSmashSettings.colorizeStars);
		drawFpsCheckBox.setChecked(AstroSmashSettings.drawFps);
		doubleFireCheckBox.setChecked(AstroSmashSettings.doubleFire);

		switch (AstroSmashSettings.graphicsScale) {
		case SCALE_120P:
			radioButton120.setChecked(true);
			break;
		case SCALE_176P:
			radioButton176.setChecked(true);
			break;
		case SCALE_240P:
			radioButton240.setChecked(true);
			break;
		case SCALE_480P:
			radioButton480.setChecked(true);
			break;
		default:
			break;
		}

		updateGameTable();
	}

	public static void updateGameTable() {
		String players = "";
		String scores = "";
		for (int i = 0; i < HISCORE_PLAYERS; ++i) {
			players += AstroSmashSettings.playerNames[i];
			scores += AstroSmashSettings.playerScores[i];
			if (i < HISCORE_PLAYERS - 1) {
				players += "\n";
				scores += "\n";
			}
		}
		playerNamesView.setText(players);
		playerScoresView.setText(scores);
	}

	public void readSettings() {
		AstroSmashSettings.autoFire = settingsStorage.getBoolean("autoFire", true);
		AstroSmashSettings.sound = settingsStorage.getBoolean("sound", true);
		AstroSmashSettings.vibro = settingsStorage.getBoolean("vibro", true);
		AstroSmashSettings.antialiasing = settingsStorage.getBoolean("antialiasing", true);
		AstroSmashSettings.showTouchRect = settingsStorage.getBoolean("showTouchRect", true);
		AstroSmashSettings.colorizeStars = settingsStorage.getBoolean("colorizeStars", false);
		AstroSmashSettings.drawFps = settingsStorage.getBoolean("drawFps", false);
		AstroSmashSettings.doubleFire = settingsStorage.getBoolean("doubleFire", false);

		AstroSmashSettings.graphicsScale = settingsStorage.getInt("graphicsScale", SCALE_240P);

		for (int i = 0; i < HISCORE_PLAYERS; ++i) {
			AstroSmashSettings.playerNames[i] = settingsStorage.getString("player" + i, AstroSmashSettings.playerNames[i]);
			AstroSmashSettings.playerScores[i] = settingsStorage.getInt("score" + i, AstroSmashSettings.playerScores[i]);
		}
	}

	private void writeSettings() {
		AstroSmashActivity.toDebug("Write Settings!");

		fillSettingsByLayout();

		SharedPreferences.Editor editor = settingsStorage.edit();

		editor.putBoolean("autoFire", AstroSmashSettings.autoFire);
		editor.putBoolean("sound", AstroSmashSettings.sound);
		editor.putBoolean("vibro", AstroSmashSettings.vibro);
		editor.putBoolean("antialiasing", AstroSmashSettings.antialiasing);
		editor.putBoolean("showTouchRect", AstroSmashSettings.showTouchRect);
		editor.putBoolean("colorizeStars", AstroSmashSettings.colorizeStars);
		editor.putBoolean("drawFps", AstroSmashSettings.drawFps);
		editor.putBoolean("doubleFire", AstroSmashSettings.doubleFire);

		editor.putInt("graphicsScale", AstroSmashSettings.graphicsScale);

		editor.commit();
	}

	public void initWidgets() {
		autoFireCheckBox = (CheckBox) findViewById(R.id.checkBoxAutoFire);
		soundCheckBox = (CheckBox) findViewById(R.id.checkBoxSound);
		vibroCheckBox = (CheckBox) findViewById(R.id.checkBoxVibro);
		antialiasingCheckBox = (CheckBox) findViewById(R.id.checkBoxAntialiasing);
		showTouchRectCheckBox = (CheckBox) findViewById(R.id.checkBoxTouchRect);
		colorizeStarsCheckBox = (CheckBox) findViewById(R.id.checkBoxColorStars);
		drawFpsCheckBox = (CheckBox) findViewById(R.id.checkBoxFPS);
		doubleFireCheckBox = (CheckBox) findViewById(R.id.checkBoxDoubleFire);

		radioButton120 = (RadioButton) findViewById(R.id.radioButton120);
		radioButton176 = (RadioButton) findViewById(R.id.radioButton176);
		radioButton240 = (RadioButton) findViewById(R.id.radioButton240);
		radioButton480 = (RadioButton) findViewById(R.id.radioButton480);

		playerNamesView = (TextView) findViewById(R.id.PlayerNames);
		playerScoresView = (TextView) findViewById(R.id.PlayerScores);
	}

	private void initHelpDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setCancelable(false);
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_help, null);
		builder.setView(dialogView);
		builder.setTitle(R.string.app_name);
		builder.setPositiveButton(R.string.StringOK, null);

		TextView helpText = (TextView) dialogView.findViewById(R.id.textViewHelp);
		TextView creditsText1 = (TextView) dialogView.findViewById(R.id.textViewCredits1);
		TextView creditsText2 = (TextView) dialogView.findViewById(R.id.textViewCredits2);
		TextView creditsText3 = (TextView) dialogView.findViewById(R.id.textViewCredits3);
		helpText.setText(InfoStrings.getHelp());
		creditsText1.setText(InfoStrings.getCredits1());
		creditsText2.setText(InfoStrings.getCredits2());
		creditsText3.setText(InfoStrings.getCredits3());

		helpDialog = builder.create();
	}

	private void initAboutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setCancelable(false);
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_about, null);
		builder.setView(dialogView);
		builder.setTitle(R.string.app_name);
		builder.setPositiveButton(R.string.StringOK, null);
		aboutDialog = builder.create();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_astrosmashlauncher);

		settingsStorage = getSharedPreferences("ru.exlmoto.astrosmash", MODE_PRIVATE);
		// Check the first run
		if (settingsStorage.getBoolean("firstrun", true)) {
			// The first run, fill GUI layout with default values
			settingsStorage.edit().putBoolean("firstrun", false).commit();
		} else {
			// Read settings from Shared Preferences
			readSettings();
		}

		initHelpDialog();
		initAboutDialog();

		InfoStrings.initializeInfo();

		initWidgets();

		fillLayoutBySettings();

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

		SOUND_HIT = soundPool.load(this, R.raw.s_hit, 1);
		SOUND_UFO = soundPool.load(this, R.raw.s_ufo, 1);
		SOUND_SHIP = soundPool.load(this, R.raw.s_ship, 1);
		SOUND_SHOT = soundPool.load(this, R.raw.s_shot, 1);

		Button astroSmashRunButton = (Button) findViewById(R.id.gameButton);
		astroSmashRunButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				writeSettings();

				Intent intent = new Intent(v.getContext(), AstroSmashActivity.class);
				startActivity(intent);
			}
		});

		Button aboutButton = (Button) findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMyDialog(aboutDialog);
			}
		});

		Button helpButton = (Button) findViewById(R.id.buttonHelp);
		helpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMyDialog(helpDialog);
			}
		});
	}

	public static void doVibrate(int duration) {
		if (AstroSmashSettings.vibro) {
			vibrator.vibrate(duration);
		}
	}

	public static void playSound(int soundID) {
		if (AstroSmashSettings.sound && (soundID != 0)) {
			final int SOUND_ID = soundID;

			new Thread(new Runnable() {

				@Override
				public void run() {
					soundPool.play(SOUND_ID, 1.0f, 1.0f, 0, 0, 1.0f);
				}

			}).start();
		}
	}

	public static void playGameOverSound() {
		if (AstroSmashSettings.sound) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					toneGenerator.startTone(ToneGenerator.TONE_CDMA_PRESSHOLDKEY_LITE);
				}

			}).start();
		}
	}

	// Prevent dialog dismiss when orientation changes
	// http://stackoverflow.com/a/27311231/2467443
	private static void doKeepDialog(AlertDialog dialog) {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
	}

	private void showMyDialog(AlertDialog dialog) {
		dialog.show();
		doKeepDialog(dialog);
	}

	@Override
	protected void onDestroy() {
		writeSettings();

		super.onDestroy();
	}
}
