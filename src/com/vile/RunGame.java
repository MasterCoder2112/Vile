package com.vile;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Title: RunGame
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 *
 * Sets up the frame of the game, the cursor, and instantiates the
 * new display object.
 */
public class RunGame 
{
	public static JFrame frame;
	
	public RunGame()
	{
		/*
		    * Sets up image of cursor, its width and height, and it's type of 
		    * BufferedImage.
		    */
			BufferedImage cursor = new BufferedImage
					(16, 16, BufferedImage.TYPE_INT_ARGB);
			
		   /*
		    * Sets up a new cursor of custom type to be used. This cursor is 
		    * blank, meaning it is completely see through and won't be seen
		    * in the way of the graphics when playing the game.
		    */
			Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor
					(cursor, new Point(0, 0), "blank");
			
		   /*
		    * Self explanitory stuff
		    */
			Display game = new Display();
			frame = new JFrame(Display.TITLE);
			frame.add(game);
			frame.setSize(Display.WIDTH, Display.HEIGHT);
			frame.getContentPane().setCursor(blank);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			
			//If full screen
			if(Display.graphicsSelection >= 4)
			{
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				frame.setUndecorated(true);
			}
			
			//Gets rid of java icon and replaces it with the Vile icon
			ImageIcon titleIcon = new ImageIcon("Images/titleIcon.png");
			frame.setIconImage(titleIcon.getImage());
			
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);

			//Start the game.
			game.start();
	}
}
