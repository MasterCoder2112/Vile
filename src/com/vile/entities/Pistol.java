package com.vile.entities;

import com.vile.Game;
import com.vile.SoundController;
import com.vile.input.Controller;

/**
 * Title: Pistol
 * @author Alexander Byrd
 * Date Created: 11/26/2016
 * 
 * Description:
 * Is a weapon with unique values and rates of fire
 *
 */
public class Pistol extends Weapon implements WeaponInterface
{
   /**
	* Creates a new Weapon of ID 2
	*/
	public Pistol() 
	{
		super(0);
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
			if(weaponShootTime > 24)
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
					//System.out.println(bullet.y);
					//Do nothing, just call the move method
				}
				
				SoundController.pistol.playAudioFile();
			}
			else if(weaponShootTime == 5)
			{
				weaponPhase = 2;
			}
			else if(weaponShootTime == 10)
			{
				weaponPhase = 3;
			}
			else if(weaponShootTime == 22)
			{
				weaponPhase = 0;
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
		
		//IF second weapon is being fired
		if(weaponShootTime2 != 0)
		{
			//If weapon already fired, set phase to dormant phase
			if(weaponShootTime2 > 24)
			{
				weaponPhase2 = 0;
			}
			
			//Fire weapon and play sound immediately after being fired
			if(weaponShootTime2 == 1)
			{
				//If unlimited ammo is not on
				if(!Controller.unlimitedAmmoOn)
				{
					ammo--;
				}
				
				weaponPhase2 = 1;
				
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
				
				SoundController.pistol.playAudioFile();
			}
			else if(weaponShootTime2 == 5)
			{
				weaponPhase2 = 2;
			}
			else if(weaponShootTime2 == 10)
			{
				weaponPhase2 = 3;
			}
			else if(weaponShootTime2 == 22)
			{
				weaponPhase2 = 0;
			}
			
			weaponShootTime2++;
			
			//Set weapon to fire-able again after its cool down time has
			//been reached
			if(weaponShootTime2 >= cooldownTime)
			{
				weaponShootTime2 = 0;
			}
		}
		//IF not being fired, it is in a dormant phase.
		else
		{
			weaponPhase2 = 0;
		}
	}

}
