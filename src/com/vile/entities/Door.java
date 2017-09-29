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
	
	//Type of door. Normal or a door that requires a certain key
	public  int doorType = 0;
	
	//Time between movement sounds
	public int soundTime = 0;
	
	//Door Flags! (Most are not in use yet)
	public boolean stayOpen = false;
	
	//Makes sure stopping sound doesn't repeat
	private boolean soundPlayed = false;
	
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
			int type, int itemActID) 
	{
		super(0, 0, 0, 0, 0, x, y, z, 8, 0, itemActID);
		
		doorX = wallX;
		doorZ = wallZ;
		doorType = type;
		
		if(itemActID > 0)
		{
			stayOpen = true;
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
		Block temp = Level.getBlock(doorX, doorZ);
		
		//Set the doorY position
		doorY = temp.y;
		
		distanceFromPlayer = Math.sqrt(((Math.abs(xPos - Player.x))
				* (Math.abs(xPos - Player.x)))
				+ ((Math.abs(zPos - Player.z))
						* (Math.abs(zPos - Player.z))));
		
		//When a door is moving, it is see through. Otherwise normally not
		temp.seeThrough = true;
		
	   /*
	    * When the door is moving, this will tell the program to adjust
	    * the Players wall detection settings so that when walking inside
	    * doorway, the player still detects walls correctly.
	    */
		temp.isaDoor = true;
	
		//If the wall was just activated, restart the sound time
		if(temp.y <= 0 && time == 0)
		{
			soundTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer);
		}
		
		//If door has reached maximum height
		if(temp.y >= 3)
		{
			time++;
			temp.y = 3;
			
			//Reset time between sounds
			soundTime = 0;
			
			if(!soundPlayed)
			{
				soundPlayed = true;
				SoundController.lifting.stopAll();
				SoundController.doorEnd.playAudioFile(distanceFromPlayer);
			}
		}
	
		//If Door has closed completely
		if(temp.y <= 0 && time > 0)
		{
			time = 0;
			temp.y = 0;
			
			//Block is no longer in an active state
			activated = false;
			
		   /*
		    * Unless the block was already seeThrough, set it back to not
		    * being see through.
		    */
			if(temp.wallID != 4)
			{
				temp.seeThrough = false;
				temp.isaDoor = false;
			}
			
			//Reset time between sounds
			soundTime = 0;
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer);
			
			//Reset
			soundPlayed = false;
			
			return;
		}
	
		//If door is moving up
		if(temp.y < 3 && time == 0)
		{
			temp.y += 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer);
				
				soundTime++;
			}
		}
		//If door is moving down
		else if(temp.y <= 3 && time > 250 && !stayOpen)
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
				for(int i = 0; i < thisBlock.enemiesOnBlock.size(); i++)
				{
					time = 0;
					return;
				}
			}
			
			//Move down
			temp.y -= 0.05;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer);
				
				soundTime++;
			}
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
}
