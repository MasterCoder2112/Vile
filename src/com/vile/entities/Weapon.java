package com.vile.entities;

import java.util.ArrayList;

/**
 * Title: Weapon
 * Date Created: October 20, 2016
 * @author Alexander Byrd
 * 
 * Description:
 * Creates a weapon with all the attributes and flags of a weapon, and
 * also includes functions to start a shot for a weapon and to reload
 * the weapon and to pick up ammo for a weapon.
 */
public abstract class Weapon 
{
	//What type of weapon
	public int weaponID = 0;
	
	//Weapons name. Default is shotgun
	public String name = "Shotgun";
	
	//Damage and ammo it holds
	public int damage = 20;
	public int ammo = 40;
	
	//Holds the cartridges it has
	public ArrayList<Cartridge> cartridges;
	
	//How long it takes to refire the weapon again in ticks
	public int cooldownTime = 25;
	
	//How much ammo it can hold at once
	public int ammoLimit = 40;
	
	//So the gun graphics can be correct for each phase
	public int weaponShootTime = 0;
	public int weaponShootTime2 = 0;
	public int weaponPhase = 0;
	public int weaponPhase2 = 0;
	public int currentWeapon = 0;	
	
	//FLAGS
	//Can the weapon be Equipped
	public boolean canBeEquipped = false;
	//Weapon can be dualWielded
	public boolean dualWield = false;
	
   /**
    * Constructs a new weapon given the ID of the weapon willing to be
    * created.
    * @param weaponID
    */
	public Weapon(int weaponID) 
	{
		this.weaponID = weaponID;
		
		if(weaponID == 0)
		{
			name = "Pistol";
			damage = 10;
			ammo = 50;
			cartridges = new ArrayList<Cartridge>();
			cooldownTime = 25;
			ammoLimit = 100;
		}
		else if(weaponID == 1)
		{
			name = "Shotgun";
			damage = 6;
			ammo = 8;
			cartridges = new ArrayList<Cartridge>();
			cooldownTime = 43;
			ammoLimit = 40;
			canBeEquipped = false;
		}
		else if(weaponID == 2)
		{
			name = "Phase Cannon";
			damage = 120;
			ammo = 5;
			cartridges = new ArrayList<Cartridge>();
			cooldownTime = 60;
			ammoLimit = 20;
		}
		else if(weaponID == 3)
		{
			name = "Rocket Launcher";
			damage = 50;
			ammo = 5;
			cartridges = new ArrayList<Cartridge>();
			cooldownTime = 40;
			ammoLimit = 25;
		}
	}
	
   /**
    * Begin to start firing up the weapon by setting weaponShootTime in
    * motion. To shoot you have to have more than 0 ammo, and the weapon
    * cannot already be in the process of firing. Returns whether it could
    * be shot or not.
    * @return
    */
	public boolean shoot()
	{
		boolean couldBeShot = false;
		
		//Can weapon be dual wielded? If not do this
		if(!dualWield)
		{
			//Only begin the next shot if previous one is done
			if(weaponShootTime == 0 && ammo > 0)
			{
				weaponShootTime++;
				
				couldBeShot = true;
			}
		}
		//If so do this
		else
		{
			//Only begin the next shot if previous one is done
			if(currentWeapon == 0 && weaponShootTime == 0
					&& (weaponShootTime2 > (int)(cooldownTime / 2)
							|| weaponShootTime2 == 0)
					&& ammo > 0)
			{
				weaponShootTime++;
				
				//Switch to second wielded weapon
				currentWeapon = 1;
				
				couldBeShot = true;
			}
			//Begin next shot if dual wielded weapon is done firing
			//And some time has passed since the first weapon shot
			//so that the pistol won't fire a nanosecond after the 
			//first basically making them seem to fire at the same
			//time which isn't cool.
			else if(currentWeapon == 1 && weaponShootTime2 == 0
					&& (weaponShootTime > (int)(cooldownTime / 2)
							|| weaponShootTime == 0)
					&& ammo > 0)
			{
				weaponShootTime2++;
				
				//Switch back to primary weapon
				currentWeapon = 0;
				
				couldBeShot = true;
			}
		}
		
		return couldBeShot;
	}
	
   /**
    * Adds ammo to the weapon from a given pick up with a given value.
    * If the amount fills your current cartridge, then begin filling up
    * the next available cartridge with ammo. After all the cartridges
    * have been filled, you can no longer add ammo to your weapon or
    * its cartridges and false will be returned as the item in question
    * could not be picked up.
    * @param value
    * @return
    */
	public boolean addAmmo(int value)
	{
		//Default amount of ammo you get
		int amount = value;
		
		//The amount you extra you get after your imediate ammo is filled
		int extra = 0;
		
		//If everything is completely full, return false
		if(cartridges.size() == 6 && ammo == ammoLimit)
		{
			Cartridge temp = cartridges.get(0);
			
			if(temp.ammo == ammoLimit)
			{
				return false;
			}
		}
		
		//Add the amount to your ammo
		ammo += amount;
			
		//If your ammo went above the limit of 40
		if(ammo > ammoLimit)
		{
			//Figure out the extra amount
			extra = ammo - ammoLimit;
				
		   /*
		    * If there are not 6 or 0 cartridges, see if the extra amount
		    * of ammo fits in the current cartridge, and if not put the
		    * extra amount into a new cartridge, and make that new 
		    * cartridge the new next cartridge.	
		    */
			if(cartridges.size() < 6 && cartridges.size() != 0)
			{
				Cartridge last = cartridges.get(0);
					
				last.ammo += extra;
				
				if(last.ammo > ammoLimit)
				{
					cartridges.add(0, new Cartridge
							(last.ammo - ammoLimit));
					last.ammo = ammoLimit;
				}
			}
		   /*
		    * If there are 6 cartridges, then if the extra shells do not
		    * fit in this cartridge they are just wasted. If No shells can
		    * be put in at all then just do nothing.
		    */
			else if(cartridges.size() == 6)
			{
				Cartridge last = cartridges.get(0);
				
				if(last.ammo + extra < ammoLimit + extra)
				{
					last.ammo += extra;
					
					if(last.ammo > ammoLimit)
					{
						last.ammo = ammoLimit;
					}
				}
			}
			//If not cartridges, add a new cartridge with the extra amount
			else
			{
				cartridges.add(new Cartridge(extra));
			}
			
			//The current ammo is full
			ammo = ammoLimit;
		}
		
		//If ammo could be added
		return true;
	}
	
   /**
    * If the weapon can be reloaded meaning that either you don't have
    * full ammo at the moment, or you at least have a cartridge with
    * ammo available in it to help refill your current ammo, then 
    * reload your weapon. If you cannot reload your weapon return
    * false.
    * @return
    */
	public boolean reload()
	{
		//If you can't reload, then return false and don't try to reload
		if(ammo == ammoLimit || cartridges.size() == 0)
		{
			return false;
		}
		
	   /*
	    * Get the first cartridge available, find how much ammo
	    * are needed to fully restock the weapons ammo, and 
	    * subtract that from the ammo in the cartridge, then add
	    * to the time since last reload.
	    */
		Cartridge temp = cartridges.get(0);
		int ammoNeeded = ammoLimit - ammo;
		temp.ammo -= ammoNeeded;
		int ammoToAdd = 0;
		
		//If the ammount of ammo needed depleted the cartridge
		if(temp.ammo <= 0)
		{
		   /*
		    * If there are still more cartridges, dispose of the
		    * last cartridge, and start using a new one, and take
		    * the extra ammo needed from the new cartridge being
		    * used. Then set the ammo back to the limit for the weapon.
		    */
			if(cartridges.size() > 1)
			{
				Cartridge temp2 = cartridges.get(1);
				int less = Math.abs(0 - temp.ammo);
				temp2.ammo -= less;
				ammo = ammoLimit;
			}
		   /*
		    * If there is no more cartridges, only add the amount
		    * of ammo the cartridge has to the weapons ammo, and
		    * dispose of the cartridge still.
		    */
			else
			{
				ammoToAdd = ammoNeeded - Math.abs(0 - temp.ammo);
				ammo += ammoToAdd;
			}
			
			//Disposes of the cartridge
			cartridges.remove(0);
		}
		//If ammo in cartridge don't reach or go below zero
		else
		{
			ammo = ammoLimit;
		}
		
		//It could be reloaded
		return true;
	}
}
