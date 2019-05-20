package com.vile.entities;

import java.util.ArrayList;
import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.SoundController;
import com.vile.graphics.Render3D;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * @Title Projectile
 * @author Alex Byrd Date Updated: 5/2/2017
 * 
 *         Description: Has all the methods a projectile might need except for a
 *         movement method as both bullets and enemy projectiles have different
 *         tracking methods.
 */
public abstract class Projectile {
	// Has projectile hit something?
	public boolean hit = false;

	// If the bullet hits something its supposed to disappear from
	private boolean disappear = false;

	// Is the bullet going to produce a critical hit?
	private boolean criticalHit = false;

	// Has this projectile hit an enemy?
	public static boolean enemyHit = false;

	// Has this projectile hit a boss?
	public static boolean bossHit = false;

	// Its damage
	public int damage = 0;

	// Its speed
	public double speed = 0;

	// Uprotation of the projectile
	public double upRotation;

	// The enemy that fired the projectile.
	// Remains null if it was the player who shot it
	public Entity sourceEnemy = null;

	// Its position
	public double x = 0;
	public double y = 0;
	public double z = 0;

	// Distance from Player
	public double distanceFromPlayer = 0;

	// Its initial set speed
	public double initialSpeed = 0;

	// The type of projectile
	public int ID = 0;

	// The id of the client that shot this
	public int clientID = 0;

	// Change in x and z values.
	public double xa = 0;
	public double za = 0;

	public int projectilePhase = 0;

	// If a projectile is shot by a friendly, and it didn't hit the target, then
	// tell that entity to relocate and try again.
	private boolean hitTargetEnemy = false;

	// Is this a command bullet that will destroy anything
	public boolean itemKiller = false;

	// If this was a melee attack, play different sounds
	public boolean melee = false;

	// Melee hit sound to play
	private int meleeHitSound = 0;

	// Pixels that the projectile occupies on screen when rendered
	// public ArrayList<Integer> pixelsOnScreen = new ArrayList<Integer>();

	/**
	 * Sets up typical new projectile
	 * 
	 * @param damage
	 * @param speed
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 */
	public Projectile(int damage, double speed, double x, double y, double z, int ID, boolean criticalHit) {
		this.damage = damage;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ID = ID;
		this.criticalHit = criticalHit;
		initialSpeed = speed;

		if (this.ID == -1) {
			melee = true;

			Random rand = new Random();
			int sound = rand.nextInt(2);

			meleeHitSound = sound;
		}
	}

	/**
	 * Determines whether the projectile is free to move to the next space or not.
	 * In the future this will be optimized to track whether an enemy is hit here or
	 * not, but for now it just tracks whether it hits a wall, floor, ceiling, edge
	 * of map, or solid item. Sends back whether it can keep moving or not.
	 * 
	 * @param xx
	 * @param zz
	 * @return
	 */
	public boolean isFree(double nextX, double nextZ) {

		// Target enemy was not hit by default.
		hitTargetEnemy = false;

		// Distance from object the projectile can hit
		double z = 0.05;

		// If shot by player, there is no error added in to the projectiles
		if (this.sourceEnemy != null) {
			z = 0;
		}

		// The projectiles distance from the player. Updated every tick
		distanceFromPlayer = Math.sqrt(((Math.abs(x - Player.x)) * (Math.abs(x - Player.x)))
				+ ((Math.abs(this.z - Player.z)) * (Math.abs(this.z - Player.z))));

		// If projectile hits ceiling or floor. Stop it
		if ((y > 1 || -y * 10 >= Render3D.ceilingDefaultHeight)) {
			// Fixes bug where a rocket goes way below the floor
			// before it hits.
			if (y > 1) {
				y = 1;
			}

			// I don't even know how this happens. When staring straight
			// down the rocket hits a y of -37 or something. This corrects
			// that for rocket jumping.
			if (y < -36) {
				y = 1;
			}

			projectileHit(true);

			return false;
		}

		// Don't let projectile leave map
		if (x < 0 || this.z < 0 || x > Level.width || this.z > Level.height) {

			projectileHit(true);
			return false;
		}

		/*
		 * Determine the block the enemy is about to move into given the direction that
		 * it is going. Then set this block as the block to check the collision of.
		 * Technically it actually checks two blocks though. The two blocks that are in
		 * the direction that the enemy is going. So in case the enemy is moving to a
		 * position in between two blocks, and not directly at the block, it will make
		 * sure the enemy cannot move through
		 */
		Block block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
		Block block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));

		if (nextX < x && nextZ == z) {
			block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
		} else if (nextX >= x && nextZ == z) {
			block = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
		} else if (nextX == x && nextZ >= z) {
			block = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
		} else // (xx == x && zz < z)
		{
			block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
		}

		try

		{
			// Go through all the enemies on the block
			for (int i = 0; i < block.entitiesOnBlock.size(); i++) {
				EntityParent enemy = block.entitiesOnBlock.get(i);

				// Distance between projectile and other enemy
				double distance = Math.sqrt(((Math.abs(enemy.xPos - nextX)) * (Math.abs(enemy.xPos - nextX)))
						+ ((Math.abs(enemy.zPos - nextZ)) * (Math.abs(enemy.zPos - nextZ))));

				// If this projectile was fired by the same enemy, skip
				// everything below
				if (this.sourceEnemy != null && this.sourceEnemy.equals(enemy)) {
					// Nothing right now
				} else {
					double distanceCheck = 0.35;

					// So that even though reapers are faster, the
					// projectile will still hit them and not miss if
					// they are running towards or away from you.
					// Also increases hitbox range for phase shots though.
					if ((enemy.ID == 4 && enemy.activated) || ID == 2) {
						distanceCheck = 0.7;
					}

					// Marines are smaller, so smaller hitbox
					if (enemy.ID >= 10 && enemy.ID <= 14) {
						distanceCheck = 0.15;
					}

					// Due to uncertainty of enemy movements, this makes it easier for
					// enemies to hit other enemies because often it looks like it should
					// hit but it doesn't
					if (this.sourceEnemy != null) {
						distanceCheck = 0.5;
					}

					if (enemy.isABoss) {
						distanceCheck = 2;
					}

					// If close enough, hit enemy. If faster enemy then
					// have a greater hit distance as the enemy may be
					// speeding toward you or away from you.
					if (distance <= distanceCheck) {
						// IF PIXELS ON SCREEN CODE IS EVER ADDED BACK IN AGAIN, TAKE FROM 1.6 DEV 3
						// The old method of checking collision
						if ((((Math.abs((this.y) - ((enemy.getY()))) <= 0.6)) && enemy.ID != 100) || enemy.isABoss) {

							// If melee, play sword hitting flesh sound.
							if (melee) {
								SoundController.swordHit.playAudioFile(0);
							}

							if (this.sourceEnemy != null && this.sourceEnemy.isFriendly && enemy.isFriendly) {

								projectileHit(false);

								return false;
							} else if (this.sourceEnemy != null && this.sourceEnemy.isFriendly && !enemy.isFriendly
									&& !(this.sourceEnemy.ID == 18 && this.sourceEnemy.itemActivationID == -2)) {
								enemy.targetEnemy = this.sourceEnemy;
							}

							enemy.searchMode = false;

							/*
							 * If enemy that shot projectile is still alive Then reset target to that unless
							 * that target is the same type of enemy as the enemy that fired it because they
							 * are immune to their own attack. Watchers will not be effected by other
							 * enemies.
							 */
							if (this.sourceEnemy != null && ((this.sourceEnemy.ID != enemy.ID)
									|| (!enemy.isFriendly && this.sourceEnemy.isFriendly)) && enemy.ID != 9) {
								enemy.targetEnemy = this.sourceEnemy;

								double damage = this.damage;

								// If a critical hit
								if (criticalHit) {
									damage *= 2;
									Display.messages.add(new PopUp("CRITICAL HIT"));

									// Force of hit on enemy
									double force = 2;

									// Heavier enemies don't move as far
									force /= enemy.weightLevel;

									// Angle that the enemy is in accordance to the explosion for
									// throwing back calculations
									double rotFromTarget = Math.atan(((enemy.xPos - x)) / ((enemy.zPos - z)));

									/*
									 * If the target is in the 3rd or 4th quadrant of the map then add PI to
									 * rotation so that the enemy will be thrown back into the correct quadrant of
									 * the map
									 */
									if (enemy.zPos < z) {
										rotFromTarget += Math.PI;
									}

									/*
									 * Corrects rotation so that the vector is centered correctly in the direction
									 * its facing. It doesn't do that automatically for some reason
									 */
									double correction = 44.765;

									/*
									 * Depending on the targets angle in the x z plane from the explosion, the enemy
									 * will move back from that explosion a certain amount.
									 */
									enemy.xEffects = ((Math.cos(rotFromTarget - correction))
											+ (Math.sin(rotFromTarget - correction))) * force;
									enemy.zEffects = ((Math.cos(rotFromTarget - correction))
											- (Math.sin(rotFromTarget - correction))) * force;

									// Play critical hit sound
									SoundController.criticalHit.playAudioFile(0);
								}

								// Hurt enemy, and activate
								// the enemy if not already.
								enemy.hurt(damage, enemyHit);

								enemy.activated = true;
								enemy.searchMode = false;

								hitTargetEnemy = true;
							} else {
								// TODO check
								double damage = this.damage;

								// If a critical hit
								if (criticalHit) {
									damage *= 2;
									Display.messages.add(new PopUp("CRITICAL HIT"));

									// Force of hit on enemy
									double force = 2;

									// Heavier enemies don't move as far
									force /= enemy.weightLevel;

									// Angle that the enemy is in accordance to the explosion for
									// throwing back calculations
									double rotFromTarget = Math.atan(((enemy.xPos - x)) / ((enemy.zPos - z)));

									/*
									 * If the target is in the 3rd or 4th quadrant of the map then add PI to
									 * rotation so that the enemy will be thrown back into the correct quadrant of
									 * the map
									 */
									if (enemy.zPos < z) {
										rotFromTarget += Math.PI;
									}

									/*
									 * Corrects rotation so that the vector is centered correctly in the direction
									 * its facing. It doesn't do that automatically for some reason
									 */
									double correction = 44.765;

									/*
									 * Depending on the targets angle in the x z plane from the explosion, the enemy
									 * will move back from that explosion a certain amount.
									 */
									enemy.xEffects = ((Math.cos(rotFromTarget - correction))
											+ (Math.sin(rotFromTarget - correction))) * force;
									enemy.zEffects = ((Math.cos(rotFromTarget - correction))
											- (Math.sin(rotFromTarget - correction))) * force;

									// Play critical hit sound
									SoundController.criticalHit.playAudioFile(0);
								}

								// Hurt enemy. Watchers can only be hurt by rockets.
								if (enemy.ID != 9 || (enemy.ID == 9 && ID == 3)) {
									enemy.hurt(damage, enemyHit);
								}

								// Activate the enemy if not already.
								enemy.activated = true;

								// Enemies target is now you
								enemy.targetEnemy = null;

								// Not searching for you anymore, it will just come
								// right towards your position
								enemy.searchMode = false;
							}

							/*
							 * If an enemy is made happy, then remove that enemy from the game, and call the
							 * death function to drop any items or add to the made happy count of the game
							 * as needed.
							 */
							if (enemy.health <= 0) {
								// If a rocket, still add knockback even
								// though enemy is technically dead
								if (ID == 3) {
									// Force of explosion on enemy
									double force = 0;

									force = 3;

									// Heavier enemies don't move as far
									force /= enemy.weightLevel;

									// Angle that the enemy is in accordance to the explosion for
									// throwing back calculations
									double rotFromTarget = Math.atan(((enemy.xPos - x)) / ((enemy.zPos - z)));

									/*
									 * If the target is in the 3rd or 4th quadrant of the map then add PI to
									 * rotation so that the enemy will be thrown back into the correct quadrant of
									 * the map
									 */
									if (enemy.zPos < z) {
										rotFromTarget += Math.PI;
									}

									/*
									 * Corrects rotation so that the vector is centered correctly in the direction
									 * its facing. It doesn't do that automatically for some reason
									 */
									double correction = 44.765;

									/*
									 * Depending on the targets angle in the x z plane from the explosion, the enemy
									 * will move back from that explosion a certain amount.
									 */
									enemy.xEffects = ((Math.cos(rotFromTarget - correction))
											+ (Math.sin(rotFromTarget - correction))) * force;
									enemy.zEffects = ((Math.cos(rotFromTarget - correction))
											- (Math.sin(rotFromTarget - correction))) * force;

									double yCorrect = this.y;

									// Explosions y value doesn't go greater than 1 (Through the floor)
									if (this.y > 1) {
										yCorrect = 1;
									}

									// If explosion is above the enemy
									if (-yCorrect > enemy.yPos) {
										double yForce = Math.abs(yCorrect - (enemy.yPos));

										// A temporary fix
										// TODO fix this to be realistic
										yForce = 0.01;

										enemy.yEffects = ((-1 / yForce) * (force / 8)) / enemy.weightLevel;
									}
									// If explosion is below the enemy
									else {
										double yForce = Math.abs((enemy.yPos) - yCorrect) / 500;

										// A temporary fix
										// TODO Fix this to be realistic
										yForce = 0.01;

										enemy.yEffects = ((1 / yForce) * (force / 8)) / enemy.weightLevel;
									}

									if (enemy.yEffects > 30) {
										enemy.yEffects = 30;
									}
								}

								enemy.enemyDeath();
							}

							// If shotgun spread bullet
							if (ID == 1) {
								enemyHit = true;
							}

							HitSprite hS;

							// Add HitSprite to the game
							if ((criticalHit || melee) && enemy.ID != 9 && enemy.hasMovement) {
								hS = new HitSprite(this.x, this.z, this.y, 6);
							} else {
								if (enemy.ID != 9 && enemy.isHuman) {
									hS = new HitSprite(this.x, this.z, this.y, 3);
								} else {
									if (ID <= 1) {
										hS = new HitSprite(this.x, this.z, this.y, 1);
									}
								}
							}

							// IF this is a shot from the Scepter of Deciet
							// TODO check to make sure this works correctly.
							if (ID == 9 && !enemy.isABoss && !(enemy.ID == 18 && enemy.itemActivationID == -2)
									&& (enemy.ID == 18 || !enemy.moveable)) {
								enemy.isFriendly = true;
								Game.enemiesInMap--;
							}

							// Bullet texture does not stay in mid-air
							disappear = true;

							if (!melee) {
								projectileHit(false);
							}

							return false;
						}
					}
				}
			}
		} catch (Exception e) {
		}

		try {
			// If fired by an enemy, not the player
			if (sourceEnemy != null) {
				// Finds the distance the item is away from the player
				double distance = Math.sqrt(((Math.abs(this.x - Player.x)) * (Math.abs(this.x - Player.x)))
						+ ((Math.abs(this.z - Player.z)) * (Math.abs(this.z - Player.z))));

				// If it hits the player, and player is alive and not
				// invincible and the enemy is not friendly. No friendly fire.
				if (distance <= 0.3 && Math.abs((Player.yCorrect) + ((this.y * 11))) <= 8 && Player.alive
						&& !sourceEnemy.isFriendly) {
					double damage = this.damage;

					// If critical hit
					if (criticalHit) {
						damage *= 2;
						Display.messages.add(new PopUp("OWW! That was a critical!"));
					}

					// If a watcher projectile, add to how long the player is frozen.
					if (this.ID == 8) {
						Player.frozen = 150;
					}

					Player.hurtPlayer(damage);
					projectileHit(true);

					// If Player died to an enemy projectile, depending on the enemy display
					// a particular death message
					if (Player.health <= 0) {
						int enemyID = sourceEnemy.ID;

						if (!Display.smileMode) {
							switch (enemyID) {
							case (1):
								Display.messages.add(new PopUp("You were burnt alive by a Brainomorph!"));
								break;
							case (2):
								Display.messages.add(new PopUp("You were phased to death by a Sentinel!"));
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
							case (15):
								Display.messages.add(new PopUp("You were pistoled by an evil marine!"));
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
							switch (enemyID) {
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

					return false;
				}
			}
		} catch (Exception e) {

		}

		// If this is the host, see if the projectile hits any of the players
		if (Display.gameType == 0) {
			for (int i = 0; i < Game.otherPlayers.size(); i++) {
				ServerPlayer sP = Game.otherPlayers.get(i);
				// The projectiles distance from the player. Updated every tick
				double distanceFromClient = Math.sqrt(((Math.abs(x - sP.x)) * (Math.abs(x - sP.x)))
						+ ((Math.abs(this.z - sP.z)) * (Math.abs(this.z - sP.z))));

				// If it hits the player, and player is alive and not
				// invincible. And its not the players own projectile.
				if (distanceFromClient <= 0.3 && Math.abs((sP.y) + ((this.y * 10) / 1.2)) <= 8 && sP.alive
						&& sP.ID != this.clientID && sP.immortality == 0 && !sP.godModeOn) {
					double damage = this.damage;

					// If critical hit
					if (criticalHit) {
						damage *= 2;
						sP.clientMessages.add(new PopUp("OWW! That was a critical!"));
					}

					// System.out.println(this.clientID + " : " + sP.ID + " : " + damage);

					sP.hurtPlayer(damage);
					projectileHit(true);

					/*
					 * If the player dies, make sure to add to his/her deaths, add to the owner of
					 * the projectiles kills, and display the kill message to everyone.
					 */
					if (sP.health <= 0) {
						sP.deaths++;
						sP.alive = false;

						for (int j = 0; j < Game.otherPlayers.size(); j++) {
							ServerPlayer serverP = Game.otherPlayers.get(j);

							if (serverP.ID == this.clientID) {
								serverP.kills++;
							}

							sP.clientMessages.add(new PopUp("Player " + this.clientID + " destroyed Player " + sP.ID));
						}
					}

					return false;
				}
			}
		}

		// If this is a command bullet that will destroy anything in front of it
		if (itemKiller) {
			for (int i = 0; i < block.wallItems.size(); i++) {
				Item item = block.wallItems.get(i);

				double distanceFromItem = Math.sqrt(((Math.abs(x - item.x)) * (Math.abs(x - item.x)))
						+ ((Math.abs(this.z - item.z)) * (Math.abs(this.z - item.z))));

				if (distanceFromItem <= 0.1 && Math.abs((item.y) + ((this.y * 10) / 1.2)) <= 8) {
					item.removeItem();
					return false;
				}
			}

			for (int i = 0; i < block2.wallItems.size(); i++) {
				Item item = block2.wallItems.get(i);

				double distanceFromItem = Math.sqrt(((Math.abs(x - item.x)) * (Math.abs(x - item.x)))
						+ ((Math.abs(this.z - item.z)) * (Math.abs(this.z - item.z))));

				if (distanceFromItem <= 0.1 && Math.abs((item.y) + ((this.y * 10) / 1.2)) <= 8) {
					item.removeItem();
					return false;
				}
			}
		}

		/*
		 * Check to see if bullet hit a block, and return whether it did or not.
		 */
		if (block.isSolid && block2.isSolid) {
			return collisionChecks(block) && collisionChecks(block2);
		}

		return true;
	}

	/**
	 * Frees up code space and makes it easier to make changes to all the collision
	 * checks at once just changing just one method.
	 * 
	 * Basically just checks to see if the projectile hits a block. Because doors
	 * are moving and wierd, the projectile can just go through doors that are
	 * moving for now.
	 * 
	 * @param block
	 * @return
	 */
	public boolean collisionChecks(Block block) {
		// Corrects projectile correctly
		double yCorrect = (-(y - 0.5) * 11) + 1;

		/*
		 * If bullet is in between the top and bottom of the block, or below a block
		 * that is on the ground.
		 */
		if (!hit && yCorrect > (block.y * 4) && yCorrect < ((block.y * 4) + (block.height)))
		/*
		 * || (block.isSolid && y >= 0 && block.y <= 2 && block.height > 0 &&
		 * ((Player.yCorrect >= Player.y && this.sourceEnemy == null)) ||
		 * this.sourceEnemy != null))))
		 */
		{
			try {
				// If this is a command bullet (destroys anything) then the wall will be
				// destroyed no matter what.
				if (itemKiller) {
					block.health -= damage;

					SoundController.explosion.playAudioFile(distanceFromPlayer);

					if (block.health <= 0) {
						// Keep track of the enemies on the block before
						// the block changes
						ArrayList<EntityParent> temp = block.entitiesOnBlock;
						ArrayList<Item> temp2 = block.wallItems;

						// New air block
						block = new Block(0, 0, 0, block.x, block.z);

						// Same enemies are on this block as the one that
						// it changed from
						block.entitiesOnBlock = temp;
						block.wallItems = temp2;

						// Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}

					// Add HitSprite to the game
					HitSprite hS = new HitSprite(this.x, this.z, this.y, 5);

					Game.sprites.add(hS);

					// Makes sure alternate bullet hit sprites aren't rendered
					disappear = true;
				}

				/*
				 * Go through all items on the block, and if one of them labels the wall as
				 * breakable, then make it so the projectile damages the wall.
				 */
				for (int i = 0; i < block.wallItems.size(); i++) {
					Item item = block.wallItems.get(i);

					if (item != null && item.itemID == ItemNames.BREAKABLEWALL.getID() && !hit) {
						block.health -= damage;

						// This is a glass wall
						if (block.wallID == 4) {
							// Glass hit/break sound
							SoundController.glassBreak.playAudioFile(distanceFromPlayer);
						}
						// If it is a box block
						else if (block.wallID == 21) {
							// Box break sound effect. Randomizes each time
							Random rand = new Random();
							int boxHitSound = rand.nextInt(2);

							if (boxHitSound == 0) {
								SoundController.boxBreak1.playAudioFile(distanceFromPlayer);
							} else {
								SoundController.boxBreak2.playAudioFile(distanceFromPlayer);
							}
						} else {
							SoundController.explosion.playAudioFile(distanceFromPlayer);
						}

						if (block.health <= 0) {
							// Remove breakable wall item
							Game.items.remove(item);

							// Keep track of the enemies on the block before
							// the block changes
							ArrayList<EntityParent> temp = block.entitiesOnBlock;
							block.wallItems.remove(item);
							ArrayList<Item> temp2 = block.wallItems;

							// New air block
							block = new Block(0, 0, 0, block.x, block.z);

							// Same enemies are on this block as the one that
							// it changed from
							block.entitiesOnBlock = temp;
							block.wallItems = temp2;

							// Re-add to level
							Level.blocks[block.x + block.z * Level.width] = block;
						}

						// Add HitSprite to the game
						HitSprite hS = new HitSprite(this.x, this.z, this.y, 5);

						Game.sprites.add(hS);

						// Makes sure alternate bullet hit sprites aren't rendered
						disappear = true;
					}
				}

				// If electric wall
				if (block.wallID == 15 && !hit) {
					// Damage wall
					block.health -= damage;

					// Blass glass hit/break sound
					SoundController.explosion.playAudioFile(distanceFromPlayer);

					// If block is broken
					if (block.health <= 0) {
						// New dead electric block
						block = new Block(block.height, 19, block.y * 4, block.x, block.z);

						// Computer Shutting down sound effect
						SoundController.computerShutdown.playAudioFile(distanceFromPlayer);

						// Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}
				}
			} catch (Exception e) {

			}

			// Loop through this bringing the projectile closer and closer to
			// the wall until it hits the wall. This makes it so that rockets
			// will explode right next to the wall instead of farther out like
			// they used to. It also fixes hit sprites going into walls for all
			// projectiles
			while ((int) this.x != block.x || (int) this.z != block.z) {
				// Move at tenth the speed it was originally for
				// smaller more precise increments
				this.x += xa / 10;
				this.z += za / 10;

				// Makes it so an infinite loop does not occur if the
				// projectile goes through the corner of a wall and misses
				// its chance to check collision or not.
				if ((xa > 0 && this.x + xa > block.x) || (xa < 0 && this.x + xa < block.x)) {
					break;
				}

				// Same as above but for the z
				if ((za > 0 && this.z + za > block.z) || (za < 0 && this.z + za < block.z)) {
					break;
				}
			}

			/*
			 * For doors and elevators, bullet holes disappear quicker as well so that if
			 * the door or elevator moves there won't be "floating" bullet holes.
			 */
			if (block.isADoor && ID <= 1) {
				// Add HitSprite to the game
				HitSprite hS = new HitSprite(this.x, this.z, this.y, 5);

				Game.sprites.add(hS);

				// Makes sure alternate bullet hit sprites aren't rendered
				disappear = true;
			}

			// Projectile hit something.
			projectileHit(true);

			// Can't move anymore
			return false;
		}

		// Idk why but for some reason the bullet comes back through this
		// loop sometimes even after its already hit something. So this
		// doesn't allow it to continue in that case.
		if (hit) {
			return false;
		}

		return true;
	}

	/**
	 * Takes care of rockets, and sets hit to being true for projectiles
	 */
	public void projectileHit(boolean playSound) {

		// Tell entity to relocate if they didn't hit the target
		if (!hitTargetEnemy && this.sourceEnemy != null) {
			this.sourceEnemy.relocating = true;
		}

		// If already hit, don't replay sound like it does for some
		// weird reason. Just return.
		if (hit) {
			return;
		}

		hit = true;

		// Normal bullet, not rocket
		if (ID <= 1 && !disappear && ID > -1) {
			if (playSound) {
				try {
					SoundController.wallHit.playAudioFile(distanceFromPlayer);
				} catch (Exception e) {

				}
			}

			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);

			Game.sprites.add(hS);
		}
		// Phase cannon hit
		else if (ID == 2) {
			SoundController.phaseCannonHit.playAudioFile(distanceFromPlayer);

			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);

			Game.sprites.add(hS);
		}
		// Sword Hit
		else if (ID == -1) {
			if (meleeHitSound == 0) {
				SoundController.swordWall1.playAudioFile(distanceFromPlayer);
			} else {
				SoundController.swordWall2.playAudioFile(distanceFromPlayer);
			}

			HitSprite hS = new HitSprite(this.x, this.z, this.y, 0);

			Game.sprites.add(hS);
		}
		// Rockets
		else if (ID == 3) {
			// Stop playing the rocket flying clip
			SoundController.rocketFly.stopAll();

			// Play hit sound effect
			SoundController.explosion.playAudioFile(distanceFromPlayer);

			double yCorrect = this.y;

			Block blockOn = Level.getBlock((int) this.x, (int) this.z);

			/*
			 * If the rocket goes through the block or floor that the player is on, then
			 * reset its height to be the block it went throughs height so that rocket
			 * jumping will work correctly.
			 */
			if (-yCorrect < (blockOn.height + blockOn.y) - 1) {
				yCorrect = -(blockOn.height + blockOn.y - 1);
			}

			// Add an explosion where it hit and tick it so it has an
			// effect right after hit.
			Explosion temp = new Explosion(this.x, this.y, this.z, 0, 0);

			temp.tick();

			Game.explosions.add(temp);
		}
		// If an enemy projectile
		else if (ID >= 4 && ID < 9) {
			try {
				SoundController.enemyFireHit.playAudioFile(distanceFromPlayer);
			} catch (Exception e) {

			}

			HitSprite hS = null;

			if (ID == 5 || ID == 7 || ID == 8) {

				try {
					SoundController.shockHit.playAudioFile(distanceFromPlayer);
				} catch (Exception e) {

				}

				hS = new HitSprite(this.x, this.z, this.y, 7);

			} else {
				// Add HitSprite to the game
				hS = new HitSprite(this.x, this.z, this.y, 4);
			}

			Game.sprites.add(hS);
		}

		// If a Deciet Scepter shot hit
		else if (ID == 9) {
			try {
				SoundController.enemyFireHit.playAudioFile(distanceFromPlayer);
				SoundController.shockHit.playAudioFile(distanceFromPlayer);
			} catch (Exception e) {

			}

			HitSprite hS = null;

			hS = new HitSprite(this.x, this.z, this.y, 8);

			Game.sprites.add(hS);
		}

		// Remove this projectile from the game
		if (Game.enemyProjectiles.contains(this)) {
			Game.enemyProjectiles.remove(this);
		} else {
			Game.bullets.remove(this);
		}
	}
}
