package com.vile.entities;

import com.vile.Display;
import com.vile.SoundController;

/**
 * @Title  Bullet
 * @author Alex Byrd
 * Date Updated: 5/10/2017
 * 
 * Description:
 * Creates a bullet object that extends the projectile class and is used
 * to move a bullet in accordance with the direction the player is looking
 * and speed of the bullet. 
 *
 */
public class Bullet extends Projectile
{
	private double upRotation;
	
   /**
    * Creates the bullet Projectile object.
    * @param damage
    * @param speed
    * @param x
    * @param y
    * @param z
    * @param ID
    * @param rotation
    */
	public Bullet(int damage, double speed, double x, double y,
			double z, int ID, double rotation) 
	{
		super(damage, speed, x, y,
				z, ID);
		
	   /*
	    * Corrects Player.rotation so that the bullet is centered on the
	    * screen to the player. Don't know why its so specific but it is
	    */
		double correction = 44.768;
		
		//Sets rotation so the bullet will continue to move in
		//a certain direction
		rotation = rotation - correction;
		
		//Calculates changes needed in x and z values for this rotation
		xa = 
				((Math.cos(rotation)) 
						+ (Math.sin(rotation))) 
						* speed;
		za = 
				((Math.cos(rotation)) 
						- (Math.sin(rotation))) 
						* speed;
		
	   /*
	    * Correct bullet for upRotation as best as possible, though it
	    * cannot be perfect, try your best to make it perfect.
	    */
		double angle = 1.105 - Player.upRotate + 0.1;
		
		//Default angle correction
		double angleChanger = 3;
		
	   /*
	    * For some reason different screen sizes change everything...
	    * haven't yet figured this out, but this corrects bullets for
	    * a full screen. 
	    */
		if(Display.graphicsSelection >= 4)
		{
			angleChanger = 0.5;
			
			if(Player.upRotate < 0.44)
			{
				angleChanger = 5.25;
			}
			else if(Player.upRotate < 0.51)
			{
				angleChanger = 4.75;
			}
			else if(Player.upRotate < 0.57)
			{
				angleChanger = 4.25;
			}
			else if(Player.upRotate < 0.62)
			{
				angleChanger = 3.75;
			}
			else if(Player.upRotate < 0.67)
			{
				angleChanger = 3.25;
			}
			else if(Player.upRotate < 0.72)
			{
				angleChanger = 2.75;
			}
			else if(Player.upRotate < 0.77)
			{
				angleChanger = 2.5;
			}
			else if(Player.upRotate < 0.82)
			{
				angleChanger = 2.25;
			}
			else if(Player.upRotate < 0.87)
			{
				angleChanger = 2;
			}
			else if(Player.upRotate < 0.91)
			{
				angleChanger = 1.75;
			}
			else if(Player.upRotate < 0.95)
			{
				angleChanger = 1.5;
			}
			else if(Player.upRotate < 1)
			{
				angleChanger = 1.25;
			}
			else if(Player.upRotate < 1.05)
			{
				angleChanger = 1;
			}
			else if(Player.upRotate > 1.2)
			{
				angleChanger = 3;
			}
		}
		//Corrects bullets for a smaller screen
		else
		{
			if(Player.upRotate < 0.44)
			{
				angleChanger = 3 + 2.25;
			}
			else if(Player.upRotate < 0.57)
			{
				angleChanger = 3 + 1.75;
			}
			else if(Player.upRotate < 0.67)
			{
				angleChanger = 3 + 1.25;
			}
			else if(Player.upRotate < 0.77)
			{
				angleChanger = 3 + 1;
			}
			else if(Player.upRotate < 0.87)
			{
				angleChanger = 3 + 0.5;
			}
		}
		
	   /*
	    * Sets bullet upRotation so that the bullet will continue the
	    * same direction upward.
	    */
		upRotation = -(speed * Math.tan(angle)) * angleChanger;
	}

   /**
    * Moves the bullet depending on where the player was looking when
    * the bullet was shot, and the speed of the bullet.
    */
	public void move()
	{				
	   /*
	    * Adds to bullets y position in such a way that it almost
	    * seems that the bullet is going straight from your
	    * crosshair, but for some reason I cannot get it exact.
	    */
		y += upRotation;

		
		//Checks to make sure bullet can move and move it if it can
		//move and play sound if a teddy bear
		if(isFree(x + xa, z)
				&& isFree(x, z + za))
		{
			x += xa;
			z += za;
			
			//If teddy bear, continue playing wee sound
			if(this.ID == 3)
			{
				if(!SoundController.rocketFly.isStillActive())
				{
					SoundController.rocketFly.playAudioFile();
				}
			}
		}
	}
}
