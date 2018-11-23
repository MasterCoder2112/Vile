package com.vile.launcher;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.vile.Display;
import com.vile.Game;
import com.vile.RunGame;
import com.vile.Sound;
import com.vile.entities.Bullet;
import com.vile.entities.Button;
import com.vile.entities.Corpse;
import com.vile.entities.Door;
import com.vile.entities.Elevator;
import com.vile.entities.Enemy;
import com.vile.entities.EnemyFire;
import com.vile.entities.Explosion;
import com.vile.entities.Item;
import com.vile.entities.Player;
import com.vile.entities.Weapon;
import com.vile.graphics.Render3D;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * @title FPSLauncher
 * @author Alex Byrd
 * @modified 11/10/2018 Description: Starts the program off with the main menu.
 *           This holds the title theme music, the Start New Game button, the
 *           Options menu to set how you want your game to be, the Controls, and
 *           the Quit button.
 *
 */
public class FPSLauncher extends JFrame {
	private static final long serialVersionUID = 1L;

	// Version of the main menu
	private static final double versionNumber = 1;

	// Music volume control
	public static FloatControl musicVolumeControl;

	// Click sound for main menu only
	private static Sound click;

	// Is the game audio on
	private static boolean audioOn = false;

	// For flashing text if needed
	private Thread t = null;

	/* Java Swing elements **********************************/
	private JLayeredPane panel = new JLayeredPane();

	private static JButton play;
	private static JButton playGame;
	private static JButton saveGame;
	private static JButton loadGame;
	private static JButton options;
	private static JButton controls;
	private static JButton quit;
	private static JButton back;
	private static JButton mouse;
	private static JButton returnToGame;
	private static JButton customMap;
	private static JButton sFPS;
	private static JButton logIn;
	private static JButton logOut;
	private static JButton removeUser;
	private static JButton saveGameMenu;
	private static JButton removeSave;
	private static JButton multiplayer;
	private static JButton host;
	private static JButton joinServer;
	private static JButton join;
	private static JButton backMult;

	private static JLabel resolutionTitle;
	private static JLabel themeTitle;
	private static JLabel levelSizeTitle;
	private static JLabel modeTitle;
	private static JLabel musicTitle;
	private static JLabel musicVolumeTitle;
	private static JLabel soundVolumeTitle;
	private static JLabel title;
	private static JLabel newMapTitle;
	private static JLabel userTitle;
	private static JLabel newUserTitle;
	private static JLabel error;
	private static JLabel availableGames;
	private static JLabel saveName;
	private static JLabel ipAddressTitle;
	private static JLabel portTitle;

	private static JTextArea readMeText;
	public static JTextField newMapName;
	private static JTextField newUserName;
	private static JTextField saveTextfield;
	private static JTextField ipAddress;
	private static JTextField port;
	/********************************************* End elements */

	private static ImageIcon titleImage;

	// Has scroll box you can put choices on
	private Choice resolution = new Choice();
	private Choice theme = new Choice();
	private Choice levelSize = new Choice();
	private Choice mode = new Choice();
	private Choice music = new Choice();
	private Choice users = new Choice();
	private Choice savedGames = new Choice();

	public static int resolutionChoice = 4;
	public static int levelSizeChoice = 3;
	public static int modeChoice = 2;
	public static int musicChoice = 2;
	public static int usersChoice = 0;
	public static int saveChoice = 0;

	// Name of resource pack being used
	public static String themeName = "/default";
	public static String themeNameOld = "/default";

	// Volume Knobs
	public static JSlider musicVolume;
	public static JSlider soundVolume;

	/*
	 * On a scale from 0 to 100% of the volume, what level is it at right now?
	 */
	public static float musicVolumeLevel = 50;
	public static float soundVolumeLevel = 50;

	// In Story, or survival mode? Or no mode at all?
	public static int gameMode = 2;

	// Is mouse option on
	private static boolean mouseStatus = true;

	// Is custom map option on?
	private static boolean nonDefaultMap = false;

	// All the usernames created thus far
	private static ArrayList<String> usernames = new ArrayList<String>();

	// All the saved games created thus far
	private static ArrayList<String> saves = new ArrayList<String>();

	// The File where all the current users info is based
	public static File userFile;

	// Is smooth FPS on?
	public static boolean smoothFPS = true;

	// Is the user trying to load a game?
	public static boolean loadingGame = false;

	// If return to game is pressed
	public static boolean returning = false;

	// Width and height of the Jframe here
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	// Current users ID number and name
	public static int currentUserID = 0;
	public static String currentUserName = "";

	// Default starting map name
	public static String startMap = "map0";

	// File name of the file being loaded
	public static String fileToLoad = "";

	// The ID of the launcher menu. Either main menu, options, controls,
	// or Log in screen.
	public int idNum = 3;

	// Input stream only for the launcher
	private static AudioInputStream input;

	// Makes sure click audio clips aren't opened multiple times
	private static boolean opened = false;

	/*
	 * Listens for if an item in a choice element was pressed. Right now I am only
	 * using this for the save and loading choices
	 */
	ItemListener iL = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			// Set the textfields text to being the name of the saved game
			saveTextfield.setText(savedGames.getItem(savedGames.getSelectedIndex()));
		}
	};

	/*
	 * Instantiates a new ActionListener that listens to whether Buttons are clicked
	 * or not.
	 */
	ActionListener aL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Call the play audio method to play the sound
			click.playAudioFile(0);

			// If Survival mode is pressed then close the main menu
			// music, dispose of the menu, and start new game
			if (e.getSource() == play) {
				try {
					Display.music.close();
				} catch (Exception ex) {

				}

				Display.music = null;

				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				audioOn = false;
				dispose();
				gameMode = 1;

				Display.mouseOn = mouseStatus;
				Display.musicTheme = musicChoice;
				Display.smoothFPS = smoothFPS;

				loadingGame = false;
				returning = false;

				new RunGame();
			}

			// Same stuff as above but start a new game with either a
			// custom map or the default map
			if (e.getSource() == playGame) {
				// Close out of current music thread
				try {
					Display.inputStream.close();
				} catch (Exception ex) {

				}

				Display.inputStream = null;

				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				try {
					Display.music.close();
				} catch (Exception ex) {

				}

				Display.music = null;

				try {
					RunGame.game = null;
				} catch (Exception ex) {

				}

				// Garbage collect
				System.gc();

				audioOn = false;
				dispose();
				gameMode = 0;

				Display.mouseOn = mouseStatus;
				Display.musicTheme = musicChoice;
				Display.smoothFPS = smoothFPS;

				// If custom map, send these values so the game knows
				// what to load up
				Display.nonDefaultMap = nonDefaultMap;
				Display.newMapName = startMap;

				loadingGame = false;
				returning = false;

				new RunGame();
			}

			// Start up options menu
			if (e.getSource() == options) {
				dispose();
				new Options();

				// TODO change if need be

				/*
				 * Fixes an old bug where the textfield was unable to gain focus sometimes, and
				 * therefore a mapname could not be entered if you wanted to do so.
				 */
				if (newMapName != null) {
					newMapName.setEnabled(true);
					newMapName.setEditable(true);
					newMapName.setFocusable(true);
					newMapName.requestFocus();
					newMapName.requestFocusInWindow();
					newMapName.grabFocus();
				}
			}

			// Start up Multiplayer menu
			if (e.getSource() == multiplayer) {

				dispose();
				new Multiplayer();

			}

			// Start up Join Server menu
			if (e.getSource() == joinServer) {

				dispose();
				new JoinServer();

			}

			// Start up controls menu
			if (e.getSource() == controls) {
				dispose();
				new Controls();
			}

			// Start up multiplayer menu
			if (e.getSource() == backMult) {
				dispose();
				new Multiplayer();
			}

			// Quit game button is clicked
			if (e.getSource() == quit) {
				quitGame();
			}

			// Save options menu changes and go back to menu screen
			if (e.getSource() == back) {
				// Only sets these if exiting out of the options
				// menu
				if (idNum == 1) {
					String temp = "/" + theme.getItem(theme.getSelectedIndex());
					resolutionChoice = resolution.getSelectedIndex();

					// If theme changed, stop current music and change themeName
					// Close all open audio threads
					if (!themeName.equals(temp)) {
						try {
							Display.inputStream.close();
						} catch (Exception ex) {

						}

						try {
							input.close();
						} catch (Exception ex) {

						}

						input = null;

						Display.inputStream = null;

						try {
							Display.music.close();
						} catch (Exception ex) {
						}

						Display.music = null;

						themeName = temp;
					}

					levelSizeChoice = levelSize.getSelectedIndex();
					modeChoice = mode.getSelectedIndex();
					musicChoice = music.getSelectedIndex();

					// If the map name is null. Catch the exception
					try {
						startMap = newMapName.getText();
					} catch (Exception ex) {
						startMap = "map0";
					}
				}

				dispose();
				new FPSLauncher(0);
			}

			// Return to current game
			if (e.getSource() == returnToGame) {
				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				// Shut off main menu music if the audio is on
				if (audioOn) {
					try {
						Display.music.close();
					} catch (Exception ex) {

					}

					Display.music = null;
					Display.audioOn = false;
				}

				audioOn = false;

				Display.mouseOn = mouseStatus;
				Display.musicTheme = musicChoice;
				Display.smoothFPS = smoothFPS;
				returning = true;

				dispose();
				new RunGame();
			}

			// Turn mouse on or off
			if (e.getSource() == mouse) {
				if (mouseStatus) {
					mouseStatus = false;
					mouse.setText("Mouse Status: Off");
				} else {
					mouseStatus = true;
					mouse.setText("Mouse Status: On");
				}
			}

			// Turn smooth fps on or off
			if (e.getSource() == sFPS) {
				if (smoothFPS) {
					smoothFPS = false;
					sFPS.setText("Smooth FPS: Off");
				} else {
					smoothFPS = true;
					sFPS.setText("Smooth FPS: On");
				}
			}

			// Custom map loading is on or off
			if (e.getSource() == customMap) {
				if (nonDefaultMap) {
					nonDefaultMap = false;
					customMap.setText("Custom Map: Off");
				} else {
					nonDefaultMap = true;
					customMap.setText("Custom Map: On");
				}
			}

			// Logs out back to the log in screen for a new user
			if (e.getSource() == logOut) {
				// Prompt user to say yes or no to removing the user
				int response = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to Log out? Any unsaved data will be erased.", "Log out?",
						JOptionPane.YES_NO_OPTION);

				// If no then return, if yes then continue
				if (response == JOptionPane.NO_OPTION) {
					return;
				}

				Display.music.stop();
				audioOn = false;

				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				saveStats();

				saves = new ArrayList<String>();

				Display.pauseGame = false;

				resolutionChoice = 4;
				themeName = "/default";
				levelSizeChoice = 3;
				modeChoice = 2;
				musicChoice = 2;
				usersChoice = 0;
				saveChoice = 0;
				mouseStatus = true;
				nonDefaultMap = false;
				smoothFPS = false;
				soundVolumeLevel = 50;
				musicVolumeLevel = 50;

				dispose();
				new LogIn();
			}

			// Opens up the save/load game menu
			if (e.getSource() == saveGameMenu) {
				dispose();
				new SaveScreen();
			}

			// Saves Game for the player
			if (e.getSource() == saveGame) {
				// If not in a game, then don't try to save
				if (gameMode == 2) {
					displayError("YOU ARE NOT CURRENTLY IN A GAME!!!");
					return;
				}

				// Get text from saveName textfield. Will contain the name
				// of the file you're trying to save
				String n = saveTextfield.getText();

				// If special character is found
				if (n.contains(":") || n.contains("/") || n.contains("\\") || n.contains("|") || n.contains("?")
						|| n.contains("*") || n.contains("\"") || n.contains(">") || n.contains("<")) {
					displayError("INVALID CHARACTERS! TRY AGAIN!");
					return;
				}

				// If name is too long
				if (n.length() >= 50) {
					displayError("NAME IS TOO LONG!!!");
					return;
				}

				String fileName;

				try {
					fileName = saveTextfield.getText();

					if (fileName == "") {
						displayError("THERE IS NO FILE NAME!");
						return;
					}
				} catch (Exception ex) {
					displayError("THERE IS NO FILE NAME!");
					return;
				}

				BufferedWriter rewrite;

				/*
				 * Tries to write game data into a save file within the current users directory
				 */
				try {
					rewrite = new BufferedWriter(new FileWriter("Users/" + currentUserName + "/" + fileName + ".txt"));

					rewrite.write(Game.mapNum + ":" + gameMode + ":" + Game.secretsFound + ":" + Game.enemiesInMap + ":"
							+ Display.kills + ":" + themeName);

					rewrite.newLine();

					// Player and level stuff
					rewrite.write(Player.health + ":" + Player.maxHealth + ":" + Player.armor + ":" + Player.x + ":"
							+ Player.y + ":" + Player.z + ":" + Player.rotation + ":" + Player.maxHeight + ":"
							+ Player.hasRedKey + ":" + Player.hasBlueKey + ":" + Player.hasGreenKey + ":"
							+ Player.hasYellowKey + ":" + Player.upRotate + ":" + Player.extraHeight + ":"
							+ Player.resurrections + ":" + Player.environProtectionTime + ":" + Player.immortality + ":"
							+ Player.vision + ":" + Player.invisibility + ":" + Player.weaponEquipped + ":"
							+ Player.godModeOn + ":" + Player.noClipOn + ":" + Player.flyOn + ":" + Player.superSpeedOn
							+ ":" + Player.unlimitedAmmoOn + ":" + Player.upgradePoints + ":" + Level.width + ":"
							+ Level.height + ":" + Game.mapNum + ":" + Game.mapAudio + ":" + Game.mapFloor + ":"
							+ Game.mapCeiling + ":" + Render3D.ceilingDefaultHeight + ":" + Level.renderDistance + ":"
							+ Game.mapName + ",");

					// Weapons
					for (int i = 0; i < Player.weapons.length; i++) {
						Weapon w = Player.weapons[i];
						int size = w.cartridges.size();

						rewrite.write(w.weaponID + ":" + w.canBeEquipped + ":" + w.dualWield + ":" + w.ammo);

						for (int j = 0; j < size; j++) {
							int cartSize = w.cartridges.get(j).ammo;
							rewrite.write(":" + cartSize);
						}

						rewrite.write(";");
					}

					rewrite.newLine();
					rewrite.write("Walls:");
					rewrite.newLine();

					for (int i = 0; i < Level.blocks.length; i++) {
						Block b = Level.blocks[i];
						rewrite.write(b.health + ":" + b.x + ":" + b.y + ":" + b.z + ":" + b.wallID + ":" + b.wallPhase
								+ ":" + b.height + ":" + b.isSolid + ":" + b.seeThrough + ";");
					}

					rewrite.newLine();
					rewrite.write("Enemies:");
					rewrite.newLine();

					for (int i = 0; i < Game.enemies.size(); i++) {
						Enemy en = Game.enemies.get(i);
						rewrite.write(en.health + ":" + en.xPos + ":" + en.yPos + ":" + en.zPos + ":" + en.ID + ":"
								+ en.itemActivationID + ":" + en.maxHeight + ":" + en.newTarget + ":" + en.targetX + ":"
								+ en.targetY + ":" + en.targetZ + ":" + en.activated + ":" + en.rotation + ":"
								+ en.isAttacking + ":" + en.isFiring + ":" + en.isABoss + ":" + en.xEffects + ":"
								+ en.yEffects + ":" + en.zEffects + ":" + en.tick + ":" + en.tickRound + ":"
								+ en.tickAmount + ";");
					}

					rewrite.newLine();
					rewrite.write("Bosses:");
					rewrite.newLine();

					for (int i = 0; i < Game.bosses.size(); i++) {
						Enemy en = Game.bosses.get(i);
						rewrite.write(en.health + ":" + en.xPos + ":" + en.yPos + ":" + en.zPos + ":" + en.ID + ":"
								+ en.itemActivationID + ":" + en.maxHeight + ":" + en.newTarget + ":" + en.targetX + ":"
								+ en.targetY + ":" + en.targetZ + ":" + en.activated + ":" + en.rotation + ":"
								+ en.isAttacking + ":" + en.isFiring + ":" + en.isABoss + ":" + en.xEffects + ":"
								+ en.yEffects + ":" + en.zEffects + ":" + en.tick + ":" + en.tickRound + ":"
								+ en.tickAmount + ";");
					}

					rewrite.newLine();
					rewrite.write("Items:");
					rewrite.newLine();

					for (int i = 0; i < Game.items.size(); i++) {
						Item item = Game.items.get(i);
						String audioQueue = item.audioQueue;

						// So any null audio queues will be set as -1
						// to be ignored
						if (audioQueue.equals("")) {
							audioQueue = "-1";
						}

						rewrite.write(item.itemActivationID + ":" + item.itemID + ":" + item.x + ":" + item.y + ":"
								+ item.z + ":" + item.rotation + ":" + audioQueue + ";");
					}

					rewrite.newLine();
					rewrite.write("Bullets:");
					rewrite.newLine();

					for (int i = 0; i < Game.bullets.size(); i++) {
						Bullet b = Game.bullets.get(i);
						rewrite.write(b.ID + ":" + b.damage + ":" + b.speed + ":" + b.x + ":" + b.y + ":" + b.z + ":"
								+ b.xa + ":" + b.za + ":" + b.initialSpeed + ";");
					}

					rewrite.newLine();
					rewrite.write("Enemy Projectiles:");
					rewrite.newLine();

					for (int i = 0; i < Game.enemyProjectiles.size(); i++) {
						EnemyFire b = Game.enemyProjectiles.get(i);
						rewrite.write(b.ID + ":" + b.damage + ":" + b.speed + ":" + b.x + ":" + b.y + ":" + b.z + ":"
								+ b.xa + ":" + b.za + ":" + b.initialSpeed + ";");
					}

					rewrite.newLine();
					rewrite.write("Explosions:");
					rewrite.newLine();

					for (int i = 0; i < Game.explosions.size(); i++) {
						Explosion exp = Game.explosions.get(i);
						rewrite.write(exp.ID + ":" + exp.phaseTime + ":" + exp.x + ":" + exp.y + ":" + exp.z + ":"
								+ exp.exploded + ":" + exp.itemActivationID + ";");
					}

					rewrite.newLine();
					rewrite.write("Buttons:");
					rewrite.newLine();

					for (int i = 0; i < Game.buttons.size(); i++) {
						Button b = Game.buttons.get(i);
						rewrite.write(b.itemID + ":" + b.itemActivationID + ":" + b.x + ":" + b.y + ":" + b.z + ":"
								+ b.pressed + ":" + b.audioQueue + ";");
					}

					rewrite.newLine();
					rewrite.write("Doors:");
					rewrite.newLine();

					for (int i = 0; i < Game.doors.size(); i++) {
						Door d = Game.doors.get(i);
						rewrite.write(d.ID + ":" + d.itemActivationID + ":" + d.xPos + ":" + d.yPos + ":" + d.zPos + ":"
								+ d.doorX + ":" + d.doorZ + ":" + d.time + ":" + d.soundTime + ":" + d.doorType + ":"
								+ d.doorY + ":" + d.maxHeight + ";");
					}

					rewrite.newLine();
					rewrite.write("Elevators:");
					rewrite.newLine();

					for (int i = 0; i < Game.elevators.size(); i++) {
						Elevator ele = Game.elevators.get(i);
						rewrite.write(ele.ID + ":" + ele.itemActivationID + ":" + ele.xPos + ":" + ele.yPos + ":"
								+ ele.zPos + ":" + ele.elevatorX + ":" + ele.elevatorZ + ":" + ele.height + ":"
								+ ele.soundTime + ":" + ele.movingUp + ":" + ele.movingDown + ":" + ele.waitTime + ":"
								+ ele.upHeight + ":" + ele.activated + ":" + ele.maxHeight + ";");
					}

					rewrite.newLine();
					rewrite.write("Corpses:");
					rewrite.newLine();

					for (int i = 0; i < Game.corpses.size(); i++) {
						Corpse cor = Game.corpses.get(i);
						rewrite.write(cor.enemyID + ":" + cor.phaseTime + ":" + cor.xPos + ":" + cor.yPos + ":"
								+ cor.zPos + ":" + cor.time + ":" + cor.xEffects + ":" + cor.yEffects + ":"
								+ cor.zEffects + ";");
					}

					rewrite.close();
				} catch (IOException ex) {
					System.out.println(ex);
					// exception handling left as an exercise for the reader
				}

				// If there is not already a save file by this name
				if (!saves.contains(fileName)) {
					saves.add(fileName);
				}

				// The saves arraylist has the same indexes as does the
				// savedGames choice element. So this chooses the most
				// recently changed index.
				saveChoice = saves.indexOf(fileName);

				// Refreshes the page
				dispose();
				new SaveScreen();
			}

			// Loads Game for the player
			if (e.getSource() == loadGame) {
				try {
					// TODO fix up
					fileToLoad = saveTextfield.getText();

					// If no file is entered, or the file doesn't exist
					if (fileToLoad == "" || !saves.contains(fileToLoad)) {
						displayError("THAT FILE DOES NOT EXIST!!!");
						return;
					}

					// The saves arraylist has the same indexes as does the
					// savedGames choice element. So this chooses the most
					// recently changed index.
					saveChoice = saves.indexOf(fileToLoad);
				} catch (Exception ex) {
					displayError("THAT FILE DOES NOT EXIST!!!");
					return;
				}

				loadingGame = true;

				Display.music.stop();
				audioOn = false;

				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				dispose();
				gameMode = 0;

				new RunGame();
			}

			// Removes the user from the list
			if (e.getSource() == removeUser) {
				// Prompt user to say yes or no to removing the user
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this user?",
						"Remove User?", JOptionPane.YES_NO_OPTION);

				// If no then return, if yes then continue
				if (response == JOptionPane.NO_OPTION) {
					return;
				}

				currentUserID = users.getSelectedIndex();
				currentUserName = users.getSelectedItem();

				users.remove(currentUserID);
				usernames.remove(currentUserName);

				BufferedWriter rewrite;

				/*
				 * Tries to write the new user to the user file
				 */
				try {
					rewrite = new BufferedWriter(new FileWriter("users.txt"));

					for (String temp : usernames) {
						if (temp.length() > 0) {
							rewrite.write(temp + ",");
						}
					}

					rewrite.close();
				} catch (IOException ex) {

				}

				deleteFolder(new File("Users/" + currentUserName));

				dispose();
				new LogIn();
			}

			// If removing a save file
			if (e.getSource() == removeSave) {
				String temp = saveTextfield.getText();

				// If the file you're trying to delete is not in the saves
				// list, or is blank
				if (temp == "" || !saves.contains(temp)) {
					displayError("THAT FILE IS NOT AVAILABLE!!!");
					return;
				}

				deleteFolder(new File("Users/" + currentUserName + "/" + temp + ".txt"));

				saves.remove(temp);

				saveChoice = 0;

				// Refresh the screen
				dispose();
				new SaveScreen();
			}

			// If logging in
			if (e.getSource() == logIn) {
				try {
					Display.music.close();
				} catch (Exception ex) {

				}

				Display.music = null;

				audioOn = false;

				// Close out of current music thread
				try {
					input.close();
				} catch (Exception ex) {

				}

				input = null;

				try {
					// If the user did not type in a new username
					if (newUserName.getText().length() == 0) {
						// If there is no user to log in
						if (users.getItemCount() == 0 || users.getSelectedItem() == "") {
							displayError("YOU NEED TO ENTER A USERNAME!!!");
							return;
						}

						// Load the user that is shown on the currently
						// selected index
						currentUserID = users.getSelectedIndex();
						currentUserName = users.getSelectedItem();

						// Keep track of the index chosen
						usersChoice = users.getSelectedIndex();

						// File used for saving
						userFile = new File("Users/" + currentUserName);

						// A new scanner object that is defaultly set to null
						Scanner sc = null;

						/*
						 * Try to read the settings file and if not, state the error
						 */
						try {
							// Creates a Scanner that can read the file
							sc = new Scanner(new BufferedReader(
									new FileReader("Users/" + currentUserName + "/stats/settings.txt")));
							// +FPSLauncher.saveFileName.getText())));

							String file = "";

							// Read in all the lines of the file
							while (sc.hasNextLine()) {
								file += sc.nextLine();
							}

							// Captures all the individual elements
							String[] elements = file.split(":");

							// Set up values based on loaded elements
							resolutionChoice = (Integer.parseInt(elements[0]));
							Player.maxKills = Integer.parseInt(elements[1]);
							levelSizeChoice = (Integer.parseInt(elements[2]));
							startMap = elements[3];
							themeName = ((elements[4]));
							modeChoice = (Integer.parseInt(elements[5]));
							musicChoice = (Integer.parseInt(elements[6]));
							soundVolumeLevel = Float.parseFloat(elements[7]);
							musicVolumeLevel = Float.parseFloat(elements[8]);
							smoothFPS = Boolean.parseBoolean(elements[9]);
							mouseStatus = Boolean.parseBoolean(elements[10]);
							nonDefaultMap = Boolean.parseBoolean(elements[11]);

							// Everything else in the file will be the saved game
							// names this user has
							for (int i = 12; i < elements.length; i++) {
								saves.add(elements[i]);
							}

							// No values are allowed to be below zero
							// otherwise they'd throw exceptions
							if (resolutionChoice < 0) {
								resolutionChoice = 0;
							}

							if (levelSizeChoice < 0) {
								levelSizeChoice = 0;
							}

							if (modeChoice < 0) {
								modeChoice = 0;
							}

							if (musicChoice < 0) {
								musicChoice = 0;
							}
						} catch (Exception ex) {
							System.out.println(ex);
						}

						sc.close();

						dispose();
						new FPSLauncher(0);
					} else {
						// If name already exists flash error
						if (usernames.contains(newUserName.getText())) {
							displayError("THAT USER ALREADY EXISTS! TRY ANOTHER!");
							return;
						} else {
							String n = newUserName.getText();

							// If special character is found
							if (n.contains(":") || n.contains("/") || n.contains("\\") || n.contains("|")
									|| n.contains("?") || n.contains("*") || n.contains("\"") || n.contains(">")
									|| n.contains("<")) {
								displayError("INVALID CHARACTERS! TRY AGAIN!");
								return;
							}

							if (n.length() >= 50) {
								displayError("NAME IS TOO LONG!!!");
								return;
							}

							currentUserID = users.getItemCount() + 1;
							currentUserName = n;

							String temp = currentUserName;
							/*
							 * Tries to write the new user to the user file
							 */
							try {
								Files.write(Paths.get("users.txt"), (temp + ",").getBytes(), StandardOpenOption.APPEND);
							} catch (IOException ex) {
								// exception handling left as an exercise for the reader
							}

							// Make directories for the users
							boolean fileMade = new File("Users/" + currentUserName + "/stats").mkdirs();

							// If the file was successfully made
							if (fileMade) {
								userFile = new File("Users/" + currentUserName);
							}

							resolutionChoice = 4;
							themeName = "/default";
							levelSizeChoice = 3;
							modeChoice = 2;
							musicChoice = 2;
							usersChoice = 0;
							saveChoice = 0;
							mouseStatus = true;
							nonDefaultMap = false;
							smoothFPS = false;
							soundVolumeLevel = 50;
							musicVolumeLevel = 50;

							dispose();
							new FPSLauncher(0);
						}
					}
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		}
	};

	/*
	 * A listener that listens for any change of the volume knob. If the state of
	 * the knob is changed, the method is passed an event which then performs a
	 * serious of operations that the user programs it to do. In this case, the clip
	 * is played at a new volume, and the tool tip on the volume knob is updated to
	 * show the new volume level.
	 */
	ChangeListener change = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == musicVolume) {
				float newVolume = musicVolume.getValue();
				musicVolumeLevel = newVolume;

				Sound.setMusicVolume(Display.music);

				// This is the volume level that makes since to us
				musicVolume.setToolTipText("Music Volume Level: " + musicVolumeLevel);
			}

			// Change sound volume as slider is moved
			if (e.getSource() == soundVolume) {
				// Gets new sound volume from slider
				float newVolume2 = soundVolume.getValue();
				soundVolumeLevel = newVolume2;

				// Resets sound volumes
				click.resetVolume(newVolume2);

				// Show the volume level that makes since to us
				soundVolume.setToolTipText("Sound Volume Level: " + soundVolumeLevel);
			}
		}
	};

	/**
	 * Starts the specific launcher depending on its idNum. It could be the pause
	 * menu, the main menu, the options menu, etc...
	 * 
	 * @param idNum
	 */
	public FPSLauncher(int idNum) {
		this.idNum = idNum;
		/*
		 * If the game was running, dispose of its frame so that the pause menu can show
		 */
		try {
			RunGame.frame.dispose();
		} catch (Exception e) {

		}

		// Title of menu depending on whether game is paused or not
		if (!Display.pauseGame) {
			setTitle("Vile Launcher (Version: " + versionNumber + ")");
		} else {
			setTitle("Pause Menu");
		}

		// Self explainitory shtuff
		setSize(new Dimension(WIDTH, HEIGHT));

		// Does nothing when you try to close the window initially
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// A listener, which checks to see if you are exiting out or not, and
		// if you are it calls the quitGame method and checks to make sure the
		// user really wants to quit, as well as saving their settings.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				quitGame();
			}
		});

		setContentPane(panel);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(false);

		// No default layout, this is so i can set positions manually
		panel.setLayout(null);

		// If normal menu, not options or controls, set up default buttons
		if (idNum == 0) {
			drawLauncherButtons();
			drawBackground();
		}

		/*
		 * Try to open up the title theme audio clip and let it loop continuously
		 */
		try {
			if ((!audioOn && !Display.audioOn) || !themeName.equals(themeNameOld)) {
				// If theme is changing
				if (audioOn || Display.audioOn) {
					try {
						input.close();
					} catch (Exception e) {

					}

					input = null;

					try {
						Display.inputStream.close();
					} catch (Exception e) {

					}

					Display.inputStream = null;

					try {
						Display.music.close();
					} catch (Exception e) {

					}

					Display.music = null;

					Display.audioOn = false;
				}

				themeNameOld = themeName;
				Display.music = AudioSystem.getClip();
				input = AudioSystem.getAudioInputStream(new File("resources" + themeName + "/audio/title.wav"));
				// (this.getClass().getResource
				// ("resources"+Textures.extraFolder+"/audio/test/title.wav"));
				Display.music.open(input);
				Display.music.loop(Clip.LOOP_CONTINUOUSLY);
				Display.music.start();
				audioOn = true;
			}
		} catch (Exception e) {
			try {
				if (!audioOn && !Display.audioOn) {
					Display.music = AudioSystem.getClip();
					AudioInputStream input = AudioSystem
							.getAudioInputStream(new File("resources/default/audio/title.wav"));
					// (this.getClass().getResource
					// ("resources"+Textures.extraFolder+"/audio/test/title.wav"));
					Display.music.open(input);
					Display.music.loop(Clip.LOOP_CONTINUOUSLY);
					Display.music.start();
					audioOn = true;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		try {
			musicVolumeControl = (FloatControl) Display.music.getControl(FloatControl.Type.MASTER_GAIN);
		}
		// For master gain issues
		catch (Exception e) {
			System.out.println(e);
		}

		// Reset music volume
		Sound.setMusicVolume(Display.music);

		if (!opened) {
			try {
				click = new Sound(4, "resources" + themeName + "/audio/titleClick.wav");
			} catch (Exception e) {
				try {
					click = new Sound(4, "resources/default/audio/titleClick.wav");
				} catch (Exception ex) {
					// Sound couldn't be found
				}
			}

			opened = true;
		}

		// Changes Java Icon to the new Vile Icon
		ImageIcon titleIcon = new ImageIcon("resources" + themeName + "/textures/hud/titleIcon.png");
		setIconImage(titleIcon.getImage());

		setVisible(true);
	}

	/**
	 * Draws all the buttons that show when the launcher first opens so that the
	 * play can choose to play the game, or open up the options menu, or the
	 * controls menu, etc...
	 * 
	 * Most of this is self explainitory Java swing stuff and because I've been
	 * commenting all day and have to get this version out I'm not even going to try
	 * to comment all this simple stuff.
	 */
	private void drawLauncherButtons() {
		// Colors of buttons when mouse is not over them or when it is
		Color defaultColor = Color.RED;
		Color mouseColor = Color.GREEN;

		play = new JButton("Survival Mode");

		play.setBounds(0, 200, 295, 40);

		// Button blends with background
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		play.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		play.setFocusPainted(false);

		// Sets font type, mode, and size
		play.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		play.setFont(play.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		play.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		play.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				play.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				play.setForeground(defaultColor);
			}
		});

		play.addActionListener(aL);
		panel.add(play);

		multiplayer = new JButton("Multiplayer");

		multiplayer.setBounds(0, 250, 265, 40);

		// Button blends with background
		multiplayer.setOpaque(false);
		multiplayer.setContentAreaFilled(false);
		multiplayer.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		multiplayer.setFocusPainted(false);

		// Sets font type, mode, and size
		multiplayer.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		multiplayer.setFont(multiplayer.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		multiplayer.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		multiplayer.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				multiplayer.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				multiplayer.setForeground(defaultColor);
			}
		});

		multiplayer.addActionListener(aL);
		panel.add(multiplayer);

		playGame = new JButton("New Game");
		playGame.setBounds(0, 100, 270, 40);

		playGame.setOpaque(false);
		playGame.setContentAreaFilled(false);
		playGame.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		playGame.setFocusPainted(false);

		playGame.setFont(new Font("Nasalization", Font.BOLD, 24));
		playGame.setFont(playGame.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		playGame.setForeground(defaultColor);

		playGame.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				playGame.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				playGame.setForeground(defaultColor);
			}
		});

		playGame.addActionListener(aL);
		panel.add(playGame);

		returnToGame = new JButton("Return to game");
		returnToGame.setBounds(500, 240, 300, 50);

		// Button blends with background
		returnToGame.setOpaque(false);
		returnToGame.setContentAreaFilled(false);
		returnToGame.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		returnToGame.setFocusPainted(false);

		// Sets font type, mode, and size
		returnToGame.setFont(new Font("Nasalization", Font.BOLD, 24));

		returnToGame.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		returnToGame.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				returnToGame.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				returnToGame.setForeground(defaultColor);
			}
		});

		returnToGame.addActionListener(aL);

		// If game is paused, show that you can return to the game
		if (Display.pauseGame) {
			panel.add(returnToGame);
		}

		saveGameMenu = new JButton("Save/Load Game");
		saveGameMenu.setBounds(30, 150, 270, 40);

		saveGameMenu.setOpaque(false);
		saveGameMenu.setContentAreaFilled(false);
		saveGameMenu.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		saveGameMenu.setFocusPainted(false);

		saveGameMenu.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));

		saveGameMenu.setForeground(defaultColor);

		saveGameMenu.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				saveGameMenu.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				saveGameMenu.setForeground(defaultColor);
			}
		});

		saveGameMenu.addActionListener(aL);
		panel.add(saveGameMenu);

		options = new JButton("Options");
		options.setBounds(0, 350, 230, 40);

		// Button blends with background
		options.setOpaque(false);
		options.setContentAreaFilled(false);
		options.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		options.setFocusPainted(false);

		// Sets font type, mode, and size
		options.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		options.setFont(options.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		options.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		options.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				options.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				options.setForeground(defaultColor);
			}
		});

		options.addActionListener(aL);
		panel.add(options);

		controls = new JButton("Controls");
		controls.setBounds(0, 300, 235, 40);

		// Button blends with background
		controls.setOpaque(false);
		controls.setContentAreaFilled(false);
		controls.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		controls.setFocusPainted(false);

		// Sets font type, mode, and size
		controls.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		controls.setFont(controls.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		controls.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		controls.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				controls.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				controls.setForeground(defaultColor);
			}
		});

		controls.addActionListener(aL);
		panel.add(controls);

		quit = new JButton("Quit");
		quit.setBounds(0, 400, 180, 40);

		// Button blends with background
		quit.setOpaque(false);
		quit.setContentAreaFilled(false);
		quit.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		quit.setFocusPainted(false);

		// Sets font type, mode, and size
		quit.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));

		quit.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		quit.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				quit.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				quit.setForeground(defaultColor);
			}
		});

		quit.addActionListener(aL);
		panel.add(quit);

		userTitle = new JLabel("Current User: " + currentUserName);
		userTitle.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		userTitle.setBounds(WIDTH - ((currentUserName.length() * 11) + 300), 25, 200 + (currentUserName.length() * 11),
				25);
		userTitle.setForeground(Color.RED);
		userTitle.setBackground(Color.BLACK);
		userTitle.setOpaque(true);
		panel.add(userTitle);

		logOut = new JButton("Log Out");

		logOut.setBounds((WIDTH) - 200, 75, 150, 40);

		// Button blends with background
		logOut.setContentAreaFilled(false);
		logOut.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		logOut.setFocusPainted(false);

		// Sets font type, mode, and size
		logOut.setFont(new Font("Nasalization", Font.BOLD, 24));

		logOut.setForeground(Color.RED);
		logOut.setBackground(Color.BLACK);
		logOut.setOpaque(true);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		logOut.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				logOut.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				logOut.setForeground(Color.RED);
			}
		});

		logOut.addActionListener(aL);
		panel.add(logOut);

		// Refreashes title screen to show all buttons and title perfectly
		panel.repaint();
	}

	/**
	 * Draws up all the JButtons, Choice boxes, and textfields within the options
	 * menu when you pull it up.
	 */
	public void drawOptionsMenu() {
		back = new JButton("Back To Main Menu");
		back.setBounds(0, 325, 300, 40);

		// Button blends with background
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		back.setFocusPainted(false);

		// Sets font type, mode, and size
		back.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		back.setFont(back.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		back.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.RED);
			}
		});

		back.addActionListener(aL);
		panel.add(back);

		if (smoothFPS) {
			sFPS = new JButton("Smooth FPS: On");
		} else {
			sFPS = new JButton("Smooth FPS: Off");
		}

		sFPS.setBounds(0, 275, 300, 40);

		// Button blends with background
		sFPS.setOpaque(false);
		sFPS.setContentAreaFilled(false);
		sFPS.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		sFPS.setFocusPainted(false);

		// Sets font type, mode, and size
		sFPS.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		sFPS.setFont(sFPS.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		sFPS.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		sFPS.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				sFPS.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				sFPS.setForeground(Color.RED);
			}
		});

		sFPS.addActionListener(aL);
		panel.add(sFPS);

		if (mouseStatus) {
			mouse = new JButton("Mouse Status: On");
		} else {
			mouse = new JButton("Mouse Status: Off");
		}

		mouse.setBounds(300, 275, 250, 40);

		// Button blends with background
		mouse.setOpaque(false);
		mouse.setContentAreaFilled(false);
		mouse.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		mouse.setFocusPainted(false);

		// Sets font type, mode, and size
		mouse.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		mouse.setFont(mouse.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		mouse.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		mouse.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				mouse.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				mouse.setForeground(Color.RED);
			}
		});

		mouse.addActionListener(aL);
		panel.add(mouse);

		if (nonDefaultMap) {
			customMap = new JButton("Custom Map: On");
		} else {
			customMap = new JButton("Custom Map: Off");
		}

		customMap.setBounds(300, 325, 250, 40);

		// Button blends with background
		customMap.setOpaque(false);
		customMap.setContentAreaFilled(false);
		customMap.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		customMap.setFocusPainted(false);

		// Sets font type, mode, and size
		customMap.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		customMap.setFont(customMap.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		customMap.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		customMap.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				customMap.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				customMap.setForeground(Color.RED);
			}
		});

		customMap.addActionListener(aL);
		panel.add(customMap);

		resolutionTitle = new JLabel("Resolution:");
		resolutionTitle.setBounds(50, 25, 200, 10);
		resolutionTitle.setForeground(Color.GREEN);
		panel.add(resolutionTitle);

		// This Swing component allows a slide down menu with all the
		// options you add here to choose from.
		resolution.setBounds(50, 50, 200, 40);
		resolution.add("800 x 600 Low Res");
		resolution.add("800 x 600 High Res");
		resolution.add("1020 x 700 Low Res");
		resolution.add("1020 x 700 High Res");
		resolution.add("Fullscreen Low Res");
		resolution.add("Fullscreen High Res");
		resolution.select(resolutionChoice);
		panel.add(resolution);

		themeTitle = new JLabel("Themes:");
		themeTitle.setBounds(250, 100, 200, 10);
		themeTitle.setForeground(Color.GREEN);
		panel.add(themeTitle);

		theme.setBounds(175, 125, 200, 40);

		// Get the list of files within the directory with the given file path
		File folder = new File("resources/");
		File[] listOfFiles = folder.listFiles();

		// Run through all the names and add them
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// At the moment do nothing here
			} else if (listOfFiles[i].isDirectory()) {
				theme.add(listOfFiles[i].getName());
			}
		}

		try {
			theme.select(themeName.substring(1, themeName.length()));
		} catch (Exception e) {
			theme.select(0);
		}

		panel.add(theme);

		levelSizeTitle = new JLabel("Level Size (Survival only):");
		levelSizeTitle.setBounds(300, 25, 250, 15);
		levelSizeTitle.setForeground(Color.GREEN);
		panel.add(levelSizeTitle);

		levelSize.setBounds(300, 50, 200, 40);
		levelSize.add("10x10");
		levelSize.add("25x25");
		levelSize.add("50x50");
		levelSize.add("100x100");
		levelSize.add("250x250");
		levelSize.select(levelSizeChoice);
		panel.add(levelSize);

		modeTitle = new JLabel("Game Mode:");
		modeTitle.setBounds(50, 175, 250, 15);
		modeTitle.setForeground(Color.GREEN);
		panel.add(modeTitle);

		mode.setBounds(50, 200, 200, 40);
		mode.add("Peaceful");
		mode.add("I'm scared to die");
		mode.add("I can handle it!");
		mode.add("Bring it on!");
		mode.add("Death cannot touch me!");
		mode.select(modeChoice);
		panel.add(mode);

		musicTitle = new JLabel("Music Options:");
		musicTitle.setBounds(300, 175, 200, 15);
		musicTitle.setForeground(Color.GREEN);
		panel.add(musicTitle);

		music.setBounds(300, 200, 200, 40);
		music.add("Music Option 1");
		music.add("Harder Rock Option");
		music.add("Music Option 3");

		/*
		 * Music choice depends on theme or what the user chooses
		 */
		if (Display.musicTheme > 2) {
			music.select(2);
		} else {
			music.select(musicChoice);
		}

		panel.add(music);

		musicVolumeTitle = new JLabel("Music Volume:");
		musicVolumeTitle.setBounds(550, 200, 200, 10);
		musicVolumeTitle.setForeground(Color.GREEN);
		panel.add(musicVolumeTitle);

		musicVolume = new JSlider();
		musicVolume.setBounds(550, 220, 200, 40);
		musicVolume.setMaximum(100);
		musicVolume.setMinimum(0);
		musicVolume.setValue((int) musicVolumeLevel);
		musicVolume.setOpaque(true);
		musicVolume.setToolTipText("Music Volume Level: " + musicVolumeLevel);
		musicVolume.addChangeListener(change);
		panel.add(musicVolume);

		soundVolumeTitle = new JLabel("Sound Volume:");
		soundVolumeTitle.setBounds(550, 100, 200, 10);
		soundVolumeTitle.setForeground(Color.GREEN);
		panel.add(soundVolumeTitle);

		soundVolume = new JSlider();
		soundVolume.setBounds(550, 120, 200, 40);
		soundVolume.setMaximum(100);
		soundVolume.setMinimum(0);
		soundVolume.setValue((int) soundVolumeLevel);
		soundVolume.setOpaque(true);
		soundVolume.setToolTipText("Sound Volume Level: " + soundVolumeLevel);
		soundVolume.addChangeListener(change);
		panel.add(soundVolume);

		// Title for custom map text field
		newMapTitle = new JLabel("Custom Map:");
		newMapTitle.setBounds(WIDTH - 250, 25, 200, 25);
		newMapTitle.setForeground(Color.GREEN);
		panel.add(newMapTitle);

		// Textfield to load up a custom map if you want
		newMapName = new JTextField();
		newMapName.setBounds(WIDTH - 250, 50, 200, 25);
		newMapName.setText(startMap);
		newMapName.setEditable(true);
		panel.add(newMapName);

		panel.repaint();
	}

	/**
	 * Draws up a within the Join Server menu when you pull it up.
	 */
	public void drawJoinServerMenu() {
		ipAddressTitle = new JLabel("IP Address:");
		ipAddressTitle.setBounds(50, 150, 200, 25);
		ipAddressTitle.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		ipAddressTitle.setForeground(Color.GREEN);
		ipAddressTitle.setBackground(Color.BLACK);
		ipAddressTitle.setVisible(true);
		ipAddressTitle.setOpaque(true);
		panel.add(ipAddressTitle);

		ipAddress = new JTextField("");
		ipAddress.setBounds(50, 175, 200, 25);
		ipAddress.setEditable(true);
		panel.add(ipAddress);

		portTitle = new JLabel("Server Port:");
		portTitle.setBounds(500, 150, 200, 25);
		portTitle.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		portTitle.setForeground(Color.GREEN);
		portTitle.setBackground(Color.BLACK);
		portTitle.setVisible(true);
		portTitle.setOpaque(true);
		panel.add(portTitle);

		port = new JTextField("");
		port.setBounds(500, 175, 200, 25);
		port.setEditable(true);
		panel.add(port);

		backMult = new JButton("Back");
		backMult.setBounds(0, 400, 300, 40);

		// Button blends with background
		backMult.setOpaque(false);
		backMult.setContentAreaFilled(false);
		backMult.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		backMult.setFocusPainted(false);

		// Sets font type, mode, and size
		backMult.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		backMult.setFont(backMult.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		backMult.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		backMult.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				backMult.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				backMult.setForeground(Color.RED);
			}
		});

		backMult.addActionListener(aL);
		panel.add(backMult);

		join = new JButton("Join Game");
		join.setBounds(300, 300, 200, 40);

		// Button blends with background
		join.setOpaque(true);
		join.setContentAreaFilled(true);
		join.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		join.setFocusPainted(false);

		// Sets font type, mode, and size
		join.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		join.setFont(join.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		join.setForeground(Color.RED);
		join.setBackground(Color.BLACK);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		join.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				join.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				join.setForeground(Color.RED);
			}
		});

		join.addActionListener(aL);
		panel.add(join);

		panel.repaint();
	}

	/**
	 * Draws up a host, join server, and back button within the multiplayer menu
	 * when you pull it up.
	 */
	public void drawMultiplayerMenu() {

		host = new JButton("Host");
		host.setBounds(-85, 100, 300, 40);

		// Button blends with background
		host.setOpaque(false);
		host.setContentAreaFilled(false);
		host.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		host.setFocusPainted(false);

		// Sets font type, mode, and size
		host.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		host.setFont(host.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		host.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		host.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				host.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				host.setForeground(Color.RED);
			}
		});

		host.addActionListener(aL);
		panel.add(host);

		joinServer = new JButton("Join Server");
		joinServer.setBounds(-50, 150, 300, 40);

		// Button blends with background
		joinServer.setOpaque(false);
		joinServer.setContentAreaFilled(false);
		joinServer.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		joinServer.setFocusPainted(false);

		// Sets font type, mode, and size
		joinServer.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		joinServer.setFont(joinServer.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		joinServer.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		joinServer.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				joinServer.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				joinServer.setForeground(Color.RED);
			}
		});

		joinServer.addActionListener(aL);
		panel.add(joinServer);

		back = new JButton("Back To Main Menu");
		back.setBounds(0, 200, 300, 40);

		// Button blends with background
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		back.setFocusPainted(false);

		// Sets font type, mode, and size
		back.setFont(new Font("Nasalization", Font.BOLD, 24));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		back.setFont(back.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		back.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.RED);
			}
		});

		back.addActionListener(aL);
		panel.add(back);

		panel.repaint();
	}

	/**
	 * Draws the JTextArea that shows all the controls the game has to offer in the
	 * controls menu.
	 */
	public void drawControlsButtons() {
		/*
		 * A JTextArea is much better than a JTextField because this allows for Text to
		 * be on multiple lines and also wrap around if there is not enough room on one
		 * line. That is why I use it to hold all this text here. It just works.
		 */
		readMeText = new JTextArea();
		readMeText.setBounds(0, 0, WIDTH, 300);
		readMeText.setText("Foward = W, Backwards = S, Strafe Left = A," + " Strafe Right = D, Turn left = Left arrow,"
				+ " Turn right = Right arrow, Run = Shift, Crouch = C," + "Jump = Space, Show FPS = F,"
				+ " Look up = Up Arrow, Look down = Down arrow," + " Noclip = N, Super Speed = P,"
				+ " Reload = R, Fly mode = O, God Mode = G, Shoot = V," + " Pause = ESC, E = Use, Give all weapons = L,"
				+ " Unlimited Ammo = K. " + " If using the mouse, moving it any direction will"
				+ " move the camera, and clicking shoots. There will" + " eventually be a way to change"
				+ " the controls you use in game, but not yet...");
		readMeText.setEditable(false);
		readMeText.setLineWrap(true);
		readMeText.setWrapStyleWord(true);
		panel.add(readMeText);

		back = new JButton("Back To Main Menu");
		back.setBounds(0, 325, 300, 40);

		// Button blends with background
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		back.setFocusPainted(false);

		// Sets font type, mode, and size
		back.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		back.setFont(back.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		back.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.RED);
			}
		});

		back.addActionListener(aL);
		panel.add(back);

		panel.repaint();
	}

	/**
	 * Draws background of menu. Always draws it last so that it is behind all the
	 * buttons and text every time.
	 */
	public void drawBackground() {
		title = new JLabel();
		title.setBounds(0, 0, WIDTH, HEIGHT);

		try {
			File file = new File("resources" + themeName + "/textures/hud/title.png");

			// If file exists
			if (file.exists()) {
				titleImage = new ImageIcon("resources" + themeName + "/textures/hud/title.png");
			} else {
				// If file does not exist throw exception
				throw new Exception();
			}
		} catch (Exception e) {
			try {
				titleImage = new ImageIcon("resources/default/textures/hud/title.png");
			} catch (Exception ex) {

			}
		}

		title.setIcon(titleImage);

		// System.out.println(title.getIcon());
		panel.add(title, new Integer(0));

		// Reset panel after drawing the background
		panel.repaint();
	}

	/**
	 * Sets the elements on the logInScreen to being visible
	 */
	public void logInScreen() {
		userTitle = new JLabel("Users:");
		userTitle.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		userTitle.setBounds(WIDTH / 6, HEIGHT / 3, 200, 25);
		userTitle.setForeground(Color.RED);
		userTitle.setBackground(Color.BLACK);
		userTitle.setOpaque(true);
		panel.add(userTitle);

		newUserTitle = new JLabel("New User:");
		newUserTitle.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		newUserTitle.setBounds((int) (WIDTH / 1.6), HEIGHT / 3, 200, 25);
		newUserTitle.setForeground(Color.RED);
		newUserTitle.setBackground(Color.BLACK);
		newUserTitle.setOpaque(true);
		panel.add(newUserTitle);

		error = new JLabel("THAT USER ALREADY EXISTS! TRY ANOTHER!");
		error.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		error.setBounds(WIDTH / 2 - 260, HEIGHT - 250, 550, 25);
		error.setForeground(Color.GREEN);
		error.setBackground(Color.BLACK);
		error.setOpaque(true);
		error.setVisible(false);
		panel.add(error);

		// A new scanner object that is defaultly set to null
		Scanner sc = null;

		usernames = new ArrayList<String>();

		/*
		 * Try to read the file full of usernames and if not, state the error
		 */
		try {
			// Creates a Scanner that can read the file
			sc = new Scanner(new BufferedReader(new FileReader("users.txt")));

			String wholeFile = "";

			// Read all the lines of the file
			while (sc.hasNextLine()) {
				wholeFile += sc.nextLine();
			}

			String[] usernamesTemp = wholeFile.split(",");

			// Add all the usernames to the array list
			for (String s : usernamesTemp) {
				usernames.add(s);
			}

			sc.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		users = new Choice();

		// This Swing component allows a slide down menu with all the
		// options you add here to choose from.
		users.setBounds(WIDTH / 6, (HEIGHT / 3) + 50, 200, 40);

		// Add all the usernames to the drop down menu
		for (String s : usernames) {
			users.add(s);
		}

		// If usersChoice is out of the bounds of the users available
		if (users.getItemCount() < usersChoice - 1) {
			usersChoice = 0;
		}

		try {
			users.select(usersChoice);
		} catch (Exception e) {
			// If no users just forget selecting one
		}

		panel.add(users);

		// Textfield to add a new user to the game file
		newUserName = new JTextField("");
		newUserName.setBounds((int) (WIDTH / 1.6), (HEIGHT / 3) + 50, 200, 25);
		newUserName.setEditable(true);
		panel.add(newUserName);

		logIn = new JButton("Log In");

		logIn.setBounds((WIDTH / 2) - 62, HEIGHT - 200, 150, 40);

		// Button blends with background
		logIn.setContentAreaFilled(false);
		logIn.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		logIn.setFocusPainted(false);

		// Sets font type, mode, and size
		logIn.setFont(new Font("Nasalization", Font.BOLD, 24));

		logIn.setForeground(Color.RED);
		logIn.setBackground(Color.BLACK);
		logIn.setOpaque(true);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		logIn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				logIn.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				logIn.setForeground(Color.RED);
			}
		});

		logIn.addActionListener(aL);
		panel.add(logIn);

		removeUser = new JButton("Remove User");

		removeUser.setBounds((WIDTH / 2) - 85, HEIGHT - 500, 200, 40);

		// Button blends with background
		removeUser.setContentAreaFilled(false);
		removeUser.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		removeUser.setFocusPainted(false);

		// Sets font type, mode, and size
		removeUser.setFont(new Font("Nasalization", Font.BOLD, 24));

		removeUser.setForeground(Color.RED);
		removeUser.setBackground(new Color(0, 0, 0));
		removeUser.setOpaque(true);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		removeUser.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				removeUser.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				removeUser.setForeground(Color.RED);
			}
		});

		removeUser.addActionListener(aL);
		panel.add(removeUser);

		panel.repaint();
	}

	/**
	 * Used to delete a folder and all of its contents
	 * 
	 * @param file
	 */
	public void deleteFolder(File file) {
		/*
		 * Gets all contents of the folder/file (Which could be more folders) and
		 * recursivly runs through them all to delete all the contents of it. Mainly
		 * used for removing users from the game.
		 */
		File[] contents = file.listFiles();

		// Recursively run through the files to get rid of everything
		// in the folder
		if (contents != null) {
			for (File folder : contents) {
				deleteFolder(folder);
			}
		}

		// Delete parent file
		file.delete();
	}

	/**
	 * Sets up the saving and loading menu. Putting them both on the same page makes
	 * it easy to access these functions.
	 */
	public void saveLoadMenuSetup() {
		// Colors of buttons when mouse is not over them or when it is
		Color defaultColor = Color.RED;
		Color mouseColor = Color.GREEN;

		saveGame = new JButton("Save Game");
		saveGame.setBounds(0, 150, 270, 40);

		saveGame.setOpaque(false);
		saveGame.setContentAreaFilled(false);
		saveGame.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		saveGame.setFocusPainted(false);

		saveGame.setFont(new Font("Nasalization", Font.BOLD, 24));

		saveGame.setForeground(defaultColor);

		saveGame.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				saveGame.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				saveGame.setForeground(defaultColor);
			}
		});

		saveGame.addActionListener(aL);
		panel.add(saveGame);

		loadGame = new JButton("Load Game");
		loadGame.setBounds(0, 200, 270, 40);

		loadGame.setOpaque(false);
		loadGame.setContentAreaFilled(false);
		loadGame.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		loadGame.setFocusPainted(false);

		loadGame.setFont(new Font("Nasalization", Font.BOLD, 24));

		loadGame.setForeground(defaultColor);

		loadGame.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				loadGame.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				loadGame.setForeground(defaultColor);
			}
		});

		loadGame.addActionListener(aL);
		panel.add(loadGame);

		removeSave = new JButton("Remove a Save");
		removeSave.setBounds(0, 250, 315, 40);

		removeSave.setOpaque(false);
		removeSave.setContentAreaFilled(false);
		removeSave.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		removeSave.setFocusPainted(false);

		removeSave.setFont(new Font("Nasalization", Font.BOLD, 24));

		removeSave.setForeground(defaultColor);

		removeSave.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				removeSave.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				removeSave.setForeground(defaultColor);
			}
		});

		removeSave.addActionListener(aL);
		panel.add(removeSave);

		// Saved Games choice list bounds
		savedGames.setBounds((WIDTH / 2) - 100, (HEIGHT / 2) - 100, 200, 100);

		// Add all the saved file names to the list
		for (String s : saves) {
			savedGames.addItem(s);
		}

		try {
			savedGames.select(saveChoice);
		} catch (Exception e) {
			// If no saved games yet just don't have anything
		}

		savedGames.addItemListener(iL);
		panel.add(savedGames);

		String temp = "";

		try {
			temp = savedGames.getItem(saveChoice);
		} catch (Exception ex) {
			// If there is no saved games, just leave blank
		}

		// Textfield to add a new user to the game file
		saveTextfield = new JTextField(temp);
		saveTextfield.setBounds(WIDTH / 2 + 150, (HEIGHT / 2) - 100, 200, 25);
		saveTextfield.setEditable(true);
		panel.add(saveTextfield);

		availableGames = new JLabel("Saved Games");
		availableGames.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		availableGames.setBounds(WIDTH / 2 - 100, (HEIGHT / 2) - 150, 200, 25);
		availableGames.setForeground(Color.GREEN);
		availableGames.setBackground(Color.BLACK);
		availableGames.setOpaque(true);
		availableGames.setVisible(true);
		panel.add(availableGames);

		saveName = new JLabel("File Name: (Can add new name)");
		saveName.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 13));
		saveName.setBounds(WIDTH / 2 + 150, (HEIGHT / 2) - 150, 200, 25);
		saveName.setForeground(Color.GREEN);
		saveName.setBackground(Color.BLACK);
		saveName.setOpaque(true);
		saveName.setVisible(true);
		panel.add(saveName);

		back = new JButton("Back To Main Menu");
		back.setBounds(0, 325, 300, 40);

		// Button blends with background
		back.setOpaque(false);
		back.setContentAreaFilled(false);
		back.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		back.setFocusPainted(false);

		// Sets font type, mode, and size
		back.setFont(new Font("Nasalization", Font.BOLD, 18));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		back.setFont(back.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		back.setForeground(Color.RED);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		back.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.GREEN);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				back.setForeground(Color.RED);
			}
		});

		back.addActionListener(aL);
		panel.add(back);

		panel.repaint();
	}

	/**
	 * Called when the user attempts to quit the game. First this method checks to
	 * see whether the user is sure about leaving or not, and if he/she is, then the
	 * game saves the users settings in the settings file and exits the game.
	 */
	public void quitGame() {
		Random rand = new Random();

		int random = rand.nextInt(11);

		String exitString = "";

		// Set a random exit message to display
		switch (random) {
		case 0:
			exitString = "Quitting is for the weak! Are you weak?!";
			break;
		case 1:
			exitString = "The VILE are invading! Are you really going to let them win?!";
			break;
		case 2:
			exitString = "Do you really want to go back to having a life?";
			break;
		case 3:
			exitString = "Don't leave yet! There's a rocket launcher in that next room! Are you sure?";
			break;
		case 4:
			exitString = "Are you really going to leave all your comrades in the hands of the VILE?";
			break;
		case 5:
			exitString = "Do you know how hard it was to make this game?! You're really going to quit now?!";
			break;
		case 6:
			exitString = "That Brainomorph called you a dummy! You really going to let him say that?!";
			break;
		case 7:
			exitString = "Morty! I need your help to defeat the VILE! You really going to quit on your grandpa?!";
			break;
		case 8:
			exitString = "Rush is the greatest band ever! You have to agree or you can't leave.";
			break;
		case 9:
			exitString = "Leaving so soon? Don't worry.... I'll be waiting... mwah haha!";
			break;
		default:
			exitString = "Are you sure you want to quit?";
			break;
		}

		// Prompt user to say yes or no to removing the user
		int response = JOptionPane.showConfirmDialog(null, exitString, "Quit Game", JOptionPane.YES_NO_OPTION);

		// If no then return, if yes then continue
		if (response == JOptionPane.NO_OPTION) {
			return;
		}

		saveStats();

		System.exit(0);
	}

	/**
	 * Called when the settings file (that holds the games stats) needs to be saved.
	 */
	public void saveStats() {
		// The buffered writer that will write to a file
		BufferedWriter writeSettings;

		/*
		 * Save settings and stats into the settings text file
		 */
		try {
			writeSettings = new BufferedWriter(new FileWriter("Users/" + currentUserName + "/stats/settings.txt"));

			// Player and level stuff
			writeSettings.write(resolutionChoice + ":" + Player.maxKills + ":" + levelSizeChoice + ":" + startMap + ":"
					+ themeName + ":" + modeChoice + ":" + musicChoice + ":" + soundVolumeLevel + ":" + musicVolumeLevel
					+ ":" + smoothFPS + ":" + mouseStatus + ":" + nonDefaultMap);

			// For all the save names. Write them into the file
			for (String s : saves) {
				writeSettings.write(":" + s);
			}

			writeSettings.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Displays an error on the menu page with a custom message sent in through the
	 * parameters.
	 * 
	 * @param errorMessage
	 */
	public void displayError(String errorMessage) {
		// Initialize error
		error = new JLabel(errorMessage);
		error.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 24));
		error.setBounds(WIDTH / 2 - 260, HEIGHT - 100, 550, 25);
		error.setForeground(Color.GREEN);
		error.setBackground(Color.BLACK);
		error.setOpaque(true);
		error.setVisible(true);
		panel.add(error);

		// Start new thread to have flashing text
		t = new Thread(new Runnable() {
			private int counter = 0;

			@Override
			public void run() {
				while (true) {
					counter++;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (counter % 2 == 0) {
								error.setForeground(Color.GREEN);
								counter = 0;
							} else {
								error.setForeground(Color.WHITE);
							}
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		t.start();
		return;
	}
}
