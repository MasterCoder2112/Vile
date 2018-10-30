package com.vile.entities;

import java.util.Random;

import com.vile.Display;
import com.vile.SoundController;
import com.vile.launcher.FPSLauncher;

/**
 * Title: Pistol
 * 
 * @author Alexander Byrd Date Created: 11/26/2016
 * 
 *         Description: Is a weapon with unique values and rates of fire
 *
 */
public class Pistol extends Weapon implements WeaponInterface {
	/**
	 * Creates a new Weapon of ID 2
	 */
	public Pistol() {
		super(0);
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
			} else if (weaponShootTime == 22) {
				weaponPhase = 0;
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
		}

		// IF second weapon is being fired
		if (weaponShootTime2 != 0) {
			// If weapon already fired, set phase to dormant phase
			if (weaponShootTime2 > 24) {
				weaponPhase2 = 0;
			}

			// Fire weapon and play sound immediately after being fired
			if (weaponShootTime2 == 1) {
				// If unlimited ammo is not on
				if (!Player.unlimitedAmmoOn) {
					ammo--;
				}

				weaponPhase2 = 1;

				fire(criticalHit);
			} else if (weaponShootTime2 == 5) {
				weaponPhase2 = 2;
			} else if (weaponShootTime2 == 10) {
				weaponPhase2 = 3;
			} else if (weaponShootTime2 == 22) {
				weaponPhase2 = 0;
			}

			weaponShootTime2++;

			// Set weapon to fire-able again after its cool down time has
			// been reached
			if (weaponShootTime2 >= cooldownTime) {
				weaponShootTime2 = 0;
			}
		}
		// IF not being fired, it is in a dormant phase.
		else {
			weaponPhase2 = 0;
		}
	}

	/**
	 * Just fires a bullet for the pistol. Eliminates need to repeat code
	 */
	private void fire(boolean criticalHit) {
		// Create the bullet
		Bullet bullet = new Bullet(damage, 0.03, Player.x, -(Player.y * 0.085), Player.z, weaponID, Player.rotation,
				criticalHit);

		// Bullet will be lower if player is crouching
		if (Player.yCorrect + 1 < Player.y) {
			bullet.y = 0.8;
		}

		int crossWidth = Display.WIDTH;
		int crossHeight = Display.HEIGHT;

		// Corrects bullet depending on resolution
		if (FPSLauncher.resolutionChoice <= 1) {
			crossHeight -= 125;
		} else if (FPSLauncher.resolutionChoice <= 3) {
			crossHeight -= 125;
		}

		// bullet.pixelsOnScreen.add((crossWidth / 2) +
		// (crossHeight / 2) * crossWidth);

		/*
		 * Instead of rendering the bullet and all that, just check its movement
		 * instantaneously in small increments to make it look like it hits the enemy
		 * instantaneously and also makes it more precise.
		 */
		while (bullet.move()) {
			// System.out.println(bullet.y);
			// Do nothing, just call the move method
		}

		try {
			SoundController.pistol.playAudioFile(0);
		} catch (Exception ex) {

		}
	}

}
