package com.vile.entities;

import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;

/**
 * Title: Pistol
 * 
 * @author Alexander Byrd Date Created: 11/26/2016
 * 
 *         Description: Is a weapon with unique values and rates of fire
 *
 */
public class Sword extends Weapon implements WeaponInterface {
	/**
	 * Creates a new Weapon of ID 2
	 */
	public Sword() {
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

		super.damage = super.baseDamage * (int) Player.meleeMultiplier;

		// IF being fired
		if (weaponShootTime != 0) {
			// If weapon already fired, set phase to dormant phase
			if (weaponShootTime > 30) {
				weaponPhase = 0;
			}

			// Update weapon values and initiate "fire" halfway through swing
			if (weaponShootTime == 1) {
				weaponPhase = 1;
			} else if (weaponShootTime == 9) {
				weaponPhase = 2;
			} else if (weaponShootTime == 17) {
				weaponPhase = 3;
				fire(criticalHit);
			} else if (weaponShootTime == 25) {
				weaponPhase = 4;
			} else if (weaponShootTime == 33) {
				weaponPhase = 5;
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
		Bullet bullet = new Bullet(damage, 0.03, Player.x, bullY, Player.z, weaponID - 1, Player.rotation, criticalHit);

		// If this is a client, add this bullet to the bulletsAdded arraylist so that it
		// may be added to the server and ticked there.
		if (Display.gameType == 1) {
			Game.bulletsAdded.add(bullet);
		}

		// int crossWidth = Display.WIDTH;
		// int crossHeight = Display.HEIGHT;

		// Corrects bullet depending on resolution
		// if (FPSLauncher.resolutionChoice <= 1) {
		// crossHeight -= 125;
		// } else if (FPSLauncher.resolutionChoice <= 3) {
		// crossHeight -= 125;
		// }

		// bullet.pixelsOnScreen.add((crossWidth / 2) +
		// (crossHeight / 2) * crossWidth);

		int movementSteps = 0;

		/*
		 * Instead of rendering the bullet and all that, just check its movement
		 * instantaneously in small increments to make it look like it hits the enemy
		 * instantaneously and also makes it more precise.
		 */
		while (bullet.move() && Display.gameType != 1 && movementSteps < 30) {
			movementSteps++;
		}

		Game.bullets.remove(bullet);
		bullet = null;

		try {
			SoundController.swordSwing.playAudioFile(0);
		} catch (Exception ex) {

		}
	}

}
