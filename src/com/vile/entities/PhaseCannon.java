package com.vile.entities;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.input.Controller;

/**
 * Title: PhaseCannon
 * Date Created: 10/20/2016
 * @author Alexander Byrd
 * 
 * Description:
 * A type of weapon that creates a new weapon of ID 1, and has a unique
 * updateValues method that updates its phases of firing and such when
 * the weapon is being fired.
 */
public class PhaseCannon extends Weapon implements WeaponInterface
{
   /**
    * Creates new Weapon of ID 1
    */
	public PhaseCannon() 
	{
		super(2);
	}

   /**
    * Update weaponShootTime, and depending on its value update its stage
    * in the firing process of the weapon.
    */
	public void updateValues()
	{
		//If weapon is in the process of being fired
		if(weaponShootTime != 0)
		{
			//Play firing sound from the beginning of being fired
			if(weaponShootTime == 1)
			{
				SoundController.phaseShot.playAudioFile();
			}
			
		   /*
		    * Update weapon phase as time in ticks goes on
		    */
			if(weaponShootTime > 45)
			{
				weaponPhase = 0;
			}
			else if(weaponShootTime > 30)
			{
				weaponPhase = 3;
			}
			else if(weaponShootTime > 15)
			{
				weaponPhase = 2;
			}
			else
			{
				weaponPhase = 1;
			}
			
			//When sound ends, fire a bullet that corresponds to the
			//weapon type
			if(weaponShootTime == 46)
			{
				//If unlimited ammo is not on
				if(!Controller.unlimitedAmmoOn)
				{
					ammo--;
				}
				
				weaponPhase = 0;
				
				Game.addBullet(damage, weaponID, 0.3, 
						Player.rotation);
			}
			
			weaponShootTime++;
			
			//If weapon has reached the end of its cooldownTime, say that
			//the weapon can once again be shot
			if(weaponShootTime >= cooldownTime)
			{
				weaponShootTime = 0;
			}
		}
		//If not being fired, set phase to its dormant phase
		else
		{
			weaponPhase = 0;
		}
	}
}
