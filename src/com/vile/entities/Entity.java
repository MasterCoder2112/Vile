package com.vile.entities;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.graphics.Render;
import com.vile.graphics.Render3D;
import com.vile.graphics.Textures;
import com.vile.input.InputHandler;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Entity
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 * 
 * Description:
 * Each Entity inherits these methods and values but each different type 
 * of entity has different values for these variables.
 */
public abstract class Entity 
{
	public int health     = 0;
	public int armor      = 0;
	public int ammo       = 0;
	public int damage     = 0;
	public int ID         = 0;
	public int tick       = 0;
	public int tickRound  = 0;
	public int tickAmount = 11;
	public double speed   = 0;
	public double xPos    = 0;
	public double yPos    = 0;
	public double zPos    = 0;
	public double harmed  = 0;
	public double targetX = Player.x;
	public double targetY = Player.y;
	public double targetZ = Player.z;
	public double rotationFromTarget = 0;
	public double rotationFromPlayer = 0;
	public double rotDifference = 0;
	public double xEffects = 0;
	public double yEffects = 0;
	public double zEffects = 0;
	public Render currentPhase = Textures.enemy1;
	public int enemyPhase = 0;
	
	//Rotation in the map in correspondence with the enemy
	public double rotation = 0;
	
	//Distance From player
	public double distanceFromPlayer = -1;
	public double distance = -1;
	
	//Height of entity
	public int height = 8;
	
	//Max height the entity can stand on
	public double maxHeight = 0;
	
	public Enemy targetEnemy = null;
	public Eyesight eyeSight = null;
	
	//Entity Flags (Many will be used in later updates)
	public boolean killable         = false;
	public boolean hasItemDrops     = false;
	public boolean hasMovement      = false;
	public boolean canActivate      = false;
	public boolean canTeleport      = false;
	
	//Can the enemy fly?
	public boolean canFly           = false;
	
	//Is the entity activated, meaning it can move and use its AI or not
	public boolean activated        = false;
	
	//Has a special attack such as firing a projectile or not
	public boolean hasSpecial       = false;
	
	//If entity is firing a projectile or not
	public boolean isFiring = false;
	
	//If entity is melee attacking
	public boolean isAttacking = false;
	
	//If a resurrector, this tells it when it can finally resurrect the
	//corpse at the end of its firing ceremony
	public boolean canResurrect = false;
	
	//If Player is within sight of enemy
	public boolean withinSight = false;
	
	//Whether to check path of sight of entities or not yet
	public static boolean checkSight = false;
	
	//Is the entity stuck in a wall?
	public boolean isStuck = false;
	
	//Is the entity a boss?
	public boolean isABoss = false;
	
	//Is Target in sight?
	public boolean inSight = false;
	
	//Searching for a way out of a room
	public boolean searchMode = false;
	
	//Is the entity still alive?
	public boolean isAlive = true;
	
	//Has this been called yet
	private boolean firstTime = true;
	
   /**
    * Instantiates the type of entity
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
	public Entity(int health, int armor, int ammo, int damage, double speed
			, double x, double y, double z, int ID, double rotation) 
	{
		this.health = health;
		this.armor  = armor;
		this.ammo   = ammo;
		this.damage = damage;
		this.speed  = speed;
		this.ID     = ID;
		this.rotation = rotation;
		xPos = x;
		yPos = y;
		zPos = z;
	}
	
	//Gets the x position of the entity
	public double getX()
	{
		return xPos;
	}
	
	//Gets the y position.
	public double getY()
	{
		return yPos;
	}
	
	//Gets the z position
	public double getZ()
	{
		return zPos;
	}
	
   /**
    * Continues to tick the entity and update its action
    */
	public void tick(Enemy currentEnemy)
	{		
		//If first time tick is called don't update enemy values yet
		//tell the enemy is told not to activate yet by the move
		//method.
		if(firstTime)
		{
			firstTime = false;
			return;
		}
		
		this.tick++;
		
		if(yEffects > 0)
		{
			yEffects -= 2;
		}
		else if(yEffects < 0)
		{
			yEffects += 2;
		}
		
		if(xEffects > 0)
		{
			xEffects -= 0.2;
		}
		else if(xEffects < 0)
		{
			xEffects += 0.2;
		}
		
		if(zEffects > 0)
		{
			zEffects -= 0.2;
		}
		else if(zEffects < 0)
		{
			zEffects += 0.2;
		}
		
		if(Math.abs(yEffects) <= 2)
		{
			yEffects = 0;
		}
		
		if(Math.abs(zEffects) <= 0.2)
		{
			zEffects = 0;
		}
		
		if(Math.abs(xEffects) <= 0.2)
		{
			xEffects = 0;
		}
		
		//If still just recently hit, keep pose the same
		//and keep counting down.
		if(harmed > 0)
		{
			harmed--;
		}
		
		//If target enemy is dead
		if(targetEnemy != null && targetEnemy.health <= 0)
		{
			targetEnemy = null;
			targetX = Player.x;
			targetY = Player.z;
			targetZ = Player.y;
		}
		
	   /*
	    * Determines distance between the enemy and the player
	    * by finding the hypotenuse between the x direction and z
	    * from the player to the enemy being scanned.
	    */
		distanceFromPlayer = Math.sqrt(((Math.abs(this.getX() - Player.x))
				* (Math.abs(this.getX() - Player.x)))
				+ ((Math.abs(this.getZ() - Player.z))
						* (Math.abs(this.getZ() - Player.z))));
		
		//Same but for the entities current target
		distance = Math.sqrt(((Math.abs(this.getX() - targetX))
				* (Math.abs(this.getX() - targetX)))
				+ ((Math.abs(this.getZ() - targetZ))
						* (Math.abs(this.getZ() - targetZ)))); 
		
		rotDifference = rotation - rotationFromPlayer;
		rotDifference = Math.abs(rotDifference);
		
		double h = Math.PI;
		
	   /*
	    * Corrects textures for certain weird cases
	    */
		if(rotation > rotationFromPlayer)
		{
			if(rotDifference > h / 8 && rotDifference <= (3 * h) / 8)
			{
				rotDifference += (3 * Math.PI) / 2;
			}
			else if(rotDifference > (3 * h) / 8 && rotDifference <= (5 * h) / 8)
			{
				rotDifference += Math.PI;
			}
			else if(rotDifference > (5 * h) / 8 && rotDifference <= (7 * h) / 8)
			{
				rotDifference += (Math.PI) / 2;
			}
			else if(rotDifference > (7 * h) / 8 && rotDifference <= (9 * h) / 8)
			{
				rotDifference += 0;
			}
			else if(rotDifference > (9 * h) / 8 && rotDifference <= (11 * h) / 8)
			{
				rotDifference += (Math.PI / 2) * 3;
			}
			else if(rotDifference > (11 * h) / 8 && rotDifference <= (13 * h) / 8)
			{
				rotDifference += (Math.PI);
			}
			else if(rotDifference > (13 * h) / 8 && rotDifference <= (15 * h) / 8)
			{
				rotDifference += (Math.PI) / 2;
			}
		}
		
		double rot = rotDifference;
		
		//Double checks to make sure its not out of bounds
		if(rot > (Math.PI * 2))
		{
			rot = rot - (Math.PI * 2);
		}
		else if(rot < 0)
		{
			rot = (Math.PI * 2) + rot;
		}
		
	   /*
	    * For each entity, depending on its ID, rotation, and phaseTime
	    * the Render (Texture file) that this entity is currently on
	    * so that it no longer is determined during the double for loops
	    * to speed up the game.
	    */
		switch (ID)
		{		
			//Brain demon
			case 1:
				//If image is static (Doesn't change)
				boolean nonMover = false;
				
				if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
				{
					currentPhase = Textures.enemy1right135;
					
					nonMover = true;
				}
				else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
				{
					currentPhase = Textures.enemy1back;
					
					nonMover = true;
				}
				else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
				{
					currentPhase = Textures.enemy1left135;
					
					nonMover = true;
				}
				else
				{
					if(!isFiring && harmed <= 0
							|| isFiring && tick < 8
							&& harmed <= 0)
					{
						if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							currentPhase = Textures.enemy1right;
							
							nonMover = true;
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							currentPhase = Textures.enemy1left;
							
							nonMover = true;
						}
					}
				}
				
				//If enemy was recently hurt
				if(harmed > 0 && !nonMover && !isFiring && !isAttacking)
				{
					if(rot <= h/8 || rot > (15 * h) / 8)
					{
						currentPhase = Textures.enemy1hurt;		
					}
					else if(rot > h / 8 && rot <= (3 * h) / 8)
					{
						currentPhase = Textures.enemy1right45hurt;
					}
					else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
					{
						currentPhase = Textures.enemy1righthurt;
					}
					else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
					{
						currentPhase = Textures.enemy1lefthurt;
					}
					else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
					{
						currentPhase = Textures.enemy1left45hurt;
					}
					
					enemyPhase = 0;
				}
				//If enemy is firing, then show enemy
				//firing phases
				else if(isFiring && !nonMover)
				{		
					if(tick <= 2 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire1;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire1;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire1;
						}
					}
					else if(tick <= 5 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire2;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire2;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire2;
						}
					}
					else if(tick < 8 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire3;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire3;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire3;
						}
					}
					else if(tick >= 8 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire4;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire4;
						}
						else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							currentPhase = Textures.enemy1rightfire;
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							currentPhase = Textures.enemy1leftfire;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire4;
						}
					}
				}
				//If enemy is attacking, then show the
				//phases of that
				else if(isAttacking && !nonMover)
				{
					if(tick <= 3 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire2;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire2;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire2;
						}
					}
					else if(tick <= 6 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire3;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire3;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire3;
						}
					}
					else if(tick <= 9 * Render3D.fpsCheck)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire2;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire2;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire2;
						}
					}
					else
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy1fire1;		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45fire1;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45fire1;
						}
					}
				}
				else
				{	
					if(!nonMover)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							//Sets currentPhase to the currentPhase int of a pixel at a
							//given location in the image to be rendered
							if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy1;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy1b;
							}
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy1right45;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy1left45;
						}
					}	
				}
				
				//If done with phases, start over again
				if(enemyPhase >= 28 * Render3D.fpsCheck)
				{
					enemyPhase = 0;
				}
				
				break;
				
			//Sentinal of the Vile
			case 2:
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					currentPhase = Textures.enemy2hurt;
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is firing, then show enemy
					//firing phases
					if(isFiring)
					{
						if(tick >= 5 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy2fire1;
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = Textures.enemy2right45fire;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy2rightfire;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = Textures.enemy2right135fire;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy2backfire;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = Textures.enemy2left135fire;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy2leftfire;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy2left45fire;
							}
						}
						else
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy2;
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = Textures.enemy2right45;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy2right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = Textures.enemy2right135;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy2back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = Textures.enemy2left135;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy2left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy2left45;
							}
						}
					}
					//If enemy is attacking, then show the
					//phases of that
					else if(isAttacking)
					{							
						if(tick > 9 * Render3D.fpsCheck 
								|| tick > 3 * Render3D.fpsCheck &&
								tick <= 6 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy2;
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = Textures.enemy2right45;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy2right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = Textures.enemy2right135;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy2back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = Textures.enemy2left135;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy2left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy2left45;
							}
						}
						else
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy2fire1;
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = Textures.enemy2right45fire;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy2rightfire;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = Textures.enemy2right135fire;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy2backfire;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = Textures.enemy2left135fire;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy2leftfire;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy2left45fire;
							}
						}
					}
					else
					{							
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy2;
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy2right45;
						}
						else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							currentPhase = Textures.enemy2right;
						}
						else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
						{
							currentPhase = Textures.enemy2right135;
						}
						else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
						{
							currentPhase = Textures.enemy2back;
						}
						else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
						{
							currentPhase = Textures.enemy2left135;
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							currentPhase = Textures.enemy2left;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy2left45;
						}
				
						enemyPhase = 0;
					}
				}
				
				break;
				
			//Mutated commando
			case 3:
				
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					if(rot <= h/8 || rot > (15 * h) / 8)
					{
						currentPhase = Textures.enemy3hurt;		
					}
					else if(rot > h / 8 && rot <= (3 * h) / 8)
					{
						currentPhase = Textures.enemy3hurt45right;
					}
					else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
					{
						currentPhase = Textures.enemy3hurtright;
					}
					if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
					{
						currentPhase = Textures.enemy3hurt135right;
					}
					else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
					{
						currentPhase = Textures.enemy3hurtback;
					}
					else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
					{
						currentPhase = Textures.enemy3hurt135left;
					}
					else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
					{
						currentPhase = Textures.enemy3hurtleft;
					}
					else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
					{
						currentPhase = Textures.enemy3hurt45left;
					}
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is firing, then show the
					//phases of that
					if(isFiring || isAttacking)
					{
						if(tick <= 6 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3fire1;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1right45;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1right135;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1left135;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire1left45;
							}
						}
						else if(tick <= 12 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3fire2;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2right45;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2right135;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2left135;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire2left45;
							}
						}
						else if(tick > 12 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3fire3;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3right45;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3right135;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3left135;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3fire3left45;
							}
						}
						
						enemyPhase = 0;
					}
					else
					{
						//Runs through movement images
						if(enemyPhase <= 14 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3a45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3aright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3a135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3aback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3a135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3aleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3a45left;
							}
						}
						else if(enemyPhase <= 28 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3b;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3b45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3bright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3b135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3bback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3b135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3bleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3b45left;
							}
						}
						else if(enemyPhase <= 42 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3c;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3c45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3cright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3c135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3cback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3c135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3cleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3c45left;
							}
						}
						else if(enemyPhase <= 56 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3d;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3d45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3dright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3d135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3dback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3d135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3dleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3d45left;
							}
						}
						else if(enemyPhase <= 70 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3e;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3e45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3eright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3e135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3eback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3e135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3eleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3e45left;
							}
						}
						else if(enemyPhase <= 84 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.enemy3f;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3f45right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.enemy3fright;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3f135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.enemy3fback;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.enemy3f135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.enemy3fleft;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.enemy3f45left;
							}
						}
						
						if(enemyPhase >= 84 * Render3D.fpsCheck)
						{
							enemyPhase = 0;
						}
					}
				}
				
				break;
				
				//Reaper
			case 4:
		
				if(enemyPhase <= 4 * Render3D.fpsCheck)
				{
					currentPhase = Textures.enemy4;
				}
				else
				{
					currentPhase = Textures.enemy4b;
				}
				
				if(enemyPhase > 9 * Render3D.fpsCheck)
				{
					enemyPhase = 0;
				}
				
				break;	
				
				//Resurrector
			case 5:
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					if(rot <= h/8 || rot > (15 * h) / 8)
					{
						currentPhase = Textures.enemy5hurt;		
					}
					else if(rot > h / 8 && rot <= (3 * h) / 8)
					{
						currentPhase = Textures.enemy5hurt45right;
					}
					else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
					{
						currentPhase = Textures.enemy5hurtright;
					}
					if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
					{
						currentPhase = Textures.enemy5hurt135right;
					}
					else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
					{
						currentPhase = Textures.enemy5hurtback;
					}
					else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
					{
						currentPhase = Textures.enemy5hurt135left;
					}
					else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
					{
						currentPhase = Textures.enemy5hurtleft;
					}
					else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
					{
						currentPhase = Textures.enemy5hurt45left;
					}
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is firing, then show enemy
					//firing phases
					if(isFiring)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fire1;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fire2;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fire1;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fire1;
							}		
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45right;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireb45right;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45right;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45right;
							}
						}
						else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firearight;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firebright;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firearight;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firearight;
							}
						}
						if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135right;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireb135right;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135right;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135right;
							}
						}
						else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireaback;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firebback;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireaback;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireaback;
							}
						}
						else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135left;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireb135left;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135left;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea135left;
							}
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firealeft;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firebleft;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firealeft;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firealeft;
							}
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							if(tick <= 6 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45left;
							}
							else if(tick <= 12 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5fireb45left;
							}
							else if(tick <= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45left;
							}
							else if(tick >= 18 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5firea45left;
							}
						}
					}
					//If enemy is firing, then show enemy
					//firing phases
					else if(isAttacking)
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							currentPhase = Textures.enemy5fire1;	
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							currentPhase = Textures.enemy5firea45right;
						}
						else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							currentPhase = Textures.enemy5firearight;
						}
						if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
						{
							currentPhase = Textures.enemy5firea135right;
						}
						else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
						{
							currentPhase = Textures.enemy5fireaback;
						}
						else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
						{
							currentPhase = Textures.enemy5firea135left;
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							currentPhase = Textures.enemy5firealeft;
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							currentPhase = Textures.enemy5firea45left;
						}
					}
					else
					{
						if(rot <= h/8 || rot > (15 * h) / 8)
						{
							//Sets currentPhase to the currentPhase int of a pixel at a
							//given location in the image to be rendered
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5b;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5c;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5d;
							}	
						}
						else if(rot > h / 8 && rot <= (3 * h) / 8)
						{
							//Sets currentPhase to the currentPhase int of a pixel at a
							//given location in the image to be rendered
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5a45right;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5b45right;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5c45right;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5d45right;
							}
						}
						else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
						{
							//Sets currentPhase to the currentPhase int of a pixel at a
							//given location in the image to be rendered
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5aright;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5bright;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5cright;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5dright;
							}
						}
						if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
						{
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5a135right;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5b135right;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5c135right;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5d135right;
							}
						}
						else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
						{
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5aback;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5bback;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5cback;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5dback;
							}
						}
						else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
						{
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5a135left;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5b135left;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5c135left;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5d135left;
							}
						}
						else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
						{
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5aleft;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5bleft;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5cleft;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5dleft;
							}
						}
						else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
						{
							if(enemyPhase <= 7 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5a45left;
							}
							else if(enemyPhase <= 14 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5b45left;
							}
							else if(enemyPhase <= 21 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5c45left;
							}
							else if(enemyPhase <= 28 * Render3D.fpsCheck)
							{
								currentPhase = Textures.enemy5d45left;
							}
						}
						
						//If done with phases, start over again
						if(enemyPhase >= 28 * Render3D.fpsCheck)
						{
							enemyPhase = 0;
						}
					}
				}
				
				break;	
				
				//Morgoth
			case 6:
		
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					currentPhase = Textures.morgothHurt;
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is firing, then show enemy
					//firing phases
					if(isFiring)
					{
						if(tick <= 6 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgothFire1;
						}
						else if(tick >= 6 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgothFire2;
						}
					}
					//If enemy is attacking, then show the
					//phases of that
					else if(isAttacking)
					{
						if(tick <= 3 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgothMelee;
						}
						else if(tick <= 6 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth;
						}
						else if(tick <= 9 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgothMelee;
						}
						else if(tick >= 9 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth;
						}
					}
					else
					{
						//Runs through movement phases of the 
						//enemies textures.
						if(enemyPhase <= 7 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth;
						}
						else if(enemyPhase <= 14 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth2;
						}
						else if(enemyPhase <= 21 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth3;
						}
						else if(enemyPhase <= 28 * Render3D.fpsCheck)
						{
							currentPhase = Textures.morgoth4;
						}
						
						if(enemyPhase >= 28 * Render3D.fpsCheck)
						{
							enemyPhase = 0;
						}
					}
				}
				
				break;
				
			//Vile Civilian	
			case 7:				
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					if(rot <= h/8 || rot > (15 * h) / 8)
					{
						currentPhase = Textures.vileCivHurt;		
					}
					else if(rot > h / 8 && rot <= (3 * h) / 8)
					{
						currentPhase = Textures.vileCivilianright45hurt;
					}
					else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
					{
						currentPhase = Textures.vileCivilianrighthurt;
					}
					if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
					{
						currentPhase = Textures.vileCivilianright135hurt;
					}
					else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
					{
						currentPhase = Textures.vileCivilianbackhurt;
					}
					else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
					{
						currentPhase = Textures.vileCivilianleft135hurt;
					}
					else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
					{
						currentPhase = Textures.vileCivilianlefthurt;
					}
					else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
					{
						currentPhase = Textures.vileCivilianleft45hurt;
					}
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is attacking, then show the
					//phases of that
					if(isAttacking)
					{
						if(tick <= 6 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCivAttack1;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack145right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack1right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack1135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack1back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack1135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack1left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack145left;
							}
						}
						else if(tick >= 6 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCivAttack2;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack245right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack2right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack2135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack2back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack2135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack2left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilianAttack245left;
							}
						}
						
						enemyPhase = 0;
					}
					else
					{
						//Runs through movement phases of the 
						//enemies textures.
						if(enemyPhase <= 7 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCiv1;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian145right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.vileCivilian1right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian1135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.vileCivilian1back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian1135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.vileCivilian1left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.vileCivilian145left;
							}
						}
						else if(enemyPhase <= 14 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCiv2;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian245right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.vileCivilian2right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian2135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.vileCivilian2back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian2135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.vileCivilian2left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.vileCivilian245left;
							}
						}
						else if(enemyPhase <= 21 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCiv3;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian345right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.vileCivilian3right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian3135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.vileCivilian3back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian3135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.vileCivilian3left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.vileCivilian345left;
							}
						}
						else if(enemyPhase <= 28 * Render3D.fpsCheck)
						{
							if(rot <= h/8 || rot > (15 * h) / 8)
							{
								currentPhase = Textures.vileCiv4;		
							}
							else if(rot > h / 8 && rot <= (3 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian445right;
							}
							else if(rot > (3 * h) / 8 && rot <= (5 * h) / 8)
							{
								currentPhase = Textures.vileCivilian4right;
							}
							else if(rot > (5 * h) / 8 && rot <= (7 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian4135right;
							}
							else if(rot > (7 * h) / 8 && rot <= (9 * h) / 8)
							{
								currentPhase = Textures.vileCivilian4back;
							}
							else if(rot > (9 * h) / 8 && rot <= (11 * h) / 8)
							{
								currentPhase = 
										Textures.vileCivilian4135left;
							}
							else if(rot > (11 * h) / 8 && rot <= (13 * h) / 8)
							{
								currentPhase = Textures.vileCivilian4left;
							}
							else if(rot > (13 * h) / 8 && rot <= (15 * h) / 8)
							{
								currentPhase = Textures.vileCivilian445left;
							}
						}
						
						if(enemyPhase >= 28 * Render3D.fpsCheck)
						{
							enemyPhase = 0;
						}
					}
				}
				
				break;
				
			//Belegoth
			case 8:
		
				//If enemy was recently hurt
				if(harmed > 0 && !isFiring && !isAttacking)
				{
					currentPhase = Textures.belegothHurt;
					
					enemyPhase = 0;
				}
				else
				{
					//If enemy is firing, then show enemy
					//firing phases
					if(isFiring)
					{
						if(tick <= 2 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothAttack1;
						}
						else if(tick <= 5 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothAttack2;
						}
						else if(tick <= 8 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothAttack3;
						}
						else if(tick >= 8 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothAttack4;
						}
					}
					//If enemy is attacking, then show the
					//phases of that
					else if(isAttacking)
					{
						if(tick <= 3 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothMelee;
						}
						else if(tick <= 6 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth4;
						}
						else if(tick <= 9 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegothMelee;
						}
						else if(tick >= 9 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth4;
						}
					}
					else
					{
						//Runs through movement phases of the 
						//enemies textures.
						if(enemyPhase <= 7 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth;
						}
						else if(enemyPhase <= 14 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth2;
						}
						else if(enemyPhase <= 21 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth3;
						}
						else if(enemyPhase <= 28 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth4;
						}
						else if(enemyPhase <= 35 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth5;
						}
						else if(enemyPhase <= 42 * Render3D.fpsCheck)
						{
							currentPhase = Textures.belegoth6;
						}
						
						if(enemyPhase >= 42 * Render3D.fpsCheck)
						{
							enemyPhase = 0;
						}
					}
				}
				
				break;
		}
		
		//If its been a few ticks and its time to check the line of sight
		//again.
		if(checkSight && Player.invisibility == 0)
		{
		   /*
		    * Figures out if Player is within the enemies view range being
		    * from 60 degrees to the right and left of the enemies centered
		    * view.
		    */
			double plus60 = rotation + ((Math.PI / 3));
			double minus60 = rotation - ((Math.PI / 3));
			double tempTarget = rotationFromTarget;
			withinSight = false;
			
		   /*
		    * In the case that adding 60 degrees goes beyond
		    * 360 degrees, correct it for checking purposes
		    */
			if(plus60 > (2 * Math.PI) - (1/180)
					&& tempTarget <= (Math.PI / 3))
			{
				plus60 = plus60 - (2 * Math.PI);
			}
			
		   /*
		    * In the case that subtracting 60 makes the degrees for
		    * checking purposes less than 0, then correct the players
		    * rotation from target for checking purposes only. Put it
		    * in range of the enemies range of sight.
		    */
			if(tempTarget < (2 * Math.PI) &&
					tempTarget > (3 * Math.PI) / 2
					&& minus60 < 0)
			{
				tempTarget = tempTarget - (2 * Math.PI);
			}
			
			//If Player could be in sight. But its within the cone of
			//sight.
			if(tempTarget >= minus60
					&& tempTarget <= plus60)
			{
				withinSight = true;
			}

			//Angle that the target is in accordance to the entity so
			//that entity looks right towards its target
			//sin/cos in this case
			double sightRotation = Math.atan
			(((targetX - xPos)) / ((targetZ - zPos)));
			
		   /*
		    * If the target is in the 3rd or 4th quadrant of the map then
		    * add PI to rotation so that the enemy will move into
		    * the correct quadrant of the map and at the target.
		    */
			if(targetZ < zPos)
			{
				sightRotation += Math.PI;
			}
			
		   /*
		    * Corrects rotation so that the enemy is centered
		    * correctly in the map graph
		    */
			double correction = 44.765;
			
		   /*
		    * Eyesight trajectory of the entity so that it will be
		    * checking a straight line of sight to the player.
		    */
			double sightX = 
					((Math.cos(sightRotation - correction)) 
							+ (Math.sin(sightRotation - correction))) * 0.01;
			double sightZ = 
					((Math.cos(sightRotation - correction)) 
							- (Math.sin(sightRotation - correction))) * 0.01;
			
			//How much the eyesight moves total each check.
			//The hypotenuse of the x and z movement
			double moveDistance = Math.sqrt((sightX * sightX) 
						+ (sightZ * sightZ));
			
			//Total hypotenuse between target and entity
			double hypot = Math.sqrt(((xPos - targetX) * (xPos - targetX))
							+ ((zPos - targetZ) * (zPos - targetZ)));
			
			//Difference between entity and target y values
			double yDifference = Math.abs(yPos - 8) - Math.abs(targetY);
			
			//Number of moves the eyesight will check for if it reaches
			//the target successfully
			double iterations = hypot / moveDistance;
			
			//How much y will have to change each time
			double sightY = yDifference / iterations;
			
			//Resets the eyesight object with its new values
			eyeSight = new Eyesight(this.xPos, this.zPos, this.yPos - 8,
						targetX, targetZ, targetY,
						sightX, sightZ, sightY);
			
			//Checks to see if Player is in its line of sight
			inSight = eyeSight.checkEyesight();
		}
		
		//Only if the enemy has you directly in its line of sight
		//or if the enemy is not behind a wall and you fire.
		if(inSight && (withinSight || InputHandler.wasFired))
		{
		   /*
		    * If the enemy is already not activated and also
		    * the gamemode is not peaceful, and the player is
		    * alive, and theres more than 5 pixels seen by the
		    * player at least (so that if seen through a texture
		    * glitch it won't accidently activate the enemy through
		    * walls. After those conditions have been met, the player
		    * has to either be within the enemies sight, or the
		    * player has to fire a gun to activate the enemy.
		    */
			if(!activated && Display.skillMode > 0
					&& Player.alive)
			{	
				//Enemy can activate
				boolean canActivate = true;
				
			   /*
			    * Because the flying enemies can see the player
			    * from much farther away than any other normal
			    * enemy standing on the ground, this limits how
			    * far away the flying enemy can activate so
			    * that all the flying enemies in the map do not
			    * activate at once causing issues.
			    */
				if(canFly && distance > 10)
				{
					canActivate = false;
				}
				
			   /*
			    * If enemy can be activated, activate the enemy
			    * and player the enemy activation sound that
			    * correlates to the enemy being activated.
			    */
				if(canActivate)
				{
					activated = true;
					
					if(ID == 6)
					{
						SoundController.bossActivate.playAudioFile();
					}
					else if(ID == 8)
					{
						SoundController.belegothActivate.playAudioFile();
					}
					else if(ID == 1)
					{
						SoundController.enemyActivate.playAudioFile();
					}
					else if(ID == 2)
					{
						SoundController.enemy2Activate.playAudioFile();
					}
					else if(ID == 3)
					{
						SoundController.enemy3Activate.playAudioFile();
					}
					else if(ID == 4)
					{
						SoundController.enemy4Activate.playAudioFile();
					}
					else if(ID == 5)
					{
						SoundController.enemy5Activate.playAudioFile();
					}
					else if(ID == 7)
					{
						SoundController.enemy7Activate.playAudioFile();
					}
				}
			}
		}
		
		
		//Every so many ticks, reset time. Depends on the games fps
		if(this.tick >= tickAmount * Render3D.fpsCheck)
		{
			this.tick = 0;
			tickRound++;
			
			//Every 5 sets of 10 ticks, reset the tick round count
			if(tickRound == 5)
			{
				tickRound = 0;
			}
			
			//If melee attacking, end the attack at the end of each tick
			//round and hurt the target accordingly if still in range
			if(isAttacking)
			{
				//If target is Player
				if(distanceFromPlayer <= 1 && targetEnemy == null)
				{
					Player.hurtPlayer(damage);
				}
				//If target is not player, then the enemy attacks the
				//other enemy, and causes that enemy to want to attack
				//it now.
				else if(distance <= 1 && targetEnemy != null)
				{
					targetEnemy.hurt(damage, false);
					targetEnemy.targetEnemy = currentEnemy;
					targetEnemy.activated = true;
					
					//If enemy is dead
					if(targetEnemy.health <= 0)
					{
						targetEnemy.enemyDeath();
					}
				}
				
				SoundController.enemyFire.playAudioFile();
				
				isAttacking = false;
			}
			
		   /*
			* Activates enemies special power only at the end of the
			* tick cycle. Also the enemy has to be in the process of
			* firing for this to occur.
			*/	
			if(isFiring)
			{
				isFiring = false;
				
				SoundController.enemyFire.playAudioFile();
				
				//Default projectile
				int tempID = 4;
				
				//If sentinel, have different type of projectile
				if(ID == 2)
				{
					tempID = 5;
				}
				
				//If Mutated Commando shoot in 3 directions
				if(ID == 3)
				{
					//Create new Projectile object with given parameters
					EnemyFire temp = new EnemyFire(10, 0.1, xPos, yPos,
							zPos, 6, targetX, targetZ, 
							targetY, 0, currentEnemy);
					
					EnemyFire temp2 = new EnemyFire(10, 0.1, xPos, yPos,
							zPos, 6, targetX, targetZ, 
							targetY, Math.PI / 8, currentEnemy);
					
					EnemyFire temp3 = new EnemyFire(10, 0.1, xPos, yPos,
							zPos, 6, targetX, targetZ, 
							targetY, -Math.PI / 8, currentEnemy);
					
					//Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
				}
				else if(ID == 5 && !canResurrect)
				{
					canResurrect = true;
				}
				//Bosses shoot in 5 directions
				else if(ID == 6 || ID == 8)
				{
					EnemyFire temp = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, 0, currentEnemy);
					
					EnemyFire temp2 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, Math.PI / 16, currentEnemy);
					
					EnemyFire temp3 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, -Math.PI / 16, currentEnemy);
					
					EnemyFire temp4 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, Math.PI / 8, currentEnemy);
					
					EnemyFire temp5 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, -Math.PI / 8, currentEnemy);
					
					EnemyFire temp6 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, Math.PI / 4, currentEnemy);
					
					EnemyFire temp7 = new EnemyFire(10, 0.2, xPos, yPos,
							zPos, 7, targetX, targetZ, 
							targetY, -Math.PI / 4, currentEnemy);
					
					//Add Projectiles to the Game events
					Game.enemyProjectiles.add(temp);
					Game.enemyProjectiles.add(temp2);
					Game.enemyProjectiles.add(temp3);
					Game.enemyProjectiles.add(temp4);
					Game.enemyProjectiles.add(temp5);
					Game.enemyProjectiles.add(temp6);
					Game.enemyProjectiles.add(temp7);
				}
				else
				{
					int tempDamage = 5;
					
					if(ID == 2)
					{
						tempDamage = 8;
					}
					//Create new Projectile object with given parameters
					EnemyFire temp = new EnemyFire(tempDamage, 0.1, xPos, yPos,
							zPos, tempID, targetX, targetZ, 
							targetY, 0, currentEnemy);
					
					//Add Projectile to the Game events
					Game.enemyProjectiles.add(temp);
				}
			}
		}
	}
	
   /**
    * Determines whether the entity is free to move to the next space or
    * not.
    * @param xx
    * @param zz
    * @return
    */
	public boolean isFree(double nextX, double nextZ)
	{	
		double z = 0.3;
		
		//Bosses have wider hit boxes so they have to be farther from
		//walls
		if(isABoss)
		{
			z = 1;
		}
		
		//Dont let entity exit the map
		if(nextX < 0 || nextX > Level.width || nextZ < 0
				|| nextZ > Level.height)
		{
			return false;
		}
		
	   /*
	    * Determine the block the entity is about to move into given the
	    * direction that it is going. Then set this block as the block
	    * to check the collision of. Technically it actually checks two
	    * blocks though. The two blocks that are in the direction that
	    * the entity is going. So in case the enemy is moving to a position
	    * in between two blocks, and not directly at the block, it will
	    * make sure the entity cannot move through 
	    */
		Block block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
		Block block2 = Level.getBlock((int)(nextX - z), (int)(nextZ + z));
			
		if(nextX < xPos && nextZ == zPos)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX - z), (int)(nextZ + z));
		}
		else if(nextX >= xPos && nextZ == zPos)
		{
			block = Level.getBlock((int)(nextX + z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX + z), (int)(nextZ + z));
		}
		else if(nextX == xPos && nextZ >= zPos)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ + z));
			block2 = Level.getBlock((int)(nextX + z),(int)(nextZ + z));
		}
		else //(xx == xPos && zz < zPos)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX + z),(int)(nextZ - z));
		}
		
		try
		{
			//If not reaper enemy
			if(ID != 4)
			{
				//Go through all the enemies on the block
				for(int i = 0; i < block.enemiesOnBlock.size(); i++)
				{
					Enemy temp = block.enemiesOnBlock.get(i);
					
					//Distance between enemy and other enemy
					double distance = Math.sqrt(((Math.abs(temp.xPos - nextX))
							* (Math.abs(temp.xPos - nextX)))
							+ ((Math.abs(temp.zPos - nextZ))
									* (Math.abs(temp.zPos - nextZ))));
					
					//If close enough, don't allow the enemy to move into
					//the other enemies. Enemy can still move if 8 units above
					//The other enemy
					if(distance <= 0.5 && !this.equals(temp)
							&& Math.abs(this.yPos - temp.yPos) <= 8)
					{
						return false;
					}	
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
		//Distance between enemy and player
		double distance = Math.sqrt(((Math.abs(Player.x - nextX))
				* (Math.abs(Player.x - nextX)))
				+ ((Math.abs(Player.z - nextZ))
						* (Math.abs(Player.z - nextZ))));
		
		//Players y value
		double playerY = Player.y;
		
		//Correct it for if the player is crouching and has a y less than
		//0
		if(playerY < 0)
		{
			playerY = 0;
		}
		
		//Difference between the the two entities y values
		double yDifference = playerY - Math.abs(yPos);
		
		//Can't clip inside player
		if(distance <= 0.3 && yDifference <= height
				&& yDifference >= -8 && !isABoss)
		{
			return false;
		}
		//Greater range of no clipping if a boss because it is bigger
		else if(distance <= 1 && isABoss)
		{
			return false;
		}
		
	   /*
	    * For the current block, check to see if the enemy
	    * can move through or onto the block. If a solid block, check 
	    * whether it can move onto it using the collisionChecks method.
	    * If not solid, then check to see if the air block has a solid
	    * item (torch, lamp, etc...) on it (as long as it is not a tree
	    * I made those able to be moved through for the reason that
	    * forests occur in my maps sometimes and they wouldn't be able
	    * to move in those circumstances) and if there is treat it as
	    * a normal solid block so the entity doesn't get stuck in the
	    * the item. Unless the enemy is above the item of course.
	    */	    
	    if(block.isSolid || block2.isSolid)
	    {
	    	return collisionChecks(block) && collisionChecks(block2);
	    }
	    else
	    {
	    	try
	    	{
	    		Item temp = block.wallItem;
	    		
	    		//If there is a solid item on the block, and its not a
	    		//tree, and its within the y value of the entity, and
	    		//the entity is not a reaper, you can't move into that
	    		//block
	    		if(Game.solidItems.contains(temp)
	    				&& temp.itemID != 31 
	    				&& Math.abs(temp.y + yPos) <= temp.height
	    				&& ID != 4)
	    		{
	    			return false;
	    		}
	    	}
	    	catch(Exception E)
	    	{
	    		
	    	}
	    	
	    	//Cannot drop off of a block thats higher than 2 units up
	    	if(-yPos > (2) && !canFly)
	    	{
	    		return false;
	    	}
	    	//If a flying entity or can drop off the block, then the
	    	//enemy is not stuck.
	    	else
	    	{
	    		isStuck = false;
	    	}
	    }
	    
	    isStuck = false;
	    
	    return true;			
	}
	
   /**
    * Frees up code space and makes it easier to make changes to all the
    * collision checks at once just changing just one method.
    * 
    * Optimizes code.
    * @param block
    * @return
    */
	public boolean collisionChecks(Block block)
	{	
	   /*
	    * The entity can't move forward anyway if the block its moving
	    * to has a solid object on it
	    */
		try
    	{
    		Item temp = block.wallItem;
    		
    		//If there is a solid item on the block, and its not a
    		//tree, and its within the y value of the enemy, and
    		//the enemy is not a reaper, you can't move into that
    		//block
    		if(Game.solidItems.contains(temp)
    				&& temp.itemID != 31 
    				&& Math.abs(temp.y + yPos) <= temp.height
    				&& ID != 4)
    		{
    			isStuck = true;
    			return false;
    		}
    	}
    	catch(Exception E)
    	{
    		
    	}
		
	   /*
	    * If the block in front of the entity is greater than two units
	    * higher than the entity, or if it is more than two lower than
	    * the entity, or the entity is still not far enough under a block
	    * to go through it (mainly used with doors) then don't allow
	    * the entity to move.
	    * 
	    * If the entity is the flyer demon entity, then only stop its
	    * passage if the block is 48 units or higher tall. Or if a
	    * fying entity is tracking the player, it acts like a normal entity.
	    */
		if(((block.height + block.y - 2) > 
			-yPos && -yPos + 2 > block.y && !block.isaDoor)
				|| ((block.height + block.y + 2) < -yPos
						&& !canFly && !block.isaDoor))
		{
			isStuck = true;
			return false;
		}
		
		if(!block.isaDoor && !canFly)
		{			
			maxHeight = -(block.height + block.y);
		}
		
		//Default is that the entity can move
		isStuck = false;
		
		return true;
	}

}
