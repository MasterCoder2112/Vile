package com.vile;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
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
import com.vile.entities.EntityParent;
import com.vile.entities.Explosion;
import com.vile.entities.ExplosiveCanister;
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
	private static Thread thread2;
	private static Thread thread3;

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

	// Used so the inputStream can be closed for music after you pause or
	// start a new game
	public static AudioInputStream inputStream;

	private int hudMovementTick = 0;

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

			try {
				thread2.join();
			} catch (Exception e) {

			}

			try {
				thread3.join();
			} catch (Exception e) {

			}

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
			thread2 = null;
			thread3 = null;

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
					Player.superSpeedOn = false;
					Player.godModeOn = false;
					Player.unlimitedAmmoOn = false;
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
			screen = new Screen(WIDTH, HEIGHT);

			// Sets up the BufferedImage size, and type (ints = color info)
			img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

			// Gets the data from each integer and converts it to a color type
			Screen.render3D.PIXELS = null;
			Screen.render3D.PIXELS = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

			loading.setTitle("Setting up input handling listeners... 90% Loaded");

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
			thread2 = new Thread(this);
			// thread3 = new Thread(this);
			thread.start();
			thread2.start();
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
					// TODO during the first time there is extra data to load in
					if (firstTime) {
						String[] sections = fromServer.split("\\?");

						// TODO This is to keep this as a checkpoint. STARTING COORDINATES
						String currentSection = sections[0];
						String[] attributes = currentSection.split(":");

						Player.startX = Double.parseDouble(attributes[0]);
						Player.startY = Double.parseDouble(attributes[1]);
						Player.startZ = Double.parseDouble(attributes[2]);
						game = new Game(this, true, attributes[3].trim());
						firstTime = false;

						// TODO ALL CLIENT INFORMATION
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

						// TODO Set up blocks on the level now

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
						// TODO for every time other than first time
						game.resetLists();

						// System.out.println(fromServer);

						String[] sections = fromServer.split("\\?");

						String[] attributes;

						// TODO ALL CLIENT INFORMATION
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

							// TODO Items
							currentSection = sections[1];
							entitiesOfType = currentSection.split(";");

							if (entitiesOfType.length > 1 && entitiesOfType[0].trim().equals("Items")) {

								for (int i = 1; i < entitiesOfType.length; i++) {
									attributes = entitiesOfType[i].split(":");

									int itemID = Integer.parseInt(attributes[0]);

									Item temp = null;

									// TODO update item loading stuff maybe

									/*
									 * If its not an explosive canister, add it as a normal item. Otherwise add it
									 * as an explosive canister
									 */
									if (itemID != ItemNames.CANISTER.getID()) {
										temp = new Item(10, Double.parseDouble(attributes[1]),
												Double.parseDouble(attributes[2]), Double.parseDouble(attributes[3]),
												itemID, Integer.parseInt(attributes[4]),
												Integer.parseInt(attributes[5]), "");
									} else {
										temp = new ExplosiveCanister(10, Double.parseDouble(attributes[1]),
												Double.parseDouble(attributes[2]), Double.parseDouble(attributes[3]),
												itemID, Integer.parseInt(attributes[4]),
												Integer.parseInt(attributes[5]));
									}

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

							// TODO Bullets loading in
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
		Graphics g = bs.getDrawGraphics();

		// Draws image with offsets, and given WIDTH and HEIGHT
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

		// Sets font of any text drawn on the screen
		g.setFont(new Font("Nasalization", 1, 15));

		/*
		 * Depending on how low the players health is, set the text to different colors
		 * on the screen.
		 */
		if (Player.health > 100) {
			Controller.moveSpeed = 1.5;
		} else if (Player.health > 60) {
			Controller.moveSpeed = 1.0;
		} else if (Player.health > 20) {
			Controller.moveSpeed = 0.75;
		} else {
			Controller.moveSpeed = 0.5;
		}

		g.setColor(Color.RED);

		// How much GUI is off from bottom of screen
		int gC = 100;

		// If Not full screen, its raised up more for the frame border
		if (FPSLauncher.resolutionChoice < 4) {
			gC = 128;
		}

		/*
		 * Tries to load up the GUI image for the HUD. If it can't, it will just pass
		 * through this and render the text only.
		 * 
		 * The GUI is the weapon picture, player face, and gray box outline.
		 */
		g.drawImage(HUD, (WIDTH / 2) - 400, HEIGHT - gC, 800, 100, null);

		// Get the Weapon the player currently has Equipped
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
		 * Display the weapon in front of the player and how it looks depending on what
		 * phase of firing the weapon is, and whether the player is dead or not
		 */
		try {
			// Image of weapon shown
			BufferedImage gun = gunNormal;

			// Coordinates weapon is rendered on screen
			int x = (WIDTH / 2) - 150;
			int y = HEIGHT - gC - 250;

			// Pistol
			if (playerWeapon.weaponID == 0) {
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
						g.drawImage(gun2, (WIDTH / 2) - 300, y, 300, 250, null);
					}
				}
			}
			// Shotgun
			else if (playerWeapon.weaponID == 1) {
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
			else if (playerWeapon.weaponID == 2) {
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
			else if (playerWeapon.weaponID == 3) {
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
			else if (playerWeapon.weaponID == 4) {
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
				g.drawImage(gun, x, y, 300, 250, null);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		/*
		 * Try to render the keys now depending on whether the player has them or not.
		 */
		if (Player.hasRedKey) {
			g.drawImage(redKey, (WIDTH / 2) + 75, HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasBlueKey) {
			g.drawImage(blueKey, (WIDTH / 2) + 95, HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasGreenKey) {
			g.drawImage(greenKey, (WIDTH / 2) + 115, HEIGHT - gC + 23, 10, 20, null);
		}

		if (Player.hasYellowKey) {
			g.drawImage(yellowKey, (WIDTH / 2) + 135, HEIGHT - gC + 23, 10, 20, null);
		}

		// For the weapon Upgrade points
		// TODO make look correct
		if (hudMovementTick <= 8) {
			g.drawImage(weaponUpgradePoint1, (WIDTH / 2) + 60, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 16) {
			g.drawImage(weaponUpgradePoint2, (WIDTH / 2) + 60, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 24) {
			g.drawImage(weaponUpgradePoint3, (WIDTH / 2) + 60, HEIGHT - gC + 45, 50, 50, null);
		} else if (hudMovementTick <= 32) {
			g.drawImage(weaponUpgradePoint4, (WIDTH / 2) + 60, HEIGHT - gC + 45, 50, 50, null);
		} else {
			g.drawImage(weaponUpgradePoint1, (WIDTH / 2) + 60, HEIGHT - gC + 45, 50, 50, null);
			hudMovementTick = 0;
		}

		g.drawString(" " + Player.upgradePoints, (WIDTH / 2) + 100, HEIGHT - gC + 85);
		g.drawString("Upgrade weapon (" + Player.weapons[Player.weaponEquipped].upgradePointsNeeded + " points)",
				(WIDTH / 2) + 150, HEIGHT - gC + 65);
		g.drawString("Current weapon damage: " + Player.weapons[Player.weaponEquipped].damage + "hp", (WIDTH / 2) + 150,
				HEIGHT - gC + 85);

		hudMovementTick++;

		// Shows the FPS on the screen if it is activated to show
		if (Controller.showFPS) {
			g.drawString("FPS: " + fps, 20, 50);
		}

		// Shows if fly mode is on
		if (Player.flyOn) {
			g.drawString("FlyMode on", 400, 50);
		}

		// Shows up if noClip is on
		if (Player.noClipOn) {
			g.drawString("noClip On", 200, 50);
		}

		// If SuperSpeed is activated
		if (Player.superSpeedOn) {
			g.drawString("Super Speed On", 200, 100);
		}

		// If GodMode is activated
		if (Player.godModeOn) {
			g.drawString("God Mode On", 400, 100);
		}

		// If Unlimited ammo is activated
		if (Player.unlimitedAmmoOn) {
			g.drawString("Infinite Ammo On", 600, 50);
		}

		/*
		 * If the player is not dead, show the number of cartridges of ammo the player
		 * has, the amount of ammo the player currently has loaded, and how much is in
		 * the current cartridge. Also show the players health, armor, and keycards.
		 */
		if (Player.alive) {
			if (smileMode) {
				g.drawString("Happiness: " + Player.health, (WIDTH / 2) - 190, HEIGHT - gC + 18);

				g.drawString("Positivity: " + Player.armor, (WIDTH / 2) - 190, HEIGHT - gC + 43);

				if (playerWeapon.weaponID == 0) {
					g.drawString("Arrow Quivers: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Arrows: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 1) {
					g.drawString("Joy Cartridges: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Joy: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 2) {
					g.drawString("Peace Cells: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390, HEIGHT - gC + 43);

					g.drawString("Peace Charges: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 3) {
					g.drawString("Teddy Bear Casings: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Teddy Bears: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 4) {
					g.drawString("Happiness Cores: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Happy Blasts: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				}

				/*
				 * If there are cartridges, show the ammo of the one being currently used, if
				 * not just display that there is 0 available.
				 */
				if (playerWeapon.cartridges.size() != 0) {
					if (playerWeapon.weaponID == 0) {
						g.drawString("Arrows in Quiver: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 1) {
						g.drawString("Joy in Casing: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Peace in Cell: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Teddy Bears in Casing: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Happy Blasts in Core: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					}
				} else {
					if (playerWeapon.weaponID == 0) {
						g.drawString("Arrows in Quiver: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 1) {
						g.drawString("Joy in Casing: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Peace in Cell: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Teddy Bears in Casing: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Happy Blasts in Core: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					}
				}
			} else {
				g.drawString("Health: " + Player.health, (WIDTH / 2) - 190, HEIGHT - gC + 18);

				g.drawString("Armor: " + Player.armor, (WIDTH / 2) - 190, HEIGHT - gC + 43);

				if (playerWeapon.weaponID == 0) {
					g.drawString("Bullet Cartridges: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Bullets: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 1) {
					g.drawString("Shell Cartridges: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Shells: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 2) {
					g.drawString("Phase Cells: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390, HEIGHT - gC + 43);

					g.drawString("Phase Charges: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 3) {
					g.drawString("Rocket Casings: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Rockets: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				} else if (playerWeapon.weaponID == 4) {
					g.drawString("Scepter Cores: " + playerWeapon.cartridges.size(), (WIDTH / 2) - 390,
							HEIGHT - gC + 43);

					g.drawString("Scepter Blasts: " + playerWeapon.ammo, (WIDTH / 2) - 390, HEIGHT - gC + 68);
				}

				/*
				 * If there are cartridges, show the ammo of the one being currently used, if
				 * not just display that there is 0 available.
				 */
				if (playerWeapon.cartridges.size() != 0) {
					if (playerWeapon.weaponID == 0) {
						g.drawString("Bullets in Cartridge: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 1) {
						g.drawString("Shells in Casing: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Charges in Cell: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Rockets in Casing: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Power Blasts in Core: " + playerWeapon.cartridges.get(0).ammo, (WIDTH / 2) - 390,
								HEIGHT - gC + 18);
					}
				} else {
					if (playerWeapon.weaponID == 0) {
						g.drawString("Bullets in Cartridge: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 1) {
						g.drawString("Shells in Casing: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 2) {
						g.drawString("Charges in Cell: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 3) {
						g.drawString("Rockets in Casing: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
					} else if (playerWeapon.weaponID == 4) {
						g.drawString("Power Blasts in Core: 0", (WIDTH / 2) - 390, HEIGHT - gC + 18);
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
			g.drawString("Survival Map", (WIDTH / 2) - 390, HEIGHT - gC + 93);

			if (smileMode) {
				g.drawString("Unhappy Faces: " + Game.entities.size(), (WIDTH / 2) + 200, HEIGHT - gC + 43);

				g.drawString("Days Made: " + Player.kills, (WIDTH / 2) + 200, HEIGHT - gC + 18);
			} else {
				g.drawString("Enemies: " + Game.entities.size(), (WIDTH / 2) + 200, HEIGHT - gC + 43);

				g.drawString("Kills: " + Player.kills, (WIDTH / 2) + 200, HEIGHT - gC + 18);
			}
		} else {
			g.drawString("Secrets found: " + Game.secretsFound + " / " + Game.secretsInMap, (WIDTH / 2) + 200,
					HEIGHT - gC + 18);

			g.drawString(Game.mapName, (WIDTH / 2) - 390, HEIGHT - gC + 93);

			if (smileMode) {
				g.drawString("Made Happy: " + Player.kills + " / " + Game.enemiesInMap, (WIDTH / 2) + 200,
						HEIGHT - gC + 43);
			} else {
				g.drawString("Kills: " + Player.kills + " / " + Game.enemiesInMap, (WIDTH / 2) + 200, HEIGHT - gC + 43);
			}

			// Only show deaths if in multiplayer deathmatch mode
			if (gameType == 1) {
				if (smileMode) {
					g.drawString("Got sad: " + Player.deaths, (WIDTH / 2) + 200, HEIGHT - gC + 68);
				} else {
					g.drawString("Deaths: " + Player.deaths, (WIDTH / 2) + 200, HEIGHT - gC + 68);
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
			g.drawString("Keys:", (WIDTH / 2) + 75, HEIGHT - gC + 18);
		} else {
			g.drawString("High Score:", (WIDTH / 2) + 75, HEIGHT - gC + 18);
			g.drawString("" + Player.maxKills, (WIDTH / 2) + 75, HEIGHT - gC + 36);
		}

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
