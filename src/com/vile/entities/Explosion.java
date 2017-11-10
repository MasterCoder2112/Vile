package com.vile.entities;

import java.util.ArrayList;

import com.vile.Game;
import com.vile.SoundController;
import com.vile.graphics.Render3D;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Explosion
 * @author Alexander James Byrd
 * Date Created: 05/10/2017
 *
 * Description:
 * Can be used anywhere basically. If an explosion is set off, it will 
 * first figure out its x, y, z, and then at what height it should be 
 * rendered at to make it look realistic, and will tick every tick in the
 * game to update how far into the explosion it is into. 
 * 
 * Also there is a method in order to determine whether anything
 * (Entity, Player, any other Canister) to see if it is within range
 * of being hurt by the explosion, and the farther away from the
 * explosion something is, the less it'll be hurt.
 * 
 * As of the current update, the closer an entity is to the explosion in
 * a given direction and quadrant of the map, the entity will have the
 * force of the explosion acting on it, throwing it back and up or down
 * depending on how far the explosion is from it.
 */
public class Explosion 
{
	//Where is it at in the explosion
	public int phaseTime = 0;
	
	//Type of explosion
	public int ID = 0;
	
	//Activation ID
	public int itemActivationID = 0;
	
	//Corrects height it is seen at
	public double heightCorrect = 8;
	
	//Distance from the player
	public double distanceFromPlayer = 0;
	
	//Position variables
	public double x;
	public double y;
	public double z;
	
	//Did it already explode?
	public boolean exploded = false;
	
   /**
    * Creates new explosion with given values
    * @param x
    * @param y
    * @param z
    * @param ID
    */
	public Explosion(double x, double y, double z, int ID, int itemActID) 
	{
		this.x  = x;
		this.y  = y;
		this.z  = z;
		this.ID = ID;
		this.itemActivationID = itemActID;
		
		this.distanceFromPlayer = Math.sqrt(((Math.abs(x - Player.x))
				* (Math.abs(x - Player.x)))
				+ ((Math.abs(z - Player.z))
						* (Math.abs(z - Player.z))));
		
		//Add explosion to game
		Game.explosions.add(this);
		
	   /*
	    * So its height graphical seems to show correctly
	    */
		if(y >= 0 && y < 18)
		{
			heightCorrect = 8;
		}
		else if(y >= 18 && y < 30)
		{
			heightCorrect = 9;
		}
		else if(y >= 30 && y <= 36)
		{
			heightCorrect = 9;
		}
		else if(y > 36 && y <= 48)
		{
			heightCorrect = 10;
		}
		else if(y <= 79)
		{
			heightCorrect = 10;
		}
		else
		{
			double addCorrect;
			y -= 60;
			
			addCorrect = y / 20;
			heightCorrect = 10 + (0.5 * addCorrect);	
		}
	}
	
   /**
    * Updates the explosions values, and also when an explosion is at
    * phaseTime 1 it will check to see what other entities are within
    * range of the explosion and harm them as such. After 16 ticks
    * (depending on the FPS of course) it will delete this explosive 
    * object from the game.
    */
	public void tick()
	{
		phaseTime++;
		
	   /*
	    * If the explosion is a rocket, let it hurt anything in the
	    * area right on impact. If an explosive canster, it waits for a
	    * few ticks before actually having an effect on the area around it
	    * since the explosion has not reached its full potential till
	    * 13 ticks into it.
	    */
		if(phaseTime == 1 && ID == 0
				|| phaseTime >= 13 * Render3D.fpsCheck
				&& ID == 1 && !exploded)
		{
			exploded = true;
			explosionHarm();
		}
		
		//If at end of explosion delete explosion object
		if(phaseTime >= 16 * Render3D.fpsCheck)
		{
			removeObject();
		}
	}
	
   /**
    * Checks radius of explosion for entities (Player, enemies, other
    * canisters) and depending on how far from the canister the entity
    * is, it will hurt it by a certain amount, or activate it if it's
    * a canister.
    * 
    * Also will now throw the entity back a bit kind of like the force
    * of an explosion normally would. Also allowing for rocket jumping
    */
	public void explosionHarm()
	{	
		//Distance between explosion and player
		double distance = Math.sqrt(((Math.abs(this.x - Player.x))
				* (Math.abs(this.x - Player.x)))
				+ ((Math.abs(this.z - Player.z))
						* (Math.abs(this.z - Player.z))));
		
		//Where the entity is from the explosion on the map
		double rotFromTarget = 0;
		
	   /*
	    * See if player is within range of explosion
	    * and if he/she is, then damage the player
	    * according to the distance they are from the explosion
	    */
		if(distance <= 3)
		{	
			double damage = 60;
			double force = 0;
			
			damage -= 2 * (distance / 0.1);	
			
			force = (damage / 60) * 3;
			
			//Angle that the player is in accordance to the explosion for
			//throwing back calculations
			rotFromTarget = Math.atan
			(((Player.x - x)) / ((Player.z - z)));
			
		   /*
		    * If the target is in the 3rd or 4th quadrant of the map then
		    * add PI to rotation so that the player will be thrown back
		    * into the correct quadrant of the map
		    */
			if(Player.z < z)
			{
				rotFromTarget += Math.PI;
			}
			
		   /*
		    * Corrects rotation so that the vector is centered correctly
		    * in the direction its facing. It doesn't do that automatically
		    * for some reason
		    */
			double correction = 44.765;
				
		   /*
		    * Depending on the targets angle in the x z plane from the
		    * explosion, the player will move back from that explosion a
		    * certain amount.
		    */
			Player.xEffects = 
					((Math.cos(rotFromTarget - correction)) 
							+ (Math.sin(rotFromTarget - correction))) 
							* force;
			Player.zEffects = 
					((Math.cos(rotFromTarget - correction)) 
							- (Math.sin(rotFromTarget - correction))) 
							* force;
			
			double yCorrect = this.y;
			
			if(this.y > 1)
			{
				yCorrect = 1;
			}
			
			if(-yCorrect > Player.y) 
			{
				double temp = Math.abs(yCorrect - (Player.y));
				
				//System.out.println("Temp: "+temp);
				
				//A temporary fix
				//TODO fix this to be realistic
				temp = 0.01;
				
				Player.yEffects = (-1/temp) * (force / 8);
			}
			else
			{
				double temp = Math.abs((Player.y) - yCorrect) / 500;
				
				//A temporary fix
				//TODO Fix this to be realistic
				temp = 0.01;
				
				//System.out.println("TEMP: "+temp);
				
				Player.yEffects = (1/temp) * (force / 8);
			}
			
			if(Player.yEffects > 20)
			{
				Player.yEffects = 20;
			}
			
			if(Player.immortality == 0 
				&& !Player.godModeOn)
			{
				Player.hurtPlayer(damage);	
			}
		}
		
	   /*
	    * Gets all blocks surrounding it
	    */
		Block[] surroundingBlocks = new Block[9];
		surroundingBlocks[0] = Level.getBlock((int)x + 1, (int)z);
		surroundingBlocks[1] = Level.getBlock((int)x , (int)z + 1);
		surroundingBlocks[2] = Level.getBlock((int)x - 1, (int)z);
		surroundingBlocks[3] = Level.getBlock((int)x, (int)z - 1);
		surroundingBlocks[4] = Level.getBlock((int)x, (int)z);
		surroundingBlocks[5] = Level.getBlock((int)x + 1, (int)z + 1);
		surroundingBlocks[6] = Level.getBlock((int)x - 1, (int)z - 1);
		surroundingBlocks[7] = Level.getBlock((int)x + 1, (int)z - 1);
		surroundingBlocks[8] = Level.getBlock((int)x - 1, (int)z + 1);
		
	   /*
	    * If a glass block with a secret object, it is breakable, and
	    * therefore if an explosion is around it, it will break and
	    * become an air block.
	    * 
	    * Can also do to electric walls. If hit enough it will break
	    * and dim.
	    */
		for(int i = 0; i < surroundingBlocks.length; i++)
		{
			Block block = surroundingBlocks[i];
			
		   /*
		    * Any enemies within range of the explosion will be harmed depending
		    * on their distance from the explosion. 
		    */
			for(int j = 0; j < block.entitiesOnBlock.size(); j++)
			{
				Entity enemy = block.entitiesOnBlock.get(j);
				
				//Self explainatory by now I hope
				distance = Math.sqrt(((Math.abs(this.x - enemy.xPos))
						* (Math.abs(this.x - enemy.xPos)))
						+ ((Math.abs(this.z - enemy.zPos))
								* (Math.abs(this.z - enemy.zPos))));
				
				//If within range of normal enemy
				if(distance <= 3 && Math.abs(this.y - (enemy.yPos / 12)) <= 2
						&& !enemy.isABoss)
				{	
					//Realistic damage
					double damage = 80;
					
					//Force of explosion on enemy
					double force = 0;
					
					//Every 0.1 distance units, subtract 2 from the damage
					//the explosion will cause.
					damage -= 2 * (distance / 0.1);
					
					force = (damage / 60) * 3;
					
					//Heavier enemies don't move as far
					force /= enemy.weightLevel;
					
					//Angle that the player is in accordance to the explosion for
					//throwing back calculations
					rotFromTarget = Math.atan
					(((enemy.xPos - x)) / ((enemy.zPos - z)));
					
				   /*
				    * If the target is in the 3rd or 4th quadrant of the map then
				    * add PI to rotation so that the player will be thrown back
				    * into the correct quadrant of the map
				    */
					if(enemy.zPos < z)
					{
						rotFromTarget += Math.PI;
					}
					
				   /*
				    * Corrects rotation so that the vector is centered correctly
				    * in the direction its facing. It doesn't do that automatically
				    * for some reason
				    */
					double correction = 44.765;
						
				   /*
				    * Depending on the targets angle in the x z plane from the
				    * explosion, the player will move back from that explosion a
				    * certain amount.
				    */
					enemy.xEffects = 
							((Math.cos(rotFromTarget - correction)) 
									+ (Math.sin(rotFromTarget - correction))) 
									* force;
					enemy.zEffects = 
							((Math.cos(rotFromTarget - correction)) 
									- (Math.sin(rotFromTarget - correction))) 
									* force;
					
					double yCorrect = this.y;
					
					if(this.y > 1)
					{
						yCorrect = 1;
					}
					
					if(-yCorrect > enemy.yPos) 
					{
						double yForce = Math.abs(yCorrect - (enemy.yPos));
						
						//A temporary fix
						//TODO fix this to be realistic
						yForce = 0.01;
						
						enemy.yEffects = ((-1/yForce) * (force / 8));// / enemy.weightLevel;
					}
					else
					{
						double yForce = Math.abs((enemy.yPos) - yCorrect) / 500;
						
						//A temporary fix
						//TODO Fix this to be realistic
						yForce = 0.01;
						
						enemy.yEffects = ((1/yForce) * (force / 8));// / enemy.weightLevel;
					}
					
					if(enemy.yEffects > 30)
					{
						enemy.yEffects = 30;
					}
					
					//Hurt the enemy, and activate the enemy
					enemy.hurt((int)damage, false);	
					enemy.activated = true;
					enemy.searchMode = false;
					
					//If enemy losses all of its health, commence
					//the enemy death.
					if(enemy.health <= 0 && enemy.isAlive)
					{
						enemy.enemyDeath();
						block.entitiesOnBlock.remove(enemy);
					}
					else if(enemy.health <= 0 && !enemy.isAlive)
					{
						block.entitiesOnBlock.remove(enemy);
					}
				}
			}
			
			try
			{
				//Secret glass block
				if(block.wallID == 4 &&
						block.wallItem.itemID ==
						ItemNames.BREAKABLEWALL.getID())
				{
					block.health -= 60;
					
					//Blass glass hit/break sound
					SoundController.glassBreak.playAudioFile(distanceFromPlayer);
					
					if(block.health <= 0)
					{
						//Remove breakable wall item
						Game.items.remove(block.wallItem);
						
						//Keep track of the enemies on the block before 
						//the block changes
						ArrayList<Entity> temp = block.entitiesOnBlock;
						
						//New air block
						block = new Block(0,0,0,block.x, block.z);
						block.wallItem = null;
						
						//Same enemies are on this block as the one that 
						//it changed from
						block.entitiesOnBlock = temp;
						
						//Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}
				}
				//If electric wall, this wall can be broken too
				else if(block.wallID == 15)
				{
					block.health -= 60;
					
					//Blass glass hit/break sound
					SoundController.glassBreak.playAudioFile(distanceFromPlayer);
					
					if(block.health <= 0)
					{		
						//Keep track of enemies on block before the 
						//block changes
						ArrayList<Entity> temp = block.entitiesOnBlock;
						
						//New non electrified block
						block = new Block(block.height,
								19, block.y, block.x, block.z);
						
						//Same enemies are on this block as the one that 
						//it changed from
						block.entitiesOnBlock = temp;
						
						//Explosion sound
						SoundController.explosion.playAudioFile(distanceFromPlayer);
						
						//Re-add to level
						Level.blocks[block.x + block.z * Level.width] = block;
					}
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
			   /*
			    * If the block surrounding it has an explosive canister
			    * on it
			    */
				if(block.wallItem.itemID == 32)
				{
					ExplosiveCanister temp 
						= (ExplosiveCanister)block.wallItem;
					
					//Distance between explosion and canister
					distance = Math.sqrt(((Math.abs(this.x - temp.x))
							* (Math.abs(this.x - temp.x)))
							+ ((Math.abs(this.z - temp.z))
									* (Math.abs(this.z - temp.z))));
					
					double yCorrect = this.y * 4;
					
					//Corrects it because when the explosion hits below
					//eye level it is technically positive, and when
					//multiplied by thirteen it causes the statement
					//below this one turn out to be false.
					if(this.y > 0)
					{
						yCorrect = 0;
					}
					
					//If within range, start a new explosion and remove the canister
					if(Math.abs((temp.y) + (yCorrect)) <= 8)
					{
						Game.explosions.add(
								new Explosion(temp.x, -temp.y/10, temp.z, 1, 0));
						block.wallItem = null;
						temp.removeCanister();
					}
				}
			}
			catch(Exception e)
			{
				
			}
		}
		
		//Bosses have a larger hit range so they have to be checked
		//separate.
		for(int i = 0; i < Game.bosses.size(); i++)
		{
			Enemy boss = Game.bosses.get(i);
			
			//Self explainatory by now I hope
			distance = Math.sqrt(((Math.abs(this.x - boss.xPos))
					* (Math.abs(this.x - boss.xPos)))
					+ ((Math.abs(this.z - boss.zPos))
							* (Math.abs(this.z - boss.zPos))));
			
			//Boss has larger range to hit
			if(distance <= 6)
			{		
				//Not meant to be funny, this just is the most realistic
				//it seems after testing. NO LAUGHING
				double damage = 69;
				
				//Every 0.1 distance units, subtract 2 from the damage
				//the explosion will cause. But only if the explosion is
				//outside of the bosses body range of 3
				if(distance > 3)
				{
					distance -= 3;
					damage -= 2 * (distance / 0.1);
				}
				
				//Hurt the enemy, and activate the enemy
				boss.hurt((int)damage, false);	
				boss.activated = true;
				boss.searchMode = false;
				
				//If enemy losses all of its sadness, commence
				//happiness proceedures.
				if(boss.health <= 0)
				{
					Block block = Level.getBlock
							((int)boss.xPos, (int)boss.zPos);
					boss.enemyDeath();
					block.entitiesOnBlock.remove(boss);
				}
			}
		}
		
		//Play explosion sound based on explosion type
		if(ID == 1)
		{
			SoundController.explosion.playAudioFile(distanceFromPlayer);
		}
		else
		{
			SoundController.rocketFly.stopClip();
			SoundController.explosion.playAudioFile(distanceFromPlayer);
		}
	}
	
   /**
    * Removes the explosion from the game
    */
	public void removeObject()
	{
		Game.explosions.remove(this);
	}
}

