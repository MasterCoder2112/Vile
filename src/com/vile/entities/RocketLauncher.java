package com.vile.entities;

import java.util.Random;

import com.vile.Game;
import com.vile.SoundController;

/**
 * Title: RocketLauncher Date Created: 4/20/2017
 * 
 * @author Alexander Byrd
 * 
 *         Description: A type of weapon that creates a new weapon of ID 3, and
 *         has a unique updateValues method that updates its phases of firing
 *         and such when the weapon is being fired.
 */
public class RocketLauncher extends Weapon implements WeaponInterface {
	/**
	 * Creates new RocketLauncher with ID 3
	 */
	public RocketLauncher() {
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
			if (weaponShootTime > 25) {
				weaponPhase = 0;
			}

			// Fire weapon and play sound immediately after being fired
			if (weaponShootTime == 1) {
				// If unlimited ammo is not on
				if (!Player.unlimitedAmmoOn) {
					ammo--;
				}

				// First firing phase
				weaponPhase = 1;

				// Adds rocket to map
				Game.addBullet(damage, weaponID - 1, 0.2, Player.rotation, criticalHit);

				try {
					// Begin rocket sound effect, and play firing sound effect
					SoundController.rocketFire.playAudioFile(0);
					SoundController.rocketFly.playAudioFile(0);
				} catch (Exception e) {

				}
			}
			// Weapon firing phase increases every so many ticks
			else if (weaponShootTime == 6) {
				weaponPhase = 2;
			} else if (weaponShootTime == 11) {
				weaponPhase = 3;
			} else if (weaponShootTime == 16) {
				weaponPhase = 3;
			} else if (weaponShootTime == 21) {
				weaponPhase = 4;
			}

			// Increase time between when shot was first fired
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

}
