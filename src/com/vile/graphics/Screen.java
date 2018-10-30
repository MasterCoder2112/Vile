package com.vile.graphics;

//import java.util.Random;
import com.vile.Display;
import com.vile.Game;
import com.vile.entities.Player;

/**
 * Title: Screen
 * 
 * @author Alex Byrd Date Created: Long ago in February 2016
 * 
 *         Description: Actually calls what is to be rendered on the screen and
 *         then renders them using the methods in the Render class. The methods
 *         called just manipulate the bits in the PIXELS array to manipulate how
 *         the screen looks.
 *
 */
public class Screen implements Runnable {
	// private Render test;

	// Holds the games Render3D object
	public static Render3D render3D;

	/**
	 * Set up the screen and the render3D object (what will be shown on the screen)
	 * with the width and height of the frame.
	 * 
	 * @param width
	 * @param height
	 */
	public Screen(int width, int height) {
		// New random that randomizes pixel colors
		// Random random = new Random();

		render3D = new Render3D(Display.WIDTH, Display.HEIGHT);

		// Test is a new render with width and height 256
		// test = new Render(256, 256);

		/*
		 * Fill each of the pixels in test with a random color using random
		 */
		// for(int i = 0; i < 256*256; i++)
		// {
		// test.PIXELS[i] = random.nextInt();
		// }

	}

	/**
	 * This method uses Renders draw method to draw the graphics on the screen
	 */
	public void render(Game game) {
		/*
		 * for(int i = 0; i < WIDTH * HEIGHT; i++) { //Sets all pixels made to blank
		 * every time the screen renders PIXELS[i] = 0; } /* for(int i = 0; i < 50; i++)
		 * { /* Causes the images offset to change very quickly in coordination with the
		 * system time in milliseconds. The +1 allows the sin to work. The number after
		 * the modulus % determines its left and rightishness. The number after the /
		 * determines its speed. The ratio of the numbers has to be the same for it to
		 * make a complete circle though. 2pi makes the circumference, and the number
		 * after it all determines the radius.
		 */
		// int animationX = (int)(Math.sin((System.currentTimeMillis() + i * 5) % 1000.0
		// / 1000 * Math.PI * 2) * 100);
		// int animationY = (int)(Math.cos((System.currentTimeMillis() + i * 5) % 1000.0
		// / 1000 * Math.PI * 2) * 100);

		// draw(test, ((WIDTH - 256)/2) + animationX, ((HEIGHT - 256)/2) + animationY);
		// }

		// Draw the floor, ceiling, and all other textures
		render3D.floor(game);

		// Display.thread2.run();
		run();
	}

	/**
	 * Runs the renderDistanceLimiter method seperately to greatly speed up the
	 * game.
	 */
	@Override
	public void run() {
		// while(Display.isRunning)
		// {
		// Does the render distance need to be limited
		boolean limitDistance = true;

		// Only create a render distance if needed. Otherwise don't because
		// it makes the game slower. Outdoors and moon themes don't need
		// it, nor does maps with a renderDistance greater than 100000
		// because at that point it might as well not have a render
		// distance.
		if (limitDistance && (Render3D.renderDistanceDefault <= 100000 || Player.playerHurt > 0 || !Player.alive
				|| Player.environProtectionTime > 0)) {
			render3D.renderDistanceLimiter();
		}

		// try
		// {
		// Thread.sleep(10);
		// }
		// catch(Exception ex)
		// {

		// }
		// }
	}
}
