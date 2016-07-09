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

	private boolean paused = false;

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
					astroSmashView.restartGame(false);
				}
			} else if (event.getY(touchId) < astroSmashView.getScreenHeightPercent()) {
				paused = !paused;
				astroSmashView.pause(paused);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!paused) {
				if (event.getY(touchId) > astroSmashView.getScreenRectChunkProcent()) {
					astroSmashView.setShipX(convertCoordX(event.getX(touchId)));
				} else if (event.getY(touchId) < astroSmashView.getScreenRectChunkProcent() &&
						event.getY(touchId) > astroSmashView.getScreenHeightPercent()) {
					astroSmashView.fire();
				}
			}
			break;
		}
		return true;
	}
}
