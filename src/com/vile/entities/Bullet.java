package com.vile.entities;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.launcher.FPSLauncher;

/**
 * @Title Bullet
 * @author Alex Byrd Date Updated: 5/10/2017
 * 
 *         Description: Creates a bullet object that extends the projectile
 *         class and is used to move a bullet in accordance with the direction
 *         the player is looking and speed of the bullet.
 *
 */
public class Bullet extends Projectile {
	/**
	 * Creates the bullet Projectile object.
	 * 
	 * @param damage
	 * @param speed
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 * @param rotation
	 */
	public Bullet(int damage, double speed, double x, double y, double z, int ID, double rotation,
			boolean criticalHit) {
		super(damage, speed, x, y, z, ID, criticalHit);

		// Bullets clientID is the ID of the player that shot it
		super.clientID = Player.ID;

		/*
		 * Corrects Player.rotation so that the bullet is centered on the screen to the
		 * player. Don't know why its so specific but it is
		 */
		double correction = 44.768;

		// Sets rotation so the bullet will continue to move in
		// a certain direction
		rotation = rotation - correction;

		// Calculates changes needed in x and z values for this rotation
		xa = ((Math.cos(rotation)) + (Math.sin(rotation))) * speed;
		za = ((Math.cos(rotation)) - (Math.sin(rotation))) * speed;

		/*
		 * Correct bullet for upRotation as best as possible, though it cannot be
		 * perfect, try your best to make it perfect.
		 */
		double angle = 1.105 - Player.upRotate + 0.1;

		// Default angle correction
		double angleChanger = 3;

		/*
		 * For some reason different screen sizes change everything... haven't yet
		 * figured this out, but this corrects bullets for a full screen.
		 */
		if (FPSLauncher.resolutionChoice >= 4) {
			// System.out.println(Player.upRotate);
			angleChanger = 0.5;

			if (Player.upRotate <= 0.31) {
				angleChanger = 12;
			} else if (Player.upRotate < 0.35) {
				angleChanger = 5.4;
			} else if (Player.upRotate < 0.4) {
				angleChanger = 5;
			} else if (Player.upRotate < 0.44) {
				angleChanger = 4.8;
			} else if (Player.upRotate < 0.47) {
				angleChanger = 4.45;
			} else if (Player.upRotate < 0.49) {
				angleChanger = 4.3;
			} else if (Player.upRotate < 0.51) {
				angleChanger = 4.2;
			} else if (Player.upRotate < 0.535) {
				angleChanger = 4.1;
			} else if (Player.upRotate < 0.57) {
				angleChanger = 4;
			} else if (Player.upRotate < 0.6) {
				angleChanger = 3.9;
			} else if (Player.upRotate < 0.62) {
				angleChanger = 3.65;
			} else if (Player.upRotate < 0.65) {
				angleChanger = 3.6;
			} else if (Player.upRotate < 0.67) {
				angleChanger = 3.5;
			} else if (Player.upRotate < 0.695) {
				angleChanger = 3.4;
			} else if (Player.upRotate < 0.72) {
				angleChanger = 3.3;
			} else if (Player.upRotate < 0.75) {
				angleChanger = 3.2;
			} else if (Player.upRotate < 0.77) {
				angleChanger = 3.1;
			} else if (Player.upRotate < 0.795) {
				angleChanger = 3;
			} else if (Player.upRotate < 0.82) {
				angleChanger = 2.9;
			} else if (Player.upRotate < 0.85) {
				angleChanger = 2.85;
			} else if (Player.upRotate < 0.87) {
				angleChanger = 2.75;
			} else if (Player.upRotate < 0.91) {
				angleChanger = 2.55;
			} else if (Player.upRotate < 0.95) {
				angleChanger = 2.3;
			} else if (Player.upRotate < 1) {
				angleChanger = 2.05;
			} else if (Player.upRotate < 1.05) {
				angleChanger = 1.5;
			} else if (Player.upRotate < 1.07) {
				angleChanger = 1;
			} else if (Player.upRotate < 1.085) {
				angleChanger = 0.65;
			} else if (Player.upRotate < 1.1) {
				angleChanger = 0.4;
			} else if (Player.upRotate < 1.15) {
				angleChanger = 0;
			} else if (Player.upRotate >= 1.15) {
				angleChanger = 3;
			} else if (Player.upRotate > 1.2) {
				angleChanger = 3;
			}
		}
		// Corrects bullets for a smaller screen
		else {
			if (Player.upRotate < 0.44) {
				angleChanger = 3 + 2.25;
			} else if (Player.upRotate < 0.57) {
				angleChanger = 3 + 1.75;
			} else if (Player.upRotate < 0.67) {
				angleChanger = 3 + 1.25;
			} else if (Player.upRotate < 0.77) {
				angleChanger = 3 + 1;
			} else if (Player.upRotate < 0.87) {
				angleChanger = 3 + 0.5;
			}
		}

		/*
		 * Sets bullet upRotation so that the bullet will continue the same direction
		 * upward.
		 */
		upRotation = -(speed * Math.tan(angle)) * angleChanger;

		// If this is a client, add this bullet to the bulletsAdded arraylist so that it
		// may be added to the server and ticked there.
		if (Display.gameType == 1) {
			Game.bulletsAdded.add(this);
		}
	}

	/**
	 * Moves the bullet depending on where the player was looking when the bullet
	 * was shot, and the speed of the bullet.
	 */
	public boolean move() {
		/*
		 * Adds to bullets y position in such a way that it almost seems that the bullet
		 * is going straight from your crosshair, but for some reason I cannot get it
		 * exact.
		 */
		y += upRotation;

		// Checks to make sure bullet can move and move it if it can
		// move and play sound if it is a rocket
		if (isFree(x + xa, z) && isFree(x, z + za)) {
			x += xa;
			z += za;

			// If a rocket, continue playing rocket sound
			if (this.ID == 3) {
				if (!SoundController.rocketFly.isStillActive()) {
					SoundController.rocketFly.playAudioFile(distanceFromPlayer);
				}
			}
		} else {
			return false;
		}

		return true;
	}
}
