package com.vile.launcher;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
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
import com.vile.entities.EnemyFire;
import com.vile.entities.Entity;
import com.vile.entities.Explosion;
import com.vile.entities.Item;
import com.vile.entities.Player;
import com.vile.entities.Weapon;
import com.vile.graphics.Render3D;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;
import com.vile.server.ServerClient;
import com.vile.server.ServerHost;

/**
 * @title FPSLauncher
 * @author Alex Byrd
 * @modified 11/24/2018
 * 
 *           Description: Starts when the application is launched. Includes all
 *           the possible options for a user playing the game. Starting a new
 *           single player game or joining or hosting a multi player game. Also
 *           holds a vast range of options in the options menu and has a quit
 *           and controls button as well. Also the ability to save and load game
 *           files at will.
 *
 */
public class FPSLauncher extends JFrame {

	// Allows for listening to if a key is pressed, used for the controls menu
	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				int keyCode = e.getKeyCode();

				// TODO Keep adding to

				/*
				 * Depending on the key pressed, say that it is no longer waiting for a key to
				 * bind, set the new keyCode for the game to listen for, reset the text in the
				 * controls menu of what that key is binded to now, and reset the buttons
				 * background to no longer being highlighted
				 */
				if (FPSLauncher.forwardWaiting) {
					FPSLauncher.forwardWaiting = false;
					Game.fowardKey = keyCode;
					FPSLauncher.forwardKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.forward.setBackground(Color.BLACK);
				} else if (backWaiting) {
					FPSLauncher.backWaiting = false;
					Game.backKey = keyCode;
					FPSLauncher.backKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.backButton.setBackground(Color.BLACK);
				} else if (leftWaiting) {
					FPSLauncher.leftWaiting = false;
					Game.leftKey = keyCode;
					FPSLauncher.leftKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.left.setBackground(Color.BLACK);
				} else if (rightWaiting) {
					FPSLauncher.rightWaiting = false;
					Game.rightKey = keyCode;
					FPSLauncher.rightKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.right.setBackground(Color.BLACK);
				} else if (turnLeftWaiting) {
					FPSLauncher.turnLeftWaiting = false;
					Game.turnLeftKey = keyCode;
					FPSLauncher.turnLeftKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.turnLeft.setBackground(Color.BLACK);
				} else if (turnRightWaiting) {
					FPSLauncher.turnRightWaiting = false;
					Game.turnRightKey = keyCode;
					FPSLauncher.turnRightKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.turnRight.setBackground(Color.BLACK);
				} else if (turnUpWaiting) {
					FPSLauncher.turnUpWaiting = false;
					Game.turnUpKey = keyCode;
					FPSLauncher.turnUpKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.turnUp.setBackground(Color.BLACK);
				} else if (turnDownWaiting) {
					FPSLauncher.turnDownWaiting = false;
					Game.turnDownKey = keyCode;
					FPSLauncher.turnDownKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.turnDown.setBackground(Color.BLACK);
				} else if (shootWaiting) {
					FPSLauncher.shootWaiting = false;
					Game.shootKey = keyCode;
					FPSLauncher.shootKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.shoot.setBackground(Color.BLACK);
				} else if (pauseWaiting) {
					FPSLauncher.pauseWaiting = false;
					Game.pauseKey = keyCode;
					FPSLauncher.pauseKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.pause.setBackground(Color.BLACK);
				} else if (runWaiting) {
					FPSLauncher.runWaiting = false;
					Game.runKey = keyCode;
					FPSLauncher.runKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.run.setBackground(Color.BLACK);
				} else if (crouchWaiting) {
					FPSLauncher.crouchWaiting = false;
					Game.crouchKey = keyCode;
					FPSLauncher.crouchKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.crouch.setBackground(Color.BLACK);
				} else if (jumpWaiting) {
					FPSLauncher.jumpWaiting = false;
					Game.jumpKey = keyCode;
					FPSLauncher.jumpKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.jump.setBackground(Color.BLACK);
				} else if (fpsShowWaiting) {
					FPSLauncher.fpsShowWaiting = false;
					Game.fpsShowKey = keyCode;
					FPSLauncher.fpsShowKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.fpsShow.setBackground(Color.BLACK);
				} else if (reloadingWaiting) {
					FPSLauncher.reloadingWaiting = false;
					Game.reloadingKey = keyCode;
					FPSLauncher.reloadingKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.reloading.setBackground(Color.BLACK);
				} else if (weapon1Waiting) {
					FPSLauncher.weapon1Waiting = false;
					Game.weaponSlot0Key = keyCode;
					FPSLauncher.weapon1Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon1.setBackground(Color.BLACK);
				} else if (weapon2Waiting) {
					FPSLauncher.weapon2Waiting = false;
					Game.weaponSlot1Key = keyCode;
					FPSLauncher.weapon2Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon2.setBackground(Color.BLACK);
				} else if (weapon3Waiting) {
					FPSLauncher.weapon3Waiting = false;
					Game.weaponSlot2Key = keyCode;
					FPSLauncher.weapon3Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon3.setBackground(Color.BLACK);
				} else if (weapon4Waiting) {
					FPSLauncher.weapon4Waiting = false;
					Game.weaponSlot3Key = keyCode;
					FPSLauncher.weapon4Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon4.setBackground(Color.BLACK);
				} else if (weapon5Waiting) {
					FPSLauncher.weapon5Waiting = false;
					Game.weaponSlot4Key = keyCode;
					FPSLauncher.weapon5Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon5.setBackground(Color.BLACK);
				} else if (weapon6Waiting) {
					FPSLauncher.weapon6Waiting = false;
					Game.weaponSlot5Key = keyCode;
					FPSLauncher.weapon6Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon6.setBackground(Color.BLACK);
				} else if (weapon7Waiting) {
					FPSLauncher.weapon7Waiting = false;
					Game.weaponSlot6Key = keyCode;
					FPSLauncher.weapon7Key.setText("" + keyCodeToString(keyCode));
					FPSLauncher.weapon7.setBackground(Color.BLACK);
				} else if (useWaiting) {
					FPSLauncher.useWaiting = false;
					Game.useKey = keyCode;
					FPSLauncher.useKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.use.setBackground(Color.BLACK);
				} else if (upgradeWeaponWaiting) {
					FPSLauncher.upgradeWeaponWaiting = false;
					Game.upgradeWeaponKey = keyCode;
					FPSLauncher.upgradeWeaponKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.upgradeWeapon.setBackground(Color.BLACK);
				} else if (recallFriendliesWaiting) {
					FPSLauncher.recallFriendliesWaiting = false;
					Game.recallFriendliesKey = keyCode;
					FPSLauncher.recallFriendliesKey.setText("" + keyCodeToString(keyCode));
					FPSLauncher.recallFriendlies.setBackground(Color.BLACK);
				}
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				// System.out.println("2test2");
			} else if (e.getID() == KeyEvent.KEY_TYPED) {
				// System.out.println("3test3");
			}

			return false;
		}
	}

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

	private static JButton survival;
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
	private static JButton join;
	private static JButton joinServer;
	private static JButton backMult;
	private static JButton hostServer;

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
	private static JLabel hostTitle;

	public static JTextField newMapName;
	private static JTextField newUserName;
	private static JTextField saveTextfield;
	private static JTextField ipAddress;
	private static JTextField port;

	// For all the controls, the buttons and the text describing them
	public static JLabel forwardKey;
	public static JLabel backKey;
	public static JLabel leftKey;
	public static JLabel rightKey;
	public static JLabel turnLeftKey;
	public static JLabel turnRightKey;
	public static JLabel turnUpKey;
	public static JLabel turnDownKey;
	public static JLabel shootKey;
	public static JLabel pauseKey;
	public static JLabel runKey;
	public static JLabel crouchKey;
	public static JLabel jumpKey;
	public static JLabel fpsShowKey;
	public static JLabel reloadingKey;
	public static JLabel weapon1Key;
	public static JLabel weapon2Key;
	public static JLabel weapon3Key;
	public static JLabel weapon4Key;
	public static JLabel weapon5Key;
	public static JLabel useKey;
	public static JLabel weapon6Key;
	public static JLabel weapon7Key;
	public static JLabel upgradeWeaponKey;
	public static JLabel recallFriendliesKey;

	// Buttons pressed to start the waiting for a new control
	public static JButton forward;
	public static JButton backButton;
	public static JButton left;
	public static JButton right;
	public static JButton turnLeft;
	public static JButton turnRight;
	public static JButton turnUp;
	public static JButton turnDown;
	public static JButton shoot;
	public static JButton pause;
	public static JButton run;
	public static JButton crouch;
	public static JButton jump;
	public static JButton fpsShow;
	public static JButton reloading;
	public static JButton weapon1;
	public static JButton weapon2;
	public static JButton weapon3;
	public static JButton weapon4;
	public static JButton weapon5;
	public static JButton use;
	public static JButton weapon6;
	public static JButton weapon7;
	public static JButton upgradeWeapon;
	public static JButton recallFriendlies;

	// Waiting on key to be pressed for control
	public static boolean forwardWaiting;
	public static boolean backWaiting;
	public static boolean leftWaiting;
	public static boolean rightWaiting;
	public static boolean turnLeftWaiting;
	public static boolean turnRightWaiting;
	public static boolean turnUpWaiting;
	public static boolean turnDownWaiting;
	public static boolean shootWaiting;
	public static boolean pauseWaiting;
	public static boolean runWaiting;
	public static boolean crouchWaiting;
	public static boolean jumpWaiting;
	public static boolean fpsShowWaiting;
	public static boolean reloadingWaiting;
	public static boolean weapon1Waiting;
	public static boolean weapon2Waiting;
	public static boolean weapon3Waiting;
	public static boolean weapon4Waiting;
	public static boolean weapon5Waiting;
	public static boolean useWaiting;
	public static boolean weapon6Waiting;
	public static boolean weapon7Waiting;
	public static boolean upgradeWeaponWaiting;
	public static boolean recallFriendliesWaiting;

	private static JTextField hostField;

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

	// Default colors of buttons
	private static Color defaultColor = Color.RED;
	private static Color mouseColor = Color.GREEN;

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

			// Only do this for the controls menu
			if (idNum == 2) {
				// If anything is clicked, it is no longer listening for the user
				// to type in a key to bind. It is canceled effectively. So turn off
				// the listener boolean and reset the button
				if (forwardWaiting) {
					forwardWaiting = false;
					forward.setBackground(Color.BLACK);
				} else if (backWaiting) {
					backWaiting = false;
					backButton.setBackground(Color.BLACK);
				} else if (leftWaiting) {
					leftWaiting = false;
					left.setBackground(Color.BLACK);
				} else if (rightWaiting) {
					rightWaiting = false;
					right.setBackground(Color.BLACK);
				} else if (turnLeftWaiting) {
					turnLeftWaiting = false;
					turnLeft.setBackground(Color.BLACK);
				} else if (turnRightWaiting) {
					turnRightWaiting = false;
					turnRight.setBackground(Color.BLACK);
				} else if (turnUpWaiting) {
					turnUpWaiting = false;
					turnUp.setBackground(Color.BLACK);
				} else if (turnDownWaiting) {
					turnDownWaiting = false;
					turnDown.setBackground(Color.BLACK);
				} else if (shootWaiting) {
					shootWaiting = false;
					shoot.setBackground(Color.BLACK);
				} else if (pauseWaiting) {
					pauseWaiting = false;
					pause.setBackground(Color.BLACK);
				} else if (runWaiting) {
					runWaiting = false;
					run.setBackground(Color.BLACK);
				} else if (crouchWaiting) {
					crouchWaiting = false;
					crouch.setBackground(Color.BLACK);
				} else if (jumpWaiting) {
					jumpWaiting = false;
					jump.setBackground(Color.BLACK);
				} else if (fpsShowWaiting) {
					fpsShowWaiting = false;
					fpsShow.setBackground(Color.BLACK);
				} else if (reloadingWaiting) {
					reloadingWaiting = false;
					reloading.setBackground(Color.BLACK);
				} else if (weapon1Waiting) {
					weapon1Waiting = false;
					weapon1.setBackground(Color.BLACK);
				} else if (weapon2Waiting) {
					weapon2Waiting = false;
					weapon2.setBackground(Color.BLACK);
				} else if (weapon3Waiting) {
					weapon3Waiting = false;
					weapon3.setBackground(Color.BLACK);
				} else if (weapon4Waiting) {
					weapon4Waiting = false;
					weapon4.setBackground(Color.BLACK);
				} else if (weapon5Waiting) {
					weapon5Waiting = false;
					weapon5.setBackground(Color.BLACK);
				} else if (useWaiting) {
					useWaiting = false;
					use.setBackground(Color.BLACK);
				} else if (weapon6Waiting) {
					weapon6Waiting = false;
					weapon6.setBackground(Color.BLACK);
				} else if (weapon7Waiting) {
					weapon7Waiting = false;
					weapon7.setBackground(Color.BLACK);
				} else if (upgradeWeaponWaiting) {
					upgradeWeaponWaiting = false;
					upgradeWeapon.setBackground(Color.BLACK);
				} else if (recallFriendliesWaiting) {
					recallFriendliesWaiting = false;
					recallFriendlies.setBackground(Color.BLACK);
				}

				panel.repaint();
			}

			// If Survival mode is pressed then close the main menu
			// music, dispose of the menu, and start new game
			if (e.getSource() == survival) {

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
				Display.itemsRespawn = false;
				Display.gameType = 2;

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
				Display.itemsRespawn = false;
				Display.gameType = 2;

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
			if (e.getSource() == join) {

				dispose();
				new JoinServer();

			}

			// Starts new Host server menu
			if (e.getSource() == host) {
				dispose();
				new Host();
			}
			/*
			 * Does everything necessary to join a server and start a game
			 */
			if (e.getSource() == joinServer) {
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

				new ServerClient(ipAddress.getText() + ":" + port.getText());
			}
			/*
			 * Does everything necessary to start a server and start a game
			 */
			if (e.getSource() == hostServer) {
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

				// Starts new server with a given port number
				new ServerHost(hostField.getText());
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

				saveStats();

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
							+ Player.kills + ":" + themeName);

					rewrite.newLine();

					// Player and level stuff
					rewrite.write(Player.health + ":" + Player.maxHealth + ":" + Player.armor + ":" + Player.x + ":"
							+ Player.y + ":" + Player.z + ":" + Player.rotation + ":" + Player.maxHeight + ":"
							+ Player.hasRedKey + ":" + Player.hasBlueKey + ":" + Player.hasGreenKey + ":"
							+ Player.hasYellowKey + ":" + Player.upRotate + ":" + Player.extraHeight + ":"
							+ Player.resurrections + ":" + Player.environProtectionTime + ":" + Player.immortality + ":"
							+ Player.vision + ":" + Player.invisibility + ":" + Player.weaponEquipped + ":"
							+ Player.godModeOn + ":" + Player.noClipOn + ":" + Player.flyOn + ":"
							+ Player.speedMultiplier + ":" + Player.unlimitedAmmoOn + ":" + Player.upgradePoints + ":"
							+ Level.width + ":" + Level.height + ":" + Game.mapNum + ":" + Game.mapAudio + ":"
							+ Game.mapFloor + ":" + Game.mapCeiling + ":" + Render3D.ceilingDefaultHeight + ":"
							+ Level.renderDistance + ":" + Game.mapName + ",");

					// Weapons
					for (int i = 0; i < Player.weapons.length; i++) {
						Weapon w = Player.weapons[i];
						int size = w.cartridges.size();

						rewrite.write(w.weaponID + ":" + w.canBeEquipped + ":" + w.dualWield + ":" + w.ammo + ":"
								+ w.damage + ":" + w.baseDamage + ":" + w.criticalHitChances + ":"
								+ w.upgradePointsNeeded);

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

					for (int i = 0; i < Game.entities.size(); i++) {
						Entity en = Game.entities.get(i);
						rewrite.write(en.health + ":" + en.xPos + ":" + en.yPos + ":" + en.zPos + ":" + en.ID + ":"
								+ en.itemActivationID + ":" + en.maxHeight + ":" + en.newTarget + ":" + en.targetX + ":"
								+ en.targetY + ":" + en.targetZ + ":" + en.activated + ":" + en.rotation + ":"
								+ en.isAttacking + ":" + en.isFiring + ":" + en.isABoss + ":" + en.xEffects + ":"
								+ en.yEffects + ":" + en.zEffects + ":" + en.tick + ":" + en.tickRound + ":"
								+ en.tickAmount + ":" + en.isFriendly + ";");
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
							Game.fowardKey = Integer.parseInt(elements[12]);
							Game.backKey = Integer.parseInt(elements[13]);
							Game.leftKey = Integer.parseInt(elements[14]);
							Game.rightKey = Integer.parseInt(elements[15]);
							Game.turnLeftKey = Integer.parseInt(elements[16]);
							Game.turnRightKey = Integer.parseInt(elements[17]);
							Game.turnUpKey = Integer.parseInt(elements[18]);
							Game.turnDownKey = Integer.parseInt(elements[19]);
							Game.shootKey = Integer.parseInt(elements[20]);
							Game.pauseKey = Integer.parseInt(elements[21]);
							Game.runKey = Integer.parseInt(elements[22]);
							Game.crouchKey = Integer.parseInt(elements[23]);
							Game.jumpKey = Integer.parseInt(elements[24]);
							Game.fpsShowKey = Integer.parseInt(elements[25]);
							Game.reloadingKey = Integer.parseInt(elements[26]);
							Game.weaponSlot0Key = Integer.parseInt(elements[27]);
							Game.weaponSlot1Key = Integer.parseInt(elements[28]);
							Game.weaponSlot2Key = Integer.parseInt(elements[29]);
							Game.weaponSlot3Key = Integer.parseInt(elements[30]);
							Game.weaponSlot4Key = Integer.parseInt(elements[31]);
							Game.useKey = Integer.parseInt(elements[32]);
							Game.weaponSlot5Key = Integer.parseInt(elements[33]);
							Game.weaponSlot6Key = Integer.parseInt(elements[34]);
							Game.upgradeWeaponKey = Integer.parseInt(elements[35]);
							Game.recallFriendliesKey = Integer.parseInt(elements[36]);

							// Everything else in the file will be the saved game
							// names this user has
							for (int i = 37; i < elements.length; i++) {
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

							// Reset all the keys so they aren't set to what the last user had
							Game.fowardKey = KeyEvent.VK_W;
							Game.backKey = KeyEvent.VK_S;
							Game.leftKey = KeyEvent.VK_A;
							Game.rightKey = KeyEvent.VK_D;
							Game.turnLeftKey = KeyEvent.VK_LEFT;
							Game.turnRightKey = KeyEvent.VK_RIGHT;
							Game.turnUpKey = KeyEvent.VK_UP;
							Game.turnDownKey = KeyEvent.VK_DOWN;
							Game.shootKey = KeyEvent.VK_V;
							Game.pauseKey = KeyEvent.VK_ESCAPE;
							Game.runKey = KeyEvent.VK_SHIFT;
							Game.crouchKey = KeyEvent.VK_C;
							Game.jumpKey = KeyEvent.VK_SPACE;
							Game.fpsShowKey = KeyEvent.VK_F;
							Game.reloadingKey = KeyEvent.VK_R;
							Game.weaponSlot0Key = KeyEvent.VK_1;
							Game.weaponSlot1Key = KeyEvent.VK_2;
							Game.weaponSlot2Key = KeyEvent.VK_3;
							Game.weaponSlot3Key = KeyEvent.VK_4;
							Game.weaponSlot4Key = KeyEvent.VK_5;
							Game.useKey = KeyEvent.VK_E;
							Game.weaponSlot5Key = KeyEvent.VK_6;
							Game.weaponSlot6Key = KeyEvent.VK_7;
							Game.upgradeWeaponKey = KeyEvent.VK_U;
							Game.recallFriendliesKey = KeyEvent.VK_Q;

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

			// TODO All of the control changing

			// Activates listener for forward key
			if (e.getSource() == forward) {
				forwardWaiting = true;
				forward.setBackground(Color.WHITE);
			}

			// Activates listener for back key
			if (e.getSource() == backButton) {
				backWaiting = true;
				backButton.setBackground(Color.WHITE);
			}

			// Activates listener for left key
			if (e.getSource() == left) {
				leftWaiting = true;
				left.setBackground(Color.WHITE);
			}

			// Activates listener for right key
			if (e.getSource() == right) {
				rightWaiting = true;
				right.setBackground(Color.WHITE);
			}

			// Activates listener for turnLeft key
			if (e.getSource() == turnLeft) {
				turnLeftWaiting = true;
				turnLeft.setBackground(Color.WHITE);
			}

			// Activates listener for turnRight key
			if (e.getSource() == turnRight) {
				turnRightWaiting = true;
				turnRight.setBackground(Color.WHITE);
			}

			// Activates listener for turnUp key
			if (e.getSource() == turnUp) {
				turnUpWaiting = true;
				turnUp.setBackground(Color.WHITE);
			}

			// Activates listener for turnDown key
			if (e.getSource() == turnDown) {
				turnDownWaiting = true;
				turnDown.setBackground(Color.WHITE);
			}

			// Activates listener for shoot key
			if (e.getSource() == shoot) {
				shootWaiting = true;
				shoot.setBackground(Color.WHITE);
			}

			// Activates listener for pause key
			if (e.getSource() == pause) {
				pauseWaiting = true;
				pause.setBackground(Color.WHITE);
			}

			// Activates listener for run key
			if (e.getSource() == run) {
				runWaiting = true;
				run.setBackground(Color.WHITE);
			}

			// Activates listener for crouch key
			if (e.getSource() == crouch) {
				crouchWaiting = true;
				crouch.setBackground(Color.WHITE);
			}

			// Activates listener for jump key
			if (e.getSource() == jump) {
				jumpWaiting = true;
				jump.setBackground(Color.WHITE);
			}

			// Activates listener for fpsShow key
			if (e.getSource() == fpsShow) {
				fpsShowWaiting = true;
				fpsShow.setBackground(Color.WHITE);
			}

			// Activates listener for reloading key
			if (e.getSource() == reloading) {
				reloadingWaiting = true;
				reloading.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 1
			if (e.getSource() == weapon1) {
				weapon1Waiting = true;
				weapon1.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 2
			if (e.getSource() == weapon2) {
				weapon2Waiting = true;
				weapon2.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 3
			if (e.getSource() == weapon3) {
				weapon3Waiting = true;
				weapon3.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 4
			if (e.getSource() == weapon4) {
				weapon4Waiting = true;
				weapon4.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 5
			if (e.getSource() == weapon5) {
				weapon5Waiting = true;
				weapon5.setBackground(Color.WHITE);
			}

			// Activates listener for use key
			if (e.getSource() == use) {
				useWaiting = true;
				use.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 6
			if (e.getSource() == weapon6) {
				weapon6Waiting = true;
				weapon6.setBackground(Color.WHITE);
			}

			// Activates listener for weapon 7
			if (e.getSource() == weapon7) {
				weapon7Waiting = true;
				weapon7.setBackground(Color.WHITE);
			}

			// Activates listener for upgradeWeapon key
			if (e.getSource() == upgradeWeapon) {
				upgradeWeaponWaiting = true;
				upgradeWeapon.setBackground(Color.WHITE);
			}

			// Activates listener for recallFriendlies key
			if (e.getSource() == recallFriendlies) {
				recallFriendliesWaiting = true;
				recallFriendlies.setBackground(Color.WHITE);
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

		// controlsHandler = new ControlsMenuListener();
		// panel.addKeyListener(controlsHandler);

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

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

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
		survival = new JButton("Survival Mode");
		survival.setBounds(0, 200, 275, 40);
		setUpMajorButton(survival, 24);
		panel.add(survival);

		multiplayer = new JButton("Multiplayer");
		multiplayer.setBounds(0, 250, 300, 40);
		setUpMajorButton(multiplayer, 24);
		panel.add(multiplayer);

		playGame = new JButton("New Game");
		playGame.setBounds(0, 100, 225, 40);
		setUpMajorButton(playGame, 24);
		panel.add(playGame);

		returnToGame = new JButton("Return to game");
		returnToGame.setBounds(500, 240, 300, 50);
		setUpMajorButton(returnToGame, 24);

		// If game is paused, show that you can return to the game
		if (Display.pauseGame) {
			panel.add(returnToGame);
		}

		saveGameMenu = new JButton("Save/Load Game");
		saveGameMenu.setBounds(0, 150, 250, 40);
		setUpMajorButton(saveGameMenu, 24);
		panel.add(saveGameMenu);

		options = new JButton("Options");
		options.setBounds(0, 350, 250, 40);
		setUpMajorButton(options, 24);
		panel.add(options);

		controls = new JButton("Controls");
		controls.setBounds(0, 300, 275, 40);
		setUpMajorButton(controls, 24);
		panel.add(controls);

		// Button blends with background
		quit = new JButton("Quit");
		quit.setBounds(0, 400, 225, 40);
		setUpMajorButton(quit, 24);
		panel.add(quit);

		userTitle = new JLabel("Current User: " + currentUserName);
		userTitle.setBounds(WIDTH - ((currentUserName.length() * 11) + 300), 25, 200 + (currentUserName.length() * 11),
				25);
		setUpDecoratedLabel(userTitle, 24);
		panel.add(userTitle);

		logOut = new JButton("Log Out");
		logOut.setBounds((WIDTH) - 200, 75, 150, 40);
		setUpMajorButton(logOut, 24);
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
		setUpOptionButton(back, 18);
		panel.add(back);

		if (smoothFPS) {
			sFPS = new JButton("Smooth FPS: On");
		} else {
			sFPS = new JButton("Smooth FPS: Off");
		}

		sFPS.setBounds(0, 275, 300, 40);
		setUpOptionButton(sFPS, 18);
		panel.add(sFPS);

		if (mouseStatus) {
			mouse = new JButton("Mouse Status: On");
		} else {
			mouse = new JButton("Mouse Status: Off");
		}

		mouse.setBounds(300, 275, 250, 40);
		setUpOptionButton(mouse, 18);
		panel.add(mouse);

		if (nonDefaultMap) {
			customMap = new JButton("Custom Map: On");
		} else {
			customMap = new JButton("Custom Map: Off");
		}

		customMap.setBounds(300, 325, 250, 40);
		setUpOptionButton(customMap, 18);
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
	 * Draws up all the JButtons and Text fields within the Host menu when you pull
	 * it up.
	 */
	public void drawHostMenu() {
		hostTitle = new JLabel("Port Number:");
		hostTitle.setBounds(50, 150, 200, 25);
		setUpDecoratedLabel(hostTitle, 18);
		panel.add(hostTitle);

		hostField = new JTextField("");
		hostField.setBounds(50, 175, 200, 25);
		hostField.setEditable(true);
		panel.add(hostField);

		backMult = new JButton("Back");
		backMult.setBounds(0, 400, 300, 40);
		setUpOptionButton(backMult, 18);
		panel.add(backMult);

		hostServer = new JButton("Host Game");
		hostServer.setBounds(300, 300, 200, 40);
		setUpMajorButton(hostServer, 24);
		panel.add(hostServer);

		panel.repaint();
	}

	/**
	 * Draws up a within the Join Server menu when you pull it up.
	 */
	public void drawJoinServerMenu() {
		ipAddressTitle = new JLabel("IP Address:");
		ipAddressTitle.setBounds(50, 150, 200, 25);
		setUpDecoratedLabel(ipAddressTitle, 18);
		panel.add(ipAddressTitle);

		ipAddress = new JTextField("");
		ipAddress.setBounds(50, 175, 200, 25);
		ipAddress.setEditable(true);
		panel.add(ipAddress);

		portTitle = new JLabel("Server Port:");
		portTitle.setBounds(500, 150, 200, 25);
		setUpDecoratedLabel(portTitle, 18);
		panel.add(portTitle);

		port = new JTextField("");
		port.setBounds(500, 175, 200, 25);
		port.setEditable(true);
		panel.add(port);

		backMult = new JButton("Back");
		backMult.setBounds(0, 400, 300, 40);
		setUpOptionButton(backMult, 18);
		panel.add(backMult);

		joinServer = new JButton("Join Game");
		joinServer.setBounds(300, 300, 200, 40);
		setUpMajorButton(joinServer, 24);
		panel.add(joinServer);

		panel.repaint();
	}

	/**
	 * Draws up a host, join server, and back button within the multiplayer menu
	 * when you pull it up.
	 */
	public void drawMultiplayerMenu() {

		host = new JButton("Host");
		host.setBounds(-85, 100, 300, 40);
		setUpOptionButton(host, 24);
		panel.add(host);

		join = new JButton("Join Server");
		join.setBounds(-50, 150, 300, 40);
		setUpOptionButton(join, 24);
		panel.add(join);

		back = new JButton("Back To Main Menu");
		back.setBounds(0, 200, 300, 40);
		setUpOptionButton(back, 18);
		panel.add(back);

		panel.repaint();
	}

	/**
	 * Draws up the controls menu. You click on a button which activates a certain
	 * control field, and an input handler will handle the key pressed in order to
	 * make that the new control.
	 */
	public void drawControlsButtons() {
		/*
		 * A JTextArea is much better than a JTextField because this allows for Text to
		 * be on multiple lines and also wrap around if there is not enough room on one
		 * line. That is why I use it to hold all this text here. It just works.
		 */
		/*
		 * readMeText = new JTextArea(); readMeText.setBounds(0, 0, WIDTH, 300);
		 * readMeText.setText("Foward = W, Backwards = S, Strafe Left = A," +
		 * " Strafe Right = D, Turn left = Left arrow," +
		 * " Turn right = Right arrow, Run = Shift, Crouch = C," +
		 * "Jump = Space, Show FPS = F," +
		 * " Look up = Up Arrow, Look down = Down arrow," +
		 * " Noclip = N, Super Speed = P," +
		 * " Reload = R, Fly mode = O, God Mode = G, Shoot = V," +
		 * " Pause = ESC, E = Use, Give all weapons = L," + " Unlimited Ammo = K. " +
		 * " If using the mouse, moving it any direction will" +
		 * " move the camera, and clicking shoots. There will" +
		 * " eventually be a way to change" +
		 * " the controls you use in game, but not yet...");
		 * readMeText.setEditable(false); readMeText.setLineWrap(true);
		 * readMeText.setWrapStyleWord(true); panel.add(readMeText);
		 */

		// TODO keep working on

		// Buttons to be pressed on to wait for key press
		forward = new JButton("Move Forward");
		forward.setBounds(25, 25, 200, 30);
		setUpMajorButton(forward, 18);
		panel.add(forward);

		backButton = new JButton("Move Backward");
		backButton.setBounds(25, 65, 200, 30);
		setUpMajorButton(backButton, 18);
		panel.add(backButton);

		left = new JButton("Strafe Left");
		left.setBounds(25, 105, 200, 30);
		setUpMajorButton(left, 18);
		panel.add(left);

		right = new JButton("Strafe Right");
		right.setBounds(25, 145, 200, 30);
		setUpMajorButton(right, 18);
		panel.add(right);

		turnLeft = new JButton("Turn Left");
		turnLeft.setBounds(25, 185, 200, 30);
		setUpMajorButton(turnLeft, 18);
		panel.add(turnLeft);

		turnRight = new JButton("Turn Right");
		turnRight.setBounds(25, 225, 200, 30);
		setUpMajorButton(turnRight, 18);
		panel.add(turnRight);

		turnUp = new JButton("Look up");
		turnUp.setBounds(25, 265, 200, 30);
		setUpMajorButton(turnUp, 18);
		panel.add(turnUp);

		turnDown = new JButton("Look Down");
		turnDown.setBounds(25, 305, 200, 30);
		setUpMajorButton(turnDown, 18);
		panel.add(turnDown);

		shoot = new JButton("Fire Weapon");
		shoot.setBounds(25, 345, 200, 30);
		setUpMajorButton(shoot, 18);
		panel.add(shoot);

		pause = new JButton("Pause Game");
		pause.setBounds(25, 385, 200, 30);
		setUpMajorButton(pause, 18);
		panel.add(pause);

		run = new JButton("Sprint");
		run.setBounds(25, 425, 200, 30);
		setUpMajorButton(run, 18);
		panel.add(run);

		crouch = new JButton("Crouch");
		crouch.setBounds(25, 465, 200, 30);
		setUpMajorButton(crouch, 18);
		panel.add(crouch);

		jump = new JButton("Jump");
		jump.setBounds(400, 25, 200, 30);
		setUpMajorButton(jump, 18);
		panel.add(jump);

		fpsShow = new JButton("Show FPS");
		fpsShow.setBounds(400, 65, 200, 30);
		setUpMajorButton(fpsShow, 18);
		panel.add(fpsShow);

		reloading = new JButton("Reload");
		reloading.setBounds(400, 105, 200, 30);
		setUpMajorButton(reloading, 18);
		panel.add(reloading);

		upgradeWeapon = new JButton("Upgrade Weapon");
		upgradeWeapon.setBounds(400, 145, 200, 30);
		setUpMajorButton(upgradeWeapon, 18);
		panel.add(upgradeWeapon);

		use = new JButton("Use/Activate");
		use.setBounds(400, 185, 200, 30);
		setUpMajorButton(use, 18);
		panel.add(use);

		recallFriendlies = new JButton("Recall Friendlies");
		recallFriendlies.setBounds(400, 225, 200, 30);
		setUpMajorButton(recallFriendlies, 18);
		panel.add(recallFriendlies);

		weapon1 = new JButton("Weapon 1");
		weapon1.setBounds(400, 265, 200, 30);
		setUpMajorButton(weapon1, 18);
		panel.add(weapon1);

		weapon2 = new JButton("Weapon 2");
		weapon2.setBounds(400, 305, 200, 30);
		setUpMajorButton(weapon2, 18);
		panel.add(weapon2);

		weapon3 = new JButton("Weapon 3");
		weapon3.setBounds(400, 345, 200, 30);
		setUpMajorButton(weapon3, 18);
		panel.add(weapon3);

		weapon4 = new JButton("Weapon 4");
		weapon4.setBounds(400, 385, 200, 30);
		setUpMajorButton(weapon4, 18);
		panel.add(weapon4);

		weapon5 = new JButton("Weapon 5");
		weapon5.setBounds(400, 425, 200, 30);
		setUpMajorButton(weapon5, 18);
		panel.add(weapon5);

		weapon6 = new JButton("Weapon 6");
		weapon6.setBounds(400, 465, 200, 30);
		setUpMajorButton(weapon6, 18);
		panel.add(weapon6);

		weapon7 = new JButton("Weapon 7");
		weapon7.setBounds(400, 505, 200, 30);
		setUpMajorButton(weapon7, 18);
		panel.add(weapon7);

		/*
		 * public static JButton godMode; public static JButton restock; public static
		 * JButton unlimAmmo;
		 */

		forwardKey = new JLabel("" + keyCodeToString(Game.fowardKey));
		forwardKey.setBounds(250, 25, 100, 30);
		setUpDecoratedLabel(forwardKey, 24);
		panel.add(forwardKey);

		backKey = new JLabel("" + keyCodeToString(Game.backKey));
		backKey.setBounds(250, 65, 100, 30);
		setUpDecoratedLabel(backKey, 24);
		panel.add(backKey);

		leftKey = new JLabel("" + keyCodeToString(Game.leftKey));
		leftKey.setBounds(250, 105, 100, 30);
		setUpDecoratedLabel(leftKey, 24);
		panel.add(leftKey);

		rightKey = new JLabel("" + keyCodeToString(Game.rightKey));
		rightKey.setBounds(250, 145, 100, 30);
		setUpDecoratedLabel(rightKey, 24);
		panel.add(rightKey);

		turnLeftKey = new JLabel("" + keyCodeToString(Game.turnLeftKey));
		turnLeftKey.setBounds(250, 185, 100, 30);
		setUpDecoratedLabel(turnLeftKey, 24);
		panel.add(turnLeftKey);

		turnRightKey = new JLabel("" + keyCodeToString(Game.turnRightKey));
		turnRightKey.setBounds(250, 225, 100, 30);
		setUpDecoratedLabel(turnRightKey, 24);
		panel.add(turnRightKey);

		turnUpKey = new JLabel("" + keyCodeToString(Game.turnUpKey));
		turnUpKey.setBounds(250, 265, 100, 30);
		setUpDecoratedLabel(turnUpKey, 24);
		panel.add(turnUpKey);

		turnDownKey = new JLabel("" + keyCodeToString(Game.turnDownKey));
		turnDownKey.setBounds(250, 305, 100, 30);
		setUpDecoratedLabel(turnDownKey, 24);
		panel.add(turnDownKey);

		shootKey = new JLabel("" + keyCodeToString(Game.shootKey));
		shootKey.setBounds(250, 345, 100, 30);
		setUpDecoratedLabel(shootKey, 24);
		panel.add(shootKey);

		pauseKey = new JLabel("" + keyCodeToString(Game.pauseKey));
		pauseKey.setBounds(250, 385, 100, 30);
		setUpDecoratedLabel(pauseKey, 24);
		panel.add(pauseKey);

		runKey = new JLabel("" + keyCodeToString(Game.runKey));
		runKey.setBounds(250, 425, 100, 30);
		setUpDecoratedLabel(runKey, 24);
		panel.add(runKey);

		crouchKey = new JLabel("" + keyCodeToString(Game.crouchKey));
		crouchKey.setBounds(250, 465, 100, 30);
		setUpDecoratedLabel(crouchKey, 24);
		panel.add(crouchKey);

		jumpKey = new JLabel("" + keyCodeToString(Game.jumpKey));
		jumpKey.setBounds(625, 25, 100, 30);
		setUpDecoratedLabel(jumpKey, 24);
		panel.add(jumpKey);

		fpsShowKey = new JLabel("" + keyCodeToString(Game.fpsShowKey));
		fpsShowKey.setBounds(625, 65, 100, 30);
		setUpDecoratedLabel(fpsShowKey, 24);
		panel.add(fpsShowKey);

		reloadingKey = new JLabel("" + keyCodeToString(Game.reloadingKey));
		reloadingKey.setBounds(625, 105, 100, 30);
		setUpDecoratedLabel(reloadingKey, 24);
		panel.add(reloadingKey);

		upgradeWeaponKey = new JLabel("" + keyCodeToString(Game.upgradeWeaponKey));
		upgradeWeaponKey.setBounds(625, 145, 100, 30);
		setUpDecoratedLabel(upgradeWeaponKey, 24);
		panel.add(upgradeWeaponKey);

		useKey = new JLabel("" + keyCodeToString(Game.useKey));
		useKey.setBounds(625, 185, 100, 30);
		setUpDecoratedLabel(useKey, 24);
		panel.add(useKey);

		recallFriendliesKey = new JLabel("" + keyCodeToString(Game.recallFriendliesKey));
		recallFriendliesKey.setBounds(625, 225, 100, 30);
		setUpDecoratedLabel(recallFriendliesKey, 24);
		panel.add(recallFriendliesKey);

		weapon1Key = new JLabel("" + keyCodeToString(Game.weaponSlot0Key));
		weapon1Key.setBounds(625, 265, 100, 30);
		setUpDecoratedLabel(weapon1Key, 24);
		panel.add(weapon1Key);

		weapon2Key = new JLabel("" + keyCodeToString(Game.weaponSlot1Key));
		weapon2Key.setBounds(625, 305, 100, 30);
		setUpDecoratedLabel(weapon2Key, 24);
		panel.add(weapon2Key);

		weapon3Key = new JLabel("" + keyCodeToString(Game.weaponSlot2Key));
		weapon3Key.setBounds(625, 345, 100, 30);
		setUpDecoratedLabel(weapon3Key, 24);
		panel.add(weapon3Key);

		weapon4Key = new JLabel("" + keyCodeToString(Game.weaponSlot3Key));
		weapon4Key.setBounds(625, 385, 100, 30);
		setUpDecoratedLabel(weapon4Key, 24);
		panel.add(weapon4Key);

		weapon5Key = new JLabel("" + keyCodeToString(Game.weaponSlot4Key));
		weapon5Key.setBounds(625, 425, 100, 30);
		setUpDecoratedLabel(weapon5Key, 24);
		panel.add(weapon5Key);

		weapon6Key = new JLabel("" + keyCodeToString(Game.weaponSlot5Key));
		weapon6Key.setBounds(625, 465, 100, 30);
		setUpDecoratedLabel(weapon6Key, 24);
		panel.add(weapon6Key);

		weapon7Key = new JLabel("" + keyCodeToString(Game.weaponSlot6Key));
		weapon7Key.setBounds(625, 505, 100, 30);
		setUpDecoratedLabel(weapon7Key, 24);
		panel.add(weapon7Key);

		back = new JButton("Back To Main Menu");
		back.setBounds(25, 525, 225, 40);
		setUpMajorButton(back, 20);
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
		userTitle.setBounds(WIDTH / 6, HEIGHT / 3, 200, 25);
		setUpDecoratedLabel(userTitle, 24);
		panel.add(userTitle);

		newUserTitle = new JLabel("New User:");
		newUserTitle.setBounds((int) (WIDTH / 1.6), HEIGHT / 3, 200, 25);
		setUpDecoratedLabel(newUserTitle, 24);
		panel.add(newUserTitle);

		error = new JLabel("THAT USER ALREADY EXISTS! TRY ANOTHER!");
		error.setBounds(WIDTH / 2 - 260, HEIGHT - 250, 550, 25);
		setUpDecoratedLabel(error, 24);
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
		setUpMajorButton(logIn, 24);
		panel.add(logIn);

		removeUser = new JButton("Remove User");
		removeUser.setBounds((WIDTH / 2) - 85, HEIGHT - 500, 200, 40);
		setUpMajorButton(removeUser, 24);
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

		saveGame = new JButton("Save Game");
		saveGame.setBounds(0, 150, 270, 40);
		setUpOptionButton(saveGame, 24);
		panel.add(saveGame);

		loadGame = new JButton("Load Game");
		loadGame.setBounds(0, 200, 270, 40);
		setUpOptionButton(loadGame, 24);
		panel.add(loadGame);

		removeSave = new JButton("Remove a Save");
		removeSave.setBounds(0, 250, 315, 40);
		setUpOptionButton(removeSave, 24);
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
		availableGames.setBounds(WIDTH / 2 - 100, (HEIGHT / 2) - 150, 200, 25);
		setUpDecoratedLabel(availableGames, 24);
		panel.add(availableGames);

		saveName = new JLabel("File Name: (Can add new name)");
		setUpDecoratedLabel(saveName, 13);
		saveName.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, 13));
		saveName.setBounds(WIDTH / 2 + 150, (HEIGHT / 2) - 150, 200, 25);
		panel.add(saveName);

		back = new JButton("Back To Main Menu");
		back.setBounds(0, 325, 300, 40);
		setUpOptionButton(back, 18);
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

			// Now for all the key bindings... Oh boy
			// TODO This exact thing
			writeSettings.write(":" + Game.fowardKey + ":" + Game.backKey + ":" + Game.leftKey + ":" + Game.rightKey
					+ ":" + Game.turnLeftKey + ":" + Game.turnRightKey + ":" + Game.turnUpKey + ":" + Game.turnDownKey
					+ ":" + Game.shootKey + ":" + Game.pauseKey + ":" + Game.runKey + ":" + Game.crouchKey + ":"
					+ Game.jumpKey + ":" + Game.fpsShowKey + ":" + Game.reloadingKey + ":" + Game.weaponSlot0Key + ":"
					+ Game.weaponSlot1Key + ":" + Game.weaponSlot2Key + ":" + Game.weaponSlot3Key + ":"
					+ Game.weaponSlot4Key + ":" + Game.useKey + ":" + Game.weaponSlot5Key + ":" + Game.weaponSlot6Key
					+ ":" + Game.upgradeWeaponKey + ":" + Game.recallFriendliesKey);

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
		error.setBounds(WIDTH / 2 - 260, HEIGHT - 100, 550, 25);
		setUpDecoratedLabel(error, 24);
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

	/**
	 * Each menu button is set up practically the same. Though there is some
	 * variance, this method will set up the basic qualities of the button for you.
	 * 
	 * @param button
	 */
	public void setUpOptionButton(JButton button, int fontSize) {
		// Button blends with background
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		button.setFocusPainted(false);

		// Sets font type, mode, and size
		button.setFont(new Font("Nasalization", Font.BOLD, fontSize));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		button.setFont(button.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		button.setForeground(defaultColor);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setForeground(mouseColor);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setForeground(defaultColor);
			}
		});

		button.addActionListener(aL);
	}

	/**
	 * This button type has the black opaque background
	 * 
	 * @param button
	 */
	public void setUpMajorButton(JButton button, int fontSize) {
		// Button blends with background
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBorderPainted(false);

		// Removes textbox focus so the textbox border is removed
		button.setFocusPainted(false);

		// Sets font type, mode, and size
		button.setFont(new Font("Nasalization", Font.BOLD, fontSize));

		// Uses a bitwise operator to merge the fonts of bold and italic
		// for the text
		button.setFont(button.getFont().deriveFont(Font.BOLD | Font.ITALIC));

		button.setForeground(defaultColor);
		button.setBackground(Color.BLACK);

		// Listens for whether the mouse has entered or exited the button
		// area and if it has or has not, change color accordingly
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setForeground(mouseColor);
				button.setBounds(button.getX(), button.getY(), button.getWidth() + 10, button.getHeight());
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setForeground(defaultColor);
				button.setBounds(button.getX(), button.getY(), button.getWidth() - 10, button.getHeight());
			}
		});

		button.addActionListener(aL);
	}

	/**
	 * Sets up a decorated label. Several of these are used, so this makes it
	 * easier.
	 * 
	 * @param label
	 */
	public void setUpDecoratedLabel(JLabel label, int fontSize) {
		label.setFont(new Font("Nasalization", Font.BOLD | Font.ITALIC, fontSize));
		label.setForeground(Color.GREEN);
		label.setBackground(Color.BLACK);
		label.setVisible(true);
		label.setOpaque(true);
	}

	/**
	 * Converts KeyCode integer to a string for the controls menu
	 * 
	 * @param keyCode
	 * @return
	 */
	public static String keyCodeToString(int keyCode) {
		String keyString = "";

		switch (keyCode) {
		case KeyEvent.VK_0:
			keyString = "0";
			break;

		case KeyEvent.VK_1:
			keyString = "1";
			break;

		case KeyEvent.VK_2:
			keyString = "2";
			break;

		case KeyEvent.VK_3:
			keyString = "3";
			break;

		case KeyEvent.VK_4:
			keyString = "4";
			break;

		case KeyEvent.VK_5:
			keyString = "5";
			break;

		case KeyEvent.VK_6:
			keyString = "6";
			break;

		case KeyEvent.VK_7:
			keyString = "7";
			break;

		case KeyEvent.VK_8:
			keyString = "8";
			break;

		case KeyEvent.VK_9:
			keyString = "9";
			break;

		case KeyEvent.VK_A:
			keyString = "A";
			break;

		case KeyEvent.VK_ALT:
			keyString = "ALT";
			break;

		case KeyEvent.VK_ASTERISK:
			keyString = "8";
			break;

		case KeyEvent.VK_AMPERSAND:
			keyString = "7";
			break;

		case KeyEvent.VK_AT:
			keyString = "2";
			break;

		case KeyEvent.VK_B:
			keyString = "B";
			break;

		case KeyEvent.VK_BACK_SLASH:
			keyString = "\\";
			break;

		case KeyEvent.VK_BACK_SPACE:
			keyString = "BackSp";
			break;

		case KeyEvent.VK_BRACELEFT:
			keyString = "{";
			break;

		case KeyEvent.VK_BRACERIGHT:
			keyString = "}";
			break;

		case KeyEvent.VK_C:
			keyString = "C";
			break;

		case KeyEvent.VK_CAPS_LOCK:
			keyString = "Caps";
			break;

		case KeyEvent.VK_CLOSE_BRACKET:
			keyString = "]";
			break;

		case KeyEvent.VK_COLON:
			keyString = ":";
			break;

		case KeyEvent.VK_COMMA:
			keyString = ",";
			break;

		case KeyEvent.VK_CONTROL:
			keyString = "ctrl";
			break;

		case KeyEvent.VK_D:
			keyString = "D";
			break;

		case KeyEvent.VK_DELETE:
			keyString = "Del";
			break;

		case KeyEvent.VK_DOWN:
			keyString = "DOWN";
			break;

		case KeyEvent.VK_E:
			keyString = "E";
			break;

		case KeyEvent.VK_ENTER:
			keyString = "Enter";
			break;

		case KeyEvent.VK_EQUALS:
			keyString = "=";
			break;

		case KeyEvent.VK_ESCAPE:
			keyString = "ESC";
			break;

		case KeyEvent.VK_EXCLAMATION_MARK:
			keyString = "1";
			break;

		case KeyEvent.VK_F:
			keyString = "F";
			break;

		case KeyEvent.VK_F1:
			keyString = "F1";
			break;

		case KeyEvent.VK_F2:
			keyString = "F2";
			break;

		case KeyEvent.VK_F3:
			keyString = "F3";
			break;

		case KeyEvent.VK_F4:
			keyString = "F4";
			break;

		case KeyEvent.VK_F5:
			keyString = "F5";
			break;

		case KeyEvent.VK_F6:
			keyString = "F6";
			break;

		case KeyEvent.VK_F7:
			keyString = "F7";
			break;

		case KeyEvent.VK_F8:
			keyString = "F8";
			break;

		case KeyEvent.VK_F9:
			keyString = "F9";
			break;

		case KeyEvent.VK_F10:
			keyString = "F10";
			break;

		case KeyEvent.VK_F11:
			keyString = "F11";
			break;

		case KeyEvent.VK_F12:
			keyString = "F12";
			break;

		case KeyEvent.VK_G:
			keyString = "G";
			break;

		case KeyEvent.VK_GREATER:
			keyString = ".";
			break;

		case KeyEvent.VK_H:
			keyString = "H";
			break;

		case KeyEvent.VK_I:
			keyString = "I";
			break;

		case KeyEvent.VK_INSERT:
			keyString = "Ins";
			break;

		case KeyEvent.VK_J:
			keyString = "J";
			break;

		case KeyEvent.VK_K:
			keyString = "K";
			break;

		case KeyEvent.VK_KP_DOWN:
			keyString = "DOWN";
			break;

		case KeyEvent.VK_KP_LEFT:
			keyString = "LEFT";
			break;

		case KeyEvent.VK_KP_RIGHT:
			keyString = "RIGHT";
			break;

		case KeyEvent.VK_KP_UP:
			keyString = "UP";
			break;

		case KeyEvent.VK_L:
			keyString = "L";
			break;

		case KeyEvent.VK_LEFT:
			keyString = "LEFT";
			break;

		case KeyEvent.VK_LEFT_PARENTHESIS:
			keyString = "(";
			break;

		case KeyEvent.VK_M:
			keyString = "M";
			break;

		case KeyEvent.VK_MINUS:
			keyString = "-";
			break;

		case KeyEvent.VK_MULTIPLY:
			keyString = "*";
			break;

		case KeyEvent.VK_N:
			keyString = "N";
			break;

		case KeyEvent.VK_NUM_LOCK:
			keyString = "Num";
			break;

		case KeyEvent.VK_NUMBER_SIGN:
			keyString = "3";
			break;

		case KeyEvent.VK_NUMPAD0:
			keyString = "Num 0";
			break;

		case KeyEvent.VK_NUMPAD1:
			keyString = "Num 1";
			break;

		case KeyEvent.VK_NUMPAD2:
			keyString = "Num 2";
			break;

		case KeyEvent.VK_NUMPAD3:
			keyString = "Num 3";
			break;

		case KeyEvent.VK_NUMPAD4:
			keyString = "Num 4";
			break;

		case KeyEvent.VK_NUMPAD5:
			keyString = "Num 5";
			break;

		case KeyEvent.VK_NUMPAD6:
			keyString = "Num 6";
			break;

		case KeyEvent.VK_NUMPAD7:
			keyString = "Num 7";
			break;

		case KeyEvent.VK_NUMPAD8:
			keyString = "Num 8";
			break;

		case KeyEvent.VK_NUMPAD9:
			keyString = "Num 9";
			break;

		case KeyEvent.VK_O:
			keyString = "O";
			break;

		case KeyEvent.VK_OPEN_BRACKET:
			keyString = "[";
			break;

		case KeyEvent.VK_P:
			keyString = "P";
			break;

		case KeyEvent.VK_PAGE_DOWN:
			keyString = "Pg dn";
			break;

		case KeyEvent.VK_PAGE_UP:
			keyString = "Pg up";
			break;

		case KeyEvent.VK_PERIOD:
			keyString = ".";
			break;

		case KeyEvent.VK_PLUS:
			keyString = "+";
			break;

		case KeyEvent.VK_PRINTSCREEN:
			keyString = "Prt sc";
			break;

		case KeyEvent.VK_Q:
			keyString = "Q";
			break;

		case KeyEvent.VK_QUOTE:
			keyString = "'";
			break;

		case KeyEvent.VK_R:
			keyString = "R";
			break;

		case KeyEvent.VK_RIGHT:
			keyString = "RIGHT";
			break;

		case KeyEvent.VK_RIGHT_PARENTHESIS:
			keyString = ")";
			break;

		case KeyEvent.VK_S:
			keyString = "S";
			break;

		case KeyEvent.VK_SCROLL_LOCK:
			keyString = "ScrLck";
			break;

		case KeyEvent.VK_SEMICOLON:
			keyString = ";";
			break;

		case KeyEvent.VK_SHIFT:
			keyString = "Shift";
			break;

		case KeyEvent.VK_SLASH:
			keyString = "/";
			break;

		case KeyEvent.VK_SPACE:
			keyString = "Space";
			break;

		case KeyEvent.VK_SUBTRACT:
			keyString = "-";
			break;

		case KeyEvent.VK_T:
			keyString = "T";
			break;

		case KeyEvent.VK_TAB:
			keyString = "Tab";
			break;

		case KeyEvent.VK_U:
			keyString = "U";
			break;

		case KeyEvent.VK_UP:
			keyString = "UP";
			break;

		case KeyEvent.VK_V:
			keyString = "V";
			break;

		case KeyEvent.VK_W:
			keyString = "W";
			break;

		case KeyEvent.VK_WINDOWS:
			keyString = "WinKey";
			break;

		case KeyEvent.VK_X:
			keyString = "X";
			break;

		case KeyEvent.VK_Y:
			keyString = "Y";
			break;

		case KeyEvent.VK_Z:
			keyString = "Z";
			break;

		case KeyEvent.VK_ADD:
			keyString = "+";
			break;

		case KeyEvent.VK_DIVIDE:
			keyString = "/";
			break;

		default:
			keyString = "NaN";
			break;
		}

		return keyString;
	}
}
