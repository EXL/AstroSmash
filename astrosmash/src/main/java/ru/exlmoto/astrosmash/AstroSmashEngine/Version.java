package ru.exlmoto.astrosmash.AstroSmashEngine;

import android.graphics.Color;

import ru.exlmoto.astrosmash.AstroSmashActivity;

@SuppressWarnings("unused")
public class Version {

	public static final int PLATFORM_UNKNOWN = -1;
	public static final int PLATFORM_MOTOROLA_66I = 0;
	public static final int PLATFORM_MOTOROLA_280I = 1;
	public static final int PLATFORM_MOTOROLA_RAINBOW = 2;
	public static final int PLATFORM_MOTOROLA_NFUII = 3;
	public static final int PLATFORM_SANYO = 4;
	public static final int PLATFORM_MOTOROLA_LG5350 = 5;
	public static final int PLATFORM_MOTOROLA_720I = 6;
	public static final int PLATFORM_MOTOROLA_176x220 = 7;
	public static final int PLATFORM_MOTOROLA_240x320= 8;
	public static final int PLATFORM_MOTOROLA_480x640= 9;
	public static final int PLATFORM_MOTOROLA_96x51= 10;
	public static final int ANDROID_ORIGINAL_120x146 = 120+146*1024;
	public static final int ANDROID_ORIGINAL_176x220 = 176+220*1024;
	public static final int ANDROID_ORIGINAL_240x320 = 240+320*1024;
	public static final int ANDROID_ORIGINAL_480x640 = 480+640*1024;
	public static final int ANDROID_ORIGINAL_96x51 = 96+51*1024;
	private static int SCREEN_HEIGHT = 146;
	private static int SCREEN_WIDGT = 120;
	private static int platform = -1;
	private static final int HEIGHT_MULTIPLIER = 1024;
	private static String DEVICE = "Default Device";
	private static String RELEASE = "1.8.9";
	private static String VARIANT = "";
	private static boolean DEMO_FLAG = false;
	private static int DEMO_DURATION = 60;
	private static boolean DEBUG_FLAG = false;
	private static boolean DEBUG_FPS_FLAG = false;
	private static boolean DEBUG_MEMORY_FLAG = false;
	private static int DEBUG_MEMORY_INTERVAL = 5000;
	private static int COMMAND_HEIGHT_PIXELS = 0;
	private static int SHIP_MOVE_X = 7;
	private static int STATISTICS_HEIGHT = 10;
	private static int STRING_RIGHT_PADDING = 3;
	private static int GROUND_THICKNESS = 1;
	private static int MOUNTAIN_FOOT_HEIGHT = 9;
	private static int MENU_FIRST_X = 20;
	private static int MENU_FIRST_Y = 11;
	private static int MENU_DELTA_Y = 9;
	public static int BLACKCOLOR = Color.BLACK;
	public static int WHITECOLOR = Color.WHITE;
	public static int GREENCOLOR = Color.parseColor("#28B152");
	public static int GREENCOLOR_DARK = Color.parseColor("#003130");
	public static int DARKCOLOR = Color.parseColor("#091219");
	public static int GRAYCOLOR = Color.parseColor("#7B9596");
	public static String GREENSHIP_FILENAME = "/ship_green.png";
	public static int GAMEPAUSEDCOLOR = Color.WHITE;
	public static int SHUDDER_TIME = 500;
	public static String CREDITS_UPC_CODE = "";

	public static int getHeight() {
		return SCREEN_HEIGHT;
	}

	public static int getWidth() {
		return SCREEN_WIDGT;
	}

	public static int getPlatform() {
		return platform;
	}

	public static String getDevice() {
		return DEVICE;
	}

	public static String getRelease() {
		return RELEASE;
	}

	public static String getVariant() {
		return VARIANT;
	}

	public static boolean getDemoFlag() {
		return DEMO_FLAG;
	}

	public static int getDemoDuration() {
		return DEMO_DURATION;
	}

	public static boolean getDebugFlag() {
		return DEBUG_FLAG;
	}

	public static boolean getDebugFpsFlag() {
		return DEBUG_FPS_FLAG;
	}

	public static boolean getDebugMemoryFlag() {
		return DEBUG_MEMORY_FLAG;
	}

	public static int getDebugMemoryInterval() {
		return DEBUG_MEMORY_INTERVAL;
	}

	public static int getCommandHeightPixels() {
		return COMMAND_HEIGHT_PIXELS;
	}

	public static int getShipMoveX() {
		return SHIP_MOVE_X;
	}

	public static int getStatisticsHeight() {
		return STATISTICS_HEIGHT;
	}

	public static int getStringRightPadding() {
		return STRING_RIGHT_PADDING;
	}

	public static int getGroundThickness() {
		return GROUND_THICKNESS;
	}

	public static int getMountainFootHeight() {
		return MOUNTAIN_FOOT_HEIGHT;
	}

	public static int getMenuFirstX() {
		return MENU_FIRST_X;
	}

	public static int getMenuFirstY() {
		return MENU_FIRST_Y;
	}

	public static int getMenuDeltaY() {
		return MENU_DELTA_Y;
	}

	public static void setScreenSizes(int android_original) {
		switch (android_original) {
		default:
		case ANDROID_ORIGINAL_120x146:
			SCREEN_WIDGT = 120;
			SCREEN_HEIGHT = 146;
			break;
		case ANDROID_ORIGINAL_176x220:
			SCREEN_WIDGT = 176;
			SCREEN_HEIGHT = 220;
			break;
		case ANDROID_ORIGINAL_240x320:
			SCREEN_WIDGT = 240;
			SCREEN_HEIGHT = 320;
			break;
		case ANDROID_ORIGINAL_480x640:
			SCREEN_WIDGT = 480;
			SCREEN_HEIGHT = 640;
			break;
		case ANDROID_ORIGINAL_96x51:
			SCREEN_WIDGT = 96;
			SCREEN_HEIGHT = 51;
			break;
		}
	}

	static {
		int i = getWidth();
		int j = getHeight();
		platform = -1;
		switch (i + j * 1024) {
		case 48224:
		case ANDROID_ORIGINAL_96x51:
			platform = 0;
			DEVICE = "Motorola 66i";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 10;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 1;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 20;
			MENU_FIRST_Y = 11;
			MENU_DELTA_Y = 9;
			GREENCOLOR = Color.WHITE;
			CREDITS_UPC_CODE = "7-19575-700030";
			break;
		case 85120:
		case 91264:
			platform = 1;
			DEVICE = "Motorola 280i";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 10;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			GREENSHIP_FILENAME = "/ship_small.png";
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 16;
			MENU_DELTA_Y = 14;
			CREDITS_UPC_CODE = "7-19575-700016";
			break;
		case 133248:
			platform = 2;
			DEVICE = "Motorola Rainbow";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 10;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 34;
			MENU_DELTA_Y = 12;
			CREDITS_UPC_CODE = "7-19575-780032";
			break;
		case 103552:
			platform = 3;
			DEVICE = "Motorola NFUII";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 10;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 23;
			MENU_DELTA_Y = 12;
			CREDITS_UPC_CODE = "7-19575-780025";
			break;
		case 96376:
			platform = 4;
			DEVICE = "Sanyo";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 10;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 20;
			MENU_DELTA_Y = 12;
			CREDITS_UPC_CODE = "7-19575-790017";
			break;
		case 101496:
			platform = 5;
			DEVICE = "LG 5350";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 13;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 21;
			MENU_DELTA_Y = 12;
			CREDITS_UPC_CODE = "7-19575-800013";
			break;
		case 147576:
		case ANDROID_ORIGINAL_120x146:
			platform = 6;
			DEVICE = "Motorola 720i";
			COMMAND_HEIGHT_PIXELS = 0;
			SHIP_MOVE_X = 7;
			STATISTICS_HEIGHT = 12;
			STRING_RIGHT_PADDING = 3;
			GROUND_THICKNESS = 3;
			MOUNTAIN_FOOT_HEIGHT = 9;
			MENU_FIRST_X = 10;
			MENU_FIRST_Y = 21;
			MENU_DELTA_Y = 30;
			GAMEPAUSEDCOLOR = Color.BLACK;
			CREDITS_UPC_CODE = "7-19575-780063 4";
			break;
		default:
			platform = -1;
		}
		AstroSmashActivity.toDebug("Platform (ASVersion): " + platform);
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/AstrosmashVersion.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
