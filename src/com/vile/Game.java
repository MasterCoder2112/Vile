package com.vile;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import com.vile.entities.Bullet;
import com.vile.entities.Button;
import com.vile.entities.Cartridge;
import com.vile.entities.Corpse;
import com.vile.entities.Door;
import com.vile.entities.Elevator;
import com.vile.entities.EnemyFire;
import com.vile.entities.Entity;
import com.vile.entities.EntityParent;
import com.vile.entities.Explosion;
import com.vile.entities.HitSprite;
import com.vile.entities.HurtingBlock;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.PhaseCannon;
import com.vile.entities.Pistol;
import com.vile.entities.Player;
import com.vile.entities.Position;
import com.vile.entities.Projectile;
import com.vile.entities.RocketLauncher;
import com.vile.entities.Scepter;
import com.vile.entities.ServerPlayer;
import com.vile.entities.Shotgun;
import com.vile.entities.Sword;
import com.vile.entities.Weapon;
import com.vile.graphics.Render3D;
import com.vile.input.Controller;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Game
 * 
 * @author Alex Byrd Date Updated: 5/14/2017
 *
 *         Keeps track of all the in game controls (using keyListener), also
 *         keeps track of game time using the time variable and adding to it in
 *         the tick method. It also sets up the random level generated each time
 *         the game is ran with a given size (determined by the launcher).
 * 
 *         It also calls the performActions method in controller to perform all
 *         the actions within the game that the player activates using the keys.
 * 
 *         Keeps track of the Player and some of the objects in the game.
 *         Because of optimization reasons and speed upgrades, most objects like
 *         enemies and items and such are tracked and updated in the Render3D
 *         class since that class needs to run through all of theme anyway to
 *         render them to the screen. So instead of running through all of the
 *         enemies and items twice, it just does it once in there.
 */
public class Game implements Runnable {
	// Time games been running in ticks
	public static int time;

	// Handles all controls and player movements
	public Controller controls;

	/*
	 * A ValueHolder is an Object which holds multiple values for a new map to read
	 * in such as the block type, block height, item on that block, and eventually
	 * things like brightness of that block in the map.
	 */
	private static ValueHolder[][] map;

	// The level the game has loaded up
	public static Level level;

	// Skill level
	public static int skillMode = 2;

	// Map number default is 1. Unless set otherwise
	public static int mapNum = 0;

	// Map floor and ceiling ID's
	public static int mapFloor = 0;
	public static int mapCeiling = 0;

	// Map name and map audio name
	public static String mapName = "";
	public static String mapAudio = "";

	// Keeps track of enemies in the map, secrets in the map, and what
	// secrets have been found.
	public static int secretsInMap = 0;
	public static int enemiesInMap = 0;
	public static int secretsFound = 0;

	// Keeps track of all different types of items and entities in the map
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public static ArrayList<EnemyFire> enemyProjectiles = new ArrayList<EnemyFire>();
	public static ArrayList<Button> buttons = new ArrayList<Button>();
	public static ArrayList<Door> doors = new ArrayList<Door>();
	public static ArrayList<Elevator> elevators = new ArrayList<Elevator>();
	public static ArrayList<HurtingBlock> hurtingBlocks = new ArrayList<HurtingBlock>();
	public static ArrayList<Corpse> corpses = new ArrayList<Corpse>();
	public static ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	public static ArrayList<Item> solidItems = new ArrayList<Item>();
	public static ArrayList<Item> activatable = new ArrayList<Item>();
	public static ArrayList<Item> teleporters = new ArrayList<Item>();
	public static ArrayList<HitSprite> sprites = new ArrayList<HitSprite>();

	// Multiplayer stuff
	public static ArrayList<ServerPlayer> otherPlayers = new ArrayList<ServerPlayer>();
	public static ArrayList<Button> activatedButtons = new ArrayList<Button>();
	public static ArrayList<Door> activatedDoors = new ArrayList<Door>();
	public static ArrayList<Elevator> activatedElevators = new ArrayList<Elevator>();
	public static ArrayList<Bullet> bulletsAdded = new ArrayList<Bullet>();
	public static ArrayList<Item> itemsAdded = new ArrayList<Item>();
	public static Corpse playerCorpse;

	// Don't worry about really sending this data, but it's still needed for
	// multiplayer
	public static ArrayList<Position> spawnPoints = new ArrayList<Position>();

	// For Smile resource pack only
	public static ArrayList<Item> happySavers = new ArrayList<Item>();

	// Keeps track of all controls, and whether they are pressed or not
	private static boolean key[];

	// Calculated Size of map to render
	private int calculatedSize;

	// Is freeze mode on or not
	public static boolean freezeMode = false;

	// Holds the display class you're using
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
	public static boolean use;
	public static boolean weaponSlot0;
	public static boolean weaponSlot1;
	public static boolean weaponSlot2;
	public static boolean weaponSlot3;
	public static boolean weaponSlot4;
	public static boolean weaponSlot5;
	public static boolean upgradeWeapon;
	public static boolean recallFriendlies;
	public static boolean consoleOpen;
	/* End Game Actions ***********************************************/

	/* Game Action Keys ***********************************************/
	public static int fowardKey = KeyEvent.VK_W;
	public static int backKey = KeyEvent.VK_S;
	public static int leftKey = KeyEvent.VK_A;
	public static int rightKey = KeyEvent.VK_D;
	public static int turnLeftKey = KeyEvent.VK_LEFT;
	public static int turnRightKey = KeyEvent.VK_RIGHT;
	public static int turnUpKey = KeyEvent.VK_UP;
	public static int turnDownKey = KeyEvent.VK_DOWN;
	public static int shootKey = KeyEvent.VK_V;
	public static int pauseKey = KeyEvent.VK_ESCAPE;
	public static int runKey = KeyEvent.VK_SHIFT;
	public static int crouchKey = KeyEvent.VK_C;
	public static int jumpKey = KeyEvent.VK_SPACE;
	public static int fpsShowKey = KeyEvent.VK_F;
	public static int reloadingKey = KeyEvent.VK_R;
	public static int useKey = KeyEvent.VK_E;
	public static int upgradeWeaponKey = KeyEvent.VK_U;
	public static int recallFriendliesKey = KeyEvent.VK_Q;
	public static int weaponSlot0Key = KeyEvent.VK_1;
	public static int weaponSlot1Key = KeyEvent.VK_2;
	public static int weaponSlot2Key = KeyEvent.VK_3;
	public static int weaponSlot3Key = KeyEvent.VK_4;
	public static int weaponSlot4Key = KeyEvent.VK_5;
	public static int weaponSlot5Key = KeyEvent.VK_6;
	public static int weaponSlot6Key = KeyEvent.VK_7;
	public static int consoleOpenKey = 192; // Key ` ID Num
	/* End Game Action Keys *******************************************/

	/**
	 * Sets up the level and the Controller object. The level size is determined by
	 * the Display.levelSize variable which is determined by the launcher. Also adds
	 * the initial enemy to the game and adds various item types to the map
	 * depending on the map size.
	 */
	public Game(Display display, boolean newStartMap, String newMapName) {
		// Set the games display object to the display sent in
		this.display = display;

		// Resets all the lists
		resetLists();

		if (!FPSLauncher.loadingGame) {
			// If a survival map
			if (FPSLauncher.gameMode == 1) {
				setUpSurvival();
			} else {
				// Load either the first map or custom map if that is chosen
				mapNum = 0;
				loadNextMap(newStartMap, newMapName);
			}
		} else {
			loadGame();
		}

		// Set up the controller to control player movements and actions
		controls = null;
		controls = new Controller();

		key = null;
	}

	/**
	 * Ticks each time the game renders, therefore keeping track of game time, and
	 * all the key events that happen within the game.
	 * 
	 * @param key2
	 */
	@SuppressWarnings("unchecked")
	public void tick(boolean key2[]) {
		// Keeps track of how many ticks the game has gone on for
		time++;

		// Just in case you go above a billion ticks
		if (time > 1000000000) {
			time = 0;
		}

		// This was to fix some bug way back, but I can't remember what
		// it was so... this is just here.
		key = key2;

		// Keeps track of what keys switch what booleans
		foward = key[fowardKey];
		back = key[backKey];
		left = key[leftKey];
		right = key[rightKey];
		turnLeft = key[turnLeftKey];
		turnRight = key[turnRightKey];
		pause = key[pauseKey];
		run = key[runKey];
		crouch = key[crouchKey];
		jump = key[jumpKey];
		fpsShow = key[fpsShowKey];
		turnUp = key[turnUpKey];
		turnDown = key[turnDownKey];
		reloading = key[reloadingKey];
		shoot = key[shootKey];
		use = key[useKey];
		weaponSlot0 = key[weaponSlot0Key];
		weaponSlot1 = key[weaponSlot1Key];
		weaponSlot2 = key[weaponSlot2Key];
		weaponSlot3 = key[weaponSlot3Key];
		weaponSlot4 = key[weaponSlot4Key];
		weaponSlot5 = key[weaponSlot5Key];
		upgradeWeapon = key[upgradeWeaponKey];
		recallFriendlies = key[recallFriendliesKey];
		consoleOpen = key[consoleOpenKey];

		// System.out.println(Display.consoleOpen);

		// TODO For Multiplayer, do not check for items unless you are hosting the game.
		// All clients need to do is send their positional data to the server and then
		// retrieve the data about everything from the server before rendering.
		// Bullets will be added to the server when they are shot as well.

		// Always request focus
		display.requestFocus();

		if (Display.clearedLevel) {
			// If time is not 0, add to it
			if (Controller.time != 0) {
				Controller.time++;
			}

			// If 25 ticks have passed, set time to 0
			if (Controller.time == 25) {
				Controller.time = 0;
			}

			if (Controller.time == 0) {
				// If any key is pressed when the clear level screen is up
				// then load the next map instead.
				for (int i = 0; i < key.length; i++) {
					if (key[i] == true) {
						loadNextMap(false, "");
						break;
					}
				}
			}

			return;
		}

		// If console is open, any keys pressed will automatically be converted
		// to letters in the console except for the escape key and the console
		// open key to close the console.
		if (Display.consoleOpen) {

			// If time is not 0, add to it
			if (Controller.time != 0) {
				Controller.time++;
			}

			// If 25 ticks have passed, set time to 0
			if (Controller.time == 25) {
				Controller.time = 0;
			}

			if ((pause || consoleOpen) && Controller.time == 0) {
				Controller.time++;
				Display.consoleOpen = false;
			}

			return;
		}

		if (Player.drunkLevels > 4000) {
			Display.messages.add(new PopUp("Blacked Out! Please sober up..."));
		}

		Collections.sort(entities);

		// TODO Make sure above works.

		/*
		 * Keeps player within the first 2 PI of rotation so that the Players rotation
		 * in respect to the enemies is kept in tact.
		 */
		if (Player.rotation >= (Math.PI * 2)) {
			Player.rotation -= (Math.PI * 2);
		} else if (Player.rotation < 0) {
			Player.rotation = (Math.PI * 2) + Player.rotation;
		}

		/*
		 * If player just drowned in tears, then determine whether he/she can be made
		 * happy again, and if he/she can't then play the player drowning in tears sound
		 * and set player status to not alive.
		 * 
		 * Otherwise reset the player with 100 happiness, but no positivity.
		 */
		if (Player.health <= 0 && Player.alive) {
			if (Player.resurrections == 0) {
				// Player is no longer alive
				Player.alive = false;

				SoundController.playerDeath.playAudioFile(0);

				if (Display.gameType == 1) {
					// Drop current weapon in multiplayer when you are killed
					if (Player.weaponEquipped == 0) {
						itemsAdded.add(new Item(10, Player.x, Player.y, Player.z, ItemNames.PISTOL.itemID, 0, 0, "-1"));
					} else if (Player.weaponEquipped == 1) {
						itemsAdded
								.add(new Item(10, Player.x, Player.y, Player.z, ItemNames.SHOTGUN.itemID, 0, 0, "-1"));
					} else if (Player.weaponEquipped == 2) {
						itemsAdded.add(
								new Item(10, Player.x, Player.y, Player.z, ItemNames.PHASECANNON.itemID, 0, 0, "-1"));
					} else {
						itemsAdded.add(new Item(10, Player.x, Player.y, Player.z, ItemNames.ROCKETLAUNCHER.itemID, 0, 0,
								"-1"));
					}

					playerCorpse = new Corpse(Player.x, Player.z, Player.y, 0, Player.xEffects, Player.zEffects,
							Player.yEffects, false);

					playerCorpse.clientID = Player.ID;

					Player.deaths++;

				}

				// If survival mode, see if its a new max number of kills
				// or not and if it is then save the new max.
				if (FPSLauncher.gameMode == 1) {
					if (Player.kills > Player.maxKills) {
						Player.maxKills = Player.kills;
					}
				}
			} else {
				// Get rid of happiness restorer item the player has
				Player.resurrections--;

				// Display that the player was restored to full happiness
				Display.messages.add(new PopUp("RESURRECTED WITH SKULL!"));

				// Reset values, and give brief immortality
				Player.health = 100;
				Player.armor = 0;
				Player.immortality = 100;
			}
		}

		// If players health is greater than the max health the player
		// can have for some reason, set health to max health
		if (Player.health > Player.maxHealth) {
			Player.health = Player.maxHealth;
		}

		// Keeps playing music audio as long as game is running
		display.playAudio(mapAudio);

		// Update players current weapon
		if (Player.weaponEquipped == 0) {
			((Sword) (Player.weapons[0])).updateValues();
		} else if (Player.weaponEquipped == 1) {
			((Pistol) (Player.weapons[1])).updateValues();
		} else if (Player.weaponEquipped == 2) {
			((Shotgun) (Player.weapons[2])).updateValues();
		} else if (Player.weaponEquipped == 3) {
			((PhaseCannon) (Player.weapons[3])).updateValues();
		} else if (Player.weaponEquipped == 4) {
			((RocketLauncher) (Player.weapons[4])).updateValues();
		} else if (Player.weaponEquipped == 5) {
			((Scepter) (Player.weapons[5])).updateValues();
		}

		// Run Single Player game events only if this is a single player game.
		if (Display.gameType == 2) {
			runSingleGameEvents();
		}
		// If game is being ran as a host
		else if (Display.gameType == 0 && Display.hostTick > 0) {
			Display.hostTick--;
			runHostGameEvents();
			return;
		}

		// Host shouldn't run this
		if (Display.gameType != 0) {
			// Perform all actions depending on keys pressed.
			controls.performActions(this);
		}
	}

	/**
	 * Runs all the game events for a multiplayer game as the host.
	 */
	public void runHostGameEvents() {

		// TODO For now enemies are disabled in multiplayer
		// Sort enemies according to their distance to you but only if
		// not in survival
		// if (FPSLauncher.gameMode == 1) {
		// Collections.sort(enemies);
		// }

		/*
		 * Check all buttons to see if any are activated, and if they are perform the
		 * desired action.
		 */
		for (int i = 0; i < buttons.size(); i++) {
			Button temp = buttons.get(i);

			// If an end button, load the next map
			if (temp.itemActivationID == 0 && temp.activated) {
				mapNum++;
				Controller.time++;
				Display.clearedLevel = true;
			}
			// If a normal button
			else if (temp.itemActivationID > 0 && temp.activated) {
				// De-activate the button
				temp.activated = false;

				Block button = Level.getBlock((int) temp.x, (int) temp.z);

				// Change the wall texture to show the button has been activated
				// Only if the original wall it was on is a button
				if (button.wallID == 26) {
					button.wallID = -1;
				}

				// If special ID, turn on lights to make map brighter
				if (temp.itemActivationID == 1221) {
					Render3D.renderDistanceDefault = 12000;
				}

				// Search through all the doors
				for (int k = 0; k < Game.doors.size(); k++) {
					Door door = Game.doors.get(k);

					// If door has the same activation ID as the
					// button then activate it.
					if (door.itemActivationID == temp.itemActivationID) {
						/*
						 * If the itemActivationID is the special ID, then just stop the door from
						 * automatically opening and closing. Otherwise activate the door as normal.
						 */
						if (door.itemActivationID == 2112) {
							// Hoping no one uses this id, but
							// this stops the door from automatically
							// opening and closing continuously.
							door.itemActivationID = 1221;
						}
						// If this special ID, activate it to continue to
						// move
						else if (door.itemActivationID == 1221) {
							door.itemActivationID = 2112;
							door.activated = true;
							door.stayOpen = false;
						} else {
							door.activated = true;
						}
					}
				}

				// Search through all the elevators
				for (int k = 0; k < Game.elevators.size(); k++) {
					Elevator e = Game.elevators.get(k);

					// If elevator has the same activation ID as the
					// button then activate it.
					if (e.itemActivationID == temp.itemActivationID) {
						/*
						 * If the itemActivationID is the special ID, then just stop the elevator from
						 * automatically moving. Otherwise activate the elevator as normal.
						 */
						if (e.itemActivationID == 2112) {
							// Hoping no one uses this id, but
							// this stops the elevator from automatically
							// moving continuously.
							e.itemActivationID = 1221;
						}
						// If this special ID, activate it to continue to
						// move
						else if (e.itemActivationID == 1221) {
							e.itemActivationID = 2112;
							e.activated = true;
						} else {
							e.activated = true;
						}
					}
				}

				// Stores Items to be deleted
				ArrayList<Item> tempItems2 = new ArrayList<Item>();

				// Scan all activatable items
				for (int j = 0; j < activatable.size(); j++) {
					Item item = activatable.get(j);

					// If Item is a Com satellite dish, activate it and
					// state that it is activated
					if (item.itemID == ItemNames.RADAR.getID() && !item.activated
							&& temp.itemActivationID == item.itemActivationID) {
						item.activated = true;
						Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
						SoundController.uplink.playAudioFile(0);
					} else {
						// If item is enemy spawnpoint, then spawn the
						// enemy, and add the item to the arraylist of
						// items to be deleted
						if (item.itemID == ItemNames.ENEMYSPAWN.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							enemiesInMap++;
							addEnemy(item.x, item.z, item.rotation);
							tempItems2.add(item);
						}
						// If Explosion has same activation ID of the button
						// then activate it
						else if (item.itemID == ItemNames.ACTIVATEEXP.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							new Explosion(item.x, item.y, item.z, 0, 0);
							tempItems2.add(item);
						}
						// If it gets rid of a wall, delete the wall and create an
						// air wall in its place.
						else if (item.itemID == ItemNames.WALLBEGONE.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							Block block2 = Level.getBlock((int) item.x, (int) item.z);

							// Block is effectively no longer there
							block2.height = 0;
							block2.wallEntities = new ArrayList<Item>();

							tempItems2.add(item);
						}
					}
				}

				// Remove all the items that need to be deleted now
				for (int j = 0; j < tempItems2.size(); j++) {
					Item temp2 = tempItems2.get(j);

					temp2.removeItem();
				}
			}
		}

		/*
		 * Check all door entities each tick through the game, and if the door is
		 * activated, continue to move the door in whatever way it was moving before.
		 * Otherwise do nothing to it.
		 */
		for (int i = 0; i < Game.doors.size(); i++) {
			Door door = Game.doors.get(i);

			if (door.activated || door.itemActivationID == 2112) {
				door.move();
			}
		}

		// Update all elevator entities, if its not already
		// moving, then there is not need to continue moving it
		for (int i = 0; i < Game.elevators.size(); i++) {
			Elevator elevator = Game.elevators.get(i);

			if (elevator.activated || elevator.itemActivationID == 2112) {
				elevator.move();
			}
		}

		// TODO For now enemies are not going to be added to the server.
		// Tick through all the enemies
		/*
		 * for (int i = 0; i < enemies.size(); i++) { Enemy enemy = Game.enemies.get(i);
		 * 
		 * // Deactivate the enemies if the player is dead and they have // no other
		 * target if (Player.health <= 0 && enemy.targetEnemy == null) { enemy.activated
		 * = false; }
		 * 
		 * // tick the enemy, updating its values and such enemy.tick(enemy);
		 * 
		 * // Update the enemies movements enemy.move();
		 * 
		 * /* If the enemy is within range of the door, open the door because enemies
		 * are smarter than just letting a door stop them from killing you.
		 * 
		 * Also the enemy has to be activated to open doors.
		 * 
		 * Flying enemies can't open doors because they can just fly over them
		 */
		/*
		 * if (!enemy.canFly && enemy.activated) { for (int a = 0; a <
		 * Game.doors.size(); a++) { Door door = Game.doors.get(a);
		 * 
		 * // If door is in range of enemy, have it open as long // as it is not a door
		 * requiring a key if (Math.abs(door.getZ() - enemy.zPos) <= 1 &&
		 * Math.abs(door.getX() - enemy.xPos) <= 1 && door.itemActivationID == 0 &&
		 * (-enemy.yPos < 2)) { if (door.doorType == 0) { door.activated = true; } } } }
		 * 
		 * /* Attempt to attack the player as long as the player is alive. The method
		 * itself will determine what attack if any the enemy will use though.
		 */
		// enemy.attack(Player.y);
		// }

		// All entities are done checking if player is in their sight
		// Entity.checkSight = false;

		// TODO Item code for server
		// Run through all players on the server, seeing if they have picked up an item
		// or not.
		for (int s = 0; s < Game.otherPlayers.size(); s++) {
			// Current Player
			ServerPlayer sP = Game.otherPlayers.get(s);
			// Items to delete
			ArrayList<Item> tempItems = new ArrayList<Item>();

			// ALL ITEM UPDATING
			for (int i = 0; i < Game.items.size(); ++i) {
				Item item = Game.items.get(i);

				// Block item is over or on
				Block temp = Level.getBlock((int) item.x, (int) item.z);

				// Difference between the item and the top of the block
				double difference = item.y - (temp.height + (temp.y * 4) + temp.baseCorrect);

				// Is this item on top of another solid item
				boolean onItem = false;

				for (Item it : temp.wallItems) {
					// If on a solid object, place it on top, and only if it has hit
					// the top of the item as well.
					if (it.isSolid && !item.equals(it) && item.y - (it.y + it.height) < 6) {
						item.y = it.y + it.height + 2;
						onItem = true;

						/*
						 * If item is above the block, then set its height from the top of the block to
						 * be the height of the solid item it is on greater than the top of the block.
						 * If not though, it is below the block and there should be no difference in
						 * height as it will not be falling.
						 */
						if (item.aboveBlock) {
							difference += (item.y - it.height);
						} else {
							difference = 0;
						}
					}
				}

				/*
				 * If the items y is greater than the blocks height plus its y value, then
				 * decrease the items y until it reaches the ground.
				 */
				if (difference > 1) {
					item.y -= 1;
				} else {
					/*
					 * If the item is within the block, and not on top of the block, and the item is
					 * not a door, then set the height to being the top of the block.
					 */
					if (item.y >= (temp.y * 4) && difference > (-1 - temp.baseCorrect) && item.aboveBlock) {
						item.y = temp.height + (temp.y * 4) + temp.baseCorrect;
					} else {
						// If landed on flat ground and not on top of a solid item
						if (!onItem) {
							item.y = 0;
						}
					}
				}

				double distance = Math.sqrt(((Math.abs(sP.x - item.x)) * (Math.abs(sP.x - item.x)))
						+ ((Math.abs(sP.z - item.z)) * (Math.abs(sP.z - item.z))));

				/*
				 * If item has been picked up in respawnable mode, tick the count of how long it
				 * has been picked up, and after a certain amount of ticks, allow it to appear
				 * again and be able to be picked up. Also only run once per tick
				 */
				if (item.pickedUp) {
					item.tick();

					if (item.tickCount > 2000) {
						item.tickCount = 0;
						item.pickedUp = false;

						sP.audioToPlay.add("teleportation");
						sP.audioDistances.add((int) distance);
					}
				}

				// If the item is at least 0.7 units away, and its not
				// a secret or linedef, and the player is not in noClip mode
				if (distance <= 0.7 && Math.abs(item.y - sP.y) <= 6 && item.itemID != ItemNames.SECRET.itemID
						&& item.itemID != ItemNames.LINEDEF.itemID && !sP.noClipOn && !item.pickedUp) {
					// Was the object activated?
					boolean activated = item.activateServer(sP);
					/*
					 * If the item was activated remove it. Otherwise keep it in the map. Also play
					 * audio queue is there is one
					 */
					if (activated) {

						if (Display.itemsRespawn) {
							item.pickedUp = true;
						} else {
							tempItems.add(item);
						}

						// Activate the audio queue if there is one
						item.activateAudioQueue();
					}

					// If item activates other items, then if the player walks over the
					// item then activate other items whether the item was picked up
					// or not. Some items cannot be activated by walking over them.
					if (item.itemActivationID > 0 && item.itemID != ItemNames.ENEMYSPAWN.getID()
							&& item.itemID != ItemNames.ACTIVATEEXP.getID()
							&& item.itemID != ItemNames.BREAKABLEWALL.itemID
							&& item.itemID != ItemNames.WALLBEGONE.itemID && item.itemID != ItemNames.ACTIVATEEXP.itemID
							&& item.itemID != ItemNames.BUTTON.itemID && item.itemID != ItemNames.DOOR.itemID
							&& item.itemID != ItemNames.GREENDOOR.itemID && item.itemID != ItemNames.REDDOOR.itemID
							&& item.itemID != ItemNames.YELLOWDOOR.itemID && item.itemID != ItemNames.BLUEDOOR.itemID
							&& item.itemID != ItemNames.ELEVATOR.itemID) {
						// De-activate the button
						item.activated = false;

						Block blockOn = Level.getBlock((int) item.x, (int) item.z);

						// Change the wall texture to show the button has been activated
						// Only if the original wall it was on is a button
						if (blockOn.wallID == 26) {
							blockOn.wallID = 43;
						}

						// If special ID, turn on lights to make map brighter
						if (item.itemActivationID == 1221) {
							Render3D.renderDistanceDefault += 5000;
						}

						// Search through all the doors
						for (int k = 0; k < Game.doors.size(); k++) {
							Door door = Game.doors.get(k);

							// If door has the same activation ID as the
							// button then activate it.
							if (door.itemActivationID == item.itemActivationID) {
								/*
								 * If the itemActivationID is the special ID, then just stop the door from
								 * automatically opening and closing. Otherwise activate the door as normal.
								 */
								if (door.itemActivationID == 2112) {
									// Hoping no one uses this id, but
									// this stops the door from automatically
									// opening and closing continuously.
									door.itemActivationID = 1221;
								}
								// If this special ID, activate it to continue to
								// move
								else if (door.itemActivationID == 1221) {
									door.itemActivationID = 2112;
									door.activated = true;
									door.stayOpen = false;
								} else {
									door.activated = true;
								}
							}
						}

						// Search through all the elevators
						for (int k = 0; k < Game.elevators.size(); k++) {
							Elevator e = Game.elevators.get(k);

							// If elevator has the same activation ID as the
							// button then activate it.
							if (e.itemActivationID == item.itemActivationID) {
								/*
								 * If the itemActivationID is the special ID, then just stop the elevator from
								 * automatically moving. Otherwise activate the elevator as normal.
								 */
								if (e.itemActivationID == 2112) {
									// Hoping no one uses this id, but
									// this stops the elevator from automatically
									// moving continuously.
									e.itemActivationID = 1221;
								}
								// If this special ID, activate it to continue to
								// move
								else if (e.itemActivationID == 1221) {
									e.itemActivationID = 2112;
									e.activated = true;
								} else {
									e.activated = true;
								}
							}
						}

						// Stores Items to be deleted
						ArrayList<Item> tempItems2 = new ArrayList<Item>();

						// Scan all activatable items
						for (int j = 0; j < Game.activatable.size(); j++) {
							Item item2 = Game.activatable.get(j);

							// If Item is a Com satellite dish, activate it and
							// state that it is activated
							if (item2.itemID == ItemNames.RADAR.getID() && !item2.activated
									&& item.itemActivationID == item2.itemActivationID) {
								item2.activated = true;
								Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
								SoundController.uplink.playAudioFile(0);
							} else {
								// If item is enemy spawnpoint, then spawn the
								// enemy, and add the item to the arraylist of
								// items to be deleted
								if (item2.itemID == ItemNames.ENEMYSPAWN.getID()
										&& item.itemActivationID == item2.itemActivationID) {
									Game.enemiesInMap++;
									addEnemy(item2.x, item2.z, item2.rotation);
									tempItems2.add(item2);
								}
								// If Explosion has same activation ID of the button
								// then activate it
								else if (item2.itemID == ItemNames.ACTIVATEEXP.getID()
										&& item.itemActivationID == item2.itemActivationID) {
									new Explosion(item2.x, item2.y, item2.z, 0, 0);
									tempItems2.add(item2);
								}
								// If it gets rid of a wall, delete the wall and create an
								// air wall in its place.
								else if (item2.itemID == ItemNames.WALLBEGONE.getID()
										&& item.itemActivationID == item2.itemActivationID) {
									Block block2 = Level.getBlock((int) item2.x, (int) item2.z);

									// Block is effectively no longer there
									block2.height = 0;

									block2.wallEntities = null;

									tempItems2.add(item2);
								}
							}
						}

						// Remove all the items that need to be deleted now
						for (int j = 0; j < tempItems2.size(); j++) {
							Item temp2 = tempItems2.get(j);

							temp2.removeItem();
						}
					}
				}
			}

			// Remove all items within list
			for (int j = 0; j < tempItems.size(); ++j) {
				Item temp = tempItems.get(j);

				// Removes all references of item in game
				temp.removeItem();
			}
		}

		for (int i = 0; i < Game.corpses.size(); i++) {
			Corpse corpse = Game.corpses.get(i);

			// Block corpse is on
			Block temp = Level.getBlock((int) corpse.xPos, (int) corpse.zPos);

			// The top of the block
			double topOfBlock = (temp.height + (temp.y * 4) + temp.baseCorrect);

			// Difference between the item and the top of the block
			double difference = corpse.yPos - topOfBlock;

			/*
			 * If the items y is greater than the blocks height plus its y value, then
			 * decrease the items y until it reaches the ground.
			 */
			if (corpse.yPos - 1 > topOfBlock) {
				corpse.yPos -= 1;
			} else {
				/*
				 * If not a door, and the y value of the corpse is inside the block, but towards
				 * the top, then place it on top of the block. Otherwise, it shall stay on
				 * ground level.
				 */
				if (corpse.yPos >= (temp.y * 4) && difference > (-1 - temp.baseCorrect)) {
					corpse.yPos = topOfBlock;
				} else {
					// If on flat ground, the height is 3. This is
					// so the textures don't go through the ground.
					corpse.yPos = 0;
				}
			}

			corpse.tick();

			/*
			 * If in Death cannot hurt me mode, the corpses will resurrect on their own
			 * after 10000 ticks.
			 */
			// if (FPSLauncher.modeChoice == 4) {
			// If 10000 ticks have passed with correction for the fps
			// and the corpse is not just a default corpse
			// if (corpse.time > (10000 / ((Display.fps / 30) + 1)) && corpse.enemyID != 0)
			// {
			/*
			 * Reconstruct enemy depending on the ID the corpse was before it died. Also
			 * activate the new resurrected enemy.
			 */
			// Enemy newEnemy = new Enemy(corpse.xPos, 0, corpse.zPos, corpse.enemyID, 0,
			// 0);

			// Activate this new enemy
			// newEnemy.activated = true;

			// Add enemy to the game
			// Game.enemies.add(newEnemy);

			// Remove the corpse
			// Game.corpses.remove(corpse);

			// Add to enemies in the map
			// Game.enemiesInMap++;

			// Play teleportation/respawn sound effect
			// SoundController.teleportation.playAudioFile(newEnemy.distanceFromPlayer);
			// }
			// }
		}

		// Tick all explosions
		for (int i = 0; i < Game.explosions.size(); i++) {
			Explosion explosion = Game.explosions.get(i);

			explosion.tick();
		}

		/*
		 * Mainly for shotgun bullets. Since they are in a spread shot, and could
		 * potentially hit the enemy at the same time, then make it so that the sound of
		 * hitting the enemy doesn't play several times in the same tick otherwise it'll
		 * be a super loud sound, and it's annoying. This checks for that and prevents
		 * it.
		 * 
		 * These reset the values to false each tick.
		 */
		Projectile.bossHit = false;
		Projectile.enemyHit = false;

		/*
		 * Moves the game bullets in a given direction and checks for collisions
		 */
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);

			if (bullet.ID <= 2) {
				while (bullet.move()) {

				}
			} else {
				bullet.move();
			}
		}

		/*
		 * Moves enemy projectiles. Also checks for collisions
		 */
		/*
		 * for (int i = 0; i < Game.enemyProjectiles.size(); i++) { EnemyFire temp =
		 * Game.enemyProjectiles.get(i);
		 * 
		 * temp.move(); }
		 */

		/*
		 * Ticks and renders all extra sprites in the game
		 */
		for (int i = 0; i < Game.sprites.size(); i++) {
			HitSprite hS = Game.sprites.get(i);

			hS.tick();
		}
	}

	/**
	 * Runs all the games events, such as updating entity movement and variables,
	 * item pickups, etc... Only the host can run these events. Player is
	 * automatically a host in single player games.
	 */
	public void runSingleGameEvents() {
		// Sort enemies according to their distance to you but only if
		// not in survival
		if (FPSLauncher.gameMode == 1) {
			Collections.sort(entities);
		}

		/*
		 * Check all buttons to see if any are activated, and if they are perform the
		 * desired action.
		 */
		for (int i = 0; i < buttons.size(); i++) {
			Button temp = buttons.get(i);

			// If an end button, load the next map
			if (temp.itemActivationID == 0 && temp.activated) {
				mapNum++;
				Controller.time++;
				Display.clearedLevel = true;
			}
			// If a normal button
			else if (temp.itemActivationID > 0 && temp.activated) {
				// De-activate the button
				temp.activated = false;

				Block button = Level.getBlock((int) temp.x, (int) temp.z);

				// Change the wall texture to show the button has been activated
				// Only if the original wall it was on is a button
				if (button.wallID == 26) {
					button.wallID = -1;
				}

				// If special ID, turn on lights to make map brighter
				if (temp.itemActivationID == 1221) {
					Render3D.renderDistanceDefault = 12000;
				}

				// Search through all the doors
				for (int k = 0; k < Game.doors.size(); k++) {
					Door door = Game.doors.get(k);

					// If door has the same activation ID as the
					// button then activate it.
					if (door.itemActivationID == temp.itemActivationID) {
						/*
						 * If the itemActivationID is the special ID, then just stop the door from
						 * automatically opening and closing. Otherwise activate the door as normal.
						 */
						if (door.itemActivationID == 2112) {
							// Hoping no one uses this id, but
							// this stops the door from automatically
							// opening and closing continuously.
							door.itemActivationID = 1221;
						}
						// If this special ID, activate it to continue to
						// move
						else if (door.itemActivationID == 1221) {
							door.itemActivationID = 2112;
							door.activated = true;
							door.stayOpen = false;
						} else {
							door.activated = true;
						}
					}
				}

				// Search through all the elevators
				for (int k = 0; k < Game.elevators.size(); k++) {
					Elevator e = Game.elevators.get(k);

					// If elevator has the same activation ID as the
					// button then activate it.
					if (e.itemActivationID == temp.itemActivationID) {
						/*
						 * If the itemActivationID is the special ID, then just stop the elevator from
						 * automatically moving. Otherwise activate the elevator as normal.
						 */
						if (e.itemActivationID == 2112) {
							// Hoping no one uses this id, but
							// this stops the elevator from automatically
							// moving continuously.
							e.itemActivationID = 1221;
						}
						// If this special ID, activate it to continue to
						// move
						else if (e.itemActivationID == 1221) {
							e.itemActivationID = 2112;
							e.activated = true;
						} else {
							e.activated = true;
						}
					}
				}

				// Stores Items to be deleted
				ArrayList<Item> tempItems2 = new ArrayList<Item>();

				// Scan all activatable items
				for (int j = 0; j < activatable.size(); j++) {
					Item item = activatable.get(j);

					// If Item is a Com satellite dish, activate it and
					// state that it is activated
					if (item.itemID == ItemNames.RADAR.getID() && !item.activated
							&& temp.itemActivationID == item.itemActivationID) {
						item.activated = true;
						Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
						SoundController.uplink.playAudioFile(0);
					} else {
						// If item is enemy spawnpoint, then spawn the
						// enemy, and add the item to the arraylist of
						// items to be deleted
						if (item.itemID == ItemNames.ENEMYSPAWN.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							enemiesInMap++;
							addEnemy(item.x, item.z, item.rotation);
							tempItems2.add(item);
						}
						// If Explosion has same activation ID of the button
						// then activate it
						else if (item.itemID == ItemNames.ACTIVATEEXP.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							new Explosion(item.x, item.y, item.z, 0, 0);
							tempItems2.add(item);
						}
						// If it gets rid of a wall, delete the wall and create an
						// air wall in its place.
						else if (item.itemID == ItemNames.WALLBEGONE.getID()
								&& temp.itemActivationID == item.itemActivationID) {
							Block block2 = Level.getBlock((int) item.x, (int) item.z);

							// Block is effectively no longer there
							block2.height = 0;
							block2.wallEntities = new ArrayList<Item>();

							tempItems2.add(item);
						}
					}
				}

				// Remove all the items that need to be deleted now
				for (int j = 0; j < tempItems2.size(); j++) {
					Item temp2 = tempItems2.get(j);

					temp2.removeItem();
				}
			}
		}

		/*
		 * Check all door entities each tick through the game, and if the door is
		 * activated, continue to move the door in whatever way it was moving before.
		 * Otherwise do nothing to it.
		 */
		for (int i = 0; i < Game.doors.size(); i++) {
			Door door = Game.doors.get(i);

			if (door.activated || door.itemActivationID == 2112) {
				door.move();
			}
		}

		// Update all elevator entities, if its not already
		// moving, then there is not need to continue moving it
		for (int i = 0; i < Game.elevators.size(); i++) {
			Elevator elevator = Game.elevators.get(i);

			if (elevator.activated || elevator.itemActivationID == 2112) {
				elevator.move();
			}
		}

		// Tick through all the enemies
		for (int i = 0; i < entities.size(); i++) {
			Entity enemy = Game.entities.get(i);

			// Deactivate the enemies if the player is dead and they have
			// no other target
			if (Player.health <= 0 && enemy.targetEnemy == null) {
				enemy.activated = false;
			}

			// tick the enemy, updating its values and such
			enemy.tick(enemy);

			// Update the enemies movements
			enemy.move();

			/*
			 * If the enemy is within range of the door, open the door because enemies are
			 * smarter than just letting a door stop them from killing you.
			 * 
			 * Also the enemy has to be activated to open doors.
			 * 
			 * Flying enemies can't open doors because they can just fly over them
			 */
			if (!enemy.canFly && enemy.activated) {
				for (int a = 0; a < Game.doors.size(); a++) {
					Door door = Game.doors.get(a);

					// If door is in range of enemy, have it open as long
					// as it is not a door requiring a key
					if (Math.abs(door.getZ() - enemy.zPos) <= 1 && Math.abs(door.getX() - enemy.xPos) <= 1
							&& door.itemActivationID == 0 && (-enemy.yPos < 2)) {
						if (door.doorType == 0) {
							door.activated = true;
						}
					}
				}
			}

			/*
			 * Attempt to attack the player as long as the player is alive. The method
			 * itself will determine what attack if any the enemy will use though.
			 */
			enemy.attack(Player.y);
		}

		// All entities are done checking if player is in their sight
		EntityParent.checkSight = false;

		// Items to delete
		ArrayList<Item> tempItems = new ArrayList<Item>();

		// ALL ITEM UPDATING
		for (int i = 0; i < Game.items.size(); ++i) {
			Item item = Game.items.get(i);

			// Block item is over or on
			Block temp = Level.getBlock((int) item.x, (int) item.z);

			// Difference between the item and the top of the block
			double difference = item.y - (temp.height + (temp.y * 4) + temp.baseCorrect);

			if (!item.hanging) {
				// Is this item on top of another solid item
				boolean onItem = false;

				for (Item it : temp.wallItems) {
					// If on a solid object, place it on top, and only if it has hit
					// the top of the item as well.
					if (it.isSolid && !item.equals(it) && item.y - (it.y + it.height) < 6) {
						item.y = it.y + it.height + 2;
						onItem = true;

						/*
						 * If item is above the block, then set its height from the top of the block to
						 * be the height of the solid item it is on greater than the top of the block.
						 * If not though, it is below the block and there should be no difference in
						 * height as it will not be falling.
						 */
						if (item.aboveBlock) {
							difference += (item.y - it.height);
						} else {
							difference = 0;
						}
					}
				}

				/*
				 * If the items y is greater than the blocks height plus its y value, then
				 * decrease the items y until it reaches the ground.
				 */
				if (difference > 1) {
					item.y -= 1;
				} else {
					/*
					 * If the item is within the block, and not on top of the block, and the item is
					 * not a door, then set the height to being the top of the block.
					 */
					if (item.y >= (temp.y * 4) && difference > (-1 - temp.baseCorrect) && item.aboveBlock) {
						item.y = temp.height + (temp.y * 4) + temp.baseCorrect;
					} else {
						// If landed on flat ground and not on top of a solid item
						if (!onItem) {
							item.y = 0;
						}
					}
				}
			} else {
				// If item is a hanging item, it is attached to the ceiling.
				if (temp.y == 0) {
					item.y = Render3D.ceilingDefaultHeight;
				} else {
					item.y = temp.y - 1;
				}
			}

			double distance = Math.sqrt(((Math.abs(Player.x - item.x)) * (Math.abs(Player.x - item.x)))
					+ ((Math.abs(Player.z - item.z)) * (Math.abs(Player.z - item.z))));

			/*
			 * If item has been picked up in respawnable mode, tick the count of how long it
			 * has been picked up, and after a certain amount of ticks, allow it to appear
			 * again and be able to be picked up.
			 */
			if (item.pickedUp) {
				item.tick();

				if (item.tickCount > 1000 * Render3D.fpsCheck) {
					item.tickCount = 0;
					item.pickedUp = false;
					SoundController.teleportation.playAudioFile(distance);
				}
			}

			// If the item is at least 0.7 units away, and its not
			// a secret or linedef, and the player is not in noClip mode
			if (distance <= 0.7 && Math.abs(item.y - Player.y) <= 6 && item.itemID != ItemNames.SECRET.itemID
					&& item.itemID != ItemNames.LINEDEF.itemID && !Player.noClipOn) {
				// Was the object activated?
				boolean activated = item.activate();

				/*
				 * If the item was activated remove it. Otherwise keep it in the map. Also play
				 * audio queue is there is one
				 */
				if (activated) {

					if (Display.itemsRespawn) {
						item.pickedUp = true;
					} else {
						tempItems.add(item);
					}

					// Activate the audio queue if there is one
					item.activateAudioQueue();
				}

				// If item activates other items, then if the player walks over the
				// item then activate other items whether the item was picked up
				// or not. Some items cannot be activated by walking over them.
				if (item.itemActivationID > 0 && item.itemID != ItemNames.ENEMYSPAWN.getID()
						&& item.itemID != ItemNames.ACTIVATEEXP.getID() && item.itemID != ItemNames.BREAKABLEWALL.itemID
						&& item.itemID != ItemNames.WALLBEGONE.itemID && item.itemID != ItemNames.ACTIVATEEXP.itemID
						&& item.itemID != ItemNames.BUTTON.itemID && item.itemID != ItemNames.DOOR.itemID
						&& item.itemID != ItemNames.GREENDOOR.itemID && item.itemID != ItemNames.REDDOOR.itemID
						&& item.itemID != ItemNames.YELLOWDOOR.itemID && item.itemID != ItemNames.BLUEDOOR.itemID
						&& item.itemID != ItemNames.ELEVATOR.itemID) {
					// De-activate the button
					item.activated = false;

					Block blockOn = Level.getBlock((int) item.x, (int) item.z);

					// Change the wall texture to show the button has been activated
					// Only if the original wall it was on is a button
					if (blockOn.wallID == 26) {
						blockOn.wallID = 43;
					}

					// If special ID, turn on lights to make map brighter
					if (item.itemActivationID == 1221) {
						Render3D.renderDistanceDefault += 5000;
					}

					// Search through all the doors
					for (int k = 0; k < Game.doors.size(); k++) {
						Door door = Game.doors.get(k);

						// If door has the same activation ID as the
						// button then activate it.
						if (door.itemActivationID == item.itemActivationID) {
							/*
							 * If the itemActivationID is the special ID, then just stop the door from
							 * automatically opening and closing. Otherwise activate the door as normal.
							 */
							if (door.itemActivationID == 2112) {
								// Hoping no one uses this id, but
								// this stops the door from automatically
								// opening and closing continuously.
								door.itemActivationID = 1221;
							}
							// If this special ID, activate it to continue to
							// move
							else if (door.itemActivationID == 1221) {
								door.itemActivationID = 2112;
								door.activated = true;
								door.stayOpen = false;
							} else {
								door.activated = true;
							}
						}
					}

					// Search through all the elevators
					for (int k = 0; k < Game.elevators.size(); k++) {
						Elevator e = Game.elevators.get(k);

						// If elevator has the same activation ID as the
						// button then activate it.
						if (e.itemActivationID == item.itemActivationID) {
							/*
							 * If the itemActivationID is the special ID, then just stop the elevator from
							 * automatically moving. Otherwise activate the elevator as normal.
							 */
							if (e.itemActivationID == 2112) {
								// Hoping no one uses this id, but
								// this stops the elevator from automatically
								// moving continuously.
								e.itemActivationID = 1221;
							}
							// If this special ID, activate it to continue to
							// move
							else if (e.itemActivationID == 1221) {
								e.itemActivationID = 2112;
								e.activated = true;
							} else {
								e.activated = true;
							}
						}
					}

					// Stores Items to be deleted
					ArrayList<Item> tempItems2 = new ArrayList<Item>();

					// Scan all activatable items
					for (int j = 0; j < Game.activatable.size(); j++) {
						Item item2 = Game.activatable.get(j);

						// If Item is a Com satellite dish, activate it and
						// state that it is activated
						if (item2.itemID == ItemNames.RADAR.getID() && !item2.activated
								&& item.itemActivationID == item2.itemActivationID) {
							item2.activated = true;
							Display.messages.add(new PopUp("COM SYSTEM ACTIVATED!"));
							SoundController.uplink.playAudioFile(0);
						} else {
							// If item is enemy spawnpoint, then spawn the
							// enemy, and add the item to the arraylist of
							// items to be deleted
							if (item2.itemID == ItemNames.ENEMYSPAWN.getID()
									&& item.itemActivationID == item2.itemActivationID) {
								Game.enemiesInMap++;
								addEnemy(item2.x, item2.z, item2.rotation);
								tempItems2.add(item2);
							}
							// If Explosion has same activation ID of the button
							// then activate it
							else if (item2.itemID == ItemNames.ACTIVATEEXP.getID()
									&& item.itemActivationID == item2.itemActivationID) {
								new Explosion(item2.x, item2.y, item2.z, 0, 0);
								tempItems2.add(item2);
							}
							// If it gets rid of a wall, delete the wall and create an
							// air wall in its place.
							else if (item2.itemID == ItemNames.WALLBEGONE.getID()
									&& item.itemActivationID == item2.itemActivationID) {
								Block block2 = Level.getBlock((int) item2.x, (int) item2.z);

								// Block is effectively no longer there
								block2.height = 0;

								block2.wallEntities = null;

								tempItems2.add(item2);
							}
						}
					}

					// Remove all the items that need to be deleted now
					for (int j = 0; j < tempItems2.size(); j++) {
						Item temp2 = tempItems2.get(j);

						temp2.removeItem();
					}
				}
			}
		}

		// Remove all items within list
		for (int j = 0; j < tempItems.size(); ++j) {
			Item temp = tempItems.get(j);

			// Removes all references of item in game
			temp.removeItem();
		}

		for (int i = 0; i < Game.corpses.size(); i++) {
			Corpse corpse = Game.corpses.get(i);

			// Block corpse is on
			Block temp = Level.getBlock((int) corpse.xPos, (int) corpse.zPos);

			// The top of the block
			double topOfBlock = (temp.height + (temp.y * 4) + temp.baseCorrect);

			// Difference between the item and the top of the block
			double difference = corpse.yPos - topOfBlock;

			/*
			 * If the items y is greater than the blocks height plus its y value, then
			 * decrease the items y until it reaches the ground.
			 */
			if (corpse.yPos - 1 > topOfBlock) {
				corpse.yPos -= 1;
			} else {
				/*
				 * If not a door, and the y value of the corpse is inside the block, but towards
				 * the top, then place it on top of the block. Otherwise, it shall stay on
				 * ground level.
				 */
				if (corpse.yPos >= (temp.y * 4) && difference > (-1 - temp.baseCorrect)) {
					corpse.yPos = topOfBlock;
				} else {
					// If on flat ground, the height is 3. This is
					// so the textures don't go through the ground.
					corpse.yPos = 0;
				}
			}

			corpse.tick();

			/*
			 * If in Death cannot hurt me mode, the corpses will resurrect on their own
			 * after 10000 ticks.
			 */
			if (FPSLauncher.modeChoice == 4) {
				// If 10000 ticks have passed with correction for the fps
				// and the corpse is not just a default corpse
				if (corpse.time > (10000 / ((Display.fps / 30) + 1)) && corpse.enemyID != 0 && corpse.enemyID != 15
						&& corpse.enemyID != 16) {
					/*
					 * Reconstruct enemy depending on the ID the corpse was before it died. Also
					 * activate the new resurrected enemy.
					 */
					Entity newEnemy = new Entity(corpse.xPos, 0, corpse.zPos, corpse.enemyID, 0, 0);

					// Activate this new enemy
					newEnemy.activated = true;

					// Add enemy to the game
					Game.entities.add(newEnemy);

					// Remove the corpse
					Game.corpses.remove(corpse);

					// Add to enemies in the map
					Game.enemiesInMap++;

					// Play teleportation/respawn sound effect
					SoundController.teleportation.playAudioFile(newEnemy.distanceFromPlayer);
				}
			}
		}

		// Tick all explosions
		for (int i = 0; i < Game.explosions.size(); i++) {
			Explosion explosion = Game.explosions.get(i);

			explosion.tick();
		}

		/*
		 * Mainly for shotgun bullets. Since they are in a spread shot, and could
		 * potentially hit the enemy at the same time, then make it so that the sound of
		 * hitting the enemy doesn't play several times in the same tick otherwise it'll
		 * be a super loud sound, and it's annoying. This checks for that and prevents
		 * it.
		 * 
		 * These reset the values to false each tick.
		 */
		Projectile.bossHit = false;
		Projectile.enemyHit = false;

		/*
		 * Moves the game bullets in a given direction and checks for collisions
		 */
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);

			// For any bullets that are supposed to hit instantly, move them
			// until they hit something.
			bullet.move();
		}

		/*
		 * Moves enemy projectiles. Also checks for collisions
		 */
		for (int i = 0; i < Game.enemyProjectiles.size(); i++) {
			EnemyFire temp = Game.enemyProjectiles.get(i);

			temp.move();
		}

		/*
		 * Ticks and renders all extra sprites in the game
		 */
		for (int i = 0; i < Game.sprites.size(); i++) {
			HitSprite hS = Game.sprites.get(i);

			hS.tick();
		}
	}

	// TODO Maybe when multithreading is figured out
	@Override
	public void run() {
		tick(Display.input.key);
	}

	/**
	 * Adds a projectile to the game starting at the players x, y and z position.
	 * This entity is used to hit enemies and make them happy.
	 */
	public static void addBullet(int damage, int ID, double speed, double rotation, boolean criticalHit) {

		double bullY = ((-Player.y) * 0.085);

		// Bullet will be lower if player is crouching
		if (Player.yCorrect + 1 < Player.y) {
			bullY = 0.8;
		}

		Bullet b = new Bullet(damage, speed, Player.x, bullY, Player.z, ID, rotation, criticalHit);

		// If this is a client, add this bullet to the bulletsAdded arraylist so that it
		// may be added to the server and ticked there.
		if (Display.gameType == 1) {
			Game.bulletsAdded.add(b);
		}

		bullets.add(b);
	}

	/**
	 * Adds a random enemy to the game if the map is random, and places it in a
	 * random location within the map, depending on the maps size.
	 */
	public void addEnemy() {
		Random rand = new Random();
		int yPos = 0;
		int ID = 0;

		int randomNum = rand.nextInt(160);

		/*
		 * Spawns a random type of enemy of the 4 types
		 */
		// Brainomorph
		if (randomNum <= 7) {
			ID = 1;
		}
		// Sentinel
		else if (randomNum <= 14) {
			yPos = -1;
			ID = 2;
		}
		// Mutated Commando
		else if (randomNum <= 21) {
			ID = 3;
		}
		// Night Reaper
		else if (randomNum <= 28) {
			ID = 4;
		}
		// Vile Warrior
		else if (randomNum <= 40) {
			ID = 7;
		}
		// The Watcher
		else if (randomNum <= 47) {
			ID = 9;
		}
		// Mage enemy
		else if (randomNum <= 54) {
			ID = 5;
		} // Evil Marines
		else if (randomNum <= 61) {
			ID = 10;
		} else if (randomNum <= 68) {
			ID = 11;
		} else if (randomNum <= 75) {
			ID = 12;
		} else if (randomNum <= 82) {
			ID = 13;
		} else if (randomNum <= 89) {
			ID = 14;
		} else if (randomNum <= 96) {
			ID = 18;
		} else if (randomNum <= 103) {
			ID = 19;
		} else if (randomNum <= 110) {
			ID = 20;
		} else if (randomNum <= 117) {
			ID = 21;
		} else if (randomNum <= 124) {
			ID = 22;
		} else if (randomNum <= 131) {
			ID = 23;
		} else if (randomNum <= 138) {
			ID = 24;
		} else if (randomNum <= 145) {
			ID = 25;
		} else if (randomNum <= 152) {
			ID = 26;
		} else {
			ID = 7;
		}

		// Add new enemy to game
		entities.add(new Entity(0.5 + rand.nextInt(Level.width), yPos, 0.5 + rand.nextInt(Level.height), ID, 0, -1));
	}

	/**
	 * Adds a random enemy to the game to a designated place in the map that is sent
	 * in through the parameters.
	 */
	public void addEnemy(double x, double z, int rotation) {
		Block block = Level.getBlock((int) x, (int) z);

		Random rand = new Random();
		int yPos = 0;
		int ID = 0;

		int choices = 160;

		// In earlier levels, the entities randomly spawned have to be
		// less dangerous than later levels
		if (Game.mapNum <= 2) {
			choices = 25;
		} else if (Game.mapNum <= 4) {
			choices = 41;
		}

		int randomNum = rand.nextInt(choices);

		/*
		 * Spawns a random type of enemy of the 4 types
		 */
		// Brainomorph
		if (randomNum <= 7) {
			ID = 1;
		}
		// Night Reaper
		else if (randomNum <= 14) {
			ID = 4;
		}
		// Vile Warrior
		else if (randomNum <= 24) {
			ID = 7;
		}
		// Mutated Commando
		else if (randomNum <= 31) {
			ID = 3;
		}
		// Sentinel
		else if (randomNum <= 38) {
			yPos = -1;
			ID = 2;
		}
		// The Watcher
		else if (randomNum <= 45) {
			ID = 9;
		}
		// Mage enemy
		else if (randomNum <= 52) {
			ID = 5;
		} // Evil Marines
		else if (randomNum <= 59) {
			ID = 10;
		} else if (randomNum <= 66) {
			ID = 11;
		} else if (randomNum <= 73) {
			ID = 12;
		} else if (randomNum <= 80) {
			ID = 13;
		} else if (randomNum <= 87) {
			ID = 14;
		} else if (randomNum <= 94) {
			ID = 4;
		} else if (randomNum <= 103) {
			ID = 19;
		} else if (randomNum <= 110) {
			ID = 20;
		} else if (randomNum <= 117) {
			ID = 21;
		} else if (randomNum <= 124) {
			ID = 22;
		} else if (randomNum <= 131) {
			ID = 23;
		} else if (randomNum <= 138) {
			ID = 24;
		} else if (randomNum <= 145) {
			ID = 25;
		} else if (randomNum <= 152) {
			ID = 26;
		} else {
			ID = 7;
		}

		Entity enemy = new Entity(x, yPos, z, ID, rotation, -1);

		// Add new enemy to game with correct values
		entities.add(enemy);
		block.entitiesOnBlock.add(enemy);

		SoundController.teleportation.playAudioFile(enemy.distanceFromPlayer);
	}

	/**
	 * Loads a game from a save file. Takes a lot more work than normal map loading
	 * because entities could be in different positions, phases, etc... when the
	 * player pauses the game and saves it. So this has to handle everything!
	 */
	public void loadGame() {
		// A new scanner object that is defaultly set to null
		Scanner sc = null;

		// Resets all the lists
		resetLists();

		/*
		 * Try to read the file and if not, state the error
		 */
		try {
			// Creates a Scanner that can read the file
			sc = new Scanner(new BufferedReader(
					new FileReader("Users/" + FPSLauncher.currentUserName + "/" + FPSLauncher.fileToLoad + ".txt")));

			// Current line being read in
			String currentLine = "";

			///////////////////// Map stuff
			currentLine = sc.nextLine();

			// Elements in the line
			String[] elements = currentLine.split(":");

			// Was it survival or campaign mode
			int gM = Integer.parseInt(elements[1]);

			FPSLauncher.gameMode = gM;

			// Resets all the lists
			resetLists();

			secretsFound = Integer.parseInt(elements[2]);
			enemiesInMap = Integer.parseInt(elements[3]);
			Player.kills = Integer.parseInt(elements[4]);
			FPSLauncher.themeName = (elements[5]);

			// Length of elements to read in
			int length = 0;

			////////////////////////// Player stuff now
			currentLine = sc.nextLine();

			String weaponStuff = "";
			String otherStuff = "";

			// Split between weapon equipped and weapon attributes
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
			Player.speedMultiplier = Integer.parseInt(elements[23]);
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

			// Because map names are normally named as Map#: Name.
			// The colon causes issues so this fixes that. And adds
			// the colon back in because its split out
			try {
				mapName = elements[34] + ":" + elements[35];
			} catch (Exception ex) {
				// If there's an issue, its probably because the map name
				// does not include the colon
				mapName = elements[34];
			}

			// System.out.println("Map and Player successully loaded");

			// Set up new level with this size
			level = new Level(Level.width, Level.height);

			// Split up weapon Attributes
			elements = weaponStuff.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			/*
			 * For each weapon, load in its attributes depending on what they were when the
			 * game was saved.
			 */
			for (int i = 0; i < length; i++) {
				Weapon w = Player.weapons[i];

				String[] weaponStats = elements[i].split(":");

				int size = weaponStats.length - 8;

				w.weaponID = Integer.parseInt(weaponStats[0]);
				w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
				w.dualWield = Boolean.parseBoolean(weaponStats[2]);
				w.ammo = Integer.parseInt(weaponStats[3]);
				w.damage = Integer.parseInt(weaponStats[4]);
				w.baseDamage = Integer.parseInt(weaponStats[5]);
				w.criticalHitChances = Integer.parseInt(weaponStats[6]);
				w.upgradePointsNeeded = Integer.parseInt(weaponStats[7]);

				for (int j = 0; j < size; j++) {
					w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[8 + j])));
				}
			}

			// System.out.println("Weapons successully loaded");

			////////////////// Walls
			sc.nextLine();

			String thisLine = "";

			currentLine = sc.nextLine();

			// Stop reading when it reaches where the next element of the
			// game is being loaded in.
			while (!thisLine.equals("Enemies:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Enemies:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				otherStuff = elements[i];
				String[] bAt = otherStuff.split(":");

				// Create block with its needed values
				Block b = new Block(Double.parseDouble(bAt[6]), Integer.parseInt(bAt[4]),
						Double.parseDouble(bAt[2]) * 4, Integer.parseInt(bAt[1]), Integer.parseInt(bAt[3]));

				b.health = Integer.parseInt(bAt[0]);
				b.wallPhase = Integer.parseInt(bAt[5]);
				b.isSolid = Boolean.parseBoolean(bAt[7]);
				b.seeThrough = Boolean.parseBoolean(bAt[8]);

				Level.blocks[b.x + b.z * Level.width] = b;
			}

			// System.out.println("Blocks successully loaded");

			////////////////// Enemies
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Items:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Items:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				otherStuff = elements[i];
				String[] enAt = otherStuff.split(":");

				// Create enemy with its needed values
				Entity en = new Entity(Double.parseDouble(enAt[1]), Double.parseDouble(enAt[2]),
						Double.parseDouble(enAt[3]), Integer.parseInt(enAt[4]), Double.parseDouble(enAt[12]),
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
				en.isFriendly = Boolean.parseBoolean(enAt[22]);

				Game.entities.add(en);

				Block blockOn = Level.getBlock((int) en.xPos, (int) en.zPos);

				// Only if in campaign mode, survival mode acts weird here
				if (gM == 0) {
					blockOn.entitiesOnBlock.add(en);
				}
			}

			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Bullets:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Bullets:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				otherStuff = elements[i];
				String[] itemAtt = otherStuff.split(":");

				int itemID = Integer.parseInt(itemAtt[1]);

				Item temp = null;

				// TODO update item loading stuff maybe

				/*
				 * If its not an explosive canister, add it as a normal item. Otherwise add it
				 * as an explosive canister
				 */
				temp = new Item(10, Double.parseDouble(itemAtt[2]), Double.parseDouble(itemAtt[3]),
						Double.parseDouble(itemAtt[4]), itemID, Integer.parseInt(itemAtt[5]),
						Integer.parseInt(itemAtt[0]), itemAtt[6]);

				Block itemBlock = Level.getBlock((int) temp.x, (int) temp.z);

				// If the item gives the block a specific quality, or if the
				// item can not be removed from the block (if its solid)
				if (temp.isSolid || itemID == ItemNames.BREAKABLEWALL.getID() || itemID == ItemNames.SECRET.getID()
						|| itemID == ItemNames.LINEDEF.getID()) {
					// Set item to being the item that is within this
					// block only if it is solid
					itemBlock.wallItems.add(temp);
				}

				// If satellite dish, add to activatable list as well
				if (itemID == ItemNames.RADAR.getID()) {
					Game.activatable.add(temp);
				}
				// If item supposed to be activated by button
				else if (itemID == ItemNames.ACTIVATEEXP.getID() || itemID == ItemNames.ENEMYSPAWN.getID()
						|| itemID == ItemNames.WALLBEGONE.getID()) {
					Game.activatable.add(temp);
				} else if (itemID == ItemNames.TELEPORTEREXIT.getID() || itemID == ItemNames.TELEPORTERENTER.getID()) {
					Game.teleporters.add(temp);

					itemBlock.wallEntities.add(temp);
				}
			}

			// System.out.println("Items successully loaded");

			//////////////////////// Bullets
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Enemy Projectiles:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Enemy Projectiles:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Bullet b = null;

				String[] bAtt = elements[i].split(":");

				b = new Bullet(Integer.parseInt(bAtt[1]), Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]),
						Double.parseDouble(bAtt[4]), Double.parseDouble(bAtt[5]), Integer.parseInt(bAtt[0]), 0, false);

				b.xa = Double.parseDouble(bAtt[6]);
				b.za = Double.parseDouble(bAtt[7]);
				b.initialSpeed = Double.parseDouble(bAtt[8]);

				Game.bullets.add(b);
			}

			// System.out.println("Bullets successully loaded");

			/////////////////////////// Enemy Fire
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Explosions:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Explosions:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				EnemyFire b = null;

				String[] bAtt = elements[i].split(":");

				// Given all the information we have, construct
				// the enemy projectile the best we can after a save
				b = new EnemyFire(Integer.parseInt(bAtt[1]), Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]),
						Double.parseDouble(bAtt[4]), Double.parseDouble(bAtt[5]), Integer.parseInt(bAtt[0]), 0, 0, 0, 0,
						null, false);

				b.xa = Double.parseDouble(bAtt[6]);
				b.za = Double.parseDouble(bAtt[7]);
				b.initialSpeed = Double.parseDouble(bAtt[8]);

				Game.enemyProjectiles.add(b);
			}

			// System.out.println("Enemy Fire successully loaded");

			/////////////////////////////////// Explosions
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Buttons:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Buttons:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Explosion exp = null;

				String[] expAtt = elements[i].split(":");

				exp = new Explosion(Double.parseDouble(expAtt[2]), Double.parseDouble(expAtt[3]),
						Double.parseDouble(expAtt[4]), Integer.parseInt(expAtt[0]), Integer.parseInt(expAtt[6]));

				exp.exploded = Boolean.parseBoolean(expAtt[5]);
				exp.phaseTime = Integer.parseInt(expAtt[1]);

				Game.explosions.add(exp);
			}

			// System.out.println("Explosions successully loaded");

			//////////////////////////// Buttons
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Doors:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Doors:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Button b = null;

				String[] bAtt = elements[i].split(":");

				b = new Button(Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]), Double.parseDouble(bAtt[4]),
						Integer.parseInt(bAtt[0]), Integer.parseInt(bAtt[1]), bAtt[5]);

				b.pressed = Boolean.parseBoolean(bAtt[5]);

				Game.buttons.add(b);
			}

			// System.out.println("Buttons successully loaded");

			//////////////////////////// Doors
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Elevators:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Elevators:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Door d = null;

				String[] dAtt = elements[i].split(":");

				d = new Door(Double.parseDouble(dAtt[2]), Double.parseDouble(dAtt[3]), Double.parseDouble(dAtt[4]),
						Integer.parseInt(dAtt[5]), Integer.parseInt(dAtt[6]), Integer.parseInt(dAtt[9]),
						Integer.parseInt(dAtt[1]), Double.parseDouble(dAtt[11]) * 4);

				d.time = Integer.parseInt(dAtt[7]);
				d.soundTime = Integer.parseInt(dAtt[8]);
				d.ID = Integer.parseInt(dAtt[0]);
				d.doorY = Double.parseDouble(dAtt[10]);

				Block thisBlock = Level.getBlock(d.doorX, d.doorZ);

				thisBlock.y = d.doorY;

				if (thisBlock.y > 0) {
					thisBlock.isMoving = true;
				}

				Game.doors.add(d);
			}

			// System.out.println("Doors successully loaded");

			//////////////////////////// Elevators
			thisLine = "";

			currentLine = sc.nextLine();

			while (!thisLine.equals("Corpses:")) {
				thisLine = sc.nextLine();

				if (thisLine.equals("Corpses:")) {
					break;
				}

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Elevator e = null;

				String[] eAtt = elements[i].split(":");

				e = new Elevator(Double.parseDouble(eAtt[2]), Double.parseDouble(eAtt[3]), Double.parseDouble(eAtt[4]),
						Integer.parseInt(eAtt[5]), Integer.parseInt(eAtt[6]), Integer.parseInt(eAtt[1]),
						Double.parseDouble(eAtt[14]));

				e.height = Integer.parseInt(eAtt[7]);
				e.soundTime = Integer.parseInt(eAtt[8]);
				e.ID = Integer.parseInt(eAtt[0]);
				e.waitTime = Integer.parseInt(eAtt[11]);
				e.upHeight = Double.parseDouble(eAtt[12]);
				e.movingUp = Boolean.parseBoolean(eAtt[9]);
				e.movingDown = Boolean.parseBoolean(eAtt[10]);
				e.activated = Boolean.parseBoolean(eAtt[13]);

				Game.elevators.add(e);

				Block thisBlock = Level.getBlock(e.elevatorX, e.elevatorZ);

				thisBlock.height = e.height;
			}

			// System.out.println("Elevators successully loaded");

			/////////////////////////////// Corpses
			thisLine = "";

			// Sometimes theres not a next line
			try {
				currentLine = sc.nextLine();
			} catch (Exception e) {

			}

			while (sc.hasNextLine()) {
				thisLine = sc.nextLine();

				currentLine += thisLine;
			}

			elements = currentLine.split(";");

			// Length is set to the amount of elements
			length = elements.length;

			/*
			 * If there is one element, and it is just blank space, then set the array to
			 * being null again.
			 */
			if (elements.length == 1 && elements[0].trim().equals("")) {
				elements = null;
				length = 0;
			}

			for (int i = 0; i < length; i++) {
				Corpse c = null;

				String[] cAtt = elements[i].split(":");

				c = new Corpse(Double.parseDouble(cAtt[2]), Double.parseDouble(cAtt[4]), Double.parseDouble(cAtt[3]),
						Integer.parseInt(cAtt[0]), Double.parseDouble(cAtt[6]), Double.parseDouble(cAtt[8]),
						Double.parseDouble(cAtt[7]), false);

				c.time = Integer.parseInt(cAtt[5]);
				c.phaseTime = Integer.parseInt(cAtt[1]);

				Game.corpses.add(c);
			}

			// System.out.println("Corpses successully loaded");

			sc.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		// Reset the music with this new audio file
		display.resetMusic(mapAudio);
	}

	/**
	 * Sets up the survival game mode.
	 */
	public void setUpSurvival() {
		// Resets all the lists
		resetLists();

		calculatedSize = 100;

		if (FPSLauncher.levelSizeChoice == 0) {
			calculatedSize = 10;
		} else if (FPSLauncher.levelSizeChoice == 1) {
			calculatedSize = 25;
		} else if (FPSLauncher.levelSizeChoice == 2) {
			calculatedSize = 50;
		} else if (FPSLauncher.levelSizeChoice == 3) {
			calculatedSize = 100;
		} else if (FPSLauncher.levelSizeChoice == 4) {
			calculatedSize = 250;
		} else {
			calculatedSize = 100;
		}

		// Set up new level with this size
		level = new Level(calculatedSize, calculatedSize);

		// Add initial enemy
		addEnemy();

		/*
		 * Put 500 of either health packs, armor, or megahealth
		 */
		for (int i = 0; i < 500; i++) {
			Random rand = new Random();

			int ammoType = rand.nextInt(102);

			// Place random ammo type in map
			if (ammoType <= 70) {
				ammoType = 2;
			} else if (ammoType <= 98) {
				ammoType = 33;
			} else if (ammoType == 99) {
				ammoType = 1;
			} else if (ammoType == 100) {
				ammoType = 34;
			} else {
				ammoType = 35;
			}

			// Item adds itself when instantiated
			new Item(5, rand.nextInt(calculatedSize), 0, rand.nextInt(calculatedSize), ammoType, 0, 0, "");
		}

		/*
		 * Add 500 random ammo types to the map
		 */
		for (int i = 0; i < 750; i++) {
			Random rand = new Random();

			int ammoType = rand.nextInt(5);

			// Place random ammo type in map
			if (ammoType == 0) {
				ammoType = 3;
			} else if (ammoType == 1) {
				ammoType = 50;
			} else if (ammoType == 2) {
				ammoType = 56;
			} else if (ammoType == 3) {
				ammoType = 61;
			} else if (ammoType == 4) {
				ammoType = 123;
			}

			// Item adds itself when instantiated
			new Item(5, rand.nextInt(calculatedSize), 0, rand.nextInt(calculatedSize), ammoType, 0, 0, "");
		}

		// Player has all weapons for survival mode
		Player.weapons[0].canBeEquipped = true;
		Player.weapons[1].canBeEquipped = true;
		Player.weapons[1].dualWield = true;
		Player.weapons[2].canBeEquipped = true;
		Player.weapons[3].canBeEquipped = true;
		Player.weapons[4].canBeEquipped = true;
		Player.weapons[5].canBeEquipped = true;
	}

	/**
	 * Loads the next map up for the game, and sets up the new level and the
	 * positions of the entities.
	 */
	public void loadNextMap(boolean newMap, String mapNameNew) {
		Display.clearedLevel = false;
		/*
		 * The player does not keep keys from level to level. Nor does he/she keep
		 * his/her effects
		 */
		Player.hasRedKey = false;
		Player.hasBlueKey = false;
		Player.hasGreenKey = false;
		Player.hasYellowKey = false;
		Player.immortality = 0;
		Player.invisibility = 0;
		Player.environProtectionTime = 0;
		Player.vision = 0;
		Player.meleeMultiplier = 1;

		try {
			Display.soundController.stopSounds();
		} catch (Exception e) {

		}

		/*
		 * Every map starts with 0 enemies, 0 secrets, and no secrets found on default.
		 */
		secretsInMap = 0;
		secretsFound = 0;
		enemiesInMap = 0;

		// Player has no kills on the map when the map is first started
		Player.kills = 0;

		// Try and read the file correctly, if not catch exception
		try {
			String temp = "";

			// Rows and columns of blocks in new level
			int row = 0;
			int col = 0;

			// Default player rotation
			Player.rotation = 0;

			// Resets all the lists
			resetLists();

			// It requires me to put something here.... sigh...
			Scanner sc = new Scanner(
					new BufferedReader(new FileReader("resources/default/maps/map" + mapNum + ".txt")));

			// First sets up default map name to be read
			try {
				sc = new Scanner(new BufferedReader(
						new FileReader("resources" + FPSLauncher.themeName + "/maps/map" + mapNum + ".txt")));

				// If a custom map name, load scan that instead
				if (newMap) {
					sc = new Scanner(new BufferedReader(
							new FileReader("resources" + FPSLauncher.themeName + "/maps/" + mapNameNew + ".txt")));
				}
			} catch (Exception e) {
				try {
					// If that map does not exist in the resource pack
					sc = new Scanner(
							new BufferedReader(new FileReader("resources/default/maps/map" + mapNum + ".txt")));

					// If a custom map name, load scan that instead
					if (newMap) {
						sc = new Scanner(
								new BufferedReader(new FileReader("resources/default/maps/" + mapNameNew + ".txt")));
					}
				} catch (Exception ex) {
					// If that map number is not found in either, just
					// quit to main menu again.
					e.printStackTrace();

					// If map doesn't load, exit to pause menu so game
					// Doesn't crash and you can retry
					Display.pauseGame = true;

					// Quit game is true
					Controller.quitGame = true;

					// Restart the game to quit
					new Game(display, false, "");
				}
			}

			// The very first part of any map file now is the name
			mapName = sc.nextLine();

			// The next line is the special settings for the map
			String mapSettings = sc.nextLine();

			try {
				// Split that line up to get the map number
				// ceiling height, and render distance of map
				// and map audio to be played and also the
				// Ceiling and floor texture ID's
				String[] temp2 = mapSettings.split(":");

				mapNum = Integer.parseInt(temp2[0]);

				Render3D.ceilingDefaultHeight = Integer.parseInt(temp2[1]);

				Render3D.renderDistanceDefault = Integer.parseInt(temp2[2]);

				Level.renderDistance = Render3D.renderDistanceDefault;

				mapAudio = temp2[3];

				mapFloor = Integer.parseInt(temp2[4]);

				mapCeiling = Integer.parseInt(temp2[5]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Reset the music with this new audio file
			display.resetMusic(mapAudio);

			/*
			 * While the whole file has not been read, keep scanning each line of the file
			 * into string format and adding it to the temporary string.
			 */
			while (sc.hasNext()) {
				temp += sc.next();
			}

			/*
			 * At each point there is a , in the string, split that up as a seperate string
			 * and add it as a new slot in the array of strings called splitedParts
			 */
			String[] splitedParts = temp.split(",");

			/*
			 * Because apparently map has to be instantiated correctly with a size that will
			 * fit all the walls into it, therefore I instantiated it with a size in both
			 * rows and columns that may be too big, but will fit all the walls and entities
			 * in the map for sure.
			 */
			ValueHolder[][] tempMap = new ValueHolder[splitedParts.length][splitedParts.length];

			/*
			 * Stores the actual number of cloumns the 2D array of the map has so that the
			 * 2D array that "map" is instantiated with is the correct size.
			 */
			int storeColumn = 0;

			/*
			 * Will stop adding to storeColumn when set to true. After one row of columns
			 * has been counted, the number is stored, and the counting needs to stop.
			 */
			boolean stop = false;

			/*
			 * For each slot of the splited parts array, convert the part into an actual
			 * integer using the parseInt method, then put that int into that slot of the 2D
			 * array map.
			 */
			for (int i = 0; i < splitedParts.length; i++) {
				String[] blockValues = splitedParts[i].split(":");

				double height = Integer.parseInt(blockValues[0]);
				int wallID = Integer.parseInt(blockValues[1]);
				int entityID1 = Integer.parseInt(blockValues[2]);
				int entityID2 = 0;
				double itemRotation = 0;
				double itemRotation2 = 0;
				int itemActID1 = 0;
				int itemActID2 = 0;
				boolean aboveBlock1 = true;
				boolean aboveBlock2 = true;
				int wallY = 0;
				int doorRaiseHeight = 0;
				String audioQueue = "";

				// Try to see if block has an entity with a given rotation
				try {
					itemRotation = Integer.parseInt(blockValues[3]);

					// Sets to radians
					itemRotation = (itemRotation / 180) * Math.PI;
				} catch (Exception e) {
					// If entity does not have a rotation, the default is 0
					itemRotation = 0;
				}

				// Try to see if block has an entity with a given
				// Activation ID
				try {
					itemActID1 = Integer.parseInt(blockValues[4]);
				} catch (Exception e) {
					// If entity does not have a activation ID
					// , the default is 0
					itemActID1 = 0;
				}

				// Try to see if block has an entity with a given
				// Audio Queue
				try {
					audioQueue = (blockValues[5]);
				} catch (Exception e) {
					// If entity does not have a audio queue
					// , the default is 0
					audioQueue = "";
				}

				// Try to see if block has an entity with a given
				// Wall y value
				try {
					wallY = Integer.parseInt(blockValues[6]);
				} catch (Exception e) {
					// If entity does not have a wallY
					// , the default is 0
					wallY = 0;
				}

				// Try to see if block has an entity with a given
				// Door Height value
				try {
					doorRaiseHeight = Integer.parseInt(blockValues[7]);
				} catch (Exception e) {
					// If entity does not have a doorHeightValue
					// , the default is 0
					doorRaiseHeight = 0;
				}

				// In early versions, there could not be two entities to a block
				try {
					entityID2 = Integer.parseInt(blockValues[8]);
				} catch (Exception e) {
					entityID2 = 0;
				}

				// Try to see if block has a second entity with a given
				// Activation ID
				try {
					itemActID2 = Integer.parseInt(blockValues[9]);
				} catch (Exception e) {
					// If entity does not have a activation ID
					// , the default is 0
					itemActID2 = 0;
				}

				// Try to get the second rotation variable mapped for the
				// second item/entity
				try {
					itemRotation2 = Integer.parseInt(blockValues[10]);

					// Sets to radians
					itemRotation2 = (itemRotation2 / 180) * Math.PI;
				} catch (Exception e) {
					// If no item rotation is found, set to the default
					itemRotation2 = 0;
				}

				// Whether item1 is above or below the block
				try {
					aboveBlock1 = Boolean.parseBoolean(blockValues[11]);
				} catch (Exception e) {
					// Above the block by default
					aboveBlock1 = true;
				}

				// Whether item1 is above or below the block
				try {
					aboveBlock2 = Boolean.parseBoolean(blockValues[12]);
				} catch (Exception e) {
					// Above the block by default
					aboveBlock2 = true;
				}

				/*
				 * 100 is a special wall ID which tells this to move onto the next row of the
				 * map and begin again.
				 */
				if (wallID == 100) {
					row++;
					col = 0;
					stop = true;
				} else {
					int[] entities = new int[2];
					int[] itemActIDs = new int[2];
					double[] rotations = new double[2];
					boolean[] aboveBlocks = new boolean[2];
					entities[0] = entityID1;
					entities[1] = entityID2;
					itemActIDs[0] = itemActID1;
					itemActIDs[1] = itemActID2;
					rotations[0] = itemRotation;
					rotations[1] = itemRotation2;
					aboveBlocks[0] = aboveBlock1;
					aboveBlocks[1] = aboveBlock2;

					// Set the next spot in the map to this new ValueHolder
					tempMap[row][col] = new ValueHolder(height, wallID, rotations, audioQueue, wallY, doorRaiseHeight,
							entities, itemActIDs, aboveBlocks);

					// Go to next column
					col++;

					// Stores how many columns are in this row
					if (!stop) {
						storeColumn++;
					}
				}
			}

			/*
			 * For now, all maps are made square in the text file so that the program works
			 * every time with these values. So the actual map loaded will be its correct
			 * size and not too small or large based on limitations that are manually set.
			 */
			map = new ValueHolder[row][storeColumn];

			/*
			 * Puts the values that actually matter in the temp map into the actual map
			 * loaded.
			 */
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					map[i][j] = tempMap[i][j];
				}
			}

			// Constructs the new level
			level = new Level(map);

			// Calculates the size of the new level
			calculatedSize = map.length * map[0].length;

			// Close the scanner
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();

			// If map doesn't load, exit to pause menu so game
			// Doesn't crash and you can retry
			Display.pauseGame = true;

			// Quit game is true
			Controller.quitGame = true;

			return;

			// Restart the game to quit
			// new Game(display, false, "");
		}
	}

	/**
	 * Used so much that I just created a method that can be called to do it.
	 */
	public void resetLists() {
		// Reset all entities in game
		entities = new ArrayList<Entity>();
		items = new ArrayList<Item>();
		bullets = new ArrayList<Bullet>();
		buttons = new ArrayList<Button>();
		hurtingBlocks = new ArrayList<HurtingBlock>();
		doors = new ArrayList<Door>();
		elevators = new ArrayList<Elevator>();
		corpses = new ArrayList<Corpse>();
		explosions = new ArrayList<Explosion>();
		solidItems = new ArrayList<Item>();
		enemyProjectiles = new ArrayList<EnemyFire>();
		activatable = new ArrayList<Item>();
		teleporters = new ArrayList<Item>();
		sprites = new ArrayList<HitSprite>();
		happySavers = new ArrayList<Item>();
		otherPlayers = new ArrayList<ServerPlayer>();
		activatedButtons = new ArrayList<Button>();
		activatedDoors = new ArrayList<Door>();
		activatedElevators = new ArrayList<Elevator>();
		bulletsAdded = new ArrayList<Bullet>();
		itemsAdded = new ArrayList<Item>();
	}

}
