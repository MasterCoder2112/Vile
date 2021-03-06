package com.vile.entities;

import java.util.ArrayList;
import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.SoundController;
import com.vile.graphics.Render;
import com.vile.graphics.Render3D;
import com.vile.graphics.Textures;
import com.vile.input.InputHandler;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Entity
 * 
 * @author Alex Byrd Date Updated: 12/12/2017
 * 
 *         Description: Each Entity is an object in the game that can usually
 *         move, act in certain ways depending on its environment, and will
 *         either fight for, or against the player.
 */
@SuppressWarnings("rawtypes")
public abstract class EntityParent implements Comparable {
	// Defaults
	public double health = 0;
	public int armor = 0;
	public int ammo = 0;
	public int damage = 0;
	public int ID = 0;
	public int tick = 0;
	public int tickRound = 0;
	public int tickAmount = 11;
	public int itemActivationID = 0;
	public int enemyPhase = 0; // Phase enemies textures are in
	public double speed = 0;
	public double xPos = 0;
	public double yPos = 0;
	public double zPos = 0;
	public double harmed = 0;
	public double targetX = Player.x;
	public double targetY = Player.y;
	public double targetZ = Player.z;
	public double rotationFromTarget = 0;
	public double rotationFromPlayer = 0;
	public double rotDifference = 0;
	public double xEffects = 0; // These are explosion effects on entity
	public double yEffects = 0;
	public double zEffects = 0;
	public Render currentPhase = Textures.enemy1;

	// Machine gun marines will shoot several times in a row
	public int machineGunShots = 0;

	// Initial y value of enemy
	public double startY = 2;

	// Initial speed
	public double initialSpeed = 0;

	// Corrects height that the enemy should actually be at
	// public double heightCorrect = 0;

	// What walking phase is the enemy in, and which direction in
	// degrees is the enemy moving
	public double direction = 0;

	// What is the acceleration down
	public double acceleration = 0.03;

	// What is the speed that the enemy is currently falling
	public double fallSpeed = 1;

	// How heavy is the enemy
	public int weightLevel = 1;

	// Pixels enemy takes up on screen (TAKEN OUT FOR NOW)
	// public ArrayList<Boolean> pixelsOnScreen = new
	// ArrayList<Boolean>(Display.WIDTH * Display.HEIGHT);

	public boolean canBeSeen = false;

	// Rotation in the map in correspondence with the entity
	public double rotation = 0;

	// Distance From player
	public double distanceFromPlayer = -1;
	public double distance = -1;

	// Height of entity
	public int height = 8;

	// Max height the entity can stand on
	public double maxHeight = 0;

	// Used for watchers, and maybe more in the future. Once player stares at enemy
	// for too long it
	// may do something.
	public int playerStareTime = 0;

	// Modulus with decimals doesn't work, so I have to try this.
	public int sentryBeepTimer = 0;

	/*
	 * Basically if this is an enemy, it is itself. This is only here so that this
	 * can call the enemy methods for itself in order to make it easier to call
	 * enemy actions only for this particular enemy. Otherwise I'd have to do a
	 * bunch of case statements and casting which I'd rather not do.
	 */
	public Entity itself = null;
	// public Marine itself2 = null //Marine type. Next version

	public Entity targetEnemy = null; // Enemy target for friendlies or enemies angry at another
	public Corpse targetCorpse = null; // Corpse target for resurrectors
	public Item targetItem = null; // Is the Entity targetting an item
	public Eyesight eyeSight = null; // An entities eyesight

	// Entity Flags (Many will be used in later updates)
	public boolean killable = false; // Is entity killable by the player?
	public boolean hasItemDrops = false; // Does this entity drop items
	public boolean hasMovement = true; // Can entity move, or is it stationary
	public boolean canTurn = true; // Can entity turn if it can't move?
	public boolean moveable = false; // Can Player push it around
	public boolean isGhost = false; // Is the entity translucent and can go through object
	public boolean isHuman = true; // Is the entity flesh and blood or other
	public boolean keepFiring = false;
	public boolean canActivate = false; // Are conditions met for entity to activate
	public boolean canTeleport = false; // Can enemy teleport?
	public boolean canFly = false; // Can enemy fly?
	public boolean initialActivation = false; // For friendlies so they aren't immediately activated.
	public boolean activated = false; // Tracking target yet?
	public boolean hasSpecial = false; // Has special attack
	public boolean isFiring = false; // Is entity in the process of firing
	public boolean isAttacking = false; // Is entity melee attacking
	public boolean canResurrect = false; // Can entity resurrect other entities
	public boolean withinSight = false; // Is entity within the players sight
	public boolean relocating = false; // If friendly does not hit enemy, try moving and trying again.
	public static boolean checkSight = false; // Does entity have permission to check its sight yet?
	public boolean isStuck = false; // Is entity stuck?
	public boolean isABoss = false; // Is entity a boss?
	public boolean inSight = false; // Is target in sight?
	public boolean searchMode = false; // Is the entity pathfinding the target?
	public boolean isAlive = true; // Is entity alive?
	public boolean isFriendly = false; // Is entity freindly to the player or not?
	public boolean tracking = false; // Is flying enemy tracking the target vertically
	public boolean newTarget = false; // Is target not player?
	public boolean isSolid = true; // Only used for friendly entities. Will entity always be solid or not
	public boolean pursuant = true; // Does entity stay back and fire from distance or pursue their target?
	public boolean moving = false;
	private boolean doorway = false; // Is the entity going through a doorway?
	private boolean firstTime = true; // Sets up primary settings the first tick through

	/**
	 * Instantiates the type of entity
	 * 
	 * @param health
	 * @param armor
	 * @param ammo
	 * @param damage
	 * @param speed
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 */
	public EntityParent(int health, int armor, int ammo, int damage, double speed, double x, double y, double z, int ID,
			double rotation, int itemActivationID) {
		this.health = health;
		this.armor = armor;
		this.ammo = ammo;
		this.damage = damage;
		this.speed = speed;
		this.ID = ID;
		this.rotation = rotation;
		this.itemActivationID = itemActivationID;
		xPos = x;
		yPos = y;
		zPos = z;
	}

	// Gets the x position of the entity
	public double getX() {
		return xPos;
	}

	// Gets the y position.
	public double getY() {
		return yPos;
	}

	// Gets the z position
	public double getZ() {
		return zPos;
	}

	/**
	 * Continues to tick the entity and update its action
	 */
	public void tick(EntityParent currentEnemy) {
		// If first time tick is called don't update enemy values yet
		// tell the enemy is told not to activate yet by the move
		// method.
		if (firstTime) {
			setHeight();
			firstTime = false;
			return;
		}

		if (activated) {
			initialActivation = true;
		}

		// Only friendly entities that can move/turn should attack.
		if (isFriendly && (!moveable || ID == 18) && initialActivation) {
			newTarget = true;

			// if (targetEnemy == null) {
			// Go through enemies from closest to farthest.
			for (int i = Game.entities.size() - 1; i >= 0; i--) {
				if (!Game.entities.get(i).isFriendly && !Game.entities.get(i).equals(currentEnemy)
						&& (Game.entities.get(i).inSight || Game.entities.get(i).canBeSeen)) {
					activated = true;
					// Check for nearest enemy IF that enemy is not friendly already
					targetEnemy = Game.entities.get(i);
				}
			}
			// }
		}

		this.tick++;

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

		// If still just recently hit, keep pose the same
		// and keep counting down.
		if (harmed > 0) {
			harmed--;
		}

		// If the entity is freindly and there is no target enemy, just deactivate when
		// close to player so it never bothers
		// the player movement.
		if (isFriendly && targetEnemy == null && initialActivation) {
			if (distanceFromPlayer <= 2) {
				activated = false;
			} else {
				activated = true;
			}
		}

		// If target enemy is dead or if it turned friendly
		if (targetEnemy != null && (targetEnemy.health <= 0 || (targetEnemy.isFriendly && isFriendly))) {
			targetEnemy = null;
			targetX = Player.x;
			targetY = Player.z;
			targetZ = Player.y;
		}

		// If not target entity, is there a target Item?
		if (targetItem != null) {
			targetX = targetItem.x;
			targetY = targetItem.y;
			targetZ = targetItem.z;
		} else {
			if (Display.smileMode && ID == 100) {
				targetX = Player.x;
				targetZ = Player.z;
				targetY = 0;
			}
		}

		/*
		 * Determines distance between the enemy and the player by finding the
		 * hypotenuse between the x direction and z from the player to the enemy being
		 * scanned.
		 */
		distanceFromPlayer = Math.sqrt(((Math.abs(this.getX() - Player.x)) * (Math.abs(this.getX() - Player.x)))
				+ ((Math.abs(this.getZ() - Player.z)) * (Math.abs(this.getZ() - Player.z))));

		// Same but for the entities current target
		distance = Math.sqrt(((Math.abs(this.getX() - targetX)) * (Math.abs(this.getX() - targetX)))
				+ ((Math.abs(this.getZ() - targetZ)) * (Math.abs(this.getZ() - targetZ))));

		determineImage();

		// If its been a few ticks and its time to check the line of sight
		// again.
		if (checkSight && (targetEnemy != null || Player.invisibility == 0)) {
			/*
			 * Figures out if Player is within the enemies view range being from 60 degrees
			 * to the right and left of the enemies centered view.
			 */
			double plus60 = rotation + ((Math.PI / 180) * 60);
			double minus60 = rotation - ((Math.PI / 180) * 60);
			double tempTarget = rotationFromTarget;
			withinSight = false;

			/*
			 * In the case that adding 60 degrees goes beyond 360 degrees, correct it for
			 * checking purposes
			 */
			if (plus60 > (2 * Math.PI) - (1 / 180) && tempTarget <= (Math.PI / 3)) {
				plus60 = plus60 - (2 * Math.PI);
			}

			/*
			 * In the case that subtracting 60 makes the degrees for checking purposes less
			 * than 0, then correct the players rotation from target for checking purposes
			 * only. Put it in range of the enemies range of sight.
			 */
			if (tempTarget < (2 * Math.PI) && tempTarget > (3 * Math.PI) / 2 && minus60 < 0) {
				tempTarget = tempTarget - (2 * Math.PI);
			}

			// If Player could be in sight. But its within the cone of
			// sight.
			if (tempTarget >= minus60 && tempTarget <= plus60) {
				withinSight = true;
			}

			// Angle that the target is in accordance to the entity so
			// that entity looks right towards its target
			// sin/cos in this case
			double sightRotation = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

			/*
			 * If the target is in the 3rd or 4th quadrant of the map then add PI to
			 * rotation so that the enemy will move into the correct quadrant of the map and
			 * at the target.
			 */
			if (targetZ < zPos) {
				sightRotation += Math.PI;
			}

			/*
			 * Corrects rotation so that the enemy is centered correctly in the map graph
			 */
			double correction = 44.765;

			// Speed that the eyesight travels
			double speed = 0.1;

			/*
			 * Eyesight trajectory of the entity so that it will be checking a straight line
			 * of sight to the player.
			 */
			double sightX = ((Math.cos(sightRotation - correction)) + (Math.sin(sightRotation - correction))) * speed;
			double sightZ = ((Math.cos(sightRotation - correction)) - (Math.sin(sightRotation - correction))) * speed;

			// How much the eyesight moves total each check.
			// The hypotenuse of the x and z movement
			double moveDistance = Math.sqrt((sightX * sightX) + (sightZ * sightZ));

			// Total hypotenuse between target and entity
			double hypot = Math.sqrt(((xPos - targetX) * (xPos - targetX)) + ((zPos - targetZ) * (zPos - targetZ)));

			// Difference between entity and target y values
			// Entity is corrected for factor of 11
			double yDifference = Math.abs(yPos * 11) - Math.abs(targetY);

			// Number of moves the eyesight will check for if it reaches
			// the target successfully
			double iterations = hypot / moveDistance;

			// How much y will have to change each time
			double sightY = yDifference / iterations;
			// TODO heres

			// Y position of entity. Height is corrected by factor of 11
			double yCorrect = ((this.yPos) * 11);

			// Flying enemies sometimes have an eyesight issue going through
			// the ceiling, so this corrects it for that case.
			if (canFly) {
				yCorrect -= 2;
			}

			// If a boss, they can see from higher up since they are taller
			if (isABoss) {
				yCorrect -= 8;
			}

			// Resets the eyesight object with its new values
			eyeSight = new Eyesight(this.xPos, this.zPos, yCorrect, targetX, targetZ, targetY, sightX, sightZ, sightY);

			// Checks to see if target is in its line of sight
			inSight = eyeSight.checkEyesight();
		}

		// Only if the enemy has you directly in its line of sight
		// or if the enemy is not behind a wall and you fire.
		if (!activated && FPSLauncher.modeChoice > 0
				&& ((Player.alive && !isFriendly) || targetEnemy != null || (isFriendly && !initialActivation))) {
			/*
			 * If the enemy is already not activated and also the gamemode is not peaceful,
			 * and the player is alive, and theres more than 5 pixels seen by the player at
			 * least (so that if seen through a texture glitch it won't accidently activate
			 * the enemy through walls. After those conditions have been met, the player has
			 * to either be within the enemies sight, or the player has to fire a gun to
			 * activate the enemy. Watchers will not activate within these conditions.
			 */
			if ((((inSight && (withinSight || InputHandler.wasFired)) || distanceFromPlayer <= 1) && ID != 9)
					|| (ID == 9 && playerStareTime >= 200)) {

				activated = true;
				initialActivation = true;

				// Depending on ID, play entity activation sound
				switch (ID) {
				case 6:
					try {
						SoundController.bossActivate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 8:
					try {
						SoundController.belegothActivate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 1:
					try {
						SoundController.enemyActivate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 2:
					try {
						SoundController.enemy2Activate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 3:
					try {
						SoundController.enemy3Activate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 4:
					try {
						SoundController.enemy4Activate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 5:
					try {
						SoundController.enemy5Activate.playAudioFile(0);
					} catch (Exception ex) {

					}
					break;

				case 7:
					try {
						SoundController.enemy7Activate.playAudioFile(0);
					} catch (Exception ex) {

					}
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
						SoundController.marineGreetingStandard.playAudioFile(distanceFromPlayer);
					} else {
						SoundController.scaredGreeting.playAudioFile(distanceFromPlayer);
					}

					break;

				case 15:
					Random rand5 = new Random();
					int greetingType5 = rand5.nextInt(2);

					if (greetingType5 == 0) {
						SoundController.marineGreetingStandard.playAudioFile(distanceFromPlayer);
					} else {
						SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
					}

					break;

				default:
					// TODO For now it just plays like enemy 2
					try {
						if (hasMovement) {
							SoundController.enemy2Activate.playAudioFile(0);
						}
					} catch (Exception ex) {

					}
					break;
				}
			}
		}

		// If entity is not activated currently, it should not be moving, so no movement
		// animation should be playing
		if (!activated) {
			moving = false;
		}

		// If the turret is freindly, it can be moved. This is so if its initially evil
		// and the player makes it freindly, it can be moved like usual.
		if (ID == 18 && isFriendly) {
			moveable = true;
		}

		// Every so many ticks, reset time. Depends on the games fps
		if (this.tick >= tickAmount * Render3D.fpsCheck) {
			this.tick = 0;
			tickRound++;

			// Every 5 sets of 10 ticks, reset the tick round count
			if (tickRound == 5) {
				tickRound = 0;

				// Marines slowly heal themselves over time. They aren't dumb monsters.
				if (ID >= 10 && ID <= 14 && health < 150) {
					health++;
				}
			}

			// If melee attacking, end the attack at the end of each tick
			// round and hurt the target accordingly if still in range
			if (isAttacking) {
				// If target is Player
				if (distanceFromPlayer <= 1 && targetEnemy == null && !isFriendly && hasMovement) {
					Player.hurtPlayer(damage);

					if (Player.health <= 0) {
						if (!Display.smileMode) {
							switch (ID) {
							case (1):
								Display.messages.add(new PopUp("You were chomped by a Brainomorph!"));
								break;
							case (2):
								Display.messages.add(new PopUp("You were ground to a pulp by a Sentinel!"));
								break;
							case (3):
								Display.messages.add(new PopUp("You were turned to ash by a Mutated Commando!"));
								break;
							case (4):
								Display.messages.add(new PopUp("You were taken to the underworld by a Reaper!"));
								break;
							case (5):
								Display.messages.add(new PopUp("You were sacrificed by a Magistrate!"));
								break;
							case (6):
								Display.messages.add(new PopUp("Morgoth has made you his minion!"));
								break;
							case (7):
								Display.messages.add(new PopUp("You were slashed by a Vile Warrior!"));
								break;
							case (8):
								Display.messages.add(new PopUp("Belegoth utterly decimated you!"));
								break;
							case (9):
								Display.messages.add(new PopUp("The Watcher saw you..."));
								break;
							case (10):
								Display.messages.add(new PopUp("Turned against you she was..."));
								break;
							case (11):
								Display.messages.add(new PopUp("You were shotgunned by an evil marine!"));
								break;
							case (12):
								Display.messages.add(new PopUp("You were shotgunned by an evil marine!"));
								break;
							case (13):
								Display.messages.add(new PopUp("You were phased into oblivion!"));
								break;
							case (14):
								Display.messages.add(new PopUp("You were killed by the wussy marine! Wow!"));
								break;
							case (18):
								Display.messages.add(new PopUp("Those Turrets are sure dangerous!"));
								break;
							case (19):
								Display.messages.add(new PopUp("You got smashed by an Armored Menace!"));
								break;
							case (20):
								Display.messages.add(new PopUp(
										"Just because the blind can't see doesn't mean they can't kill you!"));
								break;
							case (21):
								Display.messages.add(new PopUp("Your soul was damned by the Damned Soul!"));
								break;
							case (22):
								Display.messages.add(new PopUp("You got disintigrated by a Dark Strider!"));
								break;
							case (23):
								Display.messages.add(new PopUp("You have been turned by a Deceptor!"));
								break;
							case (24):
								Display.messages.add(new PopUp("You were gunned down by a Heavy Zombie!"));
								break;
							case (25):
								Display.messages.add(new PopUp("You were dragged to the ground by a Moss Springer!"));
								break;
							case (26):
								Display.messages
										.add(new PopUp("The Tetra Destructor see's all, and it also destroys all!"));
								break;
							default:
								Display.messages.add(new PopUp("You were killed by the enemy!"));
								break;
							}
						} else {
							switch (ID) {
							case (1):
								Display.messages.add(new PopUp("Crying man brought you to tears!"));
								break;
							case (2):
								Display.messages.add(new PopUp("You became confused by the Confuser!"));
								break;
							case (3):
								Display.messages.add(new PopUp("That was not sick of him to do to you!"));
								break;
							case (4):
								Display.messages.add(new PopUp("Anger has filled you with too much rage!"));
								break;
							case (5):
								Display.messages.add(new PopUp("You became depressed by another depressed person!"));
								break;
							case (6):
								Display.messages.add(new PopUp("Trauma has caused you to become bewildered!"));
								break;
							case (7):
								Display.messages.add(new PopUp("You were made sad by a sad guy!"));
								break;
							case (8):
								Display.messages.add(new PopUp("Angorth has made you better... haha"));
								break;
							case (9):
								Display.messages.add(new PopUp("Paranoia has seen you..."));
								break;
							case (10):
								Display.messages.add(new PopUp("They were just trying to help... right?"));
								break;
							case (11):
								Display.messages.add(new PopUp("They were just trying to help... right?"));
								break;
							case (12):
								Display.messages.add(new PopUp("They were just trying to help... right?"));
								break;
							case (13):
								Display.messages.add(new PopUp("They were just trying to help... right?"));
								break;
							case (14):
								Display.messages.add(new PopUp("They were just trying to help... right?"));
								break;
							case (17):
								Display.messages.add(new PopUp("That turret wasn't shooting love!"));
								break;
							default:
								Display.messages.add(new PopUp("You were made depressed by the enemy!"));
								break;
							}
						}
					}
				}
				// If target is not player, then the enemy attacks the
				// other enemy, and causes that enemy to want to attack
				// it now.
				else if (distance <= 1 && targetEnemy != null) {
					targetEnemy.hurt(damage, false);
					targetEnemy.targetEnemy = (Entity) currentEnemy;
					targetEnemy.activated = true;

					// If enemy is dead
					if (targetEnemy.health <= 0) {
						targetEnemy.enemyDeath();
					}
				}

				try {
					SoundController.enemyFire.playAudioFile(distanceFromPlayer);
				} catch (Exception ex) {

				}

				isAttacking = false;
			}

			/*
			 * Activates enemies special power only at the end of the tick cycle. Also the
			 * enemy has to be in the process of firing for this to occur.
			 */
			if (isFiring) {
				isFiring = false;

				try {
					SoundController.enemyFire.playAudioFile(distanceFromPlayer);
				} catch (Exception e) {

				}

				// Generates a random number to see if the projectile will be a
				// critical hit or not
				Random rand = new Random();
				int randInt = rand.nextInt(20);

				boolean criticalHit = false;

				// If value of 0, be a critical hit
				if (randInt == 0) {
					criticalHit = true;
				}

				// Default projectile
				int tempID = 4;

				// If sentinel, have different type of projectile
				if (ID == 2) {
					tempID = 5;
				}

				// If Watcher
				if (ID == 9) {
					EnemyFire temp = new EnemyFire(10, 0.12, xPos, (yPos - 0.3) * 10, zPos, 8, targetX, targetZ,
							-targetY, 0, (Entity) currentEnemy, criticalHit);

					Game.enemyProjectiles.add(temp);
				}

				// If Mutated Commando shoot in 3 directions
				else if (ID == 3) {
					// Create new Projectile object with given parameters
					EnemyFire temp = new EnemyFire(15, 0.1, xPos, (yPos - 0.1) * 10, zPos, 6, targetX, targetZ,
							-targetY, 0, (Entity) currentEnemy, criticalHit);

					EnemyFire temp2 = new EnemyFire(15, 0.1, xPos, (yPos - 0.1) * 10, zPos, 6, targetX, targetZ,
							-targetY, Math.PI / 8, (Entity) currentEnemy, criticalHit);

					EnemyFire temp3 = new EnemyFire(15, 0.1, xPos, (yPos - 0.1) * 10, zPos, 6, targetX, targetZ,
							-targetY, -Math.PI / 8, (Entity) currentEnemy, criticalHit);

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
				} else if (ID == 5 && !canResurrect) {
					canResurrect = true;
				}
				// Bosses shoot in 5 directions
				else if (ID == 6 || ID == 8) {
					// Random type of projectile each time
					Random random = new Random();
					int type = random.nextInt(9);

					EnemyFire temp = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, 0, (Entity) currentEnemy, criticalHit);

					EnemyFire temp2 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp3 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, -Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp4 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, Math.PI / 8, (Entity) currentEnemy, criticalHit);

					EnemyFire temp5 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, -Math.PI / 8, (Entity) currentEnemy, criticalHit);

					EnemyFire temp6 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, Math.PI / 4, (Entity) currentEnemy, criticalHit);

					EnemyFire temp7 = new EnemyFire(20, 0.12, xPos, (yPos - 3) * 10, zPos, type, targetX, targetZ,
							-targetY, -Math.PI / 4, (Entity) currentEnemy, criticalHit);

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
					Game.enemyProjectiles.add(temp4);
					Game.enemyProjectiles.add(temp5);
					Game.enemyProjectiles.add(temp6);
					Game.enemyProjectiles.add(temp7);
				} else if (ID == 10 || ID == 12) {

					int weaponDamage = 15;

					// If the marines are evil, the damage done is lessened since the player
					// is technically more fragile.
					if (!isFriendly) {
						weaponDamage /= 3;
					}

					EnemyFire temp = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					EnemyFire temp2 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 32, (Entity) currentEnemy, criticalHit);

					EnemyFire temp3 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 32, (Entity) currentEnemy, criticalHit);

					EnemyFire temp4 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp5 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp6 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 8, (Entity) currentEnemy, criticalHit);

					EnemyFire temp7 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 8, (Entity) currentEnemy, criticalHit);

					SoundController.shoot.playAudioFile(distanceFromPlayer);

					Random random = new Random();
					int randomYell = random.nextInt(3);

					if (randomYell == 0) {
						SoundController.dieVileScum.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 1) {
						SoundController.eatLead.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 2) {
						SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
					}

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
					Game.enemyProjectiles.add(temp4);
					Game.enemyProjectiles.add(temp5);
					Game.enemyProjectiles.add(temp6);
					Game.enemyProjectiles.add(temp7);
				} else if (ID == 11 || ID == 14 || ID == 15) {

					int weaponDamage = 15;

					// If the marines are evil, the damage done is lessened since the player
					// is technically more fragile.
					if (!isFriendly) {
						weaponDamage /= 3;
					}

					EnemyFire temp = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					SoundController.pistol.playAudioFile(distanceFromPlayer);

					// Only yell when he first shoots the weapon
					if (machineGunShots == 0) {
						Random random = new Random();
						int randomYell = random.nextInt(3);

						// Scared marine has unique line
						if (ID == 14) {
							randomYell = random.nextInt(4);
						}

						if (randomYell == 0) {
							SoundController.dieVileScum.playAudioFile(distanceFromPlayer);
						} else if (randomYell == 1) {
							SoundController.eatLead.playAudioFile(distanceFromPlayer);
						} else if (randomYell == 2) {
							SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
						} else if (randomYell == 2) {
							SoundController.scaredMarineShoot.playAudioFile(distanceFromPlayer);
						}
					}

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);

					// Tick how many shots were taken. Stop shooting if enemy dies.
					if (machineGunShots <= 4 && targetEnemy != null && targetEnemy.isAlive) {
						isFiring = true;
						machineGunShots++;
					} else {
						machineGunShots = 0;
					}
				} else if (ID == 13) {

					int weaponDamage = 120;

					// If the marines are evil, the damage done is lessened since the player
					// is technically more fragile.
					if (!isFriendly) {
						weaponDamage /= 4;
					}

					EnemyFire temp = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 2, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					SoundController.phaseShot.playAudioFile(distanceFromPlayer);

					Random random = new Random();
					int randomYell = random.nextInt(5);

					if (randomYell == 0) {
						SoundController.dieVileScum.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 1) {
						SoundController.eatLead.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 2) {
						SoundController.psychoLaugh2.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 3) {
						SoundController.psychoLaugh1.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 4) {
						SoundController.youLikeMyCannon.playAudioFile(distanceFromPlayer);
					}

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
				} else if (ID == 18) {
					EnemyFire temp = null;

					if (this.itemActivationID == -2) {
						criticalHit = false;
					}
					SoundController.pistol.playAudioFile(distanceFromPlayer);

					temp = new EnemyFire(5, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, rotation, 0, (Entity) currentEnemy,
							criticalHit);

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
				} else if (ID == 22) {
					EnemyFire temp = new EnemyFire(10, 0.15, xPos, (yPos - 0.3) * 10, zPos, 2, targetX, targetZ,
							-targetY, 0, (Entity) currentEnemy, criticalHit);

					SoundController.phaseCannonHit.playAudioFile(distanceFromPlayer);

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
				} else if (ID == 23) {
					Entity temp = new Entity(this.xPos + 0.5, this.yPos, this.zPos + 0.5, 21, this.rotation, -1);
					temp.targetEnemy = this.targetEnemy;

					Block tempBlock = Level.getBlock((int) temp.xPos, (int) temp.zPos);
					Game.entities.add(temp);
					tempBlock.entitiesOnBlock.add(temp);
				} else if (ID == 24) {

					int weaponDamage = 5;

					EnemyFire temp = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					EnemyFire temp2 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 32, (Entity) currentEnemy, criticalHit);

					EnemyFire temp3 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 32, (Entity) currentEnemy, criticalHit);

					EnemyFire temp4 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp5 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 16, (Entity) currentEnemy, criticalHit);

					EnemyFire temp6 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, Math.PI / 8, (Entity) currentEnemy, criticalHit);

					EnemyFire temp7 = new EnemyFire(weaponDamage, 0.2, xPos, (yPos - 0.3) * 10, zPos, 1, targetX,
							targetZ, -targetY, -Math.PI / 8, (Entity) currentEnemy, criticalHit);

					SoundController.shoot.playAudioFile(distanceFromPlayer);

					Random random = new Random();
					int randomYell = random.nextInt(2);

					if (randomYell == 0) {
						SoundController.eatLead.playAudioFile(distanceFromPlayer);
					} else if (randomYell == 1) {
						SoundController.rockAndRoll.playAudioFile(distanceFromPlayer);
					}

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
					Game.enemyProjectiles.add(temp4);
					Game.enemyProjectiles.add(temp5);
					Game.enemyProjectiles.add(temp6);
					Game.enemyProjectiles.add(temp7);
				} else if (ID == 26) {

					int weaponDamage = 15;

					EnemyFire temp = new EnemyFire(weaponDamage, 0.1, xPos, (yPos - 0.3) * 10, zPos, 6, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					EnemyFire temp2 = new EnemyFire(weaponDamage, 0.1, xPos, (yPos - 0.3) * 10, zPos, 6, targetX,
							targetZ, -targetY, Math.PI / 2, (Entity) currentEnemy, criticalHit);

					EnemyFire temp3 = new EnemyFire(weaponDamage, 0.1, xPos, (yPos - 0.3) * 10, zPos, 6, targetX,
							targetZ, -targetY, -Math.PI / 2, (Entity) currentEnemy, criticalHit);

					EnemyFire temp4 = new EnemyFire(weaponDamage, 0.1, xPos, (yPos - 0.3) * 10, zPos, 6, targetX,
							targetZ, -targetY, Math.PI, (Entity) currentEnemy, criticalHit);

					// Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
					Game.enemyProjectiles.add(temp4);
				} else {
					int tempDamage = 5;

					if (ID == 2) {
						tempDamage = 8;
					}
					// Create new Projectile object with given parameters
					EnemyFire temp = new EnemyFire(tempDamage, 0.1, xPos, (yPos - 0.3) * 10, zPos, tempID, targetX,
							targetZ, -targetY, 0, (Entity) currentEnemy, criticalHit);

					// Add Projectile to the Game events
					Game.enemyProjectiles.add(temp);
				}
			}
		}
	}

	/**
	 * Determines whether the entity is free to move to the next space or not.
	 * 
	 * @param xx
	 * @param zz
	 * @return
	 */
	public boolean isFree(double nextX, double nextZ) {
		double z = 0.3;

		// Bosses have wider hit boxes so they have to be farther from
		// walls
		if (isABoss) {
			z = 1;
		}

		// Dont let entity exit the map
		if (nextX < 0 || nextX > Level.width || nextZ < 0 || nextZ > Level.height) {
			return false;
		}

		/*
		 * Determine the block the entity is about to move into given the direction that
		 * it is going. Then set this block as the block to check the collision of.
		 * Technically it actually checks two blocks though. The two blocks that are in
		 * the direction that the entity is going. So in case the enemy is moving to a
		 * position in between two blocks, and not directly at the block, it will make
		 * sure the entity cannot move through
		 */
		Block block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
		Block block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));

		// Making the ifs like this makes it slightly faster
		if (nextZ == zPos) {
			if (nextX < xPos) {
				block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
				block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
			} else {
				block = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
				block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
			}
		} else {
			if (nextZ >= zPos) {
				block = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
				block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
			} else {
				block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
				block2 = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
			}
		}

		try {
			// If not reaper enemy or corpse entity
			if (!isGhost) {
				// Go through all the enemies on the block
				for (int i = 0; i < block.entitiesOnBlock.size(); i++) {
					EntityParent temp = block.entitiesOnBlock.get(i);

					// If entity is not in the game, remove it from the block
					// This is due to a rocket bug.
					if (!Game.entities.contains(temp)) {
						block.entitiesOnBlock.remove(temp);
					}

					// Distance between enemy and other enemy
					double distance = Math.sqrt(((Math.abs(temp.xPos - nextX)) * (Math.abs(temp.xPos - nextX)))
							+ ((Math.abs(temp.zPos - nextZ)) * (Math.abs(temp.zPos - nextZ))));

					// If close enough, don't allow the entity to move into
					// the other enemies. Entity can still move if 8 units above
					// The other entity. Can move into the entity if it is a
					// reaper or a corpse.
					if (distance <= 0.5 && !this.equals(temp) && Math.abs(this.yPos - temp.yPos) <= 0.8) {
						return false;
					}
				}
			}
		} catch (Exception e) {

		}

		// Distance between enemy and player
		double distance = Math.sqrt(((Math.abs(Player.x - nextX)) * (Math.abs(Player.x - nextX)))
				+ ((Math.abs(Player.z - nextZ)) * (Math.abs(Player.z - nextZ))));

		// Players y value
		double playerY = Player.y;

		// Correct it for if the player is crouching and has a y less than
		// 0
		if (playerY < 0) {
			playerY = 0;
		}

		// Difference between the the two entities y values
		double yDifference = Math.abs(playerY - Math.abs(yPos * 11));

		// Can't clip inside player
		if ((distance <= 0.5 && ((yDifference <= 8)) || (isABoss && distance <= 3))) {
			return false;
		}

		/*
		 * For the current block, check to see if the enemy can move through or onto the
		 * block. If a solid block, check whether it can move onto it using the
		 * collisionChecks method. If not solid, then check to see if the air block has
		 * a solid item (torch, lamp, etc...) on it (as long as it is not a tree I made
		 * those able to be moved through for the reason that forests occur in my maps
		 * sometimes and they wouldn't be able to move in those circumstances) and if
		 * there is treat it as a normal solid block so the entity doesn't get stuck in
		 * the the item. Unless the enemy is above the item of course.
		 */
		if (block.isSolid || block2.isSolid) {
			return collisionChecks(block, nextX, nextZ) && collisionChecks(block2, nextX, nextZ);
		} else {
			try {
				// If not reaper
				if (!isGhost) {
					for (Item temp : block.wallItems) {
						double dist = Math.sqrt(((Math.abs(temp.x - nextX)) * (Math.abs(temp.x - nextX)))
								+ ((Math.abs(temp.z - nextZ)) * (Math.abs(temp.z - nextZ))));

						// If there is a solid item on the block, and its not a
						// tree, and its within the y value of the entity, and
						// the entity is not a reaper, the entity can't move into that
						// block
						if (temp.isSolid && Math.abs(temp.y + (yPos * 10)) <= temp.height && dist <= 0.3) {
							isStuck = true;
							return false;
						}
					}
				}
			} catch (Exception E) {

			}

			// Cannot fall off a block more than approx. 2 units high
			// Unless the enemy can fly
			if (-yPos > 0.4 && !canFly) {
				isStuck = true;
				return false;
			}
		}

		isStuck = false;

		return true;
	}

	/**
	 * Frees up code space and makes it easier to make changes to all the collision
	 * checks at once just changing just one method.
	 * 
	 * Optimizes code.
	 * 
	 * @param block
	 * @return
	 */
	public boolean collisionChecks(Block block, double nextX, double nextZ) {
		/*
		 * The entity can't move forward anyway if the block its moving to has a solid
		 * object on it
		 */
		try {
			if (!isGhost) {
				for (Item temp : block.wallItems) {
					double dist = Math.sqrt(((Math.abs(temp.x - nextX)) * (Math.abs(temp.x - nextX)))
							+ ((Math.abs(temp.z - nextZ)) * (Math.abs(temp.z - nextZ))));

					// If there is a solid item on the block, and its not a
					// tree, and its within the y value of the entity, and
					// the entity is not a reaper, the entity can't move into that
					// block
					if (temp.isSolid && Math.abs(temp.y + (yPos * 10)) <= temp.height && dist <= 0.3) {
						isStuck = true;
						return false;
					}
				}
			}
		} catch (Exception E) {

		}

		// The top of the block the entity is moving onto
		double topOfBlock = (block.height + (block.y * 4) + block.baseCorrect) / 11;

		/*
		 * If the top of the block (minus 4 units in case it's stairs. By the way 4
		 * units is a height of 2 plus correct for the baseCorrect added) is higher than
		 * the entities y, and the bottom of the block(the blocks y) is lower than the
		 * entities y plus the entities height, then the entity is trying to move into
		 * the block and can't. Also, it cannot move forward if the top of the block is
		 * more than 4 units below the entity. That is too big of a fall for the
		 * entities. Doesn't worry about that second part though if the entity can fly.
		 */
		if (((topOfBlock) - 0.3 > -yPos && (-yPos + height) >= (block.y * 4))
				|| ((topOfBlock) + 0.3 < -yPos && !canFly)) {
			// TODO Maybe revert back to old code if the above doesn't work
			// System.out.println(-yPos + " : " + topOfBlock + " : " + height + " : " +
			// block.y + " : " + block.height
			// + " : " + block.baseCorrect);
			isStuck = true;
			return false;
		}

		// TODO check

		// Default is that the entity can move
		isStuck = false;

		return true;
	}

	/**
	 * Determines the entities image depending on its rotation to the player.
	 */
	public void determineImage() {
		rotDifference = rotation - rotationFromPlayer;
		rotDifference = Math.abs(rotDifference);

		double h = Math.PI;

		/*
		 * Corrects textures for certain weird cases
		 */
		if (rotation > rotationFromPlayer) {
			if (rotDifference > h / 8 && rotDifference <= (3 * h) / 8) {
				rotDifference += (3 * Math.PI) / 2;
			} else if (rotDifference > (3 * h) / 8 && rotDifference <= (5 * h) / 8) {
				rotDifference += Math.PI;
			} else if (rotDifference > (5 * h) / 8 && rotDifference <= (7 * h) / 8) {
				rotDifference += (Math.PI) / 2;
			} else if (rotDifference > (7 * h) / 8 && rotDifference <= (9 * h) / 8) {
				rotDifference += 0;
			} else if (rotDifference > (9 * h) / 8 && rotDifference <= (11 * h) / 8) {
				rotDifference += (Math.PI / 2) * 3;
			} else if (rotDifference > (11 * h) / 8 && rotDifference <= (13 * h) / 8) {
				rotDifference += (Math.PI);
			} else if (rotDifference > (13 * h) / 8 && rotDifference <= (15 * h) / 8) {
				rotDifference += (Math.PI) / 2;
			}
		}

		double rot = rotDifference;

		// Double checks to make sure its not out of bounds
		if (rot > (Math.PI * 2)) {
			rot = rot - (Math.PI * 2);
		} else if (rot < 0) {
			rot = (Math.PI * 2) + rot;
		}

		/*
		 * For each entity, depending on its ID, rotation, and phaseTime the Render
		 * (Texture file) that this entity is currently on so that it no longer is
		 * determined during the double for loops to speed up the game.
		 */
		switch (ID) {
		// Brainomorph
		case 1:
			// If image is static (Doesn't change)
			boolean nonMover = false;

			if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
				currentPhase = Textures.enemy1right135;

				nonMover = true;
			} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
				currentPhase = Textures.enemy1back;

				nonMover = true;
			} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
				currentPhase = Textures.enemy1left135;

				nonMover = true;
			} else {
				if ((!isFiring || (isFiring && tick < 8)) && harmed <= 0) {
					if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
						currentPhase = Textures.enemy1right;

						nonMover = true;
					} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
						currentPhase = Textures.enemy1left;

						nonMover = true;
					}
				}
			}

			if (!nonMover) {
				// If enemy was recently hurt
				if (harmed > 0 && !isFiring && !isAttacking) {
					if (rot <= h / 8 || rot > (15 * h) / 8) {
						currentPhase = Textures.enemy1hurt;
					} else if (rot > h / 8 && rot <= (3 * h) / 8) {
						currentPhase = Textures.enemy1right45hurt;
					} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
						currentPhase = Textures.enemy1righthurt;
					} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
						currentPhase = Textures.enemy1lefthurt;
					} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
						currentPhase = Textures.enemy1left45hurt;
					}

					enemyPhase = 0;
				}
				// If enemy is firing, then show enemy
				// firing phases
				else if (isFiring) {
					if (tick <= 2 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire1;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire1;
						}
					} else if (tick <= 5 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire2;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire2;
						}
					} else if (tick < 8 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire3;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire3;
						}
					} else if (tick >= 8 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire4;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire4;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy1rightfire;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy1leftfire;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire4;
						}
					}
				}
				// If enemy is attacking, then show the
				// phases of that
				else if (isAttacking) {
					if (tick <= 3 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire2;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire2;
						}
					} else if (tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire3;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire3;
						}
					} else if (tick <= 9 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire2;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire2;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy1fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy1right45fire1;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy1left45fire1;
						}
					}
				} else {
					if (rot <= h / 8 || rot > (15 * h) / 8) {
						// Sets currentPhase to the currentPhase int of a pixel at a
						// given location in the image to be rendered
						if (enemyPhase <= 14 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy1;
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy1b;
						}
					} else if (rot > h / 8 && rot <= (3 * h) / 8) {
						currentPhase = Textures.enemy1right45;
					} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
						currentPhase = Textures.enemy1left45;
					}
				}
			}

			// If done with phases, start over again
			if (enemyPhase >= 28 * Render3D.fpsCheck) {
				enemyPhase = 0;
			}

			break;

		// Sentinal of the Vile
		case 2:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				currentPhase = Textures.enemy2hurt;

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show enemy
				// firing phases
				if (isFiring) {
					if (tick >= 5 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy2fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy2right45fire;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy2rightfire;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy2right135fire;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy2backfire;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy2left135fire;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy2leftfire;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy2left45fire;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy2right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy2right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy2right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy2back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy2left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy2left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy2left45;
						}
					}
				}
				// If enemy is attacking, then show the
				// phases of that
				else if (isAttacking) {
					if (tick > 9 * Render3D.fpsCheck || tick > 3 * Render3D.fpsCheck && tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy2right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy2right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy2right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy2back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy2left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy2left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy2left45;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy2fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy2right45fire;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy2rightfire;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy2right135fire;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy2backfire;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy2left135fire;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy2leftfire;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy2left45fire;
						}
					}
				} else {
					if (rot <= h / 8 || rot > (15 * h) / 8) {
						currentPhase = Textures.enemy2;
					} else if (rot > h / 8 && rot <= (3 * h) / 8) {
						currentPhase = Textures.enemy2right45;
					} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
						currentPhase = Textures.enemy2right;
					} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
						currentPhase = Textures.enemy2right135;
					} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
						currentPhase = Textures.enemy2back;
					} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						currentPhase = Textures.enemy2left135;
					} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
						currentPhase = Textures.enemy2left;
					} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
						currentPhase = Textures.enemy2left45;
					}

					enemyPhase = 0;
				}
			}

			break;

		// Mutated commando
		case 3:

			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.enemy3hurt;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.enemy3hurt45right;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.enemy3hurtright;
				}
				if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.enemy3hurt135right;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.enemy3hurtback;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.enemy3hurt135left;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.enemy3hurtleft;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.enemy3hurt45left;
				}

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show the
				// phases of that
				if (isFiring || isAttacking) {
					if (tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy3fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy3fire1right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy3fire1right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy3fire1right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy3fire1back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy3fire1left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy3fire1left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy3fire1left45;
						}
					} else if (tick <= 12 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy3fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy3fire2right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy3fire2right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy3fire2right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy3fire2back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy3fire2left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy3fire2left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy3fire2left45;
						}
					} else if (tick > 12 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy3fire3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy3fire3right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy3fire3right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy3fire3right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy3fire3back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy3fire3left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy3fire3left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy3fire3left45;
						}
					}

					enemyPhase = 0;
				} else {
					// If entity is moving
					if (moving) {
						// Runs through movement images
						if (enemyPhase <= 14 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3a45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3aright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3a135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3aback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3a135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3aleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3a45left;
							}
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3b;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3b45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3bright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3b135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3bback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3b135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3bleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3b45left;
							}
						} else if (enemyPhase <= 42 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3c;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3c45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3cright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3c135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3cback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3c135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3cleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3c45left;
							}
						} else if (enemyPhase <= 56 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3d;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3d45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3dright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3d135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3dback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3d135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3dleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3d45left;
							}
						} else if (enemyPhase <= 70 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3e;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3e45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3eright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3e135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3eback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3e135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3eleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3e45left;
							}
						} else if (enemyPhase <= 84 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.enemy3f;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.enemy3f45right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.enemy3fright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.enemy3f135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.enemy3fback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.enemy3f135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.enemy3fleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.enemy3f45left;
							}
						}

						if (enemyPhase >= 84 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy3a45right;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy3aright;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy3a135right;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy3aback;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy3a135left;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy3aleft;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy3a45left;
						}

						enemyPhase = 0;
					}
				}
			}

			break;

		// Reaper
		case 4:

			if (enemyPhase <= 4 * Render3D.fpsCheck) {
				currentPhase = Textures.enemy4;
			} else {
				currentPhase = Textures.enemy4b;
			}

			if (enemyPhase > 9 * Render3D.fpsCheck) {
				enemyPhase = 0;
			}

			break;

		// Resurrector
		case 5:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.enemy5hurt;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.enemy5hurt45right;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.enemy5hurtright;
				}
				if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.enemy5hurt135right;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.enemy5hurtback;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.enemy5hurt135left;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.enemy5hurtleft;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.enemy5hurt45left;
				}

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show enemy
				// firing phases
				if (isFiring) {
					if (rot <= h / 8 || rot > (15 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fire1;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fire2;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fire1;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fire1;
						}
					} else if (rot > h / 8 && rot <= (3 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45right;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireb45right;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45right;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45right;
						}
					} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firearight;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firebright;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firearight;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firearight;
						}
					}
					if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135right;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireb135right;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135right;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135right;
						}
					} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireaback;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firebback;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireaback;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireaback;
						}
					} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135left;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireb135left;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135left;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea135left;
						}
					} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firealeft;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firebleft;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firealeft;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firealeft;
						}
					} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
						if (tick <= 6 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45left;
						} else if (tick <= 12 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5fireb45left;
						} else if (tick <= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45left;
						} else if (tick >= 18 * Render3D.fpsCheck) {
							currentPhase = Textures.enemy5firea45left;
						}
					}
				}
				// If enemy is firing, then show enemy
				// firing phases
				else if (isAttacking) {
					if (rot <= h / 8 || rot > (15 * h) / 8) {
						currentPhase = Textures.enemy5fire1;
					} else if (rot > h / 8 && rot <= (3 * h) / 8) {
						currentPhase = Textures.enemy5firea45right;
					} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
						currentPhase = Textures.enemy5firearight;
					}
					if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
						currentPhase = Textures.enemy5firea135right;
					} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
						currentPhase = Textures.enemy5fireaback;
					} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						currentPhase = Textures.enemy5firea135left;
					} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
						currentPhase = Textures.enemy5firealeft;
					} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
						currentPhase = Textures.enemy5firea45left;
					}
				} else {
					// If entity is moving
					if (moving) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							// Sets currentPhase to the currentPhase int of a pixel at a
							// given location in the image to be rendered
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5b;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5c;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5d;
							}
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							// Sets currentPhase to the currentPhase int of a pixel at a
							// given location in the image to be rendered
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5a45right;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5b45right;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5c45right;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5d45right;
							}
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							// Sets currentPhase to the currentPhase int of a pixel at a
							// given location in the image to be rendered
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5aright;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5bright;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5cright;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5dright;
							}
						}
						if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5a135right;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5b135right;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5c135right;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5d135right;
							}
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5aback;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5bback;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5cback;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5dback;
							}
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5a135left;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5b135left;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5c135left;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5d135left;
							}
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5aleft;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5bleft;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5cleft;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5dleft;
							}
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							if (enemyPhase <= 7 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5a45left;
							} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5b45left;
							} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5c45left;
							} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
								currentPhase = Textures.enemy5d45left;
							}
						}

						// If done with phases, start over again
						if (enemyPhase >= 28 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy5;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy5a45right;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy5aright;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy5a135right;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy5aback;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy5a135left;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy5aleft;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy5a45left;
						}

						enemyPhase = 0;
					}
				}
			}

			break;

		// Morgoth
		case 6:

			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				currentPhase = Textures.morgothHurt;

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show enemy
				// firing phases
				if (isFiring) {
					if (tick <= 6 * Render3D.fpsCheck) {
						currentPhase = Textures.morgothFire1;
					} else if (tick >= 6 * Render3D.fpsCheck) {
						currentPhase = Textures.morgothFire2;
					}
				}
				// If enemy is attacking, then show the
				// phases of that
				else if (isAttacking) {
					if (tick <= 3 * Render3D.fpsCheck) {
						currentPhase = Textures.morgothMelee;
					} else if (tick <= 6 * Render3D.fpsCheck) {
						currentPhase = Textures.morgoth;
					} else if (tick <= 9 * Render3D.fpsCheck) {
						currentPhase = Textures.morgothMelee;
					} else if (tick >= 9 * Render3D.fpsCheck) {
						currentPhase = Textures.morgoth;
					}
				} else {
					// If entity is moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						if (enemyPhase <= 7 * Render3D.fpsCheck) {
							currentPhase = Textures.morgoth;
						} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
							currentPhase = Textures.morgoth2;
						} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
							currentPhase = Textures.morgoth3;
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							currentPhase = Textures.morgoth4;
						}

						if (enemyPhase >= 28 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						currentPhase = Textures.morgoth;

						enemyPhase = 0;
					}
				}
			}

			break;

		// Vile Civilian
		case 7:
			// If entity was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.vileCivHurt;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.vileCivilianright45hurt;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.vileCivilianrighthurt;
				}
				if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.vileCivilianright135hurt;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.vileCivilianbackhurt;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.vileCivilianleft135hurt;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.vileCivilianlefthurt;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.vileCivilianleft45hurt;
				}

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking) {
					if (tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.vileCivAttack1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack145right;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack1right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack1135right;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack1back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack1135left;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack1left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack145left;
						}
					} else if (tick >= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.vileCivAttack2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack245right;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack2right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack2135right;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack2back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack2135left;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack2left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.vileCivilianAttack245left;
						}
					}

					enemyPhase = 0;
				} else {
					// If entity is moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						if (enemyPhase <= 7 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.vileCiv1;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.vileCivilian145right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.vileCivilian1right;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.vileCivilian1135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.vileCivilian1back;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.vileCivilian1135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.vileCivilian1left;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.vileCivilian145left;
							}
						} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.vileCiv2;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.vileCivilian245right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.vileCivilian2right;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.vileCivilian2135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.vileCivilian2back;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.vileCivilian2135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.vileCivilian2left;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.vileCivilian245left;
							}
						} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.vileCiv3;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.vileCivilian345right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.vileCivilian3right;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.vileCivilian3135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.vileCivilian3back;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.vileCivilian3135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.vileCivilian3left;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.vileCivilian345left;
							}
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.vileCiv4;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.vileCivilian445right;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.vileCivilian4right;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.vileCivilian4135right;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.vileCivilian4back;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.vileCivilian4135left;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.vileCivilian4left;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.vileCivilian445left;
							}
						}

						if (enemyPhase >= 28 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.vileCiv1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.vileCivilian145right;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.vileCivilian1right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.vileCivilian1135right;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.vileCivilian1back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.vileCivilian1135left;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.vileCivilian1left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.vileCivilian145left;
						}

						enemyPhase = 0;
					}
				}
			}

			break;

		// Belegoth
		case 8:

			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				currentPhase = Textures.belegothHurt;

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show enemy
				// firing phases
				if (isFiring) {
					if (tick <= 2 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothAttack1;
					} else if (tick <= 5 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothAttack2;
					} else if (tick <= 8 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothAttack3;
					} else if (tick >= 8 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothAttack4;
					}
				}
				// If enemy is attacking, then show the
				// phases of that
				else if (isAttacking) {
					if (tick <= 3 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothMelee;
					} else if (tick <= 6 * Render3D.fpsCheck) {
						currentPhase = Textures.belegoth4;
					} else if (tick <= 9 * Render3D.fpsCheck) {
						currentPhase = Textures.belegothMelee;
					} else if (tick >= 9 * Render3D.fpsCheck) {
						currentPhase = Textures.belegoth4;
					}
				} else {
					// Only play movement if the entity is moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						if (enemyPhase <= 7 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth;
						} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth2;
						} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth3;
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth4;
						} else if (enemyPhase <= 35 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth5;
						} else if (enemyPhase <= 42 * Render3D.fpsCheck) {
							currentPhase = Textures.belegoth6;
						}

						if (enemyPhase >= 42 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						currentPhase = Textures.belegoth;

						enemyPhase = 0;
					}
				}
			}

			break;

		// The watcher
		case 9:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.enemy8hurt;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.enemy8right45hurt;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.enemy8righthurt;
				}
				if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.enemy8right135;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.enemy8back;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.enemy8left135;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.enemy8lefthurt;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.enemy8left45hurt;
				}

				enemyPhase = 0;
			} else {
				// If enemy is firing, then show enemy
				// firing phases
				if (isFiring) {
					if (tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire1;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire1;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire1;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire1;
						}
					} else if (tick <= 12 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire2;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire2;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135fire2;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8backfire2;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire2;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire2;
						}
					} else if (tick <= 18 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire3;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire3;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135fire3;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8backfire3;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire3;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire3;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire4;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire4;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire4;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135fire4;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8backfire4;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire4;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire4;
						}
					}
				}
				// If enemy is attacking, then show the
				// phases of that
				else if (isAttacking) {
					if (tick > 9 * Render3D.fpsCheck || tick > 3 * Render3D.fpsCheck && tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire1;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire1;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire1;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire1;
						}
					} else {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8fire3;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45fire3;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8rightfire3;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135fire3;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8backfire3;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8leftfire3;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45fire3;
						}
					}
				} else {
					if (playerStareTime <= 50 || activated) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.enemy8;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.enemy8right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.enemy8right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.enemy8right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.enemy8back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.enemy8left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.enemy8left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.enemy8left45;
						}
					} else if (playerStareTime <= 150) {
						currentPhase = Textures.enemy8;
					} else {
						currentPhase = Textures.enemy8fire1;
					}

					enemyPhase = 0;
				}
			}

			break;

		case 10:
			currentPhase = Textures.marine1;
			enemyPhase = 0;

			break;
		case 11:
			currentPhase = Textures.marine2;
			enemyPhase = 0;

			break;
		case 12:
			currentPhase = Textures.marine3;
			enemyPhase = 0;

			break;
		case 13:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.marine4hurt;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.marine4hurtright45;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.marine4hurtright;
				}
				if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.marine4hurtright135;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.marine4hurtback;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.marine4hurtleft135;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.marine4hurtleft;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.marine4hurtleft45;
				}

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					if (tick <= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.marine4fire1;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.marine4fire1right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.marine4fire1right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.marine4fire1right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.marine4fire1back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.marine4fire1left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.marine4fire1left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.marine4fire1left45;
						}
					} else if (tick >= 6 * Render3D.fpsCheck) {
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.marine4fire2;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.marine4fire2right45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.marine4fire2right;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.marine4fire2right135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.marine4fire2back;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.marine4fire2left135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.marine4fire2left;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.marine4fire2left45;
						}
					}

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						if (enemyPhase <= 7 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.marine4a;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.marine4aright45;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.marine4aright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.marine4aright135;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.marine4aback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.marine4aleft135;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.marine4aleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.marine4aleft45;
							}
						} else if (enemyPhase <= 14 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.marine4b;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.marine4bright45;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.marine4bright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.marine4bright135;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.marine4bback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.marine4bleft135;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.marine4bleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.marine4bleft45;
							}
						} else if (enemyPhase <= 21 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.marine4c;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.marine4cright45;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.marine4cright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.marine4cright135;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.marine4cback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.marine4cleft135;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.marine4cleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.marine4cleft45;
							}
						} else if (enemyPhase <= 28 * Render3D.fpsCheck) {
							if (rot <= h / 8 || rot > (15 * h) / 8) {
								currentPhase = Textures.marine4d;
							} else if (rot > h / 8 && rot <= (3 * h) / 8) {
								currentPhase = Textures.marine4dright45;
							} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
								currentPhase = Textures.marine4dright;
							} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
								currentPhase = Textures.marine4dright135;
							} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
								currentPhase = Textures.marine4dback;
							} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
								currentPhase = Textures.marine4dleft135;
							} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
								currentPhase = Textures.marine4dleft;
							} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
								currentPhase = Textures.marine4dleft45;
							}
						}

						if (enemyPhase >= 28 * Render3D.fpsCheck) {
							enemyPhase = 0;
						}
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						if (rot <= h / 8 || rot > (15 * h) / 8) {
							currentPhase = Textures.marine4a;
						} else if (rot > h / 8 && rot <= (3 * h) / 8) {
							currentPhase = Textures.marine4aright45;
						} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
							currentPhase = Textures.marine4aright;
						} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
							currentPhase = Textures.marine4aright135;
						} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
							currentPhase = Textures.marine4aback;
						} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
							currentPhase = Textures.marine4aleft135;
						} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
							currentPhase = Textures.marine4aleft;
						} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
							currentPhase = Textures.marine4aleft45;
						}

						enemyPhase = 0;
					}
				}
			}

			break;
		case 14:
			currentPhase = Textures.marine5;
			enemyPhase = 0;

			break;
		case 15:
			currentPhase = Textures.marine6;
			enemyPhase = 0;

			break;
		case 16:
			if (rot <= h / 8 || rot > (15 * h) / 8) {
				currentPhase = Textures.chairFront;
			} else if (rot > h / 8 && rot <= (3 * h) / 8) {
				currentPhase = Textures.chair45Right;
			} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
				currentPhase = Textures.chairRight;
			} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
				currentPhase = Textures.chair135Right;
			} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
				currentPhase = Textures.chairBack;
			} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
				currentPhase = Textures.chair135Left;
			} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
				currentPhase = Textures.chairLeft;
			} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
				currentPhase = Textures.chair45Left;
			}

			enemyPhase = 0;

			break;
		case 17:
			currentPhase = Textures.toilet;
			enemyPhase = 0;

			break;
		case 18:
			if (isFiring) {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.turretFire;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.turret45RightFire;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.turret90RightFire;
				} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.turret135RightFire;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.turretBackFire;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.turret135LeftFire;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.turret90LeftFire;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.turret45LeftFire;
				}
			} else {
				if (rot <= h / 8 || rot > (15 * h) / 8) {
					currentPhase = Textures.turret;
				} else if (rot > h / 8 && rot <= (3 * h) / 8) {
					currentPhase = Textures.turret45Right;
				} else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8) {
					currentPhase = Textures.turret90Right;
				} else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) {
					currentPhase = Textures.turret135Right;
				} else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) {
					currentPhase = Textures.turretBack;
				} else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
					currentPhase = Textures.turret135Left;
				} else if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) {
					currentPhase = Textures.turret90Left;
				} else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8) {
					currentPhase = Textures.turret45Left;
				}
			}

			enemyPhase = 0;

			break;

		case 19:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.armoredMenaceHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.armoredMenaceAttack;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.armoredMenace;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.armoredMenace;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 20:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.blindChargerHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.blindChargerAttack;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.blindCharger;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.blindCharger;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 21:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.damnedSoul;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.damnedSoulAttack;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.damnedSoul;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.damnedSoul;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 22:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.darkStriderHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.darkStriderFire;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.darkStrider;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.darkStrider;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 23:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.deceptorHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.deceptorFire;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.deceptor;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.deceptor;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 24:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.heavyZombieHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.heavyZombieFire;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.heavyZombie;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.heavyZombie;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 25:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.mossSpringerHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.mossSpringerAttack;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.mossSpringer;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.mossSpringer;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 26:
			// If enemy was recently hurt
			if (harmed > 0 && !isFiring && !isAttacking) {
				/*
				 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
				 * Textures.marine4hurt; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
				 * currentPhase = Textures.marine4hurtright45; } else if (rot > (3 * h) / 8 &&
				 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4hurtright; } if (rot >
				 * (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
				 * Textures.marine4hurtright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
				 * / 8) { currentPhase = Textures.marine4hurtback; } else if (rot > (9 * h) / 8
				 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4hurtleft135; } else
				 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
				 * Textures.marine4hurtleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
				 * 8) { currentPhase = Textures.marine4hurtleft45; }
				 */
				currentPhase = Textures.tetraDestructorHurt;

				enemyPhase = 0;
			} else {
				// If enemy is attacking, then show the
				// phases of that
				if (isAttacking || isFiring) {
					/*
					 * if (tick <= 6 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8)
					 * { currentPhase = Textures.marine4fire1; } else if (rot > h / 8 && rot <= (3 *
					 * h) / 8) { currentPhase = Textures.marine4fire1right45; } else if (rot > (3 *
					 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire1right; }
					 * else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire1right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire1back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire1left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire1left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire1left45; } } else if (tick >= 6 *
					 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
					 * Textures.marine4fire2; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
					 * currentPhase = Textures.marine4fire2right45; } else if (rot > (3 * h) / 8 &&
					 * rot <= (5 * h) / 8) { currentPhase = Textures.marine4fire2right; } else if
					 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
					 * Textures.marine4fire2right135; } else if (rot > (7 * h) / 8 && rot <= (9 * h)
					 * / 8) { currentPhase = Textures.marine4fire2back; } else if (rot > (9 * h) / 8
					 * && rot <= (11 * h) / 8) { currentPhase = Textures.marine4fire2left135; } else
					 * if (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
					 * Textures.marine4fire2left; } else if (rot > (13 * h) / 8 && rot <= (15 * h) /
					 * 8) { currentPhase = Textures.marine4fire2left45; } }
					 */

					currentPhase = Textures.tetraDestructorFire;

					enemyPhase = 0;
				} else {
					// If entity is currently moving
					if (moving) {
						// Runs through movement phases of the
						// enemies textures.
						/*
						 * if (enemyPhase <= 7 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h)
						 * / 8) { currentPhase = Textures.marine4a; } else if (rot > h / 8 && rot <= (3
						 * * h) / 8) { currentPhase = Textures.marine4aright45; } else if (rot > (3 * h)
						 * / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4aright; } else if
						 * (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4aright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4aback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4aleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4aleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4aleft45; } } else if (enemyPhase <= 14 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4b; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4bright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4bright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4bright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4bback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4bleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4bleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4bleft45; }
						 * } else if (enemyPhase <= 21 * Render3D.fpsCheck) { if (rot <= h / 8 || rot >
						 * (15 * h) / 8) { currentPhase = Textures.marine4c; } else if (rot > h / 8 &&
						 * rot <= (3 * h) / 8) { currentPhase = Textures.marine4cright45; } else if (rot
						 * > (3 * h) / 8 && rot <= (5 * h) / 8) { currentPhase = Textures.marine4cright;
						 * } else if (rot > (5 * h) / 8 && rot <= (7 * h) / 8) { currentPhase =
						 * Textures.marine4cright135; } else if (rot > (7 * h) / 8 && rot <= (9 * h) /
						 * 8) { currentPhase = Textures.marine4cback; } else if (rot > (9 * h) / 8 &&
						 * rot <= (11 * h) / 8) { currentPhase = Textures.marine4cleft135; } else if
						 * (rot > (11 * h) / 8 && rot <= (13 * h) / 8) { currentPhase =
						 * Textures.marine4cleft; } else if (rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						 * { currentPhase = Textures.marine4cleft45; } } else if (enemyPhase <= 28 *
						 * Render3D.fpsCheck) { if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase =
						 * Textures.marine4d; } else if (rot > h / 8 && rot <= (3 * h) / 8) {
						 * currentPhase = Textures.marine4dright45; } else if (rot > (3 * h) / 8 && rot
						 * <= (5 * h) / 8) { currentPhase = Textures.marine4dright; } else if (rot > (5
						 * * h) / 8 && rot <= (7 * h) / 8) { currentPhase = Textures.marine4dright135; }
						 * else if (rot > (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase =
						 * Textures.marine4dback; } else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) {
						 * currentPhase = Textures.marine4dleft135; } else if (rot > (11 * h) / 8 && rot
						 * <= (13 * h) / 8) { currentPhase = Textures.marine4dleft; } else if (rot > (13
						 * * h) / 8 && rot <= (15 * h) / 8) { currentPhase = Textures.marine4dleft45; }
						 * }
						 * 
						 * if (enemyPhase >= 28 * Render3D.fpsCheck) { enemyPhase = 0; }
						 */

						currentPhase = Textures.tetraDestructor;

						enemyPhase = 0;
					} else {
						// If marine is standing still, just have the default stand
						// still texture
						/*
						 * if (rot <= h / 8 || rot > (15 * h) / 8) { currentPhase = Textures.marine4a; }
						 * else if (rot > h / 8 && rot <= (3 * h) / 8) { currentPhase =
						 * Textures.marine4aright45; } else if (rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						 * { currentPhase = Textures.marine4aright; } else if (rot > (5 * h) / 8 && rot
						 * <= (7 * h) / 8) { currentPhase = Textures.marine4aright135; } else if (rot >
						 * (7 * h) / 8 && rot <= (9 * h) / 8) { currentPhase = Textures.marine4aback; }
						 * else if (rot > (9 * h) / 8 && rot <= (11 * h) / 8) { currentPhase =
						 * Textures.marine4aleft135; } else if (rot > (11 * h) / 8 && rot <= (13 * h) /
						 * 8) { currentPhase = Textures.marine4aleft; } else if (rot > (13 * h) / 8 &&
						 * rot <= (15 * h) / 8) { currentPhase = Textures.marine4aleft45; }
						 */

						currentPhase = Textures.tetraDestructor;

						enemyPhase = 0;
					}
				}
			}

			break;

		case 27:
			currentPhase = Textures.canister;

			enemyPhase = 0;

			break;

		case 28:
			currentPhase = Textures.trash1;

			enemyPhase = 0;

			break;

		default:
			currentPhase = Textures.toilet;
			enemyPhase = 0;

			break;
		}
	}

	/**
	 * Keeps track of the enemies movement each turn
	 */
	public void move() {
		// Set enemies height based on block or object its on
		setHeight();

		/*
		 * With faster ticks, the enemies move faster, and therefore to correct the
		 * faster movements, this slows them down so that the game is still winnable.
		 */
		if (Display.fps >= 60) {
			speed = initialSpeed / ((Display.fps / 60) + 1);
		} else {
			speed = initialSpeed / 1.5;
		}

		// If there is a target other than the player causing infighting
		if (targetEnemy != null) {
			targetX = targetEnemy.xPos;
			targetY = targetEnemy.yPos;
			targetZ = targetEnemy.zPos;
		} else if (targetItem != null) {
			targetX = targetItem.x;
			targetZ = targetItem.z;
			targetY = targetItem.y;
		}
		// If target is the player
		else {
			targetX = Player.x;
			targetY = Player.y;
			targetZ = Player.z;
		}

		// Angle that the target is in accordance to the enemy so
		// that enemy moves right towards the target
		rotationFromTarget = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

		// Angle that the player is in accordance to the enemy so
		// that the enemy is facing correctly to the player
		rotationFromPlayer = Math.atan(((Player.x - xPos) * 1) / ((Player.z - zPos) * 1));

		/*
		 * If the target is in the 3rd or 4th quadrant of the map then add PI to
		 * rotation so that the enemy will move into the correct quadrant of the map and
		 * at the target.
		 */
		if (targetZ < zPos) {
			rotationFromTarget += Math.PI;
		}

		// No negative angles
		if (rotationFromTarget < 0) {
			rotationFromTarget = (2 * Math.PI) + rotationFromTarget;
		}

		/*
		 * If the target is in the 3rd or 4th quadrant of the map then add PI to
		 * rotation so that the enemy will face into the correct quadrant of the map and
		 * at the target.
		 */
		if (Player.z < zPos) {
			rotationFromPlayer += Math.PI;
		}

		// No negative angles
		if (rotationFromPlayer < 0) {
			rotationFromPlayer = (2 * Math.PI) + rotationFromPlayer;
		}

		// Block enemy is now on
		Block blockOn = Level.getBlock((int) xPos, (int) zPos);

		/*
		 * All for dealing with the force of explosions propelling enemies in some
		 * direction
		 */
		double xEff = 0;
		double zEff = 0;
		double yEff = 0;

		if (xEffects > 0) {
			xEff = 0.2;
		} else if (xEffects < 0) {
			xEff = -0.2;
		}

		if (zEffects > 0) {
			zEff = 0.2;
		} else if (zEffects < 0) {
			zEff = -0.2;
		}

		if (yEffects > 0) {
			yEff = 2;
		} else if (yEffects < 0) {
			yEff = -2;
		}

		this.yPos -= (yEff);

		// If the enemy is in the air (Usually from a explosion)
		// Make it fall.
		if (-yPos > -maxHeight && !canFly) {
			yPos += 0.2 * weightLevel * fallSpeed;
			fallSpeed += acceleration;
		} else {
			fallSpeed = 1;
		}

		// Don't let enemy go through floor or block if pushed that way.
		if (yPos > maxHeight) {
			yPos = maxHeight;
		}

		/*
		 * Can the force of the explosion push the enemy any more into the z direction
		 * and if so add that effect to the zPos of the enemy. Only if there is an
		 * effect.
		 */
		if (zEff != 0) {
			if (isFree(xPos, zPos + (zEff))) {
				zPos += (zEff);
			}
		}

		/*
		 * Can the force of the explosion push the enemy any more into the x direction
		 * and if so add that effect to the xPos of the enemy. Only if there is an
		 * effect.
		 */
		if (xEff != 0) {
			if (isFree(xPos + (xEff), zPos)) {
				xPos += (xEff);
			}
		}

		// If peaceful mode, the enemy only moves if hit by and explosion
		// so this updates what block its on still.
		if (Game.skillMode == 0 || !hasMovement) {
			// Block entity is now on
			Block blockOnNew = Level.getBlock((int) (xPos), (int) (zPos));

			/*
			 * If the entity is on a new block, then remove the entity from the last block
			 * it was on and add it to the new block its on.
			 */
			if (!blockOnNew.equals(blockOn)) {
				blockOn.entitiesOnBlock.remove(this);
				blockOnNew.entitiesOnBlock.add(this);

				setHeight();
			}

			// Allows entities to ride lifts/elevators
			// Basically set entities position to the position of the current
			// block.
			if (!canFly && Math.abs(blockOnNew.height + yPos) <= 2) {
				yPos = -(blockOnNew.height + (blockOnNew.y * 4) + blockOnNew.baseCorrect) / 11;
			}

			// If it has no movement, but can turn
			if (canTurn) {

				// Keep "scanning" the room if the enemy is dead or not there.
				if (targetEnemy == null || (targetEnemy != null && !targetEnemy.isAlive)) {
					rotation += 0.01;
					sentryBeepTimer++;

					// When searching, play the searching sentry beep sound
					if (sentryBeepTimer % 79 == 0) {
						SoundController.sentryBeep.playAudioFile(distanceFromPlayer);
					}

					// Keep turret within one circle of rotation
					if (rotation > (2 * Math.PI)) {
						rotation = 0;
						sentryBeepTimer = 0;
					}
				} else {
					// Entity does not turn if it does not have movement (Like chairs etc...)
					// Angle that the player is in accordance to the entity so
					// that entity looks at its target
					rotation = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

					/*
					 * If the target is in the 3rd or 4th quadrant of the map then add PI to
					 * rotation so that the entity will move into the correct quadrant of the map
					 * and at the target.
					 */
					if (targetZ < zPos) {
						rotation += Math.PI;
					}
				}
			}

			return;
		}

		// If the entity is activated, and isn't attacking then move
		// normally
		if (activated && !isFiring && !isAttacking && harmed == 0) {
			// Current block enemy is on. Re-update from above
			blockOn = Level.getBlock((int) xPos, (int) zPos);

			moving = true;

			// If enemy can fly
			if (canFly) {
				/*
				 * Used to correct the targets y in case the target is crouching and the y goes
				 * below the maxHeight the target can stand on.
				 */
				double yCorrect = -targetY;

				// If target is player
				if (targetEnemy == null) {
					// if(yCorrect < Player.maxHeight)
					// {
					yCorrect = Player.y / 11;
					// }
				}

				// If distance is less than or equal to 5 units from
				// the player, and its not stuck
				if (distance <= 5 && !isStuck) {
					// If above player
					if (Math.abs(yPos) > (yCorrect + 0.1) + 0.25) {
						// If negligibly close to top of the block
						// set the y to slightly above the block
						if (Math.abs(yPos + blockOn.height) <= 0.025) {
							yPos = -(blockOn.height + 0.025);
						}
						// If not then float down
						else if ((Math.abs(yPos)) > blockOn.height) {
							yPos += 0.05;
							tracking = true;
						}
					}
					// If target is above enemy, fly up
					else if (Math.abs(yPos) < (yCorrect + 0.1)) {
						yPos -= 0.05;
						tracking = true;
					}
					/*
					 * If enemy is negligible close to the ground, and is not stuck, then stay
					 * floating 1/10 units above the ground.
					 */
					else if (Math.abs(yPos) < 0) {
						yPos = 0;
						tracking = true;
					}
					/*
					 * If the enemy is negligibly close to the player, just match up the height with
					 * the players.This small correction is so small it will never be noticed if
					 * playing the game.
					 */
					else {
						yPos = -(yCorrect + 0.1);
						tracking = true;
					}
				}
				// If farther than a distance of 5 from the player
				// Or the enemy is stuck
				else if (distance > 5 || isStuck) {
					/*
					 * If stuck behind a wall, or below its default height when not in range of
					 * player, then fly up
					 */
					if (isStuck && !(-yPos + 0.05 >= (blockOn.y * 4))) {
						yPos -= 0.05;
						tracking = false;
					}
					/*
					 * If not stuck, and not in range of player, yet still above the default flying
					 * height, then fly down.
					 */
					else if (-yPos > (startY + 0.025 + (blockOn.height + (blockOn.y * 4) + blockOn.baseCorrect) / 10)
							&& !isStuck) {
						yPos += 0.05;
						tracking = false;
					}
				}

				// Set the lowest the enemy can fly as being 0 units
				// above the ground
				if (yPos > 0) {
					yPos = 0;
				}

				// Enemy cannot fly higher than ceiling
				if (yPos <= -Render3D.ceilingDefaultHeight / 10) {
					yPos = -(Render3D.ceilingDefaultHeight / 10) + 1;
				}
			}

			/*
			 * For the magistrate enemy, check to see if a corpse is in range of being
			 * resurrected, and resurrect the corpse if so.
			 */
			if (ID == 5) {
				// Check all corpses in the game
				for (int i = 0; i < Game.corpses.size(); i++) {
					Corpse corpse = Game.corpses.get(i);

					Block corpseBlock = Level.getBlock((int) corpse.xPos, (int) corpse.zPos);

					// Find the distance between a corpse and the enemy
					double distance = Math.sqrt(((Math.abs(getX() - corpse.xPos)) * (Math.abs(getX() - corpse.xPos)))
							+ ((Math.abs(getZ() - corpse.zPos)) * (Math.abs(getZ() - corpse.zPos))));

					/*
					 * If enemy is within a 1 unit range of corpse. Also corpse cannot be another
					 * mage enemy or boss otherwise they'd just keep resurrecting each other. It'd
					 * be impossible. Also they have to be within a height of 4 units from each
					 * other, and the corpse cannot be a default corpse with no ID.
					 */
					if (distance <= 1 && corpse.enemyID != 6 && corpse.enemyID != 0 && corpse.enemyID != 8
							&& Math.abs(getY() - corpse.yPos) <= 4 && !corpseBlock.isMoving) {
						// Can the mage resurrect the corpse yet
						if (canResurrect) {
							// Reset the ability to resurrect again.
							canResurrect = false;

							/*
							 * Reconstruct enemy depending on the ID the corpse was before it became sad.
							 * Also activate the new enemy.
							 */
							Entity newEnemy = new Entity(corpse.xPos, corpse.yPos, corpse.zPos, corpse.enemyID, 0, 0);

							newEnemy.activated = true;

							// If the mage is friendly, make resurrected enemies friendly.
							if (isFriendly) {
								newEnemy.isFriendly = true;
							} else {
								newEnemy.isFriendly = false;
								// Add to enemies in the map
								Game.enemiesInMap++;
							}

							// Add new enemy to game
							Game.entities.add(newEnemy);

							// Remove the corpse from the map
							Game.corpses.remove(i);
						} else {
							/*
							 * If mage should be able to resurrect but isn't in the process of doing so then
							 * start the process
							 */
							if (ID == 5) {
								this.isFiring = true;
								tick = 0;
							}
						}
					}
				}
			}

			// Entity does not turn if it does not have movement (Like chairs etc...)
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

			/*
			 * Corrects rotation so that the enemy is centered correctly in the map graph
			 */
			double correction = 44.765;

			// if (ID == 100) {
			// searchMode = false;
			// speed = 0.5/21.0;
			// }

			/*
			 * Depending on the targets angle in the x z plane from the enemy, the enemy
			 * will move in the x and z directions in a certain way in order to move towards
			 * its target.
			 */
			double newX = ((Math.cos(rotation - correction)) + (Math.sin(rotation - correction))) * speed;
			double newZ = ((Math.cos(rotation - correction)) - (Math.sin(rotation - correction))) * speed;

			// Which directions the enemy cannot move in due to a blockage
			boolean xStopped = false;
			boolean zStopped = false;

			// If enemy is not on the list of enemies on the block then
			// add it to it.
			if (!blockOn.entitiesOnBlock.contains(this)) {
				blockOn.entitiesOnBlock.add(this);
			}

			// If not in search mode, move towards target
			if (!searchMode) {

				// If one of the marines, then they are primarily a ranged unit. Stop
				// moving if the target is in sight and they are a certain distance away.
				if (!pursuant) {
					// If distance from target is less than 5 and target is in sight, stop the
					// movement
					if (distance <= 5 && inSight && targetEnemy != null && !relocating) {
						// Block enemy is now on
						Block blockOnNew = Level.getBlock((int) xPos, (int) zPos);

						// Entity is not moving at movment so just stand still
						moving = false;

						/*
						 * If the enemy is on a new block, then remove the enemy from the last block it
						 * was on and add it to the new block its on.
						 */
						if (!blockOnNew.equals(blockOn)) {
							blockOn.entitiesOnBlock.remove(this);
							blockOnNew.entitiesOnBlock.add(this);

							setHeight();
						}

						try {
							for (Item e : blockOnNew.wallEntities) {
								// Allows entities to ride lifts/elevators
								// Basically set entities position to the position of the current
								// block if its an elevator and the entity is on it
								if (e != null && e.itemID == ItemNames.ELEVATOR.getID() && !canFly
										&& Math.abs((blockOnNew.height + (blockOnNew.y * 4)) + yPos) <= 2) {
									yPos = -(blockOnNew.height + blockOnNew.y + blockOnNew.baseCorrect);
								}
							}
						} catch (Exception e) {

						}

						return;
					}

					// Not relocating by default. Only if shots don't hit.
					relocating = false;

					// Moving by default
					moving = true;
				}

				/*
				 * If moving in the direction determined above ends up hitting a wall, do not
				 * move in that direction. Otherwise do so.
				 */
				if (isFree(xPos + newX, zPos)) {
					xPos += newX;
				} else {
					// If stopped in the x direction it tried to move
					xStopped = true;
				}

				// Same as above but in the z
				if (isFree(xPos, zPos + newZ)) {
					zPos += newZ;
				} else {
					// If stopped in the z direction it tried to move
					zStopped = true;
				}

				double newSpeed = speed;

				if (Math.abs(newX) <= 0.001) {
					xStopped = true;
				}

				if (Math.abs(newZ) <= 0.001) {
					zStopped = true;
				}

				/*
				 * If stopped in both directions, then as long as the enemy is not a flying
				 * enemy, turn on search mode so that the enemy tries to find the path to get to
				 * the player. If it is a flying enemy, it is stuck and needs to go above the
				 * wall.
				 */
				if (xStopped && zStopped) {
					// If not sentinel enemy
					if (!canFly) {
						searchMode = true;
					}
					// If a flying enemy
					else {
						isStuck = true;
					}

					Random random = new Random();
					int temp = random.nextInt(2);

					/*
					 * Using the random statement above, it will try to determine which direction it
					 * will try first based upon which direction (either z or x) is farthest from
					 * the player.
					 */
					if (Math.abs(newZ) <= 0.001) {
						if (temp == 0) {
							direction = 2;
						} else {
							direction = 3;
						}
					} else {
						if (temp == 0) {
							direction = 0;
						} else {
							direction = 1;
						}
					}
				}
				/*
				 * If only the x direction is stopped then move with the distance unable to be
				 * used in the x direction for moving in the z direction faster.
				 */
				else if (xStopped && !zStopped) {
					if (newZ < 0) {
						newSpeed = -(newSpeed - newZ);
					} else {
						newSpeed = (newSpeed - newZ);
					}

					if (isFree(xPos, zPos + newSpeed)) {
						zPos += newSpeed;
					}
				}
				/*
				 * Same but if you are stopped in the z direction then move with extra speed
				 * into the x direction to reach the player faster.
				 */
				else if (!xStopped && zStopped) {
					if (newX < 0) {
						newSpeed = -(newSpeed - newX);
					} else {
						newSpeed = (newSpeed - newX);
					}

					if (isFree(xPos + newSpeed, zPos)) {
						xPos += newSpeed;
					}
				}
			}
			// If in searchmode and can move
			else if (searchMode) {
				double newSpeed = speed;

				/*
				 * For each direction, itll move in that direction until it hits a wall and/or
				 * it opens a door. If it hits a wall then it'll change direction into either
				 * one of the two opposite directions (such as if going left, it'll now choose
				 * to go forward or back, but not right which is where it came from). If it
				 * opens a door, then it will go through the doorway to see where it leads.
				 * 
				 * The enemy will only leave searchmode if the player is in sight of the enemy
				 * and the direction to get to the completely open with no obstacle.
				 * 
				 * 0 is the Lesser x direction 1 is the Greater x direction 2 is the Lesser z
				 * direction 3 is the Greater z direction
				 */
				if (direction == 0) {
					// Is this direction free? If so...
					if (isFree(xPos - speed, zPos)) {
						// Move in this direction
						xPos -= speed;

						// Rotate enemies body torwards the direction its
						// moving
						rotation = (3 * Math.PI) / 2;

						// Get all blocks surrounding enemy
						Block right = Level.getBlock((int) xPos + 1, (int) zPos);

						Block left = Level.getBlock((int) xPos - 1, (int) zPos);

						Block front = Level.getBlock((int) xPos, (int) zPos + 1);

						Block back = Level.getBlock((int) xPos, (int) zPos + 1);

						/*
						 * Check to see if each block is a doorway and if it is, then move through the
						 * doorway by changing the direction as being towards the doorway.
						 */
						if (!doorway) {
							if (back.isMoving && isFree(xPos, zPos - newZ)) {
								direction = 2;
								doorway = true;
							} else if (front.isMoving && isFree(xPos, zPos + newZ)) {
								direction = 3;
								doorway = true;
							} else if (left.isMoving && isFree(xPos - newX, zPos)) {
								direction = 0;
								doorway = true;
							} else if (right.isMoving && isFree(xPos + newX, zPos)) {
								direction = 1;
								doorway = true;
							}
						}

						// If player is in sight
						if (inSight) {
							// If player can be reached
							if (isFree(xPos + newX, zPos) && isFree(xPos, zPos + newZ)) {
								// Move more to get around a corner if
								// it can
								if (isFree(xPos - speed, zPos)) {
									xPos -= speed;
								}

								// Turn off search mode and move torwards player
								searchMode = false;
								return;
							}
						}

						/*
						 * If the enemy is on the same height level as the player, or can at least reach
						 * the players height level such as a step or something.
						 * 
						 * Then see if the enemy can move towards the players z direction or not, and if
						 * it can, move towards it.
						 */
						if (Math.abs(Player.y - yPos) <= 2 && !doorway) {
							if (newZ < 0) {
								if (isFree(xPos, zPos - newSpeed)) {
									zPos -= newSpeed;
								}
							} else {
								if (isFree(xPos, zPos + newSpeed)) {
									zPos += newSpeed;
								}
							}
						}
					}
					// If direction is not free
					else {
						// If Just went through a doorway, re-
						// search out the player if possible.
						if (doorway) {
							searchMode = false;
							return;
						}

						// No longer going through doorway
						doorway = false;

						// Reset direction to one of two opposite directions
						Random random = new Random();
						int rand = random.nextInt(2);

						if (rand == 0) {
							direction = 2;
						} else {
							direction = 3;
						}
					}
				}
				// Same as above but for different directions
				else if (direction == 1) {
					if (isFree(xPos + speed, zPos)) {
						xPos += speed;

						rotation = (Math.PI) / 2;

						Block right = Level.getBlock((int) xPos + 1, (int) zPos);

						Block left = Level.getBlock((int) xPos - 1, (int) zPos);

						Block front = Level.getBlock((int) xPos, (int) zPos + 1);

						Block back = Level.getBlock((int) xPos, (int) zPos + 1);

						/*
						 * Check to see if each block is a doorway and if it is, then move through the
						 * doorway by changing the direction as being towards the doorway.
						 */
						if (!doorway) {
							if (back.isMoving && isFree(xPos, zPos - newZ)) {
								direction = 2;
								doorway = true;
							} else if (front.isMoving && isFree(xPos, zPos + newZ)) {
								direction = 3;
								doorway = true;
							} else if (left.isMoving && isFree(xPos - newX, zPos)) {
								direction = 0;
								doorway = true;
							} else if (right.isMoving && isFree(xPos + newX, zPos)) {
								direction = 1;
								doorway = true;
							}
						}

						if (inSight) {
							if (isFree(xPos + newX, zPos) && isFree(xPos, zPos + newZ)) {
								// Move more to get around the corner if
								// it can
								if (isFree(xPos + speed, zPos)) {
									xPos += speed;
								}

								searchMode = false;
								return;
							}
						}

						/*
						 * If the enemy is on the same height level as the player, or can at least reach
						 * the players height level such as a step or something.
						 * 
						 * Then see if the enemy can move towards the players z direction or not, and if
						 * it can, move towards it.
						 */
						if (Math.abs(Player.y - yPos) <= 2 && !doorway) {
							if (newZ < 0) {
								if (isFree(xPos, zPos - newSpeed)) {
									zPos -= newSpeed;
								}
							} else {
								if (isFree(xPos, zPos + newSpeed)) {
									zPos += newSpeed;
								}
							}
						}
					} else {
						// If Just went through a doorway, re-
						// search out the player if possible.
						if (doorway) {
							searchMode = false;
							return;
						}

						doorway = false;

						Random random = new Random();
						int rand = random.nextInt(2);

						if (rand == 0) {
							direction = 2;
						} else {
							direction = 3;
						}
					}
				} else if (direction == 2) {
					if (isFree(xPos, zPos - speed)) {
						zPos -= speed;

						rotation = Math.PI;

						Block right = Level.getBlock((int) xPos + 1, (int) zPos);

						Block left = Level.getBlock((int) xPos - 1, (int) zPos);

						Block front = Level.getBlock((int) xPos, (int) zPos + 1);

						Block back = Level.getBlock((int) xPos, (int) zPos + 1);

						/*
						 * Check to see if each block is a doorway and if it is, then move through the
						 * doorway by changing the direction as being towards the doorway.
						 */
						if (!doorway) {
							if (back.isMoving && isFree(xPos, zPos - newZ)) {
								direction = 2;
								doorway = true;
							} else if (front.isMoving && isFree(xPos, zPos + newZ)) {
								direction = 3;
								doorway = true;
							} else if (left.isMoving && isFree(xPos - newX, zPos)) {
								direction = 0;
								doorway = true;
							} else if (right.isMoving && isFree(xPos + newX, zPos)) {
								direction = 1;
								doorway = true;
							}
						}

						// If player is in sight
						if (inSight) {
							// If it can move towards the player
							if (isFree(xPos + newX, zPos) && isFree(xPos, zPos + newZ)) {
								// Move more to get around the corner if
								// it can
								if (isFree(xPos, zPos - speed)) {
									zPos -= speed;
								}

								searchMode = false;
								return;
							}
						}

						/*
						 * If the enemy is on the same height level as the player, or can at least reach
						 * the players height level such as a step or something.
						 * 
						 * Then see if the enemy can move towards the players x direction or not, and if
						 * it can, move towards it.
						 */
						if (Math.abs(Player.y - yPos) <= 2 && !doorway) {
							if (newX < 0) {
								if (isFree(xPos - newSpeed, zPos)) {
									xPos -= newSpeed;
								}
							} else {
								if (isFree(xPos + newSpeed, zPos)) {
									xPos += newSpeed;
								}
							}
						}
					} else {
						// If Just went through a doorway, re-
						// search out the player if possible.
						if (doorway) {
							searchMode = false;
							return;
						}

						doorway = false;

						Random random = new Random();
						int rand = random.nextInt(2);

						if (rand == 0) {
							direction = 0;
						} else {
							direction = 1;
						}
					}
				} else {
					if (isFree(xPos, zPos + speed)) {
						zPos += speed;

						rotation = 0;

						Block right = Level.getBlock((int) xPos + 1, (int) zPos);

						Block left = Level.getBlock((int) xPos - 1, (int) zPos);

						Block front = Level.getBlock((int) xPos, (int) zPos + 1);

						Block back = Level.getBlock((int) xPos, (int) zPos + 1);

						/*
						 * Check to see if each block is a doorway and if it is, then move through the
						 * doorway by changing the direction as being towards the doorway.
						 */
						if (!doorway) {
							if (back.isMoving && isFree(xPos, zPos - newZ)) {
								direction = 2;
								doorway = true;
							} else if (front.isMoving && isFree(xPos, zPos + newZ)) {
								direction = 3;
								doorway = true;
							} else if (left.isMoving && isFree(xPos - newX, zPos)) {
								direction = 0;
								doorway = true;
							} else if (right.isMoving && isFree(xPos + newX, zPos)) {
								direction = 1;
								doorway = true;
							}
						}

						// If player is in sight
						if (inSight) {
							// If it can move towards the player
							if (isFree(xPos + newX, zPos) && isFree(xPos, zPos + newZ)) {
								// Move more to get around the corner if
								// it can
								if (isFree(xPos, zPos + speed)) {
									zPos += speed;
								}

								searchMode = false;
								return;
							}
						}

						/*
						 * If the enemy is on the same height level as the player, or can at least reach
						 * the players height level such as a step or something.
						 * 
						 * Then see if the enemy can move towards the players x direction or not, and if
						 * it can, move towards it.
						 */
						if (Math.abs(Player.y - yPos) <= 2 && !doorway) {
							if (newX < 0) {
								if (isFree(xPos - newSpeed, zPos)) {
									xPos -= newSpeed;
								}
							} else {
								if (isFree(xPos + newSpeed, zPos)) {
									xPos += newSpeed;
								}
							}
						}
					} else {
						// If Just went through a doorway, re-
						// search out the player if possible.
						if (doorway) {
							searchMode = false;
							return;
						}

						doorway = false;

						Random random = new Random();
						int rand = random.nextInt(2);

						if (rand == 0) {
							direction = 1;
						} else {
							direction = 0;
						}
					}
				}
			}

			// Block enemy is now on
			Block blockOnNew = Level.getBlock((int) xPos, (int) zPos);

			// Fixes a rare bug that occurs for no reason
			if (blockOnNew.wallEntities != null) {
				// Entities can teleport too if on top of a wall with a
				// teleportation device
				for (int j = 0; j < blockOnNew.wallEntities.size(); j++) {
					Item item = blockOnNew.wallEntities.get(j);

					// If a Teleporter enterance
					if (item.itemID == ItemNames.TELEPORTERENTER.getID()) {
						// Check all teleporters for a matching exit
						for (int i = 0; i < Game.teleporters.size(); i++) {
							// Teleporter Object
							Item tel = Game.teleporters.get(i);

							// If there is a teleporter exit with the same exact
							// activation ID, teleport the player to that location
							if (tel.itemActivationID == item.itemActivationID
									&& tel.itemID == ItemNames.TELEPORTEREXIT.getID()) {
								this.xPos = tel.x;
								this.zPos = tel.z;

								// Block teleporter exit is on
								Block teleporterExit = Level.getBlock((int) tel.x, (int) tel.z);
								blockOnNew = teleporterExit;

								// Set players y value to that new blocks height
								this.yPos = teleporterExit.height + teleporterExit.y;

								// Play teleportation sound
								SoundController.teleportation.playAudioFile(0);
							}
						}
					}
				}
			} else {
				blockOnNew.wallEntities = new ArrayList<Item>();
			}

			/*
			 * If the enemy is on a new block, then remove the enemy from the last block it
			 * was on and add it to the new block its on.
			 */
			if (!blockOnNew.equals(blockOn)) {
				blockOn.entitiesOnBlock.remove(this);
				blockOnNew.entitiesOnBlock.add(this);

				setHeight();
			}

			try {
				for (Item e : blockOnNew.wallEntities) {
					// Allows entities to ride lifts/elevators
					// Basically set entities position to the position of the current
					// block if its an elevator and the entity is on it
					if (e != null && e.itemID == ItemNames.ELEVATOR.getID() && !canFly
							&& Math.abs((blockOnNew.height + (blockOnNew.y * 4)) + yPos) <= 2) {
						yPos = -(blockOnNew.height + blockOnNew.y + blockOnNew.baseCorrect);
					}
				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Sets entities new height, and corrects the entities graphics for this new
	 * height.
	 */
	// TODO update possibly
	public void setHeight() {
		// Block enemy is now on
		Block blockOnNew = Level.getBlock((int) xPos, (int) zPos);

		// Calculates the new height the entity will be at when it moves
		double newHeight = (blockOnNew.height + (blockOnNew.y * 4) + blockOnNew.baseCorrect) / 11;
		// If not flying entity
		if (!canFly) {

			if (blockOnNew.y == 0 && -yPos < (blockOnNew.height + blockOnNew.baseCorrect)
					&& -yPos + 3 > (blockOnNew.height + blockOnNew.baseCorrect)) {
				maxHeight = -newHeight;
				yPos = maxHeight;
			}

			// Set height based on the height of the block the entity is on/in
			else if (-yPos >= ((blockOnNew.y * 4))) {
				maxHeight = -newHeight;
				yPos = maxHeight;
			}

			// If the entity is supposed to be in the block, keep them under it. Like if
			// they are
			// hidden in a box.
			if (yPos == 0 && newHeight > 0.3) {
				yPos = 0;
				maxHeight = 0;
			}
		} else {
			if (-yPos < newHeight) {
				yPos = -newHeight;
				maxHeight = -newHeight;
			}
		}
	}

	/**
	 * Calls the enemies hurt method
	 */
	public void hurt(double damage, boolean soundPlayed) {
		itself.hurt(damage, soundPlayed);
	}

	/**
	 * Calls the enemy death method
	 */
	public void enemyDeath() {
		itself.enemyDeath();
	}

	@Override
	/**
	 * Compares two enemies together depending on their distance from the player.
	 * Compares in terms of ascending distance so that closer enemies will be first
	 * in the list.
	 */
	public int compareTo(Object enemy) {
		// Cast compared enemy to entity type
		EntityParent enemyToCompare = ((EntityParent) (enemy));

		double enemyX = enemyToCompare.xPos;
		double enemyZ = enemyToCompare.zPos;

		// Get distances from player to each enemy
		double distanceToEnemy1 = Math
				.sqrt(((xPos - Player.x) * (xPos - Player.x)) + ((zPos - Player.z) * (zPos - Player.z)));
		double distanceToEnemy2 = Math
				.sqrt(((enemyX - targetX) * (enemyX - targetX)) + ((enemyZ - targetZ) * (enemyZ - targetZ)));

		int compareToSend = 0;

		if (distanceToEnemy1 > distanceToEnemy2) {
			compareToSend = 1;
		} else if (distanceToEnemy1 < distanceToEnemy2) {
			compareToSend = -1;
		}

		/*
		 * Compares two distances, and orders in ascending order.
		 */
		return compareToSend;
	}
}
