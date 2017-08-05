package com.vile;

import com.vile.Sound;

/**
 * @title SoundController
 * @author Alexander Byrd
 * @date 3/24/2017
 * 
 * Description:
 * The only purpose of this is to hold all the various sounds that the
 * game has, and makes them public and static so they can be accessed
 * from anywhere to be played. Instantiates all the sounds with a 
 * defaultSize of clips to be played, and the file location of the
 * audio file it needs to load.
 */
public class SoundController 
{
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
	public static Sound mlgDeath;
	public static Sound nickDeath;
	public static Sound glassBreak;
	public static Sound belegothDeath;
	public static Sound belegothActivate;
	
	// If player is injured by an enemy
	public static Sound playerHurt;
	
	//This is public because it used by the door and lift classes
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
	
	public static Sound level1Anouncement;
	public static Sound armorPickup;
	public static Sound computerShutdown;
	public static Sound specialPickup;
	public static Sound creepySound;
	
	private int defaultSize = 6;
	
   /**
    * Contructs all sounds held in the sound controller
    */
	public SoundController() 
	{
		enemyHit 	      =  new Sound(defaultSize, "/test/enemyHit.wav");
		defaultKill       =  new Sound(defaultSize, "/test/enemyDeath.wav");
		health 		      =  new Sound(defaultSize, "/test/health.wav");
		clip		      =  new Sound(defaultSize, "/test/shell.wav");
		playerHurt        =  new Sound(defaultSize, "/test/playerHit.wav");
		shoot		      =  new Sound(defaultSize, "/test/shot.wav");
		ammoOut           =  new Sound(defaultSize, "/test/ammoOut.wav");
		reload            =  new Sound(defaultSize, "/test/reload.wav");
		tryToUse          =  new Sound(defaultSize, "/test/oomf.wav");
		buttonPress       =  new Sound(defaultSize, "/test/endSwitch.wav");
		lifting           =  new Sound(defaultSize, "/test/wallMove.wav");
		secret            =  new Sound(defaultSize, "/test/secretFound.wav");
		megaPickUp        =  new Sound(defaultSize, "/test/megaPickUp.wav");
		wallHit           =  new Sound(defaultSize, "/test/wallHit.wav");
		keyPickUp         =  new Sound(defaultSize, "/test/keyPickUp.wav");
		weaponPickUp      =  new Sound(defaultSize, "/test/weaponPickUp.wav");
		playerDeath       =  new Sound(defaultSize, "/test/playerDeath.wav");
		bossHit           =  new Sound(defaultSize, "/test/bossHit.wav");
		bossActivate      =  new Sound(defaultSize, "/test/bossActivate.wav");
		enemyActivate     =  new Sound(defaultSize, "/test/enemyActivate.wav");
		bossDeath         =  new Sound(defaultSize, "/test/bossDeath.wav");
		phaseShot         =  new Sound(defaultSize, "/test/phaseShot.wav");
		explosion   =  new Sound(defaultSize, "/test/barrelexplosion.wav");
		pistol            =  new Sound(defaultSize, "/test/pistol.wav");
		enemy2Activate    =  new Sound(defaultSize, "/test/enemy2Activate.wav");
		enemy3Activate    =  new Sound(defaultSize, "/test/enemy3Activate.wav");
		enemy4Activate    =  new Sound(defaultSize, "/test/enemy4Activate.wav");
		enemy5Activate    =  new Sound(defaultSize, "/test/enemy5Activate.wav");
		enemy7Activate    =  new Sound(defaultSize, "/test/enemy7Activate.wav");
		enemyFire         =  new Sound(defaultSize, "/test/enemyFire.wav");
		enemyFireHit      =  new Sound(defaultSize, "/test/fireballHit.wav");
		reaperHurt        =  new Sound(defaultSize, "/test/reaperHarm.wav");
		vileCivHurt		  =  new Sound(defaultSize, "/test/defaultHurt.wav");
		tankHurt		  =  new Sound(defaultSize, "/test/tankHurt.wav");
		enemy1Death       =  new Sound(defaultSize, "/test/enemy1Death.wav");
		enemy2Death       =  new Sound(defaultSize, "/test/enemy2Death.wav");
		enemy3Death       =  new Sound(defaultSize, "/test/enemy3Death.wav");
		enemy4Death       =  new Sound(defaultSize, "/test/enemy4Death.wav");
		enemy7Death       =  new Sound(defaultSize, "/test/enemy7Death.wav");
		teleportation     =  new Sound(defaultSize, "/test/teleportation.wav");
		mlgDeath          =  new Sound(1, "/test/mlgDeath.wav");
		nickDeath         =  new Sound(1, "/test/nickDeath.wav");
		rocketFire        =  new Sound(defaultSize, "/test/rocketFire.wav");
		rocketFly         =  new Sound(1, "/test/rocketFly.wav");
		glassBreak        =  new Sound(defaultSize, "/test/glassBreak.wav");
		belegothActivate  =  new Sound(1, "/test/belegothActivate.wav");
		belegothDeath     =  new Sound(1, "/test/belegothDeath.wav");
		level1Anouncement = new Sound(1, "/test/level1Anouncement.wav");
		armorPickup       =  new Sound(defaultSize, "/test/armorPickup.wav");
		computerShutdown  =  new Sound(defaultSize, "/test/computerShutdown.wav");
		specialPickup     =  new Sound(defaultSize, "/test/specialItems.wav");
		creepySound       =  new Sound(defaultSize, "/test/creepySound.wav");
	}
	
   /**
    * Used to reset all of the sound volumes. Usually used when the game
    * is reloaded.
    * @param newVolume
    */
	public void resetAllVolumes(float newVolume)
	{
		enemyHit.resetVolume(newVolume);
		defaultKill.resetVolume(newVolume);
		health.resetVolume(newVolume);
		clip.resetVolume(newVolume);
		playerHurt.resetVolume(newVolume);
		shoot.resetVolume(newVolume);
		ammoOut.resetVolume(newVolume);
		reload.resetVolume(newVolume);
		tryToUse.resetVolume(newVolume);
		buttonPress.resetVolume(newVolume);
		lifting.resetVolume(newVolume);
		secret.resetVolume(newVolume);
		megaPickUp.resetVolume(newVolume);
		wallHit.resetVolume(newVolume);
		keyPickUp.resetVolume(newVolume);
		weaponPickUp.resetVolume(newVolume);
		playerDeath.resetVolume(newVolume);
		bossHit.resetVolume(newVolume);
		bossActivate.resetVolume(newVolume);
		enemyActivate.resetVolume(newVolume);
		bossDeath.resetVolume(newVolume);
		phaseShot.resetVolume(newVolume);
		explosion.resetVolume(newVolume);
		pistol.resetVolume(newVolume);
		enemy2Activate.resetVolume(newVolume);
		enemy3Activate.resetVolume(newVolume);
		enemy4Activate.resetVolume(newVolume);
		enemy5Activate.resetVolume(newVolume);
		enemy7Activate.resetVolume(newVolume);
		enemyFire.resetVolume(newVolume);
		enemyFireHit.resetVolume(newVolume);
		reaperHurt.resetVolume(newVolume);
		vileCivHurt.resetVolume(newVolume);
		tankHurt.resetVolume(newVolume);
		enemy1Death.resetVolume(newVolume);
		enemy2Death.resetVolume(newVolume);
		enemy3Death.resetVolume(newVolume);
		enemy4Death.resetVolume(newVolume);
		enemy7Death.resetVolume(newVolume);
		teleportation.resetVolume(newVolume);
		mlgDeath.resetVolume(newVolume);
		nickDeath.resetVolume(newVolume);
		rocketFire.resetVolume(newVolume);
		rocketFly.resetVolume(newVolume);
		glassBreak.resetVolume(newVolume);
		belegothActivate.resetVolume(newVolume);
		belegothDeath.resetVolume(newVolume);
		level1Anouncement.resetVolume(newVolume);
		armorPickup.resetVolume(newVolume);
		computerShutdown.resetVolume(newVolume);
		specialPickup.resetVolume(newVolume);
		creepySound.resetVolume(newVolume);
	}

}
