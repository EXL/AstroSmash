package ru.exlmoto.astrosmash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;

public class AstroSmashHighScoreDialog extends Activity {

	private int score;
	private int index;
	private int restart;

	private Button buttonOk = null;
	private Button buttonCancel = null;

	private EditText playerName = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		score = getIntent().getIntExtra("peakScore", 0);
		index = getIntent().getIntExtra("indexScore", -1);
		restart = getIntent().getIntExtra("restartGame", 0);

		setContentView(R.layout.activity_dialoghiscore);

		setTitle(getResources().getText(R.string.app_name).toString() +
				getResources().getText(R.string.GameOver).toString());

		setFinishOnTouchOutside(false);

		playerName = (EditText) findViewById(R.id.editTextPlayerName);

		buttonOk = (Button) findViewById(R.id.buttonOk);
		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				insertScore(playerName.getText().toString(), score, index);
				if (restart == AstroSmashActivity.RESTART_GAME_YES) {
					if (AstroSmashActivity.getAstroSmashView() != null) {
						AstroSmashActivity.getAstroSmashView().restartGame(false);
					}
				}
				finish();
			}
		});

		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (restart == AstroSmashActivity.RESTART_GAME_YES) {
					if (AstroSmashActivity.getAstroSmashView() != null) {
						AstroSmashActivity.getAstroSmashView().restartGame(false);
					}
				}
				finish();
			}
		});
	}

	private void insertScore(String name, int score, int i) {
		if (i != -1) {
			String localObject = AstroSmashSettings.playerNames[i];
			String str = null;
			int j = AstroSmashSettings.playerScores[i];
			int k = 0;
			for (int m = i + 1; m < AstroSmashLauncher.HISCORE_PLAYERS; m++) {
				k = AstroSmashSettings.playerScores[m];
				str = AstroSmashSettings.playerNames[m];
				AstroSmashSettings.playerScores[m] = j;
				AstroSmashSettings.playerNames[m] = localObject;
				j = k;
				localObject = str;
			}
			AstroSmashSettings.playerNames[i] = name;
			AstroSmashSettings.playerScores[i] = score;

			saveHiScores();

			AstroSmashLauncher.updateGameTable();
		}
	}

	public static void saveHiScores() {
		if (AstroSmashLauncher.settingsStorage != null) {
			SharedPreferences.Editor editor = AstroSmashLauncher.settingsStorage.edit();
			for (int i = 0; i < AstroSmashLauncher.HISCORE_PLAYERS; ++i) {
				editor.putString("player" + i, AstroSmashSettings.playerNames[i]);
				editor.putInt("score" + i, AstroSmashSettings.playerScores[i]);
			}
			editor.commit();
		} else {
			AstroSmashActivity.toDebug("Error: settingsStorage is null!");
		}
	}
}
