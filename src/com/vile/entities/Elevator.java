package com.vile.entities;

import com.vile.entities.Entity;
import com.vile.SoundController;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

public class Elevator extends Entity 
{
	//Defaultly goes up 3 block heights
	public double upHeight = 60;
	
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
			int itemActID) 
	{
		super(0, 0, 0, 0, 0, x, y, z, 8, 0, itemActID);
		
		elevatorX = wallX;
		elevatorZ = wallZ;
		upHeight = Level.getBlock(elevatorX, elevatorZ).height;
		height = (int)upHeight;
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
		
		//Is see through while it is moving.
		temp.seeThrough = true;
		
		//If just activated, begin moving down
		if(!movingDown && !movingUp && waitTime == 0)
		{
			movingDown = true;
			soundTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer);
		}
		
		//As long as it hasn't reached ground, keep moving the height down
		if(movingDown && temp.height > 0)
		{
			temp.height -= 0.1;
			this.height -= 0.1;
			
			//Only play sound every 10 ticks
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer);	
				
				soundTime++;
			}
		}
		//If hit the ground
		else if(movingDown && temp.height <= 0 && waitTime == 0)
		{
			waitTime++;
			temp.height = 0;
			this.height = 0;
			movingDown = false;
			
			SoundController.lifting.stopAll();
			SoundController.doorEnd.playAudioFile(distanceFromPlayer);
			
			soundTime = 0;
		}
		//After waiting for player, begin moving up
		else if(!movingDown && !movingUp && waitTime > 250)
		{
			movingUp = true;
			waitTime = 0;
			SoundController.doorStart.playAudioFile(distanceFromPlayer);
		}
		
		//While not at top, keep moving up
		if(movingUp && temp.height < upHeight)
		{
			temp.height += 0.1;
			this.height += 0.1;
			
			if(soundTime == 0)
			{
				SoundController.lifting.playAudioFile(distanceFromPlayer);
				
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
			SoundController.doorEnd.playAudioFile(distanceFromPlayer);
			
		   /*
		    * Unless the block was already seeThrough, set it back to not
		    * being see through.
		    */
			if(temp.wallID != 4)
			{
				temp.seeThrough = false;
			}
		}
		
		//Add to wait time each loop
		if(waitTime <= 250 && waitTime > 0)
		{
			waitTime++;
		}
		
		//If elevator is supposed to stay down, keep it down
		if(itemActivationID > 0)
		{
			waitTime = 0;
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
