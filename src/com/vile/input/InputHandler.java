package com.vile.input;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import com.vile.Display;
import com.vile.RunGame;

/**
 * Title: InputHandler
 * @author Alex Byrd
 * Date Updated: 5/02/2017
 * 
 * Description:
 * Deals with all the input devices such as the keyboard and mouse, using
 * listeners. It then instantiates all the required listener
 * methods and deals with events accordingly.
 * 
 * The listeners are interfaces.
 *
 * This is always updated to make the mouse less and less buggy. It is
 * still even now a little twitchy, but it at least stays in the center
 * of the screen.
 */
public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener 
{
	//All the possible combinations on the keyboard
	public boolean[] key = new boolean[68836];
	
   /*
    * Mouse position on screen now
    */
	public static int MouseX;
	public static int MouseY;

	
	//Was mouse clicked firing the weapon
	public static boolean wasFired = false;
	
	//Can take control of mouse position
	private Robot robot;
	
	public InputHandler()
	{
		try
		{
			robot = new Robot();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	@Override
   /**
    * Checks if keyCode is valid, and then sets that key to true if it
    * is. This means the key was clicked, and it sends this value to the
    * game class.
    * @param e
    */
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		
		if(keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = true;
		}
		
	}

	@Override
   /**
    * Checks if the keyCode of the key released is valid, then sets that
    * key to false if it is.
    * @param e
    */
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		
		if(keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) 
	{	
	}

	@Override
	public void focusGained(FocusEvent arg0) 
	{	
	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		for(int i = 0; i < key.length; i++)
		{
			key[i] = false;
		}
		
	}

	@Override
   /**
    * If mouse was clicked, then fire the weapon
    */
	public void mouseClicked(MouseEvent e) 
	{
		//Controller.shot = true;
		//wasFired = true;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{		
	}

	@Override
	/**
	 * Is called when the mouse exits the frame,
	 * and it uses a Robot objects to move the cursor
	 * back to a certain position on the screen.
	 */
	public void mouseExited(MouseEvent e) 
	{
	}

	@Override
	//If mouse pressed, it means you are shooting your weapon
	public void mousePressed(MouseEvent e) 
	{
		Controller.shot = true;	
		wasFired = true;
	}

	@Override
	//Weapons aren't being fired after you release
	public void mouseReleased(MouseEvent arg0) 
	{
		Controller.shot = false;
		wasFired = false;
	}

	@Override
   /**
    * Mouse is moving and being pressed so both are
    * true in this case
    */
	public void mouseDragged(MouseEvent e) 
	{
		Controller.shot = true;
		wasFired = true;
		
		//Just uses the move method already made
		mouseMoved(e);
	}

	@Override
	/**
	 * Checks if mouse was moved, If it was,
	 * get the new mouse x and y coordinates on the screen and determine
	 * how much to change where the players looking based on how fast 
	 * you moved the mouse and in what direction you turned in
	 */
	public void mouseMoved(MouseEvent e) 
	{
		Component c = RunGame.frame;

		int x = e.getX();
		int y = e.getY();
		int centerx = Display.WIDTH/2;
		int centery = Display.HEIGHT/2;
		//get the difference from the center to determine movement
		double turnAmountX = x - centerx;
		double turnAmountY = y - centery;
		//based on amount moved the 100 is the sensitivity
		Display.mouseSpeedHorizontal = Controller.rotationSpeed * turnAmountX;
		Display.mouseSpeedVertical = Controller.rotationSpeed * turnAmountY;
		//Always reset mouse to the center of the screen.
		int lx = c.getLocation().x;
		int ly = c.getLocation().y;
		robot.mouseMove(lx + centerx + 3, ly + centery + 26);
	}
}
