package com.vile.entities;

import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Eyesight
 * @author Alexander Byrd
 * Date Created: 7/4/2017
 * 
 * Description:
 * Carrying its own collision detection method. An eyesight entities
 * soul purpose is to move from an entity to its target, and will see
 * if it can see that target. Walls will block the entity from being
 * able to see the target, and this object will make sure it sends
 * back false in the case that it hits a wall and not the target.
 * 
 * Though this is an entity it is not a normal entity, and does not
 * exhibit many of the properties that other entities have. It only
 * has one main purpose. Therefore It does not extend entity.
 *
 */
public class Eyesight 
{
	public double x = 0;
	public double z = 0;
	public double y = 0;
	public double moveX = 0;
	public double moveZ = 0;
	public double moveY = 0;
	public double targetX = 0;
	public double targetY = 0;
	public double targetZ = 0;
	
	public boolean hitWall = false;
	public boolean hitTarget = false;
	
   /**
    * Creates new Eyesight object and resets the values
    * @param x
    * @param z
    * @param y
    * @param targetX
    * @param targetZ
    * @param targetY
    * @param moveX
    * @param moveZ
    * @param moveY
    */
	public Eyesight(double x, double z, double y,
			double targetX, double targetZ, double targetY,
			double moveX, double moveZ, double moveY) 
	{
		this.x = x;
		this.z = z;
		this.y = y;
		this.targetX = targetX;
		this.targetZ = targetZ;
		this.targetY = targetY;
		this.moveX = moveX;
		this.moveZ = moveZ;
		this.moveY = moveY;
	}
	
   /**
    * Automatically checks the line of sight from a certain point to
    * another to see if there are obstacles in the way between the 
    * point and the target point. Keeps moving the eyesight object 
    * along until it hits something. The isFree method keeps track
    * of whether that "thing" that was hit was a wall or the target
    * @return
    */
	public boolean checkEyesight()
	{
		int tick = 0;
		
	   /*
	    * While it hasn't hit something, then keep looping.
	    * The tick parameter is a fallback as in VERY VERY rare
	    * case scenarios it doesn't hit something or check correctly
	    * so it would got into an infinite loop. This fixes that in
	    * case it happens.
	    */
		while(!hitWall && !hitTarget && tick < 10000)
		{	
			//See if it can move in the x direction
			if(isFree(x + moveX, z))
			{
				x += moveX;
			}
			
			//See if it can move in the z
			if(isFree(x, z + moveZ) && !hitWall && !hitTarget)
			{
				z += moveZ;
			}
		
			y += moveY;
			
			tick++;
		}
		
		return hitTarget;
	}
	
	
	
   /**
    * Determines whether the eyesight is still uninhibited or not yet.
    * If it hits a wall, the entity cannot see the target. If it hits
    * the target, the entity can see the target. If it doesn't hit anything
    * 
    * @param xx
    * @param zz
    * @return
    */
	public boolean isFree(double nextX, double nextZ)
	{	
		double zz = 0;
		
		//Dont let entity exit the map
		if(nextX < 0 || nextX > Level.width || nextZ < 0
				|| nextZ > Level.height)
		{
			hitWall = true;
			hitTarget = false;
			return false;
		}
		
	   /*
	    * Eyesight cannot go through a block. It means the entity cannot
	    * see its target
	    */
		Block block = Level.getBlock((int)(nextX - zz),(int)(nextZ - zz));
		Block block2 = Level.getBlock((int)(nextX - zz), (int)(nextZ + zz));
			
		if(nextX < this.x && nextZ == this.z)
		{
			block = Level.getBlock((int)(nextX - zz),(int)(nextZ - zz));
			block2 = Level.getBlock((int)(nextX - zz), (int)(nextZ + zz));
		}
		else if(nextX >= this.x && nextZ == this.z)
		{
			block = Level.getBlock((int)(nextX + zz),(int)(nextZ - zz));
			block2 = Level.getBlock((int)(nextX + zz), (int)(nextZ + zz));
		}
		else if(nextX == this.x && nextZ >= this.z)
		{
			block = Level.getBlock((int)(nextX - zz),(int)(nextZ + zz));
			block2 = Level.getBlock((int)(nextX + zz),(int)(nextZ + zz));
		}
		else //(xx == xPos && zz < zPos)
		{
			block = Level.getBlock((int)(nextX - zz),(int)(nextZ - zz));
			block2 = Level.getBlock((int)(nextX + zz),(int)(nextZ - zz));
		}
		
		//NOT RIGHT NOW
		try
		{
			//Go through all the enemies on the block
			/*
			for(int i = 0; i < block.enemiesOnBlock.size(); i++)
			{
				Enemy temp = block.enemiesOnBlock.get(i);
				
				//Distance between enemy and other enemy
				double distance = Math.sqrt(((Math.abs(temp.xPos - nextX))
						* (Math.abs(temp.xPos - nextX)))
						+ ((Math.abs(temp.zPos - nextZ))
								* (Math.abs(temp.zPos - nextZ))));
				
				//If close enough, don't allow the sight to go through
				//other enemies
				if(distance <= 0.3 && !this.equals(temp)
						&& Math.abs(this.y - temp.yPos) <= 8)
				{
					hitWall = true;
					hitTarget = false;
					return false;
				}	
			}*/
		}
		catch(Exception e)
		{
			
		}
		
		//Distance between line of sight and target
		double distance = Math.sqrt(((Math.abs(targetX - nextX))
				* (Math.abs(targetX - nextX)))
				+ ((Math.abs(targetZ - nextZ))
						* (Math.abs(targetZ - nextZ))));
		
		//Correct it for if the target is crouching 
		//and has a y less than 0
		if(targetY < 0)
		{
			targetY = 0;
		}
		
		//Difference between the the two entities y values
		double yDifference = targetY - Math.abs(this.y);
		
		//Hit Target, meaning the target is in sight
		if(distance <= 0.3 && yDifference <= 2
				&& yDifference >= -8)
		{
			hitWall = false;
			hitTarget = true;
			return false;
		}
		
	   /*
	    * See if the line of sight has hit a block or not.
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
    * Optimizes code.
    * @param block
    * @return
    */
	public boolean collisionChecks(Block block)
	{		
	   /*
	    * If the block in front of the entity is greater than two units
	    * higher than the entity, or if it is more than two lower than
	    * the entity, or the entity is still not far enough under a block
	    * to go through it (mainly used with doors) then don't allow
	    * the entity to move.
	    */
		if(((block.height + block.y - 2) > 
			-this.y && -this.y + 2 > block.y && !block.isaDoor))
		{
			//System.out.println("This y: "+this.y);
			//System.out.println("Block Height: "+block.height);
			hitTarget = false;
			hitWall = true;
			return false;
		}
		
		return true;
	}

}
