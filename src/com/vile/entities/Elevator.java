package com.vile.entities;

import com.vile.entities.Entity;
import com.vile.SoundController;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

public class Elevator extends Entity 
{
	//Defaultly goes up 4 block heights
	public double upHeight = 48;
	
	public int elevatorX;
	public int elevatorZ;
	
	public int waitTime = 0;
	public boolean movingDown;
	public boolean movingUp;
	
	public int soundTime = 0;
	
   /**
    * Constructs a new elevator with a x and z value coordinating
    * with the elevator block. It is an entity, so it invokes
    * the super constructor.
    * @param x
    * @param y
    * @param z
    * @param wallX
    * @param wallZ
    */
	public Elevator(double x, double y, double z, int wallX, int wallZ,
			int itemActID, double maxHeight) 
	{
		super(0, 0, 0, 0, 0, x, y, z, 8, 0, itemActID);
		
		elevatorX = wallX;
		elevatorZ = wallZ;
		upHeight = maxHeight;
		
		Block temp = Level.getBlock(elevatorX, elevatorZ);
		height = (int)temp.height;
		temp.isADoor = true;
	}
	
   /**
    * Moves the elevator depending on where it is at in the process of
    * moving.
    */
	public void move()
	{
		//Get block
		Block temp = Level.getBlock(elevatorX, elevatorZ);
		
		distanceFromPlayer = Math.sqrt(((Math.abs(xPos - Player.x))
				* (Math.abs(xPos - Player.x)))
				+ ((Math.abs(zPos - Player.z))
						* (Math.abs(zPos - Player.z))));
		
		//If just activated, begin moving down
		if(!movingDown && !movingUp && waitTime == 0)
		{
			if(height == 0)
			{
				movingUp = true;
			}
			else
			{
				movingDown = true;
			}
			
			soundTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer * 2);
		}
		
		//As long as it hasn't reached ground, keep moving the height down
		if(movingDown && temp.height > 0)
		{
			temp.height -= 0.1;
			this.height -= 0.1;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);	
				
				soundTime++;
			}
		}
		//If hit the ground
		else if(movingDown && temp.height <= 0 && waitTime == 0)
		{
			waitTime++;
			temp.height = 0;
			temp.baseCorrect = 0;
			this.height = 0;
			movingDown = false;
			
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			
			soundTime = 0;
		}
		//After waiting for player, begin moving up
		else if(!movingDown && !movingUp && waitTime > 250)
		{
			movingUp = true;
			waitTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer * 2);
		}
		
		//While not at top, keep moving up
		if(movingUp && temp.height < upHeight)
		{
			temp.height += 0.1;
			this.height += 0.1;
			
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer * 2);
				
				soundTime++;
			}
		}
		
		//If reached the top
		if(movingUp && temp.height >= upHeight)
		{
			temp.height = (int)upHeight;
			this.height = (int)upHeight;
			movingUp = false;
			activated = false;
			
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer * 2);
			
			//If special ID, keep moving up and down
			if(itemActivationID == 2112)
			{
				activated = true;
				waitTime = 0;
			}
		}
		
		//Add to wait time each loop
		if(waitTime <= 250 && waitTime > 0
				|| itemActivationID > 0 && itemActivationID != 2112 && waitTime > 0)
		{
			waitTime++;
		}
		
		//If elevator is supposed to stay down, keep it down
		//and its not the special ID. Otherwise make it go up
		//immediately
		if(itemActivationID > 0 && itemActivationID != 2112)
		{
			if(upHeight == 0)
			{
				waitTime = 1;
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
