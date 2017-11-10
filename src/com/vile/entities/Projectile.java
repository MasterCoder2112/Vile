package com.vile.entities;

import java.util.ArrayList;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.graphics.Render3D;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * @Title  Projectile
 * @author Alex Byrd
 * Date Updated: 5/2/2017
 * 
 * Description:
 * Has all the methods a projectile might need except for a movement
 * method as both bullets and enemy projectiles have different tracking
 * methods. 
 */
public abstract class Projectile
{
	//Has projectile hit something?
	public boolean hit = false;
	
	//If the bullet hits something its supposed to disappear from
	private boolean disappear = false;
	
	//Has this projectile hit an enemy?
	public static boolean enemyHit = false;
	
	//Has this projectile hit a boss?
	public static boolean bossHit = false;
	
	//Its damage
	public int damage = 0;
	
	//Its speed
	public double speed = 0;
	
	//The enemy that fired the projectile.
	//Remains null if it was the player who shot it
	public Enemy sourceEnemy = null;
	
	//Its position
	public double x = 0;
	public double y = 0;
	public double z = 0;
	
	//Distance from Player
	public double distanceFromPlayer = 0;
	
	//The type of projectile
	public int ID = 0;
	
	//Its initial set speed
	public double initialSpeed = 0;
	
	//Change in x and z values.
	public double xa = 0;
	public double za = 0;
	
	//Pixels that the projectile occupies on screen when rendered
	public ArrayList<Integer> pixelsOnScreen 
			= new ArrayList<Integer>();
	
   /**
    * Sets up typical new projectile
    * @param damage
    * @param speed
    * @param x
    * @param y
    * @param z
    * @param ID
    */
	public Projectile(int damage, double speed, double x, double y,
			double z, int ID) 
	{
		this.damage = damage;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ID = ID;
		initialSpeed = speed;
	}
	
   /**
    * Determines whether the projectile is free to move to the next space
    * or not. In the future this will be optimized to track whether an
    * enemy is hit here or not, but for now it just tracks whether it hits
    * a wall, floor, ceiling, edge of map, or solid item. Sends back
    * whether it can keep moving or not.
    * 
    * @param xx
    * @param zz
    * @return
    */
	public boolean isFree(double nextX, double nextZ)
	{			
		//Distance from object the projectile can hit
		double z = 0;
		
		//The projectiles distance from the player. Updated every tick
		distanceFromPlayer = Math.sqrt(((Math.abs(x - Player.x))
				* (Math.abs(x - Player.x)))
				+ ((Math.abs(this.z - Player.z))
						* (Math.abs(this.z - Player.z))));
		
		//If projectile hits ceiling or floor. Stop it
		if((y > 1 || -y * 10 >= Render3D.ceilingDefaultHeight) && ID != 1)
		{		
			//Fixes bug where a rocket goes way below the floor
			//before it hits.
			if(y > 1)
			{
				y = 1;
			}
			
			//I don't even know how this happens. When staring straight
			//down the rocket hits a y of -37 or something. This corrects
			//that for rocket jumping.
			if(y < -36)
			{		
				y = 1;
			}
			
			//Add HitSprite to the game
			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);
			
			Game.sprites.add(hS);
			
			projectileHit(true);
			
			return false;
		}
		
		//Don't let projectile leave map
		if(x < 0 || this.z < 0 || x > Level.width 
				|| this.z > Level.height)
		{
			//Add HitSprite to the game
			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);
			
			Game.sprites.add(hS);
			
			projectileHit(true);
			return false;
		}
		
	   /*
	    * Determine the block the enemy is about to move into given the
	    * direction that it is going. Then set this block as the block
	    * to check the collision of. Technically it actually checks two
	    * blocks though. The two blocks that are in the direction that
	    * the enemy is going. So in case the enemy is moving to a position
	    * in between two blocks, and not directly at the block, it will
	    * make sure the enemy cannot move through 
	    */
		Block block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
		Block block2 = Level.getBlock((int)(nextX - z), (int)(nextZ + z));
			
		if(nextX < x && nextZ == z)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX - z), (int)(nextZ + z));
		}
		else if(nextX >= x && nextZ == z)
		{
			block = Level.getBlock((int)(nextX + z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX + z), (int)(nextZ + z));
		}
		else if(nextX == x && nextZ >= z)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ + z));
			block2 = Level.getBlock((int)(nextX + z),(int)(nextZ + z));
		}
		else //(xx == x && zz < z)
		{
			block = Level.getBlock((int)(nextX - z),(int)(nextZ - z));
			block2 = Level.getBlock((int)(nextX + z),(int)(nextZ - z));
		}

		try
		{		
			Item temp = block.wallItem;
			
			//Distance between item and player
			double distance = Math.sqrt(((Math.abs(temp.x - nextX))
					* (Math.abs(temp.x - nextX)))
					+ ((Math.abs(temp.z - nextZ))
							* (Math.abs(temp.z - nextZ))));
			
			//Difference in y
			//double yDifference = Math.abs((-y * 13) - Math.abs(temp.y));
			
			//If explosive canister
			if(temp.itemID == ItemNames.CANISTER.getID())
			{
				//If close enough, don't allow bullet to pass
				if(distance <= 0.3)
				{
					if(this.pixelsOnScreen.size() > 0
							&& temp.pixelsOnScreen.size() > 0
							&& (ID == 0 || ID == 2))
					{
						boolean hitObject = false;

						for(int j = 0; j < this.pixelsOnScreen.size(); j++)
						{
							int temp2 = this.pixelsOnScreen.get(j);
							
							if(temp.pixelsOnScreen.contains(temp2))
							{
								hitObject = true;
								break;
							}

						}
						
						//If the enemy is indeed hit
						if(hitObject)
						{
							//Cast the item type as being an ExplosiveCanister
							ExplosiveCanister can = (ExplosiveCanister)temp;
							
							//Damage canister the amount the projectile has
							can.health -= damage;
							
							//If canister is damaged enough, blow it up
							if(can.health <= 0)
							{
								Game.explosions.add(
										new Explosion(can.x,
												-can.y/10, can.z, 1, 0));
								block.wallItem = null;
								can.removeCanister();
								
								//Add HitSprite to the game
								HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
								
								Game.sprites.add(hS);
								
								disappear = true;
								
								projectileHit(true);
								return false;
							}
						}
					}
					else if(((Math.abs((this.y) - ((temp.y))) <= 0.6)))
					{

						//Cast the item type as being an ExplosiveCanister
						ExplosiveCanister can = (ExplosiveCanister)temp;
						
						//Damage canister the amount the projectile has
						can.health -= damage;
						
						//If canister is damaged enough, blow it up
						if(can.health <= 0)
						{
							Game.explosions.add(
									new Explosion(can.x,
											-can.y/10, can.z, 1, 0));
							block.wallItem = null;
							can.removeCanister();
							
							//Add HitSprite to the game
							HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
							
							Game.sprites.add(hS);
							
							disappear = true;
							
							projectileHit(true);
							return false;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			
		}	
		
		//Cycle through all bosses because they have a bigger hit
		//range you have to check for.
		for(int i = 0; i < Game.bosses.size(); i++)
		{
			Enemy boss = Game.bosses.get(i);
			
			//Distance between projectile and other enemy
			double distance = Math.sqrt(((Math.abs(boss.xPos - nextX))
					* (Math.abs(boss.xPos - nextX)))
					+ ((Math.abs(boss.zPos - nextZ))
							* (Math.abs(boss.zPos - nextZ))));
			
			//If this projectile was fired by the same enemy, skip 
			//everything below
			if(this.sourceEnemy != null &&
					this.sourceEnemy.equals(boss))
			{
				//Nothing right now
			}
			else
			{	
				//If close enough, hit boss.
				if(distance <= 2)
				{
					//If enemy can be seen on the screen
					if(boss.pixelsOnScreen.size() > 0
							&& this.pixelsOnScreen.size() > 0
							&& (ID == 0 || ID == 2))
					{
						boolean hitObject = false;

						for(int j = 0; j < this.pixelsOnScreen.size(); j++)
						{
							int temp = this.pixelsOnScreen.get(j);
							
							if(boss.pixelsOnScreen.contains(temp))
							{
								hitObject = true;
								break;
							}

						}
						
						//If the enemy is indeed hit
						if(hitObject)
						{
							boss.searchMode = false;
						   /*
						    * If enemy that shot projectile is still alive
						    * Then reset target to that unless that target
						    * is the same type of enemy as the enemy that
						    * fired it because they are immune to their
						    * own attack.
						    */
							if(this.sourceEnemy != null
									&& this.sourceEnemy.ID !=
									boss.ID)
							{
								boss.targetEnemy 
									= this.sourceEnemy;
								
								//Hurt enemy, and activate
								//the enemy if not already. Can't be a rocket
								//to hurt. The explosion is what hurts the
								//enemy.
								if(ID != 3)
								{
									boss.hurt(this.damage, enemyHit);
								}
								
								boss.activated = true;
								boss.searchMode = false;
							}
							else
							{
								//Hurt enemy if not a rocket. The explosion is
								//what harms the enemy from the rocket.
								if(ID != 3)
								{
									boss.hurt(this.damage, enemyHit);
								}	
								
								//Activate the enemy if not already.
								boss.activated = true;
								
								//Enemies target is now you
								boss.targetEnemy = null;
								
								//Not searching for you anymore, it will just come
								//right towards your position
								boss.searchMode = false;
							}
							
							/*
							 * If an enemy is made happy, then remove that enemy from
							 * the game, and call the death function to drop any items
							 * or add to the made happy count of the game as needed.
							 */
							if (boss.health <= 0) 
							{
								boss.enemyDeath();
							}
							
							//If shotgun spread bullet
							if(ID == 1)
							{
								enemyHit = true;
							}
							
							//Add HitSprite to the game
							HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
							
							Game.sprites.add(hS);
							
							//Bullet texture does not stay in mid-air
							disappear = true;
							
							projectileHit(false);
							
							return false;	
						}
					}
					else
					{
						boss.searchMode = false;
						
					   /*
					    * If enemy that shot projectile is still alive
					    * Then reset target to that unless that target
					    * is the same type of enemy as the enemy that
					    * fired it because they are immune to their
					    * own attack.
					    */
						if(this.sourceEnemy != null
								&& this.sourceEnemy.ID !=
								boss.ID)
						{
							boss.targetEnemy 
								= this.sourceEnemy;
							
							//Hurt enemy, and activate
							//the enemy if not already.
							boss.hurt(this.damage, bossHit);
							boss.activated = true;
						}
						else
						{
							//Hurt enemy
							boss.hurt(this.damage, bossHit);
							
							//Activate the enemy if not already.
							boss.activated = true;
							
							//Enemies target is now you
							boss.targetEnemy = null;
							
							//Not searching for you anymore, it will just come
							//right towards your position
							boss.searchMode = false;
						}
						
						/*
						 * If an enemy is made happy, then remove that enemy from
						 * the game, and call the death function to drop any items
						 * or add to the made happy count of the game as needed.
						 */
						if (boss.health <= 0) 
						{
							boss.enemyDeath();
						}
						
						//If shotgun spread bullet
						if(ID == 1)
						{
							bossHit = true;
						}
						
						//Add HitSprite to the game
						HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
						
						Game.sprites.add(hS);
						
						//Bullet texture does not stay in mid-air
						disappear = true;
						
						projectileHit(false);
						
						return false;
					}
				}
			}
		}
		
		try
		{
			//Go through all the enemies on the block
			for(int i = 0; i < block.entitiesOnBlock.size(); i++)
			{
				Entity enemy = block.entitiesOnBlock.get(i);
				
				//Distance between projectile and other enemy
				double distance = Math.sqrt(((Math.abs(enemy.xPos - nextX))
						* (Math.abs(enemy.xPos - nextX)))
						+ ((Math.abs(enemy.zPos - nextZ))
								* (Math.abs(enemy.zPos - nextZ))));
				
				//If this projectile was fired by the same enemy, skip 
				//everything below
				if(this.sourceEnemy != null &&
						this.sourceEnemy.equals(enemy))
				{
					//Nothing right now
				}
				else
				{	
					double distanceCheck = 0.35;
					
					//So that even though reapers are faster, the
					//projectile will still hit them and not miss if
					//they are running towards or away from you.
					if(enemy.ID == 4)
					{
						distanceCheck = 0.7;
					}
					
					//If close enough, hit enemy. If faster enemy then
					//have a greater hit distance as the enemy may be
					//speeding toward you or away from you.
					if(distance <= distanceCheck)
					{
						//If enemy can be seen on the screen
						if(enemy.pixelsOnScreen.size() > 0
								&& this.pixelsOnScreen.size() > 0
								&& (ID == 0 || ID == 2))
						{
							boolean hitObject = false;

							for(int j = 0; j < this.pixelsOnScreen.size(); j++)
							{
								int temp = this.pixelsOnScreen.get(j);
								
								if(enemy.pixelsOnScreen.contains(temp))
								{
									hitObject = true;
									break;
								}

							}
							
							//If the enemy is indeed hit
							if(hitObject)
							{
								enemy.searchMode = false;
							   /*
							    * If enemy that shot projectile is still alive
							    * Then reset target to that unless that target
							    * is the same type of enemy as the enemy that
							    * fired it because they are immune to their
							    * own attack.
							    */
								if(this.sourceEnemy != null
										&& this.sourceEnemy.ID !=
										enemy.ID)
								{
									enemy.targetEnemy 
										= this.sourceEnemy;
									
									//Hurt enemy, and activate
									//the enemy if not already. Can't be a rocket
									//to hurt. The explosion is what hurts the
									//enemy.
									if(ID != 3)
									{
										enemy.hurt(this.damage, enemyHit);
									}
									
									enemy.activated = true;
									enemy.searchMode = false;
								}
								else
								{
									//Hurt enemy if not a rocket. The explosion is
									//what harms the enemy from the rocket.
									if(ID != 3)
									{
										enemy.hurt(this.damage, enemyHit);
									}	
									
									//Activate the enemy if not already.
									enemy.activated = true;
									
									//Enemies target is now you
									enemy.targetEnemy = null;
									
									//Not searching for you anymore, it will just come
									//right towards your position
									enemy.searchMode = false;
								}
								
								/*
								 * If an enemy is made happy, then remove that enemy from
								 * the game, and call the death function to drop any items
								 * or add to the made happy count of the game as needed.
								 */
								if (enemy.health <= 0) 
								{
									enemy.enemyDeath();
								}
								
								//If shotgun spread bullet
								if(ID == 1)
								{
									enemyHit = true;
								}
								
								//Add HitSprite to the game
								HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
								
								Game.sprites.add(hS);
								
								//Bullet texture does not stay in mid-air
								disappear = true;
								
								projectileHit(false);
								
								return false;	
							}
						}
						//The old method of checking collision
						else if(((Math.abs((this.y) - ((enemy.getY()))) <= 0.6)))
						{
							enemy.searchMode = false;
							
						   /*
						    * If enemy that shot projectile is still alive
						    * Then reset target to that unless that target
						    * is the same type of enemy as the enemy that
						    * fired it because they are immune to their
						    * own attack.
						    */
							if(this.sourceEnemy != null
									&& this.sourceEnemy.ID !=
									enemy.ID)
							{
								enemy.targetEnemy 
									= this.sourceEnemy;
								
								//Hurt enemy, and activate
								//the enemy if not already. Can't be a rocket
								//to hurt. The explosion is what hurts the
								//enemy.
								if(ID != 3)
								{
									enemy.hurt(this.damage, enemyHit);
								}
								
								enemy.activated = true;
								enemy.searchMode = false;
							}
							else
							{
								//Hurt enemy if not a rocket. The explosion is
								//what harms the enemy from the rocket.
								if(ID != 3)
								{
									enemy.hurt(this.damage, enemyHit);
								}	
								
								//Activate the enemy if not already.
								enemy.activated = true;
								
								//Enemies target is now you
								enemy.targetEnemy = null;
								
								//Not searching for you anymore, it will just come
								//right towards your position
								enemy.searchMode = false;
							}
							
							/*
							 * If an enemy is made happy, then remove that enemy from
							 * the game, and call the death function to drop any items
							 * or add to the made happy count of the game as needed.
							 */
							if (enemy.health <= 0) 
							{
								enemy.enemyDeath();
							}
							
							//If shotgun spread bullet
							if(ID == 1)
							{
								enemyHit = true;
							}
							
							//Add HitSprite to the game
							HitSprite hS = new HitSprite(this.x, this.z, this.y, 3);
							
							Game.sprites.add(hS);
							
							//Bullet texture does not stay in mid-air
							disappear = true;
							
							projectileHit(false);
							
							return false;
						}
					}
				}	
			}	
		}
		catch(Exception e)
		{
		}
		
		try
		{
			//If fired by an enemy, not the player
			if(sourceEnemy != null)
			{				
				//Finds the distance the item is away from the player
				double distance = Math.sqrt(((Math.abs(this.x - Player.x))
						* (Math.abs(this.x - Player.x)))
						+ ((Math.abs(this.z - Player.z))
								* (Math.abs(this.z - Player.z))));
				
				//If it hits the player, and player is alive and not
				//invincible.
				if(distance <= 0.3 && Math.abs
						((Player.y) + ((this.y * 10) / 1.2)) <= 8
						&& Player.alive)
				{
					Player.hurtPlayer(this.damage);
					projectileHit(true);
					
					return false;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
	   /*
	    * Check to see if bullet hit a block, and return whether it did
	    * or not.
	    */	
	    if(block.isSolid || block2.isSolid)
	    {
	    	return collisionChecks(block) && collisionChecks(block2);
	    }
	    
		return true;
	}
	
   /**
    * Frees up code space and makes it easier to make changes to all the
    * collision checks at once just changing just one method.
    * 
    * Basically just checks to see if the projectile hits a block. Because
    * doors are moving and wierd, the projectile can just go through doors
    * that are moving for now.
    * @param block
    * @return
    */
	public boolean collisionChecks(Block block)
	{
		double yCorrect = -(y - 0.5) * 10;
		
	   /*
	    * The players horizontal line of sight is "technically" at y = 0
	    * so the floor is considered y = 1. This corrects the y for the
	    * bullet to make it so that the floor is considered 0 instead
	    * for collision detection purposes.
	    */
		if(yCorrect > 0 && yCorrect <= 1)
		{
			yCorrect = 0;
		}
		
	   /*
	    * If bullet is in between the top and bottom of the block,
	    * and it isn't a moving door. Or if it hits the very bottom
	    * of a block and the y value happens to be greater than 0
	    * because apparently projectiles can do that.
	    */
		if(!hit && (yCorrect > (block.y * 4) 
				&& yCorrect < ((block.y * 4) + block.height)))
		{			
		   /*
		    * If a glass wall that is also labeled as a breakable wall, it
		    * is breakable by bullets or rockets. Then it turns into
		    * an air block when it is broken.
		    */
			try
			{
				if(block.wallID == 4 &&
						block.wallItem.itemID ==
						ItemNames.BREAKABLEWALL.getID()
						&& !hit)
				{
					block.health -= damage;
					
					//Blass glass hit/break sound
					SoundController.glassBreak.playAudioFile(distanceFromPlayer);
					
					if(block.health <= 0)
					{
						Game.items.remove(block.wallItem);
						
						//New air block
						block = new Block(0,0,0,block.x, block.z);
						block.wallItem = null;
						
						//Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}
					
					//Add HitSprite to the game
					HitSprite hS = new HitSprite(this.x, this.z, this.y, 5);
					
					Game.sprites.add(hS);
					
					//Makes sure alternate bullet hit sprites aren't rendered
					disappear = true;
				}
				//If electric wall
				else if(block.wallID == 15 && !hit)
				{
					//Damage wall
					block.health -= damage;
					
					//Blass glass hit/break sound
					SoundController.explosion.playAudioFile(distanceFromPlayer);
					
					//If block is broken
					if(block.health <= 0)
					{					
						//New dead electric block
						block = new Block(block.height,
								19, block.y, block.x, block.z);
						
						//Computer Shutting down sound effect
						SoundController.computerShutdown.playAudioFile(distanceFromPlayer);
						
						//Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}
				}
			}
			catch(Exception e)
			{
				
			}
			
			//Only for rockets
			if(ID == 3)
			{
				//Loop through this bringing the projectile closer and closer to
				//the wall until it hits the wall. This makes it so that rockets
				//will explode right next to the wall instead of farther out like
				//they used to.
				while((int)this.x != block.x || (int)this.z != block.z)
				{		
					//Move at tenth the speed it was originally for
					//smaller more precise increments
					this.x += xa / 10;
					this.z += za / 10;
					
					//Makes it so an infinite loop does not occur if the
					//projectile goes through the corner of a wall and misses
					//its chance to check collision or not.
					if((xa > 0 && this.x + xa > block.x)
							|| (xa < 0 && this.x + xa < block.x))
					{
						break;
					}
					
					//Same as above but for the z
					if((za > 0 && this.z + za > block.z)
							|| (za < 0 && this.z + za < block.z))
					{
						break;
					}
				}
			}
			
		   /*
		    * For doors, bullet holes disappear quicker as well so that
		    * if the door moves there won't be "floating" bullet holes.
		    */
			if(block.isADoor)
			{
				//Add HitSprite to the game
				HitSprite hS = new HitSprite(this.x, this.z, this.y, 5);
				
				Game.sprites.add(hS);
				
				//Makes sure alternate bullet hit sprites aren't rendered
				disappear = true;
			}
			
			//Projectile hit something.
			projectileHit(true);
			
			//Can't move anymore
			return false;
		}
		
		//Idk why but for some reason the bullet comes back through this
		//loop sometimes even after its already hit something. So this 
		//doesn't allow it to continue in that case.
		if(hit)
		{
			return false;
		}
		
		return true;
	}
	
   /**
    * Takes care of rockets, and sets hit to being true for projectiles
    */
	public void projectileHit(boolean playSound)
	{
		//If already hit, don't replay sound like it does for some
		//weird reason. Just return.
		if(hit)
		{
			return;
		}
		
		hit = true;
		
		//Normal bullet, not rocket
		if(ID <= 1 && !disappear)
		{
			if(playSound)
			{
				SoundController.wallHit.playAudioFile(distanceFromPlayer);
			}
			
			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);
			
			Game.sprites.add(hS);
			
			//Remove from bullets list
			Game.bullets.remove(this);
		}
		//Phase cannon hit
		else if(ID == 2)
		{		
			SoundController.phaseCannonHit.
			playAudioFile(distanceFromPlayer);
			
			HitSprite hS = new HitSprite(this.x, this.z, this.y, this.ID);
			
			Game.sprites.add(hS);
			
			//Remove from bullets list
			Game.bullets.remove(this);
		}
		//Rockets
		else if(ID == 3)
		{
			//Stop playing the rocket flying clip
			SoundController.rocketFly.stopClip();
			
			//Play hit sound effect
			SoundController.explosion.playAudioFile(distanceFromPlayer);
			
			double yCorrect = this.y;
			
			Block blockOn = Level.getBlock((int)this.x, (int)this.z);
			
		   /*
		    * If the rocket goes through the block or floor that the
		    * player is on, then reset its height to be the block it
		    * went throughs height so that rocket jumping will work
		    * correctly.
		    */
			if(-yCorrect < (blockOn.height + blockOn.y) - 1)
			{
				yCorrect = -(blockOn.height + blockOn.y - 1);
			}
			
			//Add an explosion where it hit and tick it so it has an
			//effect right after hit.
			Explosion temp = new Explosion(this.x, this.y, this.z, 0, 0);
			
			temp.tick();
			
			Game.explosions.add(temp);
			
			//Remove from bullets list
			Game.bullets.remove(this);
		}
		//If an enemy projectile
		else if(ID >= 4)
		{
			SoundController.enemyFireHit.playAudioFile(distanceFromPlayer);
			
			//Add HitSprite to the game
			HitSprite hS = new HitSprite(this.x, this.z, this.y, 4);
			
			Game.sprites.add(hS);
			
			//Remove from its list
			Game.enemyProjectiles.remove(this);
		}
	}
}
