package com.vile.entities;

import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.launcher.FPSLauncher;

/**
 * Title: PhaseCannon Date Created: 10/20/2016
 * 
 * @author Alexander Byrd
 * 
 *         Description: A type of weapon that creates a new weapon of ID 1, and
 *         has a unique updateValues method that updates its phases of firing
 *         and such when the weapon is being fired.
 */
public class PhaseCannon extends Weapon implements WeaponInterface {
	/**
	 * Creates new Weapon of ID 1
	 */
	public PhaseCannon() {
		super(2);
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

		// If weapon is in the process of being fired
		if (weaponShootTime != 0) {
			// Play firing sound from the beginning of being fired
			if (weaponShootTime == 1) {
				SoundController.phaseShot.playAudioFile(0);
			}

			/*
			 * Update weapon phase as time in ticks goes on
			 */
			if (weaponShootTime > 45) {
				weaponPhase = 0;
			} else if (weaponShootTime > 30) {
				weaponPhase = 3;
			} else if (weaponShootTime > 15) {
				weaponPhase = 2;
			} else {
				weaponPhase = 1;
			}

			// When sound ends, fire a bullet that corresponds to the
			// weapon type
			if (weaponShootTime == 46) {
				// If unlimited ammo is not on
				if (!Player.unlimitedAmmoOn) {
					ammo--;
				}

				weaponPhase = 0;

				double bullY = -(Player.y * 0.085);

				// Bullet will be lower if player is crouching
				if (Player.yCorrect + 1 < Player.y) {
					bullY = 0.8;
				}

				// Create the bullet
				Bullet bullet = new Bullet(damage, 0.03, Player.x, bullY, Player.z, weaponID, Player.rotation,
						criticalHit);

				// If this is a client, add this bullet to the bulletsAdded arraylist so that it
				// may be added to the server and ticked there.
				if (Display.gameType == 1) {
					Game.bulletsAdded.add(bullet);
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
				while (bullet.move() && Display.gameType != 1) {
					// System.out.println(bullet.y);
					// Do nothing, just call the move method
				}
			}

			weaponShootTime++;

			// If weapon has reached the end of its cooldownTime, say that
			// the weapon can once again be shot
			if (weaponShootTime >= cooldownTime) {
				weaponShootTime = 0;
			}
		}
		// If not being fired, set phase to its dormant phase
		else {
			weaponPhase = 0;

			// If reloading, then tick away from the time its been reloading.
			if (reloading > 0) {
				reloading--;
			}
		}
	}
}
