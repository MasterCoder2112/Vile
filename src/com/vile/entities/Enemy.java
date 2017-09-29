package com.vile.entities;

import java.util.ArrayList;
import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.SoundController;
import com.vile.entities.Enemy;
import com.vile.entities.Player;
import com.vile.graphics.Render3D;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Enemy
 * @author Alex Byrd
 * Date Updated: 6/9/2017
 *
 * Keeps track of the enemies status such as movement, attacking, and
 * other such things. Individual methods and such are commented
 * seperately.
 */
public class Enemy extends Entity implements Comparable
{
	//Initial y value of enemy
	public double startY = 20;
	
	//Initial speed
	public double initialSpeed = 0;
	
	//Corrects height that the enemy should actually be at
	public double heightCorrect = 0;
	
	//What walking phase is the enemy in, and which direction in
	//degrees is the enemy moving
	public double direction = 0;
	
	//What is the acceleration down
	public double acceleration = 0.03;
	
	//What is the speed that the enemy is currently falling
	public double fallSpeed = 1;
	
	//How heavy is the enemy
	public int weightLevel = 1;
	
	//private double totalHeight = 0;
	
	//ENEMY FLAGS	
	//Is the flying enemy coming down on the player
	public boolean tracking = false;
	
	//Has an enemy target instead of Player
	public boolean newTarget = false;
	
	//Going through a door? Used for AI
	private boolean doorway = false;
	
	//Makes sure the set height "if" is only called once initially
	private boolean firstTest = true;
	
   /**
    * Creates a new enemy with values sent in. The sets up the enemy based
    * on gamemode, fps, and enemy type. This is the default constructor.
    * @param armor
    * @param ammo
    * @param damage
    * @param x
    * @param y
    * @param z
    */
	public Enemy(double x, double y, double z, int ID, double rotation,
			int itemActivationID) 
	{
		super(100, 0, 0, 6, 0, x, y, z, ID, rotation, itemActivationID);
		
		//Default speed is 1
		speed = 1;
		
		//Brainomorph
		if(ID == 1)
		{
			health = 120;
			hasSpecial = true;
			weightLevel = 2;
		}
		//Sentinel
		else if(ID == 2)
		{
			health = 150;
			hasSpecial = true;
			canFly = true;
			weightLevel = 2;
		}
		//Mutated Commando
		else if(ID == 3)
		{
			speed  *= 0.25;
			health  = 300;
			damage *= 4;
			hasSpecial = true;
			weightLevel = 3;
		}
		//Reaper
		else if(ID == 4)
		{
			speed *= 6;
			damage = 4;
			health = 40;
			weightLevel = 1;
		}
		//Magistrate
		else if(ID == 5)
		{
			damage = 2;
			health = 100;
			weightLevel = 1;
			
			//Increases time it takes to use special which is resurrecting
			tickAmount = 24;
		}
		//Morgoth
		else if(ID == 6)
		{
			speed *= 2;
			damage = 25;
			health = 5000;
			height = 60;
			weightLevel = 10;
			isABoss = true;
			hasSpecial = true;
			Game.bosses.add(this);
		}
		//Vile Warrior
		else if(ID == 7)
		{
			damage = 3;
			health = 40;
			weightLevel = 1;
		}
		//Belegoth
		else if(ID == 8)
		{
			speed *= 2;
			damage = 50;
			health = 10000;
			isABoss = true;
			height = 60;
			weightLevel = 10;
			hasSpecial = true;
			Game.bosses.add(this);
		}
		
		//If easy mode
		if(Display.skillMode == 1)
		{
			speed  *= 0.5;
			damage *= 0.5;
		}
		//If Death cannot touch me or above
		else if(Display.skillMode >= 4)
		{
			speed  *= 1.5;
			damage *= 2;
			health *= 1.5;
		}
		//If Bring it on or above
		else if(Display.skillMode >= 3)
		{
			health *= 1.25;
			damage *= 1.5;
		}
		//If peaceful mode
		else if(Display.skillMode == 0)
		{
			//speed = 0;
			damage = 0;
		}
		
		distanceFromPlayer = Math.sqrt(((Math.abs(getX() - Player.x))
				* (Math.abs(getX() - Player.x)))
				+ ((Math.abs(getZ() - Player.z))
						* (Math.abs(getZ() - Player.z))));
		
		//Sets speed to specific units the map uses
		speed /= 21.3;
		
		//Initial speed is set to be used later
		initialSpeed = speed;
		
		setHeight();
		
		//If survival
		if(FPSLauncher.gameMode == 1)
		{
			activated = true;
			
			//Depending on ID, play entity activation sound
			switch(ID)
			{
				case 6:
					SoundController.bossActivate.
					playAudioFile(0);
					break;
			
				case 8:
					SoundController.belegothActivate.
					playAudioFile(0);
					break;
			
				case 1:
					SoundController.enemyActivate.
					playAudioFile(distanceFromPlayer);
					break;
			
				case 2:
					SoundController.enemy2Activate.
					playAudioFile(distanceFromPlayer);
					break;
			
				case 3:
					SoundController.enemy3Activate.
					playAudioFile(distanceFromPlayer);
					break;
			
				case 4:
					SoundController.enemy4Activate.
					playAudioFile(distanceFromPlayer);
					break;
			
				case 5:
					SoundController.enemy5Activate.
					playAudioFile(distanceFromPlayer);
					break;
			
				case 7:
					SoundController.enemy7Activate.
					playAudioFile(distanceFromPlayer);
					break;
					
				default:
					SoundController.enemyActivate.
					playAudioFile(distanceFromPlayer);
					break;
			}
		}
	}
	
   /**
    * This is the specific constructor of an enemy. If you want to create
    * a specific enemy, you can call this constructor and send in the
    * specific values you want to create the enemy of your choice. 
    */
	public Enemy(int health, int armor, int ammo, int damage,
			int speed, double x, double y, double z, int ID,
			double rotation, int itemActivationID)
	{
		super(health, armor, ammo, damage, speed, x, y, z, ID,
				rotation, itemActivationID);
	}
	
   /**
    * Keeps track of the enemies movement each turn
    */
	public void move()
	{			
		//Set enemies height based on block or object its on
		setHeight();
		
	   /*
	    * With faster ticks, the enemies move faster, and therefore to
	    * correct the faster movements, this slows them down so that
	    * the game is still winnable.
	    */
		if(Display.fps >= 60)
		{
			speed = initialSpeed / ((Display.fps / 60) + 1);
		}
		else
		{
			speed = initialSpeed / 1.5;
		}
		
		//If there is a target other than the player causing infighting
		if(targetEnemy != null)
		{
			targetX = targetEnemy.xPos;
			targetY = targetEnemy.yPos;
			targetZ = targetEnemy.zPos;
		}
		//If target is the player
		else
		{
			targetX = Player.x;
			targetY = Player.y;
			targetZ = Player.z;
		}
		
		//Angle that the target is in accordance to the enemy so
		//that enemy moves right towards the target
		rotationFromTarget = Math.atan
		(((targetX - xPos)) / ((targetZ - zPos)));
		
		//Angle that the player is in accordance to the enemy so
		//that the enemy is facing correctly to the player
		rotationFromPlayer = Math.atan
		(((Player.x - xPos) * 1) / ((Player.z - zPos) * 1));
		
	   /*
	    * If the target is in the 3rd or 4th quadrant of the map then
	    * add PI to rotation so that the enemy will move into
	    * the correct quadrant of the map and at the target.
	    */
		if(targetZ < zPos)
		{
			rotationFromTarget += Math.PI;
		}
		
		//No negative angles
		if(rotationFromTarget < 0)
		{
			rotationFromTarget = (2 * Math.PI) 
					+ rotationFromTarget;
		}	
		
	   /*
	    * If the target is in the 3rd or 4th quadrant of the map then
	    * add PI to rotation so that the enemy will face into
	    * the correct quadrant of the map and at the target.
	    */
		if(Player.z < zPos)
		{
			rotationFromPlayer += Math.PI;
		}
		
		//No negative angles
		if(rotationFromPlayer < 0)
		{
			rotationFromPlayer = (2 * Math.PI) 
					+ rotationFromPlayer;
		}
		
		//Block enemy is now on
		Block blockOn = Level.getBlock((int)xPos, (int)zPos);
		
	   /*
	    * All for dealing with the force of explosions propelling
	    * enemies in some direction
	    */
		double xEff = 0;
		double zEff = 0;
		double yEff = 0;
		
		if(xEffects > 0)
		{
			xEff = 0.2;
		}
		else if(xEffects < 0)
		{
			xEff = -0.2;
		}
		
		if(zEffects > 0)
		{
			zEff = 0.2;
		}
		else if(zEffects < 0)
		{
			zEff = -0.2;
		}
		
		if(yEffects > 0)
		{
			yEff = 2;
		}
		else if(yEffects < 0)
		{
			yEff = -2;
		}
		
		this.yPos -= (yEff);
		
		//If the enemy is in the air (Usually from a explosion)
		//Make it fall.
		if(-yPos > -maxHeight && !canFly)
		{
			yPos += 0.2 * weightLevel * fallSpeed;
			fallSpeed += acceleration;
		}
		else
		{
			fallSpeed = 1;
		}
		
		//Don't let enemy go through floor or block if pushed that way.
		if(yPos > maxHeight)
		{
			yPos = maxHeight;
		}
		
	   /*
		* Can the force of the explosion push the enemy any more into
	    * the z direction and if so add that effect to the zPos of the
	    * enemy. Only if there is an effect.
		*/
		if(zEff != 0)
		{
			if(isFree(xPos, zPos + (zEff)))
			{
				zPos += (zEff);
			}
		}
		
	   /*
	    * Can the force of the explosion push the enemy any more into
	    * the x direction and if so add that effect to the xPos of the
	    * enemy. Only if there is an effect.
	    */
		if(xEff != 0)
		{
			if(isFree(xPos + (xEff), zPos))
			{
				xPos += (xEff);
			}
		}
		
		//If peaceful mode, the enemy only moves if hit by and explosion
		//so this updates what block its on still.
		if(Game.skillMode == 0)
		{
			//Block enemy is now on
			Block blockOnNew = Level.getBlock((int)xPos, (int)zPos);
			
		   /*
		    * If the enemy is on a new block, then remove the enemy
		    * from the last block it was on and add it to the new
		    * block its on.
		    */
			if(!blockOnNew.equals(blockOn))
			{
				blockOn.enemiesOnBlock.remove(this);
				blockOnNew.enemiesOnBlock.add(this);
				
				setHeight();
			}	
			
			//Allows enemies to ride lifts/elevators
			//Basically set enemies position to the position of the current
			//block.
			if(blockOnNew.wallID == 8 && !canFly
					&& Math.abs(blockOnNew.height + yPos) <= 2)
			{
				yPos = -(blockOnNew.height + blockOnNew.y);
			}
			
			return;
		}
		
		//If the enemy is activated, and isn't attacking then move
		//normally
		if(super.activated && !super.isFiring && !super.isAttacking
				&& super.harmed == 0)
		{
			//Current block enemy is on. Re-update from above
			blockOn = Level.getBlock((int)xPos, (int)zPos);
			
			//If enemy can fly
			if(canFly)
			{
			   /*
			    * Used to correct the players y in case the player is 
			    * crouching and the y goes below the maxHeight the player
			    * can stand on.
			    */
				double yCorrect = targetY;
				
				//If target is player
				if(targetEnemy == null)
				{
					if(yCorrect < Player.maxHeight)
					{
						yCorrect = Player.maxHeight;
					}
				}

			    //If distance is less than or equal to 5 units from
				//the player, and its not stuck
				if(distance <= 5 && !isStuck)
				{
					//If above player
					if(Math.abs(yPos) > (yCorrect + 1) + 0.25)
					{
						//If negligibly close to top of the block
						//set the y to slightly above the block
						if(Math.abs(yPos + blockOn.height) <= 0.25)
						{
							yPos = -(blockOn.height + 0.25);
						}
						//If not then float down
						else if((Math.abs(yPos)) > blockOn.height)
						{
							yPos += 0.5;
							tracking = true;
						}
					}
					//If target is above enemy, fly up
					else if(Math.abs(yPos) < (yCorrect + 1))
					{
						yPos -= 0.5;
						tracking = true;
					}
				   /*
				    * If enemy is negligible close to the ground, and is not
				    * stuck, then stay floating 1/10 units above the ground.
				    */
					else if(Math.abs(yPos) < (1 / 10))
					{
						yPos = -(1/10);
						tracking = true;
					}
				   /*
				    * If the enemy is negligibly close to the player, just
				    * match up the height with the players.This small
				    * correction is so small it will never be noticed if
				    * playing the game.
				    */
					else
					{
						yPos = -(yCorrect + 1);
						tracking = true;
					}
				}		
				//If farther than a distance of 5 from the player
				//Or the enemy is stuck
				else if(distance > 5 || isStuck)
				{
				   /*
				    * If stuck behind a wall, or below its default height
				    * when not in range of player, then fly up
				    */
					if(yPos > -startY + 0.25 || isStuck)
					{
						yPos -= 0.5;
						tracking = false;
					}
				   /*
				    * If not stuck, and not in range of player, yet still 
				    * above the default flying height, then fly down.
				    */
					else if(yPos < -startY - 0.25 && !isStuck)
					{
						yPos += 0.5;
						tracking = false;
					}
					//If near enough to start Y, then set that to the new
					//height
					else
					{
						yPos = -startY;
						tracking = false;
					}
				}
				
				//Set the lowest the enemy can fly as being 1/10 units
				//above the ground
				if(yPos > -1/10)
				{
					yPos = -1/10; 
				}
				
				//Enemy cannot fly higher than ceiling
				if(yPos <= -Render3D.ceilingDefaultHeight)
				{
					yPos = -Render3D.ceilingDefaultHeight;
				}
			}
			
		   /*
		    * For the magistrate enemy, check to see if a corpse is
		    * in range of being resurrected, and resurrect the corpse
		    * if so.
		    */
			if(ID == 5)
			{
				//TODO here
				//Check all corpses in the game
				for(int i = 0; i < Game.corpses.size(); i++)
				{
					Corpse corpse = Game.corpses.get(i);
					
					Block corpseBlock = Level.getBlock
							((int)corpse.x, (int)corpse.z);
					
					//Find the distance between a corpse and the enemy
					double distance = 
							Math.sqrt(((Math.abs(getX() - corpse.x))
							* (Math.abs(getX() - corpse.x)))
							+ ((Math.abs(getZ() - corpse.z))
									* (Math.abs(getZ() - corpse.z))));
					
				   /*
				    * If enemy is within a 1 unit range of corpse. Also
				    * corpse cannot be another mage enemy or boss
				    * otherwise they'd just keep resurrecting each other.
				    * It'd be impossible. Also they have to be within a
				    * height of 4 units from each other, and the corpse 
				    * cannot be a default corpse with no ID.
				    */
					if(distance <= 1 && corpse.enemyID != 6 && corpse.enemyID != 0
							&& corpse.enemyID != 8
							&& Math.abs(getY() - corpse.y) <= 4
							&& !corpseBlock.isaDoor)
					{	
						//Can the mage resurrect the corpse yet
						if(canResurrect)
						{
							//Reset the ability to resurrect again.
							canResurrect = false;
							
						   /*
						    * Reconstruct enemy depending on the ID the
						    * corpse was before it became sad. 
						    * Also activate the new enemy.
						    */
							Enemy newEnemy = new Enemy(corpse.x, 0, corpse.z,
									corpse.enemyID, 0, 0);
							
							newEnemy.activated = true;
							
							//Add new enemy to game
							Game.enemies.add(newEnemy);
							
							//Remove the corpse from the map
							Game.corpses.remove(i);
							
							//Add to enemies in the map
							Game.enemiesInMap++;
						}
						else
						{
						   /*
						    * If mage should be able to resurrect but
						    * isn't in the process of doing so then
						    * start the process
						    */
							if(ID == 5)
							{
								this.isFiring = true;
								tick = 0;
							}
						}
					}
				}			
			}
			
			//Angle that the player is in accordance to the enemy so
			//that enemy moves right towards its target
			rotation = Math.atan
			(((targetX - xPos)) / ((targetZ - zPos)));
			
		   /*
		    * If the target is in the 3rd or 4th quadrant of the map then
		    * add PI to rotation so that the enemy will move into
		    * the correct quadrant of the map and at the target.
		    */
			if(targetZ < zPos)
			{
				rotation += Math.PI;
			}
			
		   /*
		    * Corrects rotation so that the enemy is centered
		    * correctly in the map graph
		    */
			double correction = 44.765;
			
		   /*
		    * Depending on the targets angle in the x z plane from the
		    * enemy, the enemy will move in the x and z directions in
		    * a certain way in order to move towards its target.
		    */
			double newX = 
					((Math.cos(rotation - correction)) 
							+ (Math.sin(rotation - correction))) 
							* speed;
			double newZ = 
					((Math.cos(rotation - correction)) 
							- (Math.sin(rotation - correction))) 
							* speed;
			
			//Which directions the enemy cannot move in due to a blockage
			boolean xStopped = false;
			boolean zStopped = false;
			
			//If enemy is not on the list of enemies on the block then
			//add it to it.
			if(!blockOn.enemiesOnBlock.contains(this))
			{
				blockOn.enemiesOnBlock.add(this);
			}
			
			//If not in search mode, move towards target
			if(!searchMode)
			{		
			   /*
			    * If moving in the direction determined above ends up hitting
			    * a wall, do not move in that direction. Otherwise do so.
			    */
				if(isFree(xPos + newX, super.zPos))
				{
					xPos += newX;
				}
				else
				{
					//If stopped in the x direction it tried to move
					xStopped = true;
				}
			
				//Same as above but in the y
				if(isFree(super.xPos, zPos + newZ))
				{
					zPos += newZ;
				}
				else
				{
					//If stopped in the z direction it tried to move
					zStopped = true;
				}
				
				double newSpeed = speed;
				
				if(Math.abs(newX) <= 0.001)
				{
					xStopped = true;
				}
				
				if(Math.abs(newZ) <= 0.001)
				{
					zStopped = true;
				}
				
			   /*
			    * If stopped in both directions, then as long as the
			    * enemy is not a flying enemy, turn on search mode
			    * so that the enemy tries to find the path to get
			    * to the player. If it is a flying enemy, it is stuck
			    * and needs to go above the wall.
			    */
				if(xStopped && zStopped)
				{
					//If not sentinel enemy
					if(!canFly)
					{
						searchMode = true;
					}
					//If a flying enemy
					else
					{
						isStuck = true;
					}
					
					Random random = new Random();
					int temp = random.nextInt(2);
					
				   /*
				    * Using the random statement above, it will
				    * try to determine which direction it will
				    * try first based upon which direction (either
				    * z or x) is farthest from the player.
				    */
					if(Math.abs(newZ) <= 0.001)
					{
						if(temp == 0)
						{
							direction = 2;
						}
						else
						{
							direction = 3;
						}
					}
					else
					{
						if(temp == 0)
						{
							direction = 0;
						}
						else
						{
							direction = 1;
						}
					}
				}
			   /*
			    * If only the x direction is stopped
			    * then move with the distance unable to be used in the
			    * x direction for moving in the z direction faster.
			    */
				else if(xStopped && !zStopped)
				{
					if(newZ < 0)
					{
						newSpeed = -(newSpeed - newZ);
					}
					else
					{
						newSpeed = (newSpeed - newZ);
					}
					
					if(isFree(super.xPos, zPos + newSpeed))
					{
						zPos += newSpeed;
					}
				}
			   /*
			    * Same but if you are stopped in the z direction then
			    * move with extra speed into the x direction to reach
			    * the player faster.
			    */
				else if(!xStopped && zStopped)
				{
					if(newX < 0)
					{
						newSpeed = -(newSpeed - newX);
					}
					else
					{
						newSpeed = (newSpeed - newX);
					}
					
					if(isFree(xPos + newSpeed, super.zPos))
					{
						xPos += newSpeed;
					}
				}
			}
			//If in searchmode
			else
			{
				double newSpeed = speed;
				
			   /*
			    * For each direction, itll move in that direction until it
			    * hits a wall and/or it opens a door. If it hits a wall
			    * then it'll change direction into either one of the two
			    * opposite directions (such as if going left, it'll now
			    * choose to go forward or back, but not right which is
			    * where it came from). If it opens a door, then it will go
			    * through the doorway to see where it leads.
			    * 
			    * The enemy will only leave searchmode if the player is
			    * in sight of the enemy and the direction to get to the
			    * completely open with no obstacle.
			    * 
			    * 0 is the Lesser x direction
			    * 1 is the Greater x direction
			    * 2 is the Lesser z direction
			    * 3 is the Greater z direction
			    */
				if(direction == 0)
				{
					//Is this direction free? If so...
					if(isFree(xPos - speed, zPos))
					{
						//Move in this direction
						xPos -= speed;
						
						//Rotate enemies body torwards the direction its
						//moving
						rotation = (3 * Math.PI) / 2;
						
						//Get all blocks surrounding enemy
						Block right = Level.getBlock((int)xPos + 1,
								(int)zPos);
						
						Block left = Level.getBlock((int)xPos - 1,
								(int)zPos);
						
						Block front = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
						Block back = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
					   /*
					    * Check to see if each block is a doorway and if
					    * it is, then move through the doorway by changing
					    * the direction as being towards the doorway.
					    */
						if(!doorway)
						{
							if(back.isaDoor && 
									isFree(xPos, super.zPos - newZ))
							{
								direction = 2;
								doorway = true;
							}
							else if(front.isaDoor
									&& isFree(xPos, super.zPos + newZ))
							{
								direction = 3;
								doorway = true;
							}
							else if(left.isaDoor
									&& isFree(xPos - newX, super.zPos))
							{
								direction = 0;
								doorway = true;
							}
							else if(right.isaDoor
									&& isFree(xPos + newX, super.zPos))
							{
								direction = 1;
								doorway = true;
							}
						}
						
						//If player is in sight
						if(inSight)
						{
							//If player can be reached
							if(isFree(xPos + newX, super.zPos)
								&& isFree(super.xPos, zPos + newZ))
							{
								//Move more to get around a corner if 
								//it can
								if(isFree(xPos - speed, zPos))
								{
									xPos -= speed;
								}
								
								//Turn off search mode and move torwards player
								searchMode = false;
								return;
							}
						}
						
					   /*
					    * If the enemy is on the same height level as the
					    * player, or can at least reach the players height
					    * level such as a step or something.
					    * 
					    * Then see if the enemy can move towards the
					    * players z direction or not, and if it can, move
					    * towards it.
					    */
						if(Math.abs(Player.y - yPos) <= 2 && !doorway)
						{
							if(newZ < 0)
							{
								if(isFree(xPos, zPos - newSpeed))
								{								
									zPos -= newSpeed;
								}
							}
							else
							{
								if(isFree(xPos, zPos + newSpeed))
								{								
									zPos += newSpeed;
								}
							}
						}
					}
					//If direction is not free
					else
					{
						//If Just went through a doorway, re-
						//search out the player if possible.
						if(doorway)
						{
							searchMode = false;
							return;
						}
						
						//No longer going through doorway
						doorway = false;
						
						//Reset direction to one of two opposite directions
						Random random = new Random();
						int rand = random.nextInt(2);
						
						if(rand == 0)
						{
							direction = 2;
						}
						else
						{
							direction = 3;
						}
					}
				}
				//Same as above but for different directions
				else if(direction == 1)
				{
					if(isFree(xPos + speed, zPos))
					{
						xPos += speed;
						
						rotation = (Math.PI) / 2;
						
						Block right = Level.getBlock((int)xPos + 1,
								(int)zPos);
						
						Block left = Level.getBlock((int)xPos - 1,
								(int)zPos);
						
						Block front = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
						Block back = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
					   /*
					    * Check to see if each block is a doorway and if
					    * it is, then move through the doorway by changing
					    * the direction as being towards the doorway.
					    */
						if(!doorway)
						{
							if(back.isaDoor && 
									isFree(xPos, super.zPos - newZ))
							{
								direction = 2;
								doorway = true;
							}
							else if(front.isaDoor
									&& isFree(xPos, super.zPos + newZ))
							{
								direction = 3;
								doorway = true;
							}
							else if(left.isaDoor
									&& isFree(xPos - newX, super.zPos))
							{
								direction = 0;
								doorway = true;
							}
							else if(right.isaDoor
									&& isFree(xPos + newX, super.zPos))
							{
								direction = 1;
								doorway = true;
							}
						}
						
						if(inSight)
						{
							if(isFree(xPos + newX, super.zPos)
								&& isFree(super.xPos, zPos + newZ))
							{
								//Move more to get around the corner if 
								//it can
								if(isFree(xPos + speed, zPos))
								{
									xPos += speed;
								}
								
								searchMode = false;
								return;
							}
						}
						
					   /*
					    * If the enemy is on the same height level as the
					    * player, or can at least reach the players height
					    * level such as a step or something.
					    * 
					    * Then see if the enemy can move towards the
					    * players z direction or not, and if it can, move
					    * towards it.
					    */
						if(Math.abs(Player.y - yPos) <= 2 && !doorway)
						{
							if(newZ < 0)
							{
								if(isFree(xPos, zPos - newSpeed))
								{								
									zPos -= newSpeed;
								}
							}
							else
							{
								if(isFree(xPos, zPos + newSpeed))
								{								
									zPos += newSpeed;
								}
							}
						}
					}
					else
					{
						//If Just went through a doorway, re-
						//search out the player if possible.
						if(doorway)
						{
							searchMode = false;
							return;
						}
						
						doorway = false;
						
						Random random = new Random();
						int rand = random.nextInt(2);
						
						if(rand == 0)
						{
							direction = 2;
						}
						else
						{
							direction = 3;
						}
					}
				}	
				else if(direction == 2)
				{
					if(isFree(xPos, zPos - speed))
					{
						zPos -= speed;
						
						rotation = Math.PI;
						
						Block right = Level.getBlock((int)xPos + 1,
								(int)zPos);
						
						Block left = Level.getBlock((int)xPos - 1,
								(int)zPos);
						
						Block front = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
						Block back = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
					   /*
					    * Check to see if each block is a doorway and if
					    * it is, then move through the doorway by changing
					    * the direction as being towards the doorway.
					    */
						if(!doorway)
						{
							if(back.isaDoor && 
									isFree(xPos, super.zPos - newZ))
							{
								direction = 2;
								doorway = true;
							}
							else if(front.isaDoor
									&& isFree(xPos, super.zPos + newZ))
							{
								direction = 3;
								doorway = true;
							}
							else if(left.isaDoor
									&& isFree(xPos - newX, super.zPos))
							{
								direction = 0;
								doorway = true;
							}
							else if(right.isaDoor
									&& isFree(xPos + newX, super.zPos))
							{
								direction = 1;
								doorway = true;
							}
						}
						
						//If player is in sight
						if(inSight)
						{
							//If it can move towards the player
							if(isFree(xPos + newX, super.zPos)
								&& isFree(super.xPos, zPos + newZ))
							{
								//Move more to get around the corner if 
								//it can
								if(isFree(xPos, zPos - speed))
								{
									zPos -= speed;
								}
								
								searchMode = false;
								return;
							}
						}
						
					   /*
					    * If the enemy is on the same height level as the
					    * player, or can at least reach the players height
					    * level such as a step or something.
					    * 
					    * Then see if the enemy can move towards the
					    * players x direction or not, and if it can, move
					    * towards it.
					    */
						if(Math.abs(Player.y - yPos) <= 2 && !doorway)
						{
							if(newX < 0)
							{
								if(isFree(xPos - newSpeed, zPos))
								{								
									xPos -= newSpeed;
								}
							}
							else
							{
								if(isFree(xPos + newSpeed, zPos))
								{								
									xPos += newSpeed;
								}
							}
						}
					}
					else
					{
						//If Just went through a doorway, re-
						//search out the player if possible.
						if(doorway)
						{
							searchMode = false;
							return;
						}
						
						doorway = false;
						
						Random random = new Random();
						int rand = random.nextInt(2);
						
						if(rand == 0)
						{
							direction = 0;
						}
						else
						{
							direction = 1;
						}
					}
				}
				else
				{
					if(isFree(xPos, zPos + speed))
					{
						zPos += speed;
						
						rotation = 0;

						Block right = Level.getBlock((int)xPos + 1,
								(int)zPos);
						
						Block left = Level.getBlock((int)xPos - 1,
								(int)zPos);
						
						Block front = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
						Block back = Level.getBlock((int)xPos,
								(int)zPos + 1);
						
					   /*
					    * Check to see if each block is a doorway and if
					    * it is, then move through the doorway by changing
					    * the direction as being towards the doorway.
					    */
						if(!doorway)
						{
							if(back.isaDoor && 
									isFree(xPos, super.zPos - newZ))
							{
								direction = 2;
								doorway = true;
							}
							else if(front.isaDoor
									&& isFree(xPos, super.zPos + newZ))
							{
								direction = 3;
								doorway = true;
							}
							else if(left.isaDoor
									&& isFree(xPos - newX, super.zPos))
							{
								direction = 0;
								doorway = true;
							}
							else if(right.isaDoor
									&& isFree(xPos + newX, super.zPos))
							{
								direction = 1;
								doorway = true;
							}
						}
						
						//If player is in sight
						if(inSight)
						{
							//If it can move towards the player
							if(isFree(xPos + newX, super.zPos)
								&& isFree(super.xPos, zPos + newZ))
							{
								//Move more to get around the corner if 
								//it can
								if(isFree(xPos, zPos + speed))
								{
									zPos += speed;
								}
								
								searchMode = false;
								return;
							}
						}
						
					   /*
					    * If the enemy is on the same height level as the
					    * player, or can at least reach the players height
					    * level such as a step or something.
					    * 
					    * Then see if the enemy can move towards the
					    * players x direction or not, and if it can, move
					    * towards it.
					    */
						if(Math.abs(Player.y - yPos) <= 2 && !doorway)
						{
							if(newX < 0)
							{
								if(isFree(xPos - newSpeed, zPos))
								{								
									xPos -= newSpeed;
								}
							}
							else
							{
								if(isFree(xPos + newSpeed, zPos))
								{								
									xPos += newSpeed;
								}
							}
						}
					}
					else
					{
						//If Just went through a doorway, re-
						//search out the player if possible.
						if(doorway)
						{
							searchMode = false;
							return;
						}
						
						doorway = false;
						
						Random random = new Random();
						int rand = random.nextInt(2);
						
						if(rand == 0)
						{
							direction = 1;
						}
						else
						{
							direction = 0;
						}
					}
				}  
			}
			
			//Block enemy is now on
			Block blockOnNew = Level.getBlock((int)xPos, (int)zPos);
			
		   /*
		    * If the enemy is on a new block, then remove the enemy
		    * from the last block it was on and add it to the new
		    * block its on.
		    */
			if(!blockOnNew.equals(blockOn))
			{
				blockOn.enemiesOnBlock.remove(this);
				blockOnNew.enemiesOnBlock.add(this);
				
				setHeight();
			}	
			
			//Allows enemies to ride lifts/elevators
			//Basically set enemies position to the position of the current
			//block.
			if(blockOnNew.wallID == 8 && !canFly
					&& Math.abs(blockOnNew.height + yPos) <= 2)
			{
				yPos = -(blockOnNew.height + blockOnNew.y);
			}
		}
	}
	
   /**
    * Initiates an attack on the player when called.
    */
	public void attack(double yCorrect)
	{	
		//If player is not alive, don't try to attack
		//unless targetEnemy is not the Player but is another enemy
		if(!Player.alive && !newTarget
				&& targetEnemy == null)
		{
			return;
		}
		
		//If the target is not the player, the yCorrect variable will
		//be the target enemies y position
		if(targetEnemy != null)
		{
			yCorrect = -targetEnemy.yPos;
		}
		
		if(inSight)
		{
		   /*
		    * Only melee attack once each tick round if
		    * within distance of 1 from the target. The enemy must also
		    * be within height range of the target, and have the target
		    * within its sight.
		    */
			if(super.distance <= 1 && !isAttacking
					&& Math.abs(Math.abs(this.getY()) - 
					(Math.abs(yCorrect))) <= 4 + this.height)
			{
				tick = 0;
				
				//Angle that the player is in accordance to the enemy so
				//that enemy moves right towards its target
				rotation = Math.atan
				(((targetX - xPos)) / ((targetZ - zPos)));
				
			   /*
			    * If the target is in the 3rd or 4th quadrant of the map then
			    * add PI to rotation so that the enemy will move into
			    * the correct quadrant of the map and at the target.
			    */
				if(targetZ < zPos)
				{
					rotation += Math.PI;
				}
				
				//Set melee attacking to true
				super.isAttacking = true;	
			}
		   /*
		    * Fire at target once each 5 tick rounds if target is in sight
		    * and if the enemy is not already firing. Also the enemy has to
		    * have a special attack in order to activate it. Also to fire the
		    * target has to be within its sight, and the target has to be out
		    * of melee attack range. 
		    */
			else if(tick == 0 && tickRound == 0)
			{
				if(distance > 1 && super.activated)
				{
					if(super.hasSpecial
					&& !super.isFiring
					&& Game.skillMode > 0)
					{
						//Enemy is in process of firing
						super.isFiring = true;
						
						//Angle that the player is in accordance to the enemy so
						//that enemy moves right towards its target
						rotation = Math.atan
						(((targetX - xPos)) / ((targetZ - zPos)));
						
					   /*
					    * If the target is in the 3rd or 4th quadrant of the map then
					    * add PI to rotation so that the enemy will move into
					    * the correct quadrant of the map and at the target.
					    */
						if(targetZ < zPos)
						{
							rotation += Math.PI;
						}
					}
				}
			}
		}
	}
	
	@Override
   /**
	* Compares two enemies together depending on their distance from the
	* player, and then sorts them in increasing distance.
	*/
	public int compareTo(Object enemy) 
	{
		//Gets Integer height of the block being compared
		int comparedInteger = (int)((Enemy)(enemy)).distanceFromPlayer;
		
	   /*
	    * Compares the two blocks being compared, and sends back the
	    * result. This particular comparison would cause the list
	    * to be sorted in decending order of height. If you switched
	    * this.height and comparedInteger it would be sorted in
	    * ascending order.
	    */
		return (int)this.distanceFromPlayer - comparedInteger;
	}
	
   /**
    * Whenever the enemy is hurt, this is the method that is called to
    * actually take the enemies health away.
    * @param damage
    */
	public void hurt(int damage, boolean soundPlayed)
	{
		//Decrease enemies health
		super.health -= damage;
		
		//Enemy is stopped and shows that is harmed for 10 ticks
		harmed = 5 * Render3D.fpsCheck;
		
		//Unless the sound has already been played or the enemy is dead
		//now, play the sound that corresponds to the enemy type getting
		//hit
		if(!soundPlayed && super.health > 0)
		{
			if(ID == 1 || ID == 2)
			{
				SoundController.enemyHit.playAudioFile(distanceFromPlayer);	
			}
			else if(ID == 3)
			{
				SoundController.tankHurt.playAudioFile(distanceFromPlayer);	
			}
			else if(ID == 4 || ID == 5)
			{
				SoundController.reaperHurt.playAudioFile(distanceFromPlayer);	
			}
			else if(ID == 7)
			{
				SoundController.vileCivHurt.playAudioFile(distanceFromPlayer);
			}
			else
			{
				SoundController.bossHit.playAudioFile(distanceFromPlayer / 2);	
			}
		}
	}
	
   /**
    * If an enemy dies, the items corresponding to that enemy 
    * (including random drops) are spawned in the enemy death location. 
    * And 2 enemies are spawned when in survival mode. Also
    * enemies made happy is added to and everything else is taken care of.
    * 
    * A corpse is also dropped in the enemies made happy location.
    * 
    * @param enemy
    */
	public void enemyDeath()
	{		
		//Randomizes enemy items random item drop
		Random random = new Random();
		
		isAlive = false;
		
	   /*
	    * If enemy is not supposed to be in the game, but is not off of
	    * this block due to an explosion bug, then remove it from the 
	    * block and exit out of this method.
	    */
		if(!Game.enemies.contains(this))
		{
			Block blockOn = Level.getBlock((int)this.xPos,
					(int)this.zPos);
			
			blockOn.enemiesOnBlock.remove(this);
			return;
		}
		
	   /*
	    * Each enemy drops a particular item/s
	    */
		//Brainomorph
		if(ID == 1)
		{
			new Item(2, xPos, 
				Math.abs(yPos) + 4, 
				zPos, ItemNames.SHELLS.getID(), 0, 0);
		}
		//Sentinel
		else if(ID == 2)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHELLS.getID(), 0, 0);
			
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHARD.getID(), 0, 0);
			
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.VIAL.getID(), 0, 0);
		}
		//Mutated Commando
		else if(ID == 3)
		{
			for(int z = 0; z < 5; z++)
			{
				new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHARD.getID(), 0, 0);
			}
			
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.ROCKETS.getID(), 0, 0);

		}
		//Reaper
		else if(ID == 4)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.VIAL.getID(), 0, 0);
		}
		//Magistrate
		else if(ID == 5)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.HEALTHPACK.getID(), 0, 0);
			
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.HEALTHPACK.getID(), 0, 0);	
		}
		//Morgoth
		else if(ID == 6)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.REDKEY.getID(), 0, 0);
		}
		//Vile Warrior
		else if(ID == 7)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.BULLETS.getID(), 0, 0);
		}
		//Belegoth
		else if(ID == 8)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.GREENKEY.getID(), 0, 0);
		}
		
		//Create random number from 0 to 99
		int temp = random.nextInt(100);
		
		//Random drops. Rare chances that they'll drop with 
		//Everything else.
		if(temp == 10)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.CHAINMEAL.getID(), 0, 0);
		}
		else if(temp == 20)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SMALLCHARGE.getID(), 0, 0);
		}
		else if(temp == 30)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.VIAL.getID(), 0, 0);
		}
		else if(temp == 40)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.ADRENALINE.getID(), 0, 0);
		}
		else if(temp == 50)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHARD.getID(), 0, 0);
		}
		else if(temp == 60)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.PISTOL.getID(), 0, 0);
		}
		else if(temp == 70)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHELLS.getID(), 0, 0);
		}
		else if(temp == 80)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.HEALTHPACK.getID(), 0, 0);
		}
		else if(temp == 90)
		{
			new Item(2, xPos, 
					Math.abs(yPos) + 4, 
					zPos, ItemNames.SHOTGUN.getID(), 0, 0);
		}
		
		//Add corpse to the map
		Game.corpses.add(new Corpse(xPos,
				zPos, -yPos, ID, xEffects, zEffects, yEffects));	
		
		//If survival mode, add two enemies in its place
		if(FPSLauncher.gameMode == 1)
		{
			Display.game.addEnemy();
			Display.game.addEnemy();
		}
		
		//Add to enemies killed
		Display.kills++;
		
		//If not a boss play the normal enemyDeath
		if(!isABoss)
		{
			//Depending on enemy, have a different death sound.
			if(ID == 1)
			{
				SoundController.enemy1Death.playAudioFile(distanceFromPlayer);
			}
			else if(ID == 2)
			{
				SoundController.enemy2Death.playAudioFile(distanceFromPlayer);
			}
			else if(ID == 3)
			{
				SoundController.enemy3Death.playAudioFile(distanceFromPlayer);
			}
			else if(ID == 4 || ID == 5)
			{
				SoundController.enemy4Death.playAudioFile(distanceFromPlayer);
			}
			else if(ID == 7)
			{
				SoundController.enemy7Death.playAudioFile(distanceFromPlayer);
			}
		}
		//Morgoth death
		else if(ID == 6)
		{
			SoundController.bossDeath.playAudioFile(distanceFromPlayer / 2);
		}
		//Belegoth death
		else
		{
			SoundController.belegothDeath.playAudioFile(distanceFromPlayer / 2);
		}
		
		//Remove enemy from game
		Game.enemies.remove(this);
		
		//If a boss remove it from the bosses array as well.
		//And activate certain events if they are there
		if(isABoss)
		{
			Game.bosses.remove(this);
		}
		
		//If this has stuff to activate
		if(this.itemActivationID > 0)
		{
			//Search through all the doors
			for(int k = 0; k < Game.doors.size(); k++)
			{
				Door door = Game.doors.get(k);
				
				//If door has the same activation ID as the 
				//button then activate it.
				if(door.itemActivationID 
						== this.itemActivationID)
				{
					door.activated = true;
				}
			}
			
			//Stores Items to be deleted
			ArrayList<Item> tempItems2 = new ArrayList<Item>();
			
			//Scan all activatable items
			for(int j = 0; j < Game.activatable.size(); j++)
			{
				Item item = Game.activatable.get(j);
				
				if(this.itemActivationID == item.itemActivationID)
				{
					//If Item is a Happiness Tower, activate it and
					//state that it is activated
					if(item.itemID == ItemNames.RADAR.getID()
							&& !item.activated)
					{
						item.activated = true;
						Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
						SoundController.uplink.playAudioFile(0);
					}
					else
					{				
						//If item is enemy spawnpoint, then spawn the
						//enemy, and add the item to the arraylist of
						//items to be deleted
						if(item.itemID == ItemNames.ENEMYSPAWN.getID())
						{
							Game.enemiesInMap++;
							Display.game.addEnemy(item.x, item.z, item.rotation);
							tempItems2.add(item);
						}	
						//If Explosion has same activation ID of the button
						//then activate it
						else if(item.itemID == ItemNames.ACTIVATEEXP.getID())
						{
							new Explosion(item.x, item.y, item.z, 0, 0);
							tempItems2.add(item);
						}
						//If it gets rid of a wall, delete the wall and create an
						//air wall in its place.
						else if(item.itemID == ItemNames.WALLBEGONE.getID())
						{
							Block block2 = Level.getBlock
									((int)item.x, (int)item.z);
							
							//Block is effectively no longer there
							block2.height = 0;
							
							tempItems2.add(item);
						}
					}
				}
			}
			
			//Remove all the items that need to be deleted now
			for(int j = 0; j < tempItems2.size(); j++)
			{
				Item temp2 =  tempItems2.get(j);
						
				temp2.removeItem();
			}
		}
		
		//Block this is on
		Block block = Level.getBlock((int)this.xPos, (int)this.zPos);
		
		//Remove enemy from list of enemies on block too
		block.enemiesOnBlock.remove(this);
	}
	
   /**
    * Sets enemies new height, and corrects the enemies graphics for this
    * new height.
    */
	public void setHeight()
	{
		//Block enemy is now on
		Block blockOnNew = Level.getBlock((int)xPos, (int)zPos);
		
		//Makes the y non negative
		double yCorrect = -this.yPos;
		
	   /*
	    * Only do for non flying enemies. But this is a whole bunch of
	    * correction stuff for the enemies height graphics. Again because
	    * of forces I don't understand, the enemy is actually higher than 
	    * it is supposed to be as you get to higher and higher height
	    * levels, so this sort of fixes that kind of... I'll find better 
	    * and better ways to handle this as time goes on.
	    */
		if(!canFly)
		{
			if(yCorrect >= 0 && yCorrect < 18)
			{
				heightCorrect = 8;
			}
			else if(yCorrect >= 18 && yCorrect < 30)
			{
				heightCorrect = 9;
			}
			else if(yCorrect >= 30 && yCorrect <= 36)
			{
				heightCorrect = 9;
			}
			else if(yCorrect > 36 && yCorrect <= 48)
			{
				heightCorrect = 10;
			}
			else if(yCorrect <= 79)
			{
				heightCorrect = 10;
			}
			else
			{
				double addCorrect;
				yCorrect -= 60;
				
				addCorrect = yCorrect / 20;
				heightCorrect = 10 + (0.5 * addCorrect);
				
			}
		}
		else
		{
			heightCorrect = 10;
		}
		
		//Calculates the new height the enemy will be at when it moves
		double newHeight = -(blockOnNew.height + blockOnNew.y);
		
		//If not flying enemy
		if(!canFly)
		{
			//Set height based on the height of the block the enemy is on
			if(newHeight <= yPos + (2) 
					&& newHeight >= yPos - (2)
					&& !blockOnNew.isaDoor)
			{
				maxHeight = newHeight;
				yPos = maxHeight;
			}
		}
		
	   /*
	    * Set enemies height to the height of the block it is standing
	    * on if this is the first time the enemy is looping through
	    * this method.
	    */
		if(firstTest)
		{
			yPos = newHeight;
			
			//No longer initial height test
			firstTest = false;
			
			//Set the flying enemies initial height
			if(canFly && newHeight > -startY)
			{
				yPos = -startY;
			}
		}
	}
}
