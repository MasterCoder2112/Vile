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
	public int[] entities = new int[2];
	public double[] rotations = new double[2];
	public int[] itemActIDs = new int[2];
	public boolean[] aboveBlocks = new boolean[2];
	public int wallY = 0;
	public int doorRaiseHeight = 0;
	
	public String audioQueue = "";
	
	//Future Values
	public int brightness = 0;	
	
   /**
    * Sets and stores the values in each value holder.
    * @param height
    * @param wallID
    * @param ID
    */
	public ValueHolder(double height, int wallID,
			double[] rotations, String audioQueue,
			int wallY, int doorRaiseHeight, int[] entities,
			int[] itemActIDs, boolean[] aboveBlocks) 
	{
		this.height = height;
		this.wallID = wallID;
		this.entities = entities;
		this.rotations = rotations;
		this.aboveBlocks = aboveBlocks;
		this.itemActIDs = itemActIDs;
		this.wallY = wallY;
		this.doorRaiseHeight = doorRaiseHeight;
		this.audioQueue = audioQueue;
	}

}
