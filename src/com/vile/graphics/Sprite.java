package com.vile.graphics;

/**
 * Title: Sprite
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 * 
 * Description:
 * Keeps track of the x, y, and z positions of each sprite.
 */
public class Sprite 
{
	public double z;
	public double x;
	public double y;
	
	public Sprite(double x, double y, double z) 
	{
		this.x = x;
		this.z = z;
		this.y = y;
	}

}
