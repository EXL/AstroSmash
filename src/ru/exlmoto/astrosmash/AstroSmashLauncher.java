package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class AstroSmashLauncher extends Activity {

	public static final int SCALE_120P = 0;
	public static final int SCALE_176P = 1;
	public static final int SCALE_240P = 2;
	public static final int SCALE_480P = 3;

	public static final int HISCORE_PLAYERS = 10;

	public static SharedPreferences settingsStorage = null;

	public static class AstroSmashSettings {

		public static boolean autoFire = true;
		public static boolean sound = true;
		public static boolean vibro = true;
		public static boolean antialiasing = true;
		public static boolean showTouchRect = true;
		public static boolean colorizeStars = false;
		public static boolean drawFps = false;

		public static int graphicsScale = SCALE_240P;

		public static String[] playerNames = { "Zorge.R", "Baat", "Osta", "Armhalfer", "J()KER",
				"a1batross", "mvb06", "NoPH8", "PUSYA", "Neko-mata" };
		public static final int[] playerScores = { 100000, 70000, 60000, 50000, 40000, 30000, 20000, 10000, 5000, 1000 };
	}

	private CheckBox autoFireCheckBox = null;
	private CheckBox soundCheckBox = null;
	private CheckBox vibroCheckBox = null;
	private CheckBox antialiasingCheckBox = null;
	private CheckBox showTouchRectCheckBox = null;
	private CheckBox colorizeStarsCheckBox = null;
	private CheckBox drawFpsCheckBox = null;

	private RadioButton radioButton120 = null;
	private RadioButton radioButton176 = null;
	private RadioButton radioButton240 = null;
	private RadioButton radioButton480 = null;

	private static TextView[] playerNamesView;
	private static TextView[] playerScoresView;

	public void fillSettingsByLayout() {
		AstroSmashSettings.autoFire = autoFireCheckBox.isChecked();
		AstroSmashSettings.sound = soundCheckBox.isChecked();
		AstroSmashSettings.vibro = vibroCheckBox.isChecked();
		AstroSmashSettings.antialiasing = antialiasingCheckBox.isChecked();
		AstroSmashSettings.showTouchRect = showTouchRectCheckBox.isChecked();
		AstroSmashSettings.colorizeStars = colorizeStarsCheckBox.isChecked();
		AstroSmashSettings.drawFps = drawFpsCheckBox.isChecked();

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

		for (int i = 0; i < HISCORE_PLAYERS; ++i) {
			playerNamesView[i].setText(AstroSmashSettings.playerNames[i]);
			playerScoresView[i].setText(Integer.toString(AstroSmashSettings.playerScores[i]));
		}
	}

	public static void updateGameTable() {
		for (int i = 0; i < HISCORE_PLAYERS; ++i) {
			playerNamesView[i].setText(AstroSmashSettings.playerNames[i]);
			playerScoresView[i].setText(Integer.toString(AstroSmashSettings.playerScores[i]));
		}
	}

	public void readSettings() {
		AstroSmashSettings.autoFire = settingsStorage.getBoolean("autoFire", true);
		AstroSmashSettings.sound = settingsStorage.getBoolean("sound", true);
		AstroSmashSettings.vibro = settingsStorage.getBoolean("vibro", true);
		AstroSmashSettings.antialiasing = settingsStorage.getBoolean("antialiasing", true);
		AstroSmashSettings.showTouchRect = settingsStorage.getBoolean("showTouchRect", true);
		AstroSmashSettings.colorizeStars = settingsStorage.getBoolean("colorizeStars", false);
		AstroSmashSettings.drawFps = settingsStorage.getBoolean("drawFps", false);

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

		radioButton120 = (RadioButton) findViewById(R.id.radioButton120);
		radioButton176 = (RadioButton) findViewById(R.id.radioButton176);
		radioButton240 = (RadioButton) findViewById(R.id.radioButton240);
		radioButton480 = (RadioButton) findViewById(R.id.radioButton480);

		playerNamesView = new TextView[HISCORE_PLAYERS];
		playerScoresView = new TextView[HISCORE_PLAYERS];
		playerNamesView[0] = (TextView) findViewById(R.id.PlayerName1);
		playerScoresView[0] = (TextView) findViewById(R.id.PlayerScore1);
		playerNamesView[1] = (TextView) findViewById(R.id.PlayerName2);
		playerScoresView[1] = (TextView) findViewById(R.id.PlayerScore2);
		playerNamesView[2] = (TextView) findViewById(R.id.PlayerName3);
		playerScoresView[2] = (TextView) findViewById(R.id.PlayerScore3);
		playerNamesView[3] = (TextView) findViewById(R.id.PlayerName4);
		playerScoresView[3] = (TextView) findViewById(R.id.PlayerScore4);
		playerNamesView[4] = (TextView) findViewById(R.id.PlayerName5);
		playerScoresView[4] = (TextView) findViewById(R.id.PlayerScore5);
		playerNamesView[5] = (TextView) findViewById(R.id.PlayerName6);
		playerScoresView[5] = (TextView) findViewById(R.id.PlayerScore6);
		playerNamesView[6] = (TextView) findViewById(R.id.PlayerName7);
		playerScoresView[6] = (TextView) findViewById(R.id.PlayerScore7);
		playerNamesView[7] = (TextView) findViewById(R.id.PlayerName8);
		playerScoresView[7] = (TextView) findViewById(R.id.PlayerScore8);
		playerNamesView[8] = (TextView) findViewById(R.id.PlayerName9);
		playerScoresView[8] = (TextView) findViewById(R.id.PlayerScore9);
		playerNamesView[9] = (TextView) findViewById(R.id.PlayerName10);
		playerScoresView[9] = (TextView) findViewById(R.id.PlayerScore10);
	}

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

		initWidgets();

		fillLayoutBySettings();

		Button astroSmashRunButton = (Button) findViewById(R.id.gameButton);
		astroSmashRunButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				writeSettings();

				Intent intent = new Intent(v.getContext(), AstroSmashActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		writeSettings();
		super.onDestroy();
	}
}
