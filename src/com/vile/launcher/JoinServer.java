package com.vile.launcher;

/**
 * Title: Join Server
 * @author John Zickafoose
 * Date Updated: 11/10/2018
 * 
 * Description:
 * Creates a new menu, but with ID of 6 so that it creates the multiplayer
 * menu instead of the normal main menu.
 *
 */
public class JoinServer extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

   /**
    * Instantiates the new menu
    */
	public JoinServer()
	{
		super(6);
		setTitle("Join Server:");
		super.drawJoinServerMenu();
		super.drawBackground();
	}
}
