package ru.exlmoto.astrosmash;

public class InfoStrings {

	public static final int LANGUAGE_ENGLISH = 0;
	public static final int LANGUAGE_FRENCH = 1;
	public static final int LANGUAGE_CHINESE = 2;
	public static final int LANGUAGE_SPANISH = 3;
	public static final int LANGUAGE_PORTUGESE = 4;
	public static int LANGUAGE_CURRENT = 0;
	public static String TITLE_CREDITS = "";
	public static String TITLE_HELP = "";
	public static String EXIT_LABEL = "";
	public static String QUIT_LABEL = "";
	public static String QUIT_CONFIRM_LABEL = "";
	public static String QUIT_ABORT_LABEL = "";
	public static String PAUSE_LABEL = "";
	public static String RESUME_LABEL = "";
	public static String RESTART_LABEL = "";
	public static String NEXT_LABEL = "";
	public static String BACK_LABEL = "";
	public static String SELECT_LABEL = "";
	public static String DONE_LABEL = "";
	public static String BLANK_LABEL = "";
	public static String SKIP_LABEL = "";
	public static String DEL_LABEL = "";
	public static String STATUS_QUESTION = "";
	public static String GAME_PAUSED_STRING = "";
	public static String GAME_OVER_STRING = "";
	public static String PEAK_SCORE_STRING = "";
	public static String HISCORE_TITLE = "";
	public static String HISCORE_ENTRY_TITLE = "";
	public static String HISCORE_ENTRY_TEXT1_SUFFIX = "";
	public static String HISCORE_ENTRY_NAME_FIELD = "";
	public static String HISCORE_ENTRY_TEXT2 = "";
	public static String HISCORE_ENTRY_TEXT2_ERROR = "";
	public static String HISCORE_PLAYER_STRING = "";
	public static String LOADING_TEXT = "";
	public static String SPLASH_IMAGE_ERROR = "Image Cannot be shown";
	public static final String ASTROSMASH_TITLE = "Astrosmash";

	public static void initializeInfo() {
		// TODO: Android Locale
		String str = System.getProperty("microedition.locale");
		if (str.startsWith("fr")) {
			LANGUAGE_CURRENT = 1;
			initializeFrench();
		} else if (str.startsWith("zh")) {
			LANGUAGE_CURRENT = 2;
			initializeChinese();
		} else {
			LANGUAGE_CURRENT = 0;
			initializeEnglish();
		}
	}

	public static String getHelp() {
		switch (LANGUAGE_CURRENT)
		{
		case 0: 
			return getHelpEnglish();
		case 1: 
			return getHelpFrench();
		case 2: 
			return getHelpChinese();
		}
		return getHelpEnglish();
	}

	public static String getCredits1() {
		switch (LANGUAGE_CURRENT)
		{
		case 0: 
			return getCredits1English();
		case 1: 
			return getCredits1French();
		case 2: 
			return getCredits1Chinese();
		}
		return getCredits1English();
	}

	public static String getCredits2() {
		switch (LANGUAGE_CURRENT)
		{
		case 0: 
			return getCredits2English();
		case 1: 
			return getCredits2French();
		case 2: 
			return getCredits2Chinese();
		}
		return getCredits2English();
	}

	public static String getCredits3() {
		switch (LANGUAGE_CURRENT)
		{
		case 0: 
			return getCredits3English();
		case 1: 
			return getCredits3French();
		case 2: 
			return getCredits3Chinese();
		}
		return getCredits3English();
	}

	public static void initializeChinese() {
		TITLE_CREDITS = "制作小组";
		TITLE_HELP = "帮助";
		EXIT_LABEL = "退出";
		QUIT_LABEL = "退出";
		QUIT_CONFIRM_LABEL = "是";
		QUIT_ABORT_LABEL = "否";
		PAUSE_LABEL = "暂停";
		RESUME_LABEL = "继续游戏";
		RESTART_LABEL = "继续游戏";
		NEXT_LABEL = "下一步";
		BACK_LABEL = "后退";
		SELECT_LABEL = "选定";
		DONE_LABEL = "制作小组 ";
		BLANK_LABEL = "";
		SKIP_LABEL = "跳过";
		DEL_LABEL = "DEL";
		STATUS_QUESTION = "Are you sure you\nwant to quit?";
		GAME_PAUSED_STRING = "Game Paused";
		GAME_OVER_STRING = "游戏结束";
		PEAK_SCORE_STRING = "最高分";
		HISCORE_TITLE = "High Scores";
		HISCORE_ENTRY_TITLE = "进入积分榜";
		HISCORE_ENTRY_TEXT1_SUFFIX = "是一个很高的";
		HISCORE_ENTRY_NAME_FIELD = "请输入姓名 :";
		HISCORE_ENTRY_TEXT2 = "Press DONE and wait...";
		HISCORE_ENTRY_TEXT2_ERROR = "Please enter valid characters\n";
		HISCORE_PLAYER_STRING = "Player";
		LOADING_TEXT = "Loading game";
	}

	public static String getHelpChinese() {
		StringBuffer localStringBuffer = new StringBuffer("Your Mission\n");
		localStringBuffer.append("------------\n");
		localStringBuffer.append("Destroy all enemies from the sky! Don't let any spinning satellites hit the ground or you will be killed instantly! The more you hit, the more points you get! Any objects that hit the ground decrease your score!\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Controls\n");
		localStringBuffer.append("--------\n");
		localStringBuffer.append("Use the navigation key or the Alphanumeric keys to control your ship. Toggle auto-fire as needed, and use hyper-space to get out of close calls!\n");
		localStringBuffer.append("up- Toggle Auto-fire\n");
		localStringBuffer.append("left- Left\n");
		localStringBuffer.append("right- Right\n");
		localStringBuffer.append("down- Hyperspace\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Alphanumeric keys Controls\n");
		localStringBuffer.append("--------------------------\n");
		localStringBuffer.append("4- Move Left\n");
		localStringBuffer.append("6- Move Right\n");
		localStringBuffer.append("1- Fire\n");
		localStringBuffer.append("3- Fire\n");
		localStringBuffer.append("2- Toggle Auto-fire\n");
		localStringBuffer.append("8- Hyperspace\n");
		return localStringBuffer.toString();
	}

	public static String getCredits1Chinese() {
		StringBuffer localStringBuffer = new StringBuffer("THQ Credits\n");
		localStringBuffer.append("-----------\n");
		localStringBuffer.append("Producer- Stuart Platt, Associate Producer- Colin Totman, VP Marketing- Peter Dille, Director of Creative Services- Howard Liebeskind, Production Manager- Chris Sturr\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("THQ QA testing team credits\n");
		localStringBuffer.append("---------------------------\n");
		localStringBuffer.append("SENIOR TESTER - Scott Frazier TESTERS - Adam Affrunti, Brad Arnold, Shelley Franklin, Donald Sturkey\n");
		localStringBuffer.append("\n");
		return localStringBuffer.toString();
	}

	public static String getCredits2Chinese() {
		StringBuffer localStringBuffer = new StringBuffer("Lavastorm Credits\n-----------------\nProducer: Jason Loia Lead Coders: Albert So, Horace Lin, Veeramurthy Veeraprakash Artwork: Mark Keavney\n\nSpecial Thanks to\n-----------------\nJohn P. Sohl\n");
		return localStringBuffer.toString();
	}

	public static String getCredits3Chinese() {
		StringBuffer localStringBuffer = new StringBuffer("Astrosmash & Intellivision are trademarks of Intellivision Productions, Inc. Astrosmash game (C) 1981 Intellivision Productions, Inc. Used under exclusive license. Developed by Lavastorm, Inc. Game Implementation & Software (C) 2002 THQ Inc. THQ and logo are (R) of THQ Inc. All Rights Reserved.\n");
		localStringBuffer.append(AstroSmashVersion.CREDITS_UPC_CODE + "\n");
		return localStringBuffer.toString();
	}

	public static void initializeEnglish() {
		TITLE_CREDITS = "Credits";
		TITLE_HELP = "Help";
		EXIT_LABEL = "EXIT";
		QUIT_LABEL = "QUIT";
		QUIT_CONFIRM_LABEL = "YES";
		QUIT_ABORT_LABEL = "NO";
		PAUSE_LABEL = "PAUSE";
		RESUME_LABEL = "PLAY";
		RESTART_LABEL = "PLAY";
		NEXT_LABEL = "NEXT";
		BACK_LABEL = "BACK";
		SELECT_LABEL = "PICK";
		DONE_LABEL = "DONE";
		SKIP_LABEL = "SKIP";
		DEL_LABEL = "DEL";
		STATUS_QUESTION = "Are you sure you\nwant to quit?";
		GAME_PAUSED_STRING = "Game Paused";
		GAME_OVER_STRING = "GAME OVER";
		PEAK_SCORE_STRING = "Peak Score";
		HISCORE_TITLE = "High Scores";
		HISCORE_ENTRY_TITLE = "High Score Entry";
		HISCORE_ENTRY_TEXT1_SUFFIX = " is a high score.";
		HISCORE_ENTRY_NAME_FIELD = "Enter your name:";
		HISCORE_ENTRY_TEXT2 = "Press DONE and wait...";
		HISCORE_ENTRY_TEXT2_ERROR = "Please enter valid characters\n";
		HISCORE_PLAYER_STRING = "Player";
		LOADING_TEXT = "Loading game";
	}

	public static String getHelpEnglish() {
		StringBuffer localStringBuffer = new StringBuffer("Your Mission\n");
		localStringBuffer.append("------------\n");
		localStringBuffer.append("Destroy all enemies from the sky! Don't let any spinning satellites hit the ground or you will be killed instantly! The more you hit, the more points you get! Any objects that hit the ground decrease your score!\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Controls\n");
		localStringBuffer.append("--------\n");
		localStringBuffer.append("Use the navigation key or the Alphanumeric keys to control your ship. Toggle auto-fire as needed, and use hyper-space to get out of close calls!\n");
		localStringBuffer.append("up- Toggle Auto-fire\n");
		localStringBuffer.append("left- Left\n");
		localStringBuffer.append("right- Right\n");
		localStringBuffer.append("down- Hyperspace\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("Alphanumeric keys Controls\n");
		localStringBuffer.append("--------------------------\n");
		localStringBuffer.append("4- Move Left\n");
		localStringBuffer.append("6- Move Right\n");
		localStringBuffer.append("1- Fire\n");
		localStringBuffer.append("3- Fire\n");
		localStringBuffer.append("2- Toggle Auto-fire\n");
		localStringBuffer.append("8- Hyperspace\n");
		return localStringBuffer.toString();
	}

	public static String getCredits1English() {
		StringBuffer localStringBuffer = new StringBuffer("THQ Credits\n");
		localStringBuffer.append("-----------\n");
		localStringBuffer.append("Producer- Stuart Platt, Associate Producer- Colin Totman, VP Marketing- Peter Dille, Director of Creative Services- Howard Liebeskind, Production Manager- Chris Sturr\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("THQ QA testing team credits\n");
		localStringBuffer.append("---------------------------\n");
		localStringBuffer.append("SENIOR TESTER - Scott Frazier TESTERS - Adam Affrunti, Brad Arnold, Shelley Franklin, Donald Sturkey\n");
		localStringBuffer.append("\n");
		return localStringBuffer.toString();
	}

	public static String getCredits2English() {
		StringBuffer localStringBuffer = new StringBuffer("Lavastorm Credits\n-----------------\nProducer: Jason Loia Lead Coders: Albert So, Horace Lin, Veeramurthy Veeraprakash Artwork: Mark Keavney\n\nSpecial Thanks to\n-----------------\nJohn P. Sohl\n");
		return localStringBuffer.toString();
	}

	public static String getCredits3English() {
		StringBuffer localStringBuffer = new StringBuffer("Astrosmash & Intellivision are trademarks of Intellivision Productions, Inc. Astrosmash game (C) 1981 Intellivision Productions, Inc. Used under exclusive license. Developed by Lavastorm, Inc. Game Implementation & Software (C) 2002 THQ Inc. THQ and logo are (R) of THQ Inc. All Rights Reserved.\n");
		localStringBuffer.append(AstroSmashVersion.CREDITS_UPC_CODE + "\n");
		return localStringBuffer.toString();
	}

	public static void initializeFrench() {
		TITLE_CREDITS = "CREDITS";
		TITLE_HELP = "AIDE";
		EXIT_LABEL = "QUIT";
		QUIT_LABEL = "QUIT";
		QUIT_CONFIRM_LABEL = "OUI";
		QUIT_ABORT_LABEL = "NON";
		PAUSE_LABEL = "PAUSE";
		RESUME_LABEL = "PLAY";
		RESTART_LABEL = "PLAY";
		NEXT_LABEL = "SUIV.";
		BACK_LABEL = "PRÉC.";
		SELECT_LABEL = "SELEC";
		DONE_LABEL = "FINI";
		SKIP_LABEL = "ANNUL";
		DEL_LABEL = "DEL";
		STATUS_QUESTION = "Êtes-Vous Sûr \nDe Vouloir Quitter?";
		GAME_PAUSED_STRING = "Pause";
		GAME_OVER_STRING = "GAME OVER";
		PEAK_SCORE_STRING = "MEILLEUR SCORE";
		HISCORE_TITLE = "Meilleurs Scores";
		HISCORE_ENTRY_TITLE = "Meilleurs Scores";
		HISCORE_ENTRY_TEXT1_SUFFIX = "Est un score ÉlevÉ";
		HISCORE_ENTRY_NAME_FIELD = "Saisissez Votre Nom:";
		HISCORE_ENTRY_TEXT2 = "Appuyer sur FINI et patienter...";
		HISCORE_ENTRY_TEXT2_ERROR = "Please enter valid characters\n";
		HISCORE_PLAYER_STRING = "Player";
		LOADING_TEXT = "Loading game";
	}

	public static String getHelpFrench() {
		StringBuffer localStringBuffer = new StringBuffer("VOTRE MISSION\n");
		localStringBuffer.append("------------\n");
		localStringBuffer.append("DETRUIRE TOUS LES ENNEMIS! NE LAISSEZ AUCUN SATELLITE TOURNANT TOUCHER LE SOL OU VOUS SEREZ TUE INSTANTANEMENT ! PLUS VOUS DETRUISEZ D'ENNEMIS, PLUS VOUS MARQUEZ DE POINTS ! CHAQUE OBJET QUI TOUCHE LE SOL FAIT DESCENDRE VOTRE SCORE !\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("CONTROLES\n");
		localStringBuffer.append("--------\n");
		localStringBuffer.append("APPUYEZ SUR LES TOUCHES DE NAVIGATION OU LE CLAVIER NUMERIQUE POUR CONTROLER VOTRE VAISSEAU. ACTIVEZ LE TIR AUTOMATIQUE COMME VOUS LE DESIREZ ET UTILISEZ L'HYPER-ESPACE POUR VOUS SORTIR DE SITUATIONS DIFFICILES\n");
		localStringBuffer.append("HAUT - ACTIVER:DESACTIVER LE TIR AUTOMATIQUE\n");
		localStringBuffer.append("GAUCHE - GAUCHE\n");
		localStringBuffer.append("DROITE - DROITE\n");
		localStringBuffer.append("BAS - HYPER-ESPACE\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("COMMANDES DU CLAVIER\n");
		localStringBuffer.append("--------------------------\n");
		localStringBuffer.append("4- DÉPLACEMENT VERS LA GAUCHE\n");
		localStringBuffer.append("6- DÉPLACEMENT VERS LA DROITE\n");
		localStringBuffer.append("1- FEU\n");
		localStringBuffer.append("3- FEU\n");
		localStringBuffer.append("2- PASSER AU TIR AUTOMATIQUE\n");
		localStringBuffer.append("8-HYPERESPACE\n");
		return localStringBuffer.toString();
	}

	public static String getCredits1French() {
		StringBuffer localStringBuffer = new StringBuffer("THQ CREDITS\n");
		localStringBuffer.append("-----------\n");
		localStringBuffer.append("PRODUCTEUR- Stuart Platt, PRODUCTEUR ASSOCIE- Colin Totman, VP MARKETING- Peter Dille, DIRECTEUR DU SERVICE CREATION- Howard Liebeskind, RESPONSABLE DE PRODUCTION- Chris Sturr\n");
		localStringBuffer.append("\n");
		localStringBuffer.append("CREDITS DE L'EQUIPE DE TEST DE THQ\n");
		localStringBuffer.append("---------------------------\n");
		localStringBuffer.append("TESTEUR SENIOR - Scott Frazier TESTEURS - Adam Affrunti, Brad Arnold, Shelley Franklin, Donald Sturkey\n");
		localStringBuffer.append("\n");
		return localStringBuffer.toString();
	}

	public static String getCredits2French() {
		StringBuffer localStringBuffer = new StringBuffer("CREDITS LAVASTORM\n-----------------\nPRODUCTEUR: Jason Loia PROGRAMMEURS: Albert So, Horace Lin, Veeramurthy Veeraprakash ARTISTIQUE: Mark Keavney\nREMERCIEMENTS A\n-----------------\nJohn P. Sohl\n");
		return localStringBuffer.toString();
	}

	public static String getCredits3French() {
		StringBuffer localStringBuffer = new StringBuffer("ASTROSMASH & INTELLIVISION ARE TRADEMARKS OF INTELLIVISION PRODUCTIONS, INC.\nASTROSMASH GAME © 1981 INTELLIVISION PRODUCTIONS, INC.\nUSED UNDER EXCLUSIVE LICENSE. DEVELOPED BY LAVASTORM, INC. GAME IMPLEMENTATION & SOFTWARE © 2001 THQ INC. THQ AND LOGO ARE ® OF THQ INC.  ALL RIGHTS RESERVED.\n");
		localStringBuffer.append(AstroSmashVersion.CREDITS_UPC_CODE + "\n");
		return localStringBuffer.toString();
	}
}


/* Location:              /home/exl/Projects/Java/MIDlets-JARs/astrosmash-full.jar!/com/lavastorm/astrosmash/InfoStrings.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */