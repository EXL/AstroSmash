package ru.exlmoto.astrosmash;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.os.Vibrator;

import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.AstroSmashEngine.Version;

public class AstroSmashActivity extends Activity {

	public static final String ASTRO_SMASH_TAG = "AstroSmash";
	public static final int RESTART_GAME_NO = 0;
	public static final int RESTART_GAME_YES = 1;

	public static final int VIBRATE_SHORT = 20;
	public static final int VIBRATE_LONG = 70;

	private static AstroSmashView astroSmashView = null;
	private static Vibrator vibrator = null;

	private boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		astroSmashView = new AstroSmashView(this);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		setContentView(astroSmashView);
	}

	public static AstroSmashView getAstroSmashView() {
		return astroSmashView;
	}

	public static void toDebug(String message) {
		Log.d(ASTRO_SMASH_TAG, message);
	}

	public static void doVibrate(int duration) {
		if (AstroSmashSettings.vibro) {
			vibrator.vibrate(duration);
		}
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
			if (event.getY(touchId) > astroSmashView.getScreenRectChunkProcent()) {
				astroSmashView.setShipX(convertCoordX(event.getX(touchId)));
			} else if (event.getY(touchId) < astroSmashView.getScreenRectChunkProcent() && 
					event.getY(touchId) > astroSmashView.getScreenHeightPercent()) {
				astroSmashView.fire();
			}
			break;
		}
		return true;
	}
}
