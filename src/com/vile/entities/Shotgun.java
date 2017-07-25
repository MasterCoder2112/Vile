package com.vile.entities;

import com.vile.Game;
import com.vile.SoundController;
import com.vile.input.Controller;

/**
 * Title: Shotgun
 * Date Created: 10/20/2016
 * @author Alexander Byrd
 * 
 * Description:
 * A type of weapon that creates a new weapon of ID 0, and has a unique
 * updateValues method that updates its phases of firing and such when
 * the weapon is being fired.
 */
public class Shotgun extends Weapon implements WeaponInterface
{
   /**
    * Creates a new Weapon of ID 0
    */
	public Shotgun() 
	{
		super(1);
	}
	
   /**
    * Update weaponShootTime, and depending on its value update its stage
    * in the firing process of the weapon.
    */
	public void updateValues()
	{
		//IF being fired
		if(weaponShootTime != 0)
		{
			//If weapon already fired, set phase to dormant phase
			if(weaponShootTime > 25)
			{
				weaponPhase = 0;
			}
			
			//Fire weapon and play sound immediately after being fired
			if(weaponShootTime == 1)
			{
				//If unlimited ammo is not on
				if(!Controller.unlimitedAmmoOn)
				{
					ammo--;
				}
				
				weaponPhase = 1;
				
				//Create the bullet
				Bullet bullet = new Bullet(damage, 0.01, Player.x,
						-(Player.y * 0.085), Player.z, weaponID, Player.rotation);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation + 0.06);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation - 0.06);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation + 0.04);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation - 0.04);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation + 0.08);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation - 0.08);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation + 0.015);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				bullet = new Bullet(damage, 0.01, Player.x,
						Player.y, Player.z, weaponID,
						Player.rotation - 0.015);
				
			   /*
			    * Instead of rendering the bullet and all that, just check
			    * its movement instantaneously in small increments to make 
			    * it look like it hits the enemy instantaneously and also
			    * makes it more precise.
			    */
				while(bullet.move())
				{
					//Do nothing, just call the move method
				}
				
				SoundController.shoot.playAudioFile();
			}
			else if(weaponShootTime == 6)
			{
				weaponPhase = 2;
			}
			else if(weaponShootTime == 11)
			{
				weaponPhase = 3;
			}
			else if(weaponShootTime == 16)
			{
				weaponPhase = 3;
			}
			else if(weaponShootTime == 21)
			{
				weaponPhase = 3;
			}
			
			weaponShootTime++;
			
			//Set weapon to fire-able again after its cool down time has
			//been reached
			if(weaponShootTime >= cooldownTime)
			{
				weaponShootTime = 0;
			}
		}
		//IF not being fired, it is in a dormant phase.
		else
		{
			weaponPhase = 0;
		}
	}

}
