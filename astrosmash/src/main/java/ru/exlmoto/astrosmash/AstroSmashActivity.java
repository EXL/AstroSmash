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
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.AstroSmashEngine.Version;

public class AstroSmashActivity extends Activity {

	public static final String ASTRO_SMASH_TAG = "AstroSmash";
	public static final int RESTART_GAME_NO = 0;
	public static final int RESTART_GAME_YES = 1;

	private static AstroSmashView astroSmashView = null;

	public static boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		astroSmashView = new AstroSmashView(this);

		if (AstroSmashSettings.sound) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
		}

		setContentView(astroSmashView);
	}

	public static AstroSmashView getAstroSmashView() {
		return astroSmashView;
	}

	public static void toDebug(String message) {
		Log.d(ASTRO_SMASH_TAG, message);
	}

	private int convertCoordX(float xCoord) {
		return Math.round(xCoord * Version.getWidth() / astroSmashView.getScreenWidth());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int touchId = event.getPointerCount() - 1;
		if (touchId < 0) {
			return false;
		}
		int action = event.getActionMasked();
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			if (!astroSmashView.isM_bRunning()) {
				AstroSmashActivity.toDebug("Restart Game");
				if (astroSmashView.checkHiScores(RESTART_GAME_YES) == -1) {
					if (astroSmashView.isGameOver()) {
						astroSmashView.SetisGameOver(false);
						astroSmashView.restartGame(false);
					}
				}
			} else if (event.getY(touchId) < astroSmashView.getScreenHeightPercent()) {
				paused = !paused;
				astroSmashView.pause(paused);
			} else if (event.getY(touchId) < astroSmashView.getScreenRectChunkProcent() &&
					event.getY(touchId) > astroSmashView.getScreenHeightPercent()) {
				astroSmashView.fire();
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getY(touchId) < astroSmashView.getScreenRectChunkProcent() &&
					event.getY(touchId) > astroSmashView.getScreenHeightPercent()) {
				astroSmashView.fire();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!paused) {
				if (event.getY(touchId) > astroSmashView.getScreenRectChunkProcent()) {
					astroSmashView.setShipX(convertCoordX(event.getX(touchId)));
				}
			}
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		paused = false;
		astroSmashView.checkHiScores(AstroSmashActivity.RESTART_GAME_NO);
		super.onBackPressed();
	}
}
