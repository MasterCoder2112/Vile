package com.vile.entities;

import java.util.ArrayList;
import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.SoundController;
import com.vile.graphics.Render3D;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Enemy
 * 
 * @author Alex Byrd Date Updated: 6/9/2017
 *
 *         Keeps track of the enemies status such as movement, attacking, and
 *         other such things. Individual methods and such are commented
 *         seperately.
 */
public class Entity extends EntityParent implements Comparable {
	/**
	 * Creates a new enemy with values sent in. The sets up the enemy based on
	 * gamemode, fps, and enemy type. This is the default constructor.
	 * 
	 * @param armor
	 * @param ammo
	 * @param damage
	 * @param x
	 * @param y
	 * @param z
	 */
	public Entity(double x, double y, double z, int ID, double rotation, int itemActivationID) {
		super(100, 0, 0, 6, 0, x, y, z, ID, rotation, itemActivationID);

		// Default speed is 1
		speed = 1;

		// Defaults for entities
		hasMovement = true;
		hasItemDrops = true;
		moveable = false;
		isSolid = false;
		killable = true;
		canTurn = true;
		isHuman = true;

		// Brainomorph
		if (ID == 1) {
			health = 120;
			hasSpecial = true;
			weightLevel = 2;
			height = 7;
		}
		// Sentinel
		else if (ID == 2) {
			health = 150;
			hasSpecial = true;
			canFly = true;
			weightLevel = 2;
			height = 7;
		}
		// Mutated Commando
		else if (ID == 3) {
			speed *= 0.25;
			health = 300;
			damage *= 4;
			hasSpecial = true;
			weightLevel = 3;
			height = 7;
		}
		// Reaper
		else if (ID == 4) {
			speed *= 6;
			damage = 4;
			health = 40;
			weightLevel = 1;
			height = 7;
			isGhost = true;
		}
		// Magistrate
		else if (ID == 5) {
			damage = 2;
			health = 100;
			weightLevel = 1;
			height = 7;

			// Increases time it takes to use special which is resurrecting
			tickAmount = 24;
		}
		// Morgoth
		else if (ID == 6) {
			speed *= 2;
			damage = 25;
			health = 5000;
			height = 48;
			weightLevel = 10;
			isABoss = true;
			hasSpecial = true;
		}
		// Vile Warrior
		else if (ID == 7) {
			damage = 3;
			health = 40;
			weightLevel = 1;
			height = 7;
		}
		// Belegoth
		else if (ID == 8) {
			speed *= 2;
			damage = 50;
			health = 10000;
			isABoss = true;
			height = 48;
			weightLevel = 10;
			hasSpecial = true;
		}
		// Watcher
		else if (ID == 9) {
			speed *= 0.125;
			damage = 10;
			health = 500;
			isABoss = false;
			height = 7;
			weightLevel = 8;
			canFly = true;
			hasSpecial = true;
			tickAmount = 24;
		}
		// Marines
		else if (ID >= 10 && ID <= 15) {
			damage = 20;
			health = 150;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = true;
			isFriendly = true;
			pursuant = false;

			// If itemActivationID is special, it is an evil marine.
			// Evil marines don't do quite as much damage because the player
			// is technically more fragile than the enemies.
			if (this.itemActivationID == -1) {
				isFriendly = false;
				damage /= 4;
			}
			// For stationary marines
			else if (itemActivationID == -2) {
				hasMovement = false;
				canTurn = false;
			}
		}
		// This is a chair
		else if (ID == 16) {
			damage = 0;
			speed = 0;
			initialSpeed = 0;
			isABoss = false;
			height = 1;
			weightLevel = 1;
			hasSpecial = false;
			isFriendly = true;
			hasMovement = false;
			isSolid = true;
			moveable = true;
			hasItemDrops = false;
			canTurn = false;
			isHuman = false;
		}
		// This is a toilet
		else if (ID == 17) {
			health = 50;
			damage = 0;
			speed = 0;
			initialSpeed = 0;
			isABoss = false;
			height = 1;
			weightLevel = 1;
			hasSpecial = false;
			isFriendly = true;
			hasMovement = false;
			isSolid = true;
			moveable = false;
			hasItemDrops = false;
			canTurn = false;
			isHuman = false;
		} // This is a turret
		else if (ID == 18) {
			health = 300;
			damage = 5;
			speed = 0;
			initialSpeed = 0;
			isABoss = false;
			height = 3;
			weightLevel = 2;
			hasSpecial = true;
			isFriendly = true;
			hasMovement = false;
			isSolid = true;
			moveable = true;
			hasItemDrops = false;
			tickAmount = 2;
			isHuman = false;

			// If itemActivationID is special, it is an evil turret
			if (this.itemActivationID == -1) {
				isFriendly = false;
				moveable = false;
			} else if (this.itemActivationID == -2) {
				isFriendly = false;
				canTurn = false;
				keepFiring = true;
				moveable = false;
				health = 900000000;
				activated = true;
			}
		}
		// This is a Armored Menace
		else if (ID == 19) {
			health = 500;
			damage = 20;
			speed *= 0.2;
			initialSpeed *= 0.2;
			isABoss = false;
			height = 3;
			weightLevel = 3;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = true;
			isHuman = true;
		}
		// This is a Blind Charger
		else if (ID == 20) {
			health = 150;
			damage = 15;
			speed *= 4;
			initialSpeed *= 4;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = false;
			isHuman = true;
		}

		// This is Damned soul
		else if (ID == 21) {
			health = 30;
			damage = 5;
			speed *= 6;
			initialSpeed *= 6;
			isABoss = false;
			height = 1;
			weightLevel = 1;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = false;
			isHuman = true;
			canFly = true;
		}

		// This is a Dark Strider
		else if (ID == 22) {
			health = 200;
			damage = 10;
			speed = 0.75;
			initialSpeed = 0.75;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = true;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = true;
			tickAmount = 5;
			isHuman = true;
		}

		// This is a Deceptor
		else if (ID == 23) {
			health = 100;
			damage = 15;
			speed = 1;
			initialSpeed = 1;
			isABoss = false;
			height = 2;
			weightLevel = 1;
			hasSpecial = true;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = true;
			isHuman = true;
		}

		// This is a Heavy Zombie
		else if (ID == 24) {
			health = 125;
			damage = 5;
			speed = 1;
			initialSpeed = 1;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = true;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = true;
			isHuman = true;
		}

		// This is a Moss Springer
		else if (ID == 25) {
			health = 100;
			damage = 7;
			speed = 1.2;
			initialSpeed = 1.2;
			isABoss = false;
			height = 2;
			weightLevel = 1;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = false;
			isHuman = true;
		}

		// This is a Tetra Destructor
		else if (ID == 26) {
			health = 250;
			damage = 10;
			speed = 0.5;
			initialSpeed = 0.5;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = true;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = false;
			hasItemDrops = true;
			isHuman = false;
			canFly = true;
		}
		// This is for a explosive canister
		else if (ID == 27) {
			health = 15;
			damage = 0;
			speed = 0;
			initialSpeed = 0;
			isABoss = false;
			height = 2;
			weightLevel = 2;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = false;
			isSolid = true;
			moveable = true;
			hasItemDrops = false;
			isHuman = false;
			canFly = false;
			canTurn = false;
		}
		// This is for a trash can
		else if (ID == 28) {
			health = 30;
			damage = 0;
			speed = 0;
			initialSpeed = 0;
			isABoss = false;
			height = 2;
			weightLevel = 1;
			hasSpecial = false;
			isFriendly = false;
			hasMovement = false;
			isSolid = true;
			moveable = true;
			hasItemDrops = true;
			isHuman = false;
			canFly = false;
			canTurn = false;
		}
		// For ID's that don't exist
		else {
			health = 500;
			damage = 100;
			speed = 0.5;
			initialSpeed = 0.5;
			isABoss = true;
			height = 2;
			weightLevel = 1;
			hasSpecial = true;
			isFriendly = false;
			hasMovement = true;
			isSolid = true;
			moveable = true;
			hasItemDrops = true;
			canTurn = true;
			isHuman = false;
			canFly = true;
		}

		// If easy mode
		if (FPSLauncher.modeChoice == 1) {
			speed *= 0.5;
			damage *= 0.5;
		}
		// If Death cannot touch me or above
		else if (FPSLauncher.modeChoice >= 4) {
			speed *= 1.5;
			damage *= 2;
			health *= 1.5;
		}
		// If Bring it on or above
		else if (FPSLauncher.modeChoice >= 3) {
			health *= 1.25;
			damage *= 1.5;
		}
		// If peaceful mode
		else if (FPSLauncher.modeChoice == 0) {
			// speed = 0;
			damage = 0;
		}

		distanceFromPlayer = Math.sqrt(((Math.abs(getX() - Player.x)) * (Math.abs(getX() - Player.x)))
				+ ((Math.abs(getZ() - Player.z)) * (Math.abs(getZ() - Player.z))));

		// Sets speed to specific units the map uses
		speed /= 21.3;

		// Initial speed is set to be used later
		initialSpeed = speed;

		setHeight();

		// If survival
		if (FPSLauncher.gameMode == 1) {
			activated = true;

			// Depending on ID, play entity activation sound
			switch (ID) {
			case 1:
				SoundController.enemyActivate.playAudioFile(distanceFromPlayer);
				break;

			case 2:
				SoundController.enemy2Activate.playAudioFile(distanceFromPlayer);
				break;

			case 3:
				SoundController.enemy3Activate.playAudioFile(distanceFromPlayer);
				break;

			case 4:
				SoundController.enemy4Activate.playAudioFile(distanceFromPlayer);
				break;

			case 5:
				SoundController.enemy5Activate.playAudioFile(distanceFromPlayer);
				break;

			case 7:
				SoundController.enemy7Activate.playAudioFile(distanceFromPlayer);
				break;

			case 6:
				SoundController.bossActivate.playAudioFile(0);
				break;

			case 8:
				SoundController.belegothActivate.playAudioFile(0);
				break;

			case 9:
				SoundController.enemy2Activate.playAudioFile(distanceFromPlayer);
				break;

			case 11:
				Random rand = new Random();
				int greetingType = rand.nextInt(2);

				if (greetingType == 0) {
					SoundController.marineGreetingStandard.playAudioFile(distanceFromPlayer);
				} else {
					SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
				}

				break;
			case 12:
				Random rand2 = new Random();
				int greetingType2 = rand2.nextInt(2);

				if (greetingType2 == 0) {
					SoundController.marineGreetingStandard.playAudioFile(distanceFromPlayer);
				} else {
					SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
				}

				break;

			case 13:
				Random rand3 = new Random();
				int greetingType3 = rand3.nextInt(2);

				if (greetingType3 == 0) {
					SoundController.psychoGreeting.playAudioFile(distanceFromPlayer);
				} else {
					SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
				}

				break;

			case 14:
				Random rand4 = new Random();
				int greetingType4 = rand4.nextInt(2);

				if (greetingType4 == 0) {
					SoundController.psychoGreeting.playAudioFile(distanceFromPlayer);
				} else {
					SoundController.scaredGreeting.playAudioFile(distanceFromPlayer);
				}

				break;
			}
		}

		super.itself = this;

		Game.enemiesInMap++;
	}

	/**
	 * This is the specific constructor of an enemy. If you want to create a
	 * specific enemy, you can call this constructor and send in the specific values
	 * you want to create the enemy of your choice.
	 */
	public Entity(int health, int armor, int ammo, int damage, int speed, double x, double y, double z, int ID,
			double rotation, int itemActivationID) {
		super(health, armor, ammo, damage, speed, x, y, z, ID, rotation, itemActivationID);
	}

	/**
	 * Initiates an attack on the player when called.
	 */
	public void attack(double yCorrect) {
		// If player is not alive, don't try to attack
		// unless targetEnemy is not the Player but is another enemy
		if (!Player.alive && !newTarget && targetEnemy == null) {
			return;
		}

		// If there is no target enemy, but the entity is friendly, don't attack.
		if (targetEnemy == null && isFriendly) {
			return;
		}

		// If the target is not the player, the yCorrect variable will
		// be the target enemies y position
		if (targetEnemy != null) {
			yCorrect = -targetEnemy.yPos * 11;
		}

		// The entity can fire if its target is in sight, or it is told to keep firing
		if (inSight || keepFiring) {
			/*
			 * Only melee attack once each tick round if within distance of 1 from the
			 * target. The enemy must also be within height range of the target, and have
			 * the target within its sight. Also has to be able to move. Stationary entities
			 * like turrets just shoot still.
			 */
			if (super.distance <= 1 && !isAttacking && hasMovement
					&& Math.abs(Math.abs(yCorrect) - (Math.abs(this.yPos * 11))) <= 12) {
				tick = 0;

				// Only change rotation if it can turn
				if (canTurn) {
					// Angle that the player is in accordance to the enemy so
					// that enemy moves right towards its target
					rotation = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

					/*
					 * If the target is in the 3rd or 4th quadrant of the map then add PI to
					 * rotation so that the enemy will move into the correct quadrant of the map and
					 * at the target.
					 */
					if (targetZ < zPos) {
						rotation += Math.PI;
					}
				}

				// Set melee attacking to true
				super.isAttacking = true;
			}
			/*
			 * Fire at target once each 5 tick rounds if target is in sight and if the enemy
			 * is not already firing. Also the enemy has to have a special attack in order
			 * to activate it. Also to fire the target has to be within its sight, and the
			 * target has to be out of melee attack range.
			 */
			else if (tick == 0 && tickRound == 0) {
				if ((distance > 1 || !hasMovement) && super.activated) {
					if (super.hasSpecial && !super.isFiring && Game.skillMode > 0) {
						// Enemy is in process of firing
						super.isFiring = true;

						// Only change rotation if it can turn
						if (canTurn) {
							// Angle that the player is in accordance to the enemy so
							// that enemy moves right towards its target
							rotation = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

							/*
							 * If the target is in the 3rd or 4th quadrant of the map then add PI to
							 * rotation so that the enemy will move into the correct quadrant of the map and
							 * at the target.
							 */
							if (targetZ < zPos) {
								rotation += Math.PI;
							}
						}
					}
				}
			}
		}
	}

	@Override
	/**
	 * Compares two enemies together depending on their distance from the player,
	 * and then sorts them in increasing distance.
	 */
	public int compareTo(Object enemy) {
		// Gets Integer height of the block being compared
		int comparedInteger = (int) ((Entity) (enemy)).distanceFromPlayer;

		/*
		 * Compares the two blocks being compared, and sends back the result. This
		 * particular comparison would cause the list to be sorted in decending order of
		 * height. If you switched this.height and comparedInteger it would be sorted in
		 * ascending order.
		 */
		return (int) this.distanceFromPlayer - comparedInteger;
	}

	/**
	 * Whenever the enemy is hurt, this is the method that is called to actually
	 * take the enemies health away.
	 * 
	 * @param damage
	 */
	@Override
	public void hurt(double damage, boolean soundPlayed) {
		// Decrease enemies health
		super.health -= damage;

		// Enemy is stopped and shows that is harmed for 10 ticks
		harmed = 5 * Render3D.fpsCheck;

		// Unless the sound has already been played or the enemy is dead
		// now, play the sound that corresponds to the enemy type getting
		// hit
		if (!soundPlayed && super.health > 0) {

			if (ID == 3) {
				SoundController.tankHurt.playAudioFile(distanceFromPlayer);
			} else if (ID == 4 || ID == 5) {
				SoundController.reaperHurt.playAudioFile(distanceFromPlayer);
			} else if (ID == 7) {
				SoundController.vileCivHurt.playAudioFile(distanceFromPlayer);
			} else if (ID == 6 || ID == 8) {
				SoundController.bossHit.playAudioFile(distanceFromPlayer / 2);
			} else if (ID >= 11 && ID <= 15) {
				Random rand = new Random();
				int greetingType = rand.nextInt(5);

				if (greetingType == 0) {
					SoundController.marineHurtMale1.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 1) {
					SoundController.marineHurtMale2.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 2) {
					SoundController.marineHurtMale3.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 3) {
					SoundController.marineHurtMale4.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 4) {
					SoundController.marineHurtMale5.playAudioFile(distanceFromPlayer);
				}
			} else {
				if (hasMovement) {
					SoundController.enemyHit.playAudioFile(distanceFromPlayer);
				}
			}
		}
	}

	/**
	 * If an enemy dies, the items corresponding to that enemy (including random
	 * drops) are spawned in the enemy death location. And 2 enemies are spawned
	 * when in survival mode. Also enemies made happy is added to and everything
	 * else is taken care of.
	 * 
	 * A corpse is also dropped in the enemies made happy location.
	 * 
	 * @param enemy
	 */
	@Override
	public void enemyDeath() {
		// Randomizes enemy items random item drop
		Random random = new Random();

		isAlive = false;

		/*
		 * If enemy is not supposed to be in the game, but is not off of this block due
		 * to an explosion bug, then remove it from the block and exit out of this
		 * method.
		 */
		if (!Game.entities.contains(this)) {
			Block blockOn = Level.getBlock((int) this.xPos, (int) this.zPos);

			blockOn.entitiesOnBlock.remove(this);
			return;
		}

		/*
		 * Each enemy drops a particular item/s
		 */
		// Brainomorph
		if (ID == 1) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHARD.getID(), 0, 0, "");
		}
		// Sentinel
		else if (ID == 2) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHARD.getID(), 0, 0, "");

			new Item(2, xPos, -yPos * 10, zPos, ItemNames.VIAL.getID(), 0, 0, "");
		}
		// Mutated Commando
		else if (ID == 3) {
			for (int z = 0; z < 5; z++) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHARD.getID(), 0, 0, "");
			}

		}
		// Reaper
		else if (ID == 4) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.VIAL.getID(), 0, 0, "");
		}
		// Magistrate
		else if (ID == 5) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.HEALTHPACK.getID(), 0, 0, "");

			new Item(2, xPos, -yPos * 10, zPos, ItemNames.HEALTHPACK.getID(), 0, 0, "");
		}
		// Morgoth
		else if (ID == 6) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.MEGAHEALTH.getID(), 0, 0, "");

			for (int i = 0; i < 10; i++) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.UPGRADE.getID(), 0, 0, "");
			}
		}
		// Vile Warrior
		else if (ID == 7) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHARD.getID(), 0, 0, "");
		}
		// Belegoth
		else if (ID == 8) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.GREENKEY.getID(), 0, 0, "");

			for (int i = 0; i < 10; i++) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.UPGRADE.getID(), 0, 0, "");
			}
		} else if (ID == 9) {
			for (int z = 0; z < 2; z++) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.VIAL.getID(), 0, 0, "");
			}

			new Item(2, xPos, -yPos * 10, zPos, ItemNames.DECIETORB.getID(), 0, 0, "");
		} // Marines
		else if (ID == 10 || ID == 12) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHELLS.getID(), 0, 0, "");
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHOTGUN.getID(), 0, 0, "");
		} else if (ID == 11 || ID == 14 || ID == 15) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.PISTOL.getID(), 0, 0, "");
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.BULLETS.getID(), 0, 0, "");
		} else if (ID == 13) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.PHASECANNON.getID(), 0, 0, "");
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SMALLCHARGE.getID(), 0, 0, "");
		}
		// Toilet
		else if (ID == 17) {
			int toiletRand = random.nextInt(10);

			if (toiletRand == 0) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.GOREPILE1.getID(), 0, 0, "");
			}
		}
		// Turret
		else if (ID == 18) {
			new Item(50, xPos, -yPos * 10, zPos, ItemNames.BOXOFBULLETS.getID(), 0, 0, "");
		}
		// Armored Menace
		else if (ID == 19) {
			new Item(100, xPos, -yPos * 10, zPos, ItemNames.COMBAT.getID(), 0, 0, "");
		}
		// Dark Strider
		else if (ID == 22) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SMALLCHARGE.getID(), 0, 0, "");
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SMALLCHARGE.getID(), 0, 0, "");
		}
		// Deceptor
		else if (ID == 23) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.DECIETSCEPTER.getID(), 0, 0, "");
		}
		// Heavy Zombie
		else if (ID == 24) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.CHAINMEAL.getID(), 0, 0, "");
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHOTGUN.getID(), 0, 0, "");
		}
		// Moss Springer
		else if (ID == 25) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.VIAL.getID(), 0, 0, "");
		}
		// Tetra Destructor
		else if (ID == 26) {
			new Item(2, xPos, -yPos * 10, zPos, ItemNames.ROCKETS.getID(), 0, 0, "");
		}
		// Canister just creates an explosion
		else if (ID == 27) {
			Game.explosions.add(new Explosion(this.xPos, -this.yPos / 10, this.zPos, 1, 0));
		}

		// Create random number from 0 to 99
		int temp = random.nextInt(101);

		// Random drops. Rare chances that they'll drop with
		// Everything else. Only dropped by moving entities
		if (hasItemDrops) {
			if (temp == 10) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.CHAINMEAL.getID(), 0, 0, "");
			} else if (temp == 20) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.SMALLCHARGE.getID(), 0, 0, "");
			} else if (temp == 30) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.VIAL.getID(), 0, 0, "");
			} else if (temp == 40) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.ADRENALINE.getID(), 0, 0, "");
			} else if (temp == 50) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHARD.getID(), 0, 0, "");
			} else if (temp == 60) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.BULLETS.getID(), 0, 0, "");
			} else if (temp == 70) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.SHELLS.getID(), 0, 0, "");
			} else if (temp == 80) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.HEALTHPACK.getID(), 0, 0, "");
			} else if (temp == 90) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.ROCKETS.getID(), 0, 0, "");
			} else if (temp == 100) {
				new Item(2, xPos, -yPos * 10, zPos, ItemNames.UPGRADE.getID(), 0, 0, "");
			}
		}

		// Toilet animation continues to play
		if (ID == 17) {
			Game.corpses.add(new Corpse(xPos, zPos, -yPos * 10, ID, xEffects, zEffects, yEffects, true));
		} else {
			if (ID != 21 && ID != 27) {
				// Add corpse to the map
				Game.corpses.add(new Corpse(xPos, zPos, -yPos * 10, ID, xEffects, zEffects, yEffects, false));
			}
		}

		// If survival mode, add two enemies in its place
		if (FPSLauncher.gameMode == 1) {
			Display.game.addEnemy();
			Display.game.addEnemy();
		}

		// Add to enemies killed
		if (!isFriendly) {
			Player.kills++;
		}

		// Different Death sounds
		if (!isABoss) {
			// Depending on enemy, have a different death sound.
			if (ID == 1) {
				SoundController.enemy1Death.playAudioFile(distanceFromPlayer);
			} else if (ID == 2) {
				SoundController.enemy2Death.playAudioFile(distanceFromPlayer);
			} else if (ID == 3) {
				SoundController.enemy3Death.playAudioFile(distanceFromPlayer);
			} else if (ID == 4 || ID == 5) {
				SoundController.enemy4Death.playAudioFile(distanceFromPlayer);
			} else if (ID == 7) {
				SoundController.enemy7Death.playAudioFile(distanceFromPlayer);
			} else if (ID == 9) {
				// TODO eventually change
				SoundController.enemy2Death.playAudioFile(distanceFromPlayer);
			} else if (ID >= 11 && ID <= 15) {
				Random rand = new Random();
				int greetingType = rand.nextInt(4);

				if (greetingType == 0) {
					SoundController.marineDeathMale1.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 1) {
					SoundController.marineDeathMale2.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 2) {
					SoundController.marineDeathMale3.playAudioFile(distanceFromPlayer);
				} else if (greetingType == 3) {
					SoundController.marineDeathMale4.playAudioFile(distanceFromPlayer);
				}
			} else if (ID == 16 || ID == 28) {
				SoundController.chairBreak.playAudioFile(distanceFromPlayer);
			} else if (ID == 17) {
				SoundController.toiletBreak.playAudioFile(distanceFromPlayer);
			} else if (ID == 18) {
				SoundController.chairBreak.playAudioFile(distanceFromPlayer);
				SoundController.boxBreak1.playAudioFile(distanceFromPlayer);
				SoundController.explosion.playAudioFile(distanceFromPlayer);
			} else {
				if (hasMovement) {
					SoundController.enemy4Death.playAudioFile(distanceFromPlayer);
				}
			}
		}
		// Morgoth death
		else if (ID == 6) {
			SoundController.bossDeath.playAudioFile(distanceFromPlayer / 2);
		}
		// Belegoth death
		else {
			SoundController.belegothDeath.playAudioFile(distanceFromPlayer / 2);
		}

		// Remove enemy from game
		Game.entities.remove(this);

		// If this has stuff to activate
		if (this.itemActivationID > 0) {
			// Search through all the doors
			for (int k = 0; k < Game.doors.size(); k++) {
				Door door = Game.doors.get(k);

				// If door has the same activation ID as the
				// button then activate it.
				if (door.itemActivationID == this.itemActivationID) {
					/*
					 * If the itemActivationID is the special ID, then just stop the door from
					 * automatically opening and closing. Otherwise activate the door as normal.
					 */
					if (door.itemActivationID == 2112) {
						// Hoping no one uses this id, but
						// this stops the door from automatically
						// opening and closing continuously.
						door.itemActivationID = 1221;
					}
					// If this special ID, activate it to continue to
					// move
					else if (door.itemActivationID == 1221) {
						door.itemActivationID = 2112;
						door.activated = true;
						door.stayOpen = false;
					} else {
						door.activated = true;
					}
				}
			}

			// Search through all the elevators
			for (int k = 0; k < Game.elevators.size(); k++) {
				Elevator e = Game.elevators.get(k);

				// If elevator has the same activation ID as the
				// button then activate it.
				if (e.itemActivationID == this.itemActivationID) {
					/*
					 * If the itemActivationID is the special ID, then just stop the elevator from
					 * automatically moving. Otherwise activate the elevator as normal.
					 */
					if (e.itemActivationID == 2112) {
						// Hoping no one uses this id, but
						// this stops the elevator from automatically
						// moving continuously.
						e.itemActivationID = 1221;
					}
					// If this special ID, activate it to continue to
					// move
					else if (e.itemActivationID == 1221) {
						e.itemActivationID = 2112;
						e.activated = true;
					} else {
						e.activated = true;
					}
				}
			}

			// Stores Items to be deleted
			ArrayList<Item> tempItems2 = new ArrayList<Item>();

			// Scan all activatable items
			for (int j = 0; j < Game.activatable.size(); j++) {
				Item item = Game.activatable.get(j);

				if (this.itemActivationID == item.itemActivationID) {
					// If Item is a Happiness Tower, activate it and
					// state that it is activated
					if (item.itemID == ItemNames.RADAR.getID() && !item.activated) {
						item.activated = true;
						Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
						SoundController.uplink.playAudioFile(0);
					} else {
						// If item is enemy spawnpoint, then spawn the
						// enemy, and add the item to the arraylist of
						// items to be deleted
						if (item.itemID == ItemNames.ENEMYSPAWN.getID()) {
							Game.enemiesInMap++;
							Display.game.addEnemy(item.x, item.z, item.rotation);
							tempItems2.add(item);
						}
						// If Explosion has same activation ID of the button
						// then activate it
						else if (item.itemID == ItemNames.ACTIVATEEXP.getID()) {
							new Explosion(item.x, item.y, item.z, 0, 0);
							tempItems2.add(item);
						}
						// If it gets rid of a wall, delete the wall and create an
						// air wall in its place.
						else if (item.itemID == ItemNames.WALLBEGONE.getID()) {
							Block block2 = Level.getBlock((int) item.x, (int) item.z);

							// Block is effectively no longer there
							block2.height = 0;

							block2.wallEntities = null;

							tempItems2.add(item);
						}
					}
				}
			}

			// Remove all the items that need to be deleted now
			for (int j = 0; j < tempItems2.size(); j++) {
				Item temp2 = tempItems2.get(j);

				temp2.removeItem();
			}
		}

		// Block this is on
		Block block = Level.getBlock((int) this.xPos, (int) this.zPos);

		// Remove enemy from list of enemies on block too
		block.entitiesOnBlock.remove(this);
	}
}
