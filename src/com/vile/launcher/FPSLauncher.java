package com.vile.launcher;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.vile.Display;
import com.vile.Game;
import com.vile.RunGame;
import com.vile.Sound;

/**
 * @title  FPSLauncher
 * @author Alex Byrd
 * @modified 4/26/2017
 * Description:
 * Starts the program off with the main menu. This holds the title
 * theme music, the Start New Game button, the Options menu to
 * set how you want your game to be, the Controls, and the Quit button. 
 *
 */
public class FPSLauncher extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//Version of the main menu
	private static final double versionNumber = 0.9;
	
	//Music clip
	private static Clip clip;
	
	//Click sound for main menu only
	private static Sound click;
	
	private static boolean audioOn = false;
	
	//Controls Gain (Volume) for both music and sounds
	private static FloatControl musicControl;
	
	/* Java Swing elements **********************************/
	private JLayeredPane panel = new JLayeredPane();
	
	private JButton play;
	private JButton playGame;
	private JButton options;
	private JButton controls;
	private JButton quit;
	private JButton back1;
	private JButton back2;
	private JButton mouse;
	private JButton returnToGame;
	private JButton customMap;
	private JButton sFPS;
	
	private JLabel  resolutionTitle;
	private JLabel  themeTitle;
	private JLabel  levelSizeTitle;
	private JLabel  modeTitle;
	private JLabel  musicTitle;
	private JLabel  musicVolumeTitle;
	private JLabel  soundVolumeTitle;
	private JLabel  title;
	private JLabel  newMapTitle;
	private JLabel  mouseSensitivityTitle;
	
	private JTextArea readMeText;
	private JTextField newMapName;
	/*********************************************End elements*/
	
	private ImageIcon titleImage;
	
	//Has scroll box you can put choices on
	private Choice resolution = new Choice();
	private Choice theme      = new Choice();
	private Choice levelSize  = new Choice();
	private Choice mode       = new Choice();
	private Choice music      = new Choice();
	
	//Volume Knobs
	public JSlider musicVolume;
	public JSlider soundVolume;
	public JSlider mouseSensitivity;
	
   /*
    * Level of sound in decibals above or below the original set amount 
    * by the computer. 
    */
	public static float  musicVolumeLevel = -20.0f;
	public static float  soundVolumeLevel = -20.0f;
	public static float  mouseSensitivityLevel = 100;
	
	//Whether sound files have been opened yet
	private static boolean opened      = false;
	
	//Is mouse option on
	private static boolean mouseStatus = true;
	
	//Is custom map option on?
	private static boolean nonDefaultMap = false;
	
	//Is smooth FPS on?
	public static boolean smoothFPS = false;
	
	//Width and height of the Jframe here
	public static final int WIDTH  = 800;
	public static final int HEIGHT = 400;
	
	//Default starting map name
	public static String startMap = "map1";
	
   /*
    * Instantiates a new ActionListener that listens to whether Buttons 
    * are clicked or not.
    */
	ActionListener aL = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			//Call the play audio method to play the sound
			click.playAudioFile();	
			
			//If Survival mode is pressed then close the main menu
			//music, dispose of the menu, and start new game
			if(e.getSource() == play)
			{
				clip.close();
				audioOn = false;
				dispose();
				Game.setMap = false;
				new RunGame();
			}
			
			//Same stuff as above but start a new game with either a
			//custom map or the default map
			if(e.getSource() == playGame)
			{
				clip.close();
				audioOn = false;
				dispose();
				Game.setMap = true;
				
				//If custom map, send these values so the game knows
				//what to load up
				Display.nonDefaultMap = nonDefaultMap;
				Display.newMapName = startMap;
				
				Display.themeNum = 2;
				new RunGame();
			}
			
			//Start up options menu
			if(e.getSource() == options)
			{
				dispose();
				new Options();
			}
			
			//Start up controls menu
			if(e.getSource() ==  controls)
			{
				dispose();
				new Controls();
			}
			
			//Quit game
			if(e.getSource() == quit)
			{
				System.exit(0);
			}
			
			//Save options menu changes and go back to menu screen
			if(e.getSource() == back1)
			{
				Display.graphicsSelection = resolution.getSelectedIndex();
				Display.themeNum          = theme.getSelectedIndex();
				Display.levelSize         = levelSize.getSelectedIndex();
				Display.mouseOn           = mouseStatus;
				Display.skillMode         = mode.getSelectedIndex();
				Display.musicTheme        = music.getSelectedIndex();
				Display.smoothFPS         = smoothFPS;
				startMap                  = newMapName.getText();
				dispose();
				new FPSLauncher(0);
			}
			
			//Return to current game
			if(e.getSource() == returnToGame)
			{
				Display.resetGame = true;
				clip.close();
				audioOn = false;
				dispose();
				new RunGame();
			}
			
			//Go back to menu screen
			if(e.getSource() == back2)
			{
				dispose();
				new FPSLauncher(0);
			}
			
			//Turn mouse on or off
			if(e.getSource() == mouse)
			{
				if(mouseStatus)
				{
					mouseStatus = false;
					mouse.setText("Mouse Status: Off");
				}
				else
				{
					mouseStatus = true;
					mouse.setText("Mouse Status: On");
				}
			}
			
			//Turn smooth fps on or off
			if(e.getSource() == sFPS)
			{
				if(smoothFPS)
				{
					smoothFPS = false;
					sFPS.setText("Smooth FPS: Off");
				}
				else
				{
					smoothFPS = true;
					sFPS.setText("Smooth FPS: On");
				}
			}
			
			//Custom map loading is on or off
			if(e.getSource() == customMap)
			{
				if(nonDefaultMap)
				{
					nonDefaultMap = false;
					customMap.setText("Custom Map: Off");
				}
				else
				{
					nonDefaultMap = true;
					customMap.setText("Custom Map: On");
				}
			}
		}
	};
	
   /*
    * A listener that listens for any change of the volume knob. If
    * the state of the knob is changed, the method is passed an event
    * which then performs a serious of operations that the user 
    * programs it to do. In this case, the clip is played at a new
    * volume, and the tool tip on the volume knob is updated to show the
    * new volume level.
    */
	ChangeListener change = new ChangeListener()
	{
		@Override
		public void stateChanged(ChangeEvent e) 
		{
			if(e.getSource() == musicVolume)
			{
				float newVolume = musicVolume.getValue();
				musicVolumeLevel     = newVolume;
				
				//If newVolume is less than the volume limit, set it to limit
				if(newVolume < -80)
				{
					newVolume = -80;
				}
				
				musicControl.setValue(newVolume);	
				
				//Only do if there is music playing
				if(Display.audioOn)
				{
					   /*
					    * If the music volume is as lower than it can go (Meaning off) 
					    * then just set it to the limit. Otherwise just set the
					    * music volume to the correct level.
					    */
						if(musicVolumeLevel < -80)
						{
							Display.musicControl.setValue(-80.0f);
						}
						else
						{
							Display.musicControl.setValue(musicVolumeLevel);
						}
				}
				
				//This is the volume level that makes since to us
				int temp  = (int) musicVolumeLevel + 94;
				musicVolume.setToolTipText("Music Volume Level: "+temp);
			}

			//Change sound volume as slider is moved
			if(e.getSource() == soundVolume)
			{
				//Gets new sound volume from slider
				float newVolume2 = soundVolume.getValue();
				soundVolumeLevel     = newVolume2;
			
				//If newVolume is less than the volume limit, set it to limit
				if(newVolume2 < -80)
				{
					newVolume2 = -80;
				}

				//Resets sound volumes
				click.resetVolume(newVolume2);

				//Show the volume level that makes since to us
				int temp2  = (int) soundVolumeLevel + 94;
				soundVolume.setToolTipText("Sound Volume Level: "+temp2);
			}

			//Change for mouse sensitivity
			if(e.getSource() == mouseSensitivity)
			{
				mouseSensitivityLevel =  mouseSensitivity.getValue();
				System.out.println(mouseSensitivityLevel);
				soundVolume.setToolTipText("Mouse Sensitivity: "+ mouseSensitivityLevel);
			}
		}
	};
	
   /**
    * Starts the specific launcher depending on its idNum. It could be
    * the pause menu, the main menu, the options menu, etc...
    * @param idNum
    */
	public FPSLauncher(int idNum)
	{
	   /*
	    * If the game was running, dispose of its frame so that the
	    * pause menu can show
	    */
		try
		{
			RunGame.frame.dispose();
		}
		catch(Exception e)
		{
			
		}
		
		//Title of menu depending on whether game is paused or not
		if(!Display.pauseGame)
		{
			setTitle("Vile Launcher (Version: "+versionNumber+")");
		}
		else
		{
			setTitle("Pause Menu");
		}
		
		//Self explainitory shtuff
		setSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(panel);
		setLocationRelativeTo(null);	
		setResizable(false);
		setVisible(false);
		
		//No default layout, this is so i can set positions manually
		panel.setLayout(null);
		
		//If normal menu, not options or controls, set up default buttons
		if(idNum == 0)
		{
			drawLauncherButtons();
			drawBackground();
		}	
	
		
	   /*
	    * Try to open up the title theme audio clip and let it loop
	    * continuously
	    */
		try
		{
			if(!audioOn && !Display.pauseGame)
			{
				clip = AudioSystem.getClip();
				AudioInputStream input = AudioSystem.getAudioInputStream
						(this.getClass().getResource("/test/title.wav"));
				clip.open(input);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				clip.start();
				audioOn = true;
			}
		}
		catch (Exception e)
		{
			
		}
		
		
	   /*
	    * A FloatControl object is something that allows you to control 
	    * certain values of a sound object such as a clip, but in float
	    * form so that it is more accurate. The type MASTER_GAIN gets the
	    * volume level in decibals of the sound clip. So basically this
	    * just gains control of the decibal level of the sound clip.
	    */
		musicControl = (FloatControl) clip.getControl
			    		(FloatControl.Type.MASTER_GAIN);
		
	   /*
	    * Sets the value of a given control. In this case it is a 
	    * FloatControl that determines the volume level of the clip in
	    * decibals. The f after the number determines that the number
	    * is indeed a float and not a double. The highest the number 
	    * can be raised in terms of decibals is 6. The decibals can
	    * pretty much be decreased infinitely low though. 80 is usually
	    * all you need to complete get rid of the sound.
	    * 
	    * If the volume level is less than -80, just set the volume to
	    * -80. Otherwise, set the volume to the correct volume level.
	    */
		if(musicVolumeLevel < -80)
		{
			musicControl.setValue(-80.0f);
		}
		else
		{
			musicControl.setValue(musicVolumeLevel);
		}

		if (!opened) 
		{
			click = new Sound(20, "/test/titleClick.wav");

			opened = true;
		}
		
		//Changes Java Icon to the new Vile Icon
		ImageIcon titleIcon = new ImageIcon("Images/titleIcon.png");
		setIconImage(titleIcon.getImage());
		
		setVisible(true);
	}
  
   /**
    * Draws all the buttons that show when the launcher first opens
    * so that the play can choose to play the game, or open up the
    * options menu, or the controls menu, etc...
    * 
    * Most of this is self explainitory Java swing stuff and because
    * I've been commenting all day and have to get this version out
    * I'm not even going to try to comment all this simple stuff.
    */
	private void drawLauncherButtons()
	{		
		//Colors of buttons when mouse is not over them or when it is
		Color defaultColor = Color.RED;
		Color mouseColor = Color.GREEN;
		
		play = new JButton("Survival Mode");
		
		play.setBounds(0, 250, 295, 40);
		
		//Button blends with background
		play.setOpaque(false);
		play.setContentAreaFilled(false);
		play.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		play.setFocusPainted(false);
		
		//Sets font type, mode, and size
		play.setFont(new Font("Nasalization", Font.BOLD, 24));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		play.setFont(play.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		play.setForeground(defaultColor);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		play.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        play.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        play.setForeground(defaultColor);
		    }
		});
		
		play.addActionListener(aL);
		panel.add(play);

		playGame   = new JButton("New Game");
		playGame.setBounds(0, 100, 270, 40);
		
		playGame.setOpaque(false);
		playGame.setContentAreaFilled(false);
		playGame.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		playGame.setFocusPainted(false);
		
		playGame.setFont(new Font("Nasalization", Font.BOLD, 24));
		playGame.setFont(playGame.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		playGame.setForeground(defaultColor);
		
		playGame.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        playGame.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        playGame.setForeground(defaultColor);
		    }
		});
		
		playGame.addActionListener(aL);
		panel.add(playGame);
		
		returnToGame     = new JButton("Return to game");
		returnToGame.setBounds(500, 240, 300, 50);
		
		//Button blends with background
		returnToGame.setOpaque(false);
		returnToGame.setContentAreaFilled(false);
		returnToGame.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		returnToGame.setFocusPainted(false);
		
		//Sets font type, mode, and size
		returnToGame.setFont(new Font("Nasalization", Font.BOLD, 24));
		
		returnToGame.setForeground(defaultColor);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		returnToGame.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	returnToGame.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	returnToGame.setForeground(defaultColor);
		    }
		});
				
		returnToGame.addActionListener(aL);
		
		//If game is paused, show that you can return to the game
		if(Display.pauseGame)
		{
			panel.add(returnToGame);
		}
		
		options  = new JButton("Options");
		options.setBounds(0, 150, 230, 40);
		
		//Button blends with background
		options.setOpaque(false);
		options.setContentAreaFilled(false);
		options.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		options.setFocusPainted(false);
		
		//Sets font type, mode, and size
		options.setFont(new Font("Nasalization", Font.BOLD, 24));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		options.setFont(options.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		options.setForeground(defaultColor);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		options.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        options.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        options.setForeground(defaultColor);
		    }
		});
		
		options.addActionListener(aL);
		panel.add(options);
		
		controls  = new JButton("Controls");
		controls.setBounds(0, 200, 235, 40);
		
		//Button blends with background
		controls.setOpaque(false);
		controls.setContentAreaFilled(false);
		controls.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		controls.setFocusPainted(false);
		
		//Sets font type, mode, and size
		controls.setFont(new Font("Nasalization", Font.BOLD, 24));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		controls.setFont(controls.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		controls.setForeground(defaultColor);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		controls.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        controls.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        controls.setForeground(defaultColor);
		    }
		});
		
		controls.addActionListener(aL);
		panel.add(controls);
		
		quit      = new JButton("Quit");
		quit.setBounds(0, 300, 180, 40);
		
		//Button blends with background
		quit.setOpaque(false);
		quit.setContentAreaFilled(false);
		quit.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		quit.setFocusPainted(false);
		
		//Sets font type, mode, and size
		quit.setFont(new Font("Nasalization", Font.BOLD, 24));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		quit.setFont(quit.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		quit.setForeground(defaultColor);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		quit.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        quit.setForeground(mouseColor);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        quit.setForeground(defaultColor);
		    }
		});
		
		quit.addActionListener(aL);
		panel.add(quit);
		
		//Refreashes title screen to show all buttons and title perfectly
		panel.repaint();
	}
	
   /**
    * Draws up all the JButtons, Choice boxes, and textfields 
    * within the options menu when you pull it up.
    */
	public void drawOptionsMenu()
	{
		back1     = new JButton("Back To Main Menu");
		back1.setBounds(0, 325, 300, 40);
		
		//Button blends with background
		back1.setOpaque(false);
		back1.setContentAreaFilled(false);
		back1.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		back1.setFocusPainted(false);
		
		//Sets font type, mode, and size
		back1.setFont(new Font("Nasalization", Font.BOLD, 18));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		back1.setFont(back1.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		back1.setForeground(Color.RED);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		back1.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        back1.setForeground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        back1.setForeground(Color.RED);
		    }
		});
		
		back1.addActionListener(aL);
		panel.add(back1);
		
		if(smoothFPS)
		{
			sFPS      = new JButton("Smooth FPS: On");
		}
		else
		{
			sFPS      = new JButton("Smooth FPS: Off");
		}
		
		sFPS.setBounds(0, 275, 300, 40);
		
		//Button blends with background
		sFPS.setOpaque(false);
		sFPS.setContentAreaFilled(false);
		sFPS.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		sFPS.setFocusPainted(false);
		
		//Sets font type, mode, and size
		sFPS.setFont(new Font("Nasalization", Font.BOLD, 18));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		sFPS.setFont(sFPS.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		sFPS.setForeground(Color.RED);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		sFPS.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        sFPS.setForeground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        sFPS.setForeground(Color.RED);
		    }
		});
		
		sFPS.addActionListener(aL);
		panel.add(sFPS);
		
		if(mouseStatus)
		{
			mouse     = new JButton("Mouse Status: On");
		}
		else
		{
			mouse     = new JButton("Mouse Status: Off");
		}
		
		mouse.setBounds(300, 275, 250, 40);
		
		//Button blends with background
		mouse.setOpaque(false);
		mouse.setContentAreaFilled(false);
		mouse.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		mouse.setFocusPainted(false);
		
		//Sets font type, mode, and size
		mouse.setFont(new Font("Nasalization", Font.BOLD, 18));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		mouse.setFont(mouse.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		mouse.setForeground(Color.RED);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		mouse.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        mouse.setForeground(Color.GREEN);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        mouse.setForeground(Color.RED);
		    }
		});
		
		mouse.addActionListener(aL);
		panel.add(mouse);
		
		if(nonDefaultMap)
		{
			customMap     = new JButton("Custom Map: On");
		}
		else
		{
			customMap     = new JButton("Custom Map: Off");
		}
		
		customMap.setBounds(300, 325, 250, 40);
		
		//Button blends with background
		customMap.setOpaque(false);
		customMap.setContentAreaFilled(false);
		customMap.setBorderPainted(false);
		
		//Removes textbox focus so the textbox border is removed
		customMap.setFocusPainted(false);
		
		//Sets font type, mode, and size
		customMap.setFont(new Font("Nasalization", Font.BOLD, 18));
		
		//Uses a bitwise operator to merge the fonts of bold and italic
		//for the text
		customMap.setFont(customMap.getFont()
				.deriveFont(Font.BOLD | Font.ITALIC));
		
		customMap.setForeground(Color.RED);
		
		//Listens for whether the mouse has entered or exited the button
		//area and if it has or has not, change color accordingly
		customMap.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        customMap.setForeground(Color.GREEN);
		    }

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
		
		//This Swing component allows a slide down menu with all the
		//options you add here to choose from.
		resolution.setBounds(50, 50, 200, 40);
		resolution.add("800 x 600 Low Res");
		resolution.add("800 x 600 High Res");
		resolution.add("1020 x 700 Low Res");
		resolution.add("1020 x 700 High Res");
		resolution.add("Fullscreen Low Res");
		resolution.add("Fullscreen High Res");
		resolution.select(Display.graphicsSelection);
		panel.add(resolution);	
		
		themeTitle = new JLabel("Themes:");
		themeTitle.setBounds(250, 100, 200, 10);
		themeTitle.setForeground(Color.GREEN);
		panel.add(themeTitle);
		
		theme.setBounds(175, 125, 200, 40);
		theme.add("Outdoors");
		theme.add("Night Time");
		theme.add("Level Default");
		theme.add("Nick Cage");
		theme.add("Moon");
		theme.add("MLG");
		theme.select(Display.themeNum);
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
		levelSize.select(Display.levelSize);
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
		mode.select(Display.skillMode);
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
	    * If musicTheme is not one of the normal themes (because the 
	    * game theme is mlg or koester) then just have it select the
	    * 2nd music choice. If the music theme is one of the first
	    * 4 themes available (including music off) then have that
	    * selected as is.
	    */
		if(Display.musicTheme > 2)
		{
			music.select(2);
		}
		else
		{
			music.select(Display.musicTheme);
		}
		
		panel.add(music);
		
		musicVolumeTitle = new JLabel("Music Volume:");
		musicVolumeTitle.setBounds(550, 200, 200, 10); 
		musicVolumeTitle.setForeground(Color.GREEN);
		panel.add(musicVolumeTitle);
		
		musicVolume     = new JSlider();
		musicVolume.setBounds(550, 220, 200, 40);
		musicVolume.setMaximum(6);
		musicVolume.setMinimum(-94);
		musicVolume.setValue((int) musicVolumeLevel); 
		musicVolume.setOpaque(true);
		
		int temp  = (int) musicVolumeLevel + 94;
		musicVolume.setToolTipText("Music Volume Level: "+temp);
		
		musicVolume.addChangeListener(change);
		panel.add(musicVolume);
		
		
		soundVolumeTitle = new JLabel("Sound Volume:");
		soundVolumeTitle.setBounds(550, 100, 200, 10); 
		soundVolumeTitle.setForeground(Color.GREEN);
		panel.add(soundVolumeTitle);
		
		soundVolume     = new JSlider();
		soundVolume.setBounds(550, 120, 200, 40);
		soundVolume.setMaximum(6);
		soundVolume.setMinimum(-94);
		soundVolume.setValue((int) soundVolumeLevel); 
		soundVolume.setOpaque(true);
		
		int temp2  = (int) soundVolumeLevel + 94;
		soundVolume.setToolTipText("Sound Volume Level: "+temp2);
		
		soundVolume.addChangeListener(change);
		panel.add(soundVolume);

		mouseSensitivityTitle = new JLabel("Mouse Sensitivity");
		mouseSensitivityTitle.setBounds(550, 300, 200, 10);
		mouseSensitivityTitle.setForeground(Color.GREEN);
		panel.add(mouseSensitivityTitle);

		mouseSensitivity     = new JSlider();
		mouseSensitivity.setBounds(550, 320, 200, 40);
		mouseSensitivity.setMaximum(100);
		mouseSensitivity.setMinimum(1);
		mouseSensitivity.setValue((int) mouseSensitivityLevel);
		mouseSensitivity.setOpaque(true);

		mouseSensitivity.setToolTipText("Mouse Sensitivity: "+ mouseSensitivityLevel);

		mouseSensitivity.addChangeListener(change);
		panel.add(mouseSensitivity);

		//Title for custom map text field
		newMapTitle = new JLabel("Custom Map:");
		newMapTitle.setBounds(WIDTH - 250, 25, 200, 25); 
		newMapTitle.setForeground(Color.GREEN);
		panel.add(newMapTitle);
		
		//Textfield to load up a custom map if you want
		newMapName = new JTextField();
		newMapName.setBounds(WIDTH - 250, 50, 200, 25);
		newMapName.setText(startMap);
		newMapName.setEditable(true);
		panel.add(newMapName);
	}
	
   /**
    * Draws the JTextArea that shows all the controls the game has to
    * offer in the controls menu.
    */
	public void drawControlsButtons()
	{
	   /*
	    * A JTextArea is much better than a JTextField because this allows
	    * for Text to be on multiple lines and also wrap around if there is
	    * not enough room on one line. That is why I use it to hold all
	    * this text here. It just works.
	    */
		readMeText = new JTextArea();
		readMeText.setBounds(0, 0, WIDTH, 300);
		readMeText.setText("Foward = W, Backwards = S, Strafe Left = A,"
				+ " Strafe Right = D, Turn left = Left arrow,"
				+ " Turn right = Right arrow, Run = Shift, Crouch = C,"
				+ "Jump = Space, Show FPS = F,"
				+ " Look up = Up Arrow, Look down = Down arrow,"
				+ " Noclip = N, Super Speed = P,"
				+ " Reload = R, Fly mode = O, God Mode = G, Shoot = V,"
				+ " Pause = ESC, E = Use, Give all weapons = L,"
				+ " Unlimited Ammo = K. "
				+ " If using the mouse, moving it any direction will"
				+ " move the camera, and clicking shoots. There will"
				+ " eventually be a way to change"
				+ " the controls you use in game, but not yet...");
		readMeText.setEditable(false);
		readMeText.setLineWrap(true);
		readMeText.setWrapStyleWord(true);
		panel.add(readMeText);
		
		back2     = new JButton("Back To Main Menu");
		back2.setBounds(300, 300, 200, 40);
		back2.addActionListener(aL);
		panel.add(back2);
	}
	
   /**
    * Draws background of menu. Always draws it last so that it is behind
    * all the buttons and text every time.
    */
	public void drawBackground()
	{
		title = new JLabel();
		title.setBounds(0, 0, WIDTH, HEIGHT);
		titleImage = new ImageIcon("Images/title.png");
		title.setIcon(titleImage);
		panel.add(title, new Integer(0));
		
		//Reset panel after drawing the background
		panel.repaint();
	}
}
