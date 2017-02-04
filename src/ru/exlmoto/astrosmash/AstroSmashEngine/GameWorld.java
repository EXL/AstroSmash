package ru.exlmoto.astrosmash.AstroSmashEngine;

import java.util.Stack;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import ru.exlmoto.astrosmash.AstroSmashLauncher;
import ru.exlmoto.astrosmash.AstroSmashLauncher.AstroSmashSettings;
import ru.exlmoto.astrosmash.AstroSmashView;

public class GameWorld
implements IDeathListener {

	public static final int AUTOFIRE_DELAY_MS = 333;
	public static final int BULLET_VELOCITY_TIME = 40;
	public static final int INITIAL_LIVES = 4;
	public static final int SHIP_HIT_SCORE = -100;
	public static final int INITIAL_LEVEL = 1;
	public static final int INITIAL_DEMO_LEVEL = 2;
	public static final int INITIAL_SCORE = 0;
	public static final int MAX_LEVEL = 6;
	private static final int SWAP_INTERVAL = 500;
	private static final int UFO_FIRE_INTERVAL = 500;
	private static final int PULSER_FALLTIME_FACTOR = 2;
	private static final int UFO_BULLET_FALLTIME_FACTOR = 2;
	private static final int[] MIN_SCORES_OF_LEVEL = { 0, Integer.MIN_VALUE, 1000, 5000, 20000, 50000, 100000 };
	private static final int[] MAX_NUM_ENEMIES = { 0, 3, 4, 5, 6, 7, 8 };
	private static final int[] ENEMY_FALLTIMES = { 0, 9000, 8000, 7000, 6000, 5000, 4000 };
	private static final int[] ENEMY_FALLTIME_VARIANCES = { 0, 4500, 4000, 3500, 3000, 2500, 2000 };
	private boolean m_bSuspendEnemies = false;
	private PerformanceMeter m_perfMeter = null;
	private int m_nScreenWidth;
	private int m_nScreenHeight;
	private Enemy m_ship = null;
	private int m_nShipMoveDistance;
	private MunitionsFactory m_munitionsFactory = null;
	private Vector<Enemy> m_vecFlyingEnemies = null;
	private Vector<Collidable> m_vecFlyingBullets = null;
	private boolean m_bAutoFire;
	private EnemyFactory m_enemyFactory = null;
	private long m_nTimeSinceLastFire;
	private Stack<Enemy> m_EnemiesToRecycleStack = null;
	private Stack<Collidable> m_BulletsToRecycleStack = null;
	private BackgroundManager m_backgroundManager = null;
	private int m_groundHeight;
	private boolean m_bGameOver = false;
	private boolean m_bGamePaused = false;
	private IGameWorldListener m_gameWorldListener = null;
	private int m_nInitialLevel = Version.getDemoFlag() ? 2 : 1;
	private volatile int m_nLevel;
	private volatile int m_nLives;
	private volatile int m_nScore;
	private int m_nPeakScore;

	private Rect textBounds = null;

	private Context activityContext = null;

	public GameWorld(int paramInt1, int paramInt2, IGameWorldListener paramIGameWorldListener, Context context) {
		this.activityContext = context;
		textBounds = new Rect();

		if (this.m_vecFlyingEnemies == null) {
			this.m_vecFlyingEnemies = new Vector<Enemy>();
		}
		if (this.m_vecFlyingBullets == null) {
			this.m_vecFlyingBullets = new Vector<Collidable>();
		}
		this.m_nScreenWidth = paramInt1;
		this.m_nScreenHeight = paramInt2;
		this.m_gameWorldListener = paramIGameWorldListener;
		if (this.m_perfMeter == null) {
			this.m_perfMeter = new PerformanceMeter();
		}
		this.m_nShipMoveDistance = Version.getShipMoveX();
		int i = this.m_nScreenWidth / 2;
		if (this.m_munitionsFactory == null) {
			this.m_munitionsFactory = new MunitionsFactory(-this.m_nShipMoveDistance, BULLET_VELOCITY_TIME, activityContext);
		}
		this.m_bAutoFire = true;
		if (this.m_backgroundManager == null) {
			this.m_backgroundManager = new BackgroundManager(this.m_nScreenWidth, this.m_nScreenHeight, this.m_nInitialLevel, 0, 4, activityContext);
		}
		this.m_groundHeight = this.m_backgroundManager.getGroundLevel();
		if (this.m_enemyFactory == null) {
			this.m_enemyFactory = new EnemyFactory(this.m_nScreenWidth, this.m_nScreenHeight, this, activityContext);
		}
		this.m_ship = this.m_enemyFactory.createShip(i, this.m_groundHeight);
		this.m_nLives = 4;
		if (this.m_EnemiesToRecycleStack == null) {
			this.m_EnemiesToRecycleStack = new Stack<Enemy>();
		}
		if (this.m_BulletsToRecycleStack == null) {
			this.m_BulletsToRecycleStack = new Stack<Collidable>();
		}
		this.m_nScore = 0;
		this.m_nPeakScore = 0;
		setLevel(this.m_nInitialLevel);
	}

	public void tick(long paramLong) {
		if (!this.m_bGamePaused) {
			if ((!this.m_ship.getCollided()) && (!this.m_bGameOver)) {
				tickBullets(paramLong);
				tickEnemies(paramLong);
				if ((this.m_bAutoFire) && (this.m_nTimeSinceLastFire > AUTOFIRE_DELAY_MS)) {
					fireBullet();
					this.m_nTimeSinceLastFire = 0L;
				} else {
					this.m_nTimeSinceLastFire += paramLong;
				}
				checkLevel();
			}
		}
	}

	public int getPeakScore() {
		return this.m_nPeakScore;
	}

	public void handleAction(int paramInt) {
		if ((!this.m_ship.getCollided()) && (!this.m_bGameOver)) {
			int i;
			switch (paramInt) {
			case 2:
				i = Math.max(this.m_ship.getX() - this.m_nShipMoveDistance, 0);
				this.m_ship.setX(i);
				break;
			case 5:
				i = Math.min(this.m_ship.getX() + this.m_nShipMoveDistance, this.m_nScreenWidth - this.m_ship.getWidth());
				this.m_ship.setX(i);
				break;
			case 1:
				this.m_bAutoFire = (!this.m_bAutoFire);
				break;
			case 6:
				hyperSpace();
				break;
			case 9:
				fireBullet();
				break;
			}
		}
	}

	public void paint(Canvas canvas, Paint paint) {
		this.m_backgroundManager.paint(canvas, paint);
		this.m_ship.paint(canvas, paint);
		paintFlyingBullets(canvas, paint);
		paintEnemies(canvas, paint);
		if (this.m_bGamePaused) {
			paintMessage(canvas, paint, InfoStrings.GAME_PAUSED_STRING, "");
		} else if (this.m_bGameOver) {
			paintMessage(canvas, paint, InfoStrings.GAME_OVER_STRING, InfoStrings.PEAK_SCORE_STRING + ": " + this.m_nPeakScore);
		}
		if (AstroSmashSettings.drawFps) {
			drawFPS(canvas, paint);
		}
	}

	public void pause(boolean paramBoolean) {
		this.m_bGamePaused = paramBoolean;
	}

	public void reset() {
		this.m_EnemiesToRecycleStack.removeAllElements();
		this.m_BulletsToRecycleStack.removeAllElements();
		this.m_nPeakScore = 0;
		this.m_backgroundManager.setPeakScore(0);
		this.m_bAutoFire = AstroSmashSettings.autoFire;
		int j;
		for (int i = this.m_vecFlyingEnemies.size(); i > 0; i = this.m_vecFlyingEnemies.size()) {
			j = i - 1;
			Enemy localEnemy = this.m_vecFlyingEnemies.elementAt(j);
			this.m_vecFlyingEnemies.removeElementAt(j);
			this.m_enemyFactory.putEnemy(localEnemy);
		}
		for (int i = this.m_vecFlyingBullets.size(); i > 0; i = this.m_vecFlyingBullets.size()) {
			j = i - 1;
			Collidable localCollidable = this.m_vecFlyingBullets.elementAt(j);
			this.m_vecFlyingBullets.removeElementAt(j);
			this.m_munitionsFactory.putBullet(localCollidable);
		}
		this.m_backgroundManager.setGameLevel(this.m_nLevel);
		this.m_nLives = 4;
		this.m_backgroundManager.setLives(this.m_nLives);
		// For Debug
		// this.m_nScore = 1000000;
		this.m_nScore = 0;
		this.m_nPeakScore = 0;
		this.m_backgroundManager.setScore(this.m_nScore);
		this.m_ship.reset();
		this.m_ship.setX(this.m_nScreenWidth / 2);
		this.m_bGameOver = false;
		this.m_bGamePaused = false;
		this.m_bSuspendEnemies = false;
		setLevel(this.m_nInitialLevel);
	}

	public Enemy getShip() {
		return this.m_ship;
	}

	protected void drawFPS(Canvas canvas, Paint paint) {
		int i = this.m_perfMeter.getTimesPerSecond();
		String str = Integer.toString(i);
		str += " fps";
		paint.getTextBounds(str, 0, str.length(), textBounds);
		paint.setColor(Version.WHITECOLOR);
		final int gap = 2;
		int j = textBounds.height() + gap;
		canvas.drawText(str, gap, j, paint);
	}

	public void fireBullet() {
		Collidable localCollidable = this.m_munitionsFactory.getBullet();
		if (localCollidable != null) {
			AstroSmashLauncher.playSound(AstroSmashLauncher.SOUND_SHOT);
			localCollidable.setPosition(this.m_ship.getCenterX() - localCollidable.getWidth() / 2, this.m_ship.getY() - localCollidable.getHeight());
			this.m_vecFlyingBullets.addElement(localCollidable);
		}
	}

	protected void tickBullets(long paramLong) {
		try {
			for (int i = 0; i < this.m_vecFlyingBullets.size(); i++) {
				Collidable localCollidable = this.m_vecFlyingBullets.elementAt(i);
				localCollidable.tick(paramLong, this);
				int j = localCollidable.getY() + localCollidable.getHeight();
				if (j <= 0) {
					sendBulletToHell(localCollidable);
				}
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			localArrayIndexOutOfBoundsException.printStackTrace();
		}
	}

	protected void paintMessage(Canvas canvas, Paint paint, String paramString1, String paramString2) {
		paint.getTextBounds(paramString1, 0, paramString1.length(), textBounds);
		int i = textBounds.height() + 5; // gap = 5 pixels
		int r = textBounds.width();
		paint.getTextBounds(paramString2, 0, paramString2.length(), textBounds);
		int r2 = textBounds.width();

		//		WTF?!
		//		if (AstroSmashVersion.getPlatform() == 6) {
		//			paint.setColor(AstroSmashVersion.BLACKCOLOR);
		//		} else {
		//			paint.setColor(AstroSmashVersion.WHITECOLOR);
		//		}
		paint.setColor(Version.WHITECOLOR);

		// Draw message on center screen
		canvas.drawText(paramString1, this.m_nScreenWidth / 2 - r / 2, this.m_nScreenHeight / 2, paint);
		if ((paramString2 != null) && (!paramString2.equals(""))) {
			canvas.drawText(paramString2, this.m_nScreenWidth / 2 - r2 / 2, this.m_nScreenHeight / 2 + i, paint);
		}
	}

	protected void paintFlyingBullets(Canvas canvas, Paint paint) {
		try {
			for (int i = 0; i < this.m_vecFlyingBullets.size(); i++) {
				Collidable localCollidable = this.m_vecFlyingBullets.elementAt(i);
				localCollidable.paint(canvas, paint);
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			localArrayIndexOutOfBoundsException.printStackTrace();
		}
	}

	protected void hyperSpace() {
		this.m_ship.setX(AstroSmashView.getAbsRandomInt() % (this.m_nScreenWidth - this.m_ship.getWidth()));
	}

	protected void tickEnemies(long paramLong) {
		try {
			for (int i = 0; i < this.m_vecFlyingEnemies.size(); i++) {
				Enemy localEnemy2 = this.m_vecFlyingEnemies.elementAt(i);
				localEnemy2.tick(paramLong, this);
				if ((localEnemy2.getY() + localEnemy2.getHeight() >= this.m_groundHeight) || (localEnemy2.getX() + localEnemy2.getWidth() >= this.m_nScreenWidth) || (localEnemy2.getY() < 0) || (localEnemy2.getX() < 0)) {
					if ((localEnemy2.getY() + localEnemy2.getHeight() >= this.m_groundHeight) || (localEnemy2.getY() < 0)) {
						updateScore(localEnemy2.getGroundScore());
					}
					this.m_EnemiesToRecycleStack.push(localEnemy2);
					if ((localEnemy2.getEnemyTypeId() == 8) || (localEnemy2.getEnemyTypeId() == 9)) {
						this.m_ship.setCollided(true);
						shipDestroyed();
						updateScore(-100);
						break;
					}
				} else {
					if (this.m_ship.intersects(localEnemy2)) {
						shipDestroyed();
						updateScore(-100);
						break;
					}
					for (int k = 0; k < this.m_vecFlyingBullets.size(); k++) {
						Collidable localCollidable = this.m_vecFlyingBullets.elementAt(k);
						if (localCollidable.intersects(localEnemy2, 1, 2)) {
							AstroSmashLauncher.doVibrate(AstroSmashLauncher.VIBRATE_SHORT);
							AstroSmashLauncher.playSound(AstroSmashLauncher.SOUND_HIT);
							updateScore(localEnemy2.getHitScore());
							sendBulletToHell(localCollidable);
							break;
						}
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			localArrayIndexOutOfBoundsException.printStackTrace();
		}
		sendWaitingEnemiesToFactory();
		if (!this.m_bSuspendEnemies) {
			if (this.m_vecFlyingEnemies.size() < MAX_NUM_ENEMIES[this.m_nLevel]) {
				Enemy localEnemy1 = this.m_enemyFactory.getRandomEnemy(this.m_nLevel, this.m_nScore);
				int j;
				if (localEnemy1.getEnemyTypeId() == 11) {
					j = AstroSmashView.getAbsRandomInt() % (this.m_nScreenHeight / 3);
					localEnemy1.setPosition(0, j);
				} else {
					j = AstroSmashView.getAbsRandomInt() % (this.m_nScreenWidth - localEnemy1.getWidth());
					localEnemy1.setPosition(j, 0);
				}
				switch (localEnemy1.getEnemyTypeId()) {
				case 8:
				case 9:
					j = ENEMY_FALLTIMES[this.m_nLevel];
					localEnemy1.setVelocity(0, this.m_nScreenHeight, j);
					((SwappableEnemy)localEnemy1).setSwapInterval(SWAP_INTERVAL);
					break;
				case 10:
					j = ENEMY_FALLTIMES[this.m_nLevel] / PULSER_FALLTIME_FACTOR;
					localEnemy1.setVelocity(0, this.m_nScreenHeight, j);
					((SwappableEnemy)localEnemy1).setSwapInterval(SWAP_INTERVAL);
					break;
				case 11:
					j = ENEMY_FALLTIMES[this.m_nLevel] + AstroSmashView.getRandomInt() % ENEMY_FALLTIME_VARIANCES[this.m_nLevel];
					localEnemy1.setVelocity(this.m_nScreenWidth, 0, j);
					((Ufo)localEnemy1).setFireInterval(UFO_FIRE_INTERVAL);
					break;
				default:
					j = ENEMY_FALLTIMES[this.m_nLevel] + AstroSmashView.getRandomInt() % ENEMY_FALLTIME_VARIANCES[this.m_nLevel];
					localEnemy1.setVelocity(0, this.m_nScreenHeight, j);
				}
				this.m_vecFlyingEnemies.addElement(localEnemy1);
			}
		} else if (this.m_vecFlyingEnemies.size() == 0) {
			this.m_bGameOver = true;
			this.m_gameWorldListener.gameIsOver();
		}
	}

	public void fireUfoBullet(int paramInt1, int paramInt2) {
		Enemy localEnemy = this.m_enemyFactory.getEnemy(12);
		localEnemy.setPosition(paramInt1, paramInt2);
		int i = ENEMY_FALLTIMES[this.m_nLevel] / UFO_BULLET_FALLTIME_FACTOR;
		int j = this.m_nScreenHeight;
		int k = 0;
		int m = this.m_ship.getCenterY() - paramInt2;
		if (m != 0) {
			int n = this.m_ship.getCenterX() - paramInt1;
			long l = 1024L * n / m;
			k = (int)(l * j / 1024L);
		}
		localEnemy.setVelocity(k, j, i);
		AstroSmashLauncher.playSound(AstroSmashLauncher.SOUND_UFO);
		this.m_vecFlyingEnemies.addElement(localEnemy);
	}

	protected void sendDeadEnemyToHell(Enemy paramEnemy) {
		this.m_vecFlyingEnemies.removeElement(paramEnemy);
		this.m_enemyFactory.putEnemy(paramEnemy);
	}

	protected void paintEnemies(Canvas canvas, Paint paint) {
		try {
			for (int i = 0; i < this.m_vecFlyingEnemies.size(); i++) {
				Enemy localEnemy = this.m_vecFlyingEnemies.elementAt(i);
				localEnemy.paint(canvas, paint);
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
			localArrayIndexOutOfBoundsException.printStackTrace();
		}
	}

	protected void sendBulletToHell(Collidable paramCollidable) {
		this.m_vecFlyingBullets.removeElement(paramCollidable);
		this.m_munitionsFactory.putBullet(paramCollidable);
	}

	protected void shipDestroyed() {
		AstroSmashLauncher.doVibrate(AstroSmashLauncher.VIBRATE_LONG);
		AstroSmashLauncher.playSound(AstroSmashLauncher.SOUND_SHIP);
		try {
			for (int i = 0; i < this.m_vecFlyingEnemies.size(); i++) {
				Enemy enemy = this.m_vecFlyingEnemies.elementAt(i);
				this.m_EnemiesToRecycleStack.push(enemy);
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException1) {
			localArrayIndexOutOfBoundsException1.printStackTrace();
		}
		sendWaitingEnemiesToFactory();
		try {
			for (int j = 0; j < this.m_vecFlyingBullets.size(); j++) {
				Collidable collidable = this.m_vecFlyingBullets.elementAt(j);
				this.m_BulletsToRecycleStack.push(collidable);
			}
		} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException2) {
			localArrayIndexOutOfBoundsException2.printStackTrace();
		}
		this.m_vecFlyingBullets.removeAllElements();
		while (!this.m_BulletsToRecycleStack.empty()) {
			Collidable localCollidable = this.m_BulletsToRecycleStack.pop();
			this.m_munitionsFactory.putBullet(localCollidable);
		}
		this.m_nLives -= 1;
		this.m_backgroundManager.setLives(this.m_nLives);
		shudderGamer();
	}

	public void shudderGamer() {
		System.out.println("shudder...");
	}

	@Override
	public void doneExploding(Enemy paramEnemy) {
		int i = paramEnemy.getHitReaction();
		switch (i) {
		case 1:
			if (paramEnemy.getEnemyTypeId() != -2) {
				sendDeadEnemyToHell(paramEnemy);
			} else if (this.m_nLives >= 0) {
				paramEnemy.reset();
				paramEnemy.setX(this.m_nScreenWidth / 2);
			} else if (!this.m_bGameOver) {
				this.m_bGameOver = true;
				this.m_gameWorldListener.gameIsOver();
			}
			break;
		case 2:
			handleOnHitSeparate(paramEnemy);
			break;
		case 0:
		default:
			sendDeadEnemyToHell(paramEnemy);
		}
	}

	protected void sendWaitingEnemiesToFactory() {
		while (!this.m_EnemiesToRecycleStack.empty()) {
			Enemy localEnemy = this.m_EnemiesToRecycleStack.pop();
			sendDeadEnemyToHell(localEnemy);
		}
	}

	protected void handleOnHitSeparate(Enemy paramEnemy) {
		int n = paramEnemy.getEnemyTypeId();
		int m;
		switch (n) {
		case 1:
			m = 5;
			break;
		case 0:
			m = 4;
			break;
		case 2:
			m = 6;
			break;
		case 3:
			m = 7;
			break;
		default:
			m = 5;
		}
		int i = paramEnemy.getX();
		int j = paramEnemy.getY();
		Enemy localEnemy1 = this.m_enemyFactory.getEnemy(m);
		int k = localEnemy1.getWidth();
		localEnemy1.setPosition(i + k, j);
		localEnemy1.setVelocity(this.m_nScreenHeight, this.m_nScreenHeight, ENEMY_FALLTIMES[this.m_nLevel]);
		Enemy localEnemy2 = this.m_enemyFactory.getEnemy(m);
		localEnemy2.setPosition(i, j);
		localEnemy2.setVelocity(-this.m_nScreenHeight, this.m_nScreenHeight, ENEMY_FALLTIMES[this.m_nLevel]);
		this.m_vecFlyingEnemies.addElement(localEnemy1);
		this.m_vecFlyingEnemies.addElement(localEnemy2);
		sendDeadEnemyToHell(paramEnemy);
	}

	protected void setLevel(int paramInt) {
		this.m_nLevel = paramInt;
		this.m_backgroundManager.setGameLevel(paramInt);
	}

	protected void checkLevel() {
		int i = 0;
		if ((this.m_nLevel < MAX_LEVEL) && (this.m_nScore >= MIN_SCORES_OF_LEVEL[(this.m_nLevel + 1)])) {
			do {
				this.m_nLevel += 1;
				i = 1;
				if (this.m_nLevel >= MAX_LEVEL) {
					break;
				}
			} while (this.m_nScore >= MIN_SCORES_OF_LEVEL[(this.m_nLevel + 1)]);
		} else if (this.m_nScore < MIN_SCORES_OF_LEVEL[this.m_nLevel]) {
			while ((this.m_nLevel > this.m_nInitialLevel) && (this.m_nScore < MIN_SCORES_OF_LEVEL[this.m_nLevel])) {
				this.m_nLevel -= 1;
				i = 1;
			}
		}
		if (i != 0) {
			setLevel(this.m_nLevel);
		}
	}

	protected void updateScore(int paramInt) {
		this.m_nScore += paramInt * this.m_nLevel;
		this.m_backgroundManager.setScore(this.m_nScore);
		if (this.m_nScore > this.m_nPeakScore) {
			int i = this.m_nPeakScore;
			this.m_nPeakScore = this.m_nScore;
			int j = i / 1000;
			int k = this.m_nPeakScore / 1000;
			if (k > j) {
				if (this.m_nLives < 9) { // As Original Astrosmash Game on Motorola V150 and C350: 9 lives is max
					this.m_nLives += 1;
					this.m_backgroundManager.setLives(this.m_nLives);
				}
			}
			this.m_backgroundManager.setPeakScore(this.m_nPeakScore);
		}
	}

	public void suspendEnemies() {
		this.m_bSuspendEnemies = true;
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/GameWorld.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
