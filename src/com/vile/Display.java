package com.vile;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.vile.entities.Bullet;
import com.vile.entities.Cartridge;
import com.vile.entities.Corpse;
import com.vile.entities.Entity;
import com.vile.entities.EntityParent;
import com.vile.entities.Explosion;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;
import com.vile.entities.ServerPlayer;
import com.vile.entities.Weapon;
import com.vile.graphics.Render3D;
import com.vile.graphics.Screen;
import com.vile.graphics.Textures;
//Imports files within mineFront package holding Render and Screen Objects
import com.vile.input.Controller;
import com.vile.input.InputHandler;
import com.vile.launcher.FPSLauncher;
import com.vile.launcher.LogIn;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;
import com.vile.server.ServerClient;

/**
 * @title Display
 * @author Alex Byrd Date Updated: 5/10/2017
 *
 *         Runs the Game initially, displays everything on the screen, keeps
 *         track of fps, and renders the screen. Also creates thread to keep
 *         track of all events and such.
 */
public class Display extends Canvas implements Runnable {

	/**
	 * Listens for keys pressed, and if the console is open appends any keys pressed
	 * to the console output in the form of a string.
	 */
	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				int keyCode = e.getKeyCode();

				switch (keyCode) {
				case KeyEvent.VK_BACK_SPACE:
					try {
						// Gets rid of one character at a time in the console output
						String temp = consoleOutput.substring(0, consoleOutput.length() - 1);
						consoleOutput = temp;
					} catch (Exception ex) {
						// Will Only break if there is no more console output
					}
					break;
				case KeyEvent.VK_SHIFT:
					// TODO Eventually will capitalize letters
					// String temp = consoleOutput.substring(0, consoleOutput.length() - 1);
					// consoleOutput = temp;
					break;
				case KeyEvent.VK_SPACE:
					// Space just adds a space
					consoleOutput += " ";
					break;
				case KeyEvent.VK_UP:
					// Can't cycle out of the range of commands available
					if (commandIterator <= commands.size() - 2) {
						commandIterator++;
					}

					// When you use the up arrow, it should cycle throught the commands
					// that were previously used for efficiency.
					consoleOutput = commands.get(commandIterator);

					break;
				case KeyEvent.VK_DOWN:
					/*
					 * Decrement the commandIterator to go backwards through the commands.
					 */
					if (commandIterator > -1) {
						commandIterator--;
					}

					if (commandIterator != -1) {
						// When you use the down arrow, it should cycle throught the commands
						// that were previously used but in reverse.
						consoleOutput = commands.get(commandIterator);
					} else {
						consoleOutput = "";
					}

					break;
				case KeyEvent.VK_ENTER:
					consoleOutput = consoleOutput.toLowerCase();
					consoleMessage = "";

					String[] parameters = consoleOutput.split(" ");

					// Give player all weapons and ammo limits
					if (parameters[0].equals("give")) {
						if (parameters.length == 2 && parameters[1].equals("all")) {

							// Go through all weapons and addAmmo until full
							for (int i = 0; i < Player.weapons.length; i++) {
								Player.weapons[i].ammo = Player.weapons[i].ammoLimit;
								Player.weapons[i].canBeEquipped = true;
								Player.weapons[1].dualWield = true;

								// For some reason an infinite loop is caused sometimes, so
								// this will be the fallback to fix that issue.
								int fallBack = 0;

								while (Player.weapons[i].addAmmo(20) && fallBack <= 200) {
									fallBack++;
								}
							}

							Player.hasBlueKey = true;
							Player.hasGreenKey = true;
							Player.hasRedKey = true;
							Player.hasYellowKey = true;

							consoleMessage = "Everything is given!";
						} else if (parameters.length == 2 && parameters[1].equals("weapons")) {
							Player.weapons[1].dualWield = true;

							// Go through all weapons and addAmmo until full
							for (int i = 0; i < Player.weapons.length; i++) {
								Player.weapons[i].canBeEquipped = true;
							}

							consoleMessage = "All weapons are now available";
						} else if (parameters.length == 2 && parameters[1].equals("ammo")) {

							// Go through all weapons and addAmmo until full
							for (int i = 0; i < Player.weapons.length; i++) {
								Player.weapons[i].ammo = Player.weapons[i].ammoLimit;

								// For some reason an infinite loop is caused sometimes, so
								// this will be the fallback to fix that issue.
								int fallBack = 0;

								while (Player.weapons[i].addAmmo(20) && fallBack <= 200) {
									fallBack++;
								}
							}

							consoleMessage = "All ammo is now completely restored!";
						} else if (parameters.length == 2 && parameters[1].equals("keys")) {
							Player.hasBlueKey = true;
							Player.hasGreenKey = true;
							Player.hasRedKey = true;
							Player.hasYellowKey = true;

							consoleMessage = "All Keys are now given!";
						} else if (parameters.length == 3 && parameters[1].equals("upgradepoints")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of upgrade points needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.upgradePoints += amountToAdd;

							consoleMessage = "You have been given " + amountToAdd + " upgrade points";
						} else if (parameters.length == 3 && parameters[1].equals("resurrections")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of resurrections needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.resurrections += amountToAdd;

							consoleMessage = "You have been given " + amountToAdd + " resurrect skulls";
						} else if (parameters.length == 3 && parameters[1].equals("health")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of health needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.health += amountToAdd;

							if (Player.health > Player.maxHealth) {
								Player.health = Player.maxHealth;
							}

							consoleMessage = "You have been given " + amountToAdd + " health";
						} else if (parameters.length == 3 && parameters[1].equals("maxhealth")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of max health needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.maxHealth += amountToAdd;

							consoleMessage = "You have added " + amountToAdd + " to your max health";
						} else if (parameters.length == 3 && parameters[1].equals("armor")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of armor needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.armor += amountToAdd;

							consoleMessage = "You have been given " + amountToAdd + " armor";
						} else if (parameters.length == 2 && parameters[1].equals("berserk")) {
							if (Player.meleeMultiplier != 4) {
								Player.meleeMultiplier = 4;

								consoleMessage = "You are now in beserk mode. Try slashing something!";
							} else {
								Player.meleeMultiplier = 1;

								consoleMessage = "You are now back to normal melee damage.";
							}
						} else if (parameters.length == 3 && parameters[1].equals("invisibility")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of invisibility needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.invisibility += amountToAdd;

							consoleMessage = "You have " + amountToAdd + " milliseconds of invisibility";
						} else if (parameters.length == 3 && parameters[1].equals("protection")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of environment protection needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.environProtectionTime += amountToAdd;

							consoleMessage = "You have " + amountToAdd + " milliseconds of environment protection";
						} else if (parameters.length == 3 && parameters[1].equals("vision")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of enhanced vision needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.vision += amountToAdd;

							consoleMessage = "You have " + amountToAdd + " milliseconds of enhanced vision";
						} else if (parameters.length == 3 && parameters[1].equals("immortality")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of immortality needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.immortality += amountToAdd;

							consoleMessage = "You have " + amountToAdd + " milliseconds of immortality";
						} else if (parameters.length == 3 && parameters[1].equals("drunk")) {
							int amountToAdd = 0;

							// Try to parse to int the amount of drunk needed
							try {
								amountToAdd = Integer.parseInt(parameters[2]);
							} catch (Exception ex) {

							}

							Player.drunkLevels += amountToAdd;

							consoleMessage = "You are " + amountToAdd + " more drunk";
						} else {
							consoleMessage = "More parameters needed: give <parameter> <amount: optional>";
						}
					} else if (consoleOutput.equals("noclip")
							|| (parameters.length == 2 && parameters[0].equals("noclip"))) {
						/*
						 * There are two ways to turn on or off a command. You can either just type the
						 * command to auto switch it, or you can give it a specific parameter like on or
						 * off to turn it on or off. This takes care of both cases.
						 */
						if ((!Player.noClipOn && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("on"))) {
							Player.noClipOn = true;
							consoleMessage = "NoClip turned on";
						} else {
							Player.noClipOn = false;
							consoleMessage = "NoClip turned off";
						}
					} else if (consoleOutput.equals("exit")) {
						System.exit(0);
					} else if (consoleOutput.equals("position")) {
						consoleMessage = "X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z;
					} else if (consoleOutput.equals("height")) {
						consoleMessage = "Player Height: " + Player.height;
					} else if (consoleOutput.equals("maxheight")) {
						consoleMessage = "Player Max Height: " + Player.maxHeight;
					} else if (consoleOutput.equals("resurrections")) {
						consoleMessage = "Resurrect skulls: " + Player.resurrections;
					} else if (consoleOutput.equals("drunkedness")) {
						consoleMessage = "Player drunk level: " + Player.drunkLevels;
					} else if (consoleOutput.equals("immortality")) {
						consoleMessage = "Player Immortality: " + Player.immortality;
					} else if (consoleOutput.equals("invisibility")) {
						consoleMessage = "Player Invisibility: " + Player.invisibility;
					} else if (consoleOutput.equals("vision")) {
						consoleMessage = "Player Vision: " + Player.vision;
					} else if (consoleOutput.equals("protection")) {
						consoleMessage = "Player Environment Protection: " + Player.environProtectionTime;
					} else if (consoleOutput.equals("maxHealth")) {
						consoleMessage = "Player Max Health: " + Player.maxHealth;
					} else if (parameters.length == 4 && parameters[0].equals("warp")) {
						double warpX = 0;
						double warpY = 0;
						double warpZ = 0;
						// Try to parse to int the amount of invisibility needed
						try {
							warpX = Double.parseDouble(parameters[1]);
							warpY = Double.parseDouble(parameters[2]);
							warpZ = Double.parseDouble(parameters[3]);

							Player.x = warpX;
							Player.y = warpY;
							Player.z = warpZ;

							SoundController.teleportation.playAudioFile(0);
							consoleMessage = "Player has teleported successfully";
						} catch (Exception ex) {
							consoleMessage = "Error, try: warp <x:decimal> <y:decimal> <z:decimal>";
						}
					} else if (parameters.length == 3 && parameters[0].equals("set")) {
						if (parameters[1].equals("jump")) {

							int jumpAmount = 8;
							// Try to parse to int the amount of jump height needed
							try {
								jumpAmount = Integer.parseInt(parameters[2]);

								Player.totalJump = jumpAmount;
								consoleMessage = "You can now jump " + jumpAmount + " units in the air";
							} catch (Exception ex) {
								consoleMessage = "Error, try: set jump <value:integer>";
							}
						}
					} else if (parameters.length == 2 && parameters[0].equals("clear")) {
						// Clear corpses from map if need be
						if (parameters[1].equals("corpses")) {
							Game.corpses = new ArrayList<Corpse>();
							consoleMessage = "All corpses are now cleared!";
						}
					} else if (parameters.length == 2 && parameters[0].equals("kill")) {
						// Kill all entities in map if this is called.
						if (parameters[1].equals("entities")) {
							int originalSize = Game.entities.size() - 1;

							// Has to go in reverse as the size of Game.entities changes with each death.
							for (int i = originalSize; i >= 0; i--) {
								Entity temp = Game.entities.get(i);

								temp.enemyDeath();
							}

							consoleMessage = "All entities have died!";
						} else if (parameters[1].equals("ahead")) {
							double bullY = -(Player.y * 0.085);

							// Bullet will be lower if player is crouching
							if (Player.yCorrect + 1 < Player.y) {
								bullY = 0.8;
							}
							// Create the bullet
							Bullet bullet = new Bullet(100000, 0.03, Player.x, bullY, Player.z, 0, Player.rotation,
									true);

							bullet.itemKiller = true;

							// If this is a client, add this bullet to the bulletsAdded arraylist so that it
							// may be added to the server and ticked there.
							if (Display.gameType == 1) {
								Game.bulletsAdded.add(bullet);
							}

							/*
							 * Instead of rendering the bullet and all that, just check its movement
							 * instantaneously in small increments to make it look like it hits the enemy
							 * instantaneously and also makes it more precise.
							 */
							while (bullet.move() && Display.gameType != 1) {
								// System.out.println(bullet.y);
								// Do nothing, just call the move method
							}

							consoleMessage = "Death hath cometh to those ahead of you!";
						}
					} else if (parameters.length >= 3 && parameters[0].equals("summon")) {
						if (parameters[1].equals("item")) {

							int ID = 0;
							// Try to parse to int the amount of jump height needed
							try {
								ID = Integer.parseInt(parameters[2]);

								if (ID > ItemNames.AIR.getID() && ID <= ItemNames.YELLOWKEY.getID()
										|| ID == ItemNames.SHOTGUN.getID() || ID == ItemNames.BREAKABLEWALL.getID()
										|| ID == ItemNames.SECRET.getID() || ID > 23 && ID < 44
										|| ID >= 47 && ID != ItemNames.BUTTON.getID() && ID != 58 && ID != 59
												&& ID != ItemNames.TURRET.getID() && ID != ItemNames.EXPLOSION.getID()
												&& ID != ItemNames.TOILET.getID() && ID != 110 && ID != 111
												&& ID < 124) {
									double xa = (0 * (Math.cos(Player.rotation)) + (Math.sin(Player.rotation))) * 1;
									double za = ((Math.cos(Player.rotation)) - (0 * Math.sin(Player.rotation))) * 1;

									// Item to be added to the map and block
									Item temp = null;
									double xPos = Player.x + xa;
									double zPos = Player.z + za;

									Block blockOn = Level.getBlock((int) xPos, (int) zPos);

									// Default y Value of an item
									double yValue = (blockOn.y * 4) + blockOn.height;

									/*
									 * If its not an explosive canister, add it as a normal item. Otherwise add it
									 * as an explosive canister
									 */
									temp = new Item(10, xPos, yValue, zPos, ID, 0, 0, "");

									for (int i = 0; i < ItemNames.values().length; i++) {
										if (ItemNames.values()[i].getID() == ID) {
											consoleMessage = "Successfully summoned item "
													+ ItemNames.values()[i].getName();
											break;
										}
									}

									// If the item is solid or another interactable block
									if (temp.isSolid || ID == ItemNames.BREAKABLEWALL.getID()
											|| ID == ItemNames.SECRET.getID() || ID == ItemNames.LINEDEF.getID()) {
										// Set item to being the item that is within this
										// block only if it is solid
										blockOn.wallItems.add(temp);
									}

									// If satellite dish, add to activatable list as well
									if (ID == ItemNames.RADAR.getID()) {
										Game.activatable.add(temp);
									}
									// If item supposed to be activated by button
									else if (ID == ItemNames.ACTIVATEEXP.getID() || ID == ItemNames.ENEMYSPAWN.getID()
											|| ID == ItemNames.WALLBEGONE.getID() || ID == ItemNames.RADAR.getID()) {
										Game.activatable.add(temp);
									} else if (ID == ItemNames.TELEPORTEREXIT.getID()
											|| ID == ItemNames.TELEPORTERENTER.getID()) {
										Game.teleporters.add(temp);

										blockOn.wallEntities.add(temp);
									}
								} else {
									consoleMessage = "Invalid Item ID. It could be an entity.";
								}
							} catch (Exception ex) {
								consoleMessage = "Error, try: summon item <ID:integer>";
							}
						} else if (parameters[1].equals("entity")) {
							int ID = 0;
							// Try to parse to int of the entity ID to summon
							try {
								ID = Integer.parseInt(parameters[2]);

								double xa = (0 * (Math.cos(Player.rotation)) + (Math.sin(Player.rotation))) * 2;
								double za = ((Math.cos(Player.rotation)) - (0 * Math.sin(Player.rotation))) * 2;

								// Position that the entity should be added to within the map
								double xPos = Player.x + xa;
								double zPos = Player.z + za;

								Block blockOn = Level.getBlock((int) xPos, (int) zPos);

								// Sets the y value based on the aboveBlock boolean
								double yValue = -((blockOn.y * 4) + blockOn.height);

								int entityActivationID = 0;
								double rotation = 0;
								boolean aboveBlock = true;

								// For the optional parameters of having a activation ID and rotation
								if (parameters.length > 3) {
									try {
										entityActivationID = Integer.parseInt(parameters[3]);
									} catch (Exception exc) {
										entityActivationID = 0;
									}

									try {
										rotation = Double.parseDouble(parameters[4]);
									} catch (Exception exc) {
										rotation = 0;
									}

									try {
										aboveBlock = Boolean.parseBoolean(parameters[5]);
									} catch (Exception exc) {
										aboveBlock = true;
									}
								}

								// If entity is supposed to be summoned below the block.
								if (!aboveBlock) {
									yValue = 0;
								}

								// Add this new entity to the map
								Entity temp = new Entity(xPos, yValue, zPos, ID, rotation, entityActivationID);

								Game.entities.add(temp);
								blockOn.entitiesOnBlock.add(temp);

								consoleMessage = "Entity successfully summoned!";
							} catch (Exception ex) {
								consoleMessage = "Error, try: summon entity <ID:integer>";
							}
						} else if (parameters[1].equals("block")) {
							int ID = 0;
							// Try to parse to int of the wall ID to summon
							try {
								ID = Integer.parseInt(parameters[2]);

								double xa = (0 * (Math.cos(Player.rotation)) + (Math.sin(Player.rotation))) * 1;
								double za = ((Math.cos(Player.rotation)) - (0 * Math.sin(Player.rotation))) * 1;

								// Position of the block to select
								double xPos = Player.x + xa;
								double zPos = Player.z + za;

								// Select block at that position
								Block block = Level.getBlock((int) xPos, (int) zPos);

								// default values before optional parameters. Breakable by default
								// in case it was a mistake.
								int yValue = 0;
								int height = 12;
								boolean breakable = true;

								// For the optional parameters for the walls yValue, height, and whether it can
								// be broken.
								if (parameters.length > 3) {
									try {
										yValue = Integer.parseInt(parameters[3]);
									} catch (Exception exc) {
										yValue = 0;
									}

									try {
										height = Integer.parseInt(parameters[4]);
									} catch (Exception exc) {
										height = 12;
									}

									try {
										breakable = Boolean.parseBoolean(parameters[5]);
									} catch (Exception exc) {
										breakable = true;
									}
								}

								Block newBlock = new Block(height, ID, yValue, block.x, block.z);

								// Add ability to break wall if it is breakable
								if (breakable) {
									Item temp = new Item(10, (block.x), 0, (block.z), ItemNames.BREAKABLEWALL.getID(),
											0, 0, "");

									newBlock.wallItems.add(temp);
								}

								// Add this new block to the map
								Level.blocks[block.x + block.z * Level.width] = newBlock;

								consoleMessage = "Block successfully built!";
							} catch (Exception ex) {
								consoleMessage = "Try: summon block <ID:int> <yValue:int> <height:int> <breakable:bool>";
							}
						}
					} else if (parameters[0].equals("gamemode")) {

						int ID = 0;
						// Try to parse to int for the gamemode type
						try {
							ID = Integer.parseInt(parameters[1]);

							if (ID <= 4 && ID >= 0) {
								FPSLauncher.modeChoice = ID;
								Game.skillMode = ID;

								consoleMessage = "Difficulty changed to " + ID;
							}
						} catch (Exception ex) {
							consoleMessage = "Error, Try: gamemode <Type:int>";
						}
					} else if (consoleOutput.equals("god") || (parameters.length == 2 && parameters[0].equals("god"))) {
						if ((!Player.godModeOn && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("on"))) {
							Player.godModeOn = true;
							consoleMessage = "You decide to play God";
						} else {
							Player.godModeOn = false;
							consoleMessage = "Now you're just a silly mortal again";
						}
					} else if (consoleOutput.equals("freeze")
							|| (parameters.length == 2 && parameters[0].equals("freeze"))) {
						if ((!Game.freezeMode && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("on"))) {
							Game.freezeMode = true;

							// Freeze all the entities by replacing the current entities
							// with counterparts that are rotated and look the same but are frozen
							// and can't attack.
							for (int i = 0; i < Game.entities.size(); i++) {
								Entity current = Game.entities.get(i);
								current.damage = 0;
								current.speed = 0;
								current.initialSpeed = 0;
								current.hasSpecial = false;
								current.hasMovement = false;
								current.isSolid = true;
								current.moveable = true;
								current.canTurn = false;
							}

							consoleMessage = "The entities have all frozen over!";
						} else {
							Game.freezeMode = false;

							// Reset all entities to being able to move again
							for (int i = 0; i < Game.entities.size(); i++) {
								Entity current = Game.entities.get(i);
								Entity temp = new Entity(current.xPos, current.yPos, current.zPos, current.ID,
										current.rotation, current.itemActivationID);
								Game.entities.set(i, temp);
							}

							consoleMessage = "The entities have thawed and begin to move again!";
						}
					} else if (consoleOutput.equals("suicide")) {
						Player.health = 0;
						consoleMessage = "You have killed yourself... that ain't right man";
					} else if (consoleOutput.equals("resurrect")) {
						Player.health = 100;
						Player.alive = true;
						consoleMessage = "Like Jesus, you come back from the dead!";
					} else if (consoleOutput.equals("fly") || (parameters.length == 2 && parameters[0].equals("fly"))) {
						if ((!Player.flyOn && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("on"))) {
							Controller.fallSpeed = 0.4;
							Player.flyOn = true;

							// No longer in a jump
							Controller.inJump = false;
							consoleMessage = "You feel a bit lighter";
						} else {
							Player.flyOn = false;
							Controller.fallAmount = Player.y - Player.maxHeight;
							consoleMessage = "Oof... looks like gravity is back";
						}
					} else if (consoleOutput.equals("infammo")
							|| (parameters.length == 2 && parameters[0].equals("infammo"))) {
						if ((Player.unlimitedAmmoOn && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("off"))) {
							Player.unlimitedAmmoOn = false;
							consoleMessage = "Bullets mean something again";
						} else {
							Player.unlimitedAmmoOn = true;
							consoleMessage = "Ammo means nothing to you anymore";
						}
					} else if (parameters.length == 2 && parameters[0].equals("superspeed")) {
						// Sets players speed to the second parameter. If it not an integer as it should
						// be
						// then just by default set it to a multiplier of 1.
						try {
							Player.speedMultiplier = Integer.parseInt(parameters[1]);
						} catch (Exception ex) {
							Player.speedMultiplier = 1;
						}
						consoleMessage = "Your speed has changed";
					} else if (consoleOutput.equals("clearfog")
							|| (parameters.length == 2 && parameters[0].equals("clearfog"))) {
						if ((Render3D.renderDistanceDefault > Level.renderDistance && parameters.length == 1)
								|| (parameters.length == 2 && parameters[1].equals("on"))) {
							Render3D.renderDistanceDefault = Level.renderDistance;
							consoleMessage = "The fog returns, and with it new horrors";
						} else {
							Render3D.renderDistanceDefault = 10000000;
							consoleMessage = "The way is cleared!";
						}
					} else {
						consoleMessage = "That command does not exist. Try another.";
					}

					commands.add(0, consoleOutput);

					// Only allow 50 commands to be saved in memory
					if (commands.size() > 50) {
						commands.remove(50);
					}

					commandIterator = -1;
					consoleOutput = "";
					break;

				default:
					String toAdd = FPSLauncher.keyCodeToString(keyCode);

					// Only adds single characters or numbers to the command line
					// and not things like Esc, Up, etc...
					if (toAdd.length() > 1) {
						break;
					}

					consoleOutput += toAdd;
					break;
				}
			} else if (e.getID() == KeyEvent.KEY_RELEASED)

			{
				// System.out.println("2test2");
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				// System.out.println("3test3");
			}

			return false;
		}
	}

	// Don't worry about this, java recommends this for some reason
	private static final long serialVersionUID = 1L;

	// Version Number
	private static final double versionNumber = 1.7;

	// Frame Width and height
	public static int HEIGHT = 600;
	public static int WIDTH = 800;

	// Width and height of computer screen
	public static int screenHeight = 0;
	public static int screenWidth = 0;

	// Frame title
	public static final String TITLE = "Vile Alpha " + versionNumber;

	// Is music audio on?
	public static boolean audioOn = false;

	// Audio clip that is loaded in for music
	public static Clip music;

	// Keeps track of how fast you move mouse
	public static int mouseSpeedHorizontal;
	public static int mouseSpeedVertical;

	// Thread of events
	private static Thread thread;
	// private static Thread thread2;
	// private static Thread thread3;

	// Determines if program is running
	public static boolean isRunning = false;

	// Determines whether the game is paused
	public static boolean pauseGame = false;

	// What type of game is this (0 = host, 1 = client, 2 = single player)
	public static int gameType = 2;

	// Only tick host as fast as first client on the server for now.
	public static int hostTick = 0;

	// New screen object
	private Screen screen;

	// The BufferedImage that is displayed of pixels on screen
	private BufferedImage img;

	// Creates a new game (Handles all key events and ticks)
	public static Game game;

	// Handles input events
	public static InputHandler input;

	// Used to display frames per second
	public static int fps = 0;

	// Current music theme number
	public static int musicTheme = 2;

	// Last music theme that was played. USED FOR MLG MODE
	public static int oldMusicTheme = 2;

	// How the ints are rendered into pixels of a certain color
	private BufferStrategy bs;

	public static boolean mouseOn = true; // Is the mouse on?

	// Continues to change the face direction and look.
	private int facePhase = 0;

	/*
	 * All different HUD images to load up
	 */
	private BufferedImage yellowKey;
	private BufferedImage redKey;
	private BufferedImage blueKey;
	private BufferedImage greenKey;
	private BufferedImage decietScepter;
	private BufferedImage decietScepter1;
	private BufferedImage decietScepter2;
	private BufferedImage decietScepter3;
	private BufferedImage decietScepter4;
	private BufferedImage frozenFace;
	private BufferedImage HUD;
	private BufferedImage healthyFace1;
	private BufferedImage healthyFace2;
	private BufferedImage healthyFace3;
	private BufferedImage hurtFace1;
	private BufferedImage hurtFace2;
	private BufferedImage hurtFace3;
	private BufferedImage veryHurtFace1;
	private BufferedImage veryHurtFace2;
	private BufferedImage veryHurtFace3;
	private BufferedImage almostDead1;
	private BufferedImage almostDead2;
	private BufferedImage almostDead3;
	private BufferedImage dead;
	private BufferedImage godMode;
	private BufferedImage gunNormal;
	private BufferedImage invisible;
	private BufferedImage playerHarmedHealthy;
	private BufferedImage playerHarmedHurt;
	private BufferedImage playerHarmedVeryHurt;
	private BufferedImage playerHarmedAlmostDead;
	private BufferedImage invisGodmode;
	private BufferedImage gunShot;
	private BufferedImage gunShot2;
	private BufferedImage gunShot3;
	private BufferedImage gunShot4;
	private BufferedImage phaseCannon1;
	private BufferedImage phaseCannon2;
	private BufferedImage phaseCannon3;
	private BufferedImage phaseCannon4;
	private BufferedImage rocketLauncher;
	private BufferedImage rocketLauncher1;
	private BufferedImage rocketLauncher2;
	private BufferedImage rocketLauncher3;
	private BufferedImage rocketLauncher4;
	private BufferedImage pistolLeft1;
	private BufferedImage pistolLeft2;
	private BufferedImage pistolLeft3;
	private BufferedImage pistolLeft4;
	private BufferedImage pistolLeft5;
	private BufferedImage pistolRight1;
	private BufferedImage pistolRight2;
	private BufferedImage pistolRight3;
	private BufferedImage pistolRight4;
	private BufferedImage pistolRight5;
	private BufferedImage weaponUpgradePoint1;
	private BufferedImage weaponUpgradePoint2;
	private BufferedImage weaponUpgradePoint3;
	private BufferedImage weaponUpgradePoint4;
	private BufferedImage protectionIcon;
	private BufferedImage visionIcon;
	private BufferedImage immortalityIcon;
	private BufferedImage invisibilityIcon;
	private BufferedImage frozenIcon;
	private BufferedImage drunkIcon;
	private BufferedImage sword1;
	private BufferedImage sword2;
	private BufferedImage sword3;
	private BufferedImage swordAttack1;
	private BufferedImage swordAttack2;
	private BufferedImage swordAttack3;
	private BufferedImage swordAttack4;
	private BufferedImage swordAttack5;
	private BufferedImage clearLevelBackground;

	/*
	 * Sets up a string that can change and disappears after 100 ticks about the
	 * most recent pick up you got, or if you are trying to open a door that
	 * requires a key you don't have.
	 */
	public static ArrayList<PopUp> messages = new ArrayList<PopUp>();

	// In case user wants to manually set the map that is played
	public static boolean nonDefaultMap = false;
	public static String newMapName = "";

	// Keeps track of recent mouse values
	public static int oldX = 0;
	public static int oldY = 0;
	public static int newX = 0;
	public static int newY = 0;

	// The new sound engine I have set up
	public static SoundController soundController;

	// No theme when game first starts. Used to reset values if
	// resource pack changes
	public static String currentTheme = "";

	/*
	 * FOR AN EXPERIMENTAL SMOOTH FPS MODE. CAN ONLY BE CHANGED HERE. YOU CAN TEST
	 * THE EFFECTS IF YOU WANT BUT SOME ARE WONKY. SOME FIX BUGS, SOME CAUSE BUGS,
	 * YOU KNOW THE DRILL.
	 */
	public static boolean smoothFPS = false;
	public static boolean smileMode = false;

	// Items will respawn if this is turned on.
	public static boolean itemsRespawn = false;

	// Is the cleared level screen being shown?
	public static boolean clearedLevel = false;

	// Is the command console. If so pause the game and pull down the console for
	// typing.
	public static boolean consoleOpen = false;

	// Output generated by user in console
	public static String consoleOutput = "";

	// Used so the inputStream can be closed for music after you pause or
	// start a new game
	public static AudioInputStream inputStream;

	private int hudMovementTick = 0;

	private static MyDispatcher keyDispatcher = null;
	private ArrayList<String> commands = new ArrayList<String>();
	private int commandIterator = -1;
	private static String consoleMessage = "";
	private int swordTick = 0;

	/**
	 * Creates a new display by setting up the screen and its dimensions, sets up
	 * the image the screen displays, how the pixels will render when set, and adds
	 * all the input stuff so that the program will read input from the mouse,
	 * keyboard, and can set a focus on them if needed.
	 */
	public Display() {
		// Close all threads if new game
		if (!FPSLauncher.returning) {
			try {
				thread.join();
			} catch (Exception e) {
			}

			/*
			 * try { thread2.join(); } catch (Exception e) {
			 * 
			 * }
			 * 
			 * try { thread3.join(); } catch (Exception e) {
			 * 
			 * }
			 */

			try {
				inputStream.close();
			} catch (Exception e) {

			}

			inputStream = null;

			try {
				music.close();
			} catch (Exception e) {

			}

			music = null;

			thread = null;
			// thread2 = null;
			// thread3 = null;

			messages = null;
			messages = new ArrayList<PopUp>();

			game = null;
		}

		// TODO Only loads for host. If problems with this come to this checkpoint
		// Host only loads certain aspects up before starting the game.
		if (gameType == 0) {
			new Player();
			// The game is not ending yet
			Controller.quitGame = false;

			// Game is not paused
			Display.pauseGame = false;

			// If new game, reset the gameMode
			Game.skillMode = FPSLauncher.modeChoice;

			// New Game and map. Sets up music for that map too
			game = new Game(this, nonDefaultMap, newMapName);

			Game.freezeMode = false;

			// If themes aren't the same, then reset the sounds. Otherwise it won't
			if (!currentTheme.equals(FPSLauncher.themeName))
			// if(currentTheme.equals(""))
			{
				try {
					soundController.resetSounds();
				} catch (Exception e) {
				}
				// Loads all sounds and files
				soundController = null;
				soundController = new SoundController();
			}

			currentTheme = FPSLauncher.themeName;

			// All sound volumes are reset
			soundController.resetAllVolumes(FPSLauncher.soundVolumeLevel);

			// A new scanner object that is defaultly set to null
			Scanner sc = null;

			/*
			 * Try to read the file and if not, state the error
			 */
			try {
				// Creates a Scanner that can read the file
				sc = new Scanner(
						new BufferedReader(new FileReader("resources" + FPSLauncher.themeName + "/settings.txt")));
				/*
				 * Read through lines in the resource pack settings file to determine what game
				 * settings to change depending on special attributes set within this resource
				 * pack.
				 */
				String currentLine = "";

				currentLine = sc.nextLine();
				String[] elements = currentLine.split(":");
				String[] colorAttributes = elements[1].split(",");

				// Grab all the different color attributes
				int transparency = Integer.parseInt(colorAttributes[0]);
				int red = Integer.parseInt(colorAttributes[1]);
				int green = Integer.parseInt(colorAttributes[2]);
				int blue = Integer.parseInt(colorAttributes[3]);

				// Shift red bits 16, green 8, and blue none and add the bits together
				// to get the color that will be transparent.
				Render3D.seeThroughWallPixel = transparency << 24 | red << 16 | green << 8 | blue;

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				Controller.acceleration = Double.parseDouble(elements[1]);

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				Player.totalJump = Integer.parseInt(elements[1]);

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				// Only if in survival
				if (FPSLauncher.gameMode == 1) {
					Render3D.renderDistanceDefault = Double.parseDouble(elements[1]);
				}

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				// Will game have smile mechanics or not
				if (Integer.parseInt(elements[1]) == 1) {
					smileMode = true;
				} else {
					smileMode = false;
				}
			} catch (Exception e) {
				System.out.println(e);
				// Set to defaults if failed
				Render3D.seeThroughWallPixel = 0xffffffff;
				Controller.acceleration = 0.03;
				Player.totalJump = 8;
				Render3D.renderDistanceDefault = 100000.0;
				smileMode = false;
			}

			sc.close();
			sc = null;

			input = null;
			input = new InputHandler();

		} else {

			// A temporary frame to display where the game is at in the
			// loading process
			JFrame loading = new JFrame("Loading Variables... 0% Loaded");

			// Sets frame size, centers it on screen, makes it nonresizable,
			// and still closable.
			loading.setSize(WIDTH, HEIGHT);
			loading.setLocationRelativeTo(null);
			loading.setResizable(false);
			loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Set app icon
			ImageIcon titleIcon = new ImageIcon("resources" + FPSLauncher.themeName + "/textures/hud/titleIcon.png");
			loading.setIconImage(titleIcon.getImage());

			// Make frame visible
			loading.setVisible(true);

			// The game is not ending yet
			Controller.quitGame = false;

			// Game is not paused
			Display.pauseGame = false;

			// Just displays point in loading.
			loading.setTitle("Loading sounds... 5% Loaded");

			// Music changes if the themes aren't the same, or if the
			// music theme for survival is changed.
			if (!currentTheme.equals(FPSLauncher.themeName) || musicTheme != oldMusicTheme) {
				try {
					inputStream.close();
				} catch (Exception e) {

				}

				inputStream = null;

				try {
					music.close();
				} catch (Exception e) {

				}

				oldMusicTheme = musicTheme;

				music = null;
				audioOn = false;
			}

			// Only resets game values if the game needs to be restarted
			if (!FPSLauncher.returning) {
				loading.setTitle("Setting up game entities... 35% Loaded");

				KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

				try {
					manager.removeKeyEventDispatcher(keyDispatcher);
				} catch (Exception e) {

				}

				keyDispatcher = new MyDispatcher();
				manager.addKeyEventDispatcher(keyDispatcher);

				// If new game, reset the gameMode
				Game.skillMode = FPSLauncher.modeChoice;

				// Resets player values (health, ammo, etc...)
				new Player();

				// TODO Have this able to be changed.
				// Pistol is the only weapon defaultly equipped (can change later for
				// resource packs)
				Player.weapons[0].canBeEquipped = true;

				/*
				 * When there is a new game, there is no previous music theme so oldMusicTheme
				 * is set equal to the current music theme.
				 */
				oldMusicTheme = musicTheme;

				// New Game and map. Sets up music for that map too
				if (gameType == 2) {
					// If themes aren't the same, then reset the sounds. Otherwise it won't
					if (!currentTheme.equals(FPSLauncher.themeName))
					// if(currentTheme.equals(""))
					{
						try {
							soundController.resetSounds();
						} catch (Exception e) {
						}
						// Loads all sounds and files
						soundController = null;
						soundController = new SoundController();
					}

					game = new Game(this, nonDefaultMap, newMapName);
				}

				// Reset the popup list
				messages = new ArrayList<PopUp>();

				// Reset debug values
				Controller.showFPS = false;

				// Only if not loading the game.
				if (!FPSLauncher.loadingGame) {
					Player.flyOn = false;
					Player.noClipOn = false;
					Player.speedMultiplier = 1;
					Player.godModeOn = false;
					Player.unlimitedAmmoOn = false;
					Game.freezeMode = false;
				} else {
					// Set to no longer loading in a game if it got to here.
					FPSLauncher.loadingGame = false;
				}

				loading.setTitle("Setting up music... 50% Loaded");

				// Only do if there is music playing
				if (audioOn) {
					Sound.setMusicVolume(music);
				}
			}
			// If just resuming game, most values are already loaded
			else {
				loading.setTitle("Setting up music... 50% Loaded");

				// Only do if there is music playing
				if (audioOn) {
					Sound.setMusicVolume(music);
				}
			}

			loading.setTitle("Resetting textures and GUI... 65% Loaded");

			// A new scanner object that is defaultly set to null
			Scanner sc = null;

			/*
			 * Try to read the file and if not, state the error
			 */
			try {
				// Creates a Scanner that can read the file
				sc = new Scanner(
						new BufferedReader(new FileReader("resources" + FPSLauncher.themeName + "/settings.txt")));
				/*
				 * Read through lines in the resource pack settings file to determine what game
				 * settings to change depending on special attributes set within this resource
				 * pack.
				 */
				String currentLine = "";

				currentLine = sc.nextLine();
				String[] elements = currentLine.split(":");
				String[] colorAttributes = elements[1].split(",");

				// Grab all the different color attributes
				int transparency = Integer.parseInt(colorAttributes[0]);
				int red = Integer.parseInt(colorAttributes[1]);
				int green = Integer.parseInt(colorAttributes[2]);
				int blue = Integer.parseInt(colorAttributes[3]);

				// Shift red bits 16, green 8, and blue none and add the bits together
				// to get the color that will be transparent.
				Render3D.seeThroughWallPixel = transparency << 24 | red << 16 | green << 8 | blue;

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				Controller.acceleration = Double.parseDouble(elements[1]);

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				Player.totalJump = Integer.parseInt(elements[1]);

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				// Only if in survival
				if (FPSLauncher.gameMode == 1) {
					Render3D.renderDistanceDefault = Double.parseDouble(elements[1]);
				}

				currentLine = sc.nextLine();
				elements = currentLine.split(":");

				// Will game have smile mechanics or not
				if (Integer.parseInt(elements[1]) == 1) {
					smileMode = true;
				} else {
					smileMode = false;
				}
			} catch (Exception e) {
				System.out.println(e);
				// Set to defaults if failed
				Render3D.seeThroughWallPixel = 0xffffffff;
				Controller.acceleration = 0.03;
				Player.totalJump = 8;
				Render3D.renderDistanceDefault = 100000.0;
				smileMode = false;
			}

			sc.close();
			sc = null;

			// If themes aren't the same, then reset the sounds. Otherwise it won't
			if (!currentTheme.equals(FPSLauncher.themeName))
			// if(currentTheme.equals(""))
			{
				try {
					soundController.resetSounds();
				} catch (Exception e) {
				}
				// Loads all sounds and files
				soundController = null;
				soundController = new SoundController();
			}

			currentTheme = FPSLauncher.themeName;

			// All sound volumes are reset
			soundController.resetAllVolumes(FPSLauncher.soundVolumeLevel);

			// Try to load all the HUD images. If not catch the exception
			try {
				loadHUD();
			} catch (Exception e) {

			}

			// Set up floor and ceiling textures
			Textures.Textures();

			// Try and set all textures (other than floor and ceiling) to null to prevent
			// memory loss
			Textures.setToNull();

			// Resets games textures depending on theme
			Textures.resetTextures();

			// Resets the frames width and height
			setGameWidth();
			setGameHeight();

			/*
			 * Creates different dimensions of the screen that can be used
			 */
			Dimension size = new Dimension(WIDTH, HEIGHT);
			Dimension minSize = new Dimension(0, 0);
			Dimension maxSize = new Dimension(5000, 5000);

			/*
			 * Sets the max, min, and preferred sizes of the screen
			 */
			setPreferredSize(size);
			setMinimumSize(minSize);
			setMaximumSize(maxSize);

			// Sets up the new screen used (2D array of int values (pixels)
			// basically.
			screen = new Screen();

			// Sets up the BufferedImage size, and type (ints = color info)
			img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

			// Gets the data from each integer and converts it to a color type
			Screen.render3D.PIXELS = null;
			Screen.render3D.PIXELS = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

			loading.setTitle("Setting up input handling listeners... 90% Loaded");

			consoleMessage = "";

			/*
			 * Sets up all the input stuff and listeners
			 */
			input = null;
			input = new InputHandler();
			addKeyListener(input);
			addMouseListener(input);
			addFocusListener(input);
			addMouseMotionListener(input);
			addMouseWheelListener(input);

			// Set mouse values to starting position
			newX = InputHandler.MouseX;
			newY = InputHandler.MouseY;
			oldX = InputHandler.MouseX;
			oldY = InputHandler.MouseY;

			loading.setTitle("Done 100% Loaded");

			// Get rid of loading frame when done loading
			loading.dispose();

			// System.out.println(Thread.activeCount());
		}
	}

	/**
	 * Sets Width of frame depending on the the graphics selection from the
	 * launcher.
	 */
	public static void setGameWidth() {
		// Gets computer screen width
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();

		// Total width of screen size in integer form
		screenWidth = (int) width;

		// If screen is odd width across fix it so that it is not so that
		// low res mode will still work.
		if (screenWidth % 2 != 0) {
			screenWidth--;
		}

		// Change frame width based on graphics selection
		if (FPSLauncher.resolutionChoice <= 1) {
			WIDTH = 800;
		} else if (FPSLauncher.resolutionChoice <= 3) {
			WIDTH = 1020;
		} else {
			WIDTH = screenWidth;
		}
	}

	/**
	 * Sets Height of frame depending on the graphics selection from the launcher.
	 */
	public static void setGameHeight() {
		// Same as above but for height
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();

		// Total height of computer screen
		screenHeight = (int) height;

		// If screen is odd height across fix it so that it is not so that
		// low res mode will still work.
		if (screenHeight % 2 != 0) {
			screenHeight--;
		}

		if (FPSLauncher.resolutionChoice <= 1) {
			HEIGHT = 600;
		} else if (FPSLauncher.resolutionChoice <= 3) {
			HEIGHT = 700;
		} else {
			HEIGHT = (int) height;
		}
	}

	/**
	 * Initiates the game to start running, and sets isRunning to true. Synchronized
	 * helps synchronize the thread to run correctly if the program is being ran in
	 * an applet.
	 */
	public synchronized void start() {
		// If program is already running, just return
		if (isRunning == true) {
			return;
		}

		// If not, set the program to start running
		else {
			isRunning = true;

			// Starts a new thread to handle all the events
			thread = new Thread(this);
			// thread3 = new Thread(this);
			thread.start();
			// thread2.start();
			// thread3.start();
		}
	}

	/**
	 * What is called if the program is running. It renders the screen, performs all
	 * the actions in the thread of events, tracks fps, etc.
	 */
	@Override
	public synchronized void run() {
		// Frames that have elapsed within the last second
		int frames = 0;

		// How many ticks between seconds
		int tickCount = 0;

		/*
		 * I.... I'm still learning how this one works. I'm guessing maybe the time lost
		 * while this is checking for the games fps?
		 */
		double unprocessedSeconds = 0;

		// How many seconds are in a tick I'm guessing
		double secondsPerTick = (1 / 60.0);

		// The last time the game ticked. Initially set to current time
		long previousTime = System.nanoTime();

		if (Display.gameType == 1) {
			String fromServer = "";
			String fromUser = "";

			boolean firstTime = true;

			/*
			 * Focuses your user on the screen so you don't have to click to start
			 */
			requestFocus();

			try {
				PrintWriter out;
				BufferedReader in;
				ServerClient.hostSocket = new Socket(ServerClient.hostData[0],
						Integer.parseInt(ServerClient.hostData[1]));
				out = new PrintWriter(ServerClient.hostSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(ServerClient.hostSocket.getInputStream()));
				// While the game is running, keep ticking and rendering
				// while (isRunning == true) {
				while ((fromServer = in.readLine()) != null) {
					if (firstTime) {
						String[] sections = fromServer.split("\\?");
						String currentSection = sections[0];
						String[] attributes = currentSection.split(":");

						Player.startX = Double.parseDouble(attributes[0]);
						Player.startY = Double.parseDouble(attributes[1]);
						Player.startZ = Double.parseDouble(attributes[2]);
						game = new Game(this, true, attributes[3].trim());
						firstTime = false;

						currentSection = sections[1];
						String[] entitiesOfType = currentSection.split(";");

						for (String player : entitiesOfType) {
							attributes = player.split(":");

							if (attributes[0].trim().equals("Client")) {
								Player.x = Player.startX;
								Player.y = Player.startY;
								Player.z = Player.startZ;
								Player.ID = Integer.parseInt(attributes[4]);
								Player.health = Integer.parseInt(attributes[5]);
								Player.maxHealth = Integer.parseInt(attributes[6]);
								Player.armor = Integer.parseInt(attributes[7]);
								Player.environProtectionTime = Integer.parseInt(attributes[8]);
								Player.immortality = Integer.parseInt(attributes[9]);
								Player.vision = Integer.parseInt(attributes[10]);
								Player.invisibility = Integer.parseInt(attributes[11]);
								Player.height = Double.parseDouble(attributes[12]);
								Player.rotation = Double.parseDouble(attributes[13]);
								Player.xEffects = Double.parseDouble(attributes[14]);
								Player.yEffects = Double.parseDouble(attributes[15]);
								Player.zEffects = Double.parseDouble(attributes[16]);
								Player.alive = Boolean.parseBoolean(attributes[17]);
								Player.kills = Integer.parseInt(attributes[18]);
								Player.deaths = Integer.parseInt(attributes[19]);
								Player.forceCrouch = Boolean.parseBoolean(attributes[20]);
								Player.hasBlueKey = Boolean.parseBoolean(attributes[21]);
								Player.hasRedKey = Boolean.parseBoolean(attributes[22]);
								Player.hasGreenKey = Boolean.parseBoolean(attributes[23]);
								Player.hasYellowKey = Boolean.parseBoolean(attributes[24]);
								Player.resurrections = Integer.parseInt(attributes[25]);
								Player.weaponEquipped = Integer.parseInt(attributes[26]);

								String[] weapons = attributes[27].split("-");
								/*
								 * For each weapon, load in its attributes depending on what they were when the
								 * game was saved.
								 */
								for (int i = 0; i < weapons.length; i++) {
									Weapon w = Player.weapons[i];

									String[] weaponStats = weapons[i].split(",");

									int size = weaponStats.length - 4;

									w.weaponID = Integer.parseInt(weaponStats[0]);
									w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
									w.dualWield = Boolean.parseBoolean(weaponStats[2]);
									w.ammo = Integer.parseInt(weaponStats[3]);

									for (int j = 0; j < size; j++) {
										w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
									}
								}

								// Pop up messages are added immediately
								String[] messages = attributes[28].split("-");

								if (!messages[0].trim().equals("-1")) {
									for (String message : messages) {
										if (!message.trim().equals("1") && !message.trim().equals("")) {
											Display.messages.add(new PopUp(message));
										}
									}
								}

								// Audio stuff that should also be played immediately every tick
								String[] audioNames = attributes[29].split("-");
								String[] distances = attributes[20].split("-");

								if (!audioNames[0].trim().equals("-1") && !distances[0].trim().equals("-1")
										&& audioNames.length == distances.length) {
									for (int i = 0; i < audioNames.length; i++) {
										for (Sound currentSound : soundController.allSounds) {
											if (audioNames[i].trim().equals(currentSound.audioName.trim())) {
												try {
													currentSound.playAudioFile(Double.parseDouble(distances[i]));
												} catch (Exception e) {

												}
											}
										}
									}
								}

							} else {
								ServerPlayer sP = new ServerPlayer();

								currentSection = sections[1];
								entitiesOfType = currentSection.split(";");

								sP.x = Double.parseDouble(attributes[1]);
								sP.y = Double.parseDouble(attributes[2]);
								sP.z = Double.parseDouble(attributes[3]);
								sP.ID = Integer.parseInt(attributes[4]);
								sP.health = Integer.parseInt(attributes[5]);
								sP.maxHealth = Integer.parseInt(attributes[6]);
								sP.armor = Integer.parseInt(attributes[7]);
								sP.environProtectionTime = Integer.parseInt(attributes[8]);
								sP.immortality = Integer.parseInt(attributes[9]);
								sP.vision = Integer.parseInt(attributes[10]);
								sP.invisibility = Integer.parseInt(attributes[11]);
								sP.height = Double.parseDouble(attributes[12]);
								sP.rotation = Double.parseDouble(attributes[13]);
								sP.xEffects = Double.parseDouble(attributes[14]);
								sP.yEffects = Double.parseDouble(attributes[15]);
								sP.zEffects = Double.parseDouble(attributes[16]);
								sP.alive = Boolean.parseBoolean(attributes[17]);
								sP.kills = Integer.parseInt(attributes[18]);
								sP.deaths = Integer.parseInt(attributes[19]);
								sP.forceCrouch = Boolean.parseBoolean(attributes[20]);
								sP.hasBlueKey = Boolean.parseBoolean(attributes[21]);
								sP.hasRedKey = Boolean.parseBoolean(attributes[22]);
								sP.hasGreenKey = Boolean.parseBoolean(attributes[23]);
								sP.hasYellowKey = Boolean.parseBoolean(attributes[24]);
								sP.resurrections = Integer.parseInt(attributes[25]);
								sP.weaponEquipped = Integer.parseInt(attributes[26]);

								String[] weapons = attributes[27].split("-");
								/*
								 * For each weapon, load in its attributes depending on what they were when the
								 * game was saved.
								 */
								for (int i = 0; i < weapons.length; i++) {
									Weapon w = sP.weapons[i];

									String[] weaponStats = weapons[i].split(",");

									int size = weaponStats.length - 4;

									w.weaponID = Integer.parseInt(weaponStats[0]);
									w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
									w.dualWield = Boolean.parseBoolean(weaponStats[2]);
									w.ammo = Integer.parseInt(weaponStats[3]);

									for (int j = 0; j < size; j++) {
										w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
									}
								}

								// Pop up messages are added immediately
								String[] messages = attributes[28].split("-");

								if (!messages[0].trim().equals("-1")) {
									for (String message : messages) {
										if (!message.trim().equals("1") && !message.trim().equals("")) {
											sP.clientMessages.add(new PopUp(message));
										}
									}
								}

								// Audio stuff that should also be played immediately every tick
								String[] audioNames = attributes[29].split("-");
								String[] distances = attributes[20].split("-");

								if (!audioNames[0].trim().equals("-1") && !distances[0].trim().equals("-1")
										&& audioNames.length == distances.length) {
									for (int i = 0; i < audioNames.length; i++) {
										sP.audioToPlay.add(audioNames[i]);
										sP.audioDistances.add(new Integer(distances[i]));
									}
								}
								Game.otherPlayers.add(sP);

							}
						}

						// currentSection = sections[2];
						// entitiesOfType = currentSection.split(";");

						/*
						 * if (entitiesOfType.length > 1) { attributes = entitiesOfType[0].split(":");
						 * Level.height = Integer.parseInt(attributes[1]); Level.width =
						 * Integer.parseInt(attributes[2]); Level.blocks = new Block[Level.height *
						 * Level.width];
						 * 
						 * for (int i = 1; i < entitiesOfType.length; i++) { attributes =
						 * entitiesOfType[i].split(":");
						 * 
						 * // Create enemy with its needed values Block b = new
						 * Block(Double.parseDouble(attributes[6]), Integer.parseInt(attributes[4]),
						 * Double.parseDouble(attributes[2]) * 4, Integer.parseInt(attributes[1]),
						 * Integer.parseInt(attributes[3]));
						 * 
						 * b.health = Integer.parseInt(attributes[0]); b.wallPhase =
						 * Integer.parseInt(attributes[5]); b.isSolid =
						 * Boolean.parseBoolean(attributes[7]); b.seeThrough =
						 * Boolean.parseBoolean(attributes[8]);
						 * 
						 * Level.blocks[b.x + b.z * Level.width] = b; } }
						 */

					} else {
						game.resetLists();

						// System.out.println(fromServer);

						String[] sections = fromServer.split("\\?");

						String[] attributes;
						String currentSection = sections[0];
						String[] entitiesOfType = currentSection.split(";");

						for (String player : entitiesOfType) {
							attributes = player.split(":");

							if (attributes[0].trim().equals("Client")) {
								Player.x = Double.parseDouble(attributes[1]);
								Player.y = Double.parseDouble(attributes[2]);
								Player.z = Double.parseDouble(attributes[3]);
								Player.ID = Integer.parseInt(attributes[4]);
								Player.health = Integer.parseInt(attributes[5]);
								Player.maxHealth = Integer.parseInt(attributes[6]);
								Player.armor = Integer.parseInt(attributes[7]);
								Player.environProtectionTime = Integer.parseInt(attributes[8]);
								Player.immortality = Integer.parseInt(attributes[9]);
								Player.vision = Integer.parseInt(attributes[10]);
								Player.invisibility = Integer.parseInt(attributes[11]);
								Player.height = Double.parseDouble(attributes[12]);
								Player.rotation = Double.parseDouble(attributes[13]);
								Player.xEffects = Double.parseDouble(attributes[14]);
								Player.yEffects = Double.parseDouble(attributes[15]);
								Player.zEffects = Double.parseDouble(attributes[16]);
								Player.alive = Boolean.parseBoolean(attributes[17]);
								Player.kills = Integer.parseInt(attributes[18]);
								Player.deaths = Integer.parseInt(attributes[19]);
								// Player.forceCrouch = Boolean.parseBoolean(attributes[20]);
								Player.hasBlueKey = Boolean.parseBoolean(attributes[21]);
								Player.hasRedKey = Boolean.parseBoolean(attributes[22]);
								Player.hasGreenKey = Boolean.parseBoolean(attributes[23]);
								Player.hasYellowKey = Boolean.parseBoolean(attributes[24]);
								Player.resurrections = Integer.parseInt(attributes[25]);
								Player.weaponEquipped = Integer.parseInt(attributes[26]);

								String[] weapons = attributes[27].split("-");
								/*
								 * For each weapon, load in its attributes depending on what they were when the
								 * game was saved.
								 */
								for (int i = 0; i < weapons.length; i++) {
									Weapon w = Player.weapons[i];
									w.cartridges = new ArrayList<Cartridge>();

									String[] weaponStats = weapons[i].split(",");

									int size = weaponStats.length - 4;

									w.weaponID = Integer.parseInt(weaponStats[0]);
									w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
									w.dualWield = Boolean.parseBoolean(weaponStats[2]);
									w.ammo = Integer.parseInt(weaponStats[3]);

									for (int j = 0; j < size; j++) {
										w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
									}
								}

								// Pop up messages are added immediately
								String[] messages = attributes[28].split("-");

								if (!messages[0].trim().equals("-1")) {
									for (String message : messages) {
										if (!message.trim().equals("1") && !message.trim().equals("")) {
											Display.messages.add(new PopUp(message));
										}
									}
								}

								// Audio stuff that should also be played immediately every tick
								String[] audioNames = attributes[29].split("-");
								String[] distances = attributes[30].split("-");

								if (!audioNames[0].trim().equals("-1") && !distances[0].trim().equals("-1")
										&& audioNames.length == distances.length) {
									for (int i = 0; i < audioNames.length; i++) {
										for (Sound currentSound : soundController.allSounds) {
											if (audioNames[i].trim().equals(currentSound.audioName.trim())) {
												currentSound.playAudioFile(Double.parseDouble(distances[i]));
											}
										}
									}
								}

							} else {
								ServerPlayer sP = new ServerPlayer();

								currentSection = sections[0];
								entitiesOfType = currentSection.split(";");

								sP.x = Double.parseDouble(attributes[1]);
								sP.y = Double.parseDouble(attributes[2]);
								sP.z = Double.parseDouble(attributes[3]);
								sP.ID = Integer.parseInt(attributes[4]);
								sP.health = Integer.parseInt(attributes[5]);
								sP.maxHealth = Integer.parseInt(attributes[6]);
								sP.armor = Integer.parseInt(attributes[7]);
								sP.environProtectionTime = Integer.parseInt(attributes[8]);
								sP.immortality = Integer.parseInt(attributes[9]);
								sP.vision = Integer.parseInt(attributes[10]);
								sP.invisibility = Integer.parseInt(attributes[11]);
								sP.height = Double.parseDouble(attributes[12]);
								sP.rotation = Double.parseDouble(attributes[13]);
								sP.xEffects = Double.parseDouble(attributes[14]);
								sP.yEffects = Double.parseDouble(attributes[15]);
								sP.zEffects = Double.parseDouble(attributes[16]);
								sP.alive = Boolean.parseBoolean(attributes[17]);
								sP.kills = Integer.parseInt(attributes[18]);
								sP.deaths = Integer.parseInt(attributes[19]);
								sP.forceCrouch = Boolean.parseBoolean(attributes[20]);
								sP.hasBlueKey = Boolean.parseBoolean(attributes[21]);
								sP.hasRedKey = Boolean.parseBoolean(attributes[22]);
								sP.hasGreenKey = Boolean.parseBoolean(attributes[23]);
								sP.hasYellowKey = Boolean.parseBoolean(attributes[24]);
								sP.resurrections = Integer.parseInt(attributes[25]);
								sP.weaponEquipped = Integer.parseInt(attributes[26]);

								String[] weapons = attributes[27].split("-");
								/*
								 * For each weapon, load in its attributes depending on what they were when the
								 * game was saved.
								 */
								for (int i = 0; i < weapons.length; i++) {
									Weapon w = sP.weapons[i];

									String[] weaponStats = weapons[i].split(",");

									int size = weaponStats.length - 4;

									w.weaponID = Integer.parseInt(weaponStats[0]);
									w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
									w.dualWield = Boolean.parseBoolean(weaponStats[2]);
									w.ammo = Integer.parseInt(weaponStats[3]);

									for (int j = 0; j < size; j++) {
										w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
									}
								}

								// Pop up messages are added immediately
								String[] messages = attributes[28].split("-");

								if (!messages[0].trim().equals("-1")) {
									for (String message : messages) {
										if (!message.trim().equals("1") && !message.trim().equals("")) {
											sP.clientMessages.add(new PopUp(message));
										}
									}
								}

								// Audio stuff that should also be played immediately every tick
								String[] audioNames = attributes[29].split("-");
								String[] distances = attributes[20].split("-");

								if (!audioNames[0].trim().equals("-1") && !distances[0].trim().equals("-1")
										&& audioNames.length == distances.length) {
									for (int i = 0; i < audioNames.length; i++) {
										sP.audioToPlay.add(audioNames[i]);
										sP.audioDistances.add(new Integer(distances[i]));
									}
								}
								Game.otherPlayers.add(sP);
							}

							// System.out.println("Client effects: " + Player.xEffects + " : " +
							// Player.yEffects + " : "
							// + Player.zEffects);

							currentSection = sections[1];
							entitiesOfType = currentSection.split(";");

							if (entitiesOfType.length > 1 && entitiesOfType[0].trim().equals("Items")) {

								for (int i = 1; i < entitiesOfType.length; i++) {
									attributes = entitiesOfType[i].split(":");

									int itemID = Integer.parseInt(attributes[0]);

									Item temp = null;

									/*
									 * If its not an explosive canister, add it as a normal item. Otherwise add it
									 * as an explosive canister
									 */
									temp = new Item(10, Double.parseDouble(attributes[1]),
											Double.parseDouble(attributes[2]), Double.parseDouble(attributes[3]),
											itemID, Integer.parseInt(attributes[4]), Integer.parseInt(attributes[5]),
											"");

									temp.pickedUp = Boolean.parseBoolean(attributes[6]);
									temp.phaseTime = Integer.parseInt(attributes[7]);

									// Game.items.add(temp);

									Block itemBlock = Level.getBlock((int) temp.x, (int) temp.z);

									// If the item gives the block a specific quality, or if the
									// item can not be removed from the block (if its solid)
									if (temp.isSolid || itemID == ItemNames.BREAKABLEWALL.getID()
											|| itemID == ItemNames.SECRET.getID()
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
									else if (itemID == ItemNames.ACTIVATEEXP.getID()
											|| itemID == ItemNames.ENEMYSPAWN.getID()
											|| itemID == ItemNames.WALLBEGONE.getID()) {
										Game.activatable.add(temp);
									} else if (itemID == ItemNames.TELEPORTEREXIT.getID()
											|| itemID == ItemNames.TELEPORTERENTER.getID()) {
										Game.teleporters.add(temp);

										itemBlock.wallEntities.add(temp);
									}
								}
							}

							currentSection = sections[2];
							entitiesOfType = currentSection.split(";");

							if (entitiesOfType.length > 1 && entitiesOfType[0].trim().equals("Bullets")) {
								for (int i = 1; i < entitiesOfType.length; i++) {
									Bullet b = null;

									String[] bAtt = entitiesOfType[i].split(":");

									b = new Bullet(10, 1, Double.parseDouble(bAtt[1]), Double.parseDouble(bAtt[2]),
											Double.parseDouble(bAtt[3]), Integer.parseInt(bAtt[0]), 0, false);

									Game.bullets.add(b);
								}
							}

							// Everything dealing with explosions
							currentSection = sections[3];
							entitiesOfType = currentSection.split(";");

							if (entitiesOfType.length > 1 && entitiesOfType[0].trim().equals("Explosions")) {
								for (int i = 1; i < entitiesOfType.length; i++) {
									Explosion exp = null;

									String[] expAtt = entitiesOfType[i].split(":");

									exp = new Explosion(Double.parseDouble(expAtt[2]), Double.parseDouble(expAtt[3]),
											Double.parseDouble(expAtt[4]), Integer.parseInt(expAtt[0]), 0);

									exp.phaseTime = Integer.parseInt(expAtt[1]);

									Game.explosions.add(exp);
								}
							}

						}
					}

					// Current time
					long currentTime = System.nanoTime();

					// How much time has passed between ticks
					long passedTime = currentTime - previousTime;

					// Reset previousTime to now
					previousTime = currentTime;

					/*
					 * I'm guessing the negligible amount of time these calculations take? To be
					 * honest I had to get this fps check code off the internet and had to try to
					 * understand it from there. It makes more sense now, but somethings are very
					 * hard to understand.
					 */
					unprocessedSeconds += (passedTime / 1000000000.0);

					/*
					 * Basically checks for the difference in seconds between the set value and
					 * calculated value. As long as unprocessedSeconds is more than secondsPerTick
					 * it will continue to loop through. Each loop it will tick, and
					 * unprocessedSeconds will keep going down. Every 60 loops, it will print out
					 * the number of frames that the game has, set frames back to 0, and add to
					 * previousTime. Why it does all this I am not 100% sure yet, but it works.
					 */
					while (unprocessedSeconds > secondsPerTick) {
						// See how many ticks occured within one second
						unprocessedSeconds -= secondsPerTick;

						// Add to tick count
						tickCount++;

						if (!smoothFPS) {
							tick();
						}

						// Every sixty ticks
						if (tickCount % 60 == 0) {
							// Set how many frames per second are rendered
							fps = frames;

							// Reset number of frames rendered in current second
							frames = 0;

							// Approximately how much time in nanoseconds it took
							// to run this loop I'm guessing
							previousTime += 1000;
						}
					}

					/*
					 * Focuses your user on the screen so you don't have to click to start. Only if
					 * not the host though.
					 */
					if (gameType != 0) {
						requestFocus();
					}

					EntityParent.checkSight = true;

					if (smoothFPS) {
						tick();
					}

					// TODO Before this, if in multiplayer and a client, accept data from server so
					// that the local game renders the correct data. Host doesn't render game.
					if (gameType != 0) {
						render();

						frames++;

						// TODO Change for debug mode
						// Only do this if the mouse is on
						// if (Display.mouseOn) {
						if (false) {
							// Get the JFrame on screen
							Component c = RunGame.frame;

							/*
							 * Check to see if mouse is in frame, and if it isn't then bring it back into
							 * it.
							 */
							if (!InputHandler.isMouseWithinComponent(c)) {
								// Controls mouse without player input
								Robot robot = null;

								try {
									robot = new Robot();
								} catch (Exception ex) {
									ex.printStackTrace();
								}

								/*
								 * Finds frames position on screen for mouse reposistioning
								 */
								Rectangle bounds = c.getBounds();
								bounds.setLocation(c.getLocationOnScreen());

								// Finds mousePos on screen
								Point mousePos = MouseInfo.getPointerInfo().getLocation();

								// If it went out of the right side, move mouse that
								// way, but only for how much it moved while in the
								// frame
								if (mousePos.x > bounds.x + bounds.width) {
									Display.mouseSpeedHorizontal = Math
											.abs(InputHandler.oldMouseX - (bounds.width / 2));

									// Move back to the center of the screen
									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								}
								/*
								 * Same as above but for if the mouse moves out of the left of the frame.
								 */
								else if (mousePos.x < bounds.x) {
									Display.mouseSpeedHorizontal = -Math
											.abs(InputHandler.oldMouseX - (bounds.width / 2));

									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								}
								/*
								 * If the x part of the mouse is still "in" the frame move it as much as it
								 * moved in the x, so just like normal basically.
								 */
								else {
									Display.mouseSpeedHorizontal = Math
											.abs(InputHandler.oldMouseX - InputHandler.MouseX);

									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								}

								/*
								 * Do all the same as the above just for the y direction. Such as if the mouse
								 * went out of the top of the frame.
								 */
								if (mousePos.y > bounds.y + bounds.height) {
									Display.mouseSpeedVertical = Math.abs(InputHandler.oldMouseY - (bounds.height / 2));

									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								} else if (mousePos.y < bounds.y) {
									Display.mouseSpeedVertical = -Math
											.abs(InputHandler.oldMouseY - (bounds.height / 2));

									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								} else {
									Display.mouseSpeedVertical = Math.abs(InputHandler.oldMouseY - InputHandler.MouseY);

									robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
								}
							}
						}
					}

					fromUser = Player.x + ":" + Player.y + ":" + Player.z + ":" + Player.rotation + ":" + Player.health
							+ ":" + Player.maxHealth + ":" + Player.armor + ":" + Player.immortality + ":"
							+ Player.godModeOn + ":" + Player.alive + ":" + Player.kills + ":" + Player.deaths + ":"
							+ Player.weaponEquipped + ":" + Player.hasRedKey + ":" + Player.hasGreenKey + ":"
							+ Player.hasBlueKey + ":" + Player.hasYellowKey + ":" + Player.xEffects + ":"
							+ Player.yEffects + ":" + Player.zEffects + ":";

					// Weapons this player has, and the ammo they contain

					for (int j = 0; j < Player.weapons.length; j++) {
						Weapon w = Player.weapons[j];
						int size = w.cartridges.size();

						fromUser += w.weaponID + "," + w.canBeEquipped + "," + w.dualWield + "," + w.ammo;

						for (int k = 0; k < size; k++) {
							int cartSize = w.cartridges.get(k).ammo;
							fromUser += "," + cartSize;
						}

						if (j < Player.weapons.length - 1) {
							fromUser += "-";
						}
					}

					fromUser += "?";
					fromUser += "Bullets;";

					for (Bullet b : Game.bulletsAdded) {
						fromUser += b.damage + ":" + b.speed + ":" + b.x + ":" + b.y + ":" + b.z + ":" + b.ID + ":"
								+ Player.rotation + ":" + b.upRotation + ";";
					}

					fromUser += "?";
					fromUser += "Players;";

					for (ServerPlayer sP : Game.otherPlayers) {
						fromUser += "Player:" + sP.x + ":" + sP.y + ":" + sP.z + ":" + sP.ID + ":" + sP.health + ":"
								+ sP.maxHealth + ":" + sP.armor + ":" + sP.environProtectionTime + ":" + sP.immortality
								+ ":" + sP.vision + ":" + sP.invisibility + ":" + sP.height + ":" + sP.rotation + ":"
								+ sP.xEffects + ":" + sP.yEffects + ":" + sP.zEffects + ":" + sP.alive + ":" + sP.kills
								+ ":" + sP.deaths + ":" + sP.forceCrouch + ":" + sP.hasBlueKey + ":" + sP.hasRedKey
								+ ":" + sP.hasGreenKey + ":" + sP.hasYellowKey + ":" + sP.resurrections + ":"
								+ sP.weaponEquipped + ":";

						// Weapons this player should have
						for (int j = 0; j < sP.weapons.length; j++) {
							Weapon w = sP.weapons[j];
							int size = w.cartridges.size();

							fromUser += w.weaponID + "," + w.canBeEquipped + "," + w.dualWield + "," + w.ammo;

							if (w.canBeEquipped) {
								// System.out.println(w.weaponID + ":" + w.dualWield);
							}

							for (int k = 0; k < size; k++) {
								int cartSize = w.cartridges.get(k).ammo;
								fromUser += "," + cartSize;
							}

							if (j < sP.weapons.length - 1) {
								fromUser += "-";
							}
						}

						fromUser += ":";

						// All messages that should have been displayed this tick for the client.
						for (int j = 0; j < sP.clientMessages.size(); j++) {
							PopUp p = sP.clientMessages.get(j);

							if (j == sP.clientMessages.size() - 2) {
								fromUser += p.getText();
							} else {
								fromUser += p.getText() + "-";
							}
						}

						if (sP.clientMessages.size() == 0) {
							fromUser += "-1";
						}

						fromUser += ":";
						sP.clientMessages = new ArrayList<PopUp>();

						// Any audio files that were supposed to be activated this tick for the client.
						for (int j = 0; j < sP.audioToPlay.size(); j++) {
							String s = sP.audioToPlay.get(j);

							if (j == sP.audioToPlay.size() - 2) {
								fromUser += s;
							} else {
								fromUser += s + "-";
							}
						}

						if (sP.audioToPlay.size() == 0) {
							fromUser += "-1";
						}

						fromUser += ":";
						sP.audioToPlay = new ArrayList<String>();

						// Any audio files that were supposed to be activated have a distance from the
						// player
						// that they were activated from to seem realistic.s
						for (int j = 0; j < sP.audioDistances.size(); j++) {
							int dist = sP.audioDistances.get(j).intValue();

							if (j == sP.audioDistances.size() - 2) {
								fromUser += dist;
							} else {
								fromUser += dist + "-";
							}
						}

						if (sP.audioDistances.size() == 0) {
							fromUser += "-1";
						}

						fromUser += ";";
						sP.audioDistances = new ArrayList<Integer>();
					}

					// If game is supposed to be quit, then call the stop method
					// to end all thread events of the game
					if (Controller.quitGame == true) {
						fromUser = "bye";
						out.println(fromUser);
						stop();
					}

					if (fromUser != null) {
						// System.out.println("Client: " + fromUser);
						out.println(fromUser);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else

		{
			requestFocus();

			while (isRunning) {
				// Current time
				long currentTime = System.nanoTime();

				// How much time has passed between ticks
				long passedTime = currentTime - previousTime;

				// Reset previousTime to now
				previousTime = currentTime;

				/*
				 * I'm guessing the negligible amount of time these calculations take? To be
				 * honest I had to get this fps check code off the internet and had to try to
				 * understand it from there. It makes more sense now, but somethings are very
				 * hard to understand.
				 */
				unprocessedSeconds += (passedTime / 1000000000.0);

				/*
				 * Basically checks for the difference in seconds between the set value and
				 * calculated value. As long as unprocessedSeconds is more than secondsPerTick
				 * it will continue to loop through. Each loop it will tick, and
				 * unprocessedSeconds will keep going down. Every 60 loops, it will print out
				 * the number of frames that the game has, set frames back to 0, and add to
				 * previousTime. Why it does all this I am not 100% sure yet, but it works.
				 */
				while (unprocessedSeconds > secondsPerTick) {
					// See how many ticks occured within one second
					unprocessedSeconds -= secondsPerTick;

					// Add to tick count
					tickCount++;

					if (!smoothFPS) {
						tick();
					}

					// Every sixty ticks
					if (tickCount % 60 == 0) {
						// Set how many frames per second are rendered
						fps = frames;

						// Reset number of frames rendered in current second
						frames = 0;

						// Approximately how much time in nanoseconds it took
						// to run this loop I'm guessing
						previousTime += 1000;
					}
				}

				/*
				 * Focuses your user on the screen so you don't have to click to start. Only if
				 * not the host though.
				 */
				if (gameType != 0) {
					requestFocus();
				}

				EntityParent.checkSight = true;

				if (smoothFPS) {
					tick();
				}

				// TODO Before this, if in multiplayer and a client, accept data from server so
				// that the local game renders the correct data. Host doesn't render game.
				if (gameType != 0) {
					render();

					frames++;

					// TODO Change for debug mode
					// Only do this if the mouse is on
					// if (Display.mouseOn) {
					if (false) {
						// Get the JFrame on screen
						Component c = RunGame.frame;

						/*
						 * Check to see if mouse is in frame, and if it isn't then bring it back into
						 * it.
						 */
						if (!InputHandler.isMouseWithinComponent(c)) {
							// Controls mouse without player input
							Robot robot = null;

							try {
								robot = new Robot();
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							/*
							 * Finds frames position on screen for mouse reposistioning
							 */
							Rectangle bounds = c.getBounds();
							bounds.setLocation(c.getLocationOnScreen());

							// Finds mousePos on screen
							Point mousePos = MouseInfo.getPointerInfo().getLocation();

							// If it went out of the right side, move mouse that
							// way, but only for how much it moved while in the
							// frame
							if (mousePos.x > bounds.x + bounds.width) {
								Display.mouseSpeedHorizontal = Math.abs(InputHandler.oldMouseX - (bounds.width / 2));

								// Move back to the center of the screen
								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							}
							/*
							 * Same as above but for if the mouse moves out of the left of the frame.
							 */
							else if (mousePos.x < bounds.x) {
								Display.mouseSpeedHorizontal = -Math.abs(InputHandler.oldMouseX - (bounds.width / 2));

								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							}
							/*
							 * If the x part of the mouse is still "in" the frame move it as much as it
							 * moved in the x, so just like normal basically.
							 */
							else {
								Display.mouseSpeedHorizontal = Math.abs(InputHandler.oldMouseX - InputHandler.MouseX);

								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							}

							/*
							 * Do all the same as the above just for the y direction. Such as if the mouse
							 * went out of the top of the frame.
							 */
							if (mousePos.y > bounds.y + bounds.height) {
								Display.mouseSpeedVertical = Math.abs(InputHandler.oldMouseY - (bounds.height / 2));

								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							} else if (mousePos.y < bounds.y) {
								Display.mouseSpeedVertical = -Math.abs(InputHandler.oldMouseY - (bounds.height / 2));

								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							} else {
								Display.mouseSpeedVertical = Math.abs(InputHandler.oldMouseY - InputHandler.MouseY);

								robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
							}
						}
					}
				}

				// If game is supposed to be quit, then call the stop method
				// to end all thread events of the game
				if (Controller.quitGame == true) {
					stop();
				}
			}
		}
	}

	/**
	 * Stop the program if it is still running. Otherwise just return, the programs
	 * already stopped.
	 */
	public synchronized void stop() {
		// If the game is already stopped, return
		if (isRunning == false) {
			return;
		}
		/*
		 * If still running, stop the music, set game to not running (isRunning = false)
		 * and create a new Launcher (aka Main menu)
		 */
		else {
			isRunning = false;

			// Start new launcher
			try {
				new FPSLauncher(0);
			} catch (Exception e) {
				// If that somehow has an error, end the program anyway
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Tick through all the game events. Update all game values and movements and
	 * such.
	 */
	public void tick() {
		game.tick(input.key);
	}

	/**
	 * Renders the screen after each new event resetting the graphics. Thats the
	 * simplest way to put it.
	 */
	public void render() {
		/*
		 * Strategy of how the image and pixels are buffered. How it organizes complex
		 * memory on a particular canvas or window.
		 * 
		 * Takes the integers and renders them using a graphics object that interprets
		 * the integers and displays them correctly on the screen as being a certain
		 * color and brightness.
		 */
		bs = this.getBufferStrategy();

		// If it has not been instantiated yet
		try {
			if (bs == null) {
				/*
				 * Creates the 3rd type of BufferStrategy. Method can be called because this
				 * class extends canvas. I'm guessing the 3 is because this program will be 3D.
				 * It doesn't seem to matter if I change it though.
				 */
				createBufferStrategy(3);
				return;
			}
		} catch (Exception e) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		/*
		 * Creates a graphics object based on what the buffered strategy is and then
		 * disposes of the graphics object after it is drawn to the screen. bs.show();
		 * shows it on the screen.
		 */
		Graphics g2 = bs.getDrawGraphics();
		Graphics2D g = (Graphics2D) g2;

		// Sets font of any text drawn on the screen

		/*
		 * Depending on how low the players health is, change the player move speed.
		 * TAKEN OUT FOR BETTER PLAYABILITY.
		 */
		Controller.moveSpeed = 1.0;

		if (clearedLevel) {
			g.drawImage(clearLevelBackground, 0, 0, WIDTH, HEIGHT, null);
			g.setColor(Color.RED);
			g.drawString("Cleared " + Game.mapName + "! Entering next level!", WIDTH / 2, 100);
			g.dispose();
			bs.show();
			return;
		}

		// Draws image with offsets, and given WIDTH and HEIGHT
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

		/*
		 * if (Player.health > 100) { Controller.moveSpeed = 1.5; } else if
		 * (Player.health > 60) { Controller.moveSpeed = 1.0; } else if (Player.health >
		 * 20) { Controller.moveSpeed = 0.75; } else { Controller.moveSpeed = 0.5; }
		 */

		// How much GUI is off from bottom of screen
		int gC = 100;

		// If Not full screen, its raised up more for the frame border
		if (FPSLauncher.resolutionChoice < 4) {
			gC = 128;
		}

		g.setFont(new Font("Nasalization", 1, 15));

		g.setColor(Color.RED);

		/*
		 * Display the weapon in front of the player and how it looks depending on what
		 * phase of firing the weapon is, and whether the player is dead or not
		 */
		try {
			// Image of weapon shown
			BufferedImage gun = gunNormal;

			// Coordinates weapon is rendered on screen
			int x = (WIDTH / 2) - 150;
			int y = HEIGHT - gC - 229;
			int height = 250;
			int width = 300;

			// Get the Weapon the player currently has Equipped
			Weapon playerWeapon = Player.weapons[Player.weaponEquipped];

			// Sword
			if (playerWeapon.weaponID == 0) {
				// Image is a little off, so this helps
				x -= 5;
				y = HEIGHT - gC - 224;
				swordTick++;

				if (playerWeapon.weaponPhase == 1) {
					x = WIDTH - 275;
					gun = swordAttack1;
				} else if (playerWeapon.weaponPhase == 2) {
					x = WIDTH - 400;
					height += 100;
					width += 100;
					y -= 100;
					gun = swordAttack2;
				} else if (playerWeapon.weaponPhase == 3) {
					height += 100;
					width += 100;
					y -= 100;
					gun = swordAttack3;
				} else if (playerWeapon.weaponPhase == 4) {
					height += 100;
					width += 100;
					y -= 100;
					gun = swordAttack4;
				} else if (playerWeapon.weaponPhase == 4) {
					height += 100;
					width += 100;
					y -= 100;
					gun = swordAttack5;
				} else {
					// Sword has a moving animation
					if (swordTick <= 10) {
						gun = sword1;
					} else if (swordTick <= 20) {
						gun = sword2;
					} else if (swordTick <= 30) {
						gun = sword3;
					}
				}

				if (swordTick > 29) {
					swordTick = 0;
				}
			}
			// Pistol
			else if (playerWeapon.weaponID == 1) {
				x = (WIDTH / 2);

				if (playerWeapon.weaponPhase == 1) {
					gun = pistolRight2;
				} else if (playerWeapon.weaponPhase == 2) {
					gun = pistolRight3;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = pistolRight4;
				} else if (playerWeapon.weaponPhase == 4) {
					gun = pistolRight5;
				} else {
					gun = pistolRight1;
				}

				if (playerWeapon.reloading > 0) {
					if (playerWeapon.reloading >= 20) {
						gun = pistolRight3;
					} else if (playerWeapon.reloading >= 10) {
						gun = pistolRight4;
					} else if (playerWeapon.reloading > 0) {
						gun = pistolRight5;
					}
				}

				// If dual wielding show second pistol
				if (playerWeapon.dualWield == true) {
					BufferedImage gun2 = pistolLeft1;

					if (playerWeapon.weaponPhase2 == 1) {
						gun2 = pistolLeft2;
					} else if (playerWeapon.weaponPhase2 == 2) {
						gun2 = pistolLeft3;
					} else if (playerWeapon.weaponPhase2 == 3) {
						gun2 = pistolLeft4;
					} else if (playerWeapon.weaponPhase2 == 4) {
						gun2 = pistolLeft5;
					} else {
						gun2 = pistolLeft1;
					}

					if (playerWeapon.reloading > 0) {
						if (playerWeapon.reloading >= 20) {
							gun2 = pistolLeft3;
						} else if (playerWeapon.reloading >= 10) {
							gun2 = pistolLeft4;
						} else if (playerWeapon.reloading > 0) {
							gun2 = pistolLeft5;
						}
					}

					// Only draw gun if player is alive
					if (Player.alive) {
						double moveValue = (Player.movementTick / 180.0) * Math.PI;

						if (Player.movementTick > 0) {
							g.drawImage(gun2, (int) ((WIDTH / 2) - 300 + (Math.sin(moveValue) * 30)),
									(int) (y - ((Math.sin(moveValue) * 30))), 300, 250, null);
						} else {
							g.drawImage(gun2, (int) ((WIDTH / 2) - 300 + (Math.sin(moveValue) * 30)),
									(int) (y + ((Math.sin(moveValue) * 30))), 300, 250, null);
						}
					}
				}
			}
			// Shotgun
			else if (playerWeapon.weaponID == 2) {
				// Image is a little off, so this helps
				x -= 5;

				if (playerWeapon.weaponPhase == 1) {
					gun = gunShot;
				} else if (playerWeapon.weaponPhase == 2) {
					gun = gunShot2;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = gunShot3;
				} else if (playerWeapon.weaponPhase == 4) {
					gun = gunShot4;
				} else {
					gun = gunNormal;
				}

				// If the weapon is reloading, draw different graphics
				if (playerWeapon.reloading > 0) {
					if (playerWeapon.reloading >= 20) {
						gun = gunShot3;
					} else if (playerWeapon.reloading >= 10) {
						gun = gunShot4;
					} else if (playerWeapon.reloading > 0) {
						gun = gunShot3;
					}
				}
			}
			// Phase Cannon
			else if (playerWeapon.weaponID == 3) {
				if (playerWeapon.weaponPhase == 1) {
					gun = phaseCannon2;
				} else if (playerWeapon.weaponPhase == 2) {
					gun = phaseCannon3;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = phaseCannon4;
				} else {
					gun = phaseCannon1;
				}

				// If the weapon is reloading, draw different graphics
				if (playerWeapon.reloading > 0) {
					if (playerWeapon.reloading >= 20) {
						gun = phaseCannon3;
					} else if (playerWeapon.reloading >= 10) {
						gun = phaseCannon2;
					} else if (playerWeapon.reloading > 0) {
						gun = phaseCannon1;
					}
				}
			}
			// Rocket Launcher
			else if (playerWeapon.weaponID == 4) {
				if (playerWeapon.weaponPhase == 1) {
					gun = rocketLauncher1;
				} else if (playerWeapon.weaponPhase == 2) {
					gun = rocketLauncher2;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = rocketLauncher3;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = rocketLauncher4;
				} else {
					gun = rocketLauncher;
				}

				// If the weapon is reloading, draw different graphics
				if (playerWeapon.reloading > 0) {
					if (playerWeapon.reloading >= 20) {
						gun = rocketLauncher;
					} else if (playerWeapon.reloading >= 10) {
						gun = rocketLauncher4;
					} else if (playerWeapon.reloading > 0) {
						gun = rocketLauncher;
					}
				}
			}
			// Scepter of Deciet
			else if (playerWeapon.weaponID == 5) {
				if (playerWeapon.weaponPhase == 1) {
					gun = decietScepter1;
				} else if (playerWeapon.weaponPhase == 2) {
					gun = decietScepter2;
				} else if (playerWeapon.weaponPhase == 3) {
					gun = decietScepter3;
				} else if (playerWeapon.weaponPhase == 4) {
					gun = decietScepter4;
				} else {
					gun = decietScepter;
				}

				// If the weapon is reloading, draw different graphics
				if (playerWeapon.reloading > 0) {
					if (playerWeapon.reloading >= 20) {
						gun = decietScepter1;
					} else if (playerWeapon.reloading >= 10) {
						gun = decietScepter4;
					} else if (playerWeapon.reloading > 0) {
						gun = decietScepter;
					}
				}
			}

			// Weapons is only drawn if player is alive.
			if (Player.alive) {
				double moveValue = (Player.movementTick / 180.0) * Math.PI;

				if (Player.movementTick > 0) {
					g.drawImage(gun, (int) (x + (Math.sin(moveValue) * 30)), (int) (y - ((Math.sin(moveValue) * 30))),
							width, height, null);
				} else {
					g.drawImage(gun, (int) (x + (Math.sin(moveValue) * 30)), (int) (y + ((Math.sin(moveValue) * 30))),
							width, height, null);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// TODO
		// Only do if the console is open
		if (Display.consoleOpen) {
			g.setColor(Color.GRAY);

			g.fill3DRect(0, 0, 500, 500, true);

			g.setColor(Color.WHITE);
			g.setFont(new Font("Lucida Console", 1, 15));

			g.drawString("Command Console:", 10, 20);

			g.setFont(new Font("Lucida Console", 1, 10));
			// Shows if fly mode is on
			if (Player.flyOn) {
				g.drawString("FlyMode: On", 10, 40);
			} else {
				g.drawString("FlyMode: Off", 10, 40);
			}

			// Shows up if noClip is on
			if (Player.noClipOn) {
				g.drawString("NoClip: On", 10, 55);
			} else {
				g.drawString("NoClip: Off", 10, 55);
			}

			g.drawString("Speed Multiplier: " + Player.speedMultiplier, 10, 70);

			// If GodMode is activated
			if (Player.godModeOn) {
				g.drawString("God Mode: On", 10, 85);
			} else {
				g.drawString("God Mode: Off", 10, 85);
			}

			// If Unlimited ammo is activated
			if (Player.unlimitedAmmoOn) {
				g.drawString("Infinite Ammo: On", 10, 100);
			} else {
				g.drawString("Infinite Ammo: Off", 10, 100);
			}

			// Display map name in console now
			if (FPSLauncher.gameMode == 1) {
				g.drawString("Survival Map", 10, 115);
			} else {
				g.drawString(Game.mapName, 10, 115);
			}

			g.setFont(new Font("Lucida Console", 1, 12));

			g.setColor(Color.WHITE);

			g.drawString(consoleMessage, 10, 420);
			g.drawString(consoleOutput.toLowerCase() + "_", 10, 450);
		} else {
			consoleOutput = "";
		}

		g.setFont(new Font("Nasalization", 1, 15));

		g.setColor(Color.RED);

		/*
		 * Tries to load up the GUI image for the HUD. If it can't, it will just pass
		 * through this and render the text only.
		 * 
		 * The GUI is the weapon picture, player face, and gray box outline.
		 */
		g.drawImage(HUD, 0, HEIGHT - gC, WIDTH, 100, null);
		// TODO temp

		Weapon playerWeapon = Player.weapons[Player.weaponEquipped];

		/*
		 * Tries to load the face corresponding to the players health and phase of
		 * looking, but if it can't load it, it will just not load and go on.
		 */
		try {
			BufferedImage face = healthyFace1;

			int phase = facePhase / 50;

			// If player is healthy
			if (Player.health > 75) {
				if (phase == 0 || phase == 2) {
					face = healthyFace1;
				} else if (phase == 1) {
					face = healthyFace2;
				} else {
					face = healthyFace3;
				}

				// If player was just harmed have this face.
				if (Player.playerHurt > 0) {
					face = playerHarmedHealthy;
				}
			} else if (Player.health > 50) {
				if (phase == 0 || phase == 2) {
					face = hurtFace1;
				} else if (phase == 1) {
					face = hurtFace2;
				} else {
					face = hurtFace3;
				}

				// If player was just harmed have this face.
				if (Player.playerHurt > 0) {
					face = playerHarmedHurt;
				}
			} else if (Player.health > 25) {
				if (phase == 0 || phase == 2) {
					face = veryHurtFace1;
				} else if (phase == 1) {
					face = veryHurtFace2;
				} else {
					face = veryHurtFace3;
				}

				// If player was just harmed have this face.
				if (Player.playerHurt > 0) {
					face = playerHarmedVeryHurt;
				}
			} else if (Player.health > 0) {
				if (phase == 0 || phase == 2) {
					face = almostDead1;
				} else if (phase == 1) {
					face = almostDead2;
				} else {
					face = almostDead3;
				}

				// If player was just harmed have this face.
				if (Player.playerHurt > 0) {
					face = playerHarmedAlmostDead;
				}
			} else {
				face = dead;
			}

			// As long as Player is alive check for these
			if (Player.alive) {
				// If player is both invisible and invincible
				if (Player.invisibility > 0 && (Player.godModeOn || Player.immortality > 0)) {
					face = invisGodmode;
				}
				// If player is invisible show the invisible face
				else if (Player.invisibility > 100 * Render3D.fpsCheck
						|| (Player.invisibility > 0 && Player.invisibility % 5 == 0)) {
					face = invisible;
				}
				/*
				 * If only in god mode, override all the faces and make this the face displayed.
				 */
				else if ((Player.godModeOn || Player.immortality > 0)) {
					face = godMode;
				}
			}

			// If player is frozen
			if (Player.frozen > 0) {
				face = frozenFace;
			}

			// Update facePhase (direction face looks etc...)
			facePhase++;

			// After reaching 200 ticks, reset the facePhase to 0
			if (facePhase >= 200) {
				facePhase = 0;
			}

			// Draw face image in GUI
			g.drawImage(face, (WIDTH / 2) - 50, HEIGHT - gC, 100, 100, null);
		} catch (Exception e) {
		}

		/*
		 * Try to render the keys now depending on whether the player has them or not.
		 */
		if (Player.hasRedKey) {
			g.drawImage(redKey, (WIDTH / 2) + (WIDTH / 12), HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasBlueKey) {
			g.drawImage(blueKey, (WIDTH / 2) + (WIDTH / 12) + 20, HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasGreenKey) {
			g.drawImage(greenKey, (WIDTH / 2) + (WIDTH / 12) + 40, HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasYellowKey) {
			g.drawImage(yellowKey, (WIDTH / 2) + (WIDTH / 12) + 60, HEIGHT - gC + 23, 10, 20, null);
		}

		// For the weapon Upgrade points
		// TODO make look correct
		if (hudMovementTick <= 8) {
			g.drawImage(weaponUpgradePoint1, (WIDTH / 2) + (WIDTH / 12) - 10, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 16) {
			g.drawImage(weaponUpgradePoint2, (WIDTH / 2) + (WIDTH / 12) - 10, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 24) {
			g.drawImage(weaponUpgradePoint3, (WIDTH / 2) + (WIDTH / 12) - 10, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 32) {
			g.drawImage(weaponUpgradePoint4, (WIDTH / 2) + (WIDTH / 12) - 10, HEIGHT - gC + 45, 50, 50, null);
		} else {
			g.drawImage(weaponUpgradePoint1, (WIDTH / 2) + (WIDTH / 12) - 10, HEIGHT - gC + 45, 50, 50, null);
			hudMovementTick = 0;
		}

		g.drawString(" " + Player.upgradePoints, (WIDTH / 2) + (WIDTH / 12) + 30, HEIGHT - gC + 85);

		// If the weapon can be upgraded.
		if (Player.weapons[Player.weaponEquipped].upgradePointsNeeded != -1) {
			g.drawString("Upgrade weapon (" + Player.weapons[Player.weaponEquipped].upgradePointsNeeded + " points)",
					WIDTH - 250, HEIGHT - gC + 65);
		}

		g.drawString("Current weapon damage: " + Player.weapons[Player.weaponEquipped].damage + "hp", WIDTH - 250,
				HEIGHT - gC + 85);

		hudMovementTick++;

		// Shows the FPS on the screen if it is activated to show
		if (Controller.showFPS) {
			g.drawString("FPS: " + fps, 20, 17);
		}

		/*
		 * If the player is not dead, show the number of cartridges of ammo the player
		 * has, the amount of ammo the player currently has loaded, and how much is in
		 * the current cartridge. Also show the players health, armor, and keycards.
		 */
		if (Player.alive) {
			if (smileMode) {
				if (!Display.consoleOpen) {
					int rValue = 0;
					int gValue = 0;
					int bValue = 0;

					if (Player.health > 100) {
						bValue = 255;
						rValue = 255;
					} else if (Player.health > 80) {
						bValue = 128;
						rValue = 255;
					} else if (Player.health > 60) {
						rValue = 128;
						bValue = 64;
					} else if (Player.health > 20) {
						rValue = 64;
						bValue = 64;
					} else {
						bValue = 128;
					}

					g.setColor(new Color(rValue, gValue, bValue, 96));
					g.fillRect(10, 30, Player.maxHealth, 25);

					g.setColor(new Color(rValue, gValue, bValue, 192));
					g.fillRect(11, 31, Player.health, 24);

					g.setColor(Color.RED);

					g.drawString("Happiness: " + Player.health, 11 + (Player.maxHealth / 4), 48);

					rValue = 0;
					gValue = 0;
					bValue = 0;

					if (Player.armor > 100) {
						bValue = 255;
						rValue = 255;
					} else if (Player.armor > 80) {
						bValue = 128;
						rValue = 255;
					} else if (Player.armor > 60) {
						rValue = 128;
						bValue = 64;
					} else if (Player.armor > 20) {
						rValue = 64;
						bValue = 64;
					} else {
						bValue = 128;
					}

					g.setColor(new Color(rValue, gValue, bValue, 96));
					g.fillRect(10, 60, 200, 25);

					g.setColor(new Color(rValue, gValue, bValue, 192));
					g.fillRect(11, 61, Player.armor, 24);

					g.setColor(Color.RED);

					g.drawString("Positivity: " + Player.armor, 11 + (200 / 4), 78);
				}

				if (playerWeapon.weaponID == 0) {
					g.drawString("Sword", 20, HEIGHT - gC + 43);
				} else if (playerWeapon.weaponID == 1) {
					g.drawString("Arrow Quivers: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Arrows: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 2) {
					g.drawString("Joy Cartridges: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Joy: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 3) {
					g.drawString("Peace Cells: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Peace Charges: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 4) {
					g.drawString("Teddy Bear Casings: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Teddy Bears: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 5) {
					g.drawString("Happiness Cores: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Happy Blasts: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				}

				/*
				 * If there are cartridges, show the ammo of the one being currently used, if
				 * not just display that there is 0 available.
				 */
				if (playerWeapon.cartridges.size() != 0) {
					if (playerWeapon.weaponID == 1) {
						g.drawString("Arrows in Quiver: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Joy in Casing: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Peace in Cell: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Teddy Bears in Casing: " + playerWeapon.cartridges.get(0).ammo, 20,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 5) {
						g.drawString("Happy Blasts in Core: " + playerWeapon.cartridges.get(0).ammo, 20,
								HEIGHT - gC + 18);
					}
				} else {
					if (playerWeapon.weaponID == 1) {
						g.drawString("Arrows in Quiver: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Joy in Casing: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Peace in Cell: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Teddy Bears in Casing: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 5) {
						g.drawString("Happy Blasts in Core: 0", 20, HEIGHT - gC + 18);
					}
				}
			} else {
				if (!Display.consoleOpen) {
					int rValue = 0;
					int gValue = 0;
					int bValue = 0;

					if (Player.health > 100) {
						bValue = 255;
						gValue = 255;
					} else if (Player.health > 80) {
						gValue = 255;
					} else if (Player.health > 60) {
						rValue = 255;
						gValue = 255;
					} else if (Player.health > 20) {
						rValue = 255;
						gValue = 128;
					} else {
						rValue = 255;
					}

					g.setColor(new Color(rValue, gValue, bValue, 96));
					g.fillRect(10, 30, Player.maxHealth, 25);

					g.setColor(new Color(rValue, gValue, bValue, 192));
					g.fillRect(11, 31, Player.health, 24);

					g.setColor(Color.RED);

					g.drawString("Health: " + Player.health, 11 + (Player.maxHealth / 3), 48);

					rValue = 0;
					gValue = 0;
					bValue = 0;

					if (Player.armor > 100) {
						bValue = 255;
						gValue = 255;
					} else if (Player.armor > 80) {
						gValue = 255;
					} else if (Player.armor > 60) {
						rValue = 255;
						gValue = 255;
					} else if (Player.armor > 20) {
						rValue = 255;
						gValue = 128;
					} else {
						rValue = 255;
					}

					g.setColor(new Color(rValue, gValue, bValue, 96));
					g.fillRect(10, 60, 200, 25);

					g.setColor(new Color(rValue, gValue, bValue, 192));
					g.fillRect(11, 61, Player.armor, 24);

					g.setColor(Color.RED);

					g.drawString("Armor: " + Player.armor, 11 + (200 / 3), 78);

					ArrayList<String> effects = new ArrayList<String>();

					if (Player.environProtectionTime > 0) {
						effects.add("protection");
					}

					if (Player.immortality > 0) {
						effects.add("immortality");
					}

					if (Player.vision > 0) {
						effects.add("vision");
					}

					if (Player.invisibility > 0) {
						effects.add("invisibility");
					}

					if (Player.frozen > 0) {
						effects.add("frozen");
					}

					if (Player.drunkLevels > 0) {
						effects.add("drunk");
					}

					int yStart = 90;

					for (int i = 0; i < effects.size(); i++) {
						String currentEffect = effects.get(i);

						if (currentEffect.equals("protection")) {
							int barToShow = 200;
							double percentage = Player.environProtectionTime / 1000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, Color.GREEN, 200, 100, Color.BLACK);
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(protectionIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						} else if (currentEffect.equals("immortality")) {
							int barToShow = 200;
							double percentage = Player.immortality / 1000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, Color.WHITE, 200, 100, Color.YELLOW);
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(immortalityIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						} else if (currentEffect.equals("vision")) {
							int barToShow = 200;
							double percentage = Player.vision / 1000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, Color.RED, 200, 100, Color.BLUE);
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(visionIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						} else if (currentEffect.equals("invisibility")) {
							int barToShow = 200;
							double percentage = Player.invisibility / 1000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, Color.MAGENTA, 200, 100, Color.BLACK);
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(invisibilityIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						} else if (currentEffect.equals("frozen")) {
							int barToShow = 200;
							double percentage = Player.frozen / 1000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, Color.CYAN, 200, 100, Color.BLUE);
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(frozenIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						} else if (currentEffect.equals("drunk")) {
							int barToShow = 200;
							double percentage = Player.drunkLevels / 5000.0;

							if (percentage > 1) {
								barToShow = 200;
							} else {
								barToShow = (int) (200.0 * percentage);
							}

							GradientPaint gradient = new GradientPaint(10, 90, new Color(74, 37, 0), 200, 100,
									new Color(0, 150, 0));
							g.setPaint(gradient);
							g.fillRect(10, yStart, barToShow, 12);
							g.drawImage(drunkIcon, 210, yStart, 12, 12, null);
							yStart += 17;
						}
					}

					g.setColor(Color.RED);
				}

				if (playerWeapon.weaponID == 0) {
					g.drawString("Sword", 20, HEIGHT - gC + 43);
				} else if (playerWeapon.weaponID == 1) {
					g.drawString("Bullet Cartridges: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Bullets: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 2) {
					g.drawString("Shell Cartridges: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Shells: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 3) {
					g.drawString("Phase Cells: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Phase Charges: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 4) {
					g.drawString("Rocket Casings: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Rockets: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 5) {
					g.drawString("Scepter Cores: " + playerWeapon.cartridges.size(), 20, HEIGHT - gC + 43);

					g.drawString("Scepter Blasts: " + playerWeapon.ammo, 20, HEIGHT - gC + 68);
				}

				/*
				 * If there are cartridges, show the ammo of the one being currently used, if
				 * not just display that there is 0 available.
				 */
				if (playerWeapon.cartridges.size() != 0) {
					if (playerWeapon.weaponID == 1) {
						g.drawString("Bullets in Cartridge: " + playerWeapon.cartridges.get(0).ammo, 20,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Shells in Casing: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Charges in Cell: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Rockets in Casing: " + playerWeapon.cartridges.get(0).ammo, 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 5) {
						g.drawString("Power Blasts in Core: " + playerWeapon.cartridges.get(0).ammo, 20,
								HEIGHT - gC + 18);
					}
				} else {
					if (playerWeapon.weaponID == 1) {
						g.drawString("Bullets in Cartridge: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Shells in Casing: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Charges in Cell: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Rockets in Casing: 0", 20, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 5) {
						g.drawString("Power Blasts in Core: 0", 20, HEIGHT - gC + 18);
					}
				}
			}
		} else {
			if (smileMode) {
				g.drawString("GOT DEPRESSED...", (WIDTH / 2) - 220, HEIGHT - gC + 18);

				g.drawString("No Positivity", (WIDTH / 2) - 190, HEIGHT - gC + 43);
			} else {
				g.drawString("PLAYER DIED...", (WIDTH / 2) - 220, HEIGHT - gC + 18);

				g.drawString("No Armor", (WIDTH / 2) - 190, HEIGHT - gC + 43);
			}

			Controller.moveSpeed = 0.0;
			Player.y = -6;

			// Creates new popup displaying how to restart the level
			PopUp restartLevel = new PopUp("Press E to restart level", -1, 0);

			// If this stays true, it will add this popup to the screen.
			// This keeps the popup from begin repeated
			boolean addThis = true;

			// Check to see if any popups already contain this message
			// If so then set addThis to false.
			for (PopUp p : messages) {
				if (p.getText() == restartLevel.getText()) {
					addThis = false;
					break;
				}
			}

			// Add to popups if not yet added.
			if (addThis) {
				messages.add(restartLevel);
			}
		}

		/*
		 * In survival show the number of enemies killed that the player has total.It
		 * will never reset like in the actual game.
		 * 
		 * Otherwise show secrets found, and the actual map name.
		 */
		if (FPSLauncher.gameMode == 1) {
			if (smileMode) {
				g.drawString("Unhappy Faces: " + Game.entities.size(), WIDTH - 250, HEIGHT - gC + 43);

				g.drawString("Days Made: " + Player.kills, WIDTH - 250, HEIGHT - gC + 18);
			} else {
				g.drawString("Enemies: " + Game.entities.size(), WIDTH - 250, HEIGHT - gC + 43);

				g.drawString("Kills: " + Player.kills, WIDTH - 250, HEIGHT - gC + 18);
			}
		} else {
			g.drawString("Secrets found: " + Game.secretsFound + " / " + Game.secretsInMap, WIDTH - 250,
					HEIGHT - gC + 18);

			if (smileMode) {
				g.drawString("Made Happy: " + Player.kills + " / " + Game.enemiesInMap, WIDTH - 250, HEIGHT - gC + 43);
			} else {
				g.drawString("Kills: " + Player.kills + " / " + Game.enemiesInMap, WIDTH - 250, HEIGHT - gC + 43);
			}

			// Only show deaths if in multiplayer deathmatch mode
			if (gameType == 1) {
				if (smileMode) {
					g.drawString("Got sad: " + Player.deaths, WIDTH - 250, HEIGHT - gC + 68);
				} else {
					g.drawString("Deaths: " + Player.deaths, WIDTH - 250, HEIGHT - gC + 68);
				}
			}
		}

		// The last popup's y value so it can be checked to make
		// sure popups don't bunch up too much
		int lastY = 0;

		// For all the popups the game is handling
		for (int i = 0; i < messages.size(); i++) {
			PopUp p = messages.get(i);

			/*
			 * Popups cannot be less than 25 pixels away from each other. This check ensures
			 * that they are spaced out enough when being presented on the screen. This
			 * cannot be the first popup in the array list either because then it will not
			 * be checking for the last popup displayed.
			 */
			if ((lastY - p.getYOnScreen()) < 25 && i > 0) {
				PopUp p2 = messages.get(i - 1);
				p2.setYOnScreen(p.getYOnScreen() + 25);
			}

			// Draw pop up on screen
			g.drawString(p.getText(), (WIDTH / 2) + 75, p.getYOnScreen());

			// Save the y value
			lastY = p.getYOnScreen();

			// Tick the popups values.
			p.tick();
		}

		/*
		 * Draw other needed texts
		 */
		if (FPSLauncher.gameMode == 0) {
			g.drawString("Keys:", (WIDTH / 2) + (WIDTH / 12), HEIGHT - gC + 18);
		} else {
			g.drawString("High Score:", (WIDTH / 2) + (WIDTH / 12), HEIGHT - gC + 18);
			g.drawString("" + Player.maxKills, (WIDTH / 2) + (WIDTH / 12), HEIGHT - gC + 36);
		}
		// TODO fix spacing

		/*
		 * Disposes of this graphics object, and show what it was on the screen
		 */
		g.dispose();
		bs.show();
	}

	// Will start the launcher if Display is run instead of start game
	// for some reason
	public static void main(String[] args) {
		// Start the launcher
		new LogIn();
	}

	/**
	 * Plays the music audio. Because this audio is slightly different than other
	 * sounds because of the fact it is set to loop continuously, this is not
	 * handled by the sound controller.
	 */
	public synchronized void playAudio(String custom) {
		// Host does not play music.
		if (gameType == 0) {
			return;
		}

		try {
			// If no music is already playing
			if (!audioOn) {
				// File path to extract music from
				String file = "resources" + FPSLauncher.themeName + "/audio/";

				/*
				 * If the actual maps are being played instead of the randomly generated
				 * survival maps.
				 */
				if (FPSLauncher.gameMode == 1) {
					if (musicTheme == 1) {
						file += ("gameAudio2.wav");
					} else if (musicTheme == 2) {
						file += ("e1m3.wav");
					} else {
						file += ("gameAudio.wav");
					}
				}
				// Custom music themes
				else {
					file += (custom + ".wav");
				}

				music = AudioSystem.getClip();
				inputStream = AudioSystem.getAudioInputStream(new File(file));
				music.open(inputStream);
				music.loop(Clip.LOOP_CONTINUOUSLY);
				music.start();

				// Audio is on
				audioOn = true;

				FPSLauncher.musicVolumeControl = (FloatControl) Display.music.getControl(FloatControl.Type.MASTER_GAIN);

				// Reset clip volume
				Sound.setMusicVolume(music);
			}
		} catch (Exception e) {
			try {
				if (!audioOn) {
					String file = "resources/default/audio/";

					/*
					 * If the actual maps are being played instead of the randomly generated
					 * survival maps.
					 */
					if (FPSLauncher.gameMode == 1) {
						if (musicTheme == 1) {
							file += ("gameAudio2.wav");
						} else if (musicTheme == 2) {
							file += ("e1m3.wav");
						} else {
							file += ("gameAudio.wav");
						}
					}
					// Custom music themes
					else {
						file += (custom + ".wav");
					}

					music = AudioSystem.getClip();
					AudioInputStream input = AudioSystem.getAudioInputStream(new File(file));
					music.open(input);
					music.loop(Clip.LOOP_CONTINUOUSLY);
					music.start();

					audioOn = true;

					// Audio is on
					audioOn = true;

					FPSLauncher.musicVolumeControl = (FloatControl) Display.music
							.getControl(FloatControl.Type.MASTER_GAIN);

					// Reset clip volume
					Sound.setMusicVolume(music);
				}
			} catch (Exception ex) {
				// Music could not be loaded
			}
		}
	}

	/**
	 * Reset the music by first closing the current music stream and then setting
	 * audio to being off. Then re-call playAudio to start a new custom music file.
	 * 
	 * @param custom
	 */
	public void resetMusic(String customMusic) {
		try {
			inputStream.close();
		} catch (Exception e) {

		}

		inputStream = null;

		try {
			music.close();
		} catch (Exception e) {

		}

		music = null;

		audioOn = false;

		playAudio(customMusic);
	}

	/**
	 * Loads all the images up for the HUD, and if one fails, it throws an
	 * exception.
	 * 
	 * @throws Exception
	 */
	public void loadHUD() throws Exception {
		// Just reads the image from a file as a BufferedImage
		try {
			yellowKey = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/yellowKey.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			try {
				yellowKey = ImageIO.read(new File("resources/default/textures/hud/yellowKey.png"));
			} catch (Exception ex) {

			}
		}

		try {
			greenKey = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/greenKey.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			try {
				greenKey = ImageIO.read(new File("resources/default/textures/hud/greenKey.png"));
			} catch (Exception ex) {

			}
		}

		try {
			redKey = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/redKey.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			try {
				redKey = ImageIO.read(new File("resources/default/textures/hud/redKey.png"));
			} catch (Exception ex) {

			}
		}

		try {
			blueKey = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/blueKey.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			blueKey = ImageIO.read(new File("resources/default/textures/hud/blueKey.png"));
		}

		try {
			clearLevelBackground = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/clearLevel.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			clearLevelBackground = ImageIO.read(new File("resources/default/textures/hud/clearLevel.png"));
		}

		try {
			decietScepter = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/decietScepter.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			decietScepter = ImageIO.read(new File("resources/default/textures/hud/decietScepter.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < decietScepter.getWidth(); ++x) {
			for (int y = 0; y < decietScepter.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((decietScepter.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					decietScepter.setRGB(x, y, 0);
				}
			}
		}

		try {
			decietScepter1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/decietScepter1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			decietScepter1 = ImageIO.read(new File("resources/default/textures/hud/decietScepter1.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < decietScepter1.getWidth(); ++x) {
			for (int y = 0; y < decietScepter1.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((decietScepter1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					decietScepter1.setRGB(x, y, 0);
				}
			}
		}

		try {
			decietScepter2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/decietScepter2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			decietScepter2 = ImageIO.read(new File("resources/default/textures/hud/decietScepter2.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < decietScepter2.getWidth(); ++x) {
			for (int y = 0; y < decietScepter2.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((decietScepter2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					decietScepter2.setRGB(x, y, 0);
				}
			}
		}

		try {
			decietScepter3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/decietScepter3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			decietScepter3 = ImageIO.read(new File("resources/default/textures/hud/decietScepter3.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < decietScepter3.getWidth(); ++x) {
			for (int y = 0; y < decietScepter3.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((decietScepter3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					decietScepter3.setRGB(x, y, 0);
				}
			}
		}

		try {
			decietScepter4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/decietScepter4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			decietScepter4 = ImageIO.read(new File("resources/default/textures/hud/decietScepter4.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < decietScepter4.getWidth(); ++x) {
			for (int y = 0; y < decietScepter4.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((decietScepter4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					decietScepter4.setRGB(x, y, 0);
				}
			}
		}

		try {
			weaponUpgradePoint1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			weaponUpgradePoint1 = ImageIO.read(new File("resources/default/textures/hud/weaponUpgrade1.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < weaponUpgradePoint1.getWidth(); ++x) {
			for (int y = 0; y < weaponUpgradePoint1.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((weaponUpgradePoint1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					weaponUpgradePoint1.setRGB(x, y, 0);
				}
			}
		}

		try {
			weaponUpgradePoint2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			weaponUpgradePoint2 = ImageIO.read(new File("resources/default/textures/hud/weaponUpgrade2.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < weaponUpgradePoint2.getWidth(); ++x) {
			for (int y = 0; y < weaponUpgradePoint2.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((weaponUpgradePoint2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					weaponUpgradePoint2.setRGB(x, y, 0);
				}
			}
		}

		try {
			weaponUpgradePoint3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			weaponUpgradePoint3 = ImageIO.read(new File("resources/default/textures/hud/weaponUpgrade3.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < weaponUpgradePoint3.getWidth(); ++x) {
			for (int y = 0; y < weaponUpgradePoint3.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((weaponUpgradePoint3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					weaponUpgradePoint3.setRGB(x, y, 0);
				}
			}
		}

		try {
			weaponUpgradePoint4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			weaponUpgradePoint4 = ImageIO.read(new File("resources/default/textures/hud/weaponUpgrade4.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < weaponUpgradePoint4.getWidth(); ++x) {
			for (int y = 0; y < weaponUpgradePoint4.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((weaponUpgradePoint4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					weaponUpgradePoint4.setRGB(x, y, 0);
				}
			}
		}

		try {
			frozenFace = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/frozen.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			frozenFace = ImageIO.read(new File("resources/default/textures/hud/frozen.png"));
		}

		try {
			frozenFace = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/frozen.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			frozenFace = ImageIO.read(new File("resources/default/textures/hud/frozen.png"));
		}

		try {
			HUD = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/userGUI.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			HUD = ImageIO.read(new File("resources/default/textures/hud/userGUI.png"));
		}

		try {
			healthyFace1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/healthyface1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			healthyFace1 = ImageIO.read(new File("resources/default/textures/hud/healthyface1.png"));
		}

		try {
			healthyFace2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/healthyface2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			healthyFace2 = ImageIO.read(new File("resources/default/textures/hud/healthyface2.png"));
		}

		try {
			healthyFace3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/healthyface3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			healthyFace3 = ImageIO.read(new File("resources/default/textures/hud/healthyface3.png"));
		}

		try {
			hurtFace1 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/hurtFace1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			hurtFace1 = ImageIO.read(new File("resources/default/textures/hud/hurtFace1.png"));
		}

		try {
			hurtFace2 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/hurtFace2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			hurtFace2 = ImageIO.read(new File("resources/default/textures/hud/hurtFace2.png"));
		}

		try {
			hurtFace3 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/hurtFace3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			hurtFace3 = ImageIO.read(new File("resources/default/textures/hud/hurtFace3.png"));
		}

		try {
			veryHurtFace1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/veryHurtFace1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			veryHurtFace1 = ImageIO.read(new File("resources/default/textures/hud/veryHurtFace1.png"));
		}

		try {
			veryHurtFace2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/veryHurtFace2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			veryHurtFace2 = ImageIO.read(new File("resources/default/textures/hud/veryHurtFace2.png"));
		}

		try {
			veryHurtFace3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/veryHurtFace3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			veryHurtFace3 = ImageIO.read(new File("resources/default/textures/hud/veryHurtFace3.png"));
		}

		try {
			almostDead1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/almostDeadFace1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			almostDead1 = ImageIO.read(new File("resources/default/textures/hud/almostDeadFace1.png"));
		}

		try {
			almostDead2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/almostDeadFace2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			almostDead2 = ImageIO.read(new File("resources/default/textures/hud/almostDeadFace2.png"));
		}

		try {
			almostDead3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/almostDeadFace3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			almostDead3 = ImageIO.read(new File("resources/default/textures/hud/almostDeadFace3.png"));
		}

		try {
			dead = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/deadFace.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			dead = ImageIO.read(new File("resources/default/textures/hud/deadFace.png"));
		}

		try {
			godMode = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/godModeFace.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			godMode = ImageIO.read(new File("resources/default/textures/hud/godModeFace.png"));
		}

		try {
			invisible = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/invisibilityMode.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			invisible = ImageIO.read(new File("resources/default/textures/hud/invisibilityMode.png"));
		}

		try {
			playerHarmedHealthy = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/healthyHarmed.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			playerHarmedHealthy = ImageIO.read(new File("resources/default/textures/hud/healthyHarmed.png"));
		}

		try {
			playerHarmedHurt = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/hurtHarmed.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			playerHarmedHurt = ImageIO.read(new File("resources/default/textures/hud/hurtHarmed.png"));
		}

		try {
			playerHarmedVeryHurt = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/veryHurtHarmed.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			playerHarmedVeryHurt = ImageIO.read(new File("resources/default/textures/hud/veryHurtHarmed.png"));
		}

		try {
			playerHarmedAlmostDead = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/almostDeadHarmed.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			playerHarmedAlmostDead = ImageIO.read(new File("resources/default/textures/hud/almostDeadHarmed.png"));
		}
		try {
			invisGodmode = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/invisibleGodmode.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			invisGodmode = ImageIO.read(new File("resources/default/textures/hud/invisibleGodmode.png"));
		}

		try {
			protectionIcon = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/protection.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			protectionIcon = ImageIO.read(new File("resources/default/textures/hud/protection.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < protectionIcon.getWidth(); ++x) {
			for (int y = 0; y < protectionIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((protectionIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					protectionIcon.setRGB(x, y, 0);
				}
			}
		}

		try {
			visionIcon = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/vision.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			visionIcon = ImageIO.read(new File("resources/default/textures/hud/vision.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < visionIcon.getWidth(); ++x) {
			for (int y = 0; y < visionIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((visionIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					visionIcon.setRGB(x, y, 0);
				}
			}
		}

		try {
			immortalityIcon = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/immortality.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			immortalityIcon = ImageIO.read(new File("resources/default/textures/hud/immortality.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < immortalityIcon.getWidth(); ++x) {
			for (int y = 0; y < immortalityIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((immortalityIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					immortalityIcon.setRGB(x, y, 0);
				}
			}
		}

		try {
			invisibilityIcon = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/invisibility.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			invisibilityIcon = ImageIO.read(new File("resources/default/textures/hud/invisibility.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < invisibilityIcon.getWidth(); ++x) {
			for (int y = 0; y < invisibilityIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((invisibilityIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					invisibilityIcon.setRGB(x, y, 0);
				}
			}
		}

		try {
			frozenIcon = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/froze.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			frozenIcon = ImageIO.read(new File("resources/default/textures/hud/froze.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < frozenIcon.getWidth(); ++x) {
			for (int y = 0; y < frozenIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((frozenIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					frozenIcon.setRGB(x, y, 0);
				}
			}
		}

		try {
			drunkIcon = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/drunk.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			drunkIcon = ImageIO.read(new File("resources/default/textures/hud/drunk.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < drunkIcon.getWidth(); ++x) {
			for (int y = 0; y < drunkIcon.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((drunkIcon.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					drunkIcon.setRGB(x, y, 0x00FFFFFF);
				}
			}
		}
		// TODO BufferedImage stuff

		try {
			swordAttack5 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/swordattack5.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			swordAttack5 = ImageIO.read(new File("resources/default/textures/hud/swordattack5.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < swordAttack5.getWidth(); ++x) {
			for (int y = 0; y < swordAttack5.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((swordAttack5.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					swordAttack5.setRGB(x, y, 0);
				}
			}
		}

		try {
			swordAttack4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/swordattack4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			swordAttack4 = ImageIO.read(new File("resources/default/textures/hud/swordattack4.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < swordAttack4.getWidth(); ++x) {
			for (int y = 0; y < swordAttack4.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((swordAttack4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					swordAttack4.setRGB(x, y, 0);
				}
			}
		}

		try {
			swordAttack3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/swordattack3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			swordAttack3 = ImageIO.read(new File("resources/default/textures/hud/swordattack3.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < swordAttack3.getWidth(); ++x) {
			for (int y = 0; y < swordAttack3.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((swordAttack3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					swordAttack3.setRGB(x, y, 0);
				}
			}
		}

		try {
			swordAttack2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/swordattack2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			swordAttack2 = ImageIO.read(new File("resources/default/textures/hud/swordattack2.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < swordAttack2.getWidth(); ++x) {
			for (int y = 0; y < swordAttack2.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((swordAttack2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					swordAttack2.setRGB(x, y, 0);
				}
			}
		}

		try {
			swordAttack1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/swordattack1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			swordAttack1 = ImageIO.read(new File("resources/default/textures/hud/swordattack1.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < swordAttack1.getWidth(); ++x) {
			for (int y = 0; y < swordAttack1.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((swordAttack1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					swordAttack1.setRGB(x, y, 0);
				}
			}
		}

		try {
			sword3 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/sword3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			sword3 = ImageIO.read(new File("resources/default/textures/hud/sword3.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < sword3.getWidth(); ++x) {
			for (int y = 0; y < sword3.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((sword3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					sword3.setRGB(x, y, 0);
				}
			}
		}

		try {
			sword2 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/sword2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			sword2 = ImageIO.read(new File("resources/default/textures/hud/sword2.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < sword2.getWidth(); ++x) {
			for (int y = 0; y < sword2.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((sword2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					sword2.setRGB(x, y, 0);
				}
			}
		}

		try {
			sword1 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/sword1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			sword1 = ImageIO.read(new File("resources/default/textures/hud/sword1.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < sword1.getWidth(); ++x) {
			for (int y = 0; y < sword1.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((sword1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					sword1.setRGB(x, y, 0);
				}
			}
		}

		try {
			gunNormal = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/gunNormal.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			gunNormal = ImageIO.read(new File("resources/default/textures/hud/gunNormal.png"));
		}

		/*
		 * Goes through an entire image, and for any white pixels found on the image,
		 * make them translucent.
		 */
		for (int x = 0; x < gunNormal.getWidth(); ++x) {
			for (int y = 0; y < gunNormal.getHeight(); ++y) {
				// Finds white pixels and makes them translucent
				if ((gunNormal.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					gunNormal.setRGB(x, y, 0);
				}
			}
		}

		try {
			gunShot = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/gunShot.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			gunShot = ImageIO.read(new File("resources/default/textures/hud/gunShot.png"));
		}

		// Same as the loop above
		for (int x = 0; x < gunShot.getWidth(); ++x) {
			for (int y = 0; y < gunShot.getHeight(); ++y) {
				if ((gunShot.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					gunShot.setRGB(x, y, 0);
				}
			}
		}

		try {
			gunShot2 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/gunShot2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			gunShot2 = ImageIO.read(new File("resources/default/textures/hud/gunShot2.png"));
		}

		for (int x = 0; x < gunShot2.getWidth(); ++x) {
			for (int y = 0; y < gunShot2.getHeight(); ++y) {
				if ((gunShot2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					gunShot2.setRGB(x, y, 0);
				}
			}
		}

		try {
			gunShot3 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/gunShot3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			gunShot3 = ImageIO.read(new File("resources/default/textures/hud/gunShot3.png"));
		}

		for (int x = 0; x < gunShot3.getWidth(); ++x) {
			for (int y = 0; y < gunShot3.getHeight(); ++y) {
				if ((gunShot3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					gunShot3.setRGB(x, y, 0);
				}
			}
		}

		try {
			gunShot4 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/gunShot4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			gunShot4 = ImageIO.read(new File("resources/default/textures/hud/gunShot4.png"));
		}

		for (int x = 0; x < gunShot4.getWidth(); ++x) {
			for (int y = 0; y < gunShot4.getHeight(); ++y) {
				if ((gunShot4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					gunShot4.setRGB(x, y, 0);
				}
			}
		}

		try {
			phaseCannon1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/phaseCannon1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			phaseCannon1 = ImageIO.read(new File("resources/default/textures/hud/phaseCannon1.png"));
		}

		/*
		 * For the Peace cannon, due to there being too much white on the original
		 * image, I set this too take black pixels and make them translucent instead.
		 */
		for (int x = 0; x < phaseCannon1.getWidth(); ++x) {
			for (int y = 0; y < phaseCannon1.getHeight(); ++y) {
				if ((phaseCannon1.getRGB(x, y) & 0x00FFFFFF) == 0x000000) {
					phaseCannon1.setRGB(x, y, 0);
				}
			}
		}

		try {
			phaseCannon2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/phaseCannon2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			phaseCannon2 = ImageIO.read(new File("resources/default/textures/hud/phaseCannon2.png"));
		}

		for (int x = 0; x < phaseCannon2.getWidth(); ++x) {
			for (int y = 0; y < phaseCannon2.getHeight(); ++y) {
				if ((phaseCannon2.getRGB(x, y) & 0x00FFFFFF) == 0x000000) {
					phaseCannon2.setRGB(x, y, 0);
				}
			}
		}

		try {
			phaseCannon3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/phaseCannon3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			phaseCannon3 = ImageIO.read(new File("resources/default/textures/hud/phaseCannon3.png"));
		}

		for (int x = 0; x < phaseCannon3.getWidth(); ++x) {
			for (int y = 0; y < phaseCannon3.getHeight(); ++y) {
				if ((phaseCannon3.getRGB(x, y) & 0x00FFFFFF) == 0x000000) {
					phaseCannon3.setRGB(x, y, 0);
				}
			}
		}

		try {
			phaseCannon4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/phaseCannon4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			phaseCannon4 = ImageIO.read(new File("resources/default/textures/hud/phaseCannon4.png"));
		}

		for (int x = 0; x < phaseCannon4.getWidth(); ++x) {
			for (int y = 0; y < phaseCannon4.getHeight(); ++y) {
				if ((phaseCannon4.getRGB(x, y) & 0x00FFFFFF) == 0x000000) {
					phaseCannon4.setRGB(x, y, 0);
				}
			}
		}

		try {
			rocketLauncher = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/rocketLauncher.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			rocketLauncher = ImageIO.read(new File("resources/default/textures/hud/rocketLauncher.png"));
		}

		for (int x = 0; x < rocketLauncher.getWidth(); ++x) {
			for (int y = 0; y < rocketLauncher.getHeight(); ++y) {
				if ((rocketLauncher.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					rocketLauncher.setRGB(x, y, 0);
				}
			}
		}

		try {
			rocketLauncher1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/rocketLauncher1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			rocketLauncher1 = ImageIO.read(new File("resources/default/textures/hud/rocketLauncher1.png"));
		}

		for (int x = 0; x < rocketLauncher1.getWidth(); ++x) {
			for (int y = 0; y < rocketLauncher1.getHeight(); ++y) {
				if ((rocketLauncher1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					rocketLauncher1.setRGB(x, y, 0);
				}
			}
		}

		try {
			rocketLauncher2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/rocketLauncher2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			rocketLauncher2 = ImageIO.read(new File("resources/default/textures/hud/rocketLauncher2.png"));
		}

		for (int x = 0; x < rocketLauncher2.getWidth(); ++x) {
			for (int y = 0; y < rocketLauncher2.getHeight(); ++y) {
				if ((rocketLauncher2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					rocketLauncher2.setRGB(x, y, 0);
				}
			}
		}

		try {
			rocketLauncher3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/rocketLauncher3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			rocketLauncher3 = ImageIO.read(new File("resources/default/textures/hud/rocketLauncher3.png"));
		}

		for (int x = 0; x < rocketLauncher3.getWidth(); ++x) {
			for (int y = 0; y < rocketLauncher3.getHeight(); ++y) {
				if ((rocketLauncher3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					rocketLauncher3.setRGB(x, y, 0);
				}
			}
		}

		try {
			rocketLauncher4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/rocketLauncher4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			rocketLauncher4 = ImageIO.read(new File("resources/default/textures/hud/rocketLauncher4.png"));
		}

		for (int x = 0; x < rocketLauncher4.getWidth(); ++x) {
			for (int y = 0; y < rocketLauncher4.getHeight(); ++y) {
				if ((rocketLauncher4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					rocketLauncher4.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolLeft1 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolLeft1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolLeft1 = ImageIO.read(new File("resources/default/textures/hud/pistolLeft1.png"));
		}

		for (int x = 0; x < pistolLeft1.getWidth(); ++x) {
			for (int y = 0; y < pistolLeft1.getHeight(); ++y) {
				if ((pistolLeft1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolLeft1.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolLeft2 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolLeft2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolLeft2 = ImageIO.read(new File("resources/default/textures/hud/pistolLeft2.png"));
		}

		for (int x = 0; x < pistolLeft2.getWidth(); ++x) {
			for (int y = 0; y < pistolLeft2.getHeight(); ++y) {
				if ((pistolLeft2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolLeft2.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolLeft3 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolLeft3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolLeft3 = ImageIO.read(new File("resources/default/textures/hud/pistolLeft3.png"));
		}

		for (int x = 0; x < pistolLeft3.getWidth(); ++x) {
			for (int y = 0; y < pistolLeft3.getHeight(); ++y) {
				if ((pistolLeft3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolLeft3.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolLeft4 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolLeft4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolLeft4 = ImageIO.read(new File("resources/default/textures/hud/pistolLeft4.png"));
		}

		for (int x = 0; x < pistolLeft4.getWidth(); ++x) {
			for (int y = 0; y < pistolLeft4.getHeight(); ++y) {
				if ((pistolLeft4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolLeft4.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolLeft5 = ImageIO.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolLeft5.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolLeft5 = ImageIO.read(new File("resources/default/textures/hud/pistolLeft5.png"));
		}

		for (int x = 0; x < pistolLeft5.getWidth(); ++x) {
			for (int y = 0; y < pistolLeft5.getHeight(); ++y) {
				if ((pistolLeft5.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolLeft5.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolRight1 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolRight1.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolRight1 = ImageIO.read(new File("resources/default/textures/hud/pistolRight1.png"));
		}

		for (int x = 0; x < pistolRight1.getWidth(); ++x) {
			for (int y = 0; y < pistolRight1.getHeight(); ++y) {
				if ((pistolRight1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolRight1.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolRight2 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolRight2.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolRight2 = ImageIO.read(new File("resources/default/textures/hud/pistolRight2.png"));
		}

		for (int x = 0; x < pistolRight2.getWidth(); ++x) {
			for (int y = 0; y < pistolRight2.getHeight(); ++y) {
				if ((pistolRight2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolRight2.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolRight3 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolRight3.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolRight3 = ImageIO.read(new File("resources/default/textures/hud/pistolRight3.png"));
		}

		for (int x = 0; x < pistolRight3.getWidth(); ++x) {
			for (int y = 0; y < pistolRight3.getHeight(); ++y) {
				if ((pistolRight3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolRight3.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolRight4 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolRight4.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolRight4 = ImageIO.read(new File("resources/default/textures/hud/pistolRight4.png"));
		}

		for (int x = 0; x < pistolRight4.getWidth(); ++x) {
			for (int y = 0; y < pistolRight4.getHeight(); ++y) {
				if ((pistolRight4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolRight4.setRGB(x, y, 0);
				}
			}
		}

		try {
			pistolRight5 = ImageIO
					.read(new File("resources" + FPSLauncher.themeName + "/textures/hud/pistolRight5.png"));
		} catch (Exception e) {
			// If no file is found, use the default. If the default cannot be loaded
			// correctly, then the exception is thrown and the game doesn't display
			// any texture letting you know something is wrong.
			pistolRight5 = ImageIO.read(new File("resources/default/textures/hud/pistolRight5.png"));
		}

		for (int x = 0; x < pistolRight5.getWidth(); ++x) {
			for (int y = 0; y < pistolRight5.getHeight(); ++y) {
				if ((pistolRight5.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) {
					pistolRight5.setRGB(x, y, 0);
				}
			}
		}
	}

	/**
	 * The only way any other class can restart survival mode. So this method is
	 * called to do so.
	 */
	public void restartSurvival() {
		// So it knows what method to call
		FPSLauncher.loadingGame = false;
		FPSLauncher.gameMode = 1;

		// Starts a new game in survival mode
		game = new Game(this, false, "");

		// Resets enemies made happy
		Player.kills = 0;
	}
}
