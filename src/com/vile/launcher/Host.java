package com.vile.launcher;

/**
 * Title: Join Server
 * @author John Zickafoose
 * Date Updated: 11/24/2018
 * 
 * Description:
 * Creates a new menu, but with ID of 7 so that it creates the host
 * menu instead of the normal main menu.
 *
 */
public class Host extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

   /**
    * Instantiates the new menu
    */
	public Host()
	{
		super(7);
		setTitle("Host:");
		super.drawHostMenu();
		super.drawBackground();
	}
}
