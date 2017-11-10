package com.vile.entities;

import com.vile.Game;
import com.vile.graphics.Render3D;

/**
 * Title: HitSprite
 * @author Alexander Byrd
 * Date Created: 9/11/2017 (Not ominous at all)
 * 
 * Description:
 * Is a class that keeps track of the phase time and position of a 
 * hit sprite. Basically when a projectile hits a wall or other 
 * object this will display some textures to show it has hit a 
 * certain thing. The textures shown depend on the ID of the sprite
 * which is determined when the sprite is created.
 */
public class HitSprite 
{
	public int ID = 0;
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public int phaseTime = 0;
	private int spriteTime = 20;
	
	public HitSprite(double x, double z, double y, int ID) 
	{
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.z = z;
		
		//Normal bullets stay longer
		if(ID <= 1)
		{
			spriteTime = 400;
		}
		else if(ID == 5)
		{
			spriteTime = 10;
		}
		
		Game.sprites.add(this);
	}
	
   /**
    * Ticks the sprites phaseTime so the animation looks realistic and
    * also gets rid of the animation after the given amount of time
    * is over.
    */
	public void tick()
	{
		phaseTime++;
		
		//Remove bullets after the animation is over
		if(phaseTime > spriteTime * Render3D.fpsCheck)
		{
			Game.sprites.remove(this);
		}
	}
}
