package ru.exlmoto.astrosmash.AstroSmashEngine;

import java.util.Locale;

public class InfoStrings {

	public static final int LANGUAGE_ENGLISH = 0;
	public static final int LANGUAGE_RUSSIAN = 1;
	public static int LANGUAGE_CURRENT = 0;
	public static String GAME_PAUSED_STRING = "";
	public static String GAME_OVER_STRING = "";
	public static String PEAK_SCORE_STRING = "";
	public static final String ASTROSMASH_TITLE = "AstroSmash";

	public static void initializeInfo() {
		String str = Locale.getDefault().toString();
		if (str.startsWith("ru")) {
			LANGUAGE_CURRENT = 1;
			initializeRussian();
		} else {
			LANGUAGE_CURRENT = 0;
			initializeEnglish();
		}
	}

	public static String getHelp() {
		switch (LANGUAGE_CURRENT) {
		case 0:
			return getHelpEnglish();
		case 1:
			return getHelpRussian();
		}
		return getHelpEnglish();
	}

	public static String getCredits1() {
		switch (LANGUAGE_CURRENT) {
		case 0:
			return getCredits1English();
		case 1:
			return getCredits1Russian();
		}
		return getCredits1English();
	}

	public static String getCredits2() {
		switch (LANGUAGE_CURRENT) {
		case 0:
			return getCredits2English();
		case 1:
			return getCredits2Russian();
		}
		return getCredits2English();
	}

	public static String getCredits3() {
		switch (LANGUAGE_CURRENT) {
		case 0:
			return getCredits3English();
		case 1:
			return getCredits3Russian();
		}
		return getCredits3English();
	}

	public static void initializeRussian() {
		GAME_PAUSED_STRING = "Пауза";
		GAME_OVER_STRING = "Конец игры";
		PEAK_SCORE_STRING = "Максимум";
	}

	public static String getHelpRussian() {
		StringBuffer localStringBuffer = new StringBuffer("Цель игры\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Уничтожьте всех противников в звёздном небе! Не позволяйте вращающимся спутникам достичь поверхности, иначе вы будете незамедлительно уничтoжены! Старайтесь убивать все движущиеся объекты, они добавляют очки, если объект коснётся земли, игровой счёт понизится!\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Управление\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Используйте верхнюю часть сенсорного экрана для стрельбы и паузы, а нижнюю часть экрана для управления кораблём. Помимо этого, для управления вы можете использовать клавишу навигации или дополнительные кнопки. Включите автоматический огонь, если это необходимо и используйте прыжок в гиперпространство если враги подобрались близко!\n\n");
		localStringBuffer.append("Вверх - Вкл/выкл автоматический огонь\n");
		localStringBuffer.append("Влево - Двигаться влево\n");
		localStringBuffer.append("Вправо - Двигаться вправо\n");
		localStringBuffer.append("Вниз - Прыжок в гиперпространство\n");
		localStringBuffer.append("Центр - Огонь\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Дополнительные клавиши\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("A, 4 - Двигаться влево\n");
		localStringBuffer.append("D, 6 - Двигаться влево\n");
		localStringBuffer.append("Enter, 5 - Огонь\n");
		localStringBuffer.append("Space - Огонь\n");
		localStringBuffer.append("W, 8 - Вкл/выкл автоматический огонь\n");
		localStringBuffer.append("S, 2 - Прыжок в гиперпространство\n");
		return localStringBuffer.toString();
	}

	public static String getCredits1Russian() {
		StringBuffer localStringBuffer = new StringBuffer("Авторы THQ\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Продюсер - Stuart Platt\nДополнительный продюсер - Colin Totman\nМаркетинг - Peter Dille\nДиректор Creative Services - Howard Liebeskind\nГлавный менеджер - Chris Sturr\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Команда тестировщиков THQ QA\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Главный тестировщик - Scott Frazier\nТестировщик - Adam Affrunti\nТестировщик - Brad Arnold\nТестировщик - Shelley Franklin\nТестировщик - Donald Sturkey\n");
		return localStringBuffer.toString();
	}

	public static String getCredits2Russian() {
		StringBuffer localStringBuffer = new StringBuffer("Авторы Lavastorm\n------------------------------------\nПродюсер - Jason Loia\nРазработчик - Albert So\nРазработчик - Horace Lin\nРазработчик - Veeramurthy Veeraprakash\nХудожник - Mark Keavney\n\nОтдельная благодарность\n------------------------------------\nJohn P. Sohl\n");
		return localStringBuffer.toString();
	}

	public static String getCredits3Russian() {
		StringBuffer localStringBuffer = new StringBuffer("Astrosmash и Intellivision торговые марки компании Intellivision Productions, Inc. Игра Astrosmash © 1981 Intellivision Productions, Inc. Использована исключительная лицензия. Разработано в Lavastorm, Inc. Реализация игры и программного обеспечения © 2002 THQ Inc. THQ и логотип зарегистрированы ® THQ Inc.\nВсе права защищены.\n");
		localStringBuffer.append(Version.CREDITS_UPC_CODE + "\n");
		return localStringBuffer.toString();
	}

	public static void initializeEnglish() {
		GAME_PAUSED_STRING = "Game Paused";
		GAME_OVER_STRING = "Game Over";
		PEAK_SCORE_STRING = "Peak Score";
	}

	public static String getHelpEnglish() {
		StringBuffer localStringBuffer = new StringBuffer("Your Mission\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Destroy all enemies from the sky! Don't let any spinning satellites hit the ground or you will be killed instantly! The more you hit, the more points you get! Any objects that hit the ground decrease your score!\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Controls\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Use the top part of the sensor screen to fire or pause, and bottom part for control the ship. You can use the navigation key or the additional keys to control your ship. Toggle auto-fire as needed, and use hyper-space to get out of close calls!\n\n");
		localStringBuffer.append("Up - Toggle Auto-fire\n");
		localStringBuffer.append("Left - Left\n");
		localStringBuffer.append("Right - Right\n");
		localStringBuffer.append("Down - Hyperspace\n");
		localStringBuffer.append("Center - Fire\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Additional keys Controls\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("A, 4 - Move Left\n");
		localStringBuffer.append("D, 6 - Move Right\n");
		localStringBuffer.append("Enter, 5 - Fire\n");
		localStringBuffer.append("Space - Fire\n");
		localStringBuffer.append("W, 8 - Toggle Auto-fire\n");
		localStringBuffer.append("S, 2 - Hyperspace\n");
		return localStringBuffer.toString();
	}

	public static String getCredits1English() {
		StringBuffer localStringBuffer = new StringBuffer("THQ Credits\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Producer - Stuart Platt\nAssociate Producer - Colin Totman\nVP Marketing - Peter Dille\nDirector of Creative Services - Howard Liebeskind\nProduction Manager - Chris Sturr\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("THQ QA testing team credits\n");
		localStringBuffer.append("------------------------------------\n");
		localStringBuffer.append("Senior tester - Scott Frazier\nTester - Adam Affrunti\nTester - Brad Arnold\nTester - Shelley Franklin\nTester - Donald Sturkey\n");
		return localStringBuffer.toString();
	}

	public static String getCredits2English() {
		StringBuffer localStringBuffer = new StringBuffer("Lavastorm Credits\n------------------------------------\nProducer - Jason Loia\nLead Coder - Albert So\nLead Coder - Horace Lin\nLead Coder - Veeramurthy Veeraprakash\nArtwork - Mark Keavney\n\nSpecial Thanks to\n------------------------------------\nJohn P. Sohl\n");
		return localStringBuffer.toString();
	}

	public static String getCredits3English() {
		StringBuffer localStringBuffer = new StringBuffer("Astrosmash & Intellivision are trademarks of Intellivision Productions, Inc. Astrosmash game © 1981 Intellivision Productions, Inc. Used under exclusive license. Developed by Lavastorm, Inc. Game Implementation & Software © 2002 THQ Inc. THQ and logo are ® of THQ Inc.\nAll Rights Reserved.\n");
		localStringBuffer.append(Version.CREDITS_UPC_CODE + "\n");
		return localStringBuffer.toString();
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/InfoStrings.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */
