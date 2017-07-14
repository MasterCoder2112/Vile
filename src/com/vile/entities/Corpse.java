package com.vile.entities;

/**
 * Title: Corpse
 * @author Alexander Byrd
 * Date Created: 10/5/2016
 * 
 * Description:
 * This stores all the values needed for a corpse entity whose
 * soul purpose is for aesthetic feel, or for resurrection by
 * a resurrector enemy.
 *
 */
public class Corpse 
{
	public double x = 0;
	public double z = 0;
	public double y = 0;
	public int phaseTime = 0;
	
	public int time = 0;
	
	//Zero is default meaning wasn't alive, or not an enemy
	public int enemyID = 0;
	
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
	public Corpse(double x, double z, double y, int enemyID) 
	{
		this.x = x;
		this.z = z;
		this.y = y;
		this.enemyID = enemyID;
		
		if(enemyID != 0 && enemyID != 8)
		{
			this.phaseTime = 24;
		}
		
		if(enemyID == 8)
		{
			this.phaseTime = 48;
		}
	}
	
	public void tick()
	{
		time++;
	}

}
