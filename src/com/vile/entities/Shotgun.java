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
				
				//Adds spray shot to shotgun
				Game.addBullet(damage, weaponID, 0.3,
						Player.rotation);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation + 0.06);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation - 0.06);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation + 0.04);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation - 0.04);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation + 0.08);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation - 0.08);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation + 0.015);
				
				Game.addBullet(damage, weaponID,
						0.3, Player.rotation - 0.015);
				
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
