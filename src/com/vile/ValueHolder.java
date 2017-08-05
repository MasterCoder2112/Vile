package com.vile;

/**
 * Title: valueHolder
 * 
 * @author Alex Byrd 
 * Date Updated: 9/16/2016
 *
 * Purpose:
 * To hold each slot of the map text files values for wallHeight, wallID,
 * and entityID on the block.
 */
public class ValueHolder 
{
	public double height = 12;
	public int wallID    = 1;
	public int entityID  = 0;
	public double rotation = 0;
	public int itemActID = 0;
	
	//Future Values
	public int brightness = 0;
	public int wallY = 0;
	
	
   /**
    * Sets and stores the values in each value holder.
    * @param height
    * @param wallID
    * @param ID
    */
	public ValueHolder(double height, int wallID, int ID,
			double rotation, int itemActID) 
	{
		this.height = height;
		this.wallID = wallID;
		entityID = ID;
		this.rotation = rotation;
		this.itemActID = itemActID;
	}

}
