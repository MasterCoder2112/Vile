package com.vile.entities;

import java.util.Random;

import com.vile.Game;
import com.vile.SoundController;

/**
 * Title: Shotgun Date Created: 10/20/2016
 * 
 * @author Alexander Byrd
 * 
 *         Description: A type of weapon that creates a new weapon of ID 0, and
 *         has a unique updateValues method that updates its phases of firing
 *         and such when the weapon is being fired.
 */
public class Scepter extends Weapon implements WeaponInterface {
	/**
	 * Creates a new Weapon of ID 0
	 */
	public Scepter() {
		super(4);
	}

	/**
	 * Update weaponShootTime, and depending on its value update its stage in the
	 * firing process of the weapon.
	 */
	@Override
	public void updateValues() {
		// Generates a random number to see if the projectile will be a
		// critical hit or not
		Random rand = new Random();
		int randInt = rand.nextInt(criticalHitChances);

		boolean criticalHit = false;

		// If value of 0, be a critical hit
		if (randInt == 0) {
			criticalHit = true;
		}

		// IF being fired
		if (weaponShootTime != 0) {
			// If weapon already fired, set phase to dormant phase
			if (weaponShootTime > 24) {
				weaponPhase = 0;
			}

			// Fire weapon and play sound immediately after being fired
			if (weaponShootTime == 1) {
				// If unlimited ammo is not on
				if (!Player.unlimitedAmmoOn) {
					ammo--;
				}

				weaponPhase = 1;

				fire(criticalHit);
			} else if (weaponShootTime == 5) {
				weaponPhase = 2;
			} else if (weaponShootTime == 10) {
				weaponPhase = 3;
			} else if (weaponShootTime == 16) {
				weaponPhase = 4;
			}

			weaponShootTime++;

			// Set weapon to fire-able again after its cool down time has
			// been reached
			if (weaponShootTime >= cooldownTime) {
				weaponShootTime = 0;
			}
		}
		// IF not being fired, it is in a dormant phase.
		else {
			weaponPhase = 0;

			// If reloading, then tick away from the time its been reloading.
			if (reloading > 0) {
				reloading--;
			}
		}
	}

	/**
	 * Just fires a bullet for the pistol. Eliminates need to repeat code
	 */
	private void fire(boolean criticalHit) {

		double bullY = -(Player.y * 0.085);

		// Bullet will be lower if player is crouching
		if (Player.yCorrect + 1 < Player.y) {
			bullY = 0.8;
		}
		// Create the bullet
		// Bullet bullet = new Bullet(damage, 0.03, Player.x, bullY, Player.z, 9,
		// Player.rotation, criticalHit);

		Game.addBullet(damage, 9, 0.3, Player.rotation, criticalHit);

		// If this is a client, add this bullet to the bulletsAdded arraylist so that it
		// may be added to the server and ticked there.
		// if (Display.gameType == 1) {
		// Game.bulletsAdded.add(bullet);
		// }

		// int crossWidth = Display.WIDTH;
		// int crossHeight = Display.HEIGHT;

		// Corrects bullet depending on resolution
		/*
		 * if (FPSLauncher.resolutionChoice <= 1) { crossHeight -= 125; } else if
		 * (FPSLauncher.resolutionChoice <= 3) { crossHeight -= 125; }
		 * 
		 * // bullet.pixelsOnScreen.add((crossWidth / 2) + // (crossHeight / 2) *
		 * crossWidth);
		 * 
		 * /* Instead of rendering the bullet and all that, just check its movement
		 * instantaneously in small increments to make it look like it hits the enemy
		 * instantaneously and also makes it more precise.
		 */
		// while (bullet.move() && Display.gameType != 1) {
		// System.out.println(bullet.y);
		// Do nothing, just call the move method
		// }

		try {
			SoundController.scepterShot.playAudioFile(0);
		} catch (Exception ex) {

		}
	}
}
