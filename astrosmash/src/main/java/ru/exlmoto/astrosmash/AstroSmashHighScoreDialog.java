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

import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;

public class AstroSmashHighScoreDialog extends Activity {

	private int score;
	private int index;
	private int restart;

	private Button buttonOk = null;
	private Button buttonCancel = null;

	private EditText playerName = null;
	private TextView scoreView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		score = getIntent().getIntExtra("peakScore", 0);
		index = getIntent().getIntExtra("indexScore", -1);
		restart = getIntent().getIntExtra("restartGame", 0);

		setContentView(R.layout.activity_dialoghiscore);

		setTitle(getResources().getText(R.string.app_name).toString() +
				getResources().getText(R.string.GameOver).toString());

		scoreView = (TextView) findViewById(R.id.textViewScore);
		scoreView.setText(getResources().getText(R.string.Score).toString() + score);

		playerName = (EditText) findViewById(R.id.editTextPlayerName);
		String modelName = Build.MANUFACTURER.subSequence(0, 3).toString();
		modelName = modelName.toUpperCase(Locale.getDefault());
		modelName += "-" + Build.MODEL;
		AstroSmashActivity.toDebug(modelName);
		playerName.setText(modelName);

		buttonOk = (Button) findViewById(R.id.buttonOk);
		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = playerName.getText().toString();
				name = name.trim();
				if (name.equals("")) {
					name = "Player";
				}
				if (name.length() > 11) {
					name = name.subSequence(0, 11).toString();
				}
				insertScore(name, score, index);
				finish();
			}
		});

		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

	@Override
	protected void onDestroy() {
		if (restart == AstroSmashActivity.RESTART_GAME_YES) {
			if (AstroSmashActivity.getAstroSmashView() != null) {
				AstroSmashActivity.getAstroSmashView().restartGame(false);
			}
		}
		super.onDestroy();
	}
}
