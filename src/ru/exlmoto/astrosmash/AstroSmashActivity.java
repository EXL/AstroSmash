package ru.exlmoto.astrosmash;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
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
	private static SoundPool soundPool = null;
	private static ToneGenerator toneGenerator = null;

	public static int SOUND_HIT;
	public static int SOUND_UFO;
	public static int SOUND_SHIP;
	public static int SOUND_SHOT;

	private boolean paused = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		astroSmashView = new AstroSmashView(this);

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


		if (AstroSmashSettings.sound) {
			soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
			toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

			SOUND_HIT = soundPool.load(this, R.raw.s_hit, 1);
			SOUND_UFO = soundPool.load(this, R.raw.s_ufo, 1);
			SOUND_SHIP = soundPool.load(this, R.raw.s_ship, 1);
			SOUND_SHOT = soundPool.load(this, R.raw.s_shot, 1);
		}

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
