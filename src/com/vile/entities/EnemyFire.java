package com.vile.entities;

import com.vile.Display;
import com.vile.launcher.FPSLauncher;

/**
 * @Title EnemyFire
 * @author Alex Byrd Date Updated: 5/02/2017
 * 
 *         Description: Creates a enemy projectile object that extends the
 *         projectile class and is used to move that projectile in accordance
 *         with the direction the enemies target is.
 *
 */
public class EnemyFire extends Projectile {
	// Its angle on the x and z plane from the 0 direction
	public double rotation = 0;

	// Angle it is fired upward or downward
	private double upRotation = 0;

	// How much it moves up each movement
	public double upMovement = 0;

	/**
	 * Creates a new Projectile object that is an enemy projectile
	 * 
	 * @param damage
	 * @param speed
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 * @param targetX
	 * @param targetZ
	 * @param targetY
	 */
	public EnemyFire(int damage, double speed, double x, double y, double z, int ID, double targetX, double targetZ,
			double targetY, double rotChange, Entity source, boolean criticalHit) {
		// Creates the projectile object
		super(damage, speed, x, (y / 10), z, ID, criticalHit);

		targetY = -targetY;

		// If easy mode
		if (FPSLauncher.modeChoice == 1) {
			speed *= 0.5;
			damage *= 0.5;
		}
		// If Bring it on mode
		else if (FPSLauncher.modeChoice == 3) {
			speed *= 2;
			damage *= 2;
		}
		// Death cannot hurt me mode.
		else if (FPSLauncher.modeChoice == 4) {
			speed *= 3;
			damage *= 3;
		}
		// If peaceful mode
		else if (FPSLauncher.modeChoice == 0) {
			speed = 0;
			damage = 0;
		}

		// The enemy that was the source of the Projectile
		sourceEnemy = source;

		double hypot = 0;

		// Finds the hypotenuse of the triangle in the x and z plane
		// between the target and the enemy
		hypot = Math.sqrt(((x - targetX) * (x - targetX)) + ((z - targetZ) * (z - targetZ)));

		// Hypotenuse in the y direction between the enemy and the target
		// Currently not needed, just here for future reference
		// double upHypot = Math.sqrt((hypot * hypot) +
		// ((y - targetY) * (y - targetY)));

		// Used to correct angle for faster fps's as it acts weird when
		// the game is super fast or slow
		double angleChanger = 2;

		// None of this makes sense as it will not work as intended.
		// Have to do this to make it work. Only for player
		if (this.sourceEnemy.targetEnemy == null) {
			targetY = -targetY;
		} else {
			targetY *= 11;
		}

		// System.out.println(Player.y + " : " + y);

		double yCorrect = y / 11;

		// Corrects it so that difference is negligible
		if (Display.fps > 30) {
			angleChanger = angleChanger + ((Display.fps - 30) / 30);
		}

		// Correct it even more
		// angleChanger = angleChanger * (Render3D.fpsCheck * 1.25);

		// If less than one, just make one
		if (angleChanger <= 1) {
			angleChanger = 1;
		}

		// Find the angle upward that is needed
		upRotation = Math.atan(((Math.abs(targetY) - Math.abs(y))) / (hypot));

		if (Math.abs(y + 3.0) == Math.abs(targetY)) {
			upMovement = 0;
		} else {
			angleChanger = 20;

			upMovement = -(0.3 * Math.tan(upRotation)) / angleChanger;
		}

		// Angle that the player is in accordance to the enemy that
		// fired the projectile so that the projectile can be fired
		// in that direction
		rotation = Math.atan(((targetX - x) * speed) / ((targetZ - z) * speed));

		/*
		 * If the player is in the 3rd or 4th quadrant of the map then add PI to
		 * rotation so that the projectile will be shot into the correct quadrant of the
		 * map and at the player.
		 */
		if (targetZ < z) {
			rotation += Math.PI;
		}

		// Change in rotation sent in if applicable
		rotation += rotChange;

		/*
		 * Corrects rotation so that the projectile is centered correctly in the map
		 * graph. Idk why its so specific but this is what it takes.
		 */
		double correction = 44.768;

		// Sets rotation so the projectile will continue to move in
		// a certain direction
		rotation = rotation - correction;

		// Calculates the change in x and z needed each movement
		xa = ((Math.cos(rotation)) + (Math.sin(rotation))) * speed;
		za = ((Math.cos(rotation)) - (Math.sin(rotation))) * speed;
	}

	/**
	 * Creates a new Projectile object that is an enemy projectile, but this already
	 * has set rotations.
	 * 
	 * @param damage
	 * @param speed
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 * @param targetX
	 * @param targetZ
	 * @param targetY
	 */
	public EnemyFire(int damage, double speed, double x, double y, double z, int ID, double rotation, double upRotation,
			Entity source, boolean criticalHit) {
		// Creates the projectile object
		super(damage, speed, x, (y / 10), z, ID, criticalHit);

		// If easy mode
		if (FPSLauncher.modeChoice == 1) {
			speed *= 0.5;
			damage *= 0.5;
		}
		// If Bring it on mode
		else if (FPSLauncher.modeChoice == 3) {
			speed *= 2;
			damage *= 2;
		}
		// Death cannot hurt me mode.
		else if (FPSLauncher.modeChoice == 4) {
			speed *= 3;
			damage *= 3;
		}
		// If peaceful mode
		else if (FPSLauncher.modeChoice == 0) {
			speed = 0;
			damage = 0;
		}

		// The enemy that was the source of the Projectile
		sourceEnemy = source;

		if (upRotation == 0) {
			upMovement = 0;
		} else {
			int angleChanger = 20;

			upMovement = -(0.3 * Math.tan(upRotation)) / angleChanger;
		}

		/*
		 * Corrects rotation so that the projectile is centered correctly in the map
		 * graph. Idk why its so specific but this is what it takes.
		 */
		double correction = 44.768;

		// Sets rotation so the projectile will continue to move in
		// a certain direction
		rotation = rotation - correction;

		// Calculates the change in x and z needed each movement
		xa = ((Math.cos(rotation)) + (Math.sin(rotation))) * speed;
		za = ((Math.cos(rotation)) - (Math.sin(rotation))) * speed;
	}

	/**
	 * Moves the projectile similar to a bullet, but adds a new way that the
	 * projectile should move in the y direction depending on where the Player is in
	 * the air.
	 */
	public void move() {
		/*
		 * With faster ticks, everything moves faster, and therefore to correct the
		 * faster movements, this slows them down so that the game is still winnable.
		 */
		if (Display.fps >= 70) {
			speed = initialSpeed / ((Display.fps / 70) + 1);
		} else {
			speed = initialSpeed * 2;
		}

		// The Projectile keeps moving in same direction up or down
		y += upMovement;

		// Checks to make sure projectile can move and move it if it can
		// move
		if (isFree(x + xa, z) && isFree(x, z + za)) {
			x += xa;
			z += za;
		}
	}

}
