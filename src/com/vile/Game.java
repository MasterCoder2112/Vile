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
public class Game 
{
	//Time games been running in ticks
	public static int time;
	
	//Handles all controls and player movements
	public Controller controls;
	
	//Is this a pre-made map or a randomly generated one?
	public static boolean setMap = false;
	
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
	public static int mapNum   = 1;
	
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
	
	//Did the first announcement or sound to start the level play
	public boolean firstSound = false;
	
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
		
		//Everything is reset
		enemies       = new ArrayList<Enemy>();
		bosses        = new ArrayList<Enemy>();
		items         = new ArrayList<Item>();
		bullets       = new ArrayList<Bullet>();
		enemyProjectiles = new ArrayList<EnemyFire>();
		buttons    	  = new ArrayList<Button>();
		hurtingBlocks = new ArrayList<HurtingBlock>();
		doors         = new ArrayList<Door>();
		elevators     = new ArrayList<Elevator>();
		corpses       = new ArrayList<Corpse>();
		explosions    = new ArrayList<Explosion>();
		solidItems    = new ArrayList<Item>();
		canisters 	  = new ArrayList<ExplosiveCanister>();
		activatable   = new ArrayList<Item>();
		
		//If a survival map
		if(setMap == false)
		{
			calculatedSize = 100;
			
			if(Display.levelSize == 0)
			{
				calculatedSize = 10;
			}
			else if(Display.levelSize == 1)
			{
				calculatedSize = 25;
			}
			else if(Display.levelSize == 2)
			{
				calculatedSize = 50;
			}
			else if(Display.levelSize == 3)
			{
				calculatedSize = 100;
			}
			else if(Display.levelSize == 4)
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
						0, rand.nextInt(calculatedSize), ammoType, 0);
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
						0, rand.nextInt(calculatedSize), ammoType, 0);
			}
			
			//Player has all weapons for survival mode
			Player.weapons[0].dualWield = true;
			Player.weapons[1].canBeEquipped = true;
			Player.weapons[2].canBeEquipped = true;
			Player.weapons[3].canBeEquipped = true;
			
		}
		else
		{
			//Load either the first map or custom map if that is chosen
			mapNum = 1;
			loadNextMap(newStartMap, newMapName);
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
		if(setMap)
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
				
				//Depends on game theme
				if(Display.themeNum == 3)
				{
					SoundController.nickDeath.playAudioFile();
				}
				else if(Display.themeNum == 5)
				{
					SoundController.mlgDeath.playAudioFile();
				}
				else
				{
					SoundController.playerDeath.playAudioFile();
				}
			}
			else
			{
				//Get rid of happiness restorer item the player has
				Player.resurrections--;
				
				//Display that the player was restored to full happiness
				Display.itemPickup = "RESURRECTED WITH SKULL!";
				Display.itemPickupTime = 1;
				
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
			if(temp.ID == 0
					&& temp.activated)
			{
				mapNum++;
				loadNextMap(false, "");
			}
			
			//If a normal button
			if(temp.ID == 1 
					&& temp.activated)
			{
				//De-activate the button
				temp.activated = false;
				
				//Stores Items to be deleted
				ArrayList<Item> tempItems2 = new ArrayList<Item>();
				
				//Scan all activatable items
				for(int j = 0; j < activatable.size(); j++)
				{
					Item item = activatable.get(j);
					
					Player.hasYellowKey = true;
					
					//If Item is a Happiness Tower, activate it and
					//state that it is activated
					if(item.itemID == ItemNames.RADAR.getID()
							&& !item.activated)
					{
						item.activated = true;
						Display.itemPickup = "COM SYSTEM ACTIVATED!";
						Display.itemPickupTime = 1;
					}
					else
					{				
						//If item is enemy spawnpoint, then spawn the
						//enemy, and add the item to the arraylist of
						//items to be deleted
						if(item.itemID == ItemNames.ENEMYSPAWN.getID())
						{
							enemiesInMap++;
							addEnemy(item.x, item.z, item.rotation);
						}	
						//If activatable explosion, set forth the explosion
						else if(item.itemID == ItemNames.ACTIVATEEXP.getID())
						{
							new Explosion(item.x, item.y, item.z, 0);
						}
						//If it gets rid of a wall, delete the wall and create an
						//air wall in its place.
						else if(item.itemID == ItemNames.WALLBEGONE.getID())
						{
							Block block2 = Level.getBlock
									((int)item.x, (int)item.z);
							
							//Block is effectively no longer there
							block2.height = 0;
						}
						
						tempItems2.add(item);
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
				0.5 + rand.nextInt(calculatedSize), yPos,
				0.5 + rand.nextInt(calculatedSize), ID, 0));
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
		
		Enemy enemy = new Enemy( x, yPos, z, ID, rotation);
		
		//Add new enemy to game with correct values
		enemies.add(enemy);
		block.enemiesOnBlock.add(enemy);
	}
	
   /**
    * Loads the next map up for the game, and sets up the new level and
    * the positions of the entities.
    */
	public void loadNextMap(boolean newMap, String mapNameNew)
	{
	   /*
	    * The player does not keep keys from level to level.
	    */
		Player.hasRedKey = false;
		Player.hasBlueKey = false;
		Player.hasGreenKey = false;
		Player.hasYellowKey = false;
		Player.immortality = 0;
		Player.environProtectionTime = 0;
		
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
			
			//First sets up default map name to be read
			Scanner sc = new Scanner(new BufferedReader
				(new FileReader("map"+mapNum+".txt")));
			
			//If a custom map name, load scan that instead
			if(newMap)
			{
				sc = new Scanner(new BufferedReader
						(new FileReader(mapNameNew+".txt")));
			}
			
			//The very first part of any map file now is the name
			mapName = sc.nextLine();
			
			//The next line is the special settings for the map
			String mapSettings = sc.nextLine();
			
			try
			{
				//Split that line up to get the map number
				//ceiling height, and render distance of map
				//and map audio to be played
				String[] temp2 = mapSettings.split(":");
				mapNum = Integer.parseInt(temp2[0]);
				Render3D.ceilingDefaultHeight 
				= Integer.parseInt(temp2[1]);
				Render3D.renderDistanceDefault 
				= Integer.parseInt(temp2[2]);
				mapAudio = temp2[3];
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
									rotation);
					
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
			
			//No longer a setMap
			Game.setMap = false;
			
			//Restart the game to quit
			new Game(display, false, "");
		}
		
		firstSound = false;
	}
}
