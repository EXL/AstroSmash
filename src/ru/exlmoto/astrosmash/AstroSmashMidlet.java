package ru.exlmoto.astrosmash;

import java.util.Random;

public class AstroSmashMidlet {
	
	private static Random m_random;
	
	public AstroSmashMidlet() {
		m_random = new Random(System.currentTimeMillis());
	}

	public static int getAbsRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return Math.abs(m_random.nextInt());
	}
	
	public static int getRandomInt() {
		m_random.setSeed(System.currentTimeMillis() + m_random.nextInt());
		return m_random.nextInt();
	}
}

// TODO: Replace this?