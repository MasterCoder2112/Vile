package com.vile.levelGenerator;

import java.util.Random;

import com.vile.Game;
import com.vile.ValueHolder;
import com.vile.entities.Button;
import com.vile.entities.Corpse;
import com.vile.entities.Door;
import com.vile.entities.Elevator;
import com.vile.entities.Entity;
import com.vile.entities.Explosion;
import com.vile.entities.HurtingBlock;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;
import com.vile.entities.Position;
import com.vile.graphics.Render3D;

/**
 * Title: Level
 * 
 * @author Alex Byrd Date Updated: 4/29/2017
 *
 *         Generates a level of blocks with set positions for all of them.
 *         Blocks can be nonSolid meaning they are not seen and can be walked
 *         through or have entities spawn in them, or solid where they will have
 *         a certain texture but can't be walked through.
 */
public class Level {
	public static Block[] blocks;
	public static int width = 0;
	public static int height = 0;
	public static double renderDistance = 10000;

	/**
	 * First type of level constructor used to construct a random level.
	 * 
	 * @param w
	 * @param h
	 */
	public Level(int w, int h) {
		width = w;
		height = h;
		blocks = new Block[width * height];

		Random random = new Random();

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				Block block = null;

				// Random wall type is chosen
				int randomWall = random.nextInt(43);

				if (randomWall == 0) {
					randomWall = 1;
				}

				// One in 5 chance the block becomes solid
				if (random.nextInt(100) == 0) {

					block = new Block(24, randomWall, 0, x, z);
				} else {
					block = new Block(0, 0, 0, x, z);
				}

				/*
				 * Generates a new block at a certain position in the level.
				 */
				blocks[x + z * width] = block;
			}
		}

	}

	/**
	 * Uses a 2 dimensional array sent in to create a map of different types of
	 * walls.
	 * 
	 * @param map
	 */
	public Level(ValueHolder[][] map) {
		// Get height of map from 2D array column
		height = map[0].length;

		// Get width from 2D array row
		width = map.length;

		// Array of blocks that make up map
		blocks = new Block[width * height];

		// For each element in the map
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// Each block is null until instantiated
				Block block = null;

				// Audio Queue
				String audioQueue = map[i][j].audioQueue;

				// Door raise Height
				int doorRaiseHeight = map[i][j].doorRaiseHeight;

				// Gets each blocks height
				block = new Block(map[i][j].height, map[i][j].wallID, map[i][j].wallY, i, j);

				// Sets the block at that location in the level.
				blocks[i + j * width] = block;

				for (int k = 0; k < map[i][j].entities.length; k++) {
					// Item activation ID
					int itemActID = map[i][j].itemActIDs[k];

					// Gets the itemID at each location
					int itemID = map[i][j].entities[k];

					// Rotation of entity if there is one
					double rotation = map[i][j].rotations[k];

					// Is this entity above or below the block
					boolean aboveBlock = map[i][j].aboveBlocks[k];

					// If a toxic waste block, add that item to the block as well
					if (map[i][j].wallID == 16 || itemID == ItemNames.TOXICWASTE.getID()) {
						Item temp = new HurtingBlock(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 0);

						Game.hurtingBlocks.add((HurtingBlock) temp);

						block.wallEntities.add(temp);
					}
					// If lava block, add lava entity to block as well
					else if (map[i][j].wallID == 17 || itemID == ItemNames.LAVA.getID()) {
						Item temp = new HurtingBlock(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 1);

						Game.hurtingBlocks.add((HurtingBlock) temp);

						block.wallEntities.add(temp);
					}

					// If water block, add water entity to block as well
					else if (map[i][j].wallID == 25 || itemID == ItemNames.WATER.getID()) {
						Item temp = new Item(10, i + 0.5, (block.y * 4) + block.height, j + 0.5, 70, 0, 0, audioQueue);

						block.wallEntities.add(temp);
					}

					// All Items that are not entities
					if (itemID > ItemNames.AIR.getID() && itemID <= ItemNames.YELLOWKEY.getID()
							|| itemID == ItemNames.SHOTGUN.getID() || itemID == ItemNames.BREAKABLEWALL.getID()
							|| itemID == ItemNames.SECRET.getID()
							|| itemID > 23 && itemID < 44 && itemID != ItemNames.CANISTER.getID()
							|| itemID >= 47 && itemID != ItemNames.BUTTON.getID() && itemID != 58 && itemID != 59
									&& itemID != ItemNames.TURRET.getID() && itemID != ItemNames.TOILET.getID()
									&& itemID != 110 && itemID != 111 && itemID != ItemNames.EXPLOSION.getID()
									&& itemID != ItemNames.TRASH1.getID() && itemID < 124) {
						// Item to be added to the map and block
						Item temp = null;

						// Default y Value of an item
						double yValue = (block.y * 4) + block.height;

						// If below a block it will always be at a value of
						// 0. Whether it gets stuck in the block or not is
						// up to the creator of the map
						if (!aboveBlock) {
							yValue = 0;
						}

						/*
						 * If its not an explosive canister, add it as a normal item. Otherwise add it
						 * as an explosive canister
						 */
						temp = new Item(10, i + 0.5, yValue, j + 0.5, itemID, (int) rotation, itemActID, audioQueue);

						// If the item is solid or another interactable block
						if (temp.isSolid || itemID == ItemNames.BREAKABLEWALL.getID()
								|| itemID == ItemNames.SECRET.getID() || itemID == ItemNames.LINEDEF.getID()) {
							// Set item to being the item that is within this
							// block only if it is solid
							block.wallItems.add(temp);
						}

						// If satellite dish, add to activatable list as well
						if (itemID == ItemNames.RADAR.getID()) {
							Game.activatable.add(temp);
						}
						// If item supposed to be activated by button
						else if (itemID == ItemNames.ACTIVATEEXP.getID() || itemID == ItemNames.ENEMYSPAWN.getID()
								|| itemID == ItemNames.WALLBEGONE.getID() || itemID == ItemNames.RADAR.getID()) {
							Game.activatable.add(temp);
						} else if (itemID == ItemNames.TELEPORTEREXIT.getID()
								|| itemID == ItemNames.TELEPORTERENTER.getID()) {
							Game.teleporters.add(temp);

							block.wallEntities.add(temp);
						}
					}
					// Players spawn
					else if (itemID == 8) {
						/*
						 * Corrects for position in map, and places the player directly in the center of
						 * the block that he/she is placed at in the map file.
						 */
						Player.x = i + 0.5;
						Player.z = j + 0.5;

						// Whether player spawns below or above the block
						if (aboveBlock) {
							if (block.y > 0) {
								Player.y = block.y + block.height + block.baseCorrect + 50;
							} else {
								Player.y = block.height + block.baseCorrect;
							}
						} else {
							Player.y = 0;
							Player.yCorrect = 0;
						}

						Player.blockOn = block;

						Player.invisibility = 100;
						Player.rotation = rotation;
						Render3D.isInitialSound = true;
						Render3D.firstAudio = map[i][j].audioQueue;

						// Adds spawnpoints for multiplayer to tell the clients their starting x, y, and
						// z
						Position p = new Position();
						p.x = i + 0.5;
						p.z = j + 0.5;
						p.y = Player.y;
						p.spawnID = itemActID;
						Game.spawnPoints.add(p);
					}
					// End button or normal button
					else if (itemID == ItemNames.BUTTON.getID()) {
						Game.buttons.add(new Button(i + 0.5, (block.y * 4) + block.height, j + 0.5, itemID, itemActID,
								map[i][j].audioQueue));
					}
					// Lift/elevator
					else if (itemID == ItemNames.ELEVATOR.getID()) {
						Game.elevators.add(new Elevator(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, itemActID,
								doorRaiseHeight));
					}
					// Normal door
					else if (itemID == ItemNames.DOOR.getID()) {
						Game.doors.add(new Door(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 0, itemActID,
								doorRaiseHeight));
					}
					// Red door
					else if (itemID == ItemNames.REDDOOR.getID()) {
						Game.doors.add(new Door(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 1, itemActID,
								doorRaiseHeight));
					}
					// Blue door
					else if (itemID == ItemNames.BLUEDOOR.getID()) {
						Game.doors.add(new Door(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 2, itemActID,
								doorRaiseHeight));
					}
					// Green Door
					else if (itemID == ItemNames.GREENDOOR.getID()) {
						Game.doors.add(new Door(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 3, itemActID,
								doorRaiseHeight));
					}
					// Yellow door
					else if (itemID == ItemNames.YELLOWDOOR.getID()) {
						Game.doors.add(new Door(i + 0.5, (block.y * 4) + block.height, j + 0.5, i, j, 4, itemActID,
								doorRaiseHeight));
					}
					// Adds Brainomorpth
					else if (itemID == 16) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 1, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Sentinel enemy
					else if (itemID == 17) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 2, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Mutated Commando
					else if (itemID == 18) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 3, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds a Reaper
					else if (itemID == 19) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 4, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Vile Warrior at this location
					else if (itemID == 58) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 7, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Belegoth is added
					else if (itemID == 59) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 8, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Magistrate at this location
					else if (itemID == 45) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 5, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// The boss MORGOTH
					else if (itemID == 46) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + block.height);

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 6, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Watcher enemy
					else if (itemID == 111) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 9, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Marine entity
					else if (itemID == 124) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Random random = new Random();
						int marineType = random.nextInt(6) + 10;

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, marineType, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Chair entity
					else if (itemID == ItemNames.CHAIR.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 16, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Toilet entity
					else if (itemID == ItemNames.TOILET.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 17, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Turret entity
					else if (itemID == ItemNames.TURRET.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 18, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Armored Menace entity
					else if (itemID == 125) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 19, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Blind Charger entity
					else if (itemID == 126) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 20, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Damned Soul entity
					else if (itemID == 127) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 21, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Dark Strider entity
					else if (itemID == 128) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 22, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Deceptor entity
					else if (itemID == 129) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 23, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Heavy Zombie entity
					else if (itemID == 130) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 24, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Moss Springer entity
					else if (itemID == 131) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 25, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Tetra Destructor entity
					else if (itemID == 132) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 26, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds Explosive Canister entity
					else if (itemID == ItemNames.CANISTER.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 27, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Adds trash can entity
					else if (itemID == ItemNames.TRASH1.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = -((block.y * 4) + (block.height / 12));

						if (!aboveBlock) {
							yValue = 0;
						}

						Entity temp = new Entity(i + 0.5, yValue, j + 0.5, 28, rotation, itemActID);

						Game.entities.add(temp);
						block.entitiesOnBlock.add(temp);
					}
					// Default Corpse
					else if (itemID == ItemNames.CORPSE.getID()) {
						// Sets the y value based on the aboveBlock boolean
						double yValue = block.height + (block.y * 4) + block.baseCorrect;

						if (!aboveBlock) {
							yValue = 0;
						}

						Game.corpses.add(new Corpse(i + 0.5, j + 0.5, yValue, 0, 0, 0, 0, false));
					}
					// Explosion. Just create an instant explosion. For effects
					else if (itemID == ItemNames.EXPLOSION.getID()) {
						new Explosion(i + 0.5, 0, j + 0.5, 0, 0);
					}
				}
			}
		}

		// Sets the amount of enemies in the map
		Game.enemiesInMap = Game.entities.size();
	}

	/**
	 * Get a block at a given cooridinate
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public static Block getBlock(int x, int z) {
		/*
		 * If the x and y are larger than the size of the level, then create it as a
		 * solid block to create a solid block border around the level that will be
		 * impassable.
		 */
		if (x < 0 || x >= width || z < 0 || z >= height) {
			return new SolidBlock(12, 1, 0, x, z);
		}

		// Return block to be generated.
		return blocks[x + (z * width)];
	}

}
