package com.vile.entities;

import com.vile.entities.Player;
import com.vile.SoundController;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Door
 * @author Alex Byrd
 * Date Updated: 8/15/2016
 * 
 * Description:
 * The Door Entity is an unseeable entity that coorelates to a wall that
 * is supposed to act as a door. When this entity is activated it will
 * move the door block up, keep it there for a little bit, then let it
 * fall again. This will allow the player to move through the door and
 * onto the next part of the level.  
 */
public class Door extends Entity 
{
	//Wall that door correlates to position on map
	public  int doorX;
	public  int doorZ;
	public  double doorY;
	
	//Keeps track of time door stays in up position
	public int time = 0;
	
	//The max height the door can raise to
	public double maxY = 3;
	
	//Type of door. Normal or a door that requires a certain key
	public  int doorType = 0;
	
	//Time between movement sounds
	public int soundTime = 0;
	
	//Y value the door starts at
	public double startY = 0;
	
	//Door Flags! (Most are not in use yet)
	public boolean stayOpen = false;
	
	//Makes sure stopping sound doesn't repeat
	private boolean soundPlayed = false;
	
	//Block that door item corresponds to.
	Block doorBlock = null;
	
   /**
    * A constructor of a door entity, which also holds the coordinates of
    * the block the door will raise.
    * @param x
    * @param y
    * @param z
    * @param wallX
    * @param wallZ
    */
	public Door(double x, double y, double z, int wallX, int wallZ,
			int type, int itemActID, double maxHeight) 
	{
		super(0, 0, 0, 0, 0, x, y, z, 8, 0, itemActID);
		
		doorX = wallX;
		doorZ = wallZ;
		doorType = type;
		this.maxY = (maxHeight / 4.0);
		doorBlock = Level.getBlock(doorX, doorZ);
		doorBlock.isADoor = true;
		startY = doorBlock.y;
		
	   /*
	    * If the item activation id is greater than 0 and not
	    * the special ID.
	    */
		if(itemActID > 0 && itemActID != 2112)
		{
			stayOpen = true;
		}
		
		//If door is on the ground, but maxHeight was not set on accident
		//default to opening to a height of 3
		if(maxHeight == 0 && startY == 0)
		{
			maxY = 4;
		}
	}
	
   /**
    * Gets the door block at the location of the door entity, and moves
    * it up to a height of 3, makes it stay there long enough for the
    * player to move through, then goes back down.
    */
	@SuppressWarnings("unused")
	public void move()
	{	
		//Set the doorY position
		doorY = doorBlock.y;
		
		distanceFromPlayer = Math.sqrt(((Math.abs(xPos - Player.x))
				* (Math.abs(xPos - Player.x)))
				+ ((Math.abs(zPos - Player.z))
						* (Math.abs(zPos - Player.z))));
		
	   /*
	    * When the door is moving, this will tell the program to adjust
	    * the Players wall detection settings so that when walking inside
	    * doorway, the player still detects walls correctly.
	    */
		doorBlock.isMoving = true;
		
		if(maxY < startY)
		{
			moveDown();
		}
		else
		{
			moveUp();
		}
		
		//Reset soundTime every 10 ticks.
		if(soundTime > 11 || soundTime == 0)
		{
			soundTime = 0;
		}
		else
		{
			soundTime++;
		}
	}
	
   /**
    * The default, if the door is supposed to move up
    */
	private void moveUp()
	{
		//If the wall was just activated, restart the sound time
		if(doorBlock.y <= startY && time == 0)
		{
			soundTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer * 2);
		}
		
		//If door has reached maximum height
		if(doorBlock.y >= maxY)
		{
			time++;
			doorBlock.y = maxY;
			
			//Reset time between sounds
			soundTime = 0;
			
			if(!soundPlayed)
			{
				soundPlayed = true;
				SoundController.lifting.stopAll();
				SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			}
		}
	
		//If Door has closed completely
		if(doorBlock.y <= startY && time > 0)
		{
			time = 0;
			doorBlock.y = 0;
			
			//Block is no longer in an active state
			activated = false;
			
		   /*
		    * Unless the block was not moving, set it back to not moving.
		    */
			if(doorBlock.wallID != 4)
			{
				doorBlock.isMoving = false;
			}
			
			//Reset time between sounds
			soundTime = 0;
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			
			//Reset
			soundPlayed = false;
			
			//If special ID, keep opening and closing
			if(itemActivationID == 2112)
			{
				activated = true;
			}
			
			return;
		}
	
		//If door is moving up
		if(doorBlock.y < maxY && time == 0)
		{
			doorBlock.y += 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);
				
				soundTime++;
			}
		}
		//If door is moving down
		else if(doorBlock.y <= maxY && time > 250 && !stayOpen)
		{
			Block thisBlock = Level.getBlock(doorX, doorZ);
			
			double distance = Math.sqrt(((Math.abs(this.getX() - Player.x))
					* (Math.abs(this.getX() - Player.x)))
					+ ((Math.abs(this.getZ() - Player.z))
							* (Math.abs(this.getZ() - Player.z))));
			
		   /*
		    * Checks to see if the player or an enemy is under the door
		    * and if either is true, do not allow the door to come down 
		    * and trap the player or enemy under the door, and make the 
		    * door detect the Player or enemy under it and go back up
		    * until the object is gone.
		    * 
		    * Because the Player can crouch, making his/her height 0,
		    * causing the door to shut when he/she crouches, the 0.1
		    * is added to the if statement so that the door does not
		    * close when the player is crouched.
		    * 
		    * The check for enemies is below in the for loop.
		    */
			if(distance <= 0.7 && thisBlock.y <= 
					Player.height + 0.1)
			{
				time = 0;
				return;
			}
			else
			{
				for(int i = 0; i < thisBlock.entitiesOnBlock.size(); i++)
				{
					Entity e = thisBlock.entitiesOnBlock.get(i);
					
					//Only stop moving down if there is an entity under the
					//block.
					if(-e.yPos <= thisBlock.y)
					{
						time = 0;
						return;
					}
				}
			}
			
			//Move down
			doorBlock.y -= 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);
				
				soundTime++;
			}
		}
	}
	
   /**
    * If door is supposed to move down, not up. Can also crush
    * player and entities
    */
	private void moveDown()
	{
		//If door hasn't started moving yet
		if(doorBlock.y >= startY && time == 0)
		{
			soundTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer * 2);
		}
		
		//If door has reached maximum height
		if(doorBlock.y <= maxY)
		{
			time++;
			doorBlock.y = maxY;
			
			//Reset time between sounds
			soundTime = 0;
			
			if(!soundPlayed)
			{
				soundPlayed = true;
				SoundController.lifting.stopAll();
				SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			}
		}
	
		//If Door has closed completely
		if(doorBlock.y >= startY && time > 0)
		{
			time = 0;
			doorBlock.y = startY;
			
			//Block is no longer in an active state
			activated = false;

			//Blocks that "areMoving" do not keep bullet holes in them in case
			//they move.
			if(!doorBlock.seeThrough)
			{
				doorBlock.isMoving = false;
			}
			
			//Reset time between sounds
			soundTime = 0;
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			
			//Reset
			soundPlayed = false;
			
			//If special ID, keep opening and closing
			if(itemActivationID == 2112)
			{
				activated = true;
			}
			
			return;
		}
	
		//If door is moving down
		if(doorBlock.y > maxY && time == 0)
		{
			Block thisBlock = Level.getBlock(doorX, doorZ);
			
			//Distance from player
			double distance = Math.sqrt(((Math.abs(this.getX() - Player.x))
					* (Math.abs(this.getX() - Player.x)))
					+ ((Math.abs(this.getZ() - Player.z))
							* (Math.abs(this.getZ() - Player.z))));
			
		   /*
		    * Checks to see if the player or an enemy is under the door
		    * and if either is true, crush the player or entity under
		    * the door. Make player crouch first though
		    * 
		    * The check for enemies is below in the for loop.
		    */
			if(distance <= 0.7 && thisBlock.y <= 
					Player.y + Player.height + Player.maxHeight + 0.1
					&& Player.y + Player.height < thisBlock.y + thisBlock.height)
			{
				//System.out.println(Player.height);
				//Forces player to crouch under the moving block
				Player.forceCrouch = true;
				
				//If below crouch height, then crush the player
				if(Player.height <= 0)
				{
					Player.hurtPlayer(500);
				}
			}
			else
			{
				for(int i = 0; i < thisBlock.entitiesOnBlock.size(); i++)
				{
					Entity e = thisBlock.entitiesOnBlock.get(i);
					
					//Only stop moving down if there is an entity under the
					//block.
					if(-e.yPos + (height / 4) >= thisBlock.y)
					{
						e.enemyDeath();
					}
				}
			}
			
			doorBlock.y -= 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);
				
				soundTime++;
			}
		}
		//If door is moving up
		else if(doorBlock.y >= maxY && time > 250 && !stayOpen)
		{
			//Move up
			doorBlock.y += 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);
				
				soundTime++;
			}
		}
	}
}
