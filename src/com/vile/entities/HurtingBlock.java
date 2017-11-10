package com.vile.entities;

import com.vile.input.Controller;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Hurting Block
 * @author Alex Byrd
 * Date Updated: 5/10/2017
 * 
 * Description:
 * Hurting block can be either toxic waste, lava, or something new in the
 * future, and this tracks where the item is, and when activated it can
 * hurt the player every so many ticks if the player is on top of the
 * block this item is on.  
 */
public class HurtingBlock extends Item 
{
	//Block position that this item correlates to on map
	public  int blockX;
	public  int blockZ;
	
	//Keeps track of time since last damage of player.
	public static int time = 0;
	
	//Type of hurting block. Toxic waste, lava, or eventually spikes
	public  int blockType = 0;
	
   /**
    * A constructor of a door entity, which also holds the coordinates of
    * the block the door will raise.
    * @param x
    * @param y
    * @param z
    * @param wallX
    * @param wallZ
    */
	public HurtingBlock(double x, double y, double z, int wallX, int wallZ, int type) 
	{
		super(2, x, y, z, 22, 0, 0,"");
		
		//If lava, change the values slightly
		if(type == 1)
		{
			super.itemID = 23;
			
			//Value is really just the damage it deals
			super.itemValue = 5;
		}
		
		blockX = wallX;
		blockZ = wallZ;
		blockType = type;
	}
	
   /**
    * Activates the lava or toxic waste block to hurt the player if the
    * player is on top of it and can be hurt.
    */
	public boolean activate()
	{
		//The block this item correlates to
		Block temp = Level.getBlock(blockX, blockZ);
		
	   /*
	    * If player is not immune to this damage, is alive, and is
	    * not jumping over the block. Also has to be when the tick
	    * counter is reset to 0.
	    */
		if(time == 0 && Player.y <= temp.height + 1
				&& Player.environProtectionTime == 0)
		{
			//Toxic waste
			if(blockType == 0)
			{
				//Only deals damage of 2
				Player.hurtPlayer(2);
			}
			//Lava
			else
			{
				//Deals damage of 5
				Player.hurtPlayer(5);
			}
		}	
		
		return true;
	}
}
