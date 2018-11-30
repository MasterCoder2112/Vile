package com.vile;

import java.util.ArrayList;

import com.vile.launcher.FPSLauncher;

/**
 * @title SoundController
 * @author Alexander Byrd
 * @date 3/24/2017
 * 
 *       Description: The only purpose of this is to hold all the various sounds
 *       that the game has, and makes them public and static so they can be
 *       accessed from anywhere to be played. Instantiates all the sounds with a
 *       defaultSize of clips to be played, and the file location of the audio
 *       file it needs to load.
 */
public class SoundController {
	// Clip that is played when the player picks up a health pack
	public static Sound health;

	// When the player picks up an ammo clip
	public static Sound clip;

	public static Sound explosion;
	public static Sound secret;
	public static Sound megaPickUp;
	public static Sound keyPickUp;
	public static Sound weaponPickUp;
	public static Sound playerDeath;
	public static Sound reload;
	public static Sound ammoOut;
	public static Sound shoot;
	public static Sound phaseShot;
	public static Sound tryToUse;
	public static Sound buttonPress;
	public static Sound teleportation;
	public static Sound glassBreak;
	public static Sound belegothDeath;
	public static Sound belegothActivate;

	// If player is injured by an enemy
	public static Sound playerHurt;

	// This is public because it used by the door and lift classes
	public static Sound lifting;

	// When the enemy is hit by the players bullet
	public static Sound enemyHit;

	public static Sound bossHit;
	public static Sound bossActivate;
	public static Sound enemyActivate;
	public static Sound pistol;
	public static Sound enemy2Activate;
	public static Sound enemy3Activate;
	public static Sound enemy4Activate;
	public static Sound enemy5Activate;
	public static Sound enemy7Activate;
	public static Sound enemyFire;
	public static Sound enemyFireHit;
	public static Sound reaperHurt;
	public static Sound vileCivHurt;
	public static Sound tankHurt;
	public static Sound wallHit;
	public static Sound rocketFly;
	public static Sound rocketFire;

	// The enemy is killed clip that plays
	public static Sound defaultKill;
	public static Sound bossDeath;
	public static Sound enemy1Death;
	public static Sound enemy2Death;
	public static Sound enemy3Death;
	public static Sound enemy4Death;
	public static Sound enemy7Death;

	public static Sound tutorial1;
	public static Sound tutorial2;
	public static Sound tutorial3;
	public static Sound tutorial4;
	public static Sound tutorial5;
	public static Sound majik;
	public static Sound level1Anouncement;
	public static Sound level2Anouncement;
	public static Sound armorPickup;
	public static Sound computerShutdown;
	public static Sound specialPickup;
	public static Sound creepySound;
	public static Sound phaseCannonHit;

	public static Sound activated;
	public static Sound doorStart;
	public static Sound doorEnd;
	public static Sound healthBig;
	public static Sound keyUse;
	public static Sound keyTry;
	public static Sound uplink;
	public static Sound criticalHit;
	public static Sound crushed;

	// Stores all the sounds so that all the sounds can be
	// set to certain settings at once
	public static ArrayList<Sound> allSounds;

	private int defaultSize = 4;

	/**
	 * Contructs all sounds held in the sound controller
	 */
	public SoundController() {
		// reset the arraylist
		allSounds = null;
		allSounds = new ArrayList<Sound>();

		enemyHit = addSound(enemyHit, "enemyHit");
		defaultKill = addSound(defaultKill, "enemyDeath");
		health = addSound(health, "health");
		clip = addSound(clip, "shell");
		playerHurt = addSound(playerHurt, "playerHit");
		shoot = addSound(shoot, "shot");
		ammoOut = addSound(ammoOut, "ammoOut");
		reload = addSound(reload, "reload");
		tryToUse = addSound(tryToUse, "oomf");
		buttonPress = addSound(buttonPress, "endSwitch");
		lifting = addSound(lifting, "wallMove");
		secret = addSound(secret, "secretFound");
		megaPickUp = addSound(megaPickUp, "megaPickUp");
		wallHit = addSound(wallHit, "wallHit");
		keyPickUp = addSound(keyPickUp, "keyPickUp");
		weaponPickUp = addSound(weaponPickUp, "weaponPickUp");
		playerDeath = addSound(playerDeath, "playerDeath");
		bossHit = addSound(bossHit, "bossHit");
		bossActivate = addSound(bossActivate, "bossActivate");
		enemyActivate = addSound(enemyActivate, "enemyActivate");
		bossDeath = addSound(bossDeath, "bossDeath");
		phaseShot = addSound(phaseShot, "phaseShot");
		explosion = addSound(explosion, "barrelexplosion");
		pistol = addSound(pistol, "pistol");
		enemy2Activate = addSound(enemy2Activate, "enemy2Activate");
		enemy3Activate = addSound(enemy3Activate, "enemy3Activate");
		enemy4Activate = addSound(enemy4Activate, "enemy4Activate");
		enemy5Activate = addSound(enemy5Activate, "enemy5Activate");
		enemy7Activate = addSound(enemy7Activate, "enemy7Activate");
		enemyFire = addSound(enemyFire, "enemyFire");
		enemyFireHit = addSound(enemyFireHit, "fireballHit");
		reaperHurt = addSound(reaperHurt, "reaperHarm");
		vileCivHurt = addSound(vileCivHurt, "defaultHurt");
		tankHurt = addSound(tankHurt, "tankHurt");
		enemy1Death = addSound(enemy1Death, "enemy1Death");
		enemy2Death = addSound(enemy2Death, "enemy2Death");
		enemy3Death = addSound(enemy3Death, "enemy3Death");
		enemy4Death = addSound(enemy4Death, "enemy4Death");
		enemy7Death = addSound(enemy7Death, "enemy7Death");
		teleportation = addSound(teleportation, "teleportation");
		rocketFire = addSound(rocketFire, "rocketFire");
		rocketFly = addSound(rocketFly, "rocketFly");
		glassBreak = addSound(glassBreak, "glassBreak");
		belegothActivate = addSound(belegothActivate, "belegothActivate");
		belegothDeath = addSound(belegothDeath, "belegothDeath");
		tutorial1 = addSound(tutorial1, "tutorial1");
		tutorial2 = addSound(tutorial2, "tutorial2");
		tutorial3 = addSound(tutorial3, "tutorial3");
		tutorial4 = addSound(tutorial4, "tutorial4");
		majik = addSound(majik, "majik");
		level1Anouncement = addSound(level1Anouncement, "level1Anouncement");
		level2Anouncement = addSound(level2Anouncement, "level2Anouncement");
		armorPickup = addSound(armorPickup, "armorPickup");
		computerShutdown = addSound(computerShutdown, "computerShutdown");
		specialPickup = addSound(specialPickup, "specialItems");
		creepySound = addSound(creepySound, "creepySound");
		phaseCannonHit = addSound(phaseCannonHit, "phaseCannonHit");
		activated = addSound(activated, "activated");
		doorStart = addSound(doorStart, "doorStart");
		doorEnd = addSound(doorEnd, "doorEnd");
		healthBig = addSound(healthBig, "healthBig");
		keyUse = addSound(keyUse, "keyuse");
		keyTry = addSound(keyTry, "keytry");
		uplink = addSound(uplink, "uplink");
		criticalHit = addSound(criticalHit, "criticalHit");
		crushed = addSound(crushed, "crushed");
	}

	/**
	 * Used to reset all of the sound volumes. Usually used when the game is
	 * reloaded.
	 * 
	 * @param newVolume
	 */
	public void resetAllVolumes(float newVolume) {
		for (Sound temp : allSounds) {
			temp.resetVolume(newVolume);
		}
	}

	/**
	 * Basically nullifies all sounds to clear the heap
	 */
	public void resetSounds() {
		for (Sound temp : allSounds) {
			temp.nullify();
			temp = null;
		}

		allSounds = null;
	}

	/**
	 * Ends any currently playing sounds
	 */
	public void stopSounds() {
		for (Sound temp : allSounds) {
			temp.stopAll();
		}
	}

	/**
	 * Instantiates a single sound at a time, checking to see if first it is in the
	 * current resource pack, and then if not there it will use the default sound
	 * found in the default resource pack. If not in either the sound will just be
	 * null, but it will not cause the program to crash.
	 * 
	 * @param temp
	 * @param fileName
	 */
	private Sound addSound(Sound temp, String fileName) {
		try {
			// Try to find sound in current resource pack
			temp = new Sound(defaultSize, "resources" + FPSLauncher.themeName + "/audio/" + fileName + ".wav");
			allSounds.add(temp);
			temp.audioName = fileName;
		} catch (Exception e) {
			try {
				// If sound is not found in the resource pack
				temp = new Sound(defaultSize, "resources/default/audio/" + fileName + ".wav");
				allSounds.add(temp);
				temp.audioName = fileName;
			} catch (Exception ex) {
				// Sound is just null if not found in either
				// Print exception though for debugging
				// Sound will not be added to allSounds if this is the case
			}
		}

		return temp;
	}
}
