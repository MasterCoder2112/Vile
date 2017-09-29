package com.vile.levelGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.vile.entities.Enemy;
import com.vile.entities.Item;
import com.vile.graphics.Render;
import com.vile.graphics.Sprite;

/**
 * Title: Block
 * @author Alex Byrd
 * Date Updated: 5/11/2017
 * 
 * Description:
 * Each Block is used as a way to construct the walls in the map. A single
 * block is rendered in the game as 4 walls surround an empty space in the
 * center which shouldn't be seen unless the player jumps high enough to
 * see it. If the block isSolid this is what is seen, but if it is not
 * solid (Defaultly all blocks are not solid) then it is just air, and the
 * block cannot be seen by the player. Each block has a given height and
 * ID (used for texturing purposes, and action purposes) and y value.
 * 
 * Each block is also labeled as seeThrough if you can see other blocks
 * through it, under it, or above it.
 * 
 * wallPhase is used for animated walls as their textures will go through
 * phases to seem like the walls are animated.
 * 
 * Implements Comparable method so that when sorted in Render3D it can
 * sort Block objects by using the quickSort to sort the blocks in order
 * of height.
 * 
 * Some blocks can be broken so they also have a health variable that
 * can be altered if shot. 
 * 
 * Also each block keeps track of any enemies and items on that block
 * at the time.
 */
public class Block implements Comparable
{
	public boolean isSolid = false;
	public boolean seeThrough = false;
	
   /*
    * Only is used for when a player walks through a doorway and his/her
    * collision detection must be altered correctly to work within a
    * doorway or elevator. 
    */
	public boolean isaDoor = false;
	
	public int wallID = 0;
	public int wallPhase = 0;
	
	public double height = 12;
	
	//Used so bullets hit the walls graphics correctly
	public double hCorrect = 0;
	
	public double y = 0;
	public int x = 0;
	public int z = 0;
	
	//For now just special glass walls can be broken
	public int health = 60;
	
	//The item that the wall has on it
	public Item wallItem = null;
	
	//Stores all the enemies on the block at the time
	public ArrayList<Enemy> enemiesOnBlock = new ArrayList<Enemy>();
	
   /*
    * Not technically a "Entity" but an item that has an effect on the
    * block or the Player, such as a lava block or a door item or
    * something.
    */
	public Item wallEntity = null;
	
	//Image for the game to render
	public Render wallImage = null;
	
   /**
    * Constructs block
    * @param h
    */
	public Block(double h, int wallID, double y, int x, int z)
	{
		height = h;
		this.wallID = wallID;
		this.y = y;
		this.x = x;
		this.z = z;
		
	   /*
	    * If block is glass, it is labeled as a see through block
	    */
		if(wallID == 4)
		{
			seeThrough = true;
		}
		
	   /*
	    * Sets whether the wall is solid or not directly related to the
	    * ID of the wall.
	    */
		if(wallID == 0)
		{
			isSolid = false;
		}
		else
		{
			isSolid = true;
		}
	}

	@Override
   /**
	* Compares two blocks together depending on their height.
	* Compares in terms of descending height so that higher walls are
	* rendered before shorter blocks.
	*/
	public int compareTo(Object block) 
	{
		//Gets Integer height of the block being compared
		int comparedInteger = (int)((Block)(block)).height;
		
	   /*
	    * Compares the two blocks being compared, and sends back the
	    * result. This particular comparison would cause the list
	    * to be sorted in decending order of height. If you switched
	    * this.height and comparedInteger it would be sorted in
	    * ascending order.
	    */
		return comparedInteger - (int)this.height;
	}
	
   /**
    * Compare both blocks and sort the blocks in terms of descending x
    * values. If the x values are the same, then compare them in
    * descending order in terms of z.
    * 
    * Only is called with the player is looking in the first quadrant
    * of the map.
    */
	public static Comparator<Block> ninetyDegrees 
		= new Comparator<Block>() 
	{
			public int compare(Block b1, Block b2) 
			{			   
			    if(b1.x == b2.x)
			    {
			    	return b2.z - b1.z; 
			    }

			   		/*Descending*/
			    return b2.x-b1.x;
			   
			}
	};
	
   /**
    * Compare both blocks and sort the blocks in terms of descending x
    * values. If the x values are the same, then compare them in
    * ascending order in terms of z.
    * 
    * Only is called with the player is looking in the second quadrant
    * of the map.
    */
	public static Comparator<Block> oneEightyDegrees 
		= new Comparator<Block>() 
	{
			public int compare(Block b1, Block b2) 
			{			   
			    if(b1.x == b2.x)
			    {
			    	return b1.z - b2.z; 
			    }

			   	/*Descending*/
			   	return b2.x-b1.x;
			   
			}
	};
	
   /**
    * Compare both blocks and sort the blocks in terms of ascending x
    * values. If the x values are the same, then compare them in
    * ascending order in terms of z.
    * 
    * Only is called with the player is looking in the third quadrant
    * of the map.
    */
	public static Comparator<Block> twoSeventyDegrees 
		= new Comparator<Block>() 
	{
			public int compare(Block b1, Block b2) 
			{			   
			    if(b1.x == b2.x)
			    {
			    	return b1.z - b2.z; 
			    }

			   	/*Descending*/
			   	return b1.x-b2.x;
			   
			}
	};
	
   /**
    * Compare both blocks and sort the blocks in terms of ascending x
    * values. If the x values are the same, then compare them in
    * descending order in terms of z.
    * 
    * Only is called with the player is looking in the fourth quadrant
    * of the map.
    */
	public static Comparator<Block> threeSixtyDegrees 
		= new Comparator<Block>() 
	{
			public int compare(Block b1, Block b2) 
			{			   
			    if(b1.x == b2.x)
			    {
			    	return b2.z - b1.z; 
			    }

			   	return b1.x-b2.x;
			   
			}
	};
}
