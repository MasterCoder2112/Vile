package com.vile.launcher;

/**
 * Title: Multiplayer
 * @author John Zickafoose
 * Date Updated: 11/10/2018
 * 
 * Description:
 * Creates a new menu, but with ID of 5 so that it creates the multiplayer
 * menu instead of the normal main menu.
 *
 */
public class Multiplayer extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

   /**
    * Instantiates the new menu
    */
	public Multiplayer()
	{
		super(5);
		setTitle("Multiplayer:");
		super.drawMultiplayerMenu();
		super.drawBackground();
	}
}
