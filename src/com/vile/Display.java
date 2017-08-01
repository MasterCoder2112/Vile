package com.vile;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.vile.entities.Entity;
import com.vile.entities.Player;
import com.vile.entities.Weapon;
import com.vile.graphics.Render3D;
import com.vile.graphics.Screen;
import com.vile.graphics.Textures;
//Imports files within mineFront package holding Render and Screen Objects
import com.vile.input.Controller;
import com.vile.input.InputHandler;
import com.vile.launcher.FPSLauncher;

/**
 * @title Display
 * @author Alex Byrd 
 * Date Updated: 5/10/2017
 *
 * Runs the Game initially, displays everything on the screen, keeps
 * track of fps, and renders the screen. Also creates thread to keep
 * track of all events and such.
 */
public class Display extends Canvas implements Runnable 
{
	//Don't worry about this, java recommends this for some reason
	private static final long serialVersionUID = 1L;

	//Version Number
	private static double versionNumber = 1.6;
	
	//Frame Width and height
	public static int HEIGHT   = 600;
	public static int WIDTH    = 800;
	
	//Width and height of computer screen
	public static int screenHeight = 0;
	public static int screenWidth = 0;
	
	//Frame title
	public static final String TITLE = "Vile Alpha " + versionNumber
			+" Dev 1";

	// Is music audio on?
	public static boolean audioOn = false;

	// Audio clip that is loaded in for music
	private static Clip music;
	
	//Keeps track of gain controller for the music. Volume basically
	public static FloatControl musicControl;
	
	//Keeps track of how fast you move mouse
	public static double mouseSpeedHorizontal;
	public static double mouseSpeedVertical;

	// Thread of events
	private static Thread thread;

	// Determines if program is running
	private boolean isRunning = false;

	// Determines whether the game is paused
	public static boolean pauseGame = false;

	// New screen object
	private Screen screen;

	//The BufferedImage that is displayed of pixels on screen
	private BufferedImage img;

	// Creates a new game (Handles all keyMap events and ticks)
	public static Game game;

	// Handles input events
	private InputHandler input;
	private Controller controls;
	
	//Music input stream
	private static AudioInputStream input18;

	// Used to display frames per second
	public static int fps = 0;

	// How many kills the player has
	public static int kills = 0;

   /*
	* Gotten from the options menu to make changes in the game.
	* Mainly used for survival mode
	*/
	public static int graphicsSelection = 4; //Graphics option you chose
	public static int themeNum          = 0; //Theme you chose
	public static int levelSize         = 3; //Level size
	public static boolean mouseOn       = true; //Is the mouse on?
	public static int skillMode         = 2; //Skill Level
	
	//Current music theme number
	public static int musicTheme = 2;
	
	//Last music theme that was played. USED FOR MLG MODE
	public static int oldMusicTheme = 2;
	
	//How the ints are rendered into pixels of a certain color
	private BufferStrategy bs;
	
	//Whether it is a new game (needing complete reset) or not
	public static boolean resetGame = false;
	
	//Continues to change the face direction and look.
	private int facePhase = 0;
	
   /*
    * All different HUD images to load up
    */
	private static BufferedImage yellowKey;
	private static BufferedImage redKey;
	private static BufferedImage blueKey;
	private static BufferedImage greenKey;
	private static BufferedImage HUD;
	private static BufferedImage healthyFace1;
	private static BufferedImage healthyFace2;
	private static BufferedImage healthyFace3;
	private static BufferedImage hurtFace1;
	private static BufferedImage hurtFace2;
	private static BufferedImage hurtFace3;
	private static BufferedImage veryHurtFace1;
	private static BufferedImage veryHurtFace2;
	private static BufferedImage veryHurtFace3;
	private static BufferedImage almostDead1;
	private static BufferedImage almostDead2;
	private static BufferedImage almostDead3;
	private static BufferedImage dead;
	private static BufferedImage godMode;
	private static BufferedImage gunNormal;
	private static BufferedImage invisible;
	private static BufferedImage playerHarmedHealthy;
	private static BufferedImage playerHarmedHurt;
	private static BufferedImage playerHarmedVeryHurt;
	private static BufferedImage playerHarmedAlmostDead;
	private static BufferedImage invisGodmode;
	private static BufferedImage gunShot;
	private static BufferedImage gunShot2;
	private static BufferedImage gunShot3;
	private static BufferedImage gunShot4;
	private static BufferedImage phaseCannon1;
	private static BufferedImage phaseCannon2;
	private static BufferedImage phaseCannon3;
	private static BufferedImage phaseCannon4;
	private static BufferedImage rocketLauncher;
	private static BufferedImage rocketLauncher1;
	private static BufferedImage rocketLauncher2;
	private static BufferedImage rocketLauncher3;
	private static BufferedImage rocketLauncher4;
	private static BufferedImage pistolLeft1;
	private static BufferedImage pistolLeft2;
	private static BufferedImage pistolLeft3;
	private static BufferedImage pistolLeft4;
	private static BufferedImage pistolLeft5;
	private static BufferedImage pistolRight1;
	private static BufferedImage pistolRight2;
	private static BufferedImage pistolRight3;
	private static BufferedImage pistolRight4;
	private static BufferedImage pistolRight5;
	private static BufferedImage nickCage;
	private static BufferedImage deadNick;
	private static BufferedImage otherNick;
	private static BufferedImage otherNick2;
	private static BufferedImage mlgFace1;
	private static BufferedImage mlgFace2;
	private static BufferedImage mlgFace3;
	private static BufferedImage mlgDead;
	private static BufferedImage mlgGod;
	private static BufferedImage nickGod;
	
   /*
    * Sets up a string that can change and disappears after 100 ticks
    * about the most recent pick up you got, or if you are trying to
    * open a door that requires a keyMap you don't have.
    */
	public static String itemPickup = "";
	public static int itemPickupTime = 0;
	
	//In case user wants to manually set the map that is played
	public static boolean nonDefaultMap = false;
	public static String newMapName = "";
	
	//Keeps track of recent mouse values
	public static int oldX = 0;
	public static int oldY = 0;
	public static int newX = 0;
	public static int newY = 0;
	
	//The new sound engine I have set up
	private static SoundController soundController;
	
	//Whether the sound is set up yet or not, and without errors
	private static boolean soundSetUp = false;
	
   /*
    * FOR AN EXPERIMENTAL SMOOTH FPS MODE. CAN ONLY BE CHANGED HERE.
    * YOU CAN TEST THE EFFECTS IF YOU WANT BUT SOME ARE WONKY. SOME
    * FIX BUGS, SOME CAUSE BUGS, YOU KNOW THE DRILL.
    */
	public static boolean smoothFPS = false;

	/**
	 * Creates a new display by setting up the screen and its dimensions, sets
	 * up the image the screen displays, how the pixels will render when set,
	 * and adds all the input stuff so that the program will read input from the
	 * mouse, keyboard, and can set a focus on them if needed.
	 */
	public Display() 
	{
		//A temporary frame to display where the game is at in the 
		//loading process
		JFrame loading = new JFrame("Loading Variables... 0% Loaded");
		
		//Sets frame size, centers it on screen, makes it nonresizable,
		//and still closable.
		loading.setSize(WIDTH, HEIGHT);
		loading.setLocationRelativeTo(null);
		loading.setResizable(false);
		loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Gets rid of java icon and replaces it with the SMILE icon
		ImageIcon titleIcon = new ImageIcon("Images/titleIcon.png");
		loading.setIconImage(titleIcon.getImage());
		
		//Make frame visible
		loading.setVisible(true);
		
		// The game is not ending yet
		Controller.quitGame = false;
		
		//Game is not paused
		Display.pauseGame = false;
		
		//Just displays point in loading.
		loading.setTitle("Loading sounds... 5% Loaded");
		
		//Set up textures
		Textures.Textures();
		
	   /*
	    * If the sound system isn't set up, set up the sounds by 
	    * constructing a new SoundController. If it is, then just
	    * reset the volumes to make sure they are updated.
	    */
		if(!soundSetUp)
		{
			//Loads all sounds and files
			soundController =  new SoundController();
			soundSetUp = true;
		}
		else
		{
			try
			{
				//All sound volumes are reset
				soundController.resetAllVolumes(FPSLauncher.soundVolumeLevel);
			}
			catch(Exception e)
			{
				
			}
		}

		//Only resets game values if the game needs to be restarted
		if(!Display.resetGame)
		{
			loading.setTitle("Loading graphics... 25% Loaded");
			
			//Try to load all the HUD images. If not catch the exception
			try
			{
				loadHUD();
			}
			catch(Exception e)
			{
				
			}
			
			loading.setTitle("Setting up game entities... 35% Loaded");
			
			//If new game, reset the gameMode
			Game.skillMode = Display.skillMode;
			
			// Resets player values (health, ammo, etc...)
			new Player();
			
			// New Game and map
			game = new Game(this, nonDefaultMap, newMapName);
			
			// Reset debug values
			Controller.showFPS = false;
			Controller.flyOn = false;
			Controller.noClipOn = false;
			Controller.superSpeedOn = false;
			Controller.godModeOn = false;
			Controller.unlimitedAmmoOn = false;
			
			//Reset kills
			kills = 0;
			
			loading.setTitle("Setting up music... 50% Loaded");
	       /*
	        * If audio is already on from a previous game, close it
	        * so that the new audio can run.
	        */
			if(audioOn)
			{
				music.close();
			}
			
			//Music is not on yet
			audioOn = false;
			
			//If mlg mode, change theme music to mlg theme
			//It is special 
			if(themeNum == 5)
			{
				musicTheme = 3;
			}
			
		   /*
		    * When there is a new game, there is no previous music theme
		    * so oldMusicTheme is set equal to the current music theme.
		    */
			oldMusicTheme = musicTheme;
				
			//Only do if there is music playing
			if(audioOn)
			{
				   /*
				    * If the music volume is as lower than it can go (Meaning off) 
				    * then just set it to the limit. Otherwise just set the
				    * music volume to the correct level.
				    */
					if(FPSLauncher.musicVolumeLevel < -80)
					{
						musicControl.setValue(-80.0f);
					}
					else
					{
						musicControl.setValue(FPSLauncher.musicVolumeLevel);
					}
			}	
		}
		//If just resuming game, most values are already loaded
		else
		{
			//Do not reset the game completely
			Display.resetGame = false;
			
			loading.setTitle("Setting up music... 50% Loaded");
			
			//If mlg theme, make musicTheme the mlg theme
			//It is special.
			if(themeNum == 5)
			{
				musicTheme = 3;
			}
			
		   /*
		    * If you changed the music theme while the game was paused,
		    * then close the old music playing, turn audioOn to false, and
		    * then set the oldMusicTheme to the current theme. The music
		    * will then switch to playing the new audio.
		    */
			if(musicTheme != oldMusicTheme)
			{
				music.close();
				audioOn = false;
				oldMusicTheme =  musicTheme;
			}
			
			//Only do if there is music playing
			if(audioOn)
			{
			   /*
			    * If the music volume is as lower than it can go (Meaning off) 
			    * then just set it to the limit. Otherwise just set the
			    * music volume to the correct level.
			    */
				if(FPSLauncher.musicVolumeLevel < -80)
				{
					musicControl.setValue(-80.0f);
				}
				else
				{
					musicControl.setValue(FPSLauncher.musicVolumeLevel);
				}
			}			   
		}

		loading.setTitle("Resetting textures and GUI... 65% Loaded");
		
		// Resets the frames width and height
		setGameWidth();
		setGameHeight();

		// Resets games textures depending on theme
		Textures.resetTextures();

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
		img = new BufferedImage
				(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		//Gets the data from each integer and converts it to a color type
		screen.render3D.PIXELS = 
				((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		loading.setTitle("Setting up input handling listeners... 90% Loaded");
		
	   /*
		* Sets up all the input stuff and listeners
		*/
		input = new InputHandler();
		addKeyListener(input);
		addMouseListener(input);
		addFocusListener(input);
		addMouseMotionListener(input);
		
		//Set mouse values to starting position
		newX = InputHandler.MouseX;
		newY = InputHandler.MouseY;
		oldX = InputHandler.MouseX;
		oldY = InputHandler.MouseY;
		
		loading.setTitle("Done 100% Loaded");
		
		//Get rid of loading frame when done loading
		loading.dispose();
		controls = new Controller();
	}

	/**
	 * Sets Width of frame depending on the the graphics selection from the
	 * launcher.
	 */
	public static void setGameWidth() 
	{
		//Gets computer screen width
		Dimension screenSize = 
				Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		
		//Total width of screen size in integer form
		screenWidth = (int)width;
		
		//Change frame width based on graphics selection
		if (graphicsSelection <= 1)
		{
			WIDTH = 800;
		} 
		else if (graphicsSelection <= 3) 
		{
			WIDTH = 1020;
		} 
		else 
		{
			WIDTH = screenWidth;
		}
	}

	/**
	 * Sets Height of frame depending on the graphics selection from the
	 * launcher.
	 */
	public static void setGameHeight() 
	{
		//Same as above but for height
		Dimension screenSize = 
				Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		
		//Total height of computer screen
		screenHeight = (int)height;
		
		if (graphicsSelection <= 1)
		{
			HEIGHT = 600;
		} 
		else if (graphicsSelection <= 3) 
		{
			HEIGHT = 700;
		} 
		else 
		{
			HEIGHT = (int) height;
		}
	}

	/**
	 * Initiates the game to start running, and sets isRunning to true.
	 * Synchronized helps synchronize the thread to run correctly if the program
	 * is being ran in an applet.
	 */
	public synchronized void start() 
	{
		// If program is already running, just return
		if (isRunning == true) 
		{
			return;
		}

		// If not, set the program to start running
		else 
		{
			isRunning = true;

			// Starts a new thread to handle all the events
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * What is called if the program is running. It renders the screen, performs
	 * all the actions in the thread of events, tracks fps, etc.
	 */
	public void run() 
	{
		//Frames that have elapsed within the last second
		int frames = 0;
		
		//How many ticks between seconds
		int tickCount = 0;
		
	   /*
	    * I.... I'm still learning how this one works. I'm guessing maybe
	    * the time lost while this is checking for the games fps?
	    */
		double unprocessedSeconds = 0;
		
		//How many seconds are in a tick I'm guessing
		double secondsPerTick = (1 / 60.0);
		
		//The last time the game ticked. Initially set to current time
		long previousTime = System.nanoTime();

		/*
		 * Focuses your user on the screen so you don't 
		 * have to click to start
		 */
		requestFocus();

		// While the game is running, keep ticking and rendering
		while (isRunning == true) 
		{			
			//Current time
			long currentTime = System.nanoTime();
			
			//How much time has passed between ticks
			long passedTime = currentTime - previousTime;
			
			//Reset previousTime to now
			previousTime = currentTime;

			/*
			 * I'm guessing the negligible amount of time these
			 * calculations take? To be honest I had to get this fps
			 * check code off the internet and had to try to understand
			 * it from there. It makes more sense now, but somethings are
			 * very hard to understand.
			 */
			unprocessedSeconds += (passedTime / 1000000000.0);

			/*
			 * Basically checks for the difference in seconds between the set
			 * value and calculated value. As long as unprocessedSeconds is more
			 * than secondsPerTick it will continue to loop through. Each loop
			 * it will tick, and unprocessedSeconds will keep going down. Every
			 * 60 loops, it will print out the number of frames that the game
			 * has, set frames back to 0, and add to previousTime. Why it does
			 * all this I am not 100% sure yet, but it works.
			 */
			while (unprocessedSeconds > secondsPerTick) 
			{					
				//See how many ticks occured within one second
				unprocessedSeconds -= secondsPerTick;
				
				//Explained below
				if(!smoothFPS)
				{
					//Tick through the game and run all events
					tick();
				}
				
				//Add to tick count
				tickCount++;

				//Every sixty ticks
				if (tickCount % 60 == 0) 
				{
					//Set how many frames per second are rendered
					fps = frames;
					
					//Reset number of frames rendered in current second
					frames = 0;
					
					//Approximately how much time in nanoseconds it took
					//to run this loop I'm guessing
					previousTime += 1000;
				}
			}
			
			Entity.checkSight = true;
			
		   /*
		    * EXPERIMENTAL!!! ALLOWS ALL GAME EVENTS TO CATER TO ONE FPS 
		    * RATE. THIS FIXES SOME BUGS BUT CAUSES OTHERS. YOU CAN NO
		    * LONGER CLIP THROUGH THE MAP AT VERY LOW FRAMERATES BUT
		    * YOU THINGS WILL BE SLOWER AND UNSYNCED WHEN FIRING WEAPONS
		    * AND MOVING AND SUCH. 
		    * 
		    * IT ALSO MAKES THE GAME FASTER THOUGH...
		    */
			if(smoothFPS)
			{
				//Tick through the game and run all events
				tick();
			}
			
			// Render the pixels, and add to number of frames rendered
			render();
			frames++;
			
			// Only do this if the mouse is on
			if (Display.mouseOn) 
			{
				//Get the JFrame on screen
				Component c = RunGame.frame;
			}
			
			//If game is supposed to be quit, then call the stop method
			//to end all thread events of the game
			if (Controller.quitGame == true) 
			{
				stop();
			}
		}
	}

	/**
	 * Stop the program if it is still running. Otherwise just return, the
	 * programs already stopped.
	 */
	public synchronized void stop() 
	{
		//If the game is already stopped, return
		if (isRunning == false) 
		{
			return;
		} 
	   /*
	    * If still running, stop the music, set game to not running
	    * (isRunning = false) and create a new Launcher (aka Main menu)
	    */
		else 
		{
			isRunning = false;

			// Closes the clip, stopping the games music.
			if (musicTheme != 0 && !Display.pauseGame) 
			{
				music.close();
			}
			
			//Start new launcher
			try 
			{
				new FPSLauncher(0);
			}
			catch (Exception e) 
			{
				// If that somehow has an error, end the program anyway
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Tick through all the game events. Update all game values and
	 * movements and such.
	 */
	private void tick() 
	{
		game.tick();
		controls.performActions(input.keyMap, game);
	}

	/**
	 * Renders the screen after each new event resetting the graphics. Thats the
	 * simplest way to put it.
	 */
	private void render() 
	{
	   /*
		* Strategy of how the image and pixels are buffered. How it organizes
		* complex memory on a particular canvas or window.
		* 
		* Takes the integers and renders them using a graphics object
		* that interprets the integers and displays them correctly on the
		* screen as being a certain color and brightness.
		*/
		bs = this.getBufferStrategy();

		//If it has not been instantiated yet
		try
		{
			if (bs == null) 
			{
				/*
				 * Creates the 3rd type of BufferStrategy. Method can be called
				 * because this class extends canvas. I'm guessing the 3 is because
				 * this program will be 3D. It doesn't seem to matter if I change
				 * it though. 
				 */
				createBufferStrategy(3);
				return;
			}
		}
		catch (Exception e)
		{
			createBufferStrategy(3);
			return;
		}

		// Call screen and have it render all events that happen in game
		screen.render(game);

		/*
		 * Creates a graphics object based on what the buffered strategy is and
		 * then disposes of the graphics object after it is drawn to the screen.
		 * bs.show(); shows it on the screen.
		 */
		Graphics g = bs.getDrawGraphics();

		// Draws image with offsets, and given WIDTH and HEIGHT
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

		// Sets font of any text drawn on the screen
		g.setFont(new Font("Nasalization", 3, 15));

		/*
		 * Depending on how low the players health is, set the text to different
		 * colors on the screen.
		 */
		if (Player.health > 100) 
		{
			Controller.moveSpeed = 1.5;
		} 
		else if (Player.health > 60) 
		{
			Controller.moveSpeed = 1.0;
		} 
		else if (Player.health > 20) 
		{
			Controller.moveSpeed = 1.0;
		} 
		else 
		{
			Controller.moveSpeed = 1.0;
		}
		
		g.setColor(Color.RED);
		
		//How much GUI is off from bottom of screen
		int gC = 128;
		
		//If full screen it is less
		if(graphicsSelection == 3 || graphicsSelection == 2)
		{
			gC = 100;
		}
		
	   /*
	    * Tries to load up the GUI image for the HUD. If it can't, it will
	    * just pass through this and render the text only.
	    * 
	    * The GUI is the weapon picture, player face, and gray box outline.
	    */
		g.drawImage(HUD, (WIDTH / 2) - 400, HEIGHT - gC, 800, 100, null);
		
		//Get the Weapon the player currently has Equipped
		Weapon playerWeapon = Player.weapons[Player.weaponEquipped];
	
	   /*
	    * Tries to load the face corresponding to the players health and
	    * phase of looking, but if it can't load it, it will just not
	    * load and go on.
	    */
		try
		{
			BufferedImage face = healthyFace1; 
			
			int phase = facePhase / 50;
			
			//If player is healthy
			if(Player.health > 75)
			{
				if(phase == 0 || phase == 2)
				{
					face = healthyFace1;
				}
				else if(phase == 1)
				{
					face = healthyFace2;
				}
				else
				{
					face = healthyFace3;
				}
				
				//If player was just harmed have this face.
				if(Player.playerHurt > 0)
				{
					face = playerHarmedHealthy;
				}
			}
			else if(Player.health > 50)
			{
				if(phase == 0 || phase == 2)
				{
					face = hurtFace1;
				}
				else if(phase == 1)
				{
					face = hurtFace2;
				}
				else
				{
					face = hurtFace3;
				}
				
				//If player was just harmed have this face.
				if(Player.playerHurt > 0)
				{
					face = playerHarmedHurt;
				}
			}
			else if(Player.health > 25)
			{
				if(phase == 0 || phase == 2)
				{
					face = veryHurtFace1;
				}
				else if(phase == 1)
				{
					face = veryHurtFace2;
				}
				else
				{
					face = veryHurtFace3;
				}
				
				//If player was just harmed have this face.
				if(Player.playerHurt > 0)
				{
					face = playerHarmedVeryHurt;
				}
			}
			else if(Player.health > 0)
			{
				if(phase == 0 || phase == 2)
				{
					face = almostDead1;
				}
				else if(phase == 1)
				{
					face = almostDead2;
				}
				else
				{
					face = almostDead3;
				}
				
				//If player was just harmed have this face.
				if(Player.playerHurt > 0)
				{
					face = playerHarmedAlmostDead;
				}
			}
			else
			{
				face = dead;
				
				//Changes upon theme
				if(themeNum == 3)
				{
					face = deadNick;
				}
				else if(themeNum == 5)
				{
					face = mlgDead;
				}
			}
			
			//As long as Player is alive check for these
			if(Player.alive)
			{
				//If player is both invisible and invincible
				if(Player.invisibility > 0 && 
						(Controller.godModeOn || Player.immortality > 0))
				{
					face = invisGodmode;
				}	
				//If player is invisible show the invisible face
				else if(Player.invisibility > 100 * Render3D.fpsCheck
						|| (Player.invisibility > 0
						&& Player.invisibility % 5 == 0))
				{
					face = invisible;
				}
			   /*
			    * If in god mode, override all the faces and make this the
			    * face displayed.
			    * 
			    * Otherwise, if it is Nick Cage mode or MLG mode, then 
			    * change the faces to match those themes.
			    */
				else if((Controller.godModeOn || Player.immortality > 0))
				{
					face = godMode;
					
					if(themeNum == 3)
					{
						face = nickGod;
					}
					else if(themeNum == 5)
					{
						face = mlgGod;
					}
				}
			   /*
			    * For nick cage and mlg theme modes, change the face to
			    * be a different picture. 
			    */
				else
				{
					if((themeNum == 3 || themeNum == 5))
					{
						if(phase == 0 || phase == 2)
						{
							if(themeNum == 3)
							{
								face = nickCage;
							}
							else if(themeNum == 5)
							{
								face = mlgFace1;
							}
						}
						else if(phase == 1)
						{
							if(themeNum == 3)
							{
								face = otherNick;
							}
							else if(themeNum == 5)
							{
								face = mlgFace2;
							}
						}
						else
						{
							if(themeNum == 3)
							{
								face = otherNick2;
							}
							else if(themeNum == 5)
							{
								face = mlgFace3;
							}
						}
					}
				}
			}
			
			//Update facePhase (direction face looks etc...)
			facePhase++;
			
			//After reaching 200 ticks, reset the facePhase to 0
			if(facePhase >= 200)
			{
				facePhase = 0;
			}
			
			//Draw face image in GUI
			g.drawImage(face, (WIDTH / 2) - 50, HEIGHT - gC, 100, 100, null);
		}
		catch(Exception e)
		{
		}
		
	   /*
	    * Display the weapon in front of the player and how it looks
	    * depending on what phase of firing the weapon is, and
	    * whether the player is sad or not
	    */
		try
		{
			//Image of weapon shown
			BufferedImage gun = gunNormal;
			
			//Coordinates weapon is rendered on screen
			int x = (WIDTH / 2) - 150;
			int y = HEIGHT - gC - 250;
			
			//Love bow
			if(playerWeapon.weaponID == 0)
			{
				x = (WIDTH / 2);
				
				if(playerWeapon.weaponPhase == 1)
				{
					gun = pistolRight2;
				}
				else if(playerWeapon.weaponPhase == 2)
				{
					gun = pistolRight3;
				}
				else if(playerWeapon.weaponPhase == 3)
				{
					gun = pistolRight4;
				}
				else if(playerWeapon.weaponPhase == 4)
				{
					gun = pistolRight5;
				}
				else
				{
					gun = pistolRight1;
				}
				
				//If dual wielding show second love bow
				if(playerWeapon.dualWield == true)
				{
					BufferedImage gun2 = pistolLeft1;
					
					if(playerWeapon.weaponPhase2 == 1)
					{
						gun2 = pistolLeft2;
					}
					else if(playerWeapon.weaponPhase2 == 2)
					{
						gun2 = pistolLeft3;
					}
					else if(playerWeapon.weaponPhase2 == 3)
					{
						gun2 = pistolLeft4;
					}
					else if(playerWeapon.weaponPhase2 == 4)
					{
						gun2 = pistolLeft5;
					}
					else
					{
						gun2 = pistolLeft1;
					}
					
					//Only draw gun if player is alive
					if(Player.alive)
					{
						g.drawImage(gun2, (WIDTH / 2) - 300, y,
							300, 250, null);
					}
				}
			}
			//Joy Spreader
			else if(playerWeapon.weaponID == 1)
			{
				if(playerWeapon.weaponPhase == 1)
				{
					gun = gunShot;
				}
				else if(playerWeapon.weaponPhase == 2)
				{
					gun = gunShot2;
				}
				else if(playerWeapon.weaponPhase == 3)
				{
					gun = gunShot3;
				}
				else if(playerWeapon.weaponPhase == 4)
				{
					gun = gunShot4;
				}
				else
				{
					gun = gunNormal;
				}
			}
			//Peace Cannon
			else if(playerWeapon.weaponID == 2)
			{
				if(playerWeapon.weaponPhase == 1)
				{
					gun = phaseCannon2;
				}
				else if(playerWeapon.weaponPhase == 2)
				{
					gun = phaseCannon3;
				}
				else if(playerWeapon.weaponPhase == 3)
				{
					gun = phaseCannon4;
				}
				else
				{
					gun = phaseCannon1;
				}
			}
			//Teddy Bear launcher
			else if(playerWeapon.weaponID == 3)
			{
				if(playerWeapon.weaponPhase == 1)
				{
					gun = rocketLauncher1;
				}
				else if(playerWeapon.weaponPhase == 2)
				{
					gun = rocketLauncher2;
				}
				else if(playerWeapon.weaponPhase == 3)
				{
					gun = rocketLauncher3;
				}
				else if(playerWeapon.weaponPhase == 3)
				{
					gun = rocketLauncher4;
				}
				else
				{
					gun = rocketLauncher;
				}
			}
			
			//Weapons is only drawn if player is alive.
			if(Player.alive)
			{
				g.drawImage(gun, x, y, 300, 250, null);
			}
		}
		catch(Exception e)
		{
		}
		
	   /*
	    * Try to render the keys now depending on whether the player has
	    * them or not.
	    */
		if(Player.hasRedKey)
		{
			g.drawImage(redKey, (WIDTH / 2) + 75, HEIGHT - gC + 23, 10, 20, null);
		}
		
		if(Player.hasBlueKey)
		{
			g.drawImage(blueKey, (WIDTH / 2) + 95, HEIGHT - gC + 23, 10, 20, null);
		}
		
		if(Player.hasGreenKey)
		{
			g.drawImage(greenKey, (WIDTH / 2) + 115, HEIGHT - gC + 23, 10, 20, null);
		}
		
		if(Player.hasYellowKey)
		{
			g.drawImage(yellowKey, (WIDTH / 2) + 135, HEIGHT - gC + 23, 10, 20, null);
		}

		// Shows the FPS on the screen if it is activated to show
		if (Controller.showFPS) {
			g.drawString("FPS: " + fps, 20, 50);
		}

		// Shows if fly mode is on
		if (Controller.flyOn) {
			g.drawString("FlyMode on", 400, 50);
		}

		// Shows up if noClip is on
		if (Controller.noClipOn) {
			g.drawString("noClip On", 200, 50);
		}

		// If SuperSpeed is activated
		if (Controller.superSpeedOn) {
			g.drawString("Super Speed On", 200, 100);
		}

		// If GodMode is activated
		if (Controller.godModeOn) {
			g.drawString("God Mode On", 400, 100);
		}
		
		// If Unlimited ammo is activated
		if (Controller.unlimitedAmmoOn) {
			g.drawString("Infinite Ammo On", 600, 50);
		}

		/*
		 * If the player is not dead, show the number of cartridges of ammo the
		 * player has, the amount of ammo the player currently has loaded, and
		 * how much is in the current cartridge. Also show the players health,
		 * armor, and keycards.
		 */
		if (Player.alive) 
		{
			g.drawString("Health: "+Player.health, (WIDTH / 2) - 190,
					HEIGHT - gC + 18);
			
			g.drawString("Armor: "+Player.armor, (WIDTH / 2) - 190,
					HEIGHT - gC + 43);
			
			if(playerWeapon.weaponID == 0)
			{
				g.drawString("Bullet Cartridges: " 
						+playerWeapon.cartridges.size(),
						(WIDTH / 2) - 390, HEIGHT - gC + 43);
				
				g.drawString("Bullets: " + playerWeapon.ammo, 
						(WIDTH / 2) - 390, HEIGHT - gC + 68);
			}
			else if(playerWeapon.weaponID == 1)
			{
				g.drawString("Shell Cartridges: " 
						+playerWeapon.cartridges.size(),
						(WIDTH / 2) - 390, HEIGHT - gC + 43);
				
				g.drawString("Shells: " + playerWeapon.ammo, 
						(WIDTH / 2) - 390,HEIGHT - gC + 68);
			}
			else if(playerWeapon.weaponID == 2)
			{
				g.drawString("Phase Cells: " 
						+playerWeapon.cartridges.size(),
						(WIDTH / 2) - 390, HEIGHT - gC + 43);
				
				g.drawString("Phase Charges: " + playerWeapon.ammo, 
						(WIDTH / 2) - 390,HEIGHT - gC + 68);
			}
			else
			{
				g.drawString("Rocket Casings: " 
						+playerWeapon.cartridges.size(),
						(WIDTH / 2) - 390, HEIGHT - gC + 43);
				
				g.drawString("Rockets: " + playerWeapon.ammo, 
						(WIDTH / 2) - 390,HEIGHT - gC + 68);
			}

			/*
			 * If there are cartridges, show the ammo of the one being currently
			 * used, if not just display that there is 0 available.
			 */
			if (playerWeapon.cartridges.size() != 0) 
			{
				if(playerWeapon.weaponID == 0)
				{
					g.drawString("Bullets in Cartridge: "
							+playerWeapon.cartridges.get(0).ammo, 
							(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
				else if(playerWeapon.weaponID == 1)
				{
					g.drawString("Shells in Casing: "
						+playerWeapon.cartridges.get(0).ammo, 
						(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
				else if(playerWeapon.weaponID == 2)
				{
					g.drawString("Charges in Cell: "
							+playerWeapon.cartridges.get(0).ammo, 
							(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
				else
				{
					g.drawString("Rockets in Casing: "
							+playerWeapon.cartridges.get(0).ammo, 
							(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
			} 
			else 
			{
				if(playerWeapon.weaponID == 0)
				{
					g.drawString("Bullets in Cartridge: 0", 
							(WIDTH / 2) - 390,HEIGHT - gC + 18);
				}
				else if(playerWeapon.weaponID == 1)
				{
					g.drawString("Shells in Casing: 0", 
						(WIDTH / 2) - 390,HEIGHT - gC + 18);
				}
				else if(playerWeapon.weaponID == 2)
				{
					g.drawString("Charges in Cell: 0", 
							(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
				else
				{
					g.drawString("Rockets in Casing: 0", 
							(WIDTH / 2) - 390, HEIGHT - gC + 18);
				}
			}
		}
		else
		{
			g.drawString("PLAYER DIED...", (WIDTH / 2) - 220, HEIGHT - gC + 18);
			
			g.drawString("No Armor "+Player.armor, (WIDTH / 2) - 190,
					HEIGHT - gC + 43);
			
			Controller.moveSpeed = 0.0;
			Player.y = -6;
			
			itemPickup = "Press E to restart level";
			itemPickupTime = 1;
		}

	   /*
	    * In survival show the number of enemies killed that
	    * the player has total.It will never reset like in the actual 
	    * game.
	    * 
	    * Otherwise show secrets found, and the actual map name.
	    */
		if(!Game.setMap)
		{
			g.drawString("Kills: " + kills, 
					(WIDTH / 2) + 200, HEIGHT - gC + 18);
			
			g.drawString("Survival Map", 
					(WIDTH / 2) - 390, HEIGHT - gC + 93);
			
			g.drawString("Enemies: "+Game.enemies.size(), 
					(WIDTH / 2) + 200, HEIGHT - gC + 43);
		}
		else
		{
			g.drawString("Secrets found: "
					+Game.secretsFound+" / "+Game.secretsInMap, 
					(WIDTH / 2) + 200, HEIGHT - gC + 18);
			
			g.drawString(Game.mapName, (WIDTH / 2) - 390, HEIGHT - gC + 93);
			
			
			g.drawString("Kills: "+kills+" / "+Game.enemiesInMap, 
					(WIDTH / 2) + 200, HEIGHT - gC + 43);
		}
		
		
		
	   /*
	    * If an item has been picked up within the last 200 ticks, show 
	    * the item name that was picked up. Or show that a player can't
	    * open a door without the given keyMap if the player tries to
	    * open a door he can't open.
	    * 
	    * Also adds to itemPickupTime each time this is rendered.
	    */
		if(itemPickupTime != 0)
		{
			g.drawString(itemPickup,(WIDTH / 2) + 75, HEIGHT - gC + 93);
			itemPickupTime++;
		}
		
		if(itemPickupTime > 200)
		{
			itemPickupTime = 0;
		}
		
	   /*
	    * Draw other needed texts
	    */
		g.drawString("Keys:", (WIDTH / 2) + 75, HEIGHT - gC + 18);

		/*
		 * Disposes of this graphics object, and show what it was on the screen
		 */
		g.dispose();
		bs.show();
	}

	//Will start the launcher if Display is run instead of start game
	//for some reason
	public static void main(String[] args) 
	{
		// Start the launcher
		new FPSLauncher(0);
	}

	/**
	 * Plays the game audio, depending on the audio the player chose for the
	 * game to play. This gets an audio input stream from an audio file that is
	 * opened using the AudioSystem object's getAudioInputStream method. After
	 * getting the audio stream, and opening it, it then tells the Clip to keep
	 * looping as long as the clip is playing and then it starts the clip, and
	 * set audioOn equal to true. It will only start the clip once therefore and
	 * won't keep restarting it every tick.
	 */
	public synchronized void playAudio(String custom) 
	{
		try
		{
			if (!audioOn) 
			{
				music = AudioSystem.getClip();

				// I instantiate it up here so the code below doesn't sqauwk
				input18 = AudioSystem.getAudioInputStream(this
						.getClass().getResource("/test/gameAudio.wav"));
				
			   /*
			    * If the actual maps are being played instead of the
			    * randomly generated survival maps.
			    */
				if(!Game.setMap)
				{
					if (musicTheme == 0)
					{
						input18 = AudioSystem.getAudioInputStream(this.getClass()
								.getResource("/test/gameAudio.wav"));
					} 
					else if (musicTheme == 1) 
					{
						input18 = AudioSystem.getAudioInputStream(this.getClass()
								.getResource("/test/gameAudio2.wav"));
					}
					else if (musicTheme == 2) 
					{
						input18 = AudioSystem.getAudioInputStream(this.getClass()
								.getResource("/test/e1m3.wav"));
					}
					//MLG theme
					else if (musicTheme == 3)
					{
						input18 = AudioSystem.getAudioInputStream(this.getClass()
								.getResource("/test/gameAudio3.wav"));
					} 
				}
				//Custom music themes
				else
				{
					input18 = AudioSystem.getAudioInputStream(this.getClass()
						.getResource("/test/"+custom+".wav"));
				}
				
				//Open audio input screen
				music.open(input18);
				
				//Have music loop continuously
				music.loop(Clip.LOOP_CONTINUOUSLY);
				
				//Start music
				music.start();
				
				//Audio is on
				audioOn = true;
				
				//Set volume of music
				musicControl= (FloatControl) music.getControl
						(FloatControl.Type.MASTER_GAIN);
				
			   /*
				* If the music volume is as lower than it can go (Meaning off) 
			    * then just set it to the limit. Otherwise just set the
			    * music volume to the correct level.
				*/
				if(FPSLauncher.musicVolumeLevel < -80)
				{
					musicControl.setValue(-80.0f);
				}
				else
				{
					musicControl.setValue(FPSLauncher.musicVolumeLevel);
				}
				
				input18.close();
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
   /**
    * Reset the music by first closing the current music stream and then
    * setting audio to being off. Then re-call playAudio to start a new
    * custom music file.
    * @param custom
    */
	public void resetMusic(String custom)
	{
		try
		{
			music.close();
		}
		catch(Exception e)
		{
			
		}
		
		audioOn = false;

		playAudio(custom);
	}

   /**
    * Loads all the images up for the HUD, and if one fails, it throws
    * an exception.
    * @throws Exception
    */
	public void loadHUD() throws Exception
	{
		//Just reads the image from a file as a BufferedImage
		yellowKey = ImageIO.read
				(new File("Images/yellowKey.png"));
		
		greenKey = ImageIO.read
				(new File("Images/greenKey.png"));
		
		redKey = ImageIO.read
				(new File("Images/redKey.png"));
		
		blueKey = ImageIO.read
				(new File("Images/blueKey.png"));
		
		HUD = ImageIO.read
				(new File("Images/userGUI.png"));
		
		healthyFace1 = ImageIO.read
				(new File("Images/healthyface1.png"));
		
		healthyFace2 = ImageIO.read
				(new File("Images/healthyface2.png"));
		
		healthyFace3 = ImageIO.read
				(new File("Images/healthyface3.png"));
		
		hurtFace1 = ImageIO.read
				(new File("Images/hurtFace1.png"));
		
		hurtFace2 = ImageIO.read
				(new File("Images/hurtFace2.png"));
		
		hurtFace3 = ImageIO.read
				(new File("Images/hurtFace3.png"));
		
		veryHurtFace1 = ImageIO.read
				(new File("Images/veryHurtFace1.png"));
		
		veryHurtFace2 = ImageIO.read
				(new File("Images/veryHurtFace2.png"));
		
		veryHurtFace3 = ImageIO.read
				(new File("Images/veryHurtFace3.png"));
		
		almostDead1 = ImageIO.read
				(new File("Images/almostDeadFace1.png"));
		
		almostDead2 = ImageIO.read
				(new File("Images/almostDeadFace2.png"));
		
		almostDead3 = ImageIO.read
				(new File("Images/almostDeadFace3.png"));
		
		dead = ImageIO.read
				(new File("Images/deadFace.png"));
		
		godMode = ImageIO.read
				(new File("Images/godModeFace.png"));
		
		invisible = ImageIO.read
				(new File("Images/invisibilityMode.png"));
		
		playerHarmedHealthy = ImageIO.read
				(new File("Images/healthyHarmed.png"));
		
		playerHarmedHurt = ImageIO.read
				(new File("Images/hurtHarmed.png"));
		
		playerHarmedVeryHurt = ImageIO.read
				(new File("Images/veryHurtHarmed.png"));
		
		playerHarmedAlmostDead = ImageIO.read
				(new File("Images/almostDeadHarmed.png"));
		
		invisGodmode = ImageIO.read
				(new File("Images/invisibleGodmode.png"));
		
		nickCage = ImageIO.read
				(new File("Images/nickCage.png"));
		
		otherNick = ImageIO.read
				(new File("Images/otherNick.png"));
		
		otherNick2 = ImageIO.read
				(new File("Images/otherNick2.png"));
		
		deadNick = ImageIO.read
				(new File("Images/deadNick.png"));
		
		nickGod = ImageIO.read
				(new File("Images/nickGod.png"));
		
		mlgGod = ImageIO.read
				(new File("Images/mlgGod.png"));
		
		mlgFace1 = ImageIO.read
				(new File("Images/mlg1.png"));
		
		mlgFace2 = ImageIO.read
				(new File("Images/mlg2.png"));
		
		mlgFace3 = ImageIO.read
				(new File("Images/mlg3.png"));
		
		mlgDead = ImageIO.read
				(new File("Images/mlgDead.png"));
		
		gunNormal = ImageIO.read
				(new File("Images/gunNormal.png"));
		
	   /*
	    * Goes through an entire image, and for any white pixels found on
	    * the image, make them translucent.
	    */
		for (int x = 0; x < gunNormal.getWidth(); ++x)
		{
			for (int y = 0; y < gunNormal.getHeight(); ++y)
			{
				//Finds white pixels and makes them translucent 
			    if ((gunNormal.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			    {
			        gunNormal.setRGB(x, y, 0);
			    }
			}
		}
		
		gunShot = ImageIO.read
				(new File("Images/gunShot.png"));
		
		//Same as the loop above
		for (int x = 0; x < gunShot.getWidth(); ++x)
		{
			for (int y = 0; y < gunShot.getHeight(); ++y)
			{
			   if ((gunShot.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       gunShot.setRGB(x, y, 0);
			   }
			}
		}
		
		gunShot2 = ImageIO.read
				(new File("Images/gunShot2.png"));
		
		for (int x = 0; x < gunShot2.getWidth(); ++x)
		{
			for (int y = 0; y < gunShot2.getHeight(); ++y)
			{
			   if ((gunShot2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       gunShot2.setRGB(x, y, 0);
			   }
			}
		}
		
		gunShot3 = ImageIO.read
				(new File("Images/gunShot3.png"));
		
		for (int x = 0; x < gunShot3.getWidth(); ++x)
		{
			for (int y = 0; y < gunShot3.getHeight(); ++y)
			{
			   if ((gunShot3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       gunShot3.setRGB(x, y, 0);
			   }
			}
		}
		
		gunShot4 = ImageIO.read
				(new File("Images/gunShot4.png"));
		
		for (int x = 0; x < gunShot4.getWidth(); ++x)
		{
			for (int y = 0; y < gunShot4.getHeight(); ++y)
			{
			   if ((gunShot4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       gunShot4.setRGB(x, y, 0);
			   }
			}
		}
		
		phaseCannon1 = ImageIO.read
				(new File("Images/phaseCannon1.png"));
		
	   /*
	    * For the Peace cannon, due to there being too much
	    * white on the original image, I set this too take
	    * black pixels and make them translucent instead.
	    */
		for (int x = 0; x < phaseCannon1.getWidth(); ++x)
		{
			for (int y = 0; y < phaseCannon1.getHeight(); ++y)
			{
			   if ((phaseCannon1.getRGB(x, y) & 0x00FFFFFF) == 0x000000) 
			   {
			       phaseCannon1.setRGB(x, y, 0);
			   }
			}
		}
		
		phaseCannon2 = ImageIO.read
				(new File("Images/phaseCannon2.png"));
		
		for (int x = 0; x < phaseCannon2.getWidth(); ++x)
		{
			for (int y = 0; y < phaseCannon2.getHeight(); ++y)
			{
			   if ((phaseCannon2.getRGB(x, y) & 0x00FFFFFF) == 0x000000) 
			   {
			       phaseCannon2.setRGB(x, y, 0);
			   }
			}
		}
		
		phaseCannon3 = ImageIO.read
				(new File("Images/phaseCannon3.png"));
		
		for (int x = 0; x < phaseCannon3.getWidth(); ++x)
		{
			for (int y = 0; y < phaseCannon3.getHeight(); ++y)
			{
			   if ((phaseCannon3.getRGB(x, y) & 0x00FFFFFF) == 0x000000) 
			   {
			       phaseCannon3.setRGB(x, y, 0);
			   }
			}
		}
		
		phaseCannon4 = ImageIO.read
				(new File("Images/phaseCannon4.png"));
		
		for (int x = 0; x < phaseCannon4.getWidth(); ++x)
		{
			for (int y = 0; y < phaseCannon4.getHeight(); ++y)
			{
			   if ((phaseCannon4.getRGB(x, y) & 0x00FFFFFF) == 0x000000) 
			   {
			       phaseCannon4.setRGB(x, y, 0);
			   }
			}
		}
		
		rocketLauncher = ImageIO.read
				(new File("Images/rocketLauncher.png"));
		
		for (int x = 0; x < rocketLauncher.getWidth(); ++x)
		{
			for (int y = 0; y < rocketLauncher.getHeight(); ++y)
			{
			   if ((rocketLauncher.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       rocketLauncher.setRGB(x, y, 0);
			   }
			}
		}
		
		rocketLauncher1 = ImageIO.read
				(new File("Images/rocketLauncher1.png"));
		
		for (int x = 0; x < rocketLauncher1.getWidth(); ++x)
		{
			for (int y = 0; y < rocketLauncher1.getHeight(); ++y)
			{
			   if ((rocketLauncher1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       rocketLauncher1.setRGB(x, y, 0);
			   }
			}
		}
		
		rocketLauncher2 = ImageIO.read
				(new File("Images/rocketLauncher2.png"));
		
		for (int x = 0; x < rocketLauncher2.getWidth(); ++x)
		{
			for (int y = 0; y < rocketLauncher2.getHeight(); ++y)
			{
			   if ((rocketLauncher2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       rocketLauncher2.setRGB(x, y, 0);
			   }
			}
		}
		
		rocketLauncher3 = ImageIO.read
				(new File("Images/rocketLauncher3.png"));
		
		for (int x = 0; x < rocketLauncher3.getWidth(); ++x)
		{
			for (int y = 0; y < rocketLauncher3.getHeight(); ++y)
			{
			   if ((rocketLauncher3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       rocketLauncher3.setRGB(x, y, 0);
			   }
			}
		}
		
		rocketLauncher4 = ImageIO.read
				(new File("Images/rocketLauncher4.png"));
		
		for (int x = 0; x < rocketLauncher4.getWidth(); ++x)
		{
			for (int y = 0; y < rocketLauncher4.getHeight(); ++y)
			{
			   if ((rocketLauncher4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       rocketLauncher4.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolLeft1 = ImageIO.read
				(new File("Images/pistolLeft1.png"));
		
		for (int x = 0; x < pistolLeft1.getWidth(); ++x)
		{
			for (int y = 0; y < pistolLeft1.getHeight(); ++y)
			{
			   if ((pistolLeft1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolLeft1.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolLeft2 = ImageIO.read
				(new File("Images/pistolLeft2.png"));
		
		for (int x = 0; x < pistolLeft2.getWidth(); ++x)
		{
			for (int y = 0; y < pistolLeft2.getHeight(); ++y)
			{
			   if ((pistolLeft2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolLeft2.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolLeft3 = ImageIO.read
				(new File("Images/pistolLeft3.png"));
		
		for (int x = 0; x < pistolLeft3.getWidth(); ++x)
		{
			for (int y = 0; y < pistolLeft3.getHeight(); ++y)
			{
			   if ((pistolLeft3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolLeft3.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolLeft4 = ImageIO.read
				(new File("Images/pistolLeft4.png"));
		
		for (int x = 0; x < pistolLeft4.getWidth(); ++x)
		{
			for (int y = 0; y < pistolLeft4.getHeight(); ++y)
			{
			   if ((pistolLeft4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolLeft4.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolLeft5 = ImageIO.read
				(new File("Images/pistolLeft5.png"));
		
		for (int x = 0; x < pistolLeft5.getWidth(); ++x)
		{
			for (int y = 0; y < pistolLeft5.getHeight(); ++y)
			{
			   if ((pistolLeft5.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolLeft5.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolRight1 = ImageIO.read
				(new File("Images/pistolRight1.png"));
		
		for (int x = 0; x < pistolRight1.getWidth(); ++x)
		{
			for (int y = 0; y < pistolRight1.getHeight(); ++y)
			{
			   if ((pistolRight1.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolRight1.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolRight2 = ImageIO.read
				(new File("Images/pistolRight2.png"));
		
		for (int x = 0; x < pistolRight2.getWidth(); ++x)
		{
			for (int y = 0; y < pistolRight2.getHeight(); ++y)
			{
			   if ((pistolRight2.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolRight2.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolRight3 = ImageIO.read
				(new File("Images/pistolRight3.png"));
		
		for (int x = 0; x < pistolRight3.getWidth(); ++x)
		{
			for (int y = 0; y < pistolRight3.getHeight(); ++y)
			{
			   if ((pistolRight3.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolRight3.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolRight4 = ImageIO.read
				(new File("Images/pistolRight4.png"));
		
		for (int x = 0; x < pistolRight4.getWidth(); ++x)
		{
			for (int y = 0; y < pistolRight4.getHeight(); ++y)
			{
			   if ((pistolRight4.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolRight4.setRGB(x, y, 0);
			   }
			}
		}
		
		pistolRight5 = ImageIO.read
				(new File("Images/pistolRight1.png"));
		
		for (int x = 0; x < pistolRight5.getWidth(); ++x)
		{
			for (int y = 0; y < pistolRight5.getHeight(); ++y)
			{
			   if ((pistolRight5.getRGB(x, y) & 0x00FFFFFF) == 0xFFFFFF) 
			   {
			       pistolRight5.setRGB(x, y, 0);
			   }
			}
		}
	}
	
   /**
    * The only way any other class can restart survival mode. So this
    * method is called to do so.
    */
	public void restartSurvival()
	{
		//Starts a new game in survival mode
		game = new Game(this, false, "");
		
		//Resets enemies made happy
		kills = 0;
	}
}
