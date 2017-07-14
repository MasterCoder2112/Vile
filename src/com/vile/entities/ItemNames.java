package com.vile.entities;

/**
 * @title ItemNames
 * @author Alexander James Byrd
 * Date created: 4/20/2017
 * 
 * Description:
 * Meant to put all the items used in the game into a Name form to easier
 * understand and manage in the code.
 */
public enum ItemNames 
{	
	AIR ("Air", 0, 0),
	MEGAHEALTH ("Megahealth", 200, 1),
	HEALTHPACK ("HealthPack", 10, 2),
	SHELLS ("Shells", 4, 3),
	REDKEY ("Red Keycard", 0, 4),
	BLUEKEY ("Blue Keycard", 0, 5),
	GREENKEY ("Green Keycard", 0, 6),
	YELLOWKEY ("Yellow Keycard", 0, 7),
	SPAWN ("Player Spawn", 0, 8),
	ENDBUTTON ("End Button", 0, 9),
	ELEVATOR ("Elevator", 0, 10),
	DOOR ("Door (Normal)", 0, 11),
	REDDOOR ("Door (Red)", 0, 12),
	BLUEDOOR ("Door (Blue)", 0, 13),
	GREENDOOR ("Door (Green)", 0, 14),
	YELLOWDOOR ("Door (Yellow)", 0, 15),
	SECRET ("Secret", 0, 20),
	SHOTGUN ("Shotgun", 6, 21),
	TOXICWASTE ("Toxic Waste", 2, 22),
	LAVA ("Lava", 5, 23),
	SKULLOFRES ("Skull of Resurrection", 0, 24),
	BIOSUIT ("Environment Suit", 0, 25),
	GOBLET ("Goblet of Immortality", 0, 26),
	ADRENALINE ("Adrenaline", 1, 27),
	VISIONGLASSES ("Glasses of Vision", 0, 28),
	TORCH ("Torch", 0, 29),
	LAMP ("Lamp", 0, 30),
	TREE ("Tree", 0, 31),
	CANISTER ("Explosive Canister", 0, 32),
	CHAINMEAL ("Chainmeal Armor", 50, 33),
	COMBAT ("Combat Armor", 100, 34),
	ARGENT ("Argent Armor", 200, 35),
	SHARD ("Armor Shard", 1, 36),
	VIAL ("Health Vial", 1, 37),
	UPGRADE ("Weapon Upgrade Point", 0, 38),
	HOLYWATER ("Holy Water", 0, 39),
	DECIETSCEPTER ("Scepter of Deciet", 0, 40),
	INVISEMERALD ("Invisibility Emerald", 0, 41),
	TABLE ("Table", 0, 42),
	LAMPTABLE ("Lamp Table", 0, 43),
	CORPSE ("Default corpse", 0, 44),
	SHELLBOX ("Box of Shells", 20, 47),
	ENDLINEDEF ("End line def", 0, 48),
	PHASECANNON ("Phase Cannon", 3, 49),
	SMALLCHARGE ("Small Charge Pack", 3, 50),
	LARGECHARGE ("Large Charge Pack", 7, 51),
	RADAR ("Radar Dish", 0, 52),
	BUTTON ("Button", 0, 53),
	ENEMYSPAWN ("Enemy Spawnpoint", 0, 54),
	PISTOL ("Pistol", 10, 55),
	BULLETS ("Bullet Clip", 5, 56),
	BOXOFBULLETS ("Box of Bullets", 35, 57),
	ROCKETLAUNCHER ("Rocket Launcher", 5, 60),
	ROCKETS ("Rockets", 2, 61),
	ROCKETCRATE ("Rocket Crate", 10, 62),
	EXPLOSION ("Explosion", 10, 63),
	ACTIVATEEXP ("Activate Explosion", 10, 64),
	WALLBEGONE ("Erasable wall", 10, 65);
	
	public String itemName = "";
	public int itemID = 0;
	public int value = 0;
	
   /**
    * Sets up each enum with a name, value, and ID
    * @param name
    * @param value
    * @param ID
    */
	ItemNames(String name, int value, int ID)
	{
		this.itemName = name;
		this.itemID = ID;
		this.value = value;
	}
	
   /**
    * Returns value of item
    * @return
    */
	public int getValue()
	{
		return value;
	}
	
   /**
    * Returns ID number
    * @return
    */
	public int getID()
	{
		return itemID;
	}
	
   /**
    * Returns name of item
    * @return
    */
	public String getName()
	{
		return itemName;
	}
}
