package com.vile.levelGenerator;

import java.util.Random;

import com.vile.Game;
import com.vile.ValueHolder;
import com.vile.entities.Button;
import com.vile.entities.Corpse;
import com.vile.entities.Door;
import com.vile.entities.Elevator;
import com.vile.entities.Enemy;
import com.vile.entities.Explosion;
import com.vile.entities.ExplosiveCanister;
import com.vile.entities.HurtingBlock;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;

/**
 * Title: Level
 * @author Alex Byrd
 * Date Updated: 4/29/2017
 *
 * Generates a level of blocks with set positions for all of them.
 * Blocks can be nonSolid meaning they are not seen and can be
 * walked through or have entities spawn in them, or solid where
 * they will have a certain texture but can't be walked through.
 */
public class Level 
{
	public static Block[] blocks;
	public static int width = 0;
	public static int height = 0;
	
   /**
    * First type of level constructor used to construct a random level.
    * @param w
    * @param h
    */
	public Level(int w, int h)
	{
		width = w;
		height = h;
		blocks = new Block[width * height];
		
		Random random = new Random();
		
		for(int z = 0; z < height; z++)
		{
			for(int x = 0; x < width; x++)
			{
				Block block = null;
				
				//Random wall type is chosen
				int randomWall = random.nextInt(19);
				
				if(randomWall == 0)
				{
					randomWall = 1;
				}
				
				//One in 5 chance the block becomes solid
				if(random.nextInt(100) == 0)
				{

					block = new Block(24, randomWall, 0, x, z);
				}
				else
				{
					block = new Block(0, 0, 0, x, z);
				}
				
			   /*
			    * Generates a new block at a certain position in the
			    * level.
			    */
				blocks[x + z * width] = block;
			}
		}
		
	}
	
   /**
    * Uses a 2 dimensional array sent in to create a map of different
    * types of walls.
    * @param map
    */
	public Level(ValueHolder[][] map)
	{
		//Get height of map from 2D array column
		height = map[0].length;
		
		//Get width from 2D array row
		width  = map.length;
		
		//Array of blocks that make up map
		blocks = new Block[width * height];
		
		//For each element in the map
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				//Each block is null until instantiated
				Block block = null;
				
				//Gets the itemID at each location
				int itemID = map[i][j].entityID;
				
				//Rotation of entity if there is one
				double rotation = map[i][j].rotation;
				
				//Item activation ID
				int itemActID = map[i][j].itemActID;
				
				//Gets each blocks height
				block = new Block(map[i][j].height,
						map[i][j].wallID, 0, i, j);
				
				//Sets the block at that location in the level.
				blocks[i + j * width] = block;
				
				//If a toxic waste block, add that item to the block as well
				if(map[i][j].wallID == 16)
				{
					Item temp = new HurtingBlock( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 0);
					
					Game.hurtingBlocks.add((HurtingBlock)temp);
					
					block.wallEntity = temp;
				}
				//If lava block, add lava entity to block as well
				else if(map[i][j].wallID == 17)
				{
					Item temp = new HurtingBlock( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 1);
					
					Game.hurtingBlocks.add((HurtingBlock)temp);
					
					block.wallEntity = temp;
				}
				
				//Normal items (Keys, healthpacks, ammo, etc...)
				if(itemID > ItemNames.AIR.getID() 
						&& itemID <= ItemNames.YELLOWKEY.getID()
						|| itemID == ItemNames.SHOTGUN.getID()
						|| itemID > 23 && itemID < 44
						|| itemID >= 47
						&& itemID != ItemNames.BUTTON.getID()
						&& itemID != 58 
						&& itemID != 59
						&& itemID != ItemNames.EXPLOSION.getID() 
						&& itemID != ItemNames.ACTIVATEEXP.getID()
						&& itemID != ItemNames.WALLBEGONE.getID()
						&& itemID != ItemNames.ENEMYSPAWN.getID()
						&& itemID != ItemNames.TELEPORTERENTER.getID()
						&& itemID != ItemNames.TELEPORTEREXIT.getID())
				{
					//Item to be added to the map and block
					Item temp = null;

				   /*
				    * If its not an explosive canister, add it as a normal
				    * item. Otherwise add it as an explosive canister
				    */
					if(itemID != ItemNames.CANISTER.getID())
					{
						temp = new Item(10, 
								i + 0.5, 
								block.height - block.y, 
								j + 0.5, itemID, (int)rotation, itemActID);
					}
					else
					{
						temp = new ExplosiveCanister(10, 
								i + 0.5, 
								block.height - block.y, 
								j + 0.5, itemID, (int)rotation, itemActID);
					}
					
					//If the item is solid
					if(temp.isSolid)
					{
						//Set item to being the item that is within this
						//block only if it is solid
						block.wallItem = temp;
					}
					
					//If satellite dish, add to activatable list as well
					if(itemID == ItemNames.RADAR.getID())
					{
						Game.activatable.add(temp);
					}
				}	
				//Players spawn
				else if(itemID == 8)
				{
				   /*
				    * Corrects for position in map, and places the player
				    * directly in the center of the block that he/she
				    * is placed at in the map file.
				    */
					Player.x = i + 0.5;
					Player.z = j + 0.5;
					Player.y = block.y + block.height;
					Player.rotation = rotation;
				}
				//End button or normal button
				else if(itemID == ItemNames.BUTTON.getID())
				{
					Game.buttons.add(new Button( 
							i + 0.5, 
							0.77, j + 0.5, itemID, itemActID));
				}
				//Lift/elevator
				else if(itemID == ItemNames.ELEVATOR.getID())
				{
					Game.elevators.add(new Elevator( 
							i + 0.5, 
							0.77, j + 0.5, i, j, itemActID));
				}
				//Normal door
				else if(itemID == ItemNames.DOOR.getID())
				{
					Game.doors.add(new Door( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 0, itemActID));
				}
				//Red door
				else if(itemID == ItemNames.REDDOOR.getID())
				{
					Game.doors.add(new Door( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 1, itemActID));
				}
				//Blue door
				else if(itemID == ItemNames.BLUEDOOR.getID())
				{
					Game.doors.add(new Door( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 2, itemActID));
				}
				//Green Door
				else if(itemID == ItemNames.GREENDOOR.getID())
				{
					Game.doors.add(new Door( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 3, itemActID));
				}
				//Yellow door
				else if(itemID == ItemNames.YELLOWDOOR.getID())
				{
					Game.doors.add(new Door( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 4, itemActID));
				}
				//Adds Brainomorpth
				else if(itemID == 16)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0, 
							j + 0.5, 1, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Adds Sentinel enemy
				else if(itemID == 17)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 2, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Adds Mutated Commando
				else if(itemID == 18)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 3, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Adds a Reaper
				else if(itemID == 19)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 4, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Adds Vile Warrior at this location
				else if(itemID == 58)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 7, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Belegoth is added
				else if(itemID == 59)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 8, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Adds secret at this location
				else if(itemID == ItemNames.SECRET.getID())
				{
					Item temp = new Item(5, 
							i + 0.5, 
							0, j + 0.5, itemID, (int)rotation,
							itemActID);

					block.wallItem = temp;
				}
				//Toxic waste
				else if(itemID == ItemNames.TOXICWASTE.getID())
				{
					Item temp = new HurtingBlock( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 0);
					
					Game.hurtingBlocks.add((HurtingBlock)temp);
					
					block.wallEntity = temp;
				}
				//Lava
				else if(itemID == ItemNames.LAVA.getID())
				{
					Item temp = new HurtingBlock( 
							i + 0.5, 
							0.77, j + 0.5, i, j, 1);
					
					Game.hurtingBlocks.add((HurtingBlock)temp);
					
					block.wallEntity = temp;
				}
				//Default Corpse
				else if(itemID == ItemNames.CORPSE.getID())
				{
					Game.corpses.add(new Corpse( 
							i + 0.5,  
							j + 0.5,
							block.height - block.y, 0,0,0,0));
				}
				//Adds Magistrate at this location
				else if(itemID == 45)
				{
					Game.enemies.add(new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 5, rotation, itemActID));
				}
				//The boss MORGOTH
				else if(itemID == 46)
				{
					Enemy temp = new Enemy(
							i + 0.5, 0.77, 
							j + 0.5, 6, rotation, itemActID);
					
					Game.enemies.add(temp);
					block.enemiesOnBlock.add(temp);
				}
				//Explosion. Just create an instant explosion. For effects
				else if(itemID == ItemNames.EXPLOSION.getID())
				{
					new Explosion(i + 0.5, 0, 
							j + 0.5, 0, 0);
				}
				
				//If item supposed to be activated by button
				else if(itemID == ItemNames.ACTIVATEEXP.getID()
						|| itemID == ItemNames.ENEMYSPAWN.getID()
						|| itemID == ItemNames.WALLBEGONE.getID())
				{
					Item temp = new Item(5, 
							i + 0.5, 
							0, j + 0.5, itemID, (int)rotation
							, itemActID);
					
					Game.activatable.add(temp);
				}
				else if(itemID == ItemNames.BREAKABLEWALL.getID())
				{
					Item temp = new Item(10, 
							i + 0.5, 
							block.height - block.y, 
							j + 0.5, itemID, (int)rotation, 0);
					
					block.wallItem = temp;
				}
				else if(itemID == ItemNames.TELEPORTEREXIT.getID()
						|| itemID == ItemNames.TELEPORTERENTER.getID())
				{
					Item temp = new Item(10, 
							i + 0.5, 
							block.height - block.y, 
							j + 0.5, itemID, (int)rotation, itemActID);
					
					Game.teleporters.add(temp);
					
					block.wallEntity = temp;
				}
			}
		}
		
		//Sets the amount of enemies in the map
		Game.enemiesInMap = Game.enemies.size();
	}
	
   /**
    * Get a block at a given cooridinate
    * @param x
    * @param z
    * @return
    */
	public static Block getBlock(int x, int z)
	{
	   /*
	    * If the x and y are larger than the size of the level, then
	    * create it as a solid block to create a solid block border
	    * around the level that will be impassable.
	    */
		if(x < 0 || x >= width || z < 0 || z >= height)
		{
			return new SolidBlock(12, 1, 0, x, z);
		}
		
		//Return block to be generated.
		return blocks[x + (z * width)];
	}
	
	
}
