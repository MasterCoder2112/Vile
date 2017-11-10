package com.vile;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import com.vile.entities.*;
import com.vile.graphics.Render3D;
import com.vile.input.Controller;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.*;

/**
 * Title: Game
 * @author Alex Byrd
 * Date Updated: 5/14/2017
 *
 * Keeps track of all the in game controls (using keyListener), also
 * keeps track of game time using the time variable and adding to it
 * in the tick method. It also sets up the random level generated each
 * time the game is ran with a given size (determined by the launcher).
 * 
 * It also calls the performActions method in controller to perform all  
 * the actions within the game that the player activates using the keys.
 * 
 * Keeps track of the Player and some of the objects in the game. Because
 * of optimization reasons and speed upgrades, most objects like enemies
 * and items and such are tracked and updated in the Render3D class since 
 * that class needs to run through all of theme anyway to render them to
 * the screen. So instead of running through all of the enemies and items
 * twice, it just does it once in there. 
 */
public class Game implements Runnable
{
	//Time games been running in ticks
	public static int time;
	
	//Handles all controls and player movements
	public Controller controls;
	
   /*
    * A ValueHolder is an Object which holds multiple values for a new map
    * to read in such as the block type, block height, item on that block,
    * and eventually things like brightness of that block in the map.
    */
	public static ValueHolder[][] map;
	
	//The level the game has loaded up
	public static Level level;
	
	//Size of level
	public static int levelSize;
	
	//Skill level
	public static int skillMode = 2;
	
	//Map number default is 1. Unless set otherwise
	public static int mapNum   = 0;
	
	//Map floor and ceiling ID's
	public static int mapFloor = 0;
	public static int mapCeiling = 0;
	
	//Map name and map audio name
	public static String mapName = "";
	public static String mapAudio = "";
	
	//Keeps track of enemies in the map, secrets in the map, and what
	//secrets have been found.
	public static int secretsInMap = 0;
	public static int enemiesInMap = 0;
	public static int secretsFound = 0;
	
	//Keeps track of all different types of items and entities in the map
	public static ArrayList<Enemy> enemies    
				= new ArrayList<Enemy>();
	public static ArrayList<Enemy> bosses    
				= new ArrayList<Enemy>();
	public static ArrayList<Item> items 
				= new ArrayList<Item>();
	public static ArrayList<Bullet> bullets   
				= new ArrayList<Bullet>();
	public static ArrayList<EnemyFire> enemyProjectiles  
				= new ArrayList<EnemyFire>();
	public static ArrayList<Button> buttons   
				= new ArrayList<Button>();
	public static ArrayList<Door>  doors      
				= new ArrayList<Door>();
	public static ArrayList<Elevator> elevators 
				=  new ArrayList<Elevator>();
	public static ArrayList<HurtingBlock> hurtingBlocks 
				= new ArrayList<HurtingBlock>();
	public static ArrayList<Corpse> corpses
				= new ArrayList<Corpse>();
	public static ArrayList<Explosion> explosions
				= new ArrayList<Explosion>();
	public static ArrayList<ExplosiveCanister> canisters
				= new ArrayList<ExplosiveCanister>();
	public static ArrayList<Item> solidItems
				= new ArrayList<Item>();
	public static ArrayList<Item> activatable
				= new ArrayList<Item>();
	public static ArrayList<Item> teleporters
	 			= new ArrayList<Item>();
	public static ArrayList<HitSprite> sprites 
				= new ArrayList<HitSprite>();
	
	//Keeps track of all controls, and whether they are pressed or not
	private static boolean key[];
	
	//Calculated Size of map to render
	private int calculatedSize;
	
	//Holds the display class you're using
	public Display display;
	
	/* All Player Actions ***********************************************/
	public static boolean foward;
	public static boolean back;
	public static boolean left;
	public static boolean right;
	public static boolean turnLeft;
	public static boolean turnRight;
	public static boolean turnUp;
	public static boolean turnDown;
	public static boolean shoot;
	public static boolean pause;
	public static boolean run;
	public static boolean crouch;
	public static boolean jump;
	public static boolean fpsShow;
	public static boolean reloading;
	public static boolean noClip;
	public static boolean superSpeed;
	public static boolean fly;
	public static boolean godMode;
	public static boolean restock;
	public static boolean use;
	public static boolean weaponSlot0;
	public static boolean weaponSlot1;
	public static boolean weaponSlot2;
	public static boolean weaponSlot3;
	public static boolean unlimAmmo;
	/* End Game Actions ***********************************************/
	
   /**
    * Sets up the level and the Controller object. The level size is
    * determined by the Display.levelSize variable which is determined
    * by the  launcher. Also adds the initial enemy to the game and 
    * adds various item types to the map depending on the map size.
    */
	public Game(Display display, boolean newStartMap,
			String newMapName)
	{		
		//Set the games display object to the display sent in
		this.display = display;
		
		//Resets all the lists
		resetLists();
		
		if(!FPSLauncher.loadingGame)
		{
			//If a survival map
			if(FPSLauncher.gameMode == 1)
			{
				setUpSurvival();
			}
			else
			{
				//Load either the first map or custom map if that is chosen
				mapNum = 0;
				loadNextMap(newStartMap, newMapName);
			}
		}
		else
		{
			loadGame();
		}

		//Set up the controller to control player movements and actions
		controls = new Controller();
	}
	
   /**
    * Ticks each time the game renders, therefore keeping track of game
    * time, and all the key events that happen within the game.
    * @param key2
    */
	public void tick(boolean key2[])
	{
		//Keeps track of how many ticks the game has gone on for
		time++;
		
		//Just in case you go above a billion ticks
		if(time > 1000000000)
		{
			time = 0;
		}
		
		//This was to fix some bug way back, but I can't remember what
		//it was so... this is just here.
		key = key2;
		
		//Keeps track of what keys switch what booleans
		foward      = key[KeyEvent.VK_W];
		back        = key[KeyEvent.VK_S];
		left        = key[KeyEvent.VK_A];
		right       = key[KeyEvent.VK_D];
		turnLeft    = key[KeyEvent.VK_LEFT];
		turnRight   = key[KeyEvent.VK_RIGHT];
		pause       = key[KeyEvent.VK_ESCAPE];
		run         = key[KeyEvent.VK_SHIFT];
		crouch      = key[KeyEvent.VK_C];
		jump        = key[KeyEvent.VK_SPACE];
		fpsShow     = key[KeyEvent.VK_F];
		turnUp      = key[KeyEvent.VK_UP];
		turnDown    = key[KeyEvent.VK_DOWN];
		reloading   = key[KeyEvent.VK_R];
		noClip      = key[KeyEvent.VK_N];
		superSpeed  = key[KeyEvent.VK_P];
		fly         = key[KeyEvent.VK_O];
		godMode     = key[KeyEvent.VK_G];
		shoot       = key[KeyEvent.VK_V];
		restock     = key[KeyEvent.VK_L];
		use         = key[KeyEvent.VK_E];
		weaponSlot0 = key[KeyEvent.VK_1];
		weaponSlot1 = key[KeyEvent.VK_2];
		weaponSlot2 = key[KeyEvent.VK_3];
		weaponSlot3 = key[KeyEvent.VK_4];
		unlimAmmo   = key[KeyEvent.VK_K];
		
		//Sort enemies according to their distance to you but only if
		//not in survival
		if(FPSLauncher.gameMode == 1)
		{
			Collections.sort(enemies);
		}
		
		//Always request focus
		display.requestFocus();
		
	   /*
	    * Keeps player within the first 2 PI of rotation so that the
	    * Players rotation in respect to the enemies is kept in tact.
	    */
		if(Player.rotation >= (Math.PI * 2))
		{
			Player.rotation -= (Math.PI * 2);
		}
		else if(Player.rotation < 0)
		{
			Player.rotation = (Math.PI * 2) + Player.rotation;
		}

	   /*
	    * If player just drowned in tears, then determine 
	    * whether he/she can be made happy again, and if he/she can't
	    * then play the player drowning in tears sound
	    * and set player status to not alive. 
	    * 
	    * Otherwise reset the player with 100 happiness, but no
	    * positivity.
	    */
		if (Player.health <= 0 && Player.alive) 
		{
			if(Player.resurrections == 0)
			{
				//Player is no longer alive
				Player.alive = false;

				SoundController.playerDeath.playAudioFile(0);
				
				//If survival mode, see if its a new max number of kills
				//or not and if it is then save the new max.
				if(FPSLauncher.gameMode == 1)
				{
					if(Display.kills > Player.maxKills)
					{
						Player.maxKills = Display.kills;
					}
				}
			}
			else
			{
				//Get rid of happiness restorer item the player has
				Player.resurrections--;
				
				//Display that the player was restored to full happiness
				Display.messages.add(new PopUp("RESURRECTED WITH SKULL!"));
				
				//Reset values, and give brief immortality
				Player.health = 100;
				Player.armor  = 0;
				Player.immortality = 100;
			}
		}
		
		//If players health is greater than the max health the player
		//can have for some reason, set health to max health
		if(Player.health > Player.maxHealth)
		{
			Player.health = Player.maxHealth;
		}

		// Keeps playing music audio as long as game is running
		display.playAudio(mapAudio);
		
	   /*
	    * Check all buttons to see if any are activated, and if they
	    * are perform the desired action.
	    */
		for(int i = 0; i < buttons.size(); i++)
		{
			Button temp = buttons.get(i);
			
			//If an end button, load the next map
			if(temp.itemActivationID == 0
					&& temp.activated)
			{
				mapNum++;
				loadNextMap(false, "");
			}
			//If a normal button
			else if(temp.itemActivationID > 0 
					&& temp.activated)
			{
				//De-activate the button
				temp.activated = false;
				
				Block button = Level.getBlock((int)temp.xPos, (int)temp.zPos);
				
				//Change the wall texture to show the button has been activated
				button.wallID = 43;
				
				//Search through all the doors
				for(int k = 0; k < Game.doors.size(); k++)
				{
					Door door = Game.doors.get(k);
					
					//If door has the same activation ID as the 
					//button then activate it.
					if(door.itemActivationID 
							== temp.itemActivationID)
					{
						door.activated = true;
					}
				}
				
				//Search through all the elevators
				for(int k = 0; k < Game.elevators.size(); k++)
				{
					Elevator e = Game.elevators.get(k);
					
					//If elevator has the same activation ID as the 
					//button then activate it.
					if(e.itemActivationID 
							== temp.itemActivationID)
					{
						e.activated = true;
					}
				}
				
				//Stores Items to be deleted
				ArrayList<Item> tempItems2 = new ArrayList<Item>();
				
				//Scan all activatable items
				for(int j = 0; j < activatable.size(); j++)
				{
					Item item = activatable.get(j);
					
					//If Item is a Com satellite dish, activate it and
					//state that it is activated
					if(item.itemID == ItemNames.RADAR.getID()
							&& !item.activated &&
							temp.itemActivationID ==
							item.itemActivationID)
					{
						item.activated = true;
						Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
						SoundController.uplink.playAudioFile(0);
					}
					else
					{				
						//If item is enemy spawnpoint, then spawn the
						//enemy, and add the item to the arraylist of
						//items to be deleted
						if(item.itemID == ItemNames.ENEMYSPAWN.getID()
								&& temp.itemActivationID == item.itemActivationID)
						{
							enemiesInMap++;
							addEnemy(item.x, item.z, item.rotation);
							tempItems2.add(item);
						}	
						//If Explosion has same activation ID of the button
						//then activate it
						else if(item.itemID == ItemNames.ACTIVATEEXP.getID()
								&& temp.itemActivationID == item.itemActivationID)
						{
							new Explosion(item.x, item.y, item.z, 0, 0);
							tempItems2.add(item);
						}
						//If it gets rid of a wall, delete the wall and create an
						//air wall in its place.
						else if(item.itemID == ItemNames.WALLBEGONE.getID()
								&& temp.itemActivationID == item.itemActivationID)
						{
							Block block2 = Level.getBlock
									((int)item.x, (int)item.z);
							
							//Block is effectively no longer there
							block2.height = 0;
							
							tempItems2.add(item);
						}
					}
				}
				
				//Remove all the items that need to be deleted now
				for(int j = 0; j < tempItems2.size(); j++)
				{
					Item temp2 =  tempItems2.get(j);
							
					temp2.removeItem();
				}
			}
		}
		
	   /*
	    * Check all door entities each tick through the game,
	    * and if the door is activated, continue to move the
	    * door in whatever way it was moving before. Otherwise
	    * do nothing to it.
	    */
		for(int i = 0; i < Game.doors.size(); i++)
		{
			Door door = Game.doors.get(i);
			
			if(door.activated)
			{
				door.move();
			}
		}
		
		//Update all elevator entities, if its not already
		//moving, then there is not need to continue moving it
		for(int i = 0; i < Game.elevators.size(); i++)
		{
			Elevator elevator = Game.elevators.get(i);
			
			if(elevator.activated)
			{
				elevator.move();
			}
		}
		
		//Update players current weapon
		if(Player.weaponEquipped == 0)
		{
			((Pistol)(Player.weapons[0])).updateValues();
		}
		else if(Player.weaponEquipped == 1)
		{
			((Shotgun)(Player.weapons[1])).updateValues();
		}
		else if(Player.weaponEquipped == 2)
		{
			((PhaseCannon)(Player.weapons[2])).updateValues();
		}
		else
		{
			((RocketLauncher)(Player.weapons[3])).updateValues();
		}
		
		//Perform all actions depending on keys pressed.
		controls.performActions(this);
	}
	
	//TODO Maybe when multithreading is figured out
	@Override
	public void run()
	{
		tick(Display.input.key);
	}
	
   /**
    * Adds a projectile to the game starting at the players x, y 
    * and z position. This entity is used to hit enemies and 
    * make them happy.
    */
	public static void addBullet(int damage, int ID, double speed, 
			double rotation)
	{
		bullets.add(new Bullet(damage, speed, Player.x,
				((-Player.y) * 0.085), Player.z, ID, rotation));
	}
	
   /**
    * Adds a random enemy to the game if the map is random, and places
    * it in a random location within the map, depending on the maps 
    * size.
    */
	public void addEnemy()
	{
		Random rand  = new Random();
		int yPos     = 0;
		int ID       = 0;

		
		int randomNum = rand.nextInt(100);
		
	   /*
	    * Spawns a random type of enemy of the 4 types
	    */
		//Brainomorph
		if(randomNum <= 20)
		{
			ID = 1;
		}
		//Sentinel 
		else if(randomNum <= 40)
		{
			yPos   = -5;
			ID     = 2;
		}
		//Mutated Commando
		else if(randomNum <= 50)
		{
			ID      = 3;
		}
		//Night Reaper
		else if(randomNum <= 70)
		{
			ID      = 4;
		}
		//Vile Warrior
		else if(randomNum <= 98)
		{
			ID      = 7;
		}
		//Mage enemy
		else
		{
			ID      = 5;
		}
		
		//Add new enemy to game
		enemies.add(new Enemy( 
				0.5 + rand.nextInt(Level.width), yPos,
				0.5 + rand.nextInt(Level.height), ID, 0, 0));
	}
	
	/**
    * Adds a random enemy to the game to a designated place in
    * the map that is sent in through the parameters.
    */
	public void addEnemy(double x, double z, int rotation)
	{
		Block block = Level.getBlock((int)x, (int)z);
		
		Random rand  = new Random();
		int yPos     = 0;
		int ID       = 0;

		
		int randomNum = rand.nextInt(100);
		
	   /*
	    * Spawns a random type of enemy of the 4 types
	    */
		//Brainomorph
		if(randomNum <= 24)
		{
			ID = 1;
		}
		//Sentinel
		else if(randomNum <= 49)
		{
			yPos   = -5;
			ID     = 2;
		}
		//Mutated Commando
		else if(randomNum <= 74)
		{
			ID      = 3;
		}
		//Night Reaper
		else if(randomNum <= 98)
		{
			ID      = 4;
		}
		//Resurrector enemy
		else
		{
			ID      = 5;
		}
		
		Enemy enemy = new Enemy( x, yPos, z, ID, rotation, 0);
		
		//Add new enemy to game with correct values
		enemies.add(enemy);
		block.entitiesOnBlock.add(enemy);
		
		SoundController.teleportation.playAudioFile
				(enemy.distanceFromPlayer);
	}
	
   /**
    * Loads a game from a save file. Takes a lot more work than normal
    * map loading because entities could be in different positions,
    * phases, etc... when the player pauses the game and saves it. So
    * this has to handle everything!
    */
	public void loadGame()
	{	
		//A new scanner object that is defaultly set to null
		Scanner sc = null;  
		
	   /*
	    * Try to read the file and if not,
	    * state the error
	    */
		try 
		{
			//Creates a Scanner that can read the file
			sc = new Scanner(new BufferedReader
					(new FileReader("Users/"
							+FPSLauncher.currentUserName+"/"
							+FPSLauncher.fileToLoad+".txt")));
			
			String currentLine = "";
			
			///////////////////// Map stuff
			currentLine = sc.nextLine();
		    
			String[] elements = currentLine.split(":");
			
			//Was it survival or campaign mode
			int gM = Integer.parseInt(elements[1]);
			
			FPSLauncher.gameMode = gM;
			
			//Resets all the lists
			resetLists();
			
			secretsFound = Integer.parseInt(elements[2]);
			enemiesInMap = Integer.parseInt(elements[3]);
			Display.kills = Integer.parseInt(elements[4]);
			FPSLauncher.themeName = (elements[5]);
			
			//////////////////////////Player stuff now
			currentLine = sc.nextLine();
			
			String weaponStuff = "";
			String otherStuff = "";
			
			//Split between weapon equipped and weapon attributes
			elements = currentLine.split(",");
			
			weaponStuff = elements[1];
			otherStuff = elements[0];
			
			elements = otherStuff.split(":");
			
			Player.health = Integer.parseInt(elements[0]);
			Player.maxHealth = Integer.parseInt(elements[1]);
			Player.armor = Integer.parseInt(elements[2]);
			Player.x = Double.parseDouble(elements[3]);
			Player.y = Double.parseDouble(elements[4]);
			Player.z = Double.parseDouble(elements[5]);
			Player.rotation = Double.parseDouble(elements[6]);
			Player.maxHeight = Double.parseDouble(elements[7]);
			Player.hasRedKey = Boolean.parseBoolean(elements[8]);
			Player.hasBlueKey = Boolean.parseBoolean(elements[9]);
			Player.hasGreenKey = Boolean.parseBoolean(elements[10]);
			Player.hasYellowKey = Boolean.parseBoolean(elements[11]);
			Player.upRotate = Double.parseDouble(elements[12]);
			Player.extraHeight = Double.parseDouble(elements[13]);
			Player.resurrections = Integer.parseInt(elements[14]);
			Player.environProtectionTime = Integer.parseInt(elements[15]);
			Player.immortality = Integer.parseInt(elements[16]);
			Player.vision = Integer.parseInt(elements[17]);
			Player.invisibility = Integer.parseInt(elements[18]);	
			Player.weaponEquipped = Integer.parseInt(elements[19]);
			Player.godModeOn = Boolean.parseBoolean(elements[20]);
			Player.noClipOn = Boolean.parseBoolean(elements[21]);
			Player.flyOn = Boolean.parseBoolean(elements[22]);
			Player.superSpeedOn = Boolean.parseBoolean(elements[23]);
			Player.unlimitedAmmoOn = Boolean.parseBoolean(elements[24]);
			Player.upgradePoints = Integer.parseInt(elements[25]);
			Level.width = Integer.parseInt(elements[26]);
			Level.height = Integer.parseInt(elements[27]);
			mapNum = Integer.parseInt(elements[28]);
			mapAudio = elements[29];
			mapFloor = Integer.parseInt(elements[30]);
			mapCeiling = Integer.parseInt(elements[31]);
			Render3D.ceilingDefaultHeight = Double.parseDouble(elements[32]);
			Render3D.renderDistanceDefault = Double.parseDouble(elements[33]);
			
			//Because map names are normally named as Map#: Name. 
			//The colon causes issues so this fixes that. And adds
			//the colon back in because its split out
			try
			{
				mapName = elements[34] +":"+ elements[35];
			}
			catch(Exception ex)
			{
				//If there's an issue, its probably because the map name
				//does not include the colon
				mapName = elements[34];
			}
			
			//Set up new level with this size
			level    = new Level(Level.width, Level.height);
			
			//Split up weapon Attributes
			elements = weaponStuff.split(";");
    		
		   /*
		    * For each weapon, load in its attributes depending on what
		    * they were when the game was saved.
		    */
    		for(int i = 0; i < elements.length; i++)
    		{
    			Weapon w = Player.weapons[i];
    			
    			String[] weaponStats = elements[i].split(":");
    			
    			int size = weaponStats.length - 4;
    			
    			w.weaponID = Integer.parseInt(weaponStats[0]);
    			w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
    			w.dualWield = Boolean.parseBoolean(weaponStats[2]);
    			w.ammo = Integer.parseInt(weaponStats[3]);
    			
    			for(int j = 0; j < size; j++)
    			{
    				w.cartridges.add(new Cartridge
    						(Integer.parseInt(weaponStats[4 + j])));
    			}
    		}
    		
    		//////////////////Walls
			sc.nextLine();
			
			String thisLine ="";
			
			currentLine = sc.nextLine();
			
			//Stop reading when it reaches where the next element of the
			//game is being loaded in.
			while(!thisLine.equals("Enemies:"))
			{
				thisLine = sc.nextLine();
				
				if(thisLine.equals("Enemies:"))
				{
					break;
				}
				
				currentLine += thisLine;
			}
			
			elements = currentLine.split(";");
			
			for(int i = 0; i < elements.length; i++)
			{
				otherStuff = elements[i];
				String[] bAt = otherStuff.split(":");
				
				//Create enemy with its needed values
				Block b = new Block(Double.parseDouble(bAt[6]),
						Integer.parseInt(bAt[4]), 
						Double.parseDouble(bAt[2]),
						Integer.parseInt(bAt[1]),
						Integer.parseInt(bAt[3]));
				
				b.health = Integer.parseInt(bAt[0]);
				b.wallPhase = Integer.parseInt(bAt[5]);
				b.isSolid = Boolean.parseBoolean(bAt[7]);
				b.seeThrough = Boolean.parseBoolean(bAt[8]);
		
				Level.blocks[b.x + b.z * Level.width] = b;
			}
    		
    		////////////////// Enemies
    		thisLine = "";
    		
    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Bosses:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Bosses:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		elements = currentLine.split(";");
    		
    		for(int i = 0; i < elements.length; i++)
    		{
    			otherStuff = elements[i];
    			String[] enAt = otherStuff.split(":");
    			
    			//Create enemy with its needed values
    			Enemy en = new Enemy(Double.parseDouble(enAt[1]),
    					Double.parseDouble(enAt[2]),
    					Double.parseDouble(enAt[3]),
    					Integer.parseInt(enAt[4]),
    					Double.parseDouble(enAt[12]),
    					Integer.parseInt(enAt[5]));
    			
    			en.maxHeight = Double.parseDouble(enAt[6]);
    			en.newTarget = Boolean.parseBoolean(enAt[7]);
    			en.targetX = Double.parseDouble(enAt[8]);
    			en.targetY = Double.parseDouble(enAt[9]);
    			en.targetZ = Double.parseDouble(enAt[10]);
    			en.activated = Boolean.parseBoolean(enAt[11]);
    			en.isAttacking = Boolean.parseBoolean(enAt[13]);
    			en.isFiring = Boolean.parseBoolean(enAt[14]);
    			en.isABoss = Boolean.parseBoolean(enAt[15]);
    			en.xEffects = Double.parseDouble(enAt[16]);
    			en.yEffects = Double.parseDouble(enAt[17]);
    			en.zEffects = Double.parseDouble(enAt[18]);
    			en.tick = Integer.parseInt(enAt[19]);
    			en.tickRound = Integer.parseInt(enAt[20]);
    			en.tickAmount = Integer.parseInt(enAt[21]);
		
    			Game.enemies.add(en);
    			
    			Block blockOn = Level.getBlock((int)en.xPos, (int)en.zPos);
    			
    			//Only if in campaign mode, survival mode acts weird here
    			if(gM == 0)
    			{
    				blockOn.entitiesOnBlock.add(en);
    			}
    		}
    		
    		thisLine = "";
    		
    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Items:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Items:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		int length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			otherStuff = elements[i];
    			String[] enAt = otherStuff.split(":");
    			
    			//Create enemy with its needed values
    			Enemy en = new Enemy(Double.parseDouble(enAt[1]),
    					Double.parseDouble(enAt[2]),
    					Double.parseDouble(enAt[3]),
    					Integer.parseInt(enAt[4]),
    					Double.parseDouble(enAt[12]),
    					Integer.parseInt(enAt[5]));
    			
    			en.maxHeight = Double.parseDouble(enAt[6]);
    			en.newTarget = Boolean.parseBoolean(enAt[7]);
    			en.targetX = Double.parseDouble(enAt[8]);
    			en.targetY = Double.parseDouble(enAt[9]);
    			en.targetZ = Double.parseDouble(enAt[10]);
    			en.activated = Boolean.parseBoolean(enAt[11]);
    			en.isAttacking = Boolean.parseBoolean(enAt[13]);
    			en.isFiring = Boolean.parseBoolean(enAt[14]);
    			en.isABoss = Boolean.parseBoolean(enAt[15]);
    			en.xEffects = Double.parseDouble(enAt[16]);
    			en.yEffects = Double.parseDouble(enAt[17]);
    			en.zEffects = Double.parseDouble(enAt[18]);
    			en.tick = Integer.parseInt(enAt[19]);
    			en.tickRound = Integer.parseInt(enAt[20]);
    			en.tickAmount = Integer.parseInt(enAt[21]);
		
    			Game.bosses.add(en);
    		}
    		
    		thisLine = "";
    		
    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Bullets:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Bullets:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			otherStuff = elements[i];
    			String[] itemAtt = otherStuff.split(":");
    			
    			int itemID = Integer.parseInt(itemAtt[1]);
    			
    			Item temp = null;
    			
    		   /*
			    * If its not an explosive canister, add it as a normal
			    * item. Otherwise add it as an explosive canister
			    */
				if(itemID != ItemNames.CANISTER.getID())
				{
					temp = new Item(10, 
							Double.parseDouble(itemAtt[2]), 
							Double.parseDouble(itemAtt[3]), 
							Double.parseDouble(itemAtt[4]),
							itemID, Integer.parseInt(itemAtt[5]),
							Integer.parseInt(itemAtt[0]),
							itemAtt[6]);
				}
				else
				{
					temp = new ExplosiveCanister(10, 
							Double.parseDouble(itemAtt[2]), 
							Double.parseDouble(itemAtt[3]), 
							Double.parseDouble(itemAtt[4]),
							itemID, Integer.parseInt(itemAtt[5]),
							Integer.parseInt(itemAtt[0]));
				}
				
				Block itemBlock = Level.getBlock((int)temp.x, (int)temp.z);
				
				//If the item is solid
				if(temp.isSolid ||
						itemID == ItemNames.BREAKABLEWALL.getID()
						|| itemID == ItemNames.SECRET.getID())
				{
					//Set item to being the item that is within this
					//block only if it is solid
					itemBlock.wallItem = temp;
				}
				
				//If satellite dish, add to activatable list as well
				if(itemID == ItemNames.RADAR.getID())
				{
					Game.activatable.add(temp);
				}
				//If item supposed to be activated by button
				else if(itemID == ItemNames.ACTIVATEEXP.getID()
						|| itemID == ItemNames.ENEMYSPAWN.getID()
						|| itemID == ItemNames.WALLBEGONE.getID())
				{
					Game.activatable.add(temp);
				}
				else if(itemID == ItemNames.TELEPORTEREXIT.getID()
						|| itemID == ItemNames.TELEPORTERENTER.getID())
				{
					Game.teleporters.add(temp);
					
					itemBlock.wallEntity = temp;
				} 			
    		}
    		
    		////////////////////////Bullets
    		thisLine = "";

    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Enemy Projectiles:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Enemy Projectiles:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			Bullet b = null;
    			
    			String[] bAtt = elements[i].split(":");
    			
    			b = new Bullet(Integer.parseInt(bAtt[1]),
    					Double.parseDouble(bAtt[2]),
    					Double.parseDouble(bAtt[3]),
    					Double.parseDouble(bAtt[4]),
    					Double.parseDouble(bAtt[5]),
    					Integer.parseInt(bAtt[0]),
    					0);
    			
    			b.xa = Double.parseDouble(bAtt[6]);
    			b.za = Double.parseDouble(bAtt[7]);
    			b.initialSpeed = Double.parseDouble(bAtt[8]);
    			
    			Game.bullets.add(b);
    		}
    		
    		///////////////////////////Enemy Fire
    		thisLine = "";

    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Explosions:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Explosions:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			EnemyFire b = null;
    			
    			String[] bAtt = elements[i].split(":");
    			
    			//Given all the information we have, construct
    			//the enemy projectile the best we can after a save
    			b = new EnemyFire(Integer.parseInt(bAtt[1]),
    					Double.parseDouble(bAtt[2]),
    					Double.parseDouble(bAtt[3]),
    					Double.parseDouble(bAtt[4]),
    					Double.parseDouble(bAtt[5]),
    					Integer.parseInt(bAtt[0]),
    					0,0,0,0,null);
    			
    			b.xa = Double.parseDouble(bAtt[6]);
    			b.za = Double.parseDouble(bAtt[7]);
    			b.initialSpeed = Double.parseDouble(bAtt[8]);
    			
    			Game.enemyProjectiles.add(b);
    		}
    		
    		///////////////////////////////////Explosions
    		thisLine = "";

    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Buttons:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Buttons:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			Explosion exp = null;
    			
    			String[] expAtt = elements[i].split(":");
    			
    			exp = new Explosion(Double.parseDouble(expAtt[2]),
    					Double.parseDouble(expAtt[3]),
    					Double.parseDouble(expAtt[4]),
    					Integer.parseInt(expAtt[0]),
    					Integer.parseInt(expAtt[6]));
    			
    			exp.exploded = Boolean.parseBoolean(expAtt[5]);
    			exp.phaseTime = Integer.parseInt(expAtt[1]);
    			
    			Game.explosions.add(exp);
    		}
    		
    		////////////////////////////Buttons
    		thisLine = "";

    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Doors:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Doors:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			Button b = null;
    			
    			String[] bAtt = elements[i].split(":");
    			
    			b = new Button(Double.parseDouble(bAtt[2]),
    					Double.parseDouble(bAtt[3]),
    					Double.parseDouble(bAtt[4]),
    					Integer.parseInt(bAtt[0]),
    					Integer.parseInt(bAtt[1]));
    			
    			b.pressed = Boolean.parseBoolean(bAtt[5]);
    			
    			Game.buttons.add(b);
    		}
    		
			////////////////////////////Doors
    		thisLine = "";

    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Elevators:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Elevators:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
			
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
			
			for(int i = 0; i < length; i++)
			{
				Door d = null;
				
				String[] dAtt = elements[i].split(":");
				
				d = new Door(Double.parseDouble(dAtt[2]),
						Double.parseDouble(dAtt[3]),
						Double.parseDouble(dAtt[4]),
						Integer.parseInt(dAtt[5]),
						Integer.parseInt(dAtt[6]),
						Integer.parseInt(dAtt[9]),
						Integer.parseInt(dAtt[1]),
						Integer.parseInt(dAtt[11]));
				
				d.time = Integer.parseInt(dAtt[7]);
				d.soundTime = Integer.parseInt(dAtt[8]);
				d.ID = Integer.parseInt(dAtt[0]);
				d.doorY = Double.parseDouble(dAtt[10]);
				
				Block thisBlock = Level.getBlock
						((int) d.doorX, (int)d.doorZ);
				
				thisBlock.y = d.doorY;
				
				if(thisBlock.y > 0)
				{
					thisBlock.isMoving = true;
				}
				
				Game.doors.add(d);
			}
			
			////////////////////////////Elevators
			thisLine = "";
			
    		currentLine = sc.nextLine();
    		
    		while(!thisLine.equals("Corpses:"))
    		{
    			thisLine = sc.nextLine();
    			
    			if(thisLine.equals("Corpses:"))
    			{
    				break;
    			}
    			
    			currentLine += thisLine;
    		}
			
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
			
			for(int i = 0; i < length; i++)
			{
				Elevator e = null;
				
				String[] eAtt = elements[i].split(":");
				
				e = new Elevator(Double.parseDouble(eAtt[2]),
						Double.parseDouble(eAtt[3]),
						Double.parseDouble(eAtt[4]),
						Integer.parseInt(eAtt[5]),
						Integer.parseInt(eAtt[6]),
						Integer.parseInt(eAtt[1]),
						Integer.parseInt(eAtt[14]));
				
				e.height = Integer.parseInt(eAtt[7]);
				e.soundTime = Integer.parseInt(eAtt[8]);
				e.ID = Integer.parseInt(eAtt[0]);
				e.waitTime = Integer.parseInt(eAtt[11]);
				e.upHeight = Double.parseDouble(eAtt[12]);
				e.movingUp = Boolean.parseBoolean(eAtt[9]);
				e.movingDown = Boolean.parseBoolean(eAtt[10]);
				e.activated = Boolean.parseBoolean(eAtt[13]);
				
				Game.elevators.add(e);
				
				Block thisBlock = Level.getBlock
						((int) e.elevatorX, (int)e.elevatorZ);
				
				thisBlock.height = e.height;
			}
			
			///////////////////////////////Corpses
			thisLine = "";
    		
			//Sometimes theres not a next line
			try
			{
				currentLine = sc.nextLine();
			}
			catch(Exception e)
			{
				
			}
    		
    		while(sc.hasNextLine())
    		{
    			thisLine = sc.nextLine();
    				
    			currentLine += thisLine;
    		}
    		
    		length = 0;
    		
    		if(currentLine.equals(""))
    		{
    			elements = null;
    		}
    		else
    		{
    			elements = currentLine.split(";");
    			length = elements.length;
    		}
    		
    		for(int i = 0; i < length; i++)
    		{
    			Corpse c = null;
    			
    			String[] cAtt = elements[i].split(":");
    			
    			c = new Corpse(Double.parseDouble(cAtt[2]),
    					Double.parseDouble(cAtt[4]),
    					Double.parseDouble(cAtt[3]),
    					Integer.parseInt(cAtt[0]),
    					Double.parseDouble(cAtt[6]),
    					Double.parseDouble(cAtt[8]),
    					Double.parseDouble(cAtt[7]));
    			
    			c.time = Integer.parseInt(cAtt[5]);
    			c.phaseTime = Integer.parseInt(cAtt[1]);
    			
    			Game.corpses.add(c);
    		}
			
			sc.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
			System.exit(0);
		}
	}
	
   /**
    * Sets up the survival game mode.
    */
	public void setUpSurvival()
	{
		calculatedSize = 100;
		
		if(FPSLauncher.levelSizeChoice == 0)
		{
			calculatedSize = 10;
		}
		else if(FPSLauncher.levelSizeChoice == 1)
		{
			calculatedSize = 25;
		}
		else if(FPSLauncher.levelSizeChoice == 2)
		{
			calculatedSize = 50;
		}
		else if(FPSLauncher.levelSizeChoice == 3)
		{
			calculatedSize = 100;
		}
		else if(FPSLauncher.levelSizeChoice == 4)
		{
			calculatedSize   = 250;
		}
		else
		{
			calculatedSize   = 100;
		}
		
		//Reset level size to calculated size
		levelSize = calculatedSize;
		
		//Set up new level with this size
		level    = new Level(levelSize, levelSize);
		
		//Add initial enemy
		addEnemy();
		
	   /*
	    * Put 500 of either health packs, armor, or megahealth
	    */
		for(int i = 0; i < 500; i++)
		{
			Random rand = new Random();
			
			int ammoType = rand.nextInt(102);
			
			//Place random ammo type in map
			if(ammoType <= 70)
			{
				ammoType = 2;
			}
			else if(ammoType <= 98)
			{
				ammoType = 33;
			}
			else if(ammoType == 99)
			{
				ammoType = 1;
			}
			else if(ammoType == 100)
			{
				ammoType = 34;
			}
			else
			{
				ammoType = 35;
			}
			
			//Item adds itself when instantiated
			new Item(2, rand.nextInt(calculatedSize), 
					0, rand.nextInt(calculatedSize), ammoType, 0, 0, "");
		}
		
	   /*
	    * Add 500 random ammo types to the map
	    */
		for(int i = 0; i < 500; i++)
		{
			Random rand = new Random();
			
			int ammoType = rand.nextInt(4);
			
			//Place random ammo type in map
			if(ammoType == 0)
			{
				ammoType = 3;
			}
			else if(ammoType == 1)
			{
				ammoType = 50;
			}
			else if(ammoType == 2)
			{
				ammoType = 56;
			}
			else
			{
				ammoType = 61;
			}
			
			//Item adds itself when instantiated
			new Item(2, rand.nextInt(calculatedSize), 
					0, rand.nextInt(calculatedSize), ammoType, 0, 0, "");
		}
		
		//Player has all weapons for survival mode
		Player.weapons[0].dualWield = true;
		Player.weapons[1].canBeEquipped = true;
		Player.weapons[2].canBeEquipped = true;
		Player.weapons[3].canBeEquipped = true;
	}
	
   /**
    * Loads the next map up for the game, and sets up the new level and
    * the positions of the entities.
    */
	public void loadNextMap(boolean newMap, String mapNameNew)
	{
	   /*
	    * The player does not keep keys from level to level.
	    * Nor does he/she keep his/her effects
	    */
		Player.hasRedKey = false;
		Player.hasBlueKey = false;
		Player.hasGreenKey = false;
		Player.hasYellowKey = false;
		Player.immortality = 0;
		Player.invisibility = 0;
		Player.environProtectionTime = 0;
		Player.vision = 0;
		
	   /*
	    * Every map starts with 0 enemies, 0 secrets, and no secrets
	    * found on default.
	    */
		secretsInMap = 0;
		secretsFound = 0;
		enemiesInMap = 0;
		
		//Player has no kills on the map when the map is first started
		Display.kills = 0;
		
		//Try and read the file correctly, if not catch exception
		try
		{
			String temp = "";
			
			//Rows and columns of blocks in new level 
			int row = 0;
			int col = 0;
			
			//Default player rotation
			Player.rotation = 0;
			
			//Resets all the lists
			resetLists();
			
			//It requires me to put something here.... sigh...
			Scanner sc = new Scanner(new BufferedReader(new FileReader("resources/default/maps/map"+mapNum+".txt")));
			
			//First sets up default map name to be read
			try
			{
				sc = new Scanner(new BufferedReader
					(new FileReader("resources"+FPSLauncher.themeName+"/maps/map"+mapNum+".txt")));
				
				//If a custom map name, load scan that instead
				if(newMap)
				{
					sc = new Scanner(new BufferedReader
							(new FileReader("resources"+FPSLauncher.themeName+"/maps/"+mapNameNew+".txt")));		
				}
			}
			catch(Exception e)
			{
				try
				{
					//If that map does not exist in the resource pack
					sc = new Scanner(new BufferedReader
							(new FileReader("resources/default/maps/map"+mapNum+".txt")));
						
					//If a custom map name, load scan that instead
					if(newMap)
					{
						sc = new Scanner(new BufferedReader
								(new FileReader("resources/default/maps/"+mapNameNew+".txt")));		
					}
				}
				catch(Exception ex)
				{
					//If that map number is not found in either, just
					//quit to main menu again.
					e.printStackTrace();
					
					//If map doesn't load, exit to pause menu so game
					//Doesn't crash and you can retry
					Display.pauseGame =  true;
					
					//Quit game is true
					Controller.quitGame = true;
					
					//Restart the game to quit
					new Game(display, false, "");
				}
			}
			
			//The very first part of any map file now is the name
			mapName = sc.nextLine();
			
			//The next line is the special settings for the map
			String mapSettings = sc.nextLine();
			
			try
			{
				//Split that line up to get the map number
				//ceiling height, and render distance of map
				//and map audio to be played and also the
				//Ceiling and floor texture ID's
				String[] temp2 = mapSettings.split(":");
				
				mapNum = Integer.parseInt(temp2[0]);
				
				Render3D.ceilingDefaultHeight 
					= Integer.parseInt(temp2[1]);
				
				Render3D.renderDistanceDefault 
					= Integer.parseInt(temp2[2]);
				
				mapAudio = temp2[3];
				
				mapFloor = Integer.parseInt(temp2[4]);
				
				mapCeiling = Integer.parseInt(temp2[5]);
			}
			catch(Exception e)
			{
				
			}
			
			//Reset the music with this new audio file
			display.resetMusic(mapAudio);
			
		   /*
		    * While the whole file has not been read, keep scanning each 
		    * line of the file into string format and adding it to the
		    * temporary string.
		    */
			while(sc.hasNext())
			{
				temp += sc.next();
			}
			
		   /*
		    * At each point there is a , in the string, split that up
		    * as a seperate string and add it as a new slot in the array
		    * of strings called splitedParts
		    */
			String[] splitedParts = temp.split(",");
			
		   /*
		    * Because apparently map has to be instantiated correctly with
		    * a size that will fit all the walls into it, therefore I
		    * instantiated it with a size in both rows and columns that may
		    * be too big, but will fit all the walls and entities in the
		    * map for sure.
		    */
			ValueHolder[][] tempMap = 
					new ValueHolder[splitedParts.length][splitedParts.length];
			
		   /*
		    * Stores the actual number of cloumns the 2D array of the map
		    * has so that the 2D array that "map" is instantiated with is
		    * the correct size.
		    */
			int storeColumn = 0;
			
		   /*
		    * Will stop adding to storeColumn when set to true. After one
		    * row of columns has been counted, the number is stored, and
		    * the counting needs to stop.
		    */
			boolean stop = false;
			
		   /*
		    * For each slot of the splited parts array, convert the
		    * part into an actual integer using the parseInt method, 
		    * then put that int into that slot of the 2D array map.
		    */
			for(int i = 0; i < splitedParts.length; i++)
			{
				String[] blockValues = splitedParts[i].split(":");

				double height = Integer.parseInt(blockValues[0]);
				int wallID = Integer.parseInt(blockValues[1]);
				int entityID = Integer.parseInt(blockValues[2]);
				double rotation = 0;
				int itemActID = 0;
				int wallY = 0;
				int doorRaiseHeight = 0;
				String audioQueue = "";
				
				//Try to see if block has an entity with a given rotation
				try
				{
					rotation = Integer.parseInt(blockValues[3]);
					
					//Sets to radians
					rotation = (rotation / 180) * Math.PI;
				}
				catch (Exception e)
				{
					//If entity does not have a rotation, the default is 0
					rotation = 0;
				}
				
				//Try to see if block has an entity with a given
				//Activation ID
				try
				{
					itemActID = Integer.parseInt(blockValues[4]);
				}
				catch (Exception e)
				{
					//If entity does not have a activation ID
					//, the default is 0
					itemActID = 0;
				}
				
				//Try to see if block has an entity with a given
				//Audio Queue
				try
				{
					audioQueue = (blockValues[5]);
				}
				catch (Exception e)
				{
					//If entity does not have a audio queue
					//, the default is 0
					audioQueue = "";
				}
				
				//Try to see if block has an entity with a given
				//Wall y value
				try
				{
					wallY = Integer.parseInt(blockValues[6]);
				}
				catch (Exception e)
				{
					//If entity does not have a wallY
					//, the default is 0
					wallY = 0;
				}
				
				//Try to see if block has an entity with a given
				//Door Height value
				try
				{
					doorRaiseHeight = Integer.parseInt(blockValues[7]);
				}
				catch (Exception e)
				{
					//If entity does not have a doorHeightValue
					//, the default is 0
					doorRaiseHeight = 0;
				}
				
			   /*
			    * 100 is a special wall ID which tells this to move onto 
			    * the next row of the map and begin again.
			    */
				if(wallID == 100)
				{
					row++;
					col = 0;
					stop = true;
				}
				else
				{
					//Set the next spot in the map to this new ValueHolder
					tempMap[row][col] = 
							new ValueHolder(height, wallID, entityID,
									rotation, itemActID, audioQueue, wallY, doorRaiseHeight);
					
					//Go to next column
					col++;
					
					//Stores how many columns are in this row
					if(!stop)
					{
						storeColumn++;
					}
				}
			}
			
		   /*
		    * For now, all maps are made square in the text file so that
		    * the program works every time with these values. So the
		    * actual map loaded will be its correct size and not too
		    * small or large based on limitations that are manually set.
		    */
			map = new ValueHolder[row][storeColumn];
			
		   /*
		    * Puts the values that actually matter in the temp map into
		    * the actual map loaded.
		    */
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[0].length; j++)
				{
					map[i][j] = tempMap[i][j];
				}
			}

		   /*
		    * Figures out how many blocks in both the x and z
		    * direction that the game needs to render. It uses the 
		    * farthest direction as the amount to render in every
		    * direction
		    */
			if(map.length > map[0].length)
			{
				levelSize = map.length;
			}
			else
			{
				levelSize = map[0].length;
			}
			
			//Constructs the new level
			level    = new Level(map);
			
			//Calculates the size of the new level
			calculatedSize = map.length * map[0].length;
			
			//Close the scanner
			sc.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			//If map doesn't load, exit to pause menu so game
			//Doesn't crash and you can retry
			Display.pauseGame =  true;
			
			//Quit game is true
			Controller.quitGame = true;
			
			return;
			
			//Restart the game to quit
			//new Game(display, false, "");
		}
	}
	
   /**
    * Used so much that I just created a method that can be called to 
    * do it.
    */
	public void resetLists()
	{
		//Reset all entities in game
		enemies       = new ArrayList<Enemy>();
		bosses        = new ArrayList<Enemy>();
		items         = new ArrayList<Item>();
		bullets       = new ArrayList<Bullet>();
		buttons    	  = new ArrayList<Button>();
		hurtingBlocks = new ArrayList<HurtingBlock>();
		doors         = new ArrayList<Door>();
		elevators     = new ArrayList<Elevator>();
		corpses       = new ArrayList<Corpse>();
		canisters     = new ArrayList<ExplosiveCanister>();
		explosions    = new ArrayList<Explosion>();
		solidItems    = new ArrayList<Item>();
		enemyProjectiles = new ArrayList<EnemyFire>();
		activatable   = new ArrayList<Item>();
		sprites 	  = new ArrayList<HitSprite>();
	}

}
