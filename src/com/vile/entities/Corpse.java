package com.vile.entities;

import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.graphics.Render;
import com.vile.graphics.Textures;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Corpse
 * 
 * @author Alexander Byrd Date Created: 10/5/2016
 * 
 *         Description: This stores all the values needed for a corpse entity
 *         whose soul purpose is for aesthetic feel, or for resurrection by a
 *         magistrate enemy.
 *
 *         Also now holds values to determine if it moves because of a rocket
 *         blast. If an enemy is killed and is being shot backwards by a rocket
 *         blast, the force still acts on its corpse, so this keeps track of
 *         that.
 */
public class Corpse extends EntityParent {
	// The usual values
	// public double x = 0;
	// public double z = 0;
	// public double y = 0;

	// How much additional movement effects the corpse has on it from
	// an explosion source
	// public double xEffects = 0;
	// public double zEffects = 0;
	// public double yEffects = 0;

	// How far into death animation it is.
	public int phaseTime = 0;

	// Time corpse has been here.
	public int time = 0;

	// Zero is default meaning wasn't alive, or not an enemy
	public int enemyID = 0;

	// If it is a serverPlayer then the corpse will be added.
	public int clientID = -1;

	// Default image of the corpse
	public Render corpseImage = null;

	// Type of default corpse it is if it is one
	private int corpseType = 0;

	private boolean repeatAnimation = false;

	/**
	 * Constructs a new Corpse of a certain x, y, and z value, and the ID of the
	 * enemy it was before it was killed, or if it was never alive, then use 0.
	 * 
	 * @param x
	 * @param z
	 * @param y
	 * @param enemyID
	 */
	public Corpse(double x, double z, double y, int enemyID, double xEffects, double zEffects, double yEffects,
			boolean repeatAnimation) {
		super(100, 0, 0, 0, 1.0 / 21.0, x, y, z, 100, 0, 0);
		// Set values
		this.xPos = x;
		this.zPos = z;
		this.yPos = y;
		this.xEffects = xEffects;
		this.zEffects = zEffects;
		this.yEffects = yEffects;
		this.enemyID = enemyID;
		this.repeatAnimation = repeatAnimation;

		activated = true;
		searchMode = false;

		// Normal enemy death animation time
		if (enemyID != 0 && enemyID != 8) {
			this.phaseTime = 24;
		}

		// For Belegoth
		if (enemyID == 8) {
			this.phaseTime = 48;
		}

		Random rand = new Random();
		corpseType = rand.nextInt(6);
	}

	/**
	 * Tick the time the corpse has been here, and also calculate the force of an
	 * explosion still acting on it and perform the required operations.
	 */
	public void tick() {
		time++;

		distanceFromPlayer = Math.sqrt(((Math.abs(this.getX() - Player.x)) * (Math.abs(this.getX() - Player.x)))
				+ ((Math.abs(this.getZ() - Player.z)) * (Math.abs(this.getZ() - Player.z))));

		// Reset animation if it is to be repeated
		if (repeatAnimation && phaseTime == 0) {
			phaseTime = 24;
		}

		// Toilet continues to have water rushing
		if (this.enemyID == 16 && time % 40 == 0) {
			SoundController.waterSpray.playAudioFile(this.distanceFromPlayer * 4);
		}

		// Keeps track of corpse image in case resource pack changes
		if (corpseType == 0) {
			corpseImage = Textures.defaultCorpse1;
		} else if (corpseType == 1) {
			corpseImage = Textures.defaultCorpse2;
		} else if (corpseType == 2) {
			corpseImage = Textures.defaultCorpse3;
		} else if (corpseType == 3) {
			corpseImage = Textures.defaultCorpse4;
		} else if (corpseType == 4) {
			corpseImage = Textures.defaultCorpse5;
		} else {
			corpseImage = Textures.defaultCorpse6;
		}

		// Don't let the time ticker go too high. Especially
		// if in smileMode or as a host of a multiplayer game.
		if (time > 10000 || (time > 1000 && Display.smileMode) || (Display.gameType == 0 && time > 1000)) {
			time = 0;

			// If in survival, corpses disappear after a bit
			// to make the game faster.
			if (FPSLauncher.gameMode == 1 || Display.smileMode || Display.gameType == 0) {
				Game.corpses.remove(this);
				return;
			}
		}

		/*
		 * All for dealing with the force of explosions propelling enemies in some
		 * direction
		 */
		double xEff = 0;
		double zEff = 0;
		double yEff = 0;

		// Set movement for tick
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

		/*
		 * Can the force of the explosion push the enemy any more into the x direction?
		 */
		if (isFree(xPos + (xEff), zPos)) {
			xPos += (xEff);
		}

		/*
		 * Can the explosion push the enemy anymore into the z direction.
		 */
		if (isFree(xPos, zPos + (zEff))) {
			zPos += (zEff);
		}

		// Update effect values based on what was executed above
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

		// Only if Smile resource pack. NOT IN USE RIGHT NOW
		if (false) {
			// See if any of the happy teleporters are in sight
			// and if they are make that the new target
			for (int i = 0; i < Game.happySavers.size(); i++) {
				Item temp = Game.happySavers.get(i);

				double targetX = temp.x;
				double targetZ = temp.z;
				double targetY = temp.y;

				// Angle that the target is in accordance to the entity so
				// that entity looks right towards its target
				// sin/cos in this case
				double sightRotation = Math.atan(((targetX - xPos)) / ((targetZ - zPos)));

				/*
				 * If the target is in the 3rd or 4th quadrant of the map then add PI to
				 * rotation so that the entitywill move into the correct quadrant of the map and
				 * at the target.
				 */
				if (targetZ < zPos) {
					sightRotation += Math.PI;
				}

				/*
				 * Corrects rotation so that the entityis centered correctly in the map graph
				 */
				double correction = 44.765;

				// Speed that the eyesight travels
				double speed = 0.1;

				/*
				 * Eyesight trajectory of the entity so that it will be checking a straight line
				 * of sight to the player.
				 */
				double sightX = ((Math.cos(sightRotation - correction)) + (Math.sin(sightRotation - correction)))
						* speed;
				double sightZ = ((Math.cos(sightRotation - correction)) - (Math.sin(sightRotation - correction)))
						* speed;

				// How much the eyesight moves total each check.
				// The hypotenuse of the x and z movement
				double moveDistance = Math.sqrt((sightX * sightX) + (sightZ * sightZ));

				// Total hypotenuse between target and entity
				double hypot = Math.sqrt(((xPos - targetX) * (xPos - targetX)) + ((zPos - targetZ) * (zPos - targetZ)));

				// Difference between entity and target y values
				double yDifference = Math.abs(yPos - 8) - Math.abs(targetY);

				// Number of moves the eyesight will check for if it reaches
				// the target successfully
				double iterations = hypot / moveDistance;

				// How much y will have to change each time
				double sightY = yDifference / iterations;

				// Resets the eyesight object with its new values
				eyeSight = new Eyesight(this.xPos, this.zPos, this.yPos - 8, targetX, targetZ, targetY, sightX, sightZ,
						sightY);

				boolean tempSight = false;

				// Checks to see if target is in its line of sight
				tempSight = eyeSight.checkEyesight();

				// If happy teleporter is in sight
				if (tempSight) {
					// Only set this to true if the secondary target is in sight
					inSight = true;

					// New target is this item
					targetItem = temp;
				} else {
					// If the corpse can no longer see the target, then
					// no longer have it.
					if (temp.equals(targetItem)) {
						targetItem = null;
						super.targetX = Player.x;
						super.targetZ = Player.z;
						super.targetY = Player.y;
					}
				}
			}

			time++;
			super.tick(this);
			super.move();
		}
	}

	/**
	 * Here so that the game doesn't crash since all entities, even this, can be
	 * "hurt"
	 * 
	 * @param damage
	 */
	@Override
	public void hurt(double damage, boolean soundPlayed) {
		return;
	}

	/**
	 * Determines whether the corpse is free to move to the next space or not.
	 * 
	 * @param xx
	 * @param zz
	 * @return
	 */
	@Override
	public boolean isFree(double nextX, double nextZ) {
		double bufferZone = 0.3;

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
		Block block = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ - bufferZone));
		Block block2 = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ + bufferZone));

		if (nextX < this.xPos && nextZ == this.zPos) {
			block = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ - bufferZone));
			block2 = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ + bufferZone));
		} else if (nextX >= this.xPos && nextZ == this.zPos) {
			block = Level.getBlock((int) (nextX + bufferZone), (int) (nextZ - bufferZone));
			block2 = Level.getBlock((int) (nextX + bufferZone), (int) (nextZ + bufferZone));
		} else if (nextX == this.xPos && nextZ >= this.zPos) {
			block = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ + bufferZone));
			block2 = Level.getBlock((int) (nextX + bufferZone), (int) (nextZ + bufferZone));
		} else // (xx == xPos && zz < zPos)
		{
			block = Level.getBlock((int) (nextX - bufferZone), (int) (nextZ - bufferZone));
			block2 = Level.getBlock((int) (nextX + bufferZone), (int) (nextZ - bufferZone));
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
			return collisionChecks(block) && collisionChecks(block2);
		} else {
			try {
				for (Item temp : block.wallItems) {
					// If there is a solid item on the block, and its not a
					// tree, and its within the y value of the entity, and
					// the entity is not a reaper, the corpse can't move into that
					// block
					if (Game.solidItems.contains(temp) && Math.abs(temp.y + (yPos - 3)) <= temp.height) {
						return false;
					}
				}
			} catch (Exception E) {

			}
		}

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
	public boolean collisionChecks(Block block) {
		/*
		 * Because the y is automatically 3 higher than what it appears to be to make it
		 * appear like it does, this corrects it back for collision detection purposes.
		 */
		double yCorrect = yPos - 3;

		/*
		 * The corpse can't move forward anyway if the block its moving to has a solid
		 * object on it
		 */
		try {
			// For all wall items on this block
			for (Item temp : block.wallItems) {
				// If there is a solid item on the block, and its not a
				// tree, and its within the y value of the entity, and
				// the entity is not a reaper, the corpse can't move into that
				// block
				if (Game.solidItems.contains(temp) && Math.abs(temp.y + (yPos - 3)) <= temp.height) {
					return false;
				}
			}
		} catch (Exception E) {

		}

		/*
		 * If the block in front of the corpse is greater than two units higher than the
		 * corpse, or the corpse is still not far enough under a block to go through it
		 * (mainly used with doors) then don't allow the corpse to move.
		 */

		// TODO FIX THIS COLLISION CRAP

		// if(((block.height + block.y - (2)) >
		// -yCorrect && -yCorrect + (2) >
		// block.y && !block.isaDoor))
		// {
		// return false;
		// }

		return false;
	}
}
