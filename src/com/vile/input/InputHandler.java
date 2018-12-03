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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.vile.Display;
import com.vile.RunGame;
import com.vile.entities.Player;
import com.vile.entities.Weapon;

/**
 * Title: InputHandler
 * 
 * @author Alex Byrd Date Updated: 5/02/2017
 * 
 *         Description: Deals with all the input devices such as the keyboard
 *         and mouse, using listeners. It then instantiates all the required
 *         listener methods and deals with events accordingly.
 * 
 *         The listeners are interfaces.
 *
 *         This is always updated to make the mouse less and less buggy. It is
 *         still even now a little twitchy, but it at least stays in the center
 *         of the screen.
 */
public class InputHandler
		implements KeyListener, MouseListener, MouseMotionListener, FocusListener, MouseWheelListener {
	// All the possible combinations on the keyboard
	public boolean[] key = new boolean[68836];

	/*
	 * Mouse position on screen now
	 */
	public static int MouseX;
	public static int MouseY;

	// Whether mouse has been set up yet or not
	private int setUp = 0;

	/*
	 * Old Mouse position on Screen
	 */
	public static int oldMouseX;
	public static int oldMouseY;

	// Was mouse clicked firing the weapon
	public static boolean wasFired = false;

	// Can take control of mouse position
	private Robot robot;

	@Override
	/**
	 * Checks if keyCode is valid, and then sets that key to true if it is. This
	 * means the key was clicked, and it sends this value to the game class.
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = true;
		}

	}

	@Override
	/**
	 * Checks if the keyCode of the key released is valid, then sets that key to
	 * false if it is.
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	// If mouse wheel moved, then switch weapons
	public void mouseWheelMoved(MouseWheelEvent e) {
		Weapon[] weapons = Player.weapons;
		int weaponPick = Player.weaponEquipped;

		if (e.getWheelRotation() == -1) {
			do {
				if (weaponPick - 1 < 0) {
					weaponPick = weapons.length - 1;
				} else {
					weaponPick -= 1;
				}
			} while (!weapons[weaponPick].canBeEquipped);
		} else {
			do {
				if (weaponPick + 1 > weapons.length - 1) {
					weaponPick = 0;
				} else {
					weaponPick++;
				}
			} while (!weapons[weaponPick].canBeEquipped);
		}

		/*
		 * To switch, current weapon must be done firing, and if not have it switch
		 * whenever it is done firing.
		 */
		if (Player.weapons[Player.weaponEquipped].weaponShootTime == 0
				&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
			Player.weaponEquipped = weaponPick;
		} else {
			if (weaponPick == 0) {
				Controller.switch0 = true;
				Controller.switch1 = false;
				Controller.switch2 = false;
				Controller.switch3 = false;
			} else if (weaponPick == 1) {
				Controller.switch1 = true;
				Controller.switch0 = false;
				Controller.switch2 = false;
				Controller.switch3 = false;
			} else if (weaponPick == 2) {
				Controller.switch2 = true;
				Controller.switch1 = false;
				Controller.switch0 = false;
				Controller.switch3 = false;
			} else if (weaponPick == 3) {
				Controller.switch3 = true;
				Controller.switch1 = false;
				Controller.switch2 = false;
				Controller.switch0 = false;
			}
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		for (int i = 0; i < key.length; i++) {
			key[i] = false;
		}

		// TODO Change for debug mode
		RunGame.frame.toFront();
		RunGame.frame.requestFocus();

	}

	@Override
	/**
	 * If mouse was clicked, then fire the weapon
	 */
	public void mouseClicked(MouseEvent e) {
		// Controller.shot = true;
		// wasFired = true;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	/**
	 * Is called when the mouse exits the frame, and it uses a Robot objects to move
	 * the cursor back to a certain position on the screen.
	 */
	public void mouseExited(MouseEvent e) {
		if (true) {
			return;
		}

		MouseX = e.getX();
		MouseY = e.getY();

		// Uses the frame for position reference.
		Component c = RunGame.frame;

		/*
		 * Finds frames position on screen for mouse reposistioning
		 */
		Rectangle bounds = c.getBounds();
		bounds.setLocation(c.getLocationOnScreen());

		try {
			robot = new Robot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (robot != null) {
			/*
			 * Basically sees which way mouse moved in the x direction, and will determine
			 * the speed you need to turn based on how far your mouse moved in one tick. It
			 * also determines which way to turn based on what side of the center x did you
			 * move the mouse.
			 */
			if (MouseX > oldMouseX) {
				Display.mouseSpeedHorizontal = -Math.abs(oldMouseX - MouseX);
				Controller.mouseRight = true;
			} else {
				Display.mouseSpeedHorizontal = Math.abs(oldMouseX - MouseX);
				Controller.mouseLeft = true;
			}

			/*
			 * Basically sees which way mouse moved in the y direction, and will determine
			 * the speed you need to turn based on how far your mouse moved in one tick. It
			 * also determines which way to turn based on what side of the center y did you
			 * move the mouse.
			 */
			// If mouse moved up the screen, look up in the game
			if (MouseY > oldMouseY) {
				Display.mouseSpeedVertical = -Math.abs(oldMouseY - MouseY);
				Controller.mouseUp = true;
			}
			// If mouse moved down screen, look down in game
			else {
				Display.mouseSpeedVertical = Math.abs(oldMouseY - MouseY);
				Controller.mouseDown = true;
			}

			// Always move mouse back to the center of the screen
			robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));

			// Reset the old mouse positions to the center of the frame
			oldMouseX = e.getX();
			oldMouseY = e.getY();
		}
	}

	@Override
	// If mouse pressed, it means you are shooting your weapon
	public void mousePressed(MouseEvent e) {
		Controller.shot = true;
		wasFired = true;
	}

	@Override
	// Weapons aren't being fired after you release
	public void mouseReleased(MouseEvent arg0) {
		Controller.shot = false;
		wasFired = false;
	}

	@Override
	/**
	 * Mouse is moving and being pressed so both are true in this case
	 */
	public void mouseDragged(MouseEvent e) {
		Controller.shot = true;
		wasFired = true;

		// Just uses the move method already made
		mouseMoved(e);
	}

	@Override
	/**
	 * Checks if mouse was moved, If it was, get the new mouse x and y coordinates
	 * on the screen and determine how much to change where the players looking
	 * based on how fast you moved the mouse and in what direction you turned in
	 */
	public void mouseMoved(MouseEvent e) {
		// Get mouses current position
		MouseX = e.getX();
		MouseY = e.getY();

		// System.out.println("MouseX: "+MouseX);
		// System.out.println("MouseY: "+MouseY);

		// Uses the frame for position reference.
		Component c = RunGame.frame;

		/*
		 * Finds frames position on screen for mouse reposistioning
		 */
		Rectangle bounds = c.getBounds();
		bounds.setLocation(c.getLocationOnScreen());

		try {
			robot = new Robot();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (robot != null) {
			/*
			 * Robot takes a tick or two to move the mouse longer distances so this allows
			 * it two ticks when the game is first set up to correct its position to the
			 * center, and sets the old mouse values to being in the center as well so that
			 * the game doesn't wig out your position from the start
			 */
			if (setUp < 2) {
				if (setUp == 0) {
					robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
				} else {
					oldMouseX = e.getX();
					oldMouseY = e.getY();
				}

				// Add to ticks it has taken to set up
				setUp++;

				return;
			}

			/*
			 * Basically sees which way mouse moved in the x direction, and will determine
			 * the speed you need to turn based on how far your mouse moved in one tick. It
			 * also determines which way to turn based on what side of the center x did you
			 * move the mouse.
			 */
			if (MouseX > oldMouseX) {
				Display.mouseSpeedHorizontal = -Math.abs(oldMouseX - MouseX);
				Controller.mouseRight = true;
			} else {
				Display.mouseSpeedHorizontal = Math.abs(oldMouseX - MouseX);
				Controller.mouseLeft = true;
			}

			/*
			 * Basically sees which way mouse moved in the y direction, and will determine
			 * the speed you need to turn based on how far your mouse moved in one tick. It
			 * also determines which way to turn based on what side of the center y did you
			 * move the mouse.
			 */
			if (MouseY > oldMouseY) {
				Display.mouseSpeedVertical = -Math.abs(oldMouseY - MouseY);
				Controller.mouseUp = true;
			} else {
				Display.mouseSpeedVertical = Math.abs(oldMouseY - MouseY);
				Controller.mouseDown = true;
			}

			// Always reset mouse to the center of the screen.
			robot.mouseMove(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));

			oldMouseX = e.getX();
			oldMouseY = e.getY();
		}
	}

	/**
	 * Gets location of component and of the mouse, and see if the mouse is within
	 * the bounds of the component. Then return whether the mouse is in it or not.
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isMouseWithinComponent(Component c) {
		// Get mouses point location on screen
		Point mousePos = MouseInfo.getPointerInfo().getLocation();

		// Get bounds of frame
		Rectangle bounds = c.getBounds();

		// Is Mouses point on screen within bounds?
		bounds.setLocation(c.getLocationOnScreen());

		// Return whether mouse is within frame
		return bounds.contains(mousePos);
	}
}
