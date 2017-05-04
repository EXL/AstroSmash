package ru.exlmoto.astrosmash.AstroSmashEngine;

import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ru.exlmoto.astrosmash.R;
import ru.exlmoto.astrosmash.AstroSmashView;

@SuppressWarnings("unused")
public class EnemyFactory {

	public static final String SMALLSHIPIMAGE = "/ship_small.png";
	public static final int INITIAL_NUM_ENEMIES = 3;
	public static final int ON_HIT_DO_NOTHING = 0;
	public static final int ON_HIT_EXPLODE = 1;
	public static final int ON_HIT_SEPARATE = 2;
	private static final String[] EXPLOSION_IMAGES = { "/explode1.png", "/explode2.png" };
	private static final int[] EXPLOSION_IMAGES_AID = { R.drawable.explode1, R.drawable.explode2 };
	public static final int SHIP_ID = -2;
	public static final int BAD_ENEMY_ID = -1;
	public static final int BIG_ORANGE_ROCK_ID = 0;
	public static final int BIG_YELLOW_ROCK_ID = 1;
	public static final int BIG_BLUE_ROCK_ID = 2;
	public static final int BIG_GREEN_ROCK_ID = 3;
	public static final int SMALL_ORANGE_ROCK_ID = 4;
	public static final int SMALL_YELLOW_ROCK_ID = 5;
	public static final int SMALL_BLUE_ROCK_ID = 6;
	public static final int SMALL_GREEN_ROCK_ID = 7;
	public static final int BIG_SPINNER_ID = 8;
	public static final int SMALL_SPINNER_ID = 9;
	public static final int PULSER_ID = 10;
	public static final int UFO_ID = 11;
	public static final int UFO_BULLET_ID = 12;
	private static final EnemyData[] ENEMIES = {
			new EnemyData("/rock1.png", 10, -5, 2, null, 0, R.drawable.rock1, 0),
			new EnemyData("/rock2.png", 10, -5, 2, null, 0, R.drawable.rock2, 0),
			new EnemyData("/rock3.png", 10, -5, 2, null, 0, R.drawable.rock3, 0),
			new EnemyData("/rock4.png", 10, -5, 2, null, 0, R.drawable.rock4, 0),
			new EnemyData("/rock1_s.png", 20, -10, 1, null, 0, R.drawable.rock1_s, 0),
			new EnemyData("/rock2_s.png", 20, -10, 1, null, 0, R.drawable.rock2_s, 0),
			new EnemyData("/rock3_s.png", 20, -10, 1, null, 0, R.drawable.rock3_s, 0),
			new EnemyData("/rock4_s.png", 20, -10, 1, null, 0, R.drawable.rock4_s, 0),
			new EnemyData("/satellite_updown.png", 40, -100, 1, "/satellite_flat.png", 0, R.drawable.satellite_updown, R.drawable.satellite_flat),
			new EnemyData("/smallsat_updown.png", 80, -100, 1, "/smallsat_flat.png", 0, R.drawable.smallsat_updown, R.drawable.smallsat_flat),
			new EnemyData("/pulser.png", 80, -50, 1, "/pulser_small.png", 0, R.drawable.pulser, R.drawable.pulser_small),
			new EnemyData("/ufo.png", 100, 0, 1, null, 3, R.drawable.ufo, 0),
			new EnemyData("/ufo_bullet.png", 10, 0, 1, null, 0, R.drawable.ufo_bullet, 0)
	};
	private static final int[] ENEMY_PROBABILITIES = { 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
			1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
			1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
			1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 200, 200, 300, 400, 500, 600,
			200, 200, 300, 400, 500, 600, 50, 50, 80, 100, 200, 400, 0, 0, 80, 100, 200, 400, 0, 0, 0, 0, 0, 0 };
	private static int[] TOTAL_ENEMY_PROBABILITIES = new int[6];
	private Stack<Enemy>[] m_enemyStacks;
	private Bitmap[] m_images;
	private Bitmap[] m_swapImages;
	private Bitmap[] m_xplosionImages;
	private int m_screenWidth;
	private int m_screenHeight;
	private IDeathListener m_deathListener;

	private Context activityContext = null;

	public EnemyFactory(int screenWidth, int screenHeight, IDeathListener iDeathListener, Context context) {
		this.activityContext = context;
		this.m_screenWidth = screenWidth;
		this.m_screenHeight = screenHeight;
		this.m_deathListener = iDeathListener;
		loadImages();
		generateEnemies();
	}

	public Enemy getRandomEnemy(int paramInt1, int paramInt2) {
		int i = 1;
		int j = AstroSmashView.getAbsRandomInt() % TOTAL_ENEMY_PROBABILITIES[(paramInt1 - 1)];
		int k = 0;
		int m = paramInt1 - 1;
		for (int n = 0; n < ENEMIES.length; n++) {
			k += ENEMY_PROBABILITIES[m];
			if (j < k) {
				if ((ENEMIES[n].nMinLevel != 0) && (paramInt1 < ENEMIES[n].nMinLevel)) {
					break;
				}
				i = n;
				break;
			}
			m += 6;
		}
		return getEnemy(i);
	}

	public Enemy getEnemy(int paramInt) {
		Enemy localEnemy = null;
		if (!this.m_enemyStacks[paramInt].isEmpty()) {
			localEnemy = (Enemy)this.m_enemyStacks[paramInt].pop();
		} else {
			localEnemy = createEnemy(paramInt);
		}
		localEnemy.reset();
		localEnemy.setPosition(0, 0);
		return localEnemy;
	}

	public void putEnemy(Enemy paramEnemy) {
		int i = paramEnemy.getEnemyTypeId();
		this.m_enemyStacks[i].push(paramEnemy);
	}

	protected void loadImages() {
		//		try {
		this.m_images = new Bitmap[ENEMIES.length];
		this.m_swapImages = new Bitmap[ENEMIES.length];
		this.m_xplosionImages = new Bitmap[EXPLOSION_IMAGES.length];
		for (int i = 0; i < ENEMIES.length; i++) {
			this.m_images[i] = BitmapFactory.decodeResource(activityContext.getResources(), ENEMIES[i].androidResID);
			if (null != ENEMIES[i].sSwapImageName) {
				this.m_swapImages[i] = BitmapFactory.decodeResource(activityContext.getResources(), ENEMIES[i].androidResSwapID);
			} else {
				this.m_swapImages[i] = null;
			}
		}
		for (int j = 0; j < EXPLOSION_IMAGES.length; j++) {
			this.m_xplosionImages[j] = BitmapFactory.decodeResource(activityContext.getResources(), EXPLOSION_IMAGES_AID[j]);
		}
		//		} catch (Exception localException) {
		//			System.out.println(localException.getMessage());
		//			localException.printStackTrace();
		//		}
	}

	@SuppressWarnings("unchecked")
	protected void generateEnemies() {
		this.m_enemyStacks = new Stack[ENEMIES.length];
		for (int i = 0; i < ENEMIES.length; i++) {
			this.m_enemyStacks[i] = new Stack<Enemy>();
			for (int j = 0; j < 3; j++) {
				Enemy localEnemy = createEnemy(i);
				this.m_enemyStacks[i].push(localEnemy);
			}
		}
	}

	protected Enemy createEnemy(int paramInt) {
		Object localObject;
		switch (paramInt) {
		case 8:
		case 9:
			localObject = new SwappableEnemy();
			((SwappableEnemy)localObject).setSwapImage(this.m_swapImages[paramInt]);
			break;
		case 10:
			localObject = new Pulser();
			((Pulser)localObject).setSwapImage(this.m_swapImages[paramInt]);
			break;
		case 11:
			localObject = new Ufo();
			break;
		default:
			localObject = new Enemy();
		}
		((Enemy)localObject).setEnemyTypeId(paramInt);
		((Enemy)localObject).setHitScore(ENEMIES[paramInt].nHitScore);
		((Enemy)localObject).setGroundScore(ENEMIES[paramInt].nGroundScore);
		((Drawable)localObject).setImage(this.m_images[paramInt]);
		((Enemy)localObject).setHitReaction(ENEMIES[paramInt].nHitReaction);
		((Enemy)localObject).setDeathListener(this.m_deathListener);
		if (1 == ENEMIES[paramInt].nHitReaction) {
			((Enemy)localObject).setExplosionImages(this.m_xplosionImages);
		}
		return (Enemy)localObject;
	}

	public Enemy createShip(int paramInt1, int paramInt2) {
		GunShip localGunShip = null;
		//		try {
		Bitmap[] arrayOfImage1 = {
				BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.ship_explode1),
				BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.ship_explode2),
				BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.ship_explode3)
		};
		Bitmap[] arrayOfImage2 = {
				arrayOfImage1[0],
				arrayOfImage1[0],
				arrayOfImage1[0],
				arrayOfImage1[1],
				arrayOfImage1[1],
				arrayOfImage1[1],
				arrayOfImage1[2],
				arrayOfImage1[2],
				arrayOfImage1[2]
		};
		localGunShip = new GunShip();
		Bitmap localImage = BitmapFactory.decodeResource(activityContext.getResources(), R.drawable.ship_small);
		localGunShip.setImage(localImage);
		localGunShip.setPosition(paramInt1, paramInt2 - localGunShip.getHeight());
		localGunShip.setEnemyTypeId(-2);
		localGunShip.setHitReaction(1);
		localGunShip.setDeathListener(this.m_deathListener);
		localGunShip.setExplosionImages(arrayOfImage2);
		//		} catch (Exception localException) {
		//			System.out.println(localException.getMessage());
		//			localException.printStackTrace();
		//		}
		return localGunShip;
	}

	static {
		for (int i = 0; i < 6; i++) {
			TOTAL_ENEMY_PROBABILITIES[i] = 0;
			for (int j = 0; j < ENEMIES.length; j++) {
				TOTAL_ENEMY_PROBABILITIES[i] += ENEMY_PROBABILITIES[(j * 6 + i)];
			}
		}
	}

	static class EnemyData {

		int nId;
		String sImageFileName;
		int nHitScore;
		int nGroundScore;
		int nHitReaction;
		String sSwapImageName;
		int nMinLevel;
		int androidResID;
		int androidResSwapID;

		EnemyData(String paramString1, int paramInt1,
				int paramInt2, int paramInt3, String paramString2,
				int paramInt4, int androidResID, int androidResSwapID) {
			this.sImageFileName = paramString1;
			this.nHitScore = paramInt1;
			this.nGroundScore = paramInt2;
			this.nHitReaction = paramInt3;
			this.sSwapImageName = paramString2;
			this.nMinLevel = paramInt4;
			this.androidResID = androidResID;
			this.androidResSwapID = androidResSwapID;
		}
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/EnemyFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
