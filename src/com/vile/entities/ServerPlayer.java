package com.vile.entities;

import com.vile.Game;
import com.vile.SoundController;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: ServerPlayer
 * 
 * @author Alexander Byrd
 * 
 *         Date Created: 11/17/2018
 * 
 *         Description: Besides the host player, the ServerPlayer class is a
 *         class that holds all the information about the other clients
 *         connected so that they can all be updated by server events in the
 *         correct manner.
 *
 */
public class ServerPlayer {

	// Typical values
	public static int health = 100;
	public static int maxHealth = 200;
	public static int armor = 0;

	// How many upgrade points has the player collected
	public static int upgradePoints = 0;

	// How many health resurrections do you have
	public static int resurrections = 0;

	// How long the player is protected from toxic waste/lava
	public static int environProtectionTime = 0;

	// How long the player is immortal for
	public static int immortality = 0;

	// Enhanced Vision/night vision
	public static int vision = 0;

	// Invisibility. Enemies can't see player
	public static int invisibility = 0;

	// How long since player was last hurt
	public static int playerHurt = 0;

	// Max number of kills in survival
	public static int maxKills;

	public static double height = 2.0;
	public static double x = 0;
	public static double y = 0;
	public static double z = 0;

	// yCorrect is used for when the player is crouching
	public static double yCorrect = 0;

	// Direction player is facing.
	public static double rotation = 0;

	// If an explosion effects movement of player, this is the effect on
	// each direction of the player
	public static double zEffects = 0;
	public static double xEffects = 0;
	public static double yEffects = 0;

	// Max Height a player can stand on at moment.
	public static double maxHeight = 0;

	// The height the player can jump at this moment
	public static double jumpHeight = 8;

	// How high in the air player can jump no matter where he/she is
	public static double totalJump = 8;

	// Used for rendering
	public static double upRotate = 1.105;

	// If a solid item or enemy adds extra height to a player
	public static double extraHeight = 0;

	// Whether the player has the keys or not
	public static boolean hasRedKey = false;
	public static boolean hasBlueKey = false;
	public static boolean hasGreenKey = false;
	public static boolean hasYellowKey = false;

	// Player cheats
	public static boolean noClipOn;
	public static boolean flyOn;
	public static boolean superSpeedOn;
	public static boolean godModeOn;
	public static boolean unlimitedAmmoOn;

	// Is a wall crushing the player, forcing him/her to crouch
	public static boolean forceCrouch;

	// Is player still alive?
	public static boolean alive = true;

	// Block player is standing on
	public static Block blockOn = null;

	/*
	 * Weapon equipped. 0 for Pistols 1 for Shotgun 2 for Phase Cannon 3 for Rocket
	 * Launcher
	 */
	public static int weaponEquipped = 0;

	// Array of weapons player has
	public static Weapon[] weapons = new Weapon[4];

	/**
	 * Reset all of a Players variables and make a new Player
	 */
	public ServerPlayer() {
		health = 100;
		maxHealth = 200;
		height = 2.0;
		alive = true;
		armor = 0;
		maxHeight = 0;
		x = 1.5;
		y = 0;
		z = 1.5;
		rotation = 0;
		upRotate = 1.105;
		jumpHeight = 8;

		hasRedKey = false;
		hasBlueKey = false;
		hasGreenKey = false;
		hasYellowKey = false;

		resurrections = 0;
		environProtectionTime = 0;
		immortality = 0;
		vision = 0;

		forceCrouch = false;

		weapons[0] = new Pistol();
		weapons[1] = new Shotgun();
		weapons[2] = new PhaseCannon();
		weapons[3] = new RocketLauncher();
		weaponEquipped = 0;

		blockOn = Level.getBlock((int) Player.x, (int) Player.z);
	}

	/**
	 * Updates the players buffs by counting down the ticks until they wear off.
	 */
	public static void updateBuffs() {
		/*
		 * Each tick, take the time the the player is protected from environmental
		 * conditions by 1.
		 */
		if (environProtectionTime > 0) {
			environProtectionTime--;
		}

		/*
		 * Each tick, take the time the player is immortal for down by 1 tick.
		 */
		if (immortality > 0) {
			immortality--;
		}

		/*
		 * Each tick, take the time the player is immortal for down by 1 tick.
		 */
		if (vision > 0) {
			vision--;
		}

		/*
		 * Each tick, if player is invisible, take that down by a tick
		 */
		if (invisibility > 0) {
			invisibility--;
		}

		/*
		 * Each update, update how long its been since the player was last hurt so that
		 * the players face will display the correct expression.
		 */
		if (playerHurt > 0) {
			playerHurt--;
		}

		if (yEffects > 0) {
			yEffects -= 2;
		} else if (yEffects < 0) {
			yEffects += 2;
		}

		if (xEffects > 0) {
			xEffects -= 0.2;
		} else if (xEffects < 0) {
			xEffects += 0.2;
		}

		if (zEffects > 0) {
			zEffects -= 0.2;
		} else if (zEffects < 0) {
			zEffects += 0.2;
		}

		if (Math.abs(yEffects) <= 2) {
			yEffects = 0;
		}

		if (Math.abs(zEffects) <= 0.2) {
			zEffects = 0;
		}

		if (Math.abs(xEffects) <= 0.2) {
			xEffects = 0;
		}

		blockOn = Level.getBlock((int) Player.x, (int) Player.z);
	}

	/**
	 * Hurts the player (if he/she is not in God mode) by a certain amount sent in.
	 * Armor is also taken into account when dealing damage to the player.
	 * 
	 * @param damage
	 */
	public static void hurtPlayer(double damage) {
		// If the player is immortal then don't hurt
		// Or if in peaceful mode don't hurt
		if (godModeOn || immortality != 0 || Game.skillMode == 0 || !Player.alive) {
			return;
		}

		// Hurt player depending on how much damage the enemy deals
		// and on the armor the player is wearing.
		if (Player.armor > 150) {
			Player.health -= (int) (damage / 2);
			Player.armor -= damage;
		} else if (Player.armor > 100) {
			Player.health -= (int) (damage / 1.75);
			Player.armor -= damage * 1.5;
		} else if (Player.armor > 50) {
			Player.health -= (int) (damage / 1.5);
			Player.armor -= damage * 2;
		} else if (Player.armor > 0) {
			Player.health -= (int) (damage / 1.25);
			Player.armor -= damage * 3;
		} else {
			Player.health -= damage;
		}

		// If player armor is out, just set it to 0
		if (Player.armor < 0) {
			Player.armor = 0;
		}

		// Player is "hurting" for 10 ticks
		playerHurt = 10;

		// Player hurt sound
		SoundController.playerHurt.playAudioFile(0);
	}

}
