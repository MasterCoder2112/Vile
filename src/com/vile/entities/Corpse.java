package com.vile.entities;

import java.util.Random;

import com.vile.Game;
import com.vile.graphics.Render;
import com.vile.graphics.Textures;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Corpse
 * @author Alexander Byrd
 * Date Created: 10/5/2016
 * 
 * Description:
 * This stores all the values needed for a corpse entity whose
 * soul purpose is for aesthetic feel, or for resurrection by
 * a magistrate enemy.
 *
 * Also now holds values to determine if it moves because of a
 * rocket blast. If an enemy is killed and is being shot backwards
 * by a rocket blast, the force still acts on its corpse, so this
 * keeps track of that.
 */
public class Corpse 
{
	//The usual values
	public double x = 0;
	public double z = 0;
	public double y = 0;
	
	//How much additional movement effects the corpse has on it from
	//an explosion source
	public double xEffects = 0;
	public double zEffects = 0;
	public double yEffects = 0;
	
	//How far into death animation it is.
	public int phaseTime = 0;
	
	//Time corpse has been here.
	public int time = 0;
	
	//Zero is default meaning wasn't alive, or not an enemy
	public int enemyID = 0;
	
	//Default image of the corpse
	public Render corpseImage = null;
	
   /**
    * Constructs a new Corpse of a certain x, y, and z value, and
    * the ID of the enemy it was before it was killed, or if it
    * was never alive, then use 0.
    * 
    * @param x
    * @param z
    * @param y
    * @param enemyID
    */
	public Corpse(double x, double z, double y, int enemyID,
			double xEffects, double zEffects, double yEffects) 
	{
		//Set values
		this.x = x;
		this.z = z;
		this.y = y;
		this.xEffects = xEffects;
		this.zEffects = zEffects;
		this.yEffects = yEffects;
		this.enemyID = enemyID;
		
		//Normal enemy death animation time
		if(enemyID != 0 && enemyID != 8)
		{
			this.phaseTime = 24;
		}
		
		//For Belegoth
		if(enemyID == 8)
		{
			this.phaseTime = 48;
		}
		
		Random rand = new Random();
		int newNum = rand.nextInt(2);
		
		if(newNum == 0)
		{
			corpseImage = Textures.defaultCorpse1;
		}
		else
		{
			corpseImage = Textures.defaultCorpse2;
		}
	}
	
   /**
    * Tick the time the corpse has been here, and also calculate the force
    * of an explosion still acting on it and perform the required
    * operations.
    */
	public void tick()
	{
		time++;
		
		//Don't let the time ticker go too high
		if(time > 20000)
		{
			time = 0;
		}
		
	   /*
	    * All for dealing with the force of explosions propelling
	    * enemies in some direction
	    */
		double xEff = 0;
		double zEff = 0;
		double yEff = 0;
		
		//Set movement for tick
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
		
		this.y -= (yEff);
		
	   /*
	    * Can the force of the explosion push the enemy any more into
	    * the x direction?
	    */
		if(isFree(x + (xEff), z))
		{
			x += (xEff);
		}
			
	   /*
		* Can the explosion push the enemy anymore into the z
		* direction.
		*/
		if(isFree(x, z + (zEff)))
		{
			z += (zEff);
		}
		
		//Update effect values based on what was executed above
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
	}
	
   /**
    * Determines whether the corpse is free to move to the next space or
    * not. 
    * @param xx
    * @param zz
    * @return
    */
	public boolean isFree(double nextX, double nextZ)
	{	
		double bufferZone = 0.3;
		
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
		Block block = Level.getBlock((int)(nextX - bufferZone),
				(int)(nextZ - bufferZone));
		Block block2 = Level.getBlock((int)(nextX - bufferZone),
				(int)(nextZ + bufferZone));
			
		if(nextX < this.x && nextZ == this.z)
		{
			block = Level.getBlock((int)(nextX - bufferZone),
					(int)(nextZ - bufferZone));
			block2 = Level.getBlock((int)(nextX - bufferZone),
					(int)(nextZ + bufferZone));
		}
		else if(nextX >= this.x && nextZ == this.z)
		{
			block = Level.getBlock((int)(nextX + bufferZone),
					(int)(nextZ - bufferZone));
			block2 = Level.getBlock((int)(nextX + bufferZone),
					(int)(nextZ + bufferZone));
		}
		else if(nextX == this.x && nextZ >= this.z)
		{
			block = Level.getBlock((int)(nextX - bufferZone),
					(int)(nextZ + bufferZone));
			block2 = Level.getBlock((int)(nextX + bufferZone),
					(int)(nextZ + bufferZone));
		}
		else //(xx == xPos && zz < zPos)
		{
			block = Level.getBlock((int)(nextX - bufferZone),
					(int)(nextZ - bufferZone));
			block2 = Level.getBlock((int)(nextX + bufferZone),
					(int)(nextZ - bufferZone));
		}
		
		try
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
						&& Math.abs(this.y - temp.yPos) <= 8)
				{
					return false;
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
		double yDifference = playerY - Math.abs(y);
		
		//Can't clip inside player
		if(distance <= bufferZone && yDifference >= -8)
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
	    				&& Math.abs(temp.y + y) <= temp.height)
	    		{
	    			return false;
	    		}
	    	}
	    	catch(Exception E)
	    	{
	    		
	    	}
	    }

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
	    * The corpse can't move forward anyway if the block its moving
	    * to has a solid object on it
	    */
		try
    	{
    		Item temp = block.wallItem;
    		
    		//If there is a solid item on the block, the corpse
    		//cannot move into it
    		if(Game.solidItems.contains(temp)
    				&& Math.abs(temp.y + y) <= temp.height)
    		{
    			return false;
    		}
    	}
    	catch(Exception E)
    	{
    		
    	}
		
	   /*
	    * If the block in front of the corpse is greater than two units
	    * higher than the corpse, 
	    * or the corpse is still not far enough under a block
	    * to go through it (mainly used with doors) then don't allow
	    * the corpse to move.
	    */
		if(((block.height + block.y - 2) > 
			-y && -y + 2 > block.y && !block.isaDoor))
		{
			return false;
		}
		
	   /*
	    * If the block is 2 higher than the corpse, 
	    * the corpse can pass through or onto it. 
	    */
		else if(block.isSolid && Math.abs(y) >= (block.y + block.height - 2)
				&& Math.abs(y) <= (block.y + block.height)
				|| Math.abs(y) >= block.y + block.height)
		{			
			//Return that it can move
			return true;
		}

		return true;
	}
}
