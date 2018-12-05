package com.vile.graphics;

import java.util.ArrayList;

import com.vile.Display;
import com.vile.Game;
import com.vile.Sound;
import com.vile.SoundController;
import com.vile.entities.Bullet;
import com.vile.entities.Corpse;
import com.vile.entities.Enemy;
import com.vile.entities.EnemyFire;
import com.vile.entities.Explosion;
import com.vile.entities.HitSprite;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;
import com.vile.entities.Projectile;
import com.vile.entities.ServerPlayer;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Render3D
 * 
 * @author Alex Byrd Date Updated: 5/2/2017
 *
 *         Renders the graphics on the screen for all the entities, walls,
 *         etc... It renders all these things in a raycasting environment that
 *         simulates a 3D environment without using "actual" 3D.
 */
public class Render3D extends Render {
	// Checks the fps in all graphical checks throughout all the classes
	// so that images don't ever move too fast.
	public static double fpsCheck = 0;

	/*
	 * Basically an array of values used for the pixels rendered on the screen to
	 * get darker as they fade into the distance. The lower the value, the darker
	 * that pixel is. The pixel stays the same color, but its brightness value is
	 * set by these arrays. zBufferWall is for walls and zBuffer is for everything
	 * else.
	 */
	private double[] zBuffer;
	private double[] zBufferWall;

	/*
	 * Sets how many pixels away in the z direction the render distance limiter
	 * begins.
	 */
	public static double renderDistanceDefault = 100000.0;
	private double renderDistance = renderDistanceDefault;

	/*
	 * Corrects the movement of walls to the players movement. If the player moves,
	 * the walls look as if they are staying in place though they are truly not,
	 * they are just moving away at enough speed to simulate that they are staying
	 * in place with respect to the player.
	 */
	private double forwardCorrect = 3;
	private double rightCorrect = 3;

	// Speeds and turning values
	private double fowardSpeed, rightSpeed, cosine, sine;

	// Default ceiling height
	public static double ceilingDefaultHeight = 200;

	// Stores the id of the current wall being rendered to keep track
	public static int currentWallID = 0;

	// Is low res graphic settings on?
	public static boolean lowRes = false;

	// The pixel chosen to be the transparent one on walls
	public static long seeThroughWallPixel = 0xffffffff;

	// Is there an initial sound to be played?
	public static boolean isInitialSound = false;

	// Name of first audio file (if there is one) to be played
	public static String firstAudio = "";

	// All blocks to render in game
	ArrayList<Block> allBlocks;

	/**
	 * Sets up new Render3D object with a width and height. Also creates a
	 * ZBufferWall object which holds the current position of the nearest wall to
	 * the player. This is so the player can not see things that are behind walls.
	 * ZBuffer is the darkness basically of each pixel, mainly used for determining
	 * render distance stuff
	 * 
	 * @param width
	 * @param height
	 */
	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[HEIGHT * WIDTH];
		zBufferWall = new double[HEIGHT * WIDTH];
	}

	/**
	 * Initially called floor because it just rendered the floor, and the ceiling
	 * which was just an extension of the floor. Now it is called to render
	 * everything and call all the other rendering methods.
	 * 
	 * @param game
	 */
	public void floor(Game game) {
		/*
		 * If the game has yet to play an initial audio clip and there is an audio clip
		 * to be played, then play it here.
		 */
		if (firstAudio != "" && isInitialSound) {
			try {
				// Turn off all sounds before next level.
				// Display.soundController.resetSounds();

				// Search all sounds for the sound needed
				for (Sound s : SoundController.allSounds) {
					// If the audio names are equal, then play that sound
					if (firstAudio.equals(s.audioName)) {
						s.playAudioFile(0);
						break;
					}
				}
			} catch (Exception ex) {

			}

			isInitialSound = false;
		}

		/*
		 * If the low resolution settings are chosen, and the window is an even number
		 * of pixels in both directions, then set low Resolution to being true. But if
		 * not, then set them to not being true. If the screen has an odd number of
		 * pixels then low resolution (going two pixels at a time) will try to render
		 * off the screen causing the game to crash.
		 */
		if (WIDTH % 2 == 0 && HEIGHT % 2 == 0 && (FPSLauncher.resolutionChoice == 2 || FPSLauncher.resolutionChoice == 0
				|| FPSLauncher.resolutionChoice == 4)) {
			lowRes = true;
		} else {
			lowRes = false;
		}

		/*
		 * Determines how textures should change due to the players rotation, and
		 * movement speed.
		 */
		cosine = Math.cos(Player.rotation);
		sine = Math.sin(Player.rotation);
		fowardSpeed = Player.z * 21.3;
		rightSpeed = Player.x * 21.3;

		// Ceiling Height and floor depth in accordance to where the
		// Player is in the y direction
		double ceilingHeight = ceilingDefaultHeight - Player.yCorrect;
		double floorDepth = 9 + Player.yCorrect;

		// Default is 1,000,000 for brightness is survival
		if (FPSLauncher.gameMode == 1) {
			renderDistanceDefault = 1000000;
		}

		// If weapon isn't being fired, keep the brightness as it normally
		// is, else make it slightly brighter to simulate a shot.
		if (Player.weapons[Player.weaponEquipped].weaponShootTime == 0
				&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
			renderDistance = renderDistanceDefault;
		} else {
			renderDistance = renderDistanceDefault + 5000;
		}

		// Increase by 1 pixel at a time
		int correction = 1;

		// If low graphics, then increase by 2 pixels at a time
		if (lowRes) {
			correction = 2;
		}

		zBufferWall = new double[HEIGHT * WIDTH];
		zBuffer = new double[HEIGHT * WIDTH];

		// Corrects it so that at a higher fps rate, the animations
		// and movements in the game are not incredibly faster than
		// they should be.
		fpsCheck = ((Display.fps / 40) + 1);

		// Things were going too fast under 40fps
		// So keep correction as being 2
		if (Display.fps <= 40) {
			fpsCheck = 2;
		}

		// How much to cut the textures of the floor off by if they won't be shown
		// anyway due to the HUD.
		short cut = 0;

		if (FPSLauncher.resolutionChoice < 2) {
			cut = 128;
		}

		allBlocks = new ArrayList<Block>();

		renderBlocks();

		/*
		 * For every enemy on the map, render each, and also update all of its values
		 * and such as well. These type of things used to be updated in the Game class
		 * but because the game already goes through all the enemies here anyway,
		 * instead of going through all of them twice, this only calls all of them once,
		 * increasing the fps and optimizing the game even more, especially on very slow
		 * computers and with huge amounts of enemies.
		 */
		for (int i = 0; i < Game.enemies.size(); ++i) {
			Enemy enemy = Game.enemies.get(i);

			// Correct the enemies y so it appears correct graphically
			double yCorrect = enemy.getY();

			/*
			 * Because the boss is so gosh darn big, correct his graphical height of the
			 * ground so it looks like he is touching the ground.
			 */
			if (enemy.isABoss) {
				yCorrect -= 4;
			}

			// Call method to render Enemy
			renderEnemy(enemy.getX(), yCorrect, enemy.getZ(), 0.2, enemy.ID, enemy);
		}

		/*
		 * For each item, render correctly and update its values, as well as see if it
		 * can be picked up by the player to optimize the game since it already has to
		 * call this method anyway.
		 */
		for (int i = 0; i < Game.items.size(); ++i) {
			Item item = Game.items.get(i);

			// Block item is over or on
			Block temp = Level.getBlock((int) item.x, (int) item.z);

			// Difference between the item and the top of the block
			double difference = item.y - (temp.height + (temp.y * 4) + temp.baseCorrect);

			/*
			 * To correct corpse heights because again, graphics in this game make no gosh
			 * darn sense.
			 */
			double divideVariable = 9;

			if (item.y >= 60) {
				divideVariable = 11;
			} else if (item.y >= 48) {
				divideVariable = 10.7;
			} else if (item.y >= 36) {
				divideVariable = 10.5;
			} else if (item.y >= 24) {
				divideVariable = 10;
			} else if (item.y > 12) {
				divideVariable = 9.5;
			}
			// The correct y it will graphically be displayed as on
			// the screen
			double yCorrect = (-item.y / divideVariable);

			// Extra height added
			double extra = 0;

			/*
			 * The bigger the sprite needs to be for the item being displayed, then lift it
			 * higher off the ground so that it does not seem to clip through the ground.
			 */
			if (item.size < 240) {
				extra -= 0.3;
			}

			// Smaller sprites
			else if (item.size >= 240 && item.size < 420) {
				extra -= 0.5;
			}
			// Larger sprites
			else if (item.size >= 420 && item.size < 600) {
				extra -= 0.7;
			}
			// Larger sprites
			else if (item.size == 600) {
				extra -= 0.95;
			}

			// Larger sprites
			else if (item.size == 720) {
				extra -= 1.1;
			}

			// If Large objects
			else if (item.size == 2048) {
				extra -= 3.2;
			} else {
				extra -= 0.3;
			}

			// Fix depending on resolution
			if (FPSLauncher.resolutionChoice > 3) {
				extra = extra * (5.0 / 6);
			} else if (FPSLauncher.resolutionChoice > 1) {
				extra = extra * (11.0 / 12);
			} else {
				extra = extra * (12.0 / 11);
			}

			yCorrect += extra;

			if ((item.floatAmount > 0 && item.floated < item.floatHeight)
					|| (item.floatAmount < 0 && item.floated > 0)) {
				item.floated += item.floatAmount;
				yCorrect += (item.floated - item.floatHeight);
			}

			if ((item.floated >= item.floatHeight && item.floatAmount > 0)
					|| (item.floated <= 0 && item.floatAmount < 0)) {
				item.floatAmount = -item.floatAmount;
			}

			// Only render item if it is seeable (can be seen by player).
			if (item.isSeeable) {
				renderItems(item.x, yCorrect + 0.9, item.z, 0, item.itemID, item);
			}
		}

		/*
		 * Renders all the corpses in the game, as well as updating their values for
		 * optimization purposes so the game doesn't have to run through all the corpses
		 * twice per tick.
		 */
		for (int i = 0; i < Game.corpses.size(); i++) {
			Corpse corpse = Game.corpses.get(i);

			/*
			 * To correct corpse heights because again, graphics in this game make no gosh
			 * darn sense.
			 */
			double divideVariable = 8.5;

			if (corpse.yPos >= 60) {
				divideVariable = 10.8;
			} else if (corpse.yPos >= 48) {
				divideVariable = 11.7;
			} else if (corpse.yPos >= 36) {
				divideVariable = 11.5;
			} else if (corpse.yPos >= 24) {
				divideVariable = 11;
			} else if (corpse.yPos > 12) {
				divideVariable = 10;
			}
			// The correct y it will graphically be displayed as on
			// the screen
			double yCorrect = (-corpse.yPos / divideVariable);

			// Render corpse
			renderCorpse(corpse.xPos, yCorrect, corpse.zPos, 0, corpse);

		}

		/*
		 * Ticks here since this will be called anyway, and makes it so that it doesn't
		 * have to run through all the explosions twice since this runs through them
		 * anyway.
		 */
		for (int i = 0; i < Game.explosions.size(); i++) {
			Explosion explosion = Game.explosions.get(i);

			renderExplosion(explosion.x, explosion.y, explosion.z, 0, explosion);
		}

		/*
		 * Renders the bullet objects in the game
		 */
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);

			renderProjectiles(bullet.x, bullet.y, bullet.z, 0.2, bullet.ID, bullet);
		}

		/*
		 * Renders all the enemy projectiles.
		 */
		for (int i = 0; i < Game.enemyProjectiles.size(); i++) {
			EnemyFire temp = Game.enemyProjectiles.get(i);

			renderProjectiles(temp.x, (temp.y) / 1.5, temp.z, 0.2, temp.ID, temp);
		}

		/*
		 * Renders all extra sprites in the game
		 */
		for (int i = 0; i < Game.sprites.size(); i++) {
			HitSprite hS = Game.sprites.get(i);

			// render sprite
			renderHitSprite(hS.x, hS.y, hS.z, 0, hS);
		}

		/*
		 * Renders all other player sprites in the game
		 */
		// TODO This is not finished, only for testing the texture currently
		for (int i = 0; i < Game.otherPlayers.size(); i++) {
			ServerPlayer sP = Game.otherPlayers.get(i);
			// render sprite
			renderPlayers(sP.x, sP.y, sP.z, 0, sP.ID);
		}

		// Everything below draws up the crosshair
		int crossWidth = WIDTH;
		int crossHeight = HEIGHT;

		if (FPSLauncher.resolutionChoice < 4) {
			crossHeight -= 128;
		}

		PIXELS[(crossWidth / 2) + (crossHeight / 2) * crossWidth] = 0x00FF00;
		zBuffer[((crossWidth / 2)) + ((crossHeight / 2)) * crossWidth] = 1;

		for (int i = 3; i < 12; i++) {
			PIXELS[((crossWidth / 2)) + ((crossHeight / 2) - i) * crossWidth] = 0x00FF00;
			PIXELS[((crossWidth / 2)) + ((crossHeight / 2) + i) * crossWidth] = 0x00FF00;
			PIXELS[((crossWidth / 2) - i) + ((crossHeight / 2)) * crossWidth] = 0x00FF00;
			PIXELS[((crossWidth / 2) + i) + ((crossHeight / 2)) * crossWidth] = 0x00FF00;
			zBuffer[((crossWidth / 2)) + ((crossHeight / 2) - i) * crossWidth] = 1;
			zBuffer[((crossWidth / 2)) + ((crossHeight / 2) + i) * crossWidth] = 1;
			zBuffer[((crossWidth / 2) - i) + ((crossHeight / 2)) * crossWidth] = 1;
			zBuffer[((crossWidth / 2) + i) + ((crossHeight / 2)) * crossWidth] = 1;
		}

		// Go through all pixels in y direction
		for (int y = 0; y < HEIGHT - cut; y += correction) {
			/*
			 * Sets the ceiling and the floor basically, ceiling is both of them. they are
			 * horizontal lines drawn at certain y coordinates that basically decrease in
			 * distance between by a factor of 2.0 each time they are drawn, then they are
			 * divided by height to get the squares to look right.
			 */
			double ceiling = (y - HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate)) / HEIGHT;

			/*
			 * Determines where the floor and ceiling should meet. This is basically the
			 * depth of field. Pixels seem to get smaller and more out of focus the
			 * "farther" they seem to be from the player.
			 */
			double z = floorDepth / ceiling;

			/*
			 * Makes the actual ceiling of the frame move the same direction as the floor
			 * instead of being the inverse of the of the floor. Also sets the distance the
			 * ceiling is above the player.
			 */
			if (ceiling < 0) {
				z = ceilingHeight / -ceiling;
			}

			// Go through all pixels on the x
			for (int x = 0; x < WIDTH; x += correction) {

				boolean renderThis = false;

				if (zBuffer[(x) + (y) * WIDTH] == 0) {
					renderThis = true;
				}

				// If walls and stuff are in front of the floor or ceiling, don't render this
				// part of the floor.
				// if (zBuffer[(x) + (y) * WIDTH] <= z && zBuffer[(x) + (y) * WIDTH] > 0) {
				// break;
				// }

				if (renderThis) {
					/*
					 * Sets the bars along the x that go up in y. As the bars go into the distance,
					 * the distance between them decreases by a factor of 2.0. Then the distance
					 * between them is / HEIGHT to set them equal to the distance between the y
					 * ones, to make squares.
					 */
					double depth = (x - WIDTH / 2.0) / HEIGHT;

					// Test depth. Works similarly but has issues
					// double depth = 2 * x / (double)WIDTH - 1;

					// Sets the depth relative to z direction
					depth *= z;

					/*
					 * Positions of floor and ceiling pixels on screen in accordance to how much the
					 * player has moved already and in which direction he/she is looking. This is
					 * very hard to explain what is going on, but as seen here with the cosine and
					 * sine formulas, the map is a x and y grid in a way, and when you rotate, the
					 * pixels around you act sort of like a circle, and you rotate, the textures
					 * rotate with you in a way to make it seem as if you are turning. And if you go
					 * foward or backwards they will move in accordance to that as well.
					 */
					double xx = ((depth * (cosine)) + (z * (sine))) + rightSpeed;
					double yy = ((z * cosine) - (depth * sine)) + fowardSpeed;

					/*
					 * This makes the squares the floor and ceiling are made up of smaller the
					 * bigger the number is, and bigger the smaller the number is.
					 */
					double textureCorrect = 16;

					/*
					 * I still don't understand this part. It multiplies the integer bits by 255 so
					 * that it can get the particular pixel from the 256 by 256 pixel image file,
					 * but otherwise it confuses me how this works.
					 * 
					 * I guess textureCorrect is something that determines how much the pixels are
					 * stretched on screen
					 */
					int xPix = (int) ((xx) * textureCorrect) & 255;
					int yPix = (int) ((yy) * textureCorrect) & 255;

					// For survival in smile
					if (Display.smileMode && FPSLauncher.gameMode == 1) {
						Game.mapCeiling = 4;
						Game.mapFloor = 9;
					}

					// For the sky
					if (ceiling < 0) {
						/*
						 * Depending on the ID of the ceiling texture, then render that texture that is
						 * chosen by picking it out from the floors array from the textures class
						 */
						PIXELS[x + y * WIDTH] = // ((xPix) << 8 | (yPix) << 16);
								Textures.floors[Game.mapCeiling].PIXELS[(xPix & 255) + (yPix & 255) * 256];

						zBuffer[(x) + (y) * WIDTH] = z;

						// Here I show that it can be shown in
						// hexidecimals as well as normal decimals
						if (lowRes) {
							PIXELS[(x + 1) + (y) * WIDTH] = Textures.floors[Game.mapCeiling].PIXELS[(xPix & 255)
									+ (yPix & 255) * 256];
							PIXELS[(x + 1) + (y + 1) * WIDTH] = Textures.floors[Game.mapCeiling].PIXELS[(xPix & 255)
									+ (yPix & 255) * 256];
							PIXELS[(x) + (y + 1) * WIDTH] = Textures.floors[Game.mapCeiling].PIXELS[(xPix & 255)
									+ (yPix & 255) * 256];
							zBuffer[(x + 1) + (y + 1) * WIDTH] = z;
							zBuffer[(x + 1) + (y) * WIDTH] = z;
							zBuffer[(x) + (y + 1) * WIDTH] = z;
						}
					} else {
						PIXELS[x + y * WIDTH] = // ((xPix) << 8 | (yPix) << 16);
								Textures.floors[Game.mapFloor].PIXELS[(xPix & 255) + (yPix & 255) * 256];

						zBuffer[(x) + (y) * WIDTH] = z;

						if (lowRes) {
							PIXELS[(x + 1) + (y) * WIDTH] = // ((xPix) << 8 | (yPix) << 16);
									Textures.floors[Game.mapFloor].PIXELS[(xPix & 255) + (yPix & 255) * 256];
							PIXELS[(x + 1) + (y + 1) * WIDTH] = // ((xPix) << 8 | (yPix) << 16);
									Textures.floors[Game.mapFloor].PIXELS[(xPix & 255) + (yPix & 255) * 256];
							PIXELS[(x) + (y + 1) * WIDTH] = // ((xPix) << 8 | (yPix) << 16);
									Textures.floors[Game.mapFloor].PIXELS[(xPix & 255) + (yPix & 255) * 256];
							zBuffer[(x + 1) + (y + 1) * WIDTH] = z;
							zBuffer[(x + 1) + (y) * WIDTH] = z;
							zBuffer[(x) + (y + 1) * WIDTH] = z;
						}
					}
				}
			}
		}

		// System.out.println(zBuffer[(((crossWidth / 2) + 1) + ((crossHeight / 2)) *
		// crossWidth)]);
	}

	/*
	 * Renders other players on the server given their player ID and positions
	 */
	// TODO Not finished yet, fix this up
	public void renderPlayers(double x, double y, double z, double hOffSet, int ID) {
		/*
		 * The change in x, y, and z coordinates in correlation to the player to correct
		 * them in accordance to the players position so that they don't seem to move.
		 * Casts a vector between the item and Player to figure out the difference in
		 * distance.
		 */
		double xC = (x - Player.x) * 1.9;
		double yC = (Player.yCorrect / 11) - (y / 11);
		double zC = (z - Player.z) * 1.9;

		// TODO FIX JUMPING AND CROUCHING

		// Size of sprite in terms of pixels. 512 x 512 is typical
		int spriteSize = 512;

		/*
		 * Draws the actually distance to the entity depending on the direction the
		 * player is looking and the x, y, and z positions of the player and entity.
		 */
		double rotX = (xC * cosine) - (zC * sine);
		double rotY = yC;
		double rotZ = (zC * cosine) + (xC * sine);

		// If enemy is behind player, and not in sight, then don't even try
		// to render. This should speed things up immensely.
		if (rotZ <= 0) {
			return;
		}

		/*
		 * Centers the way the sprites are rendered towards the center of the screen as
		 * that is the center of your view
		 */
		double xCenter = WIDTH / 2;
		// double yCenter = HEIGHT / 2;

		// How wide in terms of pixels the sprite is on screen
		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;

		// How high in terms of pixels the texture is on screen.
		// Has to be slightly corrected when player looks up
		// or down.
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		/*
		 * Figures out the sides of the object being rendered by drawing vectors to
		 * them. These sides will be used in int form below to loop through the image
		 * rendering all of its pixels to the screen.
		 */
		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);
		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		// Converts them into ints
		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		// I used this because it didn't work without this.
		rotZ *= 8;

		// Dimensions of image being loaded in
		int imageDimensions = 256;

		/*
		 * Make sure it doesn't try to render textures off the screen because they can't
		 * be
		 */
		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		} else if (yPixR < 0) {
			yPixR = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		} else if (xPixR < 0) {
			xPixR = 0;
		}

		if (yPixL > HEIGHT) {
			yPixL = HEIGHT;
		} else if (yPixL < 0) {
			yPixL = 0;
		}

		if (xPixL > WIDTH) {
			xPixL = WIDTH;
		} else if (xPixL < 0) {
			xPixL = 0;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		Render playerModel = Textures.playerModel;

		try {
			/*
			 * Performs different operations when looping through the pixels depending on
			 * whether low resolution is turned on or not. This makes the game faster than
			 * having if statements within the double for loops.
			 */
			if (lowRes) {
				/*
				 * Render each pixel in the sprite correctly, and with the (zBuffer) as well so
				 * that sprites behind other sprites will not clip through sprites in front of
				 * them.
				 * 
				 * For each sprite also texture them correctly and correct the textures for
				 * rotation and movement.
				 */
				for (int yy = yPixL; yy < yPixR; yy += 2) {
					// How the sprite rotates up or down based on players up and
					// down rotation
					double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

					// Sets the current pixel to an int, and multiplies it by
					// 256 since the image is 256 by 256
					int yTexture = (int) (pixelRotationY * imageDimensions);

					// Go through width of image now
					for (int xx = xPixL; xx < xPixR; xx += 2) {
						// Corrects the way the pixel faces in the x direction
						// depending on the players rotation
						double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

						// Sets the current pixel to an int and multiplies it by
						// 256 as that is the size of the image.
						int xTexture = (int) (pixelRotationX * imageDimensions);

						// Can the textures be seen
						boolean seen = false;

						/*
						 * In case a rare bug continues to occur.
						 */
						if (zBuffer[(xx) + (yy) * WIDTH] < 0) {
							zBuffer[(xx) + (yy) * WIDTH] = 0;
						}

						try {
							// If not behind something else, and still has pixels
							// to be rendered, the enemy can be seen
							if ((zBuffer[(xx) + (yy) * WIDTH] > rotZ && zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ
									&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
									&& zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ)
									|| (zBuffer[(xx) + (yy) * WIDTH] == 0 || zBuffer[(xx) + (yy + 1) * WIDTH] == 0
											|| zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
											|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
								seen = true;
							}
						} catch (Exception e) {
							continue;
						}

						// If pixel is seen and can be rendered
						if (seen) {
							int color = 0;

							// Catch any weird errors and just continue
							try {
								// Set the color of the pixel to be generated.
								color = playerModel.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];

								if (ID > 0 && color != 0xffffffff) {
									if (ID == 1) {
										color = changeColor(255, 255, 255, color, 8, 0, 16);
									} else if (ID == 2) {
										color = changeColor(255, 255, 255, color, 16, 0, 8);
									} else {
										color = changeColor(255, 255, 255, color, 0, 16, 8);
									}
								}
							} catch (Exception e) {
								continue;
							}

							/*
							 * If color is not white, render it, otherwise don't to have transparency around
							 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
							 * green, 4th two are blue. ff is 255.
							 */
							if (color != 0xffffffff) {
								try {
									// Sets pixel in 2D array to that color
									PIXELS[xx + yy * WIDTH] = color;

									// Adds to the pixels on screen this enemy takes up
									// enemy.pixelsOnScreen.set((xx + 1) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 1) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set(xx + yy * WIDTH, true);

									// This is now the nearest pixel to the
									// player, at this coordinate so create
									// the new buffer here.
									zBuffer[(xx) + (yy) * WIDTH] = rotZ;

									PIXELS[(xx + 1) + (yy) * WIDTH] = color;
									zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
									PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
									zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
									PIXELS[(xx) + (yy + 1) * WIDTH] = color;
									zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
								} catch (Exception e) {

								}

								// Reapers are translucent because theyre ghost
								// technically
								if (ID == 4 && !Display.smileMode) {
									// Adds to the pixels on screen this takes up
									// Reapers still take up the same amount of space
									// Though they are translucent
									// enemy.pixelsOnScreen.set((xx + 2) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 2) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 3) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 3) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 4) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 4) + (yy) * WIDTH, true);

									xx += 2;
								}
							}
						}
					}
				}
			} else {
				/*
				 * Render each pixel in the sprite correctly, and with the (zBuffer) as well so
				 * that sprites behind other sprites will not clip through sprites in front of
				 * them.
				 * 
				 * For each sprite also texture them correctly and correct the textures for
				 * rotation and movement.
				 */
				for (int yy = yPixL; yy < yPixR; yy++) {
					// How the sprite rotates up or down based on players up and
					// down rotation
					double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

					// Sets the current pixel to an int, and multiplies it by
					// 256 since the image is 256 by 256
					int yTexture = (int) (pixelRotationY * imageDimensions);

					// Go through width of image now
					for (int xx = xPixL; xx < xPixR; xx++) {
						// Corrects the way the pixel faces in the x direction
						// depending on the players rotation
						double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

						// Sets the current pixel to an int and multiplies it by
						// 256 as that is the size of the image.
						int xTexture = (int) (pixelRotationX * imageDimensions);

						// Can the textures be seen
						boolean seen = false;

						try {
							// If not behind something else, and still has pixels
							// to be rendered, the enemy can be seen
							if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
								seen = true;
							}
						} catch (Exception e) {
							continue;
						}

						// If pixel is seen and can be rendered
						if (seen) {
							int color = 0;

							// Catch any weird errors and just continue
							try {
								// Set the color of the pixel to be generated.
								color = playerModel.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];

								if (ID > 0 && color != 0xffffffff) {
									if (ID == 1) {
										color = changeColor(255, 255, 255, color, 8, 0, 16);
									} else if (ID == 2) {
										color = changeColor(60, 255, 255, color, 16, 8, 0);
									} else {
										color = changeColor(255, 255, 255, color, 0, 16, 8);
									}
								}
							} catch (Exception e) {
								continue;
							}

							/*
							 * If color is not white, render it, otherwise don't to have transparency around
							 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
							 * green, 4th two are blue. ff is 255.
							 */
							if (color != 0xffffffff) {
								try {
									// This is now the nearest pixel to the
									// player, at this coordinate so create
									// the new buffer here.
									zBuffer[(xx) + (yy) * WIDTH] = rotZ;

									// Sets pixel in 2D array to that color
									PIXELS[xx + yy * WIDTH] = color;
								} catch (Exception e) {

								}

								// Reapers are translucent because theyre ghost
								// technically
								if (ID == 4 && !Display.smileMode) {
									// Adds to the pixels on screen this takes up
									// enemy.pixelsOnScreen.set((xx + 1) + yy * WIDTH, true);

									xx++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return;
		}
	}

	/*
	 * Renders each enemy on the map as a sprite. Each sprites position is corrected
	 * for the players movement so that it does not move (other than its own
	 * movement) in correlation to the players movement.
	 */
	public void renderEnemy(double x, double y, double z, double hOffSet, int ID, Enemy enemy) {
		/*
		 * The change in x, y, and z coordinates in correlation to the player to correct
		 * them in accordance to the players position so that they don't seem to move.
		 * Casts a vector between the item and Player to figure out the difference in
		 * distance.
		 */
		double xC = (x - Player.x) * 1.9;
		double yC = (y) + (Player.yCorrect / 11);
		double zC = (z - Player.z) * 1.9;

		// Size of sprite in terms of pixels. 512 x 512 is typical
		int spriteSize = 512;

		// If a boss, have a bigger sprite size
		if (enemy.isABoss) {
			spriteSize = 4096;
		}

		/*
		 * Draws the actually distance to the entity depending on the direction the
		 * player is looking and the x, y, and z positions of the player and entity.
		 */
		double rotX = (xC * cosine) - (zC * sine);
		double rotY = yC;
		double rotZ = (zC * cosine) + (xC * sine);

		// If enemy is behind player, and not in sight, then don't even try
		// to render. This should speed things up immensely.
		if (rotZ <= 0) {
			return;
		}

		/*
		 * Centers the way the sprites are rendered towards the center of the screen as
		 * that is the center of your view
		 */
		double xCenter = WIDTH / 2;
		// double yCenter = HEIGHT / 2;

		// How wide in terms of pixels the sprite is on screen
		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;

		// How high in terms of pixels the texture is on screen.
		// Has to be slightly corrected when player looks up
		// or down.
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		/*
		 * Figures out the sides of the object being rendered by drawing vectors to
		 * them. These sides will be used in int form below to loop through the image
		 * rendering all of its pixels to the screen.
		 */
		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);
		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		// Converts them into ints
		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		// I used this because it didn't work without this.
		rotZ *= 8;

		// Dimensions of image being loaded in
		int imageDimensions = 256;

		// Updates enemy phase
		enemy.enemyPhase++;

		// Is enemy in the players sight
		enemy.inSight = false;

		/*
		 * Make sure it doesn't try to render textures off the screen because they can't
		 * be
		 */
		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		} else if (yPixR < 0) {
			yPixR = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		} else if (xPixR < 0) {
			xPixR = 0;
		}

		if (yPixL > HEIGHT) {
			yPixL = HEIGHT;
		} else if (yPixL < 0) {
			yPixL = 0;
		}

		if (xPixL > WIDTH) {
			xPixL = WIDTH;
		} else if (xPixL < 0) {
			xPixL = 0;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		// enemy.pixelsOnScreen = new ArrayList<Boolean>(Display.HEIGHT *
		// Display.WIDTH);
		enemy.canBeSeen = false;

		try {
			/*
			 * Performs different operations when looping through the pixels depending on
			 * whether low resolution is turned on or not. This makes the game faster than
			 * having if statements within the double for loops.
			 */
			if (lowRes) {
				/*
				 * Render each pixel in the sprite correctly, and with the (zBuffer) as well so
				 * that sprites behind other sprites will not clip through sprites in front of
				 * them.
				 * 
				 * For each sprite also texture them correctly and correct the textures for
				 * rotation and movement.
				 */
				for (int yy = yPixL; yy < yPixR; yy += 2) {
					// How the sprite rotates up or down based on players up and
					// down rotation
					double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

					// Sets the current pixel to an int, and multiplies it by
					// 256 since the image is 256 by 256
					int yTexture = (int) (pixelRotationY * imageDimensions);

					// Go through width of image now
					for (int xx = xPixL; xx < xPixR; xx += 2) {
						// Corrects the way the pixel faces in the x direction
						// depending on the players rotation
						double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

						// Sets the current pixel to an int and multiplies it by
						// 256 as that is the size of the image.
						int xTexture = (int) (pixelRotationX * imageDimensions);

						// Can the textures be seen
						boolean seen = false;

						/*
						 * In case a rare bug continues to occur.
						 */
						if (zBuffer[(xx) + (yy) * WIDTH] < 0) {
							zBuffer[(xx) + (yy) * WIDTH] = 0;
						}

						try {
							// If not behind something else, and still has pixels
							// to be rendered, the enemy can be seen
							if ((zBuffer[(xx) + (yy) * WIDTH] > rotZ && zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ
									&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
									&& zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ)
									|| (zBuffer[(xx) + (yy) * WIDTH] == 0 || zBuffer[(xx) + (yy + 1) * WIDTH] == 0
											|| zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
											|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
								seen = true;
							}
						} catch (Exception e) {
							continue;
						}

						// If pixel is seen and can be rendered
						if (seen) {
							int color = 0;

							// Catch any weird errors and just continue
							try {
								// Set the color of the pixel to be generated.
								color = enemy.currentPhase.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
							} catch (Exception e) {
								continue;
							}

							/*
							 * If color is not white, render it, otherwise don't to have transparency around
							 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
							 * green, 4th two are blue. ff is 255.
							 */
							if (color != 0xffffffff) {
								try {
									// Sets pixel in 2D array to that color
									PIXELS[xx + yy * WIDTH] = color;

									// Adds to the pixels on screen this enemy takes up
									// enemy.pixelsOnScreen.set((xx + 1) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 1) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set(xx + yy * WIDTH, true);

									// This is now the nearest pixel to the
									// player, at this coordinate so create
									// the new buffer here.
									zBuffer[(xx) + (yy) * WIDTH] = rotZ;

									PIXELS[(xx + 1) + (yy) * WIDTH] = color;
									zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
									PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
									zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
									PIXELS[(xx) + (yy + 1) * WIDTH] = color;
									zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;

									enemy.canBeSeen = true;
								} catch (Exception e) {

								}

								// Reapers are translucent because theyre ghost
								// technically
								if (ID == 4 && !Display.smileMode) {
									// Adds to the pixels on screen this takes up
									// Reapers still take up the same amount of space
									// Though they are translucent
									// enemy.pixelsOnScreen.set((xx + 2) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 2) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 3) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 3) + (yy) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 4) + (yy + 1) * WIDTH, true);
									// enemy.pixelsOnScreen.set((xx + 4) + (yy) * WIDTH, true);

									xx += 2;
								}
							}
						}
					}
				}
			} else {
				/*
				 * Render each pixel in the sprite correctly, and with the (zBuffer) as well so
				 * that sprites behind other sprites will not clip through sprites in front of
				 * them.
				 * 
				 * For each sprite also texture them correctly and correct the textures for
				 * rotation and movement.
				 */
				for (int yy = yPixL; yy < yPixR; yy++) {
					// How the sprite rotates up or down based on players up and
					// down rotation
					double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

					// Sets the current pixel to an int, and multiplies it by
					// 256 since the image is 256 by 256
					int yTexture = (int) (pixelRotationY * imageDimensions);

					// Go through width of image now
					for (int xx = xPixL; xx < xPixR; xx++) {
						// Corrects the way the pixel faces in the x direction
						// depending on the players rotation
						double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

						// Sets the current pixel to an int and multiplies it by
						// 256 as that is the size of the image.
						int xTexture = (int) (pixelRotationX * imageDimensions);

						// Can the textures be seen
						boolean seen = false;

						try {
							// If not behind something else, and still has pixels
							// to be rendered, the enemy can be seen
							if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
								seen = true;
							}
						} catch (Exception e) {
							continue;
						}

						// If pixel is seen and can be rendered
						if (seen) {
							int color = 0;

							// Catch any weird errors and just continue
							try {
								// Set the color of the pixel to be generated.
								color = enemy.currentPhase.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
							} catch (Exception e) {
								continue;
							}

							/*
							 * If color is not white, render it, otherwise don't to have transparency around
							 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
							 * green, 4th two are blue. ff is 255.
							 */
							if (color != 0xffffffff) {
								try {
									// This is now the nearest pixel to the
									// player, at this coordinate so create
									// the new buffer here.
									zBuffer[(xx) + (yy) * WIDTH] = rotZ;

									// Sets pixel in 2D array to that color
									PIXELS[xx + yy * WIDTH] = color;

									// Adds to the pixels on screen this takes up
									// enemy.pixelsOnScreen.set(xx + yy * WIDTH, true);
									enemy.canBeSeen = true;
								} catch (Exception e) {

								}

								// Reapers are translucent because theyre ghost
								// technically
								if (ID == 4 && !Display.smileMode) {
									// Adds to the pixels on screen this takes up
									// enemy.pixelsOnScreen.set((xx + 1) + yy * WIDTH, true);

									xx++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return;
		}

		// TODO FIX
		/*
		 * If off screen, still save the pixels of the enemy in a crude way so that
		 * projectiles can still hit the enemies if you look away from them. Of course
		 * in this method it cannot determine the color of the pixels so it just gets
		 * the entire pixel array instead of only the non see through pixels. It's buggy
		 * but it works.
		 */
		/*
		 * if(!xRender) { xPixL = (int)(xPixelL); xPixR = (int)(xPixelR); yPixL =
		 * (int)(yPixelL); yPixR = (int)(yPixelR);
		 * 
		 * int lesserX = xPixL; int lesserY = yPixL; int greaterX = xPixR; int greaterY
		 * = yPixR;
		 * 
		 * if(xPixR < xPixL) { lesserX = xPixR; greaterX = xPixL; }
		 * 
		 * if(yPixR < yPixL) { lesserY = yPixR; greaterY = yPixL; }
		 * 
		 * for(int i = lesserY; i < greaterY; i++) { for(int j = lesserX; j < greaterX;
		 * j++) { enemy.pixelsOnScreen.add(i + j * WIDTH); } } }
		 */
	}

	/**
	 * Renders items the same way as the enemies
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param hOffSet
	 * @param ID
	 */
	public void renderItems(double x, double y, double z, double hOffSet, int ID, Item item) {
		// If Item is not seeable then just return
		if (!item.isSeeable || item.pickedUp) {
			return;
		}

		double xC = ((x) - Player.x) * 1.9;
		double yC = y + (Player.yCorrect / 11);
		double zC = ((z) - Player.z) * 1.9;

		double rotX = xC * cosine - zC * sine;
		double rotY = yC;
		double rotZ = zC * cosine + xC * sine;

		// If the item is behind the player, do not render it.
		if (rotZ <= 0) {
			return;
		}

		double xCenter = WIDTH / 2;

		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		double xPixelL = xPixel - (item.size / rotZ);
		double xPixelR = xPixel + (item.size / rotZ);

		double yPixelL = yPixel - (item.size / rotZ);
		double yPixelR = yPixel + (item.size / rotZ);

		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		if (xPixL < 0) {
			xPixL = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		}

		if (yPixL < 0) {
			yPixL = 0;
		}

		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		rotZ *= 8;

		/*
		 * Because the rendering loop is longer the farther you are from the item, this
		 * updates the item no matter how far you are from the item, making the phase
		 * speed the same for the item
		 */
		item.phaseTime++;

		int imageDimensions = 256;

		// Brightness of image
		int brightness = 255;

		// Does the image change brightness?
		boolean changesBrightness = false;

		/*
		 * Figures out item image to load
		 */
		switch (ID) {
		// If Megahealth. Continue to switch phases
		// For some reason enums don't work in case statements which
		// is stupid.
		case 1:
			if (item.phaseTime <= 20 * fpsCheck) {
				item.itemImage = Textures.megaPhase1;
			} else if (item.phaseTime <= 40 * fpsCheck) {
				item.itemImage = Textures.megaPhase2;
			} else if (item.phaseTime <= 60 * fpsCheck) {
				item.itemImage = Textures.megaPhase3;
			} else {
				item.itemImage = Textures.megaPhase4;
			}

			if (item.phaseTime > 80 * fpsCheck) {
				item.phaseTime = 0;
			}

			break;

		// Health pack
		case 2:
			item.phaseTime = 0;

			item.itemImage = Textures.health;

			break;

		// Shotgun shell
		case 3:
			item.phaseTime = 0;

			item.itemImage = Textures.shell;

			break;

		// Red KeyCard
		case 4:
			item.phaseTime = 0;

			item.itemImage = Textures.redKey;

			break;

		// Blue keycard
		case 5:
			item.phaseTime = 0;

			item.itemImage = Textures.blueKey;
			break;

		// Green keycard
		case 6:
			item.phaseTime = 0;

			item.itemImage = Textures.greenKey;
			break;

		// Yellow keycard
		case 7:
			item.phaseTime = 0;
			item.itemImage = Textures.yellowKey;
			break;

		// Shotgun
		case 21:
			item.phaseTime = 0;
			item.itemImage = Textures.shotgun;
			break;

		// Skull of resurrection
		case 24:
			brightness = 255;

			if (item.phaseTime <= 20 * fpsCheck) {
				brightness = 150;

				item.itemImage = Textures.resurrect1;
			} else if (item.phaseTime <= 40 * fpsCheck) {
				brightness = 200;

				item.itemImage = Textures.resurrect2;
			} else if (item.phaseTime <= 60 * fpsCheck) {
				item.itemImage = Textures.resurrect3;
			}

			if (item.phaseTime > 60 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Environmental Protection Suit
		case 25:
			item.phaseTime = 0;
			item.itemImage = Textures.environSuit;
			break;

		// Goblet of Immortality
		case 26:
			if (item.phaseTime <= 20 * fpsCheck) {
				brightness = 255;
				item.itemImage = Textures.goblet1;
			} else if (item.phaseTime <= 40 * fpsCheck) {
				brightness = 150;
				item.itemImage = Textures.goblet2;
			} else if (item.phaseTime <= 60 * fpsCheck) {
				brightness = 255;
				item.itemImage = Textures.goblet3;
			}

			if (item.phaseTime > 60 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Adrenaline
		case 27:
			item.phaseTime = 0;
			item.itemImage = Textures.adrenaline;
			break;

		// Glasses of vision
		case 28:
			brightness = 0;

			if (item.phaseTime <= 20 * fpsCheck) {
				brightness = 255;
				item.itemImage = Textures.glasses;
			} else if (item.phaseTime <= 40 * fpsCheck) {
				brightness = 150;
				item.itemImage = Textures.glasses;
			}

			if (item.phaseTime > 40 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Torch
		case 29:
			brightness = 255;

			if (item.phaseTime <= 5 * fpsCheck) {
				brightness = 200;
				item.itemImage = Textures.torch1;
			} else if (item.phaseTime <= 10 * fpsCheck) {
				item.itemImage = Textures.torch2;
			} else if (item.phaseTime <= 15 * fpsCheck) {
				brightness = 200;
				item.itemImage = Textures.torch4;
			} else if (item.phaseTime <= 20 * fpsCheck) {
				item.itemImage = Textures.torch3;
			}

			if (item.phaseTime > 20 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Lamp
		case 30:
			brightness = 255;

			if (item.phaseTime <= 5 * fpsCheck) {
				brightness = 175;
				item.itemImage = Textures.lamp3;
			} else if (item.phaseTime <= 10 * fpsCheck) {
				brightness = 200;
				item.itemImage = Textures.lamp2;
			} else if (item.phaseTime <= 15 * fpsCheck) {
				brightness = 255;
				item.itemImage = Textures.lamp1;
			} else if (item.phaseTime <= 20 * fpsCheck) {
				brightness = 200;
				item.itemImage = Textures.lamp2;
			}

			if (item.phaseTime > 20 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Tree
		case 31:
			item.itemImage = Textures.tree;
			item.phaseTime = 0;
			break;

		// Explosive Canister
		case 32:
			item.itemImage = Textures.canister;
			item.phaseTime = 0;
			break;

		// Chainmeal armor
		case 33:
			item.itemImage = Textures.chainmeal;
			item.phaseTime = 0;
			break;

		// Combat armor
		case 34:
			item.itemImage = Textures.combat;
			item.phaseTime = 0;
			break;

		// Argent armor
		case 35:
			item.itemImage = Textures.argent;
			item.phaseTime = 0;
			break;

		// Armor shard
		case 36:
			item.itemImage = Textures.shard;
			item.phaseTime = 0;
			break;

		// Health Vial
		case 37:
			item.itemImage = Textures.vial;
			item.phaseTime = 0;
			break;

		// Weapon upgrade point
		case 38:
			brightness = 255;

			if (item.phaseTime <= 7 * fpsCheck) {
				item.itemImage = Textures.upgrade1;
			} else if (item.phaseTime <= 14 * fpsCheck) {
				item.itemImage = Textures.upgrade2;
			} else if (item.phaseTime <= 21 * fpsCheck) {
				item.itemImage = Textures.upgrade3;
			} else if (item.phaseTime <= 28 * fpsCheck) {
				item.itemImage = Textures.upgrade4;
			}

			if (item.phaseTime > 28 * fpsCheck) {
				item.phaseTime = 0;
			}

			break;

		// Holy Water
		case 39:
			brightness = 255;

			if (item.phaseTime <= 7 * fpsCheck) {
				brightness = 200;

				item.itemImage = Textures.holyWater1;
			} else if (item.phaseTime <= 14 * fpsCheck) {
				item.itemImage = Textures.holyWater2;
			}

			if (item.phaseTime > 14 * fpsCheck) {
				item.phaseTime = 0;
			}

			changesBrightness = true;

			break;

		// Scepter of Deciet
		case 40:
			item.itemImage = Textures.scepter;
			item.phaseTime = 0;
			break;

		// Invisibility Emerald
		case 41:

			if (item.phaseTime <= 2 * fpsCheck) {
				item.itemImage = Textures.invisEmerald;
			} else if (item.phaseTime <= 4 * fpsCheck) {
				item.itemImage = Textures.invisEmerald2;
			} else if (item.phaseTime <= 6 * fpsCheck) {
				item.itemImage = Textures.invisEmerald3;
			} else if (item.phaseTime <= 8 * fpsCheck) {
				item.itemImage = Textures.invisEmerald4;
			}

			if (item.phaseTime > 8 * fpsCheck) {
				item.phaseTime = 0;
			}

			break;

		// Table (empty)
		case 42:
			item.itemImage = Textures.table;
			item.phaseTime = 0;
			break;

		// Candleabra table
		case 43:
			item.itemImage = Textures.lampTable;
			item.phaseTime = 0;
			break;

		// Shotgun box
		case 47:
			item.itemImage = Textures.shellBox;
			item.phaseTime = 0;
			break;

		// Phase cannon
		case 49:
			item.itemImage = Textures.phaseCannon;
			item.phaseTime = 0;
			break;

		// Charge pack
		case 50:
			item.itemImage = Textures.chargePack;
			item.phaseTime = 0;
			break;

		// Large Charge pack
		case 51:
			item.itemImage = Textures.largeChargePack;
			item.phaseTime = 0;
			break;

		// Satillite Link
		case 52:
			brightness = 100;

			if (item.activated) {
				brightness = 255;

				if (item.phaseTime <= 7 * fpsCheck) {
					brightness = 200;

					item.itemImage = Textures.sat1;
				} else if (item.phaseTime <= 14 * fpsCheck) {
					item.itemImage = Textures.sat2;
				}

				if (item.phaseTime > 14 * fpsCheck) {
					item.phaseTime = 0;
				}
			} else {
				item.phaseTime = 0;

				item.itemImage = Textures.sat1;
			}

			changesBrightness = true;

			break;

		// Pistol
		case 55:
			item.itemImage = Textures.pistol;
			item.phaseTime = 0;
			break;

		// Bullet clip
		case 56:
			item.itemImage = Textures.clip;
			item.phaseTime = 0;
			break;

		// Box of bullets
		case 57:
			item.itemImage = Textures.bullets;
			item.phaseTime = 0;
			break;

		// Rocket Launcher
		case 60:
			item.phaseTime = 0;
			item.itemImage = Textures.rocketLaucher;
			break;

		// Rockets
		case 61:
			item.phaseTime = 0;
			item.itemImage = Textures.rockets;
			break;

		// Box of Rockets
		case 62:
			item.phaseTime = 0;
			item.itemImage = Textures.rocketCrate;
			break;

		// Teleporter Enterance
		case 66:

			if (item.phaseTime <= 4 * fpsCheck) {
				item.itemImage = Textures.tEnter1;
			} else if (item.phaseTime <= 8 * fpsCheck) {
				item.itemImage = Textures.tEnter2;
			} else if (item.phaseTime <= 12 * fpsCheck) {
				item.itemImage = Textures.tEnter3;
			} else if (item.phaseTime <= 16 * fpsCheck) {
				item.itemImage = Textures.tEnter4;
			}

			if (item.phaseTime > 16 * fpsCheck) {
				item.phaseTime = 0;
			}

			break;

		// Teleporter Exit
		case 67:

			if (item.phaseTime <= 4 * fpsCheck) {
				item.itemImage = Textures.tExit1;
			} else if (item.phaseTime <= 8 * fpsCheck) {
				item.itemImage = Textures.tExit2;
			} else if (item.phaseTime <= 12 * fpsCheck) {
				item.itemImage = Textures.tExit3;
			} else if (item.phaseTime <= 16 * fpsCheck) {
				item.itemImage = Textures.tExit4;
			}

			if (item.phaseTime > 16 * fpsCheck) {
				item.phaseTime = 0;
			}

			break;

		// Bone pile
		case 69:
			item.phaseTime = 0;
			item.itemImage = Textures.bonePile;
			break;

		// Dark book
		case 71:
			item.phaseTime = 0;
			item.itemImage = Textures.darkBook;
			break;

		// Turret
		case 72:
			item.phaseTime = 0;
			item.itemImage = Textures.turret;
			break;

		// Marine 1
		case 73:
			item.phaseTime = 0;
			item.itemImage = Textures.marine1;
			break;

		// Marine 2
		case 74:
			item.phaseTime = 0;
			item.itemImage = Textures.marine2;
			break;

		// Marine 3
		case 75:
			item.phaseTime = 0;
			item.itemImage = Textures.marine3;
			break;

		// Marine 4
		case 76:
			item.phaseTime = 0;
			item.itemImage = Textures.marine4;
			break;

		// Marine 5
		case 77:
			item.phaseTime = 0;
			item.itemImage = Textures.marine5;
			break;

		// Tech Barrel
		case 78:
			item.phaseTime = 0;
			item.itemImage = Textures.techBarrel;
			break;

		// Tech Pillar 1
		case 79:
			item.phaseTime = 0;
			item.itemImage = Textures.techPillar1;
			break;

		// Tech Pillar 2
		case 80:
			item.phaseTime = 0;
			item.itemImage = Textures.techPillar2;
			break;

		// Tech Pillar 3
		case 81:
			item.phaseTime = 0;
			item.itemImage = Textures.techPillar3;
			break;

		// Toilet
		case 82:
			item.phaseTime = 0;
			item.itemImage = Textures.toilet;
			break;

		// Trash can 1
		case 83:
			item.phaseTime = 0;
			item.itemImage = Textures.trash3;
			break;

		// Trash can 2
		case 84:
			item.phaseTime = 0;
			item.itemImage = Textures.trash1;
			break;

		// Trash can 3
		case 85:
			item.phaseTime = 0;
			item.itemImage = Textures.trash2;
			break;

		// Tree alive
		case 86:
			item.phaseTime = 0;
			item.itemImage = Textures.treeAlive;
			break;

		// Hanging magistrate
		case 87:
			item.phaseTime = 0;
			item.itemImage = Textures.wizardCorpse;
			break;

		// Stalagmite 1
		case 88:
			item.phaseTime = 0;
			item.itemImage = Textures.stalagmite1;
			break;

		// Stalagmite 2
		case 89:
			item.phaseTime = 0;
			item.itemImage = Textures.stalagmite2;
			break;

		// Rock 1
		case 90:
			item.phaseTime = 0;
			item.itemImage = Textures.rock1;
			break;

		// Rock 2
		case 91:
			item.phaseTime = 0;
			item.itemImage = Textures.rock2;
			break;

		// Rock 3
		case 92:
			item.phaseTime = 0;
			item.itemImage = Textures.rock3;
			break;

		// Gore Pile 1
		case 93:
			item.phaseTime = 0;
			item.itemImage = Textures.remains;
			break;

		// Gore Pile 2
		case 94:
			item.phaseTime = 0;
			item.itemImage = Textures.remains2;
			break;

		// Red Pillar
		case 95:
			item.phaseTime = 0;
			item.itemImage = Textures.redPillar;
			break;

		// Pustule
		case 96:
			item.phaseTime = 0;
			item.itemImage = Textures.pustule;
			break;

		// Red Banner
		case 97:
			item.phaseTime = 0;
			item.itemImage = Textures.redBanner;
			break;

		// Eye Pile
		case 98:
			item.phaseTime = 0;
			item.itemImage = Textures.eyePile;
			break;

		// Beer
		case 99:
			item.phaseTime = 0;
			item.itemImage = Textures.beer;
			break;

		// Bible
		case 100:
			item.phaseTime = 0;
			item.itemImage = Textures.bible;
			break;

		// Blue Banner
		case 101:
			item.phaseTime = 0;
			item.itemImage = Textures.blueBanner;
			break;

		// Blue Pillar
		case 102:
			item.phaseTime = 0;
			item.itemImage = Textures.bluePillar;
			break;

		// Bone
		case 103:
			item.phaseTime = 0;
			item.itemImage = Textures.bone;
			break;

		// Burger
		case 104:
			item.phaseTime = 0;
			item.itemImage = Textures.burger;
			break;

		// Burning corpse
		case 105:
			item.phaseTime = 0;
			item.itemImage = Textures.burningCorpse;
			break;

		// Bush 1
		case 106:
			item.phaseTime = 0;
			item.itemImage = Textures.bush1;
			break;

		// Bush 2
		case 107:
			item.phaseTime = 0;
			item.itemImage = Textures.bush2;
			break;

		// Technical lamp 3
		case 108:
			item.phaseTime = 0;
			item.itemImage = Textures.groundLamp3;
			break;

		// Ceiling Lamp
		case 109:
			item.phaseTime = 0;
			item.itemImage = Textures.ceilingLamp;
			break;

		// Chair
		case 110:
			item.phaseTime = 0;
			item.itemImage = Textures.chair;
			break;

		// Place Holder
		case 111:
			item.phaseTime = 0;
			item.itemImage = Textures.placeHolder;
			break;

		// Corpse Pile
		case 112:
			item.phaseTime = 0;
			item.itemImage = Textures.corpsePile;
			break;

		// Tech Canister
		case 113:
			item.phaseTime = 0;
			item.itemImage = Textures.electroPod;
			break;

		// Candelabra 1
		case 114:
			item.phaseTime = 0;
			item.itemImage = Textures.evilCandels1;
			break;

		// Candelabra 2
		case 115:
			item.phaseTime = 0;
			item.itemImage = Textures.evilCandels2;
			break;

		// Candelabra 3
		case 116:
			item.phaseTime = 0;
			item.itemImage = Textures.evilCandels3;
			break;

		// Candelabra 4
		case 117:
			item.phaseTime = 0;
			item.itemImage = Textures.evilCandels4;
			break;

		// Blood Thorns
		case 118:
			item.phaseTime = 0;
			item.itemImage = Textures.hellroot;
			break;

		// Green Pillar
		case 119:
			item.phaseTime = 0;
			item.itemImage = Textures.greenPillar;
			break;

		// Tech Lamp 1
		case 120:
			item.phaseTime = 0;
			item.itemImage = Textures.groundLamp1;
			break;

		// Tech Lamp 2
		case 121:
			item.phaseTime = 0;
			item.itemImage = Textures.groundLamp2;
			break;

		// Stripped Corpse
		case 122:
			item.phaseTime = 0;
			item.itemImage = Textures.carcass;
			break;
		}

		// If brightness is not the default of 255 then the brightness
		// must be changed below
		if (brightness != 255) {
			changesBrightness = true;
		}

		// Corrects for low res setting more pixels every loop
		int correction = 1;

		if (lowRes) {
			correction = 2;
		}

		// item.pixelsOnScreen = new ArrayList<Integer>();

		// boolean xRender = false;

		/*
		 * Performs different operations when looping through the pixels depending on
		 * whether low resolution is turned on or not. This makes the game faster than
		 * having if statements within the double for loops.
		 */
		if (lowRes) {
			for (int yy = yPixL; yy < yPixR; yy += correction) {
				// If it can't render then break
				// if(yy == yPixL + correction && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx += correction) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if (zBuffer[(xx) + (yy) * WIDTH] > rotZ && zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ
								&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
								&& zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ
								|| (zBuffer[(xx) + (yy) * WIDTH] == 0 || zBuffer[(xx) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = item.itemImage.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Adjust brightness if pixel needs to have its
							// brightness changed from 255 to something else
							if (changesBrightness && brightness != 255) {
								color = adjustBrightness(brightness, color, 0);
							}

							// Try to render
							try {
								// Adds to the pixels on screen this enemy takes up
								if (item.itemID == ItemNames.CANISTER.getID()) {
									// item.pixelsOnScreen.add((xx + 1) + (yy) * WIDTH);
									// item.pixelsOnScreen.add((xx + 1) + (yy + 1) * WIDTH);
									// item.pixelsOnScreen.add((xx) + (yy + 1) * WIDTH);
									// item.pixelsOnScreen.add(xx + yy * WIDTH);
								}

								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		} else {
			for (int yy = yPixL; yy < yPixR; yy++) {
				// If it can't render then break
				// if(yy == yPixL + 1 && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx++) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = item.itemImage.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Adjust brightness if pixel needs to have its
							// brightness changed from 255 to something else
							if (changesBrightness && brightness != 255) {
								color = adjustBrightness(brightness, color, 0);
							}

							// Try to render
							try {
								// Adds to the pixels on screen this item takes up
								// if (item.itemID == ItemNames.CANISTER.getID()) {
								// item.pixelsOnScreen.add(xx + yy * WIDTH);
								// }

								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		}

		// TODO FIX THIS
		/*
		 * If off screen, still save the pixels of the enemy in a crude way so that
		 * projectiles can still hit the enemies if you look away from them. Of course
		 * in this method it cannot determine the color of the pixels so it just gets
		 * the entire pixel array instead of only the non see through pixels. It's buggy
		 * but it works.
		 */
		/*
		 * if(!xRender) { xPixL = (int)(xPixelL); xPixR = (int)(xPixelR); yPixL =
		 * (int)(yPixelL); yPixR = (int)(yPixelR);
		 * 
		 * int lesserX = xPixL; int lesserY = yPixL; int greaterX = xPixR; int greaterY
		 * = yPixR;
		 * 
		 * if(xPixR < xPixL) { lesserX = xPixR; greaterX = xPixL; }
		 * 
		 * if(yPixR < yPixL) { lesserY = yPixR; greaterY = yPixL; }
		 * 
		 * for(int i = lesserY; i < greaterY; i++) { for(int j = lesserX; j < greaterX;
		 * j++) { item.pixelsOnScreen.add(i + j * WIDTH); } } }
		 */
	}

	/**
	 * Renders explosion like methods above. It takes too long to comment this stuff
	 * so just look at the renderEnemies method to know what all this stuff does.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param hOffSet
	 * @param explosion
	 */
	public void renderExplosion(double x, double y, double z, double hOffSet, Explosion explosion) {
		double xC = ((x) - Player.x) * 1.9;
		double yC = y + (Player.yCorrect / 11);
		double zC = ((z) - Player.z) * 1.9;

		int spriteSize = 600;

		double rotX = xC * cosine - zC * sine;
		double rotY = yC;
		double rotZ = zC * cosine + xC * sine;

		// If explosion is behind the player don't even try to render it.
		if (rotZ <= 0) {
			return;
		}

		double xCenter = WIDTH / 2;

		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);

		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		if (xPixL < 0) {
			xPixL = 0;
		} else if (xPixL > WIDTH) {
			xPixR = WIDTH;
		}

		if (yPixL < 0) {
			yPixL = 0;
		} else if (yPixL > HEIGHT) {
			yPixR = HEIGHT;
		}

		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		} else if (yPixR < 0) {
			yPixR = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		} else if (xPixR < 0) {
			xPixR = 0;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		rotZ *= 8;

		int imageDimensions = 256;

		int correction = 1;

		if (lowRes) {
			correction = 2;
		}

		// The image of the explosion
		Render image = null;

		double phaseTime = explosion.phaseTime;

		if (explosion.ID == 1) {
			if (phaseTime <= 4 * fpsCheck) {
				image = Textures.canExplode1;
			} else if (phaseTime <= 8 * fpsCheck) {
				image = Textures.canExplode2;
			} else if (phaseTime <= 12 * fpsCheck) {
				image = Textures.canExplode3;
			} else if (phaseTime > 12 * fpsCheck) {
				image = Textures.canExplode4;
			}
		} else {
			if (phaseTime <= 2 * fpsCheck) {
				image = Textures.explosion1;
			} else if (phaseTime <= 4 * fpsCheck) {
				image = Textures.explosion2;
			} else if (phaseTime <= 6 * fpsCheck) {
				image = Textures.explosion3;
			} else if (phaseTime <= 8 * fpsCheck) {
				image = Textures.explosion4;
			} else if (phaseTime <= 10 * fpsCheck) {
				image = Textures.explosion5;
			} else if (phaseTime <= 12 * fpsCheck) {
				image = Textures.explosion6;
			} else if (phaseTime <= 14 * fpsCheck) {
				image = Textures.explosion7;
			} else if (phaseTime > 14 * fpsCheck) {
				image = Textures.explosion8;
			}
		}

		// boolean xRender = false;

		/*
		 * Performs different operations when looping through the pixels depending on
		 * whether low resolution is turned on or not. This makes the game faster than
		 * having if statements within the double for loops.
		 */
		if (lowRes) {
			for (int yy = yPixL; yy < yPixR; yy += correction) {
				// If it can't render then break
				// if(yy == yPixL + correction && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx += correction) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if ((zBuffer[(xx) + (yy) * WIDTH] > rotZ && zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ
								&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
								&& zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ) || (zBuffer[(xx) + (yy) * WIDTH] == 0)) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = image.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Try to render
							try {
								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		} else {
			for (int yy = yPixL; yy < yPixR; yy++) {
				// If it can't render then break
				// if(yy == yPixL + 1 && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx++) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || (zBuffer[(xx) + (yy) * WIDTH] == 0)) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = image.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Try to render
							try {
								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Renders hit sprite like methods above. It takes too long to comment this
	 * stuff so just look at the renderEnemies method to know what all this stuff
	 * does.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param hOffSet
	 * @param explosion
	 */
	public void renderHitSprite(double x, double y, double z, double hOffSet, HitSprite hitSprite) {
		double xC = ((x) - Player.x) * 1.9;
		double yC = y + (Player.yCorrect / 11);
		double zC = ((z) - Player.z) * 1.9;

		int spriteSize = 150;

		// If phase cannon bolt
		if (hitSprite.ID == 2) {
			spriteSize = 600;
		}
		// If fireball or critical hit
		else if (hitSprite.ID == 4 || hitSprite.ID == 6) {
			spriteSize = 300;
		}

		double rotX = xC * cosine - zC * sine;
		double rotY = yC;
		double rotZ = zC * cosine + xC * sine;

		// If the sprite is behind the player, don't even try to render it.
		if (rotZ <= 0) {
			return;
		}

		double xCenter = WIDTH / 2;

		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);

		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		if (xPixL < 0) {
			xPixL = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		}

		if (yPixL < 0) {
			yPixL = 0;
		}

		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		rotZ *= 8;

		int imageDimensions = 256;

		int correction = 1;

		if (lowRes) {
			correction = 2;
		}

		switch (hitSprite.ID) {
		// Pistol and shotgun bullets
		case 0:
		case 1:
			if (hitSprite.phaseTime <= 6) {
				hitSprite.spriteImage = Textures.bulletHit1;
			} else if (hitSprite.phaseTime <= 13) {
				hitSprite.spriteImage = Textures.bulletHit2;
			} else if (hitSprite.phaseTime <= 20) {
				hitSprite.spriteImage = Textures.bulletHit3;
			} else {
				hitSprite.spriteImage = Textures.bulletHit4;
				return; // TODO take return out when fixed
			}

			break;
		// If the Phase Cannon hits something.
		case 2:
			if (hitSprite.phaseTime <= 4) {
				hitSprite.spriteImage = Textures.phaseHit1;
			} else if (hitSprite.phaseTime <= 8) {
				hitSprite.spriteImage = Textures.phaseHit2;
			} else if (hitSprite.phaseTime <= 12) {
				hitSprite.spriteImage = Textures.phaseHit3;
			} else if (hitSprite.phaseTime <= 16) {
				hitSprite.spriteImage = Textures.phaseHit4;
			} else if (hitSprite.phaseTime <= 20) {
				hitSprite.spriteImage = Textures.phaseHit5;
			} else {
				return;
			}

			break;

		// If an enemy is hit
		case 3:
			if (hitSprite.phaseTime <= 5) {
				hitSprite.spriteImage = Textures.bloodSpray1;
			} else if (hitSprite.phaseTime <= 10) {
				hitSprite.spriteImage = Textures.bloodSpray2;
			} else if (hitSprite.phaseTime <= 15) {
				hitSprite.spriteImage = Textures.bloodSpray3;
			} else if (hitSprite.phaseTime <= 20) {
				hitSprite.spriteImage = Textures.bloodSpray4;
			} else {
				return;
			}

			break;

		// If an enemy fireball hits something
		case 4:
			if (hitSprite.phaseTime <= 5) {
				hitSprite.spriteImage = Textures.fireHit1;
			} else if (hitSprite.phaseTime <= 10) {
				hitSprite.spriteImage = Textures.fireHit2;
			} else if (hitSprite.phaseTime <= 15) {
				hitSprite.spriteImage = Textures.fireHit3;
			} else if (hitSprite.phaseTime <= 20) {
				hitSprite.spriteImage = Textures.fireHit4;
			} else {
				return;
			}

			break;

		// Critical hit
		case 6:
			if (hitSprite.phaseTime <= 4) {
				hitSprite.spriteImage = Textures.criticalHit1;
			} else if (hitSprite.phaseTime <= 8) {
				hitSprite.spriteImage = Textures.criticalHit2;
			} else if (hitSprite.phaseTime <= 12) {
				hitSprite.spriteImage = Textures.criticalHit3;
			} else if (hitSprite.phaseTime <= 15) {
				hitSprite.spriteImage = Textures.criticalHit4;
			} else if (hitSprite.phaseTime <= 18) {
				hitSprite.spriteImage = Textures.criticalHit5;
			} else if (hitSprite.phaseTime <= 20) {
				hitSprite.spriteImage = Textures.criticalHit6;
			} else {
				return;
			}

			break;

		// Default bullet disappears
		default:
			if (hitSprite.phaseTime <= 4) {
				hitSprite.spriteImage = Textures.bulletHit1;
			} else if (hitSprite.phaseTime <= 8) {
				hitSprite.spriteImage = Textures.bulletHit2;
			} else if (hitSprite.phaseTime <= 10) {
				hitSprite.spriteImage = Textures.bulletHit3;
			} else {
				hitSprite.spriteImage = Textures.bulletHit4;
				return;
			}

			break;
		}

		// boolean xRender = false;

		/*
		 * Performs different operations when looping through the pixels depending on
		 * whether low resolution is turned on or not. This makes the game faster than
		 * having if statements within the double for loops.
		 */
		if (lowRes) {
			for (int yy = yPixL; yy < yPixR; yy += correction) {
				// If it can't render then break
				// if(yy == yPixL + correction && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx += correction) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if ((zBuffer[(xx) + (yy) * WIDTH] > rotZ && zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ
								&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
								&& zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ)
								|| (zBuffer[(xx) + (yy) * WIDTH] == 0 || zBuffer[(xx) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = hitSprite.spriteImage.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Try to render
							try {
								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		} else {
			for (int yy = yPixL; yy < yPixR; yy++) {
				// If it can't render then break
				// if(yy == yPixL + 1 && !xRender)
				// {
				// break;
				// }

				double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
				int yTexture = (int) (pixelRotationY * imageDimensions);

				for (int xx = xPixL; xx < xPixR; xx++) {
					// xRender = true;

					double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
					int xTexture = (int) (pixelRotationX * imageDimensions);
					boolean seen = false;

					/*
					 * Try to see if the graphics can be drawn on screen or not and if there is a
					 * problem with temp being out of the array index, catch it and continue.
					 */
					try {
						if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
							seen = true;
						}
					} catch (Exception e) {
						continue;
					}

					// If within field of view
					if (seen) {
						int color = 0;

						try {
							color = hitSprite.spriteImage.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != 0xffffffff) {
							// Try to render
							try {
								PIXELS[xx + yy * WIDTH] = color;
								zBuffer[(xx) + (yy) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Render all the corpses and their different phases as they fall to the ground
	 * after being killed. Most of this rendering stuff is the same as renderEnemies
	 * method so just look at the comments there for reference
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param hOffSet
	 * @param corpse
	 */
	public void renderCorpse(double x, double y, double z, double hOffSet, Corpse corpse) {
		int spriteSize = 475;

		// Default corpse size
		if (corpse.enemyID != 0) {
			spriteSize = 512;
		}

		// If it is the corpse of a boss
		if (corpse.enemyID == 6 || corpse.enemyID == 8) {
			spriteSize = 4096;
			y -= 3;
		}

		double xC = ((x) - Player.x) * 1.9;
		double yC = y + (Player.yCorrect / 11);
		double zC = ((z) - Player.z) * 1.9;

		double rotX = xC * cosine - zC * sine;
		double rotY = yC;
		double rotZ = zC * cosine + xC * sine;

		// If the corpse is behind the player don't even try to render it
		if (rotZ <= 0) {
			return;
		}

		double xCenter = WIDTH / 2;

		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);

		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		if (xPixL < 0) {
			xPixL = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		}

		if (yPixL < 0) {
			yPixL = 0;
		}

		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		rotZ *= 8;

		if (corpse.phaseTime > 0) {
			corpse.phaseTime--;
		}

		int imageDimensions = 256;

		int correction = 1;

		// Low res setting
		if (lowRes) {
			correction = 2;
		}

		// Graphics of the happyFace to be rendered
		Render corpseGraphics = null;

		// Depending on corpse, have a different sprite
		// depending on the enemy killed.
		if (corpse.phaseTime == 0) {
			if (corpse.enemyID == 7) {
				corpseGraphics = Textures.corpseType2;
			} else if (corpse.enemyID == 1) {
				corpseGraphics = Textures.enemy1corpse;
			} else if (corpse.enemyID == 2) {
				corpseGraphics = Textures.enemy2corpse;
			} else if (corpse.enemyID == 3) {
				corpseGraphics = Textures.enemy3corpse;
			} else if (corpse.enemyID == 4) {
				corpseGraphics = Textures.enemy4corpse;
			} else if (corpse.enemyID == 5) {
				corpseGraphics = Textures.enemy5corpse;
			} else if (corpse.enemyID == 8) {
				corpseGraphics = Textures.belegothCorpse;
			} else {
				corpseGraphics = corpse.corpseImage;
			}
		}
		// As corpse falls, make its animation look realistic
		else {
			if (corpse.enemyID == 1) {
				if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.enemy1corpse1;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.enemy1corpse2;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.enemy1corpse3;
				}
			} else if (corpse.enemyID == 2) {
				if (corpse.phaseTime >= 21) {
					corpseGraphics = Textures.enemy2corpse1;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.enemy2corpse2;
				} else if (corpse.phaseTime >= 15) {
					corpseGraphics = Textures.enemy2corpse3;
				} else if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.enemy2corpse4;
				} else if (corpse.phaseTime >= 9) {
					corpseGraphics = Textures.enemy2corpse5;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.enemy2corpse6;
				} else if (corpse.phaseTime >= 3) {
					corpseGraphics = Textures.enemy2corpse;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.enemy2corpse;
				}
			} else if (corpse.enemyID == 3) {
				if (corpse.phaseTime >= 21) {
					corpseGraphics = Textures.enemy3corpse1;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.enemy3corpse2;
				} else if (corpse.phaseTime >= 15) {
					corpseGraphics = Textures.enemy3corpse3;
				} else if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.enemy3corpse4;
				} else if (corpse.phaseTime >= 9) {
					corpseGraphics = Textures.enemy3corpse5;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.enemy3corpse6;
				} else if (corpse.phaseTime >= 3) {
					corpseGraphics = Textures.enemy3corpse7;
				} else if (corpse.phaseTime >= 2) {
					corpseGraphics = Textures.enemy3corpse8;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.enemy3corpse9;
				}
			} else if (corpse.enemyID == 4) {
				if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.enemy4corpse1;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.enemy4corpse2;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.enemy4corpse3;
				}
			} else if (corpse.enemyID == 5) {
				if (corpse.phaseTime >= 22) {
					corpseGraphics = Textures.enemy5corpse1;
				} else if (corpse.phaseTime >= 20) {
					corpseGraphics = Textures.enemy5corpse2;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.enemy5corpse3;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.enemy5corpse4;
				} else if (corpse.phaseTime >= 16) {
					corpseGraphics = Textures.enemy5corpse5;
				} else if (corpse.phaseTime >= 14) {
					corpseGraphics = Textures.enemy5corpse6;
				} else if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.enemy5corpse7;
				} else if (corpse.phaseTime >= 10) {
					corpseGraphics = Textures.enemy5corpse8;
				} else if (corpse.phaseTime >= 8) {
					corpseGraphics = Textures.enemy5corpse9;
				} else if (corpse.phaseTime >= 7) {
					corpseGraphics = Textures.enemy5corpse10;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.enemy5corpse11;
				} else if (corpse.phaseTime >= 5) {
					corpseGraphics = Textures.enemy5corpse12;
				} else if (corpse.phaseTime >= 4) {
					corpseGraphics = Textures.enemy5corpse13;
				} else if (corpse.phaseTime >= 3) {
					corpseGraphics = Textures.enemy5corpse14;
				} else if (corpse.phaseTime >= 2) {
					corpseGraphics = Textures.enemy5corpse15;
				} else if (corpse.phaseTime >= 1) {
					corpseGraphics = Textures.enemy5corpse16;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.enemy5corpse17;
				}
			} else if (corpse.enemyID == 8) {
				if (corpse.phaseTime >= 41) {
					corpseGraphics = Textures.belegothCorpse1;
				} else if (corpse.phaseTime >= 37) {
					corpseGraphics = Textures.belegothCorpse2;
				} else if (corpse.phaseTime >= 33) {
					corpseGraphics = Textures.belegothCorpse3;
				} else if (corpse.phaseTime >= 29) {
					corpseGraphics = Textures.belegothCorpse4;
				} else if (corpse.phaseTime >= 25) {
					corpseGraphics = Textures.belegothCorpse5;
				} else if (corpse.phaseTime >= 21) {
					corpseGraphics = Textures.belegothCorpse6;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.belegothCorpse7;
				} else if (corpse.phaseTime >= 15) {
					corpseGraphics = Textures.belegothCorpse8;
				} else if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.belegothCorpse9;
				} else if (corpse.phaseTime >= 9) {
					corpseGraphics = Textures.belegothCorpse10;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.belegothCorpse11;
				} else if (corpse.phaseTime >= 3) {
					corpseGraphics = Textures.belegothCorpse12;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.belegothCorpse13;
				}
			} else {
				if (corpse.phaseTime >= 21) {
					corpseGraphics = Textures.corpse1;
				} else if (corpse.phaseTime >= 18) {
					corpseGraphics = Textures.corpse2;
				} else if (corpse.phaseTime >= 15) {
					corpseGraphics = Textures.corpse3;
				} else if (corpse.phaseTime >= 12) {
					corpseGraphics = Textures.corpse4;
				} else if (corpse.phaseTime >= 9) {
					corpseGraphics = Textures.corpse5;
				} else if (corpse.phaseTime >= 6) {
					corpseGraphics = Textures.corpse6;
				} else if (corpse.phaseTime >= 3) {
					corpseGraphics = Textures.corpse7;
				} else if (corpse.phaseTime >= 0) {
					corpseGraphics = Textures.corpse8;
				}
			}
		}

		// If its a player corpse
		if (corpse.enemyID == 0 && corpse.clientID != -1) {
			corpseGraphics = Textures.playerCorpse;
		}

		if (lowRes) {
			correction = 2;
		}

		// boolean xRender = false;

		/*
		 * Performs different operations when looping through the pixels depending on
		 * whether low resolution is turned on or not. This makes the game faster than
		 * having if statements within the double for loops.
		 */
		for (int yy = yPixL; yy < yPixR; yy += correction) {
			// If it can't be rendered
			// if(yy == yPixL + correction && !xRender)
			// {
			// break;
			// }

			double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) (pixelRotationY * imageDimensions);

			for (int xx = xPixL; xx < xPixR; xx += correction) {
				// xRender = true;

				double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) (pixelRotationX * imageDimensions);
				boolean seen = true;

				/*
				 * Try to see if the graphics can be drawn on screen or not and if there is a
				 * problem with temp being out of the array index, catch it and continue.
				 */
				try {
					if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
						if (correction > 1
								&& (zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ
										&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
										&& zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ)
								|| (zBuffer[(xx) + (yy + 1) * WIDTH] == 0 || zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
							seen = true;
						} else if (correction > 1) {
							seen = false;
						}
					} else {
						seen = false;
					}
				} catch (Exception e) {
					continue;
				}

				// If within field of view
				if (seen) {
					int color = 0;

					try {
						color = corpseGraphics.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];

						if (color != 0xffffffff && corpse.clientID >= 1) {
							if (corpse.clientID == 1) {
								color = changeColor(255, 255, 255, color, 8, 0, 16);
							} else if (corpse.clientID == 2) {
								color = changeColor(60, 255, 255, color, 16, 8, 0);
							} else {
								color = changeColor(255, 255, 255, color, 0, 16, 8);
							}
						}
					} catch (Exception e) {
						continue;
					}

					/*
					 * If color is not white, render it, otherwise don't to have transparency around
					 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
					 * green, 4th two are blue. ff is 255.
					 */
					if (color != 0xffffffff) {
						// Try to render
						try {
							PIXELS[xx + yy * WIDTH] = color;
							zBuffer[(xx) + (yy) * WIDTH] = rotZ;

							if (correction > 1) {
								PIXELS[(xx + 1) + (yy) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
							}
						} catch (Exception e) {
							continue;
						}
					}
				}

				// Corpses are transparent in smile mode
				if (Display.smileMode) {
					xx += correction * 2;
				}
			}
		}
	}

	/**
	 * Renders bullets the same way as the enemies so look up there for comments on
	 * how this all works.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param hOffSet
	 */
	public void renderProjectiles(double x, double y, double z, double hOffSet, int ID, Projectile proj) {
		// Corrects the way it looks like the projectile is moving for the player
		// because for some reason before it was acting weird. Again, it is only
		// for the projectiles that the player shot (sourceEnemy is null)
		double newy = y;
		if (proj.upRotation > 0 && proj.sourceEnemy == null) {
			newy = Math.abs(y);
		}

		double xC = (x - Player.x) * 1.9;
		double yC = newy + (Player.yCorrect / 11);
		double zC = (z - Player.z) * 1.9;

		double rotX = xC * cosine - zC * sine;
		double rotY = yC;
		double rotZ = zC * cosine + xC * sine;

		// Don't even try to render the projectile if it is behind the player.
		if (rotZ <= 0) {
			return;
		}

		int spriteSize = 16;

		if (ID >= 3) {
			spriteSize = 128;
		}

		double xCenter = WIDTH / 2;

		double xPixel = ((rotX / rotZ) * HEIGHT) + xCenter;
		double yPixel = ((rotY / rotZ) * HEIGHT) + (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate));

		double xPixelL = xPixel - (spriteSize / rotZ);
		double xPixelR = xPixel + (spriteSize / rotZ);

		double yPixelL = yPixel - (spriteSize / rotZ);
		double yPixelR = yPixel + (spriteSize / rotZ);

		int xPixL = (int) (xPixelL);
		int xPixR = (int) (xPixelR);
		int yPixL = (int) (yPixelL);
		int yPixR = (int) (yPixelR);

		if (xPixL < 0) {
			xPixL = 0;
		}

		if (xPixR > WIDTH) {
			xPixR = WIDTH;
		}

		if (yPixL < 0) {
			yPixL = 0;
		}

		if (yPixR > HEIGHT) {
			yPixR = HEIGHT;
		}

		// If the image is not on the screen. Return and do not render.
		if (Math.abs(xPixL - xPixR) == 0) {
			return;
		}

		rotZ *= 8;

		int correction = 1;

		// If low res setting
		if (lowRes) {
			correction = 2;
		}

		// The image being rendered in the form of a render object
		Render type;

		// Different image for different projectile types
		if (ID == 0) {
			type = Textures.bullet;
		} else if (ID == 1) {
			type = Textures.bullet;
		} else if (ID == 2) {
			type = Textures.phaser;
		} else if (ID == 3) {
			type = Textures.rocket;
		} else if (ID == 4) {
			type = Textures.defaultFireball;
		} else if (ID == 5) {
			type = Textures.electroBall;
		} else if (ID == 6) {
			type = Textures.giantFireball;
		} else {
			type = Textures.electricShock;
		}

		// proj.pixelsOnScreen = new ArrayList<Integer>();

		// boolean xRender = false;

		for (int yy = yPixL; yy < yPixR; yy += correction) {
			// If it can't be rendered then break
			// if(yy == yPixL + correction && !xRender)
			// {
			// break;
			// }

			double pixelRotationY = (yy - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) (pixelRotationY * 256);

			for (int xx = xPixL; xx < xPixR; xx += correction) {
				// Can be rendered
				// xRender = true;

				double pixelRotationX = (xx - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) (pixelRotationX * 256);
				boolean seen = false;

				/*
				 * Try to see if the graphics can be drawn on screen or not and if there is a
				 * problem with temp being out of the array index, catch it and continue.
				 */
				try {
					if (zBuffer[(xx) + (yy) * WIDTH] > rotZ || zBuffer[(xx) + (yy) * WIDTH] == 0) {
						seen = true;
					}

					if (lowRes) {
						if (seen && (zBuffer[(xx + 1) + (yy) * WIDTH] > rotZ
								&& zBuffer[(xx + 1) + (yy + 1) * WIDTH] > rotZ
								&& zBuffer[(xx) + (yy + 1) * WIDTH] > rotZ)
								|| (zBuffer[(xx) + (yy + 1) * WIDTH] == 0 || zBuffer[(xx + 1) + (yy + 1) * WIDTH] == 0
										|| zBuffer[(xx + 1) + (yy) * WIDTH] == 0)) {
							seen = true;
						} else {
							seen = false;
						}
					}
				} catch (Exception e) {
					continue;
				}

				if (seen) {
					int color = 0;

					try {
						color = type.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];
					} catch (Exception e) {
						continue;
					}

					/*
					 * If color is not white, render it, otherwise don't to have transparency around
					 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
					 * green, 4th two are blue. ff is 255.
					 */
					if (color != 0xffffffff) {
						// Try to render
						try {
							PIXELS[xx + yy * WIDTH] = color;
							zBuffer[(xx) + (yy) * WIDTH] = rotZ;

							// proj.pixelsOnScreen.add((xx) + yy * WIDTH);

							// If low res setting on
							if (lowRes) {
								// proj.pixelsOnScreen.add((xx + 1) + yy * WIDTH);
								// proj.pixelsOnScreen.add(xx + (yy + 1) * WIDTH);
								// proj.pixelsOnScreen.add((xx + 1) + (yy + 1) * WIDTH);

								PIXELS[(xx + 1) + (yy) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy) * WIDTH] = rotZ;
								PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx + 1) + (yy + 1) * WIDTH] = rotZ;
								PIXELS[(xx) + (yy + 1) * WIDTH] = color;
								zBuffer[(xx) + (yy + 1) * WIDTH] = rotZ;
							}
						} catch (Exception e) {
							continue;
						}
					}
				}
			}
		}

		// TODO FIX
		/*
		 * If off screen, still save the pixels of the enemy in a crude way so that
		 * projectiles can still hit the enemies if you look away from them. Of course
		 * in this method it cannot determine the color of the pixels so it just gets
		 * the entire pixel array instead of only the non see through pixels. It's buggy
		 * but it works.
		 */
		/*
		 * if(!xRender) { xPixL = (int)(xPixelL); xPixR = (int)(xPixelR); yPixL =
		 * (int)(yPixelL); yPixR = (int)(yPixelR);
		 * 
		 * int lesserX = xPixL; int lesserY = yPixL; int greaterX = xPixR; int greaterY
		 * = yPixR;
		 * 
		 * if(xPixR < xPixL) { lesserX = xPixR; greaterX = xPixL; }
		 * 
		 * if(yPixR < yPixL) { lesserY = yPixR; greaterY = yPixL; }
		 * 
		 * for(int i = lesserY; i < greaterY; i++) { for(int j = lesserX; j < greaterX;
		 * j++) { proj.pixelsOnScreen.add(i + j * WIDTH); } } }
		 */
	}

	/**
	 * Renders the walls depending on where the blocks in the map are, and the
	 * players movements
	 * 
	 * @param xLeft
	 * @param xRight
	 * @param yHeight
	 * @param zDepthLeft
	 * @param zDepthRight
	 * @param wallHeight
	 */
	public void render3DWalls(double xLeft, double xRight, double yHeight, double zDepthLeft, double zDepthRight,
			double wallHeight, int wallID, Block block) {
		// It was too much work to indent everything, so please forgive the
		// non indentation of everything in this try block.
		try {
			/*
			 * Corrects it so that the left side of the wall does not look like it is moving
			 * as the player moves. It is supposed to look as if it is staying put in the
			 * same place. You should test this. Make those 2's ones and you'll see just how
			 * much a little change will screw everything up.
			 */
			double xCLeft = ((xLeft) - (rightSpeed * rightCorrect)) * 2;
			double zCLeft = ((zDepthLeft) - (fowardSpeed * forwardCorrect)) * 2;

			/*
			 * Rotate the Left side x and z (up to a full circle if needed) using both
			 * cosine and sine in opposite ways for both equations to get the effect of a
			 * circle. This allows for the wall to rotate with the player yet look like its
			 * staying in the same place. You can test this effect if you want. If you set
			 * xCLeft to 0 here or anything really, the wall well allow the right side to
			 * rotate correctly but the left side of every wall will do weird crap like
			 * stretch out to infinity and stuff. This basically just makes it so that it
			 * will turn towards the player the more the player turns away from that side to
			 * make it seem like it is staying in the same place.
			 */
			double rotLeftSideX = (xCLeft * cosine - zCLeft * sine);
			double rotLeftSideZ = (zCLeft * cosine + xCLeft * sine);

			/*
			 * Moves corners of wall (or box I guess you could say) in correlation to your y
			 * direction (If you jump or crouch). So that they seemingly stay in the same
			 * positions as well.
			 */
			double yCornerTopLeft = ((-yHeight - ((0.5 / 12) * wallHeight)) + (Player.yCorrect * 0.06)) * 100;
			double yCornerBottomLeft = ((0.5 - yHeight) + (Player.yCorrect * 0.06)) * 100;

			/*
			 * Corrects it so that the right side of the wall does not look like it is
			 * moving as the player moves. It is supposed to look as if it is staying put in
			 * the same place. You should test this. Make those 2's ones and you'll see just
			 * how much a little change will screw everything up.
			 */
			double xCRight = ((xRight) - (rightSpeed * rightCorrect)) * 2;
			double zCRight = ((zDepthRight) - (fowardSpeed * forwardCorrect)) * 2;

			/*
			 * Rotate the Right side x and z (up to a full circle if needed) using both
			 * cosine and sine in opposite ways for both equations to get the effect of a
			 * circle. This allows for the wall to rotate with the player yet look like its
			 * staying in the same place. You can test this effect if you want. If you set
			 * xCRight to xCLeft here or anything really, the wall well allow the left side
			 * to rotate correctly but the right side of every wall will do weird crap like
			 * strech out to infinity and stuff. This basically just makes it so that it
			 * will turn towards the player the more the player turns away from that side to
			 * make it seem like it is staying in the same place.
			 */
			double rotRightSideX = (xCRight * cosine - zCRight * sine);
			double rotRightSideZ = (zCRight * cosine + xCRight * sine);

			// If wall is behind player, don't even continue to try to render it.
			if (rotLeftSideZ <= 0 && rotRightSideZ <= 0) {
				return;
			}

			// The radius the wall will clip out of
			double clip = 13;

			/*
			 * Uses cohen sutherland theroem to clip off any textures outside of the box
			 * created by the walls. If this wasn't used the wall textures may stretch out
			 * to infinity.
			 */
			if (rotLeftSideZ <= clip) {
				double temp = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
				rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * temp;
				rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * temp;
			}

			// Used for both sides
			if (rotRightSideZ <= clip) {
				double temp = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
				rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * temp;
				rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * temp;
			}

			/*
			 * Moves corners of wall (or box I guess you could say) in correlation to your y
			 * direction (If you jump or crouch). So that they seemingly stay in the same
			 * positions as well.
			 */
			double yCornerTopRight = ((-yHeight - ((0.5 / 12) * wallHeight)) + (Player.yCorrect * 0.06)) * 100;
			double yCornerBottomRight = ((0.5 - yHeight) + (Player.yCorrect * 0.06)) * 100;

			// All i know is it helps with making the pixels seem like they
			// rotate when the wall rotates. They rotate along with the wall
			// basically.
			double text4Modifier = 256;

			/*
			 * Calculates the vectors from the center (The players position in respect to
			 * the wall) and places them at each end of the wall to define the range in
			 * which the textures will be rendered. The width of the wall per say.
			 */
			double xPixelLeft = ((rotLeftSideX / rotLeftSideZ) * HEIGHT + (WIDTH / 2));
			double xPixelRight = ((rotRightSideX / rotRightSideZ) * HEIGHT + (WIDTH / 2));

			// Make those into ints for the for loops
			int xPixelLeftInt = (int) (xPixelLeft);
			int xPixelRightInt = (int) (xPixelRight);

			/*
			 * Don't render things off the screen
			 */
			if (xPixelLeftInt < 0) {
				xPixelLeftInt = 0;
			}

			/*
			 * Don't render things off the screen
			 */
			if (xPixelLeftInt > WIDTH) {
				xPixelLeftInt = WIDTH;
			}

			/*
			 * Don't render things off the screen
			 */
			if (xPixelRightInt < 0) {
				xPixelRightInt = 0;
			}

			/*
			 * Dont render things off the screen
			 */
			if (xPixelRightInt > WIDTH) {
				xPixelRightInt = WIDTH;
			}

			// If the image is not on the screen. Return and do not render.
			if (Math.abs(xPixelLeftInt - xPixelRightInt) == 0) {
				return;
			}

			/*
			 * //TODO for testing Don't render things off the screen
			 * 
			 * if(xPixelLeft < 0) { xPixelLeft += 100; }
			 * 
			 * /* Don't render things off the screen
			 * 
			 * if(xPixelLeft > WIDTH) { xPixelLeft -= 100; }
			 * 
			 * /* Don't render things off the screen
			 * 
			 * if(xPixelRight < 0) { xPixelRight += 100; }
			 * 
			 * /* Dont render things off the screen
			 * 
			 * if(xPixelRight > WIDTH) { xPixelRight -= 100; }
			 * 
			 * /* Sets up vectors to all 4 corners of the wall to define the edges of the
			 * wall to be rendered no matter where the player is looking up or down. For
			 * instance if yPixelTop left was lower, the wall would have the left side be
			 * lower and tilt upward towards the right side which wouldn't look right, so
			 * this makes the textures stay as a square and stretch as far as looks
			 * realistic in the y direction
			 */
			double yPixelTopLeft = (yCornerTopLeft / rotLeftSideZ * HEIGHT
					+ (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate)));
			double yPixelBottomLeft = (yCornerBottomLeft / rotLeftSideZ * HEIGHT
					+ (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate)));
			double yPixelTopRight = (yCornerTopRight / rotRightSideZ * HEIGHT
					+ (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate)));
			double yPixelBottomRight = (yCornerBottomRight / rotRightSideZ * HEIGHT
					+ (HEIGHT / Math.sin(Player.upRotate) * Math.cos(Player.upRotate)));

			// Allows the textures to rotate with the wall.
			double texture1 = 1 / rotLeftSideZ;
			double texture2 = 1 / rotRightSideZ;

			// TODO check into texture4
			double texture4 = text4Modifier / (rotRightSideZ);

			// Walls phase is always updated.
			block.wallPhase++;

			// Brightness of each wall
			int brightness = 255;

			// Does wall stay at full brightness or does it change
			boolean changesBrightness = false;

			// Figures out what wall texture to render beforehand
			switch (wallID) {
			// Dungeon Bricks
			case 1:
				block.wallImage = Textures.wall1;

				block.wallPhase = 0;
				break;

			// Futuristic Wall
			case 2:
				block.wallImage = Textures.wall2;

				block.wallPhase = 0;
				break;

			// Dark bricks
			case 3:
				block.wallImage = Textures.wall3;

				block.wallPhase = 0;
				break;

			// Glass
			case 4:
				block.wallImage = Textures.wall4;

				// If glass is damaged, it has different textures
				if (block.health <= 10) {
					block.wallImage = Textures.wall4damaged3;
				} else if (block.health <= 30) {
					block.wallImage = Textures.wall4damaged2;
				} else if (block.health <= 50) {
					block.wallImage = Textures.wall4damaged1;
				}

				block.wallPhase = 0;
				break;

			// Computer Wall
			case 5:
				block.wallImage = Textures.wall5;

				block.wallPhase = 0;
				break;

			// End Button
			case 6:
				block.wallImage = Textures.wall6;

				block.wallPhase = 0;
				break;

			// Normal door
			case 7:
				block.wallImage = Textures.wall7;

				block.wallPhase = 0;
				break;

			// Elevator
			case 8:
				block.wallImage = Textures.wall8;

				block.wallPhase = 0;
				break;

			// Red key door
			case 9:
				block.wallImage = Textures.wall9;

				block.wallPhase = 0;
				break;

			// Blue key door
			case 10:
				block.wallImage = Textures.wall10;

				block.wallPhase = 0;
				break;

			// Green key door
			case 11:
				block.wallImage = Textures.wall11;

				block.wallPhase = 0;
				break;

			// Yellow key door
			case 12:
				block.wallImage = Textures.wall12;

				block.wallPhase = 0;
				break;

			// Moving computer graphic wall
			case 13:
				if (block.wallPhase < 50 * fpsCheck) {
					block.wallImage = Textures.wall13Phase1;
				} else {
					block.wallImage = Textures.wall13Phase2;
				}

				if (block.wallPhase > 100 * fpsCheck) {
					block.wallPhase = 0;
				}

				break;

			// Secret found wall
			case 14:
				block.wallImage = Textures.wall14;

				block.wallPhase = 0;
				break;

			// Electric wall
			case 15:
				brightness = 0;

				if (block.wallPhase < 10 * fpsCheck) {
					block.wallImage = Textures.wall15Phase1;

					brightness = 25;
				} else if (block.wallPhase < 20 * fpsCheck) {
					block.wallImage = Textures.wall15Phase2;

					brightness = 75;
				} else if (block.wallPhase < 30 * fpsCheck) {
					block.wallImage = Textures.wall15Phase3;

					brightness = 150;
				} else if (block.wallPhase < 40 * fpsCheck) {
					block.wallImage = Textures.wall15Phase4;

					brightness = 200;
				} else if (block.wallPhase < 50 * fpsCheck) {
					block.wallImage = Textures.wall15Phase5;

					brightness = 255;
				} else if (block.wallPhase < 60 * fpsCheck) {
					block.wallImage = Textures.wall15Phase4;

					brightness = 200;
				} else if (block.wallPhase < 70 * fpsCheck) {
					block.wallImage = Textures.wall15Phase3;

					brightness = 150;
				} else if (block.wallPhase < 80 * fpsCheck) {
					block.wallImage = Textures.wall15Phase2;

					brightness = 75;
				} else if (block.wallPhase <= 90 * fpsCheck) {
					block.wallImage = Textures.wall15Phase1;

					brightness = 25;
				}

				if (block.wallPhase > 90 * fpsCheck) {
					block.wallPhase = 0;
				}

				changesBrightness = true;

				break;

			// Various liquid walls
			case 16:
			case 17:
			case 25:
				brightness = 0;

				if (block.wallPhase < 10 * fpsCheck) {
					block.wallImage = Textures.toxic1;

					brightness = 150;
				} else if (block.wallPhase < 20 * fpsCheck) {
					block.wallImage = Textures.toxic2;

					brightness = 175;
				} else if (block.wallPhase < 30 * fpsCheck) {
					block.wallImage = Textures.toxic3;

					brightness = 200;
				} else if (block.wallPhase < 40 * fpsCheck) {
					block.wallImage = Textures.toxic4;

					brightness = 225;
				} else if (block.wallPhase < 50 * fpsCheck) {
					block.wallImage = Textures.toxic5;

					brightness = 255;
				} else if (block.wallPhase < 60 * fpsCheck) {
					block.wallImage = Textures.toxic6;

					brightness = 225;
				} else if (block.wallPhase < 70 * fpsCheck) {
					block.wallImage = Textures.toxic7;

					brightness = 200;
				} else if (block.wallPhase < 80 * fpsCheck) {
					block.wallImage = Textures.toxic8;

					brightness = 175;
				} else if (block.wallPhase < 90 * fpsCheck) {
					block.wallImage = Textures.toxic9;

					brightness = 150;
				} else if (block.wallPhase < 100 * fpsCheck) {
					block.wallImage = Textures.toxic10;

					brightness = 175;
				} else if (block.wallPhase < 110 * fpsCheck) {
					block.wallImage = Textures.toxic11;

					brightness = 200;
				} else if (block.wallPhase < 120 * fpsCheck) {
					block.wallImage = Textures.toxic12;

					brightness = 225;
				} else if (block.wallPhase < 130 * fpsCheck) {
					block.wallImage = Textures.toxic13;

					brightness = 255;
				} else if (block.wallPhase < 140 * fpsCheck) {
					block.wallImage = Textures.toxic14;

					brightness = 225;
				} else if (block.wallPhase < 150 * fpsCheck) {
					block.wallImage = Textures.toxic15;

					brightness = 200;
				} else if (block.wallPhase < 161 * fpsCheck) {
					block.wallImage = Textures.toxic16;

					brightness = 175;
				} else {
					block.wallPhase = 0;
				}

				changesBrightness = true;

				break;

			// Spine Wall
			case 18:
				if (block.wallPhase < 10 * fpsCheck) {
					block.wallImage = Textures.spine1;
				} else if (block.wallPhase < 20 * fpsCheck) {
					block.wallImage = Textures.spine2;
				} else if (block.wallPhase < 30 * fpsCheck) {
					block.wallImage = Textures.spine3;
				} else if (block.wallPhase < 40 * fpsCheck) {
					block.wallImage = Textures.spine4;
				} else if (block.wallPhase < 50 * fpsCheck) {
					block.wallImage = Textures.spine5;
				} else if (block.wallPhase < 60 * fpsCheck) {
					block.wallImage = Textures.spine6;
				} else if (block.wallPhase < 70 * fpsCheck) {
					block.wallImage = Textures.spine7;
				} else if (block.wallPhase < 80 * fpsCheck) {
					block.wallImage = Textures.spine8;
				} else {
					block.wallPhase = 0;
				}

				break;

			// Dead electric wall
			case 19:
				brightness = 25;
				block.wallImage = Textures.wall15Phase1;
				block.wallPhase = 0;

				break;

			// MLG wall
			case 20:
				block.wallImage = Textures.mlg;
				block.wallPhase = 0;
				break;

			// Box wall
			case 21:
				block.wallImage = Textures.box;
				block.wallPhase = 0;
				break;

			// Wood wall
			case 22:
				block.wallImage = Textures.woodenWall;
				block.wallPhase = 0;
				break;

			// Wall with picture on it
			case 23:
				block.wallImage = Textures.bloodWall;
				block.wallPhase = 0;
				break;

			// Marble wall
			case 24:
				block.wallImage = Textures.marble;
				block.wallPhase = 0;
				break;

			// Normal Button
			case 26:
				block.wallImage = Textures.normButton;
				block.wallPhase = 0;
				break;

			// Cool molten rock texture
			case 27:
				block.wallImage = Textures.coolWall;

				block.wallPhase = 0;

				break;

			// Teleporter entrance
			case 28:
				block.wallImage = Textures.teleportEnter;

				block.wallPhase = 0;

				break;

			// Teleporter exit
			case 29:
				block.wallImage = Textures.teleportExit;

				block.wallPhase = 0;

				break;

			// Tutorial Wall
			case 30:
				block.wallImage = Textures.tutorialWall;

				block.wallPhase = 0;

				break;

			// Tutorial Wall 2
			case 31:
				block.wallImage = Textures.tutorialWall2;

				block.wallPhase = 0;

				break;

			// Tutorial Wall 3
			case 32:
				block.wallImage = Textures.tutorialWall3;

				block.wallPhase = 0;

				break;

			// Tutorial Wall 4
			case 33:
				block.wallImage = Textures.tutorialWall4;

				block.wallPhase = 0;

				break;

			// Tutorial Wall 5
			case 34:
				block.wallImage = Textures.tutorialWall5;

				block.wallPhase = 0;

				break;

			// Wall 35
			case 35:
				block.wallImage = Textures.wall35;

				block.wallPhase = 0;

				break;

			// Wall 36
			case 36:
				block.wallImage = Textures.wall36;

				block.wallPhase = 0;

				break;

			// Wall 37
			case 37:
				block.wallImage = Textures.wall37;

				block.wallPhase = 0;

				break;

			// Wall 38
			case 38:
				block.wallImage = Textures.wall38;

				block.wallPhase = 0;

				break;

			// Wall 39
			case 39:
				block.wallImage = Textures.wall39;

				block.wallPhase = 0;

				break;

			// Wall 40
			case 40:
				block.wallImage = Textures.wall40;

				block.wallPhase = 0;

				break;

			// Wall 41
			case 41:
				block.wallImage = Textures.wall41;

				block.wallPhase = 0;

				break;

			// Wall 42
			case 42:
				if (block.wallPhase < 25 * fpsCheck) {
					block.wallImage = Textures.wall42a;
				} else if (block.wallPhase < 50 * fpsCheck) {
					block.wallImage = Textures.wall42b;
				} else if (block.wallPhase < 75 * fpsCheck) {
					block.wallImage = Textures.wall42c;
				} else if (block.wallPhase < 100 * fpsCheck) {
					block.wallImage = Textures.wall42d;
				} else {
					block.wallPhase = 0;
				}

				break;

			// Wall 43
			case 43:
				block.wallImage = Textures.normButtonOn;

				block.wallPhase = 0;

				break;

			// Default texture
			default:
				block.wallImage = Textures.coolWall;

				block.wallPhase = 0;

				break;
			}

			// If not a brightness of 255, it'll need to be changed
			if (brightness != 255) {
				changesBrightness = true;
			}

			// How it iterates through the for loop. By 1s or 2s
			int correction = 1;

			// Color of current picture
			int color = 0;

			// Does something with textures. I don't yet understand this
			double zWall = (texture1 + (texture2 - texture1));

			// boolean yRender = false;

			if (lowRes) {
				correction = 2;

				/*
				 * While the pixels being rendered are still within the bounds of the wall (The
				 * width of the wall)
				 */
				for (int x = xPixelLeftInt; x < xPixelRightInt; x += correction) {
					// If there is nothing to render then break
					// if(x == xPixelLeftInt + correction && !yRender)
					// {
					// break;
					// }

					/*
					 * How much the pixels are to rotate depending on your movement . Your movement
					 * is tracked by the change in xPixelLeft and xPixelRight from frame to frame.
					 */
					double pixelRotation = (x - xPixelLeft) / (xPixelLeft - xPixelRight);

					// double pixelRotationTest = (x - xPixelLeft) /
					// (xPixelRight - xPixelLeft);

					// TODO check this out
					// Figures out the texture that needs to be rendered
					int xTexture = (int) (((texture4) * (pixelRotation)) / (zWall));

					/*
					 * Figures out where the top pixel and bottom pixel are located on the screen.
					 */
					double yPixelTop = yPixelTopLeft + (yPixelTopLeft - yPixelTopRight) * pixelRotation;

					double yPixelBottom = yPixelBottomLeft + (yPixelBottomLeft - yPixelBottomRight) * pixelRotation;

					/*
					 * Casts them into ints to be drawn to the screen
					 */
					int yPixelTopInt = (int) (yPixelTop);
					int yPixelBottomInt = (int) (yPixelBottom);

					/*
					 * If the wall goes out of the top of the frame, reduce it so that it stays in
					 * the frame.
					 */
					if (yPixelTopInt < 0) {
						yPixelTopInt = 0;
					}

					/*
					 * If the wall goes below the frame, make it so that it still stays in the
					 * frame.
					 */
					if (yPixelTopInt > HEIGHT) {
						yPixelTopInt = HEIGHT;
					}

					/*
					 * If the wall goes below the frame, make it so that it still stays in the
					 * frame.
					 */
					if (yPixelBottomInt > HEIGHT) {
						yPixelBottomInt = HEIGHT;
					}

					/*
					 * If the wall goes below the frame, make it so that it still stays in the
					 * frame.
					 */
					if (yPixelBottomInt < 0) {
						yPixelBottomInt = 0;
					}

					/*
					 * If the top is farther down than the bottom of the wall, then don't render
					 * that maddness
					 */
					if (yPixelTopInt > yPixelBottomInt) {
						return;
					}

					/*
					 * For each y pixel from the top of the wall to the bottom, render the pixels
					 * correctly depending on how you rotate upward (looking up). Also depending on
					 * the walls ID, change the texture of the wall to be so.
					 */
					for (int y = yPixelTopInt; y < yPixelBottomInt; y += correction) {
						// yRender = true;

						// Figures out how the pixel should be stretched or look like
						// in the y direction
						double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);

						int yTexture = (int) (255 * pixelRotationY);
						double rotZ = (0.4 / (texture1 - (texture2 - texture1) * pixelRotation)) * 0.3;

						try {
							// If wall is behind another wall, break out of the
							// loop here because it shouldn't be able to be seen
							// behind another wall.
							if ((zBufferWall[(x) + (y) * WIDTH] > zWall && zBufferWall[(x) + (y + 1) * WIDTH] > zWall
									&& zBufferWall[(x + 1) + (y + 1) * WIDTH] > zWall
									&& zBufferWall[(x + 1) + (y) * WIDTH] > zWall)
									|| (zBuffer[(x) + (y) * WIDTH] < rotZ && zBuffer[(x) + (y + 1) * WIDTH] < rotZ
											&& zBuffer[(x + 1) + (y + 1) * WIDTH] < rotZ
											&& zBuffer[(x + 1) + (y) * WIDTH] < rotZ && zBuffer[(x) + (y) * WIDTH] != 0
											&& zBuffer[(x) + (y + 1) * WIDTH] != 0
											&& zBuffer[(x + 1) + (y + 1) * WIDTH] != 0
											&& zBuffer[(x + 1) + (y) * WIDTH] != 0)) {
								continue;
							}

							// Set color of pixel to draw to the color of the pixel
							// it correlates to in the image file
							color = block.wallImage.PIXELS[((xTexture) & 255) + (yTexture & 255) * 256];

						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != seeThroughWallPixel) {
							// Change brightness of pixel if needed
							if (changesBrightness) {
								color = adjustBrightness(brightness, color, wallID);
							}

							// Try to render
							try {
								PIXELS[x + y * WIDTH] = color;
								zBuffer[(x) + (y) * WIDTH] = rotZ;
								PIXELS[(x + 1) + (y) * WIDTH] = color;
								zBuffer[(x + 1) + (y) * WIDTH] = rotZ;
								PIXELS[(x + 1) + (y + 1) * WIDTH] = color;
								zBuffer[(x + 1) + (y + 1) * WIDTH] = rotZ;
								PIXELS[(x) + (y + 1) * WIDTH] = color;
								zBuffer[(x) + (y + 1) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}

							// Set this pixel to being the front most wall pixel
							zBufferWall[(x) + (y) * WIDTH] = zWall;

							try {
								zBufferWall[(x) + (y + 1) * WIDTH] = zWall;
								zBufferWall[(x + 1) + (y + 1) * WIDTH] = zWall;
								zBufferWall[(x + 1) + (y) * WIDTH] = zWall;
							} catch (Exception e) {
								continue;
							}
						}

						/*
						 * Always render the last couple of pixels to ensure that the edges of the walls
						 * are always rendered no matter what because if they aren't... weird things
						 * occur.
						 */
						if (x >= xPixelRightInt - 6) {
							correction = 1;
						}
					}
				}
			} else {
				/*
				 * While the pixels being rendered are still within the bounds of the wall (The
				 * width of the wall)
				 */
				for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
					// If there's nothing to render then break
					// if(x == xPixelLeftInt + 1 && !yRender)
					// {
					// break;
					// }

					/*
					 * How much the pixels are to rotate depending on your movement . Your movement
					 * is tracked by the change in xPixelLeft and xPixelRight from frame to frame.
					 */
					double pixelRotation = (x - xPixelLeft) / (xPixelLeft - xPixelRight);

					// Figures out the texture that needs to be rendered
					int xTexture = (int) (((texture4) * (pixelRotation)) / zWall);

					/*
					 * Figures out where the top pixel and bottom pixel are located on the screen.
					 */
					double yPixelTop = yPixelTopLeft + (yPixelTopLeft - yPixelTopRight) * pixelRotation;
					double yPixelBottom = yPixelBottomLeft + (yPixelBottomLeft - yPixelBottomRight) * pixelRotation;

					/*
					 * Casts them into ints to be drawn to the screen
					 */
					int yPixelTopInt = (int) (yPixelTop);
					int yPixelBottomInt = (int) (yPixelBottom);

					/*
					 * If the wall goes out of the top of the frame, reduce it so that it stays in
					 * the frame.
					 */
					if (yPixelTopInt < 0) {
						yPixelTopInt = 0;
					}

					/*
					 * If the wall goes below the frame, make it so that it still stays in the
					 * frame.
					 */
					if (yPixelBottomInt > HEIGHT) {
						yPixelBottomInt = HEIGHT;
					}

					/*
					 * If the top is farther down than the bottom of the wall, then don't render
					 * that maddness
					 */
					if (yPixelTopInt > yPixelBottomInt) {
						return;
					}

					/*
					 * For each y pixel from the top of the wall to the bottom, render the pixels
					 * correctly depending on how you rotate upward (looking up). Also depending on
					 * the walls ID, change the texture of the wall to be so.
					 */
					for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
						// yRender = true;

						// Figures out how the pixel should be stretched or look like
						// in the y direction
						double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
						int yTexture = (int) (256 * pixelRotationY);
						double rotZ = (0.4 / (texture1 - (texture2 - texture1) * pixelRotation)) * 0.3;

						try {
							// If wall is behind another wall, break out of the
							// loops here because it shouldn't be able to be seen
							// behind another wall.
							if (zBufferWall[(x) + (y) * WIDTH] > zWall
									|| (zBuffer[(x) + (y) * WIDTH] < rotZ && zBuffer[(x) + (y) * WIDTH] != 0)) {
								continue;
							}

							// Set color of pixel to draw to the color of the pixel
							// it correlates to in the image file
							color = block.wallImage.PIXELS[(xTexture & 255) + (yTexture & 255) * 256];

							/*
							 * int r = (color >> 16) & 255; int g = (color >> 8) & 255; int b = color & 255;
							 * 
							 * if (r == 255 && g == 255 && b == 255) { // Do nothing } else { int
							 * realBrightness = (int) (rotZ - (255 - brightness));
							 * 
							 * if (realBrightness < 0) { realBrightness = 0; }
							 * 
							 * int bright = (int) (renderDistance / (realBrightness));
							 * 
							 * if (bright > 255) { bright = 255; }
							 * 
							 * r = (r * bright) / 255; g = (g * bright) / 255; b = (b * bright) / 255;
							 * 
							 * if (Player.alive && Player.playerHurt == 0) { color = r << 16 | g << 8 | b <<
							 * 0;
							 * 
							 * int ePT = Player.environProtectionTime;
							 * 
							 * if ((ePT < 100 * fpsCheck && ePT % 5 == 0 && ePT > 0)) { color = r << 8 | g
							 * << 8 | b << 8; }
							 * 
							 * } else { if (Display.smileMode) { color = r | g | b; } else { color = r << 16
							 * | g << 16 | b << 16; } } }
							 */

							// TODO Try this again in the future maybe?

						} catch (Exception e) {
							continue;
						}

						/*
						 * If color is not white, render it, otherwise don't to have transparency around
						 * your image. First two ff's are the images alpha, 2nd two are red, 3rd two are
						 * green, 4th two are blue. ff is 255.
						 */
						if (color != seeThroughWallPixel) {
							// Change brightness of pixel if needed
							if (changesBrightness) {
								color = adjustBrightness(brightness, color, wallID);
							}

							// Try to render
							try {
								PIXELS[x + y * WIDTH] = color;
								zBuffer[(x) + (y) * WIDTH] = rotZ;
							} catch (Exception e) {
								continue;
							}

							// Set this pixel to being the front most wall pixel
							zBufferWall[(x) + (y) * WIDTH] = zWall;
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Corrects the wall textures so they don't cause each other to clip into each
	 * other and disappear, and also renders them correctly so all 4 corners of each
	 * wall are seen correctly and with the correct width and height of textures.
	 * 
	 * @param block
	 */
	public void renderWallsCorrect(Block block) {
		currentWallID = block.wallID;

		// Corrects wall rendering for player so size looks normal.
		// And so the player doesn't clip through the textures.
		double test = 63.92;

		Block eastBlock = Level.getBlock(block.x + 1, block.z);
		Block southBlock = Level.getBlock(block.x, block.z + 1);
		Block westBlock = Level.getBlock(block.x - 1, block.z);
		Block northBlock = Level.getBlock(block.x, block.z - 1);

		double renderHeight = block.height;
		double baseOf = block.height + (block.y * 4);
		/*
		 * Based on where the graphics end, the baseOf the block (basically where
		 * entities and the player can stand on the top of the block) is set to see
		 * realistic with the graphics.
		 */
		baseOf += (int) (((block.height) + (block.y * 4)) / 2.5) * 6;

		/*
		 * Corrects the height of the wall that is rendered if the blocks height is less
		 * than 12. This is only because when the height is less than 12, the wall does
		 * not render the correct height in accordance to the player. For example when
		 * the wall height is 0, for some reason it will render as if it was at height 6
		 * and etc...
		 */
		if (block.height < 3) {
			renderHeight = (12 - ((12 - block.height) * 2));
		} else {
			double correction = ((int) (block.height / 2) - 3);

			if (block.height < 22) {
				correction *= 0.006;
			} else {
				double adder = ((int) ((block.height - 22) / 11)) * 0.0005;
				correction *= ((0.006) + adder);
			}

			renderHeight = (12 - ((12 - block.height) * 2)) - ((0.45 - correction) * (block.height - 2));
		}
		// else
		// {
		// renderHeight += (block.height * 0.5);
		// renderHeight = ((4 - (0.017 * 5)) * ((block.height - 6) / 2.0)) -
		// ((block.height - 6) * 0.5);
		// }

		block.hCorrect = renderHeight;
		block.baseCorrect = baseOf / 12;

		// Don't allow base correct to go below 0
		if (block.baseCorrect < 0) {
			block.baseCorrect = 0;
		}

		double yCorrect = 3;

		// If the blocks y is greater than 0 it takes more correction
		if (block.y > 0) {
			// All blocks 12 and above height for some reason actually
			// all work with the same yCorrect value... it's weird
			if (block.height >= 12) {
				yCorrect = 4;
			} else {
				/*
				 * Correction based off of how far the wall graphics are off of the ground.
				 */
				double baseLevel = (block.y * 4) - ((block.hCorrect - block.height));

				// Correct how we see the walls height off the ground graphically
				yCorrect = 4 - (0.017 * (int) (baseLevel / 2));

				/*
				 * Corrects the yCorrect further based on the blocks height. These graphical
				 * things take a lot of correction don't they?
				 */
				switch ((int) block.height) {
				case 1:
					yCorrect += -0.02;
					break;

				case 3:
					yCorrect += 0.02;
					break;

				case 4:
					yCorrect += 0.04;
					break;

				case 5:
					yCorrect += 0.063;
					break;

				case 6:
					yCorrect += 0.089;
					break;

				case 7:
					yCorrect += 0.201;
					break;

				case 8:
					yCorrect += 0.228;
					break;

				case 9:
					yCorrect += 0.257;
					break;

				case 10:
					yCorrect += 0.288;
					break;

				case 11:
					yCorrect += 0.32;
					break;
				}
			}
		}

		// IF the block being checked is solid
		if (block.isSolid) {
			/*
			 * If the block on the east side is not the same or greater height or same y
			 * value as the block currently being rendered.
			 * 
			 * Also if the current block is not seeThrough, but the east block is, render
			 * this block still.
			 */
			if (eastBlock.height < block.height || eastBlock.y != block.y
					|| eastBlock.seeThrough && !block.seeThrough) {
				if (Math.sin(Player.rotation) > 0) {
					render3DWalls((block.x + 1) * test, (block.x + 1) * test, block.y / yCorrect, block.z * test,
							(block.z + 0.99999) * test, renderHeight, block.wallID, block);
				} else {
					render3DWalls((block.x + 1.00001) * test, (block.x + 1.00001) * test, block.y / yCorrect,
							block.z * test, (block.z + 0.99999) * test, renderHeight, block.wallID, block);
				}
			}

			/*
			 * Same as the east block but on the south side (positive z)
			 */
			if (southBlock.height < block.height || southBlock.y != block.y
					|| southBlock.seeThrough && !block.seeThrough) {
				if (Math.cos(Player.rotation) < 0) {
					render3DWalls((block.x + 1) * test, (block.x + 0.00002) * test, block.y / yCorrect,
							(block.z + 1) * test, (block.z + 1) * test, renderHeight, block.wallID, block);
				} else {
					render3DWalls((block.x + 1) * test, (block.x + 0.00002) * test, block.y / yCorrect,
							(block.z + 1) * test, (block.z + 1) * test, renderHeight, block.wallID, block);
				}
			}

			/*
			 * Same as others but in the north direction (negative z)
			 */
			if (northBlock.height < block.height || northBlock.y != block.y
					|| northBlock.seeThrough && !block.seeThrough) {
				/*
				 * Fixes a weird graphical glitch that happens for some reason.
				 */
				if (Math.sin(Player.rotation) > 0) {
					render3DWalls(block.x * test, (block.x + 0.99999) * test, block.y / yCorrect, (block.z) * test,
							(block.z) * test, renderHeight, block.wallID, block);
				} else {
					render3DWalls(block.x * test, (block.x + 1) * test, block.y / yCorrect, (block.z) * test,
							(block.z) * test, renderHeight, block.wallID, block);
				}
			}

			/*
			 * Same as others but in west (Negative x) direction.
			 */
			if (westBlock.height < block.height || westBlock.y != block.y
					|| westBlock.seeThrough && !block.seeThrough) {
				render3DWalls((block.x) * test, (block.x) * test, block.y / yCorrect, (block.z + 1) * test,
						(block.z + 0.00001) * test, renderHeight, block.wallID, block);

			}
		}
	}

	/**
	 * Adjusts the brightness of a texture based on the brightness sent to this
	 * method, and the color (in integer form) of the texture.
	 * 
	 * WallID is only used for walls so that lava can be the toxic waste texture
	 * shifted to red. If an item calls this method, it will send in 0 for wallID.
	 * 
	 * @param brightness
	 * @param color
	 * @param wallID
	 * @return
	 */
	public int adjustBrightness(int brightness, int color, int wallID) {
		// Certain walls change brightness, but if they are at
		// full brightness, don't go through this whole method
		if (brightness == 255 && wallID != 17 && wallID != 25) {
			return color;
		}

		/*
		 * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff. The 255 is not
		 * technically needed as it just causes the number to stay the same, but it does
		 * matter for the int b for some reason. I think because it causes the render
		 * distance to fade to blue if not. The shifting of the ints causes the color to
		 * change.
		 * 
		 * Converts the bit value of the color into an int so that it can be easily
		 * tampered with
		 */
		int r = (color >> 16) & 255;
		int g = (color >> 8) & 255;
		int b = color & 255;

		/*
		 * Divides that value by 255, then multiplies it by the brightness level of the
		 * pixel to determine how bright the reds, greens, and blues in each pixel will
		 * get.
		 */
		r = (r * brightness) / 255;
		g = (g * brightness) / 255;
		b = (b * brightness) / 255;

		if (wallID == 17) {
			color = r << 16 | g << 16 | b << 16;
		} else if (wallID == 25) {
			color = r | g | b;
		}
		// Set colors back to their original components
		else {
			// Set colors back to their original components
			// If not white
			if (color != 0xffffffff) {
				color = r << 16 | g << 8 | b;
			}
		}

		// Return new color with new brightness
		return color;
	}

	/**
	 * Shifts the colors of a given sprite by a certain amount. Used primarily on
	 * other players if playing on a server.
	 * 
	 * @param brightness
	 * @param color
	 * @param redShift
	 * @param greenShift
	 * @param blueShift
	 * @return
	 */
	// TODO Still gotta fix this method.
	public int changeColor(int redBrightness, int greenBrightness, int blueBrightness, int color, int redShift,
			int greenShift, int blueShift) {
		/*
		 * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff. The 255 is not
		 * technically needed as it just causes the number to stay the same, but it does
		 * matter for the int b for some reason. I think because it causes the render
		 * distance to fade to blue if not. The shifting of the ints causes the color to
		 * change.
		 * 
		 * Converts the bit value of the color into an int so that it can be easily
		 * tampered with
		 */
		int r = (color >> 16) & 255;
		int g = (color >> 8) & 255;
		int b = color & 255;

		/*
		 * Divides that value by 255, then multiplies it by the brightness level of the
		 * pixel to determine how bright the reds, greens, and blues in each pixel will
		 * get.
		 */
		r = (r * redBrightness) / 255;
		g = (g * greenBrightness) / 255;
		b = (b * blueBrightness) / 255;

		color = r << redShift | g << greenShift | b << blueShift;

		// Return new color with new brightness
		return color;
	}

	/**
	 * Creates the effect of objects getting darker as you look into the distance of
	 * the map.
	 */
	public void renderDistanceLimiter() {
		int skip = 6;

		// Go through all the pixels on the screen in series of 6
		for (int i = 0; i < (WIDTH * HEIGHT); i += skip) {
			// Color value of this pixel in integer form
			int color = PIXELS[i];
			int color1 = 0;
			int color2 = 0;
			int color3 = 0;
			int color4 = 0;
			int color5 = 0;

			// Brightness of color. 255 is Full brightness
			int brightness = 255;
			int brightness1 = 255;
			int brightness2 = 255;
			int brightness3 = 255;
			int brightness4 = 255;
			int brightness5 = 255;

			int j = Player.vision;

			/*
			 * If player is nearing the end of immortality, or is not immortal.
			 */
			if (j < 100 * fpsCheck && j % 5 == 0) {
				/*
				 * The brightness of each pixel depending on its distance from the player, and
				 * the render Distance
				 */
				brightness = (int) (renderDistance / (zBuffer[i]));
				brightness1 = (int) (renderDistance / (zBuffer[i + 1]));
				brightness2 = (int) (renderDistance / (zBuffer[i + 2]));
				brightness3 = (int) (renderDistance / (zBuffer[i + 3]));
				brightness4 = (int) (renderDistance / (zBuffer[i + 4]));
				brightness5 = (int) (renderDistance / (zBuffer[i + 5]));
			}
			// If the player has full vision
			else {
				brightness = 255;
				brightness1 = 255;
				brightness2 = 255;
				brightness3 = 255;
				brightness4 = 255;
				brightness5 = 255;
			}

			// Never can be less than 0 brightness
			if (brightness < 0) {
				brightness = 0;
			}

			color1 = PIXELS[i + 1];
			color2 = PIXELS[i + 2];
			color3 = PIXELS[i + 3];
			color4 = PIXELS[i + 4];
			color5 = PIXELS[i + 5];

			// Can never be brighter than 255
			if (brightness1 > 255) {
				brightness1 = 255;
			}
			// Can never be brighter than 255
			if (brightness2 > 255) {
				brightness2 = 255;
			}
			// Can never be brighter than 255
			if (brightness3 > 255) {
				brightness3 = 255;
			}
			// Can never be brighter than 255
			if (brightness4 > 255) {
				brightness4 = 255;
			}
			// Can never be brighter than 255
			if (brightness5 > 255) {
				brightness5 = 255;
			}

			// Can never be brighter than 255
			if (brightness > 255) {
				brightness = 255;
			}

			/*
			 * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff. The 255 is not
			 * technically needed as it just causes the number to stay the same, but it does
			 * matter for the int b for some reason. I think because it causes the render
			 * distance to fade to blue if not. The shifting of the ints causes the color to
			 * change.
			 * 
			 * Converts the bit value of the color into an int so that it can be easily
			 * tampered with
			 */
			int r = (color >> 16) & 255;
			int g = (color >> 8) & 255;
			int b = color & 255;
			int r1 = 0;
			int g1 = 0;
			int b1 = 0;
			int r2 = 0;
			int g2 = 0;
			int b2 = 0;
			int r3 = 0;
			int g3 = 0;
			int b3 = 0;
			int r4 = 0;
			int g4 = 0;
			int b4 = 0;
			int r5 = 0;
			int g5 = 0;
			int b5 = 0;

			/*
			 * Divides that value by 255, then multiplies it by the brightness level of the
			 * pixel to determine how bright the reds, greens, and blues in each pixel will
			 * get. Getting drunk causes the players vision to get darker and a little bit
			 * distorted.
			 */
			if (Player.drunkLevels > 500) {
				int redLess = brightness;
				int greenLess = brightness;
				int blueLess = brightness;

				if (Player.drunkLevels > 4000) {
					redLess -= 255;
					greenLess -= 255;
					blueLess -= 255;
				} else if (Player.drunkLevels > 3500) {
					redLess -= 235;
					greenLess -= 220;
					blueLess -= 255;
				} else if (Player.drunkLevels > 3000) {
					redLess -= 180;
					greenLess -= 200;
					blueLess -= 145;
				} else if (Player.drunkLevels > 2500) {
					redLess -= 145;
					greenLess -= 90;
					blueLess -= 115;
				} else if (Player.drunkLevels > 2000) {
					redLess -= 90;
					greenLess -= 60;
					blueLess -= 45;
				} else if (Player.drunkLevels > 1500) {
					redLess -= 15;
					greenLess -= 45;
					blueLess -= 30;
				} else if (Player.drunkLevels > 1000) {
					redLess -= 10;
					greenLess -= 30;
					blueLess -= 5;
				} else {
					redLess -= 7;
					greenLess -= 3;
					blueLess -= 8;
				}

				if (redLess < 0) {
					redLess = 0;
				}

				if (greenLess < 0) {
					greenLess = 0;
				}

				if (blueLess < 0) {
					blueLess = 0;
				}

				r = (r * redLess) / 255;
				g = (g * greenLess) / 255;
				b = (b * blueLess) / 255;
				r1 = (color1 >> 16) & 255;
				g1 = (color1 >> 8) & 255;
				b1 = color1 & 255;
				r2 = (color2 >> 16) & 255;
				g2 = (color2 >> 8) & 255;
				b2 = color2 & 255;
				r3 = (color3 >> 16) & 255;
				g3 = (color3 >> 8) & 255;
				b3 = color3 & 255;
				r4 = (color4 >> 16) & 255;
				g4 = (color4 >> 8) & 255;
				b4 = color4 & 255;
				r5 = (color5 >> 16) & 255;
				g5 = (color5 >> 8) & 255;
				b5 = color5 & 255;
				r1 = (r1 * redLess) / 255;
				g1 = (g1 * greenLess) / 255;
				b1 = (b1 * blueLess) / 255;
				r2 = (r2 * redLess) / 255;
				g2 = (g2 * greenLess) / 255;
				b2 = (b2 * blueLess) / 255;
				r3 = (r3 * redLess) / 255;
				g3 = (g3 * greenLess) / 255;
				b3 = (b3 * blueLess) / 255;
				r4 = (r4 * redLess) / 255;
				g4 = (g4 * greenLess) / 255;
				b4 = (b4 * blueLess) / 255;
				r5 = (r5 * redLess) / 255;
				g5 = (g5 * greenLess) / 255;
				b5 = (b5 * blueLess) / 255;
			} else {
				r = (r * brightness) / 255;
				g = (g * brightness) / 255;
				b = (b * brightness) / 255;
				r1 = (color1 >> 16) & 255;
				g1 = (color1 >> 8) & 255;
				b1 = color1 & 255;
				r2 = (color2 >> 16) & 255;
				g2 = (color2 >> 8) & 255;
				b2 = color2 & 255;
				r3 = (color3 >> 16) & 255;
				g3 = (color3 >> 8) & 255;
				b3 = color3 & 255;
				r4 = (color4 >> 16) & 255;
				g4 = (color4 >> 8) & 255;
				b4 = color4 & 255;
				r5 = (color5 >> 16) & 255;
				g5 = (color5 >> 8) & 255;
				b5 = color5 & 255;
				r1 = (r1 * brightness1) / 255;
				g1 = (g1 * brightness1) / 255;
				b1 = (b1 * brightness1) / 255;
				r2 = (r2 * brightness2) / 255;
				g2 = (g2 * brightness2) / 255;
				b2 = (b2 * brightness2) / 255;
				r3 = (r3 * brightness3) / 255;
				g3 = (g3 * brightness3) / 255;
				b3 = (b3 * brightness3) / 255;
				r4 = (r4 * brightness4) / 255;
				g4 = (g4 * brightness4) / 255;
				b4 = (b4 * brightness4) / 255;
				r5 = (r5 * brightness5) / 255;
				g5 = (g5 * brightness5) / 255;
				b5 = (b5 * brightness5) / 255;
			}

			// Reset the bits of that particular pixel
			if (Player.alive && Player.playerHurt == 0) {
				int ePT = Player.environProtectionTime;

				if ((ePT < 100 * fpsCheck && ePT % 5 == 0)) {
					PIXELS[i] = r << 16 | g << 8 | b;
					PIXELS[i + 1] = r1 << 16 | g1 << 8 | b1;
					PIXELS[i + 2] = r2 << 16 | g2 << 8 | b2;
					PIXELS[i + 3] = r3 << 16 | g3 << 8 | b3;
					PIXELS[i + 4] = r4 << 16 | g4 << 8 | b4;
					PIXELS[i + 5] = r5 << 16 | g5 << 8 | b5;
				} else {
					PIXELS[i] = r << 8 | g << 8 | b << 8;
					PIXELS[i + 1] = r1 << 8 | g1 << 8 | b1 << 8;
					PIXELS[i + 2] = r2 << 8 | g2 << 8 | b2 << 8;
					PIXELS[i + 3] = r3 << 8 | g3 << 8 | b3 << 8;
					PIXELS[i + 4] = r4 << 8 | g4 << 8 | b4 << 8;
					PIXELS[i + 5] = r5 << 8 | g5 << 8 | b5 << 8;
				}
			} else {
				if (Display.smileMode) {
					PIXELS[i] = r | g | b;
					PIXELS[i + 1] = r1 | g1 | b1;
					PIXELS[i + 2] = r2 | g2 | b2;
					PIXELS[i + 3] = r3 | g3 | b3;
					PIXELS[i + 4] = r4 | g4 | b4;
					PIXELS[i + 5] = r5 | g5 | b5;
				} else {
					PIXELS[i] = r << 16 | g << 16 | b << 16;
					PIXELS[i + 1] = r1 << 16 | g1 << 16 | b1 << 16;
					PIXELS[i + 2] = r2 << 16 | g2 << 16 | b2 << 16;
					PIXELS[i + 3] = r3 << 16 | g3 << 16 | b3 << 16;
					PIXELS[i + 4] = r4 << 16 | g4 << 16 | b4 << 16;
					PIXELS[i + 5] = r5 << 16 | g5 << 16 | b5 << 16;
				}
			}

			if (i + 6 > WIDTH * HEIGHT) {
				skip = 1;
			}
		}
	}

	/**
	 * May use a special render method in the future, but for now just render all
	 * the blocks.
	 * 
	 * @param index
	 */
	public void renderBlocks() {
		for (Block block : Level.blocks) {
			renderWallsCorrect(block);
		}
	}
}
