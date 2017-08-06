package com.vile.input;

import java.awt.Component;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Key> keyMap = new HashMap<>();
	
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

		keyMap.put("Forward", new Key(KeyEvent.VK_W));
		keyMap.put("Back", new Key(KeyEvent.VK_S));
		keyMap.put("Left", new Key(KeyEvent.VK_A));
		keyMap.put("Right", new Key(KeyEvent.VK_D));
		keyMap.put("Look Left", new Key(KeyEvent.VK_LEFT));
		keyMap.put("Look Right", new Key(KeyEvent.VK_RIGHT));
		keyMap.put("Pause", new Key(KeyEvent.VK_ESCAPE));
		keyMap.put("Run", new Key(KeyEvent.VK_SHIFT));
		keyMap.put("Crouch", new Key(KeyEvent.VK_C));
		keyMap.put("Jump", new Key(KeyEvent.VK_SPACE));
		keyMap.put("Show FPS", new Key(KeyEvent.VK_F));
		keyMap.put("Look Up", new Key(KeyEvent.VK_UP));
		keyMap.put("Look Down", new Key(KeyEvent.VK_DOWN));
		keyMap.put("Reload", new Key(KeyEvent.VK_R));
		keyMap.put("No Clip", new Key(KeyEvent.VK_N));
		keyMap.put("Super Speed", new Key(KeyEvent.VK_P));
		keyMap.put("Fly", new Key(KeyEvent.VK_O));
		keyMap.put("God Mode", new Key(KeyEvent.VK_G));
		keyMap.put("Shoot", new Key(KeyEvent.VK_V));
		keyMap.put("Restock", new Key(KeyEvent.VK_L));
		keyMap.put("Use", new Key(KeyEvent.VK_E));
		keyMap.put("Weapon 1", new Key(KeyEvent.VK_1));
		keyMap.put("Weapon 2", new Key(KeyEvent.VK_2));
		keyMap.put("Weapon 3", new Key(KeyEvent.VK_3));
		keyMap.put("Weapon 4", new Key(KeyEvent.VK_4));
		keyMap.put("Unlimited Ammo", new Key(KeyEvent.VK_K));
	}

	@Override
   /**
    * Checks if keyCode is valid, and then sets that keyMap to true if it
    * is. This means the keyMap was clicked, and it sends this value to the
    * game class.
    * @param e
    */
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		for(String current : keyMap.keySet())
		{
			Key key = keyMap.get(current);
			if(key.getKeyCode() == keyCode)
			{
				key.setPressed(true);
			}
		}
		
	}

	@Override
   /**
    * Checks if the keyCode of the keyMap released is valid, then sets that
    * keyMap to false if it is.
    * @param e
    */
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();

		for(String current : keyMap.keySet())
		{
			Key key = keyMap.get(current);
			if(key.getKeyCode() == keyCode)
			{
				key.setPressed(false);
			}
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
		for(String current : keyMap.keySet())
		{
			keyMap.get(current).setPressed(false);
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
		double x = c.getMousePosition().x;
		double y = c.getMousePosition().y;
		double centerx = Display.WIDTH/2;
		double centery = Display.HEIGHT/2;
		//get the difference from the center to determine movement
		double turnAmountX = x - centerx;
		double turnAmountY = y - centery;

		//based on amount moved
		Display.mouseSpeedHorizontal = Controller.rotationSpeed * turnAmountX;
		Display.mouseSpeedVertical = Controller.rotationSpeed * turnAmountY;

		//Always reset mouse to the center of the screen.
        int lx = c.getLocationOnScreen().x;
        int ly = c.getLocationOnScreen().y;
		robot.mouseMove((int) (lx + centerx), (int) (ly + centery));
	}
}
