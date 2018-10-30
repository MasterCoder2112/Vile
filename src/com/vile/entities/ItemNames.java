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
	HEALTHPACK ("HealthPack", 15, 2),
	SHELLS ("Shells", 4, 3),
	REDKEY ("Red Keycard", 0, 4),
	BLUEKEY ("Blue Keycard", 0, 5),
	GREENKEY ("Green Keycard", 0, 6),
	YELLOWKEY ("Yellow Keycard", 0, 7),
	SPAWN ("Player Spawn", 0, 8),
	BREAKABLEWALL ("Breakable Wall", 0, 9),
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
	SHARD ("Armor Shard", 3, 36),
	VIAL ("Health Vial", 3, 37),
	UPGRADE ("Weapon Upgrade Point", 0, 38),
	HOLYWATER ("Holy Water", 0, 39),
	DECIETSCEPTER ("Scepter of Deciet", 0, 40),
	INVISEMERALD ("Invisibility Emerald", 0, 41),
	TABLE ("Table", 0, 42),
	LAMPTABLE ("Lamp Table", 0, 43),
	CORPSE ("Default corpse", 0, 44),
	SHELLBOX ("Box of Shells", 20, 47),
	LINEDEF ("Line def", 0, 48),
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
	WALLBEGONE ("Erasable wall", 10, 65),
	TELEPORTERENTER("Teleporter enterance", 0, 66),
	TELEPORTEREXIT("Teleporter exit", 0, 67),
	AUDIOQUEUE("Audio Queue", 0, 68),
	BONEPILE("Bone Pile", 0, 69),
	WATER("Water", 0, 70),
	DARKBOOK("Dark Spellbook", 0, 71),
	TURRET("Turret", 0, 72),
	MARINE1("Marine 1", 0, 73),
	MARINE2("Marine 2", 0, 74),
	MARINE3("Marine 3", 0, 75),
	MARINE4("Marine 4", 0, 76),
	MARINE5("Marine 5", 0, 77),
	TECHBARREL("Technical Barrel", 0, 78),
	TECHPILLAR1("Technical Pillar 1", 0, 79),
	TECHPILLAR2("Technical Pillar 2", 0, 80),
	TECHPILLAR3("Technical Pillar 3", 0, 81),
	TOILET("Toilet", 0, 82),
	TRASH1("Trash Can 1", 0, 83),
	TRASH2("Trash Can 2", 0, 84),
	TRASH3("Trash Can 3", 0, 85),
	TREEALIVE("Tree Alive", 0, 86),
	HANGMAN("Hanging Magistrate", 0, 87),
	STALAG1("Stalagtite 1", 0, 88),
	STALAG2("Stalagtite 2", 0, 89),
	ROCK1("Rock 1", 0, 90),
	ROCK2("Rock 2", 0, 91),
	ROCK3("Rock 3", 0, 92),
	GOREPILE1("Gore Pile 1", 0, 93),
	GOREPILE2("Gore Pile 2", 0, 94),
	REDPILLAR("Red Pillar", 0, 95),
	PUSTULE("Blood Pustule", 0, 96),
	REDBANNER("Red Banner", 0, 97),
	EYEPILE("Eye Pile", 0, 98),
	BEER("Beer", 1, 99),
	BIBLE("Bible", 0, 100),
	BLUEBANNER("Blue Banner", 0, 101),
	BLUEPILLAR("Blue Pillar", 0, 102),
	BONE("Bone", 0, 103),
	BURGER("Burger", 2, 104),
	BURNINGMAN("Burning Corpse", 0, 105),
	BUSH1("Bush 1", 0, 106),
	BUSH2("Bush 2", 0, 107),
	TECHLAMP3("Technical Lamp 3", 0, 108),
	CEILINGLAMP("Ceiling Lamp", 0, 109),
	CHAIR("Chair", 0, 110),
	PLACEHOLDER("Coming Soon", 0, 111),
	CORPSEPILE("Corpse Pile", 0, 112),
	TECHCANISTER("Technical Canister", 0, 113),
	CANDEL1("Candelabra 1", 0, 114),
	CANDEL2("Candelabra 2", 0, 115),
	CANDEL3("Candelabra 3", 0, 116),
	CANDEL4("Candelabra 4", 0, 117),
	BLOODTHORNS("Blood Thorns", 0, 118),
	GREENPILLAR("Green Pillar", 0, 119),
	TECHLAMP1("Technical Lamp 1", 0, 120),
	TECHLAMP2("Technical Lamp 2", 0, 121),
	STRIPCORPSE("Stripped Corpse", 0, 122);
	
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
