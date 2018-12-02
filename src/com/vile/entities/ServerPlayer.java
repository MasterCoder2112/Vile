package com.vile.entities;

import java.util.ArrayList;

import com.vile.Game;
import com.vile.PopUp;
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

	public int ID = 0;

	// Typical values
	public int health = 100;
	public int maxHealth = 200;
	public int armor = 0;

	// How long the player is protected from toxic waste/lava
	public int environProtectionTime = 0;

	// How long the player is immortal for
	public int immortality = 0;

	// Enhanced Vision/night vision
	public int vision = 0;

	// Invisibility. Enemies can't see player
	public int invisibility = 0;

	// How long since player was last hurt
	public int playerHurt = 0;

	public double height = 2.0;
	public double x = 0;
	public double y = 0;
	public double z = 0;

	// Direction player is facing.
	public double rotation = 0;

	// If an explosion effects movement of player, this is the effect on
	// each direction of the player
	public double zEffects = 0;
	public double xEffects = 0;
	public double yEffects = 0;

	// Player cheats
	public boolean noClipOn;
	public boolean flyOn;
	public boolean superSpeedOn;
	public boolean godModeOn;
	public boolean unlimitedAmmoOn;

	// Is player still alive?
	public boolean alive = true;

	/*
	 * Weapon equipped. 0 for Pistols 1 for Shotgun 2 for Phase Cannon 3 for Rocket
	 * Launcher
	 */
	public int weaponEquipped = 0;

	// Array of weapons player has
	public Weapon[] weapons = new Weapon[4];

	// TODO New things added for client to take in and act.
	// Any messages or audio to play on the client side activated by the server will
	// be put here.
	// Then these will be sent back to the client and the client will act on them.
	public ArrayList<PopUp> clientMessages = new ArrayList<PopUp>();
	public ArrayList<String> audioToPlay = new ArrayList<String>();
	public ArrayList<Integer> audioDistances = new ArrayList<Integer>();

	public int kills = 0;
	public int deaths = 0;

	/**
	 * Reset all of a Players variables and make a new Player
	 */
	public ServerPlayer() {
		ID = Game.otherPlayers.size();
		health = 100;
		maxHealth = 200;
		height = 2.0;
		alive = true;
		armor = 0;
		x = 1.5;
		y = 0;
		z = 1.5;
		rotation = 0;

		environProtectionTime = 0;
		immortality = 0;
		vision = 0;

		weapons[0] = new Pistol();
		weapons[1] = new Shotgun();
		weapons[2] = new PhaseCannon();
		weapons[3] = new RocketLauncher();
		weaponEquipped = 0;
	}

	/**
	 * Updates the players buffs by counting down the ticks until they wear off.
	 */
	public void updateBuffs() {
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

	}

	/**
	 * Hurts the player (if he/she is not in God mode) by a certain amount sent in.
	 * Armor is also taken into account when dealing damage to the player.
	 * 
	 * @param damage
	 */
	public void hurtPlayer(double damage) {
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
